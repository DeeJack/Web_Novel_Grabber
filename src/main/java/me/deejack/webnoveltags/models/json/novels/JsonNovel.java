package me.deejack.webnoveltags.models.json.novels;

import com.google.gson.annotations.SerializedName;
import me.deejack.webnoveltags.models.json.tags.JsonTag;

import java.util.List;

public class JsonNovel {
    private long bookId;
    @SerializedName("bookName")
    private String name;
    private String description;
    private int categoryId;
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
}
