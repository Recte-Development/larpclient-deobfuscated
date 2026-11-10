package com.larpclient.module;

import com.larpclient.LarpClient;
import com.larpclient.c5;
import com.larpclient.ei;
import com.larpclient.f1;
import com.larpclient.gd;
import com.larpclient.gv;
import com.larpclient.q8;
import com.larpclient.qd;
import com.larpclient.qo;
import com.larpclient.ti;
import com.larpclient.u9;
import com.larpclient.wb;
import com.larpclient.wc;
import io.netty.channel.ChannelFutureListener;
import java.util.ArrayDeque;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import net.fabricmc.fabric.api.client.rendering.v1.world.WorldRenderEvents;
import net.fabricmc.fabric.api.client.rendering.v1.world.WorldRenderEvents.DebugRender;
import net.minecraft.class_10017;
import net.minecraft.class_10055;
import net.minecraft.class_10182;
import net.minecraft.class_10264;
import net.minecraft.class_12075;
import net.minecraft.class_12179;
import net.minecraft.class_12180;
import net.minecraft.class_1297;
import net.minecraft.class_1309;
import net.minecraft.class_1657;
import net.minecraft.class_238;
import net.minecraft.class_243;
import net.minecraft.class_2535;
import net.minecraft.class_2547;
import net.minecraft.class_2596;
import net.minecraft.class_2600;
import net.minecraft.class_2658;
import net.minecraft.class_2661;
import net.minecraft.class_2664;
import net.minecraft.class_2670;
import net.minecraft.class_2675;
import net.minecraft.class_2684;
import net.minecraft.class_2708;
import net.minecraft.class_2716;
import net.minecraft.class_2726;
import net.minecraft.class_2739;
import net.minecraft.class_2743;
import net.minecraft.class_2749;
import net.minecraft.class_2767;
import net.minecraft.class_2777;
import net.minecraft.class_2923;
import net.minecraft.class_310;
import net.minecraft.class_3417;
import net.minecraft.class_3532;
import net.minecraft.class_5890;
import net.minecraft.class_5891;
import net.minecraft.class_5894;
import net.minecraft.class_634;
import net.minecraft.class_6373;
import net.minecraft.class_6374;
import net.minecraft.class_638;
import net.minecraft.class_7422;
import net.minecraft.class_7438;
import net.minecraft.class_7439;
import net.minecraft.class_7827;
import net.minecraft.class_8042;
import net.minecraft.class_8738;
import net.minecraft.class_8739;
import net.minecraft.class_9178;

public final class BacktrackModule extends Module implements com.larpclient.ll {
   private static BacktrackModule T;
   private static final int m = 65536;
   private static final long z = System.nanoTime();
   private final li w = this.addSetting(new li("Version", "Packet delay algorithm", "New", "Old", "New"));
   private final lo q = this.addSetting(new lo("Range Min", "Lower bound of the box distance to the target", 1.0, 0.0, 10.0, 0.1));
   private final lo p = this.addSetting(new lo("Range Max", "Upper bound of the box distance to the target", 3.0, 0.0, 10.0, 0.1));
   private final lo H = this.addSetting(new lo("Delay Min", "Shortest hold window in milliseconds", 280.0, 0.0, 2000.0, 1.0));
   private final lo V = this.addSetting(new lo("Delay Max", "Longest hold window in milliseconds", 480.0, 0.0, 2000.0, 1.0));
   private final lo E = this.addSetting(new lo("Next Backtrack Delay Min", "Shortest wait before the next window in milliseconds", 0.0, 0.0, 2000.0, 1.0));
   private final lo t = this.addSetting(new lo("Next Backtrack Delay Max", "Longest wait before the next window in milliseconds", 10.0, 0.0, 2000.0, 1.0));
   private final lo Y = this.addSetting(
      new lo("Tracking Buffer", "Keep windows available after the target leaves range, in milliseconds", 500.0, 0.0, 2000.0, 1.0)
   );
   private final lo j = this.addSetting(new lo("Chance", "Chance that an attack allows windows, in percent", 50.0, 0.0, 100.0, 1.0));
   private final l3 l = this.addSetting(new l3("Pause On Hurt Time", "Skip windows while the target is hurt", false));
   private final lo I = this.addSetting(new lo("Hurt Time", "Target hurt time that pauses windows", 3.0, 0.0, 10.0, 1.0));
   private final li n = this.addSetting(new li("Target Mode", "Use the attacked entity or the nearest one in range", "Attack", "Attack", "Range"));
   private final lo Hq = this.addSetting(
      new lo("Time Since Last Attack", "How long an attack keeps windows available, in milliseconds", 1000.0, 0.0, 5000.0, 1.0)
   );
   private final l3 v = this.addSetting(new l3("Release Out Of Reach", "Release held packets as soon as the target's server position is too far to hit", true));
   private final lo U = this.addSetting(
      new lo("Release Distance", "Server position distance from your eyes that releases held packets. Vanilla ignores hits past 6", 6.0, 1.0, 10.0, 0.1)
   );
   private final li Hd = this.addSetting(new li("ESP", "Draw the target's server position", "Box", "Box", "Wireframe", "Fill", "None"));
   private final l0 N = this.addSetting(new l0("Color", "Server position box color", -1));
   private final l0 HT = this.addSetting(new l0("Gradient", "Second ESP color, blended toward the top", -6862849));
   private final l3 Hb = this.addSetting(new l3("Ghost Model", "Draw a hologram of the target at its server position", true));
   private final l3 r = this.addSetting(new l3("Tether", "Link the displayed target to its server position", true));
   private final l3 i = this.addSetting(new l3("Bloom", "Glow pass for the ESP", true));
   private final l3 HF = this.addSetting(new l3("Through Walls", "Keep a faint outline visible behind blocks", true));
   private final lo S = this.addSetting(new lo("New Range Min", "Lower bound of the distance to the target player", 0.0, 0.0, 10.0, 0.01));
   private final lo R = this.addSetting(new lo("New Range Max", "Upper bound of the distance to the target player", 10.0, 0.0, 10.0, 0.01));
   private final lo HQ = this.addSetting(new lo("New Delay", "How long each held update waits, in milliseconds", 15000.0, 0.0, 15000.0, 1.0));
   private final l3 h = this.addSetting(new l3("Only Attack", "Release held updates when the target moves closer", true));
   private class_1297 C;
   private String HL = "Old";
   private final wb K = new wb(65536);
   private final ArrayDeque<Runnable> J = new ArrayDeque<>();
   private final class_7422 b = new class_7422();
   private boolean O;
   private boolean f;
   private long e;
   private long Hz;
   private long o;
   private int G;
   private final ArrayDeque<Runnable> HB = new ArrayDeque<>();
   private final ArrayDeque<Long> X = new ArrayDeque<>();
   private class_7422 d;
   private boolean HO;
   private class_634 s;
   private class_638 HZ;
   private volatile int B;
   private static final double P = 0.55;
   private static final int[][] A = new int[][]{{0, 1}, {2, 3}, {4, 5}, {6, 7}, {0, 2}, {1, 3}, {4, 6}, {5, 7}, {0, 4}, {1, 5}, {2, 6}, {3, 7}};
   private final qd.x c = new qd.x(25);
   private boolean F;
   private boolean k;
   private Object g;
   private int y = -1;
   private int M;
   private int Z;
   private class_243 D;
   private double W;
   private double Q;
   private double Hn;
   private double L;
   private double Hu;
   private double u = -1.0;
   private class_10017 Hc;
   private class_243 HR;
   private double a;
   private double H3;
   private double x = -10.0;

