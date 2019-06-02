package com.silvaniastudios.travellers.blocks.shipyard;

import com.silvaniastudios.travellers.ModItems;
import com.silvaniastudios.travellers.Travellers;
import com.silvaniastudios.travellers.blocks.BlockBasic;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class ShipyardBlockCore extends BlockBasic {
	
	public static final PropertyEnum<EnumRotate> ROTATE_ID = PropertyEnum.create("rotate", EnumRotate.class);

	public ShipyardBlockCore(String name) {
		super(name, Material.IRON);
		this.setCreativeTab(Travellers.tabTravellers);
		this.setDefaultState(this.blockState.getBaseState().withProperty(ROTATE_ID, EnumRotate.n));
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
   	public EnumBlockRenderType getRenderType(IBlockState state) {
		return EnumBlockRenderType.MODEL;
	}
   	
	@Override
   	public BlockRenderLayer getBlockLayer() {
		return BlockRenderLayer.CUTOUT;
	}
	
	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] {ROTATE_ID});
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(ROTATE_ID, EnumRotate.byMetadata(meta));
    }
	
	@Override
    public int getMetaFromState(IBlockState state) {
        return ((EnumRotate)state.getValue(ROTATE_ID)).getMetadata();
    }
	
	@Override
	public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
        return new ItemStack(ModItems.shipyard, 1);
    }
	
	public enum EnumRotate implements IStringSerializable {
		n(0, "n"),
		e(1, "e"),
		s(2, "s"),
		w(3, "w");
		
		private static final EnumRotate[] META_LOOKUP = new EnumRotate[values().length];
		private final int meta;
		private final String name;
		
		private EnumRotate(int meta, String name) {
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
		
		public static EnumRotate byMetadata(int meta) {
	        if (meta < 0 || meta >= META_LOOKUP.length) {
	            meta = 0;
	        }
	        
	        return META_LOOKUP[meta];
	    }
		
		static {
	        for (EnumRotate oreType: values()) {
	            META_LOOKUP[oreType.getMetadata()] = oreType;
	        }
	    }
	}
}
