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
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EntityScannerLine extends Entity {

	EntityPlayer player;
	int ticksNotInScannable;

	public EntityScannerLine(World worldIn) {
		super(worldIn);
		//System.out.println("scanner line created (via worldin) " + this.entityUniqueID.toString());
	}

	public EntityScannerLine(World worldIn, EntityPlayer player) {
		super(worldIn);
		//System.out.println("scanner line created (via worldin, player) "  + this.entityUniqueID.toString());
		//System.out.println("player given to " + this.entityUniqueID.toString() + " is " + player.getUniqueID().toString());
		this.player = player;
		this.init(player);
		this.shoot();
	}

	@SideOnly(Side.CLIENT)
	public EntityScannerLine(World worldIn, double x, double y, double z) {
		super(worldIn);
		//System.out.println("scanner line created (via world, x,y,z) "  + this.entityUniqueID.toString());
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
		this.ticksNotInScannable = 0;
		this.ignoreFrustumCheck = true;
	}

	@SideOnly(Side.CLIENT)
	public boolean isInRangeToRenderDist(double distance) {
		return distance < 4096.0D;
	}

	private void shoot() {
		float pitch = this.player.prevRotationPitch + (this.player.rotationPitch - this.player.prevRotationPitch);
		float yaw = this.player.prevRotationYaw + (this.player.rotationYaw - this.player.prevRotationYaw);

		RayTraceResult res = player.rayTrace(20.0F, 0);
		//System.out.println("Shooting scanner_line " + this.entityUniqueID.toString());
		//System.out.println(res.toString());
		if (res.typeOfHit == RayTraceResult.Type.BLOCK) {

			this.setLocationAndAngles(res.hitVec.x, res.hitVec.y, res.hitVec.z, yaw, pitch);

		} else {
			//System.out.println("scanner_line didnt hit a block so killing " + this.entityUniqueID.toString());
			this.handleKill();
		}

	}

	@Override
	public void onUpdate() {
		super.onUpdate();

		if (this.player == null) {
			//System.out.println("scanner_line not attached to a player so killing " + this.entityUniqueID.toString());
			this.handleKill();
		} else {
			boolean shouldDestroy = this.shouldDestroy();
			if (this.world.isRemote && !shouldDestroy) {
			
				BlockPos blockpos = new BlockPos(this);
				IBlockState iblockstate = this.world.getBlockState(blockpos);
				
				this.updateRotation();
				
				if (!(iblockstate.getBlock() instanceof BlockDatabank)) {
					++this.ticksNotInScannable;
	
					if (ticksNotInScannable >= 600) {
						this.handleKill();
					}
				}
			} else if (shouldDestroy) {
				//System.out.println("scanner_line marked for destruction " + this.entityUniqueID.toString());
				this.handleKill();
			}
		}
	}

	private boolean shouldDestroy() {
		ItemStack heldItemMainhand = this.player.getHeldItemMainhand();
		ItemStack heldItemOffhand = this.player.getHeldItemOffhand();
		boolean mainhandHolding = heldItemMainhand.getItem() instanceof ItemScanner;
		boolean offhandHolding = heldItemOffhand.getItem() instanceof ItemScanner;
		
		boolean notHoldingScanner = !mainhandHolding && !offhandHolding;
		
		boolean isConnected = this.player.getCapability(PlayerDataProvider.PLAYER_DATA, null).isScanning();
		
		if (notHoldingScanner || !isConnected || (this.getDistance(this.player) >= 10.0D)
				|| (this.player.isDead && !this.player.isEntityAlive())) {
			return true;
		} else {
			return false;
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

	public void handleKill() {
		this.setDead();
		//System.out.println("Killing scanner_line " + this.entityUniqueID.toString());
		if (world.isRemote && this.player != null) {
			
			IPlayerData playerData = this.player.getCapability(PlayerDataProvider.PLAYER_DATA, null);
			playerData.setScanning(null);
			
			PacketHandler.INSTANCE.sendToServer(new PlayerDataSyncMessage(playerData, player.getUniqueID()));
		}
	}

	@Override
	protected void readEntityFromNBT(NBTTagCompound compound) {
		this.player = FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayerByUUID(compound.getUniqueId("player"));
	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound compound) {
		compound.setUniqueId("player", this.player.getUniqueID());
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

}
