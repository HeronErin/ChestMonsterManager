package me.sweetpickleswine.chestmonstermanager.mixin;


import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.screen.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.List;

@Mixin(Screen.class)
public interface ScreenInvoker  {

    @Invoker("addDrawableChild")
    public <T extends Element & Drawable & Selectable> T addDrawableChildInvoker(T drawableElement);

    @Accessor("drawables")
    public List<Drawable> getDrawables();



}
