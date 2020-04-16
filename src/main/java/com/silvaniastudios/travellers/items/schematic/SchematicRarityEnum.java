package com.silvaniastudios.travellers.items.schematic;

import net.minecraft.item.EnumRarity;
import net.minecraft.util.text.TextFormatting;

public enum SchematicRarityEnum {
	
	COMMON  ("common",     75,  45, 1),
	UNCOMMON("uncommon",  100,  60, 2),
	RARE    ("rare",      135,  80, 3),
	EXOTIC  ("exotic",    180, 110, 4),
	PRISTINE("pristine",  235, 155, 5),
	LEGENDARY("legendary",300, 200, 6);
	
	public String name;
	public int statMax5;
	public int statMax3;
	
	public int tier;
	
	SchematicRarityEnum(String name, int statMax5, int statMax3, int tier) {
		this.name = name;
		this.statMax3 = statMax3;
		this.statMax5 = statMax5;
		
		this.tier = tier;
	}
	
	public int getTotal(int statNo) {
		if (statNo == 5) {
			return this.statMax5;
		} else {
			return this.statMax3;
		}
	}
	
	public static SchematicRarityEnum fromTier(int tier) {
		switch (tier) {
		case 1:
			return COMMON;
		case 2:
			return UNCOMMON;
		case 3:
			return RARE;
		case 4:
			return EXOTIC;
		default:
			return COMMON;
		}
	}
	
	public static SchematicRarityEnum fromString(String str) {
		switch (str) {
			case "common":
				return COMMON;
			case "uncommon":
				return UNCOMMON;
			case "rare":
				return RARE;
			case "exotic":
				return EXOTIC;
			case "pristine":
				return PRISTINE;
			case "legendary":
				return LEGENDARY;
			default:
				return COMMON;
		}
	}
	
	public static TextFormatting color(SchematicRarityEnum rarity) {
		switch(rarity) {
			case COMMON:
				return TextFormatting.WHITE;
			case UNCOMMON:
				return TextFormatting.GREEN;
			case RARE:
				return TextFormatting.YELLOW;
			case EXOTIC:
				return TextFormatting.BLUE;
			case PRISTINE:
				return TextFormatting.LIGHT_PURPLE;
			case LEGENDARY:
				return TextFormatting.RED;
			default:
				return TextFormatting.RESET;
		}
	}

	public EnumRarity toMCRarity() {
		if (EnumRarity.values().length > 4) {
			switch (this) {
				default:
					return EnumRarity.values()[4];
				case COMMON:
					return EnumRarity.values()[4];
				case UNCOMMON:
					return EnumRarity.values()[5];
				case RARE:
					return EnumRarity.values()[6];
				case EXOTIC:
					return EnumRarity.values()[7];
				case PRISTINE:
					return EnumRarity.values()[8];
				case LEGENDARY:
					return EnumRarity.values()[9];
			}
		} else {
			switch (this) {
				case COMMON:
					return EnumRarity.COMMON;
				case UNCOMMON:
					return EnumRarity.UNCOMMON;
				case RARE:
					return EnumRarity.RARE;
				default:
					return EnumRarity.EPIC;
			}
		}
	}

}
