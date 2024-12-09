package de.tosox.zonerelay.models.components;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import de.tosox.zonerelay.models.components.generic.AbstractComponent;

import java.util.List;

public class Addon extends AbstractComponent {
    private final String name;
    private final String link;
    private final List<String> setup;

    @JsonCreator
    public Addon(
            @JsonProperty("id") String id,
            @JsonProperty("name") String name,
            @JsonProperty("link") String link,
            @JsonProperty("setup") List<String> setup)
    {
        super(id);
        this.name = name;
        this.link = link;
        this.setup = setup;
    }

    public String getName() {
        return name;
    }

    public String getLink() {
        return link;
    }

    public List<String> getSetup() {
        return setup;
    }
}
