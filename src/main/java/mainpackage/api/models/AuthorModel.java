package mainpackage.api.models;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class AuthorModel {

  private UUID author_id;

  private String author_name;

  public AuthorModel() {
  }

  @JsonCreator
  public AuthorModel(@JsonProperty("author_name") String author_name) {
    this.author_id = UUID.randomUUID();
    this.author_name = author_name;
  }

  public String getAuthorName() {
    return author_name;
  }

  public UUID getID() {
    return author_id;
  }

  public String asValue() {
    return String.format("'%s', '%s\'", author_id.toString(), author_name);
  }

  public void setName(String author_name) {
    this.author_name = author_name;
  }

  public void setID(String author_id) {
    this.author_id = UUID.fromString(author_id);
  }

}