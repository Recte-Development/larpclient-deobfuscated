package com.larpclient;

import java.util.List;
import java.util.random.RandomGenerator;

public final class or implements ok {
   private List<String> t = List.of();
   private String v = "";
   private String n = "";
   private jx j;
   private p6 i;
   private String S = "";
   private int Z = -1;
   private double M;
   private double N;
   private double e = 4.0;
   private boolean p;

   public boolean N(ov.i var1, long var2) {
      p6 var4 = var1.playback();
      jx var5 = var1.lyrics();
      int var6 = var4 != null && var2 - var4.observedAt() > 4000000000L ? 1 : 0;
      if (var4 != this.i) {
         this.i = var4;
         this.S = var4 == null ? "" : var4.key();
      }

      String var7 = var4 != null && var6 == 0 ? this.S : "";
      int var8 = -1;
      String var9 = "";
      double var10 = var4 == null ? 0.0 : var4.positionAt(var2);
      this.N = 0.0;
      this.e = 4.0;
      if (!var7.isEmpty() && var5 != null && var5.timed() != null) {
         j var12 = var5.timed();
         var8 = var12.N(var10);
         var9 = var12.Z(var8, var10);
         if (var8 >= 0) {
            double var13 = var12.t().get(var8).seconds();
            this.N = Math.max(0.0, var10 - var13);
            double var15 = var8 + 1 < var12.t().size() ? var12.t().get(var8 + 1).seconds() : var4.duration();
            this.e = Math.clamp(var15 - var13, 0.2, 18.0);
         }
      }

      this.p = (boolean)(this.v.equals(var7) && this.j == var5 && var8 >= this.Z && var8 <= this.Z + 1 && !(Math.abs(var10 - this.M) > 1.25) ? 0 : 1);
      this.M = var10;
      if (this.v.equals(var7) && this.j == var5 && this.Z == var8 && this.n.equals(var9)) {
         return false;
      }

      this.v = var7;
      this.j = var5;
      this.Z = var8;
      this.n = var9;
      if (var7.isEmpty() || var5 == null) {
         this.t = List.of();
      } else if (var5.timed() == null) {
         this.t = var5.plain().stream().filter(var0 -> !var0.isBlank()).limit(1L).toList();
      } else {
         this.t = var9.isBlank() ? List.of() : List.of(var9);
      }

      return true;
   }

   public double i() {
      return this.N;
   }

   public int B() {
      return this.Z;
   }

   public double W() {
      return this.e;
   }

   public boolean p() {
      return this.p;
   }

   public boolean I() {
      return this.j != null && this.j.timed() != null && !this.n.isBlank();
   }

   @Override
   public String next(double var1, RandomGenerator var3) {
      return this.t.isEmpty() ? null : this.t.get(var3.nextInt(this.t.size()));
   }

   public List<String> F() {
      return this.t;
   }

   public void A() {
      this.t = List.of();
      this.v = this.n = "";
      this.j = null;
      this.Z = -1;
      this.i = null;
      this.S = "";
      this.M = this.N = 0.0;
      this.e = 4.0;
      this.p = (boolean)0;
   }
}
