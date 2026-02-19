package de.tosox.zonerelay.model;

import lombok.Getter;

@Getter
public abstract class ConfigEntry {
	private final String id;
	private final EntryType type;
	private final String name;

	public ConfigEntry(String id, EntryType type, String name) {
		this.id = id;
		this.type = type;
		this.name = name;
	}
}
