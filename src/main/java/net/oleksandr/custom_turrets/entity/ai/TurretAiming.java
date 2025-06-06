package net.oleksandr.custom_turrets.entity.ai;

import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.oleksandr.custom_turrets.entity.TurretHeadEntity;

public class TurretAiming {
    private final TurretHeadEntity turret;

    public TurretAiming(TurretHeadEntity turret) {
        this.turret = turret;
    }

    private boolean isValidTarget(LivingEntity entity) {
        return entity != null
                && entity.isAlive()
                && !entity.getUUID().equals(turret.getUUID())  // 👈 перевірка, щоб не атакувати самого себе
                && isVisible(entity);
    }


    public LivingEntity findTarget() {
        return turret.level().getEntitiesOfClass(LivingEntity.class,
                        turret.getBoundingBox().inflate(16),
                        this::isValidTarget)
                .stream()
                .findFirst()
                .orElse(null);
    }



    private boolean isVisible(LivingEntity target) {
        // Спрощений raytrace через блоки
        Vec3 start = turret.getEyePosition();
        Vec3 end = target.getEyePosition();
        BlockHitResult result = turret.level().clip(new ClipContext(start, end, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, turret));

        return result.getType() == HitResult.Type.MISS || result.getBlockPos().equals(BlockPos.containing(end));
    }

    public void lookAtTarget(LivingEntity target) {
        double dx = target.getX() - turret.getX();
        double dy = target.getEyeY() - turret.getEyeY();
        double dz = target.getZ() - turret.getZ();
        double dist = Math.sqrt(dx * dx + dz * dz);

        float targetYaw = (float) (Math.toDegrees(Math.atan2(dz, dx))) - 90.0F;
        float targetPitch = (float) (-(Math.toDegrees(Math.atan2(dy, dist))));

        turret.setYRot(rotLerp(turret.getYRot(), targetYaw, 10f));
        turret.setXRot(rotLerp(turret.getXRot(), targetPitch, 10f));
    }

    private float rotLerp(float current, float target, float maxChange) {
        float delta = Mth.wrapDegrees(target - current);
        return current + Mth.clamp(delta, -maxChange, maxChange);
    }
}
