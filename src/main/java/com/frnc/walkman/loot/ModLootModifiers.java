package com.frnc.walkman.loot;

import com.frnc.walkman.Walkman;
import com.mojang.serialization.Codec;

import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

/**
 * 全局战利品修饰器的序列化器注册表。
 *
 * <p>注意 Forge 1.20.1 这里是 <strong>Codec 注册表</strong>（注册的是 {@code Codec}），
 * 不是旧的 {@code GlobalLootModifierSerializer} —— 那个类在当前 Forge 版本里已经不存在了。
 * 注册名（这里是 {@code walkman:fishing_walkman}）同时也是 JSON 里 {@code "type"} 字段的取值。
 */
public class ModLootModifiers {

    public static final DeferredRegister<Codec<? extends IGlobalLootModifier>> SERIALIZERS =
            DeferredRegister.create(ForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, Walkman.MOD_ID);

    public static final RegistryObject<Codec<FishingWalkmanModifier>> FISHING_WALKMAN =
            SERIALIZERS.register("fishing_walkman", () -> FishingWalkmanModifier.CODEC);

    private ModLootModifiers() {
    }
}
