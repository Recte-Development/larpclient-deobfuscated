package com.larpclient.module;

import com.larpclient.cw;
import com.larpclient.ei;
import com.larpclient.ez;
import com.larpclient.jq;
import com.larpclient.q8;
import com.larpclient.qo;
import com.larpclient.u9;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import net.minecraft.class_12075;
import net.minecraft.class_1297;
import net.minecraft.class_1684;
import net.minecraft.class_243;
import net.minecraft.class_4587;

public final class PearlParticlesModule extends Module {
   private static PearlParticlesModule Q;
   private final l0 R = this.addSetting(new l0("Color", "Particle tint", -8866817));
   private final lo I = this.O("Spawn Rate", "Particles per pearl each tick", 10.0, 1.0, 10.0, 1.0);
   private final lo a = this.O("Size", "Billboard width in blocks", 0.3, 0.05, 0.5, 0.01);
   private final lo t = this.O("Lifetime", "Seconds", 0.5, 0.5, 3.0, 0.1);
   private final lo w = this.O("Spread", "Initial velocity spread", 0.2, 0.01, 0.2, 0.01);
   private final lo A = this.O("Gravity", "Downward acceleration per tick", 0.008, 0.0, 0.03, 0.001);
   private final lo u = this.O("Fade Speed", "Opacity fade exponent", 2.0, 0.3, 2.0, 0.1);
   private final l3 o = this.addSetting(new l3("Color Animation", "Animate the theme gradient", false));
   private final HashMap<Integer, class_1684> e = new HashMap<>();
   private final ArrayList<PearlParticlesModule.a> C = new ArrayList<>();
   private final Random z = new Random();
   private Object i;
   private Object b;
   private class_243 x;
   private int Y;

   public PearlParticlesModule() {
      super("Pearl Particles", "Add glowing trails to ender pearls", Category.VISUALS, -1);
      Q = this;
      this.R.O(() -> !this.o.getValue());
   }

   public List<lb<?>> B() {
      return List.of(this.I, this.a, this.t);
   }

   public List<lb<?>> v() {
      return List.of(this.o, this.R);
   }

   public List<lb<?>> T() {
      return List.of(this.w, this.A, this.u);
   }

   private lo O(String var1, String var2, double var3, double var5, double var7, double var9) {
      return this.addSetting(new lo(var1, var2, var3, var5, var7, var9));
   }

   public static PearlParticlesModule U() {
      return Q;
   }

   public void R() {
      this.e.clear();
      this.C.clear();
      this.i = this.b = null;
      this.x = null;
      this.Y = 0;
   }

   @Override
   public void onEnable() {
      this.R();
      q8.G("PearlParticles", "Enabled; max 32 observed pearls, 4096 particles; source bloom sprite");
   }

   @Override
   public void onDisable() {
      this.R();
   }

