package de.tosox.zonerelay.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.List;

@Getter
public class Patch extends ConfigEntry implements Downloadable {
	private final String url;
	private final List<String> setup;

	@JsonCreator
	public Patch(@JsonProperty("id") String id,
	             @JsonProperty("name") String name,
	             @JsonProperty("url") String url,
	             @JsonProperty("setup") List<String> setup) {
		super(id, EntryType.PATCH, name);
		this.url = url;
		this.setup = setup;
	}
}
