USE SpotifyDB;

-- ----------------------------------------------------------------------
-- Build a temporary numbers table once per session for splitting CSV lists
-- Generates 1..64; raise the upper bound if a row can have >64 tokens
-- ----------------------------------------------------------------------
DROP TEMPORARY TABLE IF EXISTS nums;
CREATE TEMPORARY TABLE nums (n INT PRIMARY KEY);
INSERT INTO nums (n)
SELECT ones.i + tens.i*10 + hundreds.i*100 + 1 AS n
FROM (
  SELECT 0 i UNION ALL SELECT 1 UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4
  UNION ALL SELECT 5 UNION ALL SELECT 6 UNION ALL SELECT 7 UNION ALL SELECT 8 UNION ALL SELECT 9
) AS ones
CROSS JOIN (
  SELECT 0 i UNION ALL SELECT 1 UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4
  UNION ALL SELECT 5 UNION ALL SELECT 6 UNION ALL SELECT 7 UNION ALL SELECT 8 UNION ALL SELECT 9
) AS tens
CROSS JOIN (
  SELECT 0 i UNION ALL SELECT 1 UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4
  UNION ALL SELECT 5 UNION ALL SELECT 6 UNION ALL SELECT 7 UNION ALL SELECT 8 UNION ALL SELECT 9
) AS hundreds
WHERE ones.i + tens.i*10 + hundreds.i*100 + 1 <= 64;

-- ----------------------------------------------------------------------
-- 1) Seed Emotion from staging
-- ----------------------------------------------------------------------
INSERT IGNORE INTO Emotion(name)
SELECT DISTINCT UPPER(TRIM(emotion))
FROM StagingSpotify
WHERE COALESCE(TRIM(emotion),'') <> '';

-- ----------------------------------------------------------------------
-- 2) Seed Artist without CTE (split comma-separated "Artist(s)")
-- ----------------------------------------------------------------------
INSERT IGNORE INTO Artist(name)
SELECT DISTINCT TRIM(SUBSTRING_INDEX(SUBSTRING_INDEX(s.`Artist(s)`, ',', nums.n), ',', -1)) AS artist_name
FROM StagingSpotify s
JOIN nums ON nums.n <= 1 + LENGTH(s.`Artist(s)`) - LENGTH(REPLACE(s.`Artist(s)`, ',', ''))
WHERE COALESCE(TRIM(s.`Artist(s)`),'') <> ''
  AND TRIM(SUBSTRING_INDEX(SUBSTRING_INDEX(s.`Artist(s)`, ',', nums.n), ',', -1)) <> '';

-- ----------------------------------------------------------------------
-- 3) Seed Genre without CTE (split comma-separated "Genre")
-- ----------------------------------------------------------------------
INSERT IGNORE INTO Genre(name)
SELECT DISTINCT TRIM(SUBSTRING_INDEX(SUBSTRING_INDEX(s.Genre, ',', nums.n), ',', -1)) AS gname
FROM StagingSpotify s
JOIN nums ON nums.n <= 1 + LENGTH(s.Genre) - LENGTH(REPLACE(s.Genre, ',', ''))
WHERE COALESCE(TRIM(s.Genre),'') <> ''
  AND TRIM(SUBSTRING_INDEX(SUBSTRING_INDEX(s.Genre, ',', nums.n), ',', -1)) <> '';

