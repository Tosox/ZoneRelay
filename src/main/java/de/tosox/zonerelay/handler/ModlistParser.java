package de.tosox.zonerelay.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import de.tosox.zonerelay.Main;
import de.tosox.zonerelay.localizer.Localizer;
import de.tosox.zonerelay.logger.Logger;
import de.tosox.zonerelay.logger.UILogger;
import de.tosox.zonerelay.models.Modlist;

import java.io.File;
import java.io.IOException;

public class ModlistParser {
    private static final UILogger uiLogger = UILogger.getInstance();
    private static final Logger logger = Logger.getInstance();
    private static final Localizer localizer = Main.getLocalizer();

    private ModlistParser() {}

    public static Modlist parse(String modListPath) {
        File modList = new File(modListPath);
        if (!modList.isFile()) {
            uiLogger.warn(localizer.translate("ERR_MISSING_MODLIST_CFG"));
            logger.warn("Unable to locate the mod-list configuration file");
            return null;
        }

        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        try {
            return mapper.readValue(modList, Modlist.class);
        } catch (IOException e) {
            uiLogger.warn(localizer.translate("ERR_MODLIST_CFG_PARSE_ERROR"));
            logger.warn("The mod-list configuration file seems to be malformed:%n%s", e.getMessage());
            return null;
        }
    }
}
