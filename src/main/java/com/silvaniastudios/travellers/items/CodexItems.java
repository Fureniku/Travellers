package com.silvaniastudios.travellers.items;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import com.google.gson.Gson;
import com.silvaniastudios.travellers.Travellers;

/**
 * Helper to load and generate the lore piece items
 * 
 * @author jamesm2w
 */
public class CodexItems {

	//private ResourceLocation loreJson = new ResourceLocation(Travellers.MODID, "lore_pieces.json");
	
	public static final String LORE_PIECES = "/assets/travellers/lore_pieces.json";
	
	public LoreCodex codex;
	
	public HashMap<String, CodexPiece> codexMapped;

	public CodexItems() {
		codexMapped = new HashMap<String, CodexPiece>();
		try {

			String json = Resources.toString(Travellers.class.getResource(LORE_PIECES), Charsets.UTF_8);

			codex = new Gson().fromJson(json, LoreCodex.class);

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
