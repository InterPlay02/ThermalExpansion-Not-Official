package cofh.thermalexpansion.gui.container.machine;

import cofh.core.gui.slot.ISlotValidator;
import cofh.core.gui.slot.SlotEnergy;
import cofh.core.gui.slot.SlotRemoveOnly;
import cofh.core.gui.slot.SlotValidated;
import cofh.core.util.helpers.EnergyHelper;
import cofh.thermalexpansion.block.machine.TileCharger;
import cofh.thermalexpansion.gui.container.ContainerTEBase;
import cofh.thermalexpansion.util.managers.machine.ChargerManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

public class ContainerCharger extends ContainerTEBase implements ISlotValidator {

	TileCharger myTile;

	public ContainerCharger(InventoryPlayer inventory, TileEntity tile) {

		super(inventory, tile);

		myTile = (TileCharger) tile;
		addSlotToContainer(new SlotValidated(this, myTile, 0, 44, 35));
		addSlotToContainer(new SlotRemoveOnly(myTile, 1, 80, 35));
		addSlotToContainer(new SlotRemoveOnly(myTile, 2, 125, 35));
		addSlotToContainer(new SlotEnergy(myTile, myTile.getChargeSlot(), 8, 53));
	}

	@Override
	public boolean isItemValid(ItemStack stack) {

		return myTile.isItemValidForSlot(0, stack);
	}

}
