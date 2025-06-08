package net.oleksandr.custom_turrets.registry;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.oleksandr.custom_turrets.TurretsMod;
import net.oleksandr.custom_turrets.entity.TurretHeadEntity;

public class ModEntities {
    // Entity register
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, TurretsMod.MOD_ID);

    // Turret entity
    public static final RegistryObject<EntityType<TurretHeadEntity>> TURRET_HEAD =
            ENTITY_TYPES.register("turret_head", () ->
                    EntityType.Builder.<TurretHeadEntity>of(TurretHeadEntity::new, MobCategory.MISC)
                            .sized(0.6F, 0.6F)
                            .clientTrackingRange(8)
                            .build("turret_head"));
}
