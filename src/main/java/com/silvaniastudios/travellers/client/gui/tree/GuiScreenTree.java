package com.silvaniastudios.travellers.client.gui.tree;

import java.io.IOException;

import com.silvaniastudios.travellers.Travellers;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;

public class GuiScreenTree extends GuiScreen {
	
	public static final ResourceLocation WINDOW = new ResourceLocation(Travellers.MODID, "gui/tree/window");
	public static final ResourceLocation BACKGROUND = new ResourceLocation(Travellers.MODID, "gui/tree/bg");
	
	private GuiTreeContents contents;
	
	private int scrollMouseX;
	private int scrollMouseY;
	private boolean scrolling;
	
	public GuiScreenTree () {}
	

}
