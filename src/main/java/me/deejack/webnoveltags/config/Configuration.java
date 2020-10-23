package me.deejack.webnoveltags.config;

import me.deejack.webnoveltags.TagsCrawler;

import java.io.File;
import java.net.URISyntaxException;
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
    try {
      return Paths.get(new File(TagsCrawler.class.getProtectionDomain().getCodeSource().getLocation()
              .toURI()).getParent() + File.separator + fileName);
    } catch (URISyntaxException e) {
      e.printStackTrace();
      return Path.of(System.getProperty("user.home") + File.separator + fileName);
    }
  }
}
