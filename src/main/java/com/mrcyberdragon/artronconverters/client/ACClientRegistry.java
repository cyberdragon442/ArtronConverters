package com.mrcyberdragon.artronconverters.client;

import com.mrcyberdragon.artronconverters.client.renderers.ArtronChargerRenderer;
import com.mrcyberdragon.artronconverters.init.TileEntityInit;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(
        value = {Dist.CLIENT},
        modid = "artrongen",
        bus = Mod.EventBusSubscriber.Bus.MOD
)
public class ACClientRegistry {
    public ACClientRegistry(){

    }

    @SubscribeEvent
    public static void register(FMLClientSetupEvent event) {
        registerTileRenderers();
    }

    private static void registerTileRenderers(){
        ClientRegistry.bindTileEntityRenderer((TileEntityType) TileEntityInit.ARTRON_CHARGER.get(), ArtronChargerRenderer::new);
    }
}
