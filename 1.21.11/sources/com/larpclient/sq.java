package com.larpclient;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.SplittableRandom;
import java.util.TreeMap;
import java.util.stream.Collectors;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.class_10185;
import net.minecraft.class_1109;
import net.minecraft.class_1799;
import net.minecraft.class_1923;
import net.minecraft.class_2246;
import net.minecraft.class_2248;
import net.minecraft.class_2338;
import net.minecraft.class_2346;
import net.minecraft.class_2350;
import net.minecraft.class_238;
import net.minecraft.class_2382;
import net.minecraft.class_241;
import net.minecraft.class_243;
import net.minecraft.class_2480;
import net.minecraft.class_2561;
import net.minecraft.class_2680;
import net.minecraft.class_2818;
import net.minecraft.class_2826;
import net.minecraft.class_310;
import net.minecraft.class_3417;
import net.minecraft.class_3486;
import net.minecraft.class_3532;
import net.minecraft.class_3610;
import net.minecraft.class_465;
import net.minecraft.class_638;
import net.minecraft.class_642;
import net.minecraft.class_7923;

public final class sq extends s4 {
   private static sq d;
   private final com.larpclient.module.l3 x = this.G("Legit", "Select the best hotbar tool while smoothly aiming at each block", false);
   private final com.larpclient.module.li R = this.addSetting(
      new com.larpclient.module.li("Width", "Tunnel cross section; 1x1 is a crawl tunnel that needs a crawling start", "1x2", "1x1", "1x2", "3x3")
   );
   private final com.larpclient.module.lo U = this.Z("Reach", "How far ahead to mine", 4.0, 2.0, 6.0, 1.0);
   private final com.larpclient.module.l3 v = this.G("Base finder", "Walk a fixed heading, avoid hazards and stop at nearby storage clusters", false);
   private final com.larpclient.module.lo m = this.Z("Hazard scan", "Distance ahead to inspect lava, water and drops", 6.0, 3.0, 12.0, 1.0);
   private final com.larpclient.module.l3 O = this.G("Avoid lava", "Avoid lava beside, above and below the tunnel", true);
   private final com.larpclient.module.l3 h = this.G("Avoid water", "Avoid water that would flood the tunnel", true);
   private final com.larpclient.module.l3 M = this.G("Avoid drops", "Avoid drops of two or more blocks", true);
   private final com.larpclient.module.l3 p = this.G("Avoid gravel", "Avoid falling sand and gravel above the tunnel", false);
   private final com.larpclient.module.lo r = this.Z("Detour", "Maximum sideways distance for a safe return to the main heading", 4.0, 2.0, 12.0, 1.0);
   private final com.larpclient.module.l3 a = this.J("Chests", "Include chests and trapped chests", true);
   private final com.larpclient.module.l3 w = this.J("Barrels", "Include barrels", true);
   private final com.larpclient.module.l3 n = this.J("Ender Chests", "Include ender chests", true);
   private final com.larpclient.module.l3 X = this.J("Hoppers", "Include hoppers", true);
   private final com.larpclient.module.l3 L = this.J("Spawners", "Include mob spawners", true);
   private final com.larpclient.module.l3 Y = this.J("Shulker Boxes", "Include uncolored and all colored shulker boxes", true);
   private final com.larpclient.module.lo o = this.Z(
      "Blocks to flag", "Selected storage blocks within 12 blocks of each other needed for a find", 5.0, 1.0, 64.0, 1.0
   );
   private final com.larpclient.module.l3 V = this.G("Skip dungeons", "Ignore spawners on cobblestone floors or near cobwebs", true);
   private final com.larpclient.module.lo W = this.Z("Ignore near start", "Ignore finds this close to the starting point", 48.0, 0.0, 256.0, 8.0);
   private final com.larpclient.module.li q = this.addSetting(
      new com.larpclient.module.li("On find", "Report and stop, or disconnect after reporting", "Notify and stop", "Notify and stop", "Leave") {
         @Override
         public String getModeLabel(String var1) {
            return var1.equals("Notify and stop") ? "Notify & stop" : var1;
         }

         @Override
         public String getValueAsString() {
            return this.getModeLabel(this.getValue());
         }
      }
   );
   private final com.larpclient.module.l3 j = this.G("Log finds", "Append time, server, dimension and coordinates to config/larp-client/bases.txt", true);
   private final com.larpclient.module.lo J = this.Z(
      "Save tool at", "Stop before the selected tool has fewer remaining uses; zero allows breaking", 30.0, 0.0, 200.0, 5.0
   );
   private final com.larpclient.module.lo A = this.Z("Stop at health", "Stop when health reaches this value; zero disables the limit", 8.0, 0.0, 20.0, 1.0);
   private final h9<class_2818> i = new h9<>();
   private final Map<Long, sq.s> T = new HashMap<>();
   private final List<class_2338> B = new ArrayList<>();
   private final ArrayDeque<class_2338> z = new ArrayDeque<>();
   private class_638 Z;
   private class_2338 E;
   private class_243 I;
   private class_2350 u;
   private class_2350 e;
   private class_2338 l;
   private class_2338 N;
   private final u5 f = new u5();
   private final SplittableRandom D = new SplittableRandom();
   private class_243 F = class_243.field_1353;
   private boolean g;
   private boolean C;
   private int Q;
   private class_2338 H;
   private class_243 b = class_243.field_1353;
   private double c = 0.6;
   private final h9.v<class_2818> K = new h9.v<class_2818>() {
      public class_2818 loaded(int var1, int var2) {
         return sq.this.Z.method_2935().method_21730(var1, var2);
      }

      @Override
      public boolean due(int var1, int var2) {
         sq.s var3 = sq.this.T.get(class_1923.method_8331(var1, var2));
         return var3 == null || sq.mc.field_1724.field_6012 - var3.tick >= 80L;
      }

      public int W(class_2818 var1) {
         return var1.method_12006().length;
      }

      public boolean E(class_2818 var1, int var2) {
         class_2826 var3 = var1.method_12006()[var2];
         return !var3.method_38292() && var3.method_19523(sq.this::I);
      }

      @Override
      public void begin() {
         sq.this.B.clear();
      }

      public void K(class_2818 var1, int var2) {
         class_2826 var3 = var1.method_12006()[var2];

         for (int var4 = 0; var4 < 16; var4++) {
            for (int var5 = 0; var5 < 16; var5++) {
               for (int var6 = 0; var6 < 16; var6++) {
                  class_2680 var7 = var3.method_12254(var6, var4, var5);
                  if (sq.this.I(var7) && sq.this.B.size() < 256) {
                     class_2338 var8 = new class_2338(
                        var1.method_12004().method_8326() + var6, var1.method_31607() + var2 * 16 + var4, var1.method_12004().method_8328() + var5
                     );
                     if (!(sq.this.E.method_10262(var8) < sq.this.W.getValue() * sq.this.W.getValue())
                        && (!sq.this.V.getValue() || !var7.method_27852(class_2246.field_10260) || !sq.this.P(var8))) {
                        sq.this.B.add(var8);
                     }
                  }
               }
            }
         }
      }

      @Override
      public void finish(int var1, int var2) {
         sq.this.T.put(class_1923.method_8331(var1, var2), new sq.s(List.copyOf(sq.this.B), sq.mc.field_1724.field_6012));
      }
   };

