package com.larpclient.module;

import com.larpclient.bd;
import com.larpclient.bn;
import com.larpclient.cr;
import com.larpclient.fs;
import com.larpclient.q8;
import com.larpclient.w6;
import net.minecraft.class_1268;
import net.minecraft.class_1294;
import net.minecraft.class_1309;
import net.minecraft.class_1657;
import net.minecraft.class_1802;
import net.minecraft.class_238;
import net.minecraft.class_310;
import net.minecraft.class_3532;
import net.minecraft.class_5134;
import net.minecraft.class_746;

public final class l {
   private final Module E;
   private final fs d;
   private int L;
   private boolean P;
   private long U;
   private long z;
   private long I;
   private long V;
   private boolean h = (boolean)1;

   public l(Module var1) {
      this.E = var1;
      this.d = new fs(var1);
   }

   public void N(class_310 var1) {
      this.d.u(var1, true);
   }

   public void K(class_310 var1) {
      if (this.d.I()) {
         this.d.o(var1, true);
      }
   }

   public void H(class_746 var1) {
      this.z = 0L;
      AuraModule.c = (boolean)0;
   }

   public boolean q(class_746 var1) {
      if (!AuraModule.c) {
         return false;
      }

      if (var1 != null && !var1.method_24828()) {
         if (System.currentTimeMillis() <= this.z) {
            return true;
         }

         this.H(var1);
         return false;
      } else {
         this.H(var1);
         return false;
      }
   }

   public boolean w() {
      return this.q(class_310.method_1551().field_1724);
   }

   public boolean d(class_746 var1, l.b var2) {
      class_1309 var3 = var2.target();
      if (var1 != null && var3 != null && var3.method_5805()) {
         this.T(var1, var2.crit(), var2.sprint());
         if (var2.pauseWhileEating() && f.X()) {
            this.H(var1);
            return false;
         }

         class_238 var4 = var2.box() != null ? var2.box().method_1014(var3.method_5871()) : var3.method_5829().method_1014(var3.method_5871());
         if (f.d(var1, var4) > var2.range()) {
            this.H(var1);
            return false;
         }

         boolean var5 = this.L(var1, var2.crit(), var2.predictCrits(), var2.blatant());
         if (var2.sprint() != l.e.r && var5 && !this.q(var1)) {
            int var6 = !var1.method_5624() && !z() ? 0 : 1;
            this.V(var1);
            if (var6 != 0 && var2.sprint() == l.e.s) {
               return false;
            }
         }

         if (this.o(var1, var2.legacy(), var2.tpsSync(), var2.randomize(), var2.blatant()) && var2.entityHit()) {
            int var8 = var2.ignoreWalls() && !var2.raycastWalls() ? 1 : 0;
            if (!f.n(var1, var4, var2.yaw(), var2.pitch(), var2.range(), (boolean)var8)) {
               if (!var5 && !this.q(var1)) {
                  this.H(var1);
               }

               return false;
            } else {
               if (var2.autoJump() && var2.crit() != l.z.x && n(var1)) {
                  var1.method_6043();
                  return false;
               }

               if (!Z(var1, var2.crit())) {
                  if (!var5 && !this.q(var1)) {
                     this.H(var1);
                  }

                  return false;
               } else if (!bd.t(var1.method_5624())) {
                  if (!this.P) {
                     q8.v("Aura", "Attack deferred: waiting for vanilla sprint report; localSprint=" + var1.method_5624());
                  }

                  this.P = (boolean)1;
                  return false;
               } else {
                  this.P = (boolean)0;
                  boolean var7 = cr.J(this.E, var3, () -> {
                     if (var2.beforeAttack() != null) {
                        var2.beforeAttack().run();
                     }

                     this.q(var1, var3, var2.shieldBreaker());
                  });
                  if (!var7) {
                     return false;
                  }

                  this.I = 0L;
                  if (var2.legacy()) {
                     this.w(var2.minCps(), var2.maxCps(), var2.tpsSync(), var2.blatant());
                  }

                  AuraModule.c = (boolean)0;
                  this.z = 0L;
                  return true;
               }
            }
         } else {
            if (!var5 && !this.q(var1)) {
               this.H(var1);
            }

            return false;
         }
      } else {
         this.H(var1);
         return false;
      }
   }

