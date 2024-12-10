package de.tosox.zonerelay.models.components;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import de.tosox.zonerelay.models.components.generic.ModlistComponent;

import java.util.List;

public record Addon(String name, String url, List<String> setup) implements ModlistComponent {
    @JsonCreator
    public Addon(
            @JsonProperty("name") String name,
            @JsonProperty("url") String url,
            @JsonProperty("setup") List<String> setup) {
        this.name = name;
        this.url = url;
        this.setup = setup;
    }
}