   private com.larpclient.module.l3 J(final String var1, String var2, boolean var3) {
      return this.addSetting(new com.larpclient.module.l3("Storage " + var1, var2, var3) {
         @Override
         public String getDisplayName() {
            return "Storage: " + var1;
         }
      });
   }

   public sq() {
      super("Auto Tunnel", "Mine and walk a centered 1x1, 1x2 or 3x3 tunnel, including while menus are open");
      d = this;

      for (com.larpclient.module.lb var2 : List.of(this.m, this.r, this.a, this.w, this.n, this.X, this.L, this.Y, this.o, this.W, this.q, this.j)) {
         var2.O(this.v::getValue);
      }

      this.V.O(() -> this.v.getValue() && this.L.getValue());
      this.v.a(var1 -> this.K());
      this.R.a(var1 -> this.K());

      for (com.larpclient.module.l3 var4 : List.of(this.a, this.w, this.n, this.X, this.L, this.Y, this.V)) {
         var4.a(var1 -> {
            this.i.F();
            this.T.clear();
            this.B.clear();
         });
      }
   }

   @Override
   public void onEnable() {
      super.onEnable();
      this.K();
   }

   @Override
   public void onDisable() {
      super.onDisable();
      this.K();
   }

   @Override
   public boolean allowsScreenInteractions() {
      return true;
   }

