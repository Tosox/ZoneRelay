package de.tosox.zonerelay.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import de.tosox.zonerelay.model.ConfigData;

import java.io.File;

public class ConfigLoader {
	private final ObjectMapper objectMapper = new ObjectMapper(new YAMLFactory());

	public ConfigData load(File file) throws Exception {
		return objectMapper.readValue(file, ConfigData.class);
	}
}
