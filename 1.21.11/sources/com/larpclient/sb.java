package com.larpclient;

import com.larpclient.module.Module;
import com.larpclient.module.aj;
import it.unimi.dsi.fastutil.objects.Object2IntMap.Entry;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents.EndTick;
import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents;
import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents.Game;
import net.fabricmc.fabric.api.client.rendering.v1.world.WorldRenderEvents;
import net.fabricmc.fabric.api.client.rendering.v1.world.WorldRenderEvents.DebugRender;
import net.minecraft.class_10185;
import net.minecraft.class_12179;
import net.minecraft.class_12180;
import net.minecraft.class_1268;
import net.minecraft.class_1294;
import net.minecraft.class_1297;
import net.minecraft.class_1304;
import net.minecraft.class_1309;
import net.minecraft.class_1569;
import net.minecraft.class_1661;
import net.minecraft.class_1671;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_1893;
import net.minecraft.class_1937;
import net.minecraft.class_2246;
import net.minecraft.class_2338;
import net.minecraft.class_238;
import net.minecraft.class_243;
import net.minecraft.class_2561;
import net.minecraft.class_265;
import net.minecraft.class_2680;
import net.minecraft.class_2806;
import net.minecraft.class_2818;
import net.minecraft.class_310;
import net.minecraft.class_3481;
import net.minecraft.class_3486;
import net.minecraft.class_3532;
import net.minecraft.class_3610;
import net.minecraft.class_3959;
import net.minecraft.class_3965;
import net.minecraft.class_465;
import net.minecraft.class_5321;
import net.minecraft.class_5354;
import net.minecraft.class_6880;
import net.minecraft.class_746;
import net.minecraft.class_9334;
import net.minecraft.class_239.class_240;
import net.minecraft.class_2902.class_2903;
import net.minecraft.class_3959.class_242;
import net.minecraft.class_3959.class_3960;

public final class sb extends s4 implements tc {
   private static sb az;
   private static boolean c;
   static final int a2 = 2;
   static final int aZ = 10;
   static final double n = 10.0;
   static final double a3 = 110.0;
   static final double x = 72.0;
   static final double am = 30.0;
   static final int aq = 6;
   static final int j = 4;
   static final int aF = 400;
   static final int a4 = 3600;
   static final double a_ = 1500.0;
   static final int k = 200;
   private final aj F = this.addSetting(
      new aj("Destination", "Where to fly, as X Z (for example 1200 -3400). X Y Z works too; the height is not used", "").A("X Z")
   );
   private final com.larpclient.module.lo aD = this.Z(
      "Flight Height",
      "Height to cruise at. Lower on long trips over flat land saves rockets; higher clears mountains and loads fewer chunks",
      290.0,
      180.0,
      320.0,
      10.0
   );
   private final com.larpclient.module.l3 e = this.G(
      "Auto Mending", "Land on the way and throw XP bottles from your hotbar to repair a Mending elytra long before it can break", true
   );
   private final com.larpclient.module.l3 a7 = this.G(
      "Log Off On Arrival", "Leave the server after landing, with a trip summary. Also leaves if the trip has to stop. Never in singleplayer", true
   );
   private final com.larpclient.module.l3 z = this.G("Waypoint Line", "Draw the planned flight, a beacon at the destination and the landing spot", true);
   private final com.larpclient.module.l3 ar = this.G(
      "Status Card", "Show a card at the top of the screen with distance, time left, rockets and elytra health", true
   );
   private final com.larpclient.module.li aT = this.addSetting(
      new com.larpclient.module.li("Notifications", "How the pilot tells you what it is doing", "Toast", "Toast", "Chat", "Off")
   );
   private final com.larpclient.module.l3 Q = this.G(
      "Auto Buy Rockets", "On DonutSMP, buy Flight Duration 3 rockets from /ah on the ground when you run low, within the price limit and budget", false
   );
   private final com.larpclient.module.lo X = this.Z("Rocket Stacks", "How many stacks of rockets to keep; it buys up to this many", 3.0, 1.0, 3.0, 1.0);
   private final com.larpclient.module.l3 U = this.G(
      "Auto Buy XP Bottles", "On DonutSMP, buy XP bottles from /ah on the ground to mend a Mending elytra, within the price limit and budget", false
   );
   private final com.larpclient.module.lo ay = this.Z("XP Bottle Stacks", "How many stacks of XP bottles to keep; it buys up to this many", 2.0, 1.0, 3.0, 1.0);
   private final com.larpclient.module.li u = this.addSetting(
      new com.larpclient.module.li(
         "Price Limit",
         "Most it pays per stack of 64. Cheap: rockets $35K, XP $320K. Normal: rockets $60K, XP $450K. High: rockets $150K, XP $700K",
         "Normal",
         wh.a.toArray(String[]::new)
      )
   );
   private final com.larpclient.module.li aw = this.addSetting(
      new com.larpclient.module.li("Trip Budget", "Most money it spends on one trip", "$2M", wh.x.toArray(String[]::new))
   );
   private final gi aJ = new gi(this.getName());
   private final com.larpclient.w au = new com.larpclient.w(this).n(true);
   private final gj jc = new gj();
   private final gj.d aX = new gj.d();
   private final wf.w S = new wf.w();
   private final u5 D = new u5();
   private final tn o = new tn(this);
   private final Map<Long, Integer> ae = new HashMap<>();
   private final List<class_243> ag = new ArrayList<>();
   private final List<class_243> aK = new ArrayList<>();
   private sb.w ax = sb.w.u;
   private sb.e aS = sb.e.g;
   private sb.n f = sb.n.Y;
   private double ab;
   private double aA;
   private boolean M;
   private Object v;
   private Object l;
   private Object J;
   private class_5321<class_1937> m;
   private int aR;
   private int al;
   private int a8;
   private int an = -1000;
   private int aL = -1000;
   private int a0;
   private gj.f E;
   private gj.o aC;
   private int O = -1000;
   private int aV;
   private int a6;
   private class_243 aj;
   private boolean aU;
   private boolean ao;
   private double jF = Double.MAX_VALUE;
   private int aB;
   private int V = -1000;
   private int W = -1000;
   private int av = 3;
   private int ak = -1000;
   private int K = -1000;
   private int aP;
   private int b;
   private Float g;
   private boolean aO;
   private float as;
   private float I;
   private boolean i;
   private boolean p;
   private boolean at;
   private int aI = Integer.MIN_VALUE;
   private int ac = -1;
   private class_243 a1;
   private int aM;
   private int Z;
   private int h;
   private int jM;
   private int a = -1000;
   private float aG = 20.0F;
   private int ai = -1000;
   private class_243 aN;
   private class_243 ap;
   private int R;
   private int r;
   private double d;
   private double af;
   private double C;
   private long T;
   private String B = "";
   private String N;
   private String aY;
   private long aQ;
   private int aa;
   private int H;
   private final EnumSet<wh.z> q = EnumSet.noneOf(wh.z.class);
   private String aH = "";
   private int aE = -10000;
   private int a5;
   private int Y;
   private final List<wh.z> w = new ArrayList<>();
   private long ad;
   private int L;
   private boolean A;
   static final double aW = 48.0;
   private int a9 = Integer.MIN_VALUE;
   private int ah = -1000;

   public sb() {
      super("Auto Elytra Pilot", "Fly to an X Z on an elytra: takeoff, cruise, landing, rockets and mending stops");
      az = this;
      this.F.M("Destination (X Z)");
      this.X.O(this.Q::getValue);
      this.ay.O(this.U::getValue);
      this.u.O(() -> this.Q.getValue() || this.U.getValue());
      this.aw.O(() -> this.Q.getValue() || this.U.getValue());
      if (!c) {
         c = (boolean)1;
         ClientReceiveMessageEvents.GAME.register((Game)(var0, var1) -> {
            sb var2 = az;
            if (!var1 && var2 != null && var2.aJ.x()) {
               gi.g(var0);
            }
         });
         ClientTickEvents.END_CLIENT_TICK.register((EndTick)var0 -> {
            sb var1 = az;
            if (var1 != null) {
               var1.B();
            }
         });
         WorldRenderEvents.BEFORE_DEBUG_RENDER.register((DebugRender)var0 -> {
            sb var1 = az;
            if (var1 != null) {
               var1.L();
            }
         });
      }
   }

   public static sb m() {
      return az;
   }

   @Override
   public boolean allowsScreenInteractions() {
      return mc == null || !(cm.j() instanceof class_465);
   }

   @Override
   public void disable() {
      this.N = null;
      super.disable();
   }

   public static double[] H(String var0) {
      if (var0 == null) {
         return null;
      }

      String[] var1 = var0.trim().replace(',', ' ').split("\\s+");
      if (var1.length != 2 && var1.length != 3) {
         return null;
      }

      double[] var2 = new double[var1.length];

      try {
         for (int var3 = 0; var3 < var1.length; var3++) {
            var2[var3] = Double.parseDouble(var1[var3]);
         }
      } catch (NumberFormatException var7) {
         return null;
      }

      double var8 = var2[0];
      double var5 = var2[var1.length - 1];
      return Double.isFinite(var8) && Double.isFinite(var5) && !(Math.abs(var8) > 2.9999984E7) && !(Math.abs(var5) > 2.9999984E7)
         ? new double[]{var8, var5}
         : null;
   }

   static String s(double var0, double var2) {
      return (long)Math.floor(var0) + " " + (long)Math.floor(var2);
   }

   static String N(double var0) {
      return String.format(Locale.ROOT, "%,d", Math.round(var0));
   }

   @Override
   public List<tc.k> commands() {
      return List.of(
         new tc.k("Set the destination", "Fly to where you look", this::Z),
         new tc.k("Set the destination", "Remember where you stand", this::O),
         new tc.k("Set the destination", "Clear destination", this::w),
         new tc.k("Buying on DonutSMP", "Buy rockets and XP now", this::W)
      );
   }

