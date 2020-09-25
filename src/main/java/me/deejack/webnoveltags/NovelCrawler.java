package me.deejack.webnoveltags;

import com.google.gson.GsonBuilder;
import me.deejack.webnoveltags.tags.TagsDownloader;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;

public class NovelCrawler {
    public static void main(String[] args) {
        final AtomicBoolean finished = new AtomicBoolean(false);
        var tagsDownloader = new TagsDownloader();
        var future = tagsDownloader.downloadAllTags();
        future.thenApply(result -> {
            var json = new GsonBuilder().setPrettyPrinting().create().toJson(result);
            try {
                Files.writeString(Paths.get(Paths.get("").toAbsolutePath() + File.separator + "webnoveltags.json"), json, StandardOpenOption.CREATE, StandardOpenOption.WRITE);
            } catch (IOException e) {
                e.printStackTrace();
            }
            finished.set(true);
            System.out.println("FINISHED");
            return null;
        });

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

        CompletableFuture<Void> combinedFuture = CompletableFuture.allOf(future, loadingFuture);
        try {
            combinedFuture.join();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
