package ghidra.apparatum.datagen;

import ghidra.apparatum.Apparatum;
import ghidra.apparatum.setup.Registration;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.tags.BlockTags;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

public class ApparatumBlockTags extends BlockTagsProvider {
    public ApparatumBlockTags(DataGenerator pGenerator, @Nullable ExistingFileHelper existingFileHelper) {
        super(pGenerator, Apparatum.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags() {
        tag(BlockTags.MINEABLE_WITH_PICKAXE)
                .add(Registration.DEEP_PUMP.get());

        tag(BlockTags.NEEDS_IRON_TOOL)
                .add(Registration.DEEP_PUMP.get());
    }
}
