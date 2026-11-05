package com.larp.obf.qbac0b262827a15ac;

import com.larpclient.t7;
import com.larpclient.module.CapesModule;
import com.larpclient.module.SkinChangerModule;
import java.util.UUID;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.world.entity.player.PlayerSkin;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractClientPlayer.class)
public class q548d009c44a35e41 {
   @Inject(method = "getSkin", at = @At("RETURN"), cancellable = true)
   private void c(CallbackInfoReturnable<PlayerSkin> var1) {
      UUID var2 = ((AbstractClientPlayer)this).getUUID();
      PlayerSkin var3 = t7.n(var2, (PlayerSkin)var1.getReturnValue());
      SkinChangerModule var4 = SkinChangerModule.X();
      if (var4 != null) {
         var3 = var4.L(var2, var3);
      }

      CapesModule var5 = CapesModule.u();
      var1.setReturnValue(var5 == null ? var3 : var5.d(var2, var3));
   }
}
