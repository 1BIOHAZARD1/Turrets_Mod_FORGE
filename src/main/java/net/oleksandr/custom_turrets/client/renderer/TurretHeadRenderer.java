package net.oleksandr.custom_turrets.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.oleksandr.custom_turrets.TurretsMod;
import net.oleksandr.custom_turrets.entity.TurretHeadEntity;

public class TurretHeadRenderer extends EntityRenderer<TurretHeadEntity> {

    // Texture location for the turret head entity
    private static final ResourceLocation TEXTURE =
            new ResourceLocation(TurretsMod.MOD_ID, "textures/entity/turret_head.png");

    public TurretHeadRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(TurretHeadEntity entity, float yaw, float partialTicks, PoseStack poseStack,
                       MultiBufferSource bufferSource, int packedLight) {
        // No rendering logic yet — model or geometry not implemented
    }

    @Override
    public ResourceLocation getTextureLocation(TurretHeadEntity entity) {
        return TEXTURE;
    }
}
