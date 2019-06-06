package com.silvaniastudios.travellers.items;

import com.silvaniastudios.travellers.ModBlocks;
import com.silvaniastudios.travellers.blocks.shipyard.ShipyardBlockCore;
import com.silvaniastudios.travellers.blocks.shipyard.ShipyardBlockParts;
import com.silvaniastudios.travellers.blocks.shipyard.ShipyardBlockPartsFlap;
import com.silvaniastudios.travellers.blocks.shipyard.ShipyardBlockPartsRamp;

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
	
	//This is a mess, please avert your eyes.
	//Can't use strucutres, because I want the placement to "animate" later.
	private void placeBlocks(EnumFacing facing, World world, BlockPos pos) {		
		ShipyardBlockParts.EnumParts base_plate = ShipyardBlockParts.EnumParts.main;
		ShipyardBlockParts.EnumParts plate_n = ShipyardBlockParts.EnumParts.plate_n;
		ShipyardBlockParts.EnumParts plate_e = ShipyardBlockParts.EnumParts.plate_e;
		ShipyardBlockParts.EnumParts plate_s = ShipyardBlockParts.EnumParts.plate_s;
		ShipyardBlockParts.EnumParts plate_w = ShipyardBlockParts.EnumParts.plate_w;
		
		ShipyardBlockParts.EnumParts topLeft_l = ShipyardBlockParts.EnumParts.angle_nw_l;
		ShipyardBlockParts.EnumParts topLeft_r = ShipyardBlockParts.EnumParts.angle_nw_r;
		ShipyardBlockParts.EnumParts topRight_l = ShipyardBlockParts.EnumParts.angle_ne_l;
		ShipyardBlockParts.EnumParts topRight_r = ShipyardBlockParts.EnumParts.angle_ne_r;
		ShipyardBlockParts.EnumParts bottomLeft_l = ShipyardBlockParts.EnumParts.angle_sw_l;
		ShipyardBlockParts.EnumParts bottomLeft_r = ShipyardBlockParts.EnumParts.angle_sw_r;
		ShipyardBlockParts.EnumParts bottomRight_l = ShipyardBlockParts.EnumParts.angle_se_l;
		ShipyardBlockParts.EnumParts bottomRight_r = ShipyardBlockParts.EnumParts.angle_se_r;
		
		ShipyardBlockPartsFlap.EnumParts flap_top_l = ShipyardBlockPartsFlap.EnumParts.flap_n_l;
		ShipyardBlockPartsFlap.EnumParts flap_top_r = ShipyardBlockPartsFlap.EnumParts.flap_n_r;
		ShipyardBlockPartsFlap.EnumParts flap_left_l = ShipyardBlockPartsFlap.EnumParts.flap_w_l;
		ShipyardBlockPartsFlap.EnumParts flap_left_r = ShipyardBlockPartsFlap.EnumParts.flap_w_r;
		ShipyardBlockPartsFlap.EnumParts flap_down_l = ShipyardBlockPartsFlap.EnumParts.flap_s_l;
		ShipyardBlockPartsFlap.EnumParts flap_down_r = ShipyardBlockPartsFlap.EnumParts.flap_s_r;
		ShipyardBlockPartsFlap.EnumParts flap_right_l = ShipyardBlockPartsFlap.EnumParts.flap_e_l;
		ShipyardBlockPartsFlap.EnumParts flap_right_r = ShipyardBlockPartsFlap.EnumParts.flap_e_r;
		
		ShipyardBlockPartsFlap.EnumParts flap_top_left_l = ShipyardBlockPartsFlap.EnumParts.flap_nw_l;
		ShipyardBlockPartsFlap.EnumParts flap_top_left_r = ShipyardBlockPartsFlap.EnumParts.flap_nw_r;
		ShipyardBlockPartsFlap.EnumParts flap_down_left_l = ShipyardBlockPartsFlap.EnumParts.flap_sw_l;
		ShipyardBlockPartsFlap.EnumParts flap_down_left_r = ShipyardBlockPartsFlap.EnumParts.flap_sw_r;
		ShipyardBlockPartsFlap.EnumParts flap_down_right_l = ShipyardBlockPartsFlap.EnumParts.flap_se_l;
		ShipyardBlockPartsFlap.EnumParts flap_down_right_r = ShipyardBlockPartsFlap.EnumParts.flap_se_r;
		ShipyardBlockPartsFlap.EnumParts flap_top_right_l = ShipyardBlockPartsFlap.EnumParts.flap_ne_l;
		ShipyardBlockPartsFlap.EnumParts flap_top_right_r = ShipyardBlockPartsFlap.EnumParts.flap_ne_r;
		
		BlockPos startPoint = pos.offset(EnumFacing.NORTH, 2).offset(EnumFacing.WEST, 3);
		
		int baseId = 0;
		
		for (int i = 0; i < 6; i++) {
			for (int j = 0; j < 6; j++) {
				BlockPos checkPoint = startPoint.offset(EnumFacing.EAST, j).offset(EnumFacing.SOUTH, i);
				IBlockState placementState = ModBlocks.block_shipyard_parts.getDefaultState();

				System.out.println("baseId: " + baseId + ", position: " + checkPoint.getX() + ", " + checkPoint.getY() + ", " + checkPoint.getZ());
				
				if (baseId == 0 || baseId == 5|| baseId == 30 || baseId == 35) {
					world.setBlockToAir(checkPoint);
					baseId++;
					continue;
				} else if (baseId == 1) {
					placementState = placementState.withProperty(ShipyardBlockParts.PART_ID, topLeft_r);
				} else if (baseId == 2 || baseId == 32) {
					placementState = placementState.withProperty(ShipyardBlockParts.PART_ID, plate_w);
				} else if (baseId == 3 || baseId == 33) {
					placementState = placementState.withProperty(ShipyardBlockParts.PART_ID, plate_e);
				} else if (baseId == 4) {
					placementState = placementState.withProperty(ShipyardBlockParts.PART_ID, topRight_l);
				} else if (baseId == 6) {
					placementState = placementState.withProperty(ShipyardBlockParts.PART_ID, topLeft_l);
				} else if (baseId == 11) {
					placementState = placementState.withProperty(ShipyardBlockParts.PART_ID, topRight_r);
				} else if (baseId == 12 || baseId == 17) {
					placementState = placementState.withProperty(ShipyardBlockParts.PART_ID, plate_n);
				} else if (baseId == 18 || baseId == 23) {
					placementState = placementState.withProperty(ShipyardBlockParts.PART_ID, plate_s);
				} else if (baseId == 24) {
					placementState = placementState.withProperty(ShipyardBlockParts.PART_ID, bottomLeft_r);
				} else if (baseId == 29) {
					placementState = placementState.withProperty(ShipyardBlockParts.PART_ID, bottomRight_l);
				} else if (baseId == 31) {
					placementState = placementState.withProperty(ShipyardBlockParts.PART_ID, bottomLeft_l);
				} else if (baseId == 34) {
					placementState = placementState.withProperty(ShipyardBlockParts.PART_ID, bottomRight_r);
				} else {
					placementState = placementState.withProperty(ShipyardBlockParts.PART_ID, base_plate);
				}
				world.setBlockState(checkPoint, placementState);
				baseId++;
			}
		}
		
		startPoint = startPoint.offset(EnumFacing.NORTH).offset(EnumFacing.WEST).offset(EnumFacing.UP);
		int id = 0;
		
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				BlockPos checkPoint = startPoint.offset(EnumFacing.EAST, j).offset(EnumFacing.SOUTH, i);
				IBlockState placementState = ModBlocks.block_shipyard_parts_flap.getDefaultState();
				
				if (id == 3) {
					placementState = placementState.withProperty(ShipyardBlockPartsFlap.PART_ID, flap_top_l);
				} else if (id == 4) {
					placementState = placementState.withProperty(ShipyardBlockPartsFlap.PART_ID, flap_top_r);
				} else if (id == 10) {
					placementState = placementState.withProperty(ShipyardBlockPartsFlap.PART_ID, flap_top_left_r);
				} else if (id == 13) {
					placementState = placementState.withProperty(ShipyardBlockPartsFlap.PART_ID, flap_top_right_l);
				} else if (id == 17) {
					placementState = placementState.withProperty(ShipyardBlockPartsFlap.PART_ID, flap_top_left_l);
				} else if (id == 22) {
					placementState = placementState.withProperty(ShipyardBlockPartsFlap.PART_ID, flap_top_right_r);
				} else if (id == 24) {
					placementState = placementState.withProperty(ShipyardBlockPartsFlap.PART_ID, flap_left_r);
				} else if (id == 31) {
					placementState = placementState.withProperty(ShipyardBlockPartsFlap.PART_ID, flap_right_l);
				} else if (id == 32) {
					placementState = placementState.withProperty(ShipyardBlockPartsFlap.PART_ID, flap_left_l);
				} else if (id == 39) {
					placementState = placementState.withProperty(ShipyardBlockPartsFlap.PART_ID, flap_right_r);
				} else if (id == 41) {
					placementState = placementState.withProperty(ShipyardBlockPartsFlap.PART_ID, flap_down_left_r);
				} else if (id == 46) {
					placementState = placementState.withProperty(ShipyardBlockPartsFlap.PART_ID, flap_down_right_l);
				} else if (id == 50) {
					placementState = placementState.withProperty(ShipyardBlockPartsFlap.PART_ID, flap_down_left_l);
				} else if (id == 53) {
					placementState = placementState.withProperty(ShipyardBlockPartsFlap.PART_ID, flap_down_right_r);
				} else if (id == 59) {
					placementState = placementState.withProperty(ShipyardBlockPartsFlap.PART_ID, flap_down_r);
				} else if (id == 60) {
					placementState = placementState.withProperty(ShipyardBlockPartsFlap.PART_ID, flap_down_l);
					
				} else if (id == 27 && facing == EnumFacing.WEST) {
					IBlockState coreState = ModBlocks.block_shipyard_core.getDefaultState().withProperty(ShipyardBlockCore.ROTATE_ID, ShipyardBlockCore.EnumRotate.w);
					world.setBlockState(checkPoint, coreState);
					id++;
					continue;
				} else if (id == 28 && facing == EnumFacing.NORTH) {
					IBlockState coreState = ModBlocks.block_shipyard_core.getDefaultState().withProperty(ShipyardBlockCore.ROTATE_ID, ShipyardBlockCore.EnumRotate.n);
					world.setBlockState(checkPoint, coreState);
					id++;
					continue;
				} else if (id == 35 && facing == EnumFacing.SOUTH) {
					IBlockState coreState = ModBlocks.block_shipyard_core.getDefaultState().withProperty(ShipyardBlockCore.ROTATE_ID, ShipyardBlockCore.EnumRotate.s);
					world.setBlockState(checkPoint, coreState);
					id++;
					continue;
				} else if (id == 36 && facing == EnumFacing.EAST) {
					IBlockState coreState = ModBlocks.block_shipyard_core.getDefaultState().withProperty(ShipyardBlockCore.ROTATE_ID, ShipyardBlockCore.EnumRotate.e);
					world.setBlockState(checkPoint, coreState);
					id++;
					continue;
					
				} else {
					id++;
					continue;
				}
				world.setBlockState(checkPoint, placementState);
				id++;
			}
		}
		
		startPoint = startPoint.offset(EnumFacing.SOUTH).offset(EnumFacing.EAST).offset(EnumFacing.DOWN);
		
		if (facing == EnumFacing.NORTH) {
			BlockPos rampPoint = startPoint.offset(EnumFacing.SOUTH, 6).offset(EnumFacing.EAST, 2);
			IBlockState ramp_a_l = ModBlocks.block_shipyard_parts_ramp.getDefaultState().withProperty(ShipyardBlockPartsRamp.PART_ID, ShipyardBlockPartsRamp.EnumParts.ramp_a_n_l);
			IBlockState ramp_a_r = ModBlocks.block_shipyard_parts_ramp.getDefaultState().withProperty(ShipyardBlockPartsRamp.PART_ID, ShipyardBlockPartsRamp.EnumParts.ramp_a_n_r);
			IBlockState ramp_b_l = ModBlocks.block_shipyard_parts_ramp.getDefaultState().withProperty(ShipyardBlockPartsRamp.PART_ID, ShipyardBlockPartsRamp.EnumParts.ramp_b_n_l);
			IBlockState ramp_b_r = ModBlocks.block_shipyard_parts_ramp.getDefaultState().withProperty(ShipyardBlockPartsRamp.PART_ID, ShipyardBlockPartsRamp.EnumParts.ramp_b_n_r);
			
			world.setBlockState(rampPoint, ramp_a_l);
			world.setBlockState(rampPoint.offset(EnumFacing.EAST), ramp_a_r);
			world.setBlockState(rampPoint.offset(EnumFacing.SOUTH), ramp_b_l);
			world.setBlockState(rampPoint.offset(EnumFacing.SOUTH).offset(EnumFacing.EAST), ramp_b_r);
			
			world.setBlockToAir(rampPoint.offset(EnumFacing.UP));
			world.setBlockToAir(rampPoint.offset(EnumFacing.UP).offset(EnumFacing.EAST));
		}
		
		if (facing == EnumFacing.EAST) {
			BlockPos rampPoint = startPoint.offset(EnumFacing.SOUTH, 2).offset(EnumFacing.WEST, 1);
			IBlockState ramp_a_l = ModBlocks.block_shipyard_parts_ramp.getDefaultState().withProperty(ShipyardBlockPartsRamp.PART_ID, ShipyardBlockPartsRamp.EnumParts.ramp_a_e_l);
			IBlockState ramp_a_r = ModBlocks.block_shipyard_parts_ramp.getDefaultState().withProperty(ShipyardBlockPartsRamp.PART_ID, ShipyardBlockPartsRamp.EnumParts.ramp_a_e_r);
			IBlockState ramp_b_l = ModBlocks.block_shipyard_parts_ramp.getDefaultState().withProperty(ShipyardBlockPartsRamp.PART_ID, ShipyardBlockPartsRamp.EnumParts.ramp_b_e_l);
			IBlockState ramp_b_r = ModBlocks.block_shipyard_parts_ramp.getDefaultState().withProperty(ShipyardBlockPartsRamp.PART_ID, ShipyardBlockPartsRamp.EnumParts.ramp_b_e_r);
			
			world.setBlockState(rampPoint, ramp_a_l);
			world.setBlockState(rampPoint.offset(EnumFacing.SOUTH), ramp_a_r);
			world.setBlockState(rampPoint.offset(EnumFacing.WEST), ramp_b_l);
			world.setBlockState(rampPoint.offset(EnumFacing.WEST).offset(EnumFacing.SOUTH), ramp_b_r);
			
			world.setBlockToAir(rampPoint.offset(EnumFacing.UP));
			world.setBlockToAir(rampPoint.offset(EnumFacing.UP).offset(EnumFacing.SOUTH));
		}
		
		if (facing == EnumFacing.SOUTH) {
			BlockPos rampPoint = startPoint.offset(EnumFacing.NORTH, 1).offset(EnumFacing.EAST, 3);
			IBlockState ramp_a_l = ModBlocks.block_shipyard_parts_ramp.getDefaultState().withProperty(ShipyardBlockPartsRamp.PART_ID, ShipyardBlockPartsRamp.EnumParts.ramp_a_s_l);
			IBlockState ramp_a_r = ModBlocks.block_shipyard_parts_ramp.getDefaultState().withProperty(ShipyardBlockPartsRamp.PART_ID, ShipyardBlockPartsRamp.EnumParts.ramp_a_s_r);
			IBlockState ramp_b_l = ModBlocks.block_shipyard_parts_ramp.getDefaultState().withProperty(ShipyardBlockPartsRamp.PART_ID, ShipyardBlockPartsRamp.EnumParts.ramp_b_s_l);
			IBlockState ramp_b_r = ModBlocks.block_shipyard_parts_ramp.getDefaultState().withProperty(ShipyardBlockPartsRamp.PART_ID, ShipyardBlockPartsRamp.EnumParts.ramp_b_s_r);
			
			world.setBlockState(rampPoint, ramp_a_l);
			world.setBlockState(rampPoint.offset(EnumFacing.WEST), ramp_a_r);
			world.setBlockState(rampPoint.offset(EnumFacing.NORTH), ramp_b_l);
			world.setBlockState(rampPoint.offset(EnumFacing.NORTH).offset(EnumFacing.WEST), ramp_b_r);
			
			world.setBlockToAir(rampPoint.offset(EnumFacing.UP));
			world.setBlockToAir(rampPoint.offset(EnumFacing.UP).offset(EnumFacing.WEST));
		}
		
		if (facing == EnumFacing.WEST) {
			BlockPos rampPoint = startPoint.offset(EnumFacing.SOUTH, 3).offset(EnumFacing.EAST, 6);
			IBlockState ramp_a_l = ModBlocks.block_shipyard_parts_ramp.getDefaultState().withProperty(ShipyardBlockPartsRamp.PART_ID, ShipyardBlockPartsRamp.EnumParts.ramp_a_w_l);
			IBlockState ramp_a_r = ModBlocks.block_shipyard_parts_ramp.getDefaultState().withProperty(ShipyardBlockPartsRamp.PART_ID, ShipyardBlockPartsRamp.EnumParts.ramp_a_w_r);
			IBlockState ramp_b_l = ModBlocks.block_shipyard_parts_ramp.getDefaultState().withProperty(ShipyardBlockPartsRamp.PART_ID, ShipyardBlockPartsRamp.EnumParts.ramp_b_w_l);
			IBlockState ramp_b_r = ModBlocks.block_shipyard_parts_ramp.getDefaultState().withProperty(ShipyardBlockPartsRamp.PART_ID, ShipyardBlockPartsRamp.EnumParts.ramp_b_w_r);
			
			world.setBlockState(rampPoint, ramp_a_l);
			world.setBlockState(rampPoint.offset(EnumFacing.NORTH), ramp_a_r);
			world.setBlockState(rampPoint.offset(EnumFacing.EAST), ramp_b_l);
			world.setBlockState(rampPoint.offset(EnumFacing.EAST).offset(EnumFacing.NORTH), ramp_b_r);
			
			world.setBlockToAir(rampPoint.offset(EnumFacing.UP));
			world.setBlockToAir(rampPoint.offset(EnumFacing.UP).offset(EnumFacing.NORTH));
		}
	}
}
