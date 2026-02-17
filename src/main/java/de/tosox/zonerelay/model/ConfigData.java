package de.tosox.zonerelay.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.List;

@Getter
public class ConfigData {
	private final List<Addon> addons;
	private final List<Patch> patches;
	private final List<Separator> separators;

	@JsonCreator
	public ConfigData(@JsonProperty("addons") List<Addon> addons,
	                  @JsonProperty("patches")List<Patch> patches,
	                  @JsonProperty("separators") List<Separator> separators) {
		this.addons = addons;
		this.patches = patches;
		this.separators = separators;
	}
}
