package mainpackage.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.UUID;

public class AuthorModel {

  @JsonProperty
  private UUID author_id;

  private String last_name;

  private String first_name;

  public AuthorModel() {
  }

  @JsonCreator
  public AuthorModel(@JsonProperty("first_name") String first_name, @JsonProperty("last_name") String last_name) {
    this.author_id = UUID.randomUUID();
    this.first_name = first_name;
    this.last_name = last_name;
  }

  public String getLastName() {
    return last_name;
  }

  public String getFirstName() {
    return first_name;
  }

  public UUID getID() {
    return author_id;
  }

  public void setID(String author_id) {
    this.author_id = UUID.fromString(author_id);
  }

  public void setLastName(String last_name) {
    this.last_name = last_name;
  }

  public void setFirstName(String first_name) {
    this.first_name = first_name;
  }

}