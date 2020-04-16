package com.silvaniastudios.travellers.data;

import java.io.IOException;
import java.util.ArrayList;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import com.google.gson.Gson;
import com.silvaniastudios.travellers.Travellers;
import com.silvaniastudios.travellers.items.schematic.ItemSchematic;

public class SchematicFixedData {
	
	public static final String SCHEMATIC_LOCATION = "/assets/travellers/schematics.json";
	
	public ArrayList<Schematic> schematicList;
	
	public SchematicFixedData () {
		try {

			String json = Resources.toString(Travellers.class.getResource(SCHEMATIC_LOCATION), Charsets.UTF_8);	
			schematicList = new Gson().fromJson(json, SchematicDataList.class).schematics;

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public ArrayList<ItemSchematic> generateItems () {
		ArrayList<ItemSchematic> list = new ArrayList<ItemSchematic>();
		for (Schematic schem : schematicList) {
			list.add(new ItemSchematic(schem.name, schem.rarity, schem.stats, schem.crafting));
		}
		return list;
	}

	public static class SchematicDataList {
		public ArrayList<Schematic> schematics;
	}
	
	public static class Schematic {
		public String name;
		public String rarity;
		public ArrayList<SchematicStatisticSlot> stats;
		public ArrayList<SchematicCraftingSlot> crafting;
	}
	
	public static class SchematicStatisticSlot {
		public String name;
		public float amount;
		
		public SchematicStatisticSlot (String name, float amount) {
			this.name = name;
			this.amount = amount;
		}
		
		public static SchematicStatisticSlot findStat (ArrayList<SchematicStatisticSlot> stats, String stat) {
			for (SchematicStatisticSlot slot : stats) {
				if (slot.name.contentEquals(stat)) {
					return slot;
				}
			}
			
			return null;
		}
	}
	
	public static class SchematicCraftingSlot {
		public String name;
		public String type;
		public int amount;
		
		public SchematicCraftingSlot (String name, String type, int amount) {
			this.name = name;
			this.type = type;
			this.amount = amount;
		}
		
		public SchematicCraftingSlot (String name) {
			this.name = name;
		}
	}

}
