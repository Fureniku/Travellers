package com.silvaniastudios.travellers.items;

import java.util.List;
import java.util.UUID;

import com.silvaniastudios.travellers.ModItems;
import com.silvaniastudios.travellers.PacketHandler;
import com.silvaniastudios.travellers.Travellers;
import com.silvaniastudios.travellers.capability.playerData.IPlayerData;
import com.silvaniastudios.travellers.capability.playerData.PlayerDataProvider;
import com.silvaniastudios.travellers.network.PlayerDataSyncMessage;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

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
		text = I18n.format("codex."+uuid);
		ModItems.codices.add(this);
	}
	
	public ItemCodex(String uuid, int index, String parentName, int knowledge, String language) {
		super(uuid);
		
		this.setGuid(uuid);
		this.pieceIndex = index;
		this.parentName = parentName;
		this.knowledge = knowledge;
		this.language = language;
		
		text = I18n.format("codex."+uuid);
		this.setCreativeTab(Travellers.tabLore);
		this.setUnlocalizedName("codex_piece");
		
		this.setMaxStackSize(1);
	}
	
	@Override
	public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		tooltip.add(String.format("%s #%d", this.parentName, this.pieceIndex + 1));
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
		ItemStack playerHolding = playerIn.getHeldItem(handIn);
		
		if (!worldIn.isRemote) {
			IPlayerData playerData = playerIn.getCapability(PlayerDataProvider.PLAYER_DATA, null);
			
			
			if (playerData.getKnownLorePieces().contains(this.guid)) {
				String message = String.format("�eYou have already collected this codex piece�r");
				playerIn.sendMessage(new TextComponentString(message));
				
				return new ActionResult<ItemStack>(EnumActionResult.PASS, playerHolding);
			}
			
			playerData.learnLorePiece(this.guid);
			String message = String.format("�eCodex piece gave you �r%d�e knowledge�r", this.knowledge);
			playerIn.sendMessage(new TextComponentString(message));
			
			PacketHandler.INSTANCE.sendTo(new PlayerDataSyncMessage(playerData),
					(EntityPlayerMP) playerIn);
			
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
	 * @param guid the guid to set
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
	 * @param pieceIndex the pieceIndex to set
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
	 * @param parentName the parentName to set
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
	 * @param knowledge the knowledge to set
	 */
	public void setKnowledge(int knowledge) {
		this.knowledge = knowledge;
	}
}