   private String Z() {
      if (!this.isInGame()) {
         return "Join a world first.";
      }

      if (this.isRuntimeActive()) {
         return "Turn the pilot off before changing the destination.";
      }

      class_243 var1 = mc.field_1724.method_33571();
      class_243 var2 = mc.field_1724.method_5720();
      double var3 = Math.max(64.0, mc.field_1690.method_38521() * 16.0);
      class_3965 var5 = mc.field_1687
         .method_17742(new class_3959(var1, var1.method_1019(var2.method_1021(var3)), class_3960.field_17559, class_242.field_1347, mc.field_1724));
      class_243 var6;
      String var7;
      if (var5.method_17783() == class_240.field_1332) {
         var6 = var5.method_17784();
         var7 = "the block you look at";
      } else {
         class_243 var8 = new class_243(var2.field_1352, 0.0, var2.field_1350);
         if (var8.method_1027() < 1.0E-6) {
            return "Look toward the horizon, not straight up or down.";
         }

         var6 = mc.field_1724.method_73189().method_1019(var8.method_1029().method_1021(1000.0));
         var7 = "1,000 blocks along your view (nothing loaded is in sight there)";
      }

      this.F.Y(s(var6.field_1352, var6.field_1350));
      this.g();
      double var10 = Math.hypot(var6.field_1352 - mc.field_1724.method_23317(), var6.field_1350 - mc.field_1724.method_23321());
      q8.G(this.getName(), "Destination set from the view to {} ({} blocks)", this.F.getValue(), N(var10));
      return "Destination " + this.F.getValue().replace(' ', ',').replace(",", ", ") + ", " + var7 + ", " + N(var10) + " blocks away.";
   }

   private String O() {
      if (!this.isInGame()) {
         return "Join a world first.";
      }

      if (this.isRuntimeActive()) {
         return "Turn the pilot off before changing the destination.";
      }

      this.F.Y(s(mc.field_1724.method_23317(), mc.field_1724.method_23321()));
      this.g();
      return "Destination set to where you stand: " + this.F.getValue().replace(" ", ", ") + ". Fly away and turn the pilot on to come back.";
   }

   private String W() {
      if (!this.isInGame()) {
         return "Join a world first.";
      }

      if (this.isRuntimeActive()) {
         return "The pilot buys on its own during a trip.";
      }

      if (!this.w.isEmpty()) {
         return "Already buying; move to stop it.";
      }

      if (!this.Q.getValue() && !this.U.getValue()) {
         return "Turn on Auto Buy Rockets or Auto Buy XP Bottles first.";
      }

      if (mc.field_1724.method_24828() && !(mc.field_1724.method_18798().method_37267() > 0.01)) {
         if (this.Q.getValue()) {
            this.w.add(wh.z.Z);
         }

         if (this.U.getValue()) {
            this.w.add(wh.z.S);
         }

         this.ad = 0L;
         this.L = 0;
         q8.G(this.getName(), "Buying now: {}", this.w);
         return "Buying "
            + (this.Q.getValue() ? "rockets" : "")
            + (this.w.size() == 2 ? " and " : "")
            + (this.U.getValue() ? "XP bottles" : "")
            + " on /ah, at most "
            + this.u.getValue().toLowerCase(Locale.ROOT)
            + " prices and "
            + wh.y(wh.C(this.aw.getValue()))
            + ". Moving stops it.";
      } else {
         return "Stand still on the ground first.";
      }
   }

   private void B() {
      if (!this.w.isEmpty()) {
         if (!this.isInGame() || this.isRuntimeActive()) {
            this.aJ.O("the restock was interrupted");
            this.w.clear();
         } else if (this.S()) {
            this.aJ.O("you moved");
            this.z("Stopped buying: you moved.");
         } else {
            wh.z var1 = this.w.getFirst();
            int var2 = var1 == wh.z.Z ? 1 : 0;
            if (!this.aJ.x()) {
               int var9 = var2 != 0 ? com.larpclient.w.A(mc.field_1724) : this.J();
               int var4 = (var2 != 0 ? this.b() : this.D()) - var9;
               long var5 = wh.C(this.aw.getValue());
               long var7 = var5 < 0L ? -1L : Math.max(0L, var5 - this.ad);
               if (var4 > 0 && var7 != 0L) {
                  this.aJ.u(var1, var4, wh.L(this.u.getValue(), var1), var7);
               } else {
                  this.w.removeFirst();
                  if (this.w.isEmpty()) {
                     this.z(null);
                  }
               }
            } else if (this.aJ.Z() != gi.a.D) {
               this.ad = this.ad + this.aJ.G();
               this.L = this.L + this.aJ.U();
               String var3 = var2 != 0 ? "rockets" : "XP bottles";
               if (this.aJ.U() > 0) {
                  this.b(wt.s.C, "Bought " + this.aJ.U() + " " + var3 + " for " + wh.y(this.aJ.G()));
               }

               if (this.aJ.b() == gi.a.a) {
                  this.b(wt.s.a, "Could not buy " + var3 + ": " + this.aJ.n());
               }

               this.w.removeFirst();
               if (this.w.isEmpty() || this.aJ.L()) {
                  this.z(null);
               }
            }
         }
      }
   }

   private void z(String var1) {
      this.w.clear();
      String var2 = var1 != null ? var1 : (this.L == 0 ? "Bought nothing." : "Done buying: " + this.L + " items for " + wh.y(this.ad) + ".");
      q8.G(this.getName(), "Buying now ended: {}", var2);
      this.v(var2.substring(0, 1).toLowerCase(Locale.ROOT) + var2.substring(1));
   }

   private String w() {
      if (this.isRuntimeActive()) {
         return "Turn the pilot off before clearing the destination.";
      }

      this.F.Y("");
      this.g();
      return "Destination cleared.";
   }

   private void g() {
      LarpClient var1 = LarpClient.b();
      if (var1 != null && var1.e() != null) {
         var1.e().U();
      }
   }

   @Override
   public String summary() {
      double[] var1 = H(this.F.getValue());
      if (var1 == null) {
         return this.F.getValue().isBlank()
            ? "Type a destination as X Z, or press Fly to where you look."
            : "\"" + this.F.getValue() + "\" is not a destination; type it as X Z, for example 1200 -3400.";
      }

      String var2 = "Destination: " + s(var1[0], var1[1]).replace(" ", ", ");
      if (!this.isInGame()) {
         return var2 + ".";
      }

      double var3 = Math.hypot(var1[0] - mc.field_1724.method_23317(), var1[1] - mc.field_1724.method_23321());
      return this.isRuntimeActive() && this.M ? var2 + ". " + this.p() + "." : var2 + ", " + N(var3) + " blocks away. Turn the pilot on to fly there.";
   }

   @Override
   public String detail() {
      if (!this.isInGame()) {
         return "Needs an elytra and firework rockets in your offhand or hotbar.";
      }

      class_1799 var1 = mc.field_1724.method_6118(class_1304.field_6174);
      String var2 = var1.method_31574(class_1802.field_8833)
         ? "elytra " + tn.p(var1) + " of " + var1.method_7936() + " uses" + (tn.S(var1) ? " (Mending)" : "")
         : (this.u() >= 0 ? "elytra in your hotbar, not worn yet" : "no elytra worn");
      return "Rockets " + com.larpclient.w.A(mc.field_1724) + ", XP bottles " + this.J() + ", " + var2 + ".";
   }

   @Override
   public List<tc.v> settingGroups() {
      return List.of(
         new tc.v("Destination", List.of(this.F)),
         new tc.v("While flying", List.of(this.e, this.a7)),
         new tc.v("Buying on DonutSMP", List.of(this.Q, this.X, this.U, this.ay, this.u, this.aw)),
         new tc.v("Display", List.of(this.z, this.ar, this.aT)),
         tc.v.advanced("Advanced", List.of(this.aD))
      );
   }

   @Override
   public void onEnable() {
      super.onEnable();
      this.e();
      if (this.N != null) {
         String var11 = this.N;
         this.N = null;
         this.c("it stopped earlier because " + var11 + ". Turn it on again to fly from here");
      } else {
         double[] var1 = H(this.F.getValue());
         if (var1 == null) {
            this.c(this.F.getValue().isBlank() ? "type a destination as X Z first" : "type the destination as X Z, for example 1200 -3400");
         } else {
            String var2 = tc.q(this);
            if (var2 != null) {
               this.c("turn off " + var2 + " first; one bot runs at a time");
            } else if (mc.field_1687.method_27983() == class_1937.field_25180) {
               this.c("it flies in the Overworld and the End, not the Nether");
            } else if (!this.d() && this.u() < 0) {
               this.c("wear an elytra, or put one in your hotbar");
            } else {
               int var3 = com.larpclient.w.A(mc.field_1724);
               if (var3 == 0 && !this.Q.getValue()) {
                  com.larpclient.w.t var12 = com.larpclient.w.S(mc.field_1724, 0);
                  this.c(
                     var12.starsOnly()
                        ? "your rockets have firework stars, which hurt when boosting; use plain rockets"
                        : (
                           this.B()
                              ? "put firework rockets in your offhand or hotbar; the pilot never clicks inventory slots"
                              : "you need firework rockets in your offhand or hotbar"
                        )
                  );
               } else {
                  this.ab = Math.floor(var1[0]) + 0.5;
                  this.aA = Math.floor(var1[1]) + 0.5;
                  double var4 = this.e();
                  if (var4 <= 30.0) {
                     this.c("you are already at the destination");
                  } else {
                     this.M = (boolean)1;
                     com.larpclient.w.y(this);
                     ArrayList var6 = new ArrayList();

                     for (String var8 : List.of("ElytraFly", "ElytraHelper", "ElytraMotion")) {
                        Module var9 = LarpClient.b().G().j(var8);
                        if (var9 != null && var9.isEnabled()) {
                           var6.add(var8);
                        }
                     }

                     if (!var6.isEmpty()) {
                        q8.G(this.getName(), "{} paused while the pilot flies", String.join(", ", var6));
                     }

                     this.v = mc.field_1687;
                     this.l = mc.field_1724;
                     this.J = mc.method_1562() == null ? null : mc.method_1562().method_48296();
                     this.m = mc.field_1687.method_27983();
                     this.T = System.currentTimeMillis();
                     this.af = var4;
                     this.a5 = var3;
                     this.aG = mc.field_1724.method_6032();
                     this.aN = mc.field_1724.method_73189();
                     this.o.V();
                     this.k(sb.w.u);
                     this.b(wt.s.R, "Flying to " + s(this.ab, this.aA).replace(" ", ", ") + " (" + N(var4) + " blocks)");
                     this.v(
                        "flying to "
                           + s(this.ab, this.aA).replace(" ", ", ")
                           + ", "
                           + N(var4)
                           + " blocks, at height "
                           + this.aD.getValue().intValue()
                           + " with "
                           + var3
                           + " rockets."
                     );
                     ArrayList var13 = new ArrayList();
                     double var14 = var4 / 1000.0 * 2.6 + 5.0;
                     if (var3 < var14 && !this.Q.getValue()) {
                        var13.add("about " + (int)Math.ceil(var14) + " rockets are needed and you have " + var3 + "; it lands safely when they run out");
                     }

                     if (this.B()) {
                        var13.add("rockets in your main inventory are not used, only the offhand and hotbar");
                     }

                     if (!var6.isEmpty()) {
                        var13.add(String.join(" and ", var6) + (var6.size() == 1 ? " pauses" : " pause") + " while the pilot flies and resumes after it lands");
                     }

                     class_1799 var10 = mc.field_1724.method_6118(class_1304.field_6174);
                     if (this.e.getValue()
                        && var10.method_31574(class_1802.field_8833)
                        && tn.S(var10)
                        && this.J() == 0
                        && !this.U.getValue()
                        && tn.p(var10) < i(var4) / (1 + this.v()) + this.s()) {
                        var13.add("no XP bottles in the hotbar to mend the elytra with on the way");
                     }

                     if (var10.method_31574(class_1802.field_8833) && !tn.S(var10) && this.u() < 0 && tn.p(var10) < i(var4)) {
                        var13.add("the elytra has " + tn.p(var10) + " uses left, no Mending and no spare, so it may land early");
                     }

                     if (tn.h(var0 -> var0.method_58694(class_9334.field_50075) != null) < 0) {
                        var13.add("no food in the hotbar");
                     }

                     if (this.Q.getValue() || this.U.getValue()) {
                        var13.add(
                           "it may buy "
                              + (this.Q.getValue() ? "rockets" : "")
                              + (this.Q.getValue() && this.U.getValue() ? " and " : "")
                              + (this.U.getValue() ? "XP bottles" : "")
                              + " from /ah on the ground, at most "
                              + this.u.getValue().toLowerCase(Locale.ROOT)
                              + " prices and "
                              + wh.y(wh.C(this.aw.getValue()))
                              + " this trip"
                        );
                     }

                     if (!var13.isEmpty()) {
                        this.v("heads up: " + String.join("; ", var13) + ".");
                     }

                     q8.G(
                        this.getName(),
                        "Started: to {} from {}, {} blocks, height {}, {} rockets, elytra {} uses, {}",
                        s(this.ab, this.aA),
                        mc.field_1724.method_24515().method_23854(),
                        N(var4),
                        this.aD.getValue().intValue(),
                        var3,
                        this.l(),
                        this.m
                     );
                  }
               }
            }
         }
      }
   }

