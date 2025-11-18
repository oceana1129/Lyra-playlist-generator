package lyra.tools;

import lyra.dal.*;
import lyra.model.*;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

public class SongFinder {
	public Song findMatchingSong(
            String[] emotions,
            String[] genres,
            int popularity,
            int energy,
            int danceability,
            int positivity,
            int instrumentalness
    ) throws SQLException {
		
		// create instances
        SongDao songDao = SongDao.getInstance();
        EmotionDao emotionDao = EmotionDao.getInstance();
        GenreDao genreDao = GenreDao.getInstance();
        SongGenreDao songGenreDao = SongGenreDao.getInstance();
        AudioFeaturesDao audioDao = AudioFeaturesDao.getInstance();

        // convert to lowercase for easy matching
        List<String> emotionList = Arrays.stream(emotions)
                .map(String::toLowerCase)
                .toList();

        List<String> genreList = Arrays.stream(genres)
                .map(String::toLowerCase)
                .toList();

        Song bestMatch = null;
        double bestScore = -1;

        // loop through all songs in db
        for (Song song : songDao.getAllSongs()) {
        	
        	// create a score which will indicate how strong the match is
            double score = 0.0;

            // Emotion Match
            Emotion e = emotionDao.getEmotionById(song.getEmotionId());
            if (e != null && emotionList.contains(e.getName().toLowerCase())) {
                score += 2.0;  // emotions are weighted highest
            }

            // Genre Match
            List<SongGenre> sgList = songGenreDao.getSongGenresBySongId(song.getSongId());
            boolean genreHit = false;
            for (SongGenre sg : sgList) {
                Genre g = genreDao.getGenreById(sg.getGenreId());
                if (g != null && genreList.contains(g.getName().toLowerCase())) {
                    genreHit = true;
                    break;
                }
            }
            if (genreHit) score += 1.5;

            // Audio Features Match
            AudioFeatures af = audioDao.getBySongId(song.getSongId());
            if (af != null) {
                score += 1.0 - (Math.abs(af.getPopularity()       - popularity)       / 100.0);
                score += 1.0 - (Math.abs(af.getEnergy()           - energy)           / 100.0);
                score += 1.0 - (Math.abs(af.getDanceability()     - danceability)     / 100.0);
                score += 1.0 - (Math.abs(af.getPositiveness()     - positivity)       / 100.0);
                score += 1.0 - (Math.abs(af.getInstrumentalness() - instrumentalness) / 100.0);
            }

            // sort the best scoring song
            if (score > bestScore) {
                bestScore = score;
                bestMatch = song;
            }
        }

        return bestMatch;
    }
}
