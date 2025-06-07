package net.oleksandr.custom_turrets.entity.ai;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.ClipContext;
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
                && !entity.getUUID().equals(turret.getUUID())
                && isVisible(entity);
    }

    public LivingEntity findTarget() {
        return turret.level().getEntitiesOfClass(LivingEntity.class,
                        turret.getBoundingBox().inflate(16),
                        this::isValidTarget)
                .stream()
                .sorted((a, b) -> Double.compare(a.distanceToSqr(turret), b.distanceToSqr(turret)))
                .findFirst()
                .orElse(null);

    }

    private boolean isVisible(LivingEntity target) {
        Vec3 start = turret.getEyePosition();
        Vec3 end = target.getEyePosition();
        HitResult result = turret.level().clip(new ClipContext(start, end, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, turret));

        return result.getType() == HitResult.Type.MISS || result.getLocation().distanceToSqr(end) < 1.0;
    }

    public void lookAtTarget(LivingEntity target) {
        float[] angles = getAimAngles(target);

        turret.setYRot(rotLerp(turret.getYRot(), angles[0], 10f));
        turret.setXRot(rotLerp(turret.getXRot(), angles[1], 10f));
    }

    public float[] getAimAngles(LivingEntity target) {
        Vec3 turretEye = turret.getEyePosition();
        Vec3 targetEye = target.getEyePosition();

        double dx = targetEye.x - turretEye.x;
        double dy = targetEye.y - turretEye.y;
        double dz = targetEye.z - turretEye.z;
        double dist = Math.sqrt(dx * dx + dz * dz);

        float yaw = (float) (Math.toDegrees(Math.atan2(dz, dx))) - 90.0F;
        float pitch = (float) -(Math.toDegrees(Math.atan2(dy, dist)));

        return new float[]{yaw, pitch};
    }

    private float rotLerp(float current, float target, float maxChange) {
        float delta = Mth.wrapDegrees(target - current);
        return current + Mth.clamp(delta, -maxChange, maxChange);
    }
}
