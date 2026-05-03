package fuzs.hoveringhotbar.fabric.client;

import fuzs.hoveringhotbar.common.HoveringHotbar;
import fuzs.hoveringhotbar.common.client.HoveringHotbarClient;
import fuzs.hoveringhotbar.common.client.helper.HotbarSpriteHelper;
import fuzs.hoveringhotbar.common.config.ClientConfig;
import fuzs.puzzleslib.common.api.client.core.v1.ClientModConstructor;
import fuzs.puzzleslib.common.api.client.event.v1.ClientLifecycleEvents;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElement;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.Identifier;

public class HoveringHotbarFabricClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ClientModConstructor.construct(HoveringHotbar.MOD_ID, HoveringHotbarClient::new);
        registerEventHandlers();
    }

    private static void registerEventHandlers() {
        ClientLifecycleEvents.STARTED.register((Minecraft minecraft) -> {
            // our gui layer system does not support modded layers, so use the native event here
            for (Identifier identifier : HoveringHotbar.CONFIG.get(ClientConfig.class).hotbarGuiLayers) {
                try {
                    HudElementRegistry.replaceElement(identifier, (HudElement hudElement) -> {
                        return HotbarSpriteHelper.getLayerWithTranslation(hudElement::extractRenderState)::extractRenderState;
                    });
                } catch (Exception exception) {
                    // NO-OP
                }
            }
        });
    }
}