-- ----------------------------------------------------------------------
-- 4) Insert Songs (transform from staging, tolerant date parsing)
-- ----------------------------------------------------------------------
INSERT INTO Song (
  title, album, releaseDate, lengthSeconds, emotionId,
  keySignature, tempo, loudness, timeSignature, explicit
)
SELECT
  TRIM(src.song) AS title,
  NULLIF(TRIM(src.Album),'') AS album,
  CASE
    WHEN src.rdate REGEXP '^[0-9]{4}-[0-9]{2}-[0-9]{2}$' THEN STR_TO_DATE(src.rdate, '%Y-%m-%d')
    WHEN src.rdate REGEXP '^[0-9]{1,2}/[0-9]{1,2}/[0-9]{4}$' THEN STR_TO_DATE(src.rdate, '%m/%d/%Y')
    WHEN src.rdate REGEXP '^[0-9]{1,2} [A-Za-z]+ [0-9]{4}$' THEN STR_TO_DATE(src.rdate, '%e %M %Y')
    WHEN src.rdate REGEXP '^[A-Za-z]+ [0-9]{1,2} [0-9]{4}$' THEN STR_TO_DATE(src.rdate, '%M %e %Y')
    WHEN src.rdate REGEXP '^[A-Za-z]{3} [0-9]{1,2} [0-9]{4}$' THEN STR_TO_DATE(src.rdate, '%b %e %Y')
    WHEN src.rdate REGEXP '^[0-9]{4}$' THEN STR_TO_DATE(CONCAT(src.rdate,'-01-01'), '%Y-%m-%d')
    WHEN src.rdate REGEXP '^[A-Za-z]+[[:space:]]+[0-9]{4}$' THEN STR_TO_DATE(CONCAT('01 ', src.rdate), '%d %M %Y')
    ELSE NULL
  END AS releaseDate,
  CASE
    WHEN src.Length REGEXP '^[0-9]+:[0-9]{2}$' THEN TIME_TO_SEC(STR_TO_DATE(src.Length, '%i:%s'))
    WHEN src.Length REGEXP '^[0-9]{1,2}:[0-9]{2}:[0-9]{2}$' THEN TIME_TO_SEC(STR_TO_DATE(src.Length, '%H:%i:%s'))
    ELSE NULL
  END AS lengthSeconds,
  e.emotionId,
  LEFT(NULLIF(TRIM(src.Key),''), 50) AS keySignature,
  CASE
    WHEN TRIM(REPLACE(src.Tempo, ' bpm', '')) REGEXP '^-?[0-9]+(\\.[0-9]+)?$'
    THEN CAST(TRIM(REPLACE(src.Tempo, ' bpm', '')) AS DECIMAL(8,3))
    ELSE NULL
  END AS tempo,
  CASE
    WHEN TRIM(REPLACE(REPLACE(REPLACE(REPLACE(src.`Loudness (db)`, 'dB', ''), ' db', ''), 'DB', ''), '−', '-'))
         REGEXP '^-?[0-9]+(\\.[0-9]+)?$'
    THEN CAST(
      TRIM(REPLACE(REPLACE(REPLACE(REPLACE(src.`Loudness (db)`, 'dB', ''), ' db', ''), 'DB', ''), '−', '-'))
      AS DECIMAL(8,3)
    )
    ELSE NULL
  END AS loudness,
  LEFT(NULLIF(TRIM(src.`Time signature`),''), 10) AS timeSignature,
  CASE LOWER(TRIM(src.Explicit))
    WHEN 'yes' THEN TRUE WHEN 'true' THEN TRUE WHEN '1' THEN TRUE
    ELSE FALSE
  END AS explicit
FROM (
  SELECT s.*,
         TRIM(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(REPLACE(s.`Release Date`, ',', ''), 'st ', ' '),
           'nd ', ' '), 'rd ', ' '), 'th ', ' '), '  ', ' ')) AS rdate
  FROM StagingSpotify s
) AS src
LEFT JOIN Emotion e ON e.name = UPPER(TRIM(src.emotion));

-- ----------------------------------------------------------------------
-- 5) SongArtist link without CTE
-- ----------------------------------------------------------------------
-- Insert missing (songId, artistId) pairs into SongArtist
INSERT IGNORE INTO SongArtist(songId, artistId)
SELECT DISTINCT so.songId, ar.artistId
FROM (
  SELECT
    TRIM(s.`song`) AS title,
    NULLIF(TRIM(s.`Album`), '') AS album,
    -- Normalize full-width commas and extra spaces
    TRIM(
      REPLACE(
        REGEXP_REPLACE(REPLACE(s.`Artist(s)`, '，', ','), '\\s*,\\s*', ','),
      ',,', ',')
    ) AS artists
  FROM StagingSpotify s
  WHERE COALESCE(TRIM(s.`Artist(s)`), '') <> ''
) AS src
JOIN JSON_TABLE(
  CONCAT(
    '[',
      REPLACE(JSON_QUOTE(src.artists), ',', '","'),
    ']'
  ),
  '$[*]' COLUMNS(token VARCHAR(255) PATH '$')
) jt
JOIN Song   so ON so.title = src.title AND (so.album <=> src.album)
JOIN Artist ar ON ar.name  = TRIM(jt.token)
LEFT JOIN SongArtist sa
  ON sa.songId = so.songId AND sa.artistId = ar.artistId
