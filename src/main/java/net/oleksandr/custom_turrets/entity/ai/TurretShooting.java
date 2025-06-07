package net.oleksandr.custom_turrets.entity.ai;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.oleksandr.custom_turrets.entity.TurretHeadEntity;

public class TurretShooting {
    private final TurretHeadEntity turret;
    private final TurretAiming aiming;

    public TurretShooting(TurretHeadEntity turret, TurretAiming aiming) {
        this.turret = turret;
        this.aiming = aiming;
    }

    public void shootAt(LivingEntity target) {
        if (target == null || !target.isAlive()) return;

        var level = turret.level();
        var weapon = turret.getWeapon();

        if (weapon.isEmpty()) return;

        var fakePlayer = turret.getFakePlayer();
        if (fakePlayer == null) return;

        fakePlayer.setItemInHand(InteractionHand.MAIN_HAND, weapon);

        float[] angles = aiming.getAimAngles(target);
        float yaw = angles[0];
        float pitch = angles[1];

        fakePlayer.setYRot(yaw);
        fakePlayer.setXRot(pitch);
        fakePlayer.yRotO = yaw;
        fakePlayer.xRotO = pitch;

        var result = weapon.use(level, fakePlayer, InteractionHand.MAIN_HAND);
        if (!result.getResult().consumesAction()) {
            System.out.println("Turret weapon use did not consume action.");
        }


        turret.playSound(SoundEvents.ARROW_SHOOT, 1.0F, 1.0F);
    }
}
