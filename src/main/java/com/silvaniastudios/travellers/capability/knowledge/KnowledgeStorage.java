package com.silvaniastudios.travellers.capability.knowledge;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;

/*
 * This thing deals with synchronising the knowledge capability across client-server as an NBT int
 */

public class KnowledgeStorage implements IStorage<IKnowledge> {

	@Override
	public NBTBase writeNBT(Capability<IKnowledge> capability, IKnowledge instance, EnumFacing side) {
		// return the knowledge value as an NBTTagInt
		return new NBTTagInt(instance.getKnowledge());
	}

	@Override
	public void readNBT(Capability<IKnowledge> capability, IKnowledge instance, EnumFacing side, NBTBase nbt) {
		// set the knowledge value from NBT
		instance.setKnowledge(((NBTTagInt) nbt).getInt());
	}

}
