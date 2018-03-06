package mainpackage.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;

import mainpackage.models.AuthorModel;
import mainpackage.models.BookAuthorModel;
import mainpackage.models.BookModel;
import mainpackage.models.ResponseModel;
import mainpackage.resources.SQLConnection;

//TODO: Change rest of responses to DataResponseModel or ResponseModel
public class API {

  public static enum status {
    ADDED, UPDATED, REMOVED, RETRIEVED, EXCEPTION, NOT_ADDED, NOT_UPDATED, NOT_REMOVED, ALREADY_ADDED
  }

  private static API instance = null;

  private API() {
  }

  public static API instance() {
    if (instance == null)
      instance = new API();
    return instance;
  }

  public ResponseModel addBookToAuthor(String author_id, HttpServletRequest req, PrintWriter out) {
    ObjectMapper mapper = new ObjectMapper();
    ResponseModel response = new ResponseModel();
    String query1 = "SELECT * FROM book WHERE title=? AND genre=? AND year_published=? AND pages=?";
    try (Connection conn = SQLConnection.instance().createConnection();
        PreparedStatement statement = conn.prepareStatement(query1)) {
      BookModel book = mapper.readValue(req.getReader(), BookModel.class);
      BookAuthorModel ba = new BookAuthorModel(book.getID(), UUID.fromString(author_id));
      statement.setString(1, book.getTitle());
      statement.setString(2, book.getGenre());
      statement.setInt(3, book.getYearPublished());
      statement.setInt(4, book.getPages());
      ResultSet result1 = statement.executeQuery();
      if (result1.next()) { //IF the book is already in the database, but the author doesn't have the book
        if (authorHasBook(result1.getString("book_id"), author_id)) {
          response.setStatus(200);
          response.setMessage("Book already added to Author");
        }
        book.setID(result1.getString("book_id"));
        ba.setBookID(book.getID().toString());
        String query2 = "INSERT INTO book_author " + "SELECT * FROM (SELECT ?, ?) AS tmp WHERE NOT EXISTS ( "
            + "SELECT book_id, author_id FROM book_author WHERE book_id=? AND author_id=? ) LIMIT 1;";
        PreparedStatement statement2 = conn.prepareStatement(query2);
        statement2.setString(1, ba.getBookID().toString());
        statement2.setString(2, ba.getAuthorID().toString());
        statement2.setString(3, ba.getBookID().toString());
        statement2.setString(4, ba.getAuthorID().toString());
        statement2.execute();
        if (statement2.getUpdateCount() > 0) {
          response.setStatus(201);
          response.setMessage("Book from table was added to Author");
        } else {
          response.setStatus(200);
          response.setMessage("Book from table already added to Author");
        }
        response.setHref(req.getPathInfo());
        return response;
      }
      String query3 = "INSERT INTO book (book_id, title, genre, year_published, pages) "
          + "SELECT * FROM (SELECT ?, ?, ?, ?, ?) AS tmp WHERE NOT EXISTS ( "
          + "SELECT title, genre, year_published, pages FROM book WHERE title=? AND genre=? AND year_published=? AND pages=? ) LIMIT 1;";
      PreparedStatement statement3 = conn.prepareStatement(query3);
      statement3.setString(1, book.getID().toString());
      statement3.setString(2, book.getTitle());
      statement3.setString(3, book.getGenre());
      statement3.setInt(4, book.getYearPublished());
      statement3.setInt(5, book.getPages());
      statement3.setString(6, book.getTitle());
      statement3.setString(7, book.getGenre());
      statement3.setInt(8, book.getYearPublished());
      statement3.setInt(9, book.getPages());
      statement3.execute();
      if (statement3.getUpdateCount() > 0) {
        String query4 = "INSERT INTO book_author " + "SELECT * FROM (SELECT ?, ?) AS tmp WHERE NOT EXISTS ( "
            + "SELECT book_id, author_id FROM book_author WHERE book_id=? AND author_id=? ) LIMIT 1;";
        PreparedStatement statement4 = conn.prepareStatement(query4);
        statement4.setString(1, ba.getBookID().toString());
        statement4.setString(2, ba.getAuthorID().toString());
        statement4.setString(3, ba.getBookID().toString());
        statement4.setString(4, ba.getAuthorID().toString());
        statement4.execute();
        if (statement4.getUpdateCount() > 0) {
          response.setStatus(201);
          response.setMessage("Book added to Author");
        } else {
          response.setStatus(200);
          response.setMessage("Book was not added to Author");
        }
        return response;
      }
      response.setStatus(200);
      response.setMessage("Book already added to Author");
      return response;
    } catch (SQLException | IOException e) {
      e.printStackTrace();
      response.setStatus(500);
      response.setMessage("There was an internal SQL exception");
      return response;
    }
  }

