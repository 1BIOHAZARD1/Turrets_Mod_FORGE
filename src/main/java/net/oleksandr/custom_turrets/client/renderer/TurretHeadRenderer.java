package net.oleksandr.custom_turrets.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
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

        poseStack.translate(0, 0.5, 0);

        float netHeadYaw = Mth.rotLerp(partialTicks, entity.yRotO, entity.getYRot());
        float headPitch = Mth.rotLerp(partialTicks, entity.xRotO, entity.getXRot());

        // Обертаємо модель всього один раз через poseStack
        poseStack.mulPose(Axis.YP.rotationDegrees(-netHeadYaw));
        poseStack.mulPose(Axis.XP.rotationDegrees(headPitch));

        VertexConsumer buffer = bufferSource.getBuffer(model.renderType(getTextureLocation(entity)));
        // В setupAnim передаємо 0, 0, щоб модель не додатково не крутилась
        model.setupAnim(entity, 0, 0, partialTicks, 0, 0);
        model.renderToBuffer(poseStack, buffer, packedLight, OverlayTexture.NO_OVERLAY,
                1f, 1f, 1f, 1f);

        poseStack.popPose();

        super.render(entity, yaw, partialTicks, poseStack, bufferSource, packedLight);
    }


    @Override
    public ResourceLocation getTextureLocation(TurretHeadEntity entity) {
        return TEXTURE;
    }
}
