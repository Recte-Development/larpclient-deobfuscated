package com.larpclient;

import com.larpclient.module.Category;
import com.larpclient.module.Module;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import net.minecraft.class_12075;
import net.minecraft.class_243;
import net.minecraft.class_310;
import net.minecraft.class_3959;
import net.minecraft.class_3965;
import net.minecraft.class_239.class_240;
import net.minecraft.class_3959.class_242;
import net.minecraft.class_3959.class_3960;
import org.joml.Quaternionf;

public final class CryptoPetsModule extends Module {
   public static final int J = 12;
   private static CryptoPetsModule L;
   private final Map<ij.r, com.larpclient.module.l3> i = new LinkedHashMap<>();
   private final com.larpclient.module.lo S = this.addSetting(new com.larpclient.module.lo("Count", "How many coins circle you", 5.0, 1.0, 12.0, 1.0));
   private final com.larpclient.module.lo u = this.addSetting(new com.larpclient.module.lo("Size", "Coin size", 1.3, 0.5, 2.5, 0.05));
   private final com.larpclient.module.lo g = this.addSetting(new com.larpclient.module.lo("Radius", "Distance from you in blocks", 1.2, 0.6, 3.0, 0.05));
   private final com.larpclient.module.lo k = this.addSetting(new com.larpclient.module.lo("Orbit speed", "How fast the ring turns", 1.0, 0.0, 3.0, 0.05));
   private final com.larpclient.module.l3 W = this.addSetting(new com.larpclient.module.l3("Sparkle trail", "Sparks follow each coin", true));
   private final com.larpclient.module.l3 X = this.addSetting(new com.larpclient.module.l3("In first person", "Show the coins in first person", true));
   private Object Y;
   private Object j;
   private final double[] o = new double[12];
   private final double[] x = new double[12];
   private double h;
   private double N = 1.0;
   private double B;
   private double c;
   private double C;
   private boolean M;
   private boolean F;
   private final ArrayDeque<CryptoPetsModule.j> E = new ArrayDeque<>();
   private final Random v = new Random(1129470288L);
   private CryptoPetsModule.x H;

   public CryptoPetsModule() {
      super("Crypto Pets", "Spinning crypto coins that circle you", Category.RENDER, -1);
      L = this;

      for (ij.r var2 : ij.g) {
         this.i.put(var2, this.addSetting(new com.larpclient.module.l3(var2.style(), "Include " + var2.style() + " coins", true)));
      }
   }

   public static CryptoPetsModule M() {
      return L;
   }

   public List<com.larpclient.module.lb<?>> V() {
      return List.copyOf(this.i.values());
   }

   public List<com.larpclient.module.lb<?>> w() {
      return List.of(this.S, this.u, this.g, this.k, this.W, this.X);
   }

   @Override
   public void onEnable() {
      this.M();
      q8.v("Pets", "Crypto Pets activated: {} coins", this.S.getValue().intValue());
   }

   @Override
   public void onDisable() {
      this.M();
   }

   public void M() {
      this.H = null;
      this.Y = this.j = null;
      this.E.clear();
      this.C = 0.0;
      this.B = this.h = 0.0;
      this.N = 1.0;
      this.M = this.F = (boolean)0;
      Arrays.fill(this.o, 0.0);
      Arrays.fill(this.x, 0.0);
      this.c = qo.x();
      this.v.setSeed(1129470288L);
   }

   private List<ij.r> R() {
      ArrayList var1 = new ArrayList();
      this.i.forEach((var1x, var2) -> {
         if (var2.getValue()) {
            var1.add(var1x);
         }
      });
      return var1;
   }

