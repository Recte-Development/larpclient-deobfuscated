package com.larpclient.module;

import com.larpclient.LarpClient;
import com.larpclient.hg;
import com.larpclient.p3;
import com.larpclient.r4;
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
import java.util.TreeMap;
import java.util.Map.Entry;
import java.util.regex.Pattern;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.GameType;
import net.minecraft.world.scores.PlayerTeam;

public final class StaffListModule extends Module {
   public final ln w = this.addSetting(new ln("Player Heads", "Show staff player heads", true));
   public final ln B = this.addSetting(new ln("Alerts", "Notify when staff join, leave or go into Vanish", true));
   public final li k = this.addSetting(
      new li("Staff Names", "Players who always count as staff, separated by commas or spaces. Needed for Vanish and for servers that hide staff ranks", "")
   );
   private static final Pattern a = Pattern.compile("[.*]?[A-Za-z0-9_]{1,16}");
   private static volatile StaffListModule.d F = new StaffListModule.d("", Set.of());
   private final StaffListModule.o P = new StaffListModule.o();
   private final Set<String> Z = new HashSet<>();
   private static final int C = 32;
   private static final int W = 20;
   private final Set<String> r = new HashSet<>();
   private boolean G;
   private boolean D;
   private int l;

   public StaffListModule() {
      super("Staff List", "Show staff from server rank labels and your Staff Names list", Category.RENDER, -1);
      this.k.R("Name1, Name2");
   }

   private static StaffListModule f() {
      LarpClient var0 = LarpClient.y();
      return var0 == null ? null : var0.e().y(StaffListModule.class);
   }

   public static boolean p() {
      StaffListModule var0 = f();
      return var0 != null && var0.isOperational();
   }

   public static boolean G() {
      if (!p()) {
         return false;
      }

      ServerData var0 = Minecraft.getInstance().getCurrentServer();
      return var0 != null && H(var0.ip);
   }

   public static boolean H(String var0) {
      if (var0 == null) {
         return false;
      }

      String var1 = var0.strip().toLowerCase(Locale.ROOT);
      int var2 = var1.lastIndexOf(58);
      if (var2 > 0 && var1.indexOf(58) == var2) {
         var1 = var1.substring(0, var2);
      }

      if (var1.endsWith(".")) {
         var1 = var1.substring(0, var1.length() - 1);
      }

      return var1.equals("donutsmp.net") || var1.endsWith(".donutsmp.net");
   }

   public static boolean W() {
      StaffListModule var0 = f();
      return var0 == null || var0.w.getValue();
   }

   public static Set<String> V() {
      StaffListModule var0 = f();
      String var1 = var0 == null ? "" : var0.k.getValue();
      StaffListModule.d var2 = F;
      if (!var2.raw().equals(var1)) {
         F = var2 = new StaffListModule.d(var1, S(var1));
      }

      return var2.lowerCase();
   }

   public static Set<String> S(String var0) {
      LinkedHashSet var1 = new LinkedHashSet();

      for (String var5 : var0.split("[\\s,;]+")) {
         if (!var5.isEmpty()) {
            var1.add(var5.toLowerCase(Locale.ROOT));
         }
      }

      return Collections.unmodifiableSet(var1);
   }

