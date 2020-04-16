package com.silvaniastudios.travellers.client.gui;

import java.io.IOException;
import java.util.ArrayList;

import com.silvaniastudios.travellers.PacketHandler;
import com.silvaniastudios.travellers.Travellers;
import com.silvaniastudios.travellers.capability.playerData.IPlayerData;
import com.silvaniastudios.travellers.capability.playerData.PlayerDataProvider;
import com.silvaniastudios.travellers.capability.schematicData.ISchematicData;
import com.silvaniastudios.travellers.capability.schematicData.SchematicDataProvider;
import com.silvaniastudios.travellers.data.SchematicFixedData.SchematicStatisticSlot;
import com.silvaniastudios.travellers.items.schematic.ItemSchematic;
import com.silvaniastudios.travellers.items.schematic.SchematicTypeEnum;
import com.silvaniastudios.travellers.network.KnowledgeIncreaseMessage;
import com.silvaniastudios.travellers.network.LearnSchematicMessage;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class GuiSchematicInfoScreen extends GuiScreen {

	private int guiLeft;
	private int guiTop;

	private int xSize;
	private int ySize;

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

			this.bottomSection = 64;
			this.middleSection = 31;

			this.descText = I18n.format(this.schem.getName());

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

		this.drawTexturedModalRect(this.guiLeft, this.guiTop + bottomSectionStart, 0, bottomSection, 150,
				100 - bottomSection);

		if (this.schem.getStats().size() > 0) {
			this.drawTexturedModalRect(this.guiLeft + 160, this.guiTop, 150, 0, 106, 108);

			String[] statNames = SchematicTypeEnum.getStatNames(this.schem.getType());
			ArrayList<SchematicStatisticSlot> stats = this.schem.getStats();
			for (int i = 0; i < statNames.length; i++) {
				this.mc.renderEngine.bindTexture(TEXTURE);
				this.drawTexturedModalRect(this.guiLeft + xSize + 13, this.guiTop + 19 + (19 * i), 0, 100, (int)stats.get(i).amount,
						104);
			}

		}
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		this.drawDefaultBackground();
		this.drawBackground(0);
		super.drawScreen(mouseX, mouseY, partialTicks);

		itemRender.renderItemIntoGUI(stack, this.guiLeft + 8, this.guiTop + 12);

		this.fontRenderer.drawSplitString(stack.getDisplayName(), this.guiLeft + 38, this.guiTop + 12, 104, 5592405);

		this.fontRenderer.drawSplitString(I18n.format(this.schem.getName()), this.guiLeft + 8, this.guiTop + 40,
				xSize - 16, 5592405);

		if (this.schem.getStats().size() > 0) {

			String[] statNames = SchematicTypeEnum.getStatNames(this.schem.getType());

			ArrayList<SchematicStatisticSlot> stats = this.schem.getStats();
			for (int i = 0; i < statNames.length; i++) {
				String stat = statNames[i];

				this.fontRenderer.drawString(I18n.format(stat), this.guiLeft + xSize + 14, this.guiTop + 8 + (19 * i),
						5592405);

				int strLength = this.fontRenderer.getStringWidth(String.valueOf(stats.get(i)));
				this.fontRenderer.drawString(String.valueOf(stats.get(i)), this.guiLeft + xSize + 108 - strLength,
						this.guiTop + 8 + (19 * i), 5592405);

			}
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
		FMLCommonHandler.instance().getMinecraftServerInstance().addScheduledTask(() -> {
			FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList()
					.getPlayerByUUID(player.getUniqueID()).setHeldItem(EnumHand.MAIN_HAND, ItemStack.EMPTY);
		});
		this.itemRemoved = true;
		player.closeScreen();
	}

	@Override
	public void onGuiClosed() {
		super.onGuiClosed();

		if (!this.itemRemoved) {
			player.setHeldItem(EnumHand.MAIN_HAND, stack);
			FMLCommonHandler.instance().getMinecraftServerInstance().addScheduledTask(() -> {
				FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList()
						.getPlayerByUUID(player.getUniqueID()).setHeldItem(EnumHand.MAIN_HAND, stack);
			});
		}
	}

	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}

}
