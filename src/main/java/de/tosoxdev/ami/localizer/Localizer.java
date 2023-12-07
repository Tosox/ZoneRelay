package de.tosoxdev.ami.localizer;

import de.tosoxdev.ami.Main;
import de.tosoxdev.ami.exceptions.InvalidLocaleException;
import de.tosoxdev.ami.utils.Logger;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static de.tosoxdev.ami.utils.Globals.DIR_LOCALES;

public class Localizer {
    private static final Logger LOGGER = Main.getLogger();

    private static JSONObject translations;

    public Localizer(Localizer.Language language) {
        translations = getLocalizationFileContents(language.getLanguageCode());
    }

    public String translate(String id) {
        try {
            return translations.getString(id);
        } catch (JSONException e) {
            LOGGER.warn("No valid translation for '%s'", id);
            return id;
        }
    }

    private JSONObject getLocalizationFileContents(String langcode) {
        try {
            String path = String.format("%s/%s.json", DIR_LOCALES, langcode);
            List<String> lines = Files.readAllLines(Path.of(path));
            String text = String.join("", lines);
            return new JSONObject(text);
        } catch (IOException e) {
            LOGGER.error("Unable to find localization for '%s'", langcode);
            throw new InvalidLocaleException(String.format("Unable to find localization for '%s'", langcode));
        } catch (JSONException e) {
            LOGGER.error("Unable to parse the contents of localization '%s'", langcode);
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