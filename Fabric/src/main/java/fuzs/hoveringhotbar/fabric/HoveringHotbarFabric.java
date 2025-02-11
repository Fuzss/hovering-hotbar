package fuzs.hoveringhotbar.fabric;

import fuzs.hoveringhotbar.HoveringHotbar;
import fuzs.puzzleslib.api.core.v1.ModConstructor;
import net.fabricmc.api.ModInitializer;

public class HoveringHotbarFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        ModConstructor.construct(HoveringHotbar.MOD_ID, HoveringHotbar::new);
    }
}