   private void q(class_746 var1, class_1309 var2, boolean var3) {
      class_310 var4 = class_310.method_1551();
      if (var3 && !this.d.I() && var2 instanceof class_1657 var5 && bn.B(var5, var1) && !bn.G(var5)) {
         int var6 = bn.d(var1);
         if (var6 >= 0 && this.d.m(var1, var6)) {
            bn.f(var5);
            q8.v("Aura", "Shield Breaker: hit " + var5.method_5628() + " with hotbar slot " + (var6 + 1));
         }
      }

      AuraModule.e(var1, "attack-before");
      var4.field_1761.method_2918(var1, var2);
      AuraModule.e(var1, "attack-after");
      w6.M(var1, class_1268.field_5808);
      this.h = (boolean)0;
      this.L++;
   }

   public void p() {
      this.h = (boolean)1;
      this.I = 0L;
   }

   public boolean o(class_746 var1, boolean var2, boolean var3, boolean var4, boolean var5) {
      if (var1 == null) {
         return false;
      }

      if (var2) {
         return System.nanoTime() >= this.U;
      }

      boolean var6 = f.i();
      int var7 = !f.c(var1) && (!var6 || var1.method_24828()) ? 0 : 1;
      float var8 = !var5 && var7 == 0 ? l(var3, var4, var5) : 0.9F;
      if (var1.method_7261(0.5F) < var8) {
         this.I = 0L;
         return false;
      }

      if (!var5 && !this.h && var7 == 0) {
         if (this.I == 0L) {
            this.I = System.nanoTime();
            this.V = (long)((5.0 + Math.random() * 15.0) * 1000000.0);
         }

         return System.nanoTime() - this.I >= this.V;
      } else {
         this.I = 0L;
         return true;
      }
   }

   public float O(class_746 var1, boolean var2, boolean var3, boolean var4, boolean var5) {
      if (var2) {
         return Math.max(0.0F, (float)(this.U - System.nanoTime()) / 5.0E7F);
      }

      if (var1 == null) {
         return Float.POSITIVE_INFINITY;
      }

      double var6 = var1.method_45325(class_5134.field_23723);
      if (var6 <= 0.0) {
         return Float.POSITIVE_INFINITY;
      }

      float var8 = (float)(20.0 / var6);
      return Math.max(0.0F, (l(var3, var4, var5) - var1.method_7261(0.5F)) * var8);
   }

   private void V(class_746 var1) {
      this.z = System.currentTimeMillis() + 1200L;
      AuraModule.c = (boolean)1;
      g(var1);
   }

   private void T(class_746 var1, l.z var2, l.e var3) {
      if (var3 != l.e.r && var2 != l.z.x && !var1.method_6047().method_31574(class_1802.field_49814)) {
         if (this.q(var1)) {
            g(var1);
         }
      } else {
         this.H(var1);
      }
   }

   private static void g(class_746 var0) {
      if (var0 != null) {
         var0.method_5728(false);
      }
   }

   private static boolean z() {
      class_310 var0 = class_310.method_1551();
      return var0.field_1690 != null && var0.field_1690.field_1867.method_1434();
   }

   private static float M() {
      return class_3532.method_15363(20.0F / a1.p(), 1.0F, 3.0F);
   }

   private static float l(boolean var0, boolean var1, boolean var2) {
      if (var2) {
         return 1.0F;
      }

      float var3 = 1.0F;
      if (var1) {
         var3 = 0.97F + (float)(Math.random() * 0.03F);
      }

      return Math.min(var3, 1.0F);
   }

