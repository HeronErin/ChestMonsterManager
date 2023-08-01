package me.sweetpickleswine.chestmonstermanager.sorting;

import me.sweetpickleswine.chestmonstermanager.ChestMonsterManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class InventoryActionTracker<T extends ScreenHandler> {
    public T screenHandler;
    public ItemStack cursorStack;

    public List<PseudoSlot> newInv = new ArrayList<>();
    public InventoryActionTracker(T _screenHandler){
        screenHandler=_screenHandler;
        cursorStack=screenHandler.getCursorStack().copy();
        for (Slot slot : screenHandler.slots){
            newInv.add(new PseudoSlot(slot));
        }
    }
    public InventoryActionTracker(T _screenHandler, ItemStack _cursorStack, List<PseudoSlot> _newInv){
        screenHandler=_screenHandler;
        cursorStack=_cursorStack;
        newInv=_newInv;
    }

    public void render(DrawContext context, int mouseX, int mouseY, int x, int y){
        TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
        Screen cs = MinecraftClient.getInstance().currentScreen;
        for (PseudoSlot slot : newInv){

            context.drawItem(slot.itemStack, slot.x + x, slot.y +y, slot.x + slot.y);
            int count = slot.itemStack.getCount();
            if (count != 1)
                context.drawItemInSlot(textRenderer, slot.itemStack, slot.x+x, slot.y+y, String.valueOf(count));
        }
        context.drawItem(cursorStack, mouseX , mouseY , mouseX + mouseY);
        int count = cursorStack.getCount();
        if (count != 1)
            context.drawItemInSlot(textRenderer, cursorStack, mouseX, mouseY, String.valueOf(count));
    }

    public InventoryActionTracker ClickSlot(int id){


        if (cursorStack.isEmpty()){

            List<PseudoSlot> newList = newInv.stream().map(
                    (slot) -> (
                            slot.id==id ? new PseudoSlot(Items.AIR.getDefaultStack(), slot.x, slot.y, slot.id)
                                    : slot.copy()
                    )).collect(Collectors.toList());

            return new InventoryActionTracker(screenHandler, getSlotById(id).itemStack.copy(), newList);

        }
        else if (getSlotById(id).itemStack.isEmpty()){

            List<PseudoSlot> newList = newInv.stream().map(
                    (slot) -> (
                            slot.id==id ? new PseudoSlot(cursorStack.copy(), slot.x, slot.y, slot.id)
                                    : slot.copy()
                    )).collect(Collectors.toList());

            return new InventoryActionTracker(screenHandler, Items.AIR.getDefaultStack(), newList);
        } else{
            if (ItemStack.canCombine(getSlotById(id).itemStack, cursorStack)){
                int newCount = getSlotById(id).itemStack.getCount() + cursorStack.getCount();
                if (newCount <= cursorStack.getMaxCount()){
                    ItemStack newStack = cursorStack.copy();
                    newStack.setCount(newCount);
                    List<PseudoSlot> newList = newInv.stream().map(
                            (slot) -> (
                                    slot.id==id ? new PseudoSlot(newStack, slot.x, slot.y, slot.id)
                                            : slot.copy()
                            )).collect(Collectors.toList());

                    return new InventoryActionTracker(screenHandler, Items.AIR.getDefaultStack(), newList);
                }else{
                    ItemStack newStack = cursorStack.copy();
                    newStack.setCount(cursorStack.getMaxCount());
                    List<PseudoSlot> newList = newInv.stream().map(
                            (slot) -> (
                                    slot.id==id ? new PseudoSlot(newStack, slot.x, slot.y, slot.id)
                                            : slot.copy()
                            )).collect(Collectors.toList());
                    ItemStack newCStack = cursorStack.copy();
                    newCStack.setCount(newCount-cursorStack.getMaxCount());
                    return new InventoryActionTracker(screenHandler, newCStack, newList);
                }
            }else{
                ItemStack newStack = cursorStack.copy();
                ItemStack newCStack = getSlotById(id).itemStack.copy();

                List<PseudoSlot> newList = newInv.stream().map(
                        (slot) -> (
                                slot.id==id ? new PseudoSlot(newStack, slot.x, slot.y, slot.id)
                                        : slot.copy()
                        )).collect(Collectors.toList());


                return new InventoryActionTracker(screenHandler, newCStack, newList);
            }
            }






    }
    public PseudoSlot getSlotById(int id){
        for (PseudoSlot slot : newInv){
            if (slot.id == id)
                return slot;
        }
        return null;
    }

}
