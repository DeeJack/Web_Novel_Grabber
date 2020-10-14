package me.deejack.webnoveltags.novels.downloaders;

import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;

public class NovelDownloaderTask {

    public static void main(String[] args) {
        new NovelDownloaderTask().start(DownloadRequested.LIST_ONLY);
    }

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
            var json = new GsonBuilder().setPrettyPrinting().create().toJson(result);
            try {
                Files.writeString(Paths.get(Paths.get("").toAbsolutePath() + File.separator + "webnovels.json"), json, StandardOpenOption.CREATE, StandardOpenOption.WRITE);
            } catch (IOException e) {
                e.printStackTrace();
            }
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
}
