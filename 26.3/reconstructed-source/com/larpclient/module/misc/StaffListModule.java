package com.larpclient.module.misc;

import com.larpclient.LarpClient;
import com.larpclient.hud.HudPanels2;
import com.larpclient.hud.NotificationManager;
import com.larpclient.util.misc.LarpLog;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.regex.Pattern;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.GameType;
import net.minecraft.world.scores.PlayerTeam;

public final class StaffListModule extends Module {
   public final BooleanSetting playerHeads = this.addSetting(new BooleanSetting("Player Heads", "Show staff player heads", true));
   public final BooleanSetting alerts = this.addSetting(new BooleanSetting("Alerts", "Notify when staff join, leave or go into Vanish", true));
   public final TextSetting staffNames = this.addSetting(
      new TextSetting(
         "Staff Names", "Players who always count as staff, separated by commas or spaces. Needed for Vanish and for servers that hide staff ranks", ""
      )
   );
   private static final Pattern namePattern = Pattern.compile("[.*]?[A-Za-z0-9_]{1,16}");
   private static volatile StaffListModule.StaffNamesCache namesCache = new StaffListModule.StaffNamesCache("", Set.of());
   private final StaffListModule.StaffTracker tracker = new StaffListModule.StaffTracker();
   private final Set<String> seenRankLabels = new HashSet<>();
   private static final int maxRankLabels = 32;
   private static final int logIntervalTicks = 20;
   private final Set<String> loggedStaff = new HashSet<>();
   private static final String staffSymbols = "★☆✦✧✪✩✫✬✭✮✯⭐✰❂⚝✴✵✶✷✸✹⍟";
   private int tickCounter;

   public StaffListModule() {
      super("Staff List", "Show staff from rank words and staff symbols in the tab list, and your Staff Names list", Category.RENDER, -1);
      this.staffNames.setTitle("Name1, Name2");
   }

   private static StaffListModule normalizeLabel() {
      LarpClient var0 = LarpClient.getInstance();
      return var0 == null ? null : var0.getModuleManager().getModule(StaffListModule.class);
   }

   public static boolean parseStaffNames() {
      StaffListModule var0 = normalizeLabel();
      return var0 != null && var0.isOperational();
   }

   public static boolean shouldShowHeads() {
      StaffListModule var0 = normalizeLabel();
      return var0 == null || var0.playerHeads.getValue();
   }

   public static Set<String> getStaffNames() {
      StaffListModule var0 = normalizeLabel();
      String var1 = var0 == null ? "" : var0.staffNames.getValue();
      StaffListModule.StaffNamesCache var2 = namesCache;
      if (!var2.raw().equals(var1)) {
         namesCache = var2 = new StaffListModule.StaffNamesCache(var1, parseStaffNames(var1));
      }

      return var2.lowerCase();
   }

   public static Set<String> parseStaffNames(String var0) {
      LinkedHashSet var1 = new LinkedHashSet();

      for (String var5 : var0.split("[\\s,;]+")) {
         if (!var5.isEmpty()) {
            var1.add(var5.toLowerCase(Locale.ROOT));
         }
      }

      return Collections.unmodifiableSet(var1);
   }

   public static List<StaffListModule.StaffEntry> getStaffEntries() {
      Minecraft var0 = Minecraft.getInstance();
      ArrayList var1 = new ArrayList();
      ClientPacketListener var2 = var0.getConnection();
      if (var2 != null && var0.level != null) {
         Set var3 = getStaffNames();
         HashSet var4 = new HashSet();

         for (PlayerInfo var6 : var2.getOnlinePlayers()) {
            String var7 = var6.getProfile().name();
            var4.add(var7.toLowerCase(Locale.ROOT));
            if (namePattern.matcher(var7).matches() && isStaff(var6, var3)) {
               StaffListModule.Status var8 = var6.getGameMode() == GameType.SPECTATOR
                  ? StaffListModule.Status.SPECTATOR
                  : (var0.level.getPlayerByUUID(var6.getProfile().id()) != null ? StaffListModule.Status.NEAR : StaffListModule.Status.ONLINE);
               var1.add(new StaffListModule.StaffEntry(var7, var6, var8));
            }
         }

         if (!var3.isEmpty()) {
            for (PlayerTeam var11 : var0.level.getScoreboard().getPlayerTeams()) {
               for (String var13 : var11.getPlayers()) {
                  String var9 = var13.toLowerCase(Locale.ROOT);
                  if (var3.contains(var9) && var4.add(var9)) {
                     var1.add(new StaffListModule.StaffEntry(var13, null, StaffListModule.Status.VANISH));
                  }
               }
            }
         }

         var1.sort(Comparator.comparing(StaffListModule.StaffEntry::name, String.CASE_INSENSITIVE_ORDER));
         return var1;
      } else {
         return var1;
      }
   }

