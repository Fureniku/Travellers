package com.silvaniastudios.travellers.capability.tree;

public class TreeNode {
	
	//TODO: Method: research node
	//TODO: Method: Unlock nodes below
	//TODO: unlocking generally
	
	public String internalName;
	private int x, y;
	private String[] parents;
	private String[] children;
	
	private int progress, threshold, max;
	private int knCost;
	
	/**
	 * @method Constructor
	 * @param name name of the node
	 * @param x X-coordinate of the node
	 * @param y Y-coordinate of the node
	 * @param strings Array of pointers in a list to parent nodes
	 * @param strings2 Array of pointers in a list to child nodes
	 * @param threshold Level at which this node is unlocked from the parent node(s)
	 * @param max Maximum amount of times this node can be researched
	 * @param knCost Knowledge Cost of the Node
	 */
	public TreeNode (String name, int x, int y, String[] strings, String[] strings2, int threshold, int max, int knCost) {
		this.internalName = name;
		this.x = x;
		this.y = y;
		this.parents = strings;
		this.children = strings2;
		this.threshold = threshold;
		this.max = max;
		this.knCost = knCost;
		
		this.progress = -1;
	}
	
	public String getInternalName() {
		return this.internalName;
	}
	
	public int getX () {
		return this.x;
	}
	
	public int getY () {
		return this.y;
	}
	
	public int getProgress() {
		return this.progress;
	}
	
	public int getThreshold () {
		return this.threshold;
	}
	
	public int getMax () {
		return this.max;
	}
	
	public int getKnCost () {
		return this.knCost;
	}
	
	public String[] getParents() {
		return this.parents;
	}
	
	public String[] getChildren() {
		return this.children;
	}

	public void setProgress(int progress) {
		this.progress = progress;
		
		//TODO: update and check for unlocks
		
	}
	
}
