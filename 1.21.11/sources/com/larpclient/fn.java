package com.larpclient;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.class_11909;
import net.minecraft.class_124;
import net.minecraft.class_1277;
import net.minecraft.class_1707;
import net.minecraft.class_1713;
import net.minecraft.class_1735;
import net.minecraft.class_1792;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_2338;
import net.minecraft.class_2561;
import net.minecraft.class_310;
import net.minecraft.class_332;
import net.minecraft.class_3917;
import net.minecraft.class_476;
import net.minecraft.class_9290;
import net.minecraft.class_9334;
import net.minecraft.class_9930;

public final class fn extends class_476 {
   private final class_2338 K;
   private final qh N;
   private final int D;
   private final int U;
   private final long m;
   private class_1799 a = class_1799.field_8037;
   private int O = -1;
   private long C;
   private boolean V;

   public fn(class_2338 var1, qh var2, int var3, int var4) {
      super(p(var2, var3, var4), class_310.method_1551().field_1724.method_31548(), class_2561.method_43470("Confirm Sell"));
      this.K = var1 == null ? null : var1.method_10062();
      this.N = var2;
      this.D = var3;
      this.U = var4;
      cz var5 = cz.T();
      this.m = var5.x(var2.E, var3) + var5.x(var2.S, var4);
   }

   private static class_1707 p(qh var0, int var1, int var2) {
      class_1277 var3 = new class_1277(27);
      var3.method_5447(
         11,
         Q(
            new class_1799(class_1802.field_8879),
            class_2561.method_43470("Cancel").method_27692(class_124.field_1068),
            List.of(class_2561.method_43470("Click to cancel").method_27695(new class_124[]{class_124.field_1080, class_124.field_1056}))
         )
      );
      ArrayList var4 = new ArrayList();
      if (var2 > 0) {
         var4.add(L(var0.S, var2));
      }

      if (var1 > 0) {
         var4.add(L(var0.E, var1));
      }

      var3.method_5447(13, Q(var0.u(), class_2561.method_43470("Loot").method_27692(class_124.field_1068), var4));
      long var5 = cz.T().x(var0.E, var1) + cz.T().x(var0.S, var2);
      var3.method_5447(
         15,
         Q(
            new class_1799(class_1802.field_8581),
            cz.T().r(var5),
            List.of(class_2561.method_43470("Click to sell items").method_27695(new class_124[]{class_124.field_1080, class_124.field_1056}))
         )
      );
      return new class_1707(class_3917.field_17326, 0, class_310.method_1551().field_1724.method_31548(), var3, 3);
   }

   private static class_1799 Q(class_1799 var0, class_2561 var1, List<class_2561> var2) {
      var0.method_57379(class_9334.field_49631, var1.method_27661().method_27694(var0x -> var0x.method_10978(false)));
      if (!var2.isEmpty()) {
         var0.method_57379(class_9334.field_49632, new class_9290(var2));
      }

      return var0;
   }

   private static class_2561 L(class_1792 var0, int var1) {
      String var2 = var0 == class_1802.field_8511 ? "Rotten Flesh" : var0.method_7864(new class_1799(var0)).getString() + "s";
      return class_2561.method_43470(var1 + " ")
         .method_27694(var0x -> var0x.method_36139(64648).method_10978(false))
         .method_10852(class_2561.method_43470(var2).method_27692(class_124.field_1068));
   }

   protected void method_62000(class_9930 var1) {
   }

   private void m() {
      ((class_1707)this.field_2797).method_34254(class_1799.field_8037);
      this.field_22787.method_1507(null);
      cz.T().S(this.K);
   }

   public void method_25394(class_332 var1, int var2, int var3, float var4) {
      if (!this.a.method_7960() && System.currentTimeMillis() >= this.C) {
         if (this.V) {
            this.m();
            return;
         }

         ((class_1707)this.field_2797).method_7611(this.O).method_7673(this.a);
         this.a = class_1799.field_8037;
      }

      ((class_1707)this.field_2797).method_34254(this.a.method_7960() ? this.field_22787.field_1724.field_7498.method_34255() : this.a);
      super.method_25394(var1, var2, var3, var4);
      this.method_2380(var1, var2, var3);
   }

   public boolean method_25402(class_11909 var1, boolean var2) {
      if (this.a.method_7960() && (s2.m(var1) == 0 || s2.m(var1) == 1)) {
         for (int var6 : new int[]{11, 13, 15}) {
            class_1735 var7 = ((class_1707)this.field_2797).method_7611(var6);
            int var8 = this.field_2776 + var7.field_7873;
            int var9 = this.field_2800 + var7.field_7872;
            if (!(var1.comp_4798() < var8) && !(var1.comp_4798() >= var8 + 16) && !(var1.comp_4799() < var9) && !(var1.comp_4799() >= var9 + 16)) {
               if (var6 == 11) {
                  this.m();
                  return true;
               }

               if (var6 == 15) {
                  if (this.m <= 0L) {
                     b1.q(class_2561.method_43470("Spawner loot price unavailable").method_27692(class_124.field_1061), true);
                     return true;
                  }

                  cz.T().R(this.K, this.N.E, this.D);
                  cz.T().R(this.K, this.N.S, this.U);
                  yp.K(this.m);
                  this.V = (boolean)1;
               }

               this.O = var6;
               this.a = var7.method_7677().method_7972();
               var7.method_7673(class_1799.field_8037);
               ((class_1707)this.field_2797).method_34254(this.a);
               this.C = System.currentTimeMillis() + 200L;
               return true;
            }
         }

         return true;
      } else {
         return true;
      }
   }

   protected void method_2383(class_1735 var1, int var2, int var3, class_1713 var4) {
   }

   public void method_25419() {
      this.m();
   }

   public void method_25432() {
      ((class_1707)this.field_2797).method_34254(class_1799.field_8037);
   }
}
