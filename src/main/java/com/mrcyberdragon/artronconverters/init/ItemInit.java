package com.mrcyberdragon.artronconverters.init;

import com.mrcyberdragon.artronconverters.ArtronConverters;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.tardis.mod.itemgroups.TItemGroups;

public class ItemInit {

    public static DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, ArtronConverters.MODID);

    public static final RegistryObject<BlockItem> ARTRON_GENERATOR = ITEMS.register("artron_generator", () -> new BlockItem(BlockInit.ARTRON_GENERATOR.get(), new Item.Properties().group(TItemGroups.MAINTENANCE)));
    public static final RegistryObject<BlockItem> UNATTUNED_GENERATOR = ITEMS.register("unattuned_generator", () -> new BlockItem(BlockInit.UNATTUNED_GENERATOR.get(), new Item.Properties().group(TItemGroups.MAINTENANCE)));
    public static final RegistryObject<BlockItem> ARTRON_CONVERTER = ITEMS.register("artron_converter", () -> new BlockItem(BlockInit.ARTRON_CONVERTER.get(), new Item.Properties().group(TItemGroups.MAINTENANCE)));
    public static final RegistryObject<BlockItem> UNATTUNED_CONVERTER = ITEMS.register("unattuned_converter", () -> new BlockItem(BlockInit.UNATTUNED_CONVERTER.get(), new Item.Properties().group(TItemGroups.MAINTENANCE)));
    public static final RegistryObject<BlockItem> ARTRON_CHARGER = ITEMS.register("artron_charger", () -> new BlockItem(BlockInit.ARTRON_CHARGER.get(), new Item.Properties().group(TItemGroups.MAINTENANCE)));
}
