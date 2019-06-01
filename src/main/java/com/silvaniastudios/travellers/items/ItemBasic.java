package com.silvaniastudios.travellers.items;

import com.silvaniastudios.travellers.Travellers;

import net.minecraft.item.Item;

public class ItemBasic extends Item {
	
	protected String name;
	
	public ItemBasic(String name) {
		this.name = name;
		this.setUnlocalizedName(name);
		this.setRegistryName(name);
		this.setCreativeTab(Travellers.tabTravellers);
	}
	
	public void registerItemModel() {
		Travellers.proxy.registerItemRenderer(this, 0, name);
	}
}
