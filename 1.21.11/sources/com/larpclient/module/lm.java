package com.larpclient.module;

import com.larpclient.ei;
import com.larpclient.ez;
import com.larpclient.j8;
import java.util.Optional;
import net.fabricmc.fabric.api.client.rendering.v1.world.WorldRenderContext;
import net.fabricmc.fabric.api.client.rendering.v1.world.WorldRenderEvents;
import net.minecraft.class_12179;
import net.minecraft.class_12180;
import net.minecraft.class_1309;
import net.minecraft.class_238;
import net.minecraft.class_243;
import net.minecraft.class_310;
import net.minecraft.class_3959;
import net.minecraft.class_3965;
import net.minecraft.class_746;
import net.minecraft.class_239.class_240;
import net.minecraft.class_3959.class_242;
import net.minecraft.class_3959.class_3960;

public final class lm {
   private static final int J = 64;
   private static final float W = 3.5F;
   private static final double L = 0.06;
   private static final double V = 0.07;
   private static Object d;
   private static long C = Long.MIN_VALUE;
   private static double P;
   private static double Z = Double.NaN;

   public static void T() {
      WorldRenderEvents.BEFORE_DEBUG_RENDER.register(lm::S);
   }

   private static void S(WorldRenderContext var0) {
      MaceModule var1 = MaceModule.c();
      class_310 var2 = class_310.method_1551();
      if (var1 != null
         && var1.isRuntimeActive()
         && var2.field_1724 != null
         && var2.field_1687 != null
         && var2.field_1755 == null
         && !var2.field_1690.field_1842) {
         float var3 = var2.method_61966().method_60637(false);
         X(var1, var2, var3);
         v(var1, var2.field_1724, var3);
      }
   }

   private static void X(MaceModule var0, class_310 var1, float var2) {
      class_746 var3 = var1.field_1724;
      long var4 = var1.field_1687.method_75260();
      if (d != var1.field_1687 || C != var4) {
         d = var1.field_1687;
         C = var4;
         P = var0.F(var3);
         Z = P > 0.0 ? U(var1, var3, P) : Double.NaN;
      }

      u(var3, P, Z, var2);
   }

   private static void u(class_746 var0, double var1, double var3, float var5) {
      if (!(var1 <= 0.0) && !Double.isNaN(var3)) {
         class_243 var6 = var0.method_30950(var5);
         double var7 = j8.T(var1, var6.field_1351 + var0.method_5751(), var3);
         if (!(var7 <= 0.0)) {
            class_243 var9 = new class_243(var6.field_1352, var3 + 0.06, var6.field_1350);
            int var10 = ei.W(ez.a, -1, 0.3) | 0xFF000000;

            for (int var11 = 0; var11 < 64; var11++) {
               class_12180.method_75546(I(var9, var11, var7), I(var9, var11 + 1, var7), var10, 3.5F);
            }
         }
      }
   }

   private static double U(class_310 var0, class_746 var1, double var2) {
      class_243 var4 = var1.method_73189().method_1031(0.0, 0.05, 0.0);
      class_243 var5 = var1.method_33571().method_1023(0.0, 1.8 + var2, 0.0);
      class_3965 var6 = var0.field_1687.method_17742(new class_3959(var4, var5, class_3960.field_17558, class_242.field_1348, var1));
      return var6.method_17783() == class_240.field_1332 ? var6.method_17784().field_1351 : Double.NaN;
   }

   private static void v(MaceModule var0, class_746 var1, float var2) {
      class_1309 var3 = var0.O();
      if (var3 != null) {
         n(var1, var3, var2);
      }
   }

   private static void n(class_746 var0, class_1309 var1, float var2) {
      class_238 var3 = var1.method_5829().method_997(var1.method_30950(var2).method_1020(var1.method_73189()));
      class_243 var4 = var0.method_5836(var2);
      class_243 var5 = var0.method_5828(var2);
      class_243 var6 = var4.method_1019(var5.method_1021(var4.method_1022(var3.method_1005()) + var3.method_995()));
      Optional var7 = var3.method_1014(var1.method_5871()).method_992(var4, var6);
      int var8 = (var7.isPresent() ? ei.W(ez.a, -8327246, 0.35) : ei.W(ez.a, -12672, 0.5)) | 0xFF000000;
      class_12180.method_75541(var3, class_12179.method_75536(var8, 2.0F)).method_75533();
      class_243 var9 = var7.orElseGet(() -> A(var4, var5, var3.method_1005()));
      class_12180.method_75541(new class_238(var9, var9).method_1014(0.07), class_12179.method_75539(var8)).method_75533();
   }

   private static class_243 A(class_243 var0, class_243 var1, class_243 var2) {
      return var0.method_1019(var1.method_1021(Math.max(0.0, var2.method_1020(var0).method_1026(var1))));
   }

   private static class_243 I(class_243 var0, int var1, double var2) {
      double var4 = (Math.PI * 2) * var1 / 64.0;
      return var0.method_1031(Math.cos(var4) * var2, 0.0, Math.sin(var4) * var2);
   }

   private lm() {
   }
}
