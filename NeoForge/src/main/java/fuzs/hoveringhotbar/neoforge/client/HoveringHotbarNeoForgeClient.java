package fuzs.hoveringhotbar.neoforge.client;

import fuzs.hoveringhotbar.HoveringHotbar;
import fuzs.hoveringhotbar.client.HoveringHotbarClient;
import fuzs.hoveringhotbar.client.helper.HotbarSpriteHelper;
import fuzs.hoveringhotbar.config.ClientConfig;
import fuzs.hoveringhotbar.data.client.ModLanguageProvider;
import fuzs.puzzleslib.api.client.core.v1.ClientModConstructor;
import fuzs.puzzleslib.neoforge.api.data.v2.core.DataProviderHelper;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.RenderGuiLayerEvent;
import net.neoforged.neoforge.common.NeoForge;

@Mod(value = HoveringHotbar.MOD_ID, dist = Dist.CLIENT)
public class HoveringHotbarNeoForgeClient {

    public HoveringHotbarNeoForgeClient() {
        ClientModConstructor.construct(HoveringHotbar.MOD_ID, HoveringHotbarClient::new);
        registerEventHandlers(NeoForge.EVENT_BUS);
        DataProviderHelper.registerDataProviders(HoveringHotbar.MOD_ID, ModLanguageProvider::new);
    }

    private static void registerEventHandlers(IEventBus eventBus) {
        // our gui layer system does not support modded layers, so use the native event here
        eventBus.addListener((final RenderGuiLayerEvent.Pre evt) -> {
            if (HoveringHotbar.CONFIG.get(ClientConfig.class).hotbarGuiLayers.contains(evt.getName())) {
                // not so great cancelling layer rendering, but NeoForge does not support wrapping by default
                HotbarSpriteHelper.getLayerWithTranslation(evt.getLayer()::render)
                        .render(evt.getGuiGraphics(), evt.getPartialTick());
                evt.setCanceled(true);
            }
        });
    }
}
