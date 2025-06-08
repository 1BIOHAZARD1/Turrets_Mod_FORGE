package net.oleksandr.custom_turrets;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mod.EventBusSubscriber(modid = TurretsMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Config {
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    // Example boolean option
    private static final ForgeConfigSpec.BooleanValue LOG_DIRT_BLOCK = BUILDER
            .comment("Log dirt block on setup")
            .define("logDirtBlock", true);

    // Example integer
    private static final ForgeConfigSpec.IntValue MAGIC_NUMBER = BUILDER
            .comment("A magic number")
            .defineInRange("magicNumber", 42, 0, Integer.MAX_VALUE);

    // Example string
    public static final ForgeConfigSpec.ConfigValue<String> MAGIC_NUMBER_INTRO = BUILDER
            .comment("Message prefix for the magic number")
            .define("magicNumberIntroduction", "The magic number is... ");

    // Item list from config
    private static final ForgeConfigSpec.ConfigValue<List<? extends String>> ITEM_STRINGS = BUILDER
            .comment("List of item IDs to log")
            .defineListAllowEmpty("items", List.of("minecraft:iron_ingot"), Config::isValidItem);

    public static final ForgeConfigSpec SPEC = BUILDER.build();

    public static boolean logDirtBlock;
    public static int magicNumber;
    public static String magicNumberIntro;
    public static Set<Item> items;

    // Validate if the string is a valid item ID
    private static boolean isValidItem(Object obj) {
        return obj instanceof String id && ForgeRegistries.ITEMS.containsKey(new ResourceLocation(id));
    }

    // Load config values when applied
    @SubscribeEvent
    static void onLoad(ModConfigEvent event) {
        logDirtBlock = LOG_DIRT_BLOCK.get();
        magicNumber = MAGIC_NUMBER.get();
        magicNumberIntro = MAGIC_NUMBER_INTRO.get();
        items = ITEM_STRINGS.get().stream()
                .map(id -> ForgeRegistries.ITEMS.getValue(new ResourceLocation(id)))
                .collect(Collectors.toSet());
    }
}
