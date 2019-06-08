package com.silvaniastudios.travellers.client.gui;

import org.lwjgl.input.Mouse;

import com.silvaniastudios.travellers.Travellers;
import com.silvaniastudios.travellers.blocks.tileentity.shipyard.ShipyardContainer;
import com.silvaniastudios.travellers.blocks.tileentity.shipyard.ShipyardEntity;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;

public class GuiShipyard extends GuiContainer {
	
	private static final ResourceLocation gui_texture_a = new ResourceLocation(Travellers.MODID, "textures/gui/shipyard_a.png");
	private static final ResourceLocation gui_texture_b = new ResourceLocation(Travellers.MODID, "textures/gui/shipyard_b.png");
	private static final ResourceLocation icons = new ResourceLocation(Travellers.MODID, "textures/gui/icons.png");
	
	private ShipyardEntity tileEntity;
	private GuiButton craft;
	private GuiButtonTravellers show;
	private GuiButtonTravellers rename;
	private GuiButtonTravellers shipSlot1;
	private GuiButtonTravellers shipSlot2;
	private GuiButtonTravellers shipSlot3;
	private GuiButtonTravellers shipSlot4;
	private GuiButtonTravellers shipSlot5;
	private GuiButtonTravellers editFrame;
	private GuiButtonTravellers saveFrame;
	private GuiButtonTravellers revertChanges;
	private GuiButtonTravellers duplicate;
	private GuiButtonTravellers unload;
	
	private String shipyard_code = "1234";
	
	private String ship_1 = "Dinghy";
	private String ship_2 = "Tug";
	private String ship_3 = "Skiff";
	private String ship_4 = "Dunebug";
	private String ship_5 = "Spear";
	
	private String frame_owner = "Bossa Studios";
	private String craft_time = "0:05";
	private String frame_weight = "0kg";
	
	private String beams_cost = "15";
	private String decks_cost = "40";
	
	private int selected_ship = 1;
	
	public GuiShipyard(ShipyardEntity entity, ShipyardContainer container) {
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
		
		craft = new GuiButton(0, left + 261, top + 200, 106, 20, I18n.format("travellers.gui.button.craft"));
		show = new GuiButtonTravellers(1, left + 119, top + 21, 50, 14);
		rename = new GuiButtonTravellers(2, left + 119, top + 48, 50, 14);
		
		shipSlot1 = new GuiButtonTravellers(3, left + 8, top + 91, 161, 12);
		shipSlot2 = new GuiButtonTravellers(4, left + 8, top + 104, 161, 12);
		shipSlot3 = new GuiButtonTravellers(5, left + 8, top + 117, 161, 12);
		shipSlot4 = new GuiButtonTravellers(6, left + 8, top + 130, 161, 12);
		shipSlot5 = new GuiButtonTravellers(7, left + 8, top + 143, 161, 12);
		
		editFrame = new GuiButtonTravellers(8, left + 174, top + 23, 93, 20);
		saveFrame = new GuiButtonTravellers(9, left + 268, top + 23, 92, 20);
		revertChanges = new GuiButtonTravellers(10, left + 361, top + 23, 93, 20);
		
		duplicate = new GuiButtonTravellers(11, left + 201, top + 225, 111, 20);
		unload = new GuiButtonTravellers(11, left + 314, top + 225, 111, 20);
		
		shipSlot1.enabled = false;
		shipSlot2.enabled = true;
		shipSlot3.enabled = true;
		shipSlot4.enabled = true;
		shipSlot5.enabled = true;
		
		buttonList.add(craft);
		buttonList.add(show);
		buttonList.add(rename);
		
		buttonList.add(shipSlot1);
		buttonList.add(shipSlot2);
		buttonList.add(shipSlot3);
		buttonList.add(shipSlot4);
		buttonList.add(shipSlot5);
		
		buttonList.add(editFrame);
		buttonList.add(saveFrame);
		buttonList.add(revertChanges);
		
		buttonList.add(duplicate);
		buttonList.add(unload);
	}
	
	@Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		super.drawScreen(mouseX, mouseY, partialTicks);
		int left = (width - xSize) / 2;
		int top = (height - ySize) / 2;
		
		if (mouseX >= left + 119 && mouseX <= left + 169 && mouseY >= top + 21 && mouseY <= top + 35 && Mouse.isButtonDown(0)) {
			fontRenderer.drawString(shipyard_code, left + 12, top + 24, 4210752);
		} else {
			fontRenderer.drawString("****", left + 12, top + 24, 4210752);
		}
		String ship_name = ship_1;
		
		if (selected_ship == 1) {
			shipSlot1.enabled = false;
			shipSlot2.enabled = true;
			shipSlot3.enabled = true;
			shipSlot4.enabled = true;
			shipSlot5.enabled = true;
			ship_name = ship_1;
		}
		
		if (selected_ship == 2) {
			shipSlot1.enabled = true;
			shipSlot2.enabled = false;
			shipSlot3.enabled = true;
			shipSlot4.enabled = true;
			shipSlot5.enabled = true;
			ship_name = ship_2;
		}
		
		if (selected_ship == 3) {
			shipSlot1.enabled = true;
			shipSlot2.enabled = true;
			shipSlot3.enabled = false;
			shipSlot4.enabled = true;
			shipSlot5.enabled = true;
			ship_name = ship_3;
		}
		
		if (selected_ship == 4) {
			shipSlot1.enabled = true;
			shipSlot2.enabled = true;
			shipSlot3.enabled = true;
			shipSlot4.enabled = false;
			shipSlot5.enabled = true;
			ship_name = ship_4;
		}
		