   public BacktrackModule() {
      super("Backtrack", "Backtracks player position to a hittable state", Category.COMBAT, -1);
      T = this;
      this.q.m(this.p).M("Range");
      this.H.m(this.V).M("Delay");
      this.E.m(this.t).M("Next Backtrack Delay");
      this.S.m(this.R).M("New Range");
      this.Y.m("ms");
      this.j.m("%");
      this.I.m("");
      this.Hq.m("ms").M("Last Attack Time To Work");
      this.HQ.m("ms");

      for (lb var4 : new lb[]{this.q, this.p, this.H, this.V, this.E, this.t, this.Y, this.j, this.l, this.n, this.Hq}) {
         var4.O(() -> this.w.G("Old"));
      }

      this.I.O(() -> this.w.G("Old") && this.l.getValue());
      this.U.O(this.v::getValue);

      for (lb var11 : new lb[]{this.N, this.HT, this.Hb, this.r, this.i, this.HF}) {
         var11.O(() -> !this.Hd.G("None"));
      }

      for (lb var12 : new lb[]{this.S, this.R, this.HQ, this.h}) {
         var12.O(() -> this.w.G("New"));
      }

      WorldRenderEvents.BEFORE_DEBUG_RENDER.register((DebugRender)var1 -> this.g());
   }

   @Override
   public Set<l5> getNetworkImpacts() {
      return Set.of(l5.F);
   }

   @Override
   public String getStatus() {
      if (!this.isRuntimeActive()) {
         return "Inactive";
      }

      if (this.C == null) {
         return "Waiting for target";
      }

      int var1 = this.K.r() + this.HB.size();
      return "Tracking " + this.C.method_5477().getString() + (var1 > 0 ? " · " + var1 + " held" : "");
   }

   @Override
   public void onEnable() {
      this.HL = this.w.getValue();
      this.G = ThreadLocalRandom.current().nextInt(0, 101);
      this.K(false, false, false, null);
      this.B(false, false, null);
      this.clear();
      this.m();
   }

   @Override
   public void onDisable() {
      this.B = 0;
      this.K(true, false, true, "Stopped");
      this.B(true, false, "Stopped");
      this.clear();
   }

   private void m() {
      this.B = !this.isRuntimeActive() ? 0 : (this.w.G("Old") ? (this.C == null && this.K.r() <= 0 ? 0 : 1) : (!this.HB.isEmpty() ? 2 : 0));
   }

   public static void i(class_2596<?> var0, class_2547 var1) {
      if (T != null && T.isEnabled()) {
         BacktrackModule var2 = T;
         if (var2 != null && var2.B != 0 && var1 instanceof class_634 var3) {
            class_310 var4 = getMc();
            if (var4 != null && !var4.method_18854()) {
               if (var2.B == 1 ? J(var0) : var0 instanceof class_2661) {
                  class_2600.method_11074(var0, var3, var4.method_74186());
               }
            }
         }
      }
   }

   static boolean J(class_2596<?> var0) {
      return var0 instanceof class_2670
         || var0 instanceof class_2661
         || var0 instanceof class_2658
         || var0 instanceof class_8739
         || var0 instanceof class_8738
         || var0 instanceof class_9178
         || var0 instanceof class_5891
         || var0 instanceof class_5890
         || var0 instanceof class_2923;
   }

   public static boolean p() {
      BacktrackModule var0 = T;
      return var0 != null && var0.K.r() > 0;
   }

   private static long m() {
      return (System.nanoTime() - z) / 1000000L;
   }

   private void s() {
      String var1 = this.w.getValue();
      if (!var1.equals(this.HL)) {
         this.K(true, false, false, "Version changed");
         this.B(true, false, "Version changed");
         this.HL = var1;
      }
   }

   private boolean S() {
      class_310 var1 = getMc();
      return this.s != null && var1.method_1562() == this.s && var1.field_1687 == this.HZ && this.s.method_48296().method_10758();
   }

