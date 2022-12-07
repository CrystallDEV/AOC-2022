package dev.crystall.utils;


public class StringUtils {

  private StringUtils() {

  }

  public static boolean isNumber(String s) {
    if (s == null || s.isEmpty() || s.isBlank()) {
      return false;
    }
    try {
      Integer.parseInt(s);
      return true;
    } catch (NumberFormatException ignored) {
      return false;
    }
  }
}
