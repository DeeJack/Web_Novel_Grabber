package me.deejack.webnoveltags.novels.downloaders;

import com.google.gson.Gson;
import me.deejack.webnoveltags.config.Configuration;
import me.deejack.webnoveltags.models.json.novels.*;
import me.deejack.webnoveltags.novels.NovelDatabase;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class NovelsDownloader {
  private static final String LIST_URL = Configuration.BASE_URL + "/apiajax/category/categoryAjax?_csrfToken=%s&orderBy=1&pageIndex=%d&categoryId=&gender=-1&categoryType=1&translateMode=0";
  private static final String NOVEL_DETAILS_URL = Configuration.BASE_URL + "/apiajax/chapter/GetChapterList?_csrfToken=%s&bookId=%d";
  private final String csrfToken;

  {
    String tempToken;
    try {
      var connection = Jsoup.connect(Configuration.BASE_URL + "/category/0_novel_page1?gender=1").execute();
      tempToken = connection.cookie("_csrfToken");
      if (tempToken.trim().length() == 0) {
        System.out.println("Failed to get token!");
      }
    } catch (IOException e) {
      tempToken = "";
      e.printStackTrace();
    }
    this.csrfToken = tempToken;
  }


  public CompletableFuture<List<SerializableNovel>> reloadList() {
    CompletableFuture<List<SerializableNovel>> future = CompletableFuture.supplyAsync(() -> {
      List<SerializableNovel> fullList = new LinkedList<>();
      List<JsonNovel> lastList;
      int page = 1;
      do {
        lastList = getListByPage(page);
        page++;
        if (lastList != null)
          fullList.addAll(lastList.stream().map(JsonNovel::toSerializableNovel).collect(Collectors.toList()));
      } while (lastList != null && page < 3);
      NovelDatabase.novels = fullList;
      return fullList;
    });
    return future;
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
    CompletableFuture<List<SerializableNovel>> future = CompletableFuture.supplyAsync(() -> {
      var currentList = NovelDatabase.novels;
      currentList.stream().filter(novel -> novel.getDetails() == null).forEach(novel -> {
        try {
          var connection = Jsoup.connect(String.format(NOVEL_DETAILS_URL, csrfToken, novel.getId())).timeout(10 * 1000).execute();
          var json = connection.body();
          var details = new Gson().fromJson(json, NovelDetailsResponse.class);
          novel.setDetails(details.getData().getDetails());
        } catch (IOException e) {
          e.printStackTrace();
          System.out.println("Failed fetch of novel details for book with id: " + novel.getId());
        }
      });
      return currentList;
    });
    return future;
  }

  public CompletableFuture downloadAll() {
    return CompletableFuture.completedFuture(null);
  }
}
