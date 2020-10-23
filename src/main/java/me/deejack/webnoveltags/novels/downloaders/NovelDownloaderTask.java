package me.deejack.webnoveltags.novels.downloaders;

import com.google.gson.GsonBuilder;
import me.deejack.webnoveltags.config.Configuration;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;

public class NovelDownloaderTask {

  public void start(DownloadRequested downloadRequested) {
    final AtomicBoolean finished = new AtomicBoolean(false);
    var novelsDownloader = new NovelsDownloader();
    CompletableFuture<?> futureNovels;

    switch (downloadRequested) {
      case ALL -> futureNovels = novelsDownloader.downloadAll();
      case NEW_ONLY -> futureNovels = novelsDownloader.downloadNewOnly();
      case LIST_ONLY -> futureNovels = novelsDownloader.reloadList();
      default -> throw new IllegalArgumentException("Wtf, it's an enum bro");
    }

    var loadingFuture = CompletableFuture.supplyAsync(() -> {
      try {
        while (!finished.get()) {
          System.out.print("-");
          Thread.sleep(100);
        }
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      return null;
    });

    futureNovels.thenApply(result -> {
      saveToFile(result);
      finished.set(true);
      System.out.println("FINISHED");
      return null;
    });

    CompletableFuture<Void> combinedFuture = CompletableFuture.allOf(futureNovels, loadingFuture);
    try {
      combinedFuture.join();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static void saveToFile(Object result, Path path) {
    var json = new GsonBuilder().setPrettyPrinting().create().toJson(result);
    try {
      Files.writeString(path, json);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static void saveToFile(Object result) {
    saveToFile(result, Configuration.getFileInCurrentPath("webnovels.json"));
  }
}
