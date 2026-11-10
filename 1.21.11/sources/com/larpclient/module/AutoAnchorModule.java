package com.larpclient.module;

import com.larpclient.LarpClient;
import com.larpclient.cr;
import com.larpclient.fk;
import com.larpclient.hw;
import com.larpclient.q8;
import com.larpclient.qf;
import com.larpclient.t9;
import com.larpclient.uu;
import com.larpclient.wc;
import com.larpclient.wp;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import net.minecraft.class_12206;
import net.minecraft.class_1297;
import net.minecraft.class_1309;
import net.minecraft.class_1569;
import net.minecraft.class_1657;
import net.minecraft.class_1792;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_2246;
import net.minecraft.class_2338;
import net.minecraft.class_238;
import net.minecraft.class_243;
import net.minecraft.class_2680;
import net.minecraft.class_3959;
import net.minecraft.class_3965;
import net.minecraft.class_4969;
import net.minecraft.class_239.class_240;
import net.minecraft.class_3959.class_242;
import net.minecraft.class_3959.class_3960;

public final class AutoAnchorModule extends Module implements uu.m, fk.h {
   private static final int l = 12;
   private static final int K = 2;
   private static AutoAnchorModule H;
   private final lo d = this.J("Range", "Anchor reach, capped by native block interaction distance", 4.5, 2.0, 6.0, 0.1);
   private final lo L = this.J("Delay", "Ticks between confirmed anchor steps", 0.0, 0.0, 20.0, 1.0);
   private final l3 M = this.F("Auto Switch", "Use hotbar items and restore the selected slot after each step", true);
   private final l3 V = this.F("Safe Anchor", "Place a block between you and the anchor before detonating, where it leaves you least exposed", true);
   private final li y = this.addSetting(
      new li("Shield Block", "Block placed as the shield; the blast hurts players before it breaks blocks", "Glowstone", "Glowstone", "Obsidian")
   );
   private final l3 E = this.F("Players", "Target other survival players", true);
   private final l3 g = this.F("Mobs", "Also target hostile mobs for combat or testing", false);
   private final lo G = this.J("Target Range", "Search distance for opponents", 8.0, 2.0, 14.0, 0.5);
   private final lo b = this.J("Min Damage", "Minimum estimated target damage in HP", 6.0, 0.0, 36.0, 0.5);
   private final l3 j = this.F("Anti Self", "Reject excessive or lethal estimated self damage", true);
   private final lo X = this.J("Max Self Damage", "Maximum estimated self damage in HP", 8.0, 0.0, 36.0, 0.5);
   private final l3 U = this.F("Pause Using Item", "Pause while eating, drinking or using an item", true);
   private final li p = this.addSetting(
      new li("Rotation", "Visible turns the player while your camera stays free; Crosshair waits for your aim", "Visible", "Visible", "Crosshair")
   );
   private final uu f;
   private AutoAnchorModule.e W;
   private String s;
   private String i = "";
   private String k = "";
   private String c = "Waiting for a target";
   private BooleanSupplier o;
   private long e;
   private long Q;
   private Object a;
   private Object z;
   private Object P;
   private Object D;
   private int R;

   public AutoAnchorModule() {
      super("AutoAnchor", "Automatically place, charge and detonate respawn anchors", Category.COMBAT, -1);
      H = this;
      this.y.O(this.V::getValue);
      this.X.O(this.j::getValue);
      this.p.a(var1 -> {
         this.N("Rotation changed");
         this.s();
      });
      this.f = new uu(this, var1 -> this.addSetting(var1), 0.5, 0.3);
   }

   private lo J(String var1, String var2, double var3, double var5, double var7, double var9) {
      return this.addSetting(new lo(var1, var2, var3, var5, var7, var9));
   }

   private l3 F(String var1, String var2, boolean var3) {
      return this.addSetting(new l3(var1, var2, var3));
   }

   public static AutoAnchorModule T() {
      return H;
   }

   public List<lb<?>> b() {
      return List.of(this.d, this.L, this.M, this.V, this.y);
   }

   public List<lb<?>> m() {
      return List.of(this.E, this.g, this.G);
   }

