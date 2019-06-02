package com.silvaniastudios.travellers.items;

import com.silvaniastudios.travellers.ModBlocks;
import com.silvaniastudios.travellers.blocks.shipyard.ShipyardBlockCore;
import com.silvaniastudios.travellers.blocks.shipyard.ShipyardBlockParts;

import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

public class ItemShipyard extends ItemBasic {

	public ItemShipyard(String name) {
		super(name);
	}
	
	public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (facing != EnumFacing.UP) {
			if (!worldIn.isRemote) { player.sendMessage(new TextComponentString(I18n.format("travellers.tooltip.ui.invalid_placement_flat"))); }
			return EnumActionResult.FAIL;
		} else {
			if (canPlaceBlocksAt(player.getHorizontalFacing(), worldIn, pos.offset(EnumFacing.UP))) {
				placeBlocks(player.getHorizontalFacing(), worldIn, pos.offset(EnumFacing.UP));
				return EnumActionResult.PASS;
			} else {
				if (!worldIn.isRemote) { player.sendMessage(new TextComponentString(I18n.format("travellers.tooltip.ui.invalid_placement"))); }
			}
		}
		return EnumActionResult.FAIL;
	}
	
	private boolean canPlaceBlocksAt(EnumFacing facing, World world, BlockPos pos) {
		EnumFacing up = EnumFacing.NORTH;
		EnumFacing left = EnumFacing.WEST;
		EnumFacing right = EnumFacing.EAST;
		EnumFacing down = EnumFacing.SOUTH;
		
		if (facing == EnumFacing.EAST) {
			up = EnumFacing.EAST;
			left = EnumFacing.NORTH;
			right = EnumFacing.SOUTH;
			down = EnumFacing.WEST;
		}
		
		if (facing == EnumFacing.SOUTH) {
			up = EnumFacing.SOUTH;
			left = EnumFacing.EAST;
			right = EnumFacing.WEST;
			down = EnumFacing.NORTH;
		}
		
		if (facing == EnumFacing.WEST) {
			up = EnumFacing.WEST;
			left = EnumFacing.SOUTH;
			right = EnumFacing.NORTH;
			down = EnumFacing.EAST;
		}
		
		
		BlockPos startPoint = pos.offset(up, 2).offset(left, 3);
		
		for (int i = 0; i < 6; i++) {
			for (int j = 0; j < 6; j++) {
				BlockPos checkPoint = startPoint.offset(right, j).offset(down, i);
				System.out.println("Checking placement at " + checkPoint.getX() + ", " + checkPoint.getY() + ", " + checkPoint.getZ());
				if (!isReplaceable(world, checkPoint)) {
					System.out.println("Invalid placement detected. Cancelling.");
					return false;
				}
			}
		}
		
		System.out.println("Placement appears valid. Place!");
		
		return world.getBlockState(pos).getBlock().isReplaceable(world, pos);
	}
	
	private boolean isReplaceable(World world, BlockPos pos) {
		Block block = world.getBlockState(pos).getBlock();
		return block.isReplaceable(world, pos) || block instanceof BlockAir;
	}
	
	private void placeBlocks(EnumFacing facing, World world, BlockPos pos) {
		EnumFacing up = EnumFacing.NORTH;
		EnumFacing left = EnumFacing.WEST;
		EnumFacing right = EnumFacing.EAST;
		EnumFacing down = EnumFacing.SOUTH;
		
		ShipyardBlockParts.EnumParts topLeft = ShipyardBlockParts.EnumParts.angle_nw;
		ShipyardBlockParts.EnumParts topRight = ShipyardBlockParts.EnumParts.angle_ne;
		ShipyardBlockParts.EnumParts bottomLeft = ShipyardBlockParts.EnumParts.angle_sw;
		ShipyardBlockParts.EnumParts bottomRight = ShipyardBlockParts.EnumParts.angle_se;
		
		int rotation = 0;
		
		if (facing == EnumFacing.EAST) {
			up = EnumFacing.EAST;
			left = EnumFacing.NORTH;
			right = EnumFacing.SOUTH;
			down = EnumFacing.WEST;
			
			topLeft = ShipyardBlockParts.EnumParts.angle_ne;
			topRight = ShipyardBlockParts.EnumParts.angle_se;
			bottomLeft = ShipyardBlockParts.EnumParts.angle_nw;
			bottomRight = ShipyardBlockParts.EnumParts.angle_sw;
			
			rotation = 1;
		}
		
		if (facing == EnumFacing.SOUTH) {
			up = EnumFacing.SOUTH;
			left = EnumFacing.EAST;
			right = EnumFacing.WEST;
			down = EnumFacing.NORTH;
			
			topLeft = ShipyardBlockParts.EnumParts.angle_se;
			topRight = ShipyardBlockParts.EnumParts.angle_sw;
			bottomLeft = ShipyardBlockParts.EnumParts.angle_ne;
			bottomRight = ShipyardBlockParts.EnumParts.angle_nw;
			
			rotation = 2;
		}
		
		if (facing == EnumFacing.WEST) {
			up = EnumFacing.WEST;
			left = EnumFacing.SOUTH;
			right = EnumFacing.NORTH;
			down = EnumFacing.EAST;
			
			topLeft = ShipyardBlockParts.EnumParts.angle_sw;
			topRight = ShipyardBlockParts.EnumParts.angle_nw;
			bottomLeft = ShipyardBlockParts.EnumParts.angle_se;
			bottomRight = ShipyardBlockParts.EnumParts.angle_ne;
			
			rotation = 3;
		}
		
		BlockPos startPoint = pos.offset(up, 2).offset(left, 3);
		
		for (int i = 0; i < 6; i++) {
			for (int j = 0; j < 6; j++) {
				BlockPos checkPoint = startPoint.offset(right, i).offset(down, j);
				if ((i == 0 && j == 0) || (i == 0 && j == 5) || (i == 5 && j == 0) || (i == 5 && j == 5)) {
					world.setBlockToAir(checkPoint);
				} else {
					IBlockState placementState = ModBlocks.block_shipyard_parts.getDefaultState();
					if ((i == 1 && j == 0) || (i == 0 && j == 1)) {
						placementState = placementState.withProperty(ShipyardBlockParts.PART_ID, topLeft);
					}
					if ((i == 4 && j == 0) || (i == 5 && j == 1)) {
						placementState = placementState.withProperty(ShipyardBlockParts.PART_ID, topRight);
					}
					if ((i == 0 && j == 4) || (i == 1 && j == 5)) {
						placementState = placementState.withProperty(ShipyardBlockParts.PART_ID, bottomLeft);
					}
					if ((i == 5 && j == 4) || (i == 4 && j == 5)) {
						placementState = placementState.withProperty(ShipyardBlockParts.PART_ID, bottomRight);
					}
					
					System.out.println("Checking placement at " + checkPoint.getX() + ", " + checkPoint.getY() + ", " + checkPoint.getZ());
					world.setBlockState(checkPoint, placementState);
				}
			}
		}
		
		IBlockState coreState = ModBlocks.block_shipyard_core.getDefaultState().withProperty(ShipyardBlockCore.ROTATE_ID, ShipyardBlockCore.EnumRotate.byMetadata(rotation));
		world.setBlockState(pos.offset(EnumFacing.UP), coreState);
	}
}
