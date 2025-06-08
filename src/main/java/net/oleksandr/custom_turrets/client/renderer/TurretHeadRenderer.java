package net.oleksandr.custom_turrets.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.oleksandr.custom_turrets.TurretsMod;
import net.oleksandr.custom_turrets.client.model.TurretHeadModel;
import net.oleksandr.custom_turrets.entity.TurretHeadEntity;

public class TurretHeadRenderer extends EntityRenderer<TurretHeadEntity> {

    // Path to the texture used by the turret head model
    private static final ResourceLocation TEXTURE =
            new ResourceLocation(TurretsMod.MOD_ID, "textures/entity/turret_head.png");

    // The turret model used for rendering
    private final TurretHeadModel model;

    // Constructor sets up the model using the baked layer
    public TurretHeadRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.model = new TurretHeadModel(context.bakeLayer(TurretHeadModel.LAYER_LOCATION));
    }

    // Main render method called each frame
    @Override
    public void render(TurretHeadEntity entity, float yaw, float partialTicks,
                       PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        poseStack.pushPose(); // Save current transformation state

        // Translate model slightly up to match world position
        poseStack.translate(0, 0.5, 0);

        // Smooth interpolation for yaw/pitch between previous and current rotation
        float netHeadYaw = Mth.rotLerp(partialTicks, entity.yRotO, entity.getYRot());
        float headPitch = Mth.rotLerp(partialTicks, entity.xRotO, entity.getXRot());

        // Apply rotations to the model
        poseStack.mulPose(Axis.YP.rotationDegrees(-netHeadYaw)); // rotate around Y (yaw)
        poseStack.mulPose(Axis.XP.rotationDegrees(headPitch));   // rotate around X (pitch)

        // Render the turret model itself
        VertexConsumer buffer = bufferSource.getBuffer(model.renderType(getTextureLocation(entity)));
        model.setupAnim(entity, 0, 0, partialTicks, 0, 0); // Apply animation
        model.renderToBuffer(poseStack, buffer, packedLight, OverlayTexture.NO_OVERLAY,
                1f, 1f, 1f, 1f); // Render with full brightness and no color tint

        // 🔫 Render the weapon held by the turret
        ItemStack weapon = entity.getWeapon();
        if (!weapon.isEmpty()) {
            poseStack.pushPose(); // Save state before rendering item

            // Position and scale the item (adjust these as needed)
            poseStack.translate(0.0, 0.2, 0.0); // Vertical offset
            poseStack.mulPose(Axis.YP.rotationDegrees(180)); // Flip direction
            poseStack.scale(1.5f, 1.5f, 1.5f); // Scale up the item

            // Use Minecraft's item renderer to render it in-world
            Minecraft.getInstance().getItemRenderer().renderStatic(
                    weapon,
                    ItemDisplayContext.GROUND, // You can also try: FIXED, THIRD_PERSON_RIGHT_HAND, etc.
                    packedLight,
                    OverlayTexture.NO_OVERLAY,
                    poseStack,
                    bufferSource,
                    entity.level(), // Needed for proper lighting/shading
                    0 // Random seed
            );

            poseStack.popPose(); // Restore state after rendering item
        }

        poseStack.popPose(); // Restore state after rendering model
        super.render(entity, yaw, partialTicks, poseStack, bufferSource, packedLight);
    }

    // Returns the texture used for rendering the turret head
    @Override
    public ResourceLocation getTextureLocation(TurretHeadEntity entity) {
        return TEXTURE;
    }
}
