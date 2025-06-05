package net.oleksandr.custom_turrets.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.oleksandr.custom_turrets.TurretsMod;
import net.oleksandr.custom_turrets.entity.TurretHeadEntity;

public class TurretHeadModel extends EntityModel<TurretHeadEntity> {

    public static final ModelLayerLocation LAYER_LOCATION =
            new ModelLayerLocation(new ResourceLocation(TurretsMod.MOD_ID, "turret_head"), "main");

    private final ModelPart base;

    public TurretHeadModel(ModelPart root) {
        this.base = root.getChild("base");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        root.addOrReplaceChild("base",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(-4, -4, -4, 8, 8, 8),
                PartPose.ZERO);

        return LayerDefinition.create(mesh, 32, 32);
    }

    @Override
    public void setupAnim(TurretHeadEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.base.yRot = netHeadYaw * Mth.DEG_TO_RAD;
        this.base.xRot = headPitch * Mth.DEG_TO_RAD;
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer,
                               int packedLight, int packedOverlay,
                               float red, float green, float blue, float alpha) {
        base.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
    }
}
