package com.silvaniastudios.travellers.items.schematic;

import javax.annotation.Nullable;

import com.silvaniastudios.travellers.capability.schematicData.ISchematicData;
import com.silvaniastudios.travellers.data.SchematicFixedData;
import com.silvaniastudios.travellers.data.SchematicFixedData.SchematicCategories;
import com.silvaniastudios.travellers.data.SchematicFixedData.SchematicCrafting;
import com.silvaniastudios.travellers.items.schematic.data.IProceduralData;

public enum SchematicTypeEnum implements IProceduralData {
	
	FIXED("fixed", 0), 
	ENGINE("engine", 5, SchematicFixedData.engineData), 
	WING("wing", 3, SchematicFixedData.wingData), 
	CANNON("cannon", 5, SchematicFixedData.cannonData), 
	SWIVELCANNON("swivel", 5, SchematicFixedData.swivelData);
	
	public String name;
	public int statNo;
	
	public IProceduralData dataManager;

	SchematicTypeEnum(String name, int statNo) {
		this.name = name;
		this.statNo = statNo;
	}
	
	SchematicTypeEnum(String name, int statNo, IProceduralData dataManager) {
		this.name = name;
		this.statNo = statNo;
		this.dataManager = dataManager;
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

	@Override
	public String toString() {
		return this.name;
	}

	@Override
	public String[] getStatNames() {
		if (this.dataManager != null) {
			return this.dataManager.getStatNames();
		}
		return null;
	}
	
	@Override
	public String[] getSlotNames () {
		if (this.dataManager != null) {
			return this.dataManager.getSlotNames();
		}
		return null;
	}
	
	@Override
	@Nullable
	public String[] getNameComponents (ISchematicData schematic) {
		if (this.dataManager != null) {
			return this.dataManager.getNameComponents(schematic);
		} 
		return null;
	}

	@Override
	@Nullable
	public String getNameFormat() {
		if (this.dataManager != null) {
			return this.dataManager.getNameFormat();
		} 
		return null;
	}
	
	public String getName (ISchematicData schematic) {
		return String.format(getNameFormat(), (Object[])getNameComponents(schematic));
	}

	@Override
	public SchematicCrafting getCrafting(ISchematicData schematic) {
		if (this.dataManager != null) {
			return this.dataManager.getCrafting(schematic);
		}
		return null;
	}
	
	/* (non-Javadoc)
	 * @see com.silvaniastudios.travellers.items.schematic.data.IProceduralData#getCategories(com.silvaniastudios.travellers.capability.schematicData.ISchematicData)
	 */
	@Override
	public SchematicCategories getCategories(ISchematicData schematic) {
		if (this.dataManager != null) {
			return this.dataManager.getCategories(schematic);
		}
		return null;
	}
}
