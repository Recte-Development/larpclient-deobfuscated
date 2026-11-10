package com.larpclient.module;

import com.larpclient.LarpClient;
import com.larpclient._q;
import com.larpclient.ib;
import com.larpclient.nv;
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
   public final com.larpclient.module.m r = this.addSetting(new com.larpclient.module.m("Player Heads", "Show staff player heads", true));
   public final com.larpclient.module.m Z = this.addSetting(new com.larpclient.module.m("Alerts", "Notify when staff join, leave or go into Vanish", true));
   public final e T = this.addSetting(
      new e("Staff Names", "Players who always count as staff, separated by commas or spaces. Needed for Vanish and for servers that hide staff ranks", "")
   );
   private static final Pattern Y = Pattern.compile("[.*]?[A-Za-z0-9_]{1,16}");
   private static volatile StaffListModule.z K = new StaffListModule.z("", Set.of());
   private final StaffListModule.f m = new StaffListModule.f();
   private final Set<String> X = new HashSet<>();
   private static final int F = 32;
   private static final int G = 20;
   private final Set<String> b = new HashSet<>();
   private static final String k = "★☆✦✧✪✩✫✬✭✮✯⭐✰❂⚝✴✵✶✷✸✹⍟";
   private int y;

   public StaffListModule() {
      super("Staff List", "Show staff from rank words and staff symbols in the tab list, and your Staff Names list", Category.RENDER, -1);
      this.T.L("Name1, Name2");
   }

   private static StaffListModule y() {
      LarpClient var0 = LarpClient.N();
      return var0 == null ? null : var0.j().y(StaffListModule.class);
   }

   public static boolean G() {
      StaffListModule var0 = y();
      return var0 != null && var0.isOperational();
   }

   public static boolean D() {
      StaffListModule var0 = y();
      return var0 == null || var0.r.getValue();
   }

   public static Set<String> m() {
      StaffListModule var0 = y();
      String var1 = var0 == null ? "" : var0.T.getValue();
      StaffListModule.z var2 = K;
      if (!var2.raw().equals(var1)) {
         K = var2 = new StaffListModule.z(var1, G(var1));
      }

      return var2.lowerCase();
   }

   public static Set<String> G(String var0) {
      LinkedHashSet var1 = new LinkedHashSet();

      for (String var5 : var0.split("[\\s,;]+")) {
         if (!var5.isEmpty()) {
            var1.add(var5.toLowerCase(Locale.ROOT));
         }
      }

      return Collections.unmodifiableSet(var1);
   }

   public static List<StaffListModule.o> F() {
      Minecraft var0 = Minecraft.getInstance();
      ArrayList var1 = new ArrayList();
      ClientPacketListener var2 = var0.getConnection();
      if (var2 != null && var0.level != null) {
         Set var3 = m();
         HashSet var4 = new HashSet();

         for (PlayerInfo var6 : var2.getOnlinePlayers()) {
            String var7 = var6.getProfile().name();
            var4.add(var7.toLowerCase(Locale.ROOT));
            if (Y.matcher(var7).matches() && n(var6, var3)) {
               StaffListModule.m var8 = var6.getGameMode() == GameType.SPECTATOR
                  ? StaffListModule.m.k
                  : (var0.level.getPlayerByUUID(var6.getProfile().id()) != null ? StaffListModule.m.Y : StaffListModule.m.z);
               var1.add(new StaffListModule.o(var7, var6, var8));
            }
         }

         if (!var3.isEmpty()) {
            for (PlayerTeam var11 : var0.level.getScoreboard().getPlayerTeams()) {
               for (String var13 : var11.getPlayers()) {
                  String var9 = var13.toLowerCase(Locale.ROOT);
                  if (var3.contains(var9) && var4.add(var9)) {
                     var1.add(new StaffListModule.o(var13, null, StaffListModule.m.b));
                  }
               }
            }
         }

         var1.sort(Comparator.comparing(StaffListModule.o::name, String.CASE_INSENSITIVE_ORDER));
         return var1;
      } else {
         return var1;
      }
   }

   private static boolean n(PlayerInfo var0, Set<String> var1) {
      if (var1.contains(var0.getProfile().name().toLowerCase(Locale.ROOT))) {
         return true;
      } else {
         PlayerTeam var2 = var0.getTeam();
         if (var2 == null || !B(var2.getPlayerPrefix().getString()) && !B(var2.getPlayerSuffix().getString())) {
            String[] var3 = H(var0);
            return var3 != null && (B(var3[0]) || B(var3[1]));
         } else {
            return true;
         }
      }
   }

   public static boolean B(String var0) {
      return nv.i(var0) || C(var0);
   }

   public static boolean C(String var0) {
      for (int var1 = 0; var1 < var0.length(); var1++) {
         char var2 = var0.charAt(var1);
         if ("★☆✦✧✪✩✫✬✭✮✯⭐✰❂⚝✴✵✶✷✸✹⍟".indexOf(var2) >= 0 || var2 >= '\ue000' && var2 <= '\uf8ff') {
            return true;
         }
      }

      return false;
   }

   private static String[] H(PlayerInfo var0) {
      Component var1 = var0.getTabListDisplayName();
      return var1 == null ? null : z(var1.getString(), var0.getProfile().name());
   }

   public static String[] z(String var0, String var1) {
      for (int var2 = 0; var2 + var1.length() <= var0.length(); var2++) {
         if (var0.regionMatches(true, var2, var1, 0, var1.length())) {
            return new String[]{var0.substring(0, var2), var0.substring(var2 + var1.length())};
         }
      }

      return null;
   }

   public static String y(String var0, String var1) {
      String var2 = (var0.strip() + " <name> " + var1.strip()).strip();
      return var2.equals("<name>") ? "" : var2.replaceAll("\\d+", "#");
   }

   private static List<String> Y(PlayerInfo var0) {
      ArrayList var1 = new ArrayList(2);
      PlayerTeam var2 = var0.getTeam();
      if (var2 != null) {
         var1.add(y(var2.getPlayerPrefix().getString(), var2.getPlayerSuffix().getString()));
      }

      String[] var3 = H(var0);
      if (var3 != null) {
         var1.add(y(var3[0], var3[1]));
      }

      var1.removeIf(String::isEmpty);
      return var1;
   }

   @Override
   public void onEnable() {
      this.m.V();
      this.y = 0;
      this.X.clear();
      this.b.clear();
   }

   @Override
   public void onDisable() {
      this.m.V();
      this.y = 0;
      this.X.clear();
      this.b.clear();
   }

   private void R(List<StaffListModule.o> var1) {
      Minecraft var2 = Minecraft.getInstance();
      if (var2.getConnection() != null && var2.level != null) {
         LinkedHashSet var3 = new LinkedHashSet();
         int var4 = 0;
         int var5 = 0;

         for (PlayerInfo var7 : var2.getConnection().getOnlinePlayers()) {
            if (Y.matcher(var7.getProfile().name()).matches()) {
               var4++;
               if (var7.getTabListDisplayName() != null && H(var7) == null) {
                  var5++;
               }

               var3.addAll(Y(var7));
            }
         }

         this.X.addAll(var3);
         List var8 = var3.stream().limit(16L).map(var0 -> (e(var0) ? "+" : "-") + "'" + var0 + "'").toList();
         _q.z(
            "StaffList",
            "Detected {} staff ({} in Vanish) among {} players ({} real, {} with tab text that hides the name) with {} Staff Names; rank labels seen ({} total, + counts as staff): {}",
            var1.size(),
            var1.stream().filter(StaffListModule.o::vanished).count(),
            var2.getConnection().getOnlinePlayers().size(),
            var4,
            var5,
            m().size(),
            var3.size(),
            var8
         );
      }
   }

   private void D() {
      ClientPacketListener var1 = Minecraft.getInstance().getConnection();
      if (var1 != null && this.X.size() < 32) {
         for (PlayerInfo var3 : var1.getOnlinePlayers()) {
            if (Y.matcher(var3.getProfile().name()).matches()) {
               for (String var5 : Y(var3)) {
                  if (this.X.add(var5)) {
                     _q.z(
                        "StaffList",
                        "New rank label '{}' on {} (tab order {}, counts as staff: {})",
                        var5,
                        var3.getProfile().name(),
                        var3.getTabListOrder(),
                        e(var5) ? "yes" : "no"
                     );
                     if (this.X.size() >= 32) {
                        _q.z("StaffList", "Rank label log limit of {} reached for this world", 32);
                        return;
                     }
                  }
               }
            }
         }
      }
   }

   private static boolean e(String var0) {
      String[] var1 = z(var0, "<name>");
      return var1 != null && (B(var1[0]) || B(var1[1]));
   }

   private void j(List<StaffListModule.o> var1) {
      Set var2 = m();

      for (StaffListModule.o var4 : var1) {
         if (this.b.size() >= 256) {
            return;
         }

         if (var4.info() != null && this.b.add(var4.name().toLowerCase(Locale.ROOT))) {
            PlayerInfo var5 = var4.info();
            String var6 = var2.contains(var4.name().toLowerCase(Locale.ROOT))
               ? "Staff Names"
               : Y(var5)
                  .stream()
                  .filter(StaffListModule::e)
                  .findFirst()
                  .map(var0 -> (C(var0) ? "staff symbol in '" : "rank word in '") + var0 + "'")
                  .orElse("rank label");
            _q.z("StaffList", "{} counts as staff by {}", var4.name(), var6);
         }
      }
   }

   @Override
   public void onTick() {
      List var1 = F();
      int var10000 = ++this.y;
      byte var6 = 40;
      int var5 = var10000;
      var10000 = Integer.compare(var5, var6);
      if ((var10000 | -var10000) >>> 31 == 0) {
         this.R(var1);
         this.j(var1);
      } else {
         byte var10 = 40;
         int var9 = this.y;
         if ((-Integer.compare(var9, var10) >>> 31 ^ 1) == 0) {
            var10000 = this.y % 20;
            boolean var12 = false;
            int var11 = var10000;
            var10000 = Integer.signum(var11);
            if ((var10000 | -var10000) >>> 31 == 0) {
               this.D();
               this.j(var1);
            }
         }
      }

      LinkedHashMap var2 = new LinkedHashMap();

      for (StaffListModule.o var4 : var1) {
         var2.put(var4.name(), var4.vanished());
      }

      StaffListModule.f.n var13 = this.m.u(var2);
      int var17 = this.Z.getValue();
      boolean var8 = false;
      byte var7 = (byte)var17;
      var17 = Integer.signum(var7);
      if ((var17 | -var17) >>> 31 != 0) {
         n("Staff online", var13.online(), ib.v.o, 5000L);
         n("Staff joined", var13.joined(), ib.v.o, 5000L);
         n("Staff left", var13.left(), ib.v.f, 4000L);
         n("Staff in Vanish", var13.vanished(), ib.v.V, 6000L);
      }
   }

   private static void n(String var0, List<String> var1, ib.v var2, long var3) {
      if (!var1.isEmpty()) {
         String var5 = var1.size() <= 3 ? String.join(", ", var1) : String.join(", ", var1.subList(0, 3)) + " +" + (var1.size() - 3);
         _q.z("StaffList", "{}: {}", var0, String.join(", ", var1));
         ib.L().N(var0, var5, var2, var3);
      }
   }

   public static final class f {
      public static final int K = 40;
      public static final int o = 40;
      private final Map<String, Boolean> C = new LinkedHashMap<>();
      private final Map<String, Integer> A = new HashMap<>();
      private int W;

      public void V() {
         this.C.clear();
         this.A.clear();
         this.W = 0;
      }

      public StaffListModule.f.n u(Map<String, Boolean> var1) {
         ArrayList var2 = new ArrayList();
         ArrayList var3 = new ArrayList();
         ArrayList var4 = new ArrayList();
         ArrayList var5 = new ArrayList();
         if (this.W < 40) {
            this.C.clear();
            this.C.putAll(var1);
            this.A.clear();
            if (++this.W == 40) {
               var1.forEach((var2x, var3x) -> (var3x ? var5 : var2).add(var2x));
            }

            return new StaffListModule.f.n(var2, var3, var4, var5);
         } else {
            for (String var7 : List.copyOf(this.C.keySet())) {
               if (!var1.containsKey(var7)) {
                  this.C.remove(var7);
                  var4.add(var7);
               }
            }

            this.A.keySet().retainAll(var1.keySet());

            for (Entry var12 : var1.entrySet()) {
               String var8 = (String)var12.getKey();
               boolean var9 = (Boolean)var12.getValue();
               Boolean var10 = this.C.get(var8);
               if (var10 == null && !var9) {
                  this.A.remove(var8);
                  this.C.put(var8, false);
                  var3.add(var8);
               } else if (var10 != null && var10 == var9) {
                  this.A.remove(var8);
               } else if (this.A.merge(var8, 1, Integer::sum) >= 40) {
                  this.A.remove(var8);
                  this.C.put(var8, var9);
                  if (var9) {
                     var5.add(var8);
                  }
               }
            }

            return new StaffListModule.f.n(var2, var3, var4, var5);
         }
      }

      public record n(List<String> online, List<String> joined, List<String> left, List<String> vanished) {
      }
   }

   public enum m {
      z("Online", -10551483),
      Y("Near", -15821),
      k("Spectator", -44462),
      b("Vanish", -44462);

      public final String d;
      public final int B;

      m(String var3, int var4) {
         this.d = var3;
         this.B = var4;
      }
   }

   public record o(String name, PlayerInfo info, StaffListModule.m status) {
      public boolean vanished() {
         return this.status == StaffListModule.m.b;
      }
   }

   private record z(String raw, Set<String> lowerCase) {
   }
}
