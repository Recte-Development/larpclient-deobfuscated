package com.larp.obf.q5682c6a8aca9b6ec;

import com.larpclient._7;
import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.FormattedText;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(Font.class)
public class qb12ed169c3a35b73 {
   @ModifyVariable(
      method = {"width(Lnet/minecraft/network/chat/FormattedText;)I", "split(Lnet/minecraft/network/chat/FormattedText;I)Ljava/util/List;"},
      at = @At("HEAD"),
      argsOnly = true
   )
   private FormattedText V(FormattedText var1) {
      return _7.s(var1);
   }
}
