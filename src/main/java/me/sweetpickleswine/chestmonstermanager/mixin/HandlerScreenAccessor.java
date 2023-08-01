package me.sweetpickleswine.chestmonstermanager.mixin;

import net.minecraft.client.gui.screen.ingame.HandledScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(HandledScreen.class)
public interface HandlerScreenAccessor {

    @Accessor("backgroundWidth")
    public int getBackgroundWidth();
    @Accessor("backgroundHeight")
    public int getBackgroundHeight();
}
