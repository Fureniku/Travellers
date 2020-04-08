package com.silvaniastudios.travellers.capability.playerData;

import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

/**
 * 
 * @author jamesm2w
 */
public class PlayerDataProvider implements ICapabilitySerializable<NBTBase> {

	@CapabilityInject(IPlayerData.class)
	public static final Capability<IPlayerData> PLAYER_DATA = null;
	
	private IPlayerData instance = PLAYER_DATA.getDefaultInstance();
	
	
	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {

		return capability == PLAYER_DATA;
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {

		return (capability == PLAYER_DATA) ? PLAYER_DATA.<T>cast(this.instance) : null;
	}

	@Override
	public NBTBase serializeNBT() {
		
		return PLAYER_DATA.getStorage().writeNBT(PLAYER_DATA, this.instance, null);
	}

	@Override
	public void deserializeNBT(NBTBase nbt) {

		PLAYER_DATA.getStorage().readNBT(PLAYER_DATA, this.instance, null, nbt);
	}

}
