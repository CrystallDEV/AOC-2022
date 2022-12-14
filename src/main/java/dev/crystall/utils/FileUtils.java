package dev.crystall.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by CrystallDEV on 02/12/2022
 */
@Slf4j
public class FileUtils {

  private FileUtils() {
  }

  public static BufferedReader readFileBuffered(String fileName) {
    var fileURL = FileUtils.class.getClassLoader().getResource(fileName);
    if (fileURL == null) {
      log.error("Unable to find file {}", fileName);
      return null;
    }
    var inputFile = fileURL.getFile();
    try {
      return new BufferedReader(new FileReader(inputFile));
    } catch (IOException e) {
      log.error("Unable to read file {}", fileName);
    }

    return null;
  }

  /**
   * Reads the given resource file as a string.
   *
   * @param fileName path to the file
   * @return the file content as one string
   */
  public static String getResourceFileAsString(String fileName) {
    ClassLoader classLoader = ClassLoader.getSystemClassLoader();
    try (InputStream is = classLoader.getResourceAsStream(fileName)) {
      if (is == null) {
        return null;
      }
      try (InputStreamReader isr = new InputStreamReader(is);
        BufferedReader reader = new BufferedReader(isr)) {
        return reader.lines().collect(Collectors.joining(System.lineSeparator()));
      }
    } catch (IOException ex) {
      log.error("Unable to read file as string {}", fileName);
    }
    return null;
  }

}
