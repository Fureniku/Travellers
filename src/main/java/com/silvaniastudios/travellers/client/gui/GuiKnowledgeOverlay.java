package com.silvaniastudios.travellers.client.gui;

import com.silvaniastudios.travellers.capability.playerData.IPlayerData;
import com.silvaniastudios.travellers.capability.playerData.PlayerDataProvider;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;

public class GuiKnowledgeOverlay extends Gui {

	protected int width;
	protected int height;

	protected IPlayerData playerData;

	public GuiKnowledgeOverlay() {
		ScaledResolution scaled = new ScaledResolution(Minecraft.getMinecraft());
		this.width = scaled.getScaledWidth();
		this.height = scaled.getScaledHeight();
	}

	public void draw() {
		if (Minecraft.getMinecraft().player.hasCapability(PlayerDataProvider.PLAYER_DATA, null)) {
			this.playerData = Minecraft.getMinecraft().player.getCapability(PlayerDataProvider.PLAYER_DATA, null);

			String string = String.format("Knowledge: %d", this.playerData.getKnowledgeBalance());

			//drawCenteredString(Minecraft.getMinecraft().fontRenderer, string, 0, 0,
			//		Integer.parseInt("FFAA00", 16));
			
			drawString(Minecraft.getMinecraft().fontRenderer, string, 0, 0, Integer.parseInt("FFAA00", 16));

		}

	}

}
