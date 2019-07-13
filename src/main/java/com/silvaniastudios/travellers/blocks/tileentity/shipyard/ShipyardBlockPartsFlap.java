package com.silvaniastudios.travellers.blocks.tileentity.shipyard;

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

public class ShipyardBlockPartsFlap extends BlockBasic implements IMetaBlockName {
	
	public static final PropertyEnum<EnumParts> PART_ID = PropertyEnum.create("part", EnumParts.class);

	public ShipyardBlockPartsFlap(String name) {
		super(name, Material.IRON);
		this.setDefaultState(this.blockState.getBaseState().withProperty(PART_ID, EnumParts.flap_n_l));
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
		flap_n_l(0, "flap_n_l"),
		flap_e_l(1, "flap_e_l"),
		flap_s_l(2, "flap_s_l"),
		flap_w_l(3, "flap_w_l"),
		flap_n_r(4, "flap_n_r"),
		flap_e_r(5, "flap_e_r"),
		flap_s_r(6, "flap_s_r"),
		flap_w_r(7, "flap_w_r"),
		flap_ne_l(8, "flap_ne_l"),
		flap_se_l(9, "flap_se_l"),
		flap_sw_l(10, "flap_sw_l"),
		flap_nw_l(11, "flap_nw_l"),
		flap_ne_r(12, "flap_ne_r"),
		flap_se_r(13, "flap_se_r"),
		flap_sw_r(14, "flap_sw_r"),
		flap_nw_r(15, "flap_nw_r");
		
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