package com.silvaniastudios.travellers.items.schematic;

public enum SchematicTypeEnum {
	FIXED("fixed", 0), ENGINE("engine", 5), WING("wing", 3), CANNON("cannon", 5), SWIVELCANNON("swivel", 5);

	public String name;
	public int statNo;

	SchematicTypeEnum(String name, int statNo) {
		this.name = name;
		this.statNo = statNo;
	}

	public static SchematicTypeEnum fromString(String name) {
		switch (name) {
		case "engine":
			return ENGINE;
		case "wing":
			return WING;
		case "cannon":
			return CANNON;
		case "swivel":
			return SWIVELCANNON;
		default:
			return FIXED;
		}
	}

	//TODO: not static?
	public static String[] getStatNames(SchematicTypeEnum type) {
		switch (type) {
		case FIXED:
			return EngineProceduralData.FIXED_STAT_NAMES;
		case ENGINE:
			return EngineProceduralData.ENGINE_STAT_NAMES;
		case WING:
			return EngineProceduralData.WING_STAT_NAMES;
		case CANNON:
			return EngineProceduralData.CANNON_STAT_NAMES;
		case SWIVELCANNON:
			return EngineProceduralData.SWIVELCANNON_STAT_NAMES;
		default:
			return EngineProceduralData.FIXED_STAT_NAMES;
		}
	}
}
