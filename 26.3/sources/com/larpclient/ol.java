package com.larpclient;

import java.util.Locale;

public final class ol {
   private final ol.c b;
   private final int K;
   private final int T;
   private final boolean M;
   private final boolean h;
   private final boolean y;

   public ol(ol.c var1, int var2, int var3, int var4, int var5) {
      this.b = var1;
      this.K = Math.max(1, var2);
      this.T = Math.max(1, var3);
      this.M = var1.rowsAlongX();
      int var6 = this.M ? var4 : var5;
      int var7 = this.M ? var5 : var4;
      this.y = (boolean)(Math.abs(var6 - this.a()) <= Math.abs(var6 - this.T()) ? 1 : 0);
      this.h = (boolean)(Math.abs(var7 - this.X()) <= Math.abs(var7 - this.o()) ? 1 : 0);
   }

   public ol.c T() {
      return this.b;
   }

   public boolean w() {
      return this.M;
   }

   public int c() {
      return this.K;
   }

   public int Z() {
      return this.T;
   }

   public int n() {
      return (this.b.sizeY() + this.K - 1) / this.K;
   }

   public ol.k i(int var1) {
      int var2 = this.b.maxY - var1 * this.K;
      return new ol.k(var1, Math.max(this.b.minY, var2 - this.K + 1), var2);
   }

   public int K(int var1) {
      return var1 <= this.b.maxY && var1 >= this.b.minY ? (this.b.maxY - var1) / this.K : -1;
   }

   private int a() {
      return this.M ? this.b.minX : this.b.minZ;
   }

   private int T() {
      return this.M ? this.b.maxX : this.b.maxZ;
   }

   private int X() {
      return this.M ? this.b.minZ : this.b.minX;
   }

   private int o() {
      return this.M ? this.b.maxZ : this.b.maxX;
   }

   public int w() {
      return (this.o() - this.X() + this.T) / this.T;
   }

   public int x(int var1, int var2, int var3) {
      int var4 = this.M ? var2 : var1;
      int var5 = this.h ? var4 - this.X() : this.o() - var4;
      int var6 = var5 / this.T;
      return var3 % 2 == 0 ? var6 : this.w() - 1 - var6;
   }

   public int[] H(int var1, int var2) {
      int var3 = var2 % 2 == 0 ? var1 : this.w() - 1 - var1;
      int var4 = this.h ? this.X() + var3 * this.T : this.o() - var3 * this.T;
      int var5 = this.h ? Math.min(this.o(), var4 + this.T - 1) : Math.max(this.X(), var4 - this.T + 1);
      return new int[]{Math.min(var4, var5), Math.max(var4, var5)};
   }

   public int w(int var1, int var2) {
      int var3 = var1 + this.E(var2);
      int var4 = this.y == (var3 % 2 == 0 ? 1 : 0) ? 1 : 0;
      return var4 != 0 ? 1 : -1;
   }

   private int E(int var1) {
      return (int)((long)this.w() * var1 % 2L);
   }

   public long A(int var1, int var2, int var3, int var4) {
      int var5 = this.x(var1, var3, var4);
      int var6 = this.M ? var1 : var3;
      int var7 = this.w(var5, var4);
      int var8 = var7 > 0 ? var6 - this.a() : this.T() - var6;
      int var9 = this.b.maxY - var2;
      return (long)var5 << 40 | (long)var8 << 12 | Math.min(var9, 4095);
   }

   public boolean J(int var1, int var2) {
      int[] var3 = this.H(var1, var2);
      ol.k var4 = this.i(var2);
      return var3[1] - var3[0] == 2 && var4.height() == 3;
   }

   public int d(int var1, int var2) {
      int[] var3 = this.H(var1, var2);
      return (var3[0] + var3[1]) / 2;
   }

   public static int[] z(String var0) {
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

   public static String Q(int var0, int var1, int var2) {
      return var0 + " " + var1 + " " + var2;
   }

   public static boolean f(int var0, int var1, int var2) {
      return var0 - (var1 + 1) <= var2;
   }

   public record c(int minX, int minY, int minZ, int maxX, int maxY, int maxZ) {
      public static ol.c of(int[] var0, int[] var1) {
         return new ol.c(
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

   public record k(int index, int bottom, int top) {
      public int height() {
         return this.top - this.bottom + 1;
      }

      public boolean contains(int var1) {
         return var1 >= this.bottom && var1 <= this.top;
      }
   }
}
