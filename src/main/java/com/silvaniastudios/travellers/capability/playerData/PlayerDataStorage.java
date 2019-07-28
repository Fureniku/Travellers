package com.silvaniastudios.travellers.capability.playerData;

import java.util.ArrayList;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;

public class PlayerDataStorage implements IStorage<IPlayerData> {

	@Override
	public NBTBase writeNBT(Capability<IPlayerData> capability, IPlayerData instance, EnumFacing side) {
		
		NBTTagCompound nbtTag = new NBTTagCompound();
		nbtTag.setInteger("knowledge", instance.getKnowledgeBalance());
		nbtTag.setBoolean("isMale", instance.isMale());
		nbtTag.setString("shipyardVisitorCode", instance.getShipyardVisitorCode());
		
		NBTTagCompound knowledgeTreeUses = new NBTTagCompound();
		for (String key : instance.getKnowledgeNodeUses().keySet()) {
			knowledgeTreeUses.setInteger(key, instance.getKnowledgeNodeUses().get(key));
		}
		
		int index = 0;
		NBTTagCompound knownLorePieces = new NBTTagCompound();
		for (String key : instance.getKnownLorePieces()) {
			knownLorePieces.setInteger(key, index++);
		}
		
		nbtTag.setTag("knowledgeTreeUses", knowledgeTreeUses);
		nbtTag.setTag("knownLorePieces", knownLorePieces);
		//TODO: ship schematics / schematic list in general
		return nbtTag;
	}

	@Override
	public void readNBT(Capability<IPlayerData> capability, IPlayerData instance, EnumFacing side, NBTBase nbt) {
		NBTTagCompound nbtTag = (NBTTagCompound) nbt;
		
		instance.setKnowledgeBalance(nbtTag.getInteger("knowledge"));
		instance.setMale(nbtTag.getBoolean("isMale"));
		instance.setShipyardVisitorCode(nbtTag.getString("shipyardVisitorCode"));
		
		for (String key : ((NBTTagCompound)nbtTag.getCompoundTag("knowledgeTreeUses")).getKeySet()) {
			instance.setKnowledgeNodeUses(key, ((NBTTagCompound)nbtTag.getTag("knowledgeTreeUses")).getInteger(key));
		}
		
		ArrayList<String> knownLorePieces = new ArrayList<String>();
		for (String key : nbtTag.getCompoundTag("knownLorePieces").getKeySet()) {
			knownLorePieces.add(key);
		}
		
		instance.setKnownLorePieces(knownLorePieces);
		
		//TODO: ship schematics / schematic list in general
	}

}