   public List<lb<?>> C() {
      return List.of(this.b, this.j, this.X, this.U);
   }

   public List<lb<?>> h() {
      return List.of(this.p);
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
      return this.isEnabled() ? this.c : "";
   }

   @Override
   public void onEnable() {
      this.a = this.z = null;
      this.N("Waiting for a target");
      this.f.d();
      q8.G(
         "AutoAnchor",
         "Enabled; rotation={}, range={}, safeAnchor={}, minDamage={}, maxSelf={}",
         this.p.getValue(),
         this.d.getValue(),
         this.V.getValue(),
         this.b.getValue(),
         this.j.getValue() ? this.X.getValue() : "off"
      );
   }

   @Override
   public void onDisable() {
      this.N("Waiting for a target");
      this.s();
      this.f.p();
      this.a = this.z = null;
      q8.G("AutoAnchor", "Disabled; anchor intents and server waits cleared");
   }

   @Override
   public void aimPlacement(float var1, float var2) {
      if (this.isEnabled()) {
         if (wp.h(wp.z.J)) {
            t9.X(t9.v.j, var1, var2, 1.0);
            if (t9.k(t9.v.j)) {
               this.P = mc.field_1724;
               this.D = mc.field_1687;
               this.R = mc.field_1724.field_6012;
               wp.S(wp.z.J);
            } else {
               this.s();
            }
         }
      }
   }

   private void s() {
      this.P = this.D = null;
      wp.k(wp.z.J);
   }

   public boolean y() {
      return !this.isEnabled()
         ? false
         : this.isRuntimeActive()
            && this.p.G("Visible")
            && mc.field_1724 != null
            && this.P == mc.field_1724
            && this.D == mc.field_1687
            && mc.field_1724.field_6012 >= this.R
            && mc.field_1724.field_6012 - this.R <= 1
            && (!this.U.getValue() || !mc.field_1724.method_6115());
   }

   public float a(float var1) {
      if (this.isEnabled()) {
         if (this.isRuntimeActive() && wp.o() == wp.z.J && t9.k(t9.v.j)) {
            mc.field_1724.method_5847(mc.field_1724.method_36454());
            return mc.field_1724.method_36454();
         } else {
            return var1;
         }
      } else {
         return var1;
      }
   }

   @Override
   public void onTick() {
      this.f.x(null);
      fk.N(this);
      if (!this.isInGame()) {
         this.N("Waiting for a world");
      } else {
         if (this.a != mc.field_1724 || this.z != mc.field_1687) {
            this.N("Waiting for a target");
            this.a = mc.field_1724;
            this.z = mc.field_1687;
         }

         try {
            this.G();
         } finally {
            int var10000 = this.c.equals(this.k);
            boolean var5 = false;
            byte var4 = (byte)var10000;
            var10000 = Integer.signum(var4);
            if ((var10000 | -var10000) >>> 31 == 0) {
               this.k = this.c;
               q8.v("AutoAnchor", "Status: {}", this.c);
            }
         }
      }
   }

   private void G() {
      long var1 = mc.field_1687.method_75260();
      if (this.s != null) {
         if (!this.o.getAsBoolean()) {
            if (var1 - this.e > 12L) {
               q8.G("AutoAnchor", "No server update after {} ticks for: {}; replanning", 12, this.s);
               this.N("Server did not confirm the last step");
               return;
            }

            if (this.W != null) {
               this.f.x(this.W.anchor());
            }

            this.c = "Waiting for " + this.s;
            return;
         }

         q8.v("AutoAnchor", "Server confirmed: {}", this.s);
         if (this.s.equals("detonation")) {
            this.W = null;
         }

         this.s = null;
         this.Q = var1 + this.L.getValue().intValue();
      }

      String var3 = this.X();
      if (var3 != null) {
         this.W = null;
         this.c = var3;
      } else if (var1 < this.Q) {
         if (this.W != null) {
            this.f.x(this.W.anchor());
         }
      } else {
         List var4 = this.w();
         if (var4.isEmpty()) {
            this.W = null;
            this.c = this.g.getValue() ? "No target in range" : "No target (enable Mobs to test on hostile mobs)";
         } else {
            if (this.W != null) {
               this.W = this.L(this.W, var4);
            }

            if (this.W == null) {
               this.W = this.A(var4);
            }

            if (this.W == null) {
               this.c = this.i;
            } else {
               this.f.x(this.W.anchor());
               wc.t(this.W.target());
               this.z(this.W);
            }
         }
      }
   }

