package me.deejack.webnoveltags.tags;

import com.google.gson.Gson;
import me.deejack.webnoveltags.config.Configuration;
import me.deejack.webnoveltags.models.json.JsonTag;
import me.deejack.webnoveltags.models.json.NextData;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class TagsDownloader {
    public static final String TAGS_URL = Configuration.BASE_URL + "/all-tags/%s/%d";
    private static final Map<String, List<JsonTag>> tagsPerCategory = new HashMap<>();

    public CompletableFuture<Map<String, List<JsonTag>>> downloadAllTags() {
        CompletableFuture<Map<String, List<JsonTag>>> future = CompletableFuture.supplyAsync(() -> {
            System.out.println("Starting");
            var page = getPage(String.format(TAGS_URL, "0", 1));
            System.out.println("Page downloaded");
            var data = getData(page);
            var biggestCategory = getBiggestCategory(data);
            System.out.println("Loading tags");
            loadTags(data);
            System.out.println("Loaded first page");
            for (int i = 2; i < biggestCategory.getValue(); i++) {
                System.out.println("Loading " + i + " page");
                var nextPage = getPage(String.format(TAGS_URL, biggestCategory.getKey(), i));
                var nextData = getData(nextPage);
                loadTags(nextData);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            for (var list : tagsPerCategory.values()) {
                list.sort((tag, nextTag) -> tag.getName().compareToIgnoreCase(nextTag.getName()));
            }
            return tagsPerCategory;
        });
        return future;
    }

    private Document getPage(String url) {
        try {
            System.out.println(url);
            return Jsoup.connect(url).timeout(5*1000).get();
        } catch (IOException exc) {
            System.out.println("ERROR");
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
        var tags = data.getProps().getPageProps().getTags();
        for (var tag : tags) {
            if (tagsPerCategory.containsKey(tag.getCategory()))
                tagsPerCategory.get(tag.getCategory()).add(tag);
            else {
                var tagList = new ArrayList<JsonTag>();
                tagList.add(tag);
                tagsPerCategory.put(tag.getCategory(), tagList);
            }
        }
    }
}
