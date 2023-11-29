package de.tosoxdev.cmi.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class Data extends ModListItem {
    private final String link;
    private final List<String> setup;

    @JsonCreator
    public Data(
            @JsonProperty("id") String id,
            @JsonProperty("link") String link,
            @JsonProperty("setup") List<String> setup)
    {
        super(id);
        this.link = link;
        this.setup = setup;
    }

    public String getLink() {
        return link;
    }

    public List<String> getSetup() {
        return setup;
    }
}
