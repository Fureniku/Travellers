package com.silvaniastudios.travellers.capability.tree;

import java.util.HashMap;

public class KnTree implements IKnTree {
	
	private static final TreeNode ShipBuildingNode = 
			new TreeNode("node.shipbuilding", 0, 0, new String[0], new String[]{"node.commonengine"}, 0, 1, 205);
	
	private static final TreeNode CommonEngineNode = 
			new TreeNode("node.commonengine", 0, 10, new String[]{"node.shipbuilding"}, new String[0], 1, -1, 250);
	
	
	public HashMap<String, TreeNode> knTree = new HashMap<String, TreeNode>();
	
	public void buildMap () {
		knTree.clear();
		knTree.put(ShipBuildingNode.getInternalName(), ShipBuildingNode);
		ShipBuildingNode.setProgress(0);
		knTree.put(CommonEngineNode.getInternalName(), CommonEngineNode);
	}
	
	@Override
	public boolean researchNode(String name) {
		if (this.knTree.containsKey(name)) {
			
			TreeNode node = this.knTree.get(name);
			
			boolean MP = node.getProgress() >= node.getMax();
			boolean M = node.getMax() != -1;
			boolean P = node.getProgress() != -1;
			
			if (!((!MP && P ) || (MP && M && P))) {
				return false;
			}
			
			node.setProgress(node.getProgress() + 1);
			
			for (String key : this.knTree.get(name).getChildren()) {
				TreeNode childNode = this.knTree.get(key);

				if (node.getProgress() >= childNode.getThreshold()) {
					childNode.setProgress(0);
				}
			}
			
			return true;
		} 
		return false;
	}
	
	public String toString () {
		String result = "";
		
		for (String key : this.knTree.keySet()) {
			TreeNode node = this.knTree.get(key);
			
			result += String.format("%s: (Cost: %d) %d / %d [Th: %d] \n", node.getInternalName(), node.getKnCost(), node.getProgress(), node.getMax(), node.getThreshold());
		}
		
		return result;
	}

	@Override
	public void updateNode(String name, int progress) {
		
		knTree.get(name).setProgress(progress);
	}

	@Override
	public HashMap<String, TreeNode> getKnTree() {
		return this.knTree;
	}

}
