package com.silvaniastudios.travellers.client.gui;

import com.silvaniastudios.travellers.Travellers;
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
		ScaledResolution scaled = new ScaledResolution(Minecraft.getMinecraft());
		this.width = scaled.getScaledWidth();
		this.height = scaled.getScaledHeight();
		
		if (Minecraft.getMinecraft().player.hasCapability(PlayerDataProvider.PLAYER_DATA, null)) {
			this.playerData = Minecraft.getMinecraft().player.getCapability(PlayerDataProvider.PLAYER_DATA, null);

			String string = String.format("Knowledge: %d", this.playerData.getKnowledgeBalance());
			String version = String.format("Travellers indev-%s", Travellers.VERSION);

			int versionWidth = Minecraft.getMinecraft().fontRenderer.getStringWidth(version);

			drawString(Minecraft.getMinecraft().fontRenderer, string, 0, 0, Integer.parseInt("FFAA00", 16));
			
			Minecraft.getMinecraft().fontRenderer.drawString(version, this.width - versionWidth,
					this.height - 9, Integer.parseInt("FFFFFF", 16));

		}

	}

}
