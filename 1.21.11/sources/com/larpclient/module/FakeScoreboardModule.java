package com.larpclient.module;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import net.minecraft.class_2561;
import net.minecraft.class_2583;
import net.minecraft.class_266;
import net.minecraft.class_268;
import net.minecraft.class_269;
import net.minecraft.class_274;
import net.minecraft.class_5250;
import net.minecraft.class_5251;
import net.minecraft.class_638;
import net.minecraft.class_8646;
import net.minecraft.class_9014;
import net.minecraft.class_9015;
import net.minecraft.class_9020;
import net.minecraft.class_274.class_275;

public final class FakeScoreboardModule extends Module {
   private static final String j = "larpclient_fakesb";
   private static final String f = "larpclient_sb_";
   private static final String N = "larpclient";
   private final aj g = this.addSetting(A("Title", "Sidebar heading"));
   private final l3 l = this.addSetting(new l3("Money", "Show the money row", true));
   private final aj b = this.addSetting(A("Money Amount", "Money text; numbers like 1500 or 2.5m are shortened"));
   private final l3 X = this.addSetting(new l3("Shards", "Show the shards row", true));
   private final aj s = this.addSetting(A("Shards Amount", "Shards text"));
   private final l3 W = this.addSetting(new l3("Kills", "Show the kills row", true));
   private final aj M = this.addSetting(A("Kills Amount", "Kills text"));
   private final l3 d = this.addSetting(new l3("Deaths", "Show the deaths row", true));
   private final aj y = this.addSetting(A("Deaths Amount", "Deaths text"));
   private final l3 S = this.addSetting(new l3("Playtime", "Show the playtime row", true));
   private final aj q = this.addSetting(A("Playtime Amount", "Playtime text"));
   private final List<String> z = new ArrayList<>();
   private class_266 H;
   private long T;

   public FakeScoreboardModule() {
      super("Fake Scoreboard", "Replaces the sidebar with your own stats.", Category.PLAYER, -1);
      this.b.O(this.l::getValue);
      this.s.O(this.X::getValue);
      this.M.O(this.W::getValue);
      this.y.O(this.d::getValue);
      this.q.O(this.S::getValue);
   }

   private static aj A(String var0, String var1) {
      return new aj(var0, var1, "larpclient").A("Text");
   }

   @Override
   public Set<l5> getNetworkImpacts() {
      return Set.of(l5.E);
   }

   @Override
   public void onEnable() {
      this.T = 0L;
      this.i();
   }

   @Override
   public void onDisable() {
      class_638 var1 = getMc().field_1687;
      if (var1 != null) {
         class_269 var2 = var1.method_8428();
         this.R(var2);
         b(var2);
         var2.method_1158(class_8646.field_45157, this.H == null ? null : var2.method_1170(this.H.method_1113()));
      }

      this.z.clear();
      this.H = null;
   }

   @Override
   public void onTick() {
      long var1 = System.currentTimeMillis();
      long var5;
      int var10000 = (var5 = var1 - this.T - 1000L) == 0L ? 0 : (var5 < 0L ? -1 : 1);
      boolean var4 = false;
      int var3 = var10000;
      if ((Integer.signum(var3) >>> 31 ^ 1) != 0) {
         this.T = var1;
         this.i();
      }
   }

   private void i() {
      class_638 var1 = getMc().field_1687;
      if (var1 != null) {
         class_269 var2 = var1.method_8428();
         class_266 var3 = var2.method_1189(class_8646.field_45157);
         if (var3 != null && !"larpclient_fakesb".equals(var3.method_1113())) {
            this.H = var3;
         }

         this.R(var2);
         b(var2);
         class_266 var4 = var2.method_1168("larpclient_fakesb", class_274.field_1468, z(this.g.getValue()), class_275.field_1472, false, class_9020.field_47557);
         var2.method_1158(class_8646.field_45157, var4);
         List var5 = this.w();

         for (int var6 = 0; var6 < var5.size(); var6++) {
            String var7 = "larpclient_sb_" + var6;
            this.z.add(var7);
            class_268 var8 = var2.method_1153(var7);
            if (var8 != null) {
               var2.method_1191(var8);
            }

            class_268 var9 = var2.method_1171(var7);
            var9.method_1138((class_2561)var5.get(var6));
            String var10 = "§" + Integer.toHexString(var6);
            class_9014 var11 = var2.method_1180(class_9015.method_55422(var10), var4);
            var11.method_55410(var5.size() - var6);
            var11.method_55412(class_9020.field_47557);
            var2.method_1172(var10, var9);
         }
      }
   }

   private void R(class_269 var1) {
      for (String var3 : this.z) {
         class_268 var4 = var1.method_1153(var3);
         if (var4 != null) {
            var1.method_1191(var4);
         }
      }

      this.z.clear();
   }

   private static void b(class_269 var0) {
      class_266 var1 = var0.method_1170("larpclient_fakesb");
      if (var1 != null) {
         var0.method_1194(var1);
      }
   }

   private List<class_2561> w() {
      ArrayList var1 = new ArrayList();
      if (this.l.getValue()) {
         var1.add(g("$ ", 65280).method_10852(z(s(this.b.getValue()))));
      }

      if (this.X.getValue()) {
         var1.add(g("★ ", 10814716).method_10852(z(this.s.getValue())));
      }

      if (this.W.getValue()) {
         var1.add(g("\ud83d\udde1 ", 16711680).method_10852(z(this.M.getValue())));
      }

      if (this.d.getValue()) {
         var1.add(g("☠ ", 16545539).method_10852(z(this.y.getValue())));
      }

      if (this.S.getValue()) {
         var1.add(g("⌚ ", 16771072).method_10852(z(this.q.getValue())));
      }

      if (var1.isEmpty()) {
         var1.add(class_2561.method_43470(" "));
      }

      return var1;
   }

   private static class_5250 z(String var0) {
      return g(var0 == null ? "" : var0, 16777215);
   }

   private static class_5250 g(String var0, int var1) {
      return class_2561.method_43470(var0).method_10862(class_2583.field_24360.method_27703(class_5251.method_27717(var1)));
   }

   public static String s(String var0) {
      Double var1 = A(var0);
      return var1 == null ? var0 : W(Math.max(0.0, var1));
   }

   private static Double A(String var0) {
      if (var0 == null) {
         return null;
      }

      String var1 = var0.trim().replace("$", "").replace(",", "").replace(" ", "");
      if (var1.isEmpty()) {
         return null;
      }

      double var2 = switch (Character.toLowerCase(var1.charAt(var1.length() - 1))) {
         case 'b' -> 1.0E9;
         case 'k' -> 1000.0;
         case 'm' -> 1000000.0;
         default -> 1.0;
      };
      if (var2 != 1.0) {
         var1 = var1.substring(0, var1.length() - 1);
      }

      try {
         return Double.parseDouble(var1) * var2;
      } catch (NumberFormatException var5) {
         return null;
      }
   }

   private static String W(double var0) {
      if (var0 >= 1.0E9) {
         return m(var0 / 1.0E9) + "B";
      } else if (var0 >= 1000000.0) {
         return m(var0 / 1000000.0) + "M";
      } else {
         return var0 >= 1000.0 ? m(var0 / 1000.0) + "k" : m(var0);
      }
   }

   private static String m(double var0) {
      return var0 == Math.floor(var0) && !Double.isInfinite(var0)
         ? Long.toString((long)var0)
         : String.format(Locale.ROOT, "%.2f", var0).replaceAll("0+$", "").replaceAll("\\.$", "");
   }
}
