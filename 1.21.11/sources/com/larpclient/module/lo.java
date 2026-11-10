package com.larpclient.module;

import java.math.BigDecimal;
import java.util.Locale;
import java.util.Objects;

public class lo extends lb<Double> {
   private final double o;
   private final double s;
   private final double Y;
   private lo x;
   private lo n;
   private String p;
   private boolean z;
   private static final String[] U = new String[]{"K", "M", "B", "T"};

   public lo(String var1, String var2, double var3, double var5, double var7, double var9) {
      super(var1, var2, var3);
      this.o = var5;
      this.s = var7;
      this.Y = var9;
   }

   public void q(Double var1) {
      if (var1 != null && Double.isFinite(var1)) {
         var1 = Math.max(this.o, Math.min(this.s, var1));
         var1 = this.o + Math.round((var1 - this.o) / this.Y) * this.Y;
         var1 = Math.round(var1 * 1000.0) / 1000.0;
         super.setValue(Math.clamp(var1, this.o, this.s));
      } else {
         throw new IllegalArgumentException("Non-finite number");
      }
   }

   public lo j() {
      this.z = (boolean)1;
      return this;
   }

   public boolean L() {
      return this.z;
   }

   public static String D(double var0) {
      BigDecimal var2 = BigDecimal.valueOf(var0).stripTrailingZeros();

      for (int var3 = U.length - 1; var3 >= 0; var3--) {
         BigDecimal var4 = var2.movePointLeft(3 * (var3 + 1)).stripTrailingZeros();
         if (var4.abs().compareTo(BigDecimal.ONE) >= 0 && var4.scale() <= 2) {
            return var4.toPlainString() + U[var3];
         }
      }

      return var2.scale() < 0 ? var2.setScale(0).toPlainString() : var2.toPlainString();
   }

   public static double h(String var0) {
      String var1 = var0.strip().replace(",", "").replace("_", "").replace(" ", "").replace("$", "").toUpperCase(Locale.ROOT);
      if (var1.isEmpty()) {
         throw new NumberFormatException("Empty amount");
      }

      int var2 = 0;
      char var3 = var1.charAt(var1.length() - 1);

      for (int var4 = 0; var4 < U.length; var4++) {
         if (var3 == U[var4].charAt(0)) {
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

   public lo m(lo var1) {
      if (var1 != this && var1.o == this.o && var1.s == this.s && var1.Y == this.Y) {
         this.x = var1;
         var1.n = this;
         return this;
      } else {
         throw new IllegalArgumentException("Range bounds must share limits and step");
      }
   }

   public lo k() {
      return this.x;
   }

   public boolean m() {
      return this.n != null;
   }

   public lo m(String var1) {
      this.p = Objects.requireNonNull(var1);
      return this;
   }

   public boolean S() {
      return this.p != null;
   }

   public String Q() {
      return this.p;
   }

   public String r() {
      if (this.x != null) {
         return String.format(Locale.US, "%.1f - %.1f", this.getValue(), this.x.getValue());
      } else if (this.p != null) {
         return String.format(Locale.US, "%.1f%s", this.getValue(), this.p);
      } else {
         return this.z ? D(this.getValue()) : this.getValueAsString();
      }
   }

   public String I() {
      if (this.x != null) {
         return String.format(Locale.US, "%.1f - %.1f", this.s, this.s);
      } else {
         return this.p != null ? String.format(Locale.US, "%.1f%s", this.s, this.p) : this.getValueAsString();
      }
   }

   public double A() {
      return this.o;
   }

   public double G() {
      return this.s;
   }

   public double J() {
      return this.Y;
   }

   public double L() {
      return (this.getValue() - this.o) / (this.s - this.o);
   }

   public void a(double var1) {
      this.q(this.o + (this.s - this.o) * Math.max(0.0, Math.min(1.0, var1)));
   }

   public String getEditValueAsString() {
      return this.z ? D(this.getValue()) : BigDecimal.valueOf(this.getValue()).stripTrailingZeros().toPlainString();
   }

   public void setFromEditString(String var1) {
      double var2 = this.z ? h(var1) : Double.parseDouble(var1);
      if (Double.compare(var2, this.getValue()) != 0) {
         this.q(var2);
      }
   }

   @Override
   public String getValueAsString() {
      if (this.z) {
         return D(this.getValue());
      } else {
         return this.Y >= 1.0 ? String.valueOf((int)this.getValue().doubleValue()) : String.format(Locale.ROOT, "%.1f", this.getValue());
      }
   }
}
