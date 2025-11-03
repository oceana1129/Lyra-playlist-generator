SET GLOBAL local_infile = 1;

-- Use your schema
CREATE SCHEMA IF NOT EXISTS SpotifyDB;
USE SpotifyDB;

-- Drop in reverse dependency order
DROP TABLE IF EXISTS Recommendation, Lyrics, ContextFilters, AudioFeatures,
                      SongGenre, SongArtist, Song, Genre, Emotion, Artist;

-- Core reference tables
CREATE TABLE Artist (
  artistId INT PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE Emotion (
  emotionId INT PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(100) NOT NULL UNIQUE
);

CREATE TABLE Genre (
  genreId INT PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(100) NOT NULL UNIQUE
);

-- Song & associations
CREATE TABLE Song (
  songId INT PRIMARY KEY AUTO_INCREMENT,
  title VARCHAR(255) NOT NULL,
  album VARCHAR(255),
  releaseDate DATE,
  lengthSeconds INT,
  emotionId INT,
  keySignature VARCHAR(50),
  tempo DECIMAL(8,3),
  loudness DECIMAL(8,3),
  timeSignature VARCHAR(20),
  explicit BOOLEAN,
  CONSTRAINT fk_song_emotion FOREIGN KEY (emotionId) REFERENCES Emotion(emotionId)
);

CREATE TABLE SongArtist (
  songId INT,
  artistId INT,
  PRIMARY KEY (songId, artistId),
  FOREIGN KEY (songId)  REFERENCES Song(songId),
  FOREIGN KEY (artistId) REFERENCES Artist(artistId)
);

CREATE TABLE SongGenre (
  songId INT,
  genreId INT,
  PRIMARY KEY (songId, genreId),
  FOREIGN KEY (songId)  REFERENCES Song(songId),
  FOREIGN KEY (genreId) REFERENCES Genre(genreId)
);

-- 1-to-1 Song extensions
CREATE TABLE AudioFeatures (
  songId INT PRIMARY KEY,
  popularity INT,
  energy INT,
  danceability INT,
  positiveness INT,
  speechiness INT,
  liveness INT,
  acousticness INT,
  instrumentalness INT,
  FOREIGN KEY (songId) REFERENCES Song(songId)
);

CREATE TABLE ContextFilters (
  songId INT PRIMARY KEY,
  party BOOLEAN,
  study BOOLEAN,
  relaxation BOOLEAN,
  exercise BOOLEAN,
  running BOOLEAN,
  yoga BOOLEAN,
  driving BOOLEAN,
  social BOOLEAN,
  morning BOOLEAN,
  FOREIGN KEY (songId) REFERENCES Song(songId)
);

CREATE TABLE Lyrics (
  songId INT PRIMARY KEY,
  content LONGTEXT,
  FOREIGN KEY (songId) REFERENCES Song(songId)
);

-- Recommendations (FK to Song on both sides)
CREATE TABLE Recommendation (
  recommendationId INT PRIMARY KEY AUTO_INCREMENT,
  songId INT NOT NULL,
  similarSongId INT NOT NULL,
  similarityScore DECIMAL(6,3),
  FOREIGN KEY (songId)      REFERENCES Song(songId),
  FOREIGN KEY (similarSongId) REFERENCES Song(songId)
);
