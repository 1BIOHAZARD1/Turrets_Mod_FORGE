package net.oleksandr.custom_turrets.entity;

import com.mojang.authlib.GameProfile;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.common.util.FakePlayerFactory;
import net.minecraftforge.entity.IEntityAdditionalSpawnData;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.network.NetworkHooks;
import net.oleksandr.custom_turrets.block.TurretBaseBlockEntity;
import net.oleksandr.custom_turrets.entity.ai.TurretAiming;
import net.oleksandr.custom_turrets.entity.ai.TurretShooting;
import org.slf4j.Logger;
import com.mojang.logging.LogUtils;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;


import javax.annotation.Nullable;
import java.util.UUID;

public class TurretHeadEntity extends Entity implements IEntityAdditionalSpawnData {

    private static final Logger LOGGER = LogUtils.getLogger();
    private static final GameProfile DUMMY_PROFILE =
            new GameProfile(UUID.fromString("00000000-0000-0000-0000-000000000000"), "TurretFake");

    private static final EntityDataAccessor<Float> DATA_YAW =
            SynchedEntityData.defineId(TurretHeadEntity.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Float> DATA_PITCH =
            SynchedEntityData.defineId(TurretHeadEntity.class, EntityDataSerializers.FLOAT);

    private BlockPos basePos;
    private int ticksSinceSpawn = 0;
    private int attackCooldown = 0;

    private final TurretAiming aiming = new TurretAiming(this);
    private final TurretShooting shooting = new TurretShooting(this, this.aiming);

    private FakePlayer fakePlayer;
    @Nullable
    private LivingEntity cachedTarget;


    public TurretHeadEntity(EntityType<? extends TurretHeadEntity> type, Level level) {
        super(type, level);
        this.noPhysics = true;
    }

    public void setBasePos(BlockPos basePos) {
        this.basePos = basePos;
        this.setPos(basePos.getX() + 0.5, basePos.getY() + 1.0, basePos.getZ() + 0.5);
    }

    @Nullable
    public TurretBaseBlockEntity getBaseEntity() {
        if (basePos != null && this.level().getBlockEntity(basePos) instanceof TurretBaseBlockEntity be) {
            return be;
        }
        return null;
    }

    public ItemStackHandler getInventory() {
        TurretBaseBlockEntity base = getBaseEntity();
        return (base != null) ? base.getInventory() : new ItemStackHandler(1);
    }

    public ItemStack getWeapon() {
        TurretBaseBlockEntity base = getBaseEntity();
        if (base == null) {
            System.out.println("[Turret] Base is NULL");
            return ItemStack.EMPTY;
        }

        ItemStackHandler inventory = base.getInventory();
        for (int i = 0; i < inventory.getSlots(); i++) {
            ItemStack stack = inventory.getStackInSlot(i);
            if (!stack.isEmpty()) {
                System.out.println("[Turret] Found weapon in slot " + i + ": " + stack);
                return stack;
            }
        }

        System.out.println("[Turret] No weapon found in any slot");
        return ItemStack.EMPTY;
    }






    public FakePlayer getFakePlayer() {
        if (fakePlayer == null && this.level() instanceof ServerLevel serverLevel) {
            fakePlayer = FakePlayerFactory.get(serverLevel, DUMMY_PROFILE);
        }

        if (fakePlayer != null) {
            fakePlayer.setItemInHand(InteractionHand.MAIN_HAND, getWeapon());
            fakePlayer.setPos(this.getX(), this.getY(), this.getZ());
        }

        return fakePlayer;
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(DATA_YAW, 0.0f);
        this.entityData.define(DATA_PITCH, 0.0f);
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        if (tag.contains("BaseX")) {
            basePos = new BlockPos(tag.getInt("BaseX"), tag.getInt("BaseY"), tag.getInt("BaseZ"));
        }
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        if (basePos != null) {
            tag.putInt("BaseX", basePos.getX());
            tag.putInt("BaseY", basePos.getY());
            tag.putInt("BaseZ", basePos.getZ());
        }
    }

    @Override
    public void writeSpawnData(FriendlyByteBuf buffer) {
        buffer.writeBlockPos(basePos != null ? basePos : BlockPos.ZERO);
    }

    @Override
    public void readSpawnData(FriendlyByteBuf buffer) {
        basePos = buffer.readBlockPos();
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }





    @Override
    public void tick() {
        super.tick();

        if (this.level().isClientSide) return;

        ticksSinceSpawn++;
        updatePositionWithBase();

        if (ticksSinceSpawn > 40 && getBaseEntity() == null) {
            LOGGER.warn("Base not found! Removing turret head.");
            this.discard();
            return;
        }

        updateTargetingAndAttack();
    }

    private void updatePositionWithBase() {
        if (basePos != null) {
            this.setPos(basePos.getX() + 0.5, basePos.getY() + 1.0, basePos.getZ() + 0.5);
        }
    }

    private void updateTargetingAndAttack() {
        cachedTarget = aiming.findTarget();

        if (cachedTarget != null) {
            aiming.lookAtTarget(cachedTarget);

            // Зберігаємо yaw/pitch для рендеру
            this.entityData.set(DATA_YAW, this.getYRot());
            this.entityData.set(DATA_PITCH, this.getXRot());

            ItemStack weapon = getWeapon();
            if (!weapon.isEmpty() && --attackCooldown <= 0) {
                FakePlayer fakePlayer = getFakePlayer();
                if (fakePlayer != null) {
                    shooting.tickShooting(cachedTarget);
                }
                attackCooldown = 4;
            }
        }
    }

    public float getRenderYaw() {
        return this.entityData.get(DATA_YAW);
    }

    public float getRenderPitch() {
        return this.entityData.get(DATA_PITCH);
    }
}