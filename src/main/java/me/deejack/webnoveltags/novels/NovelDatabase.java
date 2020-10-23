package me.deejack.webnoveltags.novels;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import me.deejack.webnoveltags.config.Configuration;
import me.deejack.webnoveltags.models.json.novels.SerializableNovel;
import me.deejack.webnoveltags.novels.downloaders.NovelDownloaderTask;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class NovelDatabase {
  public static volatile List<SerializableNovel> novels = Collections.synchronizedList(new LinkedList<>());

  static {
    try {
      loadNovels(Configuration.getFileInCurrentPath("webnovels.json"));
    } catch (IOException exc) {
      // The file doesn't exists, no problem
    }
    Runtime.getRuntime().addShutdownHook(new Thread(() ->
            NovelDownloaderTask.saveToFile(novels, Configuration.getFileInCurrentPath("webnovels_backup.json"))));
  }

  public static void loadNovels(Path path) throws IOException {
    String json = String.join("\r\n", Files.readAllLines(path, StandardCharsets.UTF_8));
    Type listType = new TypeToken<List<SerializableNovel>>() {
    }.getType();
    List<SerializableNovel> novelsLoaded = new Gson().fromJson(json, listType);
    NovelDatabase.novels = novelsLoaded;
  }


}
