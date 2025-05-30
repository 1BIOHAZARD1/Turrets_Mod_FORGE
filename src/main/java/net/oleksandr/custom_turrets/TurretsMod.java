package net.oleksandr.custom_turrets;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import net.minecraft.client.renderer.entity.EntityRenderers;
import com.mojang.logging.LogUtils;
import org.slf4j.Logger;

import net.oleksandr.custom_turrets.client.renderer.TurretHeadRenderer;
import net.oleksandr.custom_turrets.registry.ModBlocks;
import net.oleksandr.custom_turrets.registry.ModBlockEntities;
import net.oleksandr.custom_turrets.registry.ModEntities;
import net.oleksandr.custom_turrets.registry.ModItems;

@Mod(TurretsMod.MOD_ID)
public class TurretsMod {
    public static final String MOD_ID = "custom_turrets";
    private static final Logger LOGGER = LogUtils.getLogger();

    public TurretsMod() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        // Реєстрація
        ModBlocks.register(modEventBus);
        ModItems.register(modEventBus);
        ModBlockEntities.register(modEventBus); // ВИПРАВЛЕНО
        ModEntities.ENTITY_TYPES.register(modEventBus);

        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::addCreative);

        MinecraftForge.EVENT_BUS.register(this);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        // для спільної ініціалізації
    }

    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.FUNCTIONAL_BLOCKS) {
            ModBlocks.TURRET_BLOCK.ifPresent(block -> {
                event.accept(block.asItem()); // ✅ Правильний спосіб
            });
        }
    }



    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        // додатково за потреби
    }

    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            event.enqueueWork(() -> {
                EntityRenderers.register(ModEntities.TURRET_HEAD.get(), TurretHeadRenderer::new);
            });
        }
    }
}
