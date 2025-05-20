package net.oleksandr.custom_turrets.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.oleksandr.custom_turrets.TurretsMod;
import net.oleksandr.custom_turrets.entity.TurretHeadEntity;

public class TurretHeadRenderer extends EntityRenderer<TurretHeadEntity> {
    private static final ResourceLocation TEXTURE = new ResourceLocation(TurretsMod.MOD_ID, "textures/entity/turret_head.png");

    public TurretHeadRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(TurretHeadEntity entity, float yaw, float partialTicks, PoseStack poseStack,
                       MultiBufferSource bufferSource, int packedLight) {
        // Поки що нічого не рендеримо
    }

    @Override
    public ResourceLocation getTextureLocation(TurretHeadEntity entity) {
        return TEXTURE;
    }
}
