package com.silvaniastudios.travellers;

import java.util.ArrayList;
import java.util.List;

import com.silvaniastudios.travellers.items.CodexItems;
import com.silvaniastudios.travellers.items.ItemAssembler;
import com.silvaniastudios.travellers.items.ItemBasic;
import com.silvaniastudios.travellers.items.ItemCodex;
import com.silvaniastudios.travellers.items.ItemScrapMetal;
import com.silvaniastudios.travellers.items.ItemShipyard;
import com.silvaniastudios.travellers.items.ItemWoodPlanks;
import com.silvaniastudios.travellers.items.tools.ItemGrapple;
import com.silvaniastudios.travellers.items.tools.ItemRepairTool;
import com.silvaniastudios.travellers.items.tools.ItemSalvager;
import com.silvaniastudios.travellers.items.tools.ItemScanner;
import com.silvaniastudios.travellers.items.tools.ItemShipyardTool;
import com.silvaniastudios.travellers.schematic.ItemSchematic;
import com.silvaniastudios.travellers.schematic.SchematicItems;
import com.silvaniastudios.travellers.schematic.SchematicRarityEnum;
import com.silvaniastudios.travellers.schematic.SchematicTypeEnum;

import net.minecraft.item.Item;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.registries.IForgeRegistry;

public class ModItems {

	public static final ToolMaterial ANCIENT_SALVAGER = EnumHelper.addToolMaterial("ANCIENT_SALVAGER", 3, 2000, 20.0F,
			5.0F, 0);

	public static ItemBasic atlas_shard = new ItemBasic("shard_atlas");

	public static List<ItemSchematic> schematics = new ArrayList<ItemSchematic>();
	public static SchematicItems parsed_schematics = new SchematicItems();

	public static ItemSchematic default_schematic = new ItemSchematic("default_schematic");

	public static ItemSchematic common_engine = new ItemSchematic("engine.common", SchematicRarityEnum.COMMON,
			SchematicTypeEnum.ENGINE, new String[] {}, true, "schematic_default");

	public static ItemSchematic uncommon_engine = new ItemSchematic("engine.uncommon", SchematicRarityEnum.UNCOMMON,
			SchematicTypeEnum.ENGINE, new String[] {}, true, "schematic_default");

	public static ItemSchematic rare_engine = new ItemSchematic("engine.rare", SchematicRarityEnum.RARE,
			SchematicTypeEnum.ENGINE, new String[] {}, true, "schematic_default");

	public static ItemSchematic exotic_engine = new ItemSchematic("engine.exotic", SchematicRarityEnum.EXOTIC,
			SchematicTypeEnum.ENGINE, new String[] {}, true, "schematic_default");

	public static ItemSchematic common_wing = new ItemSchematic("wing.common", SchematicRarityEnum.COMMON,
			SchematicTypeEnum.WING, new String[] {}, true, "schematic_default");

	public static ItemSchematic uncommon_wing = new ItemSchematic("wing.uncommon", SchematicRarityEnum.UNCOMMON,
			SchematicTypeEnum.WING, new String[] {}, true, "schematic_default");

	public static ItemSchematic rare_wing = new ItemSchematic("wing.rare", SchematicRarityEnum.RARE,
			SchematicTypeEnum.WING, new String[] {}, true, "schematic_default");

	public static ItemSchematic exotic_wing = new ItemSchematic("wing.exotic", SchematicRarityEnum.EXOTIC,
			SchematicTypeEnum.WING, new String[] {}, true, "schematic_default");

	public static ItemSchematic common_cannon = new ItemSchematic("cannon.common", SchematicRarityEnum.COMMON,
			SchematicTypeEnum.CANNON, new String[] {}, true, "schematic_default");

	public static ItemSchematic uncommon_cannon = new ItemSchematic("cannon.uncommon", SchematicRarityEnum.UNCOMMON,
			SchematicTypeEnum.CANNON, new String[] {}, true, "schematic_default");

	public static ItemSchematic rare_cannon = new ItemSchematic("cannon.rare", SchematicRarityEnum.RARE,
			SchematicTypeEnum.CANNON, new String[] {}, true, "schematic_default");

	public static ItemSchematic exotic_cannon = new ItemSchematic("cannon.exotic", SchematicRarityEnum.EXOTIC,
			SchematicTypeEnum.CANNON, new String[] {}, true, "schematic_default");

	public static ItemSchematic common_swivel = new ItemSchematic("swivel.common", SchematicRarityEnum.COMMON,
			SchematicTypeEnum.SWIVELCANNON, new String[] {}, true, "schematic_default");

	public static ItemSchematic uncommon_swivel = new ItemSchematic("swivel.uncommon", SchematicRarityEnum.UNCOMMON,
			SchematicTypeEnum.SWIVELCANNON, new String[] {}, true, "schematic_default");

