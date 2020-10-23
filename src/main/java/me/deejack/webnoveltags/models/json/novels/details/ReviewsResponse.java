package me.deejack.webnoveltags.models.json.novels.details;

import com.google.gson.annotations.SerializedName;

public class ReviewsResponse {
  private StatisticsData data;

  public StatisticsData getData() {
    return data;
  }

  public static class StatisticsData {
    @SerializedName(value = "reviews", alternate = "bookStatisticsInfo")
    private Reviews reviews;

    public Reviews getReviews() {
      return reviews;
    }
  }

  public static class Reviews {
    private int totalReviewNum;
    private double totalScore;
  }
}
