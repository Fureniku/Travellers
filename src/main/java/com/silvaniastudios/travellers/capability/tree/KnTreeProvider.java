package com.silvaniastudios.travellers.capability.tree;

import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

public class KnTreeProvider implements ICapabilitySerializable<NBTBase> {
	
	@CapabilityInject(IKnTree.class)
	public static final Capability<IKnTree> KNTREE = null;
	
	private IKnTree instance = KNTREE.getDefaultInstance();

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		return capability == KNTREE;
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		return capability == KNTREE ? KNTREE.<T> cast (this.instance) : null;
	}

	@Override
	public NBTBase serializeNBT() {
		return KNTREE.getStorage().writeNBT(KNTREE, this.instance, null);
	}

	@Override
	public void deserializeNBT(NBTBase nbt) {
		KNTREE.getStorage().readNBT(KNTREE, this.instance, null, nbt);
	}

}
