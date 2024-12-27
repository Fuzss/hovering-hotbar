package fuzs.hoveringhotbar.client;

import fuzs.hoveringhotbar.HoveringHotbar;
import fuzs.hoveringhotbar.config.ClientConfig;
import fuzs.puzzleslib.api.client.core.v1.ClientAbstractions;
import fuzs.puzzleslib.api.client.core.v1.ClientModConstructor;
import fuzs.puzzleslib.api.client.core.v1.context.KeyMappingsContext;
import fuzs.puzzleslib.api.client.event.v1.gui.CustomizeChatPanelCallback;
import fuzs.puzzleslib.api.client.event.v1.gui.RenderGuiEvents;
import fuzs.puzzleslib.api.client.key.v1.KeyActivationHandler;
import fuzs.puzzleslib.api.client.key.v1.KeyMappingHelper;
import fuzs.puzzleslib.api.event.v1.data.MutableInt;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;

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
            posY.mapInt(value -> value - HoveringHotbar.CONFIG.get(ClientConfig.class).getHotbarOffset());
        });
        RenderGuiEvents.BEFORE.register((Gui gui, GuiGraphics guiGraphics, DeltaTracker deltaTracker) -> {
            ClientAbstractions.INSTANCE.addGuiLeftHeight(gui,
                    HoveringHotbar.CONFIG.get(ClientConfig.class).getHotbarOffset());
            ClientAbstractions.INSTANCE.addGuiRightHeight(gui,
                    HoveringHotbar.CONFIG.get(ClientConfig.class).getHotbarOffset());
        });
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
}
