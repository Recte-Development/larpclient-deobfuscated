package com.larpclient;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import java.util.function.Predicate;
import net.minecraft.class_12178;
import net.minecraft.class_12179;
import net.minecraft.class_12180;
import net.minecraft.class_1297;
import net.minecraft.class_1309;
import net.minecraft.class_238;
import net.minecraft.class_243;
import net.minecraft.class_3532;
import net.minecraft.class_12187.class_12188;
import org.joml.Matrix4f;

public final class l2 {
   private final com.larpclient.module.li o = new com.larpclient.module.li(
      "Color", "Theme follows your live accent colors", "Theme", "Theme", "Category", "Custom", "Distance", "Health", "Rainbow"
   );
   private final com.larpclient.module.l0 k = new com.larpclient.module.l0("Custom Color", "Color used by Custom mode", -4291585);
   private final com.larpclient.module.lo O = new com.larpclient.module.lo("Line Width", "Outline width in pixels", 1.5, 0.5, 4.0, 0.25);
   private final com.larpclient.module.lo T = new com.larpclient.module.lo("Fill Opacity", "Soft fill for Filled mode", 0.12, 0.0, 0.5, 0.02);
   private final com.larpclient.module.l3 W = new com.larpclient.module.l3(
      "Through Walls", "Show boxes and lines behind solid blocks; silhouettes always show through", true
   );

   public l2(Consumer<com.larpclient.module.lb<?>> var1) {
      this(var1, true);
   }

   public l2(Consumer<com.larpclient.module.lb<?>> var1, boolean var2) {
      this.k.O(() -> this.o.G("Custom"));

      for (com.larpclient.module.lb var4 : List.of(this.o, this.k, this.O, this.W)) {
         var1.accept(var4);
      }

      if (var2) {
         var1.accept(this.T);
      }
   }

   public void N(BooleanSupplier var1, BooleanSupplier var2, BooleanSupplier var3, BooleanSupplier var4) {
      this.o.O(var1);
      this.k.O(() -> var1.getAsBoolean() && this.o.G("Custom"));
      this.O.O(var2);
      this.W.O(var3);
      this.T.O(var4);
   }

   public List<com.larpclient.module.lb<?>> v() {
      return List.of(this.o, this.k);
   }

   public List<com.larpclient.module.lb<?>> P() {
      return List.of(this.O, this.W);
   }

   public List<com.larpclient.module.lb<?>> t() {
      return List.of(this.T);
   }

   public boolean L() {
      return this.W.getValue();
   }

   public double t() {
      return this.T.getValue();
   }

   public float F() {
      return this.O.getValue().floatValue();
   }

   public int C(int var1, double var2, double var4, double var6, int var8) {
      int var9 = switch ((String)this.o.getValue()) {
         case "Category" -> var1;
         case "Custom" -> this.k.getValue();
         case "Distance" -> ez.V(Math.clamp(var2 / var4, 0.0, 1.0));
         case "Health" -> ei.W(-1017711, -7347266, var6);
         case "Rainbow" -> ei.X((float)(System.currentTimeMillis() % 6000L) / 6000.0F + var8 * 0.037F, 0.45F, 1.0F);
         default -> ez.V(0.25);
      };
      return ei.R(var9, A(var2, var4));
   }

   public static double A(double var0, double var2) {
      if (!(var2 <= 0.0) && !(var0 >= var2)) {
         double var4 = Math.clamp((var0 / var2 - 0.65) / 0.35, 0.0, 1.0);
         return 1.0 - var4 * var4 * (3.0 - 2.0 * var4);
      } else {
         return 0.0;
      }
   }

   public static List<class_1297> Q(Iterable<class_1297> var0, class_1297 var1, double var2, int var4, Predicate<class_1297> var5) {
      double var6 = var2 * var2;
      ArrayList var8 = new ArrayList();

      for (class_1297 var10 : var0) {
         if (var10 != var1 && var10.method_5805() && var1.method_5858(var10) <= var6 && var5.test(var10)) {
            var8.add(var10);
         }
      }

      if (var8.size() > 1) {
         var8.sort(Comparator.comparingDouble(var1::method_5858));
      }

      return var8.size() > var4 ? List.copyOf(var8.subList(0, Math.max(0, var4))) : var8;
   }

   public static Matrix4f F(Matrix4f var0) {
      return b9.n(var0);
   }

   public static class_243 i(class_1297 var0, float var1) {
      return new class_243(
         class_3532.method_16436(var1, var0.field_6038, var0.method_23317()),
         class_3532.method_16436(var1, var0.field_5971, var0.method_23318()),
         class_3532.method_16436(var1, var0.field_5989, var0.method_23321())
      );
   }

   public static class_238 F(class_1297 var0, float var1) {
      return var0.method_5829().method_997(i(var0, var1).method_1020(var0.method_73189())).method_1014(0.025);
   }

   private void H(class_12178 var1) {
      if (this.W.getValue()) {
         var1.method_75533();
      }
   }

   public void O(class_243 var1, class_243 var2, int var3) {
      this.H(class_12180.method_75546(var1, var2, var3, this.F()));
   }

   public void X(class_238 var1, String var2, int var3) {
      if (var3 >>> 24 != 0) {
         if (var2.equals("Corners")) {
            double var4 = var1.method_17939() * 0.25;
            double var6 = var1.method_17940() * 0.18;
            double var8 = var1.method_17941() * 0.25;

            for (int var10 = 0; var10 < 2; var10++) {
               for (int var11 = 0; var11 < 2; var11++) {
                  for (int var12 = 0; var12 < 2; var12++) {
                     class_243 var13 = new class_243(
                        var10 == 0 ? var1.field_1323 : var1.field_1320,
                        var11 == 0 ? var1.field_1322 : var1.field_1325,
                        var12 == 0 ? var1.field_1321 : var1.field_1324
                     );
                     this.O(var13, var13.method_1031(var10 == 0 ? var4 : -var4, 0.0, 0.0), var3);
                     this.O(var13, var13.method_1031(0.0, var11 == 0 ? var6 : -var6, 0.0), var3);
                     this.O(var13, var13.method_1031(0.0, 0.0, var12 == 0 ? var8 : -var8), var3);
                  }
               }
            }
         } else {
            int var14 = var2.equals("Filled") ? ei.R(var3, this.T.getValue()) : 0;
            this.H(class_12180.method_75541(var1, class_12179.method_75537(var3, this.F(), var14)));
         }
      }
   }

   public void f(class_238 var1, double var2, int var4) {
      class_243 var5 = new class_243(var1.field_1323 - 0.08, var1.field_1322, var1.field_1321);
      this.H(class_12180.method_75546(var5, var5.method_1031(0.0, var1.method_17940(), 0.0), ei.R(ez.D, (var4 >>> 24) / 255.0), this.F() + 1.0F));
      int var6 = ei.W(-1017711, -7347266, var2);
      this.O(var5, var5.method_1031(0.0, var1.method_17940() * Math.clamp(var2, 0.0, 1.0), 0.0), ei.D(var6, (var4 >>> 24) / 255.0));
   }

   public void n(class_243 var1, String var2, int var3) {
      this.H(class_12180.method_75551(var2, var1, class_12188.method_75566(var3).method_75565(0.7F)));
   }

   public static double l(class_1309 var0) {
      return j2.z(var0);
   }
}