   public static List<StaffListModule.k> X() {
      Minecraft var0 = Minecraft.getInstance();
      ArrayList var1 = new ArrayList();
      ClientPacketListener var2 = var0.getConnection();
      if (var2 != null && var0.level != null) {
         Set var3 = V();
         boolean var4 = G();
         HashSet var5 = new HashSet();

         for (PlayerInfo var7 : var2.getOnlinePlayers()) {
            String var8 = var7.getProfile().name();
            var5.add(var8.toLowerCase(Locale.ROOT));
            if (a.matcher(var8).matches() && k(var7, var3, var4)) {
               StaffListModule.y var9 = var7.getGameMode() == GameType.SPECTATOR
                  ? StaffListModule.y.W
                  : (var0.level.getPlayerByUUID(var7.getProfile().id()) != null ? StaffListModule.y.n : StaffListModule.y.w);
               var1.add(new StaffListModule.k(var8, var7, var9));
            }
         }

         if (!var3.isEmpty()) {
            for (PlayerTeam var12 : var0.level.getScoreboard().getPlayerTeams()) {
               for (String var14 : var12.getPlayers()) {
                  String var10 = var14.toLowerCase(Locale.ROOT);
                  if (var3.contains(var10) && var5.add(var10)) {
                     var1.add(new StaffListModule.k(var14, null, StaffListModule.y.j));
                  }
               }
            }
         }

         var1.sort(Comparator.comparing(StaffListModule.k::name, String.CASE_INSENSITIVE_ORDER));
         return var1;
      } else {
         return var1;
      }
   }

   private static boolean k(PlayerInfo var0, Set<String> var1, boolean var2) {
      if (var1.contains(var0.getProfile().name().toLowerCase(Locale.ROOT))) {
         return true;
      }

      if (var2) {
         String var3 = com.larpclient.l7.q().h(var0.getProfile().name());
         if (var3 != null && p3.f(var3)) {
            return true;
         }
      }

      PlayerTeam var5 = var0.getTeam();
      if (var5 == null || !p3.f(var5.getPlayerPrefix().getString()) && !p3.f(var5.getPlayerSuffix().getString())) {
         String[] var4 = V(var0);
         return var4 != null && (p3.f(var4[0]) || p3.f(var4[1]));
      } else {
         return true;
      }
   }

   private static String[] V(PlayerInfo var0) {
      Component var1 = var0.getTabListDisplayName();
      return var1 == null ? null : w(var1.getString(), var0.getProfile().name());
   }

   public static String[] w(String var0, String var1) {
      for (int var2 = 0; var2 + var1.length() <= var0.length(); var2++) {
         if (var0.regionMatches(true, var2, var1, 0, var1.length())) {
            return new String[]{var0.substring(0, var2), var0.substring(var2 + var1.length())};
         }
      }

      return null;
   }

   public static String U(String var0, String var1) {
      String var2 = (var0.strip() + " <name> " + var1.strip()).strip();
      return var2.equals("<name>") ? "" : var2.replaceAll("\\d+", "#");
   }

   private static List<String> v(PlayerInfo var0) {
      ArrayList var1 = new ArrayList(2);
      PlayerTeam var2 = var0.getTeam();
      if (var2 != null) {
         var1.add(U(var2.getPlayerPrefix().getString(), var2.getPlayerSuffix().getString()));
      }

      String[] var3 = V(var0);
      if (var3 != null) {
         var1.add(U(var3[0], var3[1]));
      }

      var1.removeIf(String::isEmpty);
      return var1;
   }

   @Override
   public void onEnable() {
      this.P.c();
      this.l = 0;
      this.Z.clear();
      this.G = (boolean)0;
   }

   @Override
   public void onDisable() {
      this.P.c();
      this.l = 0;
      this.Z.clear();
      this.G = (boolean)0;
   }

   private void e(List<StaffListModule.k> var1) {
      Minecraft var2 = Minecraft.getInstance();
      if (var2.getConnection() != null && var2.level != null) {
         LinkedHashSet var3 = new LinkedHashSet();
         int var4 = 0;
         int var5 = 0;

         for (PlayerInfo var7 : var2.getConnection().getOnlinePlayers()) {
            if (a.matcher(var7.getProfile().name()).matches()) {
               var4++;
               if (var7.getTabListDisplayName() != null && V(var7) == null) {
                  var5++;
               }

               var3.addAll(v(var7));
            }
         }

         this.Z.addAll(var3);
         List var8 = var3.stream().limit(16L).map(var0 -> (r(var0) ? "+" : "-") + "'" + var0 + "'").toList();
         r4.V(
            "StaffList",
            "Detected {} staff ({} in Vanish) among {} players ({} real, {} with tab text that hides the name) with {} Staff Names; rank labels seen ({} total, + counts as staff): {}",
            var1.size(),
            var1.stream().filter(StaffListModule.k::vanished).count(),
            var2.getConnection().getOnlinePlayers().size(),
            var4,
            var5,
            V().size(),
            var3.size(),
            var8
         );
      }
   }

