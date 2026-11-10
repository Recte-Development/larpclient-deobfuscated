package com.larpclient;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public final class tu<C> {
   private C y;
   private int B;
   private int R;
   private int V;
   private int r;
   private int j;
   private List<int[]> h = List.of();

   public void y() {
      this.y = null;
      this.V = this.r = 0;
   }

   public void M(int var1, int var2) {
      if (this.y != null && this.B == var1 && this.R == var2) {
         this.y = null;
      }
   }

   public void n(tu.o<C> var1, int var2, int var3, int var4, int var5) {
      if (var4 >= 1 && var4 <= 12 && var5 >= 1 && var5 <= 8) {
         if (this.j != var4) {
            ArrayList var6 = new ArrayList();

            for (int var7 = -var4; var7 <= var4; var7++) {
               for (int var8 = -var4; var8 <= var4; var8++) {
                  var6.add(new int[]{var7, var8});
               }
            }

            var6.sort(Comparator.comparingInt(var0 -> var0[0] * var0[0] + var0[1] * var0[1]));
            this.h = List.copyOf(var6);
            this.j = var4;
            this.r = 0;
         }

         int var12 = 0;
         int var13 = 0;
         int var14 = 0;
         int var9 = 2 * var4 + 1;
         int var10 = var9 * var9;
         if (this.y != null) {
            var12++;
            if (Math.abs((long)this.B - var2) > var4 || Math.abs((long)this.R - var3) > var4 || var1.loaded(this.B, this.R) != this.y) {
               this.y = null;
            }
         }

         while (var12 < 16 && var13 < 64 && var14 < var5) {
            if (this.y == null) {
               int var11 = this.r++ % var10;
               this.r %= var10;
               this.B = var2 + this.h.get(var11)[0];
               this.R = var3 + this.h.get(var11)[1];
               var12++;
               if (!var1.due(this.B, this.R)) {
                  continue;
               }

               this.y = (C)var1.loaded(this.B, this.R);
               if (this.y == null) {
                  continue;
               }

               this.V = 0;
               var1.begin(this.y);
            }

            if (this.V >= var1.sections(this.y)) {
               var1.finish(this.B, this.R);
               this.y = null;
            } else {
               int var15 = this.V++;
               var13++;
               if (var1.candidate(this.y, var15)) {
                  var14++;
                  var1.scan(this.y, var15);
               }
            }
         }
      } else {
         throw new IllegalArgumentException("Invalid chunk scan budget");
      }
   }

   public interface o<C> {
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
