package me.deejack.webnoveltags.models.json.novels.details;

import com.google.gson.annotations.SerializedName;

public class ChaptersInfoResponse {
  private BookData data;

  public BookData getData() {
    return data;
  }

  public static class BookData {
    @SerializedName("bookInfo")
    private ChaptersInfo chaptersInfo;

    public ChaptersInfo getChaptersInfo() {
      return chaptersInfo;
    }
  }

  public static class ChaptersInfo {
    @SerializedName(value = "totalChapters", alternate = "totalChapterNum")
    private int totalChapters;
    @SerializedName(value = "lastUpdate", alternate = "newChapterTime")
    private String lastUpdate;
    @SerializedName(value = "lastChapter", alternate = "newChapterIndex")
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
}