   private void a() {
      ClientPacketListener var1 = Minecraft.getInstance().getConnection();
      if (var1 != null && this.Z.size() < 32) {
         for (PlayerInfo var3 : var1.getOnlinePlayers()) {
            if (a.matcher(var3.getProfile().name()).matches()) {
               for (String var5 : v(var3)) {
                  if (this.Z.add(var5)) {
                     r4.V(
                        "StaffList",
                        "New rank label '{}' on {} (tab order {}, counts as staff: {})",
                        var5,
                        var3.getProfile().name(),
                        var3.getTabListOrder(),
                        r(var5) ? "yes" : "no"
                     );
                     if (this.Z.size() >= 32) {
                        r4.V("StaffList", "Rank label log limit of {} reached for this world", 32);
                        return;
                     }
                  }
               }
            }
         }
      }
   }

   private void Q() {
      com.larpclient.l7 var1 = com.larpclient.l7.q();
      if (var1.b() && !var1.U()) {
         r4.V("StaffList", "DonutSMP detected; looking up tab players' ranks through DonutSMP's API");
      } else {
         r4.V(
            "StaffList",
            "DonutSMP detected but {}; staff ranks stay unknown until you run /api once",
            var1.b() ? "the API refused the saved key" : "no DonutSMP API key is saved"
         );
         if (!this.D) {
            this.D = (boolean)1;
            hg.B().Y("Staff List", "Run /api once so DonutSMP staff ranks can be read", hg.u.U, 8000L);
         }
      }
   }

   private void J() {
      ClientPacketListener var1 = Minecraft.getInstance().getConnection();
      com.larpclient.l7 var2 = com.larpclient.l7.q();
      if (var1 != null && var2.b()) {
         TreeMap var3 = new TreeMap();
         int var4 = 0;

         for (PlayerInfo var6 : var1.getOnlinePlayers()) {
            String var7 = var6.getProfile().name();
            if (a.matcher(var7).matches()) {
               String var8 = var2.h(var7);
               if (var8 == null) {
                  var4++;
               } else {
                  String var9 = var8.isBlank() ? "(none)" : var8;
                  var3.merge(var9, 1, Integer::sum);
                  if (this.r.add(var9)) {
                     r4.V("StaffList", "DonutSMP rank '{}' first seen on {}, counts as staff: {}", var9, var7, p3.f(var8) ? "yes" : "no");
                  }
               }
            }
         }

         if (var4 <= 0 && !this.G && !var3.isEmpty()) {
            this.G = (boolean)1;
            r4.V("StaffList", "DonutSMP ranks known for all {} tab players: {}", var3.values().stream().mapToInt(Integer::intValue).sum(), var3);
         }
      }
   }

   private static boolean r(String var0) {
      String[] var1 = w(var0, "<name>");
      return var1 != null && (p3.f(var1[0]) || p3.f(var1[1]));
   }

