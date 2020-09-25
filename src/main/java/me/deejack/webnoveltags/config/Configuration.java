package me.deejack.webnoveltags.config;

import java.util.Scanner;

public class Configuration {
    public static final String BASE_URL;

    static {
        System.out.print("Insert the URL (no https://www needed): ");
        var url = new Scanner(System.in).nextLine();
        BASE_URL = "https://www." + url;
    }
}
