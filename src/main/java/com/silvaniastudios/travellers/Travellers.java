package com.silvaniastudios.travellers;

import com.silvaniastudios.travellers.commands.SchematicDataViewer;
import com.silvaniastudios.travellers.commands.SchematicListViewer;
import com.silvaniastudios.travellers.entity.EntityScannerLine;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.EntityEntryBuilder;

@Mod(modid = Travellers.MODID, version = Travellers.VERSION, updateJSON = "http://www.silvaniastudios.com/mods/update/travellers.json")
public class Travellers {

	public static final String MODID = "travellers";
	public static final String VERSION = "0.0.1";

	@Instance(MODID)
	public static Travellers instance;

	@SidedProxy(clientSide = "com.silvaniastudios.travellers.client.ClientProxy", serverSide = "com.silvaniastudios.travellers.CommonProxy")
	public static CommonProxy proxy;

	public static final CreativeTabs tabTravellers = new CreativeTabs("travellers") {
		@Override
		public ItemStack getTabIconItem() {
			return new ItemStack(ModItems.atlas_shard, 1, 0);
		}
	};

	public static final CreativeTabs tabSchematics = new CreativeTabs("schematics") {
		public ItemStack getTabIconItem() {
			return new ItemStack(ModItems.default_schematic, 1, 0);
		}
	};

	public static final CreativeTabs tabLore = new CreativeTabs("lore") {
		@Override
		public ItemStack getTabIconItem() {
			return new ItemStack(ModItems.default_codex, 1, 0);
		}

		public boolean hasSearchBar() {
			return true;
		};
	}.setBackgroundImageName("item_search.png");

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		// TODO GameRegistry.registerWorldGenerator(new WorldGen(), 3);
		proxy.preInit(event);
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		NetworkRegistry.INSTANCE.registerGuiHandler(Travellers.instance, new GuiHandler());
		proxy.init(event);
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		MinecraftForge.EVENT_BUS.register(new GuiHandler());
	}

	@EventHandler
	public void serverStarting(FMLServerStartingEvent event) {
		// commands
		event.registerServerCommand(new SchematicDataViewer());
		event.registerServerCommand(new SchematicListViewer());
	}

	@Mod.EventBusSubscriber
	public static class RegistrationHandler {

		@SubscribeEvent
		public static void registerItems(RegistryEvent.Register<Item> event) {
			ModItems.register(event.getRegistry());
			ModBlocks.registerItemBlocks(event.getRegistry());
		}

		@SubscribeEvent
		public static void registerModels(ModelRegistryEvent event) {
			ModItems.registerModels();
			ModBlocks.registerModels();
		}

		@SubscribeEvent
		public static void registerBlocks(RegistryEvent.Register<Block> event) {
			ModBlocks.register(event.getRegistry());
			ModBlocks.registerTileEntities();
		}

		@SubscribeEvent
		public static void registerEntities(RegistryEvent.Register<EntityEntry> event) {
			int id = 0;
			EntityEntry SCANNERLINE = EntityEntryBuilder.create().entity(EntityScannerLine.class)
					.id(new ResourceLocation(Travellers.MODID, "scanner_line"), id++).tracker(64, 1, false)
					.name("scanner_line").build();

			event.getRegistry().register(SCANNERLINE);

		}
	}
}
