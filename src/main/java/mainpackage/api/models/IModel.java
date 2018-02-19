package mainpackage.api.models;

import java.util.UUID;

public interface IModel {
  public String asValue();
  public UUID getID();
}