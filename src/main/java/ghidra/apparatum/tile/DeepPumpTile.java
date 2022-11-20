package ghidra.apparatum.tile;

import ghidra.apparatum.Apparatum;
import ghidra.apparatum.setup.Registration;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;

public class DeepPumpTile extends ApparatumTile implements BlockEntityTicker<DeepPumpTile> {
    private int progress = 0;
    private boolean isActive = true;
    private boolean need3x3Sky = false;

    public DeepPumpTile(BlockPos pPos, BlockState pBlockState) {
        super(Registration.DEEP_PUMP_TE.get(), pPos, pBlockState, 80000, 1000, 200, 8000);
    }

    @Override
    public void tick(Level pLevel, BlockPos pPos, BlockState pState, DeepPumpTile pBlockEntity) {
    }

    public void tickServer(Level pLevel, BlockPos pPos) {
        if (this.energyStorage.getEnergyStored() > 100) {
            this.energyStorage.extractEnergy(100, false);
            progress++;
        }
        if (progress > 20) {
            progress = 0;
            if (aboveBedrockAndCanSeeSky(pLevel, pPos)) {
                FluidStack generatedFluid = generateFluid();
                fluidStorage.modify(generatedFluid, IFluidHandler.FluidAction.EXECUTE);
            }
        }
        if (Apparatum.CONSOLE_DEBUG_ENABLED) {
            consoleDebug();
        }
    }

    private void consoleDebug() {
        System.out.println("DEEP PUMP TILE ========");
        System.out.println("progress: " + progress);
        System.out.println("rf: " + energyStorage.getEnergyStored());
        System.out.println("fluid amount: " + fluidStorage.getAmount() );
        System.out.println("fluid is water? " + fluidStorage.getFluidStack().getFluid().equals(Fluids.WATER) );
        System.out.println("===========================================");
    }

    //TODO LOGIC FOR HOW MUCH FLUID IS GENERATED
    public FluidStack generateFluid() {
        return new FluidStack(Fluids.WATER, 10);
    }

    private boolean aboveBedrockAndCanSeeSky(Level pL, BlockPos pos) {
        boolean canSeeSky = true;
        if (need3x3Sky) {
            for (int i = -1; i <= 1; i++) {
                for (int j = -1; j <= 1; j++) {
                    canSeeSky &= pL.canSeeSky(pos.offset(i, 1, j));
                }
            }
        } else {
            canSeeSky = pL.canSeeSky(pos.offset(0, 1, 0));
        }
        boolean bedrockBelow = pL.getBlockState(pos.offset(0,-1,0)).getBlock().equals(Blocks.BEDROCK);
        return canSeeSky && bedrockBelow;
    }
}
