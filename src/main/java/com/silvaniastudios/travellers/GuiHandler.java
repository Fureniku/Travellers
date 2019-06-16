package com.silvaniastudios.travellers;

import com.silvaniastudios.travellers.blocks.tileentity.assembler.AssemblerContainer;
import com.silvaniastudios.travellers.blocks.tileentity.assembler.AssemblerEntity;
import com.silvaniastudios.travellers.blocks.tileentity.shipyard.ShipyardContainer;
import com.silvaniastudios.travellers.blocks.tileentity.shipyard.ShipyardEntity;
import com.silvaniastudios.travellers.client.gui.GuiAssembler;
import com.silvaniastudios.travellers.client.gui.GuiShipyard;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class GuiHandler implements IGuiHandler {

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		TileEntity te = world.getTileEntity(new BlockPos(x, y, z));
		if (te != null) {
			if (ID == 1) {
				return new ShipyardContainer(player.inventory, (ShipyardEntity) te);
			}
			if (ID == 2) {
				return new AssemblerContainer(player.inventory, (AssemblerEntity) te);
			}
		}
		return null;
	}
	
	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		TileEntity te = world.getTileEntity(new BlockPos(x, y, z));
		if (te != null) {
			if (ID == 1) {
				ShipyardEntity entity = (ShipyardEntity) te;
				return new GuiShipyard(entity, new ShipyardContainer(player.inventory, entity));
			}
			if (ID == 2) {
				AssemblerEntity entity = (AssemblerEntity) te;
				return new GuiAssembler(entity, new AssemblerContainer(player.inventory, entity));
			}
		}
		return null;
	}
}
