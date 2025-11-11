package lyra.dal;

import lyra.model.Emotion;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO for accessing Emotion data in the database.
 */
public class EmotionDao {
    /** Attributes */
    /** Represents the connection manager of the application */
    protected ConnectionManager connectionManager;
    /** Uses a singleton pattern for the base dao instance */
    private static EmotionDao instance = null;

    /** Constructor of the EmotionDao class */
    public EmotionDao() {
        this.connectionManager = new ConnectionManager();
    }


    /** Returns single instance of EmotionDao.
     * Uses singleton pattern to ensure only one instance exists.
     *  @return the EmotionDao instance*/
    public static EmotionDao getInstance() {
        if (instance == null) {
            instance = new EmotionDao();
        }
        return instance;
    }

    /** Create method:
     *  to create a new emotion.
     *  @return the created Emotion object */
    public Emotion create(Emotion emotion) throws SQLException {
        // insert statement 
        String sql = "INSERT INTO Emotion(name) VALUES(?)";
        
        // connect to the connection manager
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement insertStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            insertStatement.setString(1, emotion.getName());
            insertStatement.executeUpdate();
            try (ResultSet results = insertStatement.getGeneratedKeys()) {
                if (results.next()) {
                    emotion.setEmotionId(results.getInt(1));
                }
            }
        }
        return emotion;
    }

    /** Read method:
     * get an emotion object from the database based on its id.
     * @param id the primary key of the emotion
     * @return the updated Emotion object */
    public Emotion getEmotionById(int id) throws SQLException {
        // SQL select statement to get emotion with its pk
        String sql = "SELECT * FROM Emotion WHERE emotionId=?";

        // ensure the connection and statement are automatically closed
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement selectStatement = connection.prepareStatement(sql)) {
            //set the id parameter in the statement
            selectStatement.setInt(1, id);

            // execute query and get results
            try (ResultSet results = selectStatement.executeQuery()) {
                // if a row is returned
                if (results.next()) {
                    // extract data and return the emotion as an object
                    return new Emotion(
                            results.getInt("emotionId"),
                            results.getString("name"));
                }
            }
        }
        // if no matching row, return null
        return null;
    }

    /** Read method:
     * list all emotions.
     * @return a List of all Emotion objects */
    public List<Emotion> getAllEmotions() throws SQLException {
        // create an array to store emotions
        List<Emotion> emotions = new ArrayList<>();
        // SQL select statement to get all emotions
        String sql = "SELECT * FROM Emotion";

        // ensure the connection and statement are automatically closed
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement selectStatement = connection.prepareStatement(sql);
             ResultSet results = selectStatement.executeQuery()) {
            while (results.next()) {
                int id = results.getInt("emotionId");
                String name = results.getString("name");
                emotions.add(new Emotion(id, name));
            }
        }
        return emotions;
    }

    /** Update method:
     * update the name of the emotion
     * @param emotion the Emotion object to update
     * @param newName the new name to set
     * @return the updated emotion */
    public Emotion updateEmotionName(Emotion emotion, String newName) throws SQLException {
        // SQL statement to update emotion
        String sql = "UPDATE Emotion SET name=? WHERE emotionId=?";

        // ensure the connection and statement are automatically closed
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement updateStatement = connection.prepareStatement(sql)) {
            updateStatement.setString(1, newName);
            updateStatement.setInt(2, emotion.getEmotionId());
            updateStatement.executeUpdate();
            emotion.setName(newName);
        }
        return emotion;
    }

    /** Delete method:
     * deletes an emotion.
     * @param emotion the Emotion object to delete */
    public void delete(Emotion emotion) throws SQLException {
        // SQL statement to delete emotion
        String sql = "DELETE FROM Emotion WHERE emotionId=?";

        // ensure the connection and statement are automatically closed
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement deleteStatement = connection.prepareStatement(sql)) {
            deleteStatement.setInt(1, emotion.getEmotionId());
            deleteStatement.executeUpdate();
        }
    }


}
