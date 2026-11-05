package com.larp.obf.q5682c6a8aca9b6ec;

import com.larpclient._7;
import net.minecraft.client.multiplayer.chat.GuiMessage;
import net.minecraft.network.chat.FormattedText;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(GuiMessage.class)
public class q561b1b1f945e6302 {
   @ModifyArg(
      method = "splitLines",
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/gui/components/ComponentRenderUtils;wrapComponents(Lnet/minecraft/network/chat/FormattedText;ILnet/minecraft/client/gui/Font;)Ljava/util/List;"
      ),
      index = 0
   )
   private FormattedText g(FormattedText var1) {
      return _7.s(var1);
   }
}
