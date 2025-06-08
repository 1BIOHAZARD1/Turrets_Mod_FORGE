package net.oleksandr.custom_turrets;

import com.mojang.logging.LogUtils;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
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
import net.oleksandr.custom_turrets.registry.*;
import org.slf4j.Logger;

@Mod(TurretsMod.MOD_ID)
public class TurretsMod {
    public static final String MOD_ID = "custom_turrets";
    private static final Logger LOGGER = LogUtils.getLogger();

    public TurretsMod() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        // Register content
        ModMenus.register(modEventBus);
        ModBlocks.register(modEventBus);
        ModItems.register(modEventBus);
        ModBlockEntities.register(modEventBus);
        ModEntities.ENTITY_TYPES.register(modEventBus);
        ModCreativeTabs.register(modEventBus);

        // Register events
        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::addCreative);

        MinecraftForge.EVENT_BUS.register(this);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        // Common setup
    }

    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        // Add items to vanilla tabs if needed
        // if (event.getTabKey() == CreativeModeTabs.FUNCTIONAL_BLOCKS) {
        //     event.accept(ModBlocks.TURRET_BLOCK.get().asItem());
        // }
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        // Server start logic
    }
}
