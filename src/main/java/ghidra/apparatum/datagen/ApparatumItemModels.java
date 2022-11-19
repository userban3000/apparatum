package ghidra.apparatum.datagen;

import ghidra.apparatum.Apparatum;
import ghidra.apparatum.setup.Registration;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class ApparatumItemModels extends ItemModelProvider {
    public ApparatumItemModels(DataGenerator generator, ExistingFileHelper existingFileHelper) {
        super(generator, Apparatum.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        ResourceLocation rl = new ResourceLocation(Apparatum.MOD_ID, "name");

        withExistingParent(Registration.DEEP_PUMP_ITEM.get().getRegistryName().getPath(), modLoc("block/deep_pump"));
        singleTexture(Registration.CONFIGURATOR.get().getRegistryName().getPath(), modLoc("item/configurator"), "layer0", modLoc("item/configurator"));
    }
}
