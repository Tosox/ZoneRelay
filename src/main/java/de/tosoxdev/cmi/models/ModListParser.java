package de.tosoxdev.cmi.models;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import de.tosoxdev.cmi.exceptions.ModListParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

public class ModListParser {
    private static final Logger LOGGER = LoggerFactory.getLogger(ModListParser.class);

    public ModList parse(String modListPath) {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        File modList = new File(modListPath);
        if (!modList.isFile()) {
            throw new ModListParseException("help");
        }

        try {
            return mapper.readValue(modList, ModList.class);
        } catch (IOException e) {
            throw new ModListParseException(e.getMessage());
        }
    }
}
