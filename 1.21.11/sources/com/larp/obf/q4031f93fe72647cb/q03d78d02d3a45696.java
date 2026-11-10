package com.larp.obf.q4031f93fe72647cb;

import com.larpclient.gf;
import com.mojang.authlib.exceptions.AuthenticationException;
import com.mojang.authlib.minecraft.MinecraftSessionService;
import java.util.UUID;
import net.minecraft.class_2535;
import net.minecraft.class_635;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(class_635.class)
public abstract class q03d78d02d3a45696 {
   @Shadow
   @Final
   private class_2535 field_3707;

   @Redirect(
      method = "method_2892",
      at = @At(
         value = "INVOKE",
         target = "Lcom/mojang/authlib/minecraft/MinecraftSessionService;joinServer(Ljava/util/UUID;Ljava/lang/String;Ljava/lang/String;)V"
      )
   )
   private void Y(MinecraftSessionService var1, UUID var2, String var3, String var4) throws AuthenticationException {
      gf.I(var1, ((q035afe6f3da5ac7a)this.field_3707).larpProxyChannel(), var2, var3, var4);
   }
}
