package fuzs.hoveringhotbar.fabric.client;

import fuzs.hoveringhotbar.HoveringHotbar;
import fuzs.hoveringhotbar.client.HoveringHotbarClient;
import fuzs.puzzleslib.api.client.core.v1.ClientModConstructor;
import net.fabricmc.api.ClientModInitializer;

public class HoveringHotbarFabricClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ClientModConstructor.construct(HoveringHotbar.MOD_ID, HoveringHotbarClient::new);
    }
}
