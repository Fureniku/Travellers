package com.silvaniastudios.travellers.client.gui;

import java.util.Arrays;

import com.silvaniastudios.travellers.Travellers;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class GuiButtonTravellers extends GuiButton {

	public int iconId = 0;
	public String hoverText = "";
	public int colourType = 0;
	
	public GuiButtonTravellers(int buttonId, int x, int y, int sizeX, int sizeY) {
		super(buttonId, x, y, sizeX, sizeY, "");
	}

	@Override
	public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
        if (this.visible) {
        	ResourceLocation buttonIcon = new ResourceLocation(Travellers.MODID + ":textures/gui/buttons.png");
            mc.getTextureManager().bindTexture(buttonIcon);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            this.hovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;

            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
            GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);

            if (!this.enabled) {
            	this.drawTexturedModalRect(this.x, this.y, 0, 80, this.width, this.height);
            } else {
            	if (this.hovered) {
            		this.drawTexturedModalRect(this.x, this.y, 0, 40, this.width, this.height);
            	} else {
            		this.drawTexturedModalRect(this.x, this.y, 0, 0, this.width, this.height);
            	}
            }
                        
            this.mouseDragged(mc, mouseX, mouseY);
        }
    }

	//Borrowed from GuiScreen
	protected void drawHoveringText(String text, int x, int y, FontRenderer font)  {
        net.minecraftforge.fml.client.config.GuiUtils.drawHoveringText(Arrays.asList(text), x, y, width, height, -1, font);
    }
}