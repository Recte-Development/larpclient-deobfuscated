package com.larpclient;

import com.larpclient.module.Module;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.state.level.CameraRenderState;

final class q3 {
   private static List<qe> R = List.of();

   static void C(List<Module> var0) {
      R = var0.stream().filter(var0x -> var0x instanceof qe).map(var0x -> (qe)var0x).toList();
   }

   static void X(Minecraft var0) {
      for (qe var2 : R) {
         if (p(var2)) {
            wp.X(var2.effectName(), () -> var2.tick(var0));
         }
      }
   }

   static void o(float var0, CameraRenderState var1) {
      for (qe var3 : R) {
         if (p(var3)) {
            wp.X(var3.effectName(), () -> {
               long var3x = he.s();

               try {
                  var3.prepare(var0, var1);
               } finally {
                  he.T(var3.effectName() + " preparation CPU", var3x);
               }
            });
         }
      }
   }

   static void v(fz var0, boolean var1) {
      if (!var1 || a4.h != nl.q.q) {
         for (qe var3 : R) {
            if (p(var3)) {
               wp.X(var3.effectName(), () -> {
                  long var3x = he.s();

                  try {
                     var3.render(var0, var1);
                  } finally {
                     var0.x(false);
                     var0.blend(_0.v.z);
                     he.T(var3.effectName() + (var1 ? " glow CPU" : " base CPU"), var3x);
                  }
               });
            }
         }
      }
   }

   private static boolean p(qe var0) {
      return !(var0 instanceof Module var1 && !var1.isRuntimeActive());
   }

   static boolean h() {
      return a4.h != nl.q.q && R.stream().anyMatch(var0 -> p(var0) && !wp.M(var0.effectName()) && var0.glowing());
   }

   static boolean J() {
      return a4.h != nl.q.q && R.stream().anyMatch(var0 -> p(var0) && !wp.M(var0.effectName()) && var0.soft());
   }

   static void z() {
      for (qe var1 : R) {
         L(var1.effectName());
      }
   }

   static void L(String var0) {
      for (qe var2 : R) {
         if (var2.effectName().equals(var0)) {
            try {
               var2.clear();
            } catch (RuntimeException var4) {
               _q.g("Render", "Effect cleanup failed: " + var0, var4);
            }
         }
      }
   }

   private q3() {
   }
}
