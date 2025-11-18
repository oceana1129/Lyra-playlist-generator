package lyra.dal;

import lyra.model.SongGenre;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO for accessing SongGenre data in the database.
 */
public class SongGenreDao {
    /** Attributes */
    /** Represents the connection manager of the application */
    protected ConnectionManager connectionManager;
    /** Uses a singleton pattern for the base dao instance */
    private static SongGenreDao instance = null;

    /** Constructor of the SongGenreDao class */
    public SongGenreDao() {
        this.connectionManager = new ConnectionManager();
    }

    /** Returns single instance of SongGenreDao.
     * Uses singleton pattern to ensure only one instance exists.
     * @return the SongGenreDao instance */
    public static SongGenreDao getInstance() {
        if (instance == null) {
            instance = new SongGenreDao();
        }
        return instance;
    }

    /** Create method:
     * to create a new song-genre mapping.
     * @return the created SongGenre object */
    public SongGenre create(SongGenre songGenre) throws SQLException {
        // insert statement
        String sql = "INSERT INTO SongGenre(songId, genreId) VALUES(?, ?)";

        try (Connection connection = connectionManager.getConnection();
             PreparedStatement insertStatement = connection.prepareStatement(sql)) {
            insertStatement.setInt(1, songGenre.getSongId());
            insertStatement.setInt(2, songGenre.getGenreId());
            insertStatement.executeUpdate();
        }
        return songGenre;
    }

    /** Read method:
     * get a SongGenre object from the database based on its composite key.
     * @param songId song primary key
     * @param genreId genre primary key
     * @return the SongGenre object or null if not found */
    public SongGenre getSongGenreByIds(int songId, int genreId) throws SQLException {
        String sql = "SELECT * FROM SongGenre WHERE songId=? AND genreId=?";

        try (Connection connection = connectionManager.getConnection();
             PreparedStatement selectStatement = connection.prepareStatement(sql)) {
            selectStatement.setInt(1, songId);
            selectStatement.setInt(2, genreId);

            try (ResultSet results = selectStatement.executeQuery()) {
                if (results.next()) {
                    return new SongGenre(
                            results.getInt("songId"),
                            results.getInt("genreId"));
                }
            }
        }
        return null;
    }

    public List<SongGenre> getSongGenresBySongId(int songId) throws SQLException {
        List<SongGenre> result = new ArrayList<>();

        String sql = "SELECT songId, genreId FROM SongGenre WHERE songId=?";

        try (Connection connection = connectionManager.getConnection();
             PreparedStatement selectStatement = connection.prepareStatement(sql)) {

            selectStatement.setInt(1, songId);

            try (ResultSet results = selectStatement.executeQuery()) {
                while (results.next()) {
                    result.add(new SongGenre(
                            results.getInt("songId"),
                            results.getInt("genreId")
                    ));
                }
            }
        }

        return result;
    }

    
    /** Read method:
     * list all song-genre mappings.
     * @return a List of all SongGenre objects */
    public List<SongGenre> getAllSongGenres() throws SQLException {
        List<SongGenre> list = new ArrayList<>();
        String sql = "SELECT * FROM SongGenre";

        try (Connection connection = connectionManager.getConnection();
             PreparedStatement selectStatement = connection.prepareStatement(sql);
             ResultSet results = selectStatement.executeQuery()) {
            while (results.next()) {
                list.add(new SongGenre(results.getInt("songId"), results.getInt("genreId")));
            }
        }
        return list;
    }

    /** Update method:
     * update the genre id for an existing (songId, genreId) mapping.
     * @param mapping the SongGenre object to update
     * @param newGenreId the new genre id to set
     * @return the updated SongGenre */
    public SongGenre updateGenreId(SongGenre mapping, int newGenreId) throws SQLException {
        // note: PK is (songId, genreId). This updates the genre side.
        String sql = "UPDATE SongGenre SET genreId=? WHERE songId=? AND genreId=?";

        try (Connection connection = connectionManager.getConnection();
             PreparedStatement updateStatement = connection.prepareStatement(sql)) {
            updateStatement.setInt(1, newGenreId);
            updateStatement.setInt(2, mapping.getSongId());
            updateStatement.setInt(3, mapping.getGenreId());
            updateStatement.executeUpdate();
            mapping.setGenreId(newGenreId);
        }
        return mapping;
    }

    /** Delete method:
     * deletes a song-genre mapping.
     * @param mapping the SongGenre object to delete */
    public void delete(SongGenre mapping) throws SQLException {
        String sql = "DELETE FROM SongGenre WHERE songId=? AND genreId=?";

        try (Connection connection = connectionManager.getConnection();
             PreparedStatement deleteStatement = connection.prepareStatement(sql)) {
            deleteStatement.setInt(1, mapping.getSongId());
            deleteStatement.setInt(2, mapping.getGenreId());
            deleteStatement.executeUpdate();
        }
    }
}
