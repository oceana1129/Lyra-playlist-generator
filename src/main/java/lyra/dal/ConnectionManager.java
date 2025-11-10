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
 * In our example, we will create a DAO (data access object) java class to interact with
 * each MySQL table. The DAO java classes will use ConnectionManager to open and close
 * connections.
 *
 * Instructions:
 * 1. Install MySQL Community Server. During installation, you will need to set up a user,
 * password, and port. Keep track of these values.
 * 2. Download and install Connector/J: http://dev.mysql.com/downloads/connector/j/
 * 3. Add the Connector/J JAR to your buildpath. This allows your application to use the
 * Connector/J library. You can add the JAR using either of the following methods:
 *   A. When creating a new Java project, on the "Java Settings" page, go to the
 *   "Libraries" tab.
 *   Click on the "Add External JARs" button.
 *   Navigate to the Connector/J JAR. On Windows, this looks something like:
 *   C:\Program Files (x86)\MySQL\Connector.J 8.0\mysql-connector-java-8.0.16-bin.jar
 *   B. If you already have a Java project created, then go to your project properties.
 *   Click on the "Java Build Path" option.
 *   Click on the "Libraries" tab, click on the "Add External Jars" button, and
 *   navigate to the Connector/J JAR.
 * 4. Update the "private final" variables below.
 */
public class ConnectionManager {
    // All of the information used by the connection manager should be configured
    // in the file main/resources/config.properties
    // as an example
    //    db.user=root
    //    db.password=password1234
    //    db.host=localhost
    //    db.port=3306
    //    db.schema=SpotifyDB
    //    db.timezone=UTC

    /** Your database user. By default, this is root2 */
    private final String user;
    /** Your database password */
    private final String password;
    /** URI to your database server. If running on the same machine, then this is "localhost". */
    private final String hostName;
    /** Port to your database server. By default, this is 3307. */
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
            // so you must make your own local instance of the file
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
        Connection connection = null;
        try {
            Properties connectionProperties = new Properties();
            connectionProperties.put("user", this.user);
            connectionProperties.put("password", this.password);
            connectionProperties.put("serverTimezone", this.timezone);
            // Ensure the JDBC driver is loaded by retrieving the runtime Class descriptor.
            // Otherwise, Tomcat may have issues loading libraries in the proper order.
            // One alternative is calling this in the HttpServlet init() override.
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                throw new SQLException(e);
            }
            connection = DriverManager.getConnection(
                    // Note: for MySQL 8, add "&allowPublicKeyRetrieval=true" to the query string
                    "jdbc:mysql://" + this.hostName + ":" + this.port + "/" + this.schema + "?useSSL=false&allowPublicKeyRetrieval=true",
                    connectionProperties);
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
        return connection;
    }

    /** Close the connection to the database instance. */
    public void closeConnection(Connection connection) throws SQLException {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
            throw e;
        }
    }
}
