package com.larpclient;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public final class ha<C> {
   private C h;
   private int m;
   private int o;
   private int x;
   private int E;
   private int u;
   private List<int[]> y = List.of();

   public void X() {
      this.h = null;
      this.x = this.E = 0;
   }

   public void Z(int var1, int var2) {
      if (this.h != null && this.m == var1 && this.o == var2) {
         this.h = null;
      }
   }

   public void b(ha.j<C> var1, int var2, int var3, int var4, int var5) {
      if (var4 >= 1 && var4 <= 12 && var5 >= 1 && var5 <= 8) {
         if (this.u != var4) {
            ArrayList var6 = new ArrayList();

            for (int var7 = -var4; var7 <= var4; var7++) {
               for (int var8 = -var4; var8 <= var4; var8++) {
                  var6.add(new int[]{var7, var8});
               }
            }

            var6.sort(Comparator.comparingInt(var0 -> var0[0] * var0[0] + var0[1] * var0[1]));
            this.y = List.copyOf(var6);
            this.u = var4;
            this.E = 0;
         }

         int var12 = 0;
         int var13 = 0;
         int var14 = 0;
         int var9 = 2 * var4 + 1;
         int var10 = var9 * var9;
         if (this.h != null) {
            var12++;
            if (Math.abs((long)this.m - var2) > var4 || Math.abs((long)this.o - var3) > var4 || var1.loaded(this.m, this.o) != this.h) {
               this.h = null;
            }
         }

         while (var12 < 16 && var13 < 64 && var14 < var5) {
            if (this.h == null) {
               int var11 = this.E++ % var10;
               this.E %= var10;
               this.m = var2 + this.y.get(var11)[0];
               this.o = var3 + this.y.get(var11)[1];
               var12++;
               if (!var1.due(this.m, this.o)) {
                  continue;
               }

               this.h = (C)var1.loaded(this.m, this.o);
               if (this.h == null) {
                  continue;
               }

               this.x = 0;
               var1.begin(this.h);
            }

            if (this.x >= var1.sections(this.h)) {
               var1.finish(this.m, this.o);
               this.h = null;
            } else {
               int var15 = this.x++;
               var13++;
               if (var1.candidate(this.h, var15)) {
                  var14++;
                  var1.scan(this.h, var15);
               }
            }
         }
      } else {
         throw new IllegalArgumentException("Invalid chunk scan budget");
      }
   }

   public interface j<C> {
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
