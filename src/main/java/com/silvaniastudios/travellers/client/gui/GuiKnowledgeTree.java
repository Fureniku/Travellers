package com.silvaniastudios.travellers.client.gui;

import com.silvaniastudios.travellers.Travellers;
import com.silvaniastudios.travellers.inventory.EmptyContainer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class GuiKnowledgeTree extends GuiContainer {

	public static  final ResourceLocation TEXTURE_LEFT = new ResourceLocation(Travellers.MODID, "textures/gui/inventory_generic_left.png");

	public static  final ResourceLocation TEXTURE_RIGHT = new ResourceLocation(Travellers.MODID, "textures/gui/inventory_generic_right.png");
	
	
	private int xSize = 206 + 256;
	private int ySize = 256;
	
	private int screenLeft;
	private int screenTop;
	
	private final Minecraft mc = Minecraft.getMinecraft();
	
	public GuiKnowledgeTree(EntityPlayer player, World world) {
		super(new EmptyContainer(player.inventory, !world.isRemote, player));
	}
	
	@Override
	public void initGui() {
		super.initGui();
	
		screenLeft = (width - xSize) / 2;
		screenTop = (height - ySize) / 2;
		
		System.out.println(screenLeft);
		System.out.println(screenTop);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		screenLeft = (width - xSize) / 2;
		screenTop = (height - ySize) / 2;
		
		mc.renderEngine.bindTexture(TEXTURE_LEFT);
		drawTexturedModalRect(screenLeft, screenTop, 0, 0, 206, ySize);
		
		mc.renderEngine.bindTexture(TEXTURE_RIGHT);
		drawTexturedModalRect(screenLeft + 206, screenTop, 0, 0, 256, ySize);
		
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		screenLeft = (this.width - xSize) / 2;
		screenTop = (this.height - ySize) / 2;
		
		mc.fontRenderer.drawStringWithShadow("Knowledge Tree", 0, 0, 0xFFFFFF);
	}

}
