package com.larpclient;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class j {
   private static final Pattern g = Pattern.compile("\\[(\\d{1,3}):(\\d{2})(?:[.:](\\d{1,3}))?]");
   private static final Pattern H = Pattern.compile("(?im)^\\[offset:([+-]?\\d+)]\\s*$");
   private final List<j.w> E;

   private j(List<j.w> var1) {
      this.E = List.copyOf(var1);
   }

   public List<j.w> t() {
      return this.E;
   }

   public static j P(String var0) {
      if (var0 != null && var0.length() <= 262144) {
         double var1 = 0.0;
         Matcher var3 = H.matcher(var0);
         if (var3.find()) {
            var1 = Math.clamp(Double.parseDouble(var3.group(1)) / 1000.0, -60.0, 60.0);
         }

         ArrayList var4 = new ArrayList();

         for (String var8 : var0.split("\\R")) {
            Matcher var9 = g.matcher(var8);
            ArrayList var10 = new ArrayList();
            int var11 = 0;

            while (var9.find()) {
               if (Integer.parseInt(var9.group(2)) < 60) {
                  double var12 = var9.group(3) == null ? 0.0 : Double.parseDouble("0." + var9.group(3));
                  var10.add(Integer.parseInt(var9.group(1)) * 60 + Integer.parseInt(var9.group(2)) + var12);
                  var11 = var9.end();
               }
            }

            String var21 = var8.substring(var11).replaceAll("<\\d{1,3}:\\d{2}(?:\\.\\d+)?>", "").replaceAll("[\\p{Cntrl}§]", "").strip();
            if (var21.codePointCount(0, var21.length()) > 240) {
               var21 = var21.substring(0, var21.offsetByCodePoints(0, 240));
            }

            for (double var14 : var10) {
               if (var4.size() >= 4096) {
                  throw new IllegalArgumentException("Too many timed lyric cues");
               }

               var4.add(new j.w(Math.max(0.0, var14 - var1), var21));
            }
         }

         var4.sort(Comparator.comparingDouble(j.w::seconds));
         ArrayList var16 = new ArrayList();

         for (j.w var18 : var4) {
            if (!var16.isEmpty() && ((j.w)var16.getLast()).seconds == var18.seconds) {
               j.w var19 = (j.w)var16.removeLast();
               String var20 = var19.text.isEmpty() ? var18.text : (var18.text.isEmpty() ? var19.text : var19.text + " / " + var18.text);
               if (var20.codePointCount(0, var20.length()) > 240) {
                  var20 = var20.substring(0, var20.offsetByCodePoints(0, 240));
               }

               var16.add(new j.w(var18.seconds, var20));
            } else {
               var16.add(var18);
            }
         }

         return new j(var16);
      } else {
         throw new IllegalArgumentException("Invalid timed lyrics size");
      }
   }

   public int N(double var1) {
      int var3 = 0;
      int var4 = this.E.size() - 1;
      int var5 = -1;

      while (var3 <= var4) {
         int var6 = var3 + var4 >>> 1;
         if (this.E.get(var6).seconds <= var1) {
            var5 = var6;
            var3 = var6 + 1;
         } else {
            var4 = var6 - 1;
         }
      }

      return var5;
   }

   public String Z(int var1, double var2) {
      if (var1 >= 0 && var1 < this.E.size()) {
         return var2 - this.E.get(var1).seconds > 18.0 ? "" : this.E.get(var1).text;
      } else {
         return "";
      }
   }

   public record w(double seconds, String text) {
   }
}
