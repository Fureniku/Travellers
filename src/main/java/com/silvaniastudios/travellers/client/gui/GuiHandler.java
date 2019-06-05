package com.silvaniastudios.travellers.client.gui;

import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class GuiHandler {
	
	public static final GuiKnowledgeOverlay knowledge_overlay = new GuiKnowledgeOverlay();
	
	@SubscribeEvent
	public void onRenderGui(RenderGameOverlayEvent.Post event) {
		if (event.getType() != ElementType.EXPERIENCE) return;
		knowledge_overlay.draw();
	}
}
