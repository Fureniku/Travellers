package com.silvaniastudios.travellers.schematic;

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
		default:
			return COMMON;
		}
	}
	
	public static String color(SchematicRarityEnum rarity) {
		switch(rarity) {
		case COMMON:
			return "§f";
		case UNCOMMON:
			return "§2";
		case RARE:
			return "§1";
		case EXOTIC:
			return "§e";
		default:
			return "§r";
		}
	}

}