   private static boolean isStaff(PlayerInfo var0, Set<String> var1) {
      if (var1.contains(var0.getProfile().name().toLowerCase(Locale.ROOT))) {
         return true;
      } else {
         PlayerTeam var2 = var0.getTeam();
         if (var2 == null || !hasStaffMarker(var2.getPlayerPrefix().getString()) && !hasStaffMarker(var2.getPlayerSuffix().getString())) {
            String[] var3 = getTabNameParts(var0);
            return var3 != null && (hasStaffMarker(var3[0]) || hasStaffMarker(var3[1]));
         } else {
            return true;
         }
      }
   }

   public static boolean hasStaffMarker(String var0) {
      return HudPanels2.isStaffName(var0) || hasStaffSymbol(var0);
   }

   public static boolean hasStaffSymbol(String var0) {
      for (int var1 = 0; var1 < var0.length(); var1++) {
         char var2 = var0.charAt(var1);
         if ("★☆✦✧✪✩✫✬✭✮✯⭐✰❂⚝✴✵✶✷✸✹⍟".indexOf(var2) >= 0 || var2 >= '\ue000' && var2 <= '\uf8ff') {
            return true;
         }
      }

      return false;
   }

   private static String[] getTabNameParts(PlayerInfo var0) {
      Component var1 = var0.getTabListDisplayName();
      return var1 == null ? null : splitAroundName(var1.getString(), var0.getProfile().name());
   }

   public static String[] splitAroundName(String var0, String var1) {
      for (int var2 = 0; var2 + var1.length() <= var0.length(); var2++) {
         if (var0.regionMatches(true, var2, var1, 0, var1.length())) {
            return new String[]{var0.substring(0, var2), var0.substring(var2 + var1.length())};
         }
      }

      return null;
   }

   public static String normalizeLabel(String var0, String var1) {
      String var2 = (var0.strip() + " <name> " + var1.strip()).strip();
      return var2.equals("<name>") ? "" : var2.replaceAll("\\d+", "#");
   }

   private static List<String> getRankLabels(PlayerInfo var0) {
      ArrayList var1 = new ArrayList(2);
      PlayerTeam var2 = var0.getTeam();
      if (var2 != null) {
         var1.add(normalizeLabel(var2.getPlayerPrefix().getString(), var2.getPlayerSuffix().getString()));
      }

      String[] var3 = getTabNameParts(var0);
      if (var3 != null) {
         var1.add(normalizeLabel(var3[0], var3[1]));
      }

      var1.removeIf(String::isEmpty);
      return var1;
   }

   @Override
   public void onEnable() {
      this.tracker.reset();
      this.tickCounter = 0;
      this.seenRankLabels.clear();
      this.loggedStaff.clear();
   }

   @Override
   public void onDisable() {
      this.tracker.reset();
      this.tickCounter = 0;
      this.seenRankLabels.clear();
      this.loggedStaff.clear();
   }

