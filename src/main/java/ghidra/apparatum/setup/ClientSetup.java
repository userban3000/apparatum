package ghidra.apparatum.setup;

import ghidra.apparatum.client.DeepPumpScreen;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

public class ClientSetup {
    public static void init(FMLCommonSetupEvent event) {
        //enqueueWork() to run this on main thread
        event.enqueueWork(() -> {
            MenuScreens.register(Registration.DEEP_PUMP_CONTAINER.get(), DeepPumpScreen::new);
        });
    }
}
