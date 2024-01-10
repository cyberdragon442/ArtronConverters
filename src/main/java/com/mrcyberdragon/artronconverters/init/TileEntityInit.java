package com.mrcyberdragon.artronconverters.init;

import com.google.common.base.Supplier;
import com.mrcyberdragon.artronconverters.ArtronConverters;
import com.mrcyberdragon.artronconverters.tileentities.TileEntityArtronCharger;
import com.mrcyberdragon.artronconverters.tileentities.TileEntityArtronConverter;
import com.mrcyberdragon.artronconverters.tileentities.TileEntityArtronGenerator;
import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;


public class TileEntityInit {

    public static List<TileEntityType<?>> TYPES = new ArrayList<TileEntityType<?>>();

    public static final DeferredRegister<TileEntityType<?>> TILES = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, ArtronConverters.MODID);

    public static final RegistryObject<TileEntityType<TileEntityArtronGenerator>> ARTRON_GENERATOR = TILES.register("artron_generator", () -> registerTiles(TileEntityArtronGenerator::new, BlockInit.ARTRON_GENERATOR.get()));
    public static final RegistryObject<TileEntityType<TileEntityArtronConverter>> ARTRON_CONVERTER = TILES.register("artron_converter", () -> registerTiles(TileEntityArtronConverter::new, BlockInit.ARTRON_CONVERTER.get()));
    public static final RegistryObject<TileEntityType<TileEntityArtronCharger>> ARTRON_CHARGER = TILES.register("artron_charger", () -> registerTiles(TileEntityArtronCharger::new, BlockInit.ARTRON_CHARGER.get()));

    private static <T extends TileEntity> TileEntityType<T> registerTiles(Supplier<T> tile, Block... validBlock) {
        TileEntityType<T> type = TileEntityType.Builder.create(tile, validBlock).build(null);
        return type;
    }
}
