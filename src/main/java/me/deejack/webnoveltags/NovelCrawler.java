package me.deejack.webnoveltags;

import me.deejack.webnoveltags.config.Configuration;
import me.deejack.webnoveltags.tags.TagsDownloaderTask;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.util.Scanner;

public class NovelCrawler {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String input;
        do {
            System.out.println("What task do you want to execute? ");
            System.out.println("1) Download tags and categories");
            System.out.println("2) Download novels");
            System.out.print("==> ");

            input = scanner.nextLine().trim().toLowerCase();
        } while (input.length() == 0 || (input.charAt(0) != '1' && input.charAt(0) != '2'));

        switch (input.charAt(0)) {
            case '1':
                startTagsDownloader();
                break;
            case '2':
                startNovelDownload();
                break;
            default:
                System.out.println("The fuck?");
                System.exit(0);
                break;
        }

        // /category/0_novel_page1?gender=1&bookType=&bookStatus=
        // ->  script[type="json"]

    }

    private static void startNovelDownload() {
        String csrfToken = "";
        try {
            var connection = Jsoup.connect("https://www." + Configuration.BASE_URL + "/category/0_novel_page1?gender=1").execute();
            csrfToken = connection.cookie("_csrfToken");
            if (csrfToken.trim().length() == 0) {
                System.out.println("Failed to get token!");
                return;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        // Needs csrfToken, page index, gender
        final String novelFilter = "https://www." + Configuration.BASE_URL + "/apiajax/category/categoryAjax?_csrfToken=%s&orderBy=1&pageIndex=%d&categoryId=&gender=%d&categoryType=1&translateMode=0";

    }

    private static void startTagsDownloader() {
        new TagsDownloaderTask().start();
    }
}
