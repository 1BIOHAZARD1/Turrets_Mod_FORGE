package net.oleksandr.custom_turrets.block;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.oleksandr.custom_turrets.menu.TurretBaseMenu;
import net.oleksandr.custom_turrets.registry.ModBlockEntities;

public class TurretBaseBlockEntity extends BlockEntity implements MenuProvider {

    public TurretBaseBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.TURRET_BASE.get(), pos, state);
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("block.custom_turrets.turret_base");
    }

    @Override
    public AbstractContainerMenu createMenu(int id, Inventory playerInventory, Player player) {
        return new TurretBaseMenu(id, playerInventory, this);
    }
}