   public static boolean A() {
      if (d != null && d.isEnabled()) {
         sq var0 = d;
         class_310 var1 = class_310.method_1551();
         return var0 != null && var0.isRuntimeActive() && cr.o(var0) && var1.method_18506() == null && var1.field_1724.field_3944.method_76760();
      } else {
         return false;
      }
   }

   @Override
   protected void aimAt(class_243 var1, float var2) {
      if (this.isEnabled()) {
         if (!this.C) {
            double var3 = Math.min(this.U.getValue(), mc.field_1724.method_55754());
            class_243 var5 = var1;
            if (this.N != null) {
               if (!this.N.equals(this.H)) {
                  this.H = this.N;
                  this.b = new class_243(this.D.nextDouble(-0.22, 0.22), this.D.nextDouble(-0.22, 0.22), this.D.nextDouble(-0.22, 0.22));
               }

               class_243 var6 = var1.method_1019(this.b);
               if (bv.r(this.N, var6, var3)) {
                  var5 = var6;
               }
            }

            float[] var9 = t9.X(mc.field_1724.method_33571(), var5);
            int var7 = this.N != null && bv.L(this.N, var3) == null ? 0 : 1;
            float[] var8 = this.f.V(mc.field_1724.method_36454(), mc.field_1724.method_36455(), var9[0], var9[1], (boolean)var7);
            t9.O(this, t9.v.g, var8[0], var8[1], 1.0);
         }
      }
   }

   private void K() {
      cr.p(this);
      bv.i(this);
      this.g = this.C = (boolean)0;
      this.e = this.u = null;
      this.E = null;
      this.I = null;
      this.l = this.N = this.H = null;
      this.F = class_243.field_1353;
      this.f.k();
      this.c = this.D.nextDouble(0.35, 1.1);
      this.z.clear();
      this.i.F();
      this.T.clear();
      this.B.clear();
      this.Z = null;
      this.Q = 0;
   }

   @Override
   public void onTick() {
      this.g = (boolean)0;
      this.F = class_243.field_1353;
      if (this.r() && !this.E()) {
         byte var1 = this.M();
         byte var12 = this.C;
         byte var11 = var1;
         int var10000 = Integer.compare(var11, var12);
         if (((var10000 | -var10000) >>> 31 ^ 1) == 0) {
            this.C = (boolean)var1;
            q8.G(
               this.getName(),
               var1 != 0
                  ? "Inventory menu open; holding position and facing, mining only the block under the crosshair"
                  : "Inventory menu closed; resuming walking and aiming"
            );
            boolean var14 = false;
            byte var13 = var1;
            var10000 = Integer.signum(var13);
            if ((var10000 | -var10000) >>> 31 == 0) {
               this.f.k();
            }
         }

         if (this.Z != mc.field_1687 || this.I != null && mc.field_1724.method_73189().method_1025(this.I) > 64.0) {
            this.K();
            this.Z = mc.field_1687;
            this.E = mc.field_1724.method_24515();
            this.u = class_2350.method_10150(mc.field_1724.method_36454());
         }

         class_2350 var2 = this.u;
         if (this.A.getValue() > 0.0 && mc.field_1724.method_6032() <= this.A.getValue()) {
            this.J("Low health");
         } else {
            if (this.R.G("1x1")) {
               int var20 = mc.field_1724.method_20448();
               boolean var16 = false;
               byte var15 = (byte)var20;
               var20 = Integer.signum(var15);
               if ((var20 | -var20) >>> 31 == 0) {
                  bv.i(this);
                  this.f.k();
                  this.N = null;
                  this.m("Waiting: 1x1 needs a crawling start, such as under a trapdoor");
                  return;
               }
            }

            if (this.v.getValue()) {
               this.R();
               if (!this.isRuntimeActive()) {
                  return;
               }

               var2 = this.z();
               if (var2 == null) {
                  return;
               }
            }

            if (this.e != var2 || this.l == null) {
               this.e = var2;
               this.l = mc.field_1724.method_24515().method_10093(var2);
               this.N = null;
               this.f.k();
               q8.G(
                  this.getName(),
                  "Following {} through lane {}, {} at Y {} ({})",
                  var2,
                  this.l.method_10263(),
                  this.l.method_10260(),
                  this.l.method_10264(),
                  this.R.getValue()
               );
            }

            this.l = v(this.l, mc.field_1724.method_73189(), var2, this.f(this.l, var2));
            String var3 = this.F(this.l, var2);
            if (var3 != null) {
               bv.i(this);
               this.f.k();
               this.N = null;
               this.m("Waiting: " + var3);
            } else {
               double var4 = Math.min(this.U.getValue(), mc.field_1724.method_55754());
               class_2338 var6 = this.u(var2, var4);
               if (var6 != null) {
                  if (this.i(var6)) {
                     this.J("Tool nearly broken");
                     return;
                  }

                  class_2338 var7 = var6;
                  class_2350 var8 = var2;
                  class_2338 var9 = this.l;
                  int var10005 = this.x.getValue();
                  boolean var18 = false;
                  byte var17 = (byte)var10005;
                  var10005 = Integer.signum(var17);
                  boolean var10 = bv.t(
                     this,
                     var6,
                     var4,
                     true,
                     12.0F,
                     (var10005 | -var10005) >>> 31 != 0 || this.v.getValue(),
                     () -> !this.E() && this.F(var9, var8) == null && (!this.v.getValue() || !mc.field_1687.method_8320(var7).method_31709())
                  );
                  if (!var10) {
                     this.m("Waiting for a reachable tunnel face");
                  }
               } else {
                  bv.i(this);
               }

               this.r(var2, var6);
            }
         }
      } else {
         bv.i(this);
         this.f.k();
         this.N = null;
         this.m(this.E() ? "Paused for manual movement" : "Waiting for world or interaction control");
      }
   }

