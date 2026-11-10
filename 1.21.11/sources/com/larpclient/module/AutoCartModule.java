package com.larpclient.module;

import com.larpclient.LarpClient;
import com.larpclient.bd;
import com.larpclient.cg;
import com.larpclient.cr;
import com.larpclient.fk;
import com.larpclient.hl;
import com.larpclient.hw;
import com.larpclient.q8;
import com.larpclient.t9;
import com.larpclient.uu;
import com.larpclient.w6;
import com.larpclient.wp;
import it.unimi.dsi.fastutil.objects.Object2IntMap.Entry;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import net.minecraft.class_1268;
import net.minecraft.class_1297;
import net.minecraft.class_1299;
import net.minecraft.class_1309;
import net.minecraft.class_1569;
import net.minecraft.class_1657;
import net.minecraft.class_1661;
import net.minecraft.class_1688;
import net.minecraft.class_1701;
import net.minecraft.class_1792;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_1893;
import net.minecraft.class_2241;
import net.minecraft.class_2338;
import net.minecraft.class_238;
import net.minecraft.class_243;
import net.minecraft.class_2680;
import net.minecraft.class_2768;
import net.minecraft.class_3481;
import net.minecraft.class_3486;
import net.minecraft.class_3959;
import net.minecraft.class_3965;
import net.minecraft.class_6880;
import net.minecraft.class_746;
import net.minecraft.class_9304;
import net.minecraft.class_9334;
import net.minecraft.class_239.class_240;
import net.minecraft.class_3959.class_242;
import net.minecraft.class_3959.class_3960;

public final class AutoCartModule extends Module implements uu.m {
   private static final class_1792[] K = new class_1792[]{class_1802.field_8129, class_1802.field_8848, class_1802.field_8211, class_1802.field_8655};
   private static final int x = 20;
   private static final int D = 40;
   private static AutoCartModule s;
   private final li J = this.addSetting(
      new li(
         "Rotation", "Visible picks a rail spot by an opponent and turns the real view; Crosshair uses the block you aim at", "Visible", "Visible", "Crosshair"
      )
   );
   private final lo M = this.x("Turn Speed", "Maximum visible turn in degrees per tick", 60.0, 5.0, 180.0, 5.0);
   private final lo G = this.x("Charge", "Bow draw ticks before release; 20 is full power", 20.0, 3.0, 20.0, 1.0);
   private final lo B = this.x("Range", "Rail and cart reach, capped by native block interaction distance", 4.5, 2.0, 6.0, 0.1);
   private final l3 y = this.U("Players", "Target other survival players", true);
   private final l3 a = this.U("Mobs", "Also target hostile mobs for combat or testing", false);
   private final lo N = this.x("Target Range", "Search distance for opponents", 7.0, 2.0, 12.0, 0.5);
   private final lo W = this.x("Min Damage", "Minimum expected target damage in HP", 6.0, 0.0, 36.0, 0.5);
   private final l3 O = this.U("Anti Self", "Reject shots whose strongest possible blast is excessive or lethal for you", true);
   private final lo S = this.x("Max Self Damage", "Maximum self damage from the strongest possible blast, in HP", 10.0, 0.0, 36.0, 0.5);
   private final uu f;
   private AutoCartModule.o E = AutoCartModule.o.M;
   private AutoCartModule.k o;
   private AutoCartModule.k V;
   private AutoCartModule.i d;
   private double[] T;
   private class_2338 e;
   private class_243 z;
   private long P;
   private long i = Long.MIN_VALUE;
   private float Q = -1.0F;
   private Object j;
   private Object Y;
   private String k = "Draw a Flame bow to arm";
   private String A = "";

   public AutoCartModule() {
      super("AutoCart", "Release a drawn Flame bow into a freshly placed TNT cart", Category.COMBAT, -1);
      s = this;
      this.M.O(this::k);

      for (lb var2 : List.of(this.y, this.a, this.N, this.W)) {
         var2.O(this::k);
      }

      this.S.O(this.O::getValue);
      this.J.a(var1 -> this.i("Rotation changed"));
      AutoCartModule var3 = this;
      this.f = new uu(this, var1 -> var3.addSetting(var1));
   }

