package com.silvaniastudios.travellers.data;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import com.google.gson.Gson;
import com.silvaniastudios.travellers.Travellers;
import com.silvaniastudios.travellers.items.schematic.ItemSchematic;
import com.silvaniastudios.travellers.items.schematic.data.CannonData;
import com.silvaniastudios.travellers.items.schematic.data.EngineData;
import com.silvaniastudios.travellers.items.schematic.data.SwivelData;
import com.silvaniastudios.travellers.items.schematic.data.WingData;

public class SchematicFixedData {
	
	public static final String SCHEMATIC_LOCATION = "/assets/travellers/schematics.json";
	
	public static final EngineData engineData = new EngineData();
	public static final WingData wingData = new WingData();
	public static final CannonData cannonData = new CannonData();
	public static final SwivelData swivelData = new SwivelData();
	
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
			list.add(new ItemSchematic(schem.name, schem.rarity, schem.stats, schem.crafting, schem.categories));
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
		public SchematicCrafting crafting;
		public SchematicCategories categories;
	}
	
	public static class SchematicStatisticSlot implements Comparable<SchematicStatisticSlot> {
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

		/** (non-Javadoc)
		 * @see java.lang.Comparable#compareTo(java.lang.Object)
		 */
		@Override
		public int compareTo(SchematicStatisticSlot o) {
			if (o.amount < this.amount) {
				return -1;
			} else if (o.amount > this.amount) {
				return 1;
			} else {
				return 0;
			}
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
			Collections.sort((List<SchematicStatisticSlot>) this);
			return this.get(0);
		}
		
		public SchematicStatisticSlot secondaryStat () {
			Collections.sort((List<SchematicStatisticSlot>) this);
			return this.get(1);
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
	
	public static class SchematicCrafting extends ArrayList<SchematicCraftingSlot> {

		private static final long serialVersionUID = 8710708185018416424L;
		
		public SchematicCrafting () {
			super();
		}
		
		public SchematicCraftingSlot find (String slotName) {
			SchematicCraftingSlot foundSlot = null;
			
			for (SchematicCraftingSlot slot : this) {
				if (slot.name.contentEquals(slotName)) {
					foundSlot = slot;
				}
			}
			
			return foundSlot;
		}
		
	}
	
	public static class SchematicCategories extends ArrayList<String> {

		private static final long serialVersionUID = -4109164836752740552L;
		
		public SchematicCategories () {
			super();
		}
		
		public static enum EnumMethod {MULTITOOL, ASSEMBLER};
		
		public EnumMethod getMethod () {
			if (this.contains("multitool")) {
				return EnumMethod.MULTITOOL;
			} else if (this.contains("assembler")) {
				return EnumMethod.ASSEMBLER;
			} else {
				return null;
			}
		}
		
	}

}
