package mainpackage.resources;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class Controller {
  private static final String DB = "jdbc:mysql://localhost/assignment_2";
  private static final String USER = "voldemort";
  private static final String PWD = "hewhomustnotbenamed";

  private static Properties properties;

  private static Controller instance = null;

  private Controller() {
    properties = new Properties();
    properties.setProperty("user", USER);
    properties.setProperty("password", PWD);
    properties.setProperty("useSSL", "false");
    properties.setProperty("autoReconnect", "true");
  }

  public static Controller instance() {
    if (instance == null)
      instance = new Controller();
    return instance;
  }

  public Connection createConnection() {
    try {
      Class.forName("com.mysql.jdbc.Driver");
      return DriverManager.getConnection(DB, properties);
    } catch (SQLException | ClassNotFoundException e) {
      e.printStackTrace();
      return null;
    }
  }

}