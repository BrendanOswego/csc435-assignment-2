package mainpackage.resources;

public class SQLBuilder {

  private String query;

  public SQLBuilder() {
    query = "";
  }

  public SQLBuilder select(String select) {
    query += "SELECT " + select + " ";
    return this;
  }

  public SQLBuilder from(String from) {
    query += "FROM " + from + " ";
    return this;
  }

  public SQLBuilder insert(String table) {
    query += "INSERT INTO " + table + " ";
    return this;
  }

  public SQLBuilder insert(String table, String values) {
    query += String.format("INSERT INTO %s VALUES (%s)", table, values);
    return this;
  }

  public SQLBuilder as(String temp) {
    query += "AS " + temp + " ";
    return this;
  }

  public SQLBuilder whereExists(boolean exists, String where) {
    query += String.format("WHERE " + (exists ? "EXISTS" : "NOT EXISTS (%s)"), where) + " ";
    return this;
  }

  public SQLBuilder where(String where) {
    query += "WHERE " + where + " ";
    return this;
  }

  public SQLBuilder join(String var, String on) {
    query += "JOIN " + var + " ON " + on + " ";
    return this;
  }

  public String result() {
    return query;
  }

  public void clear() {
    query = "";
  }

  public SQLBuilder limit(int limit) {
    query += "LIMIT " + Integer.valueOf(limit) + " ";
    return this;
  }

}