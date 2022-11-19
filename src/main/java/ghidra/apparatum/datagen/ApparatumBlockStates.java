package ghidra.apparatum.datagen;

import ghidra.apparatum.Apparatum;
import ghidra.apparatum.setup.Registration;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class ApparatumBlockStates extends BlockStateProvider {

    public ApparatumBlockStates(DataGenerator gen, ExistingFileHelper exFileHelper) {
        super(gen, Apparatum.MOD_ID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        simpleBlock(Registration.DEEP_PUMP.get());
    }
}
