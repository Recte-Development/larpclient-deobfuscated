package com.larp.obf.qa300483f54d804a3;

import com.larpclient.zt;
import net.minecraft.client.multiplayer.chat.GuiMessage;
import net.minecraft.network.chat.FormattedText;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(GuiMessage.class)
public class qbb28d63f46038ab4 {
   @ModifyArg(
      method = "splitLines",
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/gui/components/ComponentRenderUtils;wrapComponents(Lnet/minecraft/network/chat/FormattedText;ILnet/minecraft/client/gui/Font;)Ljava/util/List;"
      ),
      index = 0
   )
   private FormattedText Z(FormattedText var1) {
      return zt.g(var1);
   }
}
