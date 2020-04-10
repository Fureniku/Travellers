package com.silvaniastudios.travellers.entity;

import java.util.UUID;

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

/**
 * Scanner line entity which renders the little black line between the scanner tool and whatever
 * was being scanned
 * 
 * @author jamesm2w
 */
public class EntityScannerLine extends Entity {

	/**
	 * Player which started scanning
	 */
	EntityPlayer player;
	
	/**
	 * Amount of ticks this entity has not been scanning a block which can be scanned
	 */
	int ticksNotInScannable;

	public UUID initialUUID;
	
	boolean deleteImmediately = false;
	
	/**
	 * Mandatory constructor by Forge. THIS SHOULD NEVER BE USED.
	 * 
	 * @constructor
	 * @deprecated
	 * @param worldIn
	 */
	public EntityScannerLine(World worldIn) {
		super(worldIn);

		//if (worldIn.isRemote) {
		//	this.player = Minecraft.getMinecraft().player;
		//	this.init(player);
		//	this.shoot();
		//}
		//System.out.println("scanner line created (via worldin) " + this.entityUniqueID.toString());
		initialUUID = this.entityUniqueID;
		deleteImmediately = false;
		this.ignoreFrustumCheck = true;
	}
	
	public EntityScannerLine(World worldIn, UUID playerUUID) {
		super(worldIn);
		this.player = worldIn.getPlayerEntityByUUID(playerUUID);
		//System.out.println("scanner line created (via worldin, uuid) " + this.entityUniqueID.toString() + " UUID: " + playerUUID.toString());
		initialUUID = this.entityUniqueID;
		deleteImmediately = true;
	}
	
	/**
	 * Creates the new scanner line entity, and raytraces to its position
	 * 
	 * @constructor 
	 * @param worldIn World in which this scanner line was created
	 * @param player Player which started scanning
	 */
	public EntityScannerLine(World worldIn, EntityPlayer player) {
		super(worldIn);
		//System.out.println("scanner line created (via worldin, player) "  + this.entityUniqueID.toString());
		//System.out.println("player given to " + this.entityUniqueID.toString() + " is " + player.getUniqueID().toString());
		this.player = player;
		this.init(player);
		this.shoot();
		
		initialUUID = this.entityUniqueID;
		deleteImmediately = true;
	}

	/**
	 * Client-Side constructor of the entity - rarely seen this used in testing
	 * @param worldIn
	 * @param x
	 * @param y
	 * @param z
	 */
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

	/**
	 * Initialises some basic attributes and sizing.
	 * 
	 * @param player Player which started the scanning
	 */
	public void init(EntityPlayer player) {
		this.setSize(0.25F, 0.25F);
		this.player = player;
		this.ticksNotInScannable = 0;
		this.ignoreFrustumCheck = true;
	}
	
	/**
	 * 
	 * @see net.minecraft.entity.Entity#isInRangeToRenderDist(double)
	 */
	@SideOnly(Side.CLIENT)
	public boolean isInRangeToRenderDist(double distance) {
		return distance < 4096.0D;
	}

	/**
	 * Raytraces for a block within 20 blocks. This result will be the position of the entity
	 */
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

	/**
	 * Checks whether the entity should kill itself this tick, and updates the rotation to the player
	 * 
	 * @see net.minecraft.entity.Entity#onUpdate()
	 */
	@Override
	public void onUpdate() {
		super.onUpdate();
		
		//System.out.println(this.getUniqueID().toString() + " player " + (this.player != null ? this.player.getUniqueID().toString() : "null") + " IUUD: " + this.initialUUID.toString());
		
		if (this.player == null && this.deleteImmediately) {
			//System.out.println("scanner_line not attached to a player so killing " + this.entityUniqueID.toString());
			this.handleKill();
		} else if (this.player != null) {
			boolean shouldDestroy = this.shouldDestroy();
			if (!this.world.isRemote && !shouldDestroy) {
			
				BlockPos blockpos = new BlockPos(this);
				IBlockState iblockstate = this.world.getBlockState(blockpos);
				
				this.updateRotation();
				
				if (!(iblockstate.getBlock() instanceof BlockDatabank)) {
					++this.ticksNotInScannable;
	
					if (ticksNotInScannable >= 600) {
						//System.out.println("scanner_line is not in scannable for too many ticks! " + this.entityUniqueID.toString() + " IUUD: " + this.initialUUID.toString());
						this.handleKill();
					}
				}
			} else if (shouldDestroy) {
				//System.out.println("scanner_line marked for destruction " + this.entityUniqueID.toString() + " IUUD: " + this.initialUUID.toString());
				this.handleKill();
			}
		}
	}

