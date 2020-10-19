package me.deejack.webnoveltags.novels.downloaders;

import com.google.gson.Gson;
import me.deejack.webnoveltags.config.Configuration;
import me.deejack.webnoveltags.models.json.novels.JsonNovel;
import me.deejack.webnoveltags.models.json.novels.NovelDetailsResponse;
import me.deejack.webnoveltags.models.json.novels.NovelResponse;
import me.deejack.webnoveltags.models.json.novels.SerializableNovel;
import me.deejack.webnoveltags.novels.NovelDatabase;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class NovelsDownloader {
  private static final String LIST_URL = Configuration.BASE_URL + "/apiajax/category/categoryAjax?_csrfToken=%s&orderBy=1&pageIndex=%d&categoryId=&gender=-1&categoryType=1&translateMode=0";
  private static final String NOVEL_DETAILS_URL = Configuration.BASE_URL + "/apiajax/chapter/GetChapterList?_csrfToken=%s&bookId=%d";
  private final String csrfToken;

  {
    String tempToken;
    try {
      var connection = Jsoup.connect(Configuration.BASE_URL + "/category/0_novel_page1?gender=1").execute();
      tempToken = connection.cookie("_csrfToken");
      if (tempToken.trim().length() == 0)
        System.out.println("Failed to get token!");
    } catch (IOException e) {
      tempToken = "";
      e.printStackTrace();
    }
    this.csrfToken = tempToken;
  }

  public CompletableFuture<List<SerializableNovel>> reloadList() {
    return CompletableFuture.supplyAsync(() -> {
      List<SerializableNovel> fullList = new LinkedList<>();
      List<JsonNovel> lastList;
      int page = 1;
      do {
        lastList = getListByPage(page);
        page++;
        if (lastList != null)
          fullList.addAll(lastList.stream().map(JsonNovel::toSerializableNovel).collect(Collectors.toList()));

        try {
          Thread.sleep(Configuration.TIMEOUT); // Wait some time to make it less... suspicious?
        } catch (InterruptedException e) {
          e.printStackTrace();
        }

      } while (lastList != null);
      NovelDatabase.novels = fullList;
      return fullList;
    });
  }

  private List<JsonNovel> getListByPage(int page) {
    try {
      var connection = Jsoup.connect(String.format(LIST_URL, csrfToken, page)).timeout(10 * 1000).execute();
      var json = connection.body();
      var novelsResponse = new Gson().fromJson(json, NovelResponse.class);
      return novelsResponse.getData().getNovels();
    } catch (IOException e) {
      e.printStackTrace();
      return null;
    }
  }

  public CompletableFuture<List<SerializableNovel>> downloadNewOnly() {
    return CompletableFuture.supplyAsync(() -> {
      var currentList = NovelDatabase.novels;
      downloadDetails(currentList.stream().filter(novel -> novel.getDetails() == null));
      return currentList;
    });
  }

  private void downloadDetails(Stream<SerializableNovel> novels) {
    novels.forEach(novel -> {
      try {
        var connection = Jsoup.connect(String.format(NOVEL_DETAILS_URL, csrfToken, novel.getId())).timeout(10 * 1000).execute();
        var json = connection.body();
        var details = new Gson().fromJson(json, NovelDetailsResponse.class);
        novel.setDetails(details.getData().getDetails());

        try {
          Thread.sleep(Configuration.TIMEOUT); // Wait some time to make it less... suspicious?
        } catch (InterruptedException e) {
          e.printStackTrace();
        }

      } catch (IOException e) {
        e.printStackTrace();
        System.out.println("Failed fetch of novel details for book with id: " + novel.getId());
      }
    });
  }

  public CompletableFuture<List<SerializableNovel>> downloadAll() {
    return CompletableFuture.supplyAsync(() -> {
      var currentList = NovelDatabase.novels;
      downloadDetails(currentList.stream());
      return currentList;
    });
  }
}
