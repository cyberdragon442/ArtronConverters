package com.mrcyberdragon.artronconverters.client.renderers;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mrcyberdragon.artronconverters.tileentities.TileEntityArtronCharger;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;

public class ArtronChargerRenderer extends TileEntityRenderer<TileEntityArtronCharger> {

    public ArtronChargerRenderer(TileEntityRendererDispatcher rendererDispatcherIn) {
        super(rendererDispatcherIn);
    }


    @Override
    public void render(TileEntityArtronCharger tile, float partialTicks, MatrixStack matrixStackIn,
                       IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn) {
        matrixStackIn.push();
        double flo = Math.cos(Minecraft.getInstance().world.getGameTime() * 0.05) * 0.05;
        matrixStackIn.translate(0.25, 0.6 + flo, 0.25);
        matrixStackIn.translate(0.25, 0.75 + flo, 0.25);
        Minecraft.getInstance().getItemRenderer().renderItem(tile.getItem(), ItemCameraTransforms.TransformType.GROUND, combinedLightIn, combinedOverlayIn, matrixStackIn, bufferIn);
        matrixStackIn.pop();
    }

}
