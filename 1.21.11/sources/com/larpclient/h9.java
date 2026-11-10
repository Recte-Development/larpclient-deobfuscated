package com.larpclient;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public final class h9<C> {
   private C k;
   private int A;
   private int B;
   private int J;
   private int V;
   private int Z;
   private List<int[]> y = List.of();

   public void F() {
      this.k = null;
      this.J = this.V = 0;
   }

   public void a(int var1, int var2) {
      if (this.k != null && this.A == var1 && this.B == var2) {
         this.k = null;
      }
   }

   public void T(h9.v<C> var1, int var2, int var3, int var4, int var5) {
      if (var4 >= 1 && var4 <= 12 && var5 >= 1 && var5 <= 8) {
         if (this.Z != var4) {
            ArrayList var6 = new ArrayList();

            for (int var7 = -var4; var7 <= var4; var7++) {
               for (int var8 = -var4; var8 <= var4; var8++) {
                  var6.add(new int[]{var7, var8});
               }
            }

            var6.sort(Comparator.comparingInt(var0 -> var0[0] * var0[0] + var0[1] * var0[1]));
            this.y = List.copyOf(var6);
            this.Z = var4;
            this.V = 0;
         }

         int var12 = 0;
         int var13 = 0;
         int var14 = 0;
         int var9 = 2 * var4 + 1;
         int var10 = var9 * var9;
         if (this.k != null) {
            var12++;
            if (Math.abs((long)this.A - var2) > var4 || Math.abs((long)this.B - var3) > var4 || var1.loaded(this.A, this.B) != this.k) {
               this.k = null;
            }
         }

         while (var12 < 16 && var13 < 64 && var14 < var5) {
            if (this.k == null) {
               int var11 = this.V++ % var10;
               this.V %= var10;
               this.A = var2 + this.y.get(var11)[0];
               this.B = var3 + this.y.get(var11)[1];
               var12++;
               if (!var1.due(this.A, this.B)) {
                  continue;
               }

               this.k = (C)var1.loaded(this.A, this.B);
               if (this.k == null) {
                  continue;
               }

               this.J = 0;
               var1.begin(this.k);
            }

            if (this.J >= var1.sections(this.k)) {
               var1.finish(this.A, this.B);
               this.k = null;
            } else {
               int var15 = this.J++;
               var13++;
               if (var1.candidate(this.k, var15)) {
                  var14++;
                  var1.scan(this.k, var15);
               }
            }
         }
      } else {
         throw new IllegalArgumentException("Invalid chunk scan budget");
      }
   }

   public interface v<C> {
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
