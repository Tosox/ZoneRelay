package de.tosoxdev.cmi.controllers;

import de.tosoxdev.cmi.models.ModList;
import de.tosoxdev.cmi.models.ModListParser;
import javafx.fxml.FXML;

public class MainController {
    @FXML
    protected void onButtonClick() {
        // https://github.com/TosoxDev/Anomaly-VIP/archive/refs/heads/master.zip
        // https://cloud.dlyt.de/s/2WBcPk7zpLa8W88/download/BaS_Patch_28_12_21.rar

        ModList modlist = ModListParser.parse("./data/modlist.yml");
        modlist.getAddonList().forEach(a -> System.out.println(a.getLink()));
    }
}
