package de.tosox.zonerelay.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Separator extends ConfigEntry {
	@JsonCreator
	public Separator(@JsonProperty("id") String id,
	                 @JsonProperty("name") String name) {
		super(id, EntryType.SEPARATOR, name);
	}
}
