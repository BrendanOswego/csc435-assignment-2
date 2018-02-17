package mainpackage.resources;

public class Helper {
  public static String asValue(Object o) {
    return String.format("(%s)", o.toString());
  }
}