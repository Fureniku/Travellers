/**
 * 
 */
package com.silvaniastudios.travellers.client.gui;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import com.mojang.authlib.GameProfile;
import com.silvaniastudios.travellers.Travellers;
import com.silvaniastudios.travellers.capability.playerData.PlayerDataProvider;
import com.silvaniastudios.travellers.entity.EntityScannerLine;

import net.minecraft.block.Block;
import net.minecraft.block.BlockSkull;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntitySkull;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

/**
 * @author jamesm2w
 */
public class GuiScannerInformation extends Gui {

	protected int width;
	protected int height;

	protected Minecraft minecraft;

	protected EntityScannerLine scanner;

	private static final ResourceLocation TEXTURE = new ResourceLocation(Travellers.MODID,
			"textures/gui/scanner_line.png");

	public GuiScannerInformation() {
		ScaledResolution scaled = new ScaledResolution(Minecraft.getMinecraft());
		this.width = scaled.getScaledWidth();
		this.height = scaled.getScaledHeight();
		this.minecraft = Minecraft.getMinecraft();

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

				Block targetBlock = minecraft.world.getBlockState(pos).getBlock();

				ItemStack pickStack = targetBlock.getPickBlock(minecraft.world.getBlockState(pos), null,
						minecraft.world, pos, minecraft.player);

				String targetBlockName = targetBlock.getLocalizedName();
				String pickStackName = pickStack.getDisplayName();

				String targetBlockDescr = I18n.format(targetBlock.getUnlocalizedName() + ".description");
				String pickStackDescr = I18n.format(pickStack.getUnlocalizedName() + ".description");

				String descr = targetBlockDescr;
				String name = targetBlockName;

				if (useItemStackBecauseOfSubBlocks(targetBlock)) {
					// Force ItemStack use
					name = pickStackName;
					descr = pickStackDescr;
				}

				if (targetBlockDescr.startsWith("tile.") && !pickStackDescr.startsWith("item.")) {
					descr = pickStackDescr;
				}

				if (targetBlockName.startsWith("tile.") && !pickStackName.startsWith("tile.")) {
					name = pickStackName;
				}

				if (targetBlock.getUnlocalizedName().contentEquals("tile.null")) {
					name = TextFormatting.OBFUSCATED + "NULL" + TextFormatting.RESET;
				}

				if (targetBlock instanceof BlockSkull) {
					TileEntity teSkull = minecraft.world.getTileEntity(pos);

					if (teSkull instanceof TileEntitySkull) {

						TileEntitySkull skull = (TileEntitySkull) teSkull;
						try {
							Field skullTypeField = skull.getClass().getDeclaredField("skullType");

							skullTypeField.setAccessible(true);

							int skullType = (int) skullTypeField.get(skull);

							if (skullType == 3) {

								Method playerProfileGet = skull.getClass().getDeclaredMethod("getPlayerProfile",
										(Class[]) null);
								playerProfileGet.setAccessible(true);

								GameProfile playerProfile = (GameProfile) playerProfileGet.invoke(skull,
										(Object[]) null);

								if (playerProfile != null) {
									name = String.format("%s's Head", playerProfile.getName());
									descr = String.format("A severed head belonging to %s", playerProfile.getName());
								}

							}

						} catch (Exception e) {
							e.printStackTrace();
						}

					}

				}

				minecraft.getTextureManager().bindTexture(TEXTURE);

				int rectLeft = width - 100;
				int rectTop = height - 113;

				// drawModalRectWithCustomSizedTexture(rectLeft, rectTop, 0, 0,
				// 100, 100, 100, 100);

				drawTexturedModalRect(rectLeft, rectTop, 0, 0, 100, 100);

				drawString(fontRendererIn, name, rectLeft + 5, rectTop + 5, Integer.parseInt("FFFFFF", 16));

				fontRendererIn.drawSplitString(descr, rectLeft + 5, rectTop + 18, 90, 5592405);

				int descrHeight = fontRendererIn.getWordWrappedHeight(descr, 90);

				List<String> tooltips = new ArrayList<String>();
				pickStack.getItem().addInformation(pickStack, Minecraft.getMinecraft().world, tooltips,
						ITooltipFlag.TooltipFlags.NORMAL);

				if (tooltips != null && !tooltips.isEmpty()) {
					int prevHeight = rectTop + 18 + descrHeight + 3;

					if (tooltips != null) {
						for (String tooltip : tooltips) {

							if (tooltip.contentEquals(name)) {
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

	private boolean useItemStackBecauseOfSubBlocks(Block block) {
		String name = block.getUnlocalizedName();

		String[] haveSubBlocks = new String[] { "tile.doublePlant", "tile.leaves", "tile.stainedGlass",
				"tile.thinStainedGlass", "tile.cloth", "tile.log", "tile.sandStone", "tile.redSandStone", "tile.dirt",
				"tile.stonebricksmooth", "tile.flower1", "tile.flower2", "tile.clayHardenedStained", "tile.wood",
				"tile.stone", "tile.sand", "tile.sponge", "tile.skull" };

		for (String entry : haveSubBlocks) {
			if (name.contentEquals(entry)) {
				return true;
			}
		}

		return false;
	}
}
