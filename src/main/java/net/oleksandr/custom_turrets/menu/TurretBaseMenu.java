package net.oleksandr.custom_turrets.menu;

import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.inventory.Slot;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.Level;
import net.oleksandr.custom_turrets.block.TurretBaseBlockEntity;
import net.oleksandr.custom_turrets.registry.ModMenus;
import net.minecraft.world.inventory.Slot;


/**
 * Server-side container logic for the Turret Base block.
 * Handles inventory slot setup and interaction validation.
 */
public class TurretBaseMenu extends AbstractContainerMenu {

    private final TurretBaseBlockEntity blockEntity; // The associated block entity
    private final Level level; // World level where the block exists


     // Constructor called on the client side.
     // Receives data from the server, reads block position, and retrieves the block entity.

    public TurretBaseMenu(int id, Inventory inv, FriendlyByteBuf extraData) {
        this(id, inv, (TurretBaseBlockEntity) inv.player.level().getBlockEntity(extraData.readBlockPos()));
    }


     // Constructor used to initialize the menu with the block entity directly.

    public TurretBaseMenu(int id, Inventory inv, TurretBaseBlockEntity entity) {
        super(ModMenus.TURRET_BASE_MENU.get(), id);

        this.blockEntity = entity;
        this.level = inv.player.level();

        // Turret's own inventory (3x3)
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                this.addSlot(new Slot(blockEntity, col + row * 3, 62 + col * 18, 18 + row * 18));
            }
        }




        // Player inventory and hotbar slots will be shown in the GUI
        // TODO: Add custom slots for turret input/output when inventory is implemented
        addPlayerInventory(inv);
        addPlayerHotbar(inv);
    }


     // Adds the player's main inventory (3 rows of 9 slots) to the GUI.

    private void addPlayerInventory(Inventory inv) {
        for (int row = 0; row < 3; ++row) {
            for (int col = 0; col < 9; ++col) {
                this.addSlot(new Slot(inv, col + row * 9 + 9, 8 + col * 18, 140 + row * 18));
            }
        }
    }


     // Adds the player's hotbar (bottom row of 9 slots) to the GUI.

    private void addPlayerHotbar(Inventory inv) {
        for (int col = 0; col < 9; ++col) {
            this.addSlot(new Slot(inv, col, 8 + col * 18, 198));
        }
    }


     // Handles quick item transfer with shift-click (currently disabled).

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        return ItemStack.EMPTY; // Shift-click disabled for now
    }


    // Determines if the player can still interact with the container.

    @Override
    public boolean stillValid(Player player) {
        return blockEntity != null &&
                blockEntity.getLevel().getBlockEntity(blockEntity.getBlockPos()) == blockEntity &&
                player.distanceToSqr(
                        blockEntity.getBlockPos().getX() + 0.5,
                        blockEntity.getBlockPos().getY() + 0.5,
                        blockEntity.getBlockPos().getZ() + 0.5
                ) <= 64.0; // 8 block interaction radius (8 * 8 = 64)
    }
}
