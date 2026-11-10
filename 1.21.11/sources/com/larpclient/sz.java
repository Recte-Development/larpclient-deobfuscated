package com.larpclient;

import com.larpclient.module.MCFriendModule;
import com.larpclient.module.Module;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.Function;
import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents;
import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents.Game;
import net.fabricmc.fabric.api.client.rendering.v1.world.WorldRenderEvents;
import net.fabricmc.fabric.api.client.rendering.v1.world.WorldRenderEvents.DebugRender;
import net.minecraft.class_10185;
import net.minecraft.class_12179;
import net.minecraft.class_12180;
import net.minecraft.class_1268;
import net.minecraft.class_1309;
import net.minecraft.class_1542;
import net.minecraft.class_1548;
import net.minecraft.class_1569;
import net.minecraft.class_1603;
import net.minecraft.class_1661;
import net.minecraft.class_1707;
import net.minecraft.class_1713;
import net.minecraft.class_1735;
import net.minecraft.class_1792;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_1893;
import net.minecraft.class_1937;
import net.minecraft.class_2246;
import net.minecraft.class_2338;
import net.minecraft.class_2350;
import net.minecraft.class_238;
import net.minecraft.class_243;
import net.minecraft.class_2561;
import net.minecraft.class_2680;
import net.minecraft.class_2806;
import net.minecraft.class_2818;
import net.minecraft.class_2826;
import net.minecraft.class_310;
import net.minecraft.class_3489;
import net.minecraft.class_3532;
import net.minecraft.class_3745;
import net.minecraft.class_3959;
import net.minecraft.class_3965;
import net.minecraft.class_465;
import net.minecraft.class_476;
import net.minecraft.class_634;
import net.minecraft.class_640;
import net.minecraft.class_6880;
import net.minecraft.class_742;
import net.minecraft.class_746;
import net.minecraft.class_7923;
import net.minecraft.class_9290;
import net.minecraft.class_9334;
import net.minecraft.class_2350.class_2351;
import net.minecraft.class_239.class_240;
import net.minecraft.class_2902.class_2903;
import net.minecraft.class_3959.class_242;
import net.minecraft.class_3959.class_3960;

public final class sz extends s4 implements tc, fk.h {
   private static sz E;
   private static boolean J;
   static final List<String> uL = List.of("AutoAnchor", "AnchorTap", "Anchor Macro", "AutoCrystal", "Freecam");
   static final double us = 28.0;
   static final int j = 900;
   static final long f = 120000L;
   static final long uf = 360000L;
   static final long n = 60000L;
   static final int N = 3;
   static final int un = 16000;
   private final com.larpclient.module.li uj = this.addSetting(
      new com.larpclient.module.li(
         "Region", "Which /rtp region to use. Random takes the overworld regions in turn; the Nether is never used", "Random", ys.q.toArray(new String[0])
      )
   );
   private final com.larpclient.module.lo uE = this.Z(
      "Player Range",
      "Any player closer than this makes the bot leave by the next /rtp. Servers only show players up to about 48 to 128 blocks away, so the default means anyone you can see",
      96.0,
      8.0,
      160.0,
      1.0
   );
   private final com.larpclient.module.l3 uT = this.G("Ignore Friends", "Players on your friends list (MCFriend) do not make the bot leave", true);
   private final com.larpclient.module.l3 ut = this.G(
      "Log Out If Caught",
      "If a player gets within 5 blocks while /rtp is still cooling down, log out instead of running. Never logs out while a player has hit you in the last 15 seconds",
      false
   );
   private final com.larpclient.module.l3 D = this.G(
      "Dig To Hidden Anchors", "Dig a tunnel to anchors inside walls or underground. Off: only anchors you can walk up to", true
   );
   private final com.larpclient.module.l3 Kh = this.G(
      "Pearl Travel", "Throw ender pearls from your hotbar to reach far anchors fast. Only while you wear armour and have enough health", true
   );
   private final com.larpclient.module.l3 A = this.G(
      "Ender Chest Stash",
      "When your inventory is full, put the anchors in your ender chest. Needs an ender chest and a Silk Touch pickaxe in your hotbar",
      true
   );
   private final com.larpclient.module.lo uZ = this.Z("Stop After", "Stop after this many anchors (0 never stops)", 0.0, 0.0, 2304.0, 1.0);
   private final com.larpclient.module.li uR = this.addSetting(
      new com.larpclient.module.li("Notifications", "How the bot tells you about finds and problems", "Toast", "Toast", "Chat", "Off")
   );
   private final com.larpclient.module.l3 u3 = this.G(
      "Status Card", "Show a card at the top of the screen with anchors found, the /rtp timer and what the bot is doing", true
   );
   private final com.larpclient.module.l3 uo = this.G("Show Path", "Draw the route, the blocks it digs, the anchor it is going to and its pearl throws", true);
   private final com.larpclient.module.l3 u2 = this.G(
      "Fight Mobs",
      "Hit zombies, spiders and other melee mobs that come for you, with the best sword or axe in your hotbar. Creepers and skeletons are still run from, and it runs at 8 health or less",
      false
   );
   private final com.larpclient.module.l3 q = this.G(
      "Auto Sell Anchors",
      "After picking up an anchor, sell your anchors with /sell, standing still. Only anchors go into the Sell menu; if the menu looks wrong it stops selling for five minutes",
      false
   );
   private final u5 K = new u5();
   private final tn T = new tn(this);
   private final e3 uP = new e3(this, this.K, this::D);
   private final ys Kp = new ys();
   private final ys.v e = new ys.v();
   private final ConcurrentLinkedQueue<String> uW = new ConcurrentLinkedQueue<>();
   private sz.z uN = sz.z.H;
   private sz.z uv = sz.z.t;
   private boolean um;
   private Object m;
   private Object z;
   private Object uB;
   private long i = -1L;
   private String uS;
   private String ul;
   private final List<String> uq = new ArrayList<>();
   private boolean H;
   private int uC;
   private int d;
   private long uG;
   private long u_;
   private long K9;
   private long uk = -2305843009213693952L;
   private long F = -1L;
   private String uI;
   private boolean uF;
   private class_243 Kq;
   private int ux;
   private int u7;
   private int I;
   private int KG;
   private final Map<String, Integer> C = new TreeMap<>();
   private final Map<Long, Integer> K3 = new HashMap<>();
   private final Set<Long> Y = new HashSet<>();
   private boolean uQ;
   private boolean u5;
   private boolean uV;
   private class_1309 ud;
   private int ug;
   private String uY = "";
   private double u4 = Double.MAX_VALUE;
   private final List<double[]> M = new ArrayList<>();
   private long ua = -1L;
   private float O = 20.0F;
   private double[] o;
   private int S;
   private class_2338 L;
   private int uD;
   private int V;
   private int X;
   private int p;
   private int Kt;
   private int ui;
   private int uU;
   private int u8;
   private boolean uz;
   private boolean a;
   private long uK;
   private long uJ = -2305843009213693952L;
   private int KC;
   private int w = -1000;
   private final Set<Long> uw = new HashSet<>();
   private final Set<Long> u9 = new HashSet<>();
   private Boolean u;
   private final Set<class_2338> ub = new HashSet<>();
   private int h;
   private int v;
   private int up;
   private int u0;
   private int Q;
   private int Ks = -1;
   private int uX;
   private int x;
   private class_2338 l;
   private String uH;
   private int ue;
   private int ur;
   private int uA;
   private int r = -1;
   private int uc;
   private int uh;
   private long uM;
   static final long c = 300000L;
   private sz.h U = sz.h.Z;
   private float KX;
   private float R;
   private class_243 k;
   private int W;
   private int Z = -1000;
   private class_243 uu;
   private class_243 b = class_243.field_1353;
   private boolean u6;
   private boolean K7;
   private boolean uy;
   private int uO = Integer.MIN_VALUE;
   private String B = "";
   private long KU;
   private final List<class_2338> u1 = new ArrayList<>();
   private long g;

   public sz() {
      super("Anchor Hunter", "DonutSMP: /rtp around, find respawn anchors in loaded chunks, walk or dig to them, mine and collect them");
      E = this;
      this.uZ.M("Stop After (anchors, 0 never)");
      if (!J) {
         J = (boolean)1;
         WorldRenderEvents.BEFORE_DEBUG_RENDER.register((DebugRender)var0 -> {
            sz var1 = E;
            if (var1 != null) {
               var1.v();
            }
         });
         ClientReceiveMessageEvents.GAME.register((Game)(var0, var1) -> g(var0.getString()));
      }
   }

   public static sz Y() {
      return E;
   }

   public static void g(String var0) {
      if (E != null && E.isEnabled()) {
         sz var1 = E;
         if (var1 != null && var1.um && var0 != null && !var0.isBlank() && var1.uW.size() < 64) {
            var1.uW.add(var0);
         }
      }
   }

   @Override
   public boolean allowsScreenInteractions() {
      return mc == null || !(cm.j() instanceof class_465) || this.b();
   }

   private boolean b() {
      return this.um && (this.uN == sz.z.s || this.uN == sz.z.Q) && mc != null && mc.field_1724 != null && mc.field_1724.field_7512 instanceof class_1707;
   }

   @Override
   public void disable() {
      this.uS = null;
      int var1 = this.i >= 0L ? 1 : 0;
      this.i = -1L;
      super.disable();
      if (var1 != 0) {
         this.K("turned off while teleporting");
      }
   }

   @Override
   public List<tc.k> commands() {
      return List.of(new tc.k("Before you start", "Check my setup", this::Q), new tc.k("Before you start", "Find anchors near me", this::a));
   }

   @Override
   public String summary() {
      if (this.isRuntimeActive() && this.um) {
         return B(this.ux, "anchor") + " in " + tc.T(System.currentTimeMillis() - this.u_) + ", " + B(this.u7, "teleport") + ". " + this.uN.m + ".";
      } else {
         return this.ul != null ? this.ul + "." : "Stand anywhere on DonutSMP and turn it on: it uses /rtp and mines the respawn anchors it finds.";
      }
   }

   @Override
   public String detail() {
      if (!this.isInGame()) {
         return "Needs a diamond or netherite pickaxe in your hotbar.";
      }

      int var1 = this.T(class_2246.field_23152.method_9564());
      String var2 = var1 >= 0 ? mc.field_1724.method_31548().method_5438(var1).method_7964().getString() : "no diamond or netherite pickaxe";
      return "Pickaxe: "
         + var2
         + ". Ender chest "
         + (tn.h(var0 -> var0.method_31574(class_1802.field_8466)) >= 0 ? "yes" : "no")
         + ", Silk Touch pickaxe "
         + (this.i() >= 0 ? "yes" : "no")
         + ", pearls "
         + this.b()
         + ".";
   }

   @Override
   public List<tc.v> settingGroups() {
      return List.of(
         new tc.v("Where to go", List.of(this.uj)),
         new tc.v("Other players", List.of(this.uT, this.ut)),
         new tc.v("While hunting", List.of(this.q, this.A, this.uZ, this.D, this.Kh, this.u2)),
         new tc.v("Display", List.of(this.u3, this.uo, this.uR)),
         tc.v.advanced("Advanced", List.of(this.uE))
      );
   }

   private String Q() {
      if (!this.isInGame()) {
         return "Join a world first.";
      }

      ArrayList var1 = new ArrayList();
      ArrayList var2 = new ArrayList();
      if (mc.field_1687.method_27983() != class_1937.field_25179) {
         var1.add("go to the overworld");
      }

      if (this.T(class_2246.field_23152.method_9564()) < 0) {
         var1.add("put a diamond or netherite pickaxe in your hotbar");
      }

      if (tn.h(sz::H) < 0) {
         var2.add("no food in the hotbar");
      }

      if (!this.Q()) {
         if (this.A.getValue() && this.y()) {
            var2.add("your inventory is full, so it stashes in the ender chest first");
         } else {
            var1.add("make room in your inventory");
         }
      }

      if (this.A.getValue() && !this.y()) {
         var2.add("no ender chest and Silk Touch pickaxe in the hotbar, so a full inventory stops the run");
      }

      if (this.Kh.getValue() && this.b() == 0) {
         var2.add("no ender pearls in the hotbar, so it walks everywhere");
      } else if (this.Kh.getValue() && mc.field_1724.method_6096() < 12) {
         var2.add("wear armour (12 points or more) for pearl travel");
      }

      String var3 = var1.isEmpty() ? "Ready." : "Not ready: " + String.join("; ", var1) + ".";
      return var2.isEmpty() ? var3 : var3 + " Note: " + String.join("; ", var2) + ".";
   }

