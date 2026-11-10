package com.larpclient.module;

import com.larpclient.LarpClient;
import com.larpclient.cl;
import com.larpclient.s0;
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
   public final s X = this.addSetting(new s("Player Heads", "Show staff player heads", true));
   public final s G = this.addSetting(new s("Alerts", "Notify when staff join, leave or go into Vanish", true));
   public final g f = this.addSetting(
      new g("Staff Names", "Players who always count as staff, separated by commas or spaces. Needed for Vanish and for servers that hide staff ranks", "")
   );
   private static final Pattern J = Pattern.compile("[.*]?[A-Za-z0-9_]{1,16}");
   private static volatile StaffListModule.j z = new StaffListModule.j("", Set.of());
   private final StaffListModule.r x = new StaffListModule.r();
   private final Set<String> V = new HashSet<>();
   private static final int y = 32;
   private static final int u = 20;
   private final Set<String> g = new HashSet<>();
   private static final String s = "★☆✦✧✪✩✫✬✭✮✯⭐✰❂⚝✴✵✶✷✸✹⍟";
   private int m;

   public StaffListModule() {
      super("Staff List", "Show staff from rank words and staff symbols in the tab list, and your Staff Names list", Category.RENDER, -1);
      this.f.F("Name1, Name2");
   }

   private static StaffListModule L() {
      LarpClient var0 = LarpClient.R();
      return var0 == null ? null : var0.z().p(StaffListModule.class);
   }

   public static boolean v() {
      StaffListModule var0 = L();
      return var0 != null && var0.isOperational();
   }

   public static boolean z() {
      StaffListModule var0 = L();
      return var0 == null || var0.X.getValue();
   }

   public static Set<String> p() {
      StaffListModule var0 = L();
      String var1 = var0 == null ? "" : var0.f.getValue();
      StaffListModule.j var2 = z;
      if (!var2.raw().equals(var1)) {
         z = var2 = new StaffListModule.j(var1, T(var1));
      }

      return var2.lowerCase();
   }

   public static Set<String> T(String var0) {
      LinkedHashSet var1 = new LinkedHashSet();

      for (String var5 : var0.split("[\\s,;]+")) {
         if (!var5.isEmpty()) {
            var1.add(var5.toLowerCase(Locale.ROOT));
         }
      }

      return Collections.unmodifiableSet(var1);
   }

   public static List<StaffListModule.e> g() {
      Minecraft var0 = Minecraft.getInstance();
      ArrayList var1 = new ArrayList();
      ClientPacketListener var2 = var0.getConnection();
      if (var2 != null && var0.level != null) {
         Set var3 = p();
         HashSet var4 = new HashSet();

         for (PlayerInfo var6 : var2.getOnlinePlayers()) {
            String var7 = var6.getProfile().name();
            var4.add(var7.toLowerCase(Locale.ROOT));
            if (J.matcher(var7).matches() && e(var6, var3)) {
               StaffListModule.h var8 = var6.getGameMode() == GameType.SPECTATOR
                  ? StaffListModule.h.N
                  : (var0.level.getPlayerByUUID(var6.getProfile().id()) != null ? StaffListModule.h.d : StaffListModule.h.X);
               var1.add(new StaffListModule.e(var7, var6, var8));
            }
         }

         if (!var3.isEmpty()) {
            for (PlayerTeam var11 : var0.level.getScoreboard().getPlayerTeams()) {
               for (String var13 : var11.getPlayers()) {
                  String var9 = var13.toLowerCase(Locale.ROOT);
                  if (var3.contains(var9) && var4.add(var9)) {
                     var1.add(new StaffListModule.e(var13, null, StaffListModule.h.y));
                  }
               }
            }
         }

         var1.sort(Comparator.comparing(StaffListModule.e::name, String.CASE_INSENSITIVE_ORDER));
         return var1;
      } else {
         return var1;
      }
   }

   private static boolean e(PlayerInfo var0, Set<String> var1) {
      if (var1.contains(var0.getProfile().name().toLowerCase(Locale.ROOT))) {
         return true;
      } else {
         PlayerTeam var2 = var0.getTeam();
         if (var2 == null || !n(var2.getPlayerPrefix().getString()) && !n(var2.getPlayerSuffix().getString())) {
            String[] var3 = l(var0);
            return var3 != null && (n(var3[0]) || n(var3[1]));
         } else {
            return true;
         }
      }
   }

   public static boolean n(String var0) {
      return cl.U(var0) || p(var0);
   }

   public static boolean p(String var0) {
      for (int var1 = 0; var1 < var0.length(); var1++) {
         char var2 = var0.charAt(var1);
         if ("★☆✦✧✪✩✫✬✭✮✯⭐✰❂⚝✴✵✶✷✸✹⍟".indexOf(var2) >= 0 || var2 >= '\ue000' && var2 <= '\uf8ff') {
            return true;
         }
      }

      return false;
   }

   private static String[] l(PlayerInfo var0) {
      Component var1 = var0.getTabListDisplayName();
      return var1 == null ? null : P(var1.getString(), var0.getProfile().name());
   }

   public static String[] P(String var0, String var1) {
      for (int var2 = 0; var2 + var1.length() <= var0.length(); var2++) {
         if (var0.regionMatches(true, var2, var1, 0, var1.length())) {
            return new String[]{var0.substring(0, var2), var0.substring(var2 + var1.length())};
         }
      }

      return null;
   }

   public static String N(String var0, String var1) {
      String var2 = (var0.strip() + " <name> " + var1.strip()).strip();
      return var2.equals("<name>") ? "" : var2.replaceAll("\\d+", "#");
   }

   private static List<String> i(PlayerInfo var0) {
      ArrayList var1 = new ArrayList(2);
      PlayerTeam var2 = var0.getTeam();
      if (var2 != null) {
         var1.add(N(var2.getPlayerPrefix().getString(), var2.getPlayerSuffix().getString()));
      }

      String[] var3 = l(var0);
      if (var3 != null) {
         var1.add(N(var3[0], var3[1]));
      }

      var1.removeIf(String::isEmpty);
      return var1;
   }

   @Override
   public void onEnable() {
      this.x.T();
      this.m = 0;
      this.V.clear();
      this.g.clear();
   }

   @Override
   public void onDisable() {
      this.x.T();
      this.m = 0;
      this.V.clear();
      this.g.clear();
   }

   private void M(List<StaffListModule.e> var1) {
      Minecraft var2 = Minecraft.getInstance();
      if (var2.getConnection() != null && var2.level != null) {
         LinkedHashSet var3 = new LinkedHashSet();
         int var4 = 0;
         int var5 = 0;

         for (PlayerInfo var7 : var2.getConnection().getOnlinePlayers()) {
            if (J.matcher(var7.getProfile().name()).matches()) {
               var4++;
               if (var7.getTabListDisplayName() != null && l(var7) == null) {
                  var5++;
               }

               var3.addAll(i(var7));
            }
         }

         this.V.addAll(var3);
         List var8 = var3.stream().limit(16L).map(var0 -> (G(var0) ? "+" : "-") + "'" + var0 + "'").toList();
         com.larpclient.h.A(
            "StaffList",
            "Detected {} staff ({} in Vanish) among {} players ({} real, {} with tab text that hides the name) with {} Staff Names; rank labels seen ({} total, + counts as staff): {}",
            var1.size(),
            var1.stream().filter(StaffListModule.e::vanished).count(),
            var2.getConnection().getOnlinePlayers().size(),
            var4,
            var5,
            p().size(),
            var3.size(),
            var8
         );
      }
   }

   private void j() {
      ClientPacketListener var1 = Minecraft.getInstance().getConnection();
      if (var1 != null && this.V.size() < 32) {
         for (PlayerInfo var3 : var1.getOnlinePlayers()) {
            if (J.matcher(var3.getProfile().name()).matches()) {
               for (String var5 : i(var3)) {
                  if (this.V.add(var5)) {
                     com.larpclient.h.A(
                        "StaffList",
                        "New rank label '{}' on {} (tab order {}, counts as staff: {})",
                        var5,
                        var3.getProfile().name(),
                        var3.getTabListOrder(),
                        G(var5) ? "yes" : "no"
                     );
                     if (this.V.size() >= 32) {
                        com.larpclient.h.A("StaffList", "Rank label log limit of {} reached for this world", 32);
                        return;
                     }
                  }
               }
            }
         }
      }
   }

   private static boolean G(String var0) {
      String[] var1 = P(var0, "<name>");
      return var1 != null && (n(var1[0]) || n(var1[1]));
   }

   private void S(List<StaffListModule.e> var1) {
      Set var2 = p();

      for (StaffListModule.e var4 : var1) {
         if (this.g.size() >= 256) {
            return;
         }

         if (var4.info() != null && this.g.add(var4.name().toLowerCase(Locale.ROOT))) {
            PlayerInfo var5 = var4.info();
            String var6 = var2.contains(var4.name().toLowerCase(Locale.ROOT))
               ? "Staff Names"
               : i(var5)
                  .stream()
                  .filter(StaffListModule::G)
                  .findFirst()
                  .map(var0 -> (p(var0) ? "staff symbol in '" : "rank word in '") + var0 + "'")
                  .orElse("rank label");
            com.larpclient.h.A("StaffList", "{} counts as staff by {}", var4.name(), var6);
         }
      }
   }

   @Override
   public void onTick() {
      List var1 = g();
      byte var6 = 1;
      int var5 = this.m;
      int var10000 = this.m = var5 + var6;
      byte var8 = 40;
      int var7 = var10000;
      var10000 = Integer.compare(var7, var8);
      if ((var10000 | -var10000) >>> 31 == 0) {
         this.M(var1);
         this.S(var1);
      } else {
         byte var12 = 40;
         int var11 = this.m;
         if ((-Integer.compare(var11, var12) >>> 31 ^ 1) == 0 && this.m % 20 == 0) {
            this.j();
            this.S(var1);
         }
      }

      LinkedHashMap var2 = new LinkedHashMap();

      for (StaffListModule.e var4 : var1) {
         var2.put(var4.name(), var4.vanished());
      }

      StaffListModule.r.j var13 = this.x.j(var2);
      int var15 = this.G.getValue();
      boolean var10 = false;
      byte var9 = (byte)var15;
      var15 = Integer.signum(var9);
      if ((var15 | -var15) >>> 31 != 0) {
         O("Staff online", var13.online(), s0.u.p, 5000L);
         O("Staff joined", var13.joined(), s0.u.p, 5000L);
         O("Staff left", var13.left(), s0.u.B, 4000L);
         O("Staff in Vanish", var13.vanished(), s0.u.M, 6000L);
      }
   }

   private static void O(String var0, List<String> var1, s0.u var2, long var3) {
      if (!var1.isEmpty()) {
         String var5 = var1.size() <= 3 ? String.join(", ", var1) : String.join(", ", var1.subList(0, 3)) + " +" + (var1.size() - 3);
         com.larpclient.h.A("StaffList", "{}: {}", var0, String.join(", ", var1));
         s0.s().k(var0, var5, var2, var3);
      }
   }

   public record e(String name, PlayerInfo info, StaffListModule.h status) {
      public boolean vanished() {
         return this.status == StaffListModule.h.y;
      }
   }

   public enum h {
      X("Online", -10551483),
      d("Near", -15821),
      N("Spectator", -44462),
      y("Vanish", -44462);

      public final String J;
      public final int T;

      h(String var3, int var4) {
         this.J = var3;
         this.T = var4;
      }
   }

   private record j(String raw, Set<String> lowerCase) {
   }

   public static final class r {
      public static final int S = 40;
      public static final int Y = 40;
      private final Map<String, Boolean> M = new LinkedHashMap<>();
      private final Map<String, Integer> d = new HashMap<>();
      private int Q;

      public void T() {
         this.M.clear();
         this.d.clear();
         this.Q = 0;
      }

      public StaffListModule.r.j j(Map<String, Boolean> var1) {
         ArrayList var2 = new ArrayList();
         ArrayList var3 = new ArrayList();
         ArrayList var4 = new ArrayList();
         ArrayList var5 = new ArrayList();
         if (this.Q < 40) {
            this.M.clear();
            this.M.putAll(var1);
            this.d.clear();
            if (++this.Q == 40) {
               var1.forEach((var2x, var3x) -> (var3x ? var5 : var2).add(var2x));
            }

            return new StaffListModule.r.j(var2, var3, var4, var5);
         } else {
            for (String var7 : List.copyOf(this.M.keySet())) {
               if (!var1.containsKey(var7)) {
                  this.M.remove(var7);
                  var4.add(var7);
               }
            }

            this.d.keySet().retainAll(var1.keySet());

            for (Entry var12 : var1.entrySet()) {
               String var8 = (String)var12.getKey();
               boolean var9 = (Boolean)var12.getValue();
               Boolean var10 = this.M.get(var8);
               if (var10 == null && !var9) {
                  this.d.remove(var8);
                  this.M.put(var8, false);
                  var3.add(var8);
               } else if (var10 != null && var10 == var9) {
                  this.d.remove(var8);
               } else if (this.d.merge(var8, 1, Integer::sum) >= 40) {
                  this.d.remove(var8);
                  this.M.put(var8, var9);
                  if (var9) {
                     var5.add(var8);
                  }
               }
            }

            return new StaffListModule.r.j(var2, var3, var4, var5);
         }
      }

      public record j(List<String> online, List<String> joined, List<String> left, List<String> vanished) {
      }
   }
}
