/**
 * 
 */
package com.silvaniastudios.travellers.client;

import com.silvaniastudios.travellers.client.gui.GuiCrosshair;
import com.silvaniastudios.travellers.client.gui.GuiKnowledgeOverlay;
import com.silvaniastudios.travellers.client.gui.GuiScannerInformation;

import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author jamesm2w
 *
 */
@SideOnly(Side.CLIENT)
public class GuiOverlayHandler {
	
	public static final GuiKnowledgeOverlay knowledge_overlay = new GuiKnowledgeOverlay();
	public static final GuiCrosshair crosshair_overlay = new GuiCrosshair();
	public static final GuiScannerInformation scanner_information = new GuiScannerInformation();

	@SubscribeEvent
	public void onRenderGui(RenderGameOverlayEvent.Post event) {
		if (event.getType() == ElementType.EXPERIENCE) {
			knowledge_overlay.draw();
			scanner_information.draw();
		}
		
		return;
	}
	
	@SubscribeEvent
	public void onRenderCrossHair(RenderGameOverlayEvent.Pre e) {
		if (e.getType() == ElementType.CROSSHAIRS) {
			if (crosshair_overlay.draw(e.getPartialTicks())) {
				e.setCanceled(true);
			}
		}
		
		return;
	}

}
