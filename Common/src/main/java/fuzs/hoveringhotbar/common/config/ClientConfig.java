package fuzs.hoveringhotbar.common.config;

import com.google.common.collect.ImmutableSet;
import fuzs.puzzleslib.common.api.config.v3.Config;
import fuzs.puzzleslib.common.api.config.v3.ConfigCore;
import fuzs.puzzleslib.common.api.config.v3.ValueCallback;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.Identifier;
import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class ClientConfig implements ConfigCore {
    private static final List<Identifier> DEFAULT_HOTBAR_GUI_LAYERS = List.of(Identifier.parse(
                    "hotbarslotcycling:cycling_slots"),
            Identifier.parse("enchantmentswitch:slot_overlay"),
            Identifier.parse("lockedinslots:slot_overlay"));

    @Config(description = "Move the experience level display above the experience bar.", gameRestart = true)
    public boolean moveExperienceAboveBar = true;

    private ModConfigSpec.IntValue hotbarOffsetValue;
    private ModConfigSpec.ConfigValue<List<? extends String>> hotbarGuiLayersValue;
    private int configSaveDelay;
    public Set<Identifier> hotbarGuiLayers;

    public int getHotbarOffset() {
        return this.hotbarOffsetValue.getAsInt();
    }

    public void updateHotbarOffset(int screenHeight, boolean moveUp) {
        this.hotbarOffsetValue.set(Math.clamp(this.getHotbarOffset() + (moveUp ? 1 : -1), 0, screenHeight));
        this.configSaveDelay = 40;
    }

    public void onEndClientTick(Minecraft minecraft) {
        if (this.configSaveDelay > 0 && --this.configSaveDelay == 0) {
            this.hotbarOffsetValue.save();
        }
    }

    @Override
    public void addToBuilder(ModConfigSpec.Builder builder, ValueCallback callback) {
        this.hotbarOffsetValue = builder.comment("Height offset for the hotbar from the screen bottom.")
                .defineInRange("hotbar_offset", 2, 0, Integer.MAX_VALUE);
        this.hotbarGuiLayersValue = builder.comment(
                        "Defines a set of gui layers that should be shifted together with the hotbar.")
                .gameRestart()
                .defineListAllowEmpty("hotbar_gui_layers",
                        DEFAULT_HOTBAR_GUI_LAYERS.stream().map(Identifier::toString).collect(Collectors.toList()),
                        () -> "",
                        (Object o) -> {
                            return o instanceof String string && Identifier.tryParse(string) != null;
                        });
    }

    @Override
    public void afterConfigReload() {
        this.hotbarGuiLayers = this.hotbarGuiLayersValue.get()
                .stream()
                .map(Identifier::tryParse)
                .filter(Objects::nonNull)
                .collect(ImmutableSet.toImmutableSet());
    }
}
