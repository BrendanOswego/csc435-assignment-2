package mainpackage.api;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import mainpackage.api.models.AuthorModel;
import mainpackage.api.models.BookAuthorModel;
import mainpackage.api.models.BookModel;
import mainpackage.resources.Controller;
import mainpackage.resources.Helper;

public class API {

  public static enum status {
    ADDED, UPDATED, REMOVED, RETRIEVED, EXCEPTION, NOT_ADDED
  }

  private static API instance = null;

  private API() {
  }

  public static API instance() {
    if (instance == null)
      instance = new API();
    return instance;
  }

  public status addBookToAuthor(AuthorModel author, BookModel book) {
    BookAuthorModel ba = new BookAuthorModel(book.getID(), author.getID());
    Connection conn = null;
    Statement statement = null;
    try {
      conn = Controller.instance().createConnection();
      statement = conn.createStatement();

      if (statement.executeUpdate("INSERT INTO book VALUES " + Helper.asValue(book.asValue())) != 0
          && statement.executeUpdate("INSERT INTO book_author VALUES " + Helper.asValue(ba.asValue())) != 0)
        return status.ADDED;

      return status.NOT_ADDED;
    } catch (SQLException e) {
      e.printStackTrace();
      return status.EXCEPTION;
    }
  }

  public status addAuthor(AuthorModel author) {
    Connection conn = null;
    Statement statement = null;
    try {
      conn = Controller.instance().createConnection();
      statement = conn.createStatement();
      if (statement.executeUpdate("INSERT INTO author VALUES " + Helper.asValue(author.asValue())) != 0)
        return status.ADDED;
      return status.NOT_ADDED;
    } catch (SQLException e) {
      e.printStackTrace();
      return status.EXCEPTION;
    }
  }

  public List<AuthorModel> getAllAuthors() {
    List<AuthorModel> models = new ArrayList<>();
    Connection conn = null;
    Statement statement = null;
    ResultSet authors;
    try {
      conn = Controller.instance().createConnection();
      statement = conn.createStatement();
      authors = statement.executeQuery("SELECT * FROM author");
      while (authors.next()) {
        AuthorModel model = new AuthorModel();
        model.setID(authors.getString("author_id"));
        model.setName(authors.getString("author_name"));
        models.add(model);
      }
      return models;
    } catch (SQLException e) {
      e.printStackTrace();
      return null;
    }
  }

  public AuthorModel getAuthor(String author_id) {
    Connection conn = null;
    Statement statement = null;
    ResultSet author;
    try {
      conn = Controller.instance().createConnection();
      statement = conn.createStatement();
      String format = String.format("'%s'", author_id);
      author = statement.executeQuery("SELECT * FROM author WHERE author_id=" + format);
      if (author.next()) {
        AuthorModel model = new AuthorModel();
        model.setID(author.getString("author_id"));
        model.setName(author.getString("author_name"));
        return model;
      }
      return null;
    } catch (SQLException e) {
      e.printStackTrace();
      return null;
    }
  }

}