	public static ItemSchematic rare_swivel = new ItemSchematic("swivel.rare", SchematicRarityEnum.RARE,
			SchematicTypeEnum.SWIVELCANNON, new String[] {}, true, "schematic_default");

	public static ItemSchematic exotic_swivel = new ItemSchematic("swivel.exotic", SchematicRarityEnum.EXOTIC,
			SchematicTypeEnum.SWIVELCANNON, new String[] {}, true, "schematic_default");

	public static CodexItems parsed_codex = new CodexItems();
	public static List<ItemCodex> codices = parsed_codex.generateItems();
	public static ItemCodex default_codex = new ItemCodex("default_codex");

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
	public static ItemScrapMetal magnesium_scrap = new ItemScrapMetal("scrap_magnesium");
	public static ItemScrapMetal palladium_scrap = new ItemScrapMetal("scrap_palladium");
	public static ItemScrapMetal platinum_scrap = new ItemScrapMetal("scrap_platinum");

	public static ItemWoodPlanks cedar_planks = new ItemWoodPlanks("planks_cedar");
	public static ItemWoodPlanks hemlock_planks = new ItemWoodPlanks("planks_hemlock");
	public static ItemWoodPlanks chestnut_planks = new ItemWoodPlanks("planks_chestnut");
	public static ItemWoodPlanks elm_planks = new ItemWoodPlanks("planks_elm");
	public static ItemWoodPlanks birch_planks = new ItemWoodPlanks("planks_birch");
	public static ItemWoodPlanks ash_planks = new ItemWoodPlanks("planks_ash");
	public static ItemWoodPlanks oak_planks = new ItemWoodPlanks("planks_oak");
	public static ItemWoodPlanks palm_planks = new ItemWoodPlanks("planks_palm");
	public static ItemWoodPlanks ebony_planks = new ItemWoodPlanks("planks_ebony");
	public static ItemWoodPlanks ironwood_planks = new ItemWoodPlanks("planks_ironwood");
	public static ItemWoodPlanks mahogony_planks = new ItemWoodPlanks("planks_mahogony");
	public static ItemWoodPlanks maple_planks = new ItemWoodPlanks("planks_maple");

	public static ItemGrapple grapple = new ItemGrapple("grapple");
	public static ItemSalvager salvager = new ItemSalvager("gauntlet_salvager");
	public static ItemRepairTool repair_tool = new ItemRepairTool("gauntlet_repair_tool");
	public static ItemShipyardTool shipyard_tool = new ItemShipyardTool("gauntlet_shipyard_tool");
	public static ItemScanner scanner = new ItemScanner("gauntlet_scanner");

	public static ItemShipyard shipyard = new ItemShipyard("shipyard");
	public static ItemAssembler assembling_station = new ItemAssembler("assembling_station");

	// Unregistered stub items
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

		registry.register(magnesium_scrap);
		registry.register(palladium_scrap);
		registry.register(platinum_scrap);

		registry.register(cedar_planks);
		registry.register(hemlock_planks);
		registry.register(chestnut_planks);
		registry.register(elm_planks);
		registry.register(birch_planks);
		registry.register(ash_planks);
		registry.register(oak_planks);
		registry.register(palm_planks);
		registry.register(ebony_planks);
		registry.register(ironwood_planks);
		registry.register(mahogony_planks);
		registry.register(maple_planks);

		registry.register(grapple);
		registry.register(salvager);
		registry.register(repair_tool);
		registry.register(shipyard_tool);
		registry.register(scanner);

		registry.register(shipyard);
		registry.register(assembling_station);

		registry.registerAll(schematics.toArray(new Item[] {}));
		registry.registerAll(codices.toArray(new Item[] {}));
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

		magnesium_scrap.registerItemModel();
		palladium_scrap.registerItemModel();
		platinum_scrap.registerItemModel();

		cedar_planks.registerItemModel();
		hemlock_planks.registerItemModel();
		chestnut_planks.registerItemModel();
		elm_planks.registerItemModel();
		birch_planks.registerItemModel();
		ash_planks.registerItemModel();
		oak_planks.registerItemModel();
		palm_planks.registerItemModel();

		ebony_planks.registerItemModel();
		ironwood_planks.registerItemModel();
		mahogony_planks.registerItemModel();
		maple_planks.registerItemModel();

		grapple.registerItemModel();
		salvager.registerItemModel();
		repair_tool.registerItemModel();
		shipyard_tool.registerItemModel();
		scanner.registerItemModel();

		shipyard.registerItemModel();
		assembling_station.registerItemModel();

		for (ItemSchematic schem : schematics) {
			schem.registerItemModel();
		}

		for (ItemCodex codex : codices) {
			codex.registerItemModel();
		}
	}

}