   @Override
   public void onDisable() {
      if (this.isEnabled() && this.M) {
         this.N = "the world, the player or the connection changed mid-flight";
         q8.G(this.getName(), "Interrupted in {} after {} blocks: {}", this.ax, N(this.d), this.N);
      }

      super.onDisable();
      this.o.E();
      this.au.n();
      this.e();
      if (this.aY != null) {
         this.m(this.aY);
         this.aY = null;
      }
   }

   private void e() {
      com.larpclient.w.l(this);
      this.aJ.O("the trip ended");
      this.aQ = 0L;
      this.aa = this.H = 0;
      this.q.clear();
      this.M = (boolean)0;
      this.v = this.l = this.J = null;
      this.m = null;
      this.ax = sb.w.u;
      this.aS = sb.e.g;
      this.f = sb.n.Y;
      this.aR = this.al = this.a8 = 0;
      this.an = this.aL = this.O = -1000;
      this.a0 = 0;
      this.E = null;
      this.aC = null;
      this.aV = this.a6 = 0;
      this.aj = null;
      this.aU = this.ao = (boolean)0;
      this.jF = Double.MAX_VALUE;
      this.aB = 0;
      this.V = this.W = this.ak = -1000;
      this.av = 3;
      this.K = -1000;
      this.aP = this.b = 0;
      this.g = null;
      this.aO = (boolean)1;
      this.i = (boolean)0;
      this.p = this.at = (boolean)0;
      this.aI = Integer.MIN_VALUE;
      this.ac = -1;
      this.a1 = null;
      this.aM = this.Z = this.h = this.jM = 0;
      this.a = -1000;
      this.ai = -1000;
      this.aN = this.ap = null;
      this.R = 0;
      this.r = 0;
      this.d = this.af = this.C = 0.0;
      this.B = "";
      this.ag.clear();
      this.aK.clear();
      this.ae.clear();
      this.D.k();
      this.a5 = this.Y = 0;
      this.a9 = Integer.MIN_VALUE;
      this.ah = -1000;
   }

   private void c(String var1) {
      q8.G(this.getName(), "Not started: {}", var1);
      this.v("not started, " + var1 + ".");
      wt.T().U("Auto Elytra Pilot", M(var1), wt.s.a);
      this.m("Not started: " + var1);
      this.aY = this.getStatus();
      this.disable();
   }

   @Override
   public void onTick() {
      this.au.H();
      this.p = this.at = (boolean)0;
      this.i = (boolean)0;
      boolean var2 = false;
      byte var1 = this.M;
      int var10000 = Integer.signum(var1);
      if ((var10000 | -var10000) >>> 31 != 0) {
         if (mc.field_1687 == this.v && mc.field_1724 == this.l) {
            if (mc.field_1687.method_27983() != this.m) {
               this.C(false, "you changed dimension");
            } else {
               int var9 = mc.field_1724.method_5805();
               boolean var4 = false;
               byte var3 = (byte)var9;
               var9 = Integer.signum(var3);
               if ((var9 | -var9) >>> 31 == 0) {
                  this.C(false, "you died");
               } else {
                  byte var6 = 1;
                  int var5 = this.aR;
                  this.aR = var5 + var6;
                  this.aI = mc.field_1724.field_6012;
                  this.R();
                  if (wp.d()) {
                     this.m("Paused while Freecam is on");
                  } else if (this.S()) {
                     this.D.k();
                     this.m("Paused while you steer");
                  } else {
                     if (!this.k() && !this.U()) {
                        switch (this.ax) {
                           case u:
                              this.s();
                              break;
                           case K:
                              this.Y();
                              break;
                           case Q:
                              this.V();
                              break;
                           case r:
                              this.X();
                              break;
                           case X:
                              this.d();
                              break;
                           case T:
                              this.k();
                              break;
                           case y:
                              this.q();
                              break;
                           case A:
                              this.K();
                              break;
                           case z:
                              this.G();
                        }
                     }

                     if (this.isEnabled()) {
                        boolean var8 = false;
                        byte var7 = this.M;
                        var9 = Integer.signum(var7);
                        if (((var9 | -var9) >>> 31 ^ 1) == 0) {
                           this.j();
                        }
                     }
                  }
               }
            }
         } else {
            this.C(false, "the world or the player changed");
         }
      }
   }

   private boolean S() {
      return mc.method_1569()
         && cm.j() == null
         && (
            com.larpclient.j.W(mc.field_1690.field_1894)
               || com.larpclient.j.W(mc.field_1690.field_1881)
               || com.larpclient.j.W(mc.field_1690.field_1913)
               || com.larpclient.j.W(mc.field_1690.field_1849)
               || com.larpclient.j.W(mc.field_1690.field_1903)
         );
   }

   private void k(sb.w var1) {
      if (var1 == sb.w.T && this.ax != sb.w.T) {
         this.O = -1000;
         this.aV = 0;
      }

      if (var1 == sb.w.y && this.ax != sb.w.y) {
         this.f = sb.n.Y;
         this.q.clear();
      }

      if (var1 == sb.w.K) {
         this.aP = 0;
         this.b++;
         this.g = null;
         this.aj = null;
         this.a6 = 0;
         this.aL = -1000;
         this.a0 = 0;
         if (this.aS != sb.e.w) {
            this.aS = sb.e.g;
         }
      }

      if (var1 == sb.w.r) {
         this.b = 0;
      }

      if (var1 == sb.w.X && this.ax != sb.w.X) {
         this.jF = Double.MAX_VALUE;
         this.aB = this.aR;
      }

      if (var1 != sb.w.X) {
         this.ao = (boolean)0;
      }

      if (var1 != this.ax) {
         q8.G(this.getName(), "{} -> {} at {} ({} blocks to go, job {})", this.ax, var1, mc.field_1724.method_24515().method_23854(), N(this.e()), this.aS);
      }

      this.ax = var1;
      this.al = this.aR;
   }

   private int n() {
      return this.aR - this.al;
   }

   private void s() {
      if (mc.field_1724.method_5799()) {
         this.N();
      } else if (this.n()) {
         this.k(sb.w.Q);
      } else if (!mc.field_1724.method_24828()) {
         this.m("Waiting to land");
      } else {
         this.m("Getting ready");
         if (!this.d()) {
            if (!this.E()) {
               this.C(false, "there is no usable elytra left");
            }
         } else if (this.e() <= 30.0) {
            this.aS = sb.e.g;
            this.k(sb.w.A);
         } else if (this.n() >= 4) {
            sb.n var1 = this.F(true);
            if (var1 != sb.n.Y) {
               this.aS = sb.e.I;
               this.k(sb.w.y);
            } else if (com.larpclient.w.A(mc.field_1724) == 0) {
               this.C(false, this.U());
            } else {
               int var2 = this.l();
               if (var2 >= 0 && var2 < 30) {
                  this.C(false, "the elytra is almost broken and cannot be mended here");
               } else if (this.b >= 6) {
                  this.C(false, "it could not take off here; it needs open sky above you");
               } else {
                  this.aS = sb.e.g;
                  this.k(sb.w.K);
               }
            }
         }
      }
   }

   private void Y() {
      if (mc.field_1724.method_5799()) {
         this.N();
      } else if (this.n()) {
         this.k(sb.w.Q);
      } else if (!this.d()) {
         this.k(sb.w.u);
      } else if (!this.au.N()) {
         this.k(sb.w.u);
      } else {
         this.m("Taking off");
         if (mc.field_1724.method_24828()) {
            if (this.aR - this.K >= 14) {
               if (this.aP >= 5) {
                  this.C(false, "it could not take off here; it needs open sky above you");
               } else {
                  this.aO = this.K(16);
                  if (!this.aO) {
                     this.g = this.y();
                     if (this.g == null) {
                        this.C(false, "there is no open sky to take off from; stand somewhere open");
                        return;
                     }
                  }

                  float var1 = this.aO ? mc.field_1724.method_36454() : this.g;
                  float var2 = this.aO ? -88.0F : -35.0F;
                  this.j(var1, var2);
                  this.au.z(3);
                  if (Math.abs(class_3532.method_15393(var1 - mc.field_1724.method_36454())) <= 10.0F && Math.abs(var2 - mc.field_1724.method_36455()) <= 10.0F
                     )
                   {
                     this.au.x();
                     this.K = this.aR;
                     this.aP++;
                     q8.v(this.getName(), "Takeoff jump {} ({})", this.aP, this.aO ? "straight up" : "toward open sky at yaw " + (int)var1);
                  }
               }
            }
         } else {
            this.j(this.aO ? mc.field_1724.method_36454() : this.g, this.aO ? -88.0F : -35.0F);
            if (mc.field_1724.method_18798().field_1351 < 0.12 && this.aR - this.K >= 2) {
               this.o();
            }
         }
      }
   }

