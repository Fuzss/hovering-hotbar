package fuzs.hoveringhotbar.client;

import fuzs.hoveringhotbar.HoveringHotbar;
import fuzs.hoveringhotbar.client.helper.HotbarSpriteHelper;
import fuzs.hoveringhotbar.config.ClientConfig;
import fuzs.puzzleslib.api.client.core.v1.ClientModConstructor;
import fuzs.puzzleslib.api.client.core.v1.context.GuiLayersContext;
import fuzs.puzzleslib.api.client.core.v1.context.KeyMappingsContext;
import fuzs.puzzleslib.api.client.event.v1.gui.CustomizeChatPanelCallback;
import fuzs.puzzleslib.api.client.event.v1.gui.RenderGuiEvents;
import fuzs.puzzleslib.api.client.key.v1.KeyActivationHandler;
import fuzs.puzzleslib.api.client.key.v1.KeyMappingHelper;
import fuzs.puzzleslib.api.core.v1.ModLoaderEnvironment;
import fuzs.puzzleslib.api.event.v1.core.EventPhase;
import fuzs.puzzleslib.api.event.v1.data.MutableInt;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.LayeredDraw;
import net.minecraft.resources.ResourceLocation;

public class HoveringHotbarClient implements ClientModConstructor {
    public static final KeyMapping MOVE_HOTBAR_UP_KEY_MAPPING = KeyMappingHelper.registerUnboundKeyMapping(
            HoveringHotbar.id("move_hotbar_up"));
    public static final KeyMapping MOVE_HOTBAR_DOWN_KEY_MAPPING = KeyMappingHelper.registerUnboundKeyMapping(
            HoveringHotbar.id("move_hotbar_down"));

    @Override
    public void onConstructMod() {
        registerEventHandlers();
    }

    private static void registerEventHandlers() {
        CustomizeChatPanelCallback.EVENT.register((GuiGraphics guiGraphics, DeltaTracker deltaTracker, MutableInt posX, MutableInt posY) -> {
            posY.mapInt((int value) -> value - HoveringHotbar.CONFIG.get(ClientConfig.class).getHotbarOffset());
        });
        if (ModLoaderEnvironment.INSTANCE.getModLoader().isForgeLike()) {
            RenderGuiEvents.BEFORE.register((Gui gui, GuiGraphics guiGraphics, DeltaTracker deltaTracker) -> {
                HotbarSpriteHelper.applyGuiHeight(gui);
            });
        }
    }

    @Override
    public void onRegisterKeyMappings(KeyMappingsContext context) {
        context.registerKeyMapping(MOVE_HOTBAR_UP_KEY_MAPPING, KeyActivationHandler.forGame((Minecraft minecraft) -> {
            HoveringHotbar.CONFIG.get(ClientConfig.class)
                    .updateHotbarOffset(minecraft.getWindow().getGuiScaledHeight(), true);
        }));
        context.registerKeyMapping(MOVE_HOTBAR_DOWN_KEY_MAPPING, KeyActivationHandler.forGame((Minecraft minecraft) -> {
            HoveringHotbar.CONFIG.get(ClientConfig.class)
                    .updateHotbarOffset(minecraft.getWindow().getGuiScaledHeight(), false);
        }));
    }

    @Override
    public void onRegisterGuiLayers(GuiLayersContext context) {
        context.setEventPhase(EventPhase.LAST);
        for (ResourceLocation resourceLocation : HotbarSpriteHelper.getHotbarGuiLayerLocations()) {
            context.replaceGuiLayer(resourceLocation, HotbarSpriteHelper::getLayerWithTranslation);
        }
        context.registerGuiLayer(GuiLayersContext.HOTBAR,
                HoveringHotbar.id("hotbar_selection"),
                HotbarSpriteHelper.getLayerWithTranslation(HotbarSpriteHelper::blitHotbarSelectionSprite));
        if (HoveringHotbar.CONFIG.get(ClientConfig.class).moveExperienceAboveBar) {
            context.replaceGuiLayer(GuiLayersContext.EXPERIENCE_LEVEL, (LayeredDraw.Layer layer) -> {
                return (GuiGraphics guiGraphics, DeltaTracker deltaTracker) -> {
                    guiGraphics.pose().pushPose();
                    guiGraphics.pose().translate(0.0F, -3.0F, 0.0F);
                    layer.render(guiGraphics, deltaTracker);
                    guiGraphics.pose().popPose();
                };
            });
        }
    }
}
