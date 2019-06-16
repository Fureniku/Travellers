package com.silvaniastudios.travellers.items;

import java.util.List;

import javax.annotation.Nullable;

import com.silvaniastudios.travellers.ModBlocks;
import com.silvaniastudios.travellers.blocks.tileentity.assembler.AssemblerBlock;
import com.silvaniastudios.travellers.blocks.tileentity.shipyard.ShipyardBlockCore;

import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemAssembler extends ItemBasic {

	public ItemAssembler(String name) {
		super(name);
	}
	
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		tooltip.add(I18n.format("travellers.tooltip.misc.assembler_a"));
		tooltip.add("");
		tooltip.add(I18n.format("travellers.tooltip.misc.assembler_b"));
	}
	
	public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (facing != EnumFacing.UP) {
			if (!worldIn.isRemote) { player.sendMessage(new TextComponentString(I18n.format("travellers.tooltip.ui.invalid_placement_flat"))); }
			return EnumActionResult.FAIL;
		} else {
			if (canPlaceBlocksAt(player.getHorizontalFacing(), worldIn, pos.offset(EnumFacing.UP))) {
				ShipyardBlockCore.EnumRotate rot = ShipyardBlockCore.EnumRotate.n;
				if (player.getHorizontalFacing() == EnumFacing.EAST ) { rot = ShipyardBlockCore.EnumRotate.e; }
				if (player.getHorizontalFacing() == EnumFacing.SOUTH) { rot = ShipyardBlockCore.EnumRotate.s; }
				if (player.getHorizontalFacing() == EnumFacing.WEST ) { rot = ShipyardBlockCore.EnumRotate.w; }
				
				worldIn.setBlockState(pos, ModBlocks.assembling_station.getDefaultState().withProperty(AssemblerBlock.ROTATE_ID, rot));
				return EnumActionResult.PASS;
			} else {
				if (!worldIn.isRemote) { player.sendMessage(new TextComponentString(I18n.format("travellers.tooltip.ui.invalid_placement"))); }
			}
		}
		return EnumActionResult.FAIL;
	}
	
	private boolean canPlaceBlocksAt(EnumFacing facing, World world, BlockPos pos) {
		EnumFacing left = EnumFacing.WEST;
		EnumFacing down = EnumFacing.SOUTH;
		
		if (facing == EnumFacing.EAST) {
			left = EnumFacing.NORTH;
			down = EnumFacing.WEST;
		}
		
		if (facing == EnumFacing.SOUTH) {
			left = EnumFacing.EAST;
			down = EnumFacing.NORTH;
		}
		
		if (facing == EnumFacing.WEST) {
			left = EnumFacing.SOUTH;
			down = EnumFacing.EAST;
		}
		
		if (!isReplaceable(world, pos)) {
			return false;
		}
		if (!isReplaceable(world, pos.offset(left))) {
			return false;
		}
		if (!isReplaceable(world, pos.offset(left).offset(down))) {
			return false;
		}
		if (!isReplaceable(world, pos.offset(down))) {
			return false;
		}
		
		return true;
	}
	
	private boolean isReplaceable(World world, BlockPos pos) {
		Block block = world.getBlockState(pos).getBlock();
		return block.isReplaceable(world, pos) || block instanceof BlockAir;
	}
}
