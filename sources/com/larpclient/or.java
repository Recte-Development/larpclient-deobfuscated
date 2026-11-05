package com.larpclient;

import java.util.List;
import java.util.random.RandomGenerator;

public final class or implements fh {
   private List<String> e = List.of();
   private String p = "";
   private String I = "";
   private h2 i;
   private _k l;
   private String j = "";
   private int h = -1;
   private double P;
   private double V;
   private double O = 4.0;
   private boolean M;

   public boolean a(oc.k var1, long var2) {
      _k var4 = var1.playback();
      h2 var5 = var1.lyrics();
      int var6 = var4 != null && var2 - var4.observedAt() > 4000000000L ? 1 : 0;
      if (var4 != this.l) {
         this.l = var4;
         this.j = var4 == null ? "" : var4.key();
      }

      String var7 = var4 != null && var6 == 0 ? this.j : "";
      int var8 = -1;
      String var9 = "";
      double var10 = var4 == null ? 0.0 : var4.positionAt(var2);
      this.V = 0.0;
      this.O = 4.0;
      if (!var7.isEmpty() && var5 != null && var5.timed() != null) {
         y6 var12 = var5.timed();
         var8 = var12.Q(var10);
         var9 = var12.Y(var8, var10);
         if (var8 >= 0) {
            double var13 = var12.w().get(var8).seconds();
            this.V = Math.max(0.0, var10 - var13);
            double var15 = var8 + 1 < var12.w().size() ? var12.w().get(var8 + 1).seconds() : var4.duration();
            this.O = Math.clamp(var15 - var13, 0.2, 18.0);
         }
      }

      this.M = (boolean)(this.p.equals(var7) && this.i == var5 && var8 >= this.h && var8 <= this.h + 1 && !(Math.abs(var10 - this.P) > 1.25) ? 0 : 1);
      this.P = var10;
      if (this.p.equals(var7) && this.i == var5 && this.h == var8 && this.I.equals(var9)) {
         return false;
      }

      this.p = var7;
      this.i = var5;
      this.h = var8;
      this.I = var9;
      if (var7.isEmpty() || var5 == null) {
         this.e = List.of();
      } else if (var5.timed() == null) {
         this.e = var5.plain().stream().filter(var0 -> !var0.isBlank()).limit(1L).toList();
      } else {
         this.e = var9.isBlank() ? List.of() : List.of(var9);
      }

      return true;
   }

   public double i() {
      return this.V;
   }

   public int U() {
      return this.h;
   }

   public double G() {
      return this.O;
   }

   public boolean r() {
      return this.M;
   }

   public boolean k() {
      return this.i != null && this.i.timed() != null && !this.I.isBlank();
   }

   @Override
   public String next(double var1, RandomGenerator var3) {
      return this.e.isEmpty() ? null : this.e.get(var3.nextInt(this.e.size()));
   }

   public List<String> X() {
      return this.e;
   }

   public void n() {
      this.e = List.of();
      this.p = this.I = "";
      this.i = null;
      this.h = -1;
      this.l = null;
      this.j = "";
      this.P = this.V = 0.0;
      this.O = 4.0;
      this.M = (boolean)0;
   }
}
