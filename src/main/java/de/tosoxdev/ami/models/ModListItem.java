package de.tosoxdev.ami.models;

public abstract class ModListItem {
    private final String id;

    protected ModListItem(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
