package com.larpclient;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.util.StringDecomposer;

public final class fy {
   public static final BigDecimal R = new BigDecimal("10000000000000");
   private static final Pattern B = Pattern.compile(
      "(?iu)^[^\\p{L}\\p{N}$]*?(?:money\\s*[:»|]?\\s*[$＄]?\\s*|[$＄]\\s*)([0-9]+(?:,[0-9]{3})*(?:\\.[0-9]+)?[kmbt]?)\\s*$"
   );
   private static final Pattern l = Pattern.compile("(?iu)^\\s*(?:money|balance|cash|[$＄])\\s*[:»|]?\\s*[$＄]?\\s*$");
   private static final Pattern S = Pattern.compile("(?iu)^\\s*([0-9]+(?:,[0-9]{3})*(?:\\.[0-9]+)?[kmbt]?)\\s*$");
   private static final Pattern u = Pattern.compile("(?i)(?:^|[^a-z])(money|balance|cash)(?:$|[^a-z])");
   private static final Pattern D = Pattern.compile("^[\\p{Co} ]+([0-9]+(?:,[0-9]{3})*(?:\\.[0-9]+)?[kKmMbBtT]?) *$");
   private static final Pattern C = Pattern.compile("^[\\p{Co} ]+$");
   private static final Pattern n = Pattern.compile("(?imu)^\\h*[$＄]\\h*([0-9]+(?:,[0-9]{3})*(?:\\.[0-9]+)?[kmbt]?)\\h*(?=$|[·•▪|⋅●])");
   private static final Pattern G = Pattern.compile("(?iu)^\\s*You\\h+have\\h+[$＄]\\h*([0-9]+(?:,[0-9]{3})*(?:\\.[0-9]+)?[kmbt]?)\\s*$");
   private BigDecimal q = BigDecimal.ZERO;

   public void H(BigDecimal var1) {
      if (var1.signum() < 0) {
         throw new IllegalArgumentException("Negative fake balance");
      }

      this.q = var1;
   }

   public BigDecimal J() {
      return this.q;
   }

   public boolean V(BigDecimal var1) {
      if (var1.signum() <= 0) {
         throw new IllegalArgumentException("Non-positive payment");
      }

      if (var1.compareTo(this.q) > 0) {
         return false;
      }

      this.q = this.q.subtract(var1);
      return true;
   }

   public static boolean d(Component var0) {
      return l.matcher(w(var0).text()).matches();
   }

   public static boolean Z(Component var0, String var1, String var2) {
      String var3 = w(var0).text();
      return d(var0) || C.matcher(var3).matches() && (Q(var1, var2) || O(var0));
   }

   private static boolean Q(String var0, String var1) {
      return u.matcher(var0 + " " + var1).find();
   }

   public static BigDecimal e(String var0) {
      String var1 = var0 == null ? "" : var0.strip();
      if (var1.matches("\\$?0+(?:\\.0+)?[kKmMbBtT]?")) {
         return BigDecimal.ZERO;
      } else {
         w_.e var2 = w_.Y("pay Local " + var1);
         if (!var2.valid()) {
            throw new IllegalArgumentException("Enter an amount from 0 to 10T, e.g. 10T, 1B or 61.4.");
         } else if (var2.amount().compareTo(R) > 0) {
            throw new IllegalArgumentException("Fake balance must be between 0 and 10T.");
         } else {
            return var2.amount();
         }
      }
   }

   private static String y(Component var0) {
      StringBuilder var1 = new StringBuilder();
      var0.visit((var1x, var2) -> {
         StringDecomposer.iterateFormatted(var2, var1x, (var1xx, var2x, var3) -> {
            var1.appendCodePoint(var3);
            return true;
         });
         return Optional.empty();
      }, Style.EMPTY);
      return var1.toString();
   }

   public static Component f(Component var0, BigDecimal var1) {
      if (var0 == null) {
         return null;
      }

      fy.h var2 = w(var0);
      Matcher var3 = n.matcher(var2.text());
      return (Component)(!var3.find() ? var0 : R(var0, var2.positions().get(var3.start(1)), var2.positions().get(var3.end(1) - 1) + 1, w_.U(var1)));
   }

