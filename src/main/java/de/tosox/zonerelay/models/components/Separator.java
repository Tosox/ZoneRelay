package de.tosox.zonerelay.models.components;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import de.tosox.zonerelay.models.components.generic.ModlistComponent;

public record Separator(String name) implements ModlistComponent {
    @JsonCreator
    public Separator(
            @JsonProperty("name") String name) {
        this.name = name;
    }
}
