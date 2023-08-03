package me.sweetpickleswine.chestmonstermanager.gui;

import me.sweetpickleswine.chestmonstermanager.ChestMonsterManager;
import me.sweetpickleswine.chestmonstermanager.config.Config;
import me.sweetpickleswine.chestmonstermanager.mixin.ScreenInvoker;
import me.sweetpickleswine.chestmonstermanager.sorting.InventoryActionTracker;
import me.sweetpickleswine.chestmonstermanager.sorting.PseudoSlot;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.*;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.screen.*;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import static me.sweetpickleswine.chestmonstermanager.ChestMonsterManager.inventoryActionTracker;

public class SortButtonHelper {
    public static boolean lastHoverSorted = false;
    private static final Identifier identifier = new Identifier(ChestMonsterManager.ModId, "textures/sort_buttons_atlas.png");


    public static InventoryActionTracker.DoInclude inInventoryIncludeSelector = slot -> {
//        System.out.println(MinecraftClient.);
        if (MinecraftClient.getInstance().currentScreen instanceof InventoryScreen){
            if (slot.id < 9)
                return false;
            if (slot.id == 45)
                return false;

            return true;
        }
        if (ChestMonsterManager.ContainingSlots == -1){
            ChestMonsterManager.LOGGER.error("Issue with slot in " + MinecraftClient.getInstance().currentScreen.getClass());
            return true;
        }
        System.out.println(slot.id +" " + ChestMonsterManager.ContainingSlots  +" + "+ (slot.id < ChestMonsterManager.ContainingSlots));
        return slot.id < ChestMonsterManager.ContainingSlots;



    };
    public static ButtonWidget.PressAction onRightClick = button -> {
        Config.SORTING_ACTION.setOptionListValue(Config.SORTING_ACTION.getOptionListValue().cycle(true));
        button.setTooltip(Tooltip.of(Text.of(Config.SORTING_ACTION.getOptionListValue().getDisplayName())));

        Config.saveToFile();
        inventoryActionTracker = null;
    };
    public static ButtonWidget.PressAction onMiddleClick = button -> {
      // Nothing Yet
    };
    public static ButtonWidget.PressAction onLeftClick = button ->{
        // Sort

        if (inventoryActionTracker != null && !lastHoverSorted) {
            inventoryActionTracker.commit();
            inventoryActionTracker = null;
            lastHoverSorted = true;
        }
    };
    public static void addButton(int x, int y, int backgroundWidth, int backgroundHeight){


        if (    // Screens that don't make sense to be sorted
                MinecraftClient.getInstance().player.currentScreenHandler instanceof AbstractFurnaceScreenHandler ||
                MinecraftClient.getInstance().player.currentScreenHandler instanceof AnvilScreenHandler ||
                MinecraftClient.getInstance().player.currentScreenHandler instanceof EnchantmentScreenHandler ||
                MinecraftClient.getInstance().player.currentScreenHandler instanceof MerchantScreenHandler||
                MinecraftClient.getInstance().player.currentScreenHandler instanceof BrewingStandScreenHandler||
                MinecraftClient.getInstance().player.currentScreenHandler instanceof BeaconScreenHandler ||
                MinecraftClient.getInstance().player.currentScreenHandler instanceof CartographyTableScreenHandler ||
                MinecraftClient.getInstance().player.currentScreenHandler instanceof GrindstoneScreenHandler ||
                MinecraftClient.getInstance().player.currentScreenHandler instanceof LecternScreenHandler ||
                MinecraftClient.getInstance().player.currentScreenHandler instanceof LoomScreenHandler ||
                MinecraftClient.getInstance().player.currentScreenHandler instanceof StonecutterScreenHandler ||
                MinecraftClient.getInstance().player.currentScreenHandler instanceof SmithingScreenHandler
        ) {
            lastHoverSorted=false;
            return;

        }

        System.out.println(MinecraftClient.getInstance().currentScreen.getClass());
        MultiClickButton tbw = new MultiClickButton(backgroundWidth + x - 20 - 1, y + 1, 20, 18, 0, 0, 19, identifier, onLeftClick, onMiddleClick, onRightClick);
        ChestMonsterManager.lastButton = tbw;
        tbw.setTooltip(Tooltip.of(Text.of(Config.SORTING_ACTION.getOptionListValue().getDisplayName())));
        ((ScreenInvoker) MinecraftClient.getInstance().currentScreen).addDrawableChildInvoker(tbw);

    }
}
