package dev.crystall.days;

import dev.crystall.utils.FileUtils;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by CrystallDEV on 05/12/2022
 */
@Slf4j
public class Day5 {

  private final List<Stack> stackPartOne = new ArrayList<>();
  private final List<Stack> stackPartTwo = new ArrayList<>();

  public Day5() {
    try (BufferedReader reader = FileUtils.readFileBuffered("input_day5.txt")) {
      boolean finishedInitialSetup = false;
      String line;
      while ((line = reader.readLine()) != null) {
        // After the initial setup we have an empty line which indicates us the end of the setup
        if (line.isEmpty() && !finishedInitialSetup) {
          finishedInitialSetup = true;
          continue;
        }

        // Check if we finished the initial setup and if the line contains a bracket, since we don't want to add the numbers as crates
        if (!finishedInitialSetup) {
          if (line.contains("[")) {
            List<String> crates = getCratesForLine(line);
            addCratesToStacks(stackPartOne, crates);
            addCratesToStacks(stackPartTwo, crates);
          }
          continue;
        }

        // go through all turns and move each crates in single steps between the stacks
        var turns = getTurnsForLine(line);
        executeTurnCrateMover9000(turns);
        executeTurnCrateMover9001(turns);
      }

      // Print the final results
      printFinalStacks(stackPartOne);
      printFinalStacks(stackPartTwo);

    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private void printFinalStacks(List<Stack> finalStacks) {
    StringBuilder stringBuilder = new StringBuilder();
    for (Stack s : finalStacks) {
      stringBuilder.append(s.removeCrate(), 1, 2);
    }
    log.info(stringBuilder.toString());
  }

  private void executeTurnCrateMover9000(List<Integer> turns) {
    var amount = turns.get(0);
    var from = stackPartOne.get(turns.get(1) - 1);
    var to = stackPartOne.get(turns.get(2) - 1);

    for (int i = 0; i < amount; i++) {
      var crate = from.removeCrate();
      to.addCrateOnTop(crate);
    }
  }

  private void executeTurnCrateMover9001(List<Integer> turns) {
    var amount = turns.get(0);
    var from = stackPartTwo.get(turns.get(1) - 1);
    var to = stackPartTwo.get(turns.get(2) - 1);

    List<String> cratesToMove = new ArrayList<>();
    for (int i = 0; i < amount; i++) {
      var crate = from.removeCrate();
      cratesToMove.add(0, crate);
    }
    for (String crate : cratesToMove) {
      to.addCrateOnTop(crate);
    }
  }

  private void addCratesToStacks(List<Stack> stacks, List<String> crates) {
    // If this is the first time adding crates to a stack, init the array
    if (stacks.isEmpty()) {
      initStacksArray(stacks, crates.size());
    }

    for (int i = 0; i < crates.size(); i++) {
      String crate = crates.get(i);
      if (crate.isBlank()) {
        continue;
      }
      stacks.get(i).addCrateOnBottom(crate);
    }
  }

  private void initStacksArray(List<Stack> stacks, int size) {
    for (int i = 0; i < size; i++) {
      stacks.add(new Stack());
    }
  }

  private List<String> getCratesForLine(String line) {
    List<String> crates = new ArrayList<>();
    for (int i = 0; i < line.length(); i += 4) {
      var crate = line.substring(i, i + 2);
      crates.add(crate);
    }
    return crates;
  }

  private List<Integer> getTurnsForLine(String line) {
    List<Integer> turns = new ArrayList<>();
    for (String s : line.split(" ")) {
      try {
        // If we can parse the string as a number, then we have a turn, otherwise fail silently
        var turn = Integer.parseInt(s);
        turns.add(turn);
      } catch (NumberFormatException ignored) {
      }
    }
    return turns;
  }

  @AllArgsConstructor
  private static class Stack {

    private final List<String> crates = new ArrayList<>();

    public void addCrateOnTop(String crate) {
      crates.add(crate);
    }

    public void addCrateOnBottom(String crate) {
      crates.add(0, crate);
    }

    public String removeCrate() {
      return crates.remove(crates.size() - 1);
    }
  }

  public static void main(String[] args) {
    new Day5();
  }

}
