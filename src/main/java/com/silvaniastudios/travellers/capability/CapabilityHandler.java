package com.silvaniastudios.travellers.capability;

import com.silvaniastudios.travellers.Travellers;
import com.silvaniastudios.travellers.capability.playerData.PlayerDataProvider;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class CapabilityHandler {

	public static final ResourceLocation PLAYER_DATA = new ResourceLocation(Travellers.MODID, "playerData");
	
	@SubscribeEvent 
	public void attachCapability (AttachCapabilitiesEvent<Entity> event) {
		Entity obj = event.getObject();
		if (obj instanceof EntityPlayer) {
			event.addCapability(PLAYER_DATA, new PlayerDataProvider());
		}
	}
}
