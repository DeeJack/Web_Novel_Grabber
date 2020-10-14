package me.deejack.webnoveltags.models.json.novels;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class NovelResponse {
  private Data data;

  public Data getData() {
    return data;
  }

  public static class Data {
    @SerializedName("items")
    private List<JsonNovel> novels;

    public List<JsonNovel> getNovels() {
      return novels;
    }
  }
}
