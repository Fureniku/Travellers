package com.silvaniastudios.travellers;

import com.silvaniastudios.travellers.capability.knowledge.IKnowledge;
import com.silvaniastudios.travellers.capability.knowledge.KnowledgeProvider;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;

public class EventHandler {
	@SubscribeEvent
	public void onPlayerLogsIn(PlayerLoggedInEvent event) {
		EntityPlayer player = event.player;
		IKnowledge knowledge = player.getCapability(KnowledgeProvider.KNOWLEDGE, null);

		String message = String.format("�eYou have %d knowledge�r", knowledge.getKnowledge());
		player.sendMessage(new TextComponentString(message));
		
		String message1 = String.format("�e You are entity {%s}�r", player.getUniqueID().toString());
		player.sendMessage(new TextComponentString(message1));
	}

	/**
	 * Copy data from dead player to the new player
	 */
	@SubscribeEvent
	public void onPlayerClone(PlayerEvent.Clone event) {
		EntityPlayer player = event.getEntityPlayer();
		IKnowledge knowledge = player.getCapability(KnowledgeProvider.KNOWLEDGE, null);
		IKnowledge oldKnowledge = event.getOriginal().getCapability(KnowledgeProvider.KNOWLEDGE, null);

		knowledge.setKnowledge(oldKnowledge.getKnowledge());
	}
}
