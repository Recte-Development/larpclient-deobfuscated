package com.larpclient;

import com.larpclient.module.Module;
import java.util.List;
import net.minecraft.class_12075;
import net.minecraft.class_310;

final class f5 {
   private static List<ll> f = List.of();

   static void X(List<Module> var0) {
      f = var0.stream().filter(var0x -> var0x instanceof ll).map(var0x -> (ll)var0x).toList();
   }

   static void X(class_310 var0) {
      for (ll var2 : f) {
         if (y(var2)) {
            wc.y(var2.effectName(), () -> var2.tick(var0));
         }
      }
   }

   static void i(float var0, class_12075 var1) {
      for (ll var3 : f) {
         if (y(var3)) {
            wc.y(var3.effectName(), () -> {
               long var3x = c5.X();

               try {
                  var3.prepare(var0, var1);
               } finally {
                  c5.w(var3.effectName() + " preparation CPU", var3x);
               }
            });
         }
      }
   }

   static void r(f1 var0, boolean var1) {
      if (!var1 || b0.r != t_.t.A) {
         for (ll var3 : f) {
            if (y(var3)) {
               wc.y(var3.effectName(), () -> {
                  long var3x = c5.X();

                  try {
                     var3.render(var0, var1);
                  } finally {
                     var0.I(false);
                     var0.blend(ti.d.G);
                     c5.w(var3.effectName() + (var1 ? " glow CPU" : " base CPU"), var3x);
                  }
               });
            }
         }
      }
   }

   private static boolean y(ll var0) {
      return !(var0 instanceof Module var1 && !var1.isRuntimeActive());
   }

   static boolean p() {
      return b0.r != t_.t.A && f.stream().anyMatch(var0 -> y(var0) && !wc.J(var0.effectName()) && var0.glowing());
   }

   static boolean s() {
      return b0.r != t_.t.A && f.stream().anyMatch(var0 -> y(var0) && !wc.J(var0.effectName()) && var0.soft());
   }

   static void V() {
      for (ll var1 : f) {
         i(var1.effectName());
      }
   }

   static void i(String var0) {
      for (ll var2 : f) {
         if (var2.effectName().equals(var0)) {
            try {
               var2.clear();
            } catch (RuntimeException var4) {
               q8.O("Render", "Effect cleanup failed: " + var0, var4);
            }
         }
      }
   }

   private f5() {
   }
}
