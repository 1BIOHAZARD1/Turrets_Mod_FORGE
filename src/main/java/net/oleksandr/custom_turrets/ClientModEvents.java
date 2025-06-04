package net.oleksandr.custom_turrets;

import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.oleksandr.custom_turrets.client.renderer.TurretHeadRenderer;
import net.oleksandr.custom_turrets.client.screen.TurretBaseScreen;
import net.oleksandr.custom_turrets.registry.ModEntities;
import net.oleksandr.custom_turrets.registry.ModMenus;

@Mod.EventBusSubscriber(modid = TurretsMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientModEvents {

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            // Реєстрація GUI
            MenuScreens.register(ModMenus.TURRET_BASE_MENU.get(), TurretBaseScreen::new);

            // Реєстрація рендера для TurretHead Entity
            EntityRenderers.register(ModEntities.TURRET_HEAD.get(), TurretHeadRenderer::new);
        });
    }
}
