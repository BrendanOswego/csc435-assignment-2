package mainpackage.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import mainpackage.api.API;
import mainpackage.api.models.BookModel;

public class Books extends HttpServlet {
  private static final long serialVersionUID = -2767978412483553482L;

  public void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
    PrintWriter out = res.getWriter();
    getAllBooks(out);
    out.close();
  }

  public void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
    PrintWriter out = res.getWriter();
    out.close();
  }

  public void getAllBooks(PrintWriter out) {
    Gson gson = new GsonBuilder().setPrettyPrinting().create();
    List<BookModel> books = API.instance().getAllBooks();
    out.println(gson.toJson(books));
  }
}