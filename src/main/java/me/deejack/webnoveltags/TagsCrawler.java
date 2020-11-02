package me.deejack.webnoveltags;

import me.deejack.webnoveltags.novels.downloaders.DownloadRequested;
import me.deejack.webnoveltags.novels.downloaders.NovelDownloaderTask;
import me.deejack.webnoveltags.tags.TagsDownloaderTask;

import java.util.Arrays;
import java.util.Scanner;

public class TagsCrawler {
  public static void main(String[] args) {
    var output = new String[]{
            "What task do you want to execute? ",
            "1) Download tags and categories",
            "2) Download novels"

    };
    var choice = createMenu(output, '1', '2');

    switch (choice) {
      case '1' -> startTagsDownloader();
      case '2' -> startNovelDownload();
    }
  }

  private static void startNovelDownload() {
    var output = new String[]{
            "------ Novel ------- ",
            "1) Reload list",
            "2) Download novel details"
    };
    var choice = createMenu(output, '1', '2');
    switch (choice) {
      case '1' -> new NovelDownloaderTask().start(DownloadRequested.LIST_ONLY);
      case '2' -> chooseNovelDetails();
    }
  }

  private static void chooseNovelDetails() {
    var output = new String[]{
            "------ Get Novel Details ------- ",
            "1) New only",
            "2) All",
            "3) Download 500 novels"
    };
    var choice = createMenu(output, '1', '2', '3');
    switch (choice) {
      case '1' -> new NovelDownloaderTask().start(DownloadRequested.NEW_ONLY);
      case '2' -> new NovelDownloaderTask().start(DownloadRequested.ALL);
      case '3' -> {
        int from = -1;
        boolean ok;
        var scanner = new Scanner(System.in);
        do {
          System.out.print("From novel n.: ");
          ok = scanner.hasNextInt();
          if (!ok) {
            System.out.println("Insert an int!");
            scanner.nextLine();
          } else {
            ok = true;
            from = scanner.nextInt();
          }
        } while (!ok);
        new NovelDownloaderTask().start(DownloadRequested.DOWNLOAD_500, from);
      }
    }
  }

  private static void startTagsDownloader() {
    new TagsDownloaderTask().start();
  }

  private static char createMenu(String[] lines, char... acceptedCharacters) {
    var scanner = new Scanner(System.in);
    var acceptedString = new String(acceptedCharacters);
    String input;
    do {
      Arrays.asList(lines).forEach(System.out::println);
      System.out.print("==> ");

      input = scanner.nextLine().trim().toLowerCase();
    } while (input.length() == 0 || (!acceptedString.contains(input.charAt(0) + "")));
    return input.charAt(0);
  }
}
