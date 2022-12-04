package dev.crystall.days;

import dev.crystall.utils.FileUtils;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by CrystallDEV on 04/12/2022
 */
@Slf4j
public class Day4 {

  public Day4() {
    String line;
    try (BufferedReader reader = FileUtils.readFileBuffered("input_day4.txt")) {
      int fullyContainsOtherCount = 0;
      int partlyContainsOtherCount = 0;

      while ((line = reader.readLine()) != null) {
        var pair = getPairFromLine(line);
        var firstElf = getSectionsForElf(pair[0]);
        var secondElf = getSectionsForElf(pair[1]);
        if (fullyContainsOtherElf(firstElf, secondElf) || fullyContainsOtherElf(secondElf, firstElf)) {
          fullyContainsOtherCount++;
        }

        if (partlyContainsOtherElf(firstElf, secondElf) || partlyContainsOtherElf(secondElf, firstElf)) {
          partlyContainsOtherCount++;
        }
      }

      log.info("Part 1: {}", fullyContainsOtherCount);
      log.info("Part 2: {}", partlyContainsOtherCount);

    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private boolean fullyContainsOtherElf(List<Integer> firstElfSections, List<Integer> secondElfSections) {
    for (Integer integer : firstElfSections) {
      if (!secondElfSections.contains(integer)) {
        return false;
      }
    }
    return true;
  }

  private boolean partlyContainsOtherElf(List<Integer> firstElfSections, List<Integer> secondElfSections) {
    for (Integer integer : firstElfSections) {
      if (secondElfSections.contains(integer)) {
        return true;
      }
    }
    return false;
  }

  private String[] getPairFromLine(String line) {
    return line.split(",");
  }

  private List<Integer> getSectionsForElf(String elfSections) {
    var sectionNumber = elfSections.split("-");
    var startNumber = Integer.parseInt(sectionNumber[0]);
    var endNumber = Integer.parseInt(sectionNumber[1]);

    List<Integer> sections = new ArrayList<>();
    for (int i = startNumber; i <= endNumber; i++) {
      sections.add(i);
    }
    return sections;
  }

  public static void main(String[] args) {
    new Day4();
  }

}
