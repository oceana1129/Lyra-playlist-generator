package lyra.tools;

import lyra.dal.*;
import lyra.model.*;

import java.sql.Date;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Arrays;
import java.util.List;

public class Inserter {

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

    public static void main(String[] args) throws SQLException {
        EmotionDao emotionDao = EmotionDao.getInstance();
        ArtistDao artistDao = ArtistDao.getInstance();
        SongDao songDao = SongDao.getInstance();
        SongArtistDao songArtistDao = SongArtistDao.getInstance();

        // Base emotions (idempotent)
        Emotion happy = ensureEmotion(emotionDao, "happy");
        Emotion sad = ensureEmotion(emotionDao, "sad");
        Emotion angry = ensureEmotion(emotionDao, "angry");

        ///////////////////////////////
        // EMOTION CRUD
        ///////////////////////////////
        Emotion emotionExample = new Emotion("scared");
        emotionExample = emotionDao.create(emotionExample);

        Emotion emotionRead = emotionDao.getEmotionById(emotionExample.getEmotionId());
        System.out.println("Emotion Read: " + emotionRead.getName());

        Emotion emotionUpdate = emotionDao.updateEmotionName(emotionExample, "SCARED");
        System.out.println("Emotion Update: " + emotionUpdate.getName());

        List<Emotion> currentEmotions = emotionDao.getAllEmotions();
        System.out.println("Emotions Before Delete: " + Arrays.toString(currentEmotions.toArray()));
        emotionDao.delete(emotionExample);
        currentEmotions = emotionDao.getAllEmotions();
        System.out.println("Emotions After Delete:  " + Arrays.toString(currentEmotions.toArray()));

        ///////////////////////////////
        // ARTIST CRUD
        ///////////////////////////////
        System.out.println("=== Artist CRUD ===");
        Artist artist1 = ensureArtist(artistDao, "Ed Sheeran");
        Artist artist2 = ensureArtist(artistDao, "Billie Eilish");
        Artist artist3 = ensureArtist(artistDao, "Drake");

        Artist artistRead = artistDao.getArtistById(artist1.getArtistId());
        System.out.println("Artist Read: " + artistRead.getName());

        Artist artistUpdated = artistDao.updateArtistName(artist1, "ED SHEERAN");
        System.out.println("Artist Updated: " + artistUpdated.getName());

        List<Artist> currentArtists = artistDao.getAllArtists();
        System.out.println("Artists Before Delete: " + Arrays.toString(currentArtists.toArray()));
        // artistDao.delete(artist3);
        currentArtists = artistDao.getAllArtists();
        System.out.println("Artists After Delete:  " + Arrays.toString(currentArtists.toArray()));
        System.out.println();

        ///////////////////////////////
        // SONG CRUD
        ///////////////////////////////
        System.out.println("=== Song CRUD ===");
        Song song1 = new Song(
                "Shape of You",
                "Divide",
                Date.valueOf("2017-01-06"),
                240,
                happy.getEmotionId(),
                "C#",
                96.0,
                -3.5,
                "4/4",
                false
        );
        Song song2 = new Song(
                "Bad Guy",
                "When We All Fall Asleep",
                Date.valueOf("2019-03-29"),
                194,
                angry.getEmotionId(),
                "G",
                135.0,
                -4.2,
                "4/4",
                true
        );

        song1 = songDao.create(song1);
        song2 = songDao.create(song2);

        Song songRead = songDao.getSongById(song1.getSongId());
        System.out.println("Song Read: " + songRead.getTitle() + " (" + songRead.getAlbum() + ")");

        Song songUpdated = songDao.updateSongTitle(song1, "Shape Of You (Remastered)");
        System.out.println("Song Updated: " + songUpdated.getTitle());

        List<Song> currentSongs = songDao.getAllSongs();
        System.out.println("Songs Before Delete: " + Arrays.toString(currentSongs.toArray()));
        songDao.delete(song2);
        currentSongs = songDao.getAllSongs();
        System.out.println("Songs After Delete:  " + Arrays.toString(currentSongs.toArray()));
        System.out.println();

        ///////////////////////////////
        // SONG-ARTIST RELATION CRUD
        ///////////////////////////////
        System.out.println("=== SongArtist CRUD ===");
        SongArtist mapping1 = songArtistDao.create(
                new SongArtist(song1.getSongId(), artist1.getArtistId())
        );
        SongArtist mapping2 = songArtistDao.create(
                new SongArtist(song1.getSongId(), artist2.getArtistId())
        );

        List<SongArtist> bySong = songArtistDao.getBySongId(song1.getSongId());
        System.out.println("Artists for Song " + song1.getTitle() + ": " +
                Arrays.toString(bySong.toArray()));

        List<SongArtist> byArtist = songArtistDao.getByArtistId(artist1.getArtistId());
        System.out.println("Songs by Artist " + artist1.getName() + ": " +
                Arrays.toString(byArtist.toArray()));

        mapping2 = songArtistDao.updateArtistForSong(mapping2, artist3.getArtistId());
        System.out.println("Mapping Updated: " + mapping2);

        System.out.println("Mappings Before Delete: " + Arrays.toString(bySong.toArray()));
        songArtistDao.delete(mapping1);
        songArtistDao.delete(mapping2);
        List<SongArtist> remainingMappings = songArtistDao.getBySongId(song1.getSongId());
        System.out.println("Mappings After Delete:  " + Arrays.toString(remainingMappings.toArray()));
    }
}