WHERE TRIM(jt.token) <> ''
  AND sa.songId IS NULL;


-- ----------------------------------------------------------------------
-- 6) SongGenre link without CTE
-- ----------------------------------------------------------------------
INSERT IGNORE INTO SongGenre(songId, genreId)
SELECT so.songId, g.genreId
FROM (
  SELECT TRIM(s.song) AS title, NULLIF(TRIM(s.Album),'') AS album,
         TRIM(REPLACE(REPLACE(s.Genre, '，', ','), ', ', ',')) AS genres
  FROM StagingSpotify s
  WHERE COALESCE(TRIM(s.Genre),'') <> ''
) AS src
JOIN JSON_TABLE(CONCAT('[', REPLACE(JSON_QUOTE(src.genres), ',', '","'), ']'),
                '$[*]' COLUMNS(token VARCHAR(255) PATH '$')) jt
JOIN Song so ON so.title = src.title AND (so.album <=> src.album)
JOIN Genre g ON g.name = TRIM(jt.token)
WHERE TRIM(jt.token) <> '';

-- ----------------------------------------------------------------------
-- 7) AudioFeatures (1:1)
-- ----------------------------------------------------------------------
INSERT IGNORE INTO AudioFeatures (
  songId, popularity, energy, danceability, positiveness,
  speechiness, liveness, acousticness, instrumentalness
)
SELECT
  so.songId,
  CAST(NULLIF(TRIM(s.Popularity), '') AS SIGNED),
  CAST(NULLIF(TRIM(s.Energy), '') AS SIGNED),
  CAST(NULLIF(TRIM(s.Danceability), '') AS SIGNED),
  CAST(NULLIF(TRIM(s.Positiveness), '') AS SIGNED),
  CAST(NULLIF(TRIM(s.Speechiness), '') AS SIGNED),
  CAST(NULLIF(TRIM(s.Liveness), '') AS SIGNED),
  CAST(NULLIF(TRIM(s.Acousticness), '') AS SIGNED),
  CAST(NULLIF(TRIM(s.Instrumentalness), '') AS SIGNED)
FROM StagingSpotify s
JOIN Song so ON so.title = TRIM(s.song) AND (so.album <=> NULLIF(TRIM(s.Album), ''));

-- ----------------------------------------------------------------------
-- 8) ContextFilters (1:1)
-- ----------------------------------------------------------------------
INSERT IGNORE INTO ContextFilters (
  songId, party, study, relaxation, exercise,
  running, yoga, driving, social, morning
)
SELECT
  so.songId,
  CASE LOWER(TRIM(s.`Good for Party`)) WHEN 'yes' THEN TRUE WHEN 'true' THEN TRUE WHEN '1' THEN TRUE ELSE FALSE END,
  CASE LOWER(TRIM(s.`Good for Work/Study`)) WHEN 'yes' THEN TRUE WHEN 'true' THEN TRUE WHEN '1' THEN TRUE ELSE FALSE END,
  CASE LOWER(TRIM(s.`Good for Relaxation/Meditation`)) WHEN 'yes' THEN TRUE WHEN 'true' THEN TRUE WHEN '1' THEN TRUE ELSE FALSE END,
  CASE LOWER(TRIM(s.`Good for Exercise`)) WHEN 'yes' THEN TRUE WHEN 'true' THEN TRUE WHEN '1' THEN TRUE ELSE FALSE END,
  CASE LOWER(TRIM(s.`Good for Running`)) WHEN 'yes' THEN TRUE WHEN 'true' THEN TRUE WHEN '1' THEN TRUE ELSE FALSE END,
  CASE LOWER(TRIM(s.`Good for Yoga/Stretching`)) WHEN 'yes' THEN TRUE WHEN 'true' THEN TRUE WHEN '1' THEN TRUE ELSE FALSE END,
  CASE LOWER(TRIM(s.`Good for Driving`)) WHEN 'yes' THEN TRUE WHEN 'true' THEN TRUE WHEN '1' THEN TRUE ELSE FALSE END,
  CASE LOWER(TRIM(s.`Good for Social Gatherings`)) WHEN 'yes' THEN TRUE WHEN 'true' THEN TRUE WHEN '1' THEN TRUE ELSE FALSE END,
  CASE LOWER(TRIM(s.`Good for Morning Routine`)) WHEN 'yes' THEN TRUE WHEN 'true' THEN TRUE WHEN '1' THEN TRUE ELSE FALSE END
