package com.larpclient;

import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

public final class cv {
   public static cv.z n(cv.z var0, int var1, int var2) {
      return new cv.z(o(var0.x() + var0.width() - 4, 8, var1), o(var0.y() + var0.height() - 4, 8, var2), Math.min(8, var1), Math.min(8, var2));
   }

   public static int o(int var0, int var1, int var2) {
      return Math.clamp(var0, 0, Math.max(0, var2 - var1));
   }

   public static cv.l c(int var0, int var1, int var2, boolean var3, boolean var4, int var5) {
      int var6 = Math.max(0, var2 - var1);
      int var7 = o(var0, var1, var2);
      if (var3) {
         int[] var8 = new int[]{0, var6 / 2, var6};
         int[] var9 = new int[]{0, var2 / 2, var2};

         for (int var10 = 0; var10 < var8.length; var10++) {
            if (var5 == var9[var10] && Math.abs(var0 - var8[var10]) <= 7) {
               return new cv.l(var8[var10], var9[var10]);
            }
         }

         int var14 = -1;
         int var11 = 5;

         for (int var12 = 0; var12 < var8.length; var12++) {
            int var13 = Math.abs(var0 - var8[var12]);
            if (var13 < var11) {
               var14 = var12;
               var11 = var13;
            }
         }

         if (var14 >= 0) {
            return new cv.l(var8[var14], var9[var14]);
         }
      }

      if (var4) {
         var7 = o(t(var7, var2, 8), var1, var2);
      }

      return new cv.l(var7, -1);
   }

   public static int E(int var0, int var1) {
      return Math.floorMod(var0 / 2, var1);
   }

   public static int t(int var0, int var1, int var2) {
      int var3 = var1 / 2;
      return var3 + Math.round((float)(var0 - var3) / var2) * var2;
   }

   public static cv.z a(cv.z var0, int var1, int var2, int var3, int var4, List<cv.z> var5) {
      int var6 = Math.min(var1, Math.max(1, var3 - 4));
      int var7 = o(var0.x, var6, var3);
      cv.z[] var8 = new cv.z[]{
         new cv.z(var7, var0.y - var2 - 3, var6, var2),
         new cv.z(var7, var0.y + var0.height + 3, var6, var2),
         new cv.z(var0.x + var0.width + 3, o(var0.y, var2, var4), var6, var2),
         new cv.z(var0.x - var6 - 3, o(var0.y, var2, var4), var6, var2)
      };

      for (cv.z var12 : var8) {
         if (var12.x >= 0
            && var12.y >= 0
            && var12.x + var12.width <= var3
            && var12.y + var12.height <= var4
            && !var12.intersects(var0)
            && var5.stream().noneMatch(var12::intersects)) {
            return var12;
         }
      }

      return null;
   }

   public static double y(double var0, double var2, int var4) {
      return var0 * var4 / Math.max(1.0, var2);
   }

   public static <T> T q(List<T> var0, Predicate<T> var1, Function<T, cv.z> var2, double var3, double var5) {
      for (int var7 = var0.size() - 1; var7 >= 0; var7--) {
         Object var8 = var0.get(var7);
         if (var1.test(var8) && ((cv.z)var2.apply(var8)).contains(var3, var5, 12)) {
            return (T)var8;
         }
      }

      return null;
   }

   public static boolean C(boolean var0, boolean var1, boolean var2) {
      return var0 || var1 || var2;
   }

   private cv() {
   }

   public record b(cv.z bounds, double mouseX, double mouseY, double initialScale) {
      public double scale(double var1, double var3) {
         double var5 = Math.max(1, this.bounds.width());
         double var7 = Math.max(1, this.bounds.height());
         double var9 = 1.0 + ((var1 - this.mouseX) * var5 + (var3 - this.mouseY) * var7) / (var5 * var5 + var7 * var7);
         return Math.clamp(this.initialScale * var9, 0.2, 4.0);
      }
   }

   public record f(double x, double y) {
      public cv.s move(double var1, double var3, cv.z var5, int var6, int var7, boolean var8, boolean var9, boolean var10, int var11, int var12) {
         cv.l var13 = cv.c((int)Math.round(var1 - this.x), var5.width, var6, var8 && !var10, var9 && !var10, var11);
         cv.l var14 = cv.c((int)Math.round(var3 - this.y), var5.height, var7, var8 && !var10, var9 && !var10, var12);
         return new cv.s(var13.position, var14.position, var13.guide, var14.guide);
      }
   }

   public record l(int position, int guide) {
   }

   public record s(int x, int y, int guideX, int guideY) {
   }

   public record z(int x, int y, int width, int height) {
      public boolean intersects(cv.z var1) {
         return this.x < var1.x + var1.width && this.x + this.width > var1.x && this.y < var1.y + var1.height && this.y + this.height > var1.y;
      }

      public boolean contains(double var1, double var3, int var5) {
         int var6 = Math.max(this.width, var5);
         int var7 = Math.max(this.height, var5);
         return var1 >= this.x - (var6 - this.width) / 2.0
            && var1 < this.x + this.width + (var6 - this.width) / 2.0
            && var3 >= this.y - (var7 - this.height) / 2.0
            && var3 < this.y + this.height + (var7 - this.height) / 2.0;
      }
   }
}
