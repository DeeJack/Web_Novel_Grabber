package me.deejack.webnoveltags.tags;

import com.google.gson.Gson;
import me.deejack.webnoveltags.config.Configuration;
import me.deejack.webnoveltags.models.json.NextData;
import me.deejack.webnoveltags.models.json.tags.SerializableTag;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class TagsDownloader {
  public static final String TAGS_URL = Configuration.BASE_URL + "/all-tags/%s/%d";
  private static final List<SerializableTag> tags = new LinkedList<>();

  public CompletableFuture<List<SerializableTag>> downloadAllTags() {
    return CompletableFuture.supplyAsync(() -> {
      var page = getPage(String.format(TAGS_URL, "0", 1));
      var data = getData(page);
      var pagesPerCategory = getCategoryPages(data);
      loadTags(data);

      for (var letter : pagesPerCategory.entrySet()) {
        for (int i = 1; i < letter.getValue(); i++) {
          System.out.println("Loading page n. " + i);
          var nextPage = getPage(String.format(TAGS_URL, letter.getKey(), i));
          if (nextPage == null)
            continue;
          System.out.println("Downloaded");
          var nextData = getData(nextPage);
          System.out.println("Got datas");
          loadTags(nextData);
          System.out.println("Loaded");

          try {
            Thread.sleep(Configuration.TIMEOUT);
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
        }
      }

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

  private Map<String, Integer> getCategoryPages(NextData data) {
    var lettersCount = data.getProps().getPageProps().getLetterCount();
    var pagesPerCategory = new HashMap<String, Integer>();
    for (var letter : lettersCount.entrySet()) {
      pagesPerCategory.put(letter.getKey(), letter.getValue());
    }
    return pagesPerCategory;
  }

  private void loadTags(NextData data) {
    var jsonTags = Arrays.asList(data.getProps().getPageProps().getTags());
    var noDuplicateTags = jsonTags.stream()
            .filter(jsonTag -> tags.stream().noneMatch(tag -> tag.getIds().contains(jsonTag.getId())))
            .collect(Collectors.toList());
    for (var jsonTag : noDuplicateTags) {
      var similarTag = tags.stream()
              .filter(tag -> jsonTag.getName().equalsIgnoreCase(tag.getName()))
              .findFirst();

      similarTag
              .ifPresentOrElse(tag -> tag.getIds().add(jsonTag.getId()),
                      () -> tags.add(new SerializableTag(jsonTag.getName(), jsonTag.getId())));
    }
  }
}
