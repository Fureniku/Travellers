package com.silvaniastudios.travellers.capability.playerData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import com.silvaniastudios.travellers.Travellers;
import com.silvaniastudios.travellers.capability.schematicData.ISchematicData;
import com.silvaniastudios.travellers.capability.schematicData.SchematicDataProvider;
import com.silvaniastudios.travellers.data.SchematicFixedData.SchematicCategories;
import com.silvaniastudios.travellers.data.SchematicFixedData.SchematicCategories.EnumMethod;
import com.silvaniastudios.travellers.entity.EntityScannerLine;
import com.silvaniastudios.travellers.items.schematic.SchematicTypeEnum;

import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * 
 * @author jamesm2w
 */
public class PlayerData implements IPlayerData {

	private int knowledgeBalance;
	private String shipyardVisitorCode;
	private boolean isDev;
	private EntityScannerLine entityScanning;
	private HashMap<String, Integer> knowledgeNodeUses;
	private ArrayList<String> knownLorePieces;
	private ArrayList<ItemStack> schematicList;
	private HashMap<String, ItemStack> shipDesignList;
	private ArrayList<String> scannedObjects;

	public PlayerData() {
		this.knowledgeBalance = 0;
		this.shipyardVisitorCode = "1234";
		this.isDev = false;
		this.entityScanning = null;
		this.knowledgeNodeUses = new HashMap<String, Integer>();
		this.knownLorePieces = new ArrayList<String>();
		this.scannedObjects = new ArrayList<String>();

		this.schematicList = new ArrayList<ItemStack>();
		this.shipDesignList = new HashMap<String, ItemStack>();
	}

	@Override
	public void setKnowledgeBalance(int knowledgeBalance) {
		this.knowledgeBalance = knowledgeBalance;
	}

	@Override
	public int incrementKnowledgeBalance(int increase) {
		this.knowledgeBalance += increase;
		return this.knowledgeBalance;
	}

	@Override
	public int getKnowledgeBalance() {
		return this.knowledgeBalance;
	}

	@Override
	public void setShipyardVisitorCode(String code) {
		this.shipyardVisitorCode = code;
	}

	@Override
	public String getShipyardVisitorCode() {
		return this.shipyardVisitorCode;
	}

	@Override
	public String generateNewShipyardVisitorCode() {
		this.shipyardVisitorCode = "9876"; // TODO: randomise this
		return this.shipyardVisitorCode;
	}

	/*
	 * isDev
	 * 
	 * 
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.silvaniastudios.travellers.capability.playerData.IPlayerData#isMale()
	 */

	@Override
	public boolean isDev() {
		return this.isDev;
	}

	@Override
	public void setDev(boolean isDev) {
		this.isDev = isDev;
	}

	@Override
	public boolean isScanning() {
		return this.entityScanning != null;
	}

	@Override
	public EntityScannerLine getScanningEntity() {
		return this.entityScanning;
	}

	@Override
	public void setScanning(EntityScannerLine scanning) {
		this.entityScanning = scanning;
	}

	/*
	 * knowledgeNodeUses
	 * 
	 * (non-Javadoc)
	 * 
	 * @see com.silvaniastudios.travellers.capability.playerData.IPlayerData#
	 * getKnowledgeNodeUses()
	 */

	@Override
	public HashMap<String, Integer> getKnowledgeTreeUses() {
		return this.knowledgeNodeUses;
	}

	public int getKnowledgeNodeUsage(String nodeKey) {
		if (this.knowledgeNodeUses.get(nodeKey) != null) {
			return this.knowledgeNodeUses.get(nodeKey);
		} else {
			return 0;
		}
	}

	@Override
	public void useKnowlegeNode(String nodeKey) {
		if (this.knowledgeNodeUses.containsKey(nodeKey)) {
			int value = this.knowledgeNodeUses.get(nodeKey) + 1;

			this.knowledgeNodeUses.replace(nodeKey, value);
		} else {

			this.knowledgeNodeUses.put(nodeKey, 1);
		}
		System.out.println(this.knowledgeNodeUses.toString());
	}

	@Override
	public HashMap<String, Integer> setKnowledgeNodeUses(String nodeKey, int times) {
		if (this.knowledgeNodeUses.containsKey(nodeKey)) {
			this.knowledgeNodeUses.replace(nodeKey, times);
		}

		return this.knowledgeNodeUses;
	}

	@Override
	public ArrayList<String> getKnownLorePieces() {
		return this.knownLorePieces;
	}

	@Override
	public void learnLorePiece(String loreID) {

		if (Travellers.CODEX_DATA.piecesByGuid.get(loreID) == null) {
			return;
		}

		this.knownLorePieces.add(loreID);

		this.incrementKnowledgeBalance(Travellers.CODEX_DATA.piecesByGuid.get(loreID).knowledge);
	}

	@Override
	public ArrayList<String> setKnownLorePieces(ArrayList<String> list) {
		this.knownLorePieces = list;
		return this.knownLorePieces;
	}

