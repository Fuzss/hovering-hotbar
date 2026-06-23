package fuzs.hoveringhotbar.common.mixin.client;

import net.minecraft.client.gui.Gui;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(Gui.class)
abstract class GuiMixin {

    @ModifyArg(method = "extractItemHotbar",
               at = @At(value = "INVOKE",
                        target = "Lnet/minecraft/client/gui/GuiGraphicsExtractor;blitSprite(Lcom/mojang/blaze3d/pipeline/RenderPipeline;Lnet/minecraft/resources/Identifier;IIII)V"),
               index = 5)
    private int extractItemHotbar(int height) {
        return height == 23 ? 24 : height;
    }
}
