package dev.crystall.days;

import dev.crystall.utils.FileUtils;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Day1 {

  public Day1() {
    var elfCalories = calculateElfCalories();
    // Sort by highest value
    elfCalories = sortByHighestValue(elfCalories);

    // print out final values
    var highestValue = getHighestCaloryElf(elfCalories);
    log.info("Highest Elf Value {}", highestValue);
    var topThreeValue = sumTopThree(elfCalories);
    log.info("Top 3 Elf's: {}", topThreeValue);
  }

  private int getHighestCaloryElf(List<Integer> elfCalories) {
    return elfCalories.get(0);
  }

  private int sumTopThree(List<Integer> elfCalories) {
    return elfCalories.get(0) + elfCalories.get(1) + elfCalories.get(2);
  }

  private List<Integer> sortByHighestValue(List<Integer> toSort) {
    return toSort.stream()
      .sorted(new SortByHighestValueComparator())
      .toList();
  }

  private List<Integer> calculateElfCalories() {
    List<Integer> elfCalories = new ArrayList<>();
    String line;
    var currentElfCalories = 0;

    try (BufferedReader reader = FileUtils.readFileBuffered("input_day1.txt")) {
      while ((line = reader.readLine()) != null) {
        try {
          currentElfCalories += Integer.parseInt(line);
        } catch (NumberFormatException ex) {
          elfCalories.add(currentElfCalories);
          currentElfCalories = 0;
        }
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    return elfCalories;
  }

  private static class SortByHighestValueComparator implements Comparator<Integer> {

    @Override
    public int compare(Integer o1, Integer o2) {
      if (Objects.equals(o1, o2)) {
        return 0;
      }
      return o1 > o2 ? -1 : 1;
    }
  }

  public static void main(String[] args) {
    new Day1();
  }

}
