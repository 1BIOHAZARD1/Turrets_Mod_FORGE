package net.oleksandr.custom_turrets;

import net.oleksandr.custom_turrets.registry.ModBlocks;
import net.oleksandr.custom_turrets.registry.ModBlockEntities;
import com.mojang.logging.LogUtils;
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
import net.oleksandr.custom_turrets.registry.ModEntities;
import org.slf4j.Logger;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.oleksandr.custom_turrets.client.renderer.TurretHeadRenderer;
import net.oleksandr.custom_turrets.registry.ModItems;



@Mod(TurretsMod.MOD_ID)
public class TurretsMod {
    public static final String MOD_ID = "custom_turrets";
    private static final Logger LOGGER = LogUtils.getLogger();

    public TurretsMod(FMLJavaModLoadingContext context) {
        IEventBus modEventBus = context.getModEventBus();

        // Реєстрація блоків і block entity
        ModBlocks.register(modEventBus);
        ModBlockEntities.register();
        ModEntities.ENTITY_TYPES.register(modEventBus);
        ModItems.register(modEventBus);






        // Common setup
        modEventBus.addListener(this::commonSetup);

        // Додавання в креативну вкладку
        modEventBus.addListener(this::addCreative);

        // Реєстрація серверних подій
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        // Тут можна додати логіку ініціалізації
    }

    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.FUNCTIONAL_BLOCKS) {
            if (ModBlocks.TURRET_BLOCK.isPresent()) {
                event.accept(ModBlocks.TURRET_BLOCK.get());
            }
        }
    }



    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        // Логіка запуску сервера (необов’язково)
    }

    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        public static void onClientSetup(FMLClientSetupEvent event) {
            event.enqueueWork(() -> {
                net.minecraft.client.renderer.entity.EntityRenderers.register(
                        ModEntities.TURRET_HEAD.get(),
                        TurretHeadRenderer::new
                );
            });
        }
    }
}
