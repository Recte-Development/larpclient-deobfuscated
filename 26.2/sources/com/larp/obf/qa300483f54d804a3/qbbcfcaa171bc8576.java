package com.larp.obf.qa300483f54d804a3;

import com.larpclient.p_;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public class qbbcfcaa171bc8576 {
   @Inject(method = "turn", at = @At("HEAD"), cancellable = true)
   private void i(double var1, double var3, CallbackInfo var5) {
      Entity var6 = (Entity)this;
      Minecraft var7 = Minecraft.getInstance();
      if (p_.A() != p_.o.T && var6 == var7.player) {
         p_.M(var1, var3);
         var5.cancel();
      }
   }
}
