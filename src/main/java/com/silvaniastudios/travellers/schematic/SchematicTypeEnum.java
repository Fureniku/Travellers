package com.silvaniastudios.travellers.schematic;

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
			return ENGINE;
		}
	}

	public static String[] getStatNames(SchematicTypeEnum type) {
		switch (type) {
		case FIXED:
			return ProcSchematicConfig.FIXED_STAT_NAMES;
		case ENGINE:
			return ProcSchematicConfig.ENGINE_STAT_NAMES;
		case WING:
			return ProcSchematicConfig.WING_STAT_NAMES;
		case CANNON:
			return ProcSchematicConfig.CANNON_STAT_NAMES;
		case SWIVELCANNON:
			return ProcSchematicConfig.SWIVELCANNON_STAT_NAMES;
		default:
			return ProcSchematicConfig.FIXED_STAT_NAMES;
		}
	}
}
