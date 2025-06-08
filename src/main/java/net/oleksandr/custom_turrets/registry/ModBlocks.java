package net.oleksandr.custom_turrets.registry;

import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.eventbus.api.IEventBus;
import net.oleksandr.custom_turrets.TurretsMod;
import net.oleksandr.custom_turrets.block.TurretBaseBlock;

public class ModBlocks {
    // Register blocks
    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, TurretsMod.MOD_ID);

    // Turret base block
    public static final RegistryObject<Block> TURRET_BLOCK =
            registerBlock("turret_block", TurretBaseBlock::new);

    // Helper to register block and item
    private static <T extends Block> RegistryObject<T> registerBlock(String name, java.util.function.Supplier<T> blockSupplier) {
        RegistryObject<T> block = BLOCKS.register(name, blockSupplier);
        ModItems.registerBlockItem(name, block); // auto-register BlockItem
        return block;
    }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}
