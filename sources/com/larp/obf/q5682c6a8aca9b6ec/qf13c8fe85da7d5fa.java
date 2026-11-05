package com.larp.obf.q5682c6a8aca9b6ec;

import com.larpclient.x2;
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
public class qf13c8fe85da7d5fa {
   @Inject(method = "getSkin", at = @At("RETURN"), cancellable = true)
   private void I(CallbackInfoReturnable<PlayerSkin> var1) {
      UUID var2 = ((AbstractClientPlayer)this).getUUID();
      PlayerSkin var3 = x2.X(var2, (PlayerSkin)var1.getReturnValue());
      SkinChangerModule var4 = SkinChangerModule.b();
      if (var4 != null) {
         var3 = var4.C(var2, var3);
      }

      CapesModule var5 = CapesModule.p();
      var1.setReturnValue(var5 == null ? var3 : var5.b(var2, var3));
   }
}
