package mainpackage.resources;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class SQLConnection {
  private static final String DB = "jdbc:mysql://localhost/assignment_2";
  private static final String USER = "voldemort";
  private static final String PWD = "hewhomustnotbenamed";

  private static Properties properties;

  private static SQLConnection instance = null;

  private SQLConnection() {
    properties = new Properties();
    properties.setProperty("user", USER);
    properties.setProperty("password", PWD);
    properties.setProperty("useSSL", "false");
    properties.setProperty("autoReconnect", "true");
    try {
      Class.forName("com.mysql.jdbc.Driver");
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    }
  }

  public static SQLConnection instance() {
    if (instance == null)
      instance = new SQLConnection();
    return instance;
  }

  public Connection createConnection() {
    try {
      return DriverManager.getConnection(DB, properties);
    } catch (SQLException e) {
      e.printStackTrace();
      return null;
    }
  }

}