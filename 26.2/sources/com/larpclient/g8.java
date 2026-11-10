package com.larpclient;

import com.larpclient.module.SelfVisualsModule;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import org.joml.Vector4f;

public final class g8 {
   private static g8.f w = new g8.f(0.65, 0.78, 0.8, 0.18, false);
   private static double U;
   private static int c = -7891281;
   private static int n = -1;

   public static void Z(CameraRenderState var0) {
      SelfVisualsModule var1 = SelfVisualsModule.k();
      if (var1 != null) {
         w = new g8.f(var1.B.getValue(), var1.L.getValue(), var1.f.getValue(), var1.F.getValue(), var1.D.getValue());
      }

      U = cf.F();
      if (var0.fogData != null && var0.fogData.color != null) {
         Vector4f var2 = var0.fogData.color;
         c = 0xFF000000
            | Math.clamp(Math.round(var2.x * 255.0F), 0, 255) << 16
            | Math.clamp(Math.round(var2.y * 255.0F), 0, 255) << 8
            | Math.clamp(Math.round(var2.z * 255.0F), 0, 255);
      }
   }

   public static boolean s() {
      if (n == wa.s) {
         return false;
      }

      try {
         j4.y();
         return true;
      } catch (RuntimeException var1) {
         n = wa.s;
         h.m("Liquid Glass", "Standard material unavailable; native skin retained until resource reload", var1);
         return false;
      }
   }

   public static boolean o() {
      return w.armor;
   }

   public static void Y(RuntimeException var0) {
      n = wa.s;
      h.m("Liquid Glass", "Material submission failed; native geometry retained until resource reload", var0);
   }

   public static int e(SelfVisualsModule var0, double var1) {
      return (int)(Math.clamp(var1, 0.0, 1.0) * 255.0) << 24 | (int)(w.gloss * 255.0) << 16 | (int)(w.edge * 255.0) << 8 | (int)(w.iridescence * 255.0);
   }

   public static double D(SelfVisualsModule var0) {
      return U * w.flow % (Math.PI * 2);
   }

   public static int c() {
      return c >>> 8 & 65535;
   }

   public static int u() {
      return c & 0xFF;
   }

   private g8() {
   }

   private record f(double flow, double gloss, double edge, double iridescence, boolean armor) {
   }
}