   private void logDetection(List<StaffListModule.StaffEntry> var1) {
      Minecraft var2 = Minecraft.getInstance();
      if (var2.getConnection() != null && var2.level != null) {
         LinkedHashSet var3 = new LinkedHashSet();
         int var4 = 0;
         int var5 = 0;

         for (PlayerInfo var7 : var2.getConnection().getOnlinePlayers()) {
            if (namePattern.matcher(var7.getProfile().name()).matches()) {
               var4++;
               if (var7.getTabListDisplayName() != null && getTabNameParts(var7) == null) {
                  var5++;
               }

               var3.addAll(getRankLabels(var7));
            }
         }

         this.seenRankLabels.addAll(var3);
         List var8 = var3.stream().limit(16L).map(var0 -> (isStaffLabel(var0) ? "+" : "-") + "'" + var0 + "'").toList();
         LarpLog.info(
            "StaffList",
            "Detected {} staff ({} in Vanish) among {} players ({} real, {} with tab text that hides the name) with {} Staff Names; rank labels seen ({} total, + counts as staff): {}",
            var1.size(),
            var1.stream().filter(StaffListModule.StaffEntry::vanished).count(),
            var2.getConnection().getOnlinePlayers().size(),
            var4,
            var5,
            getStaffNames().size(),
            var3.size(),
            var8
         );
      }
   }

   private void shouldShowHeads() {
      ClientPacketListener var1 = Minecraft.getInstance().getConnection();
      if (var1 != null && this.seenRankLabels.size() < 32) {
         for (PlayerInfo var3 : var1.getOnlinePlayers()) {
            if (namePattern.matcher(var3.getProfile().name()).matches()) {
               for (String var5 : getRankLabels(var3)) {
                  if (this.seenRankLabels.add(var5)) {
                     LarpLog.info(
                        "StaffList",
                        "New rank label '{}' on {} (tab order {}, counts as staff: {})",
                        var5,
                        var3.getProfile().name(),
                        var3.getTabListOrder(),
                        isStaffLabel(var5) ? "yes" : "no"
                     );
                     if (this.seenRankLabels.size() >= 32) {
                        LarpLog.info("StaffList", "Rank label log limit of {} reached for this world", 32);
                        return;
                     }
                  }
               }
            }
         }
      }
   }

   private static boolean isStaffLabel(String var0) {
      String[] var1 = splitAroundName(var0, "<name>");
      return var1 != null && (hasStaffMarker(var1[0]) || hasStaffMarker(var1[1]));
   }

   private void logStaffReasons(List<StaffListModule.StaffEntry> var1) {
      Set var2 = getStaffNames();

      for (StaffListModule.StaffEntry var4 : var1) {
         if (this.loggedStaff.size() >= 256) {
            return;
         }

         if (var4.info() != null && this.loggedStaff.add(var4.name().toLowerCase(Locale.ROOT))) {
            PlayerInfo var5 = var4.info();
            String var6 = var2.contains(var4.name().toLowerCase(Locale.ROOT))
               ? "Staff Names"
               : getRankLabels(var5)
                  .stream()
                  .filter(StaffListModule::isStaffLabel)
                  .findFirst()
                  .map(var0 -> (hasStaffSymbol(var0) ? "staff symbol in '" : "rank word in '") + var0 + "'")
                  .orElse("rank label");
            LarpLog.info("StaffList", "{} counts as staff by {}", var4.name(), var6);
         }
      }
   }

   @Override
   public void onTick() {
      List var1 = getStaffEntries();
      int var10000 = ++this.tickCounter;
      byte var6 = 40;
      int var5 = var10000;
      var10000 = Integer.compare(var5, var6);
      if ((var10000 | -var10000) >>> 31 == 0) {
         this.logDetection(var1);
         this.logStaffReasons(var1);
      } else {
         byte var10 = 40;
         int var9 = this.tickCounter;
         if ((-Integer.compare(var9, var10) >>> 31 ^ 1) == 0) {
            var10000 = this.tickCounter % 20;
            boolean var12 = false;
            int var11 = var10000;
            var10000 = Integer.signum(var11);
            if ((var10000 | -var10000) >>> 31 == 0) {
               this.shouldShowHeads();
               this.logStaffReasons(var1);
            }
         }
      }

      LinkedHashMap var2 = new LinkedHashMap();

      for (StaffListModule.StaffEntry var4 : var1) {
         var2.put(var4.name(), var4.vanished());
      }

      StaffListModule$f$n var13 = this.tracker.update(var2);
      int var17 = this.alerts.getValue();
      boolean var8 = false;
      byte var7 = (byte)var17;
      var17 = Integer.signum(var7);
      if ((var17 | -var17) >>> 31 != 0) {
         isStaff("Staff online", var13.online(), NotificationManager.Type.WARNING, 5000L);
         isStaff("Staff joined", var13.joined(), NotificationManager.Type.WARNING, 5000L);
         isStaff("Staff left", var13.left(), NotificationManager.Type.INFO, 4000L);
         isStaff("Staff in Vanish", var13.vanished(), NotificationManager.Type.DANGER, 6000L);
      }
   }

