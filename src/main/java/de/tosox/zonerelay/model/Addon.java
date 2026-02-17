package de.tosox.zonerelay.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.List;

@Getter
public class Addon extends ConfigEntry implements Downloadable {
	private final String url;
	private final List<String> setup;

	@JsonCreator
	public Addon(@JsonProperty("id") String id,
	             @JsonProperty("name") String name,
	             @JsonProperty("url") String url,
	             @JsonProperty("setup") List<String> setup) {
		super(id, EntryType.ADDON, name);
		this.url = url;
		this.setup = setup;
	}
}
