package net.oleksandr.custom_turrets.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.oleksandr.custom_turrets.TurretsMod;
import net.oleksandr.custom_turrets.menu.TurretBaseMenu;

/**
 * GUI screen class for the Turret Base block.
 * Displays the container interface and handles rendering.
 */
public class TurretBaseScreen extends AbstractContainerScreen<TurretBaseMenu> {

    // Path to the GUI texture
    private static final ResourceLocation TEXTURE =
            new ResourceLocation(TurretsMod.MOD_ID, "textures/gui/turret_base_gui.png");

    /**
     * Constructor for the Turret Base screen.
     *
     * @param menu            the container menu (server logic)
     * @param playerInventory the player's inventory
     * @param title           the screen title component
     */
    public TurretBaseScreen(TurretBaseMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        this.imageWidth = 166;   // GUI width in pixels
        this.imageHeight = 176;  // GUI height in pixels
    }

    /**
     * Renders the static background of the GUI (the texture).
     *
     * @param graphics    GUI graphics context
     * @param partialTick render timing
     * @param mouseX      mouse X position
     * @param mouseY      mouse Y position
     */
    @Override
    protected void renderBg(GuiGraphics graphics, float partialTick, int mouseX, int mouseY) {
        // Bind the GUI texture
        RenderSystem.setShaderTexture(0, TEXTURE);

        // Center the GUI on screen
        int x = (this.width - this.imageWidth) / 2;
        int y = (this.height - this.imageHeight) / 2;

        // Draw the background texture
        graphics.blit(TEXTURE, x, y, 0, 0, this.imageWidth, this.imageHeight);
    }

    /**
     * Renders the foreground labels (title and player inventory name).
     *
     * @param graphics the GUI graphics context
     * @param mouseX   mouse X position
     * @param mouseY   mouse Y position
     */
    @Override
    protected void renderLabels(GuiGraphics graphics, int mouseX, int mouseY) {
        // Draw the screen title at the top
        graphics.drawString(this.font, this.title, 8, 6, 0x404040, false);
        // Draw the player's inventory label near the bottom
        graphics.drawString(this.font, this.playerInventoryTitle, 8, this.imageHeight - 94, 0x404040, false);
    }


     // Main render loop. Draws background, screen content, and tooltips.
    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        // Render the darkened background behind the GUI
        this.renderBackground(graphics);

        // Render the GUI itself and inventory slots
        super.render(graphics, mouseX, mouseY, partialTick);

        // Render tooltips for hovered elements
        this.renderTooltip(graphics, mouseX, mouseY);
    }
}