   public static Component w(Component var0, BigDecimal var1) {
      if (var0 == null) {
         return null;
      }

      fy.h var2 = w(var0);
      Matcher var3 = G.matcher(var2.text());
      return (Component)(!var3.matches() ? var0 : R(var0, var2.positions().get(var3.start(1)), var2.positions().get(var3.end(1) - 1) + 1, w_.U(var1)));
   }

   public static MutableComponent k(MutableComponent var0, BigDecimal var1) {
      fy.h var2 = w(var0);
      Matcher var3 = B.matcher(var2.text());
      return !var3.matches() ? var0 : R(var0, var2.positions().get(var3.start(1)), var2.positions().get(var3.end(1) - 1) + 1, w_.U(var1));
   }

   public static MutableComponent N(MutableComponent var0, BigDecimal var1, String var2, String var3) {
      MutableComponent var4 = k(var0, var1);
      if (var4 != var0) {
         return var4;
      } else {
         fy.h var5 = w(var0);
         boolean var6 = Q(var2, var3);
         if (var6 && S.matcher(var5.text()).matches()) {
            return C(var0, var1);
         } else {
            Matcher var7 = D.matcher(var5.text());
            if (!var7.matches()) {
               return var0;
            } else {
               return !var6 && !O(var0) ? var0 : R(var0, var5.positions().get(var7.start(1)), var5.positions().get(var7.end(1) - 1) + 1, w_.U(var1));
            }
         }
      }
   }

   private static boolean O(Component var0) {
      boolean[] var1 = new boolean[]{false};
      var0.visit((var1x, var2) -> {
         StringDecomposer.iterateFormatted(var2, var1x, (var1xx, var2x, var3) -> {
            if (Character.getType(var3) == 18 && var2x.getColor() != null) {
               int var4 = var2x.getColor().getValue();
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
      }, Style.EMPTY);
      return var1[0];
   }

   private static fy.h w(Component var0) {
      String var1 = y(var0);
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

      return new fy.h(var2.toString(), var3);
   }

   public static MutableComponent C(MutableComponent var0, BigDecimal var1) {
      if (y(var0).isBlank()) {
         return var0;
      }

      MutableComponent var2 = k(var0, var1);
      return var2 != var0 ? var2 : R(var0, 0, y(var0).length(), w_.U(var1));
   }

   private static MutableComponent R(Component var0, int var1, int var2, String var3) {
      ArrayList var4 = new ArrayList();
      int[] var5 = new int[]{0};
      var0.visit((var2x, var3x) -> {
         StringBuilder var4x = new StringBuilder();
         Style[] var5x = new Style[]{var2x};
         StringDecomposer.iterateFormatted(var3x, var2x, (var4xx, var5xx, var6x) -> {
            if (!var5x[0].equals(var5xx)) {
               e(var4, var5, var4x, var5x[0]);
               var5x[0] = var5xx;
            }

            var4x.appendCodePoint(var6x);
            return true;
         });
         e(var4, var5, var4x, var5x[0]);
         return Optional.empty();
      }, Style.EMPTY);
      MutableComponent var6 = Component.empty();
      byte var7 = 0;

      for (fy.i var9 : var4) {
         if (var9.start() < var1) {
            m(var6, var9, var9.start(), Math.min(var1, var9.end()));
         }

         if (var7 == 0 && var9.end() > var1) {
            var6.append(Component.literal(var3).setStyle(var9.style()));
            var7 = 1;
         }

         if (var9.end() > var2) {
            m(var6, var9, Math.max(var2, var9.start()), var9.end());
         }
      }

      return var6;
   }

   private static void m(MutableComponent var0, fy.i var1, int var2, int var3) {
      if (var3 > var2) {
         var0.append(Component.literal(var1.text().substring(var2 - var1.start(), var3 - var1.start())).setStyle(var1.style()));
      }
   }

   private static void e(List<fy.i> var0, int[] var1, StringBuilder var2, Style var3) {
      if (!var2.isEmpty()) {
         var0.add(new fy.i(var1[0], var1[0] + var2.length(), var2.toString(), var3));
         var1[0] += var2.length();
         var2.setLength(0);
      }
   }

   private record h(String text, List<Integer> positions) {
   }

   private record i(int start, int end, String text, Style style) {
   }
}
