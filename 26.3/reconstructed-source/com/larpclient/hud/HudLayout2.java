package com.larpclient.hud;

import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

public final class HudLayout2 {
   public static HudLayout2.Rect resizeHandleBounds(HudLayout2.Rect var0, int var1, int var2) {
      return new HudLayout2.Rect(
         clampPosition(var0.left() + var0.width() - 4, 8, var1), clampPosition(var0.top() + var0.height() - 4, 8, var2), Math.min(8, var1), Math.min(8, var2)
      );
   }

   public static int clampPosition(int var0, int var1, int var2) {
      return Math.clamp(var0, 0, Math.max(0, var2 - var1));
   }

   public static HudLayout2.SnapResult snapPosition(int var0, int var1, int var2, boolean var3, boolean var4, int var5) {
      int var6 = Math.max(0, var2 - var1);
      int var7 = clampPosition(var0, var1, var2);
      if (var3) {
         int[] var8 = new int[]{0, var6 / 2, var6};
         int[] var9 = new int[]{0, var2 / 2, var2};

         for (int var10 = 0; var10 < var8.length; var10++) {
            if (var5 == var9[var10] && Math.abs(var0 - var8[var10]) <= 7) {
               return new HudLayout2.SnapResult(var8[var10], var9[var10]);
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
            return new HudLayout2.SnapResult(var8[var14], var9[var14]);
         }
      }

      if (var4) {
         var7 = clampPosition(snapToGrid(var7, var2, 8), var1, var2);
      }

      return new HudLayout2.SnapResult(var7, -1);
   }

   public static int wrapIndex(int var0, int var1) {
      return Math.floorMod(var0 / 2, var1);
   }

   public static int snapToGrid(int var0, int var1, int var2) {
      int var3 = var1 / 2;
      return var3 + Math.round((float)(var0 - var3) / var2) * var2;
   }

   public static HudLayout2.Rect snapToGrid(HudLayout2.Rect var0, int var1, int var2, int var3, int var4, List<HudLayout2.Rect> var5) {
      int var6 = Math.min(var1, Math.max(1, var3 - 4));
      int var7 = clampPosition(var0.left, var6, var3);
      HudLayout2.Rect[] var8 = new HudLayout2.Rect[]{
         new HudLayout2.Rect(var7, var0.top - var2 - 3, var6, var2),
         new HudLayout2.Rect(var7, var0.top + var0.height + 3, var6, var2),
         new HudLayout2.Rect(var0.left + var0.width + 3, clampPosition(var0.top, var2, var4), var6, var2),
         new HudLayout2.Rect(var0.left - var6 - 3, clampPosition(var0.top, var2, var4), var6, var2)
      };

      for (HudLayout2.Rect var12 : var8) {
         if (var12.left >= 0
            && var12.top >= 0
            && var12.left + var12.width <= var3
            && var12.top + var12.height <= var4
            && !var12.intersects(var0)
            && var5.stream().noneMatch(var12::intersects)) {
            return var12;
         }
      }

      return null;
   }

   public static double wrapIndex(double var0, double var2, int var4) {
      return var0 * var4 / Math.max(1.0, var2);
   }

   public static <T> T findTopmostAt(List<T> var0, Predicate<T> var1, Function<T, HudLayout2.Rect> var2, double var3, double var5) {
      for (int var7 = var0.size() - 1; var7 >= 0; var7--) {
         Object var8 = var0.get(var7);
         if (var1.test(var8) && ((HudLayout2.Rect)var2.apply(var8)).contains(var3, var5, 12)) {
            return (T)var8;
         }
      }

      return null;
   }

   public static boolean isAnyTrue(boolean var0, boolean var1, boolean var2) {
      return var0 || var1 || var2;
   }

   private HudLayout2() {
   }

   public record DragGrab(double x, double y) {
      private final double offsetX;
      private final double offsetY;

      public DragGrab(double x, double y) {
         this.offsetX = x;
         this.offsetY = y;
      }

      public HudLayout2.DragResult move(
         double var1, double var3, HudLayout2.Rect var5, int var6, int var7, boolean var8, boolean var9, boolean var10, int var11, int var12
      ) {
         HudLayout2.SnapResult var13 = HudLayout2.snapPosition((int)Math.round(var1 - this.offsetX), var5.width, var6, var8 && !var10, var9 && !var10, var11);
         HudLayout2.SnapResult var14 = HudLayout2.snapPosition((int)Math.round(var3 - this.offsetY), var5.height, var7, var8 && !var10, var9 && !var10, var12);
         return new HudLayout2.DragResult(var13.position, var14.position, var13.guide, var14.guide);
      }
   }

   public record DragResult(int x, int y, int guideX, int guideY) {
      private final int posX;
      private final int posY;

      public DragResult(int x, int y, int guideX, int guideY) {
         this.posX = x;
         this.posY = y;
         this.guideX = guideX;
         this.guideY = guideY;
      }
   }

   public record Rect(int x, int y, int width, int height) {
      private final int left;
      private final int top;

      public Rect(int x, int y, int width, int height) {
         this.left = x;
         this.top = y;
         this.width = width;
         this.height = height;
      }

      public boolean intersects(HudLayout2.Rect var1) {
         return this.left < var1.left + var1.width
            && this.left + this.width > var1.left
            && this.top < var1.top + var1.height
            && this.top + this.height > var1.top;
      }

      public boolean contains(double var1, double var3, int var5) {
         int var6 = Math.max(this.width, var5);
         int var7 = Math.max(this.height, var5);
         return var1 >= this.left - (var6 - this.width) / 2.0
            && var1 < this.left + this.width + (var6 - this.width) / 2.0
            && var3 >= this.top - (var7 - this.height) / 2.0
            && var3 < this.top + this.height + (var7 - this.height) / 2.0;
      }
   }

   public record ResizeDrag(HudLayout2.Rect bounds, double mouseX, double mouseY, double initialScale) {
      public double scale(double var1, double var3) {
         double var5 = Math.max(1, this.bounds.width());
         double var7 = Math.max(1, this.bounds.height());
         double var9 = 1.0 + ((var1 - this.mouseX) * var5 + (var3 - this.mouseY) * var7) / (var5 * var5 + var7 * var7);
         return Math.clamp(this.initialScale * var9, 0.2, 4.0);
      }
   }

   public record SnapResult(int position, int guide) {
   }
}
