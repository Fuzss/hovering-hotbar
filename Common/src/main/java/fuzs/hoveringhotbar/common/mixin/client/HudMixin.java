package fuzs.hoveringhotbar.common.mixin.client;

import net.minecraft.client.gui.Hud;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Slice;

@Mixin(Hud.class)
abstract class HudMixin {

    @ModifyArg(method = "extractItemHotbar",
               at = @At(value = "INVOKE",
                        target = "Lnet/minecraft/client/gui/GuiGraphicsExtractor;blitSprite(Lcom/mojang/blaze3d/pipeline/RenderPipeline;Lnet/minecraft/resources/Identifier;IIII)V"),
               index = 5,
               slice = @Slice(from = @At(value = "FIELD",
                                         target = "Lnet/minecraft/client/gui/Hud;HOTBAR_SELECTION_SPRITE:Lnet/minecraft/resources/Identifier;",
                                         opcode = Opcodes.GETSTATIC),
                              to = @At(value = "FIELD",
                                       target = "Lnet/minecraft/client/gui/Hud;HOTBAR_OFFHAND_LEFT_SPRITE:Lnet/minecraft/resources/Identifier;",
                                       opcode = Opcodes.GETSTATIC)))
    private int extractItemHotbar(int height) {
        return height == 23 ? 24 : height;
    }
}
