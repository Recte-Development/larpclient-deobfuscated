package com.larpclient.module;

import com.larpclient.LarpClient;
import com.larpclient.q8;
import com.larpclient.wt;
import com.larpclient.y4;
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
import net.minecraft.class_1934;
import net.minecraft.class_2561;
import net.minecraft.class_268;
import net.minecraft.class_310;
import net.minecraft.class_634;
import net.minecraft.class_640;

public final class StaffListModule extends Module {
   public final l3 j = this.addSetting(new l3("Player Heads", "Show staff player heads", true));
   public final l3 C = this.addSetting(new l3("Alerts", "Notify when staff join, leave or go into Vanish", true));
   public final aj S = this.addSetting(
      new aj("Staff Names", "Players who always count as staff, separated by commas or spaces. Needed for Vanish and for servers that hide staff ranks", "")
   );
   private static final Pattern f = Pattern.compile("[.*]?[A-Za-z0-9_]{1,16}");
   private static volatile StaffListModule.d b = new StaffListModule.d("", Set.of());
   private final StaffListModule.f F = new StaffListModule.f();
   private final Set<String> Y = new HashSet<>();
   private static final int v = 32;
   private static final int u = 20;
   private final Set<String> B = new HashSet<>();
   private static final String h = "★☆✦✧✪✩✫✬✭✮✯⭐✰❂⚝✴✵✶✷✸✹⍟";
   private int R;

   public StaffListModule() {
      super("Staff List", "Show staff from rank words and staff symbols in the tab list, and your Staff Names list", Category.RENDER, -1);
      this.S.A("Name1, Name2");
   }

   private static StaffListModule o() {
      LarpClient var0 = LarpClient.b();
      return var0 == null ? null : var0.G().b(StaffListModule.class);
   }

   public static boolean a() {
      StaffListModule var0 = o();
      return var0 != null && var0.isOperational();
   }

   public static boolean j() {
      StaffListModule var0 = o();
      return var0 == null || var0.j.getValue();
   }

   public static Set<String> J() {
      StaffListModule var0 = o();
      String var1 = var0 == null ? "" : var0.S.getValue();
      StaffListModule.d var2 = b;
      if (!var2.raw().equals(var1)) {
         b = var2 = new StaffListModule.d(var1, D(var1));
      }

      return var2.lowerCase();
   }

   public static Set<String> D(String var0) {
      LinkedHashSet var1 = new LinkedHashSet();

      for (String var5 : var0.split("[\\s,;]+")) {
         if (!var5.isEmpty()) {
            var1.add(var5.toLowerCase(Locale.ROOT));
         }
      }

      return Collections.unmodifiableSet(var1);
   }

   public static List<StaffListModule.z> L() {
      class_310 var0 = class_310.method_1551();
      ArrayList var1 = new ArrayList();
      class_634 var2 = var0.method_1562();
      if (var2 != null && var0.field_1687 != null) {
         Set var3 = J();
         HashSet var4 = new HashSet();

         for (class_640 var6 : var2.method_2880()) {
            String var7 = var6.method_2966().name();
            var4.add(var7.toLowerCase(Locale.ROOT));
            if (f.matcher(var7).matches() && L(var6, var3)) {
               StaffListModule.n var8 = var6.method_2958() == class_1934.field_9219
                  ? StaffListModule.n.W
                  : (var0.field_1687.method_18470(var6.method_2966().id()) != null ? StaffListModule.n.A : StaffListModule.n.M);
               var1.add(new StaffListModule.z(var7, var6, var8));
            }
         }

         if (!var3.isEmpty()) {
            for (class_268 var11 : var0.field_1687.method_8428().method_1159()) {
               for (String var13 : var11.method_1204()) {
                  String var9 = var13.toLowerCase(Locale.ROOT);
                  if (var3.contains(var9) && var4.add(var9)) {
                     var1.add(new StaffListModule.z(var13, null, StaffListModule.n.r));
                  }
               }
            }
         }

         var1.sort(Comparator.comparing(StaffListModule.z::name, String.CASE_INSENSITIVE_ORDER));
         return var1;
      } else {
         return var1;
      }
   }

   private static boolean L(class_640 var0, Set<String> var1) {
      if (var1.contains(var0.method_2966().name().toLowerCase(Locale.ROOT))) {
         return true;
      } else {
         class_268 var2 = var0.method_2955();
         if (var2 == null || !e(var2.method_1144().getString()) && !e(var2.method_1136().getString())) {
            String[] var3 = A(var0);
            return var3 != null && (e(var3[0]) || e(var3[1]));
         } else {
            return true;
         }
      }
   }

