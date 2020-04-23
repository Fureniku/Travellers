package com.silvaniastudios.travellers.client.gui;

import java.io.IOException;
import java.util.ArrayList;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import com.silvaniastudios.travellers.PacketHandler;
import com.silvaniastudios.travellers.Travellers;
import com.silvaniastudios.travellers.capability.playerData.IPlayerData;
import com.silvaniastudios.travellers.capability.playerData.PlayerDataProvider;
import com.silvaniastudios.travellers.data.KnowledgeTreeData.KnowledgeNode;
import com.silvaniastudios.travellers.data.KnowledgeTreeData.KnowledgeNodeUnlock;
import com.silvaniastudios.travellers.inventory.EmptyContainer;
import com.silvaniastudios.travellers.network.UseKnowledgeNode;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

/**
 * 
 * @author jamesm2w
 *
 */
public class GuiKnowledgeTree extends GuiContainer {

	public static final ResourceLocation TEXTURE_LEFT = new ResourceLocation(Travellers.MODID,
			"textures/gui/inventory_generic_left.png");

	public static final ResourceLocation TEXTURE_RIGHT = new ResourceLocation(Travellers.MODID,
			"textures/gui/inventory_generic_right.png");

	public static final ResourceLocation TREE_ICONS = new ResourceLocation(Travellers.MODID,
			"textures/gui/knowledge_tree.png");

	private int xSize = 206 + 256;
	private int ySize = 256;

	private boolean isDragging = false;
	private float offsetX = 0;
	private float offsetY = 0;
	private float deltaScroll = 1;

	private EntityPlayer player;
	private IPlayerData playerData;

	private NodeArea selectedNode;

	private ArrayList<NodeArea> nodeAreas = new ArrayList<NodeArea>();

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

		this.buttonList.add(
				new GuiButton(5, guiLeft + 298, guiTop + 90, 153, 20, I18n.format("travellers.gui.button.research")));

