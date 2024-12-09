package de.tosox.zonerelay.models.components.generic;

public abstract class AbstractComponent {
    private final String id;

    protected AbstractComponent(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
