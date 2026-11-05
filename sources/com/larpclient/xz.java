package com.larpclient;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public final class xz<C> {
   private C L;
   private int e;
   private int c;
   private int n;
   private int A;
   private int l;
   private List<int[]> q = List.of();

   public void V() {
      this.L = null;
      this.n = this.A = 0;
   }

   public void z(int var1, int var2) {
      if (this.L != null && this.e == var1 && this.c == var2) {
         this.L = null;
      }
   }

   public void a(xz.b<C> var1, int var2, int var3, int var4, int var5) {
      if (var4 >= 1 && var4 <= 12 && var5 >= 1 && var5 <= 8) {
         if (this.l != var4) {
            ArrayList var6 = new ArrayList();

            for (int var7 = -var4; var7 <= var4; var7++) {
               for (int var8 = -var4; var8 <= var4; var8++) {
                  var6.add(new int[]{var7, var8});
               }
            }

            var6.sort(Comparator.comparingInt(var0 -> var0[0] * var0[0] + var0[1] * var0[1]));
            this.q = List.copyOf(var6);
            this.l = var4;
            this.A = 0;
         }

         int var12 = 0;
         int var13 = 0;
         int var14 = 0;
         int var9 = 2 * var4 + 1;
         int var10 = var9 * var9;
         if (this.L != null) {
            var12++;
            if (Math.abs((long)this.e - var2) > var4 || Math.abs((long)this.c - var3) > var4 || var1.loaded(this.e, this.c) != this.L) {
               this.L = null;
            }
         }

         while (var12 < 16 && var13 < 64 && var14 < var5) {
            if (this.L == null) {
               int var11 = this.A++ % var10;
               this.A %= var10;
               this.e = var2 + this.q.get(var11)[0];
               this.c = var3 + this.q.get(var11)[1];
               var12++;
               if (!var1.due(this.e, this.c)) {
                  continue;
               }

               this.L = (C)var1.loaded(this.e, this.c);
               if (this.L == null) {
                  continue;
               }

               this.n = 0;
               var1.begin(this.L);
            }

            if (this.n >= var1.sections(this.L)) {
               var1.finish(this.e, this.c);
               this.L = null;
            } else {
               int var15 = this.n++;
               var13++;
               if (var1.candidate(this.L, var15)) {
                  var14++;
                  var1.scan(this.L, var15);
               }
            }
         }
      } else {
         throw new IllegalArgumentException("Invalid chunk scan budget");
      }
   }

   public interface b<C> {
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
