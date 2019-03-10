package fr.spacefox.confusablehomoglyphs;

import com.google.gson.GsonBuilder;

import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.net.URL;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author SpaceFox
 * @version 1.0.0
 */
abstract class UnicodeToJsonConverter<T> {

    private final String regex;
    private final String defaultUrl;

    UnicodeToJsonConverter(String regex, String defaultUrl) {
        this.regex = regex;
        this.defaultUrl = defaultUrl;
    }

    void regenerateJson(String url, String jsonFileName) {
        if (url == null) {
            url = defaultUrl;
        }
        final Pattern pattern = Pattern.compile(regex);
        try {
            InputStream response = new URL(url).openStream();
            try (Scanner scanner = new Scanner(response)) {
                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine();
                    Matcher matcher = pattern.matcher(line);
                    if (matcher.find()) {
                        System.out.println(line);
                        onLineMatched(matcher);
                    }
                }
            }
            try (Writer writer = new FileWriter(jsonFileName)) {
                new GsonBuilder().create().toJson(getData(), writer);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    abstract T getData();

    abstract void onLineMatched(Matcher matcher);

    String getRegex() {
        return regex;
    }
}
