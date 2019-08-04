package com.silvaniastudios.travellers.client.gui;

import java.io.IOException;

import com.silvaniastudios.travellers.PacketHandler;
import com.silvaniastudios.travellers.Travellers;
import com.silvaniastudios.travellers.capability.playerData.IPlayerData;
import com.silvaniastudios.travellers.capability.playerData.PlayerDataProvider;
import com.silvaniastudios.travellers.capability.schematicData.ISchematicData;
import com.silvaniastudios.travellers.capability.schematicData.SchematicDataProvider;
import com.silvaniastudios.travellers.network.KnowledgeIncreaseMessage;
import com.silvaniastudios.travellers.network.LearnSchematicMessage;
import com.silvaniastudios.travellers.schematic.ItemSchematic;
import com.silvaniastudios.travellers.schematic.SchematicTypeEnum;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;

public class GuiSchematicInfoScreen extends GuiScreen {

	private int guiLeft;
	private int guiTop;

	private int xSize;
	private int ySize;

	private int xSize2;
	private int ySize2;
	private int window2Left;
	private int window2Top;

	private int bottomSection;
	private int middleSection;
	private int middleRepeat;
	private int bottomSectionStart;
	private String descText;

	private boolean itemRemoved = false;

	private EntityPlayerSP player;

	private ISchematicData schem;
	private ItemStack stack;

	private static final ResourceLocation TEXTURE = new ResourceLocation(Travellers.MODID,
			"textures/gui/schematic.png");

	public GuiSchematicInfoScreen(ItemStack stack) {
		if (stack.getItem() instanceof ItemSchematic) {
			this.stack = stack;
			this.schem = stack.getCapability(SchematicDataProvider.SCHEMATIC_DATA, null);
			this.xSize = 150;
			this.ySize = 100;

			this.xSize2 = 100;
			this.ySize2 = 100;

			this.bottomSection = 64;
			this.middleSection = 31;

			this.descText = I18n.format("travellers.tooltip." + this.schem.getTooltip());

			this.player = Minecraft.getMinecraft().player;
			this.mc = Minecraft.getMinecraft();
		}

	}

	@Override
	public void initGui() {
		super.initGui();

		this.middleRepeat = Math.floorDiv(fontRenderer.getWordWrappedHeight(descText, xSize - 16),
				bottomSection - middleSection) + 1;

		this.ySize += (middleRepeat - 1) * (bottomSection - middleSection);

		this.bottomSectionStart = this.bottomSection + ((middleRepeat - 1) * (bottomSection - middleSection));

		this.guiLeft = (width - xSize) / 2;
		this.guiTop = (height - ySize) / 2;

		this.window2Left = guiLeft + 45;
		this.window2Top = this.guiTop;

		this.buttonList.add(new GuiButton(0, this.guiLeft + 8, this.guiTop + bottomSectionStart + 8, 60, 20, "Learn"));
		this.buttonList
				.add(new GuiButton(1, this.guiLeft + 82, this.guiTop + bottomSectionStart + 8, 60, 20, "Salvage"));
	}

	@Override
	public void updateScreen() {
		super.updateScreen();

		IPlayerData playerData = this.player.getCapability(PlayerDataProvider.PLAYER_DATA, null);

		if (playerData.hasLearntSchematic(this.stack)) {
			this.buttonList.get(0).enabled = false;
		}
	}

	@Override
	public void drawBackground(int tint) {

		this.mc.renderEngine.bindTexture(TEXTURE);

		this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, 150, middleSection);

		for (int i = 1; i <= this.middleRepeat; i++) {
			this.drawTexturedModalRect(this.guiLeft, this.guiTop + (middleSection * i), 0, middleSection, 150,
					bottomSection);
		}

		this.drawTexturedModalRect(this.guiLeft, this.guiTop + bottomSectionStart, 0, bottomSection, 150, 100);
		
		this.drawTexturedModalRect(this.guiLeft + 160, this.guiTop, 150, 0, 100, 100);
		/*if (stack.getCapability(SchematicDataProvider.SCHEMATIC_DATA, null).getType() != SchematicTypeEnum.FIXED) {
			
			this.mc.renderEngine.bindTexture(TEXTURE);
			this.drawTexturedModalRect(this.guiLeft + 150, this.guiTop, 150, 0, 216, 100);

		}*/
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		this.drawDefaultBackground();
		this.drawBackground(0);
		super.drawScreen(mouseX, mouseY, partialTicks);

		itemRender.renderItemIntoGUI(stack, this.guiLeft + 8, this.guiTop + 12);

		this.fontRenderer.drawString(stack.getDisplayName(), this.guiLeft + 38, this.guiTop + 12, 5592405);

		this.fontRenderer.drawSplitString(I18n.format("travellers.tooltip." + this.schem.getTooltip()),
				this.guiLeft + 8, this.guiTop + 40, xSize - 16, 5592405);

		if (stack.getCapability(SchematicDataProvider.SCHEMATIC_DATA, null).getType() != SchematicTypeEnum.FIXED) {

			String[] statNames = SchematicTypeEnum
					.getStatNames(stack.getCapability(SchematicDataProvider.SCHEMATIC_DATA, null).getType());

			for (int i = 0; i < statNames.length; i++) {
				String stat = statNames[i];

				this.fontRenderer.drawString(I18n.format(stat), this.guiLeft + xSize + 14, this.window2Top + 8 + (17 * i), 5592405);
				//System.out.println(this.fontRenderer.getStringWidth(I18n.format(stat)));
				
				int strLength = this.fontRenderer.getStringWidth(String.valueOf(100));
				
				this.fontRenderer.drawString(String.valueOf(100), this.guiLeft + xSize + 108 - strLength, this.window2Top + 8 + (17 * i), 5592405);
			}

			// System.out.println(this.fontRenderer.FONT_HEIGHT);
		}

	}

	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		super.actionPerformed(button);

		if (button.id == 0) { // Learn Schematic

			PacketHandler.INSTANCE.sendToServer(new LearnSchematicMessage(this.stack, player.getUniqueID()));
		} else if (button.id == 1) { // Salvage Schematic

			PacketHandler.INSTANCE.sendToServer(new KnowledgeIncreaseMessage(20, player.getUniqueID()));
		}

		player.setHeldItem(EnumHand.MAIN_HAND, ItemStack.EMPTY);
		player.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, ItemStack.EMPTY);
		this.itemRemoved = true;
		player.closeScreen();
	}

	@Override
	public void onGuiClosed() {
		super.onGuiClosed();

		if (!this.itemRemoved) {
			player.setHeldItem(EnumHand.MAIN_HAND, stack);
		}
	}

	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}

}
