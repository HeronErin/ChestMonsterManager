package me.sweetpickleswine.chestmonstermanager.gui;

import fi.dy.masa.malilib.gui.GuiConfigsBase;
import me.sweetpickleswine.chestmonstermanager.ChestMonsterManager;
import me.sweetpickleswine.chestmonstermanager.config.Config;
import net.minecraft.client.gui.screen.Screen;

import java.util.List;

public class ConfigScreen extends GuiConfigsBase {
    public ConfigScreen()
    {
        super(10, 50, ChestMonsterManager.ModId, null, "CMM.config.title");
    }

    @Override
    public List<ConfigOptionWrapper> getConfigs() {
        return ConfigOptionWrapper.createFor(Config.OPTIONS);
    }
}