		if (selected_ship == 5) {
			shipSlot1.enabled = true;
			shipSlot2.enabled = true;
			shipSlot3.enabled = true;
			shipSlot4.enabled = true;
			shipSlot5.enabled = false;
			ship_name = ship_5;
		}
		
		fontRenderer.drawString(ship_name, left + 12, top + 51, 4210752);
	}
	
	@Override
	protected void actionPerformed(GuiButton button) {
		if (button == shipSlot1) { selected_ship = 1; }
		if (button == shipSlot2) { selected_ship = 2; }
		if (button == shipSlot3) { selected_ship = 3; }
		if (button == shipSlot4) { selected_ship = 4; }
		if (button == shipSlot5) { selected_ship = 5; }
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		String show = I18n.format("travellers.gui.button.show");
		String rename = I18n.format("travellers.gui.button.rename");
		String guidance = I18n.format("travellers.gui.shipyard.guidance");
		String edit = I18n.format("travellers.gui.button.edit");
		String save = I18n.format("travellers.gui.button.save");
		String revert = I18n.format("travellers.gui.button.revert");
		
		String duplicate = I18n.format("travellers.gui.button.duplicate");
		String unload = I18n.format("travellers.gui.button.unload");
		
		fontRenderer.drawString(I18n.format("travellers.gui.shipyard.code"), 10, 11, 4210752);
		//fontRenderer.drawString(shipyard_code, 12, 24, 4210752);
		fontRenderer.drawString(show, 143 - getStringWidthInPixels(show)/2, 24, 4210752);
		
		fontRenderer.drawString(I18n.format("travellers.gui.shipyard.active_schematic"), 10, 38, 4210752);
		//fontRenderer.drawString("Dinghy", 12, 51, 4210752);
		fontRenderer.drawString(rename, 143 - getStringWidthInPixels(rename)/2, 51, 4210752);
		fontRenderer.drawString(I18n.format("travellers.gui.shipyard.owner"), 12, 64, 4210752);
		fontRenderer.drawString(frame_owner, 167 - getStringWidthInPixels(frame_owner), 64, 4210752);
		
		fontRenderer.drawString(I18n.format("travellers.gui.shipyard.schematics_list"), 10, 81, 4210752);
		fontRenderer.drawString(ship_1, 12, 94, 4210752);
		fontRenderer.drawString(ship_2, 12, 107, 4210752);
		fontRenderer.drawString(ship_3, 12, 120, 4210752);
		fontRenderer.drawString(ship_4, 12, 133, 4210752);
		fontRenderer.drawString(ship_5, 12, 146, 4210752);
		
		fontRenderer.drawString(guidance, 313 - getStringWidthInPixels(guidance)/2, 12, 4210752);
		fontRenderer.drawString(edit, 219 - getStringWidthInPixels(edit)/2, 29, 4210752);
		fontRenderer.drawString(save, 313 - getStringWidthInPixels(save)/2, 29, 4210752);
		fontRenderer.drawString(revert, 407 - getStringWidthInPixels(revert)/2, 29, 4210752);
		
		fontRenderer.drawString(duplicate, 257 - getStringWidthInPixels(duplicate)/2, 231, 4210752);
		fontRenderer.drawString(unload, 369 - getStringWidthInPixels(unload)/2, 231, 4210752);
		
		fontRenderer.drawString(I18n.format("travellers.gui.shipyard.beams"), 207, 68, 4210752);
		fontRenderer.drawString(I18n.format("travellers.gui.shipyard.decks"), 362, 68, 4210752);
		fontRenderer.drawString(beams_cost, 245 - getStringWidthInPixels(beams_cost)/2, 83, 4210752);
		fontRenderer.drawString(decks_cost, 399 - getStringWidthInPixels(decks_cost)/2, 83, 4210752);
		
		fontRenderer.drawString(craft_time, 313 - getStringWidthInPixels(craft_time)/2, 74, 4210752);
		fontRenderer.drawString(frame_weight, 313 - getStringWidthInPixels(frame_weight)/2, 175, 4210752);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		mc.getTextureManager().bindTexture(gui_texture_a);
        int left = (width - xSize) / 2;
        int top = (height - ySize) / 2;
        drawTexturedModalRect(left, top, 0, 0, 207, ySize);
        
        mc.getTextureManager().bindTexture(gui_texture_b);
        drawTexturedModalRect(left+207, top, 0, 0, 256, ySize);
        
        drawMetalIcon(left+208, top+79, true);
        drawMaterialIcon(left+362, top+79, true);
	}

	public int getStringWidthInPixels(String str) {
		int length = 0;
		
		for (int i = 0; i < str.length(); i++) {
			length += fontRenderer.getCharWidth(str.charAt(i));
		}
		
		return length;
	}
	
	private void drawMaterialIcon(int left, int top, boolean overlay) {
		int offset = overlay ? 16 : 0;
		mc.getTextureManager().bindTexture(icons);
        drawTexturedModalRect(left, top, 0, 0+offset, 16, 16);
	}
	
	private void drawMetalIcon(int left, int top, boolean overlay) {
		int offset = overlay ? 16 : 0;
		mc.getTextureManager().bindTexture(icons);
        drawTexturedModalRect(left, top, 16, 0+offset, 16, 16);
	}
	
	private void drawWoodIcon(int left, int top, boolean overlay) {
		int offset = overlay ? 16 : 0;
		mc.getTextureManager().bindTexture(icons);
        drawTexturedModalRect(left, top, 32, 0+offset, 16, 16);
	}
}