  public ResponseModel addAuthor(HttpServletRequest req) {
    ObjectMapper mapper = new ObjectMapper();
    ResponseModel model = new ResponseModel();
    try {
      AuthorModel author = mapper.readValue(req.getReader(), AuthorModel.class);
      String query = "INSERT INTO author (author_id, first_name, last_name) SELECT * FROM (SELECT ?, ?, ?) as tmp WHERE NOT EXISTS (SELECT first_name, last_name FROM author WHERE first_name=? AND last_name=?)";
      try (Connection conn = SQLConnection.instance().createConnection();
          PreparedStatement statement = conn.prepareStatement(query)) {
        statement.setString(1, author.getID().toString());
        statement.setString(2, author.getFirstName());
        statement.setString(3, author.getLastName());
        statement.setString(4, author.getFirstName());
        statement.setString(5, author.getLastName());
        statement.execute();
        if (statement.getUpdateCount() > 0) {
          model.setStatus(201);
          model.setMessage("Author added");
        } else {
          model.setMessage("Author already added");
          model.setStatus(200);
        }
        return model;
      } catch (SQLException e) {
        e.printStackTrace();
        model.setSQLException(req.getPathInfo());
        return model;
      }
    } catch (IOException e) {
      e.printStackTrace();
      model.setSQLException(req.getPathInfo());
      return model;
    }
  }

  public List<AuthorModel> getAllAuthors() {
    List<AuthorModel> models = new ArrayList<>();
    ResultSet result;
    String query = "SELECT * FROM author";
    try (Connection conn = SQLConnection.instance().createConnection();
        PreparedStatement statement = conn.prepareStatement(query);) {
      result = statement.executeQuery();
      while (result.next()) {
        AuthorModel model = new AuthorModel();
        model.setID(result.getString("author_id"));
        model.setFirstName(result.getString("first_name"));
        model.setLastName(result.getString("last_name"));
        models.add(model);
      }
      return models;
    } catch (SQLException e) {
      e.printStackTrace();
      return null;
    }
  }

  public AuthorModel getAuthorById(String author_id) {
    ResultSet result;
    String query = "SELECT * FROM author WHERE author_id=?";
    try (Connection conn = SQLConnection.instance().createConnection();
        PreparedStatement statement = conn.prepareStatement(query);) {
      statement.setString(1, author_id);
      result = statement.executeQuery();
      if (result.next()) {
        AuthorModel model = new AuthorModel();
        model.setID(result.getString("author_id"));
        model.setFirstName(result.getString("first_name"));
        model.setLastName(result.getString("last_name"));
        return model;
      }
      return null;
    } catch (SQLException e) {
      e.printStackTrace();
      return null;
    }
  }

  public List<BookModel> getAllBooks() {
    List<BookModel> models = new ArrayList<>();
    ResultSet books;
    String query = "SELECT * FROM book";
    try (Connection conn = SQLConnection.instance().createConnection();
        PreparedStatement statement = conn.prepareStatement(query)) {
      books = statement.executeQuery();
      while (books.next()) {
        BookModel model = new BookModel();
        model.setID(books.getString("book_id"));
        model.setTitle(books.getString("title"));
        model.setGenre(books.getString("genre"));
        model.setYearPublished(books.getInt("year_published"));
        model.setPages(books.getInt("pages"));
        models.add(model);
      }
      return models;
    } catch (SQLException e) {
      e.printStackTrace();
      return null;
    }
  }

