package net.oleksandr.custom_turrets.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.oleksandr.custom_turrets.block.TurretBaseBlockEntity;
import javax.annotation.Nullable;


public class TurretHeadEntity extends Entity {

    private BlockPos basePos;
    private int ticksSinceSpawn = 0;

    public TurretHeadEntity(EntityType<? extends TurretHeadEntity> type, Level level) {
        super(type, level);
        this.noPhysics = true; // Щоб не падала та не штовхалась
    }

    public void setBasePos(BlockPos basePos) {
        this.basePos = basePos;
        this.setPos(basePos.getX() + 0.5, basePos.getY() + 1.0, basePos.getZ() + 0.5);
    }

    @Nullable
    public TurretBaseBlockEntity getBaseEntity() {
        if (basePos != null && this.level().getBlockEntity(basePos) instanceof TurretBaseBlockEntity be) {
            return be;
        }
        return null;
    }


    @Override
    protected void defineSynchedData() {}

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        if (tag.contains("BaseX")) {
            basePos = new BlockPos(tag.getInt("BaseX"), tag.getInt("BaseY"), tag.getInt("BaseZ"));
        }
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        if (basePos != null) {
            tag.putInt("BaseX", basePos.getX());
            tag.putInt("BaseY", basePos.getY());
            tag.putInt("BaseZ", basePos.getZ());
        }
    }

    @Override
    public void tick() {
        super.tick();

        if (this.level().isClientSide) return;

        ticksSinceSpawn++;

        if (basePos != null) {
            this.setPos(basePos.getX() + 0.5, basePos.getY() + 1.0, basePos.getZ() + 0.5);
        }

        // Дати 40 тік (2 секунди) на пошук бази
        if (ticksSinceSpawn > 40) {
            TurretBaseBlockEntity base = getBaseEntity();
            if (base != null) {
                // Turret працює
            } else {
                System.out.println("Base not found! Removing turret head.");
                this.discard();
                return;
            }
        }

        // Проста логіка наведення
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
