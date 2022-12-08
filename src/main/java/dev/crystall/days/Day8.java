package dev.crystall.days;

import dev.crystall.utils.FileUtils;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by CrystallDEV on 07/12/2022
 */
@Slf4j
public class Day8 {

  private final List<List<Integer>> rows = new ArrayList<>();

  public Day8() {
    try (BufferedReader reader = FileUtils.readFileBuffered("input_day8.txt")) {
      String line;

      while ((line = reader.readLine()) != null) {
        rows.add(handleRow(line));
      }

      handlePartOne();
      handlePartTwo();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private List<Integer> handleRow(String line) {
    var row = new ArrayList<Integer>();
    for (char c : line.toCharArray()) {
      row.add(Integer.parseInt(String.valueOf(c)));
    }
    return row;
  }

  private void handlePartOne() {
    int visibleTrees = 0;
    StringBuilder mapBuilder = new StringBuilder(System.lineSeparator());
    for (int y = 0; y < rows.size(); y++) {
      List<Integer> row = rows.get(y);
      for (int x = 0; x < row.size(); x++) {
        // the edges are always visible
        if (x == 0 || x == row.size() - 1 || y == 0 || y == rows.size() - 1) {
          visibleTrees++;
          mapBuilder.append("+");
          continue;
        }

        // Go in all directions and check if the tree is visible / higher than all trees on the way to the edge
        if (isTreeVisible(x, y)) {
          visibleTrees++;
          mapBuilder.append("+");
          continue;
        }
        mapBuilder.append("-");
      }
      mapBuilder.append(System.lineSeparator());
    }
    log.info(mapBuilder.toString());
    log.info("Part 1: {}", visibleTrees);
  }

  private void handlePartTwo() {
    var highestScenicScore = 0;
    for (int y = 0; y < rows.size(); y++) {
      List<Integer> row = rows.get(y);
      for (int x = 0; x < row.size(); x++) {
        var scenicScore = calculateScenicScore(x, y);
        highestScenicScore = Math.max(scenicScore, highestScenicScore);
      }
    }
    log.info("Part 2: {}", highestScenicScore);
  }

  private boolean isTreeVisible(int x, int y) {
    var currentTreeHeight = getTreeHeightForXY(x, y);

    return (isTreeVisibleInDirection(currentTreeHeight, x, y, -1, 0) || // Check left
      isTreeVisibleInDirection(currentTreeHeight, x, y, 1, 0) || // Check right
      isTreeVisibleInDirection(currentTreeHeight, x, y, 0, -1) || // Check up
      isTreeVisibleInDirection(currentTreeHeight, x, y, 0, 1)); // Check down
  }

  private boolean isTreeVisibleInDirection(int currentTreeHeight, int x, int y, int dx, int dy) {
    for (int newX = x + dx, newY = y + dy; isValidCoordinate(newX, newY); newX += dx, newY += dy) {
      if (!compareTrees(currentTreeHeight, newX, newY)) {
        return false;
      }
    }
    return true;
  }

  private int getVisibleTreesInDirection(int currentTreeHeight, int x, int y, int dx, int dy) {
    int totalVisibleTrees = 0;
    for (int newX = x + dx, newY = y + dy; isValidCoordinate(newX, newY); newX += dx, newY += dy) {
      totalVisibleTrees++;
      if (!compareTrees(currentTreeHeight, newX, newY)) {
        break;
      }
    }
    return totalVisibleTrees;
  }

  private boolean isValidCoordinate(int x, int y) {
    return y >= 0 && y < rows.size() && x >= 0 && x < rows.get(y).size();
  }

  private boolean compareTrees(int height, int x, int y) {
    return getTreeHeightForXY(x, y) < height;
  }

  private int calculateScenicScore(int x, int y) {
    var currentTreeHeight = getTreeHeightForXY(x, y);

    var scenicLeft = getVisibleTreesInDirection(currentTreeHeight, x, y, -1, 0);
    var scenicRight = getVisibleTreesInDirection(currentTreeHeight, x, y, 1, 0);
    var scenicBottom = getVisibleTreesInDirection(currentTreeHeight, x, y, 0, -1);
    var scenicTop = getVisibleTreesInDirection(currentTreeHeight, x, y, 0, 1);

    return scenicLeft * scenicRight * scenicBottom * scenicTop;
  }

  private int getTreeHeightForXY(int x, int y) {
    return rows.get(y).get(x);
  }

  public static void main(String[] args) {
    new Day8();
  }

}
