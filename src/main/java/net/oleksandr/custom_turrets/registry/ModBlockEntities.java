package net.oleksandr.custom_turrets.registry;

import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.oleksandr.custom_turrets.TurretsMod;
import net.oleksandr.custom_turrets.block.TurretBaseBlockEntity;
import net.minecraftforge.eventbus.api.IEventBus;

public class ModBlockEntities {
    // Register block entities
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, TurretsMod.MOD_ID);

    // Turret base block entity
    public static final RegistryObject<BlockEntityType<TurretBaseBlockEntity>> TURRET_BASE =
            BLOCK_ENTITIES.register("turret_base", () ->
                    BlockEntityType.Builder.of(TurretBaseBlockEntity::new,
                            ModBlocks.TURRET_BLOCK.get()).build(null));

    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }
}
