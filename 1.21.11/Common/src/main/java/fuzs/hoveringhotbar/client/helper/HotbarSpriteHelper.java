package fuzs.hoveringhotbar.client.helper;

import com.google.common.collect.ImmutableList;
import fuzs.hoveringhotbar.HoveringHotbar;
import fuzs.hoveringhotbar.config.ClientConfig;
import fuzs.puzzleslib.api.client.core.v1.context.GuiLayersContext;
import fuzs.puzzleslib.api.client.gui.v2.GuiGraphicsHelper;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameType;

import java.util.List;

public class HotbarSpriteHelper {
    private static final Identifier HOTBAR_SELECTION_SPRITE = Identifier.withDefaultNamespace(
            "hud/hotbar_selection");
    public static final List<Identifier> HOTBAR_GUI_LAYER_LOCATIONS = ImmutableList.of(GuiLayersContext.HOTBAR,
            GuiLayersContext.INFO_BAR,
            GuiLayersContext.EXPERIENCE_LEVEL);

    public static void blitHotbarSelectionSprite(GuiGraphics guiGraphics, DeltaTracker deltaTracker) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.gameMode.getPlayerMode() != GameType.SPECTATOR) {
            if (minecraft.getCameraEntity() instanceof Player player) {
                int posX = guiGraphics.guiWidth() / 2 - 91 - 1 + player.getInventory().getSelectedSlot() * 20;
                int posY = guiGraphics.guiHeight() - 22 - 1;
                GuiGraphicsHelper.blitTiledSprite(guiGraphics,
                        RenderPipelines.GUI_TEXTURED,
                        HOTBAR_SELECTION_SPRITE,
                        posX,
                        posY,
                        24,
                        24,
                        24,
                        23);
            }
        }
    }

    public static GuiLayersContext.Layer getLayerWithTranslation(GuiLayersContext.Layer layer) {
        return (GuiGraphics guiGraphics, DeltaTracker deltaTracker) -> {
            guiGraphics.pose().pushMatrix();
            guiGraphics.pose().translate(0.0F, -HoveringHotbar.CONFIG.get(ClientConfig.class).getHotbarOffset());
            layer.render(guiGraphics, deltaTracker);
            guiGraphics.pose().popMatrix();
        };
    }
}
