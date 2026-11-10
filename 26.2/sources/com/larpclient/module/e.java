package com.larpclient.module;

import java.math.BigDecimal;
import java.util.Locale;
import java.util.Objects;

public class e extends q<Double> {
   private final double c;
   private final double q;
   private final double T;
   private e x;
   private e f;
   private String F;
   private boolean O;
   private static final String[] e = new String[]{"K", "M", "B", "T"};

   public e(String var1, String var2, double var3, double var5, double var7, double var9) {
      super(var1, var2, var3);
      this.c = var5;
      this.q = var7;
      this.T = var9;
   }

   public void X(Double var1) {
      if (var1 != null && Double.isFinite(var1)) {
         var1 = Math.max(this.c, Math.min(this.q, var1));
         var1 = this.c + Math.round((var1 - this.c) / this.T) * this.T;
         var1 = Math.round(var1 * 1000.0) / 1000.0;
         super.setValue(Math.clamp(var1, this.c, this.q));
      } else {
         throw new IllegalArgumentException("Non-finite number");
      }
   }

   public e h() {
      this.O = (boolean)1;
      return this;
   }

   public boolean L() {
      return this.O;
   }

   public static String k(double var0) {
      BigDecimal var2 = BigDecimal.valueOf(var0).stripTrailingZeros();

      for (int var3 = e.length - 1; var3 >= 0; var3--) {
         BigDecimal var4 = var2.movePointLeft(3 * (var3 + 1)).stripTrailingZeros();
         if (var4.abs().compareTo(BigDecimal.ONE) >= 0 && var4.scale() <= 2) {
            return var4.toPlainString() + e[var3];
         }
      }

      return var2.scale() < 0 ? var2.setScale(0).toPlainString() : var2.toPlainString();
   }

   public static double E(String var0) {
      String var1 = var0.strip().replace(",", "").replace("_", "").replace(" ", "").replace("$", "").toUpperCase(Locale.ROOT);
      if (var1.isEmpty()) {
         throw new NumberFormatException("Empty amount");
      }

      int var2 = 0;
      char var3 = var1.charAt(var1.length() - 1);

      for (int var4 = 0; var4 < e.length; var4++) {
         if (var3 == e[var4].charAt(0)) {
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

   public e C(e var1) {
      if (var1 != this && var1.c == this.c && var1.q == this.q && var1.T == this.T) {
         this.x = var1;
         var1.f = this;
         return this;
      } else {
         throw new IllegalArgumentException("Range bounds must share limits and step");
      }
   }

   public e w() {
      return this.x;
   }

   public boolean N() {
      return this.f != null;
   }

   public e V(String var1) {
      this.F = Objects.requireNonNull(var1);
      return this;
   }

   public boolean M() {
      return this.F != null;
   }

   public String J() {
      return this.F;
   }

   public String i() {
      if (this.x != null) {
         return String.format(Locale.US, "%.1f - %.1f", this.getValue(), this.x.getValue());
      } else if (this.F != null) {
         return String.format(Locale.US, "%.1f%s", this.getValue(), this.F);
      } else {
         return this.O ? k(this.getValue()) : this.getValueAsString();
      }
   }

   public String W() {
      if (this.x != null) {
         return String.format(Locale.US, "%.1f - %.1f", this.q, this.q);
      } else {
         return this.F != null ? String.format(Locale.US, "%.1f%s", this.q, this.F) : this.getValueAsString();
      }
   }

   public double e() {
      return this.c;
   }

   public double m() {
      return this.q;
   }

   public double L() {
      return this.T;
   }

   public double I() {
      return (this.getValue() - this.c) / (this.q - this.c);
   }

   public void B(double var1) {
      this.X(this.c + (this.q - this.c) * Math.max(0.0, Math.min(1.0, var1)));
   }

   public String getEditValueAsString() {
      return this.O ? k(this.getValue()) : BigDecimal.valueOf(this.getValue()).stripTrailingZeros().toPlainString();
   }

   public void setFromEditString(String var1) {
      double var2 = this.O ? E(var1) : Double.parseDouble(var1);
      if (Double.compare(var2, this.getValue()) != 0) {
         this.X(var2);
      }
   }

   @Override
   public String getValueAsString() {
      if (this.O) {
         return k(this.getValue());
      } else {
         return this.T >= 1.0 ? String.valueOf((int)this.getValue().doubleValue()) : String.format(Locale.ROOT, "%.1f", this.getValue());
      }
   }
}
