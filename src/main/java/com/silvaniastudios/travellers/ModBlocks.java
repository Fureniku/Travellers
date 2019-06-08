package com.silvaniastudios.travellers;

import com.silvaniastudios.travellers.blocks.TravellersOre;
import com.silvaniastudios.travellers.blocks.tileentity.shipyard.ShipyardBlockCore;
import com.silvaniastudios.travellers.blocks.tileentity.shipyard.ShipyardBlockParts;
import com.silvaniastudios.travellers.blocks.tileentity.shipyard.ShipyardBlockPartsFlap;
import com.silvaniastudios.travellers.blocks.tileentity.shipyard.ShipyardBlockPartsRamp;
import com.silvaniastudios.travellers.blocks.tileentity.shipyard.ShipyardEntity;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.IForgeRegistry;

public class ModBlocks {
	
	public static TravellersOre aluminium_ore = new TravellersOre("ore_aluminium", ModItems.aluminium_scrap);
	public static TravellersOre titanium_ore = new TravellersOre("ore_titanium", ModItems.titanium_scrap);
	public static TravellersOre tin_ore = new TravellersOre("ore_tin", ModItems.tin_scrap);
	public static TravellersOre iron_ore = new TravellersOre("ore_iron", ModItems.iron_scrap);
	public static TravellersOre bronze_ore = new TravellersOre("ore_bronze", ModItems.bronze_scrap);
	public static TravellersOre nickel_ore = new TravellersOre("ore_nickel", ModItems.nickel_scrap);
	public static TravellersOre orthite_ore = new TravellersOre("ore_orthite", ModItems.orthite_scrap);
	public static TravellersOre epilar_ore = new TravellersOre("ore_epilar", ModItems.epilar_scrap);
	public static TravellersOre steel_ore = new TravellersOre("ore_steel", ModItems.steel_scrap);
	public static TravellersOre eternium_ore = new TravellersOre("ore_eternium", ModItems.eternium_scrap);
	public static TravellersOre copper_ore = new TravellersOre("ore_copper", ModItems.copper_scrap);
	public static TravellersOre lead_ore = new TravellersOre("ore_lead", ModItems.lead_scrap);
	public static TravellersOre silver_ore = new TravellersOre("ore_silver", ModItems.silver_scrap);
	public static TravellersOre tungsten_ore = new TravellersOre("ore_tungsten", ModItems.tungsten_scrap);
	public static TravellersOre gold_ore = new TravellersOre("ore_gold", ModItems.gold_scrap);
	
	public static ShipyardBlockCore block_shipyard_core = new ShipyardBlockCore("block_shipyard_core");
	public static ShipyardBlockParts block_shipyard_parts = new ShipyardBlockParts("block_shipyard_parts");
	public static ShipyardBlockPartsFlap block_shipyard_parts_flap = new ShipyardBlockPartsFlap("block_shipyard_parts_flap");
	public static ShipyardBlockPartsRamp block_shipyard_parts_ramp = new ShipyardBlockPartsRamp("block_shipyard_parts_ramp");
	
	public static void register(IForgeRegistry<Block> registry) {
		registry.registerAll(
				aluminium_ore,
				titanium_ore,
				tin_ore,
				iron_ore,
				bronze_ore,
				nickel_ore,
				orthite_ore,
				epilar_ore,
				steel_ore,
				eternium_ore,
				copper_ore,
				lead_ore,
				silver_ore,
				tungsten_ore,
				gold_ore,
				
				block_shipyard_core,
				block_shipyard_parts,
				block_shipyard_parts_flap,
				block_shipyard_parts_ramp
			);
	}
	
	public static void registerItemBlocks(IForgeRegistry<Item> registry) {
		registry.register(new ItemBlock(aluminium_ore).setRegistryName(aluminium_ore.getRegistryName()));
		registry.register(new ItemBlock(titanium_ore).setRegistryName(titanium_ore.getRegistryName()));
		registry.register(new ItemBlock(tin_ore).setRegistryName(tin_ore.getRegistryName()));
		registry.register(new ItemBlock(iron_ore).setRegistryName(iron_ore.getRegistryName()));
		registry.register(new ItemBlock(bronze_ore).setRegistryName(bronze_ore.getRegistryName()));
		registry.register(new ItemBlock(nickel_ore).setRegistryName(nickel_ore.getRegistryName()));
		registry.register(new ItemBlock(orthite_ore).setRegistryName(orthite_ore.getRegistryName()));
		registry.register(new ItemBlock(epilar_ore).setRegistryName(epilar_ore.getRegistryName()));
		registry.register(new ItemBlock(steel_ore).setRegistryName(steel_ore.getRegistryName()));
		registry.register(new ItemBlock(eternium_ore).setRegistryName(eternium_ore.getRegistryName()));
		registry.register(new ItemBlock(copper_ore).setRegistryName(copper_ore.getRegistryName()));
		registry.register(new ItemBlock(lead_ore).setRegistryName(lead_ore.getRegistryName()));
		registry.register(new ItemBlock(silver_ore).setRegistryName(silver_ore.getRegistryName()));
		registry.register(new ItemBlock(tungsten_ore).setRegistryName(tungsten_ore.getRegistryName()));
		registry.register(new ItemBlock(gold_ore).setRegistryName(gold_ore.getRegistryName()));
		
		registry.register(new ItemBlock(block_shipyard_core).setRegistryName(block_shipyard_core.getRegistryName()));
		registry.register(new ItemBlock(block_shipyard_parts).setRegistryName(block_shipyard_parts.getRegistryName()));
		registry.register(new ItemBlock(block_shipyard_parts_flap).setRegistryName(block_shipyard_parts_flap.getRegistryName()));
		registry.register(new ItemBlock(block_shipyard_parts_ramp).setRegistryName(block_shipyard_parts_ramp.getRegistryName()));
	}

	public static void registerModels() {
		aluminium_ore.initModel();
		titanium_ore.initModel();
		tin_ore.initModel();
		iron_ore.initModel();
		bronze_ore.initModel();
		nickel_ore.initModel();
		orthite_ore.initModel();
		epilar_ore.initModel();
		steel_ore.initModel();
		eternium_ore.initModel();
		copper_ore.initModel();
		lead_ore.initModel();
		silver_ore.initModel();
		tungsten_ore.initModel();
		gold_ore.initModel();
		
		block_shipyard_core.initModel();
		block_shipyard_parts.initModel();
		block_shipyard_parts_flap.initModel();
		block_shipyard_parts_ramp.initModel();
	}
	
	@SuppressWarnings("deprecation")
	public static void registerTileEntities() {
		GameRegistry.registerTileEntity(ShipyardEntity.class, Travellers.MODID + ":shipyard_entity");
	}
	
	@SideOnly(Side.CLIENT)
	public static void initModel(Block block) {
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(block), 0, new ModelResourceLocation(Travellers.MODID + ":" + block.getUnlocalizedName().substring(5), "inventory"));
	}
}
