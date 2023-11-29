package de.tosoxdev.cmi.localizer;

import de.tosoxdev.cmi.exceptions.InvalidLocaleException;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static de.tosoxdev.cmi.utils.Globals.DIR_LOCALES;

public class Localizer {
    private static final Logger LOGGER = LoggerFactory.getLogger(Localizer.class);

    private static JSONObject translations;

    public static void setLanguage(Localizer.Language language) {
        translations = getLocalizationFileContents(language.getLanguageCode());
    }

    public static String translate(String id) {
        if (translations == null) {
            throw new RuntimeException("The locale for the localizer is not set or invalid");
        }

        try {
            return translations.getString(id);
        } catch (JSONException e) {
            LOGGER.warn("No valid translation for '{}'", id);
            return id;
        }
    }

    private static JSONObject getLocalizationFileContents(String langcode) {
        try {
            String path = String.format("%s/%s.json", DIR_LOCALES, langcode);
            List<String> lines = Files.readAllLines(Path.of(path));
            String text = String.join("", lines);
            return new JSONObject(text);
        } catch (IOException e) {
            LOGGER.error("Unable to find localization for '{}'", langcode);
            throw new InvalidLocaleException(String.format("Unable to find localization for '%s'", langcode));
        } catch (JSONException e) {
            LOGGER.error("Unable to parse the contents of localization '{}'", langcode);
            throw new InvalidLocaleException(String.format("Unable to parse the contents of localization '%s'", langcode));
        }
    }

    public enum Language {
        EN_US("en-US"),
        DE_DE("de-DE");

        private final String languageCode;

        Language(String languageCode) {
            this.languageCode = languageCode;
        }

        public String getLanguageCode() {
            return languageCode;
        }
    }
}
