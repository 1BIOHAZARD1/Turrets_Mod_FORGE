package net.oleksandr.custom_turrets.registry;

import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.oleksandr.custom_turrets.TurretsMod;
import net.oleksandr.custom_turrets.menu.TurretBaseMenu;

public class ModMenus {
    public static final DeferredRegister<MenuType<?>> MENUS =
            DeferredRegister.create(ForgeRegistries.MENU_TYPES, TurretsMod.MOD_ID);

    public static final RegistryObject<MenuType<TurretBaseMenu>> TURRET_BASE_MENU =
            MENUS.register("turret_base_menu", () ->
                    IForgeMenuType.create(TurretBaseMenu::new));

    public static void register(IEventBus eventBus) {
        MENUS.register(eventBus);
    }
}
