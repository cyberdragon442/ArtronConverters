package com.mrcyberdragon.artronconverters.blocks;

import com.mrcyberdragon.artronconverters.init.TileEntityInit;
import com.mrcyberdragon.artronconverters.tileentities.TileEntityArtronGenerator;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.tardis.mod.constants.TardisConstants;
import net.tardis.mod.helper.TardisHelper;
import net.tardis.mod.tileentities.ConsoleTile;

import javax.annotation.Nullable;
import java.util.List;

public class ArtronGenerator extends Block {

    private ConsoleTile console;

    public ArtronGenerator(Properties prop){
        super(prop);
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return TileEntityInit.ARTRON_GENERATOR.get().create();
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        if(!worldIn.isRemote()&&stack.getTag()!=null&&stack.getTag().contains(TardisConstants.CONSOLE_ATTUNEMENT_NBT_KEY)){
           TileEntity tile = worldIn.getTileEntity(pos);
           if(tile instanceof TileEntityArtronGenerator){
               TileEntityArtronGenerator tileentity = (TileEntityArtronGenerator)tile;
               CompoundNBT NBT = new CompoundNBT();
               NBT.putString("attuned_console",stack.getTag().getString(TardisConstants.CONSOLE_ATTUNEMENT_NBT_KEY));
               NBT.putString("tardis_name",stack.getTag().getString(TardisConstants.TARDIS_NAME_ATTUNMENT_NBT_KEY));
               tileentity.loadFromNBT(NBT);
           }
        }
        TardisHelper.getConsoleInWorld(worldIn).ifPresent(console -> this.console = console);
        if(worldIn.isRemote()&&console!=null&&stack.getTag()!=null){
            if(!stack.getTag().getString(TardisConstants.CONSOLE_ATTUNEMENT_NBT_KEY).equals(console.getWorld().getDimensionKey().getLocation().toString())){
                placer.sendMessage(new TranslationTextComponent("block.security_warning"), null);
            }
        }
        super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
    }

    @Override
    public void onBlockHarvested(World worldIn, BlockPos pos, BlockState state, PlayerEntity player) {
        TileEntity tile = worldIn.getTileEntity(pos);
        ItemStack stack = new ItemStack(state.getBlock());
        if(tile instanceof TileEntityArtronGenerator) {
            TileEntityArtronGenerator tileentity = (TileEntityArtronGenerator)tile;
            if (!worldIn.isRemote() && !player.isCreative() && tile.getTileData().getString("attuned_console") != null) {
                CompoundNBT NBT = tileentity.saveToNbt(new CompoundNBT());
                stack.setTag(NBT);
                ItemEntity itementity = new ItemEntity(worldIn, (double) pos.getX() + 0.5D, (double) pos.getY() + 0.5D, (double) pos.getZ() + 0.5D, stack);
                itementity.setDefaultPickupDelay();
                worldIn.addEntity(itementity);
            }
        }
        worldIn.setBlockState(pos, Blocks.AIR.getDefaultState());
        super.onBlockHarvested(worldIn, pos, state, player);
    }

    @Override
    public void addInformation(ItemStack stack, IBlockReader worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);
        tooltip.add(TardisConstants.Translations.TOOLTIP_HOLD_SHIFT);
        if(stack.getTag()==null||(stack.getTag()!=null&&!stack.getTag().contains(TardisConstants.CONSOLE_ATTUNEMENT_NBT_KEY))){
            tooltip.add(new TranslationTextComponent("tooltip.attunement_warning").mergeStyle(TextFormatting.RED));
        }
        if (Screen.hasShiftDown()) {
            tooltip.clear();
            tooltip.add(0, stack.getDisplayName());
            tooltip.add(new TranslationTextComponent("tooltip.artron_generator.purpose"));
            tooltip.add(new TranslationTextComponent("tooltip.artron_generator.redstone"));
        }

    }



}
