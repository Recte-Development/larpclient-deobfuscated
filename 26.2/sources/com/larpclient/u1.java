package com.larpclient;

import java.util.Locale;

public final class u1 {
   private final u1.y C;
   private final int t;
   private final int p;
   private final boolean x;
   private final boolean J;
   private final boolean d;

   public u1(u1.y var1, int var2, int var3, int var4, int var5) {
      this.C = var1;
      this.t = Math.max(1, var2);
      this.p = Math.max(1, var3);
      this.x = var1.rowsAlongX();
      int var6 = this.x ? var4 : var5;
      int var7 = this.x ? var5 : var4;
      this.d = (boolean)(Math.abs(var6 - this.q()) <= Math.abs(var6 - this.J()) ? 1 : 0);
      this.J = (boolean)(Math.abs(var7 - this.o()) <= Math.abs(var7 - this.D()) ? 1 : 0);
   }

   public u1.y J() {
      return this.C;
   }

   public boolean J() {
      return this.x;
   }

   public int u() {
      return this.t;
   }

   public int P() {
      return this.p;
   }

   public int k() {
      return (this.C.sizeY() + this.t - 1) / this.t;
   }

   public u1.b J(int var1) {
      int var2 = this.C.maxY - var1 * this.t;
      return new u1.b(var1, Math.max(this.C.minY, var2 - this.t + 1), var2);
   }

   public int k(int var1) {
      return var1 <= this.C.maxY && var1 >= this.C.minY ? (this.C.maxY - var1) / this.t : -1;
   }

   private int q() {
      return this.x ? this.C.minX : this.C.minZ;
   }

   private int J() {
      return this.x ? this.C.maxX : this.C.maxZ;
   }

   private int o() {
      return this.x ? this.C.minZ : this.C.minX;
   }

   private int D() {
      return this.x ? this.C.maxZ : this.C.maxX;
   }

   public int s() {
      return (this.D() - this.o() + this.p) / this.p;
   }

   public int d(int var1, int var2, int var3) {
      int var4 = this.x ? var2 : var1;
      int var5 = this.J ? var4 - this.o() : this.D() - var4;
      int var6 = var5 / this.p;
      return var3 % 2 == 0 ? var6 : this.s() - 1 - var6;
   }

   public int[] E(int var1, int var2) {
      int var3 = var2 % 2 == 0 ? var1 : this.s() - 1 - var1;
      int var4 = this.J ? this.o() + var3 * this.p : this.D() - var3 * this.p;
      int var5 = this.J ? Math.min(this.D(), var4 + this.p - 1) : Math.max(this.o(), var4 - this.p + 1);
      return new int[]{Math.min(var4, var5), Math.max(var4, var5)};
   }

   public int b(int var1, int var2) {
      int var3 = var1 + this.X(var2);
      int var4 = this.d == (var3 % 2 == 0 ? 1 : 0) ? 1 : 0;
      return var4 != 0 ? 1 : -1;
   }

   private int X(int var1) {
      return (int)((long)this.s() * var1 % 2L);
   }

   public long u(int var1, int var2, int var3, int var4) {
      int var5 = this.d(var1, var3, var4);
      int var6 = this.x ? var1 : var3;
      int var7 = this.b(var5, var4);
      int var8 = var7 > 0 ? var6 - this.q() : this.J() - var6;
      int var9 = this.C.maxY - var2;
      return (long)var5 << 40 | (long)var8 << 12 | Math.min(var9, 4095);
   }

   public boolean I(int var1, int var2) {
      int[] var3 = this.E(var1, var2);
      u1.b var4 = this.J(var2);
      return var3[1] - var3[0] == 2 && var4.height() == 3;
   }

   public int p(int var1, int var2) {
      int[] var3 = this.E(var1, var2);
      return (var3[0] + var3[1]) / 2;
   }

   public static int[] K(String var0) {
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

   public static String j(int var0, int var1, int var2) {
      return var0 + " " + var1 + " " + var2;
   }

   public static boolean W(int var0, int var1, int var2) {
      return var0 - (var1 + 1) <= var2;
   }

   public record b(int index, int bottom, int top) {
      public int height() {
         return this.top - this.bottom + 1;
      }

      public boolean contains(int var1) {
         return var1 >= this.bottom && var1 <= this.top;
      }
   }

   public record y(int minX, int minY, int minZ, int maxX, int maxY, int maxZ) {
      public static u1.y of(int[] var0, int[] var1) {
         return new u1.y(
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
