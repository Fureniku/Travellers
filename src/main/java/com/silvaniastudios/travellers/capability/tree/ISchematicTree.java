package com.silvaniastudios.travellers.capability.tree;

import java.util.HashMap;

import com.silvaniastudios.travellers.schematic.SchematicTreeNode;

import net.minecraft.nbt.NBTBase;

public interface ISchematicTree {
	
	public boolean researchNode(String nodeHash);
	
	public int getNodeStatus (String nodeHash);
	
	public HashMap<String, SchematicTreeNode> getHashedTree ();
	
	public void setHashedTree(HashMap<String, SchematicTreeNode> hashedTree);
	
	public String stringify();
	
	public void setTreeFromNBT(NBTBase nbt);
	
	public NBTBase makeNBTFromTree();

}
