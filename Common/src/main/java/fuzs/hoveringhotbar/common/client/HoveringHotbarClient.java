package fuzs.hoveringhotbar.common.client;

import com.google.common.collect.ImmutableList;
import fuzs.hoveringhotbar.common.HoveringHotbar;
import fuzs.hoveringhotbar.common.config.ClientConfig;
import fuzs.puzzleslib.common.api.client.core.v1.ClientModConstructor;
import fuzs.puzzleslib.common.api.client.core.v1.context.GuiLayersContext;
import fuzs.puzzleslib.common.api.client.core.v1.context.KeyMappingsContext;
import fuzs.puzzleslib.common.api.client.event.v1.ClientLifecycleEvents;
import fuzs.puzzleslib.common.api.client.event.v1.ClientTickEvents;
import fuzs.puzzleslib.common.api.client.key.v1.KeyActivationHandler;
import fuzs.puzzleslib.common.api.client.key.v1.KeyMappingHelper;
import fuzs.puzzleslib.common.api.core.v1.ModLoaderEnvironment;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Player;

import java.util.List;

public class HoveringHotbarClient implements ClientModConstructor {
    public static final KeyMapping MOVE_HOTBAR_UP_KEY_MAPPING = KeyMappingHelper.registerUnboundKeyMapping(
            HoveringHotbar.id("move_hotbar_up"));
    public static final KeyMapping MOVE_HOTBAR_DOWN_KEY_MAPPING = KeyMappingHelper.registerUnboundKeyMapping(
            HoveringHotbar.id("move_hotbar_down"));
    public static final Identifier LEFT_HOTBAR_OFFSET_LOCATION = HoveringHotbar.id("left_hotbar_offset");
    public static final Identifier RIGHT_HOTBAR_OFFSET_LOCATION = HoveringHotbar.id("right_hotbar_offset");
    private static final List<Identifier> HOTBAR_GUI_LAYER_LOCATIONS = ImmutableList.of(GuiLayersContext.HOTBAR,
            GuiLayersContext.INFO_BAR,
            GuiLayersContext.EXPERIENCE_LEVEL);
    private static final GuiLayersContext.Layer EMPTY_LAYER = (GuiGraphicsExtractor guiGraphics, DeltaTracker deltaTracker) -> {
        // NO-OP
    };

    @Override
    public void onConstructMod() {
        registerEventHandlers();
    }

    private static void registerEventHandlers() {
        ClientTickEvents.END.register(HoveringHotbar.CONFIG.get(ClientConfig.class)::onEndClientTick);
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
        // register fake layers, so we can have status bar height providers which run as early as possible
        // on Fabric this must be a status bar, all other gui layers are sorted afterward
        context.registerGuiLayer(LEFT_HOTBAR_OFFSET_LOCATION,
                ModLoaderEnvironment.INSTANCE.getModLoader().isFabric() ? GuiLayersContext.PLAYER_HEALTH :
                        GuiLayersContext.HOTBAR,
                EMPTY_LAYER);
        context.registerGuiLayer(RIGHT_HOTBAR_OFFSET_LOCATION,
                ModLoaderEnvironment.INSTANCE.getModLoader().isFabric() ? GuiLayersContext.VEHICLE_HEALTH :
                        GuiLayersContext.HOTBAR,
                EMPTY_LAYER);
        context.addLeftStatusBarHeightProvider(LEFT_HOTBAR_OFFSET_LOCATION, (Player player) -> {
            return HoveringHotbar.CONFIG.get(ClientConfig.class).getHotbarOffset();
        });
        context.addRightStatusBarHeightProvider(RIGHT_HOTBAR_OFFSET_LOCATION, (Player player) -> {
            return HoveringHotbar.CONFIG.get(ClientConfig.class).getHotbarOffset();
        });
        // push the experience bar level text above the bar, similar to the legacy console edition
        context.replaceGuiLayer(GuiLayersContext.EXPERIENCE_LEVEL, (GuiLayersContext.Layer layer) -> {
            return (GuiGraphicsExtractor guiGraphics, DeltaTracker deltaTracker) -> {
                if (HoveringHotbar.CONFIG.get(ClientConfig.class).moveExperienceAboveBar) {
                    guiGraphics.pose().pushMatrix();
                    guiGraphics.pose().translate(0.0F, -3.0F);
                    layer.extractRenderState(guiGraphics, deltaTracker);
                    guiGraphics.pose().popMatrix();
                } else {
                    layer.extractRenderState(guiGraphics, deltaTracker);
                }
            };
        });
        // run this as late as possible, so we can wrap the most possible layers
        ClientLifecycleEvents.STARTED.register((Minecraft minecraft) -> {
            for (Identifier identifier : HOTBAR_GUI_LAYER_LOCATIONS) {
                context.replaceGuiLayer(identifier, HoveringHotbarClient::getLayerWithTranslation);
            }
        });
    }

    public static GuiLayersContext.Layer getLayerWithTranslation(GuiLayersContext.Layer layer) {
        return (GuiGraphicsExtractor guiGraphics, DeltaTracker deltaTracker) -> {
            guiGraphics.pose().pushMatrix();
            guiGraphics.pose().translate(0.0F, -HoveringHotbar.CONFIG.get(ClientConfig.class).getHotbarOffset());
            layer.extractRenderState(guiGraphics, deltaTracker);
            guiGraphics.pose().popMatrix();
        };
    }
}