   private void w(float var1, float var2, boolean var3, boolean var4) {
      float var5 = Math.max(1.0F, Math.min(var1, var2));
      float var6 = Math.max(var5, Math.max(var1, var2));
      float var7 = var4 ? var6 : var5 + (float)(Math.random() * (var6 - var5));
      long var8 = (long)(1.0E9F / var7);
      if (var3) {
         var8 = Math.max(50000000L, (long)((float)var8 * M()));
      }

      this.U = System.nanoTime() + var8;
   }

   private static boolean Z(class_746 var0, l.z var1) {
      if (!var0.method_6047().method_31574(class_1802.field_49814) && var1 != l.z.x && f.m(var0)) {
         boolean var2 = f.c(var0);
         if (var1 == l.z.R) {
            return var2;
         }

         if (var2) {
            return true;
         }

         if (!var0.method_24828()) {
            return false;
         }

         class_310 var3 = class_310.method_1551();
         return var3.field_1690 == null || !var3.field_1690.field_1903.method_1434();
      } else {
         return true;
      }
   }

   private boolean L(class_746 var1, l.z var2, boolean var3, boolean var4) {
      if (var2 == l.z.x || var1.method_6047().method_31574(class_1802.field_49814)) {
         return false;
      }

      if (P(var1)) {
         return false;
      }

      if (AuraModule.c && !var1.method_24828()) {
         return true;
      }

      if (!var1.method_24828() && var1.field_6017 > 0.0) {
         return true;
      }

      if (!var3) {
         return !var1.method_24828() && var1.method_18798().field_1351 < 0.0;
      }

      int var5 = r(var1);
      return var5 >= 0 && var5 <= (var4 ? 1 : 2);
   }

   private static int r(class_746 var0) {
      if (f.c(var0)) {
         return 0;
      }

      double var1 = var0.method_18798().field_1351;
      if (var0.method_24828()) {
         class_310 var3 = class_310.method_1551();
         if (var3.field_1690 == null || !var3.field_1690.field_1903.method_1434()) {
            return -1;
         }

         var1 = 0.42;
      }

      double var6 = var0.field_6017;

      for (int var5 = 1; var5 <= 8; var5++) {
         var1 = (var1 - 0.08) * 0.98;
         if (var1 < 0.0) {
            var6 += -var1;
            if (var6 > 0.0) {
               return var5;
            }
         }
      }

      return -1;
   }

   private static boolean P(class_746 var0) {
      return var0.method_6101()
         || var0.method_5799()
         || var0.method_5771()
         || var0.method_5765()
         || var0.method_31549().field_7479
         || var0.method_6128()
         || var0.method_6059(class_1294.field_5919)
         || var0.method_6059(class_1294.field_5902)
         || var0.method_6059(class_1294.field_5906);
   }

   private static boolean n(class_746 var0) {
      return var0.method_24828() && !var0.method_6101() && !var0.method_5799() && !var0.method_5771() && !var0.method_5765() && !var0.method_31549().field_7479;
   }

   public int G() {
      return this.L;
   }

   public void H() {
      this.L = 0;
      this.h = (boolean)1;
      this.U = 0L;
      this.P = (boolean)0;
      this.z = 0L;
      AuraModule.c = (boolean)0;
   }

   public record b(
      class_1309 target,
      class_238 box,
      float yaw,
      float pitch,
      double range,
      boolean ignoreWalls,
      l.z crit,
      l.e sprint,
      boolean shieldBreaker,
      boolean pauseWhileEating,
      boolean raycastWalls,
      boolean entityHit,
      boolean autoJump,
      boolean predictCrits,
      Runnable beforeAttack,
      boolean legacy,
      boolean tpsSync,
      boolean randomize,
      boolean blatant,
      float minCps,
      float maxCps
   ) {
   }

   public enum e {
      s,
      i,
      r;
   }

   public enum z {
      x,
      H,
      R;
   }
}
