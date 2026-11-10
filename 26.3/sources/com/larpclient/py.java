package com.larpclient;

import com.larpclient.module.SelfVisualsModule;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import org.joml.Vector4f;

public final class py {
   private static py.e X = new py.e(0.65, 0.78, 0.8, 0.18, false);
   private static double E;
   private static int I = -7891281;
   private static int c = -1;

   public static void M(CameraRenderState var0) {
      SelfVisualsModule var1 = SelfVisualsModule.v();
      if (var1 != null) {
         X = new py.e(var1.F.getValue(), var1.M.getValue(), var1.m.getValue(), var1.Q.getValue(), var1.v.getValue());
      }

      E = hz.Q();
      if (var0.fogData != null && var0.fogData.color != null) {
         Vector4f var2 = var0.fogData.color;
         I = 0xFF000000
            | Math.clamp(Math.round(var2.x * 255.0F), 0, 255) << 16
            | Math.clamp(Math.round(var2.y * 255.0F), 0, 255) << 8
            | Math.clamp(Math.round(var2.z * 255.0F), 0, 255);
      }
   }

   public static boolean Q() {
      if (c == nl.q) {
         return false;
      }

      try {
         w7.v();
         return true;
      } catch (RuntimeException var1) {
         c = nl.q;
         _q.o("Liquid Glass", "Standard material unavailable; native skin retained until resource reload", var1);
         return false;
      }
   }

   public static boolean C() {
      return X.armor;
   }

   public static void m(RuntimeException var0) {
      c = nl.q;
      _q.o("Liquid Glass", "Material submission failed; native geometry retained until resource reload", var0);
   }

   public static int R(SelfVisualsModule var0, double var1) {
      return (int)(Math.clamp(var1, 0.0, 1.0) * 255.0) << 24 | (int)(X.gloss * 255.0) << 16 | (int)(X.edge * 255.0) << 8 | (int)(X.iridescence * 255.0);
   }

   public static double O(SelfVisualsModule var0) {
      return E * X.flow % (Math.PI * 2);
   }

   public static int z() {
      return I >>> 8 & 65535;
   }

   public static int i() {
      return I & 0xFF;
   }

   private py() {
   }

   private record e(double flow, double gloss, double edge, double iridescence, boolean armor) {
   }
}
