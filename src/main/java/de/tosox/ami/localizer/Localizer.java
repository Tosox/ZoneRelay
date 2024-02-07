package de.tosox.ami.localizer;

import de.tosox.ami.logger.Logger;
import de.tosox.ami.utils.Globals;
import de.tosox.ami.handler.CrashHandler;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class Localizer {
    private final Logger logger = Logger.getInstance();

    private final JSONObject translations;

    public Localizer(Localizer.Language language) {
        translations = getLocalizationFileContents(language.getLanguageCode());
    }

    public String translate(String id) {
        try {
            return translations.getString(id);
        } catch (JSONException e) {
            logger.warn("No valid translation for '%s'", id);
            return id;
        }
    }

    private JSONObject getLocalizationFileContents(String langcode) {
        try {
            String path = String.format("%s/%s.json", Globals.DIR_LOCALES, langcode);
            List<String> lines = Files.readAllLines(Path.of(path));
            return new JSONObject(String.join("", lines));
        } catch (IOException e) {
            logger.error("Unable to find localization for '%s'", langcode);
            CrashHandler.showErrorDialogAndExit("Unable to find localization for '%s':%n%s", langcode, e.getMessage());
            return null; // Unreachable
        } catch (JSONException e) {
            logger.error("Unable to parse the contents of localization '%s'", langcode);
            CrashHandler.showErrorDialogAndExit("Unable to parse the contents of localization '%s':%n%s", langcode, e.getMessage());
            return null; // Unreachable
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
