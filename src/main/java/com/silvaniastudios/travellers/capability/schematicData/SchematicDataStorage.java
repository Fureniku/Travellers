package com.silvaniastudios.travellers.capability.schematicData;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;

/**
 * 
 * @author jamesm2w
 */
public class SchematicDataStorage implements IStorage<ISchematicData> {

	@Override
	public NBTBase writeNBT(Capability<ISchematicData> capability, ISchematicData instance, EnumFacing side) {
		return instance.toNBT();
	}

	@Override
	public void readNBT(Capability<ISchematicData> capability, ISchematicData instance, EnumFacing side, NBTBase nbt) {
		instance.fromNBT((NBTTagCompound) nbt);
	}

}
