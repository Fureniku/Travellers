package com.silvaniastudios.travellers.capability.knowledge;

/*
 * This thing does the logic of the knowledge points
 */

public class Knowledge implements IKnowledge {
	
	private int knowledge = 0;
	

	@Override
	public void setKnowledge(int knowledgeAmount) {

		this.knowledge = knowledgeAmount;
	}

	@Override
	public int getKnowledge() {

		return this.knowledge;
	}

}
