package com.larpclient;

import com.larpclient.module.SelfVisualsModule;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import org.joml.Vector4f;

public final class my {
   private static my.y r = new my.y(0.65, 0.78, 0.8, 0.18, false);
   private static double g;
   private static int b = -7891281;
   private static int D = -1;

   public static void T(CameraRenderState var0) {
      SelfVisualsModule var1 = SelfVisualsModule.f();
      if (var1 != null) {
         r = new my.y(var1.G.getValue(), var1.P.getValue(), var1.j.getValue(), var1.I.getValue(), var1.T.getValue());
      }

      g = ea.y();
      if (var0.fogData != null && var0.fogData.color != null) {
         Vector4f var2 = var0.fogData.color;
         b = 0xFF000000
            | Math.clamp(Math.round(var2.x * 255.0F), 0, 255) << 16
            | Math.clamp(Math.round(var2.y * 255.0F), 0, 255) << 8
            | Math.clamp(Math.round(var2.z * 255.0F), 0, 255);
      }
   }

   public static boolean f() {
      if (D == jk.v) {
         return false;
      }

      try {
         uv.n();
         return true;
      } catch (RuntimeException var1) {
         D = jk.v;
         r4.r("Liquid Glass", "Standard material unavailable; native skin retained until resource reload", var1);
         return false;
      }
   }

   public static boolean T() {
      return r.armor;
   }

   public static void O(RuntimeException var0) {
      D = jk.v;
      r4.r("Liquid Glass", "Material submission failed; native geometry retained until resource reload", var0);
   }

   public static int J(SelfVisualsModule var0, double var1) {
      return (int)(Math.clamp(var1, 0.0, 1.0) * 255.0) << 24 | (int)(r.gloss * 255.0) << 16 | (int)(r.edge * 255.0) << 8 | (int)(r.iridescence * 255.0);
   }

   public static double w(SelfVisualsModule var0) {
      return g * r.flow % (Math.PI * 2);
   }

   public static int d() {
      return b >>> 8 & 65535;
   }

   public static int H() {
      return b & 0xFF;
   }

   private my() {
   }

   private record y(double flow, double gloss, double edge, double iridescence, boolean armor) {
   }
}
