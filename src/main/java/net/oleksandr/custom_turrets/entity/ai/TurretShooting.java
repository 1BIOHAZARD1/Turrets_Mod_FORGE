package net.oleksandr.custom_turrets.entity.ai;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.LivingEntity;
import net.oleksandr.custom_turrets.entity.TurretHeadEntity;

public class TurretShooting {
    private final TurretHeadEntity turret;

    public TurretShooting(TurretHeadEntity turret) {
        this.turret = turret;
    }

    public void shootAt(LivingEntity target) {
        if (target != null && target.isAlive()) {
            target.hurt(turret.damageSources().indirectMagic(turret, null), 10.0F);
            turret.playSound(SoundEvents.ARROW_SHOOT, 1.0F, 1.0F);
        }
    }
}
