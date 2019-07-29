package com.silvaniastudios.travellers.items;

import java.util.HashMap;
import java.util.UUID;

import com.silvaniastudios.travellers.Travellers;

public class ItemSchematic extends ItemBasic {
	
	private UUID uUID;
	private int rarity;
	private String type;
	private String[] tags;
	private String title;
	private boolean unlearnable = false;
	private String iconID = "schematic_default";
	private int timeToCraft;
	private int baseHp;
	
	private HashMap<String, Integer> baseStats;
	private int[] cipherSlots;
	
	
	public ItemSchematic (String name) {
		super(name);
	}
	
	@Override
	public void registerItemModel() {
		Travellers.proxy.registerItemRenderer(this, 0, iconID);
	}
}
