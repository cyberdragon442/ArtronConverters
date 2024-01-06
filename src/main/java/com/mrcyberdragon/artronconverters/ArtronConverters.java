package com.mrcyberdragon.artronconverters;

import com.mrcyberdragon.artronconverters.config.ArtronConverterConfig;
import com.mrcyberdragon.artronconverters.init.BlockInit;
import com.mrcyberdragon.artronconverters.init.SoundRegistry;
import com.mrcyberdragon.artronconverters.init.TileEntityInit;
import com.mrcyberdragon.artronconverters.init.ItemInit;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod("artrongen")
public class ArtronConverters
{
    public static final Logger LOGGER = LogManager.getLogger();
    public static final String MODID = "artrongen";

    public ArtronConverters() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        bus.addListener(this::setup);

        BlockInit.BLOCKS.register(bus);
        ItemInit.ITEMS.register(bus);
        TileEntityInit.TILES.register(bus);

        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(new SoundRegistry());

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, ArtronConverterConfig.SPEC, "artrongen-config.toml");
    }

    private void setup(final FMLCommonSetupEvent event)
    {
        int EnergyCapacity=ArtronConverterConfig.generator_capacity.get();
        int ConversionAmount=ArtronConverterConfig.generator_usage.get();
        int ChargeRate=ArtronConverterConfig.generator_charge.get();
        int GenRate=ArtronConverterConfig.converter_generation.get();
        int GenBuffer=ArtronConverterConfig.converter_capacity.get();

        if((ConversionAmount>EnergyCapacity)){
            throw new IllegalArgumentException("Config Error! Energy buffer must be greater than conversion/usage rate!");
        }
        if(GenRate>GenBuffer){
            throw new IllegalArgumentException("Config Error! Energy buffer must be greater than generation rate!");
        }
        if(EnergyCapacity==0||ConversionAmount<=0||ChargeRate<=0||GenRate<=0||GenBuffer<=0){
            throw new IllegalArgumentException("Config values must be a positive integer!");
        }
    }

}
