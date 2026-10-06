package com.frnc.walkman.loot;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.common.loot.LootModifier;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * 往战利品里追加一件指定物品的全局战利品修饰器。
 *
 * <p>当前唯一用途是让钓鱼有极低概率钓到随身听：{@code data/walkman/loot_modifiers/fishing_walkman.json}
 * 里用 {@code forge:loot_table_id} 条件限定只对 {@code minecraft:gameplay/fishing} 生效，
 * 再用 {@code minecraft:random_chance} 控制概率。
 *
 * <p>为什么必须走 Global Loot Modifier 而不是 {@code ItemFishedEvent}：
 * 那个事件的构造器对掉落列表做了拷贝，而 {@code FishingHook.retrieve()} 真正生成掉落物时
 * 遍历的是它自己的局部列表，所以改 {@code ItemFishedEvent#getDrops()} 完全无效且不会报错。
 * 修改钓鱼产出只能从战利品表这一层入手。
 *
 * <p>条件判定由基类 {@link LootModifier} 负责（它会把 {@code conditions} 编译成一个
 * 合并后的 {@link java.util.function.Predicate}），因此 {@link #doApply} 只在实际命中时才被调用。
 */
public class FishingWalkmanModifier extends LootModifier {

    /**
     * 只暴露一个字段 {@code item}；概率与生效的战利品表都由 JSON 里的 conditions 决定，
     * 这样调整概率不需要改代码，改数据包即可。
     */
    public static final Codec<FishingWalkmanModifier> CODEC = RecordCodecBuilder.create(inst -> codecStart(inst)
            .and(ForgeRegistries.ITEMS.getCodec().fieldOf("item").forGetter(m -> m.item))
            .apply(inst, FishingWalkmanModifier::new));

    private final Item item;

    public FishingWalkmanModifier(LootItemCondition[] conditions, Item item) {
        super(conditions);
        this.item = item;
    }

    @Override
    public Codec<? extends IGlobalLootModifier> codec() {
        return CODEC;
    }

    @Override
    protected ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
        generatedLoot.add(new ItemStack(item));
        return generatedLoot;
    }
}