   private void o() {
      if (this.aR - this.K >= 2) {
         this.au.x();
         this.K = this.aR;
      } else {
         this.au.w();
      }
   }

   private void V() {
      if (this.y()) {
         double var1 = this.e();
         this.aX.V = this.O(var1);
         if (!this.K()) {
            double var3 = mc.field_1724.method_23318();
            boolean var5 = this.u();
            this.m("Climbing to " + (int)(Math.round(this.aX.V / 10.0) * 10L));
            if (var3 >= this.aX.V - 15.0 || !var5 && this.n() > 600) {
               this.k(var1 <= this.T() ? sb.w.X : sb.w.r);
            } else if (var5) {
               this.j(mc.field_1724.method_36454(), -80.0F);
               if (this.Z() && mc.field_1724.method_36455() < -60.0F) {
                  this.x();
               }
            } else {
               this.g(this.ab, this.aA, Double.NaN, true);
            }
         }
      }
   }

   private void X() {
      if (this.y()) {
         double var1 = this.e();
         this.aX.V = this.O(var1);
         if (!this.K()) {
            if (var1 <= this.T()) {
               this.aS = sb.e.g;
               this.aj = null;
               this.a6 = 0;
               this.k(sb.w.X);
            } else {
               this.m("Flying to " + s(this.ab, this.aA).replace(" ", ", "));
               this.b();
               this.g(this.ab, this.aA, Double.NaN, true);
            }
         }
      }
   }

   private void d() {
      if (this.y()) {
         if (this.aS != sb.e.g || !this.K()) {
            int var1 = this.aS != sb.e.g ? 1 : 0;
            if (this.aj == null && this.aR - this.aL >= (var1 != 0 ? 10 : 20)) {
               this.z((boolean)var1);
            }

            double var2 = this.aj != null ? this.aj.field_1352 : this.ab;
            double var4 = this.aj != null ? this.aj.field_1350 : this.aA;
            double var6 = this.aj != null ? this.aj.field_1351 : this.p(var2, var4);
            double var8 = Math.hypot(var2 - mc.field_1724.method_23317(), var4 - mc.field_1724.method_23321());
            double var10 = mc.field_1724.method_23318() - var6;
            this.aX.V = this.O(this.e());
            this.m(
               this.aS == sb.e.g
                  ? (this.aj == null ? "Descending, looking for a landing spot" : "Descending to land")
                  : (this.aS == sb.e.I ? "Landing for a stop" : "Landing to stop safely")
            );
            if (this.aj == null && this.aS != sb.e.g) {
               this.g(this.ab, this.aA, Double.NaN, var1 == 0 || com.larpclient.w.A(mc.field_1724) > 3);
            } else {
               if (this.aj != null) {
                  if (var10 < this.jF - 3.0) {
                     this.jF = var10;
                     this.aB = this.aR;
                  } else if (this.aR - this.aB > 700) {
                     this.b(wt.s.R, "Cannot get down to this landing spot, picking another");
                     this.D();
                     this.aB = this.aR;
                     return;
                  }

                  if (var8 <= 50.0 && var10 <= 28.0 && var10 >= -1.0) {
                     this.k(sb.w.T);
                     return;
                  }
               }

               double var12 = var6 + 14.0 + Math.max(0.0, var8 - 45.0) * 0.095;
               double var14 = mc.field_1724.method_23318() - var12;
               if (!this.ao && var8 < 170.0 && var10 > 32.0 && (var14 > 22.0 || var8 < 60.0)) {
                  this.ao = (boolean)1;
                  q8.v(this.getName(), "Circling down: {} blocks above the slope, {} across", (int)var14, (int)var8);
               }

               if (this.ao && (var10 <= 26.0 || var14 < 8.0 && var8 >= 60.0 || var8 > 260.0)) {
                  this.ao = (boolean)0;
               }

               if (this.ao) {
                  double var16 = Math.atan2(mc.field_1724.method_23321() - var4, mc.field_1724.method_23317() - var2) + Math.toRadians(55.0);
                  this.aX.P = var14 > 40.0 ? 3.0 : 1.9;
                  this.g(var2 + Math.cos(var16) * 42.0, var4 + Math.sin(var16) * 42.0, var6, true);
               } else {
                  this.aX.P = 1.45;
                  this.g(var2, var4, var6, true);
               }
            }
         }
      }
   }

   private void k() {
      if (mc.field_1724.method_5799()) {
         if (mc.field_1724.method_5869()) {
            this.at = (boolean)1;
         } else {
            this.J();
         }
      } else if (mc.field_1724.method_24828() && !this.n()) {
         this.J();
      } else if (!this.n()) {
         this.m("Landing");
         if (mc.field_1724.method_18798().field_1351 < -0.3 && !mc.field_1724.method_6059(class_1294.field_5902)) {
            this.o();
         }
      } else if (this.aj == null) {
         this.k(sb.w.X);
      } else {
         double var1 = Math.hypot(this.aj.field_1352 - mc.field_1724.method_23317(), this.aj.field_1350 - mc.field_1724.method_23321());
         double var3 = mc.field_1724.method_23318() - this.aj.field_1351;
         if (!(var3 < -4.0) && this.n() <= 700 && this.aV <= 24 && !(var1 > 90.0)) {
            this.m("Landing (" + (int)var1 + " blocks out, " + (int)Math.max(0.0, var3) + " up)");
            if (this.aR - this.O >= 2 || this.aC == null) {
               this.O = this.aR;
               this.F();
               this.aC = this.jc.Q(this::C, this::B, this.S, this.aj.field_1352, this.aj.field_1351, this.aj.field_1350, this.y(), this.j());
               this.aV = this.aC.cost() >= 900.0 ? this.aV + 1 : 0;
            }

            this.j(this.aC.yaw(), this.aC.pitch());
         } else {
            this.a(var3 < -4.0 ? "below the spot" : (var1 > 90.0 ? "too far from the spot" : "the approach did not work out"));
         }
      }
   }

   private void J() {
      if (this.aS == sb.e.g) {
         double var1 = this.aj == null ? Double.MAX_VALUE : Math.hypot(this.aj.field_1352 - this.ab, this.aj.field_1350 - this.aA);
         double var3 = this.aj == null
            ? Double.MAX_VALUE
            : Math.hypot(this.aj.field_1352 - mc.field_1724.method_23317(), this.aj.field_1350 - mc.field_1724.method_23321());
         if (this.e() <= 72.0 || var3 <= 25.0 || this.e() <= var1 + 20.0) {
            this.k(sb.w.A);
            return;
         }

         q8.G(this.getName(), "Touched down {} blocks short of the spot; taking off again", N(var3));
         if (this.aj != null) {
            this.D();
         }

         this.k(sb.w.u);
      } else if (this.aS == sb.e.w) {
         if (this.e() <= 72.0) {
            q8.G(this.getName(), "Stopped at the destination; counting it as arrived ({})", this.B);
            this.k(sb.w.A);
         } else {
            this.k(sb.w.z);
         }
      } else {
         this.k(sb.w.y);
      }
   }

   private void a(String var1) {
      this.a6++;
      this.b(wt.s.R, "Go-around (" + var1 + ")");
      if (this.a6 >= 4) {
         this.D();
         this.a6 = 0;
      }

      this.k(sb.w.X);
   }

   private void q() {
      if (mc.field_1724.method_5799()) {
         this.N();
      } else if (this.n()) {
         this.k(sb.w.T);
      } else if (!mc.field_1724.method_24828()) {
         this.m("Waiting to land");
      } else if (this.C()) {
         this.o.K();
         this.aJ.O("a threat at the stop");
         int var3 = com.larpclient.w.A(mc.field_1724) < 2 || !this.d() || this.l() >= 0 && this.l() < 30 ? 0 : 1;
         if (var3 != 0) {
            this.b(wt.s.a, "Danger at the landing spot, taking off");
            this.ag.add(mc.field_1724.method_73189());
            this.k(sb.w.K);
         } else {
            this.C(false, "a mob reached the stop and there was no way to take off");
         }
      } else {
         if (this.f == sb.n.Y) {
            this.f = this.F(false);
            this.a8 = this.aR;
            if (this.f == sb.n.Y) {
               if (!this.d()) {
                  if (this.E()) {
                     return;
                  }

                  this.C(false, "there is no usable elytra left");
                  return;
               }

               if (com.larpclient.w.A(mc.field_1724) == 0) {
                  this.C(false, this.U());
                  return;
               }

               int var2 = this.l();
               if (var2 >= 0 && var2 < 40) {
                  this.C(false, "the elytra is too worn to fly on and cannot be mended");
                  return;
               }

               if (this.e() <= 30.0) {
                  this.aS = sb.e.g;
                  this.k(sb.w.A);
                  return;
               }

               this.aS = sb.e.g;
               this.b(wt.s.C, "Stop done: " + com.larpclient.w.A(mc.field_1724) + " rockets, " + this.J() + " XP bottles");
               this.k(sb.w.K);
               return;
            }

            q8.G(this.getName(), "Stop task: {}", this.f);
         }

         int var1 = this.f != sb.n.M && this.f != sb.n.n ? 0 : 1;
         if (this.aR - this.a8 > (var1 != 0 ? 3600 : 400)) {
            q8.G(this.getName(), "Stop task {} timed out", this.f);
            if (var1 != 0) {
               this.aJ.O("it took too long");
               this.q.add(this.f == sb.n.M ? wh.z.Z : wh.z.S);
            }

            this.u();
         } else {
            switch (this.f) {
               case b:
                  this.m("Eating");
                  if (!this.o.d(4)) {
                     this.u();
                  }
                  break;
               case i:
                  this.v();
                  break;
               case v:
                  this.m("Putting on the spare elytra");
                  if (!this.E()) {
                     this.u();
                  }
                  break;
               case M:
                  this.g(wh.z.Z);
                  break;
               case n:
                  this.g(wh.z.S);
                  break;
               default:
                  this.f = sb.n.Y;
            }
         }
      }
   }

