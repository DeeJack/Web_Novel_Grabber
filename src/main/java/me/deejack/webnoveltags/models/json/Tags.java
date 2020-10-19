package me.deejack.webnoveltags.models.json;

import me.deejack.webnoveltags.models.json.categories.Category;
import me.deejack.webnoveltags.models.json.tags.SerializableTag;

import java.util.Collections;
import java.util.List;

public class Tags {
  private final List<Category> mainCategories;
  private final List<SerializableTag> tags;

  public Tags(List<Category> mainCategories, List<SerializableTag> tags) {
    this.mainCategories = Collections.unmodifiableList(mainCategories);
    this.tags = Collections.unmodifiableList(tags);
  }

  public List<Category> getMainCategories() {
    return mainCategories;
  }

  public List<SerializableTag> getTags() {
    return tags;
  }
}