   private void r(class_2350 var1, class_2338 var2) {
      class_243 var3 = class_243.method_24955(this.l);
      boolean var4 = this.f(this.l, var1);
      double var5 = u5.h(mc.field_1724.method_73189(), var3, var1) - (var4 ? -0.5 : 0.85);
      class_243 var7 = mc.field_1724.method_18798();
      double var8 = var7.field_1352 * var1.method_10148() + var7.field_1350 * var1.method_10165();
      class_243 var10 = u5.z(mc.field_1724.method_73189(), var3, var1);
      boolean var11 = this.f.O();
      int var12 = cr.a(this) && !this.C && !ff.e() && this.u(U(var10)) ? 1 : 0;
      this.g = this.f.V(var5, var8, (boolean)var12);
      this.F = this.g ? var10 : class_243.field_1353;
      if (this.g != var11) {
         q8.v(this.getName(), this.g ? "Walk keys pressed; {} clear ahead" : "Walk keys released; {} clear ahead", String.format(Locale.ROOT, "%.2f", var5));
      }

      if (var2 == null && cr.a(this)) {
         class_243 var13 = var3.method_1031(var1.method_10148() * 6, 0.0, var1.method_10165() * 6);
         if (mc.field_1724.field_6012 % 60 == 0 && this.D.nextInt(4) == 0) {
            this.c = this.D.nextDouble(0.35, 1.1);
         }

         this.aimAt(new class_243(var13.field_1352, mc.field_1724.method_23320() - this.c, var13.field_1350), 12.0F);
      }

      if (this.I != null && mc.field_1724.method_73189().method_1025(this.I) < 1.0E-4 && this.g && var2 == null) {
         this.Q++;
      } else {
         this.Q = 0;
      }

      this.I = mc.field_1724.method_73189();
      if (var2 == null) {
         this.m(
            this.C
               ? "Holding still while an inventory menu is open"
               : (this.g ? (this.v.getValue() ? "Searching for bases" : "Following the cleared tunnel") : "Waiting at a protected or unreachable obstruction")
         );
      }

      if (this.Q > 160) {
         this.J("Movement blocked; clear the route or choose a new heading");
      }
   }

   public static class_2338 v(class_2338 var0, class_243 var1, class_2350 var2, boolean var3) {
      return var3 && u5.h(var1, class_243.method_24955(var0), var2) < 0.12 ? var0.method_10093(var2) : var0;
   }

   private boolean M() {
      return mc.field_1755 instanceof class_465;
   }