FROM StagingSpotify s
JOIN Song so ON so.title = TRIM(s.song) AND (so.album <=> NULLIF(TRIM(s.Album), ''));

/* =========================
   9) Lyrics (1:1)
   ========================= */

-- Step 1: Add _rid column (if not exists)
SET @col_exists := (
  SELECT COUNT(*)
  FROM INFORMATION_SCHEMA.COLUMNS
  WHERE TABLE_SCHEMA = DATABASE()
    AND TABLE_NAME = 'StagingSpotify'
    AND COLUMN_NAME = '_rid'
);

SET @has_pk := (
  SELECT COUNT(*)
  FROM INFORMATION_SCHEMA.TABLE_CONSTRAINTS
  WHERE TABLE_SCHEMA = DATABASE()
    AND TABLE_NAME = 'StagingSpotify'
    AND CONSTRAINT_TYPE = 'PRIMARY KEY'
);

SET @sql := CASE
  WHEN @col_exists = 0 AND @has_pk = 0 THEN
    'ALTER TABLE StagingSpotify ADD COLUMN _rid BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY FIRST'
  WHEN @col_exists = 0 AND @has_pk > 0 THEN
    'ALTER TABLE StagingSpotify ADD COLUMN _rid BIGINT NOT NULL AUTO_INCREMENT FIRST, ADD UNIQUE KEY ux_staging__rid (_rid)'
  ELSE
    'SELECT 1'
END;

PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- Step 2: Create indexes if not exist
-- Song(title, album)
SET @idx1 := (
  SELECT COUNT(*)
  FROM INFORMATION_SCHEMA.STATISTICS
  WHERE TABLE_SCHEMA = DATABASE()
    AND TABLE_NAME = 'Song'
    AND INDEX_NAME = 'idx_song_title_album'
);
SET @sql := IF(@idx1 = 0, 'CREATE INDEX idx_song_title_album ON Song(title, album)', 'SELECT 1');
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- StagingSpotify(song, Album)
SET @idx2 := (
  SELECT COUNT(*)
  FROM INFORMATION_SCHEMA.STATISTICS
  WHERE TABLE_SCHEMA = DATABASE()
    AND TABLE_NAME = 'StagingSpotify'
    AND INDEX_NAME = 'idx_staging_song_album'
);
SET @sql := IF(@idx2 = 0, 'CREATE INDEX idx_staging_song_album ON StagingSpotify(song(191), Album(191))', 'SELECT 1');
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- Step 3: Create stored procedure for batch insert
DELIMITER $$
DROP PROCEDURE IF EXISTS load_lyrics_in_chunks $$
CREATE PROCEDURE load_lyrics_in_chunks(
  IN p_batch_size INT,   -- e.g. 50000
  IN p_max_chars INT     -- e.g. 100000, 0 = no truncation
)
BEGIN
  DECLARE v_min BIGINT;
  DECLARE v_max BIGINT;
  DECLARE v_start BIGINT;
  DECLARE v_end BIGINT;

  SELECT MIN(_rid), MAX(_rid)
  INTO v_min, v_max
  FROM StagingSpotify
  WHERE COALESCE(TRIM(text),'') <> '';

  IF v_min IS NOT NULL AND v_max IS NOT NULL THEN
    SET v_start = v_min;
    WHILE v_start <= v_max DO
      SET v_end = v_start + p_batch_size - 1;

      IF p_max_chars > 0 THEN
        INSERT IGNORE INTO Lyrics (songId, content)
        SELECT so.songId, LEFT(s.text, p_max_chars)
        FROM StagingSpotify s
        JOIN Song so ON so.title = TRIM(s.song)
                     AND (so.album <=> NULLIF(TRIM(s.Album),''))
        WHERE s._rid BETWEEN v_start AND v_end
          AND COALESCE(TRIM(s.text),'') <> '';
      ELSE
        INSERT IGNORE INTO Lyrics (songId, content)
        SELECT so.songId, s.text
        FROM StagingSpotify s
        JOIN Song so ON so.title = TRIM(s.song)
                     AND (so.album <=> NULLIF(TRIM(s.Album),''))
        WHERE s._rid BETWEEN v_start AND v_end
          AND COALESCE(TRIM(s.text),'') <> '';
      END IF;

      SET v_start = v_end + 1;
    END WHILE;
  END IF;
