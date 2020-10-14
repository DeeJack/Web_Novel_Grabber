package me.deejack.webnoveltags.novels.downloaders;

import com.google.gson.Gson;
import me.deejack.webnoveltags.config.Configuration;
import me.deejack.webnoveltags.models.json.novels.JsonNovel;
import me.deejack.webnoveltags.models.json.novels.NovelResponse;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class NovelsDownloader {
  private static final String LIST_URL = Configuration.BASE_URL + "/apiajax/category/categoryAjax?_csrfToken=%s&orderBy=1&pageIndex=%d&categoryId=&gender=-1&categoryType=1&translateMode=0";
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


  public CompletableFuture<List<JsonNovel>> reloadList() {
    CompletableFuture<List<JsonNovel>> future = CompletableFuture.supplyAsync(() -> {
      List<JsonNovel> fullList = new LinkedList<>();
      List<JsonNovel> lastList;
      int page = 1;
      do {
        lastList = getListByPage(page);
        page++;
        if (lastList != null)
          fullList.addAll(lastList);
      } while (lastList != null);
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

  public CompletableFuture downloadNewOnly() {
    return CompletableFuture.completedFuture(null);
  }

  public CompletableFuture downloadAll() {
    return CompletableFuture.completedFuture(null);
  }
}
