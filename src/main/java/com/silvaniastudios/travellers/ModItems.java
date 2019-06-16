package com.silvaniastudios.travellers;

import com.silvaniastudios.travellers.items.ItemAssembler;
import com.silvaniastudios.travellers.items.ItemBasic;
import com.silvaniastudios.travellers.items.ItemScrapMetal;
import com.silvaniastudios.travellers.items.ItemShipyard;
import com.silvaniastudios.travellers.items.tools.ItemGrapple;
import com.silvaniastudios.travellers.items.tools.ItemRepairTool;
import com.silvaniastudios.travellers.items.tools.ItemSalvager;
import com.silvaniastudios.travellers.items.tools.ItemScanner;
import com.silvaniastudios.travellers.items.tools.ItemShipyardTool;

import net.minecraft.item.Item;
import net.minecraftforge.registries.IForgeRegistry;

public class ModItems {
	
	public static ItemBasic atlas_shard = new ItemBasic("shard_atlas");
	
	public static ItemScrapMetal aluminium_scrap = new ItemScrapMetal("scrap_aluminium");
	public static ItemScrapMetal titanium_scrap = new ItemScrapMetal("scrap_titanium");
	public static ItemScrapMetal tin_scrap = new ItemScrapMetal("scrap_tin");
	public static ItemScrapMetal iron_scrap = new ItemScrapMetal("scrap_iron");
	public static ItemScrapMetal bronze_scrap = new ItemScrapMetal("scrap_bronze");
	public static ItemScrapMetal nickel_scrap = new ItemScrapMetal("scrap_nickel");
	public static ItemScrapMetal orthite_scrap = new ItemScrapMetal("scrap_orthite");
	public static ItemScrapMetal epilar_scrap = new ItemScrapMetal("scrap_epilar");
	public static ItemScrapMetal steel_scrap = new ItemScrapMetal("scrap_steel");
	public static ItemScrapMetal eternium_scrap = new ItemScrapMetal("scrap_eternium");
	public static ItemScrapMetal copper_scrap = new ItemScrapMetal("scrap_copper");
	public static ItemScrapMetal lead_scrap = new ItemScrapMetal("scrap_lead");
	public static ItemScrapMetal silver_scrap = new ItemScrapMetal("scrap_silver");
	public static ItemScrapMetal tungsten_scrap = new ItemScrapMetal("scrap_tungsten");
	public static ItemScrapMetal gold_scrap = new ItemScrapMetal("scrap_gold");
	
	public static ItemGrapple grapple = new ItemGrapple("grapple");
	public static ItemSalvager salvager = new ItemSalvager("gauntlet_salvager");
	public static ItemRepairTool repair_tool = new ItemRepairTool("gauntlet_repair_tool");
	public static ItemShipyardTool shipyard_tool = new ItemShipyardTool("gauntlet_shipyard_tool");
	public static ItemScanner scanner = new ItemScanner("gauntlet_scanner");
	
	public static ItemShipyard shipyard = new ItemShipyard("shipyard");
	public static ItemAssembler assembling_station = new ItemAssembler("assembling_station");
	
	//Unregistered stub items
	public static ItemBasic paint_barel = new ItemBasic("paint_barrel");
	public static ItemBasic paint_bucket = new ItemBasic("paint_bucket");

	public static void register(IForgeRegistry<Item> registry) {
		registry.register(atlas_shard);
		
		registry.register(aluminium_scrap);
		registry.register(titanium_scrap);
		registry.register(tin_scrap);
		registry.register(iron_scrap);
		registry.register(bronze_scrap);
		registry.register(nickel_scrap);
		registry.register(orthite_scrap);
		registry.register(epilar_scrap);
		registry.register(steel_scrap);
		registry.register(eternium_scrap);
		registry.register(copper_scrap);
		registry.register(lead_scrap);
		registry.register(silver_scrap);
		registry.register(tungsten_scrap);
		registry.register(gold_scrap);
		
		registry.register(grapple);
		registry.register(salvager);
		registry.register(repair_tool);
		registry.register(shipyard_tool);
		registry.register(scanner);
		
		registry.register(shipyard);
		registry.register(assembling_station);
	}
	
	public static void registerModels() {
		atlas_shard.registerItemModel();
		
		aluminium_scrap.registerItemModel();
		titanium_scrap.registerItemModel();
		tin_scrap.registerItemModel();
		iron_scrap.registerItemModel();
		bronze_scrap.registerItemModel();
		nickel_scrap.registerItemModel();
		orthite_scrap.registerItemModel();
		epilar_scrap.registerItemModel();
		steel_scrap.registerItemModel();
		eternium_scrap.registerItemModel();
		copper_scrap.registerItemModel();
		lead_scrap.registerItemModel();
		silver_scrap.registerItemModel();
		tungsten_scrap.registerItemModel();
		gold_scrap.registerItemModel();
		
		grapple.registerItemModel();
		salvager.registerItemModel();
		repair_tool.registerItemModel();
		shipyard_tool.registerItemModel();
		scanner.registerItemModel();
		
		shipyard.registerItemModel();
		assembling_station.registerItemModel();
	}

}
