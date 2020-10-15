package me.deejack.webnoveltags.models.json.novels;

import com.google.gson.annotations.SerializedName;

public class NovelDetails {
  @SerializedName("totalChapterNum")
  private int totalChapters;
  @SerializedName("newChapterTime")
  private String lastUpdate;
  @SerializedName("newChapterIndex")
  private int lastChapter;

  public int getTotalChapters() {
    return totalChapters;
  }

  public String getLastUpdate() {
    return lastUpdate;
  }

  public int getLastChapter() {
    return lastChapter;
  }
}
