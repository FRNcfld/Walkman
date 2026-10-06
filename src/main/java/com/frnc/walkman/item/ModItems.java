package com.frnc.walkman.item;

import com.frnc.walkman.Walkman;

import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

/**
 * 本模组的物品注册表。
 */
public class ModItems {

    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, Walkman.MOD_ID);

    /**
     * 随身听。普通物品，不可堆叠。
     *
     * <p>能放进 Curios 腰带槽位靠的是数据包标签 {@code data/curios/tags/items/belt.json}，
     * 不涉及代码 —— 这样未安装 Curios 时不会有任何类被加载。
     */
    public static final RegistryObject<Item> WALKMAN =
            ITEMS.register("walkman", () -> new Item(new Item.Properties().stacksTo(1)));

    private ModItems() {
    }
}
