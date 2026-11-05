package com.larpclient;

import com.larpclient.module.Module;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.state.level.CameraRenderState;

final class mb {
   private static List<c> V = List.of();

   static void i(List<Module> var0) {
      V = var0.stream().filter(var0x -> var0x instanceof c).map(var0x -> (c)var0x).toList();
   }

   static void i(Minecraft var0) {
      for (c var2 : V) {
         if (K(var2)) {
            h.K(var2.effectName(), () -> var2.tick(var0));
         }
      }
   }

   static void P(float var0, CameraRenderState var1) {
      for (c var3 : V) {
         if (K(var3)) {
            h.K(var3.effectName(), () -> {
               long var3x = kx.G();

               try {
                  var3.prepare(var0, var1);
               } finally {
                  kx.u(var3.effectName() + " preparation CPU", var3x);
               }
            });
         }
      }
   }

   static void w(ki var0, boolean var1) {
      if (!var1 || _k.t != m5.n.N) {
         for (c var3 : V) {
            if (K(var3)) {
               h.K(var3.effectName(), () -> {
                  long var3x = kx.G();

                  try {
                     var3.render(var0, var1);
                  } finally {
                     var0.v(false);
                     var0.blend(pf.x.S);
                     kx.u(var3.effectName() + (var1 ? " glow CPU" : " base CPU"), var3x);
                  }
               });
            }
         }
      }
   }

   private static boolean K(c var0) {
      return !(var0 instanceof Module var1 && !var1.isRuntimeActive());
   }

   static boolean h() {
      return _k.t != m5.n.N && V.stream().anyMatch(var0 -> K(var0) && !h.m(var0.effectName()) && var0.glowing());
   }

   static boolean f() {
      return _k.t != m5.n.N && V.stream().anyMatch(var0 -> K(var0) && !h.m(var0.effectName()) && var0.soft());
   }

   static void e() {
      for (c var1 : V) {
         A(var1.effectName());
      }
   }

   static void A(String var0) {
      for (c var2 : V) {
         if (var2.effectName().equals(var0)) {
            try {
               var2.clear();
            } catch (RuntimeException var4) {
               mt.B("Render", "Effect cleanup failed: " + var0, var4);
            }
         }
      }
   }

   private mb() {
   }
}
