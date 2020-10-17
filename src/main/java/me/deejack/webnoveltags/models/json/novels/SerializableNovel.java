package me.deejack.webnoveltags.models.json.novels;

import me.deejack.webnoveltags.models.json.tags.JsonTag;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class SerializableNovel {
  private final String id;
  private final String name;
  private final String description;
  private final long mainCategoryId;
  private final String author;
  private final float score;
  private final List<SerializableTag> tags = new LinkedList<>();
  private NovelDetails details;

  public SerializableNovel(String id, String name, String description, long mainCategoryId, String author, float score, List<JsonTag> tags) {
    this.id = id;
    this.name = name;
    this.description = description;
    this.mainCategoryId = mainCategoryId;
    this.author = author;
    this.score = score;
    this.tags.addAll(tags.stream().map(JsonTag::getId).map(SerializableTag::new).collect(Collectors.toList()));
  }

  public String getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public String getDescription() {
    return description;
  }

  public long getMainCategoryId() {
    return mainCategoryId;
  }

  public String getAuthor() {
    return author;
  }

  public float getScore() {
    return score;
  }

  public List<SerializableTag> getTags() {
    return tags;
  }

  public NovelDetails getDetails() {
    return details;
  }

  public void setDetails(NovelDetails details) {
    this.details = details;
  }

  public static class SerializableTag {
    private final long id;

    public SerializableTag(long id) {
      this.id = id;
    }

    public long getId() {
      return id;
    }
  }
}
