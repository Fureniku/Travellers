package com.silvaniastudios.travellers.schematic;

public class SchematicProcEngine extends Schematic {

	protected SchematicRarity rarity;

	public SchematicProcEngine(SchematicRarity rarity) {
		super(Schematic.SchematicType.ENGINE, String.format("%sEngine", rarity.toString().toLowerCase()));

		switch (rarity) {
		case COMMON:
			this.setKnowledgeCost(175);
			break;
		case UNCOMMON:
			this.setKnowledgeCost(320);
			break;
		case RARE:
			this.setKnowledgeCost(400);
			break;
		case EXOTIC:
			this.setKnowledgeCost(500);
			break;
		default:
			this.setKnowledgeCost(1);
			break;
		}
	}

}