   private boolean E() {
      return mc != null
         && mc.field_1724 != null
         && mc.method_1569()
         && mc.field_1755 == null
         && (
            com.larpclient.j.W(mc.field_1690.field_1894)
               || com.larpclient.j.W(mc.field_1690.field_1881)
               || com.larpclient.j.W(mc.field_1690.field_1913)
               || com.larpclient.j.W(mc.field_1690.field_1849)
               || com.larpclient.j.W(mc.field_1690.field_1903)
               || com.larpclient.j.W(mc.field_1690.field_1832)
         );
   }

   private List<class_2338> U(class_2338 var1, class_2350 var2) {
      return O(var1, var2, this.R.getValue());
   }

   public static int H(String var0) {
      return switch (var0) {
         case "1x1" -> 1;
         case "3x3" -> 3;
         default -> 2;
      };
   }

   public static List<class_2338> O(class_2338 var0, class_2350 var1, String var2) {
      boolean var3 = var2.equals("3x3");
      int var4 = H(var2);
      ArrayList var5 = new ArrayList(var3 ? 9 : var4);

      for (int var6 = 0; var6 < var4; var6++) {
         var5.add(var0.method_10086(var6));
         if (var3) {
            var5.add(var0.method_10093(var1.method_10170()).method_10086(var6));
            var5.add(var0.method_10093(var1.method_10160()).method_10086(var6));
         }
      }

      return var5;
   }

   private class_2338 u(class_2350 var1, double var2) {
      for (int var4 = 0; var4 < Math.ceil(var2); var4++) {
         class_2338 var5 = this.l.method_10079(var1, var4);
         if (!this.z.isEmpty() && u5.h(class_243.method_24955(var5), class_243.method_24955((class_2382)this.z.peekFirst()), var1) < -0.01
            || this.F(var5, var1) != null) {
            break;
         }

         List var6 = this.U(var5, var1).stream().filter(var3 -> this.V(var3, var2)).toList();
         if (!var6.isEmpty()) {
            if (this.N != null && var6.contains(this.N)) {
               return this.N;
            }

            this.N = var6.stream().min(Comparator.comparingDouble(this::F)).orElseThrow();
            return this.N;
         }

         if (!this.f(var5, var1)) {
            break;
         }
      }

      this.N = null;
      return null;
   }

   private boolean V(class_2338 var1, double var2) {
      return bv.P(var1)
         && mc.field_1724.method_33571().method_1025(class_243.method_24953(var1)) <= var2 * var2
         && (!this.v.getValue() || !mc.field_1687.method_8320(var1).method_31709())
         && bv.z(this, var1, var2) != null;
   }

   private double F(class_2338 var1) {
      float[] var2 = t9.X(mc.field_1724.method_33571(), class_243.method_24953(var1));
      return Math.hypot(class_3532.method_15393(var2[0] - mc.field_1724.method_36454()), var2[1] - mc.field_1724.method_36455());
   }

   private boolean f(class_2338 var1, class_2350 var2) {
      for (class_2338 var4 : this.U(var1, var2)) {
         if (!mc.field_1687.method_22340(var4) || !mc.field_1687.method_8320(var4).method_26220(mc.field_1687, var4).method_1110()) {
            return false;
         }
      }

      return true;
   }

   private static class_243 U(class_243 var0) {
      return var0.method_1029().method_1021(0.55);
   }

   private boolean u(class_243 var1) {
      class_243 var2 = var1.method_1021(0.65);
      class_238 var3 = mc.field_1724.method_5829().method_18804(var2);
      if (!mc.field_1687.method_8587(mc.field_1724, var3)) {
         return false;
      }

      class_2338 var4 = class_2338.method_49638(mc.field_1724.method_73189().method_1019(var2));
      return this.y(var4) && this.F(var4, this.e) == null;
   }

   private boolean i(class_2338 var1) {
      if (this.J.getValue() <= 0.0) {
         return false;
      }

      class_2680 var2 = mc.field_1687.method_8320(var1);
      class_1799 var3 = mc.field_1724.method_6047();
      float var4 = var3.method_7924(var2) + (var3.method_7951(var2) ? 1000 : 0);

      for (int var5 = 0; (this.x.getValue() || this.v.getValue()) && var5 < 9; var5++) {
         class_1799 var6 = mc.field_1724.method_31548().method_5438(var5);
         float var7 = var6.method_7924(var2) + (var6.method_7951(var2) ? 1000 : 0);
         if (var7 > var4) {
            var3 = var6;
            var4 = var7;
         }
      }

      return var3.method_7963() && var3.method_7936() - var3.method_7919() <= this.J.getValue();
   }

