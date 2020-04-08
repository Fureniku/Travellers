package com.silvaniastudios.travellers.client.render;

import com.silvaniastudios.travellers.Travellers;
import com.silvaniastudios.travellers.entity.EntityScannerLine;
import com.silvaniastudios.travellers.items.tools.ItemScanner;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

/**
 * Renders the scanner line entity to the world.
 * Heavily borrowed from the vanilla Fishing Rod & bobber
 * 
 * @author james_pntzyfo
 * 
 */
public class RenderScannerLine extends Render<EntityScannerLine> {

	public static final ResourceLocation SCANNERLINE = new ResourceLocation(Travellers.MODID,
			"textures/entity/scanner.png");

	public RenderScannerLine(RenderManager renderManager) {
		super(renderManager);
	}

	/** 
	 * Renders Scanner Line entity in world from entity position to player position
	 * This function is mainly copied from the obfuscated fishing rod example, I've done my best to try
	 * decode how it works, but there are still some issues with the exact placement of the entity
	 * 
	 * @see net.minecraft.client.renderer.entity.Render#doRender(net.minecraft.entity.Entity, double, double, double, float, float)
	 */
	@Override
	public void doRender(EntityScannerLine entity, double x, double y, double z, float entityYaw, float partialTicks) {
		EntityPlayer entityplayer = entity.getPlayer();

		if (entityplayer != null && !this.renderOutlines) {
			GlStateManager.pushMatrix();
			GlStateManager.translate((float) x, (float) y, (float) z);
			GlStateManager.enableRescaleNormal();
			GlStateManager.scale(0.5F, 0.5F, 0.5F);
			this.bindEntityTexture(entity);
			Tessellator tessellator = Tessellator.getInstance();
			BufferBuilder bufferbuilder = tessellator.getBuffer();
			
			GlStateManager.popMatrix();
			int k = entityplayer.getPrimaryHand() == EnumHandSide.RIGHT ? 1 : -1;
			ItemStack itemstack = entityplayer.getHeldItemMainhand();

			if (!(itemstack.getItem() instanceof ItemScanner)) {
				k = -k;
			}

			float swingProgress = entityplayer.getSwingProgress(partialTicks);
			float f8 = MathHelper.sin(MathHelper.sqrt(swingProgress) * (float) Math.PI);
			float f9 = (entityplayer.prevRenderYawOffset
					+ (entityplayer.renderYawOffset - entityplayer.prevRenderYawOffset) * partialTicks) * 0.017453292F;
			double yawSin = (double) MathHelper.sin(f9);
			double yawCos = (double) MathHelper.cos(f9);
			double handAdjustment = (double) k * 0.35D;
			//double d3 = 0.8D;
			double playerX;
			double playerY;
			double playerZ;
			double eyeHeightAdjustment;

			if ((this.renderManager.options == null || this.renderManager.options.thirdPersonView <= 0)
					&& entityplayer == Minecraft.getMinecraft().player) {
				float f10 = this.renderManager.options.fovSetting;
				f10 = f10 / 100.0F;
				Vec3d vec3d = new Vec3d((double) k * -0.36D * (double) f10, -0.045D * (double) f10, 0.4D);
				vec3d = vec3d.rotatePitch(-(entityplayer.prevRotationPitch
						+ (entityplayer.rotationPitch - entityplayer.prevRotationPitch) * partialTicks) * 0.017453292F);
				vec3d = vec3d.rotateYaw(-(entityplayer.prevRotationYaw
						+ (entityplayer.rotationYaw - entityplayer.prevRotationYaw) * partialTicks) * 0.017453292F);
				vec3d = vec3d.rotateYaw(f8 * 0.5F);
				vec3d = vec3d.rotatePitch(-f8 * 0.7F);
				playerX = entityplayer.prevPosX + (entityplayer.posX - entityplayer.prevPosX) * (double) partialTicks
						+ vec3d.x;
				playerY = entityplayer.prevPosY + (entityplayer.posY - entityplayer.prevPosY) * (double) partialTicks
						+ vec3d.y;
				playerZ = entityplayer.prevPosZ + (entityplayer.posZ - entityplayer.prevPosZ) * (double) partialTicks
						+ vec3d.z;
				eyeHeightAdjustment = (double) entityplayer.getEyeHeight();
			} else {
				playerX = entityplayer.prevPosX + (entityplayer.posX - entityplayer.prevPosX) * (double) partialTicks
						- yawCos * handAdjustment - yawSin * 0.8D;
				playerY = entityplayer.prevPosY + (double) entityplayer.getEyeHeight()
						+ (entityplayer.posY - entityplayer.prevPosY) * (double) partialTicks - 0.8D;
				playerZ = entityplayer.prevPosZ + (entityplayer.posZ - entityplayer.prevPosZ) * (double) partialTicks
						- yawSin * handAdjustment + yawCos * 0.4D;
				eyeHeightAdjustment = entityplayer.isSneaking() ? -0.1875D : 0.0D;
			}

			double groundX = entity.prevPosX + (entity.posX - entity.prevPosX) * (double) partialTicks;
			double groundY = entity.prevPosY + (entity.posY - entity.prevPosY) * (double) partialTicks + 0.25D;
			double grounxZ = entity.prevPosZ + (entity.posZ - entity.prevPosZ) * (double) partialTicks;
			double deltaX = (double) ((float) (playerX - groundX));
			double deltaY = (double) ((float) (playerY - groundY)) + eyeHeightAdjustment;
			double deltaZ = (double) ((float) (playerZ - grounxZ));
			GlStateManager.disableTexture2D();
			GlStateManager.disableLighting();
			bufferbuilder.begin(3, DefaultVertexFormats.POSITION_COLOR);
			
			for (int count = 0; count <= 16; ++count) {
				float proportion = (float) count / 16.0F;
				
				bufferbuilder.pos(x + deltaX * (double) proportion, y + deltaY * (double) (proportion * proportion + proportion) * 0.5D /*+ 0.25D*/,
						z + deltaZ * (double) proportion).color(0, 0, 0, 255).endVertex();
			}

			tessellator.draw();
			GlStateManager.enableLighting();
			GlStateManager.enableTexture2D();
			super.doRender(entity, x, y, z, entityYaw, partialTicks);
		}
	}

	/**
	 * Returns resource location for the scanner line entity, this texture is not actually used, but this
	 * exists to stop any errors from code which might try to find it (e.g. bind entity texture)
	 * 
	 * @see net.minecraft.client.renderer.entity.Render#getEntityTexture(net.minecraft.entity.Entity)
	 */
	@Override
	protected ResourceLocation getEntityTexture(EntityScannerLine entity) {

		return SCANNERLINE;
	}

}
