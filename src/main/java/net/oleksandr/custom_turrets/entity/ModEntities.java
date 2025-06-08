package net.oleksandr.custom_turrets.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.oleksandr.custom_turrets.TurretsMod;

public class ModEntities {

    // Deferred register for registering custom entity types
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, TurretsMod.MOD_ID);

    // Registering the turret head entity
    public static final RegistryObject<EntityType<TurretHeadEntity>> TURRET_ENTITY =
            ENTITY_TYPES.register("turret_entity", () ->
                    EntityType.Builder.<TurretHeadEntity>of(TurretHeadEntity::new, MobCategory.MISC)
                            .sized(0.8f, 0.8f) // Entity's hitbox width and height
                            .build("turret_entity"));

    // Called during mod initialization to register entities to the Forge event bus
    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
    }
}
