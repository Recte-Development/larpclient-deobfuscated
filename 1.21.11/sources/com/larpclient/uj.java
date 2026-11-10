package com.larpclient;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.minecraft.class_2561;
import net.minecraft.class_2583;
import net.minecraft.class_5223;
import net.minecraft.class_5250;

public final class uj {
   public static final BigDecimal y = new BigDecimal("10000000000000");
   private static final Pattern S = Pattern.compile(
      "(?iu)^[^\\p{L}\\p{N}$]*?(?:money\\s*[:»|]?\\s*[$＄]?\\s*|[$＄]\\s*)([0-9]+(?:,[0-9]{3})*(?:\\.[0-9]+)?[kmbt]?)\\s*$"
   );
   private static final Pattern f = Pattern.compile("(?iu)^\\s*(?:money|balance|cash|[$＄])\\s*[:»|]?\\s*[$＄]?\\s*$");
   private static final Pattern W = Pattern.compile("(?iu)^\\s*([0-9]+(?:,[0-9]{3})*(?:\\.[0-9]+)?[kmbt]?)\\s*$");
   private static final Pattern C = Pattern.compile("(?i)(?:^|[^a-z])(money|balance|cash)(?:$|[^a-z])");
   private static final Pattern Y = Pattern.compile("^[\\p{Co} ]+([0-9]+(?:,[0-9]{3})*(?:\\.[0-9]+)?[kKmMbBtT]?) *$");
   private static final Pattern X = Pattern.compile("^[\\p{Co} ]+$");
   private static final Pattern N = Pattern.compile("(?imu)^\\h*[$＄]\\h*([0-9]+(?:,[0-9]{3})*(?:\\.[0-9]+)?[kmbt]?)\\h*(?=$|[·•▪|⋅●])");
   private static final Pattern w = Pattern.compile("(?iu)^\\s*You\\h+have\\h+[$＄]\\h*([0-9]+(?:,[0-9]{3})*(?:\\.[0-9]+)?[kmbt]?)\\s*$");
   private BigDecimal n = BigDecimal.ZERO;

   public void c(BigDecimal var1) {
      if (var1.signum() < 0) {
         throw new IllegalArgumentException("Negative fake balance");
      }

      this.n = var1;
   }

   public BigDecimal Z() {
      return this.n;
   }

   public boolean i(BigDecimal var1) {
      if (var1.signum() <= 0) {
         throw new IllegalArgumentException("Non-positive payment");
      }

      if (var1.compareTo(this.n) > 0) {
         return false;
      }

      this.n = this.n.subtract(var1);
      return true;
   }

   public static boolean F(class_2561 var0) {
      return f.matcher(v(var0).text()).matches();
   }

   public static boolean A(class_2561 var0, String var1, String var2) {
      String var3 = v(var0).text();
      return F(var0) || X.matcher(var3).matches() && (I(var1, var2) || P(var0));
   }

   private static boolean I(String var0, String var1) {
      return C.matcher(var0 + " " + var1).find();
   }

   public static BigDecimal Z(String var0) {
      String var1 = var0 == null ? "" : var0.strip();
      if (var1.matches("\\$?0+(?:\\.0+)?[kKmMbBtT]?")) {
         return BigDecimal.ZERO;
      } else {
         qy.b var2 = qy.j("pay Local " + var1);
         if (!var2.valid()) {
            throw new IllegalArgumentException("Enter an amount from 0 to 10T, e.g. 10T, 1B or 61.4.");
         } else if (var2.amount().compareTo(y) > 0) {
            throw new IllegalArgumentException("Fake balance must be between 0 and 10T.");
         } else {
            return var2.amount();
         }
      }
   }

   private static String V(class_2561 var0) {
      StringBuilder var1 = new StringBuilder();
      var0.method_27658((var1x, var2) -> {
         class_5223.method_27479(var2, var1x, (var1xx, var2x, var3) -> {
            var1.appendCodePoint(var3);
            return true;
         });
         return Optional.empty();
      }, class_2583.field_24360);
      return var1.toString();
   }

   public static class_2561 h(class_2561 var0, BigDecimal var1) {
      if (var0 == null) {
         return null;
      }

      uj.y var2 = v(var0);
      Matcher var3 = N.matcher(var2.text());
      return (class_2561)(!var3.find() ? var0 : e(var0, var2.positions().get(var3.start(1)), var2.positions().get(var3.end(1) - 1) + 1, qy.r(var1)));
   }

   public static class_2561 a(class_2561 var0, BigDecimal var1) {
      if (var0 == null) {
         return null;
      }

      uj.y var2 = v(var0);
      Matcher var3 = w.matcher(var2.text());
      return (class_2561)(!var3.matches() ? var0 : e(var0, var2.positions().get(var3.start(1)), var2.positions().get(var3.end(1) - 1) + 1, qy.r(var1)));
   }

