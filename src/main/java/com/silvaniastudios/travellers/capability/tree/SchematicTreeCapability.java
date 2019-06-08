package com.silvaniastudios.travellers.capability.tree;

import java.util.HashMap;

import com.silvaniastudios.travellers.schematic.SchematicProcEngine;
import com.silvaniastudios.travellers.schematic.SchematicRarity;
import com.silvaniastudios.travellers.schematic.SchematicShipBuilding;
import com.silvaniastudios.travellers.schematic.SchematicTreeNode;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;

public class SchematicTreeCapability implements ISchematicTree {

	public SchematicTreeNode ROOT = new SchematicTreeNode(new SchematicShipBuilding(), 1);

	public SchematicTreeNode COMMON_ENGINE;
	public SchematicTreeNode UNCOMMON_ENGINE;
	public SchematicTreeNode RARE_ENGINE;
	public SchematicTreeNode EXOTIC_ENGINE;

	public HashMap<String, SchematicTreeNode> hashedTree;

	SchematicTreeNode rootNode;

	public SchematicTreeCapability() {
		this.COMMON_ENGINE = new SchematicTreeNode(new SchematicProcEngine(SchematicRarity.COMMON), 3);
		this.UNCOMMON_ENGINE = new SchematicTreeNode(new SchematicProcEngine(SchematicRarity.UNCOMMON), 4);
		this.RARE_ENGINE = new SchematicTreeNode(new SchematicProcEngine(SchematicRarity.RARE), 5);
		this.EXOTIC_ENGINE = new SchematicTreeNode(new SchematicProcEngine(SchematicRarity.EXOTIC), 6);

		this.hashedTree = new HashMap<String, SchematicTreeNode>();

		this.rootNode = makeTree();
	}

	public SchematicTreeNode makeTree() {

		ROOT.addChild(COMMON_ENGINE);
		COMMON_ENGINE.addChild(UNCOMMON_ENGINE);
		UNCOMMON_ENGINE.addChild(RARE_ENGINE);
		RARE_ENGINE.addChild(EXOTIC_ENGINE);
		
		ROOT.setUnlocked();

		hashedTree = ROOT.hashSubTree(hashedTree);
		
		return ROOT;
	}

	public String stringify() {
		String string = "";

		for (String key : hashedTree.keySet()) {
			SchematicTreeNode node = hashedTree.get(key);
			string += String.format("%s [%s] [%s] (%d / %d) {%d}", key, node.getNodeData().getKnowledgeCost(), node.checkIfUnlocked(),
					node.getTimesResearched(), node.getNoForNextLevel(), node.getNodeDepth(0));
			string += "\n";
		}

		return string;
	}

	@Override
	public boolean researchNode(String nodeHash) {
		if (this.hashedTree.containsKey(nodeHash)) {

			if (this.hashedTree.get(nodeHash).checkIfUnlocked()) {
				// TODO: Check if next node level needs to be unlocked
				this.hashedTree.get(nodeHash).researchNode();

				return true;
			}
		}

		return false;
	}

	@Override
	public int getNodeStatus(String nodeHash) {
		return this.hashedTree.containsKey(nodeHash) ? this.hashedTree.get(nodeHash).getTimesResearched() : -1;
	}

	@Override
	public HashMap<String, SchematicTreeNode> getHashedTree() {
		return this.hashedTree;
	}

	@Override
	public void setHashedTree(HashMap<String, SchematicTreeNode> hashedTree) {
		this.hashedTree = hashedTree;
	}

	@Override
	public void setTreeFromNBT(NBTBase nbt) {
		NBTTagCompound castedNbt = (NBTTagCompound) nbt;
		for (String key : castedNbt.getKeySet()) {
			if (this.getHashedTree().containsKey(key)) {
				this.getHashedTree().get(key).setTimesResearched(castedNbt.getInteger(key));
			}
		}
	}

	@Override
	public NBTBase makeNBTFromTree() {
		NBTTagCompound nbt = new NBTTagCompound();
		for (String key : this.getHashedTree().keySet()) {
			int timesResearched = this.getHashedTree().get(key).getTimesResearched();

			nbt.setInteger(key, timesResearched);
		}

		return nbt;
	}

}
