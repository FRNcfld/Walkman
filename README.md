# Walkman 随身听

面向 Minecraft **1.20.1 / Forge** 的模组，目标是在游戏内播放你自己的音乐。

> 当前状态：已加入**随身听物品**，但**还不能播放音乐** —— 它目前只是一个可获取、可佩戴的物品。

## 已实现内容

### 随身听（`walkman:walkman`）

不可堆叠的物品，贴图为 16×16 的 `walk_man.png`。

| 途径 | 说明 |
|---|---|
| **切石机** | 拿唱片机切制，产出 1 个随身听 |
| **钓鱼** | 0.1%（千分之一）的极低概率钓到，见下方「钓鱼概率」 |
| **创造模式** | 原版「功能方块」页签（唱片机所在处） |
| **饰品栏** | 装了 Curios 时可放进**腰带**槽位 |

**钓鱼概率**写在 `data/walkman/loot_modifiers/fishing_walkman.json` 的 `chance` 字段里，
用数据包即可调整；调成 `1.0` 便于测试（`/loot give @s loot minecraft:gameplay/fishing` 会触发它）。

## 环境要求

| 项目 | 版本 |
|---|---|
| Minecraft | 1.20.1 |
| Forge | 47.x（`forge_version_range=[47,)`） |
| Java | 17（Gradle toolchain 已固定，无需手动切换） |
| Gradle | 8.8（由 wrapper 提供，直接执行 `gradlew` 即可） |
| 映射 | Parchment `2023.08.20-1.20.1`（叠加官方映射，与同仓库其它项目一致） |

## 项目结构

| 路径 | 说明 |
|---|---|
| `src/main/java/com/frnc/walkman/Walkman.java` | 模组主类，`@Mod(Walkman.MOD_ID)`，`MOD_ID = "walkman"`，含 `id()` 快捷方法 |
| `src/main/java/com/frnc/walkman/Config.java` | Forge 配置，生成 `config/walkman-common.toml` |
| `src/main/java/com/frnc/walkman/item/ModItems.java` | 物品注册表（随身听） |
| `src/main/java/com/frnc/walkman/loot/` | 钓鱼用的全局战利品修饰器及其序列化器注册 |
| `src/main/resources/META-INF/mods.toml` | 模组元数据，占位符由 `gradle.properties` 注入 |
| `src/main/resources/assets/walkman/` | 语言文件、物品模型与贴图 |
| `src/main/resources/data/curios/tags/items/belt.json` | 把随身听加进 Curios 腰带槽位 |
| `src/main/resources/data/walkman/recipes/` | 切石机配方 |
| `src/main/resources/data/forge/loot_modifiers/` | 向 Forge 注册本模组的战利品修饰器 |
| `gradle.properties` | 模组 id、版本、作者、Minecraft / Forge / 映射版本、可选前置版本等集中配置 |
| `settings.gradle` | 插件仓库（Forge、Parchment 等） |

> `lang/` 里的 `itemGroup.walkman` 键目前**没有被使用**：随身听是挂进原版「功能方块」页签的，
> 没有自建页签。这个键留着以便日后需要自建页签时使用。

> **模型文件名必须与物品注册名一致**：物品 id 是 `walkman`，所以模型必须是
> `models/item/walkman.json`。贴图文件名不受此限制（由模型里的 `layer0` 显式指定），
> 因此贴图仍叫 `walk_man.png`。

## 构建与运行

| 任务 | 命令 | 说明 |
|---|---|---|
| **编译** | `./gradlew build` | 编译并打包，产物在 `build/libs/` |
| **仅编译** | `./gradlew compileJava` | 快速检查语法与依赖 |
| **启动客户端** | `./gradlew runClient` | 使用 `run/` 作为运行目录 |
| **启动服务端** | `./gradlew runServer` | 无 GUI，同样使用 `run/` |
| **数据生成** | `./gradlew runData` | 输出到 `src/generated/resources/` |
| **刷新依赖** | `./gradlew --refresh-dependencies` | 依赖出现异常时使用；切换映射后也需要 |

> Windows 下把 `./gradlew` 换成 `gradlew.bat` 即可。

## 依赖说明

| 依赖 | 声明方式 | 为什么 |
|---|---|---|
| **Forge** / **Minecraft** | `mods.toml` 中 `mandatory=true` | 运行本模组的必需前置 |
| **Curios API** | **软依赖**：`compileOnly` + `runtimeOnly`，`mods.toml` 中 `mandatory=false` | 随身听可佩戴在腰带槽位所需。既不会被打进本 mod 的 jar，也不会成为玩家的必需依赖——没装 Curios 的玩家照样能正常加载本模组 |

> 本模组**不引用任何 Curios 类**：腰带槽位完全靠数据包标签
> `data/curios/tags/items/belt.json` 实现，因此未安装 Curios 时不存在类加载风险。
> 若日后需要写 `ICurio` 实现或调用 `CuriosApi`，必须先把引用集中到单独的类里，
> 并在调用前判断 `ModList.get().isLoaded("curios")`——否则加载到那个类的瞬间就会
> `NoClassDefFoundError`。这套隔离写法可参考同目录 `better_experience` 的
> `spyglass/integration/CuriosLookup.java`。

## 开发环境

| IDE | 步骤 |
|---|---|
| **IntelliJ IDEA** | 直接以 Gradle 项目导入根目录的 `build.gradle`；导入后按提示刷新 Gradle 项目。需要时执行 `gradlew genIntellijRuns` |
| **Eclipse** | 执行 `gradlew genEclipseRuns`，再以 `Import > Existing Gradle Project` 导入 |

## 开发约定

| 项目 | 约定 |
|---|---|
| **包名** | `com.frnc.<mod_id>`（本项目为 `com.frnc.walkman`） |
| **模组 id** | `walkman`，小写下划线风格；主类常量名为 `MOD_ID`（与同仓库其它项目一致），需与 `mods.toml` 中的 `modId` 保持一致 |
| **资源路径** | 用 `Walkman.id("xxx")` 生成 `ResourceLocation`，不要手写 `new ResourceLocation(...)` |
| **中文文本** | `mod_name` / `mod_description` 含中文，由 `build.gradle` 的 `readUtf8Property` 按 UTF-8 重新读取。**不要改名这两个键**，否则中文会乱码 |
| **许可证** | MIT，见 [LICENSE](LICENSE) |
| **作者** | FRNc |

## 许可证

本项目使用 [MIT](LICENSE) 许可证。