   private String a() {
      if (!this.isInGame()) {
         return "Join a world first.";
      }

      HashMap var1 = new HashMap();
      int var2 = this.X(var1);
      this.u1.clear();
      class_243 var3 = mc.field_1724.method_33571();
      List var4 = var1.keySet()
         .stream()
         .map(var0 -> new class_2338(g8.x(var0), g8.T(var0), g8.G(var0)))
         .sorted(Comparator.comparingDouble(var1x -> var3.method_1025(class_243.method_24953(var1x))))
         .toList();
      this.u1.addAll(var4.subList(0, Math.min(64, var4.size())));
      this.g = System.currentTimeMillis() + 15000L;
      q8.G(this.getName(), "Scan around the player: {} anchors in {} loaded chunks", var1.size(), var2);
      if (var4.isEmpty()) {
         return "No respawn anchors in the " + var2 + " loaded chunks around you.";
      }

      class_2338 var5 = (class_2338)var4.getFirst();
      return var1.size()
         + " anchors in the "
         + var2
         + " loaded chunks around you, shown for 15 seconds. Nearest at "
         + var5.method_23854()
         + ", "
         + (int)Math.round(Math.sqrt(var3.method_1025(class_243.method_24953(var5))))
         + " blocks away ("
         + var1.get(g8.M(var5.method_10263(), var5.method_10264(), var5.method_10260()))
         + " charges).";
   }

   @Override
   public void onEnable() {
      super.onEnable();
      if (this.i >= 0L) {
         long var1 = System.currentTimeMillis() - this.i;
         this.i = -1L;
         if (this.m() == this.uB && this.uB != null && var1 <= 60000L && mc.field_1724.method_5805()) {
            this.um = (boolean)1;
            this.m = mc.field_1687;
            this.z = mc.field_1724;
            q8.G(this.getName(), "Teleport landed in a new world after {} ms; carrying on", var1);
            this.U(System.currentTimeMillis(), "a new world");
            return;
         }

         this.uS = var1 > 60000L ? "the teleport did not land within a minute" : "the connection changed during the teleport";
         this.K(this.uS);
      }

      if (this.uS != null) {
         String var4 = this.uS;
         this.uS = null;
         this.D("it stopped earlier because " + var4 + ". Turn it on again to start a new run");
      } else {
         String var3 = tc.q(this);
         if (var3 != null) {
            this.D("turn off " + var3 + " first; one bot runs at a time");
         } else if (mc.field_1687.method_27983() != class_1937.field_25179) {
            this.D("go to the overworld first; it only hunts there");
         } else if (this.T(class_2246.field_23152.method_9564()) < 0) {
            this.D("put a diamond or netherite pickaxe with uses left in your hotbar");
         } else if (mc.field_1724.method_7344().method_7586() < 10 && tn.h(sz::H) < 0) {
            this.D("bring food in your hotbar; you are hungry");
         } else if (this.Q() || this.A.getValue() && this.y() && this.e(class_1802.field_23141) > 0) {
            this.a();
            this.um = (boolean)1;
            this.ul = null;
            this.m = mc.field_1687;
            this.z = mc.field_1724;
            this.uB = this.m();
            this.H = mc.method_1542();
            this.u_ = System.currentTimeMillis();
            this.O = mc.field_1724.method_6032();
            this.T.V();
            this.H();
            ArrayList var2 = new ArrayList();
            if (!this.uq.isEmpty()) {
               var2.add(String.join(", ", this.uq) + (this.uq.size() == 1 ? " is" : " are") + " off while hunting and back on after");
            }

            if (tn.h(sz::H) < 0) {
               var2.add("no food in the hotbar");
            }

            if (this.A.getValue() && !this.y()) {
               var2.add("no ender chest and Silk Touch pickaxe in the hotbar, so a full inventory ends the run");
            }

            if (this.Kh.getValue() && this.b() == 0) {
               var2.add("no pearls in the hotbar, so it walks to far anchors");
            }

            if (this.H) {
               var2.add("singleplayer has no /rtp, so it only hunts around you");
            }

            this.r(this.H ? "hunting anchors around you." : "hunting anchors with /rtp " + this.uj.getValue().toLowerCase(Locale.ROOT) + ".");
            if (!var2.isEmpty()) {
               this.r("heads up: " + String.join("; ", var2) + ".");
            }

            q8.G(
               this.getName(),
               "Started at {} ({}), region {}, range {}, dig {}, pearls {}, stash {}, stop after {}",
               mc.field_1724.method_24515().method_23854(),
               this.H ? "singleplayer" : "server",
               this.uj.getValue(),
               this.uE.getValue().intValue(),
               this.D.getValue(),
               this.Kh.getValue(),
               this.A.getValue(),
               this.uZ.getValue().intValue()
            );
            if (this.H) {
               this.K9 = this.u_;
               this.V(sz.z.K, "start");
            } else {
               this.V(sz.z.V, "start");
            }
         } else {
            this.D("your inventory is full; make room first");
         }
      }
   }

   @Override
   public void onDisable() {
      int var1 = this.isEnabled() && this.um ? 1 : 0;
      this.c();
      super.onDisable();
      this.T.E();
      fk.N(this);
      if (var1 == 0) {
         if (this.um) {
            this.K((String)null);
         }

         if (this.ul != null) {
            this.m(this.ul);
         }
      } else {
         int var2 = mc != null && mc.field_1724 != null && mc.field_1724.method_5805() ? 1 : 0;
         if (var2 == 0 || this.m() != this.uB || this.uB == null || !this.Kp.c() && this.uN != sz.z.M && this.uN != sz.z.j) {
            this.uS = var2 != 0 ? "the world, the player or the connection changed" : "you died";
            q8.G(this.getName(), "Interrupted in {} after {} anchors: {}", this.uN, this.ux, this.uS);
            this.K(this.uS);
         } else {
            this.i = System.currentTimeMillis();
            this.um = (boolean)0;
            q8.G(this.getName(), "World changed while waiting for /rtp; waiting for the new world");
         }
      }
   }

   private void K(String var1) {
      this.Z();
      if (this.um || var1 != null) {
         q8.G(
            this.getName(),
            "Run ended{}: {} anchors, {} teleports, {} spots{}",
            var1 == null ? "" : " (" + var1 + ")",
            this.ux,
            this.u7,
            this.I,
            this.C.isEmpty() ? "" : ", skipped " + this.C
         );
      }

      this.um = (boolean)0;
      this.m = this.z = this.uB = null;
      this.uW.clear();
   }

   private void a() {
      this.Kp.P();
      this.e.g();
      this.uW.clear();
      this.C.clear();
      this.u = null;
      this.ub.clear();
      this.uh = 0;
      this.uM = 0L;
      this.ue = 0;
      this.r = -1;
      this.uN = sz.z.H;
      this.uv = sz.z.t;
      this.uC = this.d = 0;
      this.uF = (boolean)0;
      this.F = -1L;
      this.uI = null;
      this.ux = this.u7 = this.I = this.KG = 0;
      this.uk = -2305843009213693952L;
      this.Kq = null;
      this.i = -1L;
      this.M();
      this.K.k();
   }

   private void M() {
      this.K3.clear();
      this.Y.clear();
      this.uQ = this.u5 = this.uV = (boolean)0;
      this.ud = null;
      this.uY = "";
      this.u4 = Double.MAX_VALUE;
      this.M.clear();
      this.o = null;
      this.t();
      this.U = sz.h.Z;
      this.h = 0;
      this.l = null;
   }

   private void t() {
      this.L = null;
      this.uD = this.V = this.X = this.p = this.Kt = this.u8 = this.ug = 0;
      this.uz = this.a = (boolean)0;
      this.uw.clear();
      this.u9.clear();
      this.uP.i();
      this.w = -1000;
   }

   private void c() {
      this.uP.i();
      bv.i(this);
      this.b = class_243.field_1353;
      this.u6 = this.K7 = this.uy = (boolean)0;
   }

   private Object m() {
      return mc != null && mc.method_1562() != null ? mc.method_1562().method_48296() : null;
   }

   private void D(String var1) {
      q8.G(this.getName(), "Not started: {}", var1);
      this.r("not started, " + var1 + ".");
      wt.T().U("Anchor Hunter", U(var1), wt.s.a);
      this.ul = "Not started: " + var1;
      this.m(this.ul);
      this.disable();
   }

   private void U(String var1) {
      String var2 = this.ux + " anchors in " + tc.T(System.currentTimeMillis() - this.u_) + ", " + this.u7 + " teleports";
      q8.G(this.getName(), "Stopped: {} ({})", var1, var2);
      this.r(sz.w.d, U(var1) + ". " + U(var2) + ".");
      this.ul = "Stopped: " + var1;
      this.m(this.ul);
      this.uN = sz.z.L;
      this.disable();
   }

   private void H() {
      this.uq.clear();
      com.larpclient.module.lr var1 = LarpClient.b().G();

      for (String var3 : uL) {
         Module var4 = var1.j(var3);
         if (var4 != null && var4 != this && var4.isEnabled()) {
            var4.setEnabled(false);
            if (!var4.isEnabled()) {
               this.uq.add(var3);
            }
         }
      }

      if (!this.uq.isEmpty()) {
         q8.G(this.getName(), "Turned off for the run: {}", String.join(", ", this.uq));
      }
   }

   private void Z() {
      if (!this.uq.isEmpty()) {
         com.larpclient.module.lr var1 = LarpClient.b().G();
         ArrayList var2 = new ArrayList();

         for (String var4 : this.uq) {
            Module var5 = var1.j(var4);
            if (var5 != null && !var5.isEnabled()) {
               var5.setEnabled(true);
               var2.add(var4);
            }
         }

         this.uq.clear();
         if (!var2.isEmpty()) {
            q8.G(this.getName(), "Turned back on: {}", String.join(", ", var2));
         }
      }
   }

   private String b() {
      com.larpclient.module.lr var1 = LarpClient.b().G();

      for (String var3 : uL) {
         Module var4 = var1.j(var3);
         if (var4 != null && var4 != this && var4.isEnabled()) {
            return var3;
         }
      }

      return null;
   }

   @Override
   public void onTick() {
      this.b = class_243.field_1353;
      this.u6 = this.K7 = this.uy = (boolean)0;
      boolean var8 = false;
      byte var7 = this.um;
      int var10000 = Integer.signum(var7);
      if ((var10000 | -var10000) >>> 31 != 0) {
         if (mc.field_1687 == this.m && mc.field_1724 == this.z) {
            byte var10 = 1;
            int var9 = this.uC;
            this.uC = var9 + var10;
            this.uO = mc.field_1724.field_6012;
            long var1 = System.currentTimeMillis();
            if (!mc.field_1724.method_5805()) {
               this.U("you died");
            } else {
               String var3 = this.b();
               if (var3 != null) {
                  this.U(var3 + " was turned on");
               } else {
                  this.Y(var1);
                  if (this.um && this.uN != sz.z.L) {
                     this.e(var1);
                     this.U(var1);
                     this.q();
                     String var4 = tn.b();
                     if (var4 != null) {
                        this.c();
                        this.T.K();
                        this.u6 = (boolean)1;
                        this.m(var4 + ": swimming up");
                     } else if (!this.x(var1)) {
                        boolean var12 = false;
                        byte var11 = this.u5;
                        var10000 = Integer.signum(var11);
                        if (((var10000 | -var10000) >>> 31 ^ 1) == 0) {
                           this.T(var1);
                        } else if (this.j() instanceof String var5) {
                           if (this.uN == sz.z.s && mc.field_1724.field_7512 instanceof class_1707) {
                              mc.field_1724.method_7346();
                           }

                           if (this.uN != sz.z.f) {
                              this.uv = this.uN != sz.z.s && this.uN != sz.z.o ? this.uN : sz.z.q;
                              this.V(sz.z.f, var5);
                           }

                           this.c();
                           if (!this.b() && cm.j() instanceof class_465) {
                              fk.N(this);
                           }

                           this.m("Paused: " + var5);
                        } else {
                           if (this.uN == sz.z.f) {
                              this.K.k();
                              this.V(this.uv, "resumed");
                           }

                           if (this.H() && this.T.d(4)) {
                              this.c();
                              this.m("Eating");
                           } else {
                              if (this.w()) {
                                 boolean var14 = false;
                                 byte var13 = this.H;
                                 var10000 = Integer.signum(var13);
                                 if ((var10000 | -var10000) >>> 31 == 0 && var1 - this.K9 > 360000L) {
                                    this.Q("six minutes on this spot");
                                    return;
                                 }
                              }

                              switch (this.uN) {
                                 case H:
                                 case V:
                                    this.O(var1);
                                    break;
                                 case M:
                                 case j:
                                    this.o(var1);
                                    break;
                                 case G:
                                    if (this.Kp.D(var1)) {
                                       this.O(var1);
                                    }
                                    break;
                                 case K:
                                    this.a(var1);
                                    break;
                                 case t:
                                    this.C();
                                    break;
                                 case X:
                                    this.z(var1);
                                    break;
                                 case q:
                                    this.L(var1);
                                    break;
                                 case o:
                                    this.P(var1);
                                    break;
                                 case n:
                                    this.i();
                                    break;
                                 case s:
                                    this.m();
                                    break;
                                 case Q:
                                    this.r();
                                    break;
                                 case u:
                                    this.uQ = (boolean)1;
                                    this.Q("the area is protected");
                                    break;
                                 case w:
                                    this.V(this.Kp.c() ? sz.z.M : sz.z.V, "nobody near now");
                                 case f:
                                 case L:
                              }

                              if (this.um && this.uN != sz.z.L) {
                                 this.m(this.I(var1));
                              }
                           }
                        }
                     }
                  }
               }
            }
         } else {
            this.U("the world or the player changed");
         }
      }
   }

