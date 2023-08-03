package me.sweetpickleswine.chestmonstermanager.sorting;

import me.sweetpickleswine.chestmonstermanager.ChestMonsterManager;
import me.sweetpickleswine.chestmonstermanager.config.ListOptions.SortType;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

import static me.sweetpickleswine.chestmonstermanager.sorting.SortCases.getStringForSort;

public class sort {
    private static ItemGroup getFirstItemGroup(ItemStack stack) {
        List<ItemGroup> groups = ItemGroups.getGroups();
        for (ItemGroup group : groups) {
            if (group.contains(new ItemStack(stack.getItem())))
                return group;

        }
        return null;
    }
    static boolean isItemAir(PseudoSlot slot){
        return slot.itemStack.getTranslationKey().equals("block.minecraft.air");
    }

    public static InventoryActionTracker<? extends net.minecraft.screen.ScreenHandler> nameSort(InventoryActionTracker<? extends net.minecraft.screen.ScreenHandler> inventoryActionTracker, SortType sortType){
        inventoryActionTracker=MergeMaker.autoMerge(inventoryActionTracker);
        List<PseudoSlot> newSlots = inventoryActionTracker.newInv.stream().sorted((o1, o2) -> {
            PseudoSlot slot1 = o1;
            PseudoSlot slot2 = o2;
            boolean slot1IsAir = isItemAir(slot1);
            boolean slot2IsAir = isItemAir(slot2);
            if (slot1IsAir && slot2IsAir)
                return 0;
            if (slot1IsAir)
                return 1;
            if (slot2IsAir)
                return -1;

            return getStringForSort(slot1.itemStack, sortType).compareTo(getStringForSort(slot2.itemStack, sortType));



        }).collect(Collectors.toList());

        return oldToSorted(newSlots, inventoryActionTracker);
    }
    public static InventoryActionTracker<? extends net.minecraft.screen.ScreenHandler> oldToSorted(List<PseudoSlot> newL, InventoryActionTracker<? extends net.minecraft.screen.ScreenHandler> inventoryActionTracker){
        List<PseudoSlot> oldList = new ArrayList<>(inventoryActionTracker.newInv);


        // Actually do the swaps
        for (int i =0; i < inventoryActionTracker.newInv.size(); i ++){
            ItemStack temp = newL.get(i).itemStack;
            Optional<PseudoSlot> temp2 = inventoryActionTracker.newInv.subList(i, inventoryActionTracker.newInv.size()).stream().filter((ps)-> ItemStack.areEqual(ps.itemStack, temp)).findFirst();
            if (temp2.isEmpty()){
                ChestMonsterManager.LOGGER.error("Unexpected change of items from merge");
                return inventoryActionTracker;
            }
            PseudoSlot moveTo = inventoryActionTracker.newInv.get(i);
            PseudoSlot moveFrom = temp2.get();
//            newL.remove(moveFrom);
            if (isItemAir(moveTo) && isItemAir(moveFrom))
                continue;
            if (moveTo.id == moveFrom.id)
                continue;
            if (isItemAir(moveTo) && !isItemAir(moveFrom)){
                inventoryActionTracker=inventoryActionTracker.ClickSlot(moveFrom.id);
                inventoryActionTracker=inventoryActionTracker.ClickSlot(moveTo.id);
                continue;
            }
            if (!isItemAir(moveTo) && isItemAir(moveFrom)){
                inventoryActionTracker=inventoryActionTracker.ClickSlot(moveTo.id);
                inventoryActionTracker=inventoryActionTracker.ClickSlot(moveFrom.id);
                continue;
            }
            if (!isItemAir(moveTo) && !isItemAir(moveFrom)){
                inventoryActionTracker=inventoryActionTracker.ClickSlot(moveFrom.id);
                inventoryActionTracker=inventoryActionTracker.ClickSlot(moveTo.id);
                inventoryActionTracker=inventoryActionTracker.ClickSlot(moveFrom.id);
                continue;
            }





        }




        return inventoryActionTracker;
    }

}
