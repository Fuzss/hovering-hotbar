package fuzs.hoveringhotbar.neoforge.client;

import fuzs.hoveringhotbar.HoveringHotbar;
import fuzs.hoveringhotbar.client.HoveringHotbarClient;
import fuzs.hoveringhotbar.client.helper.HotbarSpriteHelper;
import fuzs.hoveringhotbar.config.ClientConfig;
import fuzs.hoveringhotbar.data.client.ModLanguageProvider;
import fuzs.puzzleslib.api.client.core.v1.ClientModConstructor;
import fuzs.puzzleslib.neoforge.api.data.v2.core.DataProviderHelper;
import net.minecraft.resources.Identifier;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.neoforged.neoforge.client.gui.GuiLayer;

@Mod(value = HoveringHotbar.MOD_ID, dist = Dist.CLIENT)
public class HoveringHotbarNeoForgeClient {

    public HoveringHotbarNeoForgeClient(ModContainer modContainer) {
        ClientModConstructor.construct(HoveringHotbar.MOD_ID, HoveringHotbarClient::new);
        registerLoadingHandlers(modContainer.getEventBus());
        DataProviderHelper.registerDataProviders(HoveringHotbar.MOD_ID, ModLanguageProvider::new);
    }

    private static void registerLoadingHandlers(IEventBus eventBus) {
        // our gui layer system does not support modded layers, so use the native event here
        eventBus.addListener((final RegisterGuiLayersEvent event) -> {
            // our gui layer system does not support modded layers, so use the native event here
            for (Identifier identifier : HoveringHotbar.CONFIG.get(ClientConfig.class).hotbarGuiLayers) {
                try {
                    event.wrapLayer(identifier, (GuiLayer guiLayer) -> {
                        return HotbarSpriteHelper.getLayerWithTranslation(guiLayer::render)::render;
                    });
                } catch (Exception exception) {
                    // NO-OP
                }
            }
        });
    }
}
