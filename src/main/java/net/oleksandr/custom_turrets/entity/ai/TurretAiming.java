package net.oleksandr.custom_turrets.entity.ai;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.oleksandr.custom_turrets.entity.TurretHeadEntity;

public class TurretAiming {
    private final TurretHeadEntity turret;

    // Constructor: attach to a turret entity
    public TurretAiming(TurretHeadEntity turret) {
        this.turret = turret;
    }

    // Check if the given entity is a valid target
    private boolean isValidTarget(LivingEntity entity) {
        return entity != null
                && entity.isAlive() // Must be alive
                && !entity.getUUID().equals(turret.getUUID()) // Cannot be the turret itself
                && isVisible(entity); // Must be visible (line of sight)
    }

    // Find the closest valid target within a 16-block radius
    public LivingEntity findTarget() {
        return turret.level().getEntitiesOfClass(
                        LivingEntity.class,
                        turret.getBoundingBox().inflate(16), // Expand detection range
                        this::isValidTarget // Filter only valid targets
                )
                .stream()
                .sorted((a, b) -> Double.compare(a.distanceToSqr(turret), b.distanceToSqr(turret))) // Sort by distance
                .findFirst()
                .orElse(null); // Return closest or null
    }

    // Check line-of-sight to the target
    private boolean isVisible(LivingEntity target) {
        Vec3 start = turret.getEyePosition(); // Start from turret's eyes
        Vec3 end = target.getEyePosition();   // End at target's eyes

        // Raytrace to see if anything blocks the view
        HitResult result = turret.level().clip(
                new ClipContext(start, end, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, turret)
        );

        // Consider visible if ray hits nothing or is very close to the target
        return result.getType() == HitResult.Type.MISS || result.getLocation().distanceToSqr(end) < 1.0;
    }

    // Gradually rotate the turret toward the target
    public void lookAtTarget(LivingEntity target) {
        float[] angles = getAimAngles(target); // Calculate yaw & pitch

        // Smoothly interpolate to new yaw and pitch with max change per tick
        turret.setYRot(rotLerp(turret.getYRot(), angles[0], 10f));
        turret.setXRot(rotLerp(turret.getXRot(), angles[1], 10f));
    }

    // Calculate yaw and pitch needed to look at the target
    public float[] getAimAngles(LivingEntity target) {
        Vec3 turretEye = turret.getEyePosition();
        Vec3 targetEye = target.getEyePosition();

        double dx = targetEye.x - turretEye.x;
        double dy = targetEye.y - turretEye.y;
        double dz = targetEye.z - turretEye.z;
        double dist = Math.sqrt(dx * dx + dz * dz); // Horizontal distance

        float yaw = (float) (Math.toDegrees(Math.atan2(dz, dx))) - 90.0F; // -90 to align with Minecraft's yaw system
        float pitch = (float) -(Math.toDegrees(Math.atan2(dy, dist))); // Negative to look upward/downward

        return new float[]{yaw, pitch};
    }

    // Smooth rotation interpolation to avoid snapping
    private float rotLerp(float current, float target, float maxChange) {
        float delta = Mth.wrapDegrees(target - current); // Wrap to [-180, 180] range
        return current + Mth.clamp(delta, -maxChange, maxChange); // Clamp change speed
    }
}
