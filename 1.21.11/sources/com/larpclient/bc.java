package com.larpclient;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class bc {
   private static final Pattern O = Pattern.compile("\\[(\\d{1,3}):(\\d{2})(?:[.:](\\d{1,3}))?]");
   private static final Pattern l = Pattern.compile("(?im)^\\[offset:([+-]?\\d+)]\\s*$");
   private final List<bc.p> r;

   private bc(List<bc.p> var1) {
      this.r = List.copyOf(var1);
   }

   public List<bc.p> S() {
      return this.r;
   }

   public static bc p(String var0) {
      if (var0 != null && var0.length() <= 262144) {
         double var1 = 0.0;
         Matcher var3 = l.matcher(var0);
         if (var3.find()) {
            var1 = Math.clamp(Double.parseDouble(var3.group(1)) / 1000.0, -60.0, 60.0);
         }

         ArrayList var4 = new ArrayList();

         for (String var8 : var0.split("\\R")) {
            Matcher var9 = O.matcher(var8);
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

               var4.add(new bc.p(Math.max(0.0, var14 - var1), var21));
            }
         }

         var4.sort(Comparator.comparingDouble(bc.p::seconds));
         ArrayList var16 = new ArrayList();

         for (bc.p var18 : var4) {
            if (!var16.isEmpty() && ((bc.p)var16.getLast()).seconds == var18.seconds) {
               bc.p var19 = (bc.p)var16.removeLast();
               String var20 = var19.text.isEmpty() ? var18.text : (var18.text.isEmpty() ? var19.text : var19.text + " / " + var18.text);
               if (var20.codePointCount(0, var20.length()) > 240) {
                  var20 = var20.substring(0, var20.offsetByCodePoints(0, 240));
               }

               var16.add(new bc.p(var18.seconds, var20));
            } else {
               var16.add(var18);
            }
         }

         return new bc(var16);
      } else {
         throw new IllegalArgumentException("Invalid timed lyrics size");
      }
   }

   public int F(double var1) {
      int var3 = 0;
      int var4 = this.r.size() - 1;
      int var5 = -1;

      while (var3 <= var4) {
         int var6 = var3 + var4 >>> 1;
         if (this.r.get(var6).seconds <= var1) {
            var5 = var6;
            var3 = var6 + 1;
         } else {
            var4 = var6 - 1;
         }
      }

      return var5;
   }

   public String K(int var1, double var2) {
      if (var1 >= 0 && var1 < this.r.size()) {
         return var2 - this.r.get(var1).seconds > 18.0 ? "" : this.r.get(var1).text;
      } else {
         return "";
      }
   }

   public record p(double seconds, String text) {
   }
}