END $$
DELIMITER ;

-- Step 4: Run batch insert
CALL load_lyrics_in_chunks(50000, 100000);

-- Step 5: Validate Lyrics insert result
SELECT COUNT(*) AS total_rows_in_lyrics FROM Lyrics;


/* =========================
   10) Recommendation (split by batch)
   ========================= */

-- Settings (smaller batch to avoid timeout)
SET @lim := 5000;
SET @min := (
  SELECT MIN(_rid)
  FROM StagingSpotify
  WHERE COALESCE(TRIM(`Similar Song 1`),'') <> ''
     OR COALESCE(TRIM(`Similar Song 2`),'') <> ''
     OR COALESCE(TRIM(`Similar Song 3`),'') <> ''
);
SET @max := (
  SELECT MAX(_rid)
  FROM StagingSpotify
  WHERE COALESCE(TRIM(`Similar Song 1`),'') <> ''
     OR COALESCE(TRIM(`Similar Song 2`),'') <> ''
     OR COALESCE(TRIM(`Similar Song 3`),'') <> ''
);

-- ====== BATCH 0 ======
-- 0a) Stub similar songs (create missing titles)
INSERT INTO Song (title)
SELECT DISTINCT t.sim
FROM (
  SELECT TRIM(s.`Similar Song 1`) AS sim
  FROM StagingSpotify s
  WHERE s._rid BETWEEN @min + @lim*0 AND LEAST(@min + @lim*1 - 1, @max)
    AND COALESCE(TRIM(s.`Similar Song 1`),'') <> ''
  UNION
  SELECT TRIM(s.`Similar Song 2`)
  FROM StagingSpotify s
  WHERE s._rid BETWEEN @min + @lim*0 AND LEAST(@min + @lim*1 - 1, @max)
    AND COALESCE(TRIM(s.`Similar Song 2`),'') <> ''
  UNION
  SELECT TRIM(s.`Similar Song 3`)
  FROM StagingSpotify s
  WHERE s._rid BETWEEN @min + @lim*0 AND LEAST(@min + @lim*1 - 1, @max)
    AND COALESCE(TRIM(s.`Similar Song 3`),'') <> ''
) t
LEFT JOIN Song ss ON ss.title = t.sim
WHERE t.sim <> '' AND ss.songId IS NULL;

-- 0b) Insert recommendations (no duplicates, numeric-safe)
INSERT INTO Recommendation (songId, similarSongId, similarityScore)
SELECT
  base.songId,
  sim.songId,
  CASE WHEN r.sc REGEXP '^-?[0-9]+(\\.[0-9]+)?$'
       THEN CAST(r.sc AS DECIMAL(6,3))
       ELSE NULL END
FROM (
  SELECT TRIM(s.song) AS baseSong,
         NULLIF(TRIM(s.Album),'') AS baseAlbum,
         TRIM(s.`Similar Song 1`) AS sim,
         NULLIF(TRIM(s.`Similarity Score 1`),'') AS sc
  FROM StagingSpotify s
  WHERE s._rid BETWEEN @min + @lim*0 AND LEAST(@min + @lim*1 - 1, @max)
    AND COALESCE(TRIM(s.`Similar Song 1`),'') <> ''
  UNION ALL
  SELECT TRIM(s.song), NULLIF(TRIM(s.Album),''), TRIM(s.`Similar Song 2`), NULLIF(TRIM(s.`Similarity Score 2`),'')
  FROM StagingSpotify s
  WHERE s._rid BETWEEN @min + @lim*0 AND LEAST(@min + @lim*1 - 1, @max)
    AND COALESCE(TRIM(s.`Similar Song 2`),'') <> ''
  UNION ALL
  SELECT TRIM(s.song), NULLIF(TRIM(s.Album),''), TRIM(s.`Similar Song 3`), NULLIF(TRIM(s.`Similarity Score 3`),'')
  FROM StagingSpotify s
  WHERE s._rid BETWEEN @min + @lim*0 AND LEAST(@min + @lim*1 - 1, @max)
    AND COALESCE(TRIM(s.`Similar Song 3`),'') <> ''
) r
JOIN Song base ON base.title = r.baseSong AND (base.album <=> r.baseAlbum)
JOIN Song sim ON sim.title = r.sim
LEFT JOIN Recommendation dedup
  ON dedup.songId = base.songId AND dedup.similarSongId = sim.songId
