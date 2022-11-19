package ghidra.apparatum.util;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.energy.EnergyStorage;

import static com.google.common.primitives.Ints.max;
import static com.google.common.primitives.Ints.min;

public class ApparatumEnergyStorage extends EnergyStorage {
    private final BlockEntity blockEntity;

    public ApparatumEnergyStorage(int capacity, BlockEntity blockEntity) {
        super(capacity);
        this.blockEntity = blockEntity;
    }

    public ApparatumEnergyStorage(int capacity, int maxTransfer, BlockEntity blockEntity) {
        super(capacity, maxTransfer);
        this.blockEntity = blockEntity;
    }

    public ApparatumEnergyStorage(int capacity, int maxReceive, int maxExtract, BlockEntity blockEntity) {
        super(capacity, maxReceive, maxExtract);
        this.blockEntity = blockEntity;
    }

    public ApparatumEnergyStorage(int capacity, int maxReceive, int maxExtract, int energy, BlockEntity blockEntity) {
        super(capacity, maxReceive, maxExtract, energy);
        this.blockEntity = blockEntity;
    }

    @Override
    public int extractEnergy(int maxExtract, boolean simulate) {
        int result = super.extractEnergy(maxExtract, simulate);
        this.blockEntity.setChanged();
        return result;
    }

    @Override
    public int receiveEnergy(int maxReceive, boolean simulate) {
        int result = super.receiveEnergy(maxReceive, simulate);
        this.blockEntity.setChanged();
        return result;
    }

    public ApparatumEnergyStorage read(CompoundTag nbt) {
        setEnergy(nbt.getInt("energy"));
        return this;
    }

    public CompoundTag write(CompoundTag nbt) {
        nbt.putInt("energy", energy);
        return nbt;
    }

    public void setEnergy(int energy) {
        this.energy = max(0, min(energy, this.capacity));
    }
}
