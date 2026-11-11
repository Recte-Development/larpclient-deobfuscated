package com.larpclient.util.misc;

import com.larpclient.social.misc.PayCommand;
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

public final class FakeBalance {
   public static final BigDecimal maxBalance = new BigDecimal("10000000000000");
   private static final Pattern moneyLinePattern = Pattern.compile(
      "(?iu)^[^\\p{L}\\p{N}$]*?(?:money\\s*[:»|]?\\s*[$＄]?\\s*|[$＄]\\s*)([0-9]+(?:,[0-9]{3})*(?:\\.[0-9]+)?[kmbt]?)\\s*$"
   );
   private static final Pattern balanceLabelPattern = Pattern.compile("(?iu)^\\s*(?:money|balance|cash|[$＄])\\s*[:»|]?\\s*[$＄]?\\s*$");
   private static final Pattern amountOnlyPattern = Pattern.compile("(?iu)^\\s*([0-9]+(?:,[0-9]{3})*(?:\\.[0-9]+)?[kmbt]?)\\s*$");
   private static final Pattern balanceWordPattern = Pattern.compile("(?i)(?:^|[^a-z])(money|balance|cash)(?:$|[^a-z])");
   private static final Pattern iconAmountPattern = Pattern.compile("^[\\p{Co} ]+([0-9]+(?:,[0-9]{3})*(?:\\.[0-9]+)?[kKmMbBtT]?) *$");
   private static final Pattern iconOnlyPattern = Pattern.compile("^[\\p{Co} ]+$");
   private static final Pattern dollarAmountPattern = Pattern.compile("(?imu)^\\h*[$＄]\\h*([0-9]+(?:,[0-9]{3})*(?:\\.[0-9]+)?[kmbt]?)\\h*(?=$|[·•▪|⋅●])");
   private static final Pattern youHavePattern = Pattern.compile("(?iu)^\\s*You\\h+have\\h+[$＄]\\h*([0-9]+(?:,[0-9]{3})*(?:\\.[0-9]+)?[kmbt]?)\\s*$");
   private BigDecimal balance = BigDecimal.ZERO;

   public void setBalance(BigDecimal var1) {
      if (var1.signum() < 0) {
         throw new IllegalArgumentException("Negative fake balance");
      }

      this.balance = var1;
   }

   public BigDecimal getBalance() {
      return this.balance;
   }

   public boolean tryPay(BigDecimal var1) {
      if (var1.signum() <= 0) {
         throw new IllegalArgumentException("Non-positive payment");
      }

      if (var1.compareTo(this.balance) > 0) {
         return false;
      }

      this.balance = this.balance.subtract(var1);
      return true;
   }

   public static boolean isBalanceLabel(Component var0) {
      return balanceLabelPattern.matcher(replaceYouHaveAmount(var0).text()).matches();
   }

   public static boolean isBalanceLabelComponent(Component var0, String var1, String var2) {
      String var3 = replaceYouHaveAmount(var0).text();
      return isBalanceLabel(var0) || iconOnlyPattern.matcher(var3).matches() && (mentionsBalance(var1, var2) || hasGreenGlyph(var0));
   }

   private static boolean mentionsBalance(String var0, String var1) {
      return balanceWordPattern.matcher(var0 + " " + var1).find();
   }

   public static BigDecimal parseAmount(String var0) {
      String var1 = var0 == null ? "" : var0.strip();
      if (var1.matches("\\$?0+(?:\\.0+)?[kKmMbBtT]?")) {
         return BigDecimal.ZERO;
      } else {
         PayCommand.ParsedPay var2 = PayCommand.parse("pay Local " + var1);
         if (!var2.valid()) {
            throw new IllegalArgumentException("Enter an amount from 0 to 10T, e.g. 10T, 1B or 61.4.");
         } else if (var2.amount().compareTo(maxBalance) > 0) {
            throw new IllegalArgumentException("Fake balance must be between 0 and 10T.");
         } else {
            return var2.amount();
         }
      }
   }

