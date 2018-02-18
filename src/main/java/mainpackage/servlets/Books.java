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

public class Books extends HttpServlet {
  private static final long serialVersionUID = -2767978412483553482L;

  public void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
    PrintWriter out = res.getWriter();
    out.close();
  }

  public void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
    PrintWriter out = res.getWriter();

    out.close();
  }

  private void addAuthor(PrintWriter out, HttpServletRequest req) {

  }

  private void addBook(String author_id, PrintWriter out, HttpServletRequest req) {

  }
}