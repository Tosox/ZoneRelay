package de.tosoxdev.ami.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

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
