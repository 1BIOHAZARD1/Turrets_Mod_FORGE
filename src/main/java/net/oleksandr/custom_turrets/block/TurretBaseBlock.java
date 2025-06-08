package net.oleksandr.custom_turrets.block;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.network.NetworkHooks;
import net.oleksandr.custom_turrets.block.TurretBaseBlockEntity;
import net.oleksandr.custom_turrets.entity.TurretHeadEntity;
import net.oleksandr.custom_turrets.registry.ModBlockEntities;
import net.oleksandr.custom_turrets.registry.ModEntities;
import net.minecraftforge.entity.IEntityAdditionalSpawnData;

import javax.annotation.Nullable;
import java.util.List;

public class TurretBaseBlock extends Block implements EntityBlock {

    // Constructor with block properties (e.g. hardness)
    public TurretBaseBlock() {
        super(BlockBehaviour.Properties.of().strength(2.0f));
    }

    // Called when a player right-clicks the block
    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (!level.isClientSide) {
            BlockEntity be = level.getBlockEntity(pos);
            // Open the GUI if the block entity is correct
            if (be instanceof TurretBaseBlockEntity turretEntity) {
                NetworkHooks.openScreen((ServerPlayer) player, turretEntity, pos);
            }
        }
        return InteractionResult.SUCCESS;
    }

    // Creates the associated block entity when the block is placed
    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new TurretBaseBlockEntity(pos, state);
    }

    // Determines how the block is rendered (use model)
    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    // Called when the block is placed in the world
    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
        super.setPlacedBy(level, pos, state, placer, stack);

        if (!level.isClientSide) {
            // Spawns the turret head entity slightly above the base
            TurretHeadEntity turretHead = new TurretHeadEntity(ModEntities.TURRET_HEAD.get(), level);
            turretHead.setBasePos(pos); // links the head to the base
            turretHead.moveTo(pos.getX() + 0.5, pos.getY() + 1.0, pos.getZ() + 0.5, 0, 0);
            level.addFreshEntity(turretHead);
        }
    }

    // Called when the block is removed/broken
    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!state.is(newState.getBlock())) {
            if (!level.isClientSide) {
                // Removes any turret head entity near the block
                List<TurretHeadEntity> turretHeads = level.getEntitiesOfClass(
                        TurretHeadEntity.class,
                        new AABB(pos.above()).inflate(0.5)
                );

                for (TurretHeadEntity entity : turretHeads) {
                    entity.discard();
                }
            }

            super.onRemove(state, level, pos, newState, isMoving);
        }
    }

    // Provides server-side ticking for the block entity (logic can be added here)
    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return level.isClientSide ? null :
                (lvl, pos, st, be) -> {
                    if (be instanceof TurretBaseBlockEntity turretBE) {
                        // Server-side update logic for the base can be added here
                    }
                };
    }
}
