package me.sweetpickleswine.chestmonstermanager.config;

import com.google.common.collect.ImmutableList;
import fi.dy.masa.malilib.config.options.ConfigHotkey;
import fi.dy.masa.malilib.gui.GuiBase;
import fi.dy.masa.malilib.hotkeys.IHotkeyCallback;
import fi.dy.masa.malilib.hotkeys.IKeybind;
import fi.dy.masa.malilib.hotkeys.KeyAction;
import fi.dy.masa.malilib.hotkeys.KeybindSettings;
import me.sweetpickleswine.chestmonstermanager.ChestMonsterManager;
import me.sweetpickleswine.chestmonstermanager.gui.ConfigScreen;
import net.minecraft.client.MinecraftClient;

import java.util.List;

public class Hotkeys {
    public static final ConfigHotkey OPEN_GUI_MAIN_MENU = new ConfigHotkey("openGuiMainMenu", "M", KeybindSettings.RELEASE_EXCLUSIVE, "Open the ChestMonsterManager main menu");



    public static final List<ConfigHotkey> HOTKEY_LIST = ImmutableList.of(
            OPEN_GUI_MAIN_MENU
    );


    public static void init(){
        KeyCallbackHotkeys callbackHotkeys = new KeyCallbackHotkeys(MinecraftClient.getInstance());

        OPEN_GUI_MAIN_MENU.getKeybind().setCallback(callbackHotkeys);

    }
    private static class KeyCallbackHotkeys implements IHotkeyCallback
    {

        private final MinecraftClient mc;

        public KeyCallbackHotkeys(MinecraftClient mc)
        {
            this.mc = mc;
        }

        @Override
        public boolean onKeyAction(KeyAction action, IKeybind key)
        {
            if (this.mc.player == null || this.mc.world == null)
            {
                return false;
            }

            ChestMonsterManager.LOGGER.debug("Key: " + key.toString());
            if (key.equals(OPEN_GUI_MAIN_MENU.getKeybind())){
                GuiBase.openGui(new ConfigScreen());
                return true;
            }

            return false;
        }
    }
}
