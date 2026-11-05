package com.larpclient;

import com.larpclient.module.Module;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.state.level.CameraRenderState;

final class f {
   private static List<u2> n = List.of();

   static void E(List<Module> var0) {
      n = var0.stream().filter(var0x -> var0x instanceof u2).map(var0x -> (u2)var0x).toList();
   }

   static void T(Minecraft var0) {
      for (u2 var2 : n) {
         if (m(var2)) {
            f8.M(var2.effectName(), () -> var2.tick(var0));
         }
      }
   }

   static void j(float var0, CameraRenderState var1) {
      for (u2 var3 : n) {
         if (m(var3)) {
            f8.M(var3.effectName(), () -> {
               long var3x = h_.U();

               try {
                  var3.prepare(var0, var1);
               } finally {
                  h_.n(var3.effectName() + " preparation CPU", var3x);
               }
            });
         }
      }
   }

   static void E(r6 var0, boolean var1) {
      if (!var1 || lo.q != jk.x.i) {
         for (u2 var3 : n) {
            if (m(var3)) {
               f8.M(var3.effectName(), () -> {
                  long var3x = h_.U();

                  try {
                     var3.render(var0, var1);
                  } finally {
                     var0.T(false);
                     var0.blend(fk.h.z);
                     h_.n(var3.effectName() + (var1 ? " glow CPU" : " base CPU"), var3x);
                  }
               });
            }
         }
      }
   }

   private static boolean m(u2 var0) {
      return !(var0 instanceof Module var1 && !var1.isRuntimeActive());
   }

   static boolean l() {
      return lo.q != jk.x.i && n.stream().anyMatch(var0 -> m(var0) && !f8.H(var0.effectName()) && var0.glowing());
   }

   static boolean o() {
      return lo.q != jk.x.i && n.stream().anyMatch(var0 -> m(var0) && !f8.H(var0.effectName()) && var0.soft());
   }

   static void l() {
      for (u2 var1 : n) {
         n(var1.effectName());
      }
   }

   static void n(String var0) {
      for (u2 var2 : n) {
         if (var2.effectName().equals(var0)) {
            try {
               var2.clear();
            } catch (RuntimeException var4) {
               r4.T("Render", "Effect cleanup failed: " + var0, var4);
            }
         }
      }
   }

   private f() {
   }
}
