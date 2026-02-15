package com.github.errebenito.metallumbot.helper;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;

public class UpcomingAlbumHelper {

    public static final String FULL_UPCOMING_ALBUMS_URL = "https://www.metal-archives.com/release/ajax-upcoming/json/1";
    public static final String PARTIAL_UPCOMING_ALBUMS_URL = "https://www.metal-archives.com/release/upcoming";

    private static final Logger LOGGER = LogManager.getLogger(UpcomingAlbumHelper.class);

    private UpcomingAlbumHelper() {
        // intentionally empty
    }

    public static String extractHref(String html) {
        Matcher m = Pattern.compile("href=\"([^\"]+)\"").matcher(html);
        return m.find() ? m.group(1) : "";
    }

    public static String extractText(String html) {
        return Jsoup.parse(html).text();
    }

    public static LocalDate parseDate(String value) {
        try {
            return LocalDate.parse(value, DateTimeFormatter.ofPattern("MMMM d['st']['nd']['rd']['th'], yyyy", Locale.ENGLISH));
        } catch (DateTimeParseException e) {
            LOGGER.error("Error parsing date {}: {}", value, e.getMessage());
            return null;
        }
    }
}
