package de.tosox.ami.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import de.tosox.ami.Main;
import de.tosox.ami.localizer.Localizer;
import de.tosox.ami.logger.Logger;
import de.tosox.ami.logger.UILogger;
import de.tosox.ami.models.ModList;

import java.io.File;
import java.io.IOException;

public class ModListParser {
    private static final UILogger uiLogger = UILogger.getInstance();
    private static final Logger logger = Logger.getInstance();
    private static final Localizer localizer = Main.getLocalizer();

    private ModListParser() {}

    public static ModList parse(String modListPath) {
        File modList = new File(modListPath);
        if (!modList.isFile()) {
            uiLogger.warn(localizer.translate("err_missing_mod-list_cfg"));
            logger.warn("Unable to locate the mod-list configuration file");
            return null;
        }

        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        try {
            return mapper.readValue(modList, ModList.class);
        } catch (IOException e) {
            uiLogger.warn(localizer.translate("err_mod-list_cfg_parse_error"));
            logger.warn("The mod-list configuration file seems to be malformed:%n%s", e.getMessage());
            return null;
        }
    }
}
