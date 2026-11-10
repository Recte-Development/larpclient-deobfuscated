package com.larp.obf.qa300483f54d804a3;

import com.larpclient.zt;
import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.FormattedText;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(Font.class)
public class q09aafea3b9d55c7c {
   @ModifyVariable(
      method = {"width(Lnet/minecraft/network/chat/FormattedText;)I", "split(Lnet/minecraft/network/chat/FormattedText;I)Ljava/util/List;"},
      at = @At("HEAD"),
      argsOnly = true
   )
   private FormattedText e(FormattedText var1) {
      return zt.g(var1);
   }
}
