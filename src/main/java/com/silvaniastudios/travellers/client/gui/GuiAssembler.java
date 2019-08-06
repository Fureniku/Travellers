package com.silvaniastudios.travellers.client.gui;

import com.silvaniastudios.travellers.Travellers;
import com.silvaniastudios.travellers.blocks.tileentity.assembler.AssemblerContainer;
import com.silvaniastudios.travellers.blocks.tileentity.assembler.AssemblerEntity;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.GuiScrollingList;

public class GuiAssembler extends GuiContainer {
	
	private static final ResourceLocation gui_texture_a = new ResourceLocation(Travellers.MODID, "textures/gui/assembler_a.png");
	private static final ResourceLocation gui_texture_b = new ResourceLocation(Travellers.MODID, "textures/gui/assembler_b.png");
	private static final ResourceLocation icons = new ResourceLocation(Travellers.MODID, "textures/gui/icons.png");
	
	private AssemblerEntity tileEntity;
	
	private GuiScrollingList listTest;
	
	public GuiAssembler(AssemblerEntity entity, AssemblerContainer container) {
		super(container);
		this.tileEntity = entity;
		xSize = 463;
		ySize = 256;
	}
	
	@Override
	public void initGui() {
		super.initGui();
		int left = (width - xSize) / 2;
		int top = (height - ySize) / 2;
		//listTest = new GuiScrollingList(Minecraft.getMinecraft(), 100, 600, 50, 650, 400, 100, width, height);
	}
	
	@Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		super.drawScreen(mouseX, mouseY, partialTicks);
		int left = (width - xSize) / 2;
		int top = (height - ySize) / 2;
	}
	
	@Override
	protected void actionPerformed(GuiButton button) {
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		
	}

	public int getStringWidthInPixels(String str) {
		int length = 0;
		
		for (int i = 0; i < str.length(); i++) {
			length += fontRenderer.getCharWidth(str.charAt(i));
		}
		
		return length;
	}
}
