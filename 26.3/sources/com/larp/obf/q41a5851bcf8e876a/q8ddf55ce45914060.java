package com.larp.obf.q41a5851bcf8e876a;

import com.larpclient.dr;
import net.minecraft.client.multiplayer.chat.GuiMessage;
import net.minecraft.network.chat.FormattedText;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(GuiMessage.class)
public class q8ddf55ce45914060 {
   @ModifyArg(
      method = "splitLines",
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/client/gui/components/ComponentRenderUtils;wrapComponents(Lnet/minecraft/network/chat/FormattedText;ILnet/minecraft/client/gui/Font;)Ljava/util/List;"
      ),
      index = 0
   )
   private FormattedText d(FormattedText var1) {
      return dr.L(var1);
   }
}