   private boolean y(class_2338 var1) {
      if (!mc.field_1687.method_22340(var1)) {
         return false;
      }

      int var2 = Math.min(H(this.R.getValue()), 2);

      for (int var3 = 0; var3 < var2; var3++) {
         if (!mc.field_1687.method_8320(var1.method_10086(var3)).method_26220(mc.field_1687, var1.method_10086(var3)).method_1110()) {
            return false;
         }
      }

      return true;
   }

   private String F(class_2338 var1, class_2350 var2) {
      if (!mc.field_1687.method_22340(var1)) {
         return "Waiting for loaded terrain";
      }

      for (class_2338 var4 : this.U(var1, var2)) {
         for (class_2350 var8 : class_2350.values()) {
            class_2338 var9 = var4.method_10093(var8);
            if (!mc.field_1687.method_22340(var9)) {
               return "Waiting for loaded terrain";
            }

            class_3610 var10 = mc.field_1687.method_8316(var9);
            if (this.O.getValue() && var10.method_15767(class_3486.field_15518)) {
               return "Lava";
            }

            if (this.h.getValue() && var10.method_15767(class_3486.field_15517)) {
               return "Water";
            }
         }

         class_3610 var13 = mc.field_1687.method_8316(var4);
         if (this.O.getValue() && var13.method_15767(class_3486.field_15518)) {
            return "Lava";
         }

         if (this.h.getValue() && var13.method_15767(class_3486.field_15517)) {
            return "Water";
         }
      }

      if (this.M.getValue()
         && r(
            mc.field_1687.method_8320(var1.method_10074()).method_26220(mc.field_1687, var1.method_10074()).method_1110(),
            mc.field_1687.method_8320(var1.method_10087(2)).method_26220(mc.field_1687, var1.method_10087(2)).method_1110()
         )) {
         return "Deep drop";
      }

      if (this.p.getValue()) {
         for (class_2338 var12 : this.U(var1, var2)) {
            if (mc.field_1687.method_8320(var12.method_10084()).method_26204() instanceof class_2346) {
               return "Falling sand or gravel";
            }
         }
      }

      return null;
   }

   private class_2350 z() {
      class_2338 var1 = mc.field_1724.method_24515();

      while (
         !this.z.isEmpty()
            && mc.field_1724
                  .method_73189()
                  .method_18805(1.0, 0.0, 1.0)
                  .method_1025(class_243.method_24955((class_2382)this.z.peekFirst()).method_18805(1.0, 0.0, 1.0))
               < 0.08
      ) {
         this.z.removeFirst();
      }

      if (!this.z.isEmpty()) {
         class_2350 var9 = var1.equals(this.z.peekFirst()) ? this.e : q(var1, this.z.peekFirst());
         String var11 = this.F(var1.method_10093(var9), var9);
         if (var11 != null) {
            this.J("Detour blocked by " + var11.toLowerCase(Locale.ROOT));
            return null;
         } else {
            return var9;
         }
      } else {
         String var2 = null;

         for (int var3 = 1; var3 <= this.m.getValue().intValue(); var3++) {
            var2 = this.F(var1.method_10079(this.u, var3), this.u);
            if (var2 != null) {
               break;
            }
         }

         if (var2 == null) {
            return this.u;
         }

         if (var2.equals("Waiting for loaded terrain")) {
            bv.i(this);
            this.m(var2);
            return null;
         }

         for (int var10 = 1; var10 <= this.r.getValue().intValue(); var10++) {
            for (class_2350 var5 : List.of(this.u.method_10170(), this.u.method_10160())) {
               int var6 = this.m.getValue().intValue() + 2;
               class_2338 var7 = var1.method_10079(var5, var10);
               class_2338 var8 = var7.method_10079(this.u, var6);
               if (this.H(var1, var5, var10) && this.H(var7, this.u, var6) && this.H(var8, var5.method_10153(), var10)) {
                  this.z.add(var7);
                  this.z.add(var8);
                  this.z.add(var1.method_10079(this.u, var6));
                  q8.G(this.getName(), "Routing {} blocks around {} using {}", var10, var2, var5);
                  this.m("Detouring around " + var2.toLowerCase(Locale.ROOT));
                  return var5;
               }
            }
         }

         this.J("No safe detour around " + var2.toLowerCase(Locale.ROOT));
         return null;
      }
   }

