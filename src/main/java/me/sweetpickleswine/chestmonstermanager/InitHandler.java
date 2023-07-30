package me.sweetpickleswine.chestmonstermanager;


import fi.dy.masa.malilib.config.ConfigManager;
import fi.dy.masa.malilib.event.InputEventHandler;
import fi.dy.masa.malilib.interfaces.IInitializationHandler;
import fi.dy.masa.malilib.util.StringUtils;
import me.sweetpickleswine.chestmonstermanager.config.Config;
import me.sweetpickleswine.chestmonstermanager.config.Hotkeys;

public class InitHandler implements IInitializationHandler {
    private static final String CONFIG_FILE_NAME = ChestMonsterManager.ModId + ".json";
    @Override
    public void registerModHandlers()
    {
        ConfigManager.getInstance().registerConfigHandler(ChestMonsterManager.ModId, new Config());

        InputEventHandler.getKeybindManager().registerKeybindProvider(InputHandler.getInstance());
        InputEventHandler.getInputManager().registerMouseInputHandler(InputHandler.getInstance());

        Hotkeys.init();

    }
}