   private String I(long var1) {
      return switch (this.uN) {
         case G -> "Waiting for /rtp (" + O(this.Kp.n(var1)) + ")";
         default -> this.uN.m;
         case q -> this.U == sz.h.y
            ? "Aiming a pearl at the anchor"
            : (
               this.U == sz.h.H
                  ? "Pearling to an anchor"
                  : (
                     this.uP.y() != null
                        ? "Digging toward the anchor at " + this.L.method_23854()
                        : (this.L == null ? this.uN.m : "Going to the anchor at " + this.L.method_23854() + ", " + this.K(this.L) + " blocks")
                  )
            );
         case o -> this.L == null ? this.uN.m : "Mining the anchor at " + this.L.method_23854();
         case s -> "Stashing anchors in the ender chest (" + (this.h + 1) + "/6)";
      };
   }

   private int K(class_2338 var1) {
      return (int)Math.sqrt(mc.field_1724.method_5707(class_243.method_24953(var1)));
   }

   private String j() {
      if (wp.d()) {
         return "Freecam is on";
      } else if (this.j()) {
         return "you are steering";
      } else {
         return cm.j() instanceof class_465 && !this.b() ? "an inventory is open" : null;
      }
   }

   private boolean j() {
      return mc.method_1569()
         && cm.j() == null
         && (
            com.larpclient.j.W(mc.field_1690.field_1894)
               || com.larpclient.j.W(mc.field_1690.field_1881)
               || com.larpclient.j.W(mc.field_1690.field_1913)
               || com.larpclient.j.W(mc.field_1690.field_1849)
               || com.larpclient.j.W(mc.field_1690.field_1903)
               || com.larpclient.j.W(mc.field_1690.field_1832)
         );
   }

   private boolean H() {
      return this.uN == sz.z.G
         || this.uN == sz.z.K
         || this.uN == sz.z.t
         || this.uN == sz.z.X
         || this.uN == sz.z.q && !this.uP.G()
         || this.uN == sz.z.o && mc.field_1724.method_7344().method_7586() <= 6;
   }

   private boolean w() {
      return this.uN == sz.z.t || this.uN == sz.z.X || this.uN == sz.z.q || this.uN == sz.z.o || this.uN == sz.z.n;
   }

   private void V(sz.z var1, String var2) {
      if (var1 != this.uN) {
         q8.v(this.getName(), "{} -> {} ({})", this.uN, var1, var2);
         this.uN = var1;
         this.d = this.uC;
         this.uG = System.currentTimeMillis();
      }
   }

   private void O(long var1) {
      if (this.H) {
         this.h("mined every anchor it can reach around you (singleplayer has no /rtp)");
      } else if (!this.Kp.D(var1)) {
         this.V(sz.z.G, "rtp not ready");
      } else {
         this.c();
         if (!mc.field_1724.method_24828() && !mc.field_1724.method_5799()) {
            this.m("Landing before /rtp");
         } else {
            this.uI = this.e.n(this.uj.getValue());
            mc.field_1724.field_3944.method_45730("rtp " + this.uI);
            this.Kp.M(var1);
            this.uk = var1;
            this.uF = (boolean)0;
            this.u7++;
            this.Kq = mc.field_1724.method_73189();
            q8.G(this.getName(), "Sent /rtp {} ({} so far)", this.uI, this.u7);
            this.V(sz.z.M, "rtp " + this.uI);
         }
      }
   }

   private void o(long var1) {
      if (this.Kp.B(var1)) {
         this.e.D(this.uI);
         if (this.Kp.r(var1)) {
            this.U("/rtp did not answer 5 times in a row");
         } else {
            q8.G(this.getName(), "/rtp {} got no answer; trying again in {}", this.uI, O(this.Kp.n(var1)));
            this.V(sz.z.G, "rtp silent");
         }
      } else {
         if (!this.Kp.c()) {
            this.V(sz.z.G, "rtp answered");
         }
      }
   }

   private void Y(long var1) {
      Set var4 = null;

      String var3;
      while ((var3 = this.uW.poll()) != null) {
         if (var4 == null) {
            var4 = this.h();
         }

         Set var5 = var4;
         t5.k var6 = t5.H(var3, var1x -> var5.contains(var1x.toLowerCase(Locale.ROOT)));
         if (this.uN == sz.z.Q && t5.D(var3).contains("sold")) {
            q8.G(this.getName(), "Sell reply: {}", t5.D(var3));
         }

         if (var6.kind() != t5.v.M) {
            q8.G(this.getName(), "Server reply {} ({} ms wait): {}", var6.kind(), var6.waitMs(), t5.D(var3));
            this.T(var6, var1);
            if (!this.um || this.uN == sz.z.L) {
               return;
            }
         }
      }
   }

   private Set<String> h() {
      HashSet var1 = new HashSet();
      if (mc.method_1562() != null) {
         for (class_640 var3 : mc.method_1562().method_2880()) {
            var1.add(var3.method_2966().name().toLowerCase(Locale.ROOT));
         }
      }

      return var1;
   }

   private void T(t5.k var1, long var2) {
      boolean var4 = this.Kp.c();
      int var5 = var2 - this.uk < 2000L ? 1 : 0;
      switch (var1.kind()) {
         case E:
         case s:
            this.Kp.D(var2);
            break;
         case A:
            if (var4) {
               this.uF = (boolean)1;
               this.F = var2;
               if (this.uN == sz.z.M) {
                  this.V(sz.z.j, "countdown");
               }
            }
            break;
         case o:
            if (!var4) {
               return;
            }

            this.Kp.j(var2, var1.waitMs());
            q8.G(this.getName(), "/rtp is cooling down; next in {}", O(this.Kp.n(var2)));
            this.W("rtp cooldown");
            break;
         case O:
            if (var4) {
               this.Kp.m(var2, 10000L);
               this.W("combat tagged");
            }
            break;
         case l:
            if (var4 && var5 != 0) {
               this.Kp.m(var2, 1500L);
               this.W("chat limit");
            }
            break;
         case y:
            if (var4 && var5 != 0) {
               this.U("this server has no /rtp");
            }
            break;
         case j:
         case z:
            if (var4 || this.uF) {
               this.Kp.m(var2, 3000L);
               this.uF = (boolean)0;
               this.W(var1.kind() == t5.v.z ? "unsafe spot" : "teleport cancelled");
            }
            break;
         case f:
            if (var4) {
               this.e.D(this.uI);
               this.Kp.m(var2, 3000L);
               this.W("region down");
            }
            break;
         case v:
            if (this.uN == sz.z.s) {
               this.uH = "the area is protected";
            } else if ((this.uN == sz.z.o || this.uN == sz.z.q) && var2 - this.uJ <= 3000L) {
               this.c();
               q8.G(this.getName(), "The server says {} is protected", this.L == null ? "this spot" : this.L.method_23854());
               this.V(sz.z.u, "protected area");
            }
         case Q:
         case M:
      }
   }

   private void W(String var1) {
      if (this.uN == sz.z.M || this.uN == sz.z.j || this.uN == sz.z.w) {
         this.V(sz.z.G, var1);
      }
   }

   private void e(long var1) {
      class_243 var3 = mc.field_1724.method_73189();
      class_243 var4 = this.Kq;
      this.Kq = var3;
      if (var4 != null && (this.Kp.c() || this.uF)) {
         if (var3.method_1022(var4) > 64.0) {
            this.U(var1, String.format(Locale.ROOT, "%.0f blocks", var3.method_1022(var4)));
         }
      }
   }

   private void U(long var1, String var3) {
      this.Kp.U(var1);
      this.e.R(this.uI);
      this.I++;
      this.uF = (boolean)0;
      this.M();
      this.c();
      this.K9 = var1;
      this.Kq = mc.field_1724.method_73189();
      q8.G(this.getName(), "Teleported ({}) to {}; spot {}", var3, mc.field_1724.method_24515().method_23854(), this.I);
      this.V(sz.z.K, "teleported");
   }

   private void Q(String var1) {
      this.c();
      this.t();
      q8.G(this.getName(), "Leaving this spot: {}", var1);
      if (this.H) {
         this.h("mined every anchor it can reach around you (singleplayer has no /rtp)");
      } else {
         this.V(this.Kp.D(System.currentTimeMillis()) ? sz.z.V : sz.z.G, var1);
      }
   }

   private int S() {
      return Math.clamp(mc.field_1690.method_38521(), 2, 12);
   }

   private void a(long var1) {
      if (mc.field_1687.method_27983() != class_1937.field_25179) {
         if (var1 - this.uG > 10000L) {
            this.U("the /rtp sent you out of the overworld");
         }
      } else {
         int var3 = !mc.field_1724.method_24828() && !mc.field_1724.method_5799() ? 0 : 1;
         int var4 = this.S();
         int var5 = 0;
         int var6 = 0;
         int var7 = mc.field_1724.method_31477() >> 4;
         int var8 = mc.field_1724.method_31479() >> 4;

         for (int var9 = -var4; var9 <= var4; var9++) {
            for (int var10 = -var4; var10 <= var4; var10++) {
               var6++;
               if (mc.field_1687.method_2935().method_2857(var7 + var9, var8 + var10, class_2806.field_12803, false) != null) {
                  var5++;
               }
            }
         }

         double var11 = var6 == 0 ? 0.0 : (double)var5 / var6;
         if (var3 != 0 && var1 - this.uG >= 1000L && var11 >= 0.9 || var1 - this.uG > 8000L) {
            q8.v(this.getName(), "Landed; {}% of {} chunks loaded", Math.round(var11 * 100.0), var6);
            this.V(sz.z.t, "landed");
         }
      }
   }

   private int X(Map<Long, Integer> var1) {
      int var2 = this.S();
      int var3 = 0;
      int var4 = mc.field_1724.method_31477() >> 4;
      int var5 = mc.field_1724.method_31479() >> 4;

      for (int var6 = -var2; var6 <= var2; var6++) {
         for (int var7 = -var2; var7 <= var2; var7++) {
            class_2818 var8 = mc.field_1687.method_2935().method_2857(var4 + var6, var5 + var7, class_2806.field_12803, false);
            if (var8 != null) {
               var3++;
               class_2826[] var9 = var8.method_12006();

               for (int var10 = 0; var10 < var9.length; var10++) {
                  final class_2826 var11 = var9[var10];
                  if (var11 != null && !var11.method_38292()) {
                     ig.f(new ig.r() {
                        @Override
                        public boolean maybeHasAnchor() {
                           return var11.method_19523(var0 -> var0.method_27852(class_2246.field_23152));
                        }

                        @Override
                        public class_2680 state(int var1, int var2x, int var3x) {
                           return var11.method_12254(var1, var2x, var3x);
                        }
                     }, var4 + var6 << 4, var8.method_31604(var10) << 4, var5 + var7 << 4, var1);
                  }
               }
            }
         }
      }

      return var3;
   }

