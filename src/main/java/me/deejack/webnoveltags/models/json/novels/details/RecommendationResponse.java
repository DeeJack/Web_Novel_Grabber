package me.deejack.webnoveltags.models.json.novels.details;

import com.google.gson.annotations.SerializedName;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class RecommendationResponse {
  @SerializedName("data")
  private RecommendationData recommendationData;

  public RecommendationData getRecommendationData() {
    return recommendationData;
  }

  public static class RecommendationData {
    private List<RecommendationRaw> recommendListItems;

    public Recommendations toList() {
      return new Recommendations(recommendListItems.stream().map(RecommendationRaw::getBookId).collect(Collectors.toList()));
    }

    private static class RecommendationRaw {
      private String bookId;

      public String getBookId() {
        return bookId;
      }
    }
  }

  public static class Recommendations {
    private final List<String> recommendedNovels = new LinkedList<>();

    public Recommendations(List<String> ids) {
      recommendedNovels.addAll(ids);
    }

    public List<String> getRecommendedNovels() {
      return recommendedNovels;
    }
  }
}