   private String X() {
      if (!cr.f() || !mc.method_1569()) {
         return "Paused: screen, focus or detached camera";
      } else if (this.U.getValue() && mc.field_1724.method_6115()) {
         return "Paused while using an item";
      } else if (mc.field_1724.method_21823()) {
         return "Paused while sneaking";
      } else if (this.e(mc.field_1724.method_24515())) {
         return "Anchors set spawn here, so they cannot explode";
      } else {
         return this.p.G("Visible") && !wp.h(wp.z.J) ? "Paused for another camera owner" : null;
      }
   }

   private void z(AutoAnchorModule.e var1) {
      class_2680 var2 = mc.field_1687.method_8320(var1.anchor());
      class_2338 var3 = var1.anchor();
      if (!var2.method_27852(class_2246.field_23152)) {
         this.J(true, var3, class_1802.field_23141, "anchor placement", () -> mc.field_1687.method_8320(var3).method_27852(class_2246.field_23152));
      } else if ((Integer)var2.method_11654(class_4969.field_23153) == 0) {
         this.J(false, var3, class_1802.field_8801, "anchor charge", () -> this.R(var3) > 0);
      } else if (var1.shield() != null && fk.v(var1.shield())) {
         class_2338 var5 = var1.shield();
         this.J(true, var5, this.z(), "shield placement", () -> !fk.v(var5));
      } else {
         if (mc.field_1724.method_6079().method_31574(class_1802.field_8801) && (Integer)var2.method_11654(class_4969.field_23153) < 4) {
            this.c = "Move offhand glowstone before detonation";
            return;
         }

         class_1792 var4 = this.y();
         if (var4 == null) {
            this.c = "A hotbar item other than glowstone is required to detonate";
            return;
         }

         this.J(false, var3, var4, "detonation", () -> !mc.field_1687.method_8320(var3).method_27852(class_2246.field_23152));
      }
   }

   private void J(boolean var1, class_2338 var2, class_1792 var3, String var4, BooleanSupplier var5) {
      AutoAnchorModule.e var6 = this.W;
      BooleanSupplier var7 = () -> this.W == var6 && this.s == null && this.X() == null;
      Consumer var8 = var5x -> {
         if (!var5x) {
            this.c = "Anchor " + var4 + " rejected; retrying";
         } else {
            this.s = var4;
            this.o = var5;
            this.e = mc.field_1687.method_75260();
            if (var4.equals("detonation")) {
               q8.G(
                  "AutoAnchor",
                  "Detonating anchor at {}: {} HP on {}, {} HP to you, {}% exposed{}",
                  var2.method_23854(),
                  String.format(Locale.ROOT, "%.1f", var6.damage()),
                  var6.target().method_5477().getString(),
                  String.format(Locale.ROOT, "%.1f", var6.self()),
                  Math.round(var6.exposure() * 100.0),
                  var6.shield() == null ? "" : " behind a shield at " + var6.shield().method_23854()
               );
            }
         }
      };
      boolean var9 = this.p.G("Visible");
      boolean var10 = var1
         ? fk.a(this, var2, var3, this.d.getValue(), var9, this.M.getValue(), var7, var8)
         : fk.C(this, var2, var3, this.d.getValue(), var9, this.M.getValue(), var7, var8);
      this.c = var10
         ? String.format(
            Locale.ROOT,
            "%s%s; %.1f HP on %s",
            Character.toUpperCase(var4.charAt(0)),
            var4.substring(1),
            this.W.damage(),
            this.W.target().method_5477().getString()
         )
         : (var9 ? "No reachable face for " + var4 : "Aim at the " + (var1 ? "support" : "anchor") + " for " + var4);
   }

