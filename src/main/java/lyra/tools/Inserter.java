package lyra.tools;

import lyra.dal.*;
import lyra.model.*;

import java.sql.Date;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Inserter {

    // ---------- Ensurers (idempotent creators) ----------
    private static Emotion ensureEmotion(EmotionDao emotionDao, String name) throws SQLException {
        try {
            return emotionDao.create(new Emotion(name));
        } catch (SQLIntegrityConstraintViolationException e) {
            for (Emotion e2 : emotionDao.getAllEmotions()) {
                if (e2.getName().equalsIgnoreCase(name)) {
                    return e2;
                }
            }
            throw e;
        }
    }

    private static Artist ensureArtist(ArtistDao artistDao, String name) throws SQLException {
        try {
            return artistDao.create(new Artist(name));
        } catch (SQLIntegrityConstraintViolationException e) {
            for (Artist a : artistDao.getAllArtists()) {
                if (a.getName().equalsIgnoreCase(name)) {
                    return a;
                }
            }
            throw e;
        }
    }

    private static Genre ensureGenre(GenreDao genreDao, String name) throws SQLException {
        try {
            return genreDao.create(new Genre(name));
        } catch (SQLIntegrityConstraintViolationException e) {
            for (Genre g : genreDao.getAllGenres()) {
                if (g.getName().equalsIgnoreCase(name)) {
                    return g;
                }
            }
            throw e;
        }
    }

    // ---------- Local helpers (scan-all emulation) ----------
    private static List<SongGenre> getSongGenresBySongId(SongGenreDao songGenreDao, int songId) throws SQLException {
        List<SongGenre> out = new ArrayList<>();
        for (SongGenre sg : songGenreDao.getAllSongGenres()) {
            if (sg.getSongId() == songId) out.add(sg);
        }
        return out;
    }

    private static List<SongGenre> getSongGenresByGenreId(SongGenreDao songGenreDao, int genreId) throws SQLException {
        List<SongGenre> out = new ArrayList<>();
        for (SongGenre sg : songGenreDao.getAllSongGenres()) {
            if (sg.getGenreId() == genreId) out.add(sg);
        }
        return out;
    }

    private static List<Recommendation> getRecommendationsBySongId(RecommendationDao recommendationDao, int songId) throws SQLException {
        List<Recommendation> out = new ArrayList<>();
        for (Recommendation r : recommendationDao.getAllRecommendations()) {
            if (r.getSongId() == songId) out.add(r);
        }
        return out;
    }

    public static void main(String[] args) throws SQLException {
        GenreDao genreDao = GenreDao.getInstance();
        SongGenreDao songGenreDao = SongGenreDao.getInstance();
        RecommendationDao recommendationDao = RecommendationDao.getInstance();
        EmotionDao emotionDao = EmotionDao.getInstance();
        ArtistDao artistDao = ArtistDao.getInstance();
        SongDao songDao = SongDao.getInstance();
        SongArtistDao songArtistDao = SongArtistDao.getInstance();

        // ---------- Baseline emotions (kept) ----------
        Emotion happy = ensureEmotion(emotionDao, "happy");
        Emotion sad   = ensureEmotion(emotionDao, "sad");
        Emotion angry = ensureEmotion(emotionDao, "angry");

        // ---------- Emotion CRUD (delete the temp only) ----------
        Emotion emotionTemp = emotionDao.create(new Emotion("scared"));
        System.out.println("Emotion Read: " + emotionDao.getEmotionById(emotionTemp.getEmotionId()).getName());
        emotionTemp = emotionDao.updateEmotionName(emotionTemp, "SCARED");
        System.out.println("Emotion Update: " + emotionTemp.getName());
        System.out.println("Emotions Before Delete: " + Arrays.toString(emotionDao.getAllEmotions().toArray()));
        emotionDao.delete(emotionTemp); // delete only the temp
        System.out.println("Emotions After Delete:  " + Arrays.toString(emotionDao.getAllEmotions().toArray()));
        System.out.println();

        // ---------- Artist CRUD (keep baselines, delete a temp) ----------
        System.out.println("=== Artist CRUD ===");
        Artist artist1 = ensureArtist(artistDao, "Ed Sheeran");
        Artist artist2 = ensureArtist(artistDao, "Billie Eilish");
        Artist artist3 = ensureArtist(artistDao, "Drake");

        System.out.println("Artist Read: " + artistDao.getArtistById(artist1.getArtistId()).getName());
        artist1 = artistDao.updateArtistName(artist1, "ED SHEERAN");
        System.out.println("Artist Updated: " + artist1.getName());

        // Temp artist for delete demo
        Artist artistTemp = artistDao.create(new Artist("Temp Artist"));
        System.out.println("Artists Before Delete: " + Arrays.toString(artistDao.getAllArtists().toArray()));
        artistDao.delete(artistTemp); // delete only the temp
        System.out.println("Artists After Delete:  " + Arrays.toString(artistDao.getAllArtists().toArray()));
        System.out.println();

        // ---------- Song CRUD (keep baselines, delete a temp) ----------
        System.out.println("=== Song CRUD ===");
        Song song1 = songDao.create(new Song(
                "Shape of You", "Divide", Date.valueOf("2017-01-06"),
                240, happy.getEmotionId(), "C#", 96.0, -3.5, "4/4", false
        ));
        Song song2 = songDao.create(new Song(
                "Bad Guy", "When We All Fall Asleep", Date.valueOf("2019-03-29"),
                194, angry.getEmotionId(), "G", 135.0, -4.2, "4/4", true
        ));

        System.out.println("Song Read: " + songDao.getSongById(song1.getSongId()).getTitle() + " (" + song1.getAlbum() + ")");
        song1 = songDao.updateSongTitle(song1, "Shape Of You (Remastered)");
        System.out.println("Song Updated: " + song1.getTitle());

        // Temp song for delete demo
        Song songTemp = songDao.create(new Song(
                "Temp Song", "Temp Album", Date.valueOf("2020-01-01"),
                123, sad.getEmotionId(), "A", 120.0, -6.0, "4/4", false
        ));

        System.out.println("Songs Before Delete: " + Arrays.toString(songDao.getAllSongs().toArray()));
        songDao.delete(songTemp); // delete only the temp
        System.out.println("Songs After  Keep:  " + Arrays.toString(songDao.getAllSongs().toArray()));
        System.out.println();

        // ---------- SongArtist CRUD (leave at least one mapping) ----------
        System.out.println("=== SongArtist CRUD ===");
        SongArtist sa1 = songArtistDao.create(new SongArtist(song1.getSongId(), artist1.getArtistId()));
        SongArtist sa2 = songArtistDao.create(new SongArtist(song1.getSongId(), artist2.getArtistId()));

        System.out.println("Artists for Song " + song1.getTitle() + ": " +
                Arrays.toString(songArtistDao.getBySongId(song1.getSongId()).toArray()));
        System.out.println("Songs by Artist " + artist1.getName() + ": " +
                Arrays.toString(songArtistDao.getByArtistId(artist1.getArtistId()).toArray()));

        sa2 = songArtistDao.updateArtistForSong(sa2, artist3.getArtistId());
        System.out.println("Mapping Updated: " + sa2);

        System.out.println("Mappings Before Delete: " +
                Arrays.toString(songArtistDao.getBySongId(song1.getSongId()).toArray()));
        // delete only the second mapping; keep the first so DB retains data
        songArtistDao.delete(sa2);
        System.out.println("Mappings After Delete:  " +
                Arrays.toString(songArtistDao.getBySongId(song1.getSongId()).toArray()));
        System.out.println();

        // ---------- Genre CRUD (keep all) ----------
        System.out.println("=== Genre CRUD ===");
        Genre pop   = ensureGenre(genreDao, "Pop");
        Genre hiphop= ensureGenre(genreDao, "Hip-Hop");
        Genre edm   = ensureGenre(genreDao, "EDM");

        System.out.println("Genre Read: " + genreDao.getGenreById(pop.getGenreId()));
        pop = genreDao.updateGenreName(pop, "POP");
        System.out.println("Genre Updated: " + pop);
        System.out.println("Genres: " + Arrays.toString(genreDao.getAllGenres().toArray()));
        System.out.println();

        // ---------- SongGenre CRUD (delete only one, keep one) ----------
        System.out.println("=== SongGenre CRUD ===");
        SongGenre sg1 = songGenreDao.create(new SongGenre(song1.getSongId(), pop.getGenreId()));
        SongGenre sg2 = songGenreDao.create(new SongGenre(song1.getSongId(), hiphop.getGenreId()));

        System.out.println("Genres for Song " + song1.getTitle() + ": " +
                Arrays.toString(getSongGenresBySongId(songGenreDao, song1.getSongId()).toArray()));
        System.out.println("Songs under " + hiphop.getName() + ": " +
                Arrays.toString(getSongGenresByGenreId(songGenreDao, hiphop.getGenreId()).toArray()));

        sg2 = songGenreDao.updateGenreId(sg2, edm.getGenreId());
        System.out.println("SongGenre Updated: " + sg2);

        System.out.println("SongGenres Before Delete: " +
                Arrays.toString(getSongGenresBySongId(songGenreDao, song1.getSongId()).toArray()));
        // delete only sg2; keep sg1 so DB retains at least one mapping
        songGenreDao.delete(sg2);
        System.out.println("SongGenres After  Delete: " +
                Arrays.toString(getSongGenresBySongId(songGenreDao, song1.getSongId()).toArray()));
        System.out.println();

        // ---------- Recommendation CRUD (keep r1, delete a temp r2) ----------
        System.out.println("=== Recommendation CRUD ===");
        Recommendation r1 = recommendationDao.create(new Recommendation(
                song1.getSongId(), song2.getSongId(), 0.92));
        System.out.println("Recommendation Read: " + recommendationDao.getRecommendationById(r1.getRecommendationId()));
        r1 = recommendationDao.updateSimilarityScore(r1, 0.95);
        System.out.println("Recommendation Updated: " + r1);
        System.out.println("Recommendations for Song: " +
                Arrays.toString(getRecommendationsBySongId(recommendationDao, song1.getSongId()).toArray()));

        // temp recommendation to demonstrate delete
        Recommendation r2 = recommendationDao.create(new Recommendation(
                song1.getSongId(), song2.getSongId(), 0.50));
        recommendationDao.delete(r2); // delete only the temp
        System.out.println("Recommendations After Delete (kept r1): " +
                Arrays.toString(getRecommendationsBySongId(recommendationDao, song1.getSongId()).toArray()));

        // Do NOT delete baseline songs/artists/genres here.
    }
}
