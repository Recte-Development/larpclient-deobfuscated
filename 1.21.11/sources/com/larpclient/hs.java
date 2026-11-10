package com.larpclient;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_1297;
import net.minecraft.class_1542;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_310;
import net.minecraft.class_9209;
import net.minecraft.class_9334;

@Environment(EnvType.CLIENT)
public class hs {
   private static final hs S = new hs();
   public boolean p;

   private hs() {
   }

   public static hs B() {
      return S;
   }

   public void H() {
      qc.i().A();
   }

   public void K() {
      qc.i().P();
   }

   public void v() {
      qc.i().clearQueue();
   }

   public String B() {
      return qc.i().pendingLabel();
   }

   public void u(class_310 var1) {
      if (var1 != null && var1.field_1687 != null) {
         for (class_1297 var3 : var1.field_1687.method_18112()) {
            if (var3 instanceof class_1542 var4) {
               class_1799 var5 = var4.method_6983();
               class_9209 var6 = (class_9209)var5.method_58694(class_9334.field_49646);
               int var7 = qc.i().c(var6);
               boolean var8 = var5.method_31574(class_1802.field_8791);
               if (!this.p && var7 > 0 || var8) {
                  String var9 = var8 ? "Wither Skull (Lose)" : Integer.toString(var7);
                  yq.M(var1, hu.q.C, "Odd/Even", var4, var9, var9, this.p);
               }
            }
         }
      }

      if (!this.p) {
         qc.i().clearQueue();
      } else {
         qc.i().tick(var1);
      }
   }
}
