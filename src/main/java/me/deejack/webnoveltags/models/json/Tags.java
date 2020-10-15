package me.deejack.webnoveltags.models.json;

import me.deejack.webnoveltags.models.json.categories.Category;
import me.deejack.webnoveltags.models.json.tags.JsonTag;

import java.util.Collections;
import java.util.List;

public class Tags {
  private final List<Category> mainCategories;
  private final List<JsonTag> tags;

  public Tags(List<Category> mainCategories, List<JsonTag> tags) {
    this.mainCategories = Collections.unmodifiableList(mainCategories);
    this.tags = Collections.unmodifiableList(tags);
  }

  public List<Category> getMainCategories() {
    return mainCategories;
  }

  public List<JsonTag> getTags() {
    return tags;
  }
}
