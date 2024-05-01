package de.tosox.smi.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Separator extends ModListItem {
    private final String name;

    @JsonCreator
    public Separator(
            @JsonProperty("id") String id,
            @JsonProperty("name") String name)
    {
        super(id);
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
