package me.deejack.webnoveltags.models.json.novels.details;

import com.google.gson.annotations.SerializedName;

public class GiftsResponse {
  private Gifts data;

  public Gifts getData() {
    return data;
  }

  public static class Gifts {
    @SerializedName(value = "numOfGifts", alternate = "giftNum")
    private int numOfGifts;

    public int getNumOfGifts() {
      return numOfGifts;
    }
  }
}
