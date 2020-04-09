/**
 * 
 */
package com.silvaniastudios.travellers.client.gui;

import java.util.ArrayList;
import java.util.List;

import com.silvaniastudios.travellers.Travellers;
import com.silvaniastudios.travellers.capability.playerData.PlayerDataProvider;
import com.silvaniastudios.travellers.entity.EntityScannerLine;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * @author jamesm2w
 */
public class GuiScannerInformation extends Gui {

	protected int width;
	protected int height;

	protected EntityScannerLine scanner;

	private static final ResourceLocation TEXTURE = new ResourceLocation(Travellers.MODID,
			"textures/gui/scanner_line.png");

	public GuiScannerInformation() {
		ScaledResolution scaled = new ScaledResolution(Minecraft.getMinecraft());
		this.width = scaled.getScaledWidth();
		this.height = scaled.getScaledHeight();

	}

	public void draw() {
		ScaledResolution scaled = new ScaledResolution(Minecraft.getMinecraft());
		this.width = scaled.getScaledWidth();
		this.height = scaled.getScaledHeight();

		FontRenderer fontRendererIn = Minecraft.getMinecraft().fontRenderer;

		if (Minecraft.getMinecraft().player.hasCapability(PlayerDataProvider.PLAYER_DATA, null)) {
			scanner = Minecraft.getMinecraft().player.getCapability(PlayerDataProvider.PLAYER_DATA, null)
					.getScanningEntity();

			if (scanner != null) {

				BlockPos pos = new BlockPos(scanner.posX, scanner.posY, scanner.posZ);

				if (isBlockAir(pos)) {

					if (Math.floor(scanner.posX) == scanner.posX) {
						pos = pos.east();

						if (isBlockAir(pos)) {
							pos = pos.west(2);
						}

						// System.out.println("x(+): " + scanner.posX + " y: " +
						// scanner.posY + " z: " + scanner.posZ);
					} else if (Math.floor(scanner.posY) == scanner.posY) {
						pos = pos.up();

						if (isBlockAir(pos)) {
							pos = pos.down(2);
						}

						// System.out.println("x: " + scanner.posX + " y(+): " +
						// scanner.posY + " z: " + scanner.posZ);
					} else if (Math.floor(scanner.posZ) == scanner.posZ) {
						pos = pos.south();

						if (isBlockAir(pos)) {
							pos = pos.north(2);
						}

						// System.out.println("x: " + scanner.posX + " y: " +
						// scanner.posY + " z(+): " + scanner.posZ);
					}
				} else {
					// System.out.println("x: " + scanner.posX + " y: " +
					// scanner.posY + " z: " + scanner.posZ);
				}

				String blockName = Minecraft.getMinecraft().world.getBlockState(pos).getBlock().getLocalizedName();

				String blockDescr = I18n
						.format(Minecraft.getMinecraft().world.getBlockState(pos).getBlock().getUnlocalizedName()
								+ ".description");

				Minecraft.getMinecraft().getTextureManager().bindTexture(TEXTURE);

				int rectLeft = width - 100;
				int rectTop = height - 113;

				// drawModalRectWithCustomSizedTexture(rectLeft, rectTop, 0, 0,
				// 100, 100, 100, 100);

				drawTexturedModalRect(rectLeft, rectTop, 0, 0, 100, 100);

				drawString(fontRendererIn, blockName, rectLeft + 5, rectTop + 5, Integer.parseInt("FFFFFF", 16));

				fontRendererIn.drawSplitString(blockDescr, rectLeft + 5, rectTop + 18, 90, 5592405);

				int descrHeight = fontRendererIn.getWordWrappedHeight(blockDescr, 90);

				// @SuppressWarnings("deprecation")
				// ItemStack pickStack =
				// Minecraft.getMinecraft().world.getBlockState(pos).getBlock().getItem(
				// Minecraft.getMinecraft().world, pos,
				// Minecraft.getMinecraft().world.getBlockState(pos));

				ItemStack pickStack = Minecraft.getMinecraft().world.getBlockState(pos).getBlock().getPickBlock(
						Minecraft.getMinecraft().world.getBlockState(pos), null, Minecraft.getMinecraft().world, pos,
						Minecraft.getMinecraft().player);

				List<String> tooltips = new ArrayList<String>();
				pickStack.getItem().addInformation(pickStack, Minecraft.getMinecraft().world, tooltips,
						ITooltipFlag.TooltipFlags.NORMAL);

				if (tooltips != null && !tooltips.isEmpty()) {
					int prevHeight = rectTop + 18 + descrHeight + 3;

					if (tooltips != null) {
						for (String tooltip : tooltips) {

							if (tooltip.contentEquals(blockName)) {
								continue;
							}

							int tooltipEntryHeight = fontRendererIn.getWordWrappedHeight(tooltip, 90);

							fontRendererIn.drawSplitString(tooltip, rectLeft + 5, prevHeight, 90, 5592405);

							prevHeight = prevHeight + tooltipEntryHeight + 2;
						}
					}
				}

			} else {
				return;
			}
		} else {
			return;
		}
	}

	private boolean isBlockAir(BlockPos pos) {
		World world = Minecraft.getMinecraft().world;

		return world.getBlockState(pos).getBlock() == Blocks.AIR;
	}

}
