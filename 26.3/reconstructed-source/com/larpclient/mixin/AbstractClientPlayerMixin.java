package com.larpclient.mixin;

import com.larpclient.module.misc.SkinChangerModule;
import com.larpclient.module.render.CapesModule;
import com.larpclient.social.cosmetic.CosmeticOverrides;
import java.util.UUID;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.world.entity.player.PlayerSkin;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractClientPlayer.class)
public class AbstractClientPlayerMixin {
   @Inject(method = "getSkin", at = @At("RETURN"), cancellable = true)
   private void J(CallbackInfoReturnable<PlayerSkin> var1) {
      UUID var2 = ((AbstractClientPlayer)this).getUUID();
      PlayerSkin var3 = CosmeticOverrides.applySkin(var2, (PlayerSkin)var1.getReturnValue());
      SkinChangerModule var4 = SkinChangerModule.getInstance();
      if (var4 != null) {
         var3 = var4.overrideSkinFor(var2, var3);
      }

      CapesModule var5 = CapesModule.getInstance();
      var1.setReturnValue(var5 == null ? var3 : var5.applyToOwnSkin(var2, var3));
   }
}
