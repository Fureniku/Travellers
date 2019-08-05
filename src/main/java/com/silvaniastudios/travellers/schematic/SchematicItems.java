package com.silvaniastudios.travellers.schematic;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;

import com.google.gson.Gson;
import com.silvaniastudios.travellers.ModItems;
import com.silvaniastudios.travellers.Travellers;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

public class SchematicItems {

	private ResourceLocation schematicJson = new ResourceLocation(Travellers.MODID, "schematics.json");

	public SchematicList list;

	public HashMap<String, Schematic> listMapped;

	public SchematicItems() {
		listMapped = new HashMap<String, Schematic>();
		try {
			InputStream file = Minecraft.getMinecraft().getResourceManager().getResource(schematicJson)
					.getInputStream();

			Reader read = new InputStreamReader(file, "UTF-8");

			list = new Gson().fromJson(read, SchematicList.class);
			
			generateItems();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void generateItems() {

		for (Schematic schematic : list.schematics) {
			ModItems.schematics.add(new ItemSchematic(schematic.name, SchematicRarityEnum.fromString(schematic.rarity),
					schematic.unlearnable, schematic.baseHp));
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
