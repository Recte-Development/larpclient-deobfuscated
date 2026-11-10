package com.larpclient;

import java.util.Locale;

public final class fx {
   private final fx.o X;
   private final int T;
   private final int a;
   private final boolean M;
   private final boolean w;
   private final boolean r;

   public fx(fx.o var1, int var2, int var3, int var4, int var5) {
      this.X = var1;
      this.T = Math.max(1, var2);
      this.a = Math.max(1, var3);
      this.M = var1.rowsAlongX();
      int var6 = this.M ? var4 : var5;
      int var7 = this.M ? var5 : var4;
      this.r = (boolean)(Math.abs(var6 - this.X()) <= Math.abs(var6 - this.U()) ? 1 : 0);
      this.w = (boolean)(Math.abs(var7 - this.L()) <= Math.abs(var7 - this.x()) ? 1 : 0);
   }

   public fx.o X() {
      return this.X;
   }

   public boolean l() {
      return this.M;
   }

   public int K() {
      return this.T;
   }

   public int i() {
      return this.a;
   }

   public int r() {
      return (this.X.sizeY() + this.T - 1) / this.T;
   }

   public fx.i V(int var1) {
      int var2 = this.X.maxY - var1 * this.T;
      return new fx.i(var1, Math.max(this.X.minY, var2 - this.T + 1), var2);
   }

   public int J(int var1) {
      return var1 <= this.X.maxY && var1 >= this.X.minY ? (this.X.maxY - var1) / this.T : -1;
   }

   private int X() {
      return this.M ? this.X.minX : this.X.minZ;
   }

   private int U() {
      return this.M ? this.X.maxX : this.X.maxZ;
   }

   private int L() {
      return this.M ? this.X.minZ : this.X.minX;
   }

   private int x() {
      return this.M ? this.X.maxZ : this.X.maxX;
   }

   public int w() {
      return (this.x() - this.L() + this.a) / this.a;
   }

   public int c(int var1, int var2, int var3) {
      int var4 = this.M ? var2 : var1;
      int var5 = this.w ? var4 - this.L() : this.x() - var4;
      int var6 = var5 / this.a;
      return var3 % 2 == 0 ? var6 : this.w() - 1 - var6;
   }

   public int[] O(int var1, int var2) {
      int var3 = var2 % 2 == 0 ? var1 : this.w() - 1 - var1;
      int var4 = this.w ? this.L() + var3 * this.a : this.x() - var3 * this.a;
      int var5 = this.w ? Math.min(this.x(), var4 + this.a - 1) : Math.max(this.L(), var4 - this.a + 1);
      return new int[]{Math.min(var4, var5), Math.max(var4, var5)};
   }

   public int l(int var1, int var2) {
      int var3 = var1 + this.r(var2);
      int var4 = this.r == (var3 % 2 == 0 ? 1 : 0) ? 1 : 0;
      return var4 != 0 ? 1 : -1;
   }

   private int r(int var1) {
      return (int)((long)this.w() * var1 % 2L);
   }

   public long l(int var1, int var2, int var3, int var4) {
      int var5 = this.c(var1, var3, var4);
      int var6 = this.M ? var1 : var3;
      int var7 = this.l(var5, var4);
      int var8 = var7 > 0 ? var6 - this.X() : this.U() - var6;
      int var9 = this.X.maxY - var2;
      return (long)var5 << 40 | (long)var8 << 12 | Math.min(var9, 4095);
   }

   public boolean S(int var1, int var2) {
      int[] var3 = this.O(var1, var2);
      fx.i var4 = this.V(var2);
      return var3[1] - var3[0] == 2 && var4.height() == 3;
   }

   public int b(int var1, int var2) {
      int[] var3 = this.O(var1, var2);
      return (var3[0] + var3[1]) / 2;
   }

   public static int[] C(String var0) {
      if (var0 == null) {
         return null;
      }

      String[] var1 = var0.trim().replace(',', ' ').split("\\s+");
      if (var1.length != 3) {
         return null;
      }

      try {
         return new int[]{Integer.parseInt(var1[0]), Integer.parseInt(var1[1]), Integer.parseInt(var1[2])};
      } catch (NumberFormatException var3) {
         return null;
      }
   }

   public static String A(int var0, int var1, int var2) {
      return var0 + " " + var1 + " " + var2;
   }

   public static boolean K(int var0, int var1, int var2) {
      return var0 - (var1 + 1) <= var2;
   }

   public record i(int index, int bottom, int top) {
      public int height() {
         return this.top - this.bottom + 1;
      }

      public boolean contains(int var1) {
         return var1 >= this.bottom && var1 <= this.top;
      }
   }

   public record o(int minX, int minY, int minZ, int maxX, int maxY, int maxZ) {
      public static fx.o of(int[] var0, int[] var1) {
         return new fx.o(
            Math.min(var0[0], var1[0]),
            Math.min(var0[1], var1[1]),
            Math.min(var0[2], var1[2]),
            Math.max(var0[0], var1[0]),
            Math.max(var0[1], var1[1]),
            Math.max(var0[2], var1[2])
         );
      }

      public int sizeX() {
         return this.maxX - this.minX + 1;
      }

      public int sizeY() {
         return this.maxY - this.minY + 1;
      }

      public int sizeZ() {
         return this.maxZ - this.minZ + 1;
      }

      public long volume() {
         return (long)this.sizeX() * this.sizeY() * this.sizeZ();
      }

      public boolean contains(int var1, int var2, int var3) {
         return var1 >= this.minX && var1 <= this.maxX && var2 >= this.minY && var2 <= this.maxY && var3 >= this.minZ && var3 <= this.maxZ;
      }

      public boolean containsColumn(int var1, int var2) {
         return var1 >= this.minX && var1 <= this.maxX && var2 >= this.minZ && var2 <= this.maxZ;
      }

      public boolean rowsAlongX() {
         return this.sizeX() >= this.sizeZ();
      }

      public String describe() {
         return String.format(Locale.ROOT, "%d x %d x %d (%,d blocks)", this.sizeX(), this.sizeY(), this.sizeZ(), this.volume());
      }
   }
}
