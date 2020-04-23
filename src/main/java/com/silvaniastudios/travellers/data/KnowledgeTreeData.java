package com.silvaniastudios.travellers.data;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import com.google.gson.Gson;
import com.silvaniastudios.travellers.Travellers;

public class KnowledgeTreeData {
	
	public static final String KNOWLEDGE_TREE_DATA = "/assets/travellers/knowledge_tree.json";
	
	public ArrayList<KnowledgeNode> nodes;
	public HashMap<String, KnowledgeNode> nodesHashed = new HashMap<String, KnowledgeNode>();
	
	public KnowledgeNode root;
	
	public KnowledgeTreeData () {
		try {

			String json = Resources.toString(Travellers.class.getResource(KNOWLEDGE_TREE_DATA), Charsets.UTF_8);
			nodes = new Gson().fromJson(json, KnowledgeTree.class).tree;
			//System.out.println(nodes);
			for (KnowledgeNode node : nodes) {
				//System.out.println(node);
				nodesHashed.put(node.name, node);
			}
			
			root = nodesHashed.get("reviver_interface");

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public KnowledgeNode getNode (String name) {
		return nodesHashed.get(name);
	}
	
	public static class KnowledgeTree {
		public ArrayList<KnowledgeNode> tree;
	}
	
	public static class KnowledgeNode {
		public String name;
		public int[] position;
		public int[] texture;
		public int maxUses;
		public int infiniteRoll;
		public int cost;
		public ArrayList<KnowledgeNodeUnlock> unlocks;
		public KnowledgeNodeRewards rewards;
		
		@Override
		public String toString() {
			return String.format("[name=%s, pos=[%d, %d], tex=[%d, %d], max=%d, inf=%d, cost=%d, unlocks=%s]", 
					name, position[0], position[1], texture[0], texture[1], maxUses, infiniteRoll, cost, unlocks.toString());
		}
	}
	
	public static class KnowledgeNodeUnlock {
		public String name;
		public int uses;
		
		@Override
		public String toString() {
			return String.format("[name=%s, uses=%d]", name, uses);
		}
	}
	
	public static class KnowledgeNodeRewards {
		public String type;
		public ArrayList<String> schematics;
	}

}