   private void C() {
      this.K3.clear();
      int var1 = this.X(this.K3);
      this.K3.keySet().removeAll(this.Y);
      q8.G(this.getName(), "{} anchors in {} loaded chunks", this.K3.size(), var1);
      if (this.K3.isEmpty()) {
         this.KG++;
         this.Q("no anchors here");
      } else {
         this.r(sz.w.g, this.K3.size() + (this.K3.size() == 1 ? " anchor" : " anchors") + " in sight of the chunks here");
         this.V(sz.z.X, this.K3.size() + " anchors");
      }
   }

   private void z(long var1) {
      this.X(this.K3);
      this.K3.keySet().removeAll(this.Y);
      class_243 var3 = mc.field_1724.method_33571();
      List var4 = this.K3
         .keySet()
         .stream()
         .sorted(Comparator.comparingDouble(var1x -> var3.method_1028(g8.x(var1x) + 0.5, g8.T(var1x) + 0.5, g8.G(var1x) + 0.5)))
         .limit(48L)
         .toList();
      class_2338 var5 = null;
      double var6 = Double.MAX_VALUE;
      int var8 = mc.field_1724.method_31478();
      cm var9 = this.P((class_2338)null);

      for (long var11 : var4) {
         var9.D(new class_2338(g8.x(var11), g8.T(var11), g8.G(var11)));
      }

      ig.h var21 = this.u();

      for (long var12 : var4) {
         class_2338 var14 = new class_2338(g8.x(var12), g8.T(var12), g8.G(var12));
         if (!mc.field_1687.method_8320(var14).method_27852(class_2246.field_23152)) {
            this.K3.remove(var12);
         } else {
            double var15 = Math.hypot(var14.method_10263() + 0.5 - mc.field_1724.method_23317(), var14.method_10260() + 0.5 - mc.field_1724.method_23321());
            ig.b var17 = var15 <= 48.0 ? ig.v(var9, var14.method_10263(), var14.method_10264(), var14.method_10260(), var21) : null;
            String var18 = var17 == null
               ? (this.uQ ? "protected" : null)
               : ig.x(var9, var14.method_10263(), var14.method_10264(), var14.method_10260(), var8, var17, this.D.getValue(), this.uQ);
            if (var18 != null) {
               this.E(var12, var18);
            } else {
               double var19 = Math.sqrt(var3.method_1025(class_243.method_24953(var14)))
                  + (var17 != null && var17.exposed().isEmpty() ? 6 + 3 * var17.dig().getFirst().digs() : 0);
               if (var19 < var6) {
                  var6 = var19;
                  var5 = var14;
               }
            }
         }
      }

      if (var5 == null) {
         this.Q(this.K3.isEmpty() ? "no anchors left here" : "no safe anchors left here");
      } else {
         this.o(var5, var1);
      }
   }

   private void E(long var1, String var3) {
      this.Y.add(var1);
      this.K3.remove(var1);
      this.C.merge(var3, 1, Integer::sum);
      q8.v(this.getName(), "Skipping the anchor at {} {} {}: {}", g8.x(var1), g8.T(var1), g8.G(var1), var3);
   }

   private void o(class_2338 var1, long var2) {
      this.t();
      this.L = var1.method_10062();
      this.uD = this.K3.getOrDefault(g8.M(var1.method_10263(), var1.method_10264(), var1.method_10260()), 0);
      this.uK = var2;
      this.KC = this.uP.R();
      q8.G(
         this.getName(),
         "Going for the anchor at {} ({} charges, {} blocks away)",
         this.L.method_23854(),
         this.uD,
         (int)Math.sqrt(mc.field_1724.method_5707(class_243.method_24953(this.L)))
      );
      this.V(sz.z.q, "target " + this.L.method_23854());
   }

   private void e(String var1) {
      if (this.L != null) {
         this.E(g8.M(this.L.method_10263(), this.L.method_10264(), this.L.method_10260()), var1);
         q8.G(this.getName(), "Gave up on the anchor at {}: {}", this.L.method_23854(), var1);
      }

      this.c();
      this.t();
      this.V(sz.z.X, var1);
   }

   private void L(long var1) {
      if (this.L == null) {
         this.V(sz.z.X, "no target");
      } else if (!mc.field_1687.method_8320(this.L).method_27852(class_2246.field_23152)) {
         this.K3.remove(g8.M(this.L.method_10263(), this.L.method_10264(), this.L.method_10260()));
         q8.v(this.getName(), "The anchor at {} is gone", this.L.method_23854());
         this.t();
         this.V(sz.z.X, "anchor gone");
      } else if (!this.Q()) {
         this.A();
      } else if (this.V < 3 && this.uP.R() - this.KC <= 40 && var1 - this.uK <= 120000L) {
         double var3 = Math.hypot(this.L.method_10263() + 0.5 - mc.field_1724.method_23317(), this.L.method_10260() + 0.5 - mc.field_1724.method_23321());
         if (!this.a && var3 > 28.0) {
            this.a = (boolean)1;
         } else if (this.a && var3 <= 22.0) {
            this.a = (boolean)0;
            this.uP.i();
         }

         if (this.U == sz.h.Z && (!this.a || !this.Kh.getValue()) || !this.B()) {
            if (this.uP.G() && (!this.a || this.uC - this.w <= 100) && (!this.uP.W() || this.uP.M().size() > 4 || this.uC - this.w <= 10)
               || this.e()
               || this.uP.G()) {
               e3.l var5 = this.uP.d(this.c(), this::G, true);
               this.E();
               switch (var5) {
                  case L:
                     this.uP.i();
                     if (!this.a && this.uw.contains(this.M())) {
                        this.t(var1);
                     }
                     break;
                  case s:
                     this.V++;
                     q8.G(this.getName(), "Route to {} stuck ({}): {}", this.L.method_23854(), this.V, this.uP.D());
                     this.uP.i();
               }
            }
         }
      } else {
         this.e(this.V >= 3 ? "no way there" : (this.uP.R() - this.KC > 40 ? "too much digging" : "took too long to reach"));
      }
   }

   private boolean e() {
      this.w = this.uC;
      cm var1 = this.P(this.L);
      class_2338 var2 = this.Z();
      g8.t var3;
      if (this.a) {
         g8.l var4 = g8.l.around(
            var2.method_10263(), var2.method_10264(), var2.method_10260(), this.L.method_10263(), this.L.method_10264(), this.L.method_10260(), 16, 24, 24
         );
         g8.b var5 = new g8.b(16000, this.D.getValue(), 3, true, 3.0);
         var3 = g8.O(
            var1,
            var2.method_10263(),
            var2.method_10264(),
            var2.method_10260(),
            g8.i.T(this.L.method_10263() + 0.5, this.L.method_10264(), this.L.method_10260() + 0.5, 18.0, 24.0),
            var4,
            var5
         );
      } else {
         ig.b var10 = ig.v(var1, this.L.method_10263(), this.L.method_10264(), this.L.method_10260(), this.u());
         String var11 = ig.x(var1, this.L.method_10263(), this.L.method_10264(), this.L.method_10260(), var2.method_10264(), var10, this.D.getValue(), this.uQ);
         if (var11 != null) {
            this.e(var11);
            return false;
         }

         this.uw.clear();

         for (ig.z var7 : var10.usable(this.D.getValue())) {
            long var8 = g8.M(var7.x(), var7.y(), var7.z());
            if (!this.u9.contains(var8)) {
               this.uw.add(var8);
            }
         }

         if (this.uw.isEmpty()) {
            this.e("no clear view from any standing spot");
            return false;
         }

         if (this.uw.contains(this.M())) {
            this.t(System.currentTimeMillis());
            return false;
         }

         g8.l var12 = g8.l.around(
            var2.method_10263(), var2.method_10264(), var2.method_10260(), this.L.method_10263(), this.L.method_10264(), this.L.method_10260(), 10, 12, 12
         );
         g8.b var13 = new g8.b(16000, this.D.getValue(), 3, false, 1.0);
         var3 = g8.O(
            var1,
            var2.method_10263(),
            var2.method_10264(),
            var2.method_10260(),
            g8.i.S(this.uw, this.L.method_10263(), this.L.method_10264() - 1, this.L.method_10260(), 1.0),
            var12,
            var13
         );
      }

      if (!var3.found()) {
         this.V++;
         if (var3.explored() <= 1) {
            this.o(var2);
         }

         q8.G(this.getName(), "No route toward {} ({} cells searched, try {})", this.L.method_23854(), var3.explored(), this.V);
         return false;
      } else if (var3.steps().isEmpty()) {
         if (this.a) {
            this.a = (boolean)0;
         }

         return false;
      } else {
         this.uP.S(var3.steps(), var3.complete());
         q8.v(
            this.getName(),
            "Route of {} steps ({} to dig{}) toward {}",
            var3.steps().size(),
            var3.digs(),
            var3.complete() ? "" : ", partial",
            this.L.method_23854()
         );
         return true;
      }
   }

   private class_2338 Z() {
      return class_2338.method_49637(mc.field_1724.method_23317(), mc.field_1724.method_23318() + 0.2, mc.field_1724.method_23321());
   }

   private void o(class_2338 var1) {
      Function var2 = var0 -> class_7923.field_41175.method_10221(mc.field_1687.method_8320(var0).method_26204()).method_12832()
         + (mc.field_1687.method_8316(var0).method_15769() ? "" : "+fluid");
      q8.G(
         this.getName(),
         "Route could not start at {} (y {}): feet {}, head {}, below {}, in water {}, on ground {}",
         var1.method_23854(),
         String.format(Locale.ROOT, "%.3f", mc.field_1724.method_23318()),
         var2.apply(var1),
         var2.apply(var1.method_10084()),
         var2.apply(var1.method_10074()),
         mc.field_1724.method_5799(),
         mc.field_1724.method_24828()
      );
   }

   private long M() {
      class_2338 var1 = this.Z();
      return g8.M(var1.method_10263(), var1.method_10264(), var1.method_10260());
   }

   private cm P(class_2338 var1) {
      cm var2 = new cm(this::G, this.i());
      if (var1 != null) {
         var2.D(var1);
      }

      for (class_1309 var4 : mc.field_1687
         .method_8390(
            class_1309.class,
            mc.field_1724.method_5829().method_1014(24.0),
            var0 -> var0 instanceof class_1569 && var0.method_5805() && mc.field_1724.method_6057(var0)
         )) {
         var2.j(var4.method_23317(), var4.method_23318(), var4.method_23321(), var4 instanceof class_1548 ? 7.0 : 3.0);
      }

      return var2;
   }

   private ig.h u() {
      return (var0, var2, var4, var6, var7, var8) -> {
         class_243 var9 = new class_243(var0, var2, var4);
         class_2338 var10 = new class_2338(var6, var7, var8);
         class_243 var11 = class_243.method_24953(var10);

         for (int var12 = -1; var12 < 6; var12++) {
            class_243 var13 = var12 < 0 ? var11 : var11.method_1019(class_243.method_24954(class_2350.values()[var12].method_62675()).method_1021(0.49));
            class_3965 var14 = mc.field_1687.method_17742(new class_3959(var9, var13, class_3960.field_17559, class_242.field_1348, mc.field_1724));
            if (var14.method_17783() != class_240.field_1332 || var14.method_17777().equals(var10)) {
               return true;
            }
         }

         return false;
      };
   }

   private void t(long var1) {
      this.uP.i();
      this.X = 0;
      this.u8 = 0;
      this.uz = (boolean)0;
      this.ui = this.e(class_1802.field_23141);
      this.uJ = var1;
      this.V(sz.z.o, "at a standing spot");
   }