   private void u() {
      this.o.K();
      this.f = sb.n.Y;
   }

   private boolean R(wh.z var1) {
      boolean var2 = var1 == wh.z.Z ? this.Q.getValue() : this.U.getValue();
      return var2 && !this.q.contains(var1) && !this.A && this.W() > 0L;
   }

   private long W() {
      long var1 = wh.C(this.aw.getValue());
      return var1 < 0L ? Long.MAX_VALUE : Math.max(0L, var1 - this.aQ);
   }

   private int b() {
      return this.X.getValue().intValue() * 64;
   }

   private int D() {
      return this.ay.getValue().intValue() * 64;
   }

   private void g(wh.z var1) {
      int var2 = var1 == wh.z.Z ? 1 : 0;
      if (!this.aJ.x()) {
         int var7 = var2 != 0 ? com.larpclient.w.A(mc.field_1724) : this.J();
         int var8 = (var2 != 0 ? this.b() : this.D()) - var7;
         if (var8 <= 0) {
            this.u();
         } else {
            long var5 = this.W();
            this.aJ.u(var1, var8, wh.L(this.u.getValue(), var1), var5 == Long.MAX_VALUE ? -1L : var5);
            this.m("Buying " + (var2 != 0 ? "rockets" : "XP bottles") + " on /ah");
         }
      } else {
         gi.a var3 = this.aJ.Z();
         this.m("Buying " + (var2 != 0 ? "rockets" : "XP bottles") + " on /ah: " + this.aJ.U() + " so far");
         if (var3 != gi.a.D) {
            this.aQ = this.aQ + this.aJ.G();
            if (var2 != 0) {
               this.aa = this.aa + this.aJ.U();
            } else {
               this.H = this.H + this.aJ.U();
            }

            if (this.aJ.L()) {
               this.A = (boolean)1;
            }

            String var4 = var2 != 0 ? "rockets" : "XP bottles";
            if (this.aJ.U() > 0) {
               this.b(wt.s.C, "Bought " + this.aJ.U() + " " + var4 + " for " + wh.y(this.aJ.G()) + (this.aJ.n().isEmpty() ? "" : " (" + this.aJ.n() + ")"));
            }

            if (var3 == gi.a.a || !this.aJ.n().isEmpty()) {
               this.q.add(var1);
            }

            if (var3 == gi.a.a) {
               this.b(wt.s.a, "Could not buy " + var4 + ": " + this.aJ.n());
            }

            this.u();
         }
      }
   }

   private sb.n F(boolean var1) {
      class_746 var2 = mc.field_1724;
      if ((var2.method_7344().method_7586() <= 10 || var2.method_6032() < 15.0F && var2.method_7344().method_7586() < 20)
         && tn.h(var0 -> var0.method_58694(class_9334.field_50075) != null) >= 0
         && this.s()) {
         return sb.n.b;
      } else {
         int var3 = this.l();
         int var4 = this.r();
         boolean var5 = this.t();
         int var6 = var5 && this.R(wh.z.S) ? 1 : 0;
         if (var3 >= 0 && var3 <= this.s() && (!var5 || this.J() <= 0 && var6 == 0) && this.u() >= 0) {
            return sb.n.v;
         } else {
            int var7 = var5 && var3 >= 0 && var3 < var4 * (var1 ? 0.9 : 0.97) ? 1 : 0;
            int var8 = com.larpclient.w.A(mc.field_1724);
            if (var7 != 0 && this.J() == 0 && var6 != 0) {
               return sb.n.n;
            } else if (var7 != 0 && this.J() > 0) {
               return sb.n.i;
            } else if (this.e() < 1500.0 && var8 >= 4) {
               return sb.n.Y;
            } else if (var8 < this.b() / 2 && this.R(wh.z.Z)) {
               return sb.n.M;
            } else {
               return var6 != 0 && this.J() < this.D() / 2 ? sb.n.n : sb.n.Y;
            }
         }
      }
   }

   private boolean s() {
      return !mc.field_1724.method_6115() || mc.field_1724.method_6030().method_58694(class_9334.field_50075) != null;
   }

   private void v() {
      int var1 = this.l();
      int var2 = this.r();
      class_1799 var3 = mc.field_1724.method_6118(class_1304.field_6174);
      if (var1 >= 0 && var3.method_7986() && var1 < var2) {
         if (this.aR - this.a8 < 2) {
            this.aM = var1;
            this.Z = this.aR;
         }

         if (var1 > this.aM) {
            this.aM = var1;
            this.Z = this.aR;
         }

         if (this.aR - this.Z > 200) {
            this.h++;
            this.b(wt.s.a, "Mending stopped working here (other Mending gear may be taking the XP)");
            this.u();
         } else {
            this.m("Mending the elytra: " + var1 * 10 / Math.max(1, var2) * 10 + "%");
            if (!this.o.Q(() -> this.jM++)) {
               q8.G(this.getName(), "Mending ended at {} of {} uses: no XP bottles left in the hotbar", var1, var2);
               this.u();
            }
         }
      } else {
         if (var1 >= 0) {
            this.h = 0;
            this.b(wt.s.C, "Elytra repaired");
         }

         this.u();
      }
   }

   private void K() {
      this.m("Arrived");
      if (mc.field_1724.method_5799()) {
         if (mc.field_1724.method_5869()) {
            this.at = (boolean)1;
         } else if (this.n() > 20) {
            this.C(true, null);
         }
      } else if (!mc.field_1724.method_24828()) {
         if (this.n()) {
            this.aS = sb.e.g;
            this.k(sb.w.T);
         }
      } else {
         class_243 var1 = mc.field_1724.method_18798();
         if (this.n() >= 10 || Math.hypot(var1.field_1352, var1.field_1350) <= 0.03) {
            this.C(true, null);
         }
      }
   }

   private void G() {
      this.m("Stopping: " + this.B);
      if (!mc.field_1724.method_24828() && !mc.field_1724.method_5799()) {
         if (this.n()) {
            this.k(sb.w.T);
         }
      } else {
         this.C(false, this.B.isEmpty() ? "the trip could not go on" : this.B);
      }
   }

   private void g(double var1, double var3, double var5, boolean var7) {
      if (this.aR - this.an >= 2 || this.E == null) {
         this.an = this.aR;
         this.ae.clear();
         this.F();
         this.aX.h = var1;
         this.aX.A = var3;
         this.aX.N = var5;
         this.aX.j = 14.0;
         int var8 = this.m == class_1937.field_25181 ? 1 : 0;
         this.aX.S = var8 != 0 ? 185.0 : 262.0;
         if (!Double.isNaN(var5)) {
            this.aX.S = Math.min(this.aX.S, Math.max(this.w(), var5) + 48.0);
         }

         this.aX.Y = var8 != 0 ? 35.0 : Double.NEGATIVE_INFINITY;
         this.aX.C = this.av;
         this.aX.W = this.j();
         this.aX.X = (boolean)(!var7 || this.aR < this.ak || !this.I() && !(mc.field_1724.method_23318() < this.aX.V - 50.0) ? 0 : 1);
         int var9 = com.larpclient.w.A(mc.field_1724);
         this.aX.u = var9 < 12 ? 140.0 : (var9 < 32 ? 95.0 : 70.0);
         this.E = this.jc.S(this::C, this.S, this.y(), this.Z(), this.aX);
         this.T();
      }

      this.j(this.E.yaw(), this.E.pitch());
      if (this.E.rocket()
         && this.Z()
         && Math.abs(mc.field_1724.method_36455() - this.E.pitch()) < 20.0F
         && Math.abs(class_3532.method_15393(this.E.yaw() - mc.field_1724.method_36454())) < 30.0F) {
         this.x();
      }
   }

   private void T() {
      this.aK.clear();
      wf.w var1 = new wf.w().P(this.S);
      int var2 = this.E.rocket() ? wf.A(this.aX.C) : this.y();

      for (int var3 = 0; var3 < 60; var3++) {
         var1.j = wf.a(var1.j, this.E.yaw(), 12.0F);
         var1.i = wf.a(var1.i, this.E.pitch(), 9.0F);
         wf.L(var1, this.aX.W);
         wf.s(var1);
         if (var2-- > 0) {
            wf.d(var1);
         }

         if (var3 % 3 == 0) {
            this.aK.add(new class_243(var1.P, var1.G, var1.b));
         }
      }
   }

   private void F() {
      class_243 var1 = mc.field_1724.method_18798();
      this.S
         .q(
            mc.field_1724.method_23317(),
            mc.field_1724.method_23318(),
            mc.field_1724.method_23321(),
            var1.field_1352,
            var1.field_1351,
            var1.field_1350,
            mc.field_1724.method_36454(),
            mc.field_1724.method_36455()
         );
   }

   private double j() {
      return mc.field_1724.method_6059(class_1294.field_5906) && mc.field_1724.method_18798().field_1351 <= 0.0 ? 0.01 : 0.08;
   }

   private boolean f() {
      if (this.aR < this.W - 12 && this.aR - this.V < 4) {
         return true;
      }

      if (this.aR - this.V > 80) {
         return false;
      }

      class_746 var1 = mc.field_1724;
      return !mc.field_1687
         .method_8390(
            class_1671.class, var1.method_5829().method_1014(3.0), var1x -> var1x.method_5805() && var1x.method_24921() == var1 && !var1x.method_7477()
         )
         .isEmpty();
   }

   private int y() {
      return this.f() ? Math.max(0, this.W - this.aR) : 0;
   }

   private boolean Z() {
      return this.n() && this.au.N() && this.aR - this.V >= 10 && !this.f() && !mc.field_1724.method_6115() && this.allowsScreenInteractions();
   }

   private void x() {
      this.au.z(3);
      com.larpclient.w.t var1 = com.larpclient.w.S(mc.field_1724, 3);
      int var2 = var1.rocket() == null ? 3 : Math.max(1, var1.rocket().duration());
      this.au.q(() -> this.isRuntimeActive() && this.M && this.n() && mc.field_1724 == this.l, 10.0, 3, () -> {
         this.V = this.aR;
         this.av = var2;
         this.W = this.aR + wf.A(var2);
         this.Y++;
      });
   }

