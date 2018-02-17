package mainpackage.resources;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Controller {
  public static final String DB = "jdbc:mysql://localhost/assignment_2";
  public static final String USER = "voldemort";
  public static final String PASSWORD = "hewhomustnotbenamed";

  private static Controller instance = null;

  private Controller() {
  }

  public static Controller instance() {
    if (instance == null)
      instance = new Controller();
    return instance;
  }

  public Connection createConnection() {
    Connection conn = null;
    try {
      Class.forName("com.mysql.jdbc.Driver");
      conn = DriverManager.getConnection(DB, USER, PASSWORD);
      return conn;
    } catch (SQLException | ClassNotFoundException e) {
      e.printStackTrace();
      return null;
    }
  }

}