package lyra.dal;

import lyra.model.Genre;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO for accessing Genre data in the database.
 */
public class GenreDao {
    /** Attributes */
    protected ConnectionManager connectionManager;
    private static GenreDao instance = null;

    /** Constructor */
    protected GenreDao() {
        this.connectionManager = new ConnectionManager();
    }

    /** Returns the singleton instance of GenreDao. */
    public static GenreDao getInstance() {
        if (instance == null) {
            instance = new GenreDao();
        }
        return instance;
    }

    /** Create a new genre record. */
    public Genre create(Genre genre) throws SQLException {
        String sql = "INSERT INTO Genre(name) VALUES(?)";

        try (Connection connection = connectionManager.getConnection();
             PreparedStatement insertStatement =
                     connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            insertStatement.setString(1, genre.getName());
            insertStatement.executeUpdate();

            try (ResultSet results = insertStatement.getGeneratedKeys()) {
                if (results.next()) {
                    genre.setGenreId(results.getInt(1));
                }
            }
        }
        return genre;
    }

    /** Get a genre by its ID. */
    public Genre getGenreById(int id) throws SQLException {
        String sql = "SELECT * FROM Genre WHERE genreId=?";

        try (Connection connection = connectionManager.getConnection();
             PreparedStatement selectStatement = connection.prepareStatement(sql)) {

            selectStatement.setInt(1, id);
            try (ResultSet results = selectStatement.executeQuery()) {
                if (results.next()) {
                    return new Genre(
                            results.getInt("genreId"),
                            results.getString("name")
                    );
                }
            }
        }
        return null;
    }

    /** List all genres. */
    public List<Genre> getAllGenres() throws SQLException {
        List<Genre> genres = new ArrayList<>();
        String sql = "SELECT * FROM Genre";

        try (Connection connection = connectionManager.getConnection();
             PreparedStatement selectStatement = connection.prepareStatement(sql);
             ResultSet results = selectStatement.executeQuery()) {

            while (results.next()) {
                int id = results.getInt("genreId");
                String name = results.getString("name");
                genres.add(new Genre(id, name));
            }
        }
        return genres;
    }

    /** Update a genre name. */
    public Genre updateGenreName(Genre genre, String newName) throws SQLException {
        String sql = "UPDATE Genre SET name=? WHERE genreId=?";

        try (Connection connection = connectionManager.getConnection();
             PreparedStatement updateStatement = connection.prepareStatement(sql)) {

            updateStatement.setString(1, newName);
            updateStatement.setInt(2, genre.getGenreId());
            updateStatement.executeUpdate();
            genre.setName(newName);
        }
        return genre;
    }

    /** Delete a genre record. */
    public void delete(Genre genre) throws SQLException {
        String sql = "DELETE FROM Genre WHERE genreId=?";

        try (Connection connection = connectionManager.getConnection();
             PreparedStatement deleteStatement = connection.prepareStatement(sql)) {

            deleteStatement.setInt(1, genre.getGenreId());
            deleteStatement.executeUpdate();
        }
    }

    public Genre getGenreByName(String name) throws SQLException {
        String sql = "SELECT GenreId, Name FROM Genre WHERE Name = ?;";
        try (Connection conn = connectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, name);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Genre(rs.getInt("GenreId"), rs.getString("Name"));
                }
            }
        }
        return null;
    }
}
