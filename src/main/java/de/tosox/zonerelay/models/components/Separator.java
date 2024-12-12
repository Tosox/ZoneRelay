package de.tosox.zonerelay.models.components;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import de.tosox.zonerelay.models.components.generic.ModlistComponent;

public class Separator extends ModlistComponent {
    private final String name;

    @JsonCreator
    public Separator(
            @JsonProperty("id") String id,
            @JsonProperty("name") String name) {
        super(id);
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
