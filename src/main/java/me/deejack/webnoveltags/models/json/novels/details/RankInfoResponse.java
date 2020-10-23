package me.deejack.webnoveltags.models.json.novels.details;

import com.google.gson.annotations.SerializedName;

public class RankInfoResponse {
  @SerializedName(value = "rankInfo", alternate = "data")
  private RankInfo rankInfo;

  public RankInfo getRankInfo() {
    return rankInfo;
  }

  public static class RankInfo {
    @SerializedName(value = "powerVotes", alternate = "powerNums")
    private int powerVotes;
    @SerializedName(value = "currentRank", alternate = "rank")
    private int currentRank;
  }
}
