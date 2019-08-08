package com.silvaniastudios.travellers.entity;

import com.silvaniastudios.travellers.blocks.databank.BlockDatabank;
import com.silvaniastudios.travellers.items.tools.ItemScanner;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EntityScannerLine extends Entity {

	EntityPlayer player;
	int ticksNotInScannable;

	public EntityScannerLine(World worldIn) {
		super(worldIn);
	}

	public EntityScannerLine(World worldIn, EntityPlayer player) {
		super(worldIn);
		this.player = player;
		this.init(player);
		this.shoot();
	}

	@SideOnly(Side.CLIENT)
	public EntityScannerLine(World worldIn, double x, double y, double z) {
		super(worldIn);
		player = Minecraft.getMinecraft().player;
		this.init(player);
		this.setPosition(x, y, z);
		this.prevPosX = this.posX;
		this.prevPosY = this.posY;
		this.prevPosZ = this.posZ;
	}

	public void init(EntityPlayer player) {
		this.setSize(0.25F, 0.25F);
		this.player = player;
		this.ignoreFrustumCheck = true;
	}

	@SideOnly(Side.CLIENT)
	public boolean isInRangeToRenderDist(double distance) {
		return distance < 4096.0D;
	}

	private void shoot() {
		/*
		 * float f = this.player.prevRotationPitch + (this.player.rotationPitch
		 * - this.player.prevRotationPitch); float f1 =
		 * this.player.prevRotationYaw + (this.player.rotationYaw -
		 * this.player.prevRotationYaw); float f2 = MathHelper.cos(-f1 *
		 * 0.017453292F - (float) Math.PI); float f3 = MathHelper.sin(-f1 *
		 * 0.017453292F - (float) Math.PI); float f4 = -MathHelper.cos(-f *
		 * 0.017453292F); float f5 = MathHelper.sin(-f * 0.017453292F); double
		 * d0 = this.player.prevPosX + (this.player.posX - this.player.prevPosX)
		 * - (double) f3 * 0.3D; double d1 = this.player.prevPosY +
		 * (this.player.posY - this.player.prevPosY) + (double)
		 * this.player.getEyeHeight(); double d2 = this.player.prevPosZ +
		 * (this.player.posZ - this.player.prevPosZ) - (double) f2 * 0.3D;
		 * this.setLocationAndAngles(d0, d1, d2, f1, f); this.motionX = (double)
		 * (-f3); this.motionY = (double) MathHelper.clamp(-(f5 / f4), -5.0F,
		 * 5.0F); this.motionZ = (double) (-f2); float f6 = MathHelper
		 * .sqrt(this.motionX * this.motionX + this.motionY * this.motionY +
		 * this.motionZ * this.motionZ); this.motionX *= 0.6D / (double) f6 +
		 * 0.5D + this.rand.nextGaussian() * 0.0045D; this.motionY *= 0.6D /
		 * (double) f6 + 0.5D + this.rand.nextGaussian() * 0.0045D; this.motionZ
		 * *= 0.6D / (double) f6 + 0.5D + this.rand.nextGaussian() * 0.0045D;
		 * float f7 = MathHelper.sqrt(this.motionX * this.motionX + this.motionZ
		 * * this.motionZ); this.rotationYaw = (float)
		 * (MathHelper.atan2(this.motionX, this.motionZ) * (180D / Math.PI));
		 * this.rotationPitch = (float) (MathHelper.atan2(this.motionY, (double)
		 * f7) * (180D / Math.PI)); this.prevRotationYaw = this.rotationYaw;
		 * this.prevRotationPitch = this.rotationPitch;
		 */
		float f = this.player.prevRotationPitch + (this.player.rotationPitch - this.player.prevRotationPitch);
		float f1 = this.player.prevRotationYaw + (this.player.rotationYaw - this.player.prevRotationYaw);

		RayTraceResult res = player.rayTrace(20.0F, 0);
		if (res.typeOfHit == RayTraceResult.Type.BLOCK) {
			BlockPos pos = res.getBlockPos();
			
			if (world.getBlockState(pos).getBlock() instanceof BlockDatabank) {
				this.setLocationAndAngles(pos.getX(), pos.getY(), pos.getZ(), f1, f);
			} else {
				this.setDead();
			}
			
			
		} else {
			this.setDead();
		}
		
	}

	@Override
	public void onUpdate() {
		super.onUpdate();

		if (this.player == null) {
			this.setDead();
		} else if (this.world.isRemote || !this.shouldDestroy()) {

			BlockPos blockpos = new BlockPos(this);
			IBlockState iblockstate = this.world.getBlockState(blockpos);

			if (!(iblockstate.getBlock() instanceof BlockDatabank)) {
				++this.ticksNotInScannable;

				if (ticksNotInScannable >= 600) {
					this.setDead();
					return;
				}
			}

			//this.move(MoverType.SELF, this.motionX, this.motionY, this.motionZ);
			this.updateRotation();

			//this.motionX *= 0.92D;
			//this.motionY *= 0.92D;
			//this.motionZ *= 0.92D;
			this.setPosition(this.posX, this.posY, this.posZ);
		}
	}

	private boolean shouldDestroy() {
		ItemStack itemstack = this.player.getHeldItemMainhand();
		ItemStack itemstack1 = this.player.getHeldItemOffhand();
		boolean flag = itemstack.getItem() instanceof ItemScanner;
		boolean flag1 = itemstack1.getItem() instanceof ItemScanner;

		if (!this.player.isDead && this.player.isEntityAlive() && (flag || flag1)
				&& this.getDistanceSq(this.player) <= 1024.0D) {
			return false;
		} else {
			this.setDead();
			return true;
		}
	}

	private void updateRotation() {
		float f = MathHelper.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
		this.rotationYaw = (float) (MathHelper.atan2(this.motionX, this.motionZ) * (180D / Math.PI));

		for (this.rotationPitch = (float) (MathHelper.atan2(this.motionY, (double) f)
				* (180D / Math.PI)); this.rotationPitch
						- this.prevRotationPitch < -180.0F; this.prevRotationPitch -= 360.0F) {
			;
		}

		while (this.rotationPitch - this.prevRotationPitch >= 180.0F) {
			this.prevRotationPitch += 360.0F;
		}

		while (this.rotationYaw - this.prevRotationYaw < -180.0F) {
			this.prevRotationYaw -= 360.0F;
		}

		while (this.rotationYaw - this.prevRotationYaw >= 180.0F) {
			this.prevRotationYaw += 360.0F;
		}

		this.rotationPitch = this.prevRotationPitch + (this.rotationPitch - this.prevRotationPitch) * 0.2F;
		this.rotationYaw = this.prevRotationYaw + (this.rotationYaw - this.prevRotationYaw) * 0.2F;
	}

	@Override
	protected void readEntityFromNBT(NBTTagCompound compound) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound compound) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void entityInit() {
		// TODO Auto-generated method stub

	}

	public EntityPlayer getPlayer() {
		return this.player;
	}

	public void setPlayer(EntityPlayer player) {
		this.player = player;
	}

}
