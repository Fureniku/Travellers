package com.silvaniastudios.travellers.capability.tree;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;

public class KnTreeStorage implements IStorage<IKnTree>{

	@Override
	public NBTBase writeNBT(Capability<IKnTree> capability, IKnTree instance, EnumFacing side) {
		NBTTagCompound base = new NBTTagCompound();
		for (String key : instance.getKnTree().keySet()) {
			base.setTag(key, new NBTTagInt(instance.getKnTree().get(key).getProgress()));
		}
		
		return base;
	}

	@Override
	public void readNBT(Capability<IKnTree> capability, IKnTree instance, EnumFacing side, NBTBase nbt) {

		instance.buildMap();
		
		for (String key : ((NBTTagCompound) nbt).getKeySet()) {
			instance.updateNode(key, ((NBTTagCompound) nbt).getInteger(key));
		}
		
	}

}
