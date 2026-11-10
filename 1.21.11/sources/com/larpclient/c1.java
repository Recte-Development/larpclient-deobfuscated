package com.larpclient;

import com.larpclient.module.SelfVisualsModule;
import net.minecraft.class_12075;
import org.joml.Vector4fc;

public final class c1 {
   private static c1.e e = new c1.e(0.65, 0.78, 0.8, 0.18, false);
   private static double i;
   private static int b = -7891281;
   private static int p = -1;

   public static void u(class_12075 var0) {
      SelfVisualsModule var1 = SelfVisualsModule.o();
      if (var1 != null) {
         e = new c1.e(var1.h.getValue(), var1.B.getValue(), var1.K.getValue(), var1.v.getValue(), var1.I.getValue());
      }

      i = qo.x();
      if (b9.G()) {
         Vector4fc var2 = b9.l();
         b = 0xFF000000
            | Math.clamp(Math.round(var2.x() * 255.0F), 0, 255) << 16
            | Math.clamp(Math.round(var2.y() * 255.0F), 0, 255) << 8
            | Math.clamp(Math.round(var2.z() * 255.0F), 0, 255);
      }
   }

   public static boolean s() {
      if (p == t_.g) {
         return false;
      }

      try {
         gv.F();
         return true;
      } catch (RuntimeException var1) {
         p = t_.g;
         q8.u("Liquid Glass", "Standard material unavailable; native skin retained until resource reload", var1);
         return false;
      }
   }

   public static boolean d() {
      return e.armor;
   }

   public static void f(RuntimeException var0) {
      p = t_.g;
      q8.u("Liquid Glass", "Material submission failed; native geometry retained until resource reload", var0);
   }

   public static int L(SelfVisualsModule var0, double var1) {
      return (int)(Math.clamp(var1, 0.0, 1.0) * 255.0) << 24 | (int)(e.gloss * 255.0) << 16 | (int)(e.edge * 255.0) << 8 | (int)(e.iridescence * 255.0);
   }

   public static double O(SelfVisualsModule var0) {
      return i * e.flow % (Math.PI * 2);
   }

   public static int a() {
      return b >>> 8 & 65535;
   }

   public static int N() {
      return b & 0xFF;
   }

   private c1() {
   }

   private record e(double flow, double gloss, double edge, double iridescence, boolean armor) {
   }
}
