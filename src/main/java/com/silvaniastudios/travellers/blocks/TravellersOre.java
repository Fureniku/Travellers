package com.silvaniastudios.travellers.blocks;

import java.util.Random;

import com.silvaniastudios.travellers.ModItems;
import com.silvaniastudios.travellers.Travellers;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class TravellersOre extends BlockBasic implements IMetaBlockName {
	
	public static final PropertyEnum<EnumMeta> META_ID = PropertyEnum.create("ores", EnumMeta.class);
	
	Item dropItem;

	public TravellersOre(String name, Item dropItem) {
		super(name, Material.ROCK);
		this.setCreativeTab(Travellers.tabTravellers);
		this.dropItem = dropItem;
		this.setHarvestLevel("pickaxe", 0, getStateFromMeta(0));
		this.setHardness(1.2F);
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
		return null;
	}
	
	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] {META_ID});
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(META_ID, EnumMeta.byMetadata(meta));
    }
	
	@Override
    public int getMetaFromState(IBlockState state) {
        return ((EnumMeta)state.getValue(META_ID)).getMetadata();
    }
	
	@Override
	public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
		Random rand = world instanceof World ? ((World)world).rand : RANDOM;
		int meta = state.getValue(META_ID).getMetadata();
		int chance = rand.nextInt(8);
		
		drops.add(new ItemStack(dropItem, 1, meta));
		
		//One WA ore = 2-6 metal scraps + 50% shard chance
		//One MC ore = 1 metal scrap, so averaged to 1 in 8 chance for shard.
		if (chance <= 1) {
			drops.add(new ItemStack(ModItems.atlas_shard, 1));
		}
	}
	
	@Override
	public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
        return new ItemStack(state.getBlock(), 1, this.getMetaFromState(state));
    }
	
	public enum EnumMeta implements IStringSerializable {
		id0(0, "0"),
		id1(1, "1"),
		id2(2, "2"),
		id3(3, "3"),
		id4(4, "4"),
		id5(5, "5"),
		id6(6, "6"),
		id7(7, "7"),
		id8(8, "8"),
		id9(9, "9"),
		id10(10, "10");
		
		private static final EnumMeta[] META_LOOKUP = new EnumMeta[values().length];
		private final int meta;
		private final String name;
		
		private EnumMeta(int meta, String name) {
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
		
		public static EnumMeta byMetadata(int meta) {
	        if (meta < 0 || meta >= META_LOOKUP.length) {
	            meta = 0;
	        }
	        
	        return META_LOOKUP[meta];
	    }
		
		static {
	        for (EnumMeta oreType: values()) {
	            META_LOOKUP[oreType.getMetadata()] = oreType;
	        }
	    }
	}
}
