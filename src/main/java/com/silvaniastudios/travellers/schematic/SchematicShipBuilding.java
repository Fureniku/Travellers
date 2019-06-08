package com.silvaniastudios.travellers.schematic;

public class SchematicShipBuilding extends Schematic {
	
	public SchematicShipBuilding() {
		super(Schematic.SchematicType.SPECIAL, "shipBuilding");
		
		setKnowledgeCost(250);
	}
}
