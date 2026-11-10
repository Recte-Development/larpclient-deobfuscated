package com.larpclient;

import java.util.List;
import java.util.random.RandomGenerator;

public final class sp implements td {
   private List<String> B = List.of();
   private String p = "";
   private String d = "";
   private sd X;
   private mt V;
   private String J = "";
   private int H = -1;
   private double I;
   private double k;
   private double o = 4.0;
   private boolean z;

   public boolean x(cn.j var1, long var2) {
      mt var4 = var1.playback();
      sd var5 = var1.lyrics();
      int var6 = var4 != null && var2 - var4.observedAt() > 4000000000L ? 1 : 0;
      if (var4 != this.V) {
         this.V = var4;
         this.J = var4 == null ? "" : var4.key();
      }

      String var7 = var4 != null && var6 == 0 ? this.J : "";
      int var8 = -1;
      String var9 = "";
      double var10 = var4 == null ? 0.0 : var4.positionAt(var2);
      this.k = 0.0;
      this.o = 4.0;
      if (!var7.isEmpty() && var5 != null && var5.timed() != null) {
         h1 var12 = var5.timed();
         var8 = var12.g(var10);
         var9 = var12.h(var8, var10);
         if (var8 >= 0) {
            double var13 = var12.y().get(var8).seconds();
            this.k = Math.max(0.0, var10 - var13);
            double var15 = var8 + 1 < var12.y().size() ? var12.y().get(var8 + 1).seconds() : var4.duration();
            this.o = Math.clamp(var15 - var13, 0.2, 18.0);
         }
      }

      this.z = (boolean)(this.p.equals(var7) && this.X == var5 && var8 >= this.H && var8 <= this.H + 1 && !(Math.abs(var10 - this.I) > 1.25) ? 0 : 1);
      this.I = var10;
      if (this.p.equals(var7) && this.X == var5 && this.H == var8 && this.d.equals(var9)) {
         return false;
      }

      this.p = var7;
      this.X = var5;
      this.H = var8;
      this.d = var9;
      if (var7.isEmpty() || var5 == null) {
         this.B = List.of();
      } else if (var5.timed() == null) {
         this.B = var5.plain().stream().filter(var0 -> !var0.isBlank()).limit(1L).toList();
      } else {
         this.B = var9.isBlank() ? List.of() : List.of(var9);
      }

      return true;
   }

   public double a() {
      return this.k;
   }

   public int U() {
      return this.H;
   }

   public double y() {
      return this.o;
   }

   public boolean o() {
      return this.z;
   }

   public boolean B() {
      return this.X != null && this.X.timed() != null && !this.d.isBlank();
   }

   @Override
   public String next(double var1, RandomGenerator var3) {
      return this.B.isEmpty() ? null : this.B.get(var3.nextInt(this.B.size()));
   }

   public List<String> Z() {
      return this.B;
   }

   public void h() {
      this.B = List.of();
      this.p = this.d = "";
      this.X = null;
      this.H = -1;
      this.V = null;
      this.J = "";
      this.I = this.k = 0.0;
      this.o = 4.0;
      this.z = (boolean)0;
   }
}
