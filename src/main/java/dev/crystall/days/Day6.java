package dev.crystall.days;

import dev.crystall.utils.FileUtils;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by CrystallDEV on 06/12/2022
 */
@Slf4j
public class Day6 {

  public Day6() {
    var input = FileUtils.getResourceFileAsString("input_day6.txt");

    var packetMarkerIndex = getMarkerAfterXDifferentCharacters(input, 4);
    var messageMarkerIndex = getMarkerAfterXDifferentCharacters(input, 14);

    log.info("Part 1: {}", packetMarkerIndex);
    log.info("Part 2: {}", messageMarkerIndex);
  }

  public int getMarkerAfterXDifferentCharacters(String input, int differentCharactersNeeded) {
    var stringBuilder = new StringBuilder();
    var markerIndex = -1;
    for (int i = 0; i < input.length(); i++) {
      char c = input.charAt(i);
      // Check if we have a marker already
      if (stringBuilder.length() >= differentCharactersNeeded && !hasDuplicateCharacter(stringBuilder.toString())) {
        markerIndex = i;
        break;
      }
      // We only want to have the last X characters
      if (stringBuilder.length() >= differentCharactersNeeded) {
        stringBuilder.deleteCharAt(0);
      }
      // Add the character to the string builder
      stringBuilder.append(c);
    }

    log.info("Received marker after {} characters ({})", markerIndex, stringBuilder);

    return markerIndex;
  }

  private boolean hasDuplicateCharacter(String s) {
    List<Character> characterList = new ArrayList<>();
    for (char c : s.toCharArray()) {
      if (characterList.contains(c)) {
        return true;
      }
      characterList.add(c);
    }
    return false;
  }

  public static void main(String[] args) {
    new Day6();
  }
}
