package me.deejack.webnoveltags.novels;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import me.deejack.webnoveltags.config.Configuration;
import me.deejack.webnoveltags.models.json.novels.SerializableNovel;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedList;
import java.util.List;

public class NovelDatabase {
  public static List<SerializableNovel> novels = new LinkedList<>();

  static {
    try {
      loadNovels(Configuration.getFileInCurrentPath("webnovels.json"));
    } catch (IOException exc) {
      // The file doesn't exists, no problem
    }
  }

  public static void loadNovels(Path path) throws IOException {
    String json = String.join("\r\n", Files.readAllLines(path, StandardCharsets.UTF_8));
    Type listType = new TypeToken<List<SerializableNovel>>() {
    }.getType();
    List<SerializableNovel> novelsLoaded = new Gson().fromJson(json, listType);
    NovelDatabase.novels = novelsLoaded;
  }
}
