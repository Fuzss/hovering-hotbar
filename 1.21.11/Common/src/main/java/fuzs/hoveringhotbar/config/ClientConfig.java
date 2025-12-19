package fuzs.hoveringhotbar.config;

import com.google.common.collect.ImmutableSet;
import fuzs.puzzleslib.api.config.v3.Config;
import fuzs.puzzleslib.api.config.v3.ConfigCore;
import fuzs.puzzleslib.api.config.v3.ValueCallback;
import net.minecraft.resources.Identifier;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.Identifier;
import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.List;
import java.util.Objects;
import java.util.Set;

public class ClientConfig implements ConfigCore {
    @Config(description = "Move the experience level display above the experience bar.", gameRestart = true)
    public boolean moveExperienceAboveBar = true;
    @Config(name = "hotbar_gui_layers",
            description = "Defines a set of gui layers that should be shifted together with the hotbar.",
            gameRestart = true)
    List<String> hotbarGuiLayersRaw = List.of("hotbarslotcycling:cycling_slots",
            "enchantmentswitch:slot_overlay",
            "lockedinslots:slot_overlay");

    private ModConfigSpec.IntValue hotbarOffsetValue;
    private int configSaveDelay;
    public Set<Identifier> hotbarGuiLayers;

    public int getHotbarOffset() {
        return this.hotbarOffsetValue.getAsInt();
    }

    public void updateHotbarOffset(int screenHeight, boolean moveUp) {
        this.hotbarOffsetValue.set(Math.clamp(this.getHotbarOffset() + (moveUp ? 1 : -1), 0, screenHeight));
        this.configSaveDelay = 20;
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
    }

    @Override
    public void afterConfigReload() {
        this.hotbarGuiLayers = this.hotbarGuiLayersRaw.stream()
                .map(Identifier::tryParse)
                .filter(Objects::nonNull)
                .collect(ImmutableSet.toImmutableSet());
    }
}
