package com.larpclient;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public final class pt<C> {
   private C Q;
   private int t;
   private int c;
   private int p;
   private int C;
   private int v;
   private List<int[]> L = List.of();

   public void r() {
      this.Q = null;
      this.p = this.C = 0;
   }

   public void i(int var1, int var2) {
      if (this.Q != null && this.t == var1 && this.c == var2) {
         this.Q = null;
      }
   }

   public void k(pt.d<C> var1, int var2, int var3, int var4, int var5) {
      if (var4 >= 1 && var4 <= 12 && var5 >= 1 && var5 <= 8) {
         if (this.v != var4) {
            ArrayList var6 = new ArrayList();

            for (int var7 = -var4; var7 <= var4; var7++) {
               for (int var8 = -var4; var8 <= var4; var8++) {
                  var6.add(new int[]{var7, var8});
               }
            }

            var6.sort(Comparator.comparingInt(var0 -> var0[0] * var0[0] + var0[1] * var0[1]));
            this.L = List.copyOf(var6);
            this.v = var4;
            this.C = 0;
         }

         int var12 = 0;
         int var13 = 0;
         int var14 = 0;
         int var9 = 2 * var4 + 1;
         int var10 = var9 * var9;
         if (this.Q != null) {
            var12++;
            if (Math.abs((long)this.t - var2) > var4 || Math.abs((long)this.c - var3) > var4 || var1.loaded(this.t, this.c) != this.Q) {
               this.Q = null;
            }
         }

         while (var12 < 16 && var13 < 64 && var14 < var5) {
            if (this.Q == null) {
               int var11 = this.C++ % var10;
               this.C %= var10;
               this.t = var2 + this.L.get(var11)[0];
               this.c = var3 + this.L.get(var11)[1];
               var12++;
               if (!var1.due(this.t, this.c)) {
                  continue;
               }

               this.Q = (C)var1.loaded(this.t, this.c);
               if (this.Q == null) {
                  continue;
               }

               this.p = 0;
               var1.begin(this.Q);
            }

            if (this.p >= var1.sections(this.Q)) {
               var1.finish(this.t, this.c);
               this.Q = null;
            } else {
               int var15 = this.p++;
               var13++;
               if (var1.candidate(this.Q, var15)) {
                  var14++;
                  var1.scan(this.Q, var15);
               }
            }
         }
      } else {
         throw new IllegalArgumentException("Invalid chunk scan budget");
      }
   }

   public interface d<C> {
      C loaded(int var1, int var2);

      boolean due(int var1, int var2);

      int sections(C var1);

      boolean candidate(C var1, int var2);

      void begin();

      default void begin(C var1) {
         this.begin();
      }

      void scan(C var1, int var2);

      void finish(int var1, int var2);
   }
}
