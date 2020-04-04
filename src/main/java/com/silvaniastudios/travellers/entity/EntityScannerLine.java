package com.silvaniastudios.travellers.entity;

import com.silvaniastudios.travellers.PacketHandler;
import com.silvaniastudios.travellers.blocks.databank.BlockDatabank;
import com.silvaniastudios.travellers.capability.playerData.IPlayerData;
import com.silvaniastudios.travellers.capability.playerData.PlayerDataProvider;
import com.silvaniastudios.travellers.items.tools.ItemScanner;
import com.silvaniastudios.travellers.network.PlayerDataSyncMessage;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EntityScannerLine extends Entity {

	EntityPlayer player;
	int ticksNotInScannable;

	public EntityScannerLine(World worldIn) {
		super(worldIn);
		System.out.println("scanner line created only with worldin. why??????");
	}

	public EntityScannerLine(World worldIn, EntityPlayer player) {
		super(worldIn);
		System.out.println("scanner line created via worldin and playerin");
		this.player = player;
		this.init(player);
		this.shoot();
	}

	@SideOnly(Side.CLIENT)
	public EntityScannerLine(World worldIn, EntityPlayer playerIn, double x, double y, double z) {
		super(worldIn);
		System.out.println("scanner line created via worldin, playerIn and x,y,z");
		//player = Minecraft.getMinecraft().player;
		this.init(playerIn);
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
		float rotationPitch = this.player.rotationPitch;// + (this.player.rotationPitch - this.player.prevRotationPitch);
		float rotationYaw = this.player.rotationYaw;// + (this.player.rotationYaw - this.player.prevRotationYaw);
		// Not sure why the above isn't just this.player.rotationPitch 
		// since its doing U[x-1] + (U[x] - U[x-1])
		System.out.println("Shooting scaner_line");
		RayTraceResult res = player.rayTrace(20.0F, 0);
		if (res.typeOfHit == RayTraceResult.Type.BLOCK) {
			BlockPos pos = res.getBlockPos();

			//if (world.getBlockState(pos).getBlock() instanceof BlockDatabank) { // If raytrace hits a databank block
				System.out.println(res.toString());
				this.setLocationAndAngles(pos.getX(), pos.getY(), pos.getZ(), this.player.rotationYaw, this.player.rotationPitch);
				
			//}

		}
		
	}

	@Override
	public void onUpdate() {
		super.onUpdate();
		
		boolean shouldDestroy = this.shouldDestroy();
		System.out.println(shouldDestroy);
		if (this.player == null) {
			this.handleKill();
			
		} else if (this.world.isRemote || !shouldDestroy) {

			BlockPos blockpos = new BlockPos(this); // Gets block where scanner currently is
			IBlockState scannedBlockState = this.world.getBlockState(blockpos);

			if (!(scannedBlockState.getBlock() instanceof BlockDatabank)) {
				++this.ticksNotInScannable;

				if (ticksNotInScannable >= 600) { // Kills self if not in a scannable block for 600 ticks
					this.handleKill();
					return;
				}
			}

			// this.move(MoverType.SELF, this.motionX, this.motionY,
			// this.motionZ);
			this.updateRotation();

			// this.motionX *= 0.92D;
			// this.motionY *= 0.92D;
			// this.motionZ *= 0.92D;
			this.setPosition(this.posX, this.posY, this.posZ);
		}
	}

	private boolean shouldDestroy() { // Returns whether the scanner line entity should be killed
		
		if (this.player == null) {
			System.out.println("Can't find a player attached to scanner line");
			return true;
		}
		
		ItemStack mainHandStack = this.getPlayer().getHeldItemMainhand();
		ItemStack offHandStack = this.getPlayer().getHeldItemOffhand();
		boolean mainHandIsScanner = mainHandStack.getItem() instanceof ItemScanner;
		boolean offHandIsScanner = offHandStack.getItem() instanceof ItemScanner;
		//System.out.println(mainHandIsScanner);
		boolean isConnected = this.getPlayer().getCapability(PlayerDataProvider.PLAYER_DATA, null).isScanning();

		if (!this.getPlayer().isDead && this.getPlayer().isEntityAlive() && (mainHandIsScanner || offHandIsScanner)
				&& this.getDistanceSq(this.getPlayer()) <= 400.0D && isConnected) {
			
			return false;
		} else if (!mainHandIsScanner && !offHandIsScanner) {
			
			return true;
		} else {
			
			return true;
		}
	}

	private void updateRotation() { // Some magic
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

	public void handleKill() {
		System.out.println("Killing scanner_line");
		super.setDead(); // Will remove entity on the next tick
		
		if (!world.isRemote && this.player != null) { // Updates player capability to mention no longer scanning
			IPlayerData playerData = this.player.getCapability(PlayerDataProvider.PLAYER_DATA, null);
			playerData.setScanning(null);
			
			PacketHandler.INSTANCE.sendTo(new PlayerDataSyncMessage(playerData), (EntityPlayerMP) player);

		}
	}

	@Override
	protected void readEntityFromNBT(NBTTagCompound compound) {
	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound compound) {
	}

	@Override
	protected void entityInit() {
		
	}

	public EntityPlayer getPlayer() {
		return this.player;
	}

	public void setPlayer(EntityPlayer player) {
		this.player = player;
	}
	
	public static class EntityScannerFactory {
		@SideOnly(Side.CLIENT)
		public static Entity createEntityScanner (World worldIn) {
			return new EntityScannerLine(worldIn, Minecraft.getMinecraft().player);
		}
	}

}