   private void r() {
      if (this.K.r() <= 0 && this.J.isEmpty() && this.HB.isEmpty()) {
         this.s = mc.method_1562();
         this.HZ = mc.field_1687;
      }
   }

   public static boolean m() {
      BacktrackModule var0 = T;
      return var0 != null && (var0.K.w() || var0.HO || var0.f);
   }

   public static boolean r(class_2596<?> var0, class_2547 var1, Runnable var2) {
      if (T != null && T.isEnabled()) {
         BacktrackModule var3 = T;
         class_310 var4 = getMc();
         if (var3 != null
            && !m()
            && var4.method_18854()
            && !(var0 instanceof class_8042)
            && var1 instanceof class_634
            && var1 == var4.method_1562()
            && var3.isRuntimeActive()) {
            try {
               var3.s();
               return var3.w.G("New") ? var3.S(var0, var2) : var3.Y(var0, var2);
            } finally {
               var3.m();
            }
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   public static boolean N(class_2535 var0, class_2596<?> var1, ChannelFutureListener var2, boolean var3) {
      if (T != null && T.isEnabled()) {
         BacktrackModule var4 = T;
         class_310 var5 = getMc();
         if (var4 != null
            && !m()
            && var5 != null
            && var5.method_18854()
            && var4.isRuntimeActive()
            && var5.field_1724 != null
            && var5.field_1687 != null
            && var5.method_1562() != null
            && !var5.method_1542()
            && var0 == var5.method_1562().method_48296()) {
            var4.s();
            if (!var4.w.G("New") && var1 instanceof class_6374 && var4.K.r() != 0) {
               var4.J.addLast(() -> var0.method_52906(var1, var2, var3));
               return true;
            } else {
               return false;
            }
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   public static boolean b() {
      BacktrackModule var0 = T;
      return var0 != null && var0.f;
   }

   public static void Z(class_1297 var0) {
      if (T != null && T.isEnabled()) {
         BacktrackModule var1 = T;
         if (var1 != null && var0 != null && var1.isRuntimeActive() && var1.isInGame()) {
            try {
               var1.s();
               if (!var1.w.G("New")) {
                  var1.Hz = m();
                  var1.G = ThreadLocalRandom.current().nextInt(0, 101);
                  if (var1.n.G("Attack")) {
                     var1.h(var0);
                  }

                  return;
               }

               var1.v(var0);
            } finally {
               var1.m();
            }
         }
      }
   }

   @Override
   public void onTick() {
      try {
         this.E();
      } finally {
         this.m();
      }
   }

   private void E() {
      this.s();
      if (this.w.G("New")) {
         this.j();
      } else if (mc.field_1724 != null && mc.field_1687 != null) {
         if (this.n.G("Range")) {
            class_1297 var1 = this.J();
            if (var1 == null) {
               this.K(true, false, true, "No target in range");
            } else {
               this.h(var1);
            }
         }

         int var2 = this.K.r() > 0 ? 1 : 0;
         if (var2 == 0 || !this.C(this.S())) {
            if (this.y()) {
               if (var2 != 0 && this.K.F(m())) {
                  this.a("Window ended");
                  this.K(false, false, true, null);
               }
            } else if (var2 != 0) {
               this.a("Target no longer eligible");
               this.K(false, false, true, null);
            }
         }
      } else {
         this.K(false, true, false, "World unavailable");
      }
   }

   private boolean Y(class_2596<?> var1, Runnable var2) {
      if (mc.field_1724 == null || mc.field_1687 == null || mc.method_1562() == null || mc.method_1542()) {
         return false;
      }

      if (r(var1)) {
         return false;
      }

      if (this.z(var1)) {
         this.K(true, false, true, "Server boundary packet");
         return false;
      }

      boolean var3 = this.y();
      int var4 = this.K.r() > 0 ? 1 : 0;
      if (var4 == 0 && !var3) {
         return false;
      }

      class_1297 var5 = this.C;
      if (var4 == 0 && !(var1 instanceof class_6373)) {
         this.P(var1, var5);
         return false;
      }

      if (var5 != null) {
         class_243 var6 = this.P(var1, var5);
         if (var6 != null && this.C(var5.method_5864().method_18386().method_30757(var6))) {
            return false;
         }

         if (var6 != null && this.n(var5, var6)) {
            this.a("Target moved closer");
            return false;
         }
      }

      if (var4 == 0) {
         if (var5 != null && this.o(this.S())) {
            return false;
         }

         this.r();
         long var8 = T(this.H, this.V);
         if (this.K.G()) {
            this.K.i();
         }

         this.K.m(m(), var8);
         q8.v("Backtrack", "Old window opened for {} ms", var8);
      }

      if (this.K.N(var2)) {
         return true;
      }

      this.a("Packet capacity reached");
      return false;
   }

   private static boolean r(class_2596<?> var0) {
      return var0 instanceof class_7438
         || var0 instanceof class_7439
         || var0 instanceof class_7827
         || var0 instanceof class_5894
         || var0 instanceof class_2767 var1 && var1.method_11894().comp_349() == class_3417.field_15115;
   }

   private boolean z(class_2596<?> var1) {
      return var1 instanceof class_2708
         || var1 instanceof class_2661
         || var1 instanceof class_2749 var3 && var3.method_11833() <= 0.0F
         || var1 instanceof class_2716 var2 && this.C != null && C(var2, this.C.method_5628());
   }

   private class_243 P(class_2596<?> var1, class_1297 var2) {
      return var2 != null && mc.field_1687 != null ? P(this.b, var1, mc.field_1687, var2) : null;
   }

   static class_243 P(class_7422 var0, class_2596<?> var1, class_638 var2, class_1297 var3) {
      class_243 var4 = null;
      if (var1 instanceof class_2684 var5 && var5.method_22826() && var5.method_11645(var2) == var3) {
         var4 = I(var0, var5);
      } else if (var1 instanceof class_2777 var6 && var6.comp_3237() == var3.method_5628()) {
         var4 = var6.comp_3239().isEmpty()
            ? var6.comp_3238().comp_3148()
            : class_10182.method_63639(
                  new class_10182(var0.method_60933(), var3.method_18798(), var3.method_36454(), var3.method_36455()), var6.comp_3238(), var6.comp_3239()
               )
               .comp_3148();
      } else if (var1 instanceof class_10264 var7 && var7.comp_3223() == var3.method_5628()) {
         var4 = z(var7);
      }

      if (var4 != null) {
         var0.method_43494(var4);
      }

      return var4;
   }

   private boolean n(class_1297 var1, class_243 var2) {
      class_238 var3 = var1.method_5864().method_18386().method_30757(var2);
      class_238 var4 = mc.field_1724.method_5829();
      return Q(var4, var3) < Q(var4, var1.method_5829());
   }

   private boolean y() {
      return this.C != null && this.C.method_5805() && this.Q(this.C);
   }

   private boolean Q(class_1297 var1) {
      if (mc.field_1724 != null && var1 != null) {
         boolean var2 = this.D(var1);
         long var3 = m();
         if (var2) {
            this.e = var3;
         }

         return (var2 || this.e != 0L && var3 - this.e <= this.Y.getValue().longValue())
            && this.a(var1)
            && mc.field_1724.field_6012 > 10
            && this.G < this.j.getValue()
            && var3 >= this.o
            && (!this.l.getValue() || !this.O)
            && this.Hz != 0L
            && var3 - this.Hz <= this.Hq.getValue().longValue();
      } else {
         return false;
      }
   }

   private boolean a(class_1297 var1) {
      if (!(var1 instanceof class_1309 var2 && mc.field_1724 != null && var1 != mc.field_1724)) {
         return false;
      } else {
         if (!var2.method_5805() || var2.method_31481() || var2.method_7325() || !var2.method_5863()) {
            return false;
         }

         if (!(var1 instanceof class_1657)) {
            return true;
         }

         MCFriendModule var3 = LarpClient.b().G().b(MCFriendModule.class);
         return var3 == null || !var3.M(var1.method_5477().getString());
      }
   }

   private boolean D(class_1297 var1) {
      double var2 = Math.sqrt(Q(mc.field_1724.method_5829(), var1.method_5829()));
      return var2 >= Math.min(this.q.getValue(), this.p.getValue()) && var2 <= Math.max(this.q.getValue(), this.p.getValue());
   }

   private class_1297 J() {
      double var1 = Math.min(this.q.getValue(), this.p.getValue());
      double var3 = Math.max(this.q.getValue(), this.p.getValue());
      class_238 var5 = mc.field_1724.method_5829();
      class_1297 var6 = null;
      double var7 = Double.MAX_VALUE;

      for (class_1297 var10 : mc.field_1687.method_8333(mc.field_1724, var5.method_1014(var3 + 1.0), this::a)) {
         double var11 = Math.sqrt(Q(var5, var10.method_5829()));
         if (var11 >= var1 && var11 <= var3 && var11 < var7) {
            var6 = var10;
            var7 = var11;
         }
      }

      return var6;
   }

   private void h(class_1297 var1) {
      this.O = (boolean)(var1 instanceof class_1309 var2 && var2.field_6235 >= this.I.getValue() ? 1 : 0);
      if (this.Q(var1)) {
         if (var1 != this.C) {
            this.K(true, false, false, "Target changed");
            this.b.method_43494(var1.method_43389().method_60933());
         }

         this.C = var1;
      }
   }

   private void K(boolean var1, boolean var2, boolean var3, String var4) {
      if (var1 && !var2) {
         this.a(var4);
      } else if (var2) {
         this.o(var4);
      }

      if (this.C != null && var3) {
         this.o = m() + T(this.E, this.t);
      }

      this.C = null;
      this.O = (boolean)0;
      this.b.method_43494(class_243.field_1353);
   }

   private void a(String var1) {
      int var2 = this.K.r() + this.J.size();
      if (var2 == 0) {
         this.K.i();
      } else if (mc.method_1562() != null && this.S()) {
         this.f = (boolean)1;

         try {
            while (!this.J.isEmpty() && this.S()) {
               this.J.removeFirst().run();
            }
         } finally {
            this.f = (boolean)0;
            this.J.clear();
         }

         this.K.U(this::S);
         if (var1 != null) {
            q8.v("Backtrack", "Released {} held packets: {}", var2, var1);
         }
      } else {
         this.o(var1 + "; session changed");
      }
   }

   private void o(String var1) {
      int var2 = this.K.r() + this.J.size();
      this.K.i();
      this.J.clear();
      if (var2 > 0 && var1 != null) {
         q8.v("Backtrack", "Discarded {} held packets: {}", var2, var1);
      }
   }

   private void j() {
      if (mc.field_1724 != null && mc.field_1687 != null) {
         if (!this.R()) {
            this.B(true, false, "Target left New range");
         } else if (!this.C(this.S())) {
            this.E(false, null);
         }
      } else {
         this.B(true, true, "World unavailable");
      }
   }

   private boolean S(class_2596<?> var1, Runnable var2) {
      if (mc.field_1724 == null || mc.field_1687 == null) {
         this.B(true, true, "World unavailable");
         return false;
      }

      if (var1 instanceof class_2708 || var1 instanceof class_2661 || var1 instanceof class_2749 var3 && var3.method_11833() <= 0.0F) {
         this.B(true, false, "Server boundary packet");
         return false;
      }

      if (this.HB.isEmpty() && !this.R()) {
         return false;
      }

      this.X(var1);
      if (this.C == null || s(var1) || !b(var1)) {
         return false;
      }

      if (this.HB.size() >= 65536) {
         this.E(true, "Packet capacity reached");
         return false;
      }

      if (this.HB.isEmpty()) {
         this.r();
         q8.v("Backtrack", "New queue opened with {} ms delay", this.HQ.getValue().longValue());
      }

      this.HB.addLast(var2);
      this.X.addLast(m());
      return true;
   }

   private static boolean s(class_2596<?> var0) {
      return var0 instanceof class_2767 || var0 instanceof class_2749 || var0 instanceof class_2739;
   }

   private static boolean b(class_2596<?> var0) {
      return var0 instanceof class_6373
         || var0 instanceof class_2726
         || var0 instanceof class_2777
         || var0 instanceof class_10264
         || var0 instanceof class_2743
         || var0 instanceof class_2684
         || var0 instanceof class_2675
         || var0 instanceof class_2664;
   }

   private void X(class_2596<?> var1) {
      if (mc.field_1724 != null && mc.field_1687 != null && this.C != null) {
         class_243 var2 = null;
         if (var1 instanceof class_2684 var3 && var3.method_22826() && var3.method_11645(mc.field_1687) == this.C && this.d != null) {
            var2 = I(this.d, var3);
         } else if (var1 instanceof class_2777 var4 && var4.comp_3237() == this.C.method_5628()) {
            var2 = var4.comp_3238().comp_3148();
         } else if (var1 instanceof class_10264 var5 && var5.comp_3223() == this.C.method_5628()) {
            var2 = z(var5);
         }

         if (var2 != null) {
            if (this.d == null) {
               this.d = new class_7422();
            }

            this.d.method_43494(var2);
            class_238 var6 = this.C.method_5829().method_997(var2.method_1020(this.C.method_73189()));
            if (!this.C(var6) && this.h.getValue()) {
               class_243 var7 = mc.field_1724.method_73189();
               if (J(var7, var6) < J(var7, this.C.method_5829())) {
                  this.E(true, "Target moved closer");
               }
            }
         }
      }
   }

   private boolean R() {
      return this.C != null && this.C.method_5805() && this.d(this.C);
   }

   private boolean d(class_1297 var1) {
      if (mc.field_1724 != null && var1 instanceof class_1657 && var1 != mc.field_1724) {
         double var2 = J(mc.field_1724.method_73189(), var1.method_5829());
         double var4 = Math.min(this.S.getValue(), this.R.getValue());
         double var6 = Math.max(this.S.getValue(), this.R.getValue());
         return var2 >= var4 * var4 && var2 <= var6 * var6;
      } else {
         return false;
      }
   }

   private void v(class_1297 var1) {
      if (this.d(var1)) {
         if (var1 != this.C) {
            this.B(true, false, "Target changed");
            this.d = new class_7422();
            this.d.method_43494(var1.method_73189());
            q8.v("Backtrack", "New target {}", var1.method_5477().getString());
         }

         this.C = var1;
      }
   }

   private void B(boolean var1, boolean var2, String var3) {
      if (var2) {
         this.t(var3);
      } else if (var1) {
         this.E(true, var3);
      }

      this.C = null;
      this.d = null;
   }

   private void E(boolean var1, String var2) {
      if (!this.HO && !this.HB.isEmpty()) {
         if (mc.method_1562() != null && this.S()) {
            long var3 = m();
            long var5 = this.HQ.getValue().longValue();
            int var7 = 0;
            this.HO = (boolean)1;

            try {
               while (!this.HB.isEmpty() && (var1 || var3 - this.X.peekFirst() >= var5) && this.S()) {
                  this.X.removeFirst();
                  this.HB.removeFirst().run();
                  var7++;
               }

               if (!this.S()) {
                  this.t("Session changed during release");
               }
            } catch (RuntimeException | Error var12) {
               this.HB.clear();
               this.X.clear();
               throw var12;
            } finally {
               this.HO = (boolean)0;
            }

            if (var7 > 0 && var2 != null) {
               q8.v("Backtrack", "Released {} held updates: {}", var7, var2);
            }
         } else {
            this.t(var2 == null ? "Session changed" : var2 + "; session changed");
         }
      }
   }

   private void t(String var1) {
      int var2 = this.HB.size();
      this.HB.clear();
      this.X.clear();
      if (var2 > 0 && var1 != null) {
         q8.v("Backtrack", "Discarded {} held updates: {}", var2, var1);
      }
   }

   static long T(lo var0, lo var1) {
      int var2 = Math.round((float)Math.min(var0.getValue(), var1.getValue()));
      int var3 = Math.round((float)Math.max(var0.getValue(), var1.getValue()));
      return var3 > var2 ? ThreadLocalRandom.current().nextInt(var2, var3 + 1) : Math.max(0, var2);
   }

   static double Q(class_238 var0, class_238 var1) {
      double var2 = Math.max(0.0, Math.max(var0.field_1323 - var1.field_1320, var1.field_1323 - var0.field_1320));
      double var4 = Math.max(0.0, Math.max(var0.field_1322 - var1.field_1325, var1.field_1322 - var0.field_1325));
      double var6 = Math.max(0.0, Math.max(var0.field_1321 - var1.field_1324, var1.field_1321 - var0.field_1324));
      return var2 * var2 + var4 * var4 + var6 * var6;
   }

   static double J(class_243 var0, class_238 var1) {
      return var0.method_1025(
         new class_243(
            class_3532.method_15350(var0.field_1352, var1.field_1323, var1.field_1320),
            class_3532.method_15350(var0.field_1351, var1.field_1322, var1.field_1325),
            class_3532.method_15350(var0.field_1350, var1.field_1321, var1.field_1324)
         )
      );
   }

   private boolean o(class_238 var1) {
      if (var1 != null && mc.field_1724 != null && this.v.getValue()) {
         double var2 = this.U.getValue();
         return J(mc.field_1724.method_33571(), var1) > var2 * var2;
      } else {
         return false;
      }
   }

   private boolean C(class_238 var1) {
      if (!this.o(var1)) {
         return false;
      }

      String var2 = String.format(Locale.ROOT, "Target out of reach (%.1f blocks)", Math.sqrt(J(mc.field_1724.method_33571(), var1)));
      if (this.K.r() + this.HB.size() > 0) {
         this.d(var1);
      }

      if (this.w.G("New")) {
         this.B(true, false, var2);
      } else {
         this.a(var2);
         this.K(false, false, true, null);
      }

      return true;
   }

   private class_238 S() {
      if (this.C == null) {
         return null;
      } else if (this.w.G("New")) {
         return this.d == null ? null : this.C.method_5829().method_997(this.d.method_60933().method_1020(this.C.method_73189()));
      } else {
         return this.C.method_5805() && !this.b.method_60933().equals(class_243.field_1353)
            ? this.C.method_5864().method_18386().method_30757(this.b.method_60933())
            : null;
      }
   }

   private void d(class_238 var1) {
      this.HR = new class_243((var1.field_1323 + var1.field_1320) / 2.0, var1.field_1322, (var1.field_1321 + var1.field_1324) / 2.0);
      this.a = Math.max(var1.method_17939(), var1.method_17941()) / 2.0;
      this.H3 = var1.method_17940();
      this.x = qo.x();
   }

   @Override
   public String effectName() {
      return "Backtrack";
   }

   @Override
   public void tick(class_310 var1) {
      if (!this.isEnabled()) {
         ;
      }
   }

   @Override
   public boolean glowing() {
      return !this.isEnabled() ? false : this.F && this.k;
   }

   @Override
   public void clear() {
      this.F = (boolean)0;
      this.D = this.HR = null;
      this.Hc = null;
      this.g = null;
      this.y = -1;
      this.Hu = this.L = 0.0;
      this.u = -1.0;
      this.x = -10.0;
      this.c.T();
   }

   @Override
   public void prepare(float var1, class_12075 var2) {
      if (this.isEnabled()) {
         this.F = (boolean)0;
         this.Hc = null;
         this.c.T();
         if (this.isInGame() && !this.Hd.G("None")) {
            double var3 = qo.x();
            double var5 = qo.Q();
            this.k = this.i.getValue();
            this.M = this.N.getValue();
            this.Z = this.HT.getValue();
            this.u = this.HR != null && var3 - this.x < 0.55 ? Math.max(0.0, var3 - this.x) / 0.55 : -1.0;
            class_238 var7 = this.S();
            if (var7 == null) {
               this.D = null;
               this.y = -1;
               this.Hu = 0.0;
               this.F = (boolean)(this.u >= 0.0 ? 1 : 0);
            } else {
               class_243 var8 = new class_243((var7.field_1323 + var7.field_1320) / 2.0, var7.field_1322, (var7.field_1321 + var7.field_1324) / 2.0);
               int var9 = this.D != null && this.y == this.C.method_5628() && this.g == mc.field_1687 && this.D.method_1025(var8) < 16.0 ? 1 : 0;
               this.D = var9 != 0 ? this.D.method_35590(var8, 1.0 - Math.exp(-var5 * 24.0)) : var8;
               this.y = this.C.method_5628();
               this.g = mc.field_1687;
               this.W = var7.method_17939() / 2.0;
               this.Q = var7.method_17941() / 2.0;
               this.Hn = var7.method_17940();
               this.Hu = this.Hu + ((this.K.r() + this.HB.size() > 0 ? 1 : 0) - this.Hu) * (1.0 - Math.exp(-var5 * 10.0));
               class_243 var10 = this.C.method_30950(var1);
               this.L = u9.L((var10.method_1022(this.D) - 0.05) / 0.45);
               if (this.Hb.getValue() && this.L > 0.01) {
                  class_10017 var11 = gv.i(this.C, var1);
                  var11.field_53325 = this.D.field_1352;
                  var11.field_53326 = this.D.field_1351;
                  var11.field_53327 = this.D.field_1350;
                  var11.field_53337 = null;
                  if (var11 instanceof class_10055 var12) {
                     var12.field_53525 = null;
                  }

                  var11.field_60160 = null;
                  var11.field_53335 = (boolean)0;
                  var11.field_61823.clear();
                  var11.field_61821 = 0;
                  this.Hc = var11;
               }

               if (this.r.getValue() && this.L > 0.01) {
                  class_243 var22 = var10.method_1031(0.0, this.Hn * 0.55, 0.0);
                  class_243 var23 = this.D.method_1031(0.0, this.Hn * 0.55, 0.0);
                  double var13 = Math.min(0.3, var22.method_1022(var23) * 0.12);

                  for (int var15 = 0; var15 <= 24; var15++) {
                     double var16 = var15 / 24.0;
                     double var18 = Math.pow(0.5 + 0.5 * Math.cos((var16 * 2.5 - var3 * 1.6) * Math.PI * 2.0), 6.0);
                     double var20 = u9.L(var16 / 0.12) * u9.L((1.0 - var16) / 0.12);
                     this.c
                        .x(
                           var22.method_35590(var23, var16).method_1031(0.0, Math.sin(Math.PI * var16) * var13, 0.0),
                           0.03 * (1.0 + 0.7 * var18) * (0.35 + 0.65 * var20),
                           ei.R(ei.W(this.M, this.Z, var16), this.L * (0.3 + 0.7 * var18))
                        );
                  }

                  this.c.k(var2.field_63078);
               }

               this.F = (boolean)1;
            }
         } else {
            this.clear();
         }
      }
   }

   @Override
   public void render(f1 var1, boolean var2) {
      if (this.isEnabled()) {
         if (this.F && (!var2 || this.k)) {
            long var3 = c5.X();
            double var5 = qo.x();
            if (this.D != null) {
               double var7 = 0.65 + 0.35 * this.Hu;
               class_238 var9 = new class_238(
                     this.D.field_1352 - this.W,
                     this.D.field_1351,
                     this.D.field_1350 - this.Q,
                     this.D.field_1352 + this.W,
                     this.D.field_1351 + this.Hn,
                     this.D.field_1350 + this.Q
                  )
                  .method_1014(0.03);
               if (this.Hc != null) {
                  var1.k(this.Hc, gv.y.I, ei.W(this.M, this.Z, 0.4), 0.55 * this.L * (var2 ? 0.5 : 1.0));
               }

               var1.blend(ti.d.l);
               if (!this.Hd.G("Fill")) {
                  this.Y(var1, var9, var5, var7);
               }

               if (!this.Hd.G("Wireframe")) {
                  this.V(var1, var9, var5, var7);
               }

               this.i(var1, var9, var5, var7);
               this.g(var1, var9, var5, var7, var2);
               if (this.c.F() > 1) {
                  var1.G(this.c, true);
               }

               var1.blend(ti.d.G);
               if (!var2 && !this.Hd.G("Wireframe")) {
                  this.Q(var1, var9, var7);
               }
            }

            if (this.u >= 0.0) {
               var1.blend(ti.d.l);
               this.C(var1, this.u);
               var1.blend(ti.d.G);
            }

            c5.w("Backtrack ESP", var3);
         }
      }
   }

   private static class_243 l(class_238 var0, int var1) {
      return new class_243(
         (var1 & 1) == 0 ? var0.field_1323 : var0.field_1320,
         (var1 & 2) == 0 ? var0.field_1322 : var0.field_1325,
         (var1 & 4) == 0 ? var0.field_1321 : var0.field_1324
      );
   }

   private void Y(f1 var1, class_238 var2, double var3, double var5) {
      class_243 var7 = var2.method_1005();

      for (int[] var11 : A) {
         class_243 var12 = l(var2, var11[0]);
         class_243 var13 = l(var2, var11[1]);

         for (int var14 = 0; var14 < 6; var14++) {
            class_243 var15 = var12.method_35590(var13, var14 / 6.0);
            class_243 var16 = var12.method_35590(var13, (var14 + 1) / 6.0);
            var1.I(
               var15.field_1352,
               var15.field_1351,
               var15.field_1350,
               var16.field_1352,
               var16.field_1351,
               var16.field_1350,
               0.045,
               0.045,
               this.L(var2, var7, var15, var3, var5),
               this.L(var2, var7, var16, var3, var5)
            );
         }
      }
   }

   private int L(class_238 var1, class_243 var2, class_243 var3, double var4, double var6) {
      double var8 = Math.clamp((var3.field_1351 - var1.field_1322) / Math.max(1.0E-6, var1.method_17940()), 0.0, 1.0);
      double var10 = Math.atan2(var3.field_1350 - var2.field_1350, var3.field_1352 - var2.field_1352);
      double var12 = Math.pow(0.5 + 0.5 * Math.cos(var10 * 2.0 + var8 * 4.0 - var4 * 3.2), 10.0);
      int var14 = ei.W(this.M, this.Z, var8);
      return ei.R(ei.W(var14, var14 | 16777215, var12 * 0.6), var6 * (0.6 + 0.4 * var12));
   }

   private void V(f1 var1, class_238 var2, double var3, double var5) {
      double var7 = var3 * 1.6;
      double var9 = 0.5 - 0.5 * Math.cos(var7);
      double var11 = Math.sin(var7);

      for (int var13 = 0; var13 < 3; var13++) {
         double var14 = Math.clamp(var9 - Math.signum(var11) * var13 * 0.07, 0.0, 1.0);
         double var16 = var13 == 0 ? 0.85 : 0.4 / var13 * Math.abs(var11);
         int var18 = ei.R(ei.W(this.M, this.Z, var14), var16 * var5);
         double var19 = var2.field_1322 + var2.method_17940() * var14;
         double var21 = var13 == 0 ? 0.04 : 0.025;
         var1.I(var2.field_1323, var19, var2.field_1321, var2.field_1320, var19, var2.field_1321, var21, var21, var18, var18);
         var1.I(var2.field_1320, var19, var2.field_1321, var2.field_1320, var19, var2.field_1324, var21, var21, var18, var18);
         var1.I(var2.field_1320, var19, var2.field_1324, var2.field_1323, var19, var2.field_1324, var21, var21, var18, var18);
         var1.I(var2.field_1323, var19, var2.field_1324, var2.field_1323, var19, var2.field_1321, var21, var21, var18, var18);
      }
   }

   private void i(f1 var1, class_238 var2, double var3, double var5) {
      class_243 var7 = var2.method_1005();
      double var8 = Math.max(var2.method_17939(), var2.method_17941()) * 0.75;
      double var10 = var2.field_1322 + 0.02;
      double var12 = var3 / 1.3 - Math.floor(var3 / 1.3);
      var1.f(var7.field_1352, var10, var7.field_1350, var8 * (1.0 + 0.04 * Math.sin(var3 * 3.0)), 0.14, ei.R(this.Z, 0.7 * var5));
      var1.f(var7.field_1352, var10, var7.field_1350, var8 * (1.0 + var12 * 1.1), 0.1, ei.R(this.M, 0.5 * var5 * (1.0 - var12) * (1.0 - var12)));
   }

   private void g(f1 var1, class_238 var2, double var3, double var5, boolean var7) {
      for (int var8 = 0; var8 < 8; var8++) {
         class_243 var9 = l(var2, var8);
         double var10 = 0.5 + 0.5 * Math.sin(var3 * 4.5 + var8 * 1.9);
         int var12 = ei.W((var8 & 2) == 0 ? this.M : this.Z, -1, 0.45);
         var1.U(var9.field_1352, var9.field_1351, var9.field_1350, var7 ? 0.22 : 0.16, 0.0, ei.R(var12, (var7 ? 0.35 : 0.2) * var5), gd.h.f, 0.0, 0.0, 0.0);
         if (!var7) {
            var1.V(var9.field_1352, var9.field_1351, var9.field_1350, 0.05 + 0.035 * var10, var3 * 0.8 + var8, ei.R(var12, var5 * (0.55 + 0.45 * var10)));
         }
      }
   }

   private void Q(f1 var1, class_238 var2, double var3) {
      double var5 = this.Hd.G("Fill") ? 1.8 : 1.0;
      int var7 = ei.R(this.M, 0.16 * var5 * var3);
      int var8 = ei.R(this.Z, 0.035 * var5 * var3);
      double[][] var9 = new double[][]{
         {var2.field_1323, var2.field_1321, var2.field_1320, var2.field_1321},
         {var2.field_1320, var2.field_1321, var2.field_1320, var2.field_1324},
         {var2.field_1320, var2.field_1324, var2.field_1323, var2.field_1324},
         {var2.field_1323, var2.field_1324, var2.field_1323, var2.field_1321}
      };

      for (double[] var13 : var9) {
         var1.vertex(var13[0], var2.field_1322, var13[1], var7);
         var1.vertex(var13[2], var2.field_1322, var13[3], var7);
         var1.vertex(var13[2], var2.field_1325, var13[3], var8);
         var1.vertex(var13[0], var2.field_1325, var13[1], var8);
      }

      var1.vertex(var2.field_1323, var2.field_1325, var2.field_1321, var8);
      var1.vertex(var2.field_1320, var2.field_1325, var2.field_1321, var8);
      var1.vertex(var2.field_1320, var2.field_1325, var2.field_1324, var8);
      var1.vertex(var2.field_1323, var2.field_1325, var2.field_1324, var8);
   }

   private void C(f1 var1, double var2) {
      double var4 = 1.0 - Math.pow(1.0 - var2, 3.0);
      double var6 = Math.pow(1.0 - var2, 1.5);
      class_243 var8 = this.HR;
      double var9 = this.a * 1.5 * (1.0 + var4 * 2.2);
      double var11 = var8.field_1351 + 0.03;
      var1.f(var8.field_1352, var11, var8.field_1350, var9, 0.05 + 0.22 * (1.0 - var2), ei.R(this.M, 0.9 * var6));
      var1.f(var8.field_1352, var11, var8.field_1350, var9 * 0.7, 0.12, ei.R(this.Z, 0.6 * var6));
      class_238 var13 = new class_238(
            var8.field_1352 - this.a, var8.field_1351, var8.field_1350 - this.a, var8.field_1352 + this.a, var8.field_1351 + this.H3, var8.field_1350 + this.a
         )
         .method_1014(0.35 * var4);
      int var14 = ei.R(ei.W(this.M, this.Z, 0.5), var6 * var6);

      for (int[] var18 : A) {
         class_243 var19 = l(var13, var18[0]);
         class_243 var20 = l(var13, var18[1]);
         var1.I(var19.field_1352, var19.field_1351, var19.field_1350, var20.field_1352, var20.field_1351, var20.field_1350, 0.05, 0.05, var14, var14);
      }

      int var23 = ei.R(ei.W(this.Z, -1, 0.5), var6);

      for (int var24 = 0; var24 < 12; var24++) {
         double var25 = var24 * Math.PI / 6.0 + var2;
         double var26 = Math.cos(var25);
         double var21 = Math.sin(var25);
         var1.U(
            var8.field_1352 + var26 * var9,
            var11 + 0.1 + var24 % 2 * var4 * 0.4,
            var8.field_1350 + var21 * var9,
            0.02 + 0.06 * (1.0 - var2),
            var25,
            var23,
            gd.h.P,
            var26,
            0.0,
            var21
         );
      }
   }

   private void g() {
      if (this.isRuntimeActive() && this.isInGame() && this.C != null && !this.Hd.G("None")) {
         int var1 = wc.l() && wc.U(this.effectName()) ? 1 : 0;
         class_238 var2 = var1 != 0 && this.D != null && this.y == this.C.method_5628()
            ? new class_238(
               this.D.field_1352 - this.W,
               this.D.field_1351,
               this.D.field_1350 - this.Q,
               this.D.field_1352 + this.W,
               this.D.field_1351 + this.Hn,
               this.D.field_1350 + this.Q
            )
            : this.S();
         if (var2 != null) {
            int var3 = this.N.getValue() & 16777215;
            if (var1 == 0) {
               int var4 = this.Hd.G("Fill") ? 0 : -520093696 | var3;
               int var5 = this.Hd.G("Wireframe") ? 0 : 754974720 | var3;
               class_12180.method_75541(var2, class_12179.method_75537(var4, 1.0F, var5)).method_75533();
            } else if (this.HF.getValue()) {
               int var6 = this.Hd.G("Fill") ? 0 : 1174405120 | var3;
               int var7 = this.Hd.G("Fill") ? 503316480 | var3 : 0;
               class_12180.method_75541(var2, class_12179.method_75537(var6, 1.0F, var7)).method_75533();
            }
         }
      }
   }

   private static boolean C(class_2716 var0, int var1) {
      return var0.method_36548().contains(var1);
   }

   private static class_243 I(class_7422 var0, class_2684 var1) {
      return var0.method_43489(var1.method_36150(), var1.method_36151(), var1.method_36152());
   }

   private static class_243 z(class_10264 var0) {
      return var0.comp_3224().comp_3148();
   }
}
