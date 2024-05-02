package de.tosox.smi.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import de.tosox.smi.models.components.Addon;
import de.tosox.smi.models.components.Data;
import de.tosox.smi.models.components.Separator;

import java.util.List;

public class ModList {
    private final List<Addon> addonList;
    private final List<Data> dataList;
    private final List<Separator> separatorList;

    @JsonCreator
    public ModList(
            @JsonProperty("addonList") List<Addon> addonList,
            @JsonProperty("dataList") List<Data> dataList,
            @JsonProperty("separatorList") List<Separator> separatorList)
    {
        this.addonList = addonList;
        this.dataList = dataList;
        this.separatorList = separatorList;
    }

    public List<Addon> getAddonList() {
        return addonList;
    }

    public List<Data> getDataList() {
        return dataList;
    }

    public List<Separator> getSeparatorList() {
        return separatorList;
    }
}
