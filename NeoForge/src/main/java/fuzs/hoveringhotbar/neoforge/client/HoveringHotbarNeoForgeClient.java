package fuzs.hoveringhotbar.neoforge.client;

import fuzs.hoveringhotbar.HoveringHotbar;
import fuzs.hoveringhotbar.client.HoveringHotbarClient;
import fuzs.hoveringhotbar.neoforge.client.handler.HotbarShiftHandler;
import fuzs.puzzleslib.api.client.core.v1.ClientModConstructor;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;

@Mod(value = HoveringHotbar.MOD_ID, dist = Dist.CLIENT)
public class HoveringHotbarNeoForgeClient {

    public HoveringHotbarNeoForgeClient() {
        ClientModConstructor.construct(HoveringHotbar.MOD_ID, HoveringHotbarClient::new);
        registerEventHandlers(NeoForge.EVENT_BUS);
    }

    private static void registerEventHandlers(IEventBus eventBus) {
        // try to not push a pose on the stack when the event is cancelled
        eventBus.addListener(EventPriority.LOW, HotbarShiftHandler::onBeforeRenderGui);
        eventBus.addListener(HotbarShiftHandler::onAfterRenderGui);
        eventBus.addListener(HotbarShiftHandler::onBeforeRenderGuiLayer);
    }
}
