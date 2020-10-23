package me.deejack.webnoveltags.models.json.novels.details;

import java.time.LocalDate;

public class NovelDetails {
  private LocalDate updatedAt;
  private ChaptersInfoResponse.ChaptersInfo chaptersInfo;
  private RankInfoResponse.RankInfo rankInfo;
  private GiftsResponse.Gifts gifts;
  private ReviewsResponse.Reviews reviews;
  private RecommendationResponse.Recommendations recommendations;

  public LocalDate getUpdatedAt() {
    return updatedAt;
  }

  public ChaptersInfoResponse.ChaptersInfo getChaptersInfo() {
    return chaptersInfo;
  }

  public RankInfoResponse.RankInfo getRankInfo() {
    return rankInfo;
  }

  public GiftsResponse.Gifts getGifts() {
    return gifts;
  }

  public ReviewsResponse.Reviews getReviews() {
    return reviews;
  }

  public RecommendationResponse.Recommendations getRecommendations() {
    return recommendations;
  }

  public void setUpdatedAt(LocalDate updatedAt) {
    this.updatedAt = updatedAt;
  }

  public void setChaptersInfo(ChaptersInfoResponse.ChaptersInfo chaptersInfo) {
    this.chaptersInfo = chaptersInfo;
  }

  public void setRankInfo(RankInfoResponse.RankInfo rankInfo) {
    this.rankInfo = rankInfo;
  }

  public void setGifts(GiftsResponse.Gifts gifts) {
    this.gifts = gifts;
  }

  public void setReviews(ReviewsResponse.Reviews reviews) {
    this.reviews = reviews;
  }

  public void setRecommendations(RecommendationResponse.Recommendations recommendations) {
    this.recommendations = recommendations;
  }
}
