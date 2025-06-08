package net.oleksandr.custom_turrets.entity.ai;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.oleksandr.custom_turrets.entity.TurretHeadEntity;

public class TurretShooting {
    private final TurretHeadEntity turret;
    private final TurretAiming aiming;

    // Base damage dealt by the turret
    private static final float DAMAGE = 11.0f;

    // Number of ticks between shots (20 ticks = 1 second)
    private static final int FIRE_RATE_TICKS = 5; // ~4 shots per second
    private int cooldown = 0; // Ticks remaining until next shot

    // Constructor: needs both the turret and its aiming helper
    public TurretShooting(TurretHeadEntity turret, TurretAiming aiming) {
        this.turret = turret;
        this.aiming = aiming;
    }

    // Called every tick to check and shoot at a given target
    public void tickShooting(LivingEntity target) {
        // Wait for cooldown before shooting again
        if (cooldown > 0) {
            cooldown--;
            return;
        }

        // Validate the target
        if (target == null || !target.isAlive()) return;

        var level = turret.level();

        // Use fake player to simulate shooting
        var shooter = turret.getFakePlayer();
        if (shooter == null) return;

        // Aim directly at the target before firing
        float[] angles = aiming.getAimAngles(target);
        turret.setYRot(angles[0]);
        turret.setXRot(angles[1]);

        // Create an indirect damage source (like magic) using the turret and its fake player
        DamageSource source = level.damageSources().indirectMagic(shooter, turret);

        // Apply damage to the target
        target.hurt(source, DAMAGE);

        // Reset cooldown
        cooldown = FIRE_RATE_TICKS;
    }
}
