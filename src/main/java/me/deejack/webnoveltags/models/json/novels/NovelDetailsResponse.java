package me.deejack.webnoveltags.models.json.novels;

import com.google.gson.annotations.SerializedName;

public class NovelDetailsResponse {
  private BookData data;

  public BookData getData() {
    return data;
  }

  public static class BookData {
    @SerializedName("bookInfo")
    private NovelDetails details;

    public NovelDetails getDetails() {
      return details;
    }
  }
}
