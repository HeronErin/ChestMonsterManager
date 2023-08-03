package me.sweetpickleswine.chestmonstermanager.mixin;


import me.sweetpickleswine.chestmonstermanager.ChestMonsterManager;
import net.minecraft.inventory.Inventory;
import net.minecraft.screen.ScreenHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ScreenHandler.class)
public class ScreenHandlerMixin {

    @Inject(at = @At("HEAD"), method = "checkSize")
    private static void canCheckInject(Inventory inventory, int expectedSize, CallbackInfo ci){

        ChestMonsterManager.ContainingSlots=expectedSize;
    }
}
