package com.larpclient;

import com.larpclient.module.Module;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.state.level.CameraRenderState;

final class wv {
   private static List<tk> x = List.of();

   static void l(List<Module> var0) {
      x = var0.stream().filter(var0x -> var0x instanceof tk).map(var0x -> (tk)var0x).toList();
   }

   static void a(Minecraft var0) {
      for (tk var2 : x) {
         if (v(var2)) {
            kz.W(var2.effectName(), () -> var2.tick(var0));
         }
      }
   }

   static void m(float var0, CameraRenderState var1) {
      for (tk var3 : x) {
         if (v(var3)) {
            kz.W(var3.effectName(), () -> {
               long var3x = zj.m();

               try {
                  var3.prepare(var0, var1);
               } finally {
                  zj.l(var3.effectName() + " preparation CPU", var3x);
               }
            });
         }
      }
   }

   static void K(kj var0, boolean var1) {
      if (!var1 || y.m != wa.a.Z) {
         for (tk var3 : x) {
            if (v(var3)) {
               kz.W(var3.effectName(), () -> {
                  long var3x = zj.m();

                  try {
                     var3.render(var0, var1);
                  } finally {
                     var0.j(false);
                     var0.blend(gl.c.K);
                     zj.l(var3.effectName() + (var1 ? " glow CPU" : " base CPU"), var3x);
                  }
               });
            }
         }
      }
   }

   private static boolean v(tk var0) {
      return !(var0 instanceof Module var1 && !var1.isRuntimeActive());
   }

   static boolean n() {
      return y.m != wa.a.Z && x.stream().anyMatch(var0 -> v(var0) && !kz.B(var0.effectName()) && var0.glowing());
   }

   static boolean W() {
      return y.m != wa.a.Z && x.stream().anyMatch(var0 -> v(var0) && !kz.B(var0.effectName()) && var0.soft());
   }

   static void h() {
      for (tk var1 : x) {
         f(var1.effectName());
      }
   }

   static void f(String var0) {
      for (tk var2 : x) {
         if (var2.effectName().equals(var0)) {
            try {
               var2.clear();
            } catch (RuntimeException var4) {
               h.w("Render", "Effect cleanup failed: " + var0, var4);
            }
         }
      }
   }

   private wv() {
   }
}
