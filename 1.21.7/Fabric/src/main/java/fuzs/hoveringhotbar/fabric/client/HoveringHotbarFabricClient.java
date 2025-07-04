package fuzs.hoveringhotbar.fabric.client;

import fuzs.hoveringhotbar.HoveringHotbar;
import fuzs.hoveringhotbar.client.HoveringHotbarClient;
import fuzs.hoveringhotbar.client.helper.HotbarSpriteHelper;
import fuzs.hoveringhotbar.config.ClientConfig;
import fuzs.puzzleslib.api.client.core.v1.ClientModConstructor;
import fuzs.puzzleslib.api.client.event.v1.ClientLifecycleEvents;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElement;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;

public class HoveringHotbarFabricClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ClientModConstructor.construct(HoveringHotbar.MOD_ID, HoveringHotbarClient::new);
        registerEventHandlers();
    }

    private static void registerEventHandlers() {
        ClientLifecycleEvents.STARTED.register((Minecraft minecraft) -> {
            // our gui layer system does not support modded layers, so use the native event here
            for (ResourceLocation resourceLocation : HoveringHotbar.CONFIG.get(ClientConfig.class).hotbarGuiLayers) {
                try {
                    HudElementRegistry.replaceElement(resourceLocation, (HudElement hudElement) -> {
                        return HotbarSpriteHelper.getLayerWithTranslation(hudElement::render)::render;
                    });
                } catch (Exception exception) {
                    HoveringHotbar.LOGGER.warn("Failed to replace gui layer {}", resourceLocation);
                }
            }
        });
    }
}
