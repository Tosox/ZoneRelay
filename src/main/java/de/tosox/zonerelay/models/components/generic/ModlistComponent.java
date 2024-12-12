package de.tosox.zonerelay.models.components.generic;

public abstract class ModlistComponent {
	private final String id;

	public ModlistComponent(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}
}
