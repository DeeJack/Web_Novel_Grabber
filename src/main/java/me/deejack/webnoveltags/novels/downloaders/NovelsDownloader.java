package me.deejack.webnoveltags.novels.downloaders;

import com.google.gson.Gson;
import me.deejack.webnoveltags.config.Configuration;
import me.deejack.webnoveltags.models.json.novels.JsonNovel;
import me.deejack.webnoveltags.models.json.novels.NovelResponse;
import me.deejack.webnoveltags.models.json.novels.SerializableNovel;
import me.deejack.webnoveltags.models.json.novels.details.*;
import me.deejack.webnoveltags.novels.NovelDatabase;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class NovelsDownloader {
  private static final String LIST_URL = Configuration.BASE_URL + "/apiajax/category/categoryAjax?_csrfToken=%s&orderBy=1&pageIndex=%d&categoryId=&gender=-1&categoryType=1&translateMode=0";
  private static final String CHAPTERS_INFO_URL = Configuration.BASE_URL + "/apiajax/chapter/GetChapterList?_csrfToken=%s&bookId=%s";
  private static final String RANK_INFO_URL = Configuration.BASE_URL + "/apiajax/powerStone/getRankInfoAjax?_csrfToken=%s&bookId=%s";
  private static final String GIFTS_URL = Configuration.BASE_URL + "/apiajax/gift/getGiftInfo?_csrfToken=%s&bookId=%s&novelType=0";
  private static final String REVIEWS_URL = Configuration.BASE_URL + "/go/pcm/bookReview/get-reviews?_csrfToken=%s&bookId=%s&pageIndex=1&pageSize=0&orderBy=1&novelType=0&needSummary=1";
  private static final String RECOMMENDED_URL = Configuration.BASE_URL + "/go/pcm/recommend/getRecommendList?_csrfToken=%s&bookId=%s&type=2&pageSize=32";
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
      List<SerializableNovel> fullList = NovelDatabase.novels;
      fullList.clear();
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
        if (page % 10 == 0 && page != 0) {
          NovelDownloaderTask.saveToFile(fullList);
        }
      } while (lastList != null);
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
      downloadDetails(currentList.stream().filter(novel -> novel.getDetails() == null).collect(Collectors.toList()));
      return currentList;
    });
  }

  private void downloadDetails(List<SerializableNovel> novels) {
    novels.forEach(novel -> {
      System.out.printf("Loading details for novel n. %d out of %d total novels", novels.indexOf(novel), novels.size());
      var chaptersInfo = getFromJson(String.format(CHAPTERS_INFO_URL, csrfToken, novel.getId()), ChaptersInfoResponse.class);
      var rankInfo = getFromJson(String.format(RANK_INFO_URL, csrfToken, novel.getId()), RankInfoResponse.class);
      var reviews = getFromJson(String.format(REVIEWS_URL, csrfToken, novel.getId()), ReviewsResponse.class);
      var gifts = getFromJson(String.format(GIFTS_URL, csrfToken, novel.getId()), GiftsResponse.class);
      //var recommended = getFromJson(String.format(RECOMMENDED_URL, csrfToken, novel.getId()), RecommendationResponse.class);
      var details = new NovelDetails();
      chaptersInfo.ifPresent(chapters -> details.setChaptersInfo(chapters.getData().getChaptersInfo()));
      rankInfo.ifPresent(rank -> details.setRankInfo(rank.getRankInfo()));
      reviews.ifPresent(review -> details.setReviews(review.getData().getReviews()));
      gifts.ifPresent(gift -> details.setGifts(gift.getData()));
      //recommended.ifPresent(recommendation -> details.setRecommendations(recommendation.getRecommendationData().toList()));
      novel.setDetails(details);
      try {
        if (novels.indexOf(novel) % 10 == 0 && novels.indexOf(novel) != 0) { // Every 10 novels
          NovelDownloaderTask.saveToFile(NovelDatabase.novels);
          Thread.sleep(Configuration.TIMEOUT); // Wait some time to make it less... suspicious?
        }
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    });
  }

  private <T> Optional<T> getFromJson(String url, Class<T> type) {
    try {
      var connection = Jsoup.connect(url).followRedirects(true).ignoreContentType(true).timeout(10 * 1000).execute();
      var gson = new Gson();
      return Optional.of(gson.fromJson(connection.body(), type));
    } catch (IOException e) {
      e.printStackTrace();
      return Optional.empty();
    }
  }

  public CompletableFuture<List<SerializableNovel>> downloadAll() {
    return CompletableFuture.supplyAsync(() -> {
      var currentList = NovelDatabase.novels;
      downloadDetails(currentList);
      return currentList;
    });
  }

  public CompletableFuture<List<SerializableNovel>> download500(int from) {
    return CompletableFuture.supplyAsync(() -> {
      var currentList = NovelDatabase.novels;
      if (currentList.size() > from + 500) {
        downloadDetails(currentList.subList(from, from + 500));
      } else {
        downloadDetails(currentList.subList(from, from + (currentList.size() - from)));
      }
      return currentList;
    });
  }
}
