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
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, TurretsMod.MOD_ID);

    public static final RegistryObject<EntityType<TurretHeadEntity>> TURRET_ENTITY =
            ENTITY_TYPES.register("turret_entity", () ->
                    EntityType.Builder.<TurretHeadEntity>of(TurretHeadEntity::new, MobCategory.MISC)
                            .sized(0.8f, 0.8f) // розмір ентіті
                            .build("turret_entity"));

    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
    }
}
