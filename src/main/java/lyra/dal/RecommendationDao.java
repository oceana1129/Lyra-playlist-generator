package lyra.dal;

import lyra.model.Recommendation;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO for accessing Recommendation data in the database.
 */
public class RecommendationDao {
    /** Attributes */
    /** Represents the connection manager of the application */
    protected ConnectionManager connectionManager;
    /** Uses a singleton pattern for the base dao instance */
    private static RecommendationDao instance = null;

    /** Constructor of the RecommendationDao class */
    public RecommendationDao() {
        this.connectionManager = new ConnectionManager();
    }

    /** Returns single instance of RecommendationDao.
     * Uses singleton pattern to ensure only one instance exists.
     * @return the RecommendationDao instance */
    public static RecommendationDao getInstance() {
        if (instance == null) {
            instance = new RecommendationDao();
        }
        return instance;
    }

    /** Create method:
     * to create a new recommendation.
     * @return the created Recommendation object */
    public Recommendation create(Recommendation recommendation) throws SQLException {
        // insert statement
        String sql = "INSERT INTO Recommendation(songId, similarSongId, similarityScore) VALUES(?, ?, ?)";

        try (Connection connection = connectionManager.getConnection();
             PreparedStatement insertStatement =
                     connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            insertStatement.setInt(1, recommendation.getSongId());
            insertStatement.setInt(2, recommendation.getSimilarSongId());
            insertStatement.setDouble(3, recommendation.getSimilarityScore());
            insertStatement.executeUpdate();

            try (ResultSet results = insertStatement.getGeneratedKeys()) {
                if (results.next()) {
                    recommendation.setRecommendationId(results.getInt(1));
                }
            }
        }
        return recommendation;
    }

    /** Read method:
     * get a recommendation object from the database based on its id.
     * @param id the primary key of the recommendation
     * @return the Recommendation object or null if not found */
    public Recommendation getRecommendationById(int id) throws SQLException {
        String sql = "SELECT * FROM Recommendation WHERE recommendationId=?";

        try (Connection connection = connectionManager.getConnection();
             PreparedStatement selectStatement = connection.prepareStatement(sql)) {
            selectStatement.setInt(1, id);

            try (ResultSet results = selectStatement.executeQuery()) {
                if (results.next()) {
                    return new Recommendation(
                            results.getInt("recommendationId"),
                            results.getInt("songId"),
                            results.getInt("similarSongId"),
                            results.getDouble("similarityScore"));
                }
            }
        }
        return null;
    }

    /** Read method:
     * list all recommendations.
     * @return a List of all Recommendation objects */
    public List<Recommendation> getAllRecommendations() throws SQLException {
        List<Recommendation> list = new ArrayList<>();
        String sql = "SELECT * FROM Recommendation";

        try (Connection connection = connectionManager.getConnection();
             PreparedStatement selectStatement = connection.prepareStatement(sql);
             ResultSet results = selectStatement.executeQuery()) {
            while (results.next()) {
                list.add(new Recommendation(
                        results.getInt("recommendationId"),
                        results.getInt("songId"),
                        results.getInt("similarSongId"),
                        results.getDouble("similarityScore")));
            }
        }
        return list;
    }

    /** Update method:
     * update the similarity score of a recommendation.
     * @param recommendation the Recommendation object to update
     * @param newScore the new similarity score to set
     * @return the updated Recommendation */
    public Recommendation updateSimilarityScore(Recommendation recommendation, double newScore) throws SQLException {
        String sql = "UPDATE Recommendation SET similarityScore=? WHERE recommendationId=?";

        try (Connection connection = connectionManager.getConnection();
             PreparedStatement updateStatement = connection.prepareStatement(sql)) {
            updateStatement.setDouble(1, newScore);
            updateStatement.setInt(2, recommendation.getRecommendationId());
            updateStatement.executeUpdate();
            recommendation.setSimilarityScore(newScore);
        }
        return recommendation;
    }

    /** Delete method:
     * deletes a recommendation.
     * @param recommendation the Recommendation object to delete */
    public void delete(Recommendation recommendation) throws SQLException {
        String sql = "DELETE FROM Recommendation WHERE recommendationId=?";

        try (Connection connection = connectionManager.getConnection();
             PreparedStatement deleteStatement = connection.prepareStatement(sql)) {
            deleteStatement.setInt(1, recommendation.getRecommendationId());
            deleteStatement.executeUpdate();
        }
    }
}
