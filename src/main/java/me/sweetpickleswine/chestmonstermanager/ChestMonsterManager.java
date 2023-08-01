package me.sweetpickleswine.chestmonstermanager;

import fi.dy.masa.malilib.event.InitializationHandler;
import fi.dy.masa.malilib.hotkeys.IKeybind;
import fi.dy.masa.malilib.hotkeys.KeyAction;
import me.sweetpickleswine.chestmonstermanager.sorting.InventoryActionTracker;
import net.fabricmc.api.ModInitializer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ChestMonsterManager implements ModInitializer {
    public static final String ModId = "chestmonstermanager";
    public static final Logger LOGGER = LoggerFactory.getLogger(ModId);

    @Override
    public void onInitialize() {

        InitializationHandler.getInstance().registerInitializationHandler(new InitHandler());

    }

    public static ButtonWidget lastButton = null;
    public static InventoryActionTracker inventoryActionTracker = null;





}
