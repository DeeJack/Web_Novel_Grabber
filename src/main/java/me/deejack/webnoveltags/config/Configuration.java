package me.deejack.webnoveltags.config;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class Configuration {
  public static final String BASE_URL;
  public static final int TIMEOUT;

  static {
    System.out.print("Insert the URL (no https://www needed): ");
    var scanner = new Scanner(System.in);
    var url = scanner.nextLine();
    BASE_URL = "https://www." + url;
    System.out.print("Insert the timeout: ");
    TIMEOUT = scanner.nextInt();
  }

  public static Path getFileInCurrentPath(String fileName) {
    return Paths.get(Paths.get("").toAbsolutePath() + File.separator + fileName);
  }
}
