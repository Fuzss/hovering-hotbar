package fuzs.hoveringhotbar.fabric.client;

import fuzs.hoveringhotbar.HoveringHotbar;
import fuzs.hoveringhotbar.client.HoveringHotbarClient;
import fuzs.hoveringhotbar.client.helper.HotbarSpriteHelper;
import fuzs.hoveringhotbar.config.ClientConfig;
import fuzs.puzzleslib.api.client.core.v1.ClientModConstructor;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.HudLayerRegistrationCallback;
import net.fabricmc.fabric.api.client.rendering.v1.IdentifiedLayer;
import net.fabricmc.fabric.api.client.rendering.v1.LayeredDrawerWrapper;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;

public class HoveringHotbarFabricClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ClientModConstructor.construct(HoveringHotbar.MOD_ID, HoveringHotbarClient::new);
        registerEventHandlers();
    }

    private static void registerEventHandlers() {
        // this should ideally use a late event phase for wrapping, add that if it ever becomes an issue
        HudLayerRegistrationCallback.EVENT.register((LayeredDrawerWrapper layeredDrawerWrapper) -> {
            layeredDrawerWrapper.replaceLayer(IdentifiedLayer.HOTBAR_AND_BARS, (IdentifiedLayer identifiedLayer) -> {
                return IdentifiedLayer.of(identifiedLayer.id(),
                        (GuiGraphics guiGraphics, DeltaTracker deltaTracker) -> {
                            HotbarSpriteHelper.getLayerWithTranslation(identifiedLayer)
                                    .render(guiGraphics, deltaTracker);
                            HotbarSpriteHelper.applyGuiHeight(Minecraft.getInstance().gui);
                        });
            });
            // our gui layer system does not support modded layers, so use the native event here
            for (ResourceLocation resourceLocation : HoveringHotbar.CONFIG.get(ClientConfig.class).hotbarGuiLayers) {
                try {
                    layeredDrawerWrapper.replaceLayer(resourceLocation, (IdentifiedLayer identifiedLayer) -> {
                        return IdentifiedLayer.of(identifiedLayer.id(),
                                HotbarSpriteHelper.getLayerWithTranslation(identifiedLayer));
                    });
                } catch (Exception exception) {
                    HoveringHotbar.LOGGER.warn("Failed to replace gui layer {}", resourceLocation);
                }
            }
        });
    }
}
