package com.silvaniastudios.travellers.data;

import java.io.IOException;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import com.silvaniastudios.travellers.Travellers;

/**
 * Loads the lore data
 * 
 * @author jamesm2w
 */
public class LoreCodexData {
	public static final String CODEX_DATA = "/assets/travellers/codex_pieces.json";
	
	public LoreCodexData () {
		try {

			String json = Resources.toString(Travellers.class.getResource(CODEX_DATA), Charsets.UTF_8);	
			//list = new Gson().fromJson(json, ObjectEntryList.class);
		
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
