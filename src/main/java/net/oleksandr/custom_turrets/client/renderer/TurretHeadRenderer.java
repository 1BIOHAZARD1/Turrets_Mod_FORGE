package net.oleksandr.custom_turrets.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
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

        poseStack.mulPose(Axis.YP.rotationDegrees(-netHeadYaw));
        poseStack.mulPose(Axis.XP.rotationDegrees(headPitch));

        // Рендер моделі голови турелі
        VertexConsumer buffer = bufferSource.getBuffer(model.renderType(getTextureLocation(entity)));
        model.setupAnim(entity, 0, 0, partialTicks, 0, 0);
        model.renderToBuffer(poseStack, buffer, packedLight, OverlayTexture.NO_OVERLAY, 1f, 1f, 1f, 1f);

        // 📦 РЕНДЕР ПРЕДМЕТА
        ItemStack weapon = entity.getWeapon();
        if (!weapon.isEmpty()) {
            poseStack.pushPose();

            // Позиція і масштаб предмета
            poseStack.translate(0.0, 0.2, 0.0); // Зміни позицію при потребі
            poseStack.mulPose(Axis.YP.rotationDegrees(180));
            poseStack.scale(1.5f, 1.5f, 1.5f); // Масштаб
            Minecraft.getInstance().getItemRenderer().renderStatic(
                    weapon,
                    ItemDisplayContext.GROUND, // або FIXED, або THIRD_PERSON_RIGHT_HAND
                    packedLight,
                    OverlayTexture.NO_OVERLAY,
                    poseStack,
                    bufferSource,
                    entity.level(),
                    0
            );

            poseStack.popPose();
        }

        poseStack.popPose();
        super.render(entity, yaw, partialTicks, poseStack, bufferSource, packedLight);
    }


    @Override
    public ResourceLocation getTextureLocation(TurretHeadEntity entity) {
        return TEXTURE;
    }
}