   private void j(float var1, float var2) {
      this.as = var1;
      this.I = class_3532.method_15363(var2, -90.0F, 90.0F);
      this.i = (boolean)1;
   }

   private void j() {
      if (this.i && cr.a(this)) {
         float var1 = Math.abs(class_3532.method_15393(this.as - mc.field_1724.method_36454())) + Math.abs(this.I - mc.field_1724.method_36455());
         float[] var2 = this.D.V(mc.field_1724.method_36454(), mc.field_1724.method_36455(), this.as, this.I, var1 < 1.5F);
         t9.O(this, t9.v.e, var2[0], var2[1], 1.0);
      }
   }

   private boolean n() {
      return mc.field_1724.method_6128() && !mc.field_1724.method_5799();
   }

   private boolean y() {
      if (this.n()) {
         return true;
      }

      if (!mc.field_1724.method_24828()) {
         if (mc.field_1724.method_5799()) {
            this.k(sb.w.u);
            return false;
         }

         this.m("Opening the elytra");
         if (mc.field_1724.method_18798().field_1351 < 0.0 && !mc.field_1724.method_6059(class_1294.field_5902)) {
            this.o();
         }

         return false;
      } else {
         if (this.ax != sb.w.T && this.ax != sb.w.X) {
            this.k(sb.w.u);
         } else {
            this.J();
         }

         return false;
      }
   }

   private boolean U() {
      if (this.ax == sb.w.K || this.n() || mc.field_1724.method_24828() || mc.field_1724.method_6059(class_1294.field_5902)) {
         return false;
      }

      if (!this.d() || mc.field_1724.method_6101() || mc.field_1724.method_5799()) {
         return false;
      }

      if (mc.field_1724.method_18798().field_1351 > -0.5) {
         return false;
      }

      if (mc.field_1724.field_6017 < 3.0 && !this.G(10)) {
         return false;
      }

      this.m("Falling: opening the elytra");
      this.o();
      this.j(mc.field_1724.method_36454(), 8.0F);
      return true;
   }

   private boolean G(int var1) {
      class_2338 var2 = mc.field_1724.method_24515();

      for (int var3 = 1; var3 <= var1; var3++) {
         if (!mc.field_1687.method_8320(var2.method_10087(var3)).method_26220(mc.field_1687, var2.method_10087(var3)).method_1110()) {
            return false;
         }
      }

      return true;
   }

   private boolean k() {
      int var1 = this.l();
      if (this.n() && var1 >= 0 && var1 <= 6 && this.aR - this.a >= 6) {
         this.a = this.aR;
         if (this.u() < 0) {
            return false;
         }

         q8.G(this.getName(), "Elytra at {} uses mid-air; swapping in the spare", var1);
         return this.E();
      } else {
         return false;
      }
   }

   private void b() {
      int var1 = mc.field_1724.method_7344().method_7586();
      if (var1 > 10 && !mc.field_1724.method_6115()) {
         this.o.K();
      } else if (!this.f() && !(mc.field_1724.method_23318() < 70.0) && (this.E == null || !this.E.wide())) {
         if (this.o.d(4)) {
            this.m("Eating while cruising");
         }
      }
   }

   private boolean K() {
      if (this.ax == sb.w.X && this.aS != sb.e.g) {
         return false;
      }

      int var1 = this.l();
      int var2 = com.larpclient.w.A(mc.field_1724);
      double var3 = this.e();
      int var5 = this.v();
      int var6 = (var1 < 0 || var1 > i(var3) / (1 + var5) * 1.35 + 25.0) && var2 > var3 / 1000.0 * 2.6 + 5.0 ? 1 : 0;
      int var7 = var1 >= 0 && var1 <= (var5 >= 1 ? 40 : 60) ? 1 : 0;
      int var8 = var2 <= 3 ? 1 : 0;
      if (var6 != 0 && var7 == 0 && var8 == 0) {
         return false;
      }

      int var9 = var1 >= 0 && var1 <= this.s() ? 1 : 0;
      int var10 = !this.t() || this.J() <= 0 && !this.R(wh.z.S) ? 0 : 1;
      boolean var11 = this.R(wh.z.Z);
      sb.e var12 = null;
      if (var9 == 0 || var10 == 0 && this.u() < 0) {
         if (var9 != 0 && var1 <= 40) {
            var12 = sb.e.w;
         }
      } else {
         var12 = sb.e.I;
      }

      if (var12 == null && var2 <= 20 && var11) {
         var12 = sb.e.I;
      }

      if (var12 == null && var2 <= 6) {
         var12 = sb.e.w;
      }

      if (var12 == null && var7 != 0) {
         var12 = var10 == 0 && this.u() < 0 ? sb.e.w : sb.e.I;
      }

      if (var12 == null && var8 != 0) {
         var12 = var11 ? sb.e.I : sb.e.w;
      }

      if (var12 == null) {
         return false;
      }

      if (var12 == sb.e.w && this.B.isEmpty()) {
         this.B = var2 <= 6 ? this.U() : "the elytra is worn out and cannot be mended";
      }

      this.aS = var12;
      this.aj = null;
      this.a6 = 0;
      this.aL = -1000;
      this.b(
         wt.s.R, var12 == sb.e.I ? "Landing for a stop (" + (var9 != 0 ? "mend or swap the elytra" : "elytra low") + ")" : "Landing to stop safely: " + this.B
      );
      this.k(sb.w.X);
      return true;
   }

   static double i(double var0) {
      return var0 / 27.0 + 50.0;
   }

   private int s() {
      int var1 = this.v();
      int var2 = this.r();
      return Math.max(var1 >= 3 ? 50 : (var1 >= 1 ? 80 : 110), (int)(var2 * (var1 >= 3 ? 0.15 : (var1 >= 1 ? 0.2 : 0.27))));
   }

   private boolean t() {
      return this.e.getValue() && tn.S(mc.field_1724.method_6118(class_1304.field_6174)) && this.h < 3;
   }

   private String U() {
      int var1 = com.larpclient.w.A(mc.field_1724);
      return var1 == 0 ? "it ran out of rockets" : "only " + var1 + " rockets are left";
   }

   private boolean C() {
      if (this.aR - this.ai < 30 && mc.field_1724.method_6032() < 14.0F) {
         return true;
      }

      class_238 var1 = mc.field_1724.method_5829().method_1009(9.0, 4.0, 9.0);

      for (class_1297 var3 : mc.field_1687.method_8333(mc.field_1724, var1, var0 -> var0.method_5805() && var0 instanceof class_1569)) {
         if (!(var3 instanceof class_5354 var4 && !var4.method_29511()) && var3 instanceof class_1309 var5 && mc.field_1724.method_6057(var5)) {
            return true;
         }
      }

      return false;
   }

   private void z(boolean var1) {
      this.aL = this.aR;
      double var2 = mc.field_1724.method_23317();
      double var4 = mc.field_1724.method_23321();
      l8.h var7 = new l8.h() {
         @Override
         public int surface(int var1, int var2x) {
            return sb.this.C(var1, var2x);
         }

         @Override
         public l8.g ground(int var1, int var2x, int var3) {
            return sb.this.C(var1, var2x, var3);
         }
      };
      l8.c var6;
      if (this.aS == sb.e.g) {
         if (this.C(class_3532.method_15357(this.ab), class_3532.method_15357(this.aA)) == Integer.MIN_VALUE) {
            return;
         }

         this.a0++;
         var6 = l8.b(var7, this.ab, this.aA, var2, var4, 96, false, false);
         if (var6 == null) {
            var6 = l8.b(var7, this.ab, this.aA, var2, var4, 96, false, true);
         }

         if (var6 == null) {
            var6 = l8.b(var7, this.ab, this.aA, var2, var4, 160, false, true);
         }

         if (var6 == null) {
            var6 = l8.b(var7, this.ab, this.aA, var2, var4, 160, true, false);
         }

         if (var6 == null && this.a0 >= 12) {
            var6 = l8.b(var7, this.ab, this.aA, var2, var4, 300, true, true);
         }

         if (var6 == null && this.a0 >= 24) {
            var6 = l8.b(var7, var2, var4, var2, var4, 220, true, true);
         }

         if (var6 == null && this.a0 >= 40) {
            this.B = "there is no safe place to land near the destination";
            this.aS = sb.e.w;
            return;
         }
      } else {
         class_243 var8 = class_243.method_1030(0.0F, mc.field_1724.method_36454());
         double var9 = var1 ? 30.0 : 110.0;
         double var11 = var2 + var8.field_1352 * var9;
         double var13 = var4 + var8.field_1350 * var9;
         var6 = l8.b(var7, var11, var13, var2, var4, var1 ? 120 : 150, false, false);
         if (var6 == null) {
            var6 = l8.b(var7, var11, var13, var2, var4, var1 ? 120 : 150, false, true);
         }

         if (var6 == null && var1) {
            var6 = l8.b(var7, var2, var4, var2, var4, 200, true, true);
         }
      }

      for (int var15 = 0; var6 != null && var15 < 4 && this.U(var6); var15++) {
         double var16 = Math.toRadians(90 * var15 + 45);
         var6 = l8.b(var7, var6.x() + Math.cos(var16) * 60.0, var6.z() + Math.sin(var16) * 60.0, var2, var4, 90, this.aS != sb.e.g && var1, true);
      }

      if (var6 != null && !this.U(var6)) {
         this.aj = new class_243(var6.x() + 0.5, var6.y(), var6.z() + 0.5);
         this.aU = var6.water();
         q8.G(
            this.getName(),
            "Landing spot {} {} {}{} ({} blocks from the destination, score {})",
            var6.x(),
            var6.y(),
            var6.z(),
            var6.water() ? " in water" : "",
            N(Math.hypot(this.aj.field_1352 - this.ab, this.aj.field_1350 - this.aA)),
            String.format(Locale.ROOT, "%.1f", var6.score())
         );
      }
   }

   private boolean U(l8.c var1) {
      for (class_243 var3 : this.ag) {
         if (Math.hypot(var3.field_1352 - var1.x() - 0.5, var3.field_1350 - var1.z() - 0.5) < 12.0) {
            return true;
         }
      }

      return false;
   }

   private void D() {
      if (this.aj != null) {
         this.ag.add(this.aj);
         q8.G(this.getName(), "Landing spot {} set aside", class_2338.method_49638(this.aj).method_23854());
      }

      this.aj = null;
      this.ao = (boolean)0;
      this.jF = Double.MAX_VALUE;
   }

