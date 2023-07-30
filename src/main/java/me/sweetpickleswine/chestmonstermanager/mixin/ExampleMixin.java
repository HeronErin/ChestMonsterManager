package me.sweetpickleswine.chestmonstermanager.mixin;

import me.sweetpickleswine.chestmonstermanager.ChestMonsterManager;
import net.minecraft.client.gui.screen.TitleScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TitleScreen.class)
public class ExampleMixin {
	private static boolean firstRun = true;


	@Inject(at = @At("HEAD"), method = "init()V")
	private void init(CallbackInfo info) {
		if (ExampleMixin.firstRun){
			ChestMonsterManager.LOGGER.info("This line is printed by an example mod mixin!");
			ExampleMixin.firstRun=false;
		}


	}
}