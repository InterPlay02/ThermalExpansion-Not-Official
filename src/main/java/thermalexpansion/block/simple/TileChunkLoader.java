package thermalexpansion.block.simple;

import cofh.api.core.IEnergyInfo;
import cofh.api.energy.EnergyStorage;
import cofh.api.energy.IEnergyHandler;
import cofh.util.fluid.FluidTankAdv;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.ForgeChunkManager.Ticket;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;

import thermalexpansion.block.TileRSControl;
import thermalexpansion.core.TEProps;

public class TileChunkLoader extends TileRSControl implements IFluidHandler, IEnergyHandler, IEnergyInfo {

	FluidTankAdv tank = new FluidTankAdv(TEProps.MAX_FLUID_SMALL);
	EnergyStorage energyStorage = new EnergyStorage(400000);
	Ticket ticket;
	byte radius;

	public TileChunkLoader() {

	}

	@Override
	public String getName() {

		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getType() {

		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void updateEntity() {

	}

	/* GUI METHODS */
	@Override
	public GuiContainer getGuiClient(InventoryPlayer inventory) {

		return null;
	}

	@Override
	public Container getGuiServer(InventoryPlayer inventory) {

		return null;
	}

	/* NBT METHODS */
	@Override
	public void readFromNBT(NBTTagCompound nbt) {

		super.readFromNBT(nbt);
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt) {

		super.writeToNBT(nbt);
	}

	/* IFluidHandler */
	@Override
	public int fill(ForgeDirection from, FluidStack resource, boolean doFill) {

		return tank.fill(resource, doFill);
	}

	@Override
	public FluidStack drain(ForgeDirection from, FluidStack resource, boolean doDrain) {

		return tank.drain(resource, doDrain);
	}

	@Override
	public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain) {

		return tank.drain(maxDrain, doDrain);
	}

	@Override
	public boolean canFill(ForgeDirection from, Fluid fluid) {

		return true;
	}

	@Override
	public boolean canDrain(ForgeDirection from, Fluid fluid) {

		return true;
	}

	@Override
	public FluidTankInfo[] getTankInfo(ForgeDirection from) {

		return new FluidTankInfo[] { tank.getInfo() };
	}

	/* IEnergyHandler */
	@Override
	public boolean canConnectEnergy(ForgeDirection from) {

		return true;
	}

	@Override
	public int receiveEnergy(ForgeDirection from, int maxReceive, boolean simulate) {

		return energyStorage.receiveEnergy(maxReceive, simulate);
	}

	@Override
	public int extractEnergy(ForgeDirection from, int maxExtract, boolean simulate) {

		return 0;
	}

	@Override
	public int getEnergyStored(ForgeDirection from) {

		return energyStorage.getEnergyStored();
	}

	@Override
	public int getMaxEnergyStored(ForgeDirection from) {

		return energyStorage.getMaxEnergyStored();
	}

	/* IEnergyInfo */
	@Override
	public int getInfoEnergyPerTick() {

		// return calcEnergy() * energyMod;
		return 0;
	}

	@Override
	public int getInfoMaxEnergyPerTick() {

		// return energyConfig.maxPower * energyMod;
		return 0;
	}

	@Override
	public int getInfoEnergyStored() {

		return energyStorage.getEnergyStored();
	}

	@Override
	public int getInfoMaxEnergyStored() {

		return energyStorage.getMaxEnergyStored();
	}

}