   private boolean B(int var1, int var2, int var3) {
      l8.g var4 = this.C(var1, var2, var3);
      return var4 == l8.g.M ? this.aU || this.aS == sb.e.w : var4 != l8.g.e && var4 != l8.g.C;
   }

   int C(int var1, int var2) {
      long var3 = (long)var1 << 32 ^ var2 & 4294967295L;
      Integer var5 = this.ae.get(var3);
      if (var5 != null) {
         return var5;
      }

      class_2818 var6 = mc.field_1687.method_2935().method_2857(var1 >> 4, var2 >> 4, class_2806.field_12803, false);
      int var7;
      if (var6 == null) {
         var7 = Integer.MIN_VALUE;
      } else {
         int var8 = var6.method_12005(class_2903.field_13197, var1 & 15, var2 & 15) + 1;
         var7 = var8 <= mc.field_1687.method_31607() ? mc.field_1687.method_31607() - 64 : var8;
      }

      this.ae.put(var3, var7);
      return var7;
   }

   private double p(double var1, double var3) {
      int var5 = this.C(class_3532.method_15357(var1), class_3532.method_15357(var3));
      return var5 != Integer.MIN_VALUE && var5 > mc.field_1687.method_31607() + 1 ? var5 : (this.m == class_1937.field_25181 ? 60.0 : 68.0);
   }

   l8.g C(int var1, int var2, int var3) {
      if (var2 - 1 < mc.field_1687.method_31607()) {
         return l8.g.q;
      }

      class_2338 var4 = new class_2338(var1, var2 - 1, var3);
      class_2680 var5 = mc.field_1687.method_8320(var4);
      class_3610 var6 = var5.method_26227();
      if (var6.method_15767(class_3486.field_15518)) {
         return l8.g.e;
      }

      if (var6.method_15767(class_3486.field_15517)) {
         return l8.g.M;
      }

      if (var5.method_26164(class_3481.field_15503)) {
         return l8.g.u;
      }

      if (var5.method_27852(class_2246.field_10092)
         || var5.method_26164(class_3481.field_23799)
         || var5.method_27852(class_2246.field_10029)
         || var5.method_27852(class_2246.field_16999)
         || var5.method_26164(class_3481.field_21952)
         || var5.method_27852(class_2246.field_27879)
         || var5.method_27852(class_2246.field_28048)
         || var5.method_27852(class_2246.field_10606)
         || var5.method_27852(class_2246.field_10343)) {
         return l8.g.C;
      }

      if (!var5.method_27852(class_2246.field_10030) && !var5.method_27852(class_2246.field_21211)) {
         class_265 var7 = var5.method_26220(mc.field_1687, var4);
         if (var7.method_1110()) {
            return l8.g.q;
         } else {
            class_238 var8 = var7.method_1107();
            if (!(var8.field_1325 > 1.01)
               && !(var8.field_1325 < 0.45)
               && !(var8.field_1320 - var8.field_1323 < 0.9)
               && !(var8.field_1324 - var8.field_1321 < 0.9)) {
               class_2680 var9 = mc.field_1687.method_8320(var4.method_10084());
               return var9.method_26227().method_15769() && var9.method_26220(mc.field_1687, var4.method_10084()).method_1110() ? l8.g.m : l8.g.q;
            } else {
               return l8.g.q;
            }
         }
      } else {
         return l8.g.q;
      }
   }

   private boolean I() {
      class_243 var1 = mc.field_1724.method_18798();
      double var2 = Math.hypot(var1.field_1352, var1.field_1350);
      class_243 var4 = var2 > 0.2
         ? new class_243(var1.field_1352 / var2, 0.0, var1.field_1350 / var2)
         : class_243.method_1030(0.0F, mc.field_1724.method_36454());
      int var5 = class_3532.method_15357(mc.field_1724.method_23317() + var4.field_1352 * 110.0);
      int var6 = class_3532.method_15357(mc.field_1724.method_23321() + var4.field_1350 * 110.0);
      return mc.field_1687.method_2935().method_2857(var5 >> 4, var6 >> 4, class_2806.field_12803, false) != null;
   }

   private int w() {
      if (this.aR - this.ah >= 20 || this.a9 == Integer.MIN_VALUE) {
         this.ah = this.aR;
         int var1 = mc.field_1724.method_31477();
         int var2 = mc.field_1724.method_31479();
         int var3 = Integer.MIN_VALUE;

         for (short var4 = -160; var4 <= 160; var4 += 16) {
            for (short var5 = -160; var5 <= 160; var5 += 16) {
               int var6 = this.C(var1 + var4, var2 + var5);
               if (var6 != Integer.MIN_VALUE) {
                  var3 = Math.max(var3, var6);
               }
            }
         }

         this.a9 = var3 == Integer.MIN_VALUE ? (int)this.p(this.ab, this.aA) : var3;
      }

      return this.a9;
   }

   private boolean u() {
      int var1 = mc.field_1724.method_31477();
      int var2 = mc.field_1724.method_31479();
      int var3 = Integer.MIN_VALUE;

      for (byte var4 = -24; var4 <= 24; var4 += 6) {
         for (byte var5 = -24; var5 <= 24; var5 += 6) {
            var3 = Math.max(var3, this.C(var1 + var4, var2 + var5));
         }
      }

      return var3 != Integer.MIN_VALUE && mc.field_1724.method_23318() < var3 + 20 && this.n() < 200;
   }

   private boolean K(int var1) {
      class_238 var2 = mc.field_1724.method_5829().method_35580(0.02, 0.0, 0.02);
      int var3 = class_3532.method_15357(mc.field_1724.method_23318() + 0.6);

      for (int var4 = class_3532.method_15357(var2.field_1323); var4 <= class_3532.method_15357(var2.field_1320); var4++) {
         for (int var5 = class_3532.method_15357(var2.field_1321); var5 <= class_3532.method_15357(var2.field_1324); var5++) {
            for (int var6 = var3; var6 <= var3 + var1; var6++) {
               class_2338 var7 = new class_2338(var4, var6, var5);
               if (!mc.field_1687.method_8320(var7).method_26220(mc.field_1687, var7).method_1110()) {
                  return false;
               }
            }
         }
      }

      return true;
   }

   private Float y() {
      class_243 var1 = mc.field_1724.method_33571();
      if (this.A(var1, new class_243(0.0, 1.0, 0.0), 2.2)) {
         return null;
      }

      class_243 var2 = mc.field_1724.method_73189();
      float var3 = mc.field_1724.method_36454();

      for (float var7 : new float[]{0.0F, 45.0F, -45.0F, 90.0F, -90.0F, 135.0F, -135.0F, 180.0F}) {
         class_243 var8 = class_243.method_1030(-32.0F, var3 + var7);
         class_243 var9 = new class_243(-var8.field_1350, 0.0, var8.field_1352).method_1029().method_1021(0.35);
         byte var10 = 0;

         for (class_243 var14 : new class_243[]{
            var2.method_1031(0.0, 1.2, 0.0),
            var2.method_1031(0.0, 1.8, 0.0),
            var2.method_1031(0.0, 1.2, 0.0).method_1019(var9),
            var2.method_1031(0.0, 1.2, 0.0).method_1020(var9)
         }) {
            if (this.A(var14, var8, 22.0)) {
               var10 = 1;
               break;
            }
         }

         if (var10 == 0) {
            return var3 + var7;
         }
      }

      return null;
   }

   private boolean A(class_243 var1, class_243 var2, double var3) {
      class_3965 var5 = mc.field_1687
         .method_17742(
            new class_3959(var1, var1.method_1019(var2.method_1029().method_1021(var3)), class_3960.field_17558, class_242.field_1348, mc.field_1724)
         );
      return var5.method_17783() == class_240.field_1332;
   }

   private boolean d() {
      class_1799 var1 = mc.field_1724.method_6118(class_1304.field_6174);
      return var1.method_31574(class_1802.field_8833) && class_1309.method_63624(var1, class_1304.field_6174);
   }

   private int l() {
      class_1799 var1 = mc.field_1724.method_6118(class_1304.field_6174);
      return var1.method_31574(class_1802.field_8833) && var1.method_7963() ? var1.method_7936() - var1.method_7919() : -1;
   }

   private int r() {
      class_1799 var1 = mc.field_1724.method_6118(class_1304.field_6174);
      return var1.method_31574(class_1802.field_8833) ? var1.method_7936() : 432;
   }

   private int v() {
      class_1799 var1 = mc.field_1724.method_6118(class_1304.field_6174);

      for (Entry var3 : var1.method_58657().method_57539()) {
         if (((class_6880)var3.getKey()).method_40225(class_1893.field_9119)) {
            return var3.getIntValue();
         }
      }

      return 0;
   }

   private int u() {
      int var1 = Math.max(0, this.l());
      int var2 = -1;
      int var3 = var1;

      for (int var4 = 0; var4 < 9; var4++) {
         class_1799 var5 = mc.field_1724.method_31548().method_5438(var4);
         if (var5.method_31574(class_1802.field_8833) && class_1309.method_63624(var5, class_1304.field_6174)) {
            int var6 = var5.method_7936() - var5.method_7919();
            if (var6 >= 60 && var6 > var3 + 20) {
               var2 = var4;
               var3 = var6;
            }
         }
      }

      return var2;
   }

   private boolean E() {
      int var1 = this.u();
      if (var1 < 0) {
         return false;
      }

      cr.y(this, () -> {
         int var1x = this.u();
         if (var1x >= 0) {
            class_1661 var2 = mc.field_1724.method_31548();
            var2.method_61496(var1x);
            if (mc.field_1761.method_2919(mc.field_1724, class_1268.field_5808).method_23665()) {
               w6.H(mc.field_1724, class_1268.field_5808);
               q8.G(this.getName(), "Put on the spare elytra from hotbar slot {}", var1x + 1);
               this.b(wt.s.R, "Put on the spare elytra");
            }
         }
      }, () -> this.isRuntimeActive() && this.M && mc.field_1724 == this.l);
      return true;
   }

   private int J() {
      int var1 = 0;

      for (int var2 = 0; var2 < 9; var2++) {
         class_1799 var3 = mc.field_1724.method_31548().method_5438(var2);
         if (var3.method_31574(class_1802.field_8287)) {
            var1 += var3.method_7947();
         }
      }

      return var1;
   }

