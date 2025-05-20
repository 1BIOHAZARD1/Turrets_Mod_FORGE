package net.oleksandr.custom_turrets.registry;

import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.oleksandr.custom_turrets.TurretsMod;
import net.oleksandr.custom_turrets.block.TurretBaseBlock;
import net.minecraftforge.eventbus.api.IEventBus;



public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, TurretsMod.MOD_ID);

    public static final RegistryObject<Block> TURRET_BLOCK =
            BLOCKS.register("turret_block", TurretBaseBlock::new);

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }

}
