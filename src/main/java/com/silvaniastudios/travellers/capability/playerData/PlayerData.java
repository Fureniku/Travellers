package com.silvaniastudios.travellers.capability.playerData;

import java.util.ArrayList;
import java.util.HashMap;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;

public class PlayerData implements IPlayerData {
	
	private int knowledgeBalance;
	private String shipyardVisitorCode;
	private boolean isMale;
	private HashMap<String, Integer> knowledgeNodeUses;
	private ArrayList<String> knownLorePieces;
	
	//TODO: private <T> shipDesignSchematics;
	
	public PlayerData () {
		this.knowledgeBalance = 0;
		this.shipyardVisitorCode = "1234";
		this.isMale = true;
		this.knowledgeNodeUses = new HashMap<String, Integer>();
		this.knownLorePieces = new ArrayList<String>();
	}

	@Override
	public void setKnowledgeBalance(int knowledgeBalance) {
		this.knowledgeBalance = knowledgeBalance;
	}

	@Override
	public int incrementKnowledgeBalance(int increase) {
		this.knowledgeBalance += increase;
		return this.knowledgeBalance;
	}

	@Override
	public int getKnowledgeBalance() {
		return this.knowledgeBalance;
	}

	@Override
	public void setShipyardVisitorCode(String code) {
		this.shipyardVisitorCode = code;
	}

	@Override
	public String getShipyardVisitorCode() {
		return this.shipyardVisitorCode;
	}

	@Override
	public String generateNewShipyardVisitorCode() {
		this.shipyardVisitorCode = "9876"; //TODO: randomise this
		return this.shipyardVisitorCode;
	}
	
	/*
	 * isMale
	 * 
	 * 
	 * (non-Javadoc)
	 * @see com.silvaniastudios.travellers.capability.playerData.IPlayerData#isMale()
	 */

	@Override
	public boolean isMale() {
		return this.isMale;
	}
	

	@Override
	public void setMale(boolean isMale) {
		this.isMale = isMale;
	}
	
	/*
	 * knowledgeNodeUses
	 * 
	 * (non-Javadoc)
	 * @see com.silvaniastudios.travellers.capability.playerData.IPlayerData#getKnowledgeNodeUses()
	 */

	@Override
	public HashMap<String, Integer> getKnowledgeNodeUses() {
		return this.knowledgeNodeUses;
	}

	@Override
	public void useKnowlegeNode(String nodeKey) {
		if (this.knowledgeNodeUses.containsKey(nodeKey)) {
			int value = this.knowledgeNodeUses.get(nodeKey) + 1;
			
			this.knowledgeNodeUses.replace(nodeKey, value);
		} else {
			
			this.knowledgeNodeUses.put(nodeKey, 1);
		}
	}

	@Override
	public HashMap<String, Integer> setKnowledgeNodeUses(String nodeKey, int times) {
		if (this.knowledgeNodeUses.containsKey(nodeKey)) {
			this.knowledgeNodeUses.replace(nodeKey, times);
		}
		
		return this.knowledgeNodeUses;
	}

	@Override
	public ArrayList<String> getKnownLorePieces() {
		return this.knownLorePieces;
	}

	@Override
	public void learnLorePiece(String loreID) {
		this.knownLorePieces.add(loreID);
	}

	@Override
	public ArrayList<String> setKnownLorePieces(ArrayList<String> list) {
		this.knownLorePieces = list;
		return this.knownLorePieces;
	}

	@Override
	public void getShipDesignSchematics() {
		// TODO Auto-generated method stub
	}

	@Override
	public void editShipDesignSchematic(String designKey) {
		// TODO Auto-generated method stub		
	}

	@Override
	public NBTTagCompound toNBT() {
		NBTTagCompound nbtTag = new NBTTagCompound();
		nbtTag.setInteger("knowledge", this.getKnowledgeBalance());
		nbtTag.setBoolean("isMale", this.isMale());
		nbtTag.setString("shipyardVisitorCode", this.getShipyardVisitorCode());
		
		NBTTagCompound knowledgeTreeUses = new NBTTagCompound();
		for (String key : this.getKnowledgeNodeUses().keySet()) {
			knowledgeTreeUses.setInteger(key, this.getKnowledgeNodeUses().get(key));
		}
		
		int index = 0;
		NBTTagCompound knownLorePieces = new NBTTagCompound();
		for (String key : this.getKnownLorePieces()) {
			knownLorePieces.setInteger(key, index++);
		}
		
		nbtTag.setTag("knowledgeTreeUses", knowledgeTreeUses);
		nbtTag.setTag("knownLorePieces", knownLorePieces);
		//TODO: ship schematics / schematic list in general
		return nbtTag;
	}

	@Override
	public void fromNBT(NBTBase nbt) {
		NBTTagCompound nbtTag = (NBTTagCompound) nbt;

		this.setKnowledgeBalance(nbtTag.getInteger("knowledge"));
		this.setMale(nbtTag.getBoolean("isMale"));
		this.setShipyardVisitorCode(nbtTag.getString("shipyardVisitorCode"));
		
		for (String key : ((NBTTagCompound)nbtTag.getCompoundTag("knowledgeTreeUses")).getKeySet()) {
			this.setKnowledgeNodeUses(key, ((NBTTagCompound)nbtTag.getTag("knowledgeTreeUses")).getInteger(key));
		}
		
		ArrayList<String> knownLorePieces = new ArrayList<String>();
		for (String key : nbtTag.getCompoundTag("knownLorePieces").getKeySet()) {
			knownLorePieces.add(key);
		}
		
		this.setKnownLorePieces(knownLorePieces);
		//TODO: ship schematics / schematic list in general
	}

	@Override
	public void copyData(IPlayerData capability) {
		this.setKnowledgeBalance(capability.getKnowledgeBalance());
		
		this.setMale(capability.isMale());
		
		this.setShipyardVisitorCode(this.getShipyardVisitorCode());
		
		for (String key : capability.getKnowledgeNodeUses().keySet()) {
			this.setKnowledgeNodeUses(key, capability.getKnowledgeNodeUses().get(key));
		}
		
		this.setKnownLorePieces(capability.getKnownLorePieces());
		//TODO: ship schematics / schematic list in general
		
	}


}
