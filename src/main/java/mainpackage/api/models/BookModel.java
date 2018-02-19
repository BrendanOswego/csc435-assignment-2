package mainpackage.api.models;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class BookModel implements IModel {

  @JsonProperty
  private UUID book_id;

  private String title;

  private String genre;

  private int year_published;

  private int pages;

  public BookModel() {
  }

  @JsonCreator
  public BookModel(@JsonProperty("title") String title, @JsonProperty("genre") String genre,
      @JsonProperty("year_published") int year_published, @JsonProperty("pages") int pages) {
    this.book_id = UUID.randomUUID();
    this.title = title;
    this.genre = genre;
    this.year_published = year_published;
    this.pages = pages;
  }

  public String asValue() {
    return String.format("'%s', '%s', '%s', %d, %d", book_id.toString(), title, genre, year_published, pages);
  }

  public String getTitle() {
    return title;
  }

  public UUID getID() {
    return book_id;
  }

  public String getGenre() {
    return genre;
  }

  public int getYearPublished() {
    return year_published;
  }

  public int getPages() {
    return pages;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public void setID(String book_id) {
    this.book_id = UUID.fromString(book_id);
  }

  public void setGenre(String genre) {
    this.genre = genre;
  }

  public void setYearPublished(int year_published) {
    this.year_published = year_published;
  }

  public void setPages(int pages) {
    this.pages = pages;
  }

}