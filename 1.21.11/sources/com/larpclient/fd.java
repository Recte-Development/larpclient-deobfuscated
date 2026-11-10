package com.larpclient;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_1297;
import net.minecraft.class_1542;
import net.minecraft.class_1799;
import net.minecraft.class_310;
import net.minecraft.class_9209;
import net.minecraft.class_9334;

@Environment(EnvType.CLIENT)
public final class fd {
   private static final fd e = new fd();
   public boolean v;

   private fd() {
   }

   public static fd h() {
      return e;
   }

   public void m(class_310 var1) {
      if (var1 != null && var1.field_1687 != null) {
         for (class_1297 var3 : var1.field_1687.method_18112()) {
            if (var3 instanceof class_1542 var4) {
               class_1799 var5 = var4.method_6983();
               class_9209 var6 = (class_9209)var5.method_58694(class_9334.field_49646);
               int var7 = qr.h().c(var6);
               if (!this.v && var7 > 0) {
                  String var8 = Integer.toString(var7);
                  yq.M(var1, hu.q.R, "HigherLower", var4, var8, var8, this.v);
               }
            }
         }
      }

      if (!this.v) {
         qr.h().clearQueue();
      }

      qr.h().tick(var1);
   }
}
