package com.larpclient.gui.misc;

import java.util.BitSet;

public final class DrawingGrid {
   public static final int sIZE = 15;
   public static final int cENTER = 7;
   private static final int cELL_COUNT = 225;
   private static final int hEX_LENGTH = 57;
   public static final DrawingGrid eMPTY = new DrawingGrid(new BitSet(225));
   public static final DrawingGrid dEFAULT_CROSSHAIR;
   private final BitSet cells;

   private DrawingGrid(BitSet var1) {
      this.cells = var1;
   }

   private static int indexOf(int var0, int var1) {
      if (var0 >= 0 && var1 >= 0 && var0 < 15 && var1 < 15) {
         return var1 * 15 + var0;
      } else {
         throw new IndexOutOfBoundsException("Cell outside the drawing");
      }
   }

   public boolean isSet(int var1, int var2) {
      return this.cells.get(indexOf(var1, var2));
   }

   public boolean isEmpty() {
      return this.cells.isEmpty();
   }

   public int cellCount() {
      return this.cells.cardinality();
   }

   public DrawingGrid withCell(int var1, int var2, boolean var3) {
      int var4 = indexOf(var1, var2);
      if (this.cells.get(var4) == var3) {
         return this;
      }

      BitSet var5 = (BitSet)this.cells.clone();
      var5.set(var4, var3);
      return new DrawingGrid(var5);
   }

   public String toHex() {
      StringBuilder var1 = new StringBuilder(57);

      for (int var2 = 0; var2 < 57; var2++) {
         int var3 = 0;

         for (int var4 = 0; var4 < 4; var4++) {
            if (this.cells.get(var2 * 4 + var4)) {
               var3 |= 8 >> var4;
            }
         }

         var1.append(Character.forDigit(var3, 16));
      }

      return var1.toString();
   }

   public static DrawingGrid fromHex(String var0) {
      if (var0 != null && var0.length() == 57) {
         BitSet var1 = new BitSet(225);

         for (int var2 = 0; var2 < 57; var2++) {
            int var3 = Character.digit(var0.charAt(var2), 16);
            if (var3 < 0) {
               throw new IllegalArgumentException("Expected hex digits");
            }

            for (int var4 = 0; var4 < 4; var4++) {
               if ((var3 & 8 >> var4) != 0) {
                  if (var2 * 4 + var4 >= 225) {
                     throw new IllegalArgumentException("Cell outside the drawing");
                  }

                  var1.set(var2 * 4 + var4);
               }
            }
         }

         return new DrawingGrid(var1);
      } else {
         throw new IllegalArgumentException("Expected 57 hex digits");
      }
   }

   @Override
   public boolean equals(Object var1) {
      return var1 instanceof DrawingGrid var2 && this.cells.equals(var2.cells);
   }

   @Override
   public int hashCode() {
      return this.cells.hashCode();
   }

   static {
      BitSet var0 = new BitSet(225);
      var0.set(indexOf(7, 7));

      for (int var1 = 2; var1 <= 4; var1++) {
         var0.set(indexOf(7 - var1, 7));
         var0.set(indexOf(7 + var1, 7));
         var0.set(indexOf(7, 7 - var1));
         var0.set(indexOf(7, 7 + var1));
      }

      dEFAULT_CROSSHAIR = new DrawingGrid(var0);
   }
}
