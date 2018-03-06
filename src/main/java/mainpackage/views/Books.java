package mainpackage.views;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import mainpackage.controller.API;

public class Books extends HttpServlet {
  private static final long serialVersionUID = -2767978412483553482L;

  public void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
    res.setContentType("application/json");
    Gson gson = new GsonBuilder().setPrettyPrinting().create();
    PrintWriter out = res.getWriter();
    if (req.getPathInfo() == null) {
      out.println(gson.toJson(API.instance().getAllBooks()));
    } else {
      String id = req.getPathInfo().substring(1).split("/")[0];
      out.println(gson.toJson(API.instance().getBook(id)));
    }
    out.close();
  }

  public void doPut(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
    res.setContentType("application/json");
    Gson gson = new GsonBuilder().setPrettyPrinting().create();
    PrintWriter out = res.getWriter();
    String[] paths = req.getPathInfo().substring(1).split("/");
    if (paths.length == 0) {
      doGet(req, res);
    } else if (paths.length == 1) {
      out.println(gson.toJson(API.instance().updateBook(paths[0], req)));
    }
  }

  public void doDelete(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
    res.setContentType("application/json");
    Gson gson = new GsonBuilder().setPrettyPrinting().create();
    String[] paths = req.getPathInfo().substring(1).split("/");
    if (paths.length == 0) {
      doGet(req, res);
    } else if (paths.length == 1) {
      res.getWriter().println(gson.toJson(API.instance().removeBook(paths[0], req)));
    }
  }

}