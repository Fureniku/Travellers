package com.silvaniastudios.travellers.client.gui;

import com.silvaniastudios.travellers.capability.knowledge.IKnowledge;
import com.silvaniastudios.travellers.capability.knowledge.KnowledgeProvider;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;

public class GuiKnowledgeOverlay extends Gui {

	protected int width;
	protected int height;

	protected IKnowledge knowledgeCapability;

	public GuiKnowledgeOverlay() {
		ScaledResolution scaled = new ScaledResolution(Minecraft.getMinecraft());
		this.width = scaled.getScaledWidth();
		this.height = scaled.getScaledHeight();
	}

	public void draw() {
		if (Minecraft.getMinecraft().player.hasCapability(KnowledgeProvider.KNOWLEDGE, null)) {
			this.knowledgeCapability = Minecraft.getMinecraft().player.getCapability(KnowledgeProvider.KNOWLEDGE, null);

			String string = String.format("Knowledge: %d", this.knowledgeCapability.getKnowledge());

			//drawCenteredString(Minecraft.getMinecraft().fontRenderer, string, 0, 0,
			//		Integer.parseInt("FFAA00", 16));
			
			drawString(Minecraft.getMinecraft().fontRenderer, string, 0, 0, Integer.parseInt("FFAA00", 16));

		}

	}

}
