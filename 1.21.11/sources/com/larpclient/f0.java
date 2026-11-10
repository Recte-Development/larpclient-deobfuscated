package com.larpclient;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.class_10017;
import net.minecraft.class_10055;
import net.minecraft.class_243;
import net.minecraft.class_310;
import net.minecraft.class_4587;

public final class f0 {
   private final ArrayDeque<f0.v> w = new ArrayDeque<>();
   private double o = -100.0;
   private class_243 X;
   private boolean A;
   private List<f0.e> E = List.of();
   private boolean P;
   public double a = 1.2;
   public double y = 2.4;
   public double H = 0.45;
   public int d = 6;
   public int i = -8798209;
   public boolean D = (boolean)1;
   public String U = "Original";
   public String e = "Smooth";

   public boolean h() {
      return this.P;
   }

   public void c(class_243 var1) {
      if (this.A) {
         this.A = (boolean)0;
         this.A();
      }

      ArrayList var2 = new ArrayList();
      this.P = (boolean)0;

      for (f0.v var4 : this.w) {
         class_10017 var5 = var4.state;
         double var6 = var5.field_53325 - var1.field_1352;
         double var8 = var5.field_53326 - var1.field_1351;
         double var10 = var5.field_53327 - var1.field_1350;
         if (!(var6 * var6 + var8 * var8 + var10 * var10 > 2304.0)) {
            double var12 = Math.clamp((qo.x() - var4.born) / var4.lifetime, 0.0, 1.0);
            double var14 = this.H * (this.e.equals("Linear") ? 1.0 - var12 : u9.L(1.0 - var12));
            if (!(var14 <= 0.0)) {
               gv.y var16 = switch (this.U) {
                  case "Original" -> gv.y.K;
                  case "Silhouette" -> gv.y.U;
                  case "Hologram" -> gv.y.I;
                  default -> gv.y.V;
               };
               var2.add(new f0.e(var5, var16, this.U.equals("Theme Tint") ? ez.a : this.i, var14, this.D));
               this.P = this.P | this.D;
            }
         }
      }

      this.E = List.copyOf(var2);
   }

   public void D() {
      while (this.w.size() > Math.clamp(this.d, 1, 12)) {
         this.w.removeFirst();
      }

      this.w.removeIf(var0 -> qo.K() - var0.born >= var0.lifetime);
      class_310 var1 = class_310.method_1551();
      if (var1.field_1724 == null || !var1.field_1724.method_5805()) {
         this.e();
      }
   }

   public void Z() {
      this.A = (boolean)1;
   }

   private void A() {
      class_310 var1 = class_310.method_1551();
      if (var1.field_1724 != null && var1.field_1724.method_5805()) {
         double var2 = qo.K();
         class_243 var4 = var1.field_1724.method_73189();
         if (!(var2 - this.o < this.a) && (this.X == null || !(this.X.method_1025(var4) < 0.04))) {
            if (this.X == null) {
               this.X = var4;
               this.o = var2;
            } else if (this.X.method_1025(var4) > 64.0) {
               this.e();
               this.X = var4;
               this.o = var2;
            } else {
               class_10017 var5 = gv.i(var1.field_1724, 1.0F);
               var5.field_53337 = null;
               if (var5 instanceof class_10055 var6) {
                  var6.field_53525 = null;
               }

               var5.field_61823.clear();
               var5.field_61821 = 0;

               while (this.w.size() >= Math.clamp(this.d, 1, 12)) {
                  this.w.removeFirst();
               }

               this.w.addLast(new f0.v(var5, var2, this.y));
               this.X = var4;
               this.o = var2;
            }
         }
      }
   }

   public boolean R() {
      return this.w.isEmpty();
   }

   public void e() {
      this.w.clear();
      this.E = List.of();
      this.P = this.A = (boolean)0;
      this.X = null;
      this.o = -100.0;
   }

   public void I(class_4587 var1, cw var2, boolean var3) {
      long var4 = c5.X();

      for (f0.e var7 : this.E) {
         if (!var3 || var7.bloom) {
            gv.x(var7.state, var1, var2, var7.material, var7.color, var7.alpha * (var3 ? 0.5 : 1.0), false);
         }
      }

      c5.w("GhostEcho", var4);
   }

   private record e(class_10017 state, gv.y material, int color, double alpha, boolean bloom) {
   }

   private record v(class_10017 state, double born, double lifetime) {
   }
}