   public void I(float var1, class_12075 var2) {
      if (this.isEnabled()) {
         this.H = null;
         if (this.isRuntimeActive() && this.isInGame()) {
            class_310 var3 = getMc();
            if (this.Y != var3.field_1687 || this.j != var3.field_1724) {
               this.M();
               this.Y = var3.field_1687;
               this.j = var3.field_1724;
            }

            double var4 = Math.clamp(qo.x() - this.c, 0.0, 0.1);
            this.c = qo.x();
            this.B += var4;
            List var6 = this.R();
            int var7 = var6.isEmpty() ? 0 : this.S.getValue().intValue();
            double var8 = this.F ? 1.0 - Math.exp(-6.0 * var4) : 1.0;

            for (int var10 = 0; var10 < 12; var10++) {
               if (var10 < var7) {
                  this.o[var10] = this.o[var10] + ((Math.PI * 2) * var10 / var7 - this.o[var10]) * var8;
               }

               this.x[var10] = this.x[var10] + ((var10 < var7 ? 1 : 0) - this.x[var10]) * var8;
            }

            this.F = (boolean)1;
            this.h = (this.h + var4 * this.k.getValue() * 0.9) % (Math.PI * 2);
            if (w6.l(var3.field_1724) && !this.M) {
               this.N = 0.0;
            }

            this.M = w6.l(var3.field_1724);
            this.N = Math.min(1.0, this.N + var4 / 0.65);
            this.E.removeIf(var1x -> this.B - var1x.birth > var1x.lifetime);
            if (!this.W.getValue()) {
               this.E.clear();
               this.C = 0.0;
            }

            int var29 = var3.field_1690.method_31044().method_31034() && !this.X.getValue() ? 0 : 1;
            if (var29 != 0 && !var6.isEmpty()) {
               ij.y();
               class_243 var11 = var3.field_1724.method_5836(var1);
               double var12 = this.N < 1.0 ? (1.0 - Math.pow(1.0 - this.N, 3.0)) * Math.PI * 2.0 : 0.0;
               ArrayList var14 = new ArrayList();

               for (int var15 = 0; var15 < 12; var15++) {
                  if (!(this.x[var15] < 0.01)) {
                     ij.r var16 = (ij.r)var6.get(var15 % var6.size());
                     double var17 = b(var16, this.u.getValue()) * this.x[var15];
                     double var19 = this.h + this.o[var15];
                     class_243 var21 = var11.method_1031(
                        Math.cos(var19) * this.g.getValue(), -0.35 + Math.sin(this.B * 2.1 + var15 * 1.7) * 0.06, Math.sin(var19) * this.g.getValue()
                     );
                     class_3965 var22 = var3.field_1687
                        .method_17742(new class_3959(var11, var21, class_3960.field_17558, class_242.field_1348, var3.field_1724));
                     if (var22.method_17783() == class_240.field_1332) {
                        var21 = var22.method_17784().method_1020(var21.method_1020(var11).method_1029().method_1021(0.3 * var17));
                     }

                     class_3965 var23 = var3.field_1687
                        .method_17742(new class_3959(var21, var21.method_1031(0.0, -4.5, 0.0), class_3960.field_17558, class_242.field_1347, var3.field_1724));
                     class_243 var24 = var23.method_17783() == class_240.field_1332 ? var23.method_17784().method_1031(0.0, 0.012, 0.0) : null;
                     double var25 = var24 == null ? 0.0 : Math.pow(Math.max(0.0, 1.0 - (var21.field_1351 - var24.field_1351) / 4.5), 2.0) * 0.4 * this.x[var15];
                     Quaternionf var27 = new Quaternionf().rotationY((float)((this.B * 1.6 + var15 * 0.9 + var12) % (Math.PI * 2)));
                     var14.add(new CryptoPetsModule.t(var16, var21, var27, var17, var24, var25));
                     if (this.W.getValue() && var4 > 0.0 && this.v.nextDouble() < var4 * 15.0) {
                        class_243 var28 = var21.method_1031(
                           this.v.nextGaussian() * 0.06 * var17, this.v.nextGaussian() * 0.06 * var17, this.v.nextGaussian() * 0.06 * var17
                        );
                        this.E.addLast(new CryptoPetsModule.j(var28, this.B, 0.8 + this.v.nextDouble() * 0.8, 0.8 + this.v.nextDouble() * 0.8, var16.spark()));

                        while (this.E.size() > 96) {
                           this.E.removeFirst();
                        }
                     }
                  }
               }

               this.H = new CryptoPetsModule.x(List.copyOf(var14), this.B, this.W.getValue() ? 0.5 : 0.0, List.copyOf(this.E));
            }
         }
      }
   }

   public static double b(ij.r var0, double var1) {
      return var1 * var0.scale();
   }

   public boolean r() {
      return !this.isEnabled() ? false : this.H != null && this.H.glowStrength > 0.0 && !this.H.coins.isEmpty();
   }

   public void m(f1 var1, boolean var2) {
      if (this.isEnabled()) {
         CryptoPetsModule.x var3 = this.H;
         if (var3 != null && (!var2 || !(var3.glowStrength <= 0.0))) {
            for (CryptoPetsModule.t var5 : var3.coins) {
               if (!var2) {
                  if (var5.shadow != null) {
                     var1.r(var5.shadow, 0.18 * var5.scale, 0.16 * var5.scale, ei.D(-16777216, var5.shadowAlpha));
                  }

                  ij.U(var1, var5.kind, var5.center, var5.rotation, (float)var5.scale);
               }
            }

            for (CryptoPetsModule.j var9 : var3.sparks) {
               double var6 = Math.clamp(1.0 - (var3.time - var9.birth) / var9.lifetime, 0.0, 1.0);
               var1.V(
                  var9.point.field_1352,
                  var9.point.field_1351 + 0.08 * (1.0 - var6),
                  var9.point.field_1350,
                  0.035 * this.u.getValue() * var6 * var9.scale,
                  var3.time,
                  ei.D(var9.color, var6 * var6 * var3.glowStrength)
               );
            }
         }
      }
   }

   private record j(class_243 point, double birth, double lifetime, double scale, int color) {
   }

   public record t(ij.r kind, class_243 center, Quaternionf rotation, double scale, class_243 shadow, double shadowAlpha) {
   }

   public record x(List<CryptoPetsModule.t> coins, double time, double glowStrength, List<CryptoPetsModule.j> sparks) {
   }
}
