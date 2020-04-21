package com.silvaniastudios.travellers.client.gui;

import java.io.IOException;

import com.silvaniastudios.travellers.Travellers;
import com.silvaniastudios.travellers.items.ItemCodex;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;

public class GuiCodexPieceInformation extends GuiScreen {

	public static final ResourceLocation TEXTURE = new ResourceLocation(Travellers.MODID,
			"textures/gui/codex_information.png");

	private ItemCodex piece;

	private int xSize;
	private int ySize;

	private int screenLeft;
	private int screenTop;

	private String titleText;
	private String rarityText;

	private boolean shouldScroll = true;
	private GuiTextScrollPane scrollPane;

	private EntityPlayer player;
	
	private EnumRarity rarity;

	public GuiCodexPieceInformation(ItemCodex piece) {
		
		this.piece = piece;
		this.player = Minecraft.getMinecraft().player;
		
		if (piece.text == null) {
			player.closeScreen();
		}
		this.rarity = piece.getRarity(null);
		
		this.titleText = this.piece.getParentName() + " #" + String.valueOf(this.piece.getPieceIndex() + 1);
		this.rarityText = rarity.rarityColor + " [" + rarity.rarityName + "]" + TextFormatting.RESET;
	}

	@Override
	public void initGui() {
		super.initGui();
		
		if (this.piece.text == null) {
			player.closeScreen();
			return;
		}

		xSize = 256;

		int textHeight = fontRenderer.getWordWrappedHeight(this.piece.text, xSize - 10);
		int titleHeight = fontRenderer.getWordWrappedHeight(this.titleText + this.rarityText, xSize - 10);

		if (textHeight > 250) {
			shouldScroll = true;
			ySize = 45 + titleHeight + 250;
		} else {
			shouldScroll = false;
			ySize = 45 + titleHeight + textHeight;
		}

		screenLeft = (this.width - xSize) / 2;
		screenTop = (this.height - ySize) / 2;

		scrollPane = new GuiTextScrollPane(this.piece.text, screenLeft + 7, screenTop + titleHeight + 5, xSize - 14,
				250);

		this.buttonList.add(new GuiButton(0, screenLeft + (xSize - 60) / 2, screenTop + ySize - 25, 60, 20,
				I18n.format("travellers.gui.button.ok")));
	}

	@Override
	public void updateScreen() {
		super.updateScreen();
		this.buttonList.get(0).enabled = true;
	}

	@Override
	public void drawBackground(int tint) {
		this.mc.renderEngine.bindTexture(TEXTURE);

		this.drawTexturedModalRect(screenLeft, screenTop, 0, 0, xSize, 5);
		for (int i = 0; i < ySize - 30; i++) {
			this.drawTexturedModalRect(screenLeft, screenTop + 5 + i, 0, 5, xSize, 1);
		}
		this.drawTexturedModalRect(screenLeft, screenTop + ySize - 30, 0, 70, xSize, 30);
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		if (this.piece.text == null) {
			player.closeScreen();
			return;
		}

		
		this.drawDefaultBackground();
		this.drawBackground(0);

		super.drawScreen(mouseX, mouseY, partialTicks);
		int titleHeight = fontRenderer.getWordWrappedHeight(titleText, xSize - 10);
		fontRenderer.drawSplitString(titleText, screenLeft + 6, screenTop + 6, xSize - 10, 0x0);
		fontRenderer.drawSplitString(titleText + rarityText, screenLeft + 5, screenTop + 5, xSize - 10, 0xFFFFFF);

		if (shouldScroll) {
			scrollPane.updateScrollPos();
			scrollPane.draw();
		} else {
			fontRenderer.drawSplitString(this.piece.text, this.screenLeft + 7, this.screenTop + titleHeight + 10,
					xSize - 14, 5592405);
		}
	}

	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		super.actionPerformed(button);

		if (button.id == 0) {
			player.closeScreen();
		}

	}

	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		super.mouseClicked(mouseX, mouseY, mouseButton);

		if (mouseButton == 0 && shouldScroll) {
			scrollPane.onMouseDown(mouseX, mouseY);
		}
	}

	@Override
	protected void mouseReleased(int mouseX, int mouseY, int state) {
		super.mouseReleased(mouseX, mouseY, state);

		if (shouldScroll) {
			scrollPane.onMouseUp(mouseX, mouseY);
		}
	}

	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}

}
