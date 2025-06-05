package net.oleksandr.custom_turrets.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;


public class TurretHeadEntity extends Entity {
    public TurretHeadEntity(EntityType<? extends TurretHeadEntity> type, Level level) {
        super(type, level);
    }

    @Override
    protected void defineSynchedData() {}

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {}

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {}

    @Override
    public void tick() {
        super.tick();

        if (this.level().isClientSide) return;

        LivingEntity target = this.level().getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(8.0),
                        e -> e != null && e.isAlive() && !e.getType().equals(this.getType()))
                .stream().findFirst().orElse(null);

        if (target != null) {
            this.lookAt(target);
        }
    }




    private void lookAt(LivingEntity target) {
        double dx = target.getX() - this.getX();
        double dz = target.getZ() - this.getZ();
        double dy = target.getEyeY() - this.getEyeY();

        double dist = Math.sqrt(dx * dx + dz * dz);
        float yaw = (float)(Math.toDegrees(Math.atan2(dz, dx))) - 90F;
        float pitch = (float)(-Math.toDegrees(Math.atan2(dy, dist)));

        this.setYRot(yaw);
        this.setXRot(pitch);
        this.yRotO = yaw;
        this.xRotO = pitch;
    }

}
