package mainpackage.views;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import mainpackage.controller.*;
import mainpackage.models.AuthorModel;

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
          if (paths.length >= 2 && paths[1].equals("books")) {
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

  public void doPut(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
    res.setContentType("application/json");
    PrintWriter out = res.getWriter();
    if (req.getPathInfo() != null) {
      String[] paths = req.getPathInfo().substring(1).split("/");
      AuthorModel dbAuthor = API.instance().getAuthorById(paths[0]);
      if (paths.length == 1 && dbAuthor != null)
        updateAuthor(paths[0], out, req);
      else if (paths.length == 3 && dbAuthor != null)
        updateBook(paths[2], out, req);
    }
    out.close();
  }

  public void doDelete(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
    res.setContentType("application/json");
    PrintWriter out = res.getWriter();
    if (req.getPathInfo() != null) {
      String[] paths = req.getPathInfo().substring(1).split("/");
      AuthorModel dbAuthor = API.instance().getAuthorById(paths[0]);
      if (paths.length == 1 && dbAuthor != null)
        removeAuthor(paths[0], res.getWriter(), req);
      else if (paths.length == 3 && dbAuthor != null)
        removeBookByAuthor(paths[0], paths[2], out, req);
    }
    out.close();
  }

  private void addAuthor(PrintWriter out, HttpServletRequest req) {
    Gson gson = new GsonBuilder().setPrettyPrinting().create();
    API.status status = API.instance().addAuthor(req);
    out.println(gson.toJson(status));
  }

  private void addBook(String author_id, PrintWriter out, HttpServletRequest req) {
    Gson gson = new GsonBuilder().setPrettyPrinting().create();
    API.status status = API.instance().addBookToAuthor(author_id, req, out);
    out.println(gson.toJson(status));
  }

  private void updateAuthor(String author_id, PrintWriter out, HttpServletRequest req) {
    Gson gson = new GsonBuilder().setPrettyPrinting().create();
    API.instance().updateAuthor(author_id, req);
    out.println(gson.toJson(API.instance().getAuthorById(author_id)));
  }

  private void updateBook(String book_id, PrintWriter out, HttpServletRequest req) {
    Gson gson = new GsonBuilder().setPrettyPrinting().create();
    out.println(gson.toJson(API.instance().updateBook(book_id, req)));
  }

  private void removeAuthor(String author_id, PrintWriter out, HttpServletRequest req) {
    Gson gson = new GsonBuilder().setPrettyPrinting().create();
    out.println(gson.toJson(API.instance().removeAuthor(author_id, req)));
  }

  private void removeBookByAuthor(String author_id, String book_id, PrintWriter out, HttpServletRequest req) {
    Gson gson = new GsonBuilder().setPrettyPrinting().create();
    out.println(gson.toJson(API.instance().removeBookByAuthor(author_id, book_id, req)));
  }

}