package com.larpclient.module.misc;

import java.math.BigDecimal;
import java.util.Locale;
import java.util.Objects;

public class NumberSetting extends Setting<Double> {
   private final double min;
   private final double max;
   private final double step;
   private NumberSetting upperBound;
   private NumberSetting lowerBound;
   private String suffix;
   private boolean abbreviated;
   private static final String[] aBBREVIATIONS = new String[]{"K", "M", "B", "T"};

   public NumberSetting(String var1, String var2, double var3, double var5, double var7, double var9) {
      super(var1, var2, var3);
      this.min = var5;
      this.max = var7;
      this.step = var9;
   }

   public void getDisplayValue(Double var1) {
      if (var1 != null && Double.isFinite(var1)) {
         var1 = Math.max(this.min, Math.min(this.max, var1));
         var1 = this.min + Math.round((var1 - this.min) / this.step) * this.step;
         var1 = Math.round(var1 * 1000.0) / 1000.0;
         super.setValue(Math.clamp(var1, this.min, this.max));
      } else {
         throw new IllegalArgumentException("Non-finite number");
      }
   }

   public NumberSetting abbreviated() {
      this.abbreviated = (boolean)1;
      return this;
   }

   public boolean isAbbreviated() {
      return this.abbreviated;
   }

   public static String formatAmount(double var0) {
      BigDecimal var2 = BigDecimal.valueOf(var0).stripTrailingZeros();

      for (int var3 = aBBREVIATIONS.length - 1; var3 >= 0; var3--) {
         BigDecimal var4 = var2.movePointLeft(3 * (var3 + 1)).stripTrailingZeros();
         if (var4.abs().compareTo(BigDecimal.ONE) >= 0 && var4.scale() <= 2) {
            return var4.toPlainString() + aBBREVIATIONS[var3];
         }
      }

      return var2.scale() < 0 ? var2.setScale(0).toPlainString() : var2.toPlainString();
   }

   public static double formatAmount(String var0) {
      String var1 = var0.strip().replace(",", "").replace("_", "").replace(" ", "").replace("$", "").toUpperCase(Locale.ROOT);
      if (var1.isEmpty()) {
         throw new NumberFormatException("Empty amount");
      }

      int var2 = 0;
      char var3 = var1.charAt(var1.length() - 1);

      for (int var4 = 0; var4 < aBBREVIATIONS.length; var4++) {
         if (var3 == aBBREVIATIONS[var4].charAt(0)) {
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

   public NumberSetting linkUpperBound(NumberSetting var1) {
      if (var1 != this && var1.min == this.min && var1.max == this.max && var1.step == this.step) {
         this.upperBound = var1;
         var1.lowerBound = this;
         return this;
      } else {
         throw new IllegalArgumentException("Range bounds must share limits and step");
      }
   }

   public NumberSetting getUpperBound() {
      return this.upperBound;
   }

   public boolean getUpperBound() {
      return this.lowerBound != null;
   }

   public NumberSetting setSuffix(String var1) {
      this.suffix = Objects.requireNonNull(var1);
      return this;
   }

   public boolean hasSuffix() {
      return this.suffix != null;
   }

   public String getSuffix() {
      return this.suffix;
   }

   public String getDisplayValue() {
      if (this.upperBound != null) {
         return String.format(Locale.US, "%.1f - %.1f", this.getValue(), this.upperBound.getValue());
      } else if (this.suffix != null) {
         return String.format(Locale.US, "%.1f%s", this.getValue(), this.suffix);
      } else {
         return this.abbreviated ? formatAmount(this.getValue()) : this.getValueAsString();
      }
   }

   public String getMaxDisplay() {
      if (this.upperBound != null) {
         return String.format(Locale.US, "%.1f - %.1f", this.max, this.max);
      } else {
         return this.suffix != null ? String.format(Locale.US, "%.1f%s", this.max, this.suffix) : this.getValueAsString();
      }
   }

   public double getMin() {
      return this.min;
   }

   public double getMax() {
      return this.max;
   }

   public double getStep() {
      return this.step;
   }

   public double getProgress() {
      return (this.getValue() - this.min) / (this.max - this.min);
   }

   public void setProgress(double var1) {
      this.getDisplayValue(this.min + (this.max - this.min) * Math.max(0.0, Math.min(1.0, var1)));
   }

   public String getEditValueAsString() {
      return this.abbreviated ? formatAmount(this.getValue()) : BigDecimal.valueOf(this.getValue()).stripTrailingZeros().toPlainString();
   }

   public void setFromEditString(String var1) {
      double var2 = this.abbreviated ? formatAmount(var1) : Double.parseDouble(var1);
      if (Double.compare(var2, this.getValue()) != 0) {
         this.getDisplayValue(var2);
      }
   }

   @Override
   public String getValueAsString() {
      if (this.abbreviated) {
         return formatAmount(this.getValue());
      } else {
         return this.step >= 1.0 ? String.valueOf((int)this.getValue().doubleValue()) : String.format(Locale.ROOT, "%.1f", this.getValue());
      }
   }
}
