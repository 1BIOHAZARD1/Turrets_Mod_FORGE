package net.oleksandr.custom_turrets.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.oleksandr.custom_turrets.registry.ModBlockEntities;

public class TurretBaseBlockEntity extends BlockEntity {

    public TurretBaseBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.TURRET_BLOCK_ENTITY.get(), pos, state);
    }

    public void tick() {
        // Логіка обертання/стрільби
    }
}