   public static class_5250 A(class_5250 var0, BigDecimal var1) {
      uj.y var2 = v(var0);
      Matcher var3 = S.matcher(var2.text());
      return !var3.matches() ? var0 : e(var0, var2.positions().get(var3.start(1)), var2.positions().get(var3.end(1) - 1) + 1, qy.r(var1));
   }

   public static class_5250 h(class_5250 var0, BigDecimal var1, String var2, String var3) {
      class_5250 var4 = A(var0, var1);
      if (var4 != var0) {
         return var4;
      } else {
         uj.y var5 = v(var0);
         boolean var6 = I(var2, var3);
         if (var6 && W.matcher(var5.text()).matches()) {
            return U(var0, var1);
         } else {
            Matcher var7 = Y.matcher(var5.text());
            if (!var7.matches()) {
               return var0;
            } else {
               return !var6 && !P(var0) ? var0 : e(var0, var5.positions().get(var7.start(1)), var5.positions().get(var7.end(1) - 1) + 1, qy.r(var1));
            }
         }
      }
   }

   private static boolean P(class_2561 var0) {
      boolean[] var1 = new boolean[]{false};
      var0.method_27658((var1x, var2) -> {
         class_5223.method_27479(var2, var1x, (var1xx, var2x, var3) -> {
            if (Character.getType(var3) == 18 && var2x.method_10973() != null) {
               int var4 = var2x.method_10973().method_27716();
               int var5 = var4 >> 16 & 0xFF;
               int var6 = var4 >> 8 & 0xFF;
               int var7 = var4 & 0xFF;
               if (var6 >= 120 && var6 > var5 * 1.3 && var6 > var7 * 1.3) {
                  var1[0] = (boolean)1;
               }
            }

            return true;
         });
         return Optional.empty();
      }, class_2583.field_24360);
      return var1[0];
   }

   private static uj.y v(class_2561 var0) {
      String var1 = V(var0);
      StringBuilder var2 = new StringBuilder();
      ArrayList var3 = new ArrayList();

      for (int var4 = 0; var4 < var1.length(); var4++) {
         char var5 = var1.charAt(var4);
         if (Character.getType(var5) != 16) {
            if (Character.isSpaceChar(var5)) {
               var5 = ' ';
            }
            var5 = switch (var5) {
               case 'ɴ' -> 'n';
               case 'ʏ' -> 'y';
               case 'ᴇ' -> 'e';
               case 'ᴍ' -> 'm';
               case 'ᴏ' -> 'o';
               default -> var5;
            };
            var2.append(var5);
            var3.add(var4);
         }
      }

      return new uj.y(var2.toString(), var3);
   }

   public static class_5250 U(class_5250 var0, BigDecimal var1) {
      if (V(var0).isBlank()) {
         return var0;
      }

      class_5250 var2 = A(var0, var1);
      return var2 != var0 ? var2 : e(var0, 0, V(var0).length(), qy.r(var1));
   }

   private static class_5250 e(class_2561 var0, int var1, int var2, String var3) {
      ArrayList var4 = new ArrayList();
      int[] var5 = new int[]{0};
      var0.method_27658((var2x, var3x) -> {
         StringBuilder var4x = new StringBuilder();
         class_2583[] var5x = new class_2583[]{var2x};
         class_5223.method_27479(var3x, var2x, (var4xx, var5xx, var6x) -> {
            if (!var5x[0].equals(var5xx)) {
               Y(var4, var5, var4x, var5x[0]);
               var5x[0] = var5xx;
            }

            var4x.appendCodePoint(var6x);
            return true;
         });
         Y(var4, var5, var4x, var5x[0]);
         return Optional.empty();
      }, class_2583.field_24360);
      class_5250 var6 = class_2561.method_43473();
      byte var7 = 0;

      for (uj.l var9 : var4) {
         if (var9.start() < var1) {
            b(var6, var9, var9.start(), Math.min(var1, var9.end()));
         }

         if (var7 == 0 && var9.end() > var1) {
            var6.method_10852(class_2561.method_43470(var3).method_10862(var9.style()));
            var7 = 1;
         }

         if (var9.end() > var2) {
            b(var6, var9, Math.max(var2, var9.start()), var9.end());
         }
      }

      return var6;
   }

   private static void b(class_5250 var0, uj.l var1, int var2, int var3) {
      if (var3 > var2) {
         var0.method_10852(class_2561.method_43470(var1.text().substring(var2 - var1.start(), var3 - var1.start())).method_10862(var1.style()));
      }
   }

   private static void Y(List<uj.l> var0, int[] var1, StringBuilder var2, class_2583 var3) {
      if (!var2.isEmpty()) {
         var0.add(new uj.l(var1[0], var1[0] + var2.length(), var2.toString(), var3));
         var1[0] += var2.length();
         var2.setLength(0);
      }
   }

   private record l(int start, int end, String text, class_2583 style) {
   }

   private record y(String text, List<Integer> positions) {
   }
}
