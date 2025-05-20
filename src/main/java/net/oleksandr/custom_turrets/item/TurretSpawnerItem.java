package net.oleksandr.custom_turrets.item;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.oleksandr.custom_turrets.entity.TurretHeadEntity;
import net.oleksandr.custom_turrets.registry.ModEntities;

public class TurretSpawnerItem extends Item {
    public TurretSpawnerItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        if (!level.isClientSide()) {
            BlockPos spawnPos = player.blockPosition().above();

            TurretHeadEntity turret = new TurretHeadEntity(ModEntities.TURRET_HEAD.get(), level);
            turret.setPos(spawnPos.getX() + 0.5, spawnPos.getY(), spawnPos.getZ() + 0.5);
            level.addFreshEntity(turret);

            level.playSound(null, spawnPos, SoundEvents.IRON_GOLEM_STEP, SoundSource.PLAYERS, 1.0f, 1.0f);
        }
        return InteractionResultHolder.sidedSuccess(player.getItemInHand(hand), level.isClientSide());
    }
}
