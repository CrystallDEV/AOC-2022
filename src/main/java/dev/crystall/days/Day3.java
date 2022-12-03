package dev.crystall.days;

import dev.crystall.utils.FileUtils;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by CrystallDEV on 03/12/2022
 */
@Slf4j
public class Day3 {

  private static final String POSSIBLE_ITEMS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

  public Day3() {
    String line;
    try (BufferedReader reader = FileUtils.readFileBuffered("input_day3.txt")) {

      var prioritySum = 0;
      var groupPrioritySum = 0;
      List<String> group = new ArrayList<>();
      while ((line = reader.readLine()) != null) {
        String[] compartments = getCompartmentsFromLine(line);
        String leftCompartment = compartments[0];
        String rightCompartment = compartments[1];
        log.info("{} - {}", leftCompartment, rightCompartment);

        // Part one
        prioritySum += collectPriorityForRucksack(leftCompartment, rightCompartment);

        // Part two
        group.add(line);
        if (group.size() == 3) {
          groupPrioritySum += collectPriorityForBadge(group);
        }
      }
      log.info("Part1: {}", prioritySum);
      log.info("Part2: {}", groupPrioritySum);

    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private int collectPriorityForBadge(List<String> group) {
    // Go through all characters of the first rucksack
    for (char c : group.remove(0).toCharArray()) {
      // Check if any of the chars is present in any of the other rucksacks
      if (group.get(0).indexOf(c) != -1 && group.get(1).indexOf(c) != -1) {
        var priority = getPriorityForItem(c);
        log.info("{} is all the groups rucksack and adds {} priority", c, priority);
        group.clear();
        return priority;
      }
    }
    group.clear();
    return 0;
  }

  private int collectPriorityForRucksack(String leftCompartment, String rightCompartment) {
    for (char c : leftCompartment.toCharArray()) {
      if (rightCompartment.indexOf(c) != -1) {
        var priority = getPriorityForItem(c);
        log.info("{} is in both compartments and adds {} priority", c, priority);
        return priority;
      }
    }
    return 0;
  }

  private String[] getCompartmentsFromLine(String line) {
    int middleIndex = line.length() / 2;
    return new String[]{line.substring(0, middleIndex), line.substring(middleIndex)};
  }

  private int getPriorityForItem(char item) {
    return POSSIBLE_ITEMS.indexOf(item) + 1;
  }

  public static void main(String[] args) {
    new Day3();
  }

}
