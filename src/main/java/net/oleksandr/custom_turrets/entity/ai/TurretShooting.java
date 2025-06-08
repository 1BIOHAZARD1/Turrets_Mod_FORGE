package net.oleksandr.custom_turrets.entity.ai;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.oleksandr.custom_turrets.entity.TurretHeadEntity;

public class TurretShooting {
    private final TurretHeadEntity turret;
    private final TurretAiming aiming;

    private static final float DAMAGE = 9.0f;
    private static final int FIRE_RATE_TICKS = 5; // стріляє кожні 5 тік (~4 рази/сек)
    private int cooldown = 0;

    public TurretShooting(TurretHeadEntity turret, TurretAiming aiming) {
        this.turret = turret;
        this.aiming = aiming;
    }

    public void tickShooting(LivingEntity target) {
        if (cooldown > 0) {
            cooldown--;
            return;
        }

        if (target == null || !target.isAlive()) return;

        var level = turret.level();
        var shooter = turret.getFakePlayer();
        if (shooter == null) return;

        float[] angles = aiming.getAimAngles(target);
        turret.setYRot(angles[0]);
        turret.setXRot(angles[1]);

        DamageSource source = level.damageSources().indirectMagic(shooter, turret);
        target.hurt(source, DAMAGE);

        // Ставимо кулдаун
        cooldown = FIRE_RATE_TICKS;
    }
}