   @Override
   public void onTick() {
      byte var1 = G();
      if (var1 != 0) {
         com.larpclient.l7.q().n();
      }

      List var2 = X();
      byte var7 = 1;
      int var6 = this.l;
      int var10000 = this.l = var6 + var7;
      byte var9 = 40;
      int var8 = var10000;
      var10000 = Integer.compare(var8, var9);
      if ((var10000 | -var10000) >>> 31 == 0) {
         this.e(var2);
         if (var1 != 0) {
            this.Q();
         }
      } else {
         byte var11 = 40;
         int var10 = this.l;
         if ((-Integer.compare(var10, var11) >>> 31 ^ 1) == 0 && this.l % 20 == 0) {
            this.a();
            boolean var13 = false;
            byte var12 = var1;
            var10000 = Integer.signum(var12);
            if (((var10000 | -var10000) >>> 31 ^ 1) == 0) {
               this.J();
            }
         }
      }

      LinkedHashMap var3 = new LinkedHashMap();

      for (StaffListModule.k var5 : var2) {
         var3.put(var5.name(), var5.vanished());
      }

      StaffListModule.o.h var14 = this.P.O(var3);
      if (this.B.getValue()) {
         Z("Staff online", var14.online(), hg.u.Z, 5000L);
         Z("Staff joined", var14.joined(), hg.u.Z, 5000L);
         Z("Staff left", var14.left(), hg.u.U, 4000L);
         Z("Staff in Vanish", var14.vanished(), hg.u.y, 6000L);
      }
   }

   private static void Z(String var0, List<String> var1, hg.u var2, long var3) {
      if (!var1.isEmpty()) {
         String var5 = var1.size() <= 3 ? String.join(", ", var1) : String.join(", ", var1.subList(0, 3)) + " +" + (var1.size() - 3);
         r4.V("StaffList", "{}: {}", var0, String.join(", ", var1));
         hg.B().Y(var0, var5, var2, var3);
      }
   }

   private record d(String raw, Set<String> lowerCase) {
   }

   public record k(String name, PlayerInfo info, StaffListModule.y status) {
      public boolean vanished() {
         return this.status == StaffListModule.y.j;
      }
   }

   public static final class o {
      public static final int x = 40;
      public static final int V = 40;
      private final Map<String, Boolean> T = new LinkedHashMap<>();
      private final Map<String, Integer> M = new HashMap<>();
      private int Q;

      public void c() {
         this.T.clear();
         this.M.clear();
         this.Q = 0;
      }

      public StaffListModule.o.h O(Map<String, Boolean> var1) {
         ArrayList var2 = new ArrayList();
         ArrayList var3 = new ArrayList();
         ArrayList var4 = new ArrayList();
         ArrayList var5 = new ArrayList();
         if (this.Q < 40) {
            this.T.clear();
            this.T.putAll(var1);
            this.M.clear();
            if (++this.Q == 40) {
               var1.forEach((var2x, var3x) -> (var3x ? var5 : var2).add(var2x));
            }

            return new StaffListModule.o.h(var2, var3, var4, var5);
         } else {
            for (String var7 : List.copyOf(this.T.keySet())) {
               if (!var1.containsKey(var7)) {
                  this.T.remove(var7);
                  var4.add(var7);
               }
            }

            this.M.keySet().retainAll(var1.keySet());

            for (Entry var12 : var1.entrySet()) {
               String var8 = (String)var12.getKey();
               boolean var9 = (Boolean)var12.getValue();
               Boolean var10 = this.T.get(var8);
               if (var10 == null && !var9) {
                  this.M.remove(var8);
                  this.T.put(var8, false);
                  var3.add(var8);
               } else if (var10 != null && var10 == var9) {
                  this.M.remove(var8);
               } else if (this.M.merge(var8, 1, Integer::sum) >= 40) {
                  this.M.remove(var8);
                  this.T.put(var8, var9);
                  if (var9) {
                     var5.add(var8);
                  }
               }
            }

            return new StaffListModule.o.h(var2, var3, var4, var5);
         }
      }

      public record h(List<String> online, List<String> joined, List<String> left, List<String> vanished) {
      }
   }

   public enum y {
      w("Online", -10551483),
      n("Near", -15821),
      W("Spectator", -44462),
      j("Vanish", -44462);

      public final String g;
      public final int r;

      y(String var3, int var4) {
         this.g = var3;
         this.r = var4;
      }
   }
}
