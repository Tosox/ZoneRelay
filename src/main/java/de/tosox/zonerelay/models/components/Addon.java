package de.tosox.zonerelay.models.components;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import de.tosox.zonerelay.models.components.generic.ModlistComponent;

import java.util.List;

public class Addon extends ModlistComponent {
    private final String name;
    private final String url;
    private final List<String> setup;

    @JsonCreator
    public Addon(
            @JsonProperty("id") String id,
            @JsonProperty("name") String name,
            @JsonProperty("url") String url,
            @JsonProperty("setup") List<String> setup) {
        super(id);
        this.name = name;
        this.url = url;
        this.setup = setup;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    public List<String> getSetup() {
        return setup;
    }
}
