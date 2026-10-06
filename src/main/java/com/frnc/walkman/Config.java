package com.frnc.walkman;

import net.minecraftforge.common.ForgeConfigSpec;

// Walkman 的配置文件。Forge 会在 config/ 目录下生成 walkman-common.toml，
// 之后每加一项设置，就在这里声明一个 ForgeConfigSpec 值。
public class Config
{
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    private static final ForgeConfigSpec.BooleanValue LOG_LOADING = BUILDER
            .comment("Whether to log a message when Walkman finishes loading")
            .define("logLoading", true);

    public static final ForgeConfigSpec SPEC = BUILDER.build();

    // 读取配置项必须在配置加载之后，因此提供一个方法而不是 public 字段：
    // 在配置加载前访问 @Nullable 字段会抛异常。
    public static boolean logLoading()
    {
        return LOG_LOADING.get();
    }
}