   private boolean B() {
      for (int var1 = 9; var1 < 36; var1++) {
         if (com.larpclient.w.P(mc.field_1724.method_31548().method_5438(var1))) {
            return true;
         }
      }

      return false;
   }

   private void N() {
      if (this.ac < 0) {
         this.ac = this.aR;
      }

      if (this.aR - this.ac > 1200) {
         this.C(false, "it was stuck in water and could not reach the shore");
      } else {
         this.m("Swimming to shore");
         this.at = (boolean)1;
         if (this.aR % 20 == 0 || this.a1 == null) {
            this.a1 = this.b(32);
         }

         if (this.a1 == null) {
            this.j(mc.field_1724.method_36454(), 0.0F);
         } else {
            float[] var1 = t9.X(mc.field_1724.method_33571(), this.a1);
            this.j(var1[0], 0.0F);
            if (Math.abs(class_3532.method_15393(var1[0] - mc.field_1724.method_36454())) < 30.0F) {
               this.p = (boolean)1;
            }
         }
      }
   }

   private class_243 b(int var1) {
      int var2 = mc.field_1724.method_31477();
      int var3 = mc.field_1724.method_31479();
      class_243 var4 = null;
      double var5 = Double.MAX_VALUE;

      for (int var7 = -var1; var7 <= var1; var7 += 2) {
         for (int var8 = -var1; var8 <= var1; var8 += 2) {
            int var9 = this.C(var2 + var7, var3 + var8);
            if (var9 != Integer.MIN_VALUE && this.C(var2 + var7, var9, var3 + var8) == l8.g.m) {
               double var10 = var7 * var7 + var8 * var8;
               if (var10 < var5) {
                  var5 = var10;
                  var4 = new class_243(var2 + var7 + 0.5, var9, var3 + var8 + 0.5);
               }
            }
         }
      }

      return var4;
   }

   public static class_10185 O() {
      if (az != null && az.isEnabled()) {
         sb var0 = az;
         class_310 var1 = class_310.method_1551();
         return var0 != null
               && var0.isRuntimeActive()
               && var0.M
               && var1.field_1724 != null
               && var1.field_1724 == var0.l
               && var1.field_1724.field_6012 == var0.aI
               && (var0.p || var0.at)
               && !wp.d()
               && !ff.e()
               && !(cm.j() instanceof class_465)
            ? new class_10185(var0.p, false, false, false, var0.at, false, false)
            : null;
      } else {
         return null;
      }
   }

   private double e() {
      return Math.hypot(this.ab - mc.field_1724.method_23317(), this.aA - mc.field_1724.method_23321());
   }

   private double O(double var1) {
      double var3 = this.p(this.ab, this.aA);
      double var5 = var3 + 14.0 + Math.max(0.0, var1 - 45.0) * 0.095 + 30.0;
      return Math.max(this.m == class_1937.field_25181 ? 150.0 : 140.0, Math.min(this.aD.getValue(), var5));
   }

   private double T() {
      double var1 = this.p(this.ab, this.aA);
      double var3 = Math.max(0.0, mc.field_1724.method_23318() - var1 - 14.0);
      return 45.0 + var3 / 0.095 + 60.0;
   }

   private void R() {
      class_243 var1 = mc.field_1724.method_73189();
      if (this.aN != null) {
         double var2 = Math.hypot(var1.field_1352 - this.aN.field_1352, var1.field_1350 - this.aN.field_1350);
         if (this.ap != null && var1.method_1022(this.aN.method_1019(this.ap)) > 4.0 && this.n()) {
            if (this.aR - this.r > 200) {
               this.r = this.aR;
               this.R = 0;
            }

            this.R++;
            if (this.R >= 3 && this.aR >= this.ak) {
               this.ak = this.aR + 60;
               q8.G(this.getName(), "The server pulled the player back {} times in 10 seconds; holding rockets for 3 seconds", this.R);
            }
         } else if (var2 < 10.0) {
            this.d += var2;
         }

         double var4 = (Math.hypot(this.ab - this.aN.field_1352, this.aA - this.aN.field_1350) - this.e()) * 20.0;
         this.C = this.C == 0.0 ? var4 : this.C * 0.97 + var4 * 0.03;
      }

      this.aN = var1;
      this.ap = mc.field_1724.method_18798();
      float var6 = mc.field_1724.method_6032();
      if (var6 < this.aG) {
         this.ai = this.aR;
      }

      this.aG = var6;
   }

   private String p() {
      double var1 = this.e();
      StringBuilder var3 = new StringBuilder(N(var1)).append(" blocks left");
      if (this.C > 2.0 && this.ax != sb.w.A) {
         var3.append(", about ").append(tc.T((long)(var1 / this.C * 1000.0)));
      }

      return var3.toString();
   }

   private void C(boolean var1, String var2) {
      if (!this.M) {
         this.disable();
      } else {
         String var3 = tc.T(System.currentTimeMillis() - this.T);
         String var4 = class_2338.method_49638(mc.field_1724.method_73189()).method_23854();
         String var5 = N(this.d)
            + " blocks in "
            + var3
            + ", "
            + this.Y
            + " rockets"
            + (this.jM > 0 ? ", " + this.jM + " XP bottles" : "")
            + (
               this.aQ > 0L
                  ? ", bought "
                     + (this.aa > 0 ? this.aa + " rockets" : "")
                     + (this.aa > 0 && this.H > 0 ? " and " : "")
                     + (this.H > 0 ? this.H + " XP bottles" : "")
                     + " for "
                     + wh.y(this.aQ)
                  : ""
            );
         String var6 = var1
            ? "arrived at " + s(this.ab, this.aA).replace(" ", ", ") + ": " + var5 + "."
            : "stopped at " + var4 + ", " + var2 + ". " + M(var5) + ".";
         q8.G(this.getName(), var1 ? "Arrived: {}" : "Stopped: {}", var6);
         this.v(var6);
         this.b(var1 ? wt.s.C : wt.s.a, M(var6));
         this.m(var1 ? "Arrived" : "Stopped: " + var2);
         this.aY = this.getStatus();
         int var7 = !this.a7.getValue()
               || mc.method_1562() == null
               || mc.method_1542()
               || !var1 && !mc.field_1724.method_24828() && !mc.field_1724.method_5799()
            ? 0
            : 1;
         this.M = (boolean)0;
         this.disable();
         if (var7 != 0) {
            q8.G(this.getName(), "Logging off after the trip");
            mc.method_1562().method_48296().method_10747(class_2561.method_43470("Larp Auto Elytra Pilot: " + var6));
         } else if (this.a7.getValue() && mc.method_1542()) {
            q8.G(this.getName(), "Log Off On Arrival skipped in singleplayer");
         }
      }
   }

   private void v(String var1) {
      if (mc != null && mc.field_1724 != null) {
         mc.field_1724.method_7353(class_2561.method_43470("Larp Auto Elytra Pilot: " + var1), false);
      }
   }

   private void b(wt.s var1, String var2) {
      q8.G(this.getName(), "Notice: {}", var2);
      if (!var2.equals(this.aH) || this.aR - this.aE >= 100) {
         this.aH = var2;
         this.aE = this.aR;
         switch ((String)this.aT.getValue()) {
            case "Toast":
               wt.T().k("Auto Elytra Pilot", var2, var1, 5000L);
               break;
            case "Chat":
               this.v(var2.substring(0, 1).toLowerCase(Locale.ROOT) + var2.substring(1));
         }
      }
   }

   private static String M(String var0) {
      return var0.isEmpty() ? var0 : var0.substring(0, 1).toUpperCase(Locale.ROOT) + var0.substring(1);
   }

   private void L() {
      if (this.isInGame() && this.z.getValue() && this.isRuntimeActive() && this.M && mc.field_1687 == this.v) {
         try {
            int var1 = ez.V(0.25);
            double var2 = this.p(this.ab, this.aA);
            class_12180.method_75546(new class_243(this.ab, var2, this.aA), new class_243(this.ab, var2 + 256.0, this.aA), ei.D(var1, 0.9), 3.0F)
               .method_75533();
            class_243 var4 = mc.field_1724.method_73189();
            if (!this.aK.isEmpty() && this.ax != sb.w.T && mc.field_1724.method_6128()) {
               for (class_243 var6 : this.aK) {
                  class_12180.method_75546(var4, var6, ei.D(var1, 0.8), 2.0F).method_75533();
                  var4 = var6;
               }
            }

            class_12180.method_75546(var4, new class_243(this.ab, Math.max(var2, var4.field_1351 - 40.0), this.aA), ei.D(var1, 0.35), 1.5F).method_75533();
            if (this.aj != null) {
               class_12180.method_75541(
                     new class_238(
                        this.aj.field_1352 - 1.5,
                        this.aj.field_1351,
                        this.aj.field_1350 - 1.5,
                        this.aj.field_1352 + 1.5,
                        this.aj.field_1351 + 0.1,
                        this.aj.field_1350 + 1.5
                     ),
                     class_12179.method_75537(-14249, 2.0F, 872400983)
                  )
                  .method_75533();
            }
         } catch (RuntimeException var7) {
            q8.O(this.getName(), "Waypoint line failed", var7);
         }
      }
   }

   @Override
   public tc.g hudCard() {
      if (this.isInGame() && this.ar.getValue() && this.isRuntimeActive() && this.M && mc.field_1687 == this.v) {
         double var1 = this.af <= 0.0 ? -1.0 : Math.clamp(1.0 - this.e() / this.af, 0.0, 1.0);
         int var3 = this.l();
         String var4 = this.getStatus();
         return new tc.g(
            this.p(),
            var1 < 0.0 ? "" : Math.round(var1 * 100.0) + "%",
            var4 != null && !var4.isBlank() ? var4 : "Starting",
            com.larpclient.w.A(mc.field_1724) + " rockets" + (var3 >= 0 ? ", elytra " + var3 + "/" + this.r() : ""),
            var1
         );
      } else {
         return null;
      }
   }

   public sb.w z() {
      return this.ax;
   }

   public boolean N() {
      return this.M;
   }

   public double g() {
      return this.M && mc != null && mc.field_1724 != null ? this.e() : -1.0;
   }

   public int Z() {
      return this.Y;
   }

   public class_243 c() {
      return this.aj;
   }

   enum e {
      g,
      I,
      w;
   }

   enum n {
      Y,
      b,
      i,
      v,
      M,
      n;
   }

   public enum w {
      u,
      K,
      Q,
      r,
      X,
      T,
      y,
      A,
      z;
   }
}
