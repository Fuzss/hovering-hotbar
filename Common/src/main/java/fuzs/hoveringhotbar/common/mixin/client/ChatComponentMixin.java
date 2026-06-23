package fuzs.hoveringhotbar.common.mixin.client;

import fuzs.hoveringhotbar.common.HoveringHotbar;
import fuzs.hoveringhotbar.common.config.ClientConfig;
import net.minecraft.client.gui.components.ChatComponent;
import net.minecraft.util.Mth;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(ChatComponent.class)
abstract class ChatComponentMixin {
    @Shadow
    @Final
    private static int BOTTOM_MARGIN;

    @ModifyVariable(method = "extractRenderState(Lnet/minecraft/client/gui/components/ChatComponent$ChatGraphicsAccess;IILnet/minecraft/client/gui/components/ChatComponent$DisplayMode;)V",
                    at = @At("STORE"),
                    ordinal = 4)
    private int extractRenderState(int chatBottom, ChatComponent.ChatGraphicsAccess graphics, int screenHeight, int ticks, ChatComponent.DisplayMode displayMode) {
        int hotbarOffset = HoveringHotbar.CONFIG.get(ClientConfig.class).getHotbarOffset();
        if (hotbarOffset == 0) {
            return chatBottom;
        }

        float scale = (float) this.getScale();
        return Mth.floor((float) (screenHeight - BOTTOM_MARGIN - hotbarOffset) / scale);
    }

    @Shadow
    protected abstract double getScale();
}
