package com.silvaniastudios.travellers.items.tools;

import java.util.Set;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import com.silvaniastudios.travellers.ModItems;
import com.silvaniastudios.travellers.Travellers;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;

public class ItemSalvager extends ItemTool implements ITravellerTool{
	
	String name;
	
	private static final Set<Block> effectiveBlocks = Sets.newHashSet(Blocks.PLANKS, Blocks.STONE, Blocks.COBBLESTONE);

	public ItemSalvager(String name) {
		super(ModItems.ANCIENT_SALVAGER, effectiveBlocks);
		
		this.name = name;
		this.setUnlocalizedName(name);
		this.setRegistryName(name);
		this.setCreativeTab(Travellers.tabTravellers);
		
		this.setMaxStackSize(1);
	}
	
	@Override
	public Set<String> getToolClasses(ItemStack stack) {
		return ImmutableSet.of("pickaxe", "spade", "axe", "sword", "hoe");
	}
	
	@Override
	public boolean canHarvestBlock(IBlockState blockIn) {
		return true;
	}
	
	@Override
	public float getDestroySpeed(ItemStack stack, IBlockState state) {
		return this.efficiency;
	}
	
	public void registerItemModel() {
		Travellers.proxy.registerItemRenderer(this, 0, name);
	}
}