   private void P(long var1) {
      if (this.L == null) {
         this.V(sz.z.X, "no target");
      } else {
         class_2680 var3 = mc.field_1687.method_8320(this.L);
         if (!var3.method_27852(class_2246.field_23152)) {
            bv.i(this);
            if (!this.uz) {
               this.uz = (boolean)1;
               this.p = 0;
            }

            if (++this.p >= 3) {
               this.uU = this.uC;
               this.K3.remove(g8.M(this.L.method_10263(), this.L.method_10264(), this.L.method_10260()));
               this.V(sz.z.n, "anchor broken");
            }
         } else {
            if (this.uz) {
               this.uz = (boolean)0;
               if (++this.Kt >= 2) {
                  q8.G(this.getName(), "The anchor at {} came back twice", this.L.method_23854());
                  this.V(sz.z.u, "anchor came back twice");
                  return;
               }
            }

            if (!this.Q()) {
               this.A();
            } else if (++this.X > 900) {
               this.e("took too long to mine");
            } else {
               double var4 = this.c();
               if (mc.field_1724.method_33571().method_1022(class_243.method_24953(this.L)) > var4 + 0.2) {
                  this.V(sz.z.q, "out of reach");
               } else if (this.i() && this.L(this.L)) {
                  this.e("water or lava next to it and your pickaxe may cut 3x3");
               } else {
                  class_2338 var6 = this.L;
                  this.k();
                  boolean var7 = bv.q(this, var6, var4, true, 12.0F, this::T, () -> !this.j());
                  if (var7) {
                     this.u8 = 0;
                  } else {
                     if (++this.u8 > 20) {
                        this.u9.add(this.M());
                        this.u8 = 0;
                        if (this.u9.size() >= 3) {
                           this.e("no clear view");
                           return;
                        }

                        q8.v(
                           this.getName(),
                           "No clear view of {} from {}; trying another spot",
                           this.L.method_23854(),
                           mc.field_1724.method_24515().method_23854()
                        );
                        this.uP.i();
                        this.V(sz.z.q, "lost sight");
                     }
                  }
               }
            }
         }
      }
   }

   private void i() {
      this.T();
      int var1 = this.e(class_1802.field_23141);
      if (var1 > this.ui) {
         int var4 = var1 - this.ui;
         this.ux += var4;
         q8.G(this.getName(), "Picked up {} anchor{} ({} total){}", var4, var4 == 1 ? "" : "s", this.ux, this.uD > 0 ? ", it had " + this.uD + " charges" : "");
         this.r(sz.w.q, "+" + var4 + " anchor" + (var4 == 1 ? "" : "s") + " (" + this.ux + " total)");
         this.c();
         this.t();
         if (this.q.getValue() && !this.H && System.currentTimeMillis() >= this.uM) {
            this.X();
         } else {
            int var5 = this.uZ.getValue().intValue();
            if (var5 > 0 && this.ux >= var5) {
               this.h("collected " + this.ux + " anchors");
            } else {
               this.V(sz.z.X, "picked up");
            }
         }
      } else {
         int var2 = this.uC - this.uU;
         if (var2 > 200) {
            q8.G(this.getName(), "Lost the drop of the anchor at {}", this.L == null ? "?" : this.L.method_23854());
            this.c();
            this.t();
            this.V(sz.z.X, "drop lost");
         } else if (var2 >= 10) {
            class_1542 var3 = this.U(class_1802.field_23141, 8.0);
            if (var3 != null) {
               this.P(var3);
            }
         }
      }
   }

   private void h(String var1) {
      q8.G(this.getName(), "Done: {}", var1);
      this.r(sz.w.q, "Done, " + var1 + ".");
      this.ul = "Done: " + var1;
      this.m(this.ul);
      this.uN = sz.z.L;
      this.disable();
   }

   private class_1542 U(class_1792 var1, double var2) {
      List var4 = mc.field_1687
         .method_8390(class_1542.class, mc.field_1724.method_5829().method_1014(var2), var1x -> var1x.method_5805() && var1x.method_6983().method_31574(var1));
      return var4.stream().min(Comparator.comparingDouble(var0 -> var0.method_5858(mc.field_1724))).orElse(null);
   }

   private void P(class_1542 var1) {
      class_2338 var2 = class_2338.method_49638(var1.method_73189());
      class_2338 var3 = this.Z();
      if (!(Math.hypot(var1.method_23317() - mc.field_1724.method_23317(), var1.method_23321() - mc.field_1724.method_23321()) < 0.6)) {
         if (!this.uP.G() || this.uC - this.w > 20) {
            this.w = this.uC;
            g8.l var4 = g8.l.around(
               var3.method_10263(), var3.method_10264(), var3.method_10260(), var2.method_10263(), var2.method_10264(), var2.method_10260(), 6, 6, 4
            );
            g8.t var5 = g8.O(
               this.P((class_2338)null),
               var3.method_10263(),
               var3.method_10264(),
               var3.method_10260(),
               g8.i.T(var1.method_23317(), var2.method_10264(), var1.method_23321(), 0.5, 1.0),
               var4,
               g8.b.walking(4000)
            );
            if (!var5.found() || var5.steps().isEmpty()) {
               this.F(var1.method_73189());
               return;
            }

            this.uP.X(var5.steps());
         }

         e3.l var6 = this.uP.d(this.c(), this::G, false);
         this.E();
         if (var6 == e3.l.L || var6 == e3.l.s) {
            this.uP.i();
            this.F(var1.method_73189());
         }
      }
   }

   private void F(class_243 var1) {
      class_243 var2 = var1.method_1020(mc.field_1724.method_73189()).method_18805(1.0, 0.0, 1.0);
      double var3 = var2.method_1033();
      double var5 = var3 < 1.0E-6 ? 0.0 : mc.field_1724.method_18798().method_18805(1.0, 0.0, 1.0).method_1026(var2.method_1029());
      this.K7 = (boolean)1;
      boolean var7 = this.K.V(var3, var5, cr.a(this));
      this.b = var7 ? var2 : class_243.field_1353;
   }

   private void E() {
      this.b = this.uP.g();
      this.u6 = this.uP.t();
      this.K7 = this.uP.V();
      this.uy = this.uP.C();
   }

   private void A() {
      this.c();
      if (this.A.getValue() && this.y() && this.e(class_1802.field_23141) > 0) {
         this.uv = this.L != null ? sz.z.q : sz.z.X;
         this.h = 0;
         this.v = this.uC;
         this.up = 0;
         this.uH = null;
         if (this.l != null && !mc.field_1687.method_8320(this.l).method_27852(class_2246.field_10443)) {
            this.l = null;
         }

         this.u0 = this.e(class_1802.field_23141);
         q8.G(this.getName(), "Inventory full with {} anchors; stashing them in the ender chest", this.u0);
         this.V(sz.z.s, "inventory full");
      } else {
         this.U(this.e(class_1802.field_23141) > 0 ? "your inventory is full" : "your inventory is full of other things; make room for anchors");
      }
   }

   private void m() {
      if (this.uH != null && this.h <= 1) {
         this.p(this.uH);
      } else if (this.uC - this.v > 300) {
         this.p("step " + this.h + " took too long");
      } else if (mc.field_1724.method_24828()) {
         switch (this.h) {
            case 0:
               this.O();
               break;
            case 1:
               this.S();
               break;
            case 2:
               this.L();
               break;
            case 3:
               this.D();
               break;
            case 4:
               this.l();
               break;
            case 5:
               this.f();
               break;
            default:
               this.p("unknown step");
         }
      }
   }

   private void W(int var1) {
      this.h = var1;
      this.v = this.uC;
   }

   private void O() {
      if (this.l != null && mc.field_1687.method_8320(this.l).method_27852(class_2246.field_10443)) {
         this.W(1);
      } else {
         if (this.l == null) {
            class_2338 var1 = this.Z();
            int[] var2 = ig.v(this.P(this.L), var1.method_10263(), var1.method_10264(), var1.method_10260());
            if (var2 == null) {
               this.p("no room to place the ender chest");
               return;
            }

            this.l = new class_2338(var2[0], var2[1], var2[2]);
         }

         int var4 = tn.h(var0 -> var0.method_31574(class_1802.field_8466));
         if (var4 < 0) {
            this.p("no ender chest in the hotbar");
         } else if (!this.H(var4)) {
            this.m("Taking out the ender chest");
         } else {
            class_2338 var5 = this.l;
            boolean var3 = fk.a(
               this,
               var5,
               class_1802.field_8466,
               this.c(),
               true,
               true,
               () -> fk.v(var5),
               var2x -> q8.v(this.getName(), "Ender chest at {} {}", var5.method_23854(), var2x ? "placed" : "rejected")
            );
            if (!var3 && this.uC - this.v > 60) {
               this.l = null;
               this.v = this.uC;
               if (++this.up >= 3) {
                  this.p("could not place the ender chest");
               }
            }
         }
      }
   }

   private void S() {
      if (mc.field_1724.field_7512 instanceof class_1707) {
         this.W(2);
      } else if (!mc.field_1687.method_8320(this.l).method_27852(class_2246.field_10443)) {
         if (++this.up >= 3) {
            this.p("the ender chest disappeared");
         } else {
            this.l = null;
            this.W(0);
         }
      } else if (cm.j() == null) {
         class_1799 var1 = mc.field_1724.method_6047();
         if (this.uC % 10 == 0) {
            fk.C(
               this,
               this.l,
               var1.method_7909(),
               this.c(),
               true,
               false,
               () -> cm.j() == null,
               var1x -> q8.v(this.getName(), "Opening the ender chest {}", var1x ? "sent" : "refused")
            );
         }
      }
   }

   private void L() {
      if (!(mc.field_1724.field_7512 instanceof class_1707 var1)) {
         this.W(1);
      } else if (!(mc.field_1724.method_18798().method_37268() > 1.0E-4)) {
         int var7 = var1.method_17388() * 9;
         int var3 = -1;
         byte var4 = 0;

         for (int var5 = 0; var5 < var1.field_7761.size(); var5++) {
            class_1799 var6 = ((class_1735)var1.field_7761.get(var5)).method_7677();
            if (var5 < var7) {
               if (var6.method_7960() || var6.method_31574(class_1802.field_23141) && var6.method_7947() < var6.method_7914()) {
                  var4 = 1;
               }
            } else if (var3 < 0 && var6.method_31574(class_1802.field_23141)) {
               var3 = var5;
            }
         }

         if (var3 >= 0 && var4 != 0) {
            if (this.uC - this.x >= 4) {
               this.x = this.uC;
               mc.field_1761.method_2906(var1.field_7763, var3, 0, class_1713.field_7794, mc.field_1724);
            }
         } else {
            int var8 = Math.max(0, this.u0 - this.e(class_1802.field_23141));
            if (var8 == 0) {
               this.uH = var3 < 0 ? "no anchors to store" : "the ender chest is full";
            }

            q8.G(this.getName(), "Stored {} anchors in the ender chest{}", var8, this.uH == null ? "" : " (" + this.uH + ")");
            this.W(3);
         }
      }
   }

   private void D() {
      if (mc.field_1724.field_7512 instanceof class_1707) {
         mc.field_1724.method_7346();
      } else {
         this.Q = this.e(class_1802.field_8466);
         this.W(4);
      }
   }

   private void l() {
      if (!mc.field_1687.method_8320(this.l).method_27852(class_2246.field_10443)) {
         this.uU = this.uC;
         this.W(5);
      } else if (!this.b(new class_1799(class_1802.field_8466))) {
         this.p("no room to pick the ender chest back up");
      } else if (this.i() < 0) {
         this.p("no Silk Touch pickaxe in the hotbar");
      } else {
         class_2338 var1 = this.l;
         if (!bv.q(this, var1, this.c(), true, 12.0F, var1x -> this.i(), () -> true) && this.uC - this.v > 60) {
            this.p("cannot reach the ender chest to take it back");
         }
      }
   }

   private void f() {
      if (this.e(class_1802.field_8466) > this.Q) {
         bv.i(this);
         int var2 = Math.max(0, this.u0 - this.e(class_1802.field_23141));
         if (this.uH != null && var2 == 0) {
            this.U("your inventory is full and " + this.uH);
         } else {
            this.r(sz.w.q, var2 + " anchors stored in your ender chest");
            this.l = null;
            this.V(this.uv, "stashed");
         }
      } else {
         class_1542 var1 = this.U(class_1802.field_8466, 8.0);
         if (var1 != null && this.uC - this.uU < 200) {
            this.P(var1);
         } else {
            if (this.uC - this.uU > 60) {
               this.p("lost the ender chest");
            }
         }
      }
   }

