package net.oleksandr.custom_turrets.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.resources.ResourceLocation;
import net.oleksandr.custom_turrets.TurretsMod;
import net.oleksandr.custom_turrets.client.model.TurretHeadModel;
import net.oleksandr.custom_turrets.entity.TurretHeadEntity;

public class TurretHeadRenderer extends EntityRenderer<TurretHeadEntity> {

    private static final ResourceLocation TEXTURE =
            new ResourceLocation(TurretsMod.MOD_ID, "textures/entity/turret_head.png");

    private final TurretHeadModel model;

    public TurretHeadRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.model = new TurretHeadModel(context.bakeLayer(TurretHeadModel.LAYER_LOCATION));
    }

    @Override
    public void render(TurretHeadEntity entity, float yaw, float partialTicks,
                       PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {

        poseStack.pushPose();

        poseStack.translate(0, 0.5, 0); // Трохи вище
        poseStack.scale(1.0F, 1.0F, 1.0F); // Можеш змінити масштаб тут

        VertexConsumer buffer = bufferSource.getBuffer(model.renderType(getTextureLocation(entity)));
        model.setupAnim(entity, 0, 0, 0, entity.getYRot(), entity.getXRot());
        model.renderToBuffer(poseStack, buffer, packedLight, OverlayTexture.NO_OVERLAY,
                1.0F, 1.0F, 1.0F, 1.0F);

        poseStack.popPose();
        super.render(entity, yaw, partialTicks, poseStack, bufferSource, packedLight);
    }

    @Override
    public ResourceLocation getTextureLocation(TurretHeadEntity entity) {
        return TEXTURE;
    }
}
