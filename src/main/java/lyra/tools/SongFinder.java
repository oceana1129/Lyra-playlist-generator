package lyra.tools;

import lyra.dal.*;
import lyra.model.*;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

public class SongFinder {

    private static final int CANDIDATE_SONG_LIMIT = 2000;

    public Song findMatchingSong(
            String[] emotions,
            String[] genres,
            int popularity,
            int energy,
            int danceability,
            int positivity,
            int instrumentalness
    ) throws SQLException {

        SongDao songDao = SongDao.getInstance();
        EmotionDao emotionDao = EmotionDao.getInstance();
        GenreDao genreDao = GenreDao.getInstance();
        SongGenreDao songGenreDao = SongGenreDao.getInstance();
        AudioFeaturesDao audioDao = AudioFeaturesDao.getInstance();

        if (emotions == null || emotions.length == 0 ||
                genres == null || genres.length == 0) {
            return null;
        }

        List<String> emotionList = Arrays.stream(emotions)
                .map(String::toLowerCase)
                .toList();

        List<String> genreList = Arrays.stream(genres)
                .map(String::toLowerCase)
                .toList();

        Song bestMatch = null;
        double bestScore = -1.0;

        // Use random subset of songs to avoid scanning the entire table
        for (Song song : songDao.getRandomSongs(CANDIDATE_SONG_LIMIT)) {

            double score = 0.0;

            // Emotion match (handle null emotionId safely)
            Integer emotionId = song.getEmotionId();
            Emotion e = null;
            if (emotionId != null) {
                e = emotionDao.getEmotionById(emotionId);
            }
            if (e != null && emotionList.contains(e.getName().toLowerCase())) {
                score += 2.0;
            }

            // Genre match
            List<SongGenre> sgList = songGenreDao.getSongGenresBySongId(song.getSongId());
            boolean genreHit = false;
            if (sgList != null) {
                for (SongGenre sg : sgList) {
                    Genre g = genreDao.getGenreById(sg.getGenreId());
                    if (g != null && genreList.contains(g.getName().toLowerCase())) {
                        genreHit = true;
                        break;
                    }
                }
            }
            if (genreHit) {
                score += 1.5;
            }

            // Audio features match
            AudioFeatures af = audioDao.getBySongId(song.getSongId());
            if (af != null) {
                score += 1.0 - (Math.abs(af.getPopularity() - popularity) / 100.0);
                score += 1.0 - (Math.abs(af.getEnergy() - energy) / 100.0);
                score += 1.0 - (Math.abs(af.getDanceability() - danceability) / 100.0);
                score += 1.0 - (Math.abs(af.getPositiveness() - positivity) / 100.0);
                score += 1.0 - (Math.abs(af.getInstrumentalness() - instrumentalness) / 100.0);
            }

            if (score > bestScore) {
                bestScore = score;
                bestMatch = song;
            }
        }

        return bestMatch;
    }
}