   public static boolean e(String var0) {
      return y4.x(var0) || M(var0);
   }

   public static boolean M(String var0) {
      for (int var1 = 0; var1 < var0.length(); var1++) {
         char var2 = var0.charAt(var1);
         if ("★☆✦✧✪✩✫✬✭✮✯⭐✰❂⚝✴✵✶✷✸✹⍟".indexOf(var2) >= 0 || var2 >= '\ue000' && var2 <= '\uf8ff') {
            return true;
         }
      }

      return false;
   }

   private static String[] A(class_640 var0) {
      class_2561 var1 = var0.method_2971();
      return var1 == null ? null : A(var1.getString(), var0.method_2966().name());
   }

   public static String[] A(String var0, String var1) {
      for (int var2 = 0; var2 + var1.length() <= var0.length(); var2++) {
         if (var0.regionMatches(true, var2, var1, 0, var1.length())) {
            return new String[]{var0.substring(0, var2), var0.substring(var2 + var1.length())};
         }
      }

      return null;
   }

   public static String h(String var0, String var1) {
      String var2 = (var0.strip() + " <name> " + var1.strip()).strip();
      return var2.equals("<name>") ? "" : var2.replaceAll("\\d+", "#");
   }

   private static List<String> s(class_640 var0) {
      ArrayList var1 = new ArrayList(2);
      class_268 var2 = var0.method_2955();
      if (var2 != null) {
         var1.add(h(var2.method_1144().getString(), var2.method_1136().getString()));
      }

      String[] var3 = A(var0);
      if (var3 != null) {
         var1.add(h(var3[0], var3[1]));
      }

      var1.removeIf(String::isEmpty);
      return var1;
   }

   @Override
   public void onEnable() {
      this.F.P();
      this.R = 0;
      this.Y.clear();
      this.B.clear();
   }

   @Override
   public void onDisable() {
      this.F.P();
      this.R = 0;
      this.Y.clear();
      this.B.clear();
   }

   private void I(List<StaffListModule.z> var1) {
      class_310 var2 = class_310.method_1551();
      if (var2.method_1562() != null && var2.field_1687 != null) {
         LinkedHashSet var3 = new LinkedHashSet();
         int var4 = 0;
         int var5 = 0;

         for (class_640 var7 : var2.method_1562().method_2880()) {
            if (f.matcher(var7.method_2966().name()).matches()) {
               var4++;
               if (var7.method_2971() != null && A(var7) == null) {
                  var5++;
               }

               var3.addAll(s(var7));
            }
         }

         this.Y.addAll(var3);
         List var8 = var3.stream().limit(16L).map(var0 -> (A(var0) ? "+" : "-") + "'" + var0 + "'").toList();
         q8.G(
            "StaffList",
            "Detected {} staff ({} in Vanish) among {} players ({} real, {} with tab text that hides the name) with {} Staff Names; rank labels seen ({} total, + counts as staff): {}",
            var1.size(),
            var1.stream().filter(StaffListModule.z::vanished).count(),
            var2.method_1562().method_2880().size(),
            var4,
            var5,
            J().size(),
            var3.size(),
            var8
         );
      }
   }

   private void A() {
      class_634 var1 = class_310.method_1551().method_1562();
      if (var1 != null && this.Y.size() < 32) {
         for (class_640 var3 : var1.method_2880()) {
            if (f.matcher(var3.method_2966().name()).matches()) {
               for (String var5 : s(var3)) {
                  if (this.Y.add(var5)) {
                     q8.G(
                        "StaffList",
                        "New rank label '{}' on {} (tab order {}, counts as staff: {})",
                        var5,
                        var3.method_2966().name(),
                        var3.method_62154(),
                        A(var5) ? "yes" : "no"
                     );
                     if (this.Y.size() >= 32) {
                        q8.G("StaffList", "Rank label log limit of {} reached for this world", 32);
                        return;
                     }
                  }
               }
            }
         }
      }
   }

   private static boolean A(String var0) {
      String[] var1 = A(var0, "<name>");
      return var1 != null && (e(var1[0]) || e(var1[1]));
   }

   private void e(List<StaffListModule.z> var1) {
      Set var2 = J();

      for (StaffListModule.z var4 : var1) {
         if (this.B.size() >= 256) {
            return;
         }

         if (var4.info() != null && this.B.add(var4.name().toLowerCase(Locale.ROOT))) {
            class_640 var5 = var4.info();
            String var6 = var2.contains(var4.name().toLowerCase(Locale.ROOT))
               ? "Staff Names"
               : s(var5)
                  .stream()
                  .filter(StaffListModule::A)
                  .findFirst()
                  .map(var0 -> (M(var0) ? "staff symbol in '" : "rank word in '") + var0 + "'")
                  .orElse("rank label");
            q8.G("StaffList", "{} counts as staff by {}", var4.name(), var6);
         }
      }
   }