   private void p(String var1) {
      this.c();
      if (mc.field_1724.field_7512 instanceof class_1707) {
         mc.field_1724.method_7346();
      }

      q8.G(this.getName(), "Ender chest stash failed: {}", var1);
      this.U("your inventory is full (" + var1 + ")");
   }

   private boolean H(int var1) {
      class_1661 var2 = mc.field_1724.method_31548();
      if (var2.method_67532() != var1) {
         bv.i(this);
         var2.method_61496(var1);
         this.Ks = var1;
         this.uX = this.uC;
         return false;
      }

      if (this.Ks != var1) {
         this.Ks = var1;
         this.uX = this.uC;
      }

      return this.uC - this.uX >= 3;
   }

   private void X() {
      this.ue = 0;
      this.ur = this.uC;
      this.r = -1;
      this.uA = this.e(class_1802.field_23141);
      q8.G(this.getName(), "Selling {} anchors with /sell", this.uA);
      this.V(sz.z.Q, "selling");
   }

   private void v(int var1) {
      this.ue = var1;
      this.ur = this.uC;
   }

   private void r() {
      if (this.uC - this.ur > 200) {
         this.R("the Sell menu took too long (step " + (this.ue + 1) + ")");
      } else {
         switch (this.ue) {
            case 0:
               if (!mc.field_1724.method_24828() || mc.field_1724.field_7512 != mc.field_1724.field_7498 || cm.j() instanceof class_465) {
                  return;
               }

               if (this.e(class_1802.field_23141) == 0) {
                  this.b();
                  return;
               }

               mc.field_1724.field_3944.method_45730("sell");
               this.v(1);
               break;
            case 1:
               class_1707 var12 = this.l();
               if (var12 == null) {
                  return;
               }

               int var13 = var12.method_17388() * 9;
               this.r = -1;

               for (int var14 = var13 - 9; var14 < var13; var14++) {
                  class_1799 var4 = var12.method_7611(var14).method_7677();
                  if (!var4.method_7960()
                     && (var4.method_57826(class_9334.field_49631) || var4.method_57826(class_9334.field_50239))
                     && var4.method_7947() == 1) {
                     String var5 = class_7923.field_41178.method_10221(var4.method_7909()).toString();
                     class_9290 var6 = (class_9290)var4.method_58694(class_9334.field_49632);
                     ArrayList var7 = new ArrayList();
                     if (var6 != null) {
                        for (class_2561 var9 : var6.comp_2400()) {
                           var7.add(var9.getString());
                        }
                     }

                     if (ii.j(var5, var4.method_7964().getString(), var7, var14, var13)) {
                        if (this.r >= 0) {
                           this.R("the Sell menu has more than one Sell button");
                           return;
                        }

                        this.r = var14;
                     }
                  }
               }

               if (this.r < 0) {
                  this.R("the Sell menu has no Sell button in its bottom row");
                  return;
               }

               for (int var15 = 0; var15 < var13 - 9; var15++) {
                  if (!var12.method_7611(var15).method_7677().method_7960()) {
                     this.R("the Sell menu already holds items");
                     return;
                  }
               }

               this.v(2);
               break;
            case 2:
               class_1707 var11 = this.l();
               if (var11 == null) {
                  this.R("the Sell menu closed");
                  return;
               }

               if (mc.field_1724.method_18798().method_37268() > 1.0E-4 || !var11.method_34255().method_7960()) {
                  return;
               }

               int var2 = var11.method_17388() * 9;

               for (int var3 = var2; var3 < var11.field_7761.size(); var3++) {
                  if (((class_1735)var11.field_7761.get(var3)).method_7677().method_31574(class_1802.field_23141)) {
                     if (this.uC - this.uc >= 4) {
                        this.uc = this.uC;
                        mc.field_1761.method_2906(var11.field_7763, var3, 0, class_1713.field_7794, mc.field_1724);
                     }

                     return;
                  }
               }

               if (this.j(var11) == 0) {
                  this.R("no anchors went into the Sell menu");
                  return;
               }

               this.v(3);
               break;
            case 3:
               class_1707 var10 = this.l();
               if (var10 == null) {
                  this.R("the Sell menu closed before selling");
                  return;
               }

               if (this.uC - this.uc < 4 || !var10.method_34255().method_7960()) {
                  return;
               }

               this.uc = this.uC;
               mc.field_1761.method_2906(var10.field_7763, this.r, 0, class_1713.field_7790, mc.field_1724);
               this.v(4);
               break;
            default:
               class_1707 var1 = this.l();
               if (var1 != null && this.j(var1) > 0) {
                  if (this.uC - this.ur > 100) {
                     this.R("the server did not take the anchors");
                  }

                  return;
               }

               if (var1 != null) {
                  mc.field_1724.method_7346();
               }

               this.b();
         }
      }
   }

   private int j(class_1707 var1) {
      int var2 = 0;
      int var3 = var1.method_17388() * 9;

      for (int var4 = 0; var4 < var3 - 9; var4++) {
         class_1799 var5 = var1.method_7611(var4).method_7677();
         if (var5.method_31574(class_1802.field_23141)) {
            var2 += var5.method_7947();
         }
      }

      return var2;
   }

   private class_1707 l() {
      return cm.j() instanceof class_476 var1
            && mc.field_1724.field_7512 instanceof class_1707 var2
            && var1.method_17577() == var2
            && com.larpclient.uk.J(var1.method_25440().getString(), "Sell")
         ? var2
         : null;
   }

   private void b() {
      int var1 = Math.max(0, this.uA - this.e(class_1802.field_23141));
      this.uh += var1;
      q8.G(this.getName(), "Sold {} anchors ({} this run)", var1, this.uh);
      if (var1 > 0) {
         this.r(sz.w.q, "Sold " + var1 + " anchor" + (var1 == 1 ? "" : "s") + " (" + this.uh + " this run)");
      }

      int var2 = this.uZ.getValue().intValue();
      if (var2 > 0 && this.ux >= var2) {
         this.h("collected " + this.ux + " anchors");
      } else {
         this.V(sz.z.X, "sold");
      }
   }

   private void R(String var1) {
      if (mc.field_1724.field_7512 instanceof class_1707) {
         mc.field_1724.method_7346();
      }

      this.uM = System.currentTimeMillis() + 300000L;
      q8.G(this.getName(), "Selling stopped: {}; trying again in five minutes", var1);
      this.r(sz.w.t, "Could not sell anchors: " + var1);
      int var2 = this.uZ.getValue().intValue();
      if (var2 > 0 && this.ux >= var2) {
         this.h("collected " + this.ux + " anchors");
      } else {
         this.V(sz.z.X, "sell failed");
      }
   }

   private boolean B() {
      class_746 var1 = mc.field_1724;
      switch (this.U) {
         case y:
            if (this.uC - this.W > 40 || !this.M()) {
               this.U = sz.h.Z;
               this.Z = this.uC + 60;
               return false;
            } else if (!cr.a(this)) {
               return true;
            } else {
               float[] var4 = this.K.V(var1.method_36454(), var1.method_36455(), this.KX, this.R, false);
               t9.O(this, t9.v.e, var4[0], var4[1], 1.0);
               int var3 = Math.abs(class_3532.method_15393(this.KX - var1.method_36454())) <= 0.75F && Math.abs(this.R - var1.method_36455()) <= 0.75F ? 1 : 0;
               if (var3 != 0 && var1.method_18798().method_37268() < 1.0E-5) {
                  this.x();
               }

               return true;
            }
         case H:
            int var2 = var1.method_73189().method_1022(this.uu) > 4.0 && var1.method_24828() ? 1 : 0;
            if (var2 != 0 || this.uC - this.W > 100) {
               q8.G(this.getName(), var2 != 0 ? "Pearl landed at {}" : "Pearl throw from {} did not arrive", var1.method_24515().method_23854());
               this.U = sz.h.Z;
               this.Z = this.uC + 20;
               this.uP.i();
            }

            return true;
         default:
            if (this.uC < this.Z || !this.M() || !var1.method_24828()) {
               return false;
            } else if (!this.V()) {
               this.Z = this.uC + 100;
               return false;
            } else {
               this.c();
               this.U = sz.h.y;
               this.W = this.uC;
               return true;
            }
      }
   }

   private boolean M() {
      class_746 var1 = mc.field_1724;
      int var2 = tn.h(var0 -> var0.method_31574(class_1802.field_8634));
      return var2 >= 0
         && !var1.method_7357().method_7904(var1.method_31548().method_5438(var2))
         && var1.method_6096() >= 12
         && var1.method_6032() + var1.method_6067() >= 12.0F;
   }

   private boolean V() {
      cm var1 = this.P(this.L);
      ig.b var2 = ig.v(var1, this.L.method_10263(), this.L.method_10264(), this.L.method_10260(), this.u());
      ArrayList var3 = new ArrayList();

      for (ig.z var5 : var2.exposed()) {
         if (var3.size() >= 3) {
            break;
         }

         var3.add(new class_243(var5.x() + 0.5, var5.y(), var5.z() + 0.5));
      }

      if (var3.isEmpty()) {
         for (byte var15 = -4; var15 <= 4 && var3.size() < 3; var15 += 4) {
            for (byte var17 = -4; var17 <= 4 && var3.size() < 3; var17 += 4) {
               int var6 = this.L.method_10263() + var15;
               int var7 = this.L.method_10260() + var17;
               int var8 = mc.field_1687.method_8624(class_2903.field_13203, var6, var7);
               if (var1.floor(var6, var8 - 1, var7) && var1.open(var6, var8, var7) && var1.open(var6, var8 + 1, var7)) {
                  var3.add(new class_243(var6 + 0.5, var8, var7 + 0.5));
               }
            }
         }
      }

      ym var16 = new ym(mc.field_1687, mc.field_1724);
      class_243 var18 = mc.field_1724.method_33571();

      for (class_243 var20 : var3) {
         float var21 = (float)(Math.toDegrees(Math.atan2(var20.field_1350 - var18.field_1350, var20.field_1352 - var18.field_1352)) - 90.0);
         class_243 var9 = null;
         float var10 = 0.0F;
         int var11 = Integer.MAX_VALUE;

         for (int var12 = -55; var12 <= 45; var12++) {
            sf.c var13 = sf.H(sf.y.thrown(var18, class_243.method_1030(var12, var21), class_243.field_1353), var16);
            class_243 var14 = var13.N();
            if (var14 != null && !(var14.method_1022(var20) > 2.0) && var13.m() < var11) {
               var9 = var14;
               var10 = var12;
               var11 = var13.m();
            }
         }

         if (var9 != null) {
            this.KX = var21;
            this.R = var10;
            this.k = var9;
            q8.G(this.getName(), "Pearl throw toward {} (pitch {}, {} ticks of flight)", class_2338.method_49638(var9).method_23854(), var10, var11);
            return true;
         }
      }

      q8.v(this.getName(), "No pearl throw lands near the anchor at {}", this.L.method_23854());
      return false;
   }

   private void x() {
      class_746 var1 = mc.field_1724;
      this.uu = var1.method_73189();
      cr.y(this, () -> {
         int var2 = tn.h(var0 -> var0.method_31574(class_1802.field_8634));
         if (var2 >= 0) {
            class_1661 var3 = var1.method_31548();
            int var4 = var3.method_67532();
            var3.method_61496(var2);

            try {
               if (mc.field_1761.method_2919(var1, class_1268.field_5808).method_23665()) {
                  w6.H(var1, class_1268.field_5808);
                  this.U = sz.h.H;
                  this.W = this.uC;
               }
            } finally {
               if (var3.method_67532() == var2) {
                  var3.method_61496(var4);
               }
            }
         }
      }, () -> mc.field_1724 == var1 && this.isRuntimeActive() && this.U == sz.h.y);
   }

   private void q() {
      this.M.clear();
      this.u4 = Double.MAX_VALUE;
      double var1 = this.uE.getValue();
      MCFriendModule var3 = LarpClient.b().G().b(MCFriendModule.class);
      String var4 = null;

      for (class_742 var6 : mc.field_1687.method_18456()) {
         if (var6 != mc.field_1724 && !var6.method_7325()) {
            String var7 = var6.method_7334().name();
            if (!this.uT.getValue() || var3 == null || !var3.M(var7)) {
               double var8 = var6.method_5739(mc.field_1724);
               if (!(var8 > var1)) {
                  this.M.add(new double[]{var6.method_23317(), var6.method_23321()});
                  if (var8 < this.u4) {
                     this.u4 = var8;
                     var4 = var7;
                  }
               }
            }
         }
      }

      if (var4 != null) {
         this.uY = var4;
         if (!this.u5) {
            this.u5 = (boolean)1;
            q8.G(this.getName(), "{} is {} blocks away; leaving by the next /rtp", var4, (int)this.u4);
            this.r(sz.w.t, "Player " + var4 + " is " + (int)Math.round(this.u4) + " blocks away, leaving");
         }
      }
   }