WHERE r.sim <> '' AND base.songId <> sim.songId AND dedup.songId IS NULL;

-- ====== BATCH 1 ======
INSERT INTO Song (title)
SELECT DISTINCT t.sim
FROM (
  SELECT TRIM(s.`Similar Song 1`) AS sim
  FROM StagingSpotify s
  WHERE s._rid BETWEEN @min + @lim*1 AND LEAST(@min + @lim*2 - 1, @max)
    AND COALESCE(TRIM(s.`Similar Song 1`),'') <> ''
  UNION
  SELECT TRIM(s.`Similar Song 2`)
  FROM StagingSpotify s
  WHERE s._rid BETWEEN @min + @lim*1 AND LEAST(@min + @lim*2 - 1, @max)
    AND COALESCE(TRIM(s.`Similar Song 2`),'') <> ''
  UNION
  SELECT TRIM(s.`Similar Song 3`)
  FROM StagingSpotify s
  WHERE s._rid BETWEEN @min + @lim*1 AND LEAST(@min + @lim*2 - 1, @max)
    AND COALESCE(TRIM(s.`Similar Song 3`),'') <> ''
) t
LEFT JOIN Song ss ON ss.title = t.sim
WHERE t.sim <> '' AND ss.songId IS NULL;

INSERT INTO Recommendation (songId, similarSongId, similarityScore)
SELECT
  base.songId,
  sim.songId,
  CASE WHEN r.sc REGEXP '^-?[0-9]+(\\.[0-9]+)?$'
       THEN CAST(r.sc AS DECIMAL(6,3))
       ELSE NULL END
FROM (
  SELECT TRIM(s.song) AS baseSong,
         NULLIF(TRIM(s.Album),'') AS baseAlbum,
         TRIM(s.`Similar Song 1`) AS sim,
         NULLIF(TRIM(s.`Similarity Score 1`),'') AS sc
  FROM StagingSpotify s
  WHERE s._rid BETWEEN @min + @lim*1 AND LEAST(@min + @lim*2 - 1, @max)
    AND COALESCE(TRIM(s.`Similar Song 1`),'') <> ''
  UNION ALL
  SELECT TRIM(s.song), NULLIF(TRIM(s.Album),''), TRIM(s.`Similar Song 2`), NULLIF(TRIM(s.`Similarity Score 2`),'')
  FROM StagingSpotify s
  WHERE s._rid BETWEEN @min + @lim*1 AND LEAST(@min + @lim*2 - 1, @max)
    AND COALESCE(TRIM(s.`Similar Song 2`),'') <> ''
  UNION ALL
  SELECT TRIM(s.song), NULLIF(TRIM(s.Album),''), TRIM(s.`Similar Song 3`), NULLIF(TRIM(s.`Similarity Score 3`),'')
  FROM StagingSpotify s
  WHERE s._rid BETWEEN @min + @lim*1 AND LEAST(@min + @lim*2 - 1, @max)
    AND COALESCE(TRIM(s.`Similar Song 3`),'') <> ''
) r
JOIN Song base ON base.title = r.baseSong AND (base.album <=> r.baseAlbum)
JOIN Song sim ON sim.title = r.sim
LEFT JOIN Recommendation dedup
  ON dedup.songId = base.songId AND dedup.similarSongId = sim.songId
WHERE r.sim <> '' AND base.songId <> sim.songId AND dedup.songId IS NULL;

-- ====== Continue batches (2..9) similarly if data is large ======

-- Quick validation
SELECT COUNT(*) AS total_recommendations FROM Recommendation;