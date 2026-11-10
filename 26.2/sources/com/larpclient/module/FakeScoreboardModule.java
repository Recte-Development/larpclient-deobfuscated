package com.larpclient.module;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.network.chat.numbers.BlankFormat;
import net.minecraft.world.scores.DisplaySlot;
import net.minecraft.world.scores.Objective;
import net.minecraft.world.scores.PlayerTeam;
import net.minecraft.world.scores.ScoreAccess;
import net.minecraft.world.scores.ScoreHolder;
import net.minecraft.world.scores.Scoreboard;
import net.minecraft.world.scores.criteria.ObjectiveCriteria;
import net.minecraft.world.scores.criteria.ObjectiveCriteria.RenderType;

public final class FakeScoreboardModule extends Module {
   private static final String P = "larpclient_fakesb";
   private static final String u = "larpclient_sb_";
   private static final String H = "larpclient";
   private final g R = this.addSetting(v("Title", "Sidebar heading"));
   private final s Y = this.addSetting(new s("Money", "Show the money row", true));
   private final g m = this.addSetting(v("Money Amount", "Money text; numbers like 1500 or 2.5m are shortened"));
   private final s y = this.addSetting(new s("Shards", "Show the shards row", true));
   private final g j = this.addSetting(v("Shards Amount", "Shards text"));
   private final s Q = this.addSetting(new s("Kills", "Show the kills row", true));
   private final g l = this.addSetting(v("Kills Amount", "Kills text"));
   private final s s = this.addSetting(new s("Deaths", "Show the deaths row", true));
   private final g k = this.addSetting(v("Deaths Amount", "Deaths text"));
   private final s I = this.addSetting(new s("Playtime", "Show the playtime row", true));
   private final g O = this.addSetting(v("Playtime Amount", "Playtime text"));
   private final List<String> w = new ArrayList<>();
   private Objective e;
   private long N;

   public FakeScoreboardModule() {
      super("Fake Scoreboard", "Replaces the sidebar with your own stats.", Category.PLAYER, -1);
      this.m.P(this.Y::getValue);
      this.j.P(this.y::getValue);
      this.l.P(this.Q::getValue);
      this.k.P(this.s::getValue);
      this.O.P(this.I::getValue);
   }

   private static g v(String var0, String var1) {
      return new g(var0, var1, "larpclient").F("Text");
   }

   @Override
   public Set<m> getNetworkImpacts() {
      return Set.of(com.larpclient.module.m.k);
   }

   @Override
   public void onEnable() {
      this.N = 0L;
      this.P();
   }

   @Override
   public void onDisable() {
      ClientLevel var1 = getMc().level;
      if (var1 != null) {
         Scoreboard var2 = var1.getScoreboard();
         this.Q(var2);
         N(var2);
         var2.setDisplayObjective(DisplaySlot.SIDEBAR, this.e == null ? null : var2.getObjective(this.e.getName()));
      }

      this.w.clear();
      this.e = null;
   }

   @Override
   public void onTick() {
      long var1 = System.currentTimeMillis();
      long var5;
      int var10000 = (var5 = var1 - this.N - 1000L) == 0L ? 0 : (var5 < 0L ? -1 : 1);
      boolean var4 = false;
      int var3 = var10000;
      if ((Integer.signum(var3) >>> 31 ^ 1) != 0) {
         this.N = var1;
         this.P();
      }
   }

   private void P() {
      ClientLevel var1 = getMc().level;
      if (var1 != null) {
         Scoreboard var2 = var1.getScoreboard();
         Objective var3 = var2.getDisplayObjective(DisplaySlot.SIDEBAR);
         if (var3 != null && !"larpclient_fakesb".equals(var3.getName())) {
            this.e = var3;
         }

         this.Q(var2);
         N(var2);
         Objective var4 = var2.addObjective("larpclient_fakesb", ObjectiveCriteria.DUMMY, d(this.R.getValue()), RenderType.INTEGER, false, BlankFormat.INSTANCE);
         var2.setDisplayObjective(DisplaySlot.SIDEBAR, var4);
         List var5 = this.W();

         for (int var6 = 0; var6 < var5.size(); var6++) {
            String var7 = "larpclient_sb_" + var6;
            this.w.add(var7);
            PlayerTeam var8 = var2.getPlayerTeam(var7);
            if (var8 != null) {
               var2.removePlayerTeam(var8);
            }

            PlayerTeam var9 = var2.addPlayerTeam(var7);
            var9.setPlayerPrefix((Component)var5.get(var6));
            String var10 = "§" + Integer.toHexString(var6);
            ScoreAccess var11 = var2.getOrCreatePlayerScore(ScoreHolder.forNameOnly(var10), var4);
            var11.set(var5.size() - var6);
            var11.numberFormatOverride(BlankFormat.INSTANCE);
            var2.addPlayerToTeam(var10, var9);
         }
      }
   }

   private void Q(Scoreboard var1) {
      for (String var3 : this.w) {
         PlayerTeam var4 = var1.getPlayerTeam(var3);
         if (var4 != null) {
            var1.removePlayerTeam(var4);
         }
      }

      this.w.clear();
   }

   private static void N(Scoreboard var0) {
      Objective var1 = var0.getObjective("larpclient_fakesb");
      if (var1 != null) {
         var0.removeObjective(var1);
      }
   }

   private List<Component> W() {
      ArrayList var1 = new ArrayList();
      if (this.Y.getValue()) {
         var1.add(K("$ ", 65280).append(d(N(this.m.getValue()))));
      }

      if (this.y.getValue()) {
         var1.add(K("★ ", 10814716).append(d(this.j.getValue())));
      }

      if (this.Q.getValue()) {
         var1.add(K("\ud83d\udde1 ", 16711680).append(d(this.l.getValue())));
      }

      if (this.s.getValue()) {
         var1.add(K("☠ ", 16545539).append(d(this.k.getValue())));
      }

      if (this.I.getValue()) {
         var1.add(K("⌚ ", 16771072).append(d(this.O.getValue())));
      }

      if (var1.isEmpty()) {
         var1.add(Component.literal(" "));
      }

      return var1;
   }

   private static MutableComponent d(String var0) {
      return K(var0 == null ? "" : var0, 16777215);
   }

   private static MutableComponent K(String var0, int var1) {
      return Component.literal(var0).setStyle(Style.EMPTY.withColor(TextColor.fromRgb(var1)));
   }

   public static String N(String var0) {
      Double var1 = X(var0);
      return var1 == null ? var0 : E(Math.max(0.0, var1));
   }

   private static Double X(String var0) {
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

   private static String E(double var0) {
      if (var0 >= 1.0E9) {
         return q(var0 / 1.0E9) + "B";
      } else if (var0 >= 1000000.0) {
         return q(var0 / 1000000.0) + "M";
      } else {
         return var0 >= 1000.0 ? q(var0 / 1000.0) + "k" : q(var0);
      }
   }

   private static String q(double var0) {
      return var0 == Math.floor(var0) && !Double.isInfinite(var0)
         ? Long.toString((long)var0)
         : String.format(Locale.ROOT, "%.2f", var0).replaceAll("0+$", "").replaceAll("\\.$", "");
   }
}