   private static String toPlainText(Component var0) {
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

   public static Component replaceDollarAmount(Component var0, BigDecimal var1) {
      if (var0 == null) {
         return null;
      }

      FakeBalance.ReplacedText var2 = replaceYouHaveAmount(var0);
      Matcher var3 = dollarAmountPattern.matcher(var2.text());
      return (Component)(!var3.find()
         ? var0
         : replaceRange(var0, var2.positions().get(var3.start(1)), var2.positions().get(var3.end(1) - 1) + 1, PayCommand.formatAmount(var1)));
   }

   public static Component replaceYouHaveAmount(Component var0, BigDecimal var1) {
      if (var0 == null) {
         return null;
      }

      FakeBalance.ReplacedText var2 = replaceYouHaveAmount(var0);
      Matcher var3 = youHavePattern.matcher(var2.text());
      return (Component)(!var3.matches()
         ? var0
         : replaceRange(var0, var2.positions().get(var3.start(1)), var2.positions().get(var3.end(1) - 1) + 1, PayCommand.formatAmount(var1)));
   }

   public static MutableComponent replaceMoneyLine(MutableComponent var0, BigDecimal var1) {
      FakeBalance.ReplacedText var2 = replaceYouHaveAmount(var0);
      Matcher var3 = moneyLinePattern.matcher(var2.text());
      return !var3.matches()
         ? var0
         : replaceRange(var0, var2.positions().get(var3.start(1)), var2.positions().get(var3.end(1) - 1) + 1, PayCommand.formatAmount(var1));
   }

   public static MutableComponent replaceBalanceComponent(MutableComponent var0, BigDecimal var1, String var2, String var3) {
      MutableComponent var4 = replaceMoneyLine(var0, var1);
      if (var4 != var0) {
         return var4;
      } else {
         FakeBalance.ReplacedText var5 = replaceYouHaveAmount(var0);
         boolean var6 = mentionsBalance(var2, var3);
         if (var6 && amountOnlyPattern.matcher(var5.text()).matches()) {
            return replaceWholeComponent(var0, var1);
         } else {
            Matcher var7 = iconAmountPattern.matcher(var5.text());
            if (!var7.matches()) {
               return var0;
            } else {
               return !var6 && !hasGreenGlyph(var0)
                  ? var0
                  : replaceRange(var0, var5.positions().get(var7.start(1)), var5.positions().get(var7.end(1) - 1) + 1, PayCommand.formatAmount(var1));
            }
         }
      }
   }

   private static boolean hasGreenGlyph(Component var0) {
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

   private static FakeBalance.ReplacedText replaceYouHaveAmount(Component var0) {
      String var1 = toPlainText(var0);
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

      return new FakeBalance.ReplacedText(var2.toString(), var3);
   }

   public static MutableComponent replaceWholeComponent(MutableComponent var0, BigDecimal var1) {
      if (toPlainText(var0).isBlank()) {
         return var0;
      }

      MutableComponent var2 = replaceMoneyLine(var0, var1);
      return var2 != var0 ? var2 : replaceRange(var0, 0, toPlainText(var0).length(), PayCommand.formatAmount(var1));
   }

   private static MutableComponent replaceRange(Component var0, int var1, int var2, String var3) {
      ArrayList var4 = new ArrayList();
      int[] var5 = new int[]{0};
      var0.visit((var2x, var3x) -> {
         StringBuilder var4x = new StringBuilder();
         Style[] var5x = new Style[]{var2x};
         StringDecomposer.iterateFormatted(var3x, var2x, (var4xx, var5xx, var6x) -> {
            if (!var5x[0].equals(var5xx)) {
               parseAmount(var4, var5, var4x, var5x[0]);
               var5x[0] = var5xx;
            }

            var4x.appendCodePoint(var6x);
            return true;
         });
         parseAmount(var4, var5, var4x, var5x[0]);
         return Optional.empty();
      }, Style.EMPTY);
      MutableComponent var6 = Component.empty();
      byte var7 = 0;

      for (FakeBalance.StyledSpan var9 : var4) {
         if (var9.start() < var1) {
            appendSlice(var6, var9, var9.start(), Math.min(var1, var9.end()));
         }

         if (var7 == 0 && var9.end() > var1) {
            var6.append(Component.literal(var3).setStyle(var9.style()));
            var7 = 1;
         }

         if (var9.end() > var2) {
            appendSlice(var6, var9, Math.max(var2, var9.start()), var9.end());
         }
      }

      return var6;
   }

   private static void appendSlice(MutableComponent var0, FakeBalance.StyledSpan var1, int var2, int var3) {
      if (var3 > var2) {
         var0.append(Component.literal(var1.text().substring(var2 - var1.start(), var3 - var1.start())).setStyle(var1.style()));
      }
   }

   private static void parseAmount(List<FakeBalance.StyledSpan> var0, int[] var1, StringBuilder var2, Style var3) {
      if (!var2.isEmpty()) {
         var0.add(new FakeBalance.StyledSpan(var1[0], var1[0] + var2.length(), var2.toString(), var3));
         var1[0] += var2.length();
         var2.setLength(0);
      }
   }

   private record ReplacedText(String text, List<Integer> positions) {
   }

   private record StyledSpan(int start, int end, String text, Style style) {
   }
}
