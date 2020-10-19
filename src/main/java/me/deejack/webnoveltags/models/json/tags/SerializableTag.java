package me.deejack.webnoveltags.models.json.tags;

import java.util.ArrayList;
import java.util.List;

public class SerializableTag {
  private final List<Long> ids = new ArrayList<>();
  private final String name;

  public SerializableTag(String name, long id) {
    this.name = name;
    this.ids.add(id);
  }

  public SerializableTag(JsonTag tag) {
    this(tag.getName(), tag.getId());
  }

  public SerializableTag() {
    this("", 0);
    this.ids.clear();
  }

  public List<Long> getIds() {
    return ids;
  }

  public String getName() {
    return name;
  }
}
