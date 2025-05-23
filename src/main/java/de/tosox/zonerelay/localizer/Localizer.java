package de.tosox.zonerelay.localizer;

import de.tosox.zonerelay.handler.CrashHandler;
import de.tosox.zonerelay.logger.Logger;
import de.tosox.zonerelay.utils.Globals;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

public class Localizer {
    private final Logger logger = Logger.getInstance();

    private final JSONObject translations;

    public Localizer(Localizer.Language language) {
        translations = getLocalizationFileContents(language.getLanguageCode());
    }

    public String translate(String id, Object... args) {
        try {
            return String.format(translations.getString(id), args);
        } catch (JSONException e) {
            logger.warn("No valid translation for '%s'", id);
            return id;
        } catch (IllegalArgumentException e) {
            logger.warn("Unable to format string for '%s' with '%s'", id, Arrays.toString(args));
            return id;
        }
    }

    private JSONObject getLocalizationFileContents(String langcode) {
        try {
            String path = String.format("%s/%s.json", Globals.DIR_LOCALES, langcode);
            List<String> lines = Files.readAllLines(Path.of(path));
            return new JSONObject(String.join("", lines));
        } catch (IOException e) {
            CrashHandler.showErrorDialog("Unable to find localization for '%s':%n%s", langcode, e);
            throw new RuntimeException(String.format("Unable to find localization for '%s'", langcode), e);
        } catch (JSONException e) {
            CrashHandler.showErrorDialog("Unable to parse the contents of localization '%s':%n%s", langcode, e);
            throw new RuntimeException(String.format("Unable to parse the contents of localization '%s'", langcode), e);
        }
    }

    public enum Language {
        EN_US("en-US");

        private final String languageCode;

        Language(String languageCode) {
            this.languageCode = languageCode;
        }

        public String getLanguageCode() {
            return languageCode;
        }
    }
}