   private boolean e(class_2338 var1) {
      return Boolean.TRUE.equals(mc.field_1687.method_75728().method_75697(class_12206.field_63757, var1));
   }

   private int R(class_2338 var1) {
      class_2680 var2 = mc.field_1687.method_8320(var1);
      return var2.method_27852(class_2246.field_23152) ? (Integer)var2.method_11654(class_4969.field_23153) : 0;
   }

   private class_1792 z() {
      return this.y.G("Obsidian") ? class_1802.field_8281 : class_1802.field_8801;
   }

   private class_1792 y() {
      class_1799 var1 = mc.field_1724.method_6079();
      class_1799 var2 = mc.field_1724.method_6047();
      if (!var2.method_31574(class_1802.field_8801)) {
         return var2.method_7960() ? class_1802.field_8162 : var2.method_7909();
      }

      if (this.M.getValue()) {
         for (int var3 = 0; var3 < 9; var3++) {
            class_1799 var4 = mc.field_1724.method_31548().method_5438(var3);
            if (!var4.method_31574(class_1802.field_8801) && (var4.method_7960() ? !var1.method_7960() : !var1.method_31574(var4.method_7909()))) {
               return var4.method_7960() ? class_1802.field_8162 : var4.method_7909();
            }
         }
      }

      return null;
   }

   private boolean M(class_1309 var1) {
      if (var1 == mc.field_1724
         || !var1.method_5805()
         || var1.method_31481()
         || var1.method_7325()
         || var1.method_73183() != mc.field_1687
         || mc.field_1724.method_5739(var1) > this.G.getValue()
         || mc.field_1724.method_5722(var1)) {
         return false;
      }

      if (var1 instanceof class_1657 var2) {
         if (this.E.getValue() && !var2.method_31549().field_7480) {
            MCFriendModule var3 = LarpClient.b().G().b(MCFriendModule.class);
            return var3 == null || !var3.M(var2.method_5477().getString());
         } else {
            return false;
         }
      } else {
         return this.g.getValue() && var1 instanceof class_1569;
      }
   }

   private List<class_1309> w() {
      ArrayList var1 = new ArrayList();

      for (class_1297 var3 : mc.field_1687.method_18112()) {
         if (var3 instanceof class_1309 var4 && this.M(var4)) {
            var1.add(var4);
         }
      }

      var1.sort(Comparator.comparingDouble(mc.field_1724::method_5858));
      return var1.size() > 4 ? new ArrayList<>(var1.subList(0, 4)) : var1;
   }

   private double S() {
      return Math.min(this.d.getValue(), mc.field_1724.method_55754());
   }

   private boolean Q(double var1) {
      return !this.j.getValue() || hw.t(var1, this.X.getValue(), mc.field_1724.method_6032() + mc.field_1724.method_6067());
   }

   private AutoAnchorModule.e C(class_2338 var1, class_2338 var2, List<class_1309> var3, double var4, double var6) {
      class_1309 var8 = null;
      double var9 = 0.0;

      for (class_1309 var12 : var3) {
         double var13 = qf.Q(var12, var1, var2);
         if (var13 > var9) {
            var9 = var13;
            var8 = var12;
         }
      }

      return var8 == null ? null : new AutoAnchorModule.e(var1, var2, var8, var9, var4, var6);
   }

   private AutoAnchorModule.e L(AutoAnchorModule.e var1, List<class_1309> var2) {
      boolean var3 = mc.field_1687.method_8320(var1.anchor()).method_27852(class_2246.field_23152);
      if ((var3 || fk.v(var1.anchor())) && !this.e(var1.anchor())) {
         if (this.e(var1.anchor(), var3) && this.G(var1.anchor(), var3)) {
            class_2338 var4 = var1.shield() == null || fk.v(var1.shield()) && (!this.R(var1.shield()) || !this.E(var1.anchor(), var1.shield()))
               ? null
               : var1.shield();
            float var5 = qf.W(mc.field_1724, var1.anchor(), var4);
            double var6 = qf.q(mc.field_1724, var1.anchor(), var5);
            if (!this.Q(var6)) {
               return null;
            }

            AutoAnchorModule.e var8 = this.C(var1.anchor(), var4, var2, var6, var5);
            return var8 != null && var8.damage() >= this.b.getValue() ? var8 : null;
         } else {
            return null;
         }
      } else {
         return null;
      }
   }

