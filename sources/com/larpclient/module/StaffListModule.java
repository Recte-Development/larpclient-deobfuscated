package com.larpclient.module;

import com.larpclient.LarpClient;
import com.larpclient.e0;
import com.larpclient.mt;
import com.larpclient.oj;
import com.larpclient.op;
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
   public final dh j = this.addSetting(new dh("Player Heads", "Show staff player heads", true));
   public final dh S = this.addSetting(new dh("Alerts", "Notify when staff join, leave or go into Vanish", true));
   public final ds e = this.addSetting(
      new ds("Staff Names", "Players who always count as staff, separated by commas or spaces. Needed for Vanish and for servers that hide staff ranks", "")
   );
   private static final Pattern M = Pattern.compile("[.*]?[A-Za-z0-9_]{1,16}");
   private static volatile StaffListModule.n E = new StaffListModule.n("", Set.of());
   private final StaffListModule.s K = new StaffListModule.s();
   private final Set<String> d = new HashSet<>();
   private static final int G = 32;
   private static final int L = 20;
   private final Set<String> C = new HashSet<>();
   private boolean r;
   private boolean Q;
   private int O;

   public StaffListModule() {
      super("Staff List", "Show staff from server rank labels and your Staff Names list", Category.RENDER, -1);
      this.e.C("Name1, Name2");
   }

   private static StaffListModule e() {
      LarpClient var0 = LarpClient.F();
      return var0 == null ? null : var0.x().V(StaffListModule.class);
   }

   public static boolean L() {
      StaffListModule var0 = e();
      return var0 != null && var0.isOperational();
   }

   public static boolean e() {
      if (!L()) {
         return false;
      }

      ServerData var0 = Minecraft.getInstance().getCurrentServer();
      return var0 != null && A(var0.ip);
   }

   public static boolean A(String var0) {
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

   public static boolean X() {
      StaffListModule var0 = e();
      return var0 == null || var0.j.P();
   }

   public static Set<String> W() {
      StaffListModule var0 = e();
      String var1 = var0 == null ? "" : var0.e.P();
      StaffListModule.n var2 = E;
      if (!var2.raw().equals(var1)) {
         E = var2 = new StaffListModule.n(var1, Q(var1));
      }

      return var2.lowerCase();
   }

   public static Set<String> Q(String var0) {
      LinkedHashSet var1 = new LinkedHashSet();

      for (String var5 : var0.split("[\\s,;]+")) {
         if (!var5.isEmpty()) {
            var1.add(var5.toLowerCase(Locale.ROOT));
         }
      }

      return Collections.unmodifiableSet(var1);
   }

   public static List<StaffListModule.l> Q() {
      Minecraft var0 = Minecraft.getInstance();
      ArrayList var1 = new ArrayList();
      ClientPacketListener var2 = var0.getConnection();
      if (var2 != null && var0.level != null) {
         Set var3 = W();
         boolean var4 = e();
         HashSet var5 = new HashSet();

         for (PlayerInfo var7 : var2.getOnlinePlayers()) {
            String var8 = var7.getProfile().name();
            var5.add(var8.toLowerCase(Locale.ROOT));
            if (M.matcher(var8).matches() && j(var7, var3, var4)) {
               StaffListModule.y var9 = var7.getGameMode() == GameType.SPECTATOR
                  ? StaffListModule.y.N
                  : (var0.level.getPlayerByUUID(var7.getProfile().id()) != null ? StaffListModule.y.P : StaffListModule.y.p);
               var1.add(new StaffListModule.l(var8, var7, var9));
            }
         }

         if (!var3.isEmpty()) {
            for (PlayerTeam var12 : var0.level.getScoreboard().getPlayerTeams()) {
               for (String var14 : var12.getPlayers()) {
                  String var10 = var14.toLowerCase(Locale.ROOT);
                  if (var3.contains(var10) && var5.add(var10)) {
                     var1.add(new StaffListModule.l(var14, null, StaffListModule.y.t));
                  }
               }
            }
         }

         var1.sort(Comparator.comparing(StaffListModule.l::name, String.CASE_INSENSITIVE_ORDER));
         return var1;
      } else {
         return var1;
      }
   }

   private static boolean j(PlayerInfo var0, Set<String> var1, boolean var2) {
      if (var1.contains(var0.getProfile().name().toLowerCase(Locale.ROOT))) {
         return true;
      }

      if (var2) {
         String var3 = op.a().I(var0.getProfile().name());
         if (var3 != null && e0.w(var3)) {
            return true;
         }
      }

      PlayerTeam var5 = var0.getTeam();
      if (var5 == null || !e0.w(var5.getPlayerPrefix().getString()) && !e0.w(var5.getPlayerSuffix().getString())) {
         String[] var4 = z(var0);
         return var4 != null && (e0.w(var4[0]) || e0.w(var4[1]));
      } else {
         return true;
      }
   }

   private static String[] z(PlayerInfo var0) {
      Component var1 = var0.getTabListDisplayName();
      return var1 == null ? null : p(var1.getString(), var0.getProfile().name());
   }

   public static String[] p(String var0, String var1) {
      for (int var2 = 0; var2 + var1.length() <= var0.length(); var2++) {
         if (var0.regionMatches(true, var2, var1, 0, var1.length())) {
            return new String[]{var0.substring(0, var2), var0.substring(var2 + var1.length())};
         }
      }

      return null;
   }

   public static String w(String var0, String var1) {
      String var2 = (var0.strip() + " <name> " + var1.strip()).strip();
      return var2.equals("<name>") ? "" : var2.replaceAll("\\d+", "#");
   }

   private static List<String> z(PlayerInfo var0) {
      ArrayList var1 = new ArrayList(2);
      PlayerTeam var2 = var0.getTeam();
      if (var2 != null) {
         var1.add(w(var2.getPlayerPrefix().getString(), var2.getPlayerSuffix().getString()));
      }

      String[] var3 = z(var0);
      if (var3 != null) {
         var1.add(w(var3[0], var3[1]));
      }

      var1.removeIf(String::isEmpty);
      return var1;
   }

   @Override
   public void onEnable() {
      this.K.m();
      this.O = 0;
      this.d.clear();
      this.r = (boolean)0;
   }

   @Override
   public void onDisable() {
      this.K.m();
      this.O = 0;
      this.d.clear();
      this.r = (boolean)0;
   }

   private void q(List<StaffListModule.l> var1) {
      Minecraft var2 = Minecraft.getInstance();
      if (var2.getConnection() != null && var2.level != null) {
         LinkedHashSet var3 = new LinkedHashSet();
         int var4 = 0;
         int var5 = 0;

         for (PlayerInfo var7 : var2.getConnection().getOnlinePlayers()) {
            if (M.matcher(var7.getProfile().name()).matches()) {
               var4++;
               if (var7.getTabListDisplayName() != null && z(var7) == null) {
                  var5++;
               }

               var3.addAll(z(var7));
            }
         }

         this.d.addAll(var3);
         List var8 = var3.stream().limit(16L).map(var0 -> (z(var0) ? "+" : "-") + "'" + var0 + "'").toList();
         mt.K(
            "StaffList",
            "Detected {} staff ({} in Vanish) among {} players ({} real, {} with tab text that hides the name) with {} Staff Names; rank labels seen ({} total, + counts as staff): {}",
            var1.size(),
            var1.stream().filter(StaffListModule.l::vanished).count(),
            var2.getConnection().getOnlinePlayers().size(),
            var4,
            var5,
            W().size(),
            var3.size(),
            var8
         );
      }
   }

   private void H() {
      ClientPacketListener var1 = Minecraft.getInstance().getConnection();
      if (var1 != null && this.d.size() < 32) {
         for (PlayerInfo var3 : var1.getOnlinePlayers()) {
            if (M.matcher(var3.getProfile().name()).matches()) {
               for (String var5 : z(var3)) {
                  if (this.d.add(var5)) {
                     mt.K(
                        "StaffList",
                        "New rank label '{}' on {} (tab order {}, counts as staff: {})",
                        var5,
                        var3.getProfile().name(),
                        var3.getTabListOrder(),
                        z(var5) ? "yes" : "no"
                     );
                     if (this.d.size() >= 32) {
                        mt.K("StaffList", "Rank label log limit of {} reached for this world", 32);
                        return;
                     }
                  }
               }
            }
         }
      }
   }

   private void D() {
      op var1 = op.a();
      if (var1.J() && !var1.c()) {
         mt.K("StaffList", "DonutSMP detected; looking up tab players' ranks through DonutSMP's API");
      } else {
         mt.K(
            "StaffList",
            "DonutSMP detected but {}; staff ranks stay unknown until you run /api once",
            var1.J() ? "the API refused the saved key" : "no DonutSMP API key is saved"
         );
         if (!this.Q) {
            this.Q = (boolean)1;
            oj.K().g("Staff List", "Run /api once so DonutSMP staff ranks can be read", oj.u.O, 8000L);
         }
      }
   }

   private void B() {
      ClientPacketListener var1 = Minecraft.getInstance().getConnection();
      op var2 = op.a();
      if (var1 != null && var2.J()) {
         TreeMap var3 = new TreeMap();
         int var4 = 0;

         for (PlayerInfo var6 : var1.getOnlinePlayers()) {
            String var7 = var6.getProfile().name();
            if (M.matcher(var7).matches()) {
               String var8 = var2.I(var7);
               if (var8 == null) {
                  var4++;
               } else {
                  String var9 = var8.isBlank() ? "(none)" : var8;
                  var3.merge(var9, 1, Integer::sum);
                  if (this.C.add(var9)) {
                     mt.K("StaffList", "DonutSMP rank '{}' first seen on {}, counts as staff: {}", var9, var7, e0.w(var8) ? "yes" : "no");
                  }
               }
            }
         }

         if (var4 <= 0 && !this.r && !var3.isEmpty()) {
            this.r = (boolean)1;
            mt.K("StaffList", "DonutSMP ranks known for all {} tab players: {}", var3.values().stream().mapToInt(Integer::intValue).sum(), var3);
         }
      }
   }

   private static boolean z(String var0) {
      String[] var1 = p(var0, "<name>");
      return var1 != null && (e0.w(var1[0]) || e0.w(var1[1]));
   }

   @Override
   public void onTick() {
      byte var1 = e();
      boolean var7 = false;
      byte var6 = var1;
      int var10000 = Integer.signum(var6);
      if (((var10000 | -var10000) >>> 31 ^ 1) == 0) {
         op.a().O();
      }

      List var2 = Q();
      var10000 = ++this.O;
      byte var9 = 40;
      int var8 = var10000;
      var10000 = Integer.compare(var8, var9);
      if ((var10000 | -var10000) >>> 31 == 0) {
         this.q(var2);
         if (var1 != 0) {
            this.D();
         }
      } else {
         byte var11 = 40;
         int var10 = this.O;
         if ((-Integer.compare(var10, var11) >>> 31 ^ 1) == 0) {
            var10000 = this.O % 20;
            boolean var13 = false;
            int var12 = var10000;
            var10000 = Integer.signum(var12);
            if ((var10000 | -var10000) >>> 31 == 0) {
               this.H();
               if (var1 != 0) {
                  this.B();
               }
            }
         }
      }

      LinkedHashMap var3 = new LinkedHashMap();

      for (StaffListModule.l var5 : var2) {
         var3.put(var5.name(), var5.vanished());
      }

      StaffListModule.s.o var14 = this.K.F(var3);
      if (this.S.P()) {
         o("Staff online", var14.online(), oj.u.B, 5000L);
         o("Staff joined", var14.joined(), oj.u.B, 5000L);
         o("Staff left", var14.left(), oj.u.O, 4000L);
         o("Staff in Vanish", var14.vanished(), oj.u.i, 6000L);
      }
   }

   private static void o(String var0, List<String> var1, oj.u var2, long var3) {
      if (!var1.isEmpty()) {
         String var5 = var1.size() <= 3 ? String.join(", ", var1) : String.join(", ", var1.subList(0, 3)) + " +" + (var1.size() - 3);
         mt.K("StaffList", "{}: {}", var0, String.join(", ", var1));
         oj.K().g(var0, var5, var2, var3);
      }
   }

   public record l(String name, PlayerInfo info, StaffListModule.y status) {
      public boolean vanished() {
         return this.status == StaffListModule.y.t;
      }
   }

   private record n(String raw, Set<String> lowerCase) {
   }

   public static final class s {
      public static final int s = 40;
      public static final int y = 40;
      private final Map<String, Boolean> G = new LinkedHashMap<>();
      private final Map<String, Integer> B = new HashMap<>();
      private int J;

      public void m() {
         this.G.clear();
         this.B.clear();
         this.J = 0;
      }

      public StaffListModule.s.o F(Map<String, Boolean> var1) {
         ArrayList var2 = new ArrayList();
         ArrayList var3 = new ArrayList();
         ArrayList var4 = new ArrayList();
         ArrayList var5 = new ArrayList();
         if (this.J < 40) {
            this.G.clear();
            this.G.putAll(var1);
            this.B.clear();
            if (++this.J == 40) {
               var1.forEach((var2x, var3x) -> (var3x ? var5 : var2).add(var2x));
            }

            return new StaffListModule.s.o(var2, var3, var4, var5);
         } else {
            for (String var7 : List.copyOf(this.G.keySet())) {
               if (!var1.containsKey(var7)) {
                  this.G.remove(var7);
                  var4.add(var7);
               }
            }

            this.B.keySet().retainAll(var1.keySet());

            for (Entry var12 : var1.entrySet()) {
               String var8 = (String)var12.getKey();
               boolean var9 = (Boolean)var12.getValue();
               Boolean var10 = this.G.get(var8);
               if (var10 == null && !var9) {
                  this.B.remove(var8);
                  this.G.put(var8, false);
                  var3.add(var8);
               } else if (var10 != null && var10 == var9) {
                  this.B.remove(var8);
               } else if (this.B.merge(var8, 1, Integer::sum) >= 40) {
                  this.B.remove(var8);
                  this.G.put(var8, var9);
                  if (var9) {
                     var5.add(var8);
                  }
               }
            }

            return new StaffListModule.s.o(var2, var3, var4, var5);
         }
      }

      public record o(List<String> online, List<String> joined, List<String> left, List<String> vanished) {
      }
   }

   public enum y {
      p("Online", -10551483),
      P("Near", -15821),
      N("Spectator", -44462),
      t("Vanish", -44462);

      public final String O;
      public final int e;

      y(String var3, int var4) {
         this.O = var3;
         this.e = var4;
      }
   }
}
