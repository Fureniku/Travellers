package com.silvaniastudios.travellers.items;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;

import com.google.gson.Gson;
import com.silvaniastudios.travellers.Travellers;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

public class CodexItems {

	private ResourceLocation loreJson = new ResourceLocation(Travellers.MODID, "lore_pieces.json");

	public LoreCodex codex;
	
	public HashMap<String, CodexPiece> codexMapped;

	public CodexItems() {
		codexMapped = new HashMap<String, CodexPiece>();
		try {
			InputStream file = Minecraft.getMinecraft().getResourceManager().getResource(loreJson).getInputStream();

			Reader read = new InputStreamReader(file, "UTF-8");

			codex = new Gson().fromJson(read, LoreCodex.class);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public ArrayList<ItemCodex> generateItems() {
		ArrayList<ItemCodex> items = new ArrayList<ItemCodex>();

		for (CodexStory codex : this.codex.codex) {
			for (CodexPiece piece : codex.pieces) {
				items.add(
						new ItemCodex(piece.guid, piece.pieceIndex, piece.parentName, piece.knowledge, piece.language)
				);
				
				codexMapped.put(piece.guid, piece);
			}
		}

		return items;

	}

	public class LoreCodex {
		public ArrayList<CodexStory> codex;

		public LoreCodex(ArrayList<CodexStory> codex) {
			this.codex = codex;
		}
	}

	public class CodexStory {
		public String name;
		public int totalPieces;
		public ArrayList<CodexPiece> pieces;

		public CodexStory(String name, int totalPieces, ArrayList<CodexPiece> pieces) {
			this.name = name;
			this.totalPieces = totalPieces;
			this.pieces = pieces;
		}
	}

	public class CodexPiece {
		public String parentName;
		public String guid;
		public String language;
		public int knowledge;
		public int pieceIndex;

		public CodexPiece(String parentName, String guid, String language, int knowledge, int pieceIndex) {
			this.parentName = parentName;
			this.guid = guid;
			this.language = language;
			this.knowledge = knowledge;
			this.pieceIndex = pieceIndex;
		}
	}

}
