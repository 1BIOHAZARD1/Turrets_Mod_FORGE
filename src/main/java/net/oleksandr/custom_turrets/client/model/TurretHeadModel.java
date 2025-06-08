package net.oleksandr.custom_turrets.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.oleksandr.custom_turrets.TurretsMod;
import net.oleksandr.custom_turrets.entity.TurretHeadEntity;

public class TurretHeadModel extends EntityModel<TurretHeadEntity> {

    // Unique model layer identifier (used in renderer registration)
    public static final ModelLayerLocation LAYER_LOCATION =
            new ModelLayerLocation(new ResourceLocation(TurretsMod.MOD_ID, "turret_head"), "main");

    // The main body part of the model
    private final ModelPart base;

    // Constructor that takes the root model part and gets the "base" child
    public TurretHeadModel(ModelPart root) {
        this.base = root.getChild("base");
    }

    // Defines the structure of the model (geometry, texture size, shape)
    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        // Add a cube at center (8x8x8)
        root.addOrReplaceChild("base",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(-4, -4, -4, 8, 8, 8),
                PartPose.ZERO); // no rotation or offset

        return LayerDefinition.create(mesh, 32, 32); // texture size 32x32
    }

    // Animates rotation of the base (used for yaw and pitch)
    @Override
    public void setupAnim(TurretHeadEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.base.yRot = netHeadYaw * Mth.DEG_TO_RAD; // yaw (left-right)
        this.base.xRot = headPitch * Mth.DEG_TO_RAD;  // pitch (up-down)
    }

    // Renders the model
    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer,
                               int packedLight, int packedOverlay,
                               float red, float green, float blue, float alpha) {
        base.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
    }
}
