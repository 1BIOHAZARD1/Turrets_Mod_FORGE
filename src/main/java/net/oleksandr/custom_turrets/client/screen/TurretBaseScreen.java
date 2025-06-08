package net.oleksandr.custom_turrets.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.oleksandr.custom_turrets.TurretsMod;
import net.oleksandr.custom_turrets.menu.TurretBaseMenu;

public class TurretBaseScreen extends AbstractContainerScreen<TurretBaseMenu> {

    // Path to the GUI texture for the turret base
    private static final ResourceLocation TEXTURE =
            new ResourceLocation(TurretsMod.MOD_ID, "textures/gui/turret_base_gui.png");

    // Constructor initializes GUI size and assigns menu and inventory
    public TurretBaseScreen(TurretBaseMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);

        // Set GUI dimensions
        this.imageWidth = 176;   // Standard container width
        this.imageHeight = 222;  // Height allows space for 3 rows of slots (3*18 = 54 + padding)
    }

    // Called to render the background of the GUI
    @Override
    protected void renderBg(GuiGraphics graphics, float partialTick, int mouseX, int mouseY) {
        RenderSystem.setShaderTexture(0, TEXTURE); // Bind the custom GUI texture

        // Calculate center position for the GUI on screen
        int x = (this.width - this.imageWidth) / 2;
        int y = (this.height - this.imageHeight) / 2;

        // Draw the background texture
        graphics.blit(TEXTURE, x, y, 0, 0, this.imageWidth, this.imageHeight);
    }

    // Called to draw static text like titles
    @Override
    protected void renderLabels(GuiGraphics graphics, int mouseX, int mouseY) {
        // Draw the screen title (top-left corner)
        graphics.drawString(this.font, this.title, 8, 6, 0x404040, false);

        // Draw the player's inventory label (near bottom)
        graphics.drawString(this.font, this.playerInventoryTitle, 8, this.imageHeight - 94, 0x404040, false);
    }

    // Main render method (background, items, tooltips)
    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(graphics); // Render darkened background
        super.render(graphics, mouseX, mouseY, partialTick); // Render items and slots
        this.renderTooltip(graphics, mouseX, mouseY); // Show tooltip when hovering over items
    }
}
