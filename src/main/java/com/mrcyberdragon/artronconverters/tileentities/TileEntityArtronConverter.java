package com.mrcyberdragon.artronconverters.tileentities;

import com.mrcyberdragon.artronconverters.config.ArtronConverterConfig;
import com.mrcyberdragon.artronconverters.init.ModEnergy;
import com.mrcyberdragon.artronconverters.init.SoundInit;
import com.mrcyberdragon.artronconverters.init.TileEntityInit;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.particles.RedstoneParticleData;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.EnergyStorage;
import net.tardis.mod.helper.TardisHelper;
import net.tardis.mod.tileentities.ConsoleTile;
import net.tardis.mod.tileentities.console.misc.ArtronUse;

import java.util.Random;

public class TileEntityArtronConverter extends TileEntity implements ITickableTileEntity {

    private int Buffer= ArtronConverterConfig.converter_capacity.get();
    private int GenerationRate=ArtronConverterConfig.converter_generation.get();
    private ModEnergy energy = new ModEnergy(Buffer, GenerationRate, Buffer);
    private LazyOptional<EnergyStorage> energyHolder = LazyOptional.of(() -> energy);
    private int tick = 7;
    private long time1 = 0;
    private long time2 = 0;
    private boolean energyFull = false;
    private boolean spawnParticle=false;

    private String TARDIS_ID;
    private String TARDIS_NAME;
    private ConsoleTile tile;

    public TileEntityArtronConverter(){ super(TileEntityInit.ARTRON_CONVERTER.get()); }

    @Override
    public void tick(){
        this.getCapability(CapabilityEnergy.ENERGY).ifPresent(cap -> {

            if (tile == null) return;

            if(world.isBlockPowered(this.getPos())) {
                if (0 < tick) {
                    tick--;
                }
                if(tick==7)time1=world.getGameTime();
                if(tick==0)
                {
                    time2=world.getGameTime();
                    tick=7;
                }
                if (tile != null && tile.getArtron() > 1 && energy.getEnergyStored() < (energy.getMaxEnergyStored()-GenerationRate)) {
                    if(TARDIS_ID!=null&&TARDIS_ID.equals(tile.getWorld().getDimensionKey().getLocation().toString())) {
                            cap.receiveEnergy(GenerationRate, false);
                            ArtronUse use = tile.getOrCreateArtronUse(ArtronUse.ArtronType.CONVERTER);
                            use.setArtronUsePerTick(1);
                            use.setTicksToDrain(1);
                            spawnParticle = true;
                            if ((time2 - time1) > 6) {
                                time1 = 0;
                                time2 = 0;
                                if (!world.isRemote()) {
                                    this.world.playSound(null, this.getPos(), SoundInit.ARTRON_GEN, SoundCategory.BLOCKS, 0.75F, 1F);
                                }
                                world.notifyBlockUpdate(this.getPos(), this.getBlockState(), this.getBlockState(), 2);
                            }
                    }
                }
                else {
                    if(energy.getEnergyStored()<energy.getMaxEnergyStored()) energyFull=false;
                    if (tick==0) {
                        if(!energyFull) {
                            if(energy.getEnergyStored()==energy.getMaxEnergyStored()) energyFull=true;
                            world.notifyBlockUpdate(this.getPos(), this.getBlockState(), this.getBlockState(), 2);
                            spawnParticle = false;
                        }
                    }
                }

                for (Direction dir : Direction.values()) {
                    TileEntity te = world.getTileEntity(getPos().offset(dir));
                    if (te != null) {
                        te.getCapability(CapabilityEnergy.ENERGY, dir.getOpposite()).ifPresent(power -> {
                            if(cap.getEnergyStored()>=GenerationRate) {
                                cap.extractEnergy(power.receiveEnergy(GenerationRate, false), false);
                            }
                        });
                    }
                }

            }
            else {
                if (0 < tick && tick <= 7) {
                    tick--;
                }
                if(energy.getEnergyStored()<energy.getMaxEnergyStored()) energyFull=false;
                if (tick==0) {
                    if(!energyFull) {
                        if(energy.getEnergyStored()==energy.getMaxEnergyStored()) energyFull=true;
                        world.notifyBlockUpdate(this.getPos(), this.getBlockState(), this.getBlockState(), 2);
                        spawnParticle = false;
                    }
                    tick=7;
                }
            }

    });

        if(tile == null || tile.isRemoved())
            TardisHelper.getConsoleInWorld(world).ifPresent(console -> this.tile = console);
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        compound.putBoolean("particle",spawnParticle);
        compound.put("energy", energy.serializeNBT());
        if(TARDIS_ID!=null)compound.putString("attuned_console",TARDIS_ID);
        if(TARDIS_NAME!=null)compound.putString("tardis_name",TARDIS_NAME);
        return super.write(compound);
    }

    @Override
    public void read(BlockState state, CompoundNBT nbt) {
        energy.deserializeNBT(nbt.getCompound("energy"));
        this.spawnParticle= nbt.getBoolean("particle");
        TARDIS_ID=nbt.getString("attuned_console");
        TARDIS_NAME=nbt.getString("tardis_name");
        super.read(state, nbt);
    }

    public CompoundNBT saveToNbt(CompoundNBT compound) {
        if(TARDIS_ID!=null)compound.putString("attuned_console",TARDIS_ID);
        if(TARDIS_NAME!=null)compound.putString("tardis_name",TARDIS_NAME);
        return compound;
    }

    public CompoundNBT loadFromNBT(CompoundNBT nbt){
        TARDIS_ID=nbt.getString("attuned_console");
        TARDIS_NAME=nbt.getString("tardis_name");
        return nbt;
    }

    @Override
    public CompoundNBT getUpdateTag() {
        return this.write(new CompoundNBT());
    }

    @Override
    public void handleUpdateTag(BlockState state, CompoundNBT tag) {
        this.read(state, tag);
    }

    @Override
    public SUpdateTileEntityPacket getUpdatePacket(){
        CompoundNBT nbtTag = new CompoundNBT();
        this.write(nbtTag);
        return new SUpdateTileEntityPacket(this.getPos(), -1, nbtTag);
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt){
        this.read(this.getBlockState(), pkt.getNbtCompound());
        Random random = new Random();
        if(spawnParticle) {
            for (int i = 5; i > 0; i--) {
                world.addParticle(RedstoneParticleData.REDSTONE_DUST, this.getPos().getX() + random.nextFloat(), this.getPos().getY() + random.nextFloat(), this.getPos().getZ() + random.nextFloat(), 0, 0, 0);
            }
        }
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        return cap == CapabilityEnergy.ENERGY ? this.energyHolder.cast() : super.getCapability(cap, side);
    }
}
