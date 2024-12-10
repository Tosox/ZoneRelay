package de.tosox.zonerelay.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import de.tosox.zonerelay.models.components.Addon;
import de.tosox.zonerelay.models.components.Patch;
import de.tosox.zonerelay.models.components.Separator;

import java.util.List;

public class Modlist {
    private final List<Addon> addons;
    private final List<Patch> patches;
    private final List<Separator> separators;

    @JsonCreator
    public Modlist(
            @JsonProperty("addons") List<Addon> addons,
            @JsonProperty("patches") List<Patch> patches,
            @JsonProperty("separators") List<Separator> separators)
    {
        this.addons = addons;
        this.patches = patches;
        this.separators = separators;
    }

    public List<Addon> getAddons() {
        return addons;
    }

    public List<Patch> getPatches() {
        return patches;
    }

    public List<Separator> getSeparators() {
        return separators;
    }
}
