package mainpackage.api;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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

  public status addBookToAuthor(AuthorModel author, BookModel book) throws SQLException {
    BookAuthorModel ba = new BookAuthorModel(book.getID(), author.getID());
    try (Connection conn = Controller.instance().createConnection(); Statement statement = conn.createStatement()) {
      if (statement.executeUpdate("INSERT INTO book VALUES " + Helper.asValue(book.asValue())) != 0
          && statement.executeUpdate("INSERT INTO book_author VALUES " + Helper.asValue(ba.asValue())) != 0)
        return status.ADDED;
      return status.NOT_ADDED;
    }
  }

  public status addAuthor(AuthorModel author) throws SQLException {
    try (Connection conn = Controller.instance().createConnection(); Statement statement = conn.createStatement()) {
      if (statement.executeUpdate("INSERT INTO author VALUES " + Helper.asValue(author.asValue())) != 0)
        return status.ADDED;
      return status.NOT_ADDED;
    }
  }

  public List<AuthorModel> getAllAuthors() throws SQLException {
    List<AuthorModel> models = new ArrayList<>();
    ResultSet authors;
    try (Connection conn = Controller.instance().createConnection(); Statement statement = conn.createStatement();) {
      authors = statement.executeQuery("SELECT * FROM author");
      while (authors.next()) {
        AuthorModel model = new AuthorModel();
        model.setID(authors.getString("author_id"));
        model.setName(authors.getString("author_name"));
        models.add(model);
      }
      return models;
    }
  }

  public AuthorModel getAuthor(String author_id) throws SQLException {
    ResultSet author;
    try (Connection conn = Controller.instance().createConnection(); Statement statement = conn.createStatement();) {
      String format = String.format("'%s'", author_id);
      author = statement.executeQuery("SELECT * FROM author WHERE author_id=" + format);
      if (author.next()) {
        AuthorModel model = new AuthorModel();
        model.setID(author.getString("author_id"));
        model.setName(author.getString("author_name"));
        return model;
      }
      return null;
    }
  }

  public List<BookModel> getAllBooks() throws SQLException {
    List<BookModel> models = new ArrayList<>();
    ResultSet books;
    try (Connection conn = Controller.instance().createConnection(); Statement statement = conn.createStatement();) {
      books = statement.executeQuery("SELECT * FROM book");
      while (books.next()) {
        BookModel model = new BookModel();
        model.setID(books.getString("book_id"));
        model.setTitle(books.getString("title"));
        models.add(model);
      }
      return models;
    }
  }

  public List<BookModel> getBooksByAuthor(String author_id) throws SQLException {
    List<BookModel> models = new ArrayList<>();
    ResultSet book_author = null;
    ResultSet books = null;
    try (Connection conn = Controller.instance().createConnection();
        Statement baStatement = conn.createStatement();
        Statement booksStatement = conn.createStatement()) {
      String format = String.format("'%s'", author_id);
      book_author = baStatement.executeQuery("SELECT * FROM book_author WHERE author_id=" + format);
      while (book_author.next()) {
        String book_id = book_author.getString("book_id");
        format = String.format("'%s'", book_id.toString());
        books = booksStatement.executeQuery("SELECT * FROM book WHERE book_id=" + format);
        while (books.next()) {
          BookModel book = new BookModel();
          book.setID(books.getString("book_id"));
          book.setTitle(books.getString("title"));
          models.add(book);
        }
      }
      return models;
    }
  }

}