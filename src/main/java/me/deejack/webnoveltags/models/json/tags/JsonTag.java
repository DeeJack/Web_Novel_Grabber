package me.deejack.webnoveltags.models.json.tags;

import com.google.gson.annotations.SerializedName;
import me.deejack.webnoveltags.config.Configuration;

public class JsonTag {
  @SerializedName("tagId")
  private long id;
  @SerializedName("tagName")
  private String name;
  @SerializedName("type")
  private String category;

  public JsonTag() {
  }

  public JsonTag(long id, String name) {
    this.id = id;
    this.name = name;
  }

  public String getUrl() {
    var base = Configuration.BASE_URL + "/tags/";
    return base + name.replaceAll(" ", "-") + "_novel_" + id + "_page1";
  }

  public long getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public String getCategory() {
    if (category == "0")
      return "#";
    return category;
  }
}
