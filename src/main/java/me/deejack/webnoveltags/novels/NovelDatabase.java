package me.deejack.webnoveltags.novels;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import me.deejack.webnoveltags.models.json.novels.JsonNovel;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;

public class NovelDatabase {
    public static List<JsonNovel> novels;

    static {
        try {
            loadNovels(new File("./novels.json"));
        } catch (IOException exc) {
            // The file doesn't exists, no problem
        }
    }

    public static void loadNovels(File file) throws IOException {
        String json = String.join("\r\n", Files.readAllLines(file.toPath(), StandardCharsets.UTF_8));
        Type listType = new TypeToken<List<JsonNovel>>() {
        }.getType();
        List<JsonNovel> novelsLoaded = new Gson().fromJson(json, listType);
        NovelDatabase.novels = novelsLoaded;
    }
}
