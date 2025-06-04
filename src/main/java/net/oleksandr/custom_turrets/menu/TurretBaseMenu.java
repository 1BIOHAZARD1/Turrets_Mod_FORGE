package net.oleksandr.custom_turrets.menu;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.inventory.Slot;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.Level;
import net.oleksandr.custom_turrets.block.TurretBaseBlockEntity;
import net.oleksandr.custom_turrets.registry.ModMenus;


public class TurretBaseMenu extends AbstractContainerMenu {

    private final TurretBaseBlockEntity blockEntity;

    private final Level level;

    public TurretBaseMenu(int id, Inventory inv, FriendlyByteBuf extraData) {
        this(id, inv, (TurretBaseBlockEntity) inv.player.level().getBlockEntity(extraData.readBlockPos()));
    }

    public TurretBaseMenu(int id, Inventory inv, TurretBaseBlockEntity entity) {
        super(ModMenus.TURRET_BASE_MENU.get(), id);

        this.blockEntity = entity;
        this.level = inv.player.level();

        // TODO: Додати слоти тут (коли зʼявиться інвентар)
        addPlayerInventory(inv);
        addPlayerHotbar(inv);
    }

    private void addPlayerInventory(Inventory inv) {
        for (int row = 0; row < 3; ++row) {
            for (int col = 0; col < 9; ++col) {
                this.addSlot(new Slot(inv, col + row * 9 + 9, 8 + col * 18, 84 + row * 18));
            }
        }
    }

    private void addPlayerHotbar(Inventory inv) {
        for (int col = 0; col < 9; ++col) {
            this.addSlot(new Slot(inv, col, 8 + col * 18, 142));
        }
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean stillValid(Player player) {
        return blockEntity != null && blockEntity.getLevel().getBlockEntity(blockEntity.getBlockPos()) == blockEntity &&
                player.distanceToSqr(
                        blockEntity.getBlockPos().getX() + 0.5,
                        blockEntity.getBlockPos().getY() + 0.5,
                        blockEntity.getBlockPos().getZ() + 0.5
                ) <= 64.0;
    }
}
