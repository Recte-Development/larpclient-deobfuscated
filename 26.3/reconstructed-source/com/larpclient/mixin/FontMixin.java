package com.larpclient.mixin;

import com.larpclient.config.DisplayAliases;
import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.FormattedText;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(Font.class)
public class FontMixin {
   @ModifyVariable(
      method = {"width(Lnet/minecraft/network/chat/FormattedText;)I", "split(Lnet/minecraft/network/chat/FormattedText;I)Ljava/util/List;"},
      at = @At("HEAD"),
      argsOnly = true
   )
   private FormattedText e(FormattedText var1) {
      return DisplayAliases.rewrite(var1);
   }
}
