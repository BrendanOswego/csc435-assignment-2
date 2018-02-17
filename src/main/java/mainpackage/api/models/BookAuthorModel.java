package mainpackage.api.models;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;

public class BookAuthorModel {

  private UUID ba_id;
  private UUID book_id;
  private UUID author_id;

  public BookAuthorModel(@JsonProperty("book_id") UUID book_id, @JsonProperty("author_id") UUID author_id) {
    this.ba_id = UUID.randomUUID();
    this.book_id = book_id;
    this.author_id = author_id;
  }

  public UUID getBookID() {
    return book_id;
  }

  public UUID getAuthorID() {
    return author_id;
  }

  public String asValue() {
    return String.format("'%s', '%s', '%s'", ba_id.toString(), book_id.toString(), author_id.toString());
  }

}