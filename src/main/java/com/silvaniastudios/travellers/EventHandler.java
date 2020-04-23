package com.silvaniastudios.travellers;

import com.silvaniastudios.travellers.capability.playerData.IPlayerData;
import com.silvaniastudios.travellers.capability.playerData.PlayerDataProvider;
import com.silvaniastudios.travellers.client.ClientProxy;
import com.silvaniastudios.travellers.network.OpenKnowledgeTree;
import com.silvaniastudios.travellers.network.PlayerDataSyncMessage;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerRespawnEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EventHandler {
	
	@SubscribeEvent
	public void onPlayerLogsIn(PlayerLoggedInEvent event) {
		EntityPlayer player = event.player;
		IPlayerData playerData = player.getCapability(PlayerDataProvider.PLAYER_DATA, null);
		
		ITextComponent msg = ChatHandler.translatedString("chat.message.knowledgeBalance", TextFormatting.GOLD,
				ChatHandler.number(playerData.getKnowledgeBalance(), TextFormatting.WHITE));
		player.sendMessage(msg);
		
		msg = ChatHandler.translatedString("chat.message.entityInfo", TextFormatting.GOLD,
				ChatHandler.translatedString(player.getUniqueID().toString(), TextFormatting.WHITE));
		player.sendMessage(msg);

		PacketHandler.INSTANCE.sendTo(new PlayerDataSyncMessage(playerData), (EntityPlayerMP) player);
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

		PacketHandler.INSTANCE.sendTo(new PlayerDataSyncMessage(playerData), (EntityPlayerMP) event.player);
	}
	
	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void onKeyPressed (KeyInputEvent event) {
		KeyBinding[] keyBindings = ClientProxy.keyBindings;
		
		if (keyBindings[0].isPressed()) {
			// open gui
			PacketHandler.INSTANCE.sendToServer(new OpenKnowledgeTree((EntityPlayer)Minecraft.getMinecraft().player));
		}
	}
	
	

}
