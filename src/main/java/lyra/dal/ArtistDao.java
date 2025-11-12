package lyra.dal;

import lyra.model.Artist;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO for accessing Artist data in the database.
 */
public class ArtistDao {
    /** Attributes */
    protected ConnectionManager connectionManager;
    private static ArtistDao instance = null;

    /** Constructor of the ArtistDao class */
    public ArtistDao() {
        this.connectionManager = new ConnectionManager();
    }

    /** Returns single instance of ArtistDao (singleton). */
    public static ArtistDao getInstance() {
        if (instance == null) {
            instance = new ArtistDao();
        }
        return instance;
    }

    /** Create method:
     *  to create a new artist.
     *  @return the created Artist object */
    public Artist create(Artist artist) throws SQLException {
        String sql = "INSERT INTO Artist(name) VALUES(?)";

        try (Connection connection = connectionManager.getConnection();
             PreparedStatement insertStatement =
                     connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            insertStatement.setString(1, artist.getName());
            insertStatement.executeUpdate();

            try (ResultSet results = insertStatement.getGeneratedKeys()) {
                if (results.next()) {
                    artist.setArtistId(results.getInt(1));
                }
            }
        }
        return artist;
    }

    /** Read method:
     * get an artist by id.
     * @param id the primary key of the artist
     * @return the Artist object or null if not found */
    public Artist getArtistById(int id) throws SQLException {
        String sql = "SELECT * FROM Artist WHERE artistId=?";

        try (Connection connection = connectionManager.getConnection();
             PreparedStatement selectStatement = connection.prepareStatement(sql)) {

            selectStatement.setInt(1, id);

            try (ResultSet results = selectStatement.executeQuery()) {
                if (results.next()) {
                    return new Artist(
                            results.getInt("artistId"),
                            results.getString("name")
                    );
                }
            }
        }
        return null;
    }

    /** Read method:
     * list all artists.
     * @return a List of all Artist objects */
    public List<Artist> getAllArtists() throws SQLException {
        List<Artist> artists = new ArrayList<>();
        String sql = "SELECT * FROM Artist";

        try (Connection connection = connectionManager.getConnection();
             PreparedStatement selectStatement = connection.prepareStatement(sql);
             ResultSet results = selectStatement.executeQuery()) {

            while (results.next()) {
                int id = results.getInt("artistId");
                String name = results.getString("name");
                artists.add(new Artist(id, name));
            }
        }
        return artists;
    }

    /** Update method:
     * update an artist's name.
     * @param artist the Artist object to update
     * @param newName the new name to set
     * @return the updated Artist object */
    public Artist updateArtistName(Artist artist, String newName) throws SQLException {
        String sql = "UPDATE Artist SET name=? WHERE artistId=?";

        try (Connection connection = connectionManager.getConnection();
             PreparedStatement updateStatement = connection.prepareStatement(sql)) {

            updateStatement.setString(1, newName);
            updateStatement.setInt(2, artist.getArtistId());
            updateStatement.executeUpdate();
            artist.setName(newName);
        }
        return artist;
    }

    /** Delete method:
     * deletes an artist.
     * @param artist the Artist object to delete */
    public void delete(Artist artist) throws SQLException {
        String sql = "DELETE FROM Artist WHERE artistId=?";

        try (Connection connection = connectionManager.getConnection();
             PreparedStatement deleteStatement = connection.prepareStatement(sql)) {

            deleteStatement.setInt(1, artist.getArtistId());
            deleteStatement.executeUpdate();
        }
    }
}
