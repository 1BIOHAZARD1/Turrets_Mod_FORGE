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

        var weapon = turret.getWeapon();
        if (weapon.isEmpty()) return;

        var fakePlayer = turret.getFakePlayer();
        if (fakePlayer == null) return;

        // Встановлюємо зброю
        fakePlayer.setItemInHand(InteractionHand.MAIN_HAND, weapon);

        // Прицілюємось
        float[] angles = aiming.getAimAngles(target);
        fakePlayer.setYRot(angles[0]);
        fakePlayer.setXRot(angles[1]);
        fakePlayer.yRotO = angles[0];
        fakePlayer.xRotO = angles[1];

        // Симуляція пострілу (імітація кліку ЛКМ)
        fakePlayer.swing(InteractionHand.MAIN_HAND); // анімація атаки
        fakePlayer.attack(fakePlayer); // так T&C перевіряє власника зброї — використовує `shooter instanceof Player`

        // Гарантуємо, що фейковий гравець отримує тік
        fakePlayer.tick();
    }
}

