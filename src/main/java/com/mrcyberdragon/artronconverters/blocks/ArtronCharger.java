package com.mrcyberdragon.artronconverters.blocks;

import com.mrcyberdragon.artronconverters.init.TileEntityInit;
import com.mrcyberdragon.artronconverters.tileentities.TileEntityArtronCharger;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.tardis.mod.constants.TardisConstants;
import net.tardis.mod.helper.TInventoryHelper;

import java.util.List;

public class ArtronCharger extends Block {

    public ArtronCharger(Properties prop){
        super(prop);
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return TileEntityInit.ARTRON_CHARGER.get().create();
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {

        if(handIn == Hand.OFF_HAND)
            return ActionResultType.FAIL;

        TileEntityArtronCharger tile = (TileEntityArtronCharger) worldIn.getTileEntity(pos);
        ItemStack stack = player.getHeldItem(handIn);

        if(!tile.getItem().isEmpty())
            this.dropCurrentItem(tile, player);
        if (!stack.isEmpty()) {
            tile.placeItem(stack.copy());
            player.setHeldItem(handIn, ItemStack.EMPTY);
            tile.update();
        }
        return ActionResultType.SUCCESS;
    }

    @Override
    public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
        if(!worldIn.isRemote() && state.getBlock() != newState.getBlock()) {
            TileEntityArtronCharger tile = (TileEntityArtronCharger) worldIn.getTileEntity(pos);
            if(!tile.getItem().isEmpty())
                InventoryHelper.spawnItemStack(worldIn, pos.getX() + 0.5, pos.getY() + 2, pos.getZ() + 0.5, tile.getItem());
        }
        super.onReplaced(state, worldIn, pos, newState, isMoving);
    }


    public boolean dropCurrentItem(TileEntityArtronCharger tile, PlayerEntity player) {
        if (!tile.getItem().isEmpty()) {
            TInventoryHelper.giveStackTo(player, tile.getItem());
            tile.placeItem(ItemStack.EMPTY);
            tile.update();
            return true;
        }
        return false;
    }

    @Override
    public void addInformation(ItemStack stack, IBlockReader worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);
        tooltip.add(TardisConstants.Translations.TOOLTIP_HOLD_SHIFT);
        if (Screen.hasShiftDown()) {
            tooltip.clear();
            tooltip.add(0, stack.getDisplayName());
            tooltip.add(new TranslationTextComponent("tooltip.artron_charger.purpose"));
        }
    }
}
