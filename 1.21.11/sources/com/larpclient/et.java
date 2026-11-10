package com.larpclient;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import net.minecraft.class_2246;
import net.minecraft.class_2248;
import net.minecraft.class_238;
import net.minecraft.class_2480;
import net.minecraft.class_2504;
import net.minecraft.class_2506;
import net.minecraft.class_2680;
import net.minecraft.class_2741;
import net.minecraft.class_2350.class_2351;

public final class et {
   private static final Set<class_2248> i = Set.of(
      class_2246.field_10327, class_2246.field_23152, class_2246.field_10485, class_2246.field_46797, class_2246.field_10282, class_2246.field_10502
   );
   private static final Set<class_2248> Q = Set.of(
      class_2246.field_10034, class_2246.field_10380, class_2246.field_16328, class_2246.field_10312, class_2246.field_10200, class_2246.field_10228
   );
   private static final Set<class_2248> m = Set.of(
      class_2246.field_9980,
      class_2246.field_10181,
      class_2246.field_16333,
      class_2246.field_16334,
      class_2246.field_10535,
      class_2246.field_10105,
      class_2246.field_10414,
      class_2246.field_10333,
      class_2246.field_16329,
      class_2246.field_10083,
      class_2246.field_16335,
      class_2246.field_16337,
      class_2246.field_16336,
      class_2246.field_16331,
      class_2246.field_17563
   );
   private static final Set<class_2248> I = Set.of(
      class_2246.field_10033,
      class_2246.field_10285,
      class_2246.field_27115,
      class_2246.field_10085,
      class_2246.field_10205,
      class_2246.field_10201,
      class_2246.field_10234,
      class_2246.field_22108,
      class_2246.field_10002,
      class_2246.field_28892,
      class_2246.field_28900,
      class_2246.field_28896
   );

   public static boolean l(class_2680 var0) {
      return (var0.method_27852(class_2246.field_20422) || var0.method_27852(class_2246.field_20421))
         && (Integer)var0.method_11654(class_2741.field_20432) == 5;
   }

   public static et.g i(class_2680 var0, int var1, boolean var2) {
      class_2248 var3 = var0.method_26204();
      if (var3 instanceof class_2480 || i.contains(var3) || l(var0)) {
         return et.g.U;
      }

      if (var0.method_27852(class_2246.field_10443)) {
         return var2 ? et.g.b : et.g.U;
      }

      if (Q.contains(var3)) {
         return et.g.b;
      }

      if (m.contains(var3)) {
         return et.g.l;
      }

      if (!I.contains(var3) && !(var3 instanceof class_2506) && !(var3 instanceof class_2504)) {
         if (var0.method_27852(class_2246.field_28888) && var0.method_11654(class_2741.field_12496) != class_2351.field_11052) {
            return et.g.k;
         } else {
            return !var0.method_27852(class_2246.field_29031) && (var1 < 16 || !var0.method_27852(class_2246.field_28888)) ? et.g.i : et.g.k;
         }
      } else {
         return et.g.I;
      }
   }

   public enum g {
      i,
      b,
      l,
      I,
      k,
      U;
   }

   public static final class o {
      private static final et.g[] G = et.g.values();
      private static final int f = 256;
      private final class_2680[] g = new class_2680[256];
      private final byte[] v = new byte[256];
      private final byte[] U = new byte[256];
      private boolean e;

      public et.g j(class_2680 var1, int var2, boolean var3) {
         if (var3 != this.e) {
            this.M();
            this.e = var3;
         }

         int var4 = System.identityHashCode(var1) & 0xFF;
         if (this.g[var4] != var1) {
            this.v[var4] = (byte)et.i(var1, 15, var3).ordinal();
            this.U[var4] = (byte)et.i(var1, 16, var3).ordinal();
            this.g[var4] = var1;
         }

         return G[var2 >= 16 ? this.U[var4] : this.v[var4]];
      }

      public void M() {
         Arrays.fill(this.g, null);
      }
   }

   public static final class x {
      private final Map<Integer, et.x.q> u = new HashMap<>();
      private final et.o W = new et.o();
      private et.x.q B;
      private int t;

      public void m() {
         this.u.clear();
         this.B = null;
      }

      public int E() {
         return this.u.size();
      }

      public et.g v(class_2680 var1, int var2, boolean var3) {
         return this.W.j(var1, var2, var3);
      }

      public void T(class_2680 var1, int var2, boolean var3, boolean var4) {
         this.R(var1, 0, var2, 0, var3, var4);
      }

      public void R(class_2680 var1, int var2, int var3, int var4, boolean var5, boolean var6) {
         this.C(this.W.j(var1, var3, var5), var1, var2, var3, var4, var5, var6);
      }

      public void C(et.g var1, class_2680 var2, int var3, int var4, int var5, boolean var6, boolean var7) {
         if (var1 != et.g.i && (var1 != et.g.k || var7)) {
            int var8 = var4 >> 4;
            et.x.q var9 = this.B != null && this.t == var8 ? this.B : this.u.get(var8);
            if (var9 == null) {
               this.u.put(var8, var9 = new et.x.q(var4));
            }

            this.B = var9;
            this.t = var8;
            if (var2.method_27852(class_2246.field_10443) && !var6) {
               var9.Z++;
            }

            if (var1 != et.g.k) {
               var9.n(var3, var4, var5);
            }

            switch (var1) {
               case b:
                  var9.g++;
                  break;
               case l:
                  var9.M++;
                  break;
               case I:
                  var9.N++;
                  break;
               case k:
                  var9.k++;
                  break;
               case U:
                  var9.s++;
                  if (var9.E.isEmpty()) {
                     var9.E = var2.method_26204().method_9518().getString();
                     var9.z = var4;
                  }
            }
         }
      }

      public z T(String var1, int var2, int var3) {
         return this.n(var1, var2, var3, 1);
      }

      public z n(String var1, int var2, int var3, int var4) {
         z var5 = z.EMPTY;

         for (et.x.q var7 : this.u.values()) {
            z var8 = var7.e();
            boolean var9 = var8.qualifies(var1, var2, var3, var4);
            boolean var10 = var5.qualifies(var1, var2, var3, var4);
            if (var9 && !var10 || var9 == var10 && (var8.weight() > var5.weight() || var8.weight() == var5.weight() && var8.sampleY() < var5.sampleY())) {
               var5 = var8;
            }
         }

         return var5;
      }

      private static final class q {
         int g;
         int M;
         int N;
         int k;
         int s;
         int z;
         int Z;
         String E = "";
         int Y = 16;
         int V = Integer.MAX_VALUE;
         int I = 16;
         int h;
         int K = Integer.MIN_VALUE;
         int i;

         q(int var1) {
            this.z = var1;
         }

         void n(int var1, int var2, int var3) {
            this.Y = Math.min(this.Y, var1);
            this.V = Math.min(this.V, var2);
            this.I = Math.min(this.I, var3);
            this.h = Math.max(this.h, var1 + 1);
            this.K = Math.max(this.K, var2 + 1);
            this.i = Math.max(this.i, var3 + 1);
         }

         z e() {
            class_238 var1 = this.V == Integer.MAX_VALUE ? null : new class_238(this.Y, this.V, this.I, this.h, this.K, this.i);
            return new z(this.g, this.M, this.N, this.k, this.s, this.z, this.E, var1, this.Z);
         }
      }
   }
}
