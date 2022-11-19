package ghidra.apparatum.container;

import ghidra.apparatum.setup.Registration;
import ghidra.apparatum.tile.DeepPumpTile;
import ghidra.apparatum.util.ApparatumEnergyStorage;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.DataSlot;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;

public class DeepPumpContainer extends ApparatumContainer {
    
    public DeepPumpContainer(int windowId, BlockPos pos, Inventory playerInv, Player player) {
        super(Registration.DEEP_PUMP_CONTAINER.get(), windowId, pos, playerInv, player);

        layoutPlayerInventorySlots(8, 84);
        trackPower();
        //trackFluid();
    }

    private void trackPower() {
        // On a dedicated server ints are truncated to short (for Mojang reasons)
        // so we split our integer here (32 bit -> 16 bit)
        addDataSlot(new DataSlot() {
            @Override
            public int get() {
                return getEnergy() & 0xffff;
            }

            @Override
            public void set(int pValue) {
                tileEntity.getCapability(CapabilityEnergy.ENERGY).ifPresent(h -> {
                    int energyStored = h.getEnergyStored() & 0xffff0000;
                    ((ApparatumEnergyStorage)h).setEnergy(energyStored + (pValue & 0xffff));
                });
            }
        });
        addDataSlot(new DataSlot() {
            @Override
            public int get() {
                return getEnergy() >> 16 & 0xffff;
            }

            @Override
            public void set(int pValue) {
                tileEntity.getCapability(CapabilityEnergy.ENERGY).ifPresent(h -> {
                    int energyStored = h.getEnergyStored() & 0x00004444;
                    ((ApparatumEnergyStorage)h).setEnergy(energyStored | (pValue << 16));
                });
            }
        });
    }

    public FluidStack getFluid() {
        if (!tileEntity.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY).isPresent()) {
            throw new IllegalArgumentException("Tile entity is not Deep Pump! Tried to extract fluid.");
        }
        DeepPumpTile dp = (DeepPumpTile) tileEntity;
        return dp.getFluidInTank(0);
    }

    public int getFluidQuantityScaled() {
        return (int) Math.ceil(getFluid().getAmount() / (double)TALL_CONTAINER_HEIGHT);
    }

    public int getEnergyScaled() {
        return (int) Math.ceil(getEnergy() / (double)TALL_CONTAINER_HEIGHT);
    }

    public int getMaxEnergyScaled() {
        return (int) Math.ceil(tileEntity.getCapability(CapabilityEnergy.ENERGY).map(IEnergyStorage::getMaxEnergyStored).orElse(0) / (double)TALL_CONTAINER_HEIGHT);
    }

    public int getEnergy() {
        return tileEntity.getCapability(CapabilityEnergy.ENERGY).map(IEnergyStorage::getEnergyStored).orElse(0);
    }

    @Override
    public boolean stillValid(Player pPlayer) {
        return stillValid(ContainerLevelAccess.create(tileEntity.getLevel(), tileEntity.getBlockPos()), playerEntity, Registration.DEEP_PUMP.get());
    }
}