   private boolean H(class_2338 var1, class_2350 var2, int var3) {
      for (int var4 = 1; var4 <= var3; var4++) {
         if (this.F(var1.method_10079(var2, var4), var2) != null) {
            return false;
         }
      }

      return true;
   }

   private static class_2350 q(class_2338 var0, class_2338 var1) {
      int var2 = var1.method_10263() - var0.method_10263();
      int var3 = var1.method_10260() - var0.method_10260();
      return Math.abs(var2) > Math.abs(var3)
         ? (var2 > 0 ? class_2350.field_11034 : class_2350.field_11039)
         : (var3 > 0 ? class_2350.field_11035 : class_2350.field_11043);
   }

   public static class_10185 B() {
      sq var0 = d;
      if (var0 != null && var0.r() && var0.g && !var0.C && var0.e != null && !wp.d() && !ff.e() && !var0.E()) {
         class_310 var1 = class_310.method_1551();
         if (var0.Z == var1.field_1687 && !(var0.F.method_1027() < 1.0E-6) && var0.u(U(var0.F)) && (!t9.I() || t9.k(t9.v.g))) {
            class_10185 var2 = var0.f.V(var1.field_1724.method_36454(), var0.F);
            return var2.comp_3160() ? null : var2;
         } else {
            return null;
         }
      } else {
         return null;
      }
   }

   public static class_241 U() {
      class_10185 var0 = B();
      return var0 == null ? null : u5.R(var0);
   }

   public static boolean r(boolean var0, boolean var1) {
      return var0 && var1;
   }

   public static boolean Z(class_2338 var0, class_2338 var1) {
      return Math.abs((long)var1.method_10263() - var0.method_10263()) <= 12L
         && Math.abs((long)var1.method_10264() - var0.method_10264()) <= 12L
         && Math.abs((long)var1.method_10260() - var0.method_10260()) <= 12L;
   }

   public static class_241 r(float var0, class_2350 var1) {
      double var2 = Math.toRadians(var0 - var1.method_10144());
      return new class_241((float)Math.sin(var2), (float)Math.cos(var2));
   }

   private boolean I(class_2680 var1) {
      class_2248 var2 = var1.method_26204();
      return this.a.getValue() && (var2 == class_2246.field_10034 || var2 == class_2246.field_10380)
         || this.w.getValue() && var2 == class_2246.field_16328
         || this.n.getValue() && var2 == class_2246.field_10443
         || this.X.getValue() && var2 == class_2246.field_10312
         || this.L.getValue() && var2 == class_2246.field_10260
         || this.Y.getValue() && var2 instanceof class_2480;
   }

   private boolean P(class_2338 var1) {
      class_2680 var2 = mc.field_1687.method_8320(var1.method_10074());
      if (!var2.method_27852(class_2246.field_10445) && !var2.method_27852(class_2246.field_9989)) {
         for (class_2338 var4 : class_2338.method_10097(var1.method_10069(-2, -1, -2), var1.method_10069(2, 1, 2))) {
            if (mc.field_1687.method_22340(var4) && mc.field_1687.method_8320(var4).method_27852(class_2246.field_10343)) {
               return true;
            }
         }

         return false;
      } else {
         return true;
      }
   }

