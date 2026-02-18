package de.tosox.zonerelay.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.List;

@Getter
public class ConfigData {
	private final List<Mod> mods;
	private final List<Patch> patches;
	private final List<Separator> separators;

	@JsonCreator
	public ConfigData(@JsonProperty("mods") List<Mod> mods,
	                  @JsonProperty("patches") List<Patch> patches,
	                  @JsonProperty("separators") List<Separator> separators) {
		this.mods = mods != null ? mods : List.of();
		this.patches = patches != null ? patches : List.of();
		this.separators = separators != null ? separators : List.of();
	}
}
