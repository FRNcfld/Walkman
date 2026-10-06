package com.frnc.walkman;

import com.frnc.walkman.item.ModItems;
import com.frnc.walkman.loot.ModLootModifiers;
import com.mojang.logging.LogUtils;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

/**
 * Walkman 随身听主入口。
 *
 * <p>本 mod 面向 1.20.1 / Forge，目标是在游戏内播放玩家自己的音乐。
 *
 * <p>当前内容：
 * <ul>
 *   <li><b>随身听</b>：不可堆叠的物品，可放进 Curios 腰带饰品栏（数据包标签实现）。
 *       由唱片机在切石机中切制获得，钓鱼时也有 0.1% 的极低概率钓到（Global Loot Modifier）。</li>
 * </ul>
 *
 * <p>随身听目前<strong>还不能播放音乐</strong>，只是一个可获取、可佩戴的物品。
 *
 * <p>Curios 是可选前置（依赖声明见 {@code build.gradle} 与 {@code META-INF/mods.toml}）：
 * 未安装 Curios 的玩家同样能正常加载本模组 —— 本项目不引用任何 Curios 类，
 * 腰带槽位完全靠数据包标签实现。
 */
@Mod(Walkman.MOD_ID)
public class Walkman {

    /** 本 mod 的命名空间，需与 META-INF/mods.toml 中的 modId 一致。 */
    public static final String MOD_ID = "walkman";

    private static final Logger LOGGER = LogUtils.getLogger();

    /** 快捷创建本 mod 的 ResourceLocation。 */
    public static ResourceLocation id(String path) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }

    public Walkman(FMLJavaModLoadingContext context) {
        IEventBus modEventBus = context.getModEventBus();

        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);

        // 把随身听加进原版"功能方块"页签（唱片机所在的那个）
        modEventBus.addListener(this::addCreative);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);

        ModItems.ITEMS.register(modEventBus);
        ModLootModifiers.SERIALIZERS.register(modEventBus);

        // 服务端/世界机制集中在 COMMON 配置: config/walkman-common.toml
        context.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    /**
     * 把本模组的物品塞进原版创造模式页签。
     *
     * <p>选 FUNCTIONAL_BLOCKS（功能方块）是因为唱片机在那里；唱片机同时还出现在
     * REDSTONE_BLOCKS（红石方块）里，这里只跟其中一处保持一致。
     */
    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.FUNCTIONAL_BLOCKS) {
            event.accept(ModItems.WALKMAN);
        }
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        // 目前没有需要 enqueueWork 的主线程初始化工作。
        if (Config.logLoading()) {
            LOGGER.info("Walkman common setup complete");
        }
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        // Do something when the server starts
        LOGGER.info("Walkman loaded on server start");
    }
}
