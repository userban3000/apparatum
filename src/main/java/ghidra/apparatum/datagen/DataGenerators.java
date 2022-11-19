package ghidra.apparatum.datagen;

import ghidra.apparatum.Apparatum;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;

@Mod.EventBusSubscriber(modid = Apparatum.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataGenerators {

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        if (event.includeServer()) {
            //recipes
            //loot tables
            generator.addProvider(new ApparatumBlockTags(generator, event.getExistingFileHelper()));
            //item tags
        }
        if (event.includeClient()) {
            generator.addProvider(new ApparatumBlockStates(generator, event.getExistingFileHelper()));
            generator.addProvider(new ApparatumItemModels(generator, event.getExistingFileHelper()));
            generator.addProvider(new ApparatumLangProvider(generator, "en_us"));
        }
    }
}
