package me.sweetpickleswine.chestmonstermanager.sorting;

import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.Slot;

public class PseudoSlot {
    public ItemStack itemStack;
    public int x;
    public int y;
    public int id;
    public PseudoSlot(Slot s){
        itemStack = s.getStack().copy();
        x=s.x;
        y=s.y;
        id=s.id;
    }
    public PseudoSlot(ItemStack _itemStack, int _x, int _y, int _id){
        itemStack=_itemStack.copy();
        x=_x;y=_y;id=_id;
    }

    public PseudoSlot copy() {
        return new PseudoSlot(itemStack.copy(), x, y, id);
    }
}
