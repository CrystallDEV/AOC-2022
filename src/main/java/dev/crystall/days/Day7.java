package dev.crystall.days;

import dev.crystall.utils.FileUtils;
import dev.crystall.utils.StringUtils;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * Created by CrystallDEV on 07/12/2022
 */
@Slf4j
public class Day7 {

  private static final int PART_ONE_MAX_DELETE_THRESHOLD = 100_000;
  private static final int TOTAL_FILESYSTEM_SIZE = 70_000_000;
  private static final int UPDATE_SIZE = 30_000_000;

  private final Directory rootDirectory = Directory.builder().name("/").build();
  private Directory currentDirectory = rootDirectory;

  public Day7() {
    try (BufferedReader reader = FileUtils.readFileBuffered("input_day7.txt")) {
      // Skip the first line to manually init the root directory
      reader.readLine();

      String line;
      while ((line = reader.readLine()) != null) {
        String[] s = line.split(" ");
        // The first character/string of a command line is always a "$"
        if (isCommand(s[0])) {
          Command cmd = Command.of(s[1]);
          log.info("{} -- {}", line, cmd);
          handleCommand(cmd, s.length == 3 ? s[2] : null);
        } else {
          handleContent(s);
        }
      }
      handlePartOne();
      handlePartTwo();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private void handlePartOne() {
    HashMap<Directory, Integer> deletableDirectories = new HashMap<>();
    CheckDirectory(deletableDirectories, rootDirectory);
    deletableDirectories.entrySet().removeIf(directoryIntegerEntry -> directoryIntegerEntry.getValue() >= PART_ONE_MAX_DELETE_THRESHOLD);
    var totalSize = 0;
    for (Entry<Directory, Integer> entry : deletableDirectories.entrySet()) {
      Directory directory = entry.getKey();
      Integer size = entry.getValue();
      totalSize += size;
      log.info("DD: {} - {}", directory.getName(), size);
    }
    log.info("Part 1: {}", totalSize);
  }

  private void handlePartTwo() {
    HashMap<Directory, Integer> deletableDirectories = new HashMap<>();
    CheckDirectory(deletableDirectories, rootDirectory);

    // Calculate the space we need
    var freeDiskSpace = TOTAL_FILESYSTEM_SIZE - deletableDirectories.get(rootDirectory);
    var neededSpace = UPDATE_SIZE - freeDiskSpace;

    // Remove all directories that are too small
    deletableDirectories.entrySet().removeIf(directoryIntegerEntry -> directoryIntegerEntry.getValue() < neededSpace);
    var directories = deletableDirectories.entrySet().stream().sorted((o1, o2) -> o1.getValue() < o2.getValue() ? -1 : 1).toList();
    directories.forEach(directoryIntegerEntry -> log.info("{} - {}", directoryIntegerEntry.getKey().getName(), directoryIntegerEntry.getValue()));

    // Get the smallest directory so we know which one to delete
    var smallestDirectory = directories.get(0);
    log.info("Part 2: {} - {}", smallestDirectory.getKey().getName(), smallestDirectory.getValue());
  }

  /**
   * Recursively checks a directory for the total size is ready to be deleted
   *
   * @param directories the map to save the objects in
   * @param directory the initial directory to recursively go through
   */
  private void CheckDirectory(Map<Directory, Integer> directories, Directory directory) {
    var directorySize = getSizeOfDirectory(directory);
    directories.put(directory, directorySize);
    for (Directory dir : directory.getDirectories()) {
      CheckDirectory(directories, dir);
    }
  }

  /**
   * Gets the total size of all files and sub directories for the given directory
   *
   * @param directory
   * @return
   */
  private int getSizeOfDirectory(Directory directory) {
    int size = directory.getTotalSizeOfFiles();
    for (Directory dir : directory.getDirectories()) {
      size += getSizeOfDirectory(dir);
    }
    return size;
  }

  private void changeDirectory(String name) {
    Directory changeInto;
    if (name.equals("..")) {
      changeInto = currentDirectory.parent;
    } else {
      changeInto = currentDirectory.directories.stream().filter(directory -> directory.name.equals(name)).findFirst().orElse(null);
    }

    if (changeInto != null) {
      currentDirectory = changeInto;
      log.info("Changed directory to {}", currentDirectory.name);
      return;
    }

    log.error("Unable to find child directory by name {}", currentDirectory.name);
  }

  private void handleCommand(Command cmd, String... args) {
    switch (cmd) {
      case LIST_DIRECTORY -> log.info("Listing directory");// do nothing;
      case CHANGE_DIRECTORY -> changeDirectory(args[0]);
    }
  }

  private void handleContent(String[] s) {
    if (s[0].equals("dir")) {
      addDirectory(s[1]);
    } else if (StringUtils.isNumber(s[0])) {
      addFile(s[1], Integer.parseInt(s[0]));
    }
  }

  private void addDirectory(String name) {
    var childDirectory = currentDirectory.directories.stream().filter(directory -> directory.name.equals(name)).findFirst().orElse(null);

    if (childDirectory != null) {
      log.error("Directory already contains a child directory with the name {}", name);
      return;
    }

    var newDir = Directory.builder().name(name).parent(currentDirectory).build();
    currentDirectory.directories.add(newDir);
  }

  private void addFile(String fileName, int fileSize) {
    var childFile = currentDirectory.files.stream().filter(file -> file.getFileName().equals(fileName)).findFirst().orElse(null);

    if (childFile != null) {
      log.error("Directory already contains a file with the name {}", fileName);
      return;
    }

    var newFile = new File(currentDirectory, fileName, fileSize);
    currentDirectory.files.add(newFile);
  }

  private boolean isCommand(String line) {
    return line.contains("$");
  }

  @Getter
  @AllArgsConstructor
  enum Command {
    LIST_DIRECTORY("ls"),
    CHANGE_DIRECTORY("cd"),
    ADD_DIRECTORY("dir");

    private final String terminalInput;

    public static Command of(String s) {
      return Arrays.stream(values()).filter(command -> command.terminalInput.equals(s)).findFirst().get();
    }
  }

  @Getter
  @Builder
  private static class Directory {

    /**
     * The name of the directory
     */
    private String name;

    /**
     * The parent directory if one exists.
     */
    private Directory parent;

    /**
     * List of directories that are a child fo this directory
     */
    @Builder.Default
    private List<Directory> directories = new ArrayList<>();

    /**
     * List of files that are in this directory
     */
    @Builder.Default
    private List<File> files = new ArrayList<>();

    public int getTotalSizeOfFiles() {
      return files.stream().mapToInt(file -> file.size).sum();
    }

  }

  @Getter
  @AllArgsConstructor
  private static class File {

    /**
     * The directory this file is in
     */
    private Directory parent;

    String fileName;

    int size;

  }

  public static void main(String[] args) {
    new Day7();
  }

}
