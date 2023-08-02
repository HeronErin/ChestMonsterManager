package me.sweetpickleswine.chestmonstermanager.sorting;

import me.sweetpickleswine.chestmonstermanager.ChestMonsterManager;
import me.sweetpickleswine.chestmonstermanager.config.Config;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.GenericContainerScreen;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.Generic3x3ContainerScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class InventoryActionTracker<T extends net.minecraft.screen.ScreenHandler> {
    public T screenHandler;
    public ItemStack cursorStack;

    private DoInclude doInclude = null;

    public List<PseudoSlot> newInv = new ArrayList<>();
    public InventoryActionTracker(T _screenHandler){
        screenHandler=_screenHandler;
        cursorStack=screenHandler.getCursorStack().copy();
        for (Slot slot : screenHandler.slots){
            // Safety for certain lockable slots
            if (slot.canTakeItems(MinecraftClient.getInstance().player) && slot.canTakePartial(MinecraftClient.getInstance().player))
                newInv.add(new PseudoSlot(slot));
        }
        actionHistory=new ArrayList<>();
    }
    interface DoInclude{
        boolean DoInclude(PseudoSlot slot);
    }
    public InventoryActionTracker(T _screenHandler, DoInclude doInclude){
        this(_screenHandler);

        newInv = newInv.stream().filter(doInclude::DoInclude).toList();


    }

    public InventoryActionTracker(T _screenHandler, ItemStack _cursorStack, List<PseudoSlot> _newInv, List<Pair<Action, int[]>> _actionHistory){
        screenHandler=_screenHandler;
        cursorStack=_cursorStack;
        newInv=_newInv;
        actionHistory=_actionHistory;
    }

    public void render(DrawContext context, int mouseX, int mouseY, int x, int y){
        TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;

        for (PseudoSlot slot : newInv){
            int slotX = slot.x+x;
            int slotY = slot.y+y;
            context.drawItem(slot.itemStack, slotX, slotY, slot.x + slot.y);

            if (!ItemStack.areEqual(screenHandler.slots.stream().filter((s) -> s.id == slot.id).toList().get(0).getStack(), slot.itemStack))
                context.fill(slotX, slotY, slotX+16 ,slotY+16, 822018048);

            int count = slot.itemStack.getCount();
            if (count != 1)
                context.drawItemInSlot(textRenderer, slot.itemStack, slotX, slotY, String.valueOf(count));
        }
        context.drawItem(cursorStack, mouseX , mouseY , mouseX + mouseY);
        int count = cursorStack.getCount();
        if (count != 1)
            context.drawItemInSlot(textRenderer, cursorStack, mouseX, mouseY, String.valueOf(count));
    }
    public List<Pair<Action, int[]>> actionHistory;
    public InventoryActionTracker ClickSlot(int id){
        List<Pair<Action, int[]>> newActionHistory = new ArrayList<>(actionHistory);
        newActionHistory.add(new Pair<>(Action.CLICK_SLOT, new int[]{id}));

        if (cursorStack.isEmpty()){

            List<PseudoSlot> newList = newInv.stream().map(
                    (slot) -> (
                            slot.id==id ? new PseudoSlot(Items.AIR.getDefaultStack(), slot.x, slot.y, slot.id)
                                    : slot.copy()
                    )).collect(Collectors.toList());

            return new InventoryActionTracker(screenHandler, getSlotById(id).itemStack.copy(), newList, newActionHistory);

        }
        else if (getSlotById(id).itemStack.isEmpty()){

            List<PseudoSlot> newList = newInv.stream().map(
                    (slot) -> (
                            slot.id==id ? new PseudoSlot(cursorStack.copy(), slot.x, slot.y, slot.id)
                                    : slot.copy()
                    )).collect(Collectors.toList());

            return new InventoryActionTracker(screenHandler, Items.AIR.getDefaultStack(), newList, newActionHistory);
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

                    return new InventoryActionTracker(screenHandler, Items.AIR.getDefaultStack(), newList, newActionHistory);
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
                    return new InventoryActionTracker(screenHandler, newCStack, newList, newActionHistory);
                }
            }else{
                ItemStack newStack = cursorStack.copy();
                ItemStack newCStack = getSlotById(id).itemStack.copy();

                List<PseudoSlot> newList = newInv.stream().map(
                        (slot) -> (
                                slot.id==id ? new PseudoSlot(newStack, slot.x, slot.y, slot.id)
                                        : slot.copy()
                        )).collect(Collectors.toList());


                return new InventoryActionTracker(screenHandler, newCStack, newList, newActionHistory);
            }
            }






    }

    public void commit(){
        final int ogId = screenHandler.syncId;
        new Thread(()->{


            for (final Pair<Action, int[]> action : actionHistory){
                switch (action.getLeft()){
                    case CLICK_SLOT:

                        final int id = MinecraftClient.getInstance().player.currentScreenHandler.syncId;

                        if (ogId != id){
                            ChestMonsterManager.LOGGER.warn("User closed inventory during sort commit");
                            return;
                        }


                        MinecraftClient.getInstance().execute(()->
                                MinecraftClient.getInstance().interactionManager.clickSlot
                                        (id, (action.getRight()[0]), 0, SlotActionType.PICKUP, MinecraftClient.getInstance().player));
                }
                try {
                    Thread.sleep(Config.SORT_ACTION_DELAY.getIntegerValue());
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();
    }

    public enum Action{
        CLICK_SLOT
    }
    public PseudoSlot getSlotById(int id){
        for (PseudoSlot slot : newInv){
            if (slot.id == id)
                return slot;
        }
        return null;
    }

}
