package ghidra.apparatum.util;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.util.function.Supplier;

public class ApparatumFluidStorage implements IFluidHandler {
    protected final int capacity;
    protected Supplier<FluidStack> emptyFluid = () -> FluidStack.EMPTY;

    @Nonnull
    protected FluidStack fluid = FluidStack.EMPTY;

    public ApparatumFluidStorage(int capacity) {
        this.capacity = capacity;
    }

    public void setFluidStack(FluidStack stack) {
        this.fluid = stack.isEmpty() ? emptyFluid.get() : stack;
    }

    //NBT
    public ApparatumFluidStorage read(CompoundTag nbt) {
        //FluidStack stack = FluidStack.loadFluidStackFromNBT(nbt);
        //setFluidStack(stack);
        return this;
    }

    public CompoundTag write(CompoundTag nbt) {
        fluid.writeToNBT(nbt);
        return nbt;
    }

    @Override
    public int getTanks() {
        return 1;
    }

    @NotNull
    @Override
    public FluidStack getFluidInTank(int tank) {
        return fluid;
    }

    public int getAmount() {
        return fluid.getAmount();
    }

    public boolean isEmpty() {
        return fluid.isEmpty();
    }

    @Override
    public int getTankCapacity(int tank) {
        return capacity;
    }

    @Override
    public boolean isFluidValid(int tank, @NotNull FluidStack stack) {
        return false;
    }

    @Override
    public int fill(FluidStack resource, FluidAction action) {
        if (action.simulate()) {
            if (fluid.isEmpty()) {
                return Math.min(capacity, resource.getAmount());
            }
            if (!fluid.isFluidEqual(resource)) {
                return 0;
            }
            return Math.min(capacity - fluid.getAmount(), resource.getAmount());
        }
        if (fluid.isEmpty()) {
            setFluidStack(new FluidStack(resource, Math.min(capacity, resource.getAmount())));
            return fluid.getAmount();
        }
        if (!fluid.isFluidEqual(resource)) {
            return 0;
        }
        int space = capacity - fluid.getAmount();
        if (resource.getAmount() < space) {
            fluid.grow(resource.getAmount());
            space = resource.getAmount();
        } else {
            fluid.setAmount(capacity);
        }
        return space;
    }

    public int modify(FluidStack resource, FluidAction action) {
        if (action.simulate()) {
            if (fluid.isEmpty() || !fluid.isFluidEqual(resource)) {
                return 0;
            }
            return Math.max(capacity, fluid.getAmount() + resource.getAmount());
        }
        if (fluid.isEmpty()) {
            setFluidStack(resource);
            fluid.setAmount(resource.getAmount());
        } else {
            if (!fluid.isFluidEqual(resource)) {
                return 0;
            }
            fluid.grow(resource.getAmount());
        }
        fluid.setAmount(Math.min(capacity, fluid.getAmount()));
        return capacity - fluid.getAmount();
    }

    @NotNull
    @Override
    public FluidStack drain(FluidStack resource, FluidAction action) {
        if (resource.isEmpty() || !resource.isFluidEqual(fluid)) {
            return FluidStack.EMPTY;
        }
        return drain(resource.getAmount(), action);
    }

    @NotNull
    @Override
    public FluidStack drain(int maxDrain, FluidAction action) {
        if (maxDrain <= 0 || fluid.isEmpty()) {
            return FluidStack.EMPTY;
        }
        int drained = maxDrain;
        if (fluid.getAmount() < drained) {
            drained = fluid.getAmount();
        }
        FluidStack stack = new FluidStack(fluid, drained);
        if (action.execute()) {
            fluid.shrink(drained);
            if (fluid.isEmpty()) {
                setFluidStack(emptyFluid.get());
            }
        }
        return stack;
    }

    @Nonnull
    public FluidStack getFluidStack() {
        return fluid;
    }
}
