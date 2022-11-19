package ghidra.apparatum;

import com.mojang.logging.LogUtils;
import ghidra.apparatum.setup.ClientSetup;
import ghidra.apparatum.setup.ModSetup;
import ghidra.apparatum.setup.Registration;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(Apparatum.MOD_ID)
public class Apparatum
{
    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();
    public static final String MOD_ID = "apparatum";
    public static final String TAB_NAME = "apparatum";
    public static boolean CONSOLE_DEBUG_ENABLED = false;

    public Apparatum()
    {
        Registration.init();

        final IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addListener(ModSetup::init);

        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> modEventBus.addListener(ClientSetup::init));
    }


}
