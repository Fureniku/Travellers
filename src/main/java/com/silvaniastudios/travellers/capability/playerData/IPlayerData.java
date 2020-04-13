package com.silvaniastudios.travellers.capability.playerData;

import java.util.ArrayList;
import java.util.HashMap;

import com.silvaniastudios.travellers.entity.EntityScannerLine;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;

/**
 * 
 * @author jamesm2w
 */
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
	public boolean isDev();
	
	public void setDev(boolean isDev);
	
	/*
	 * Scanning Entity
	 */
	
	public boolean isScanning();
	
	public EntityScannerLine getScanningEntity();
	
	public void setScanning(EntityScannerLine scanning);
	
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
	 * Scanned Things List
	 */
	public boolean scanObject(String unlocalizedString);
	
	public ArrayList<String> getScannedObjects();
	
	public boolean hasScannedObject(String unlocalizedString);
	
	/*
	 * General
	 */
	public NBTTagCompound toNBT();
	
	public void fromNBT(NBTBase nbtTag);
	
	public void copyData(IPlayerData capability);
}
