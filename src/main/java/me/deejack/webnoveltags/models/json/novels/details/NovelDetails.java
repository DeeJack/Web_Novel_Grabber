package me.deejack.webnoveltags.models.json.novels.details;

import java.time.LocalDateTime;

public class NovelDetails {
  private final String updatedAt;
  private ChaptersInfoResponse.ChaptersInfo chaptersInfo;
  private RankInfoResponse.RankInfo rankInfo;
  private GiftsResponse.Gifts gifts;
  private ReviewsResponse.Reviews reviews;
  //private RecommendationResponse.Recommendations recommendations;

  public NovelDetails() {
    updatedAt = LocalDateTime.now().toString();
  }

  public String getUpdatedAt() {
    return updatedAt;
  }

  public ChaptersInfoResponse.ChaptersInfo getChaptersInfo() {
    return chaptersInfo;
  }

  public void setChaptersInfo(ChaptersInfoResponse.ChaptersInfo chaptersInfo) {
    this.chaptersInfo = chaptersInfo;
  }

  public RankInfoResponse.RankInfo getRankInfo() {
    return rankInfo;
  }

  public void setRankInfo(RankInfoResponse.RankInfo rankInfo) {
    this.rankInfo = rankInfo;
  }

  public GiftsResponse.Gifts getGifts() {
    return gifts;
  }

  public void setGifts(GiftsResponse.Gifts gifts) {
    this.gifts = gifts;
  }

  public ReviewsResponse.Reviews getReviews() {
    return reviews;
  }

  public void setReviews(ReviewsResponse.Reviews reviews) {
    this.reviews = reviews;
  }

}
