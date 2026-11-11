package com.larpclient.gui.misc;

import java.util.ArrayList;
import java.util.List;

public final class FlowLayout {
   public static final int cellHeight = 24;
   public static final int gap = 6;

   public static FlowLayout.Layout layout(int var0, List<Integer> var1) {
      int var2 = Math.max(1, var0);
      int var3 = 0;
      byte var4 = 0;
      ArrayList var5 = new ArrayList();

      for (int var7 : var1) {
         int var8 = Math.clamp(var7, 1, var2);
         if (var3 > 0 && var3 + var8 > var2) {
            var3 = 0;
            var4 += 30;
         }

         var5.add(new FlowLayout.Cell(var3, var4, var8));
         var3 += var8 + 6;
      }

      return new FlowLayout.Layout(List.copyOf(var5), var5.isEmpty() ? 0 : var4 + 24);
   }

   public static FlowLayout.Layout layoutJustified(int var0, List<Integer> var1) {
      int var2 = Math.max(1, var0);
      FlowLayout.Layout var3 = layout(var2, var1);
      ArrayList var4 = new ArrayList();
      int var5 = 0;

      while (var5 < var3.cells().size()) {
         int var6 = var3.cells().get(var5).y2();
         int var7 = var5;

         while (var7 < var3.cells().size() && var3.cells().get(var7).y2() == var6) {
            var7++;
         }

         FlowLayout.Cell var8 = var3.cells().get(var7 - 1);
         int var9 = Math.max(0, var2 - (var8.x2() + var8.width()));
         int var10 = var7 - var5;

         for (int var11 = 0; var11 < var10; var11++) {
            FlowLayout.Cell var12 = var3.cells().get(var5 + var11);
            int var13 = var9 * var11 / var10;
            int var14 = var9 * (var11 + 1) / var10 - var13;
            var4.add(new FlowLayout.Cell(var12.x2() + var13, var6, var12.width() + var14));
         }

         var5 = var7;
      }

      return new FlowLayout.Layout(List.copyOf(var4), var3.height());
   }

   private FlowLayout() {
   }

   public record Cell(int x, int y, int width) {
      private final int x2;
      private final int y2;

      public Cell(int x, int y, int width) {
         this.x2 = x;
         this.y2 = y;
         this.width = width;
      }
   }

   public record Layout(List<FlowLayout.Cell> cells, int height) {
   }
}