  public List<BookModel> getBooksByAuthor(String author_id) {
    List<BookModel> models = new ArrayList<>();
    ResultSet result;
    String query = "SELECT * FROM book b JOIN book_author ba ON b.book_id=ba.book_id JOIN author a ON a.author_id=ba.author_id WHERE a.author_id=?";
    try (Connection conn = SQLConnection.instance().createConnection();
        PreparedStatement statement = conn.prepareStatement(query)) {
      statement.setString(1, author_id);
      result = statement.executeQuery();
      while (result.next()) {
        BookModel model = new BookModel();
        model.setID(result.getString("book_id"));
        model.setTitle(result.getString("title"));
        model.setGenre(result.getString("genre"));
        model.setYearPublished(result.getInt("year_published"));
        model.setPages(result.getInt("pages"));
        models.add(model);
      }
      return models;
    } catch (SQLException e) {
      e.printStackTrace();
      return null;
    }
  }

  public ResponseModel updateAuthor(String author_id, HttpServletRequest req) {
    ObjectMapper mapper = new ObjectMapper();
    ResponseModel model = new ResponseModel();
    ResultSet result;
    Connection conn = null;
    PreparedStatement statement = null;
    try {
      AuthorModel newModel = (AuthorModel) mapper.readValue(req.getReader(), AuthorModel.class);
      AuthorModel oldModel = API.instance().getAuthorById(author_id);
      conn = SQLConnection.instance().createConnection();
      statement = conn.prepareStatement("SELECT * FROM author WHERE author_id=?", ResultSet.TYPE_SCROLL_INSENSITIVE,
          ResultSet.CONCUR_UPDATABLE);
      statement.setString(1, author_id);
      result = statement.executeQuery();
      if (result.next()) {
        if (newModel.getFirstName() != null && !newModel.getFirstName().equals(oldModel.getLastName()))
          result.updateString("first_name", newModel.getFirstName());
        if (newModel.getLastName() != null && !newModel.getLastName().equals(oldModel.getLastName()))
          result.updateString("last_name", newModel.getLastName());
        result.updateRow();
        model.setMessage("Author updated successfully");
        model.setStatus(200);
        return model;
      }
      model.setMessage("Author specified does not exist");
      model.setStatus(200);
      return model;
    } catch (SQLException | IOException e) {
      e.printStackTrace();
      model.setSQLException(req.getPathInfo());
      return model;
    } finally {
      if (statement != null) {
        try {
          statement.close();
        } catch (SQLException e) {
          e.printStackTrace();
        }
      }
    }
  }

  public ResponseModel updateBook(String book_id, HttpServletRequest req) {
    ObjectMapper mapper = new ObjectMapper();
    ResponseModel model = new ResponseModel();
    ResultSet result;
    Connection conn = null;
    PreparedStatement statement = null;
    try {
      BookModel oldBook = getBook(book_id);
      BookModel newBook = (BookModel) mapper.readValue(req.getReader(), BookModel.class);
      conn = SQLConnection.instance().createConnection();
      statement = conn.prepareStatement("SELECT * FROM book where book_id=?", ResultSet.TYPE_SCROLL_INSENSITIVE,
          ResultSet.CONCUR_UPDATABLE);
      statement.setString(1, book_id);
      result = statement.executeQuery();
      if (result.next()) {
        if (newBook.getGenre() != null && !newBook.getGenre().equals(oldBook.getGenre()))
          result.updateString("genre", newBook.getGenre());
        if (newBook.getTitle() != null && !newBook.getTitle().equals(oldBook.getTitle()))
          result.updateString("title", newBook.getTitle());
        if (newBook.getPages() != 0 && (newBook.getPages() != oldBook.getPages()))
          result.updateInt("pages", newBook.getPages());
        if (newBook.getYearPublished() != 0 && (newBook.getYearPublished() != oldBook.getYearPublished()))
          result.updateInt("year_published", newBook.getYearPublished());
        result.updateRow();
        model.setMessage("Book updated successfully");
        model.setStatus(200);
        return model;
      }
      model.setMessage("Book does not exist");
      model.setStatus(200);
      return model;
    } catch (SQLException | IOException e) {
      e.printStackTrace();
      model.setSQLException(req.getPathInfo());
      return model;
    } finally {
      if (statement != null) {
        try {
          statement.close();
        } catch (SQLException e) {
          e.printStackTrace();
        }
      }
    }
  }

