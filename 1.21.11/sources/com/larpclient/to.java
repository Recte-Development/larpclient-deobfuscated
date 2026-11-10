package com.larpclient;

import java.util.List;
import java.util.random.RandomGenerator;

public final class to implements d {
   private List<String> V = List.of();
   private String T = "";
   private String N = "";
   private jy F;
   private yl r;
   private String q = "";
   private int Q = -1;
   private double p;
   private double B;
   private double H = 4.0;
   private boolean Y;

   public boolean i(h0.s var1, long var2) {
      yl var4 = var1.playback();
      jy var5 = var1.lyrics();
      int var6 = var4 != null && var2 - var4.observedAt() > 4000000000L ? 1 : 0;
      if (var4 != this.r) {
         this.r = var4;
         this.q = var4 == null ? "" : var4.key();
      }

      String var7 = var4 != null && var6 == 0 ? this.q : "";
      int var8 = -1;
      String var9 = "";
      double var10 = var4 == null ? 0.0 : var4.positionAt(var2);
      this.B = 0.0;
      this.H = 4.0;
      if (!var7.isEmpty() && var5 != null && var5.timed() != null) {
         bc var12 = var5.timed();
         var8 = var12.F(var10);
         var9 = var12.K(var8, var10);
         if (var8 >= 0) {
            double var13 = var12.S().get(var8).seconds();
            this.B = Math.max(0.0, var10 - var13);
            double var15 = var8 + 1 < var12.S().size() ? var12.S().get(var8 + 1).seconds() : var4.duration();
            this.H = Math.clamp(var15 - var13, 0.2, 18.0);
         }
      }

      this.Y = (boolean)(this.T.equals(var7) && this.F == var5 && var8 >= this.Q && var8 <= this.Q + 1 && !(Math.abs(var10 - this.p) > 1.25) ? 0 : 1);
      this.p = var10;
      if (this.T.equals(var7) && this.F == var5 && this.Q == var8 && this.N.equals(var9)) {
         return false;
      }

      this.T = var7;
      this.F = var5;
      this.Q = var8;
      this.N = var9;
      if (var7.isEmpty() || var5 == null) {
         this.V = List.of();
      } else if (var5.timed() == null) {
         this.V = var5.plain().stream().filter(var0 -> !var0.isBlank()).limit(1L).toList();
      } else {
         this.V = var9.isBlank() ? List.of() : List.of(var9);
      }

      return true;
   }

   public double g() {
      return this.B;
   }

   public int W() {
      return this.Q;
   }

   public double H() {
      return this.H;
   }

   public boolean U() {
      return this.Y;
   }

   public boolean t() {
      return this.F != null && this.F.timed() != null && !this.N.isBlank();
   }

   @Override
   public String next(double var1, RandomGenerator var3) {
      return this.V.isEmpty() ? null : this.V.get(var3.nextInt(this.V.size()));
   }

   public List<String> y() {
      return this.V;
   }

   public void u() {
      this.V = List.of();
      this.T = this.N = "";
      this.F = null;
      this.Q = -1;
      this.r = null;
      this.q = "";
      this.p = this.B = 0.0;
      this.H = 4.0;
      this.Y = (boolean)0;
   }
}
