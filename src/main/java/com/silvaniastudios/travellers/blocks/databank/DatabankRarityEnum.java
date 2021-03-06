package com.silvaniastudios.travellers.blocks.databank;

public enum DatabankRarityEnum {
	COMMON(25), UNCOMMON(30), RARE(35), EXOTIC(40);

	private final int knowledgeBoost;

	DatabankRarityEnum(int knowledge) {
		this.knowledgeBoost = knowledge;
	}

	public int getKnowledgeBoost() {
		return knowledgeBoost;
	}

	public static String color(DatabankRarityEnum rarity) {
		switch(rarity) {
		case COMMON:
			return "�f";
		case UNCOMMON:
			return "�2";
		case RARE:
			return "�1";
		case EXOTIC:
			return "�e";
		default:
			return "�r";
		}
	}
}
