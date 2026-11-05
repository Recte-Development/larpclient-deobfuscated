package com.larpclient;

import com.larpclient.module.AntiKnockbackModule;
import com.larpclient.module.dz;
import net.minecraft.world.phys.Vec3;

public final class x {
   public static Vec3 N(Vec3 var0, double var1, double var3) {
      return new Vec3(var0.x * var1, var0.y * var3, var0.z * var1);
   }

   public static Vec3 l(Vec3 var0, boolean var1) {
      LarpClient var2 = LarpClient.F();
      if (var2 != null && var2.x() != null) {
         dz var3 = var2.x();
         AntiKnockbackModule var4 = var3.V(AntiKnockbackModule.class);
         return var4 != null && var4.isRuntimeActive() ? N(var0, var4.A(), var4.V()) : var0;
      } else {
         return var0;
      }
   }

   private x() {
   }
}
