package me.sweetpickleswine.chestmonstermanager.sorting;

import me.sweetpickleswine.chestmonstermanager.config.ListOptions.SortType;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.*;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

// https://github.com/kyrptonaught/Inventory-Sorter/blob/1.19/src/main/java/net/kyrptonaught/inventorysorter/SortCases.java#L82
public class SortCases {

    static String getStringForSort(ItemStack stack, SortType sortType) {
        Item item = stack.getItem();
        String itemName = specialCases(stack);
        switch (sortType) {
            case BY_CAT -> {
                ItemGroup group = getFirstItemGroup(stack);
                return (group != null ? group.getDisplayName().getString() : "zzz") + itemName;
            }

            case NAME -> {
                if (stack.hasCustomName()) return stack.getName() + itemName;
            }

        }


        return itemName;
    }

    private static ItemGroup getFirstItemGroup(ItemStack stack) {
        List<ItemGroup> groups = ItemGroups.getGroups();
        for (ItemGroup group : groups) {
            if (group.contains(new ItemStack(stack.getItem())))
                return group;

        }
        return null;
    }

    private static String specialCases(ItemStack stack) {
        Item item = stack.getItem();
        NbtCompound tag = stack.getNbt();

        if (tag != null && tag.contains("SkullOwner"))
            return playerHeadCase(stack);
        if (stack.getCount() != stack.getMaxCount())
            return stackSize(stack);
        if (item instanceof EnchantedBookItem)
            return enchantedBookNameCase(stack);
        if (item instanceof ToolItem)
            return toolDuribilityCase(stack);
        return item.toString();
    }

    private static String playerHeadCase(ItemStack stack) {
        NbtCompound tag = stack.getNbt();
        NbtCompound skullOwner = tag.getCompound("SkullOwner");
        String ownerName = skullOwner.getString("Name");

        // this is duplicated logic, so we should probably refactor
        String count = "";
        if (stack.getCount() != stack.getMaxCount()) {
            count = Integer.toString(stack.getCount());
        }

        return stack.getItem().toString() + " " + ownerName + count;
    }

    private static String stackSize(ItemStack stack) {
        return stack.getItem().toString() + stack.getCount();
    }

    private static String enchantedBookNameCase(ItemStack stack) {
        NbtList enchants = EnchantedBookItem.getEnchantmentNbt(stack);
        List<String> names = new ArrayList<>();
        StringBuilder enchantNames = new StringBuilder();
        for (int i = 0; i < enchants.size(); i++) {
            NbtCompound enchantTag = enchants.getCompound(i);
            Identifier enchantID = Identifier.tryParse(enchantTag.getString("id"));
            if (enchantID == null) continue;
            Enchantment enchant = Registries.ENCHANTMENT.get(enchantID);
            if (enchant == null) continue;
            names.add(enchant.getName(enchantTag.getInt("lvl")).getString());
        }
        Collections.sort(names);
        for (String enchant : names) {
            enchantNames.append(enchant).append(" ");
        }
        return stack.getItem().toString() + " " + enchants.size() + " " + enchantNames;
    }

    private static String toolDuribilityCase(ItemStack stack) {
        return stack.getItem().toString() + stack.getDamage();
    }
}