   private static void isStaff(String var0, List<String> var1, NotificationManager.Type var2, long var3) {
      if (!var1.isEmpty()) {
         String var5 = var1.size() <= 3 ? String.join(", ", var1) : String.join(", ", var1.subList(0, 3)) + " +" + (var1.size() - 3);
         LarpLog.info("StaffList", "{}: {}", var0, String.join(", ", var1));
         NotificationManager.getInstance().notify(var0, var5, var2, var3);
      }
   }

   public record StaffEntry(String name, PlayerInfo info, StaffListModule.Status status) {
      public boolean vanished() {
         return this.status == StaffListModule.Status.VANISH;
      }
   }

   private record StaffNamesCache(String raw, Set<String> lowerCase) {
   }

   public static final class StaffTracker {
      public static final int wARMUP_TICKS = 40;
      public static final int cONFIRM_TICKS = 40;
      private final Map<String, Boolean> knownStaff = new LinkedHashMap<>();
      private final Map<String, Integer> pendingChanges = new HashMap<>();
      private int warmupTicks;

      public void reset() {
         this.knownStaff.clear();
         this.pendingChanges.clear();
         this.warmupTicks = 0;
      }

      public StaffListModule$f$n update(Map<String, Boolean> var1) {
         ArrayList var2 = new ArrayList();
         ArrayList var3 = new ArrayList();
         ArrayList var4 = new ArrayList();
         ArrayList var5 = new ArrayList();
         if (this.warmupTicks < 40) {
            this.knownStaff.clear();
            this.knownStaff.putAll(var1);
            this.pendingChanges.clear();
            if (++this.warmupTicks == 40) {
               var1.forEach((var2x, var3x) -> (var3x ? var5 : var2).add(var2x));
            }

            return new StaffListModule$f$n(var2, var3, var4, var5);
         } else {
            for (String var7 : List.copyOf(this.knownStaff.keySet())) {
               if (!var1.containsKey(var7)) {
                  this.knownStaff.remove(var7);
                  var4.add(var7);
               }
            }

            this.pendingChanges.keySet().retainAll(var1.keySet());

            for (Entry var12 : var1.entrySet()) {
               String var8 = (String)var12.getKey();
               boolean var9 = (Boolean)var12.getValue();
               Boolean var10 = this.knownStaff.get(var8);
               if (var10 == null && !var9) {
                  this.pendingChanges.remove(var8);
                  this.knownStaff.put(var8, false);
                  var3.add(var8);
               } else if (var10 != null && var10 == var9) {
                  this.pendingChanges.remove(var8);
               } else if (this.pendingChanges.merge(var8, 1, Integer::sum) >= 40) {
                  this.pendingChanges.remove(var8);
                  this.knownStaff.put(var8, var9);
                  if (var9) {
                     var5.add(var8);
                  }
               }
            }

            return new StaffListModule$f$n(var2, var3, var4, var5);
         }
      }
   }

   public enum Status {
      ONLINE("Online", -10551483),
      NEAR("Near", -15821),
      SPECTATOR("Spectator", -44462),
      VANISH("Vanish", -44462);

      public final String LABEL;
      public final int COLOR;

      Status(String var3, int var4) {
         this.LABEL = var3;
         this.COLOR = var4;
      }
   }
}
