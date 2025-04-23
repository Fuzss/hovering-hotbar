package fuzs.hoveringhotbar.client.helper;

import com.google.common.collect.ImmutableList;
import fuzs.hoveringhotbar.HoveringHotbar;
import fuzs.hoveringhotbar.config.ClientConfig;
import fuzs.puzzleslib.api.client.core.v1.context.GuiLayersContext;
import fuzs.puzzleslib.api.client.gui.v2.GuiGraphicsHelper;
import fuzs.puzzleslib.api.client.gui.v2.GuiHeightHelper;
import fuzs.puzzleslib.api.core.v1.ModLoaderEnvironment;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.LayeredDraw;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

import java.util.List;

public class HotbarSpriteHelper {
    private static final ResourceLocation HOTBAR_SELECTION_SPRITE = ResourceLocation.withDefaultNamespace(
            "hud/hotbar_selection");
    static final List<ResourceLocation> HOTBAR_GUI_LAYER_LOCATIONS = ImmutableList.of(GuiLayersContext.HOTBAR,
            GuiLayersContext.JUMP_METER,
            GuiLayersContext.EXPERIENCE_BAR,
            GuiLayersContext.EXPERIENCE_LEVEL);
    static final List<ResourceLocation> FABRIC_HOTBAR_GUI_LAYER_LOCATIONS = ImmutableList.of(GuiLayersContext.EXPERIENCE_LEVEL);

    public static List<ResourceLocation> getHotbarGuiLayerLocations() {
        // other layers are handled via the native event on Fabric, as it offers a gui layer encompassing most hotbar elements,
        // which has a good chance of also catching anything modded
        if (ModLoaderEnvironment.INSTANCE.getModLoader().isFabricLike()) {
            return FABRIC_HOTBAR_GUI_LAYER_LOCATIONS;
        } else {
            return HOTBAR_GUI_LAYER_LOCATIONS;
        }
    }

    public static void blitHotbarSelectionSprite(GuiGraphics guiGraphics, DeltaTracker deltaTracker) {
        if (Minecraft.getInstance().getCameraEntity() instanceof Player player) {
            guiGraphics.pose().pushPose();
            guiGraphics.pose().translate(0.0F, 0.0F, -95.0F);
            int posX = guiGraphics.guiWidth() / 2 - 91 - 1 + player.getInventory().getSelectedSlot() * 20;
            int posY = guiGraphics.guiHeight() - 22 - 1;
            GuiGraphicsHelper.blitTiledSprite(guiGraphics,
                    RenderType::guiTextured,
                    HOTBAR_SELECTION_SPRITE,
                    posX,
                    posY,
                    24,
                    24,
                    24,
                    23);
            guiGraphics.pose().popPose();
        }
    }

    public static LayeredDraw.Layer getLayerWithTranslation(LayeredDraw.Layer layer) {
        return (GuiGraphics guiGraphics, DeltaTracker deltaTracker) -> {
            guiGraphics.pose().pushPose();
            guiGraphics.pose().translate(0.0F, -HoveringHotbar.CONFIG.get(ClientConfig.class).getHotbarOffset(), 0.0F);
            layer.render(guiGraphics, deltaTracker);
            guiGraphics.pose().popPose();
        };
    }

    public static void applyGuiHeight(Gui gui) {
        GuiHeightHelper.addLeftHeight(gui, HoveringHotbar.CONFIG.get(ClientConfig.class).getHotbarOffset());
        GuiHeightHelper.addRightHeight(gui, HoveringHotbar.CONFIG.get(ClientConfig.class).getHotbarOffset());
    }
}
