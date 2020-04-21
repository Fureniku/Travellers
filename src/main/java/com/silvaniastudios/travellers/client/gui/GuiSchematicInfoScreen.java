package com.silvaniastudios.travellers.client.gui;

import java.io.IOException;
import java.util.ArrayList;

import com.silvaniastudios.travellers.PacketHandler;
import com.silvaniastudios.travellers.Travellers;
import com.silvaniastudios.travellers.capability.playerData.IPlayerData;
import com.silvaniastudios.travellers.capability.playerData.PlayerDataProvider;
import com.silvaniastudios.travellers.capability.schematicData.ISchematicData;
import com.silvaniastudios.travellers.capability.schematicData.SchematicDataProvider;
import com.silvaniastudios.travellers.data.SchematicFixedData.SchematicCraftingSlot;
import com.silvaniastudios.travellers.data.SchematicFixedData.SchematicStatisticSlot;
import com.silvaniastudios.travellers.items.schematic.ItemSchematic;
import com.silvaniastudios.travellers.items.schematic.SchematicTypeEnum;
import com.silvaniastudios.travellers.network.LearnSchematicMessage;
import com.silvaniastudios.travellers.network.SalvageSchematicMessage;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;


/**
 * 
 * @author jamesm2w
 *
 */
public class GuiSchematicInfoScreen extends GuiScreen {

	private static final ResourceLocation TEXTURE = new ResourceLocation(Travellers.MODID,
			"textures/gui/schematic.png");
	private static final ResourceLocation icons = new ResourceLocation(Travellers.MODID, "textures/gui/icons.png");

	private int screenLeft;
	private int screenTop;

	private int xSize;
	private int ySize;

	private int statSectionXSize = 114;

	private int infoSectionXSize = 150;
	private int infoSectionYSize = 100;

	private int infoPaddingLeft = 8;
	private int infoPaddingRight = 8;
	private int infoPaddingTop = 12;
	private int infoPaddingBottom = 8;

	private int buttonHeight = 20;
	private int buttonWidth = 60;

	private int itemStackWidth = 16;
	private int itemStackMarginLeft = 5;

	private int infoMarginRight = 5;

	private int descriptionPaddingTop = 12;
	private int buttonPaddingTop = 12;

	private ISchematicData schematic;
	private ItemStack stack;

	private Minecraft mc;
	private EntityPlayer player;

	private String titleText;
	private String descriptionText;

	private int titleHeight;
	private int descriptionHeight;

	private boolean itemRemoved = false;

	public GuiSchematicInfoScreen(ItemStack stack) {
		if (stack.getItem() instanceof ItemSchematic
				&& stack.hasCapability(SchematicDataProvider.SCHEMATIC_DATA, null)) {

			this.stack = stack;
			this.schematic = stack.getCapability(SchematicDataProvider.SCHEMATIC_DATA, null);

			this.player = Minecraft.getMinecraft().player;
			this.mc = Minecraft.getMinecraft();
		}

	}

