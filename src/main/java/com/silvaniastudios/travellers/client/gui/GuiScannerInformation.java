/**
 * 
 */
package com.silvaniastudios.travellers.client.gui;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import com.mojang.authlib.GameProfile;
import com.silvaniastudios.travellers.Travellers;
import com.silvaniastudios.travellers.capability.playerData.PlayerDataProvider;
import com.silvaniastudios.travellers.entity.EntityScannerLine;

import net.minecraft.block.Block;
import net.minecraft.block.BlockCrops;
import net.minecraft.block.BlockNetherWart;
import net.minecraft.block.BlockSkull;
import net.minecraft.block.BlockStem;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntitySkull;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;

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

				List<String> tooltips = new ArrayList<String>();
				ObjectInformation information;

				switch (scanner.attachmentType) {
				case BLOCK:
					BlockPos pos = scanner.getHitBlockPos();
					information = getBlockInformation(pos, tooltips);
					break;
				case ENTITY:
					Entity entity = scanner.getHitEntity();
					information = getEntityInformation(entity, tooltips);
					break;
				default:
					information = new ObjectInformation(I18n.format("entity.generic.name"),
							I18n.format("entity.generic.description"));
					break;
				}

				int informationHeight = 3 + fontRendererIn.getWordWrappedHeight(information.name, 113)
						+ fontRendererIn.getWordWrappedHeight(information.description, 113) + 3;
				
				if (tooltips != null && !tooltips.isEmpty()) {
					for (String tooltip : tooltips) {
						if (tooltip.contentEquals(information.name)) {
							continue;
						}
						informationHeight += fontRendererIn.getWordWrappedHeight(tooltip, 113) + 2;
					}
				}

				minecraft.getTextureManager().bindTexture(TEXTURE);

				int rectLeft = width - 124;
				int rectTop = height - informationHeight - 10 - 12;

				drawTexturedModalRect(rectLeft, rectTop, 0, 0, 124, 5);
				for (int i = 0; i < informationHeight; i++) {
					drawTexturedModalRect(rectLeft, rectTop + 5 + i, 0, 5, 124, 1);
				}
				drawTexturedModalRect(rectLeft, rectTop + informationHeight + 5, 0, 149, 124, 5);

				
				fontRendererIn.drawSplitString(information.name, rectLeft + 6, rectTop + 6, 113, 5592405);
				fontRendererIn.drawSplitString(information.name, rectLeft + 5, rectTop + 5, 113,
						Integer.parseInt("FFFFFF", 16));

				int titleHeight = fontRendererIn.getWordWrappedHeight(information.name, 113);
				fontRendererIn.drawSplitString(information.description, rectLeft + 5, rectTop + 5 + titleHeight + 3,
						113, 5592405);
				int descrHeight = fontRendererIn.getWordWrappedHeight(information.description, 113);

				if (tooltips != null && !tooltips.isEmpty()) {
					int prevHeight = rectTop + 5 + titleHeight + 3 + descrHeight + 3;

					if (tooltips != null) {
						for (String tooltip : tooltips) {

							if (tooltip.contentEquals(information.name)) {
								continue;
							}

							int tooltipEntryHeight = fontRendererIn.getWordWrappedHeight(tooltip, 90);
							fontRendererIn.drawSplitString(tooltip, rectLeft + 5, prevHeight, 113, 5592405);
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

	private ObjectInformation getBlockInformation(BlockPos pos, List<String> tooltips) {

		if (pos == null) {
			return new ObjectInformation("No block", "No block position was passed");
		}

		Block targetBlock = minecraft.world.getBlockState(pos).getBlock();
		ItemStack pickStack = targetBlock.getPickBlock(minecraft.world.getBlockState(pos), null, minecraft.world, pos,
				minecraft.player);

		String targetBlockName = targetBlock.getLocalizedName();
		String pickStackName = pickStack.getDisplayName();

		String targetBlockDescr = I18n.format(targetBlock.getUnlocalizedName() + ".description");
		String pickStackDescr = I18n.format(pickStack.getUnlocalizedName() + ".description");

		pickStack.getItem().addInformation(pickStack, Minecraft.getMinecraft().world, tooltips,
				ITooltipFlag.TooltipFlags.NORMAL);

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

						GameProfile playerProfile = (GameProfile) playerProfileGet.invoke(skull, (Object[]) null);

						if (playerProfile != null) {
							name = String.format("%s's Head", playerProfile.getName());
							descr = String.format("A severed head belonging to %s", playerProfile.getName());

							if (playerProfile.getName().contentEquals("jamesm2w")) {
								tooltips.add(String.format("%sDeveloper%s", TextFormatting.GOLD, TextFormatting.RESET));

								tooltips.add(String.format("%sLong Live Godhand!%s", TextFormatting.BLUE,
										TextFormatting.RESET));
							} else if (playerProfile.getName().contentEquals("Fureniku")) {
								tooltips.add(String.format("%sDeveloper%s", TextFormatting.BLUE, TextFormatting.RESET));
							} else if (playerProfile.getName().contentEquals("kittco36")) {
								tooltips.add(String.format("alinium btw"));
							}

						} else {
							name = "Head";
							descr = "A severed head.";
						}

					}

				} catch (Exception e) {
					e.printStackTrace();
				}

			}

		}

		if (targetBlock instanceof BlockCrops) {

			IBlockState cropState = minecraft.world.getBlockState(pos);
			BlockCrops cropBlock = (BlockCrops) targetBlock;
			try {
				Method getCropAge = BlockCrops.class.getDeclaredMethod("getAge", IBlockState.class);
				getCropAge.setAccessible(true);

				int currentGrowthState = (int) getCropAge.invoke(cropBlock, cropState);
				int maxGrowthState = cropBlock.getMaxAge();

				tooltips.add(String.format("%sGrowth: %.1f%%%s", TextFormatting.DARK_GREEN,
						(double) (currentGrowthState / maxGrowthState * 100), TextFormatting.RESET));

			} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException
					| InvocationTargetException e) {
				e.printStackTrace();
			}
		} else if (targetBlock instanceof BlockNetherWart) {
			IBlockState cropState = minecraft.world.getBlockState(pos);
			int currentGrowthState = cropState.getValue(BlockNetherWart.AGE);
			int maxGrowthState = 3;

			tooltips.add(String.format("%sGrowth: %.1f%%%s", TextFormatting.DARK_GREEN,
					(double) (currentGrowthState / maxGrowthState * 100), TextFormatting.RESET));
		} else if (targetBlock instanceof BlockStem) {
			IBlockState cropState = minecraft.world.getBlockState(pos);
			int currentGrowthState = cropState.getValue(BlockStem.AGE);
			int maxGrowthState = 3;

			tooltips.add(String.format("%sGrowth: %.1f%%%s", TextFormatting.DARK_GREEN,
					(double) (currentGrowthState / maxGrowthState * 100), TextFormatting.RESET));
		}

		return new ObjectInformation(name, descr);
	}

	private ObjectInformation getEntityInformation(Entity entity, List<String> tooltips) {

		String name;
		String descr;

		if (entity.hasCustomName()) {
			name = I18n.format(entity.getCustomNameTag());
		} else {
			name = I18n.format("entity." + EntityList.getEntityString(entity) + ".name");
		}

		descr = I18n.format("entity." + EntityList.getEntityString(entity) + ".description");

		if (entity instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) entity;

			name = player.getGameProfile().getName();
			descr = "";
		}

		return new ObjectInformation(name, descr);
	}

	private static class ObjectInformation {
		public String name;
		public String description;

		public ObjectInformation(String n, String d) {
			name = n;
			description = d;
		}
	}
}
