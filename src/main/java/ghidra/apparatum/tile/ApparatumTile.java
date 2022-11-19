package ghidra.apparatum.tile;

import ghidra.apparatum.util.ApparatumEnergyStorage;
import ghidra.apparatum.util.ApparatumFluidStorage;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ApparatumTile extends BlockEntity implements ICapabilityProvider, IFluidHandler, IEnergyStorage {
    private final int energy_capacity = 80000;
    private final int max_receive = 80000;
    private final int max_extract = 80000;

    private final int fluid_capacity = 8000;

    public final ApparatumEnergyStorage energyStorage;
    private LazyOptional<ApparatumEnergyStorage> energyStorageLazyOptional;

    public final ApparatumFluidStorage fluidStorage;
    private LazyOptional<ApparatumFluidStorage> fluidStorageLazyOptional;

    public ApparatumTile(BlockEntityType pType, BlockPos pPos, BlockState pBlockState) {
        super(pType, pPos, pBlockState);
        this.energyStorage = new ApparatumEnergyStorage(energy_capacity, max_receive, max_extract, this);
        this.fluidStorage = new ApparatumFluidStorage(fluid_capacity);

        this.energyStorageLazyOptional = LazyOptional.of(() -> this.energyStorage);
        this.fluidStorageLazyOptional = LazyOptional.of(() -> this.fluidStorage);
    }

    @NotNull
    @Override
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap) {
        if(cap == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
            return fluidStorageLazyOptional.cast();
        }
        if(cap == CapabilityEnergy.ENERGY) {
            return energyStorageLazyOptional.cast();
        }
        return super.getCapability(cap);
    }

    @NotNull
    @Override
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if(cap == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
            return fluidStorageLazyOptional.cast();
        }
        if(cap == CapabilityEnergy.ENERGY) {
            return energyStorageLazyOptional.cast();
        }
        return super.getCapability(cap, side);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        this.energyStorageLazyOptional.invalidate();
        this.fluidStorageLazyOptional.invalidate();
    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        pTag = energyStorage.write(pTag);
        pTag = fluidStorage.write(pTag);
        super.saveAdditional(pTag);
    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);
        energyStorage.read(pTag);
        fluidStorage.read(pTag);
    }

    @Override
    public int receiveEnergy(int maxReceive, boolean simulate) {
        return energyStorage.receiveEnergy(maxReceive, simulate);
    }

    @Override
    public int extractEnergy(int maxExtract, boolean simulate) {
        return energyStorage.extractEnergy(maxExtract, simulate);
    }

    @Override
    public int getEnergyStored() {
        return energyStorage.getEnergyStored();
    }

    @Override
    public int getMaxEnergyStored() {
        return energyStorage.getMaxEnergyStored();
    }

    @Override
    public boolean canExtract() {
        return energyStorage.canExtract();
    }

    @Override
    public boolean canReceive() {
        return energyStorage.canReceive();
    }

    @Override
    public int getTanks() {
        return fluidStorage.getTanks();
    }

    @NotNull
    @Override
    public FluidStack getFluidInTank(int tank) {
        return fluidStorage.getFluidInTank(tank);
    }

    @Override
    public int getTankCapacity(int tank) {
        return fluidStorage.getTankCapacity(tank);
    }

    @Override
    public boolean isFluidValid(int tank, @NotNull FluidStack stack) {
        return true;
    }

    @Override
    public int fill(FluidStack resource, FluidAction action) {
        return fluidStorage.fill(resource, action);
    }

    @NotNull
    @Override
    public FluidStack drain(FluidStack resource, FluidAction action) {
        return fluidStorage.drain(resource, action);
    }

    @NotNull
    @Override
    public FluidStack drain(int maxDrain, FluidAction action) {
        return fluidStorage.drain(maxDrain, action);
    }
}
