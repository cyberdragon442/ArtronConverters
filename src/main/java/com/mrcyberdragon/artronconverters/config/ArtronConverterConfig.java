package com.mrcyberdragon.artronconverters.config;

import net.minecraftforge.common.ForgeConfigSpec;

public final class ArtronConverterConfig {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    public static final ForgeConfigSpec.ConfigValue<Integer> generator_capacity;
    public static final ForgeConfigSpec.ConfigValue<Integer> generator_usage;
    public static final ForgeConfigSpec.ConfigValue<Integer> generator_charge;
    public static final ForgeConfigSpec.ConfigValue<Integer> charger_capacity;
    public static final ForgeConfigSpec.ConfigValue<Integer> charger_usage;
    public static final ForgeConfigSpec.ConfigValue<Integer> charger_charge;
    public static final ForgeConfigSpec.ConfigValue<Integer> converter_capacity;
    public static final ForgeConfigSpec.ConfigValue<Integer> converter_generation;
    public static final ForgeConfigSpec.ConfigValue<Float> charger_max_artron;
    public static final ForgeConfigSpec.ConfigValue<Float> charger_rate;

    static {
        BUILDER.push("Electric Artron Generator");
        generator_capacity = BUILDER.comment("The amount of energy the Electric Artron Generator can hold.").define("Artron Generator Buffer", 100000);
        generator_usage = BUILDER.comment("The amount of energy the Electric Artron Generator needs to make one Artron Unit.").define("FE to AU rate", 25000);
        generator_charge = BUILDER.comment("The amount of energy per tick the Electric Artron Generator can charge at.").define("Artron Generator Charge Rate", 4096);
        BUILDER.pop();
        BUILDER.push("Artron Converter");
        converter_capacity = BUILDER.comment("The amount of energy the Artron Converter can hold.").define("Artron Converter Buffer", 100000);
        converter_generation = BUILDER.comment("The amount of energy created from one Artron Unit.").define("AU to FE rate", 1000);
        BUILDER.pop();
        BUILDER.push("Electric Artron Charger");
        charger_rate = BUILDER.comment("The amount of AU per second batteries can charge").define("Artron Charger Charge Rate", 4.0F);
        charger_capacity = BUILDER.comment("The amount of energy the Electric Artron Charger can hold.").define("Artron Charger Buffer", 100000);
        charger_usage = BUILDER.comment("The amount of energy the Electric Artron Charger needs to make one Artron Unit.").define("FE to AU rate", 25000);
        charger_charge = BUILDER.comment("The amount of energy per tick the Electric Artron Charger can refill itself at.").define("Artron Charger Refill Rate", 4096);
        charger_max_artron = BUILDER.comment("The amount of artron the Artron Charger can hold.").define("Atron Charger AU Buffer", 256F);
        BUILDER.pop();
        SPEC = BUILDER.build();
    }
}
