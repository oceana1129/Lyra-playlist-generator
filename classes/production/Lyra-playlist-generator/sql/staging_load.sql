DROP TABLE IF EXISTS StagingSpotify;
CREATE TABLE StagingSpotify (
  `Artist(s)` TEXT,
  `song` VARCHAR(512),
  `text` LONGTEXT,
  `Length` VARCHAR(32),
  `emotion` VARCHAR(64),
  `Genre` TEXT,
  `Album` VARCHAR(512),
  `Release Date` VARCHAR(32),
  `Key` VARCHAR(64),
  `Tempo` VARCHAR(32),
  `Loudness (db)` VARCHAR(32),
  `Time signature` VARCHAR(32),
  `Explicit` VARCHAR(16),
  `Popularity` VARCHAR(16),
  `Energy` VARCHAR(16),
  `Danceability` VARCHAR(16),
  `Positiveness` VARCHAR(16),
  `Speechiness` VARCHAR(16),
  `Liveness` VARCHAR(16),
  `Acousticness` VARCHAR(16),
  `Instrumentalness` VARCHAR(16),
  `Good for Party` VARCHAR(16),
  `Good for Work/Study` VARCHAR(16),
  `Good for Relaxation/Meditation` VARCHAR(16),
  `Good for Exercise` VARCHAR(16),
  `Good for Running` VARCHAR(16),
  `Good for Yoga/Stretching` VARCHAR(16),
  `Good for Driving` VARCHAR(16),
  `Good for Social Gatherings` VARCHAR(16),
  `Good for Morning Routine` VARCHAR(16),
  `Similar Artist 1` VARCHAR(512),
  `Similar Song 1` VARCHAR(512),
  `Similarity Score 1` VARCHAR(32),
  `Similar Artist 2` VARCHAR(512),
  `Similar Song 2` VARCHAR(512),
  `Similarity Score 2` VARCHAR(32),
  `Similar Artist 3` VARCHAR(512),
  `Similar Song 3` VARCHAR(512),
  `Similarity Score 3` VARCHAR(32)
);

-- Load your CSV (adjust path; enable LOCAL if needed)
LOAD DATA LOCAL INFILE '/Users/jackychen/Desktop/spotify_dataset.csv'
INTO TABLE StagingSpotify
CHARACTER SET utf8mb4
FIELDS TERMINATED BY ',' OPTIONALLY ENCLOSED BY '"'
LINES TERMINATED BY '\n'
IGNORE 1 ROWS;
