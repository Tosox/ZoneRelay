package de.tosox.zonerelay.models.components;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import de.tosox.zonerelay.models.components.generic.ModlistComponent;

import java.util.List;

public record Patch(String link, List<String> setup) implements ModlistComponent {
    @JsonCreator
    public Patch(
            @JsonProperty("link") String link,
            @JsonProperty("setup") List<String> setup) {
        this.link = link;
        this.setup = setup;
    }
}
