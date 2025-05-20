package net.oleksandr.custom_turrets.registry;

import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.oleksandr.custom_turrets.TurretsMod;
import net.oleksandr.custom_turrets.block.TurretBaseBlockEntity;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, TurretsMod.MOD_ID);

    public static final RegistryObject<BlockEntityType<TurretBaseBlockEntity>> TURRET_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("turret_block_entity", () ->
                    BlockEntityType.Builder.of(TurretBaseBlockEntity::new,
                            ModBlocks.TURRET_BLOCK.get()).build(null)
            );

    public static void register() {
        BLOCK_ENTITIES.register(FMLJavaModLoadingContext.get().getModEventBus());
    }
}
