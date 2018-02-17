package mainpackage.servlets;

import java.io.IOException;
import java.io.PrintWriter;

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

public class Author extends HttpServlet {
  private static final long serialVersionUID = -2767978412483553482L;

  public void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
    PrintWriter out = res.getWriter();
    Gson gson = new GsonBuilder().setPrettyPrinting().create();
    if (req.getPathInfo() == null) {
      out.println(gson.toJson(API.instance().getAllAuthors()));
    } else {
      String id = req.getPathInfo().trim().substring(1).split("/")[0];
      out.println(gson.toJson(API.instance().getAuthor(id)));
    }
    out.close();
  }

  public void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
    PrintWriter out = res.getWriter();
    if (req.getPathInfo() == null) {
      addAuthor(out, req);
    } else {
      String[] paths = req.getPathInfo().substring(1).split("/");
      switch (paths.length) {
      case 0: {
        addAuthor(out, req);
        break;
      }
      case 2: {
        addBook(paths[0], out, req);
        break;
      }
      default: {
        break;
      }
      }
    }

    out.close();
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
      e.printStackTrace();
    }
  }

  private void addBook(String author_id, PrintWriter out, HttpServletRequest req) {
    ObjectMapper mapper = new ObjectMapper();
    AuthorModel author = API.instance().getAuthor(author_id);
    try {
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