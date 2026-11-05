package com.larpclient;

import java.util.List;
import java.util.random.RandomGenerator;

public final class t9 implements oy {
   private List<String> s = List.of();
   private String S = "";
   private String V = "";
   private r K;
   private ha l;
   private String N = "";
   private int r = -1;
   private double z;
   private double U;
   private double o = 4.0;
   private boolean b;

   public boolean K(wj.h var1, long var2) {
      ha var4 = var1.playback();
      r var5 = var1.lyrics();
      int var6 = var4 != null && var2 - var4.observedAt() > 4000000000L ? 1 : 0;
      if (var4 != this.l) {
         this.l = var4;
         this.N = var4 == null ? "" : var4.key();
      }

      String var7 = var4 != null && var6 == 0 ? this.N : "";
      int var8 = -1;
      String var9 = "";
      double var10 = var4 == null ? 0.0 : var4.positionAt(var2);
      this.U = 0.0;
      this.o = 4.0;
      if (!var7.isEmpty() && var5 != null && var5.timed() != null) {
         e6 var12 = var5.timed();
         var8 = var12.M(var10);
         var9 = var12.W(var8, var10);
         if (var8 >= 0) {
            double var13 = var12.K().get(var8).seconds();
            this.U = Math.max(0.0, var10 - var13);
            double var15 = var8 + 1 < var12.K().size() ? var12.K().get(var8 + 1).seconds() : var4.duration();
            this.o = Math.clamp(var15 - var13, 0.2, 18.0);
         }
      }

      this.b = (boolean)(this.S.equals(var7) && this.K == var5 && var8 >= this.r && var8 <= this.r + 1 && !(Math.abs(var10 - this.z) > 1.25) ? 0 : 1);
      this.z = var10;
      if (this.S.equals(var7) && this.K == var5 && this.r == var8 && this.V.equals(var9)) {
         return false;
      }

      this.S = var7;
      this.K = var5;
      this.r = var8;
      this.V = var9;
      if (var7.isEmpty() || var5 == null) {
         this.s = List.of();
      } else if (var5.timed() == null) {
         this.s = var5.plain().stream().filter(var0 -> !var0.isBlank()).limit(1L).toList();
      } else {
         this.s = var9.isBlank() ? List.of() : List.of(var9);
      }

      return true;
   }

   public double s() {
      return this.U;
   }

   public int P() {
      return this.r;
   }

   public double y() {
      return this.o;
   }

   public boolean Q() {
      return this.b;
   }

   public boolean n() {
      return this.K != null && this.K.timed() != null && !this.V.isBlank();
   }

   @Override
   public String next(double var1, RandomGenerator var3) {
      return this.s.isEmpty() ? null : this.s.get(var3.nextInt(this.s.size()));
   }

   public List<String> Y() {
      return this.s;
   }

   public void n() {
      this.s = List.of();
      this.S = this.V = "";
      this.K = null;
      this.r = -1;
      this.l = null;
      this.N = "";
      this.z = this.U = 0.0;
      this.o = 4.0;
      this.b = (boolean)0;
   }
}
