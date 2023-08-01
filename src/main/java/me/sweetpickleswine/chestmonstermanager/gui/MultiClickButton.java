package me.sweetpickleswine.chestmonstermanager.gui;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TexturedButtonWidget;
import net.minecraft.client.input.KeyCodes;
import net.minecraft.util.Identifier;

public class MultiClickButton extends TexturedButtonWidget {
    @Override
    public boolean isSelected(){
        return this.isHovered();
    }
    public ButtonWidget.PressAction onMiddlePress;
    public ButtonWidget.PressAction onRightPress;
    public MultiClickButton(int x, int y, int width, int height, int u, int v, int hoveredVOffset, Identifier texture, ButtonWidget.PressAction pressAction, ButtonWidget.PressAction middleClick, ButtonWidget.PressAction rightClick ) {
        super(x, y, width, height, u, v, hoveredVOffset, texture, 256, 256, pressAction);
        onRightPress = rightClick;
        onMiddlePress = middleClick;
    }
    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button){
        if (this.isHovered()) {
            switch (button){
                case 0:
                    this.onPress.onPress(this);
                    return true;
                case 1:
                    this.onRightPress.onPress(this);
                    return true;
                case 2:
                    this.onMiddlePress.onPress(this);
                    return true;

            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }
}
