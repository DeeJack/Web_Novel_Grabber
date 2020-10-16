package me.deejack.webnoveltags.tags;

import com.google.gson.GsonBuilder;
import me.deejack.webnoveltags.config.Configuration;
import me.deejack.webnoveltags.models.json.Tags;
import me.deejack.webnoveltags.models.json.categories.Category;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;

public class TagsDownloaderTask {

  private CompletableFuture<List<Category>> getCategories() {
    var categories = new LinkedList<Category>();
    Document document = null;
    try {
      document = Jsoup.connect(Configuration.BASE_URL + "/category/0_novel_page1?gender=1").get();
    } catch (IOException e) {
      e.printStackTrace();
      return CompletableFuture.completedFuture(Collections.emptyList());
    }
    var elements = document.select("div.filter-wrap > a");
    for (var element : elements) {
      var id = element.attr("href").split("/")[2].split("_")[0];
      var category = new Category(element.text(), Integer.parseInt(id));
      categories.add(category);
    }
    return CompletableFuture.completedFuture(categories);
  }

  public void start() {
    final AtomicBoolean finished = new AtomicBoolean(false);
    var tagsDownloader = new TagsDownloader();
    var futureTags = tagsDownloader.downloadAllTags();
    var futureCategories = getCategories();

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

    futureTags.thenApplyAsync((result) -> {
      var categories = futureCategories.join();
      return new Tags(categories, result);
    }).thenApply(result -> {
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

    CompletableFuture<Void> combinedFuture = CompletableFuture.allOf(futureTags, futureCategories, loadingFuture);
    try {
      combinedFuture.join();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
