package com.silvaniastudios.travellers.capability.schematicData;

import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

public class SchematicDataProvider implements ICapabilitySerializable<NBTBase> {
	
	@CapabilityInject(ISchematicData.class)
	public static final Capability<ISchematicData> SCHEMATIC_DATA = null;

	private ISchematicData instance = SCHEMATIC_DATA.getDefaultInstance();
	
	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {

		return capability == SCHEMATIC_DATA;
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {

		return (capability == SCHEMATIC_DATA) ? SCHEMATIC_DATA.<T>cast(this.instance) : null;
	}

	@Override
	public NBTBase serializeNBT() {

		return SCHEMATIC_DATA.getStorage().writeNBT(SCHEMATIC_DATA, instance, null);
	}

	@Override
	public void deserializeNBT(NBTBase nbt) {
		
		SCHEMATIC_DATA.getStorage().readNBT(SCHEMATIC_DATA, instance, null, nbt);
	}

}
