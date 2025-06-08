package net.oleksandr.custom_turrets.registry;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Item.Properties;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.oleksandr.custom_turrets.TurretsMod;
import net.minecraft.world.level.block.Block;

import java.util.HashMap;
import java.util.Map;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, TurretsMod.MOD_ID);

    private static final Map<String, RegistryObject<? extends Block>> BLOCK_ITEMS_TO_REGISTER = new HashMap<>();

    // Stores block references for item auto-registration
    public static void registerBlockItem(String name, RegistryObject<? extends Block> block) {
        BLOCK_ITEMS_TO_REGISTER.put(name, block);
    }

    // Registers all items and block items
    public static void register(IEventBus eventBus) {
        for (Map.Entry<String, RegistryObject<? extends Block>> entry : BLOCK_ITEMS_TO_REGISTER.entrySet()) {
            ITEMS.register(entry.getKey(), () ->
                    new BlockItem(entry.getValue().get(), new Properties()));
        }

        ITEMS.register(eventBus);
    }
}