   private lo x(String var1, String var2, double var3, double var5, double var7, double var9) {
      return this.addSetting(new lo(var1, var2, var3, var5, var7, var9));
   }

   private l3 U(String var1, String var2, boolean var3) {
      return this.addSetting(new l3(var1, var2, var3));
   }

   public List<lb<?>> f() {
      return List.of(this.J, this.M);
   }

   public List<lb<?>> W() {
      return List.of(this.G, this.B);
   }

   public List<lb<?>> N() {
      return List.of(this.y, this.a, this.N, this.W);
   }

   public List<lb<?>> M() {
      return List.of(this.O, this.S);
   }

   @Override
   public uu placementHighlight() {
      return this.f;
   }

   @Override
   public Module.p getTickPhase() {
      return Module.p.P;
   }

   @Override
   public Set<l5> getNetworkImpacts() {
      return Set.of(l5.Y, l5.F);
   }

   @Override
   public String getStatus() {
      return this.isEnabled() ? this.k : "";
   }

   public static void w(class_243 var0, float var1) {
      if (s != null && s.isEnabled()) {
         AutoCartModule var2 = s;
         if (var2 != null && var2.E == AutoCartModule.o.F && var2.z != null && var0.method_1025(var2.z) < 1.0) {
            var2.Q = var1;
         }
      }
   }

   @Override
   public void onTick() {
      this.f.x(null);
      if (!this.isInGame()) {
         this.i("Waiting for a world");
      } else {
         if (this.j != mc.field_1724 || this.Y != mc.field_1687) {
            this.i("Draw a Flame bow to arm");
            this.j = mc.field_1724;
            this.Y = mc.field_1687;
         }

         long var1 = mc.field_1687.method_75260();
         if (this.E != AutoCartModule.o.F || !this.j(var1)) {
            int var10000 = cr.f();
            boolean var13 = false;
            byte var12 = (byte)var10000;
            var10000 = Integer.signum(var12);
            if (((var10000 | -var10000) >>> 31 ^ 1) == 0 && mc.method_1569() && wp.o() == wp.z.D) {
               int var3 = this.s();
               if (var3 < 0) {
                  this.p("Draw a Flame bow to arm");
               } else {
                  class_1792 var4 = S();
                  if (var4 != null) {
                     var10000 = fk.T(class_1802.field_8069, true);
                     byte var15 = -1;
                     int var14 = var10000;
                     var10000 = Integer.compare(var14, var15);
                     if ((var10000 | -var10000) >>> 31 != 0) {
                        int var20 = AuraModule.d();
                        boolean var9 = false;
                        byte var8 = (byte)var20;
                        var20 = Integer.signum(var8);
                        if (((var20 | -var20) >>> 31 ^ 1) == 0) {
                           this.p("Paused while Kill Aura lowers your shield");
                           return;
                        }

                        double[] var5 = cg.G(cg.X(Math.max(var3, this.G.getValue().intValue())));
                        this.o = this.k() ? this.P(var4, var5) : this.O(var4, var5);
                        if (this.o == null) {
                           this.E = AutoCartModule.o.M;
                           this.d = null;
                           this.b(this.A);
                           return;
                        }

                        this.E = AutoCartModule.o.T;
                        this.f.x(this.o.rail());
                        if (this.k()) {
                           int var22 = cr.a(this);
                           boolean var11 = false;
                           byte var10 = (byte)var22;
                           var22 = Integer.signum(var10);
                           if (((var22 | -var22) >>> 31 ^ 1) == 0) {
                              float[] var6 = t9.X(mc.field_1724.method_33571(), this.o.aim());
                              float[] var7 = hl.m(mc.field_1724.method_36454(), mc.field_1724.method_36455(), var6[0], var6[1], this.M.getValue());
                              t9.O(this, t9.v.B, var7[0], var7[1], 1.0);
                           }
                        }

                        if (var3 < this.G.getValue()) {
                           this.k = String.format(Locale.ROOT, "Drawing %d/%d; expected %.1f HP", var3, this.G.getValue().intValue(), this.o.damage());
                           return;
                        }

                        this.k = String.format(Locale.ROOT, "Armed; expected %.1f HP, worst self %.1f HP", this.o.damage(), this.o.self());
                        AutoCartModule.k var16 = this.o;
                        cr.y(this, this::a, () -> this.o == var16 && this.E == AutoCartModule.o.T && this.m());
                        return;
                     }
                  }

                  this.p("Rails and TNT carts must be in your hotbar or hands");
               }
            } else {
               this.p("Paused: screen, focus or another camera owner");
            }
         }
      }
   }

