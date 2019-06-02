package com.silvaniastudios.travellers.capability.knowledge;

import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

/*
 * This thing actually *provides* the capability to the game. other people could build a seperate
 * one if they wanted ( it'd have to return a IKnowledge implementation tho)
 */

public class KnowledgeProvider implements ICapabilitySerializable<NBTBase> {
	
	@CapabilityInject(IKnowledge.class)
	public static final Capability<IKnowledge> KNOWLEDGE = null;
	
	private IKnowledge instance = KNOWLEDGE.getDefaultInstance();
	
	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		// Checks if capability is a knowledge capability
		return capability == KNOWLEDGE;
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		// Check is this is a knowledge capability, if so return the instance
		return capability == KNOWLEDGE ? KNOWLEDGE.<T> cast (this.instance) : null;
	}

	@Override
	public NBTBase serializeNBT() {
		
		return KNOWLEDGE.getStorage().writeNBT(KNOWLEDGE, this.instance, null);
	}

	@Override
	public void deserializeNBT(NBTBase nbt) {
		
		KNOWLEDGE.getStorage().readNBT(KNOWLEDGE, this.instance, null, nbt);
		
	}
	
}
