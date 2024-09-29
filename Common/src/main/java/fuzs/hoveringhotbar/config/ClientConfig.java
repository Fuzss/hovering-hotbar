package fuzs.hoveringhotbar.config;

import com.google.common.collect.ImmutableSet;
import fuzs.puzzleslib.api.client.event.v1.gui.RenderGuiLayerEvents;
import fuzs.puzzleslib.api.config.v3.Config;
import fuzs.puzzleslib.api.config.v3.ConfigCore;
import fuzs.puzzleslib.api.config.v3.ValueCallback;
import fuzs.puzzleslib.api.core.v1.ModLoaderEnvironment;
import fuzs.puzzleslib.api.core.v1.utility.ResourceLocationHelper;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ClientConfig implements ConfigCore {
    @Config(description = "Height offset for the hotbar from the screen bottom.")
    @Config.IntRange(min = 0)
    public int hotbarOffset = 2;
    @Config(description = "Move the experience level display above the experience bar.")
    public boolean moveExperienceAboveBar = true;

    private ModConfigSpec.ConfigValue<List<? extends String>> hotbarGuiLayersValue;
    public Set<ResourceLocation> hotbarGuiLayers = Collections.emptySet();

    @Override
    public void addToBuilder(ModConfigSpec.Builder builder, ValueCallback callback) {
        if (ModLoaderEnvironment.INSTANCE.getModLoader().isForgeLike()) {
            // We do not need to include {@link VanillaGuiLayers#SELECTED_ITEM_NAME} &amp; {@link VanillaGuiLayers#OVERLAY_MESSAGE},
            // both get their render height from {@link Gui#leftHeight} &amp; {@link Gui#rightHeight}.
            List<String> defaultValue = Stream.of(RenderGuiLayerEvents.HOTBAR, RenderGuiLayerEvents.JUMP_METER,
                    RenderGuiLayerEvents.EXPERIENCE_BAR, RenderGuiLayerEvents.SPECTATOR_TOOLTIP,
                    RenderGuiLayerEvents.EXPERIENCE_LEVEL
            ).map(ResourceLocation::toString).collect(Collectors.toList());
            this.hotbarGuiLayersValue = builder.comment(
                    "Defines a set of gui layers that should be shifted together with the hotbar.").defineList(
                    "hotbar_gui_layers", defaultValue, () -> "",
                    o -> o instanceof String s && ResourceLocationHelper.tryParse(s) != null
            );
        }
    }

    @Override
    public void afterConfigReload() {
        if (ModLoaderEnvironment.INSTANCE.getModLoader().isForgeLike()) {
            this.hotbarGuiLayers = this.hotbarGuiLayersValue.get().stream().map(ResourceLocationHelper::parse).collect(
                    ImmutableSet.toImmutableSet());
        }
    }
}
