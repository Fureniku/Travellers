package com.silvaniastudios.travellers.client.gui;

import java.io.IOException;
import java.util.ArrayList;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import com.silvaniastudios.travellers.Travellers;
import com.silvaniastudios.travellers.capability.playerData.IPlayerData;
import com.silvaniastudios.travellers.capability.playerData.PlayerDataProvider;
import com.silvaniastudios.travellers.inventory.EmptyContainer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class GuiKnowledgeTree extends GuiContainer {

	public static final ResourceLocation TEXTURE_LEFT = new ResourceLocation(Travellers.MODID,
			"textures/gui/inventory_generic_left.png");

	public static final ResourceLocation TEXTURE_RIGHT = new ResourceLocation(Travellers.MODID,
			"textures/gui/inventory_generic_right.png");

	public static final ResourceLocation SCROLL_BAR = new ResourceLocation(Travellers.MODID,
			"textures/gui/codex_information.png");

	private int xSize = 206 + 256;
	private int ySize = 256;

	private boolean isDragging = false;
	private float offsetX = 0;
	private float offsetY = 0;
	private float deltaScroll = 1;

	private EntityPlayer player;
	private IPlayerData playerData;
	public ArrayList<ItemStack> schematicList;

	private final Minecraft mc = Minecraft.getMinecraft();

	public GuiKnowledgeTree(EntityPlayer player, World world) {
		super(new EmptyContainer(player.inventory, !world.isRemote, player));
		this.player = player;
	}

	@Override
	public void initGui() {
		super.initGui();

		guiLeft = (width - xSize) / 2;
		guiTop = (height - ySize) / 2;

		if (player.hasCapability(PlayerDataProvider.PLAYER_DATA, null)) {
			playerData = player.getCapability(PlayerDataProvider.PLAYER_DATA, null);
			schematicList = playerData.getSchematicList();
		}
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		mc.renderEngine.bindTexture(TEXTURE_LEFT);
		drawTexturedModalRect(guiLeft, guiTop, 0, 0, 206, ySize);

		mc.renderEngine.bindTexture(TEXTURE_RIGHT);
		drawTexturedModalRect(guiLeft + 206, guiTop, 0, 0, 256, ySize);
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
		fontRenderer.drawStringWithShadow("Knowledge Tree", 206 + 87, 8, 0xFFFFFF);

		fontRenderer.drawStringWithShadow("Belt", 206 + 87, 129, 0xFFFFFF);
		if (playerData != null) {
			fontRenderer.drawStringWithShadow("Your Schematics", 206 + 87, 22, 0xFFFFFF);
		}
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {

		this.drawDefaultBackground();

		super.drawScreen(mouseX, mouseY, partialTicks);

		int dWheel = Mouse.getDWheel();

		if (guiLeft + 8 < mouseX && mouseX < guiLeft + 281 + 8) {
			if (guiTop + 8 < mouseY && mouseY < guiTop + 8 + 240) {
				this.deltaScroll += dWheel / 120F;
			}
		}
		
		drawInnerTree();
	}

	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		super.mouseClicked(mouseX, mouseY, mouseButton);

		if (mouseButton == 0) {

			if (guiLeft + 8 < mouseX && mouseX < guiLeft + 281 + 8) {
				if (guiTop + 8 < mouseY && mouseY < guiTop + 8 + 240) {
					this.isDragging = true;
				}
			}
		}
	}

	@Override
	protected void mouseReleased(int mouseX, int mouseY, int state) {
		super.mouseReleased(mouseX, mouseY, state);
		this.isDragging = false;
	}

	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		super.keyTyped(typedChar, keyCode);
		if (typedChar == 'k') {
			this.player.closeScreen();
		}
	}

	public void drawInnerTree() {

		if (deltaScroll < 1F) {
			deltaScroll = 1F;
		} else if (deltaScroll > 5F) {
			deltaScroll = 5F;
		}

		int viewportWidth = 281;
		int viewportHeight = 240;

		int contentWidth = 30;
		int contentHeight = 30;

		int contentLeftEdge = -(int) (contentWidth * deltaScroll) / 2;
		int contentTopEdge = -(int) (contentHeight * deltaScroll) / 2;

		int contentRightEdge = (int) (contentWidth * deltaScroll) / 2;
		int contentBottomEdge = (int) (contentHeight * deltaScroll) / 2;

		if (this.isDragging) {
			offsetX += Mouse.getDX() / (2);
			offsetY -= Mouse.getDY() / (2); // Mouse Y inverted?
		}

		if (offsetX < contentLeftEdge) {
			offsetX = contentLeftEdge;
		}
		if (offsetX > contentRightEdge) {
			offsetX = contentRightEdge;
		}

		if (offsetY < contentTopEdge) {
			offsetY = contentTopEdge;
		}
		if (offsetY > contentBottomEdge) {
			offsetY = contentBottomEdge;
		}

		// System.out.printf("X:%f Y:%f Scale: %f\n", offsetX, offsetY,
		// deltaScroll);
		GlStateManager.disableLighting();
		GlStateManager.pushMatrix();
		GlStateManager.translate((float) guiLeft + 8, (float) guiTop + 8, -500F);
		GlStateManager.enableDepth();

		// Render background rectangle
		GlStateManager.depthFunc(GL11.GL_GEQUAL);
		drawRect(0, 0, viewportWidth, viewportHeight, 0xffa0a0a0);

		// Draw scale indicator
		GlStateManager.translate(viewportWidth - 40, viewportHeight - 40, -1F);
		drawVerticalLine(37, 0, 37, 0xff000000);
		drawVerticalLine(0, 30, 37, 0xff000000);
		drawVerticalLine(5, 33, 37, 0xff000000);
		drawVerticalLine(10, 30, 37, 0xff000000);
		drawVerticalLine(15, 33, 37, 0xff000000);
		drawVerticalLine(20, 30, 37, 0xff000000);

		drawHorizontalLine(0, 37, 37, 0xff000000);
		drawHorizontalLine(30, 37, 0, 0xff000000);
		drawHorizontalLine(33, 37, 5, 0xff000000);
		drawHorizontalLine(30, 37, 10, 0xff000000);
		drawHorizontalLine(33, 37, 15, 0xff000000);
		drawHorizontalLine(30, 37, 20, 0xff000000);

		mc.renderEngine.bindTexture(TEXTURE_LEFT);

		drawTexturedModalRect(25, -3 + ((deltaScroll - 1) * 5), 210, 0, 4, 7); // Vertical

		drawTexturedModalRect(-3 + ((deltaScroll - 1) * 5), 25, 214, 0, 7, 4); // Horizontal

		GlStateManager.translate(40 - viewportWidth, 40 - viewportHeight, 1F);

		// Render the fore-ground of the knowledge tree
		GlStateManager.depthFunc(GL11.GL_EQUAL);
		GlStateManager.translate((viewportWidth) / 2, (viewportHeight) / 2, 0F);
		GlStateManager.translate((float) offsetX, (float) offsetY, 0F);
		// GlStateManager.scale((float)deltaScroll, (float)deltaScroll, 0F);
		drawRect(contentLeftEdge, contentTopEdge, contentRightEdge, contentBottomEdge, 0xFF00FF00);

		// Finish and reset GlStateManager
		GlStateManager.popMatrix();
		GlStateManager.depthFunc(GL11.GL_LEQUAL);
		GlStateManager.disableDepth();

		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
	}
}