   private void R() {
      class_1923 var1 = mc.field_1724.method_31476();
      int var2 = Math.clamp(mc.field_1690.method_38521(), 1, 12);
      this.T
         .entrySet()
         .removeIf(
            var3x -> {
               class_1923 var4 = new class_1923(var3x.getKey());
               return Math.abs(var4.field_9181 - var1.field_9181) > var2
                  || Math.abs(var4.field_9180 - var1.field_9180) > var2
                  || this.Z.method_2935().method_21730(var4.field_9181, var4.field_9180) == null;
            }
         );
      this.i.T(this.K, var1.field_9181, var1.field_9180, var2, 2);
      if (mc.field_1724.field_6012 % 10 == 0) {
         ArrayList var3 = new ArrayList();

         for (sq.s var5 : this.T.values()) {
            for (class_2338 var7 : var5.positions) {
               if (var3.size() >= 4096) {
                  break;
               }

               if (mc.field_1687.method_22340(var7)
                  && this.I(mc.field_1687.method_8320(var7))
                  && this.E.method_10262(var7) >= this.W.getValue() * this.W.getValue()
                  && (!this.V.getValue() || !mc.field_1687.method_8320(var7).method_27852(class_2246.field_10260) || !this.P(var7))) {
                  var3.add(var7);
               }
            }
         }

         HashMap var16 = new HashMap();

         for (class_2338 var19 : var3) {
            var16.computeIfAbsent(Y(var19.method_10263(), var19.method_10264(), var19.method_10260()), var0 -> new ArrayList<>()).add(var19);
         }

         for (class_2338 var20 : var3) {
            ArrayList var21 = new ArrayList();
            int var8 = Math.floorDiv(var20.method_10263(), 12);
            int var9 = Math.floorDiv(var20.method_10264(), 12);
            int var10 = Math.floorDiv(var20.method_10260(), 12);

            for (int var11 = var8 - 1; var11 <= var8 + 1; var11++) {
               for (int var12 = var9 - 1; var12 <= var9 + 1; var12++) {
                  for (int var13 = var10 - 1; var13 <= var10 + 1; var13++) {
                     for (class_2338 var15 : var16.getOrDefault(var11 + ":" + var12 + ":" + var13, List.of())) {
                        if (Z(var20, var15)) {
                           var21.add(var15);
                        }
                     }
                  }
               }
            }

            if (var21.size() >= this.o.getValue()) {
               this.l(var20, var21);
               return;
            }
         }
      }
   }

   private static String Y(int var0, int var1, int var2) {
      return Math.floorDiv(var0, 12) + ":" + Math.floorDiv(var1, 12) + ":" + Math.floorDiv(var2, 12);
   }

   private void l(class_2338 var1, List<class_2338> var2) {
      TreeMap var3 = new TreeMap();

      for (class_2338 var5 : var2) {
         var3.merge(class_7923.field_41175.method_10221(mc.field_1687.method_8320(var5).method_26204()).method_12832(), 1, Integer::sum);
      }

      String var11 = var1.method_10263() + ", " + var1.method_10264() + ", " + var1.method_10260();
      String var12 = var11 + " (" + var3.entrySet().stream().map(var0 -> var0.getValue() + " " + (String)var0.getKey()).collect(Collectors.joining(", ")) + ")";
      mc.field_1724.method_7353(class_2561.method_43470("Larp Auto Tunnel: base found at " + var12), false);
      wt.T().k("Base found", var12, wt.s.C, 10000L);
      mc.method_1483().method_4873(class_1109.method_4758(class_3417.field_14709, 1.4F));
      q8.G(this.getName(), "Base found at {}", var12);
      if (this.j.getValue()) {
         Path var6 = FabricLoader.getInstance().getConfigDir().resolve("larp-client/bases.txt");
         class_642 var7 = mc.method_1558();
         String var8 = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
            + "  "
            + (var7 == null ? "singleplayer" : var7.field_3761)
            + "  "
            + mc.field_1687.method_27983().method_29177()
            + "  "
            + var12
            + System.lineSeparator();

         try {
            Files.createDirectories(var6.getParent());
            Files.writeString(var6, var8, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
         } catch (IOException var10) {
            q8.u(this.getName(), "Could not append base find to " + var6, var10);
         }
      }

      boolean var13 = this.q.G("Leave");
      this.disable();
      if (var13) {
         mc.method_73360(class_2561.method_43470("Larp Auto Tunnel found a base at " + var12));
      }
   }

   private void J(String var1) {
      q8.G(this.getName(), "Stopped: {}", var1);
      mc.field_1724.method_7353(class_2561.method_43470("Larp Auto Tunnel stopped: " + var1), false);
      wt.T().U("Auto Tunnel stopped", var1, wt.s.a);
      this.disable();
      this.m(var1);
   }

   private record s(List<class_2338> positions, long tick) {
   }
}
