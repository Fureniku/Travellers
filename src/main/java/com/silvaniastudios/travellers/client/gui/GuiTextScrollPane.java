package com.silvaniastudios.travellers.client.gui;

import org.lwjgl.input.Mouse;

import com.silvaniastudios.travellers.Travellers;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

/**
 * 
 * @author jamesm2w
 */
public class GuiTextScrollPane extends Gui {

	/**
	 * What pixel the top of the element is on
	 */
	protected int top;
	/**
	 * What pixel the left of the element is on
	 */
	protected int left;

	/**
	 * Scroll Element Width
	 */
	protected int width;

	/**
	 * Scroll Element Height
	 */
	protected int height;

	/**
	 * Width of the scroll bar
	 */
	protected int scrollBarWidth = 5;

	/**
	 * Height of the scroll bar
	 */
	protected double scrollBarHeight = 1;

	/**
	 * Current position of the scroll bar
	 */
	protected int scrollPosition;

	/**
	 * Text Content to show
	 */
	protected String text;

	/**
	 * Height of the text element
	 */
	protected int textHeight;

	/**
	 * ResourceLocation for the Scroll bar
	 */
	public static final ResourceLocation SCROLL_BAR = new ResourceLocation(Travellers.MODID,
			"textures/gui/codex_information.png");

	/**
	 * Minecraft instance
	 */
	Minecraft mc = Minecraft.getMinecraft();

	private int deltaScroll = 0;
	private int scrollTop = 0;
	private int scrollBottom = 0;
	private double scrollBarPosition = 0;
	private boolean draggingScroll = false;

	public GuiTextScrollPane(String text, int x, int y, int width, int height) {
		this.text = text;
		this.left = x;
		this.top = y;
		this.width = width;
		this.height = height;

		this.textHeight = mc.fontRenderer.getWordWrappedHeight(this.text, this.width - this.scrollBarWidth);
		this.scrollBarHeight = (double) height / (double) textHeight * (double) height;

		this.scrollTop = 0;
		this.scrollBottom = height - textHeight;

		if (scrollBottom > scrollTop) {
			scrollBottom = scrollTop;
		}
	}

	public int updateScrollPos() {
		int dWheel = Mouse.getDWheel();
		if (draggingScroll) {
			scrollPosition += Mouse.getDY();
		} else {
			deltaScroll = dWheel / 8;
			scrollPosition += deltaScroll;
		}
		
		if (scrollPosition < scrollBottom) {
			scrollPosition = scrollBottom;
		} else if (scrollPosition > scrollTop) {
			scrollPosition = scrollTop;
		}

		scrollBarPosition = ((double) scrollPosition / (double) scrollBottom) * (height - scrollBarHeight);
		return dWheel;
	}

	/**
	 * Draws the Scroll pane to the window.
	 */
	public void draw() {
		// Update the variables with the scroll bar position
		//updateScrollPos();

		// Set up GlStateManager for drawing
		GlStateManager.pushMatrix();
		GlStateManager.translate((float) left, (float) top + 5, -400.0F);
		GlStateManager.enableDepth();

		// Render background rectangle
		GlStateManager.depthFunc(518);
		drawRect(0, 0, width, height, 0xffC6C6C6);

		// Render text, translates in the y by the current scroll position
		GlStateManager.depthFunc(515);
		GlStateManager.translate(0F, (float) scrollPosition, 0F);
		
		this.drawInside();

		// Render scroll bar. First undoes the scroll position translation, then
		// moves to the scroll bar position
		GlStateManager.depthFunc(514);
		GlStateManager.translate(0F, -(float) scrollPosition, 0F);
		GlStateManager.translate(0F, (float) scrollBarPosition, 0F);

		mc.renderEngine.bindTexture(SCROLL_BAR);
		drawTexturedModalRect(width - scrollBarWidth, 0, 0, 101, scrollBarWidth, 2);
		for (int i = 0; i < (int) scrollBarHeight - 4; i++) {
			drawTexturedModalRect(width - scrollBarWidth, i + 2, 0, 102, scrollBarWidth, 1);
		}
		drawTexturedModalRect(width - scrollBarWidth, (int) scrollBarHeight - 2, 0, 107, scrollBarWidth, 2);

		// Finish and reset GlStateManager
		GlStateManager.popMatrix();
		GlStateManager.depthFunc(515);
		GlStateManager.disableDepth();
	}
	
	public void drawInside () {
		mc.fontRenderer.drawSplitString(text, 0, 0, width - scrollBarWidth, 5592405);
	}

	public void onMouseDown(int mouseX, int mouseY) {
		mouseX -= left;
		mouseY -= top;

		if (!draggingScroll) {
			if ((width - 5) < mouseX && mouseX < width && (int) scrollBarPosition < mouseY
					&& mouseY < (int) scrollBarPosition + scrollBarHeight) {
				draggingScroll = true;
			}
		}
	}

	public void onMouseUp(int mouseX, int mouseY) {
		this.draggingScroll = false;
	}

}
