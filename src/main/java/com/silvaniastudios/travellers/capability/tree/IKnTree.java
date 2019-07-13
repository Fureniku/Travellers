package com.silvaniastudios.travellers.capability.tree;

import java.util.HashMap;

public interface IKnTree {
	
	public boolean researchNode (String name);
	
	public void updateNode (String name, int progress);
	
	public void buildMap ();
	
	public HashMap<String, TreeNode> getKnTree();
	
	public String toString();
}
