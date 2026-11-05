package com.larp.obf.qbac0b262827a15ac;

import com.larpclient._r;
import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.FormattedText;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(Font.class)
public class qafb1f3f7439b181c {
   @ModifyVariable(
      method = {"width(Lnet/minecraft/network/chat/FormattedText;)I", "split(Lnet/minecraft/network/chat/FormattedText;I)Ljava/util/List;"},
      at = @At("HEAD"),
      argsOnly = true
   )
   private FormattedText Q(FormattedText var1) {
      return _r.t(var1);
   }
}
