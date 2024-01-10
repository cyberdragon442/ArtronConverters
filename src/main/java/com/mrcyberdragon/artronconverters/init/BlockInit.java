package com.mrcyberdragon.artronconverters.init;

import com.mrcyberdragon.artronconverters.ArtronConverters;
import com.mrcyberdragon.artronconverters.blocks.*;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class BlockInit {
    public static DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, ArtronConverters.MODID);

    public static final RegistryObject<ArtronGenerator> ARTRON_GENERATOR = BLOCKS.register("artron_generator", () -> new ArtronGenerator(AbstractBlock.Properties.create(Material.IRON).hardnessAndResistance(5F, 6F).harvestTool(ToolType.PICKAXE).harvestLevel(2).sound(SoundType.METAL).notSolid()));
    public static final RegistryObject<UnattunedGenerator> UNATTUNED_GENERATOR = BLOCKS.register("unattuned_generator", () -> new UnattunedGenerator(AbstractBlock.Properties.create(Material.IRON).hardnessAndResistance(5F, 6F).harvestTool(ToolType.PICKAXE).harvestLevel(2).sound(SoundType.METAL).notSolid()));
    public static final RegistryObject<ArtronConverter> ARTRON_CONVERTER = BLOCKS.register("artron_converter", () -> new ArtronConverter(AbstractBlock.Properties.create(Material.IRON).hardnessAndResistance(5F, 6F).harvestTool(ToolType.PICKAXE).harvestLevel(2).sound(SoundType.METAL).notSolid()));
    public static final RegistryObject<UnattunedConverter> UNATTUNED_CONVERTER = BLOCKS.register("unattuned_converter", () -> new UnattunedConverter(AbstractBlock.Properties.create(Material.IRON).hardnessAndResistance(5F, 6F).harvestTool(ToolType.PICKAXE).harvestLevel(2).sound(SoundType.METAL).notSolid()));
    public static final RegistryObject<ArtronCharger>  ARTRON_CHARGER = BLOCKS.register("artron_charger", () -> new ArtronCharger(AbstractBlock.Properties.create(Material.IRON).hardnessAndResistance(5F, 6F).harvestTool(ToolType.PICKAXE).harvestLevel(2).sound(SoundType.METAL).notSolid()));
}
