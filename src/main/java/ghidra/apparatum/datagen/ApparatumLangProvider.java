package ghidra.apparatum.datagen;

import ghidra.apparatum.Apparatum;
import ghidra.apparatum.block.DeepPumpBlock;
import ghidra.apparatum.setup.Registration;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.LanguageProvider;

public class ApparatumLangProvider extends LanguageProvider {
    public ApparatumLangProvider(DataGenerator gen, String locale) {
        super(gen, Apparatum.MOD_ID, locale);
    }

    @Override
    protected void addTranslations() {
        add("itemGroup." + Apparatum.TAB_NAME, "Apparatum");

        add(Registration.DEEP_PUMP.get(), "Deep Pump");
        add(DeepPumpBlock.MESSAGE_DEEPPUMP,  "Extracts ore-filled fluids from below bedrock.");
        add(DeepPumpBlock.SCREEN_APPARATUM_DEEPPUMP,  "Deep Pump");


        add(Registration.CONFIGURATOR.get(), "Configurator");

    }
}
