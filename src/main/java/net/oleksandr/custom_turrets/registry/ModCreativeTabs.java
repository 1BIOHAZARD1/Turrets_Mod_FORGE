package net.oleksandr.custom_turrets.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import net.oleksandr.custom_turrets.TurretsMod;

@Mod.EventBusSubscriber(modid = TurretsMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModCreativeTabs {

    // 1. Регістрація вкладки
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, TurretsMod.MOD_ID);

    // 2. Вкладка без предметів
    public static final RegistryObject<CreativeModeTab> CUSTOM_TURRETS_TAB =
            CREATIVE_MODE_TABS.register("custom_turrets_tab",
                    () -> CreativeModeTab.builder()
                            .title(Component.translatable("itemGroup.custom_turrets_tab"))
                            .icon(() -> new ItemStack(
                                    ModBlocks.TURRET_BLOCK.isPresent()
                                            ? ModBlocks.TURRET_BLOCK.get().asItem()
                                            : Items.BARRIER
                            ))
                            .build()
            );

    // 3. Реєстрація регістру вкладок
    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }

    // 4. Додавання вмісту до вкладки
    @SubscribeEvent
    public static void onBuildContents(BuildCreativeModeTabContentsEvent event) {
        if (event.getTab() == CUSTOM_TURRETS_TAB.get()) {
            event.accept(ModBlocks.TURRET_BLOCK.get().asItem());

        }
    }

}
