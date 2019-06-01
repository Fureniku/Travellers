package com.silvaniastudios.travellers.blocks.shipyard;

import com.silvaniastudios.travellers.blocks.BlockBasic;
import com.silvaniastudios.travellers.blocks.IMetaBlockName;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class ShipyardBlockParts extends BlockBasic implements IMetaBlockName {
	
	public static final PropertyEnum<EnumParts> PART_ID = PropertyEnum.create("part", EnumParts.class);

	public ShipyardBlockParts(String name) {
		super(name, Material.IRON);
		this.setDefaultState(this.blockState.getBaseState().withProperty(PART_ID, EnumParts.ramp_n));
	}
	
	@Override
	public String getSpecialName(ItemStack stack) {
		int meta = stack.getItemDamage();
		
		if (meta == 0) { return "0"; }
		if (meta == 1) { return "1"; }
		if (meta == 2) { return "2"; }
		if (meta == 3) { return "3"; }
		if (meta == 4) { return "4"; }
		if (meta == 5) { return "5"; }
		if (meta == 6) { return "6"; }
		if (meta == 7) { return "7"; }
		if (meta == 8) { return "8"; }
		if (meta == 9) { return "9"; }
		if (meta == 10) { return "10"; }
		if (meta == 11) { return "11"; }
		if (meta == 12) { return "12"; }
		if (meta == 13) { return "13"; }
		if (meta == 14) { return "14"; }
		if (meta == 15) { return "15"; }
		return null;
	}
	
	@Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }
	
	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] {PART_ID});
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(PART_ID, EnumParts.byMetadata(meta));
    }
	
	@Override
    public int getMetaFromState(IBlockState state) {
        return ((EnumParts)state.getValue(PART_ID)).getMetadata();
    }
	
	@Override
	public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
        return new ItemStack(state.getBlock(), 1, this.getMetaFromState(state));
    }
	
	public void getSubBlocks(CreativeTabs itemIn, NonNullList<ItemStack> items) {
		items.add(new ItemStack(this, 1, 0));
		items.add(new ItemStack(this, 1, 1));
		items.add(new ItemStack(this, 1, 2));
		items.add(new ItemStack(this, 1, 3));
		items.add(new ItemStack(this, 1, 4));
		items.add(new ItemStack(this, 1, 5));
		items.add(new ItemStack(this, 1, 6));
		items.add(new ItemStack(this, 1, 7));
	}
	
	public enum EnumParts implements IStringSerializable {
		side_n (0, "side_n"),
		side_ne(1, "side_ne"),
		side_e (2, "side_e"),
		side_se(3, "side_se"),
		side_s (4, "side_s"),
		side_sw(5, "side_sw"),
		side_w (6, "side_w"),
		side_nw(7, "side_nw"),
		ramp_n (8, "ramp_n"),
		ramp_ne(9, "ramp_ne"),
		ramp_e (10, "ramp_e"),
		ramp_se(11, "ramp_se"),
		ramp_s (12, "ramp_s"),
		ramp_sw(13, "ramp_sw"),
		ramp_w (14, "ramp_w"),
		ramp_nw(15, "ramp_nw");
		
		private static final EnumParts[] META_LOOKUP = new EnumParts[values().length];
		private final int meta;
		private final String name;
		
		private EnumParts(int meta, String name) {
			this.meta = meta;
			this.name = name;
		}

		@Override
		public String getName() {
			return this.name;
		}
		
		@Override
		public String toString() {
			return getName();
		}
		
		public int getMetadata() {
	        return this.meta;
	    }
		
		public static EnumParts byMetadata(int meta) {
	        if (meta < 0 || meta >= META_LOOKUP.length) {
	            meta = 0;
	        }
	        
	        return META_LOOKUP[meta];
	    }
		
		static {
	        for (EnumParts oreType: values()) {
	            META_LOOKUP[oreType.getMetadata()] = oreType;
	        }
	    }
	}
}