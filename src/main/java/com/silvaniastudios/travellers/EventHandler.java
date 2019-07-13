package com.silvaniastudios.travellers;

import com.silvaniastudios.travellers.capability.knowledge.IKnowledge;
import com.silvaniastudios.travellers.capability.knowledge.KnowledgeProvider;
import com.silvaniastudios.travellers.capability.tree.IKnTree;
import com.silvaniastudios.travellers.capability.tree.KnTreeProvider;
import com.silvaniastudios.travellers.network.KnowledgeMessage;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;

public class EventHandler {
	@SubscribeEvent
	public void onPlayerLogsIn(PlayerLoggedInEvent event) {
		EntityPlayer player = event.player;
		IKnowledge knowledge = player.getCapability(KnowledgeProvider.KNOWLEDGE, null);
		boolean hasKnTree = player.hasCapability(KnTreeProvider.KNTREE, null);

		String message = String.format("§eYou have %d knowledge§r", knowledge.getKnowledge());
		player.sendMessage(new TextComponentString(message));
		
		String message2 = String.format("§eKn Tree: %s§r", hasKnTree);
		player.sendMessage(new TextComponentString(message2));
		
		String message1 = String.format("§eYou are entity {%s}§r", player.getUniqueID().toString());
		player.sendMessage(new TextComponentString(message1));
		
		PacketHandler.INSTANCE.sendTo(
				new KnowledgeMessage(
						player.getCapability(KnowledgeProvider.KNOWLEDGE, null).getKnowledge()),
				(EntityPlayerMP) player);
	}

	/**
	 * Copy data from dead player to the new player
	 */
	@SubscribeEvent
	public void onPlayerClone(PlayerEvent.Clone event) {
		EntityPlayer player = event.getEntityPlayer();
		
		IKnTree knTree = player.getCapability(KnTreeProvider.KNTREE, null);
		IKnTree oldKnTree = event.getOriginal().getCapability(KnTreeProvider.KNTREE, null);
		
		knTree.buildMap();
		for (String key : knTree.getKnTree().keySet()) {
			System.out.print(key);
			System.out.print("| Old:");
			System.out.print(oldKnTree.getKnTree().get(key).getProgress());
			System.out.print("| New:");
			System.out.println(knTree.getKnTree().get(key).getProgress());
			
			knTree.updateNode(key, oldKnTree.getKnTree().get(key).getProgress());
		}
		
		IKnowledge knowledge = player.getCapability(KnowledgeProvider.KNOWLEDGE, null);
		IKnowledge oldKnowledge = event.getOriginal().getCapability(KnowledgeProvider.KNOWLEDGE, null);

		knowledge.setKnowledge(oldKnowledge.getKnowledge());
	}

}
