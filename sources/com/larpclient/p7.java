package com.larpclient;

import com.larpclient.module.SelfVisualsModule;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import org.joml.Vector4f;

public final class p7 {
   private static p7.i a = new p7.i(0.65, 0.78, 0.8, 0.18, false);
   private static double Y;
   private static int K = -7891281;
   private static int A = -1;

   public static void t(CameraRenderState var0) {
      SelfVisualsModule var1 = SelfVisualsModule.V();
      if (var1 != null) {
         a = new p7.i(var1.m.P(), var1.i.P(), var1.F.P(), var1.O.P(), var1.D.P());
      }

      Y = ty.K();
      if (var0.fogData != null && var0.fogData.color != null) {
         Vector4f var2 = var0.fogData.color;
         K = 0xFF000000
            | Math.clamp(Math.round(var2.x * 255.0F), 0, 255) << 16
            | Math.clamp(Math.round(var2.y * 255.0F), 0, 255) << 8
            | Math.clamp(Math.round(var2.z * 255.0F), 0, 255);
      }
   }

   public static boolean B() {
      if (A == m5.k) {
         return false;
      }

      try {
         tj.O();
         return true;
      } catch (RuntimeException var1) {
         A = m5.k;
         mt.P("Liquid Glass", "Standard material unavailable; native skin retained until resource reload", var1);
         return false;
      }
   }

   public static boolean o() {
      return a.armor;
   }

   public static void i(RuntimeException var0) {
      A = m5.k;
      mt.P("Liquid Glass", "Material submission failed; native geometry retained until resource reload", var0);
   }

   public static int e(SelfVisualsModule var0, double var1) {
      return (int)(Math.clamp(var1, 0.0, 1.0) * 255.0) << 24 | (int)(a.gloss * 255.0) << 16 | (int)(a.edge * 255.0) << 8 | (int)(a.iridescence * 255.0);
   }

   public static double D(SelfVisualsModule var0) {
      return Y * a.flow % (Math.PI * 2);
   }

   public static int L() {
      return K >>> 8 & 65535;
   }

   public static int I() {
      return K & 0xFF;
   }

   private p7() {
   }

   private record i(double flow, double gloss, double edge, double iridescence, boolean armor) {
   }
}
