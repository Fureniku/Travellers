package com.silvaniastudios.travellers.capability.playerData;

import java.util.ArrayList;
import java.util.HashMap;

import com.silvaniastudios.travellers.ModItems;
import com.silvaniastudios.travellers.items.ItemSchematic;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;

public class PlayerData implements IPlayerData {
	
	private int knowledgeBalance;
	private String shipyardVisitorCode;
	private boolean isMale;
	private HashMap<String, Integer> knowledgeNodeUses;
	private ArrayList<String> knownLorePieces;
	private ArrayList<ItemSchematic> schematicList;
	private HashMap<String, ItemSchematic> shipDesignList;
	
	public PlayerData () {
		this.knowledgeBalance = 0;
		this.shipyardVisitorCode = "1234";
		this.isMale = true;
		this.knowledgeNodeUses = new HashMap<String, Integer>();
		this.knownLorePieces = new ArrayList<String>();
		
		this.schematicList = new ArrayList<ItemSchematic>();
		this.shipDesignList = new HashMap<String, ItemSchematic>();
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
		
		this.incrementKnowledgeBalance(ModItems.parsed_codex.codexMapped.get(loreID).knowledge);
	}

	@Override
	public ArrayList<String> setKnownLorePieces(ArrayList<String> list) {
		this.knownLorePieces = list;
		return this.knownLorePieces;
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
		
		index = 0;
		NBTTagCompound learntSchematics = new NBTTagCompound();
		for (ItemSchematic schematic : this.schematicList) {
			learntSchematics.setTag(String.format("Schematic%d", index++), schematic.toNBT());
		}
		
		nbtTag.setTag("learntSchematicList", learntSchematics);
		nbtTag.setTag("knowledgeTreeUses", knowledgeTreeUses);
		nbtTag.setTag("knownLorePieces", knownLorePieces);
		
		NBTTagCompound shipDesigns = new NBTTagCompound();
		for (String key : this.shipDesignList.keySet()) {
			shipDesigns.setTag(key, this.shipDesignList.get(key).toNBT());
		}
		
		nbtTag.setTag("shipDesignList", shipDesigns);
		
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
		
		ArrayList<ItemSchematic> schematicList = new ArrayList<ItemSchematic>();
		for (String key : nbtTag.getCompoundTag("learntSchematicList").getKeySet()) {
			schematicList.add(ItemSchematic.fromNBT(nbtTag.getCompoundTag("learntSchematicList").getCompoundTag(key)));
		}
		
		HashMap<String, ItemSchematic> designList = new HashMap<String, ItemSchematic>();
		for (String key : nbtTag.getCompoundTag("shipDesignList").getKeySet()) {
			designList.put(key, ItemSchematic.fromNBT(nbtTag.getCompoundTag("shipDesignList").getCompoundTag(key)));
		}
		
		this.shipDesignList = designList;
		this.schematicList = schematicList;
		this.setKnownLorePieces(knownLorePieces);

	}

	@Override
	public void copyData(IPlayerData capability) {
		this.setKnowledgeBalance(capability.getKnowledgeBalance());
		
		this.setMale(capability.isMale());
		
		this.setShipyardVisitorCode(capability.getShipyardVisitorCode());
		
		for (String key : capability.getKnowledgeNodeUses().keySet()) {
			this.setKnowledgeNodeUses(key, capability.getKnowledgeNodeUses().get(key));
		}
		
		this.schematicList = capability.getSchematicList();
		
		this.shipDesignList = capability.getShipDesignSchematics();
		
		this.setKnownLorePieces(capability.getKnownLorePieces());
	}

	@Override
	public HashMap<String, ItemSchematic> getShipDesignSchematics() {
		return this.shipDesignList;
	}

	@Override
	public ItemSchematic editShipDesignSchematic(String designKey) {
		return this.shipDesignList.get(designKey);
	}

	@Override
	public boolean learnSchematic(ItemSchematic schematic) {
		if (!hasLearntSchematic(schematic)) {
			this.schematicList.add(schematic);
			
			return true;
		}
		
		return false;
	}

	@Override
	public ItemSchematic unlearnSchematic(ItemSchematic schematic) {
		
		return schematic;
	}

	@Override
	public ArrayList<ItemSchematic> getSchematicList() {
		return this.schematicList;
	}
	
	@Override
	public boolean hasLearntSchematic(ItemSchematic schematic) {
		for (ItemSchematic i : this.schematicList) {
			if (i == schematic) {
				return true;
			}
		}
		
		return false;
	}

}
