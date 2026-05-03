package fuzs.hoveringhotbar.fabric;

import fuzs.hoveringhotbar.common.HoveringHotbar;
import fuzs.puzzleslib.common.api.core.v1.ModConstructor;
import net.fabricmc.api.ModInitializer;

public class HoveringHotbarFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        ModConstructor.construct(HoveringHotbar.MOD_ID, HoveringHotbar::new);
    }
}
