package com.silvaniastudios.travellers.schematic;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import com.google.gson.Gson;
import com.silvaniastudios.travellers.ModItems;
import com.silvaniastudios.travellers.Travellers;

public class SchematicItems {

	//private ResourceLocation schematicJson = new ResourceLocation(Travellers.MODID, "schematics.json");

	public SchematicList list;

	public HashMap<String, Schematic> listMapped;

	public SchematicItems() {
		listMapped = new HashMap<String, Schematic>();
		try {
			String json = Resources.toString(Travellers.class.getResource("/assets/travellers/schematics.json"), Charsets.UTF_8);

			list = new Gson().fromJson(json, SchematicList.class);
			
			generateItems();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void generateItems() {

		for (Schematic schematic : list.schematics) {
			ModItems.schematics.add(new ItemSchematic(schematic.name, SchematicRarityEnum.fromString(schematic.rarity),
					schematic.unlearnable, schematic.baseHp, true));
		}
	}

	public class SchematicList {
		public ArrayList<Schematic> schematics;

		public SchematicList(ArrayList<Schematic> schematics) {
			this.schematics = schematics;
		}
	}

	public class Schematic {

		public String name;
		public String rarity;
		public boolean unlearnable;
		public float baseHp;

		public Schematic(String name, String rarity, boolean unlearnable, float baseHp) {
			this.name = name;
			this.rarity = rarity;
			this.unlearnable = unlearnable;
			this.baseHp = baseHp;
		}

	}

}