	@Override
	public NBTTagCompound toNBT() {
		NBTTagCompound nbtTag = new NBTTagCompound();
		nbtTag.setInteger("knowledge", this.getKnowledgeBalance());
		nbtTag.setBoolean("isDev", this.isDev());

		if (this.entityScanning != null) {
			nbtTag.setTag("entityScanning", this.entityScanning.serializeNBT());
		}

		nbtTag.setString("shipyardVisitorCode", this.getShipyardVisitorCode());

		NBTTagCompound knowledgeTreeUses = new NBTTagCompound();
		for (String key : this.getKnowledgeTreeUses().keySet()) {
			knowledgeTreeUses.setInteger(key, this.getKnowledgeTreeUses().get(key));
		}

		int index = 0;
		NBTTagCompound knownLorePieces = new NBTTagCompound();
		for (String key : this.getKnownLorePieces()) {
			knownLorePieces.setInteger(key, index++);
		}

		index = 0;
		NBTTagCompound learntSchematics = new NBTTagCompound();
		for (ItemStack schematic : this.schematicList) {
			learntSchematics.setTag(String.format("Schematic%d", index++), schematic.serializeNBT());
		}

		nbtTag.setTag("learntSchematicList", learntSchematics);
		nbtTag.setTag("knowledgeTreeUses", knowledgeTreeUses);
		nbtTag.setTag("knownLorePieces", knownLorePieces);

		NBTTagCompound scannedObjects = new NBTTagCompound();
		index = 0;
		for (String object : this.scannedObjects) {
			scannedObjects.setString(String.valueOf(index), object);
			index++;
		}
		nbtTag.setTag("scannedObjects", scannedObjects);

		NBTTagCompound shipDesigns = new NBTTagCompound();
		for (String key : this.shipDesignList.keySet()) {
			shipDesigns.setTag(key, this.shipDesignList.get(key).serializeNBT());
		}

		nbtTag.setTag("shipDesignList", shipDesigns);

		return nbtTag;
	}

	@Override
	public void fromNBT(NBTBase nbt) {

		NBTTagCompound nbtTag = (NBTTagCompound) nbt;

		this.setKnowledgeBalance(nbtTag.getInteger("knowledge"));
		this.setDev(nbtTag.getBoolean("isDev"));

		if (nbtTag.hasKey("entityScanning") && nbtTag.getTag("entityScanning") != null) {

			NBTTagCompound entityTag = nbtTag.getCompoundTag("entityScanning");

			World world = null;

			if (FMLCommonHandler.instance().getMinecraftServerInstance() != null) {
				world = FMLCommonHandler.instance().getMinecraftServerInstance().getEntityWorld();
			} else if (FMLCommonHandler.instance().getSide() == Side.CLIENT) {
				world = clientGetWorld();
			}

			UUID player = entityTag.getUniqueId("player");

			if (player.toString().contentEquals("00000000-0000-0000-0000-000000000000")) {
				this.entityScanning = null;
			} else {
				this.entityScanning = new EntityScannerLine(world, player);
				this.entityScanning.deserializeNBT(nbtTag.getCompoundTag("entityScanning"));
			}
		} else {
			this.entityScanning = null;
		}

		this.setShipyardVisitorCode(nbtTag.getString("shipyardVisitorCode"));

		for (String key : ((NBTTagCompound) nbtTag.getCompoundTag("knowledgeTreeUses")).getKeySet()) {
			this.setKnowledgeNodeUses(key, ((NBTTagCompound) nbtTag.getTag("knowledgeTreeUses")).getInteger(key));
		}

		ArrayList<String> knownLorePieces = new ArrayList<String>();
		for (String key : nbtTag.getCompoundTag("knownLorePieces").getKeySet()) {
			knownLorePieces.add(key);
		}

		ArrayList<ItemStack> schematicList = new ArrayList<ItemStack>();
		for (String key : nbtTag.getCompoundTag("learntSchematicList").getKeySet()) {
			schematicList.add(new ItemStack(nbtTag.getCompoundTag("learntSchematicList").getCompoundTag(key)));
			;
		}

		HashMap<String, ItemStack> designList = new HashMap<String, ItemStack>();
		for (String key : nbtTag.getCompoundTag("shipDesignList").getKeySet()) {
			designList.put(key, new ItemStack(nbtTag.getCompoundTag("shipDesignList").getCompoundTag(key)));
		}

		ArrayList<String> scannedObjects = new ArrayList<String>();
		for (String key : nbtTag.getCompoundTag("scannedObjects").getKeySet()) {
			scannedObjects.add(nbtTag.getCompoundTag("scannedObjects").getString(key));
		}

		HashMap<String, Integer> knowledgeTree = new HashMap<String, Integer>();
		for (String key : nbtTag.getCompoundTag("knowledgeTreeUses").getKeySet()) {
			knowledgeTree.put(key, nbtTag.getCompoundTag("knowledgeTreeUses").getInteger(key));
		}

		this.scannedObjects = scannedObjects;
		this.shipDesignList = designList;
		this.schematicList = schematicList;
		this.knowledgeNodeUses = knowledgeTree;
		this.setKnownLorePieces(knownLorePieces);

	}

