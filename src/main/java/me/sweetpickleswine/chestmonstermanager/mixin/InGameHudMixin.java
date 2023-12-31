package me.sweetpickleswine.chestmonstermanager.mixin;

import me.sweetpickleswine.chestmonstermanager.ChestMonsterManager;
import me.sweetpickleswine.chestmonstermanager.config.Config;
import me.sweetpickleswine.chestmonstermanager.config.ListOptions.SortType;
import me.sweetpickleswine.chestmonstermanager.gui.MultiClickButton;
import me.sweetpickleswine.chestmonstermanager.gui.SortButtonHelper;
import me.sweetpickleswine.chestmonstermanager.sorting.InventoryActionTracker;
import me.sweetpickleswine.chestmonstermanager.sorting.sort;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.screen.ingame.CreativeInventoryScreen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.screen.AnvilScreenHandler;
import net.minecraft.screen.EnchantmentScreenHandler;
import net.minecraft.screen.FurnaceScreenHandler;
import net.minecraft.screen.MerchantScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.awt.*;

import static me.sweetpickleswine.chestmonstermanager.ChestMonsterManager.inventoryActionTracker;
import static me.sweetpickleswine.chestmonstermanager.sorting.MergeMaker.autoMerge;

@Mixin(HandledScreen.class)
public abstract class InGameHudMixin {



    @Shadow protected int backgroundHeight;

    @Shadow protected int backgroundWidth;
    @Shadow protected int x;
    @Shadow protected int y;

    @Shadow protected abstract void drawBackground(DrawContext context, float delta, int mouseX, int mouseY);

    @Shadow protected abstract void drawForeground(DrawContext context, int mouseX, int mouseY);

    @Inject(at = @At("HEAD"), method = "close")
    private void onClose(CallbackInfo ci){
        ChestMonsterManager.ContainingSlots=-1;
    }
    @Inject(at = @At("RETURN"), method = "init")
    private void init(CallbackInfo info){
        SortButtonHelper.addButton(this.x, this.y, this.backgroundWidth, this.backgroundHeight);




        }
        private static float anim = 1f;
        private static boolean isGoingUp = false;
    @Inject(at = @At("HEAD"), method = "render", cancellable = true)
    private void onRenderHead(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo info) {
        if (MinecraftClient.getInstance().player == null)
            return;
        if (ChestMonsterManager.lastButton != null)
        if (ChestMonsterManager.lastButton.isSelected()){
            if (SortButtonHelper.lastHoverSorted)
                return;
            info.cancel();

            this.drawBackground(context, delta, mouseX, mouseY);
            context.fill(this.x, this.y, this.x+this.backgroundWidth, this.y+this.backgroundHeight, new Color(255, 0, 0, 20).getRGB());

            for (Drawable drawable : ((ScreenInvoker)this).getDrawables()){
                drawable.render(context, mouseX, mouseY, delta);
            }
            // Foreground position fix
            context.getMatrices().push();
            context.getMatrices().translate((float)this.x, (float)this.y, 0.0F);
            this.drawForeground(context, mouseX, mouseY);
            context.getMatrices().pop();

            if (inventoryActionTracker == null){
                inventoryActionTracker = new InventoryActionTracker(MinecraftClient.getInstance().player.currentScreenHandler, SortButtonHelper.inInventoryIncludeSelector);


                if (Config.SORTING_ACTION.getOptionListValue() == SortType.JUST_MERGE){
                    inventoryActionTracker=autoMerge(inventoryActionTracker);
                }else{
                    inventoryActionTracker= sort.nameSort(inventoryActionTracker, (SortType) Config.SORTING_ACTION.getOptionListValue());
                }
            }
            inventoryActionTracker.render(context, mouseX, mouseY, this.x, this.y);

        }
        else if (inventoryActionTracker != null){inventoryActionTracker=null;}
        else{SortButtonHelper.lastHoverSorted=false;}
    }

}
