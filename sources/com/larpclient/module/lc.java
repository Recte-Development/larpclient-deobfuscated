package com.larpclient.module;

import java.math.BigDecimal;
import java.util.Locale;
import java.util.Objects;

public class lc extends l6<Double> {
   private final double f;
   private final double T;
   private final double o;
   private lc X;
   private lc H;
   private String U;

   public lc(String var1, String var2, double var3, double var5, double var7, double var9) {
      super(var1, var2, var3);
      this.f = var5;
      this.T = var7;
      this.o = var9;
   }

   public void C(Double var1) {
      if (var1 != null && Double.isFinite(var1)) {
         var1 = Math.max(this.f, Math.min(this.T, var1));
         var1 = this.f + Math.round((var1 - this.f) / this.o) * this.o;
         var1 = Math.round(var1 * 1000.0) / 1000.0;
         super.setValue(Math.clamp(var1, this.f, this.T));
      } else {
         throw new IllegalArgumentException("Non-finite number");
      }
   }

   public lc A(lc var1) {
      if (var1 != this && var1.f == this.f && var1.T == this.T && var1.o == this.o) {
         this.X = var1;
         var1.H = this;
         return this;
      } else {
         throw new IllegalArgumentException("Range bounds must share limits and step");
      }
   }

   public lc R() {
      return this.X;
   }

   public boolean b() {
      return this.H != null;
   }

   public lc q(String var1) {
      this.U = Objects.requireNonNull(var1);
      return this;
   }

   public boolean H() {
      return this.U != null;
   }

   public String y() {
      return this.U;
   }

   public String n() {
      if (this.X != null) {
         return String.format(Locale.US, "%.1f - %.1f", this.getValue(), this.X.getValue());
      } else {
         return this.U != null ? String.format(Locale.US, "%.1f%s", this.getValue(), this.U) : this.getValueAsString();
      }
   }

   public String T() {
      if (this.X != null) {
         return String.format(Locale.US, "%.1f - %.1f", this.T, this.T);
      } else {
         return this.U != null ? String.format(Locale.US, "%.1f%s", this.T, this.U) : this.getValueAsString();
      }
   }

   public double t() {
      return this.f;
   }

   public double T() {
      return this.T;
   }

   public double P() {
      return this.o;
   }

   public double g() {
      return (this.getValue() - this.f) / (this.T - this.f);
   }

   public void o(double var1) {
      this.C(this.f + (this.T - this.f) * Math.max(0.0, Math.min(1.0, var1)));
   }

   public String getEditValueAsString() {
      return BigDecimal.valueOf(this.getValue()).stripTrailingZeros().toPlainString();
   }

   public void setFromEditString(String var1) {
      double var2 = Double.parseDouble(var1);
      if (Double.compare(var2, this.getValue()) != 0) {
         this.C(var2);
      }
   }

   @Override
   public String getValueAsString() {
      return this.o >= 1.0 ? String.valueOf((int)this.getValue().doubleValue()) : String.format(Locale.ROOT, "%.1f", this.getValue());
   }
}