   private AutoCartModule.k P(class_1792 var1, double[] var2) {
      List var3 = this.I();
      if (var3.isEmpty()) {
         return this.f(this.a.getValue() ? "No target in range" : "No target (enable Mobs to test on hostile mobs)");
      }

      if (this.o != null && this.o.target() != null && var3.contains(this.o.target())) {
         AutoCartModule.k var4 = this.P(this.o.rail(), this.o.aim(), var1, var2, var3);
         if (var4 != null) {
            return var4;
         }
      }

      AutoCartModule.k var13 = null;
      HashSet var5 = new HashSet();

      for (class_1309 var7 : var3) {
         for (int var8 = -1; var8 <= 0; var8++) {
            for (int var9 = -1; var9 <= 1; var9++) {
               for (int var10 = -1; var10 <= 1; var10++) {
                  class_2338 var11 = var7.method_24515().method_10069(var9, var8, var10);
                  if (var5.add(var11)) {
                     AutoCartModule.k var12 = this.P(var11, null, var1, var2, var3);
                     if (var12 != null && (var13 == null || f(var12) > f(var13))) {
                        var13 = var12;
                     }
                  }
               }
            }
         }
      }

      return var13 != null ? var13 : this.f("No reachable rail spot meets the damage limits");
   }

   private static double f(AutoCartModule.k var0) {
      return var0.damage() - var0.self() * 0.25;
   }

   private AutoCartModule.k P(class_2338 var1, class_243 var2, class_1792 var3, double[] var4, List<class_1309> var5) {
      if (!mc.field_1687.method_22340(var1)) {
         return null;
      }

      if (!mc.field_1687.method_8320(var1).method_26164(class_3481.field_15463) && !fk.v(var1)) {
         return null;
      }

      class_243 var6 = mc.field_1724.method_33571();
      double var7 = this.v();
      if (var6.method_1025(class_243.method_24953(var1)) > (var7 + 1.0) * (var7 + 1.0)) {
         return null;
      }

      for (class_243 var10 : var2 != null ? List.of(var2) : J(var1)) {
         class_243 var11 = var10.method_1020(var6);
         AutoCartModule.i var12 = this.r(var11, var11, var1, var3);
         if (var12 != null) {
            return this.P(var12, var10, var4, var5, true);
         }
      }

      return null;
   }

   private static List<class_243> J(class_2338 var0) {
      ArrayList var1 = new ArrayList(9);

      for (double var5 : new double[]{0.0, -0.42, 0.42}) {
         for (double var10 : new double[]{0.0, -0.42, 0.42}) {
            var1.add(new class_243(var0.method_10263() + 0.5 + var5, var0.method_10264() + 0.0625, var0.method_10260() + 0.5 + var10));
         }
      }

      return var1;
   }

   private AutoCartModule.k O(class_1792 var1, double[] var2) {
      class_243 var3 = mc.field_1724.method_5720();
      AutoCartModule.i var4 = this.r(var3, var3, null, var1);
      return var4 == null ? null : this.P(var4, var4.center(), var2, this.I(), false);
   }

   private AutoCartModule.k P(AutoCartModule.i var1, class_243 var2, double[] var3, List<class_1309> var4, boolean var5) {
      double var6 = hw.b(mc.field_1724, var1.center(), var3[2]);
      if (this.O.getValue() && !hw.t(var6, this.S.getValue(), mc.field_1724.method_6032() + mc.field_1724.method_6067())) {
         return this.f(String.format(Locale.ROOT, "Worst-case self damage %.1f HP is over the limit", var6));
      }

      class_1309 var8 = null;
      double var9 = 0.0;

      for (class_1309 var12 : var4) {
         double var13 = cg.S(hw.O(var12, var1.center(), var3));
         if (var13 > var9) {
            var9 = var13;
            var8 = var12;
         }
      }

      return !var5 || var8 != null && !(var9 < this.W.getValue())
         ? new AutoCartModule.k(var1.rail(), var2, var8, var9, var6)
         : this.f(String.format(Locale.ROOT, "Expected %.1f HP is below Min Damage", var9));
   }

