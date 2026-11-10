package com.larp.obf.qa300483f54d804a3;

import com.larpclient.ci;
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
public class qc398da74bd97f3bc {
   @Inject(method = "getSkin", at = @At("RETURN"), cancellable = true)
   private void B(CallbackInfoReturnable<PlayerSkin> var1) {
      UUID var2 = ((AbstractClientPlayer)this).getUUID();
      PlayerSkin var3 = ci.X(var2, (PlayerSkin)var1.getReturnValue());
      SkinChangerModule var4 = SkinChangerModule.X();
      if (var4 != null) {
         var3 = var4.I(var2, var3);
      }

      CapesModule var5 = CapesModule.K();
      var1.setReturnValue(var5 == null ? var3 : var5.L(var2, var3));
   }
}
