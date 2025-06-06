package net.oleksandr.custom_turrets.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.oleksandr.custom_turrets.registry.ModBlockEntities;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.network.chat.Component;
import net.oleksandr.custom_turrets.menu.TurretBaseMenu;



public class TurretBaseBlockEntity extends BlockEntity implements MenuProvider {

    private final NonNullList<ItemStack> items = NonNullList.withSize(9, ItemStack.EMPTY); // інвентар на 9 слотів

    public TurretBaseBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.TURRET_BASE.get(), pos, state);
    }

    public NonNullList<ItemStack> getItems() {
        return items;
    }

    // Збереження
    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        ContainerHelper.saveAllItems(tag, items);
    }

    // Завантаження
    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        ContainerHelper.loadAllItems(tag, items);
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("block.custom_turrets.turret_base");
    }

    @Override
    public AbstractContainerMenu createMenu(int id, Inventory playerInventory, Player player) {
        return new TurretBaseMenu(id, playerInventory, this);
    }

    // Додатково: метод для контейнера, якщо треба буде простий доступ
    public SimpleContainer toContainer() {
        SimpleContainer container = new SimpleContainer(items.size());
        for (int i = 0; i < items.size(); i++) {
            container.setItem(i, items.get(i));
        }
        return container;
    }
}