   private void U(long var1) {
      float var3 = mc.field_1724.method_6032();
      if (var3 < this.O && mc.field_1724.field_6235 > 0) {
         for (class_742 var5 : mc.field_1687.method_18456()) {
            if (var5 != mc.field_1724 && var5.method_5739(mc.field_1724) < 6.0F) {
               this.ua = var1;
               break;
            }
         }
      }

      this.O = var3;
   }

   private void T(long var1) {
      bv.i(this);
      if (mc.field_1724.field_7512 instanceof class_1707) {
         mc.field_1724.method_7346();
      }

      if (this.uN != sz.z.w && this.uN != sz.z.M && this.uN != sz.z.j) {
         this.V(sz.z.w, "player " + this.uY);
      }

      if (this.Kp.B(var1)) {
         this.e.D(this.uI);
         if (this.Kp.r(var1)) {
            this.U("/rtp did not answer 5 times in a row");
            return;
         }
      }

      double var3 = this.M.isEmpty() ? 64.0 : this.u4;
      ys.k var5 = ys.S(true, this.Kp.D(var1), this.Kp.c(), this.uF, var3, this.Kp.n(var1), this.ut.getValue(), this.ua < 0L ? -1L : var1 - this.ua);
      switch (var5) {
         case P:
            this.O(var1);
            break;
         case y:
            this.y("a player came within " + (int)Math.round(var3) + " blocks during the /rtp cooldown");
            break;
         case x:
            this.m("Player nearby, standing still for the teleport");
            break;
         case I:
            this.Q();
            this.m("Player nearby, running (/rtp " + (this.Kp.c() ? "pending" : "in " + O(this.Kp.n(var1))) + ")");
         case A:
      }
   }

   private void Q() {
      this.R(24.0, 40);
   }

   private void R(double var1, int var3) {
      if (!this.M.isEmpty() || this.o != null) {
         if (this.o == null || this.uC - this.S > var3 || !this.uP.G()) {
            this.S = this.uC;
            if (this.M.isEmpty()) {
               return;
            }

            this.o = ys.q(mc.field_1724.method_23317(), mc.field_1724.method_23321(), this.M, var1);
            class_2338 var4 = this.Z();
            g8.l var5 = g8.l.around(
               var4.method_10263(),
               var4.method_10264(),
               var4.method_10260(),
               var4.method_10263(),
               var4.method_10264(),
               var4.method_10260(),
               (int)var1 + 8,
               10,
               10
            );
            g8.t var6 = g8.O(
               this.P((class_2338)null),
               var4.method_10263(),
               var4.method_10264(),
               var4.method_10260(),
               g8.i.g(List.copyOf(this.M), var1),
               var5,
               g8.b.walking(8000).withPartial(true)
            );
            if (var6.found() && !var6.steps().isEmpty()) {
               this.uP.S(var6.steps(), var6.complete());
            }
         }

         e3.l var7 = this.uP.d(this.c(), this::G, true);
         this.E();
         if (var7 == e3.l.L || var7 == e3.l.s) {
            this.uP.i();
         }
      }
   }

   private boolean x(long var1) {
      class_746 var3 = mc.field_1724;
      ArrayList var4 = new ArrayList();
      byte var5 = 0;
      class_1309 var6 = null;
      double var7 = Math.min(3.0, var3.method_55755());

      for (class_1309 var10 : mc.field_1687
         .method_8390(class_1309.class, var3.method_5829().method_1014(10.0), var0 -> var0 instanceof class_1569 && var0.method_5805())) {
         byte var11 = var10 instanceof class_1548;
         int var12 = !(var10 instanceof class_1603) && !(var10 instanceof class_3745) ? 0 : 1;
         double var13 = var10.method_5739(var3);
         if (this.u2.getValue() && ys.F((boolean)var11, (boolean)var12, var3.method_6032())) {
            if (ys.y((boolean)var11, (boolean)var12, cr.M(var10), var7, var3.method_6032())
               && var3.method_6057(var10)
               && (var6 == null || var13 < var6.method_5739(var3))) {
               var6 = var10;
            }
         } else if (ys.t(var13, (boolean)var11, var3.method_6057(var10), var3.method_6032(), this.uV)) {
            var4.add(var10);
            var5 |= var11;
         }
      }

      if (var4.isEmpty() && var6 != null) {
         this.X(var6, var7);
         return true;
      }

      if (this.ud != null) {
         q8.G(this.getName(), "Done fighting; carrying on");
         this.ud = null;
      }

      if (var4.isEmpty()) {
         if (this.uV) {
            this.uV = (boolean)0;
            this.uP.i();
            this.o = null;
            q8.G(this.getName(), "Away from the mobs; carrying on");
         }

         return false;
      } else {
         if (!this.uV) {
            this.uV = (boolean)1;
            if (this.L != null && ++this.ug >= 3) {
               q8.G(this.getName(), "Ran from mobs three times on the way to {}; leaving it", this.L.method_23854());
               this.e("mobs keep guarding it");
            }

            this.uP.i();
            this.o = null;
            class_1309 var15 = var4.stream().min(Comparator.comparingDouble(var1x -> var1x.method_5739(var3))).orElseThrow();
            q8.G(
               this.getName(),
               "{} {} blocks away at {} health; running",
               var15.method_5864().method_5897().getString(),
               String.format(Locale.ROOT, "%.1f", var15.method_5739(var3)),
               (int)var3.method_6032()
            );
         }

         bv.i(this);
         fk.N(this);
         this.T.K();
         if (var3.field_7512 instanceof class_1707) {
            var3.method_7346();
         }

         if (!this.H && this.Kp.D(var1)) {
            int var16 = this.u7;
            this.O(var1);
            if (this.u7 > var16) {
               q8.G(this.getName(), "{} close; left by /rtp", var5 != 0 ? "Creeper" : "Hostile mob");
            }
         }

         this.M.clear();

         for (class_1309 var18 : var4) {
            this.M.add(new double[]{var18.method_23317(), var18.method_23321()});
         }

         this.R(var5 != 0 ? 18.0 : 12.0, 20);
         if (!this.uP.G()) {
            this.n();
         }

         this.m(var5 != 0 ? "Running from a creeper" : "Running from hostile mobs");
         return true;
      }
   }

   private void X(class_1309 var1, double var2) {
      class_746 var4 = mc.field_1724;
      if (this.ud != var1) {
         this.ud = var1;
         q8.G(
            this.getName(),
            "Fighting a {} {} blocks away at {} health",
            var1.method_5864().method_5897().getString(),
            String.format(Locale.ROOT, "%.1f", var1.method_5739(var4)),
            (int)var4.method_6032()
         );
      }

      bv.i(this);
      fk.N(this);
      this.T.K();
      this.uP.i();
      int var5 = this.F();
      if (var5 >= 0 && var4.method_31548().method_67532() != var5) {
         var4.method_31548().method_61496(var5);
      }

      if (cr.a(this)) {
         class_243 var6 = var1.method_5829().method_1005().method_1031(0.0, var1.method_17682() * 0.2, 0.0);
         float[] var7 = t9.X(var4.method_33571(), var6);
         float[] var8 = this.K.V(var4.method_36454(), var4.method_36455(), var7[0], var7[1], false);
         t9.O(this, t9.v.e, var8[0], var8[1], 1.0);
      }

      class_1309 var9 = var1;
      cr.F(this, var9, var2, true, () -> this.um && var9.method_5805() && mc.field_1724 == var4);
      this.m("Fighting a " + var1.method_5864().method_5897().getString());
   }

   private int F() {
      int var1 = -1;
      double var2 = -1.0;

      for (int var4 = 0; var4 < 9; var4++) {
         class_1799 var5 = mc.field_1724.method_31548().method_5438(var4);
         boolean var6 = var5.method_31573(class_3489.field_42611);
         boolean var7 = var5.method_31573(class_3489.field_42612);
         if (var6 || var7) {
            double var8 = (var6 ? 10000 : 0) + var5.method_7936();
            if (var8 > var2) {
               var2 = var8;
               var1 = var4;
            }
         }
      }

      return var1;
   }

   private void n() {
      double[] var1 = ys.q(mc.field_1724.method_23317(), mc.field_1724.method_23321(), this.M, 3.0);
      if (var1 != null) {
         cm var2 = this.P((class_2338)null);
         class_2338 var3 = this.Z();
         int var4 = (int)Math.floor(var1[0]);
         int var5 = (int)Math.floor(var1[1]);
         byte var6 = 0;

         for (int var7 = 1; var7 >= -3 && var6 == 0; var7--) {
            int var8 = var3.method_10264() + var7;
            var6 = (byte)(var2.open(var4, var8, var5)
                  && var2.open(var4, var8 + 1, var5)
                  && var2.floor(var4, var8 - 1, var5)
                  && var2.penalty(var4, var8, var5) < 8.0
               ? 1
               : 0);
         }

         if (var6 != 0) {
            this.b = new class_243(var1[0] - mc.field_1724.method_23317(), 0.0, var1[1] - mc.field_1724.method_23321());
            this.uy = (boolean)(mc.field_1724.method_7344().method_7586() > 6 ? 1 : 0);
         }
      }
   }

   private void y(String var1) {
      q8.G(this.getName(), "Logging out: {}", var1);
      String var2 = "Larp Anchor Hunter: " + var1 + ". " + this.ux + " anchors collected.";
      class_634 var3 = mc.method_1562();
      this.U("logged out, " + var1);
      if (var3 != null && !mc.method_1542()) {
         var3.method_48296().method_10747(class_2561.method_43470(var2));
      }
   }

   private static boolean H(class_1799 var0) {
      return var0.method_58694(class_9334.field_50075) != null
         && !var0.method_31574(class_1802.field_8463)
         && !var0.method_31574(class_1802.field_8367)
         && !var0.method_31574(class_1802.field_8511)
         && !var0.method_31574(class_1802.field_8680)
         && !var0.method_31574(class_1802.field_8635)
         && !var0.method_31574(class_1802.field_8323)
         && !var0.method_31574(class_1802.field_8233)
         && !var0.method_31574(class_1802.field_8766);
   }

   private int e(class_1792 var1) {
      class_1661 var2 = mc.field_1724.method_31548();
      int var3 = 0;

      for (int var4 = 0; var4 < 36; var4++) {
         class_1799 var5 = var2.method_5438(var4);
         if (var5.method_31574(var1)) {
            var3 += var5.method_7947();
         }
      }

      return var3;
   }

   private boolean Q() {
      return this.b(new class_1799(class_1802.field_23141));
   }

   private boolean b(class_1799 var1) {
      class_1661 var2 = mc.field_1724.method_31548();

      for (int var3 = 0; var3 < 36; var3++) {
         class_1799 var4 = var2.method_5438(var3);
         if (var4.method_7960() || class_1799.method_31577(var4, var1) && var4.method_7947() < var4.method_7914()) {
            return true;
         }
      }

      return false;
   }

   private int b() {
      return this.isInGame() ? this.x(class_1802.field_8634) : 0;
   }

   private int x(class_1792 var1) {
      int var2 = 0;

      for (int var3 = 0; var3 < 9; var3++) {
         class_1799 var4 = mc.field_1724.method_31548().method_5438(var3);
         if (var4.method_31574(var1)) {
            var2 += var4.method_7947();
         }
      }

      return var2;
   }

   private boolean y() {
      return tn.h(var0 -> var0.method_31574(class_1802.field_8466)) >= 0 && this.i() >= 0;
   }

   static boolean x(class_1799 var0) {
      for (class_6880 var2 : var0.method_58657().method_57534()) {
         if (var2.method_40225(class_1893.field_9099)) {
            return true;
         }
      }

      return false;
   }

