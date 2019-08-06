package com.silvaniastudios.travellers.capability.playerData;

import java.util.ArrayList;
import java.util.HashMap;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;

public interface IPlayerData {
	
	/*
	 * Knowledge Balance
	 */
	public void setKnowledgeBalance(int knowledgeBalance);
	
	public int incrementKnowledgeBalance(int increase);
	
	public int getKnowledgeBalance();
	
	/*
	 * Shipyard Visitor Code
	 */
	public void setShipyardVisitorCode(String code);
	
	public String getShipyardVisitorCode();
	
	public String generateNewShipyardVisitorCode();
	
	/*
	 * Character
	 */
	public boolean isMale();
	
	public void setMale(boolean isMale);
	
	/*
	 * Knowledge Tree
	 */
	
	public HashMap<String, Integer> getKnowledgeNodeUses();
	
	public void useKnowlegeNode(String nodeKey);
	
	public HashMap<String, Integer> setKnowledgeNodeUses(String nodeKey, int times);
	
	/*
	 * Lore pieces
	 */
	
	public ArrayList<String> getKnownLorePieces();
	
	public void learnLorePiece(String loreID);
	
	public ArrayList<String> setKnownLorePieces(ArrayList<String> list);
	
	//TODO: ship schematics / schematic list in general
	/*
	 * Ship schematics
	 */
	// TODO: <T> getShipDesignSchematics();
	public HashMap<String, ItemStack> getShipDesignSchematics();
	
	public ItemStack editShipDesignSchematic(String designKey);
	
	/*
	 * Schematic List
	 */
	public boolean learnSchematic(ItemStack schematic);
	
	public ItemStack unlearnSchematic(ItemStack schematic);
	
	public ArrayList<ItemStack> getSchematicList();
	
	public boolean hasLearntSchematic(ItemStack schematic);
	
	/*
	 * General
	 */
	public NBTTagCompound toNBT();
	
	public void fromNBT(NBTBase nbtTag);
	
	public void copyData(IPlayerData capability);
}
