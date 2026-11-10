package com.larpclient.module;

import java.math.BigDecimal;
import java.util.Locale;
import java.util.Objects;

public class b extends r<Double> {
   private final double q;
   private final double w;
   private final double m;
   private b J;
   private b O;
   private String W;
   private boolean Z;
   private static final String[] h = new String[]{"K", "M", "B", "T"};

   public b(String var1, String var2, double var3, double var5, double var7, double var9) {
      super(var1, var2, var3);
      this.q = var5;
      this.w = var7;
      this.m = var9;
   }

   public void O(Double var1) {
      if (var1 != null && Double.isFinite(var1)) {
         var1 = Math.max(this.q, Math.min(this.w, var1));
         var1 = this.q + Math.round((var1 - this.q) / this.m) * this.m;
         var1 = Math.round(var1 * 1000.0) / 1000.0;
         super.setValue(Math.clamp(var1, this.q, this.w));
      } else {
         throw new IllegalArgumentException("Non-finite number");
      }
   }

   public b J() {
      this.Z = (boolean)1;
      return this;
   }

   public boolean E() {
      return this.Z;
   }

   public static String q(double var0) {
      BigDecimal var2 = BigDecimal.valueOf(var0).stripTrailingZeros();

      for (int var3 = h.length - 1; var3 >= 0; var3--) {
         BigDecimal var4 = var2.movePointLeft(3 * (var3 + 1)).stripTrailingZeros();
         if (var4.abs().compareTo(BigDecimal.ONE) >= 0 && var4.scale() <= 2) {
            return var4.toPlainString() + h[var3];
         }
      }

      return var2.scale() < 0 ? var2.setScale(0).toPlainString() : var2.toPlainString();
   }

   public static double q(String var0) {
      String var1 = var0.strip().replace(",", "").replace("_", "").replace(" ", "").replace("$", "").toUpperCase(Locale.ROOT);
      if (var1.isEmpty()) {
         throw new NumberFormatException("Empty amount");
      }

      int var2 = 0;
      char var3 = var1.charAt(var1.length() - 1);

      for (int var4 = 0; var4 < h.length; var4++) {
         if (var3 == h[var4].charAt(0)) {
            var2 = 3 * (var4 + 1);
            var1 = var1.substring(0, var1.length() - 1);
         }
      }

      if (!var1.isEmpty() && var1.matches("[0-9]*\\.?[0-9]*")) {
         return new BigDecimal(var1).movePointRight(var2).doubleValue();
      } else {
         throw new NumberFormatException("Not an amount: " + var0);
      }
   }

   public b R(b var1) {
      if (var1 != this && var1.q == this.q && var1.w == this.w && var1.m == this.m) {
         this.J = var1;
         var1.O = this;
         return this;
      } else {
         throw new IllegalArgumentException("Range bounds must share limits and step");
      }
   }

   public b Y() {
      return this.J;
   }

   public boolean Y() {
      return this.O != null;
   }

   public b A(String var1) {
      this.W = Objects.requireNonNull(var1);
      return this;
   }

   public boolean w() {
      return this.W != null;
   }

   public String L() {
      return this.W;
   }

   public String O() {
      if (this.J != null) {
         return String.format(Locale.US, "%.1f - %.1f", this.getValue(), this.J.getValue());
      } else if (this.W != null) {
         return String.format(Locale.US, "%.1f%s", this.getValue(), this.W);
      } else {
         return this.Z ? q(this.getValue()) : this.getValueAsString();
      }
   }

   public String D() {
      if (this.J != null) {
         return String.format(Locale.US, "%.1f - %.1f", this.w, this.w);
      } else {
         return this.W != null ? String.format(Locale.US, "%.1f%s", this.w, this.W) : this.getValueAsString();
      }
   }

   public double e() {
      return this.q;
   }

   public double C() {
      return this.w;
   }

   public double g() {
      return this.m;
   }

   public double p() {
      return (this.getValue() - this.q) / (this.w - this.q);
   }

   public void I(double var1) {
      this.O(this.q + (this.w - this.q) * Math.max(0.0, Math.min(1.0, var1)));
   }

   public String getEditValueAsString() {
      return this.Z ? q(this.getValue()) : BigDecimal.valueOf(this.getValue()).stripTrailingZeros().toPlainString();
   }

   public void setFromEditString(String var1) {
      double var2 = this.Z ? q(var1) : Double.parseDouble(var1);
      if (Double.compare(var2, this.getValue()) != 0) {
         this.O(var2);
      }
   }

   @Override
   public String getValueAsString() {
      if (this.Z) {
         return q(this.getValue());
      } else {
         return this.m >= 1.0 ? String.valueOf((int)this.getValue().doubleValue()) : String.format(Locale.ROOT, "%.1f", this.getValue());
      }
   }
}
