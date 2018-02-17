package mainpackage.api.models;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class BookModel {

  private UUID book_id;
  private String title;

  public BookModel() {
  }

  @JsonCreator
  public BookModel(@JsonProperty("title") String title) {
    this.book_id = UUID.randomUUID();
    this.title = title;
  }

  public String getTitle() {
    return title;
  }

  public UUID getID() {
    return book_id;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public void setID(String book_id) {
    this.book_id = UUID.fromString(book_id);
  }

  public String asValue() {
    return String.format("'%s', '%s\'", book_id.toString(), title);
  }

}