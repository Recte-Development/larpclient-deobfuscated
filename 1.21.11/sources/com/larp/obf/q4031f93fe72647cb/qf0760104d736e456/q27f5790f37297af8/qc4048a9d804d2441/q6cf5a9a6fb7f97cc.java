package com.larp.obf.q4031f93fe72647cb.qf0760104d736e456.q27f5790f37297af8.qc4048a9d804d2441;

import com.larpclient.cz;
import net.minecraft.class_2338;
import net.minecraft.class_2680;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Desc;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Pseudo
@Mixin(targets = "net/caffeinemc/mods/sodium/client/world/LevelSlice", remap = false)
public abstract class q6cf5a9a6fb7f97cc {
   @Inject(
      target = @Desc(args = {int.class, int.class, int.class}, ret = class_2680.class, value = "getBlockState"),
      at = @At("RETURN"),
      cancellable = true,
      remap = false
   )
   private void V(int var1, int var2, int var3, CallbackInfoReturnable<class_2680> var4) {
      cz var5 = cz.T();
      long var6 = class_2338.method_10064(var1, var2, var3);
      var4.setReturnValue(var5.r(var6, var5.O(var6, (class_2680)var4.getReturnValue())));
   }
}