	@SideOnly(Side.CLIENT)
	public World clientGetWorld() {
		return Minecraft.getMinecraft().world;
	}

	@Override
	public void copyData(IPlayerData capability) {
		this.setKnowledgeBalance(capability.getKnowledgeBalance());

		this.setDev(capability.isDev());

		this.setScanning(capability.getScanningEntity());

		this.setShipyardVisitorCode(capability.getShipyardVisitorCode());

		for (String key : capability.getKnowledgeTreeUses().keySet()) {
			this.setKnowledgeNodeUses(key, capability.getKnowledgeTreeUses().get(key));
		}

		this.schematicList = capability.getSchematicList();

		this.shipDesignList = capability.getShipDesignSchematics();

		this.setKnownLorePieces(capability.getKnownLorePieces());

		this.knowledgeNodeUses = capability.getKnowledgeTreeUses();
	}

	@Override
	public HashMap<String, ItemStack> getShipDesignSchematics() {
		return this.shipDesignList;
	}

	@Override
	public ItemStack editShipDesignSchematic(String designKey) {
		return this.shipDesignList.get(designKey);
	}

	@Override
	public boolean learnSchematic(ItemStack schematic) {
		if (!hasLearntSchematic(schematic)) {

			EnumMethod method = schematic.getCapability(SchematicDataProvider.SCHEMATIC_DATA, null).getCategories()
					.getMethod();

			int maxSize = (method == EnumMethod.ASSEMBLER) ? 25 : 15;
			maxSize += getKnowledgeNodeUsage("increase_slots");

			System.out.println("Max Schematic Size: " + String.valueOf(maxSize));
			System.out.println("Type: " + method.toString());

			if (method == EnumMethod.ASSEMBLER && getAssemblerSchematics().size() <= maxSize) {
				this.schematicList.add(schematic);
				return true;
			} else if (method == EnumMethod.MULTITOOL && getMultitoolSchematics().size() <= maxSize) {
				this.schematicList.add(schematic);
				return true;
			}
		}

		return false;
	}

	@Override
	public ItemStack unlearnSchematic(ItemStack schematic) {

		return schematic;
	}

	@Override
	public ArrayList<ItemStack> getSchematicList() {
		return this.schematicList;
	}

	public ArrayList<ItemStack> getMultitoolSchematics() {
		ArrayList<ItemStack> filtered = new ArrayList<ItemStack>();
		for (ItemStack schematic : this.schematicList) {
			if (schematic.getCapability(SchematicDataProvider.SCHEMATIC_DATA, null).getCategories()
					.getMethod() == EnumMethod.MULTITOOL) {
				filtered.add(schematic);
			}
		}

		return filtered;
	}

	public ArrayList<ItemStack> getAssemblerSchematics() {
		ArrayList<ItemStack> filtered = new ArrayList<ItemStack>();
		for (ItemStack schematic : this.schematicList) {
			if (schematic.getCapability(SchematicDataProvider.SCHEMATIC_DATA, null).getCategories()
					.getMethod() == EnumMethod.ASSEMBLER) {
				filtered.add(schematic);
			}
		}

		return filtered;
	}

	public ArrayList<ItemStack> getSchematicsWithCategory(SchematicCategories.EnumMethod craftingMethod,
			String category) {
		ArrayList<ItemStack> filtered = new ArrayList<ItemStack>();
		for (ItemStack schematic : this.schematicList) {
			SchematicCategories schemCategories = schematic.getCapability(SchematicDataProvider.SCHEMATIC_DATA, null)
					.getCategories();

			if (schemCategories.getMethod() == craftingMethod && schemCategories.contains(category)) {
				filtered.add(schematic);
			}
		}

		return filtered;
	}

	@Override
	public boolean hasLearntSchematic(ItemStack schematic) {

		ISchematicData schematicData = schematic.getCapability(SchematicDataProvider.SCHEMATIC_DATA, null);

		UUID schematicUUID = schematicData.getUUID();

		for (ItemStack item : this.schematicList) {
			ISchematicData itemData = item.getCapability(SchematicDataProvider.SCHEMATIC_DATA, null);
			UUID currentItemUUID = itemData.getUUID();

			// System.out.println(String.valueOf(schematicUUID.compareTo(currentItemUUID)));
			// System.out.println("Schematic: " + schematicUUID.toString());
			// System.out.println("Current I: " + currentItemUUID.toString());
			if (schematicData.getType() == SchematicTypeEnum.FIXED
					&& schematicData.getName().equals(itemData.getName())) {
				return true;
			}

			if (schematicUUID.compareTo(currentItemUUID) == 0) {
				return true;
			}
		}

		return false;
	}

	@Override
	public boolean scanObject(String unlocalizedString) {
		if (!hasScannedObject(unlocalizedString)) {
			this.scannedObjects.add(unlocalizedString.toLowerCase());
			return true;
		}

		return false;
	}

	@Override
	public ArrayList<String> getScannedObjects() {
		return this.scannedObjects;
	}

	@Override
	public boolean hasScannedObject(String unlocalizedString) {
		return this.scannedObjects.contains(unlocalizedString.toLowerCase());
	}

}
