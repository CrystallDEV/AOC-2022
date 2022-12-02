package dev.crystall.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
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


}
