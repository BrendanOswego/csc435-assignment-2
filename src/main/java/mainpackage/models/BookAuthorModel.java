package mainpackage.models;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class BookAuthorModel {

  private UUID book_id;

  private UUID author_id;

  public BookAuthorModel() {
  }

  @JsonCreator
  public BookAuthorModel(@JsonProperty("book_id") UUID book_id, @JsonProperty("author_id") UUID author_id) {
    this.book_id = book_id;
    this.author_id = author_id;
  }

  public UUID getBookID() {
    return book_id;
  }

  public UUID getAuthorID() {
    return author_id;
  }

  public void setBookID(String book_id) {
    this.book_id = UUID.fromString(book_id);
  }

  public void setAuthorID(String author_id) {
    this.author_id = UUID.fromString(author_id);
  }

}