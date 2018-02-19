package mainpackage.api.models;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class BookAuthorModel implements IModel {

  @JsonProperty
  private UUID ba_id;

  private UUID book_id;

  private UUID author_id;

  public BookAuthorModel() {
  }

  @JsonCreator
  public BookAuthorModel(@JsonProperty("book_id") UUID book_id, @JsonProperty("author_id") UUID author_id) {
    this.ba_id = UUID.randomUUID();
    this.book_id = book_id;
    this.author_id = author_id;
  }

  public String asValue() {
    return String.format("'%s', '%s', '%s'", ba_id.toString(), book_id.toString(), author_id.toString());
  }

  public UUID getID() {
    return ba_id;
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