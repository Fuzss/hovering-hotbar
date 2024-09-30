package fuzs.hoveringhotbar.client;

import fuzs.hoveringhotbar.HoveringHotbar;
import fuzs.hoveringhotbar.config.ClientConfig;
import fuzs.puzzleslib.api.client.core.v1.ClientAbstractions;
import fuzs.puzzleslib.api.client.core.v1.ClientModConstructor;
import fuzs.puzzleslib.api.client.event.v1.gui.CustomizeChatPanelCallback;
import fuzs.puzzleslib.api.client.event.v1.gui.RenderGuiEvents;
import fuzs.puzzleslib.api.event.v1.data.MutableInt;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;

public class HoveringHotbarClient implements ClientModConstructor {

    @Override
    public void onConstructMod() {
        registerEventHandlers();
    }

    private static void registerEventHandlers() {
        CustomizeChatPanelCallback.EVENT.register(
                (GuiGraphics guiGraphics, DeltaTracker deltaTracker, MutableInt posX, MutableInt posY) -> {
                    posY.mapInt(value -> value - HoveringHotbar.CONFIG.get(ClientConfig.class).hotbarOffset);
                });
        RenderGuiEvents.BEFORE.register((Gui gui, GuiGraphics guiGraphics, DeltaTracker deltaTracker) -> {
            ClientAbstractions.INSTANCE.addGuiLeftHeight(gui,
                    HoveringHotbar.CONFIG.get(ClientConfig.class).hotbarOffset
            );
            ClientAbstractions.INSTANCE.addGuiRightHeight(gui,
                    HoveringHotbar.CONFIG.get(ClientConfig.class).hotbarOffset
            );
        });
    }
}
