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
		public SchematicStats stats;
		public ArrayList<SchematicCraftingSlot> crafting;
	}
	
	public static class SchematicStatisticSlot {
		public String name;
		public float amount;
		
		public SchematicStatisticSlot (String name, float amount) {
			this.name = name;
			this.amount = amount;
		}
		
		@Override
		public String toString() {
			return String.format("{name=%s, amount=%.2f}", name, amount);
		}
	}
	
	public static class SchematicStats extends ArrayList<SchematicStatisticSlot> {

		private static final long serialVersionUID = -3413675288030167381L;

		public SchematicStats() {
			super();
		}
		
		/**
		 * Finds the given stat or null if not found
		 * @param name
		 * @return
		 */
		public SchematicStatisticSlot find (String name) {
			SchematicStatisticSlot foundSlot = null;
			
			for (SchematicStatisticSlot slot : this) {
				if (slot.name.contentEquals(name)) {
					foundSlot = slot;
				}
			}
			
			return foundSlot;
		}
		
		/** 
		 * Finds the highest stat in the set
		 * @return SchematicStatisticSlot
		 */
		public SchematicStatisticSlot maxStat () {
			SchematicStatisticSlot maxStat = new SchematicStatisticSlot("null", 0);
			for (SchematicStatisticSlot slot : this) {
				if (slot.amount > maxStat.amount) {
					maxStat = slot;
				}
			}
			
			return maxStat;
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
		
		@Override
		public String toString() {
			return String.format("[name=%s, type=%s, amount=%d]", name, type, amount);
		}
	}

}
