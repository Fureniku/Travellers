package com.silvaniastudios.travellers.items;

import java.util.List;
import java.util.UUID;

import com.silvaniastudios.travellers.ModItems;
import com.silvaniastudios.travellers.PacketHandler;
import com.silvaniastudios.travellers.Travellers;
import com.silvaniastudios.travellers.capability.playerData.IPlayerData;
import com.silvaniastudios.travellers.capability.playerData.PlayerDataProvider;
import com.silvaniastudios.travellers.client.gui.GuiCodexPieceInformation;
import com.silvaniastudios.travellers.network.PlayerDataSyncMessage;

import net.minecraft.client.Minecraft;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemCodex extends ItemBasic {

	private String guid;
	private int pieceIndex;
	private String parentName;
	private int knowledge;
	public String text;
	public String language;

	public ItemCodex(String uuid) {
		super(uuid);
		this.setGuid(UUID.randomUUID().toString());
		text = Travellers.CODEX_DATA.getText(uuid);
		ModItems.codices.add(this);
	}

	public ItemCodex(String uuid, int index, String parentName, int knowledge, String language, String text) {
		super(cleanString(parentName).concat("_").concat(String.valueOf(index + 1)));

		this.setGuid(uuid);
		this.pieceIndex = index;
		this.parentName = parentName;
		this.knowledge = knowledge;
		this.language = language;

		this.text = text;
		this.setCreativeTab(Travellers.tabLore);

		this.setUnlocalizedName("codex_piece");
		this.setMaxStackSize(1);
	}

	@Override
	public EnumRarity getRarity(ItemStack stack) {
		switch (knowledge) {
		case 15:
			return EnumRarity.values()[4];
		case 25:
			return EnumRarity.values()[5];
		case 50:
			return EnumRarity.values()[6];
		case 100:
			return EnumRarity.values()[7];
		case 150:
			return EnumRarity.values()[8];
		default:
			return EnumRarity.values()[4];
		}
	}

	@Override
	public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		tooltip.add(String.format("%s%s%s", this.getRarity(stack).rarityColor, this.getRarity(stack).rarityName,
				TextFormatting.RESET));
		tooltip.add(String.format("%s #%d", this.parentName, this.pieceIndex + 1));
		tooltip.add(String.format("Language: %s", this.language));

	}

	@Override
	@SideOnly(Side.CLIENT)
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
		ItemStack playerHolding = playerIn.getHeldItem(handIn);

		if (worldIn.isRemote) {
			Minecraft.getMinecraft()
					.displayGuiScreen(new GuiCodexPieceInformation((ItemCodex) playerHolding.getItem()));
		}

		if (!worldIn.isRemote) {
			IPlayerData playerData = playerIn.getCapability(PlayerDataProvider.PLAYER_DATA, null);

			if (playerData.getKnownLorePieces().contains(this.guid)) {
				String message = String.format("%sYou have already collected this codex piece%s", TextFormatting.GOLD,
						TextFormatting.RESET);
				playerIn.sendMessage(new TextComponentString(message));

				return new ActionResult<ItemStack>(EnumActionResult.PASS, playerHolding);
			}

			playerData.learnLorePiece(this.guid);
			String message = String.format("%sPiece %s%d%s of %s%s%s gave you %s%d%s knowledge%s", TextFormatting.GOLD,
					TextFormatting.RESET, this.pieceIndex + 1, TextFormatting.GOLD, TextFormatting.RESET,
					this.parentName, TextFormatting.GOLD, TextFormatting.RESET, this.knowledge, TextFormatting.GOLD,
					TextFormatting.RESET);
			playerIn.sendMessage(new TextComponentString(message));

			PacketHandler.INSTANCE.sendTo(new PlayerDataSyncMessage(playerData), (EntityPlayerMP) playerIn);

			return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, ItemStack.EMPTY);
		}

		return new ActionResult<ItemStack>(EnumActionResult.PASS, playerHolding);
	}

	@Override
	public void registerItemModel() {
		Travellers.proxy.registerItemRenderer(this, 0, "codex_default");
	}

	/**
	 * @return the guid
	 */
	public String getGuid() {
		return guid;
	}

	/**
	 * @param guid
	 *            the guid to set
	 */
	public void setGuid(String guid) {
		this.guid = guid;
	}

	/**
	 * @return the pieceIndex
	 */
	public int getPieceIndex() {
		return pieceIndex;
	}

	/**
	 * @param pieceIndex
	 *            the pieceIndex to set
	 */
	public void setPieceIndex(int pieceIndex) {
		this.pieceIndex = pieceIndex;
	}

	/**
	 * @return the parentName
	 */
	public String getParentName() {
		return parentName;
	}

	/**
	 * @param parentName
	 *            the parentName to set
	 */
	public void setParentName(String parentName) {
		this.parentName = parentName;
	}

	/**
	 * @return the knowledge
	 */
	public int getKnowledge() {
		return knowledge;
	}

	/**
	 * @param knowledge
	 *            the knowledge to set
	 */
	public void setKnowledge(int knowledge) {
		this.knowledge = knowledge;
	}

	public static String cleanString(String str) {
		return str.replace("<", "").replace(">", "").replace("\"", "").replace("\"", "").replace(" ", "_")
				.replace("-", "_").replace(",", "").replace(".", "").replace(":", "_").replace("'", "")
				.replace("___", "_").replace("__", "_")
				.toLowerCase();
	}
}
