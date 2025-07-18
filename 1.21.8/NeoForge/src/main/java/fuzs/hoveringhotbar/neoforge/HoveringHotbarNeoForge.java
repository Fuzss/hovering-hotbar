package fuzs.hoveringhotbar.neoforge;

import fuzs.hoveringhotbar.HoveringHotbar;
import fuzs.puzzleslib.api.core.v1.ModConstructor;
import net.neoforged.fml.common.Mod;

@Mod(HoveringHotbar.MOD_ID)
public class HoveringHotbarNeoForge {

    public HoveringHotbarNeoForge() {
        ModConstructor.construct(HoveringHotbar.MOD_ID, HoveringHotbar::new);
    }
}
