package dev.crystall.days;

import dev.crystall.utils.FileUtils;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by CrystallDEV on 02/12/2022
 */
@Slf4j
public class Day2 {

  public Day2() {
    String line;
    try (BufferedReader reader = FileUtils.readFileBuffered("input_day2.txt")) {
      var totalScore = 0;
      var totalRealScore = 0;
      while ((line = reader.readLine()) != null) {
        totalScore += calculateScoreForLine(line);
        totalRealScore += calculateRealScoreForLine(line);
      }

      log.info("Total score: {}", totalScore);
      log.info("Total real score: {}", totalRealScore);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private int calculateRealScoreForLine(String line) {
    String[] turns = line.split(" ");
    PlayResponse enemyTurn = PlayResponse.of(turns[0]);
    GameResult expectedResult = GameResult.of(turns[1]);

    // Go through all possible answers and check if the response was found for the expected GameResult
    PlayResponse myTurn = null;
    for (PlayResponse possibleReponse : PlayResponse.values()) {
      if (checkResult(enemyTurn, possibleReponse) == expectedResult) {
        myTurn = possibleReponse;
        break;
      }
    }
    if(myTurn == null){
      throw new RuntimeException("Unable to find matching PlayResponse for expected GameResult");
    }
    return calculateScoreForResponses(enemyTurn, myTurn);
  }

  private int calculateScoreForLine(String line) {
    String[] turns = line.split(" ");
    PlayResponse enemyTurn = PlayResponse.of(turns[0]);
    PlayResponse myTurn = PlayResponse.of(turns[1]);
    return calculateScoreForResponses(enemyTurn, myTurn);
  }

  private int calculateScoreForResponses(PlayResponse enemyTurn, PlayResponse myTurn) {
    var responseWorth = myTurn.reponseWorth;
    log.info("Response worth: {}", responseWorth);
    var score = checkResult(enemyTurn, myTurn).score;
    log.info("Result score: {}", score);
    return score + responseWorth;
  }

  private GameResult checkResult(PlayResponse enemyTurn, PlayResponse myTurn) {
    if (enemyTurn.equals(myTurn)) {
      return GameResult.DRAW;
    }
    return switch (enemyTurn) {
      case ROCK -> myTurn == PlayResponse.PAPER ? GameResult.WIN : GameResult.LOSS;
      case PAPER -> myTurn == PlayResponse.SCISSORS ? GameResult.WIN : GameResult.LOSS;
      case SCISSORS -> myTurn == PlayResponse.ROCK ? GameResult.WIN : GameResult.LOSS;
    };
  }

  @Getter
  @AllArgsConstructor
  enum PlayResponse {
    ROCK(List.of("A", "X"), 1),
    PAPER(List.of("B", "Y"), 2),
    SCISSORS(List.of("C", "Z"), 3);

    private final List<String> inputNames;
    private final int reponseWorth;

    public static PlayResponse of(String s) {
      return Arrays.stream(values()).filter(playResponse -> playResponse.inputNames.contains(s)).findFirst().get();
    }
  }

  @Getter
  @AllArgsConstructor
  enum GameResult {
    WIN("Z", 6),
    LOSS("X", 0),
    DRAW("Y", 3);

    private final String inputName;
    private final int score;

    public static GameResult of(String s) {
      return Arrays.stream(values()).filter(playResponse -> playResponse.inputName.equals(s)).findFirst().get();
    }
  }

  public static void main(String[] args) {
    new Day2();
  }

}
