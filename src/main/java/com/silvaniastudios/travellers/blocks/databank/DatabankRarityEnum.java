package com.silvaniastudios.travellers.blocks.databank;

import net.minecraft.util.text.TextFormatting;

/**
 * Common - 25 kn
 * Uncommon - 30 kn
 * Rare - 35 kn
 * Exotic - 40 kn
 * 
 * @author jamesm2w
 */
public enum DatabankRarityEnum {
	COMMON(25), UNCOMMON(30), RARE(35), EXOTIC(40);

	private final int knowledgeBoost;

	DatabankRarityEnum(int knowledge) {
		this.knowledgeBoost = knowledge;
	}

	public int getKnowledgeBoost() {
		return knowledgeBoost;
	}

	public static TextFormatting color(DatabankRarityEnum rarity) {
		switch(rarity) {
		case COMMON:
			return TextFormatting.WHITE;
		case UNCOMMON:
			return TextFormatting.GREEN;
		case RARE:
			return TextFormatting.DARK_AQUA;
		case EXOTIC:
			return TextFormatting.YELLOW;
		default:
			return TextFormatting.RESET;
		}
	}
}
