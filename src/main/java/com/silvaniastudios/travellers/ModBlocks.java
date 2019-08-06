package com.silvaniastudios.travellers;

import java.util.ArrayList;
import java.util.List;

import com.silvaniastudios.travellers.blocks.TravellersOre;

import com.silvaniastudios.travellers.blocks.databank.BlockDatabank;
import com.silvaniastudios.travellers.blocks.databank.DatabankRarityEnum;
import com.silvaniastudios.travellers.blocks.databank.TileEntityDatabank;
import com.silvaniastudios.travellers.blocks.tileentity.assembler.AssemblerBlock;
import com.silvaniastudios.travellers.blocks.tileentity.assembler.AssemblerEntity;

import com.silvaniastudios.travellers.blocks.tileentity.shipyard.ShipyardBlockCore;
import com.silvaniastudios.travellers.blocks.tileentity.shipyard.ShipyardBlockParts;
import com.silvaniastudios.travellers.blocks.tileentity.shipyard.ShipyardBlockPartsFlap;
import com.silvaniastudios.travellers.blocks.tileentity.shipyard.ShipyardBlockPartsRamp;
import com.silvaniastudios.travellers.blocks.tileentity.shipyard.ShipyardEntity;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
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
	public static TravellersOre magnesium_ore = new TravellersOre("ore_magnesium", ModItems.magnesium_scrap);
	public static TravellersOre palladium_ore = new TravellersOre("ore_palladium", ModItems.palladium_scrap);
	public static TravellersOre platinum_ore = new TravellersOre("ore_platinum", ModItems.platinum_scrap);
	
	public static List<BlockDatabank> block_databanks = new ArrayList<BlockDatabank>();
	public static List<Item> item_databanks = new ArrayList<Item>();
	
	public static BlockDatabank databank_common = new BlockDatabank("databank_common", DatabankRarityEnum.COMMON);
	public static BlockDatabank databank_uncommon = new BlockDatabank("databank_uncommon", DatabankRarityEnum.UNCOMMON);
	public static BlockDatabank databank_rare = new BlockDatabank("databank_rare", DatabankRarityEnum.RARE);
	public static BlockDatabank databank_exotic = new BlockDatabank("databank_exotic", DatabankRarityEnum.EXOTIC);
	
	public static ShipyardBlockCore block_shipyard_core = new ShipyardBlockCore("block_shipyard_core");
	public static ShipyardBlockParts block_shipyard_parts = new ShipyardBlockParts("block_shipyard_parts");
	public static ShipyardBlockPartsFlap block_shipyard_parts_flap = new ShipyardBlockPartsFlap("block_shipyard_parts_flap");
	public static ShipyardBlockPartsRamp block_shipyard_parts_ramp = new ShipyardBlockPartsRamp("block_shipyard_parts_ramp");
	
	public static AssemblerBlock assembling_station = new AssemblerBlock("assembling_station_block");
	
	public static void register(IForgeRegistry<Block> registry) {
		GameRegistry.registerTileEntity(TileEntityDatabank.class, new ResourceLocation(Travellers.MODID + ":databank"));

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
				magnesium_ore,
				palladium_ore,
				platinum_ore,
				
				block_shipyard_core,
				block_shipyard_parts,
				block_shipyard_parts_flap,
				block_shipyard_parts_ramp,
				
				assembling_station
			);
		
		registry.registerAll(block_databanks.toArray(new Block[0]));
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
		registry.register(new ItemBlock(magnesium_ore).setRegistryName(magnesium_ore.getRegistryName()));
		registry.register(new ItemBlock(palladium_ore).setRegistryName(palladium_ore.getRegistryName()));
		registry.register(new ItemBlock(platinum_ore).setRegistryName(platinum_ore.getRegistryName()));
		
		registry.registerAll(item_databanks.toArray(new Item[0]));

		registry.register(new ItemBlock(block_shipyard_core).setRegistryName(block_shipyard_core.getRegistryName()));
		registry.register(new ItemBlock(block_shipyard_parts).setRegistryName(block_shipyard_parts.getRegistryName()));
		registry.register(new ItemBlock(block_shipyard_parts_flap).setRegistryName(block_shipyard_parts_flap.getRegistryName()));
		registry.register(new ItemBlock(block_shipyard_parts_ramp).setRegistryName(block_shipyard_parts_ramp.getRegistryName()));
		
		registry.register(new ItemBlock(assembling_station).setRegistryName(assembling_station.getRegistryName()));
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
		magnesium_ore.initModel();
		palladium_ore.initModel();
		platinum_ore.initModel();

		for (BlockDatabank block : block_databanks) {

			block.initModel();
		}
		
		block_shipyard_core.initModel();
		block_shipyard_parts.initModel();
		block_shipyard_parts_flap.initModel();
		block_shipyard_parts_ramp.initModel();
		
		assembling_station.initModel();
	}
	
	@SuppressWarnings("deprecation")
	public static void registerTileEntities() {
		GameRegistry.registerTileEntity(ShipyardEntity.class, Travellers.MODID + ":shipyard_entity");
		GameRegistry.registerTileEntity(AssemblerEntity.class, Travellers.MODID + ":assembler_entity");
	}
	
	@SideOnly(Side.CLIENT)
	public static void initModel(Block block) {
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(block), 0, new ModelResourceLocation(Travellers.MODID + ":" + block.getUnlocalizedName().substring(5), "inventory"));
	}
}