package lyra.dal;

import lyra.model.Song;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO for accessing Song data in the database.
 */
public class SongDao {
    protected ConnectionManager connectionManager;
    private static SongDao instance = null;

    /** Default constructor. */
    public SongDao() {
        this.connectionManager = new ConnectionManager();
    }

    /** Returns single instance of SongDao (singleton). */
    public static SongDao getInstance() {
        if (instance == null) {
            instance = new SongDao();
        }
        return instance;
    }

    /** Create a new Song row. */
    public Song create(Song song) throws SQLException {
        String sql = "INSERT INTO Song(" +
                "title, album, releaseDate, lengthSeconds, emotionId, " +
                "keySignature, tempo, loudness, timeSignature, explicit" +
                ") VALUES(?,?,?,?,?,?,?,?,?,?)";

        try (Connection connection = connectionManager.getConnection();
             PreparedStatement insertStatement =
                     connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            insertStatement.setString(1, song.getTitle());
            insertStatement.setString(2, song.getAlbum());

            if (song.getReleaseDate() != null) {
                insertStatement.setDate(3, song.getReleaseDate());
            } else {
                insertStatement.setNull(3, Types.DATE);
            }

            insertStatement.setInt(4, song.getLengthSeconds());

            if (song.getEmotionId() != null) {
                insertStatement.setInt(5, song.getEmotionId());
            } else {
                insertStatement.setNull(5, Types.INTEGER);
            }

            insertStatement.setString(6, song.getKeySignature());

            if (song.getTempo() != null) {
                insertStatement.setDouble(7, song.getTempo());
            } else {
                insertStatement.setNull(7, Types.DOUBLE);
            }

            if (song.getLoudness() != null) {
                insertStatement.setDouble(8, song.getLoudness());
            } else {
                insertStatement.setNull(8, Types.DOUBLE);
            }

            insertStatement.setString(9, song.getTimeSignature());

            if (song.getExplicit() != null) {
                insertStatement.setBoolean(10, song.getExplicit());
            } else {
                insertStatement.setNull(10, Types.BOOLEAN);
            }

            insertStatement.executeUpdate();

            try (ResultSet results = insertStatement.getGeneratedKeys()) {
                if (results.next()) {
                    song.setSongId(results.getInt(1));
                }
            }
        }
        return song;
    }

    /** Get a song by primary key. */
    public Song getSongById(int id) throws SQLException {
        String sql = "SELECT * FROM Song WHERE songId=?";

        try (Connection connection = connectionManager.getConnection();
             PreparedStatement selectStatement = connection.prepareStatement(sql)) {

            selectStatement.setInt(1, id);

            try (ResultSet results = selectStatement.executeQuery()) {
                if (results.next()) {
                    return new Song(
                            results.getInt("songId"),
                            results.getString("title"),
                            results.getString("album"),
                            results.getDate("releaseDate"),
                            results.getInt("lengthSeconds"),
                            (Integer) results.getObject("emotionId"),
                            results.getString("keySignature"),
                            results.getObject("tempo") != null ? results.getDouble("tempo") : null,
                            results.getObject("loudness") != null ? results.getDouble("loudness") : null,
                            results.getString("timeSignature"),
                            results.getObject("explicit") != null ? results.getBoolean("explicit") : null
                    );
                }
            }
        }
        return null;
    }

    /** Get all songs (use with care on very large tables). */
    public List<Song> getAllSongs() throws SQLException {
        List<Song> songs = new ArrayList<>();
        String sql = "SELECT * FROM Song";

        try (Connection connection = connectionManager.getConnection();
             PreparedStatement selectStatement = connection.prepareStatement(sql);
             ResultSet results = selectStatement.executeQuery()) {

            while (results.next()) {
                Song song = new Song(
                        results.getInt("songId"),
                        results.getString("title"),
                        results.getString("album"),
                        results.getDate("releaseDate"),
                        results.getInt("lengthSeconds"),
                        (Integer) results.getObject("emotionId"),
                        results.getString("keySignature"),
                        results.getObject("tempo") != null ? results.getDouble("tempo") : null,
                        results.getObject("loudness") != null ? results.getDouble("loudness") : null,
                        results.getString("timeSignature"),
                        results.getObject("explicit") != null ? results.getBoolean("explicit") : null
                );
                songs.add(song);
            }
        }
        return songs;
    }

    /** Get at most `limit` songs. */
    public List<Song> getTopSongs(int limit) throws SQLException {
        List<Song> songs = new ArrayList<>();
        String sql = "SELECT * FROM Song LIMIT ?";

        try (Connection connection = connectionManager.getConnection();
             PreparedStatement selectStatement = connection.prepareStatement(sql)) {

            selectStatement.setInt(1, limit);

            try (ResultSet results = selectStatement.executeQuery()) {
                while (results.next()) {
                    Song song = new Song(
                            results.getInt("songId"),
                            results.getString("title"),
                            results.getString("album"),
                            results.getDate("releaseDate"),
                            results.getInt("lengthSeconds"),
                            (Integer) results.getObject("emotionId"),
                            results.getString("keySignature"),
                            results.getObject("tempo") != null ? results.getDouble("tempo") : null,
                            results.getObject("loudness") != null ? results.getDouble("loudness") : null,
                            results.getString("timeSignature"),
                            results.getObject("explicit") != null ? results.getBoolean("explicit") : null
                    );
                    songs.add(song);
                }
            }
        }
        return songs;
    }

    /** NEW — Get random songs. */
    public List<Song> getRandomSongs(int limit) throws SQLException {
        List<Song> songs = new ArrayList<>();
        String sql = "SELECT * FROM Song ORDER BY RAND() LIMIT ?";

        try (Connection connection = connectionManager.getConnection();
             PreparedStatement selectStatement = connection.prepareStatement(sql)) {

            selectStatement.setInt(1, limit);

            try (ResultSet results = selectStatement.executeQuery()) {
                while (results.next()) {
                    Song song = new Song(
                            results.getInt("songId"),
                            results.getString("title"),
                            results.getString("album"),
                            results.getDate("releaseDate"),
                            results.getInt("lengthSeconds"),
                            (Integer) results.getObject("emotionId"),
                            results.getString("keySignature"),
                            results.getObject("tempo") != null ? results.getDouble("tempo") : null,
                            results.getObject("loudness") != null ? results.getDouble("loudness") : null,
                            results.getString("timeSignature"),
                            results.getObject("explicit") != null ? results.getBoolean("explicit") : null
                    );
                    songs.add(song);
                }
            }
        }
        return songs;
    }

    /** Update song title. */
    public Song updateSongTitle(Song song, String newTitle) throws SQLException {
        String sql = "UPDATE Song SET title=? WHERE songId=?";

        try (Connection connection = connectionManager.getConnection();
             PreparedStatement updateStatement = connection.prepareStatement(sql)) {

            updateStatement.setString(1, newTitle);
            updateStatement.setInt(2, song.getSongId());
            updateStatement.executeUpdate();
            song.setTitle(newTitle);
        }
        return song;
    }

    /** Delete a song row. */
    public void delete(Song song) throws SQLException {
        String sql = "DELETE FROM Song WHERE songId=?";

        try (Connection connection = connectionManager.getConnection();
             PreparedStatement deleteStatement = connection.prepareStatement(sql)) {

            deleteStatement.setInt(1, song.getSongId());
            deleteStatement.executeUpdate();
        }
    }
}