   private AutoCartModule.i r(class_243 var1, class_243 var2, class_2338 var3, class_1792 var4) {
      class_243 var5 = mc.field_1724.method_33571();
      double var6 = this.v();
      class_243 var8 = var5.method_1019(var1.method_1029().method_1021(var6));
      class_3965 var9 = mc.field_1687.method_17742(new class_3959(var5, var8, class_3960.field_17559, class_242.field_1348, mc.field_1724));
      if (var9.method_17783() != class_240.field_1332) {
         return this.f("Aim at the top of a block within reach");
      }

      class_3965 var12 = null;
      class_2680 var13 = mc.field_1687.method_8320(var9.method_17777());
      class_2338 var10;
      class_2680 var11;
      if (var13.method_26164(class_3481.field_15463)) {
         if (fk.q(var9.method_17777(), var1, var6) == null) {
            return this.f("That rail is outside native reach");
         }

         var10 = var9.method_17777();
         var11 = var13;
      } else {
         var12 = fk.q(var9.method_17777(), var1, var6);
         fk.p var14 = var12 == null ? null : fk.A(var12, var4, true);
         if (var14 == null) {
            return this.f("A rail cannot be placed on that support");
         }

         var10 = var14.target();
         var11 = var14.state();
      }

      if (var3 != null && !var10.equals(var3)) {
         return this.f("Aim does not reach the planned rail");
      }

      if (var12 != null && var11.method_26218(mc.field_1687, var10).method_1092(var5, var8, var10) == null) {
         return this.f("Aim at a top face so the cart lands on the new rail");
      }

      int var23 = var11.method_26204() instanceof class_2241 var15 && ((class_2768)var11.method_11654(var15.method_9474())).method_11897() ? 1 : 0;
      class_243 var24 = new class_243(var10.method_10263() + 0.5, var10.method_10264() + 0.0625 + (var23 != 0 ? 0.5 : 0.0), var10.method_10260() + 0.5);
      class_238 var25 = class_1299.field_6053.method_18386().method_30757(var24);
      if (!mc.field_1687.method_8333((class_1297)null, var25, var0 -> var0 instanceof class_1688).isEmpty()) {
         return this.f("A minecart already occupies that rail");
      }

      class_243 var17 = var5.method_1023(0.0, 0.1, 0.0);
      class_243 var18 = var17.method_1019(var2.method_1029().method_1021(var17.method_1022(var24) + 2.0));
      class_3965 var19 = mc.field_1687.method_17742(new class_3959(var17, var18, class_3960.field_17558, class_242.field_1348, mc.field_1724));
      if (var19.method_17783() != class_240.field_1333) {
         var18 = var19.method_17784();
      }

      ArrayList var20 = new ArrayList();

      for (class_1297 var22 : mc.field_1687
         .method_8333(mc.field_1724, new class_238(var17, var18).method_1014(1.0), var0 -> var0.method_49108() && !var0.method_7325())) {
         var20.add(var22.method_5829());
      }

      if (!cg.t(var17, var18, var25, var20)) {
         return this.f("The arrow would hit something before the cart");
      } else {
         return !this.v(var17) && !this.v(var24) ? new AutoCartModule.i(var10, var12, var4, var24) : this.f("Rain or water would put out the Flame arrow");
      }
   }

   private boolean v(class_243 var1) {
      class_2338 var2 = class_2338.method_49638(var1);
      return mc.field_1687.method_8520(var2) || mc.field_1687.method_8316(var2).method_15767(class_3486.field_15517);
   }