	/**
	 * @return boolean Whether this entity should kill itself.
	 */
	private boolean shouldDestroy() {
		ItemStack heldItemMainhand = this.player.getHeldItemMainhand();
		ItemStack heldItemOffhand = this.player.getHeldItemOffhand();
		boolean mainhandHolding = heldItemMainhand.getItem() instanceof ItemScanner;
		boolean offhandHolding = heldItemOffhand.getItem() instanceof ItemScanner;
		
		boolean notHoldingScanner = !mainhandHolding && !offhandHolding;
		
		boolean isConnected = this.player.getCapability(PlayerDataProvider.PLAYER_DATA, null).isScanning();
		//System.out.println("notHoldingScanner: " + String.valueOf(notHoldingScanner) + " isConnected: " + String.valueOf(isConnected));
		
		// This checks if the game is running as a client and the entity is attached to another player. if so, we don't have authority over when this thing dies
		if (!world.isRemote || this.player == Minecraft.getMinecraft().player) {
			if (notHoldingScanner || !isConnected || (this.getDistance(this.player) >= 10.0D)
					|| (this.player.isDead && !this.player.isEntityAlive())) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
		
		
	}

	/**
	 * Copied from the fishing bobber, basically magic
	 */
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

	/**
	 * Handles making this entity dead, and updating the player capabilities
	 */
	public void handleKill() {
		if (!this.world.isRemote && this.player != null) {
			this.setDead();
			//System.out.println("Killed scanner_line " + this.entityUniqueID.toString() + " because it was marked dead on server with player. IUUID: " + initialUUID.toString());
			
			IPlayerData playerData = this.player.getCapability(PlayerDataProvider.PLAYER_DATA, null);
			playerData.setScanning(null);
			
			PacketHandler.INSTANCE.sendTo(new PlayerDataSyncMessage(playerData), (EntityPlayerMP) player);
		} else if (this.player == null) {
			this.setDead();
			//System.out.println("Killed scanner_line " + this.getUniqueID().toString() + " because it has no parent player on" + (this.world.isRemote ? "client" : "server") + " IUUID: " + initialUUID.toString());
		} else {
			//System.out.println("Killed scanner_line " + this.getUniqueID().toString() + " which has player but is marked dead on client IUUID: "+ initialUUID.toString());
			this.setDead();
		}
	}

	@Override
	protected void readEntityFromNBT(NBTTagCompound compound) {
		
		//System.out.println(getUniqueID().toString() + " read nbt: " + String.valueOf(compound.hasUniqueId("player")));
		
		if (compound.hasUniqueId("player")) {
			this.player = this.world.getPlayerEntityByUUID(compound.getUniqueId("player"));
			
			//System.out.println(this.getUniqueID().toString() + " with player: " + this.player.getUniqueID().toString());
		} else if (compound.hasKey("playerID")) {
			this.player = this.world.getPlayerEntityByUUID(UUID.fromString(compound.getString("playerID")));
		} else{
			this.handleKill();
		}
		
		this.deleteImmediately = true;
	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound compound) {
		if (this.player != null) {
			compound.setUniqueId("player", this.player.getUniqueID());
			compound.setString("playerID", this.player.getUniqueID().toString());
			//System.out.println(this.getUniqueID().toString() + " writing player to NBT: " + this.player.getUniqueID().toString());
		}
	}

	@Override
	protected void entityInit() {
	}

	public EntityPlayer getPlayer() {
		return this.player;
	}

	public void setPlayer(EntityPlayer player) {
		this.player = player;
		
		this.deleteImmediately = true;
	}

}