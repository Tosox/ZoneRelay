package de.tosox.zonerelay.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import de.tosox.zonerelay.models.components.Addon;
import de.tosox.zonerelay.models.components.Patch;
import de.tosox.zonerelay.models.components.Separator;

import java.util.List;

public record Modlist(List<Addon> addons, List<Patch> patches, List<Separator> separators) {
    @JsonCreator
    public Modlist(
            @JsonProperty("addons") List<Addon> addons,
            @JsonProperty("patches") List<Patch> patches,
            @JsonProperty("separators") List<Separator> separators) {
        this.addons = addons;
        this.patches = patches;
        this.separators = separators;
    }
}
