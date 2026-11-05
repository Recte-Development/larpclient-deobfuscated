package com.larpclient;

import com.larpclient.module.AntiKnockbackModule;
import net.minecraft.world.phys.Vec3;

public final class f6 {
   public static Vec3 u(Vec3 var0, double var1, double var3) {
      return new Vec3(var0.x * var1, var0.y * var3, var0.z * var1);
   }

   public static Vec3 z(Vec3 var0, boolean var1) {
      LarpClient var2 = LarpClient.y();
      if (var2 != null && var2.e() != null) {
         com.larpclient.module.ly var3 = var2.e();
         AntiKnockbackModule var4 = var3.y(AntiKnockbackModule.class);
         return var4 != null && var4.isRuntimeActive() ? u(var0, var4.F(), var4.b()) : var0;
      } else {
         return var0;
      }
   }

   private f6() {
   }
}