  public ResponseModel removeAuthor(String author_id, HttpServletRequest req) {
    ResponseModel model = new ResponseModel();
    try {
      Connection conn = SQLConnection.instance().createConnection();
      PreparedStatement ba_statement = conn.prepareStatement("DELETE FROM book_author WHERE author_id=?");
      ba_statement.setString(1, author_id);
      ba_statement.execute();
      PreparedStatement author_statement = conn.prepareStatement("DELETE FROM author WHERE author_id=?");
      author_statement.setString(1, author_id);
      author_statement.execute();
      if (author_statement.getUpdateCount() > 0) {
        model.setMessage("Author removed successfully");
        model.setStatus(200);
      } else {
        model.setMessage("Author does not exist");
        model.setStatus(200);
      }
      return model;
    } catch (SQLException e) {
      e.printStackTrace();
      model.setSQLException(req.getPathInfo());
      return model;
    }
  }

  public ResponseModel removeBookByAuthor(String author_id, String book_id, HttpServletRequest req) {
    ResponseModel model = new ResponseModel();
    try {
      Connection conn = SQLConnection.instance().createConnection();
      PreparedStatement statement = conn.prepareStatement("DELETE FROM book_author WHERE book_id=? AND author_id=?");
      statement.setString(1, book_id);
      statement.setString(2, author_id);
      statement.execute();
      if (statement.getUpdateCount() > 0) {
        model.setMessage("Book removed from Author successfully");
        model.setStatus(200);
      } else {
        model.setMessage("Book by Author does not exist");
        model.setStatus(200);
      }
      return model;
    } catch (SQLException e) {
      e.printStackTrace();
      model.setSQLException(req.getPathInfo());
      return model;
    }
  }

  public ResponseModel removeBook(String book_id, HttpServletRequest req) {
    ResponseModel model = new ResponseModel();
    try {
      Connection conn = SQLConnection.instance().createConnection();
      PreparedStatement ba_statement = conn.prepareStatement("DELETE FROM book_author WHERE book_id=?");
      ba_statement.setString(1, book_id);
      ba_statement.execute();
      PreparedStatement book_statement = conn.prepareStatement("DELETE FROM book WHERE book_id=?");
      book_statement.setString(1, book_id);
      book_statement.execute();
      if (book_statement.getUpdateCount() > 0) {
        model.setMessage("Book removed successfully");
        model.setStatus(200);
      } else {
        model.setMessage("Book does not exist");
        model.setStatus(200);
      }
      return model;
    } catch (SQLException e) {
      e.printStackTrace();
      model.setSQLException(req.getPathInfo());
      return model;
    }
  }

  public BookModel getBook(String book_id) {
    String query = "SELECT * FROM book where book_id=?";
    try (Connection conn = SQLConnection.instance().createConnection();
        PreparedStatement statement = conn.prepareStatement(query);) {
      statement.setString(1, book_id);
      ResultSet result = statement.executeQuery();
      if (result.next()) {
        BookModel temp = new BookModel();
        temp.setID(result.getString("book_id"));
        temp.setTitle(result.getString("title"));
        temp.setYearPublished(result.getInt("year_published"));
        temp.setPages(result.getInt("pages"));
        temp.setGenre(result.getString("genre"));
        return temp;
      }
      return null;
    } catch (SQLException e) {
      e.printStackTrace();
      return null;
    }
  }

  private boolean authorHasBook(String book_id, String author_id) {
    String query = "SELECT * FROM book_author WHERE book_id=? AND author_id=?";
    try (Connection conn = SQLConnection.instance().createConnection();
        PreparedStatement statement = conn.prepareStatement(query);) {
      statement.setString(1, book_id);
      statement.setString(2, author_id);
      ResultSet result = statement.executeQuery();
      return result.next();
    } catch (SQLException e) {
      e.printStackTrace();
      return false;
    }
  }

}