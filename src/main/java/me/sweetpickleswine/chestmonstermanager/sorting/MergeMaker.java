package me.sweetpickleswine.chestmonstermanager.sorting;

import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class MergeMaker {
    private static List<List<PseudoSlot>> getMergeJobs(InventoryActionTracker inventoryActionTracker){
        List<List<PseudoSlot>> mergeSlotJobs = new ArrayList<>();
        inventoryActionTracker.newInv.forEach((slot)->{
            if (((PseudoSlot)slot).itemStack.getItem().getTranslationKey().equals("block.minecraft.air"))
                return;

            boolean found = false;
            for (List<PseudoSlot> mergeList : mergeSlotJobs){
                if(ItemStack.canCombine(((PseudoSlot)slot).itemStack, mergeList.get(0).itemStack)){
                    mergeList.add((PseudoSlot)slot);
                    found=true;
                    break;
                }

            }
            if (!found){
                List<PseudoSlot> newList = new ArrayList<>();
                newList.add((PseudoSlot) slot);
                mergeSlotJobs.add(newList);
            }


        });
        return mergeSlotJobs;
    }
    public static InventoryActionTracker autoMerge(InventoryActionTracker inventoryActionTracker){
        boolean running = true;
        int i = 0;
        while (running && i < 100) {
            running=false;
            List<List<PseudoSlot>> slotJobs = getMergeJobs(inventoryActionTracker);
            List<List<PseudoSlot>> cleanedJobs = slotJobs.stream().map(
                    slotList ->
                            slotList.stream().filter((slot) ->
                                    slot.itemStack.getCount() != slot.itemStack.getMaxCount()
                            ).toList()).toList();
            for (List<PseudoSlot> mergeList : cleanedJobs) {
                if (mergeList.size() == 0)
                    continue;
                if (ItemStack.canCombine(inventoryActionTracker.cursorStack, mergeList.get(0).itemStack)){
                    inventoryActionTracker=inventoryActionTracker.ClickSlot(mergeList.get(0).id);
                    running=true;
                    break;
                }else{
                    if (mergeList.size() == 1)
                        continue;
                    else {
                        inventoryActionTracker = inventoryActionTracker.ClickSlot(mergeList.get(0).id);
                        inventoryActionTracker = inventoryActionTracker.ClickSlot(mergeList.get(1).id);
                        running = true;
                        break;
                    }

                }

            }
            i++;

        }


        return inventoryActionTracker;
    }
}
