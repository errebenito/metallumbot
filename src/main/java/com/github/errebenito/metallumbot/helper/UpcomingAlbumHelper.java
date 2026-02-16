package com.github.errebenito.metallumbot.helper;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Locale;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class UpcomingAlbumHelper {

    public static final String FULL_UPCOMING_ALBUMS_URL = "https://www.metal-archives.com/release/ajax-upcoming/json/1";

    private static final Logger LOGGER = LogManager.getLogger(UpcomingAlbumHelper.class);

    private UpcomingAlbumHelper() {
        // intentionally empty
    }

    public static String extractHref(String anchorHtml) {
        int hrefStart = anchorHtml.indexOf("href=\"");
        if (hrefStart == -1) {
            throw new IllegalArgumentException("No href found");
        }

        int start = hrefStart + 6;
        int end = anchorHtml.indexOf('"', start);

        if (end == -1) {
            throw new IllegalArgumentException("Malformed href");
        }

        return anchorHtml.substring(start, end);
    }

    public static String extractText(String anchorHtml) {
        int start = anchorHtml.indexOf('>');
        int end = anchorHtml.lastIndexOf('<');

        if (start == -1 || end == -1 || end <= start) {
            throw new IllegalArgumentException("Malformed anchor");
        }

        return anchorHtml.substring(start + 1, end);
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
