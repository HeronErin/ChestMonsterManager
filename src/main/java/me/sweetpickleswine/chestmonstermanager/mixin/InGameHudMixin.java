package me.sweetpickleswine.chestmonstermanager.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import me.sweetpickleswine.chestmonstermanager.ChestMonsterManager;
import me.sweetpickleswine.chestmonstermanager.config.Config;
import me.sweetpickleswine.chestmonstermanager.gui.MultiClickButton;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.screen.ingame.GenericContainerScreen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.Text;
import net.minecraft.util.Colors;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.awt.*;
import java.util.Iterator;

@Mixin(HandledScreen.class)
public abstract class InGameHudMixin {
    private static final Identifier identifier = new Identifier(ChestMonsterManager.ModId, "textures/sort_buttons_atlas.png");


    @Shadow protected int backgroundHeight;

    @Shadow protected int backgroundWidth;
    @Shadow protected int x;
    @Shadow protected int y;

    @Shadow protected abstract void drawBackground(DrawContext context, float delta, int mouseX, int mouseY);

    @Shadow protected abstract void drawForeground(DrawContext context, int mouseX, int mouseY);

    @Inject(at = @At("RETURN"), method = "init")
    private void init(CallbackInfo info){
        ChestMonsterManager.LOGGER.error(identifier.getNamespace());
        MultiClickButton tbw = new MultiClickButton( this.backgroundWidth+this.x-20-1, this.y+1, 20, 18, 0, 0, 19, identifier, (button) -> {
            // Sort
            System.out.println("sort");
        },(button) -> {
            // Nothing
            System.out.println("Middle");
        }, (button) -> {
            // Right click
            Config.SORTING_ACTION.setOptionListValue(Config.SORTING_ACTION.getOptionListValue().cycle(true));
            button.setTooltip(Tooltip.of(Text.of(Config.SORTING_ACTION.getOptionListValue().getDisplayName())));
        });
        ChestMonsterManager.lastButton = tbw;
        tbw.setTooltip(Tooltip.of(Text.of(Config.SORTING_ACTION.getOptionListValue().getDisplayName())));
        ((ScreenInvoker) MinecraftClient.getInstance().currentScreen).addDrawableChildInvoker(tbw);


        }
        private static float anim = 1f;
        private static boolean isGoingUp = false;
    @Inject(at = @At("HEAD"), method = "render", cancellable = true)
    private void onRenderHead(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo info) {
        if (ChestMonsterManager.lastButton.isSelected()){
            info.cancel();
            this.drawBackground(context, delta, mouseX, mouseY);


            for (Drawable drawable : ((ScreenInvoker)this).getDrawables()){
                drawable.render(context, mouseX, mouseY, delta);
            }
            // Foreground position fix
            context.getMatrices().push();
            context.getMatrices().translate((float)this.x, (float)this.y, 0.0F);
            this.drawForeground(context, mouseX, mouseY);
            context.getMatrices().pop();

        }
    }

}
