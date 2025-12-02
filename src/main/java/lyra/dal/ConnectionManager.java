package lyra.dal;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.io.InputStream;

/**
 * Use ConnectionManager to connect to your database instance.
 *
 * ConnectionManager uses the MySQL Connector/J driver to connect to your local MySQL instance.
 *
 * The DAO classes will use ConnectionManager to open and close connections.
 */
public class ConnectionManager {

    // Values are loaded from main/resources/config.properties, e.g.:
    // db.user=root
    // db.password=yourPassword
    // db.host=localhost
    // db.port=3306
    // db.schema=spotifydb
    // db.timezone=UTC

    /** Your database user. */
    private final String user;
    /** Your database password. */
    private final String password;
    /** URI to your database server. If running on the same machine, then this is "localhost". */
    private final String hostName;
    /** Port to your database server. */
    private final int port;
    /** Name of the MySQL schema that contains your tables. */
    private final String schema;
    /** Default timezone for MySQL server. */
    private final String timezone;

    /** Will set up the database configurations from main/resources/config.properties */
    public ConnectionManager() {
        try {
            // Load from classpath
            InputStream input = getClass()
                    .getClassLoader()
                    .getResourceAsStream("config.properties");

            // If the classpath does not exist, please build and add the information yourself
            // config.properties will be ignored by git
            if (input == null) {
                throw new RuntimeException("config.properties not found in classpath");
            }

            Properties props = new Properties();
            props.load(input);

            this.user = props.getProperty("db.user");
            this.password = props.getProperty("db.password");
            this.hostName = props.getProperty("db.host");
            this.port = Integer.parseInt(props.getProperty("db.port"));
            this.schema = props.getProperty("db.schema");
            this.timezone = props.getProperty("db.timezone");

        } catch (Exception e) {
            throw new RuntimeException("Failed to load DB configuration", e);
        }
    }

    /** Get the connection to the database instance. */
    public Connection getConnection() throws SQLException {
        Connection connection;
        try {
            Properties connectionProperties = new Properties();
            connectionProperties.put("user", this.user);
            connectionProperties.put("password", this.password);
            connectionProperties.put("serverTimezone", this.timezone);

            // Ensure the JDBC driver is loaded.
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                throw new SQLException(e);
            }

            // JDBC URL: add allowLoadLocalInfile=true so LOAD DATA LOCAL INFILE can work if needed.
            String url = "jdbc:mysql://" + this.hostName + ":" + this.port + "/" + this.schema
                    + "?useSSL=false&allowPublicKeyRetrieval=true&allowLoadLocalInfile=true";

            connection = DriverManager.getConnection(url, connectionProperties);
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
        return connection;
    }

    /** Close the connection to the database instance. */
    public void closeConnection(Connection connection) throws SQLException {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }
}