   @Override
   public void onTick() {
      if (this.isRuntimeActive() && this.isInGame()) {
         class_243 var1;
         label62: {
            var1 = this.getPlayer().method_73189();
            if (this.i == getMc().field_1687 && this.b == this.getPlayer()) {
               if (this.x == null) {
                  break label62;
               }

               double var14;
               int var10000 = (var14 = var1.method_1025(this.x) - 256.0) == 0.0 ? 0 : (var14 < 0.0 ? -1 : 1);
               boolean var13 = false;
               int var12 = var10000;
               if ((-Integer.signum(var12) >>> 31 ^ 1) != 0) {
                  break label62;
               }
            }

            this.R();
         }

         this.i = getMc().field_1687;
         this.b = this.getPlayer();
         this.x = var1;
         double var2 = qo.K();
         this.e.entrySet().removeIf(var0 -> getMc().field_1687.method_8469(var0.getKey()) != var0.getValue() || var0.getValue().method_31481());
         HashSet var4 = new HashSet();
         int var5 = 0;

         for (class_1297 var7 : getMc().field_1687.method_18112()) {
            if (var7 instanceof class_1684 var8 && !var8.method_31481() && !(var8.method_5858(this.getPlayer()) > 16384.0)) {
               if (++var5 > 32) {
                  break;
               }

               var4.add(var8.method_5628());
               if (this.e.get(var8.method_5628()) == var8) {
                  double var9 = this.w.getValue();

                  for (int var11 = 0; var11 < this.I.getValue().intValue() && this.C.size() < 4096; var11++) {
                     this.C
                        .add(
                           new PearlParticlesModule.a(
                              var8.method_73189(),
                              new class_243(
                                 (this.z.nextDouble() * 2.0 - 1.0) * var9,
                                 (this.z.nextDouble() * 2.0 - 1.0) * var9 * 0.25,
                                 (this.z.nextDouble() * 2.0 - 1.0) * var9
                              ),
                              this.A.getValue(),
                              var2,
                              this.Y++
                           )
                        );
                  }
               }

               if (this.e.size() < 32 || this.e.containsKey(var8.method_5628())) {
                  this.e.put(var8.method_5628(), var8);
               }
            }
         }

         this.e.keySet().retainAll(var4);
         this.C.removeIf(var3 -> var3.T(var2, this.t.getValue()));
      } else {
         this.R();
      }
   }

   public boolean l(double var1) {
      if (this.isEnabled()) {
         if (this.isRuntimeActive() && this.isInGame() && this.i == getMc().field_1687) {
            for (PearlParticlesModule.a var4 : this.C) {
               double var5 = this.h(var4, var1);
               if (var5 >= 0.001 && ei.R(this.A(var4, var1), var5) >>> 24 != 0) {
                  return true;
               }
            }

            return false;
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   private double h(PearlParticlesModule.a var1, double var2) {
      double var4 = Math.clamp((var2 - var1.T) / this.t.getValue(), 0.0, 1.0);
      return Math.pow(1.0 - var4, this.u.getValue());
   }

   private int A(PearlParticlesModule.a var1, double var2) {
      return this.o.getValue() ? ei.W(ez.a, ez.f, 0.5 + 0.5 * Math.sin(var2 * 2.0 + var1.H * 0.04)) : this.R.getValue();
   }

   public void L(class_4587 var1, cw var2, class_12075 var3, double var4, boolean var6) {
      if (this.isEnabled()) {
         if (this.isRuntimeActive() && this.i == getMc().field_1687) {
            jq var7 = new jq(var1, var2, var3);

            for (PearlParticlesModule.a var9 : this.C) {
               double var10 = this.h(var9, var4);
               if (!(var10 < 0.001)) {
                  class_243 var12 = var9.p.method_35590(var9.o, qo.C());
                  int var13 = this.A(var9, var4);
                  double var14 = Math.clamp((var4 - var9.T) / this.t.getValue(), 0.0, 1.0);
                  var13 = ei.W(var13, var13 & 0xFF000000 | 16777215, 0.22 * Math.pow(1.0 - var14, 4.0));
                  var7.i(var12, m(this.a.getValue(), var14, var9.H), ei.R(var13, var10));
               }
            }
         }
      }
   }

   public static double m(double var0, double var2, int var4) {
      int var5 = var4 * -1640531527;
      var5 ^= var5 >>> 16;
      double var6 = 0.74 + 0.26 * (var5 >>> 8 & 65535) / 65535.0;
      return var0 * var6 * (1.0 - 0.55 * u9.L(var2));
   }

   private static final class a {
      class_243 o;
      class_243 p;
      class_243 A;
      final double O;
      final double T;
      final int H;

      a(class_243 var1, class_243 var2, double var3, double var5, int var7) {
         this.o = this.p = var1;
         this.A = var2;
         this.O = var3;
         this.T = var5;
         this.H = var7;
      }

      boolean T(double var1, double var3) {
         this.p = this.o;
         this.o = this.o.method_1019(this.A);
         this.A = this.A.method_1031(0.0, -this.O, 0.0).method_1021(0.96);
         return var1 - this.T >= var3;
      }
   }
}