   private boolean m() {
      this.d = null;
      int var1 = this.s();
      if (this.isRuntimeActive()
         && this.o != null
         && !(var1 < this.G.getValue())
         && !AuraModule.d()
         && (this.o.target() == null || !this.k() || this.l(this.o.target()))) {
         class_1792 var2 = S();
         if (var2 != null && fk.T(class_1802.field_8069, true) != -1) {
            class_243 var3 = mc.field_1724.method_5720();
            class_243 var4 = bd.a(var3);
            AutoCartModule.i var5 = this.r(var3, var4, this.o.rail(), var2);
            if (var5 == null) {
               this.b(var4.method_1026(var3) < Math.cos(Math.toRadians(0.5)) ? "Waiting for the aim to reach the server" : this.A);
               return false;
            } else {
               double[] var6 = cg.G(cg.X(var1));
               AutoCartModule.k var7 = this.P(var5, this.o.aim(), var6, this.o.target() == null ? List.of() : List.of(this.o.target()), this.k());
               if (var7 == null) {
                  this.b(this.A);
                  return false;
               } else {
                  this.d = var5;
                  this.V = var7;
                  this.T = var6;
                  return true;
               }
            }
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   private void a() {
      AutoCartModule.i var1 = this.d;
      this.d = null;
      if (var1 != null) {
         class_1661 var2 = mc.field_1724.method_31548();
         int var3 = var2.method_67532();
         int var4 = var1.railHit() == null ? var3 : fk.T(var1.railItem(), true);
         int var5 = fk.T(class_1802.field_8069, true);
         if (var4 != -1 && var5 != -1) {
            int var6 = this.s();

            try {
               mc.field_1761.method_2897(mc.field_1724);
               if (var1.railHit() != null && !this.z(var4, var1.railHit())) {
                  this.U("Rail placement rejected; the arrow flew without a cart");
                  return;
               }

               class_3965 var7 = fk.q(var1.rail(), mc.field_1724.method_5720(), this.v());
               if (var7 == null || !mc.field_1687.method_8320(var1.rail()).method_26164(class_3481.field_15463)) {
                  this.U("Placed rail is off the look; the arrow flew without a cart");
                  return;
               }

               if (!this.z(var5, var7)) {
                  this.U("Cart use rejected; the arrow flew without a cart");
                  return;
               }
            } finally {
               var2.method_61496(var3);
            }

            this.E = AutoCartModule.o.F;
            this.e = var1.rail();
            this.z = var1.center();
            this.P = mc.field_1687.method_75260();
            this.Q = -1.0F;
            this.k = "Released; waiting for detonation";
            q8.G(
               "AutoCart",
               "Released after {} draw ticks into a cart on {}{}; power {} to {}, expected {} HP{}, worst self {} HP",
               var6,
               var1.rail(),
               var1.railHit() == null ? " (existing rail)" : "",
               v(this.T[0]),
               v(this.T[2]),
               v(this.V.damage()),
               this.V.target() == null ? "" : " to " + this.V.target().method_5477().getString(),
               v(this.V.self())
            );
         }
      }
   }

   private boolean z(int var1, class_3965 var2) {
      if (var1 >= 0) {
         mc.field_1724.method_31548().method_61496(var1);
      }

      class_1268 var3 = var1 == -2 ? class_1268.field_5810 : class_1268.field_5808;
      boolean var4 = mc.field_1761.method_2896(mc.field_1724, var3, var2).method_23665();
      if (var4) {
         w6.H(mc.field_1724, var3);
      }

      return var4;
   }

   private boolean j(long var1) {
      if (this.Q >= 0.0F) {
         q8.G("AutoCart", "Cart on {} detonated {} ticks after release with server power {}", this.e, var1 - this.P, v(this.Q));
         this.c(String.format(Locale.ROOT, "Detonated with power %.2f", this.Q));
         return false;
      }

      if (var1 - this.P <= 20L) {
         return true;
      }

      class_2338 var3 = this.e;
      int var4 = !mc.field_1687.method_8333((class_1297)null, new class_238(var3), var0 -> var0 instanceof class_1701).isEmpty() ? 1 : 0;
      this.U(
         (var4 != 0 ? "No detonation; the cart remains, so the arrow missed or was put out" : "No detonation observed within one second")
            + " at "
            + var3.method_23854()
      );
      return false;
   }

   private boolean l(class_1309 var1) {
      if (var1 == mc.field_1724
         || !var1.method_5805()
         || var1.method_31481()
         || var1.method_7325()
         || var1.method_73183() != mc.field_1687
         || mc.field_1724.method_5739(var1) > this.N.getValue()
         || mc.field_1724.method_5722(var1)) {
         return false;
      }

      if (var1 instanceof class_1657 var2) {
         if (this.y.getValue() && !var2.method_31549().field_7480) {
            MCFriendModule var3 = LarpClient.b().G().b(MCFriendModule.class);
            return var3 == null || !var3.M(var2.method_5477().getString());
         } else {
            return false;
         }
      } else {
         return this.a.getValue() && var1 instanceof class_1569;
      }
   }

   private List<class_1309> I() {
      ArrayList var1 = new ArrayList();

      for (class_1297 var3 : mc.field_1687.method_18112()) {
         if (var3 instanceof class_1309 var4 && this.l(var4)) {
            var1.add(var4);
         }
      }

      var1.sort(Comparator.comparingDouble(mc.field_1724::method_5858));
      return var1.size() > 4 ? new ArrayList<>(var1.subList(0, 4)) : var1;
   }

   private int s() {
      class_746 var1 = mc.field_1724;
      if (var1 != null && var1.method_6115()) {
         class_1799 var2 = var1.method_6030();
         return var2.method_31574(class_1802.field_8102) && q(var2) ? var1.method_6048() : -1;
      } else {
         return -1;
      }
   }

   private static boolean q(class_1799 var0) {
      class_9304 var1 = (class_9304)var0.method_58694(class_9334.field_49633);
      if (var1 == null) {
         return false;
      }

      for (Entry var3 : var1.method_57539()) {
         if (((class_6880)var3.getKey()).method_40225(class_1893.field_9126)) {
            return true;
         }
      }

      return false;
   }

   private static class_1792 S() {
      for (class_1792 var3 : K) {
         if (fk.T(var3, true) != -1) {
            return var3;
         }
      }

      return null;
   }

   private boolean k() {
      return this.J.G("Visible");
   }

   private double v() {
      return Math.min(this.B.getValue(), mc.field_1724.method_55754());
   }

   private static String v(double var0) {
      return String.format(Locale.ROOT, "%.2f", var0);
   }

   private <T> T f(String var1) {
      this.A = var1;
      return null;
   }

   private void b(String var1) {
      long var2 = mc.field_1687 == null ? 0L : mc.field_1687.method_75260();
      if (!var1.equals(this.k) && (this.i == Long.MIN_VALUE || var2 < this.i || var2 - this.i >= 40L)) {
         q8.v("AutoCart", "{}", var1);
         this.i = var2;
      }

      this.k = var1;
   }

   private void p(String var1) {
      this.E = AutoCartModule.o.M;
      this.o = null;
      this.d = null;
      this.b(var1);
   }

   private void U(String var1) {
      q8.G("AutoCart", "{}", var1);
      this.c(var1);
   }

   private void c(String var1) {
      this.E = AutoCartModule.o.M;
      this.o = null;
      this.d = null;
      this.e = null;
      this.z = null;
      this.Q = -1.0F;
      this.k = var1;
   }

   private void i(String var1) {
      cr.p(this);
      this.f.p();
      this.c(var1);
   }

   @Override
   public void onEnable() {
      this.j = this.Y = null;
      this.i = Long.MIN_VALUE;
      this.i("Draw a Flame bow to arm");
      this.f.d();
      q8.G("AutoCart", "Insta-cart armed; rotation={}, charge={} ticks, range={}", this.J.getValue(), this.G.getValue().intValue(), this.B.getValue());
   }

   @Override
   public void onDisable() {
      this.i("Draw a Flame bow to arm");
      this.j = this.Y = null;
      q8.G("AutoCart", "Insta-cart disarmed; aim, pending release and detonation wait cleared");
   }

   private record i(class_2338 rail, class_3965 railHit, class_1792 railItem, class_243 center) {
   }

   private record k(class_2338 rail, class_243 aim, class_1309 target, double damage, double self) {
   }

   private enum o {
      M,
      T,
      F;
   }
}
