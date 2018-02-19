package mainpackage.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import mainpackage.api.API;
import mainpackage.api.models.AuthorModel;
import mainpackage.api.models.BookModel;

public class Authors extends HttpServlet {
  private static final long serialVersionUID = -2767978412483553482L;

  public void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
    PrintWriter out = res.getWriter();
    Gson gson = new GsonBuilder().setPrettyPrinting().create();
    if (req.getPathInfo() == null) {
      out.println(gson.toJson(API.instance().getAllAuthors()));
    } else {
      String[] paths = req.getPathInfo().trim().substring(1).split("/");
      String id = paths[0];
      if (id != null) {
        try {
          if (paths[1] != null && paths[1].equals("books")) {
            out.println(gson.toJson(API.instance().getBooksByAuthor(id)));
          } else {
            out.println(gson.toJson(API.instance().getAuthorById(id)));
          }
        } catch (ArrayIndexOutOfBoundsException e) {
          e.printStackTrace();
          out.println(gson.toJson(API.instance().getAuthorById(id)));
        }
      }
    }
    out.close();
  }

  public void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
    PrintWriter out = res.getWriter();
    if (req.getPathInfo() == null) {
      addAuthor(out, req);
    } else {
      String[] paths = req.getPathInfo().substring(1).split("/");
      if (paths.length == 0)
        addAuthor(out, req);
      else if (paths.length == 2)
        addBook(paths[0], out, req);
    }
    out.close();
  }

  public void doDelete(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
    PrintWriter out = res.getWriter();
    String id = req.getPathInfo().trim().substring(1).split("/")[0];

  }

  private void addAuthor(PrintWriter out, HttpServletRequest req) {
    ObjectMapper mapper = new ObjectMapper();
    try {
      AuthorModel author = mapper.readValue(req.getReader(), AuthorModel.class);
      API.status status = API.instance().addAuthor(author);
      if (status == API.status.ADDED)
        out.println("Author added successfully");
      else if (status == API.status.EXCEPTION)
        out.println("Error when trying to add author");
      else
        out.println("Could not add author to database");
    } catch (IOException e) {
      out.println(e.getMessage());
    }
  }

  private void addBook(String author_id, PrintWriter out, HttpServletRequest req) {
    ObjectMapper mapper = new ObjectMapper();
    try {
      AuthorModel author = API.instance().getAuthorById(author_id);
      BookModel book = mapper.readValue(req.getReader(), BookModel.class);
      API.status status = API.instance().addBookToAuthor(author, book);
      if (status == API.status.ADDED)
        out.println("Book added successfully");
      else if (status == API.status.EXCEPTION)
        out.println("Error when trying to add book");
      else
        out.println("Could not add book to database");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}