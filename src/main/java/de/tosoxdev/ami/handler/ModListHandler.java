package de.tosoxdev.ami.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import de.tosoxdev.ami.models.ModList;

import java.io.File;
import java.io.IOException;

public class ModListHandler {
    public ModList parse(String modListPath) {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        File modList = new File(modListPath);
        if (!modList.isFile()) {
            // TODO: Mod list file not found
            return null;
        }

        try {
            return mapper.readValue(modList, ModList.class);
        } catch (IOException e) {
            // TODO: Parse exception
            return null;
        }
    }
}
