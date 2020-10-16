package me.deejack.webnoveltags.tags;

import com.google.gson.Gson;
import me.deejack.webnoveltags.config.Configuration;
import me.deejack.webnoveltags.models.json.NextData;
import me.deejack.webnoveltags.models.json.tags.JsonTag;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class TagsDownloader {
  public static final String TAGS_URL = Configuration.BASE_URL + "/all-tags/%s/%d";
  private static final List<JsonTag> tags = new LinkedList<>();
  private static final int TIMEOUT_TIME = 100;

  public CompletableFuture<List<JsonTag>> downloadAllTags() {
    return CompletableFuture.supplyAsync(() -> {
      var page = getPage(String.format(TAGS_URL, "0", 1));
      var data = getData(page);
      var biggestCategory = getBiggestCategory(data);
      loadTags(data);
      for (int i = 2; i < biggestCategory.getValue(); i++) {
        var nextPage = getPage(String.format(TAGS_URL, biggestCategory.getKey(), i));
        var nextData = getData(nextPage);
        loadTags(nextData);

        try {
          Thread.sleep(TIMEOUT_TIME);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
      }

      tags.sort((tag, nextTag) -> tag.getName().compareToIgnoreCase(nextTag.getName()));
      return tags;
    });
  }

  private Document getPage(String url) {
    try {
      return Jsoup.connect(url).timeout(5 * 1000).get();
    } catch (IOException exc) {
      exc.printStackTrace();
      return null;
    }
  }

  private String getJson(Document document) {
    return document.select("#__NEXT_DATA__").first().html();
  }

  private NextData getData(Document document) {
    return new Gson().fromJson(getJson(document), NextData.class);
  }

  private Map.Entry<String, Integer> getBiggestCategory(NextData data) {
    var lettersCount = data.getProps().getPageProps().getLetterCount();
    String biggestCategory = "0";
    int biggest = 0;
    for (var letter : lettersCount.entrySet()) {
      if (letter.getValue() > biggest) {
        biggest = letter.getValue();
        biggestCategory = letter.getKey();
      }
    }
    return Map.entry(biggestCategory, biggest);
  }

  private void loadTags(NextData data) {
    var jsonTags = data.getProps().getPageProps().getTags();
    tags.addAll(Arrays.asList(jsonTags));
  }
}
