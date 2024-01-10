package com.mrcyberdragon.artronconverters.tileentities;

import com.mrcyberdragon.artronconverters.config.ArtronConverterConfig;
import com.mrcyberdragon.artronconverters.init.ModEnergy;
import com.mrcyberdragon.artronconverters.init.SoundInit;
import com.mrcyberdragon.artronconverters.init.TileEntityInit;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.EnergyStorage;
import net.tardis.mod.artron.IArtronHolder;
import net.tardis.mod.artron.IArtronItemStackBattery;
import net.tardis.mod.constants.TardisConstants;
import net.tardis.mod.network.Network;
import net.tardis.mod.particles.TParticleTypes;
import net.tardis.mod.sounds.TSounds;

public class TileEntityArtronCharger extends TileEntity implements ITickableTileEntity, IArtronHolder {

    private float artron = 0;
    private float maxartron = ArtronConverterConfig.charger_max_artron.get();
    private float chargerate = ArtronConverterConfig.charger_rate.get();
    private int EnergyCapacity = ArtronConverterConfig.charger_capacity.get();
    private int fillRate = ArtronConverterConfig.charger_charge.get();
    private int ConversionAmount = ArtronConverterConfig.charger_usage.get();
    private ModEnergy energy = new ModEnergy(EnergyCapacity, fillRate, EnergyCapacity);
    private LazyOptional<EnergyStorage> energyHolder = LazyOptional.of(() -> energy);
    ItemStack stack = ItemStack.EMPTY;
    private int particleSpawnTimer = TardisConstants.BUBBLE_PARTICLE_MAX_AGE;
    private boolean hasSpawnedInitialParticle = false;
    private boolean tick = false;
    private boolean energyfull = false;
    private boolean artronfull = false;


    public TileEntityArtronCharger(){
        super(TileEntityInit.ARTRON_CHARGER.get());
    }

    @Override
    public void tick(){
        if(world.getGameTime()%7==0){
            tick=true;
        }
        else tick=false;
        if (this.getItem().isEmpty()) {
            this.hasSpawnedInitialParticle = false;
        }
        if(!this.getItem().isEmpty() && world.isRemote()) {
            if (!this.hasSpawnedInitialParticle) {
                world.addParticle(TParticleTypes.BUBBLE.get(), pos.getX() + 0.5, pos.getY() + 1.5, pos.getZ() + 0.5, 0, 0.0, 0);
                this.hasSpawnedInitialParticle = true;
            }
            if (particleSpawnTimer > 0) {
                --particleSpawnTimer;
            }
            if (this.particleSpawnTimer == 7) {
                world.addParticle(TParticleTypes.BUBBLE.get(), pos.getX() + 0.5, pos.getY() + 1.5, pos.getZ() + 0.5, 0, 0.0, 0);
                this.resetParticleSpawnTimer();
            }
        }
        this.getCapability(CapabilityEnergy.ENERGY).ifPresent(cap -> {
        if(!world.isRemote) {
            if (this.getArtron() < maxartron) {
                if(energy.getEnergyStored() >= ConversionAmount) {
                    if (tick=true) {
                        this.recieveArtron(1.0F);
                        world.notifyBlockUpdate(this.getPos(), this.getBlockState(), this.getBlockState(), 2);
                        energy.extractEnergy(ConversionAmount, false);
                        this.world.playSound(null, this.getPos(), SoundInit.ARTRON_GEN, SoundCategory.BLOCKS, 1F, 1F);
                    }
                }
            }
            else if(artronfull==false&&this.getArtron()==this.maxartron){
                artronfull=true;
                world.notifyBlockUpdate(this.getPos(), this.getBlockState(), this.getBlockState(), 2);
            }
            else if(tick==true&&this.getArtron()<this.maxartron){
                artronfull=false;
                world.notifyBlockUpdate(this.getPos(), this.getBlockState(), this.getBlockState(), 2);
            }
            if(energyfull==false&&energy.getEnergyStored()==energy.getMaxEnergyStored()){
                energyfull=true;
                world.notifyBlockUpdate(this.getPos(), this.getBlockState(), this.getBlockState(), 2);
            }
            else if(tick==true&&energy.getEnergyStored()<energy.getMaxEnergyStored()){
                energyfull=false;
                world.notifyBlockUpdate(this.getPos(), this.getBlockState(), this.getBlockState(), 2);
            }
            if (this.getItem().getItem() instanceof IArtronItemStackBattery) {
                if (world.getGameTime() % 20 == 0) {
                    if(this.getArtron()>=chargerate&&((IArtronItemStackBattery)this.getItem().getItem()).getCharge(this.getItem())<((IArtronItemStackBattery)this.getItem().getItem()).getMaxCharge(this.getItem())) {
                        float artronUse = this.takeArtron(chargerate);
                        ((IArtronItemStackBattery)this.getItem().getItem()).charge(this.getItem(), artronUse/2.0F);
                        if (world.getGameTime() % 60 == 0) {
                            this.world.playSound(null, this.getPos(), TSounds.ELECTRIC_ARC.get(), SoundCategory.BLOCKS, 0.05F, 1F);
                        }
                        this.markDirty();
                    }
                }
            }
         }
        });
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
        super.onDataPacket(net, pkt);
        this.deserializeNBT(pkt.getNbtCompound());
    }

    @Override
    public void read(BlockState state, CompoundNBT compound) {
        super.read(state, compound);
        if(compound.contains("item"))
            this.stack = ItemStack.read(compound.getCompound("item"));
        this.artron = compound.getFloat("artron");
        energy.deserializeNBT(compound.getCompound("energy"));
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        compound.put("item", this.stack.serializeNBT());
        compound.put("energy", energy.serializeNBT());
        compound.putFloat("artron", this.artron);
        return super.write(compound);
    }

    @Override
    public SUpdateTileEntityPacket getUpdatePacket() {
        return Network.createTEUpdatePacket(this);
    }

    @Override
    public void handleUpdateTag(BlockState state, CompoundNBT tag) {
        this.read(state, tag);
    }

    @Override
    public CompoundNBT getUpdateTag() {
        return this.serializeNBT();
    }

    public void update() {
        if(!world.isRemote)
            world.notifyBlockUpdate(getPos(), getBlockState(), getBlockState(), 3);
    }


    public float getArtron() {
        return this.artron;
    }

    @Override
    public float recieveArtron(float amount) {
        this.artron += amount;
        return amount;
    }

    @Override
    public float takeArtron(float amt) {
        if(this.artron >= amt) {
            this.artron -= amt;
            return amt;
        }
        else {
            float oldArtron = this.artron;
            this.artron = 0;
            return oldArtron;
        }
    }

    public void placeItem(ItemStack stack) {
        this.stack = stack;
    }

    public ItemStack getItem() {
        return this.stack;
    }

    public void resetParticleSpawnTimer() {
        this.particleSpawnTimer = TardisConstants.BUBBLE_PARTICLE_MAX_AGE;
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        return cap == CapabilityEnergy.ENERGY && (side==Direction.DOWN || side==null) ? this.energyHolder.cast() : super.getCapability(cap, side);
    }

}