   @Override
   public void onTick() {
      List var1 = L();
      byte var6 = 1;
      int var5 = this.R;
      int var10000 = this.R = var5 + var6;
      byte var8 = 40;
      int var7 = var10000;
      var10000 = Integer.compare(var7, var8);
      if ((var10000 | -var10000) >>> 31 == 0) {
         this.I(var1);
         this.e(var1);
      } else {
         byte var12 = 40;
         int var11 = this.R;
         if ((-Integer.compare(var11, var12) >>> 31 ^ 1) == 0 && this.R % 20 == 0) {
            this.A();
            this.e(var1);
         }
      }

      LinkedHashMap var2 = new LinkedHashMap();

      for (StaffListModule.z var4 : var1) {
         var2.put(var4.name(), var4.vanished());
      }

      StaffListModule.f.w var13 = this.F.B(var2);
      int var15 = this.C.getValue();
      boolean var10 = false;
      byte var9 = (byte)var15;
      var15 = Integer.signum(var9);
      if ((var15 | -var15) >>> 31 != 0) {
         v("Staff online", var13.online(), wt.s.a, 5000L);
         v("Staff joined", var13.joined(), wt.s.a, 5000L);
         v("Staff left", var13.left(), wt.s.R, 4000L);
         v("Staff in Vanish", var13.vanished(), wt.s.u, 6000L);
      }
   }

   private static void v(String var0, List<String> var1, wt.s var2, long var3) {
      if (!var1.isEmpty()) {
         String var5 = var1.size() <= 3 ? String.join(", ", var1) : String.join(", ", var1.subList(0, 3)) + " +" + (var1.size() - 3);
         q8.G("StaffList", "{}: {}", var0, String.join(", ", var1));
         wt.T().k(var0, var5, var2, var3);
      }
   }

   private record d(String raw, Set<String> lowerCase) {
   }

   public static final class f {
      public static final int R = 40;
      public static final int U = 40;
      private final Map<String, Boolean> a = new LinkedHashMap<>();
      private final Map<String, Integer> I = new HashMap<>();
      private int E;

      public void P() {
         this.a.clear();
         this.I.clear();
         this.E = 0;
      }

      public StaffListModule.f.w B(Map<String, Boolean> var1) {
         ArrayList var2 = new ArrayList();
         ArrayList var3 = new ArrayList();
         ArrayList var4 = new ArrayList();
         ArrayList var5 = new ArrayList();
         if (this.E < 40) {
            this.a.clear();
            this.a.putAll(var1);
            this.I.clear();
            if (++this.E == 40) {
               var1.forEach((var2x, var3x) -> (var3x ? var5 : var2).add(var2x));
            }

            return new StaffListModule.f.w(var2, var3, var4, var5);
         } else {
            for (String var7 : List.copyOf(this.a.keySet())) {
               if (!var1.containsKey(var7)) {
                  this.a.remove(var7);
                  var4.add(var7);
               }
            }

            this.I.keySet().retainAll(var1.keySet());

            for (Entry var12 : var1.entrySet()) {
               String var8 = (String)var12.getKey();
               boolean var9 = (Boolean)var12.getValue();
               Boolean var10 = this.a.get(var8);
               if (var10 == null && !var9) {
                  this.I.remove(var8);
                  this.a.put(var8, false);
                  var3.add(var8);
               } else if (var10 != null && var10 == var9) {
                  this.I.remove(var8);
               } else if (this.I.merge(var8, 1, Integer::sum) >= 40) {
                  this.I.remove(var8);
                  this.a.put(var8, var9);
                  if (var9) {
                     var5.add(var8);
                  }
               }
            }

            return new StaffListModule.f.w(var2, var3, var4, var5);
         }
      }

      public record w(List<String> online, List<String> joined, List<String> left, List<String> vanished) {
      }
   }

   public enum n {
      M("Online", -10551483),
      A("Near", -15821),
      W("Spectator", -44462),
      r("Vanish", -44462);

      public final String K;
      public final int g;

      n(String var3, int var4) {
         this.K = var3;
         this.g = var4;
      }
   }

   public record z(String name, class_640 info, StaffListModule.n status) {
      public boolean vanished() {
         return this.status == StaffListModule.n.r;
      }
   }
}