		if (player.hasCapability(PlayerDataProvider.PLAYER_DATA, null)) {
			playerData = player.getCapability(PlayerDataProvider.PLAYER_DATA, null);
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
		fontRenderer.drawStringWithShadow(I18n.format("travellers.gui.title.knowledge_tree"), 206 + 87, 8, 0xFFFFFF);

		fontRenderer.drawStringWithShadow(I18n.format("travellers.gui.title.belt"), 206 + 87, 129, 0xFFFFFF);
		fontRenderer.drawStringWithShadow(I18n.format("travellers.gui.title.selected_schematic"), 206 + 87, 22,
				0xFFFFFF);
		if (selectedNode != null) {
			String[] lines = new String[] { I18n.format("travellers.node." + selectedNode.node.name),
					I18n.format("travellers.gui.message.cost", selectedNode.node.cost) + " "
							+ ((playerData.getKnowledgeBalance() >= selectedNode.node.cost) ? ""
									: TextFormatting.RED + I18n.format("travellers.gui.message.too_expensive")
											+ TextFormatting.RESET),
					I18n.format("travellers.description." + selectedNode.node.name) };
			int heightOffset = 38;
			for (int i = 0; i < lines.length; i++) {
				fontRenderer.drawSplitString(lines[i], 298, heightOffset, 162, 0xFFFFFF);
				heightOffset += fontRenderer.getWordWrappedHeight(lines[i], 162);
			}
			this.buttonList.get(0).visible = true;

			if (selectedNode.unlocked && (playerData.getKnowledgeBalance() >= selectedNode.node.cost)) {
				this.buttonList.get(0).enabled = true;
			} else {
				this.buttonList.get(0).enabled = false;
			}

		} else {
			fontRenderer.drawSplitString(I18n.format("travellers.gui.message.default_tree"), 298, 38, 162,
					0xFFFFFF);

			this.buttonList.get(0).visible = false;
		}
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		
		if (player.hasCapability(PlayerDataProvider.PLAYER_DATA, null)) {
			playerData = player.getCapability(PlayerDataProvider.PLAYER_DATA, null);
			// System.out.println(playerData.getKnowledgeNodeUses().toString());
		}

		this.drawDefaultBackground();

		super.drawScreen(mouseX, mouseY, partialTicks);
		

		int dWheel = Mouse.getDWheel();

		boolean isOverKnowledgeTree = guiLeft + 8 < mouseX && mouseX < guiLeft + 281 + 8 && guiTop + 8 < mouseY
				&& mouseY < guiTop + 8 + 240;

		if (isOverKnowledgeTree) {
			this.deltaScroll += dWheel / 120F;
		}

		drawInnerTree();

		if (isOverKnowledgeTree) {
			for (NodeArea area : nodeAreas) {
				// drawRect((int)area.x, (int)area.y, (int)(area.x +
				// area.width), (int)(area.y + area.width), 0x7700ff00);
				if (area.isOverArea(mouseX, mouseY)) {
					boolean maxedOut = playerData.getKnowledgeNodeUsage(area.node.name) == area.node.maxUses
							&& area.node.infiniteRoll != 1;

					ArrayList<String> lines = new ArrayList<String>();
					lines.add(I18n.format("travellers.node." + area.node.name));

					if (!maxedOut) {
						
						if (!area.available) {
							lines.add(TextFormatting.RED + I18n.format("Locked") + TextFormatting.RESET);
						}
						
						if (playerData.getKnowledgeBalance() >= area.node.cost) {
							lines.add(TextFormatting.GREEN + I18n.format("travellers.gui.message.cost", area.node.cost)
									+ TextFormatting.RESET);
						} else {
							lines.add(I18n.format("travellers.gui.message.cost", area.node.cost) + " " + TextFormatting.RED
									+ I18n.format("travellers.gui.message.too_expensive") + TextFormatting.RESET);
						}
					} else {
						lines.add(TextFormatting.GREEN + I18n.format("Researched") + TextFormatting.RESET);
					}

					if (Mouse.isButtonDown(0)) {
						this.selectedNode = area;
					}

					drawHoveringText(lines, mouseX, mouseY);
				}
			}
		}
		nodeAreas = new ArrayList<NodeArea>();
		
		this.renderHoveredToolTip(mouseX, mouseY);
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
	protected void actionPerformed(GuiButton button) throws IOException {
		if (button.id == 5) {
			PacketHandler.INSTANCE.sendToServer(new UseKnowledgeNode(this.selectedNode.node.name, this.player));
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

		int contentWidth = 300;
		int contentHeight = 300;

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
		// Render Knowledge Count
		GlStateManager.translate(0F, 0F, -1F);
		String knowledge = I18n.format("travellers.gui.message.knowledge", playerData.getKnowledgeBalance());
		drawRect(0, 0, fontRenderer.getStringWidth(knowledge) + 4, fontRenderer.FONT_HEIGHT + 4, 0xff545454);
		fontRenderer.drawString(knowledge, 2, 2, 0xffffff);
		// Draw scale indicator
		GlStateManager.translate(viewportWidth - 40, viewportHeight - 40, 0F);
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
		GlStateManager.translate((viewportWidth) / 2 - 15, (viewportHeight) / 2 - 15, 0F);
		GlStateManager.translate((float) offsetX, (float) offsetY, 0F);
		GlStateManager.scale((float) deltaScroll, (float) deltaScroll, 0F);
		drawNode(Travellers.KNOWLEDGE_TREE.root.name, true, 0);

		// Finish and reset GlStateManager
		GlStateManager.popMatrix();
		GlStateManager.depthFunc(GL11.GL_LEQUAL);
		GlStateManager.disableDepth();

		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
	}

	public void drawNode(String nodeKey, boolean unlocked, int distanceFromUnlocked) {
		GlStateManager.color(1F, 1F, 1F, 1F);
		KnowledgeNode node = Travellers.KNOWLEDGE_TREE.getNode(nodeKey);
		int uses = playerData.getKnowledgeNodeUsage(nodeKey);
		int knowledge = playerData.getKnowledgeBalance();

		if (node == null)
			return;

		for (KnowledgeNodeUnlock unlock : node.unlocks) {

			boolean unlockedSubNode = uses >= unlock.uses && unlocked;
			if (!unlocked)
				distanceFromUnlocked++;

			KnowledgeNode unlockNode = Travellers.KNOWLEDGE_TREE.getNode(unlock.name);
			if (unlockNode != null) {
				drawConnectingLine(node.position[0], node.position[1], unlockNode.position[0], unlockNode.position[1]);
				// System.out.printf("subname=%s uses=%s sub-unlock=%s\n",
				// unlockNode.name, String.valueOf(uses),
				// String.valueOf(unlock.uses));
			}

			drawNode(unlock.name, unlockedSubNode, distanceFromUnlocked);

		}

		boolean invisible = distanceFromUnlocked > 0;
		boolean used = uses > 0;
		boolean canUse = knowledge >= node.cost;
		boolean maxedOut = uses == node.maxUses && node.infiniteRoll == 0;

		// System.out.printf("name=%s text=%s %s\n",
		// node.name, String.valueOf(node.texture[0]),
		// String.valueOf(node.texture[1]));

		mc.renderEngine.bindTexture(TREE_ICONS);

		if (!invisible) {

			mc.renderEngine.bindTexture(TREE_ICONS);

			if (used) {

				float percentUsed = (float) uses / (float) node.maxUses;

				if (percentUsed > 1) {
					percentUsed = 1;
				} else if (percentUsed < 0) {
					percentUsed = 0;
				}

				int proportion = (int) (1 + 28 * percentUsed);

				drawTexturedModalRect(node.position[0], node.position[1], 0, 90, 29, proportion);
			}

			if (!unlocked) {
				drawTexturedModalRect(node.position[0], node.position[1], 0, 0, 30, 30);
			}

			if (unlocked && !canUse) {
				drawTexturedModalRect(node.position[0], node.position[1], 0, 30, 30, 30);
			}

			if (unlocked && canUse && !maxedOut) {
				drawTexturedModalRect(node.position[0], node.position[1], 0, 60, 30, 30);
			}

			if (maxedOut) {
				drawTexturedModalRect(node.position[0], node.position[1], 0, 30, 30, 30);
			}

			drawTexturedModalRect(node.position[0], node.position[1], node.texture[0], node.texture[1], 30, 30);

			float boxLeft = guiLeft + 8 + ((281) / 2) - 15 + offsetX + (node.position[0]) * (deltaScroll);
			float boxTop = guiTop + 8 + ((240) / 2) - 15 + offsetY + (node.position[1]) * (deltaScroll);

			float width = 30 * deltaScroll;
			float height = 30 * deltaScroll;

			nodeAreas.add(new NodeArea(boxLeft, boxTop, width, height, node, (unlocked && canUse && !maxedOut), unlocked));

		} else {
			drawTexturedModalRect(node.position[0], node.position[1], 0, 120, 30, 30);
		}

	}

	public void drawConnectingLine(int x1, int y1, int x2, int y2) {

		x1 += 15;
		x2 += 15;
		y1 += 15;
		y2 += 15;

		Tessellator tessellator = Tessellator.getInstance();
		BufferBuilder bufferbuilder = tessellator.getBuffer();

		GlStateManager.pushMatrix();
		GlStateManager.disableTexture2D();

		bufferbuilder.begin(GL11.GL_LINES, DefaultVertexFormats.POSITION_COLOR);
		GlStateManager.glLineWidth((float) 4);
		bufferbuilder.pos(x1, y1, 0F).color(255, 255, 255, 255).endVertex();
		bufferbuilder.pos(x2, y2, 0F).color(255, 255, 255, 255).endVertex();

		tessellator.draw();
		GlStateManager.enableTexture2D();
		GlStateManager.color(1F, 1F, 1F, 1F);
		GlStateManager.popMatrix();
	}

	private static class NodeArea {
		float x;
		float y;
		float width;
		float height;
		KnowledgeNode node;
		
		boolean available = false;
		boolean unlocked = false;

		public NodeArea(float left, float top, float width, float height, KnowledgeNode node, boolean locked, boolean available) {
			this.x = left;
			this.y = top;
			this.width = width;
			this.height = height;
			this.node = node;
			this.unlocked = locked;
			this.available = available;
		}

		public boolean isOverArea(int mouseX, int mouseY) {
			if (x < mouseX && mouseX < x + width) {
				if (y < mouseY && mouseY < y + height) {
					return true;
				}
			}
			return false;
		}
	}
}
