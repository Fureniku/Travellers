package com.silvaniastudios.travellers.schematic;

import java.util.ArrayList;
import java.util.Map;

import javax.annotation.Nullable;

import com.silvaniastudios.travellers.items.ItemBasic;

public class Schematic extends ItemBasic {

	protected String schematicName;
	protected String schematicDesc;
	private Map<String, ArrayList<Integer>> stats;
	private Map<String, ArrayList<Integer>> crafting;

	private int knowledgeCost;
	private SchematicType type;

	public Schematic(SchematicType type, String schematicName, Map<String, ArrayList<Integer>> stats, Map<String, ArrayList<Integer>> crafting,
			@Nullable int knowledgeCost) {
		super(schematicName);

		this.setSchematicName(schematicName);
		this.setSchematicDesc(String.format("item.tooltip.%s", schematicName));
		this.setKnowledgeCost(knowledgeCost);
		this.setStats(stats);
		this.setCrafting(crafting);
		this.setType(type);
	}
	
	public Schematic(SchematicType type, String schematicName) {
		super(schematicName);
		
		this.setSchematicName(schematicName);
		this.setType(type);
	}
	
	public static enum SchematicType {
		SPECIAL, ENGINE, WING, CANNON, SWIVELCANNON, SHIPPART, COOKING, CLOTHING
	}

	/**
	 * @return the schematicName
	 */
	public String getSchematicName() {
		return schematicName;
	}

	/**
	 * @param schematicName the schematicName to set
	 */
	public void setSchematicName(String schematicName) {
		this.schematicName = schematicName;
	}

	/**
	 * @return the schematicDesc
	 */
	public String getSchematicDesc() {
		return schematicDesc;
	}

	/**
	 * @param schematicDesc the schematicDesc to set
	 */
	public void setSchematicDesc(String schematicDesc) {
		this.schematicDesc = schematicDesc;
	}

	/**
	 * @return the stats
	 */
	public Map<String, ArrayList<Integer>> getStats() {
		return stats;
	}

	/**
	 * @param stats the stats to set
	 */
	public void setStats(Map<String, ArrayList<Integer>> stats) {
		this.stats = stats;
	}

	/**
	 * @return the knowledgeCost
	 */
	public int getKnowledgeCost() {
		return knowledgeCost;
	}

	/**
	 * @param knowledgeCost the knowledgeCost to set
	 */
	public void setKnowledgeCost(int knowledgeCost) {
		this.knowledgeCost = knowledgeCost;
	}

	/**
	 * @return the crafting
	 */
	public Map<String, ArrayList<Integer>> getCrafting() {
		return crafting;
	}

	/**
	 * @param crafting the crafting to set
	 */
	public void setCrafting(Map<String, ArrayList<Integer>> crafting) {
		this.crafting = crafting;
	}

	/**
	 * @return the type
	 */
	public SchematicType getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(SchematicType type) {
		this.type = type;
	}

}
