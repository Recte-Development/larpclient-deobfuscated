package com.larpclient.module;

import java.math.BigDecimal;
import java.util.Locale;
import java.util.Objects;

public class d9 extends df<Double> {
   private final double y;
   private final double X;
   private final double t;
   private d9 K;
   private d9 h;
   private String S;

   public d9(String var1, String var2, double var3, double var5, double var7, double var9) {
      super(var1, var2, var3);
      this.y = var5;
      this.X = var7;
      this.t = var9;
   }

   public void o(Double var1) {
      if (var1 != null && Double.isFinite(var1)) {
         var1 = Math.max(this.y, Math.min(this.X, var1));
         var1 = this.y + Math.round((var1 - this.y) / this.t) * this.t;
         var1 = Math.round(var1 * 1000.0) / 1000.0;
         super.setValue(Math.clamp(var1, this.y, this.X));
      } else {
         throw new IllegalArgumentException("Non-finite number");
      }
   }

   public d9 V(d9 var1) {
      if (var1 != this && var1.y == this.y && var1.X == this.X && var1.t == this.t) {
         this.K = var1;
         var1.h = this;
         return this;
      } else {
         throw new IllegalArgumentException("Range bounds must share limits and step");
      }
   }

   public d9 m() {
      return this.K;
   }

   public boolean q() {
      return this.h != null;
   }

   public d9 T(String var1) {
      this.S = Objects.requireNonNull(var1);
      return this;
   }

   public boolean Z() {
      return this.S != null;
   }

   public String w() {
      return this.S;
   }

   public String b() {
      if (this.K != null) {
         return String.format(Locale.US, "%.1f - %.1f", this.P(), this.K.P());
      } else {
         return this.S != null ? String.format(Locale.US, "%.1f%s", this.P(), this.S) : this.getValueAsString();
      }
   }

   public String h() {
      if (this.K != null) {
         return String.format(Locale.US, "%.1f - %.1f", this.X, this.X);
      } else {
         return this.S != null ? String.format(Locale.US, "%.1f%s", this.X, this.S) : this.getValueAsString();
      }
   }

   public double v() {
      return this.y;
   }

   public double J() {
      return this.X;
   }

   public double Q() {
      return this.t;
   }

   public double i() {
      return (this.P() - this.y) / (this.X - this.y);
   }

   public void O(double var1) {
      this.o(this.y + (this.X - this.y) * Math.max(0.0, Math.min(1.0, var1)));
   }

   public String getEditValueAsString() {
      return BigDecimal.valueOf(this.P()).stripTrailingZeros().toPlainString();
   }

   public void setFromEditString(String var1) {
      double var2 = Double.parseDouble(var1);
      if (Double.compare(var2, this.P()) != 0) {
         this.o(var2);
      }
   }

   @Override
   public String getValueAsString() {
      return this.t >= 1.0 ? String.valueOf((int)this.P().doubleValue()) : String.format(Locale.ROOT, "%.1f", this.P());
   }
}