	@Override
	public void initGui() {
		super.initGui();

		this.titleText = stack.getDisplayName();
		if (schematic.getType() != SchematicTypeEnum.FIXED) {
			this.descriptionText = I18n
					.format("travellers.tooltip.ship_parts." + schematic.getType().name.toLowerCase());
		} else {
			this.descriptionText = I18n.format("travellers.tooltip." + schematic.getName().toLowerCase());
		}

		this.titleHeight = fontRenderer.getWordWrappedHeight(this.titleText,
				this.infoSectionXSize - infoPaddingLeft - infoPaddingRight - itemStackWidth - itemStackMarginLeft);
		this.descriptionHeight = fontRenderer.getWordWrappedHeight(this.descriptionText,
				this.infoSectionXSize - infoPaddingLeft - infoPaddingRight);

		int height = infoPaddingTop + titleHeight + descriptionPaddingTop + descriptionHeight + buttonPaddingTop
				+ buttonHeight + infoPaddingBottom;
		if (height > this.infoSectionYSize) {
			this.infoSectionYSize = infoPaddingTop + titleHeight + descriptionPaddingTop + descriptionHeight
					+ buttonPaddingTop + buttonHeight + infoPaddingBottom;
		}

		this.xSize = this.infoSectionXSize;
		this.ySize = this.infoSectionYSize;

		this.screenLeft = (this.width - this.xSize) / 2;
		this.screenTop = (this.height - this.ySize) / 2;

		this.buttonList.add(new GuiButton(0, screenLeft + infoPaddingLeft,
				screenTop + infoSectionYSize - buttonHeight - infoPaddingBottom, buttonWidth, buttonHeight, "Learn"));
		this.buttonList.add(new GuiButton(1, screenLeft + infoSectionXSize - buttonWidth - infoPaddingRight,
				screenTop + infoSectionYSize - infoPaddingBottom - buttonHeight, buttonWidth, buttonHeight, "Salvage"));
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

		// Draw info section
		this.drawTexturedModalRect(screenLeft, screenTop, 0, 0, infoSectionXSize, 5);
		for (int i = 0; i < infoSectionYSize - 10; i++) {
			this.drawTexturedModalRect(screenLeft, screenTop + 5 + i, 0, 5, infoSectionXSize, 1);
		}
		this.drawTexturedModalRect(screenLeft, screenTop + infoSectionYSize - 5, 0, 95, infoSectionXSize, 5);

		// Draw stat section
		if (schematic.getStats().size() > 0) {
			this.mc.renderEngine.bindTexture(TEXTURE);
			// Draw main section
			this.drawTexturedModalRect(screenLeft + infoSectionXSize + infoMarginRight, screenTop, 0, 105,
					statSectionXSize, 5);

			// Draw progress bars
			int index = 1;
			for (SchematicStatisticSlot stat : schematic.getStats()) {

				this.drawTexturedModalRect(screenLeft + infoSectionXSize + infoMarginRight,
						screenTop - 14 + ((index) * 19), 0, 110, 114, 19);

				this.drawTexturedModalRect(screenLeft + infoSectionXSize + infoMarginRight + 7,
						screenTop + (index * 19), 0, 100, (int) stat.amount, 5);
				index++;
			}

			this.drawTexturedModalRect(screenLeft + infoSectionXSize + infoMarginRight, screenTop - 14 + ((index) * 19),
					0, 205, 114, 8);
		}

		if (schematic.getCrafting().size() > 0) {
			this.drawTexturedModalRect(screenLeft - 5 - 70, screenTop, 114, 105, 70, 7);

			int index = 0;
			for (String slot : schematic.getSlotNames()) {

				SchematicCraftingSlot crafting = schematic.getCrafting().find(slot);
				if (crafting != null) {
					this.mc.renderEngine.bindTexture(TEXTURE);

					int left = screenLeft - 5 - 70;
					int top = screenTop + 7 + (index * 25);

					if (index > 0) {
						this.drawTexturedModalRect(left, top - 7, 114, 130, 70, 7);
					}

					this.drawTexturedModalRect(left, top, 114, 112, 70, 18);
					top += 1;
					left += 7;

					switch (crafting.type) {
					case "travellers.material.wood":
						this.drawWoodIcon(left, top, false);
						break;
					case "travellers.material.metal":
						this.drawMetalIcon(left, top, false);
						break;
					default:
						this.drawMaterialIcon(left, top, false);
						break;
					}

					top += 5;
					left += 19;

					index++;
				}

				this.mc.renderEngine.bindTexture(TEXTURE);

				this.drawTexturedModalRect(screenLeft - 5 - 70, screenTop + (index * 25), 114, 205, 70, 7);
			}
		}
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		this.drawDefaultBackground();
		this.drawBackground(0);
		super.drawScreen(mouseX, mouseY, partialTicks);

		int index = 0;
		for (String slot : schematic.getSlotNames()) {

			SchematicCraftingSlot crafting = schematic.getCrafting().find(slot);
			if (crafting != null) {
				fontRenderer.drawString(String.valueOf(crafting.amount), screenLeft - 5 - 70 + 26,
						screenTop + 7 + (index * 25) + 6, 0xFFFFFF);
			}
			index++;
		}

		itemRender.renderItemIntoGUI(stack, this.screenLeft + infoPaddingLeft, this.screenTop + infoPaddingTop);

		fontRenderer.drawSplitString(
				String.format("%s%s%s", schematic.getRarity().toMCRarity().rarityColor, this.titleText,
						TextFormatting.RESET),
				this.screenLeft + infoPaddingLeft + itemStackWidth + itemStackMarginLeft,
				this.screenTop + infoPaddingTop,
				this.infoSectionXSize - infoPaddingLeft - infoPaddingRight - itemStackWidth - itemStackMarginLeft,
				Integer.parseInt("FFFFFF", 16));

		fontRenderer.drawSplitString(this.descriptionText, this.screenLeft + infoPaddingLeft,
				this.screenTop + infoPaddingTop + titleHeight + descriptionPaddingTop,
				infoSectionXSize - infoPaddingLeft - infoPaddingRight, 5592405);

		if (schematic.getStats().size() > 0) {

			index = 0;
			for (SchematicStatisticSlot stat : schematic.getStats()) {
				fontRenderer.drawString(I18n.format(stat.name), screenLeft + infoSectionXSize + infoMarginRight + 7,
						screenTop + 8 + (19 * index), 5592405);

				int amountLength = fontRenderer.getStringWidth(String.valueOf((int) stat.amount));

				fontRenderer.drawString(String.valueOf((int) stat.amount),
						screenLeft + infoSectionXSize + infoMarginRight + statSectionXSize - 7 - amountLength,
						screenTop + 8 + (19 * index), 5592405);

				index++;
			}
		}

		index = 0;
		for (String slot : schematic.getSlotNames()) {

			SchematicCraftingSlot crafting = schematic.getCrafting().find(slot);
			if (crafting != null) {
				ArrayList<String> tooltips = new ArrayList<String>();
				tooltips.add(I18n.format(crafting.name));
				tooltips.add(I18n.format(crafting.type) + ": " + String.valueOf(crafting.amount));

				if ((screenLeft - 5 - 70 < mouseX && mouseX < screenLeft - 5 - 6)
						&& (screenTop + 7 + (index * 25) < mouseY && mouseY < screenTop + 7 + (index * 25) + 18)) {

					this.drawHoveringText(tooltips, mouseX, mouseY, fontRenderer);

				}
			}
			index++;
		}

	}

	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		super.actionPerformed(button);