   private int i() {
      if (!this.isInGame()) {
         return -1;
      }

      class_2680 var1 = class_2246.field_10443.method_9564();

      for (int var2 = 0; var2 < 9; var2++) {
         class_1799 var3 = mc.field_1724.method_31548().method_5438(var2);
         if (x(var3) && var3.method_7951(var1) && tn.p(var3) > 5) {
            return var2;
         }
      }

      return -1;
   }

   private int T(class_2680 var1) {
      if (!this.isInGame()) {
         return -1;
      }

      int var2 = -1;
      float var3 = -1.0F;

      for (int var4 = 0; var4 < 9; var4++) {
         class_1799 var5 = mc.field_1724.method_31548().method_5438(var4);
         if (var5.method_7951(var1) && tn.p(var5) > 20) {
            float var6 = var5.method_7924(var1) + (var5.method_57826(class_9334.field_49631) ? 0 : 1000);
            if (var6 > var3) {
               var3 = var6;
               var2 = var4;
            }
         }
      }

      return var2;
   }

   private int G(class_2680 var1) {
      class_1661 var2 = mc.field_1724.method_31548();
      int var3 = var2.method_67532();
      float var4 = -1.0F;

      for (int var5 = 0; var5 < 9; var5++) {
         class_1799 var6 = var2.method_5438(var5);
         if (!var6.method_7963() || tn.p(var6) > 5) {
            float var7 = var6.method_7924(var1)
               + (var6.method_7951(var1) ? 100 : 0)
               + (var6.method_57826(class_9334.field_49631) ? 0 : 1000)
               + (var5 == var2.method_67532() ? 0.5F : 0.0F);
            if (var7 > var4) {
               var4 = var7;
               var3 = var5;
            }
         }
      }

      return var3;
   }

   private boolean i() {
      int var1 = this.T(class_2246.field_23152.method_9564());
      return var1 >= 0 && mc.field_1724.method_31548().method_5438(var1).method_57826(class_9334.field_49631) && !Boolean.FALSE.equals(this.u);
   }

   private void k() {
      if (this.u == null && this.ub.isEmpty() && this.i()) {
         class_3965 var1 = bv.L(this.L, this.c());
         if (var1 != null) {
            class_2351 var2 = var1.method_17780().method_10166();

            for (int var3 = -1; var3 <= 1; var3++) {
               for (int var4 = -1; var4 <= 1; var4++) {
                  if (var3 != 0 || var4 != 0) {
                     class_2338 var5 = var2 == class_2351.field_11048
                        ? this.L.method_10069(0, var3, var4)
                        : (var2 == class_2351.field_11052 ? this.L.method_10069(var3, 0, var4) : this.L.method_10069(var3, var4, 0));
                     if (bv.P(var5)) {
                        this.ub.add(var5.method_10062());
                     }
                  }
               }
            }
         }
      }
   }

   private void T() {
      if (!this.ub.isEmpty() && this.uC - this.uU >= 10) {
         long var1 = this.ub.stream().filter(var0 -> mc.field_1687.method_8320(var0).method_26215()).count();
         this.u = var1 * 4L >= this.ub.size() * 3L;
         q8.G(
            this.getName(),
            "Tool check: {} of {} blocks around the anchor broke, so the renamed pickaxe {}",
            var1,
            this.ub.size(),
            this.u ? "cuts 3x3" : "breaks one block at a time"
         );
         this.r(this.u ? "your renamed pickaxe cuts 3x3, so it keeps away from lava and water." : "your renamed pickaxe breaks one block at a time here.");
         this.ub.clear();
      }
   }

   private boolean L(class_2338 var1) {
      for (int var2 = -1; var2 <= 1; var2++) {
         for (int var3 = -1; var3 <= 1; var3++) {
            for (int var4 = -1; var4 <= 1; var4++) {
               if (!mc.field_1687.method_8316(var1.method_10069(var2, var3, var4)).method_15769()) {
                  return true;
               }
            }
         }
      }

      return false;
   }

   private double c() {
      return Math.min(4.5, mc.field_1724.method_55754());
   }

   @Override
   protected void aimAt(class_243 var1, float var2) {
      if (this.isEnabled()) {
         if (cr.a(this)) {
            float[] var3 = t9.X(mc.field_1724.method_33571(), var1);
            class_2338 var4 = this.uP.y() != null ? this.uP.y() : (this.uN == sz.z.o ? this.L : null);
            int var5 = var4 != null && bv.L(var4, this.c()) != null ? 1 : 0;
            float[] var6 = this.K.V(mc.field_1724.method_36454(), mc.field_1724.method_36455(), var3[0], var3[1], (boolean)var5);
            t9.O(this, t9.v.e, var6[0], var6[1], 1.0);
         }
      }
   }

   @Override
   public void aimPlacement(float var1, float var2) {
      if (this.isEnabled()) {
         float[] var3 = this.K.V(mc.field_1724.method_36454(), mc.field_1724.method_36455(), var1, var2, false);
         t9.O(this, t9.v.e, var3[0], var3[1], 1.0);
      }
   }

   private void D(class_243 var1) {
      if (cr.a(this)) {
         float[] var2 = t9.X(mc.field_1724.method_33571(), var1);
         float[] var3 = this.K.V(mc.field_1724.method_36454(), mc.field_1724.method_36455(), var2[0], var2[1], false);
         t9.O(this, t9.v.e, var3[0], var3[1], 1.0);
      }
   }

   public static class_10185 e() {
      if (E != null && E.isEnabled()) {
         sz var0 = E;
         class_310 var1 = class_310.method_1551();
         if (var0 != null
            && var0.isRuntimeActive()
            && var0.um
            && var1.field_1724 != null
            && var1.field_1724 == var0.z
            && var1.field_1724.field_6012 == var0.uO
            && !wp.d()
            && !ff.e()
            && !(cm.j() instanceof class_465)
            && (!t9.I() || t9.k(t9.v.e))) {
            int var2 = var0.b.method_1027() > 1.0E-6 ? 1 : 0;
            if (var2 == 0 && !var0.u6 && !var0.K7) {
               return null;
            }

            class_10185 var3 = var2 != 0 ? var0.K.V(var1.field_1724.method_36454(), var0.b) : new class_10185(false, false, false, false, false, false, false);
            return new class_10185(var3.comp_3159(), var3.comp_3160(), var3.comp_3161(), var3.comp_3162(), var0.u6, var0.K7, var0.uy);
         } else {
            return null;
         }
      } else {
         return null;
      }
   }

   private void r(String var1) {
      if (mc != null && mc.field_1724 != null) {
         mc.field_1724.method_7353(class_2561.method_43470("Larp Anchor Hunter: " + var1), false);
      }
   }

   private void r(sz.w var1, String var2) {
      q8.v(this.getName(), "Notice {}: {}", var1, var2);
      if (var1 != sz.w.g) {
         long var3 = System.currentTimeMillis();
         if (var1 == sz.w.d || !var2.equals(this.B) || var3 - this.KU >= 30000L) {
            this.B = var2;
            this.KU = var3;
            String var5 = this.uR.getValue();
            if (!var5.equals("Off") || var1 == sz.w.d) {
               if (!var5.equals("Chat") && !var5.equals("Off")) {
                  wt.s var6 = switch (var1) {
                     case q -> wt.s.C;
                     case t, d -> wt.s.a;
                     default -> wt.s.R;
                  };
                  wt.T().k("Anchor Hunter", var2, var6, 5000L);
                  if (var1 == sz.w.d) {
                     this.r(var2.substring(0, 1).toLowerCase(Locale.ROOT) + var2.substring(1));
                  }
               } else {
                  this.r(var2.substring(0, 1).toLowerCase(Locale.ROOT) + var2.substring(1));
               }
            }
         }
      }
   }

   private static String U(String var0) {
      return var0.isEmpty() ? var0 : var0.substring(0, 1).toUpperCase(Locale.ROOT) + var0.substring(1);
   }

   static String B(int var0, String var1) {
      return var0 + " " + var1 + (var0 == 1 ? "" : "s");
   }

   static String O(long var0) {
      return (var0 + 999L) / 1000L + "s";
   }

   private void v() {
      if (this.isInGame()) {
         try {
            int var1 = ez.V(0.25);
            if (System.currentTimeMillis() < this.g) {
               for (class_2338 var3 : this.u1) {
                  class_12180.method_75541(new class_238(var3).method_1014(0.02), class_12179.method_75537(-14249, 2.0F, 587188311)).method_75533();
               }
            }

            if (!this.um || !this.uo.getValue() || mc.field_1687 != this.m) {
               return;
            }

            if (this.L != null) {
               class_12180.method_75541(new class_238(this.L).method_1014(0.03), class_12179.method_75537(-14249, 2.5F, 872400983)).method_75533();
            }

            List var12 = this.uP.M();
            double var13 = 0.05;
            class_243 var5 = mc.field_1724.method_73189().method_1031(0.0, var13, 0.0);

            for (g8.p var7 : var12) {
               class_243 var8 = new class_243(var7.x() + 0.5, var7.y() + var13, var7.z() + 0.5);
               class_12180.method_75546(var5, var8, ei.D(var1, 0.85), 2.0F).method_75533();
               var5 = var8;

               for (g8.v var10 : var7.digs()) {
                  class_12180.method_75541(
                     new class_238(var10.x(), var10.y(), var10.z(), var10.x() + 1, var10.y() + 1, var10.z() + 1).method_1014(0.01),
                     class_12179.method_75537(ei.D(-38037, 0.9), 1.5F, ei.D(-38037, 0.12))
                  );
               }
            }

            if (this.U == sz.h.y && this.k != null) {
               class_12180.method_75541(
                     new class_238(
                        this.k.field_1352 - 0.5,
                        this.k.field_1351,
                        this.k.field_1350 - 0.5,
                        this.k.field_1352 + 0.5,
                        this.k.field_1351 + 0.1,
                        this.k.field_1350 + 0.5
                     ),
                     class_12179.method_75537(-8530948, 2.0F, 863884284)
                  )
                  .method_75533();
            }
         } catch (RuntimeException var11) {
            q8.O(this.getName(), "Path overlay failed", var11);
         }
      }
   }

   @Override
   public tc.g hudCard() {
      if (this.isInGame() && this.u3.getValue() && this.isRuntimeActive() && this.um && mc.field_1687 == this.m) {
         long var1 = System.currentTimeMillis();
         double var3 = Math.max(1.0E-6, (var1 - this.u_) / 3600000.0);
         int var5 = this.uZ.getValue().intValue();
         String var6 = this.u5
            ? "Player " + this.uY + " nearby"
            : B(this.ux, "anchor") + (var1 - this.u_ > 60000L ? ", " + Math.round(this.ux / var3) + "/h" : "");
         String var7 = this.H ? "Singleplayer" : (this.Kp.c() ? "/rtp waiting" : (this.Kp.D(var1) ? "/rtp ready" : "/rtp in " + O(this.Kp.n(var1))));
         String var8 = this.getStatus();
         return new tc.g(
            var6,
            var5 > 0 ? this.ux + " of " + var5 : "",
            var8 != null && !var8.isBlank() ? var8 : "Starting",
            var7,
            var5 > 0 ? Math.clamp((double)this.ux / var5, 0.0, 1.0) : -1.0,
            this.u5
         );
      } else {
         return null;
      }
   }

   public sz.z O() {
      return this.uN;
   }

   public boolean L() {
      return this.um;
   }

   public int L() {
      return this.ux;
   }

   public class_2338 J() {
      return this.L;
   }

   public int v() {
      return this.uP.R();
   }

   public double s() {
      return this.uP.F();
   }

   private enum h {
      Z,
      y,
      H;
   }

   enum w {
      g,
      q,
      t,
      d;
   }

   public enum z {
      H("Getting ready"),
      V("Sending /rtp"),
      M("Waiting for the teleport"),
      j("Standing still for the teleport"),
      G("Waiting for /rtp"),
      K("Landing"),
      t("Looking for anchors"),
      X("Choosing an anchor"),
      q("Going to an anchor"),
      o("Mining an anchor"),
      n("Picking up the anchor"),
      s("Putting anchors in the ender chest"),
      Q("Selling anchors"),
      u("Protected area, leaving"),
      w("Player nearby, leaving"),
      f("Paused"),
      L("Stopped");

      final String m;

      z(String var3) {
         this.m = var3;
      }
   }
}
