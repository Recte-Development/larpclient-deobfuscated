package com.larp.obf.q41a5851bcf8e876a;

import com.larpclient.p3;
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
public class q0ab9ccb090737207 {
   @Inject(method = "getSkin", at = @At("RETURN"), cancellable = true)
   private void J(CallbackInfoReturnable<PlayerSkin> var1) {
      UUID var2 = ((AbstractClientPlayer)this).getUUID();
      PlayerSkin var3 = p3.n(var2, (PlayerSkin)var1.getReturnValue());
      SkinChangerModule var4 = SkinChangerModule.h();
      if (var4 != null) {
         var3 = var4.n(var2, var3);
      }

      CapesModule var5 = CapesModule.E();
      var1.setReturnValue(var5 == null ? var3 : var5.N(var2, var3));
   }
}
