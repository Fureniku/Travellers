package com.silvaniastudios.travellers.capability.tree;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;

public class SchematicTreeStorage implements IStorage<ISchematicTree> {

	/**
	 * Takes a capability and returns it as an NBTTag of the hashedNode value and the times researched.
	 * When loaded from NBT the times researched is updated on the tree
	 * 
	 * @param capability the capabaility
	 * @param instance the instance of the capability
	 * @param side null
	 * 
	 * @return NBTTagCompound of key: value pairs, hashedKey and timesResearched
	 */
	@Override
	public NBTBase writeNBT(Capability<ISchematicTree> capability, ISchematicTree instance,
			EnumFacing side) {
		NBTTagCompound nbt = new NBTTagCompound();
		
		for (String key : instance.getHashedTree().keySet()) {
			int timesResearched = instance.getHashedTree().get(key).getTimesResearched();
			
			nbt.setInteger(key, timesResearched);
		}
		
		return nbt;
	}

	/**
	 * Takes an NBTTagCompound and updates the instance's tree with the timesresearched.
	 * 
	 * TODO: Add checks if the next node level needs to be unlocked
	 */
	@Override
	public void readNBT(Capability<ISchematicTree> capability, ISchematicTree instance,
			EnumFacing side, NBTBase nbt) {
		NBTTagCompound castedNbt = (NBTTagCompound) nbt;
		for (String key : castedNbt.getKeySet()) {
			if (instance.getHashedTree().containsKey(key)) {
				instance.getHashedTree().get(key).setTimesResearched(castedNbt.getInteger(key));
			}
		}
		
	}
}
