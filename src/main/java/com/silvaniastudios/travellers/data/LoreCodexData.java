package com.silvaniastudios.travellers.data;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import com.google.gson.Gson;
import com.silvaniastudios.travellers.Travellers;
import com.silvaniastudios.travellers.items.ItemCodex;

/**
 * Loads the lore data
 * 
 * @author jamesm2w
 */
public class LoreCodexData {
	public static final String CODEX_DATA = "/assets/travellers/lore_codex.json";

	public ArrayList<CodexStory> loreCodex;

	public ArrayList<ItemCodex> items;

	public HashMap<String, CodexPiece> piecesByGuid = new HashMap<String, CodexPiece>();

	public LoreCodexData() {
		try {

			String json = Resources.toString(Travellers.class.getResource(CODEX_DATA), Charsets.UTF_8);
			loreCodex = new Gson().fromJson(json, Codex.class).codex;

			items = new ArrayList<ItemCodex>();

			for (CodexStory story : loreCodex) {
				for (CodexPiece piece : story.pieces) {
					piecesByGuid.put(piece.guid, piece);
					
					items.add(new ItemCodex(piece.guid, piece.pieceIndex, piece.parentName, piece.knowledge,
							piece.language, piece.text));
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public ArrayList<ItemCodex> itemList() {
		return items;
	}

	public String getText(String guid) {
		CodexPiece piece = piecesByGuid.get(guid);

		if (piece != null) {
			return piece.text;
		} else {
			return null;
		}
	}

	public static class CodexPiece {
		public String guid;
		public String language;
		public String parentName;
		public String text;
		public int pieceIndex;
		public int knowledge;
	}

	public static class CodexStory {
		public String name;
		public int totalPieces;
		public ArrayList<CodexPiece> pieces;
	}

	public static class Codex {
		public ArrayList<CodexStory> codex;
	}
}