   private AutoAnchorModule.e A(List<class_1309> var1) {
      class_243 var2 = mc.field_1724.method_33571();
      double var3 = this.S();
      int var5 = fk.T(class_1802.field_23141, this.M.getValue()) != -1 ? 1 : 0;
      int var6 = fk.T(class_1802.field_8801, this.M.getValue()) != -1 ? 1 : 0;
      HashSet var7 = new HashSet();
      ArrayList var8 = new ArrayList();
      int var9 = 0;

      record e(class_2338 pos, double bound) {
      }

      for (class_1309 var11 : var1) {
         class_2338 var12 = var11.method_24515();

         for (int var13 = -1; var13 <= 2; var13++) {
            for (int var14 = -2; var14 <= 2; var14++) {
               for (int var15 = -2; var15 <= 2; var15++) {
                  class_2338 var16 = var12.method_10069(var14, var13, var15);
                  if (var7.add(var16)
                     && mc.field_1687.method_22340(var16)
                     && !(var2.method_1025(class_243.method_24953(var16)) > (var3 + 1.0) * (var3 + 1.0))
                     && !this.e(var16)) {
                     boolean var17 = mc.field_1687.method_8320(var16).method_27852(class_2246.field_23152);
                     if (var17) {
                        var9++;
                     }

                     if (var17 ? this.e(var16, true) : var5 != 0 && var6 != 0 && fk.v(var16)) {
                        double var18 = 0.0;

                        for (class_1309 var21 : var1) {
                           var18 = Math.max(var18, hw.X(var21, hw.d(var21.method_73189().method_1022(class_243.method_24953(var16)), 1.0, 5.0)));
                        }

                        if (var18 >= this.b.getValue()) {
                           var8.add(new e(var16, var18));
                        }
                     }
                  }
               }
            }
         }
      }

      if (var8.isEmpty()) {
         this.i = var5 == 0 && var9 == 0
            ? "No respawn anchors in hotbar or hands"
            : (var6 == 0 && var9 == 0 ? "No glowstone in hotbar or hands" : "No anchor spot in reach can reach Min Damage");
         return null;
      }

      var8.sort(Comparator.comparingDouble(e::bound).reversed());
      AutoAnchorModule.e var28 = null;
      double var29 = Double.MAX_VALUE;
      double var30 = 0.0;

      for (e var32 : var8) {
         if (var28 != null && var32.bound() <= var28.value()) {
            break;
         }

         class_2338 var33 = var32.pos();
         boolean var34 = mc.field_1687.method_8320(var33).method_27852(class_2246.field_23152);
         AutoAnchorModule.e var19 = this.C(var33, null, var1, 0.0, 0.0);
         if (var19 != null) {
            var30 = Math.max(var30, var19.damage());
            if (!(var19.damage() < this.b.getValue()) && (var28 == null || !(var19.damage() <= var28.value()))) {
               float var35 = qf.W(mc.field_1724, var33, null);
               ArrayList var36 = new ArrayList();
               if (this.V.getValue() && var35 > 0.0F && fk.T(this.z(), this.M.getValue()) != -1) {
                  for (class_2338 var23 : qf.n(var33, mc.field_1724.method_33571())) {
                     if (fk.v(var23)) {
                        float var24 = qf.W(mc.field_1724, var33, var23);
                        if (var24 < var35) {
                           var36.add(new AutoAnchorModule.f(var23, var24));
                        }
                     }
                  }

                  var36.sort(Comparator.comparingDouble(AutoAnchorModule.f::exposure));
               }

               var36.add(new AutoAnchorModule.f(null, var35));
               Boolean var37 = null;

               for (AutoAnchorModule.f var39 : var36) {
                  double var25 = qf.q(mc.field_1724, var33, var39.exposure());
                  AutoAnchorModule.e var27 = var39.pos() == null
                     ? new AutoAnchorModule.e(var33, null, var19.target(), var19.damage(), var25, var35)
                     : this.C(var33, var39.pos(), var1, var25, var39.exposure());
                  if (var27 != null) {
                     var29 = Math.min(var29, var27.self());
                     if (this.Q(var27.self()) && !(var27.damage() < this.b.getValue()) && (var28 == null || !(var27.value() <= var28.value()))) {
                        if (var37 == null) {
                           var37 = this.G(var33, var34);
                        }

                        if (!var37) {
                           break;
                        }

                        if (var27.shield() == null || this.R(var27.shield()) && this.E(var33, var27.shield())) {
                           var28 = var27;
                           break;
                        }
                     }
                  }
               }
            }
         }
      }

      if (var28 == null) {
         this.i = var29 != Double.MAX_VALUE && !this.Q(var29)
            ? String.format(Locale.ROOT, "Self damage too high: lowest %.1f HP, Max Self Damage %.1f", var29, this.X.getValue())
            : (
               var30 < this.b.getValue()
                  ? String.format(Locale.ROOT, "Target damage too low: best %.1f HP, Min Damage %.1f", var30, this.b.getValue())
                  : (this.p.G("Visible") ? "No anchor spot with a reachable face or support" : "Aim at an anchor or at a support beside an anchor spot")
            );
      } else {
         q8.v(
            "AutoAnchor",
            "Planned anchor at {}{}: {} HP on {}, {} HP to you",
            var28.anchor().method_23854(),
            var28.shield() == null ? "" : " with shield at " + var28.shield().method_23854(),
            String.format(Locale.ROOT, "%.1f", var28.damage()),
            var28.target().method_5477().getString(),
            String.format(Locale.ROOT, "%.1f", var28.self())
         );
      }

      return var28;
   }

