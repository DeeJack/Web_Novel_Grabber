package me.deejack.webnoveltags.models.json.novels;

import com.google.gson.annotations.SerializedName;
import me.deejack.webnoveltags.models.json.tags.JsonTag;

import java.util.List;

public class JsonNovel {
  private String bookId;
  @SerializedName("bookName")
  private String name;
  private String description;
  private long categoryId;
  private String categoryName; // TODO: needed??
  private String authorName;
  @SerializedName("totalScore")
  private float score;
  private byte translateMode;
  @SerializedName("tagInfo")
  private List<JsonTag> tags;

  public String getUrl() {
    return "";
  }

  public SerializableNovel toSerializableNovel() {
    return new SerializableNovel(bookId, name, description, categoryId, authorName, score, tags);
  }
}
