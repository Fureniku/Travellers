package com.silvaniastudios.travellers;

import com.silvaniastudios.travellers.capability.playerData.IPlayerData;
import com.silvaniastudios.travellers.capability.playerData.PlayerDataProvider;
import com.silvaniastudios.travellers.network.PlayerDataSyncMessage;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.ItemPickupEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerRespawnEvent;

public class EventHandler {
	@SubscribeEvent
	public void onPlayerLogsIn(PlayerLoggedInEvent event) {
		EntityPlayer player = event.player;
		//IKnowledge knowledge = player.getCapability(KnowledgeProvider.KNOWLEDGE, null);
		
		IPlayerData playerData = player.getCapability(PlayerDataProvider.PLAYER_DATA, null);
		
		String message = String.format("§eYou have %d knowledge§r", playerData.getKnowledgeBalance());
		player.sendMessage(new TextComponentString(message));
		
		String message1 = String.format("§eYou are entity {%s}§r", player.getUniqueID().toString());
		player.sendMessage(new TextComponentString(message1));
		
		PacketHandler.INSTANCE.sendTo(new PlayerDataSyncMessage(playerData),
				(EntityPlayerMP) player);
	}

	/**
	 * Copy data from dead player to the new player
	 */
	@SubscribeEvent
	public void onPlayerClone(PlayerEvent.Clone event) {
		EntityPlayer player = event.getEntityPlayer();
		
		IPlayerData playerData = player.getCapability(PlayerDataProvider.PLAYER_DATA, null);
		IPlayerData oldPlayerData = event.getOriginal().getCapability(PlayerDataProvider.PLAYER_DATA, null);

		playerData.copyData(oldPlayerData);
		
	}
	
	@SubscribeEvent
	public void onPlayerRespawn(PlayerRespawnEvent event) {
		
		IPlayerData playerData = event.player.getCapability(PlayerDataProvider.PLAYER_DATA, null);

		PacketHandler.INSTANCE.sendTo(new PlayerDataSyncMessage(playerData),
				(EntityPlayerMP) event.player);
	}
	
	@SubscribeEvent
	public void onPlayerPickupItemStack (ItemPickupEvent event) {
		System.out.println(event.getOriginalEntity().serializeNBT().toString());
	}

}
