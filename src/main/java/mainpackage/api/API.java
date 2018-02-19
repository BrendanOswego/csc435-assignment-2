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
import mainpackage.resources.SQLBuilder;

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
    SQLBuilder builder = new SQLBuilder();
    SQLBuilder inner = new SQLBuilder();
    int bookUpdate, baUpdate;
    try (Connection conn = Controller.instance().createConnection(); Statement statement = conn.createStatement()) {
      inner.select(book.asValue());
      builder.insert("book").select("*").from(String.format("(%s)", inner.result())).as("tmp");
      inner.clear();
      builder.whereExists(false,
          inner.select("title").from("book").where(String.format("title='%s'", book.getTitle())).result()).limit(1);
      bookUpdate = statement.executeUpdate(builder.result());
      System.out.println(builder.result());
      if (bookUpdate != 0) {
        builder.clear();
        builder.insert("book_author", ba.asValue());
        baUpdate = statement.executeUpdate(builder.result());
        System.out.println(builder.result());
        if (baUpdate != 0)
          return status.ADDED;
      }
      return status.NOT_ADDED;
    } catch (SQLException e) {
      e.printStackTrace();
      return status.EXCEPTION;
    }
  }

  public status addAuthor(AuthorModel author) {
    try (Connection conn = Controller.instance().createConnection(); Statement statement = conn.createStatement()) {
      SQLBuilder builder = new SQLBuilder();
      SQLBuilder inner = new SQLBuilder();
      inner.select(
          String.format("'%s', '%s', '%s'", author.getID().toString(), author.getFirstName(), author.getLastName()));
      builder.insert("author").select("*").from(String.format("(%s)", inner.result())).as("tmp");
      inner.clear();
      builder.whereExists(false,
          inner.select("first_name, last_name").from("author")
              .where(String.format("first_name='%s' and last_name='%s'", author.getFirstName(), author.getLastName()))
              .result())
          .limit(1);
      if (statement.executeUpdate(builder.result()) != 0)
        return status.ADDED;
      return status.NOT_ADDED;
    } catch (SQLException e) {
      e.printStackTrace();
      return status.EXCEPTION;
    }
  }

  public List<AuthorModel> getAllAuthors() {
    List<AuthorModel> models = new ArrayList<>();
    SQLBuilder builder = new SQLBuilder();
    ResultSet authors;
    try (Connection conn = Controller.instance().createConnection(); Statement statement = conn.createStatement();) {
      builder.select("*").from("author");
      authors = statement.executeQuery(builder.result());
      while (authors.next()) {
        AuthorModel model = new AuthorModel();
        model.setID(authors.getString("author_id"));
        model.setFirstName(authors.getString("first_name"));
        model.setLastName(authors.getString("last_name"));
        models.add(model);
      }
      return models;
    } catch (SQLException e) {
      e.printStackTrace();
      return null;
    }
  }

  public AuthorModel getAuthorById(String author_id) {
    SQLBuilder builder = new SQLBuilder();
    ResultSet author;
    try (Connection conn = Controller.instance().createConnection(); Statement statement = conn.createStatement();) {
      builder.select("*").from("author").where(String.format("author_id='%s'", author_id));
      author = statement.executeQuery(builder.result());
      if (author.next()) {
        AuthorModel model = new AuthorModel();
        model.setID(author.getString("author_id"));
        model.setFirstName(author.getString("first_name"));
        model.setLastName(author.getString("last_name"));
        return model;
      }
      return null;
    } catch (SQLException e) {
      e.printStackTrace();
      return null;
    }
  }

  public AuthorModel getAuthorByName(String name) {
    SQLBuilder builder = new SQLBuilder();
    ResultSet author;
    try (Connection conn = Controller.instance().createConnection(); Statement statement = conn.createStatement();) {
      builder.select("*").from("author").where(String.format("name='%s'", name));
      author = statement.executeQuery(builder.result());
      if (author.next()) {
        AuthorModel model = new AuthorModel();
        model.setID(author.getString("author_id"));
        model.setFirstName(author.getString("first_name"));
        model.setLastName(author.getString("last_name"));
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
    SQLBuilder builder = new SQLBuilder();
    ResultSet books;
    try (Connection conn = Controller.instance().createConnection(); Statement statement = conn.createStatement();) {
      builder.select("*").from("book");
      books = statement.executeQuery(builder.result());
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
    SQLBuilder builder = new SQLBuilder();
    ResultSet result;
    try (Connection conn = Controller.instance().createConnection(); Statement statement = conn.createStatement()) {
      builder.select("title, genre, year_published, pages").from("book b")
          .join("book_author ba", "b.book_id=ba.book_id").join("author a", "a.author_id=ba.author_id")
          .where(String.format("a.author_id='%s'", author_id));
      System.out.println(builder.result());
      result = statement.executeQuery(builder.result());
      while (result.next()) {
        BookModel model = new BookModel();
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

}