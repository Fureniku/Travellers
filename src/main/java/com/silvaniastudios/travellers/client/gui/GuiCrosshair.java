package com.silvaniastudios.travellers.client.gui;

import com.silvaniastudios.travellers.Travellers;
import com.silvaniastudios.travellers.items.tools.ITravellerTool;
import com.silvaniastudios.travellers.items.tools.ItemGrapple;
import com.silvaniastudios.travellers.items.tools.ItemRepairTool;
import com.silvaniastudios.travellers.items.tools.ItemSalvager;
import com.silvaniastudios.travellers.items.tools.ItemScanner;
import com.silvaniastudios.travellers.items.tools.ItemShipyardTool;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;

public class GuiCrosshair extends Gui {
	public Minecraft mc;

	public static final ResourceLocation TEXTURE = new ResourceLocation(Travellers.MODID, "textures/gui/crosshair.png");

	public GuiCrosshair() {
		mc = Minecraft.getMinecraft();
	}

	public boolean draw(float partialTicks) {
		ScaledResolution scaled = new ScaledResolution(mc);
		
		EntityPlayerSP player = Minecraft.getMinecraft().player;
		Minecraft.getMinecraft().renderEngine.bindTexture(TEXTURE);
		GlStateManager.enableBlend();
		
		ItemStack heldItem = player.getHeldItemMainhand();
		
		if (heldItem.getItem() instanceof ITravellerTool) {
			
			renderAttackIndicator(partialTicks, scaled, heldItem);
			return true;
		}
		
		if (player.getHeldItemOffhand().getItem() instanceof ITravellerTool) {
			
			renderAttackIndicator(partialTicks, scaled, player.getHeldItemOffhand());
			return true;
		}
		
		return false;
	}

	void renderAttackIndicator(float partialTicks, ScaledResolution resolution, ItemStack item) {
		GameSettings gamesettings = this.mc.gameSettings;

		if (gamesettings.thirdPersonView == 0) {
			if (this.mc.playerController.isSpectator() && this.mc.pointedEntity == null) {
				RayTraceResult raytraceresult = this.mc.objectMouseOver;

				if (raytraceresult == null || raytraceresult.typeOfHit != RayTraceResult.Type.BLOCK) {
					return;
				}

				BlockPos blockpos = raytraceresult.getBlockPos();

				net.minecraft.block.state.IBlockState state = this.mc.world.getBlockState(blockpos);
				if (!state.getBlock().hasTileEntity(state)
						|| !(this.mc.world.getTileEntity(blockpos) instanceof IInventory)) {
					return;
				}
			}

			int width = resolution.getScaledWidth();
			int height = resolution.getScaledHeight();

			if (gamesettings.showDebugInfo && !gamesettings.hideGUI && !this.mc.player.hasReducedDebug()
					&& !gamesettings.reducedDebugInfo) {
				GlStateManager.pushMatrix();
				GlStateManager.translate((float) (width / 2), (float) (height / 2), this.zLevel);
				Entity entity = this.mc.getRenderViewEntity();
				GlStateManager.rotate(
						entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * partialTicks,
						-1.0F, 0.0F, 0.0F);
				GlStateManager.rotate(
						entity.prevRotationYaw + (entity.rotationYaw - entity.prevRotationYaw) * partialTicks, 0.0F,
						1.0F, 0.0F);
				GlStateManager.scale(-1.0F, -1.0F, -1.0F);
				OpenGlHelper.renderDirections(10);
				GlStateManager.popMatrix();
			} else {
				GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.ONE_MINUS_DST_COLOR,
						GlStateManager.DestFactor.ONE_MINUS_SRC_COLOR, GlStateManager.SourceFactor.ONE,
						GlStateManager.DestFactor.ZERO);
				GlStateManager.enableAlpha();
				
				int startX = 0;
				int startY = 0;
				
				if (item.getItem() instanceof ItemSalvager) {
					startX = 0;
				} else if (item.getItem() instanceof ItemRepairTool) {
					startX = 15;
				} else if (item.getItem() instanceof ItemShipyardTool) {
					startX = 30;
				} else if (item.getItem() instanceof ItemScanner) {
					startX = 45;
				} else if (item.getItem() instanceof ItemGrapple) {
					startX = 15; // or 0 for grapple allowed
					startY = 15;
				}
				
				this.drawTexturedModalRect(width / 2 - 7, height / 2 - 7, startX, startY, 16, 16);

				if (this.mc.gameSettings.attackIndicator == 1) {
					this.mc.renderEngine.bindTexture(ICONS);
					float f = this.mc.player.getCooledAttackStrength(0.0F);
					boolean flag = false;

					if (this.mc.pointedEntity != null && this.mc.pointedEntity instanceof EntityLivingBase
							&& f >= 1.0F) {
						flag = this.mc.player.getCooldownPeriod() > 5.0F;
						flag = flag & ((EntityLivingBase) this.mc.pointedEntity).isEntityAlive();
					}

					int i = height / 2 - 7 + 16;
					int j = width / 2 - 8;

					if (flag) {
						this.drawTexturedModalRect(j, i, 68, 94, 16, 16);
					} else if (f < 1.0F) {
						int k = (int) (f * 17.0F);
						this.drawTexturedModalRect(j, i, 36, 94, 16, 4);
						this.drawTexturedModalRect(j, i, 52, 94, k, 4);
					}
				}
			}
		}
	}
}
