package com.silvaniastudios.travellers.schematic;

import java.util.ArrayList;
import java.util.HashMap;

public class SchematicTreeNode {

	private Schematic nodeData;
	private SchematicTreeNode parentNode;
	private ArrayList<SchematicTreeNode> childNodes = new ArrayList<SchematicTreeNode>();

	private int noForNextLevel;
	private int timesResearched = 0;
	protected boolean unlocked = false;

	/**
	 * Represents a node in the recursive tree structure
	 * 
	 * @param nodeData Schematic to act as node data
	 * @param parentNode Parent node (if there is one)
	 * @param childNodes Children nodes (if there are any)
	 * @param noForNextLevel No of times to be researched before moving onto the next node
	 */
	public SchematicTreeNode(Schematic nodeData, SchematicTreeNode parentNode, ArrayList<SchematicTreeNode> childNodes,
			int noForNextLevel) {
		this.setNodeData(nodeData);

		if (parentNode != null) {
			this.setParentNode(parentNode);
		}

		if (childNodes != null) {
			this.setChildNodes(childNodes);
		}
	}

	public SchematicTreeNode(Schematic nodeData) {
		this.setNodeData(nodeData);
	}
	
	public SchematicTreeNode(Schematic nodeData, int noForNextLevel) {
		this.setNodeData(nodeData);
		this.setNoForNextLevel(noForNextLevel);
	}

	/**
	 * 
	 * @param node Node to add to this tree
	 * @return the new parent node
	 */
	public SchematicTreeNode addChild(SchematicTreeNode node) {
		this.childNodes.add(node);
		node.setParentNode(this);
		return this;
	}

	/**
	 * 
	 * @param count current depth
	 * @return depth of the node its called on
	 */
	public int getNodeDepth(int count) {
		if (this.parentNode != null) {
			return this.parentNode.getNodeDepth(count++);
		} else {
			return count;
		}
	}

	/**
	 * Increments the times researched
	 * @return ratio of times researched to the number needed for the next level
	 */
	public float researchNode() {
		return timesResearched++ / noForNextLevel;
	}
	
	/**
	 * @return true if the times researched is higher than the times needed to unlock the next node level
	 */
	public boolean checkIfUnlocked () {
		return getTimesResearched() >= noForNextLevel;
	}
	
	/**
	 * Sets the unlocked value to true
	 */
	public void setUnlocked() {
		this.unlocked = true;
	}
	
	
	/**
	 * Takes a hashmap and appends the subtree to the hashmap
	 * 
	 * @param hash Current hashmap to append to
	 * @return Hashed map of all nodes in the subtree
	 */
	public HashMap<String, SchematicTreeNode> hashSubTree (HashMap<String, SchematicTreeNode> hash) {
		hash.put(this.nodeData.schematicName, this);
		if (!this.getChildNodes().isEmpty()) {
			for (SchematicTreeNode node : this.getChildNodes()) {
				hash.putAll(node.hashSubTree(new HashMap<String, SchematicTreeNode>()));
			}
		}
		
		return hash;
	}

	/**
	 * @return the nodeData
	 */
	public Schematic getNodeData() {
		return nodeData;
	}

	/**
	 * @param nodeData
	 *            the nodeData to set
	 */
	public void setNodeData(Schematic nodeData) {
		this.nodeData = nodeData;
	}

	/**
	 * @return the parentNode
	 */
	public SchematicTreeNode getParentNode() {
		return parentNode;
	}

	/**
	 * @param parentNode
	 *            the parentNode to set
	 */
	public void setParentNode(SchematicTreeNode parentNode) {
		this.parentNode = parentNode;
	}

	/**
	 * @return the childNodes
	 */
	public ArrayList<SchematicTreeNode> getChildNodes() {
		return childNodes;
	}

	/**
	 * @param childNodes
	 *            the childNodes to set
	 */
	public void setChildNodes(ArrayList<SchematicTreeNode> childNodes) {
		this.childNodes = childNodes;
	}

	/**
	 * @param noForNextLevel
	 *            the noForNextLevel to set
	 * @return
	 */
	public SchematicTreeNode setNoForNextLevel(int noForNextLevel) {
		this.noForNextLevel = noForNextLevel;
		return this;
	}
	
	public int getNoForNextLevel() {
		return this.noForNextLevel;
	}

	/**
	 * @return the timesResearched
	 */
	public int getTimesResearched() {
		return timesResearched;
	}

	/**
	 * @param timesResearched the timesResearched to set
	 */
	// TODO: check if next node needs to be unlocked
	public void setTimesResearched(int timesResearched) {
		this.timesResearched = timesResearched;
	}

}