   private boolean e(class_2338 var1, boolean var2) {
      int var3 = fk.T(class_1802.field_8801, this.M.getValue()) != -1 ? 1 : 0;
      return !var2 ? var3 != 0 && fk.T(class_1802.field_23141, this.M.getValue()) != -1 : this.R(var1) > 0 || var3 != 0;
   }

   private boolean G(class_2338 var1, boolean var2) {
      boolean var3 = this.p.G("Visible");
      if (!var2) {
         return fk.M(var1, class_1802.field_23141, this.d.getValue(), var3, this.M.getValue()) != null;
      }

      if (!var3) {
         return cr.Y(var1, this.S()) != null;
      }

      class_243 var4 = mc.field_1724.method_33571();

      for (class_243 var6 : AutoCrystalModule.g(var1, var4)) {
         if (cr.S(var1, this.S(), var6.method_1020(var4)) != null) {
            return true;
         }
      }

      return false;
   }

   private boolean R(class_2338 var1) {
      return fk.M(var1, this.z(), this.d.getValue(), this.p.G("Visible"), this.M.getValue()) != null;
   }

   private boolean E(class_2338 var1, class_2338 var2) {
      class_243 var3 = mc.field_1724.method_33571();
      class_238 var4 = new class_238(var2);
      double var5 = this.S();

      for (class_243 var8 : AutoCrystalModule.g(var1, var3)) {
         if (!(var3.method_1025(var8) > var5 * var5) && !var4.method_992(var3, var8).isPresent()) {
            class_3965 var9 = mc.field_1687.method_17742(new class_3959(var3, var8, class_3960.field_17559, class_242.field_1348, mc.field_1724));
            if (var9.method_17783() == class_240.field_1333 || var9.method_17777().equals(var1)) {
               return true;
            }
         }
      }

      return false;
   }

   private void N(String var1) {
      fk.N(this);
      this.W = null;
      this.s = null;
      this.o = null;
      this.e = this.Q = 0L;
      this.c = var1;
   }

   private record e(class_2338 anchor, class_2338 shield, class_1309 target, double damage, double self, double exposure) {
      double value() {
         return this.damage - this.self * 0.25;
      }
   }

   private record f(class_2338 pos, float exposure) {
   }
}
