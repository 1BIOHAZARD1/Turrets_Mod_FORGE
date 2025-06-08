package net.oleksandr.custom_turrets.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.items.ItemStackHandler;
import net.oleksandr.custom_turrets.registry.ModBlockEntities;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.network.chat.Component;
import net.oleksandr.custom_turrets.menu.TurretBaseMenu;

public class TurretBaseBlockEntity extends BlockEntity implements MenuProvider, net.minecraft.world.Container {

    // Inventory with 9 slots
    private final NonNullList<ItemStack> items = NonNullList.withSize(9, ItemStack.EMPTY);

    // Wrapper for inventory operations (used in automation, etc.)
    public ItemStackHandler getInventory() {
        return new ItemStackHandler(9) {
            @Override
            protected void onContentsChanged(int slot) {
                setChanged(); // mark block entity as changed
            }

            @Override
            public int getSlots() {
                return items.size();
            }

            @Override
            public ItemStack getStackInSlot(int slot) {
                return items.get(slot);
            }

            @Override
            public void setStackInSlot(int slot, ItemStack stack) {
                items.set(slot, stack);
                onContentsChanged(slot);
            }

            @Override
            public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
                ItemStack existing = items.get(slot);
                if (!existing.isEmpty()) return stack; // slot occupied

                if (!simulate) {
                    items.set(slot, stack);
                    onContentsChanged(slot);
                }

                return ItemStack.EMPTY;
            }

            @Override
            public ItemStack extractItem(int slot, int amount, boolean simulate) {
                ItemStack stack = items.get(slot);
                if (stack.isEmpty()) return ItemStack.EMPTY;

                ItemStack result = stack.split(amount);
                if (!simulate) {
                    items.set(slot, stack);
                    onContentsChanged(slot);
                }
                return result;
            }
        };
    }

    // Container size (used for built-in inventory interface)
    @Override
    public int getContainerSize() {
        return items.size();
    }

    // Check if inventory is empty
    @Override
    public boolean isEmpty() {
        for (ItemStack stack : items) {
            if (!stack.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    // Get item in slot
    @Override
    public ItemStack getItem(int index) {
        return items.get(index);
    }

    // Remove certain amount of items from a slot
    @Override
    public ItemStack removeItem(int index, int count) {
        ItemStack result = ContainerHelper.removeItem(items, index, count);
        if (!result.isEmpty()) {
            setChanged(); // mark block entity as changed
        }
        return result;
    }

    // Remove item from slot without triggering updates
    @Override
    public ItemStack removeItemNoUpdate(int index) {
        return ContainerHelper.takeItem(items, index);
    }

    // Set item in slot
    @Override
    public void setItem(int index, ItemStack stack) {
        items.set(index, stack);
        setChanged();
    }

    // Clear entire inventory
    @Override
    public void clearContent() {
        items.clear();
    }

    // Allow player to interact (you could add distance checks here)
    @Override
    public boolean stillValid(Player player) {
        return true;
    }

    // Constructor
    public TurretBaseBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.TURRET_BASE.get(), pos, state);
    }

    // Access full inventory list
    public NonNullList<ItemStack> getItems() {
        return items;
    }

    // Save inventory to NBT
    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        ContainerHelper.saveAllItems(tag, items);
    }

    // Load inventory from NBT
    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        ContainerHelper.loadAllItems(tag, items);
    }

    // GUI name
    @Override
    public Component getDisplayName() {
        return Component.translatable("block.custom_turrets.turret_base");
    }

    // Create container menu (GUI)
    @Override
    public AbstractContainerMenu createMenu(int id, Inventory playerInventory, Player player) {
        return new TurretBaseMenu(id, playerInventory, this);
    }

    // Convert internal inventory to SimpleContainer (useful for dropping items, etc.)
    public SimpleContainer toContainer() {
        SimpleContainer container = new SimpleContainer(items.size());
        for (int i = 0; i < items.size(); i++) {
            container.setItem(i, items.get(i));
        }
        return container;
    }
}
