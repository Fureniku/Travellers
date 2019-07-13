package com.silvaniastudios.travellers.capability;

import com.silvaniastudios.travellers.Travellers;
import com.silvaniastudios.travellers.capability.knowledge.KnowledgeProvider;
import com.silvaniastudios.travellers.capability.tree.KnTreeProvider;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class CapabilityHandler {
	
	public static final ResourceLocation KNOWLEDGE = new ResourceLocation(Travellers.MODID, "knowledge");
	public static final ResourceLocation KNTREE = new ResourceLocation(Travellers.MODID, "kntree");
	
	@SubscribeEvent 
	public void attachCapability (AttachCapabilitiesEvent<Entity> event) {
		Entity obj = event.getObject();
		if (obj instanceof EntityPlayer) {
			event.addCapability(KNOWLEDGE, new KnowledgeProvider());
			event.addCapability(KNTREE, new KnTreeProvider());
		}
	}
}