		if (button.id == 0) { // Learn Schematic

			PacketHandler.INSTANCE.sendToServer(new LearnSchematicMessage(this.stack, player.getUniqueID()));
		} else if (button.id == 1) { // Salvage Schematic
			PacketHandler.INSTANCE.sendToServer(new SalvageSchematicMessage(this.stack, player.getUniqueID()));
		}

		player.setHeldItem(EnumHand.MAIN_HAND, ItemStack.EMPTY);
		player.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, ItemStack.EMPTY);
		// FMLCommonHandler.instance().getMinecraftServerInstance().addScheduledTask(()
		// -> {
		// FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList()
		// .getPlayerByUUID(player.getUniqueID()).setHeldItem(EnumHand.MAIN_HAND,
		// ItemStack.EMPTY);
		// });
		this.itemRemoved = true;
		player.closeScreen();
	}

	@Override
	public void onGuiClosed() {
		super.onGuiClosed();

		if (!this.itemRemoved) {
			player.setHeldItem(EnumHand.MAIN_HAND, stack);

			// FMLCommonHandler.instance().getMinecraftServerInstance().addScheduledTask(()
			// -> {
			// FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList()
			// .getPlayerByUUID(player.getUniqueID()).setHeldItem(EnumHand.MAIN_HAND,
			// stack);
			// });
		}
	}

	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}

	private void drawMaterialIcon(int left, int top, boolean overlay) {
		int offset = overlay ? 16 : 0;
		mc.getTextureManager().bindTexture(icons);
		drawTexturedModalRect(left, top, 0, 0 + offset, 16, 16);
	}

	private void drawMetalIcon(int left, int top, boolean overlay) {
		int offset = overlay ? 16 : 0;
		mc.getTextureManager().bindTexture(icons);
		drawTexturedModalRect(left, top, 16, 0 + offset, 16, 16);
	}

	private void drawWoodIcon(int left, int top, boolean overlay) {
		int offset = overlay ? 16 : 0;
		mc.getTextureManager().bindTexture(icons);
		drawTexturedModalRect(left, top, 32, 0 + offset, 16, 16);
	}

}