package easyinventories.blocks;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import easyinventories.ModInfo;

public class TileEntityBlockSorting extends TileEntity implements ISidedInventory {

	private ItemStack [] contents;
	private int [] mainStorage;

	public TileEntityBlockSorting() {
		contents = new ItemStack [36];
		mainStorage = new int [36];

		for (int i = 0; i < mainStorage.length; i++) {
			mainStorage[i] = i;
		}
	}
	
	public ItemStack [] getContents() {
		return contents;
	}
	
	/**
	 * Writes contents of its inventory to NBT Data
	 */
	@Override
	public void writeToNBT(NBTTagCompound nbtTag) {
		System.out.println("Writing Sorting Block to NBT");

		super.writeToNBT(nbtTag);
		NBTTagList tagList = new NBTTagList();

		for (int i = 0; i < contents.length; i ++) {
			if (this.contents[i] != null) {
				NBTTagCompound tag = new NBTTagCompound();
				tag.setByte("Slot", (byte)i);
				this.contents[i].writeToNBT(tag);
				tagList.appendTag(tag);
			}
		}

		nbtTag.setTag(ModInfo.BLOCKSORTING_KEY, tagList);
	}

	@Override
	public void readFromNBT(NBTTagCompound tag) {
		System.out.println("Reading SORTING_KEY Block from NBT");
		super.readFromNBT(tag);
		NBTTagList tagList = tag.getTagList(ModInfo.BLOCKSORTING_KEY);
		this.contents = new ItemStack[this.getSizeInventory()];

		for (int i = 0; i < tagList.tagCount(); i++) {
			NBTTagCompound slotTag = (NBTTagCompound) tagList.tagAt(i);
			int slot = slotTag.getByte("Slot") & 255;
			if (slot >= 0 && slot < this.contents.length) {
				this.contents[slot] = ItemStack.loadItemStackFromNBT(slotTag);
			}
		}
	}

	//ISidedInventory Methods

	@Override
	public int getSizeInventory() {
		return contents.length;
	}

	@Override
	public ItemStack getStackInSlot(int i) {
		return contents[i];
	}

	@Override
	public ItemStack decrStackSize(int i, int count) {
		ItemStack itemstack = getStackInSlot(i);
		
		if (contents[i] == null) return itemstack;
		
		if (itemstack.stackSize <= count) {
			setInventorySlotContents(i, null);
		}
		else {
			itemstack.splitStack(count);
			onInventoryChanged();
		}
	
		return itemstack;
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int i) {
		ItemStack itemstack = getStackInSlot(i);
		setInventorySlotContents(i, null);
		return itemstack;
	}

	@Override
	public void setInventorySlotContents(int i, ItemStack itemstack) {
		this.contents[i] = itemstack;

		if (itemstack != null && itemstack.stackSize > getInventoryStackLimit()) {
			itemstack.stackSize = getInventoryStackLimit();
		}

		onInventoryChanged();
		
	}

	@Override
	public String getInvName() {
		return "Sorting Block";
	}

	@Override
	public boolean isInvNameLocalized() {
		return true;
	}

	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	@Override
	public void onInventoryChanged() {
		// TODO Change items rendered on block
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer entityplayer) {
		return entityplayer.getDistanceSq(xCoord + 0.5, yCoord + 0.5, zCoord + 0.5) <= 64;
	}

	@Override
	public void openChest() {}

	@Override
	public void closeChest() {}

	@Override
	public boolean isItemValidForSlot(int i, ItemStack itemstack) {
		return true;
	}

	@Override
	public int[] getAccessibleSlotsFromSide(int var1) {
		return mainStorage;
	}

	@Override
	/**
     * Returns true if automation can insert the given item in the given slot from the given side. Args: Slot, item,
     * side
     */
	public boolean canInsertItem(int i, ItemStack itemstack, int j) {
		// TODO Add gui first
		return true;
	}


	@Override
	public boolean canExtractItem(int i, ItemStack itemstack, int j) {
		// TODO add gui first.
		return true;
	}
}