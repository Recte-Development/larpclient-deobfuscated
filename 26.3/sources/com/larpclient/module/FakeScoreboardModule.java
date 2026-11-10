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
   private static final String H = "larpclient_fakesb";
   private static final String w = "larpclient_sb_";
   private static final String i = "larpclient";
   private final e n = this.addSetting(S("Title", "Sidebar heading"));
   private final m S = this.addSetting(new m("Money", "Show the money row", true));
   private final e N = this.addSetting(S("Money Amount", "Money text; numbers like 1500 or 2.5m are shortened"));
   private final m U = this.addSetting(new m("Shards", "Show the shards row", true));
   private final e p = this.addSetting(S("Shards Amount", "Shards text"));
   private final m r = this.addSetting(new m("Kills", "Show the kills row", true));
   private final e Q = this.addSetting(S("Kills Amount", "Kills text"));
   private final m g = this.addSetting(new m("Deaths", "Show the deaths row", true));
   private final e b = this.addSetting(S("Deaths Amount", "Deaths text"));
   private final m l = this.addSetting(new m("Playtime", "Show the playtime row", true));
   private final e s = this.addSetting(S("Playtime Amount", "Playtime text"));
   private final List<String> h = new ArrayList<>();
   private Objective D;
   private long M;

   public FakeScoreboardModule() {
      super("Fake Scoreboard", "Replaces the sidebar with your own stats.", Category.PLAYER, -1);
      this.N.a(this.S::getValue);
      this.p.a(this.U::getValue);
      this.Q.a(this.r::getValue);
      this.b.a(this.g::getValue);
      this.s.a(this.l::getValue);
   }

   private static e S(String var0, String var1) {
      return new e(var0, var1, "larpclient").L("Text");
   }

   @Override
   public Set<l> getNetworkImpacts() {
      return Set.of(com.larpclient.module.l.E);
   }

   @Override
   public void onEnable() {
      this.M = 0L;
      this.t();
   }

   @Override
   public void onDisable() {
      ClientLevel var1 = getMc().level;
      if (var1 != null) {
         Scoreboard var2 = var1.getScoreboard();
         this.H(var2);
         I(var2);
         var2.setDisplayObjective(DisplaySlot.SIDEBAR, this.D == null ? null : var2.getObjective(this.D.getName()));
      }

      this.h.clear();
      this.D = null;
   }

   @Override
   public void onTick() {
      long var1 = System.currentTimeMillis();
      long var5;
      int var10000 = (var5 = var1 - this.M - 1000L) == 0L ? 0 : (var5 < 0L ? -1 : 1);
      boolean var4 = false;
      int var3 = var10000;
      if ((Integer.signum(var3) >>> 31 ^ 1) != 0) {
         this.M = var1;
         this.t();
      }
   }

   private void t() {
      ClientLevel var1 = getMc().level;
      if (var1 != null) {
         Scoreboard var2 = var1.getScoreboard();
         Objective var3 = var2.getDisplayObjective(DisplaySlot.SIDEBAR);
         if (var3 != null && !"larpclient_fakesb".equals(var3.getName())) {
            this.D = var3;
         }

         this.H(var2);
         I(var2);
         Objective var4 = var2.addObjective("larpclient_fakesb", ObjectiveCriteria.DUMMY, W(this.n.getValue()), RenderType.INTEGER, false, BlankFormat.INSTANCE);
         var2.setDisplayObjective(DisplaySlot.SIDEBAR, var4);
         List var5 = this.b();

         for (int var6 = 0; var6 < var5.size(); var6++) {
            String var7 = "larpclient_sb_" + var6;
            this.h.add(var7);
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

   private void H(Scoreboard var1) {
      for (String var3 : this.h) {
         PlayerTeam var4 = var1.getPlayerTeam(var3);
         if (var4 != null) {
            var1.removePlayerTeam(var4);
         }
      }

      this.h.clear();
   }

   private static void I(Scoreboard var0) {
      Objective var1 = var0.getObjective("larpclient_fakesb");
      if (var1 != null) {
         var0.removeObjective(var1);
      }
   }

   private List<Component> b() {
      ArrayList var1 = new ArrayList();
      if (this.S.getValue()) {
         var1.add(I("$ ", 65280).append(W(D(this.N.getValue()))));
      }

      if (this.U.getValue()) {
         var1.add(I("★ ", 10814716).append(W(this.p.getValue())));
      }

      if (this.r.getValue()) {
         var1.add(I("\ud83d\udde1 ", 16711680).append(W(this.Q.getValue())));
      }

      if (this.g.getValue()) {
         var1.add(I("☠ ", 16545539).append(W(this.b.getValue())));
      }

      if (this.l.getValue()) {
         var1.add(I("⌚ ", 16771072).append(W(this.s.getValue())));
      }

      if (var1.isEmpty()) {
         var1.add(Component.literal(" "));
      }

      return var1;
   }

   private static MutableComponent W(String var0) {
      return I(var0 == null ? "" : var0, 16777215);
   }

   private static MutableComponent I(String var0, int var1) {
      return Component.literal(var0).setStyle(Style.EMPTY.withColor(TextColor.fromRgb(var1)));
   }

   public static String D(String var0) {
      Double var1 = e(var0);
      return var1 == null ? var0 : Y(Math.max(0.0, var1));
   }

   private static Double e(String var0) {
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

   private static String Y(double var0) {
      if (var0 >= 1.0E9) {
         return X(var0 / 1.0E9) + "B";
      } else if (var0 >= 1000000.0) {
         return X(var0 / 1000000.0) + "M";
      } else {
         return var0 >= 1000.0 ? X(var0 / 1000.0) + "k" : X(var0);
      }
   }

   private static String X(double var0) {
      return var0 == Math.floor(var0) && !Double.isInfinite(var0)
         ? Long.toString((long)var0)
         : String.format(Locale.ROOT, "%.2f", var0).replaceAll("0+$", "").replaceAll("\\.$", "");
   }
}
