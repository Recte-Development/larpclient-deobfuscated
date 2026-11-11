package com.larpclient.module.misc;

import com.larpclient.module.world.ScanPhase;
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
   private static final String oBJECTIVE_NAME = "larpclient_fakesb";
   private static final String tEAM_PREFIX = "larpclient_sb_";
   private static final String dEFAULT_TEXT = "larpclient";
   private final TextSetting titleSetting = this.addSetting(createTextSetting("Title", "Sidebar heading"));
   private final BooleanSetting showMoney = this.addSetting(new BooleanSetting("Money", "Show the money row", true));
   private final TextSetting moneyAmount = this.addSetting(createTextSetting("Money Amount", "Money text; numbers like 1500 or 2.5m are shortened"));
   private final BooleanSetting showShards = this.addSetting(new BooleanSetting("Shards", "Show the shards row", true));
   private final TextSetting shardsAmount = this.addSetting(createTextSetting("Shards Amount", "Shards text"));
   private final BooleanSetting showKills = this.addSetting(new BooleanSetting("Kills", "Show the kills row", true));
   private final TextSetting killsAmount = this.addSetting(createTextSetting("Kills Amount", "Kills text"));
   private final BooleanSetting showDeaths = this.addSetting(new BooleanSetting("Deaths", "Show the deaths row", true));
   private final TextSetting deathsAmount = this.addSetting(createTextSetting("Deaths Amount", "Deaths text"));
   private final BooleanSetting showPlaytime = this.addSetting(new BooleanSetting("Playtime", "Show the playtime row", true));
   private final TextSetting playtimeAmount = this.addSetting(createTextSetting("Playtime Amount", "Playtime text"));
   private final List<String> teamNames = new ArrayList<>();
   private Objective previousObjective;
   private long lastRefreshMs;

   public FakeScoreboardModule() {
      super("Fake Scoreboard", "Replaces the sidebar with your own stats.", Category.PLAYER, -1);
      this.moneyAmount.visibleWhen(this.showMoney::getValue);
      this.shardsAmount.visibleWhen(this.showShards::getValue);
      this.killsAmount.visibleWhen(this.showKills::getValue);
      this.deathsAmount.visibleWhen(this.showDeaths::getValue);
      this.playtimeAmount.visibleWhen(this.showPlaytime::getValue);
   }

   private static TextSetting createTextSetting(String var0, String var1) {
      return new TextSetting(var0, var1, "larpclient").setTitle("Text");
   }

   @Override
   public Set<ScanPhase> getNetworkImpacts() {
      return Set.of(ScanPhase.iDLE);
   }

   @Override
   public void onEnable() {
      this.lastRefreshMs = 0L;
      this.refreshSidebar();
   }

   @Override
   public void onDisable() {
      ClientLevel var1 = getMc().level;
      if (var1 != null) {
         Scoreboard var2 = var1.getScoreboard();
         this.removeTeams(var2);
         removeObjective(var2);
         var2.setDisplayObjective(DisplaySlot.SIDEBAR, this.previousObjective == null ? null : var2.getObjective(this.previousObjective.getName()));
      }

      this.teamNames.clear();
      this.previousObjective = null;
   }

   @Override
   public void onTick() {
      long var1 = System.currentTimeMillis();
      long var5;
      int var10000 = (var5 = var1 - this.lastRefreshMs - 1000L) == 0L ? 0 : (var5 < 0L ? -1 : 1);
      boolean var4 = false;
      int var3 = var10000;
      if ((Integer.signum(var3) >>> 31 ^ 1) != 0) {
         this.lastRefreshMs = var1;
         this.refreshSidebar();
      }
   }

   private void refreshSidebar() {
      ClientLevel var1 = getMc().level;
      if (var1 != null) {
         Scoreboard var2 = var1.getScoreboard();
         Objective var3 = var2.getDisplayObjective(DisplaySlot.SIDEBAR);
         if (var3 != null && !"larpclient_fakesb".equals(var3.getName())) {
            this.previousObjective = var3;
         }

         this.removeTeams(var2);
         removeObjective(var2);
         Objective var4 = var2.addObjective(
            "larpclient_fakesb", ObjectiveCriteria.DUMMY, whiteText(this.titleSetting.getValue()), RenderType.INTEGER, false, BlankFormat.INSTANCE
         );
         var2.setDisplayObjective(DisplaySlot.SIDEBAR, var4);
         List var5 = this.buildLines();

         for (int var6 = 0; var6 < var5.size(); var6++) {
            String var7 = "larpclient_sb_" + var6;
            this.teamNames.add(var7);
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

   private void removeTeams(Scoreboard var1) {
      for (String var3 : this.teamNames) {
         PlayerTeam var4 = var1.getPlayerTeam(var3);
         if (var4 != null) {
            var1.removePlayerTeam(var4);
         }
      }

      this.teamNames.clear();
   }

   private static void removeObjective(Scoreboard var0) {
      Objective var1 = var0.getObjective("larpclient_fakesb");
      if (var1 != null) {
         var0.removeObjective(var1);
      }
   }

   private List<Component> buildLines() {
      ArrayList var1 = new ArrayList();
      if (this.showMoney.getValue()) {
         var1.add(removeObjective("$ ", 65280).append(whiteText(formatMoney(this.moneyAmount.getValue()))));
      }

      if (this.showShards.getValue()) {
         var1.add(removeObjective("★ ", 10814716).append(whiteText(this.shardsAmount.getValue())));
      }

      if (this.showKills.getValue()) {
         var1.add(removeObjective("\ud83d\udde1 ", 16711680).append(whiteText(this.killsAmount.getValue())));
      }

      if (this.showDeaths.getValue()) {
         var1.add(removeObjective("☠ ", 16545539).append(whiteText(this.deathsAmount.getValue())));
      }

      if (this.showPlaytime.getValue()) {
         var1.add(removeObjective("⌚ ", 16771072).append(whiteText(this.playtimeAmount.getValue())));
      }

      if (var1.isEmpty()) {
         var1.add(Component.literal(" "));
      }

      return var1;
   }

   private static MutableComponent whiteText(String var0) {
      return removeObjective(var0 == null ? "" : var0, 16777215);
   }

   private static MutableComponent removeObjective(String var0, int var1) {
      return Component.literal(var0).setStyle(Style.EMPTY.withColor(TextColor.fromRgb(var1)));
   }

   public static String formatMoney(String var0) {
      Double var1 = parseAmount(var0);
      return var1 == null ? var0 : abbreviate(Math.max(0.0, var1));
   }

   private static Double parseAmount(String var0) {
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

   private static String abbreviate(double var0) {
      if (var0 >= 1.0E9) {
         return formatNumber(var0 / 1.0E9) + "B";
      } else if (var0 >= 1000000.0) {
         return formatNumber(var0 / 1000000.0) + "M";
      } else {
         return var0 >= 1000.0 ? formatNumber(var0 / 1000.0) + "k" : formatNumber(var0);
      }
   }

   private static String formatNumber(double var0) {
      return var0 == Math.floor(var0) && !Double.isInfinite(var0)
         ? Long.toString((long)var0)
         : String.format(Locale.ROOT, "%.2f", var0).replaceAll("0+$", "").replaceAll("\\.$", "");
   }
}
