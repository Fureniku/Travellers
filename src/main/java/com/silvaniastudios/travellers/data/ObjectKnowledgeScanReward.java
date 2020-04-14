package com.silvaniastudios.travellers.data;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import com.google.gson.Gson;
import com.silvaniastudios.travellers.Travellers;
/**
 * Gets boosts for blocks and entities while being scanned.
 * 
 * @author jamesm2w
 */
public class ObjectKnowledgeScanReward {
	
	public static final String BOOSTS_LOCATION = "/assets/travellers/knowledge_boosts.json";
	
	public ObjectEntryList list;
	
	public HashMap<String, Integer> boostsHashed = new HashMap<String, Integer>();
	
	public ObjectKnowledgeScanReward () {
		try {

			String json = Resources.toString(Travellers.class.getResource(BOOSTS_LOCATION), Charsets.UTF_8);	
			list = new Gson().fromJson(json, ObjectEntryList.class);
			
			for (ObjectEntry entry : list.list) {
				if (entry != null) {
					boostsHashed.put(entry.key, entry.knowledge);
				}
			}
		
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public int findReward (String key) {
		Integer result = boostsHashed.get(key);
		if (result != null) {
			return result;
		}
		return 0;
	}
	
	
	public static class ObjectEntryList {
		
		ArrayList<ObjectEntry> list;
		
		public ObjectEntryList (ArrayList<ObjectEntry> list) {
			this.list = list;
		}
		
	}
	
	public static class ObjectEntry {
		String key;
		int knowledge;
		
		public ObjectEntry (String key, int knowledge) {
			this.key = key;
			this.knowledge = knowledge;
		}
		
		public String toString () {
			return "[key=" + key + ", knowledge=" + String.valueOf(knowledge) + "]";
 		}
	}
}
