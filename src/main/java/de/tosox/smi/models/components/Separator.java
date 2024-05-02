package de.tosox.smi.models.components;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import de.tosox.smi.models.components.generic.AbstractComponent;

public class Separator extends AbstractComponent {
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
