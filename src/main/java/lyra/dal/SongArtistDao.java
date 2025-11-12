package lyra.dal;

import lyra.model.SongArtist;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO for accessing SongArtist relationship data.
 */
public class SongArtistDao {
    /** Attributes */
    protected ConnectionManager connectionManager;
    private static SongArtistDao instance = null;

    /** Constructor of the SongArtistDao class */
    public SongArtistDao() {
        this.connectionManager = new ConnectionManager();
    }

    /** Returns single instance of SongArtistDao (singleton). */
    public static SongArtistDao getInstance() {
        if (instance == null) {
            instance = new SongArtistDao();
        }
        return instance;
    }

    /** Create method:
     *  create a new mapping between a song and an artist.
     *  @return the created SongArtist object */
    public SongArtist create(SongArtist songArtist) throws SQLException {
        String sql = "INSERT INTO SongArtist(songId, artistId) VALUES(?,?)";

        try (Connection connection = connectionManager.getConnection();
             PreparedStatement insertStatement = connection.prepareStatement(sql)) {

            insertStatement.setInt(1, songArtist.getSongId());
            insertStatement.setInt(2, songArtist.getArtistId());
            insertStatement.executeUpdate();
        }
        return songArtist;
    }

    /** Read method:
     * get all SongArtist mappings for a given songId.
     * @return a List of SongArtist objects */
    public List<SongArtist> getBySongId(int songId) throws SQLException {
        List<SongArtist> mappings = new ArrayList<>();
        String sql = "SELECT * FROM SongArtist WHERE songId=?";

        try (Connection connection = connectionManager.getConnection();
             PreparedStatement selectStatement = connection.prepareStatement(sql)) {

            selectStatement.setInt(1, songId);

            try (ResultSet results = selectStatement.executeQuery()) {
                while (results.next()) {
                    int sId = results.getInt("songId");
                    int aId = results.getInt("artistId");
                    mappings.add(new SongArtist(sId, aId));
                }
            }
        }
        return mappings;
    }

    /** Read method:
     * get all SongArtist mappings for a given artistId.
     * @return a List of SongArtist objects */
    public List<SongArtist> getByArtistId(int artistId) throws SQLException {
        List<SongArtist> mappings = new ArrayList<>();
        String sql = "SELECT * FROM SongArtist WHERE artistId=?";

        try (Connection connection = connectionManager.getConnection();
             PreparedStatement selectStatement = connection.prepareStatement(sql)) {

            selectStatement.setInt(1, artistId);

            try (ResultSet results = selectStatement.executeQuery()) {
                while (results.next()) {
                    int sId = results.getInt("songId");
                    int aId = results.getInt("artistId");
                    mappings.add(new SongArtist(sId, aId));
                }
            }
        }
        return mappings;
    }

    /** Update method:
     * change the artist for a given song-artist mapping.
     * @param mapping the SongArtist mapping to update
     * @param newArtistId the new artist id
     * @return the updated SongArtist object */
    public SongArtist updateArtistForSong(SongArtist mapping, int newArtistId) throws SQLException {
        String sql = "UPDATE SongArtist SET artistId=? WHERE songId=? AND artistId=?";

        try (Connection connection = connectionManager.getConnection();
             PreparedStatement updateStatement = connection.prepareStatement(sql)) {

            updateStatement.setInt(1, newArtistId);
            updateStatement.setInt(2, mapping.getSongId());
            updateStatement.setInt(3, mapping.getArtistId());
            updateStatement.executeUpdate();
            mapping.setArtistId(newArtistId);
        }
        return mapping;
    }

    /** Delete method:
     * deletes a song-artist mapping.
     * @param mapping the SongArtist object to delete */
    public void delete(SongArtist mapping) throws SQLException {
        String sql = "DELETE FROM SongArtist WHERE songId=? AND artistId=?";

        try (Connection connection = connectionManager.getConnection();
             PreparedStatement deleteStatement = connection.prepareStatement(sql)) {

            deleteStatement.setInt(1, mapping.getSongId());
            deleteStatement.setInt(2, mapping.getArtistId());
            deleteStatement.executeUpdate();
        }
    }
}
