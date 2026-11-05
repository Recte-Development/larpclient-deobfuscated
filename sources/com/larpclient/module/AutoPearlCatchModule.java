package com.larpclient.module;

import com.larp.obf.q5682c6a8aca9b6ec.q143ef5d8ac4bd5b8;
import com.larp.obf.q5682c6a8aca9b6ec.qa1ad45ac3ead4599;
import com.larpclient.d8;
import com.larpclient.ep;
import com.larpclient.h6;
import com.larpclient.hp;
import com.larpclient.l0;
import com.larpclient.m;
import com.larpclient.o_;
import com.larpclient.r4;
import com.larpclient.rr;
import com.larpclient.y7;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.component.DataComponents;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.throwableitemprojectile.ThrownEnderpearl;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.UseCooldown;
import net.minecraft.world.phys.Vec3;

public final class AutoPearlCatchModule extends Module {
   private static final int K = 20;
   private static final int I = 20;
   private static final int m = 20;
   private static final int t = 3;
   private static final int J = 4;
   private static final double N = 0.6;
   private static final double X = 5.0;
   private static final float u = 0.05F;
   public static final float Z = 2.5F;
   private static AutoPearlCatchModule B;
   private final la Q = this.addSetting(
      new la("Catch Bind", "Press for one pearl catch; pressing again while it throws cancels it. Separate from the module toggle", -1)
   );
   private final lp G = this.addSetting(new lp("Direction", "Up throws straight up for height; Look throws where you aim for distance", "Up", "Up", "Look"));
   private final lp a = this.addSetting(
      new lp(
         "Timing",
         "Instant throws both in one tick for a catch a few blocks out; Solved aims the wind charge a tick or more later for a farther catch; Fixed Delay throws it along the same look after Delay",
         "Instant",
         "Instant",
         "Solved",
         "Fixed Delay"
      )
   );
   private final lc p = this.addSetting(
      new lc("Delay", "Ticks from the pearl to the wind charge; same-look catches work near straight up", 2.0, 1.0, 10.0, 1.0)
   );
   private final lc S = this.addSetting(
      new lc(
         "Turn Speed", "Maximum player turn in degrees per tick; your camera stays on your look. 180 with Humanize off turns at once", 90.0, 10.0, 180.0, 5.0
      )
   );
   private final ln e = this.addSetting(new ln("Swap Back", "Return to your previous hotbar slot after each throw", true));
   private final lc O = this.addSetting(
      new lc("Return Speed", "Most the player turns per tick back to your look after the last throw. 180 returns at once", 40.0, 10.0, 180.0, 5.0)
   );
   private final ln E = this.addSetting(
      new ln(
         "Humanize", "Turn like a hand on a mouse: speed up, ease in, drift a little and move in whole sensitivity steps. Off turns in straight lines", true
      )
   );
   private final l0 b = new l0();
   private final o_ H = new o_(this);
   private final y7 y = new y7();
   private final Set<Integer> s = new HashSet<>();
   private final List<AutoPearlCatchModule.o> q = new ArrayList<>();
   private LocalPlayer P;
   private ClientLevel R;
   private long M;
   private long n = Long.MIN_VALUE;
   private long g = -1L;
   private float[] A;
   private float j;
   private float x;
   private List<h6.q> l;
   private Vec3 k;
   private Vec3 z = Vec3.ZERO;
   private Vec3 U;
   private Vec3 v = Vec3.ZERO;
   private Vec3 i = Vec3.ZERO;
   private Vec3 L = Vec3.ZERO;
   private Vec3 F = Vec3.ZERO;
   private float W;
   private float Y;
   private ThrownEnderpearl c;
   private h6.n V;
   private h6.n T;
   private String r = "Set a Catch Bind";

   public AutoPearlCatchModule() {
      super("Auto Pearl Catch", "Throw a pearl, then a wind charge it hits in flight, to teleport mid-air", Category.MOVEMENT, -1);
      B = this;
      this.p.x(this::i);
      this.G.W(var1 -> this.S("Direction changed"));
      this.a.W(var1 -> this.S("Timing changed"));
      this.Q.W(var1 -> this.S("Bind changed"));
   }

   public static AutoPearlCatchModule g() {
      return B;
   }

   public List<l6<?>> y() {
      return List.of(this.Q, this.G, this.a, this.p);
   }

   public List<l6<?>> E() {
      return List.of(this.S, this.O, this.E, this.e);
   }

   public l0 g() {
      return this.b;
   }

   public double G() {
      return this.O.getValue();
   }

   public boolean v() {
      return this.E.getValue();
   }

   @Override
   public Module.b getTickPhase() {
      return Module.b.w;
   }

   @Override
   public Set<ld> getNetworkImpacts() {
      return Set.of(ld.a, ld.c);
   }

   @Override
   public String getStatus() {
      return this.isEnabled() ? this.r : "";
   }

   public boolean n() {
      return !this.isEnabled()
         ? false
         : this.isRuntimeActive() && this.P != null && this.P == mc.player && this.R == mc.level && (this.y.R() || this.M == this.n);
   }

   public static void K(int var0) {
      if (B != null && B.isEnabled()) {
         AutoPearlCatchModule var1 = B;
         if (var1 != null && var1.isRuntimeActive() && var0 > 0 && var0 == var1.Q.getValue()) {
            if (!var1.y.W()) {
               var1.S("Cancelled with Catch Bind");
            }
         }
      }
   }

   @Override
   public void onTick() {
      if (!this.isInGame()) {
         this.S("Waiting for a world");
         this.q.clear();
      } else {
         if (this.P != mc.player || this.R != mc.level) {
            float[] var1 = !this.y.R() && this.M != this.n ? null : this.A;
            this.S("World changed");
            this.q.clear();
            this.P = mc.player;
            this.R = mc.level;
            this.k = null;
            if (var1 != null) {
               this.P.setYRot(var1[0]);
               this.P.setXRot(var1[1]);
               this.P.yRotO = var1[0];
               this.P.xRotO = var1[1];
               r4.V("PearlCatch", "World changed during a catch; your look (yaw {} pitch {}) restored", Q(var1[0]), Q(var1[1]));
            }
         }

         this.M++;
         if (rr.u() == rr.f.H) {
            this.A = rr.l(rr.j(), rr.T(), mc.options.getCameraType().isMirrored());
         }

         Vec3 var13 = this.P.position();
         this.z = this.k != null && !(var13.distanceToSqr(this.k) > 16.0) ? var13.subtract(this.k) : Vec3.ZERO;
         this.k = var13;
         this.Z();
         byte var2 = this.y.U();
         y7.c var3 = this.y.r();
         if (var3 == y7.c.s) {
            boolean var6 = false;
            byte var5 = var2;
            int var10000 = Integer.signum(var5);
            if (((var10000 | -var10000) >>> 31 ^ 1) == 0) {
               int var14 = this.y.e();
               boolean var8 = false;
               byte var7 = (byte)var14;
               var14 = Integer.signum(var7);
               if ((var14 | -var14) >>> 31 == 0) {
                  this.j = this.x = 0.0F;
                  this.y.J(this.M);
               }
            }

            int var16 = this.y.e();
            boolean var10 = false;
            byte var9 = (byte)var16;
            var16 = Integer.signum(var9);
            if (((var16 | -var16) >>> 31 ^ 1) == 0) {
               this.I();
            }
         } else {
            String var4 = this.r();
            if (var4 != null) {
               this.S(var4);
            } else if (this.y.x(this.M)) {
               String var10001;
               if (var3 == y7.c.M) {
                  var10001 = "The view did not reach the throw angle in time";
               } else {
                  byte var18 = this.i();
                  boolean var12 = false;
                  byte var11 = var18;
                  int var19 = Integer.signum(var11);
                  var10001 = ((var19 | -var19) >>> 31 ^ 1) == 0
                     ? "The wind charge could not be thrown on its tick"
                     : "No reliable catch before the pearl came down";
               }

               this.S(var10001);
            } else {
               if (var3 == y7.c.M) {
                  this.c();
               } else {
                  this.o();
               }
            }
         }
      }
   }

   private void I() {
      String var1 = this.r();
      if (var1 == null) {
         var1 = this.B();
      }

      if (var1 != null) {
         this.y.h();
         this.C(var1);
      } else {
         int var2 = this.b();
         if (var2 > 0) {
            if (var2 <= 30 && !this.y.v(this.M)) {
               this.r = String.format(Locale.ROOT, "Catch in %.2f s (cooldown)", var2 / 20.0);
            } else {
               this.y.h();
               this.C(String.format(Locale.ROOT, "Ender pearl or wind charge is cooling down (%.1f s left)", var2 / 20.0));
            }
         } else {
            this.y.h();
            this.P();
         }
      }
   }

   private int b() {
      float var1 = this.i(Items.ENDER_PEARL);
      float var2 = this.i(Items.WIND_CHARGE);
      int var3 = Math.max(D(var1, this.j, Items.ENDER_PEARL), D(var2, this.x, Items.WIND_CHARGE));
      this.j = var1;
      this.x = var2;
      return var3;
   }

   private float i(Item var1) {
      int var2 = com.larpclient.m.Q(var1, true);
      if (var2 == -1) {
         return 0.0F;
      }

      ItemStack var3 = var2 == -2 ? this.P.getOffhandItem() : this.P.getInventory().getItem(var2);
      return this.P.getCooldowns().getCooldownPercent(var3, 0.0F);
   }

   private static int D(float var0, float var1, Item var2) {
      if (var0 <= 0.0F) {
         return 0;
      }

      float var3 = var1 - var0;
      if (var3 > 0.0F) {
         return (int)Math.ceil(var0 / var3);
      }

      UseCooldown var4 = (UseCooldown)new ItemStack(var2).get(DataComponents.USE_COOLDOWN);
      return (int)Math.ceil(var0 * (var4 == null ? 20 : var4.ticks()));
   }

   private void P() {
      String var1 = this.r();
      if (var1 == null) {
         var1 = this.B();
      }

      if (var1 != null) {
         this.C(var1);
      } else {
         float[] var2 = this.n();
         float var3 = var2[0];
         float var4 = var2[1];
         float var5 = this.w() ? -90.0F : var4;
         Vec3 var6 = this.P.getEyePosition();
         h6.n var7;
         if (this.W()) {
            h6.v var8 = h6.C(var6, var3, var5, this.E(), this.o(), 0.6);
            if (var8 == null) {
               this.C(this.w() ? "No instant catch: clear the space above you" : "No instant catch along this look; clear the space ahead");
               return;
            }

            var5 = var8.pitch();
            var7 = var8.plan();
         } else {
            Vec3 var11 = h6.p(var3, var5);
            var7 = this.i()
               ? h6.v(this.k(var6, var11, this.E(), this.p.getValue().intValue()), var6, this.E(), var11, this.o())
               : this.f(var6, var11, this.E());
            if (var7 == null || var7.reliability() < 0.6) {
               this.C(this.L(var7));
               return;
            }
         }

         this.W = var3;
         this.Y = var5;
         this.V = var7;
         this.s.clear();

         for (Entity var9 : mc.level.entitiesForRendering()) {
            if (var9 instanceof ThrownEnderpearl var10 && var10.getOwner() == this.P) {
               this.s.add(var10.getId());
            }
         }

         this.y.W(this.M, 20);
         this.b.O();
         this.r = this.j(var3, var5) ? "Throwing" : "Turning to throw";
         r4.V(
            "PearlCatch",
            "Catch started; direction={}, timing={}, pitch {}{}, forecast catch on step {} at {} ({}/{} samples)",
            this.G.getValue(),
            this.a.getValue(),
            Q(var5),
            !this.w() && var5 < var4 - 0.5F ? " raised from " + Q(var4) : "",
            var7.nominal().tick(),
            r(var7.nominal().point()),
            var7.score(),
            var7.samples()
         );
         this.c();
      }
   }

   private h6.n f(Vec3 var1, Vec3 var2, Vec3 var3) {
      h6.n var4 = null;

      for (int var5 = 1; var5 <= 20; var5++) {
         h6.n var6 = h6.g(this.k(var1, var2, var3, var5), var1, var3, this.o());
         if (var6 != null && var6.reliability() >= 0.6) {
            return var6;
         }

         if (var6 != null && (var4 == null || var6.score() > var4.score())) {
            var4 = var6;
         }
      }

      return var4;
   }

   private List<h6.q> k(Vec3 var1, Vec3 var2, Vec3 var3, int var4) {
      return h6.A(var1, var2, var3, var4, this.o());
   }

   private String L(h6.n var1) {
      if (this.i() && !this.w()) {
         return "Fixed Delay only catches near straight up; use Instant or Solved for Look";
      } else if (var1 == null) {
         return this.w() ? "No catch: the pearl would come down first, so clear the space above you" : "No catch at this angle; aim higher or use Instant";
      } else {
         return String.format(
            Locale.ROOT,
            "Only %d of %d samples catch here; %s",
            var1.score(),
            var1.samples(),
            this.i() ? "try another Delay or Instant" : (this.w() ? "clear the space above you" : "aim higher or use Instant")
         );
      }
   }

   private void c() {
      if (d8.m(this)) {
         this.S(this.W, this.Y);
      }

      d8.A(this, this.W() ? this::R : this::q, this::z);
   }

   private boolean z() {
      if (!this.isRuntimeActive()
         || this.y.r() != y7.c.M
         || this.r() != null
         || (this.W() ? !this.r(this.W, this.Y, 2.5F) : !this.j(this.W, this.Y))
         || !this.n(Items.ENDER_PEARL)
         || !this.n(Items.WIND_CHARGE)
         || !this.K(Items.ENDER_PEARL)
         || AuraModule.W()) {
         return false;
      } else if (!this.W()) {
         return true;
      } else if (!this.K(Items.WIND_CHARGE)) {
         return false;
      } else {
         Vec3 var1 = this.P.getEyePosition();
         Vec3 var2 = h6.p(this.P.getYRot(), this.P.getXRot());
         h6.n var3 = h6.v(h6.j(var1, var2, this.E(), this.o()), var1, this.E(), var2, this.o());
         if (var3 != null && !(var3.reliability() < 0.6)) {
            this.T = var3;
            return true;
         } else {
            return false;
         }
      }
   }

   private void R() {
      h6.n var1 = this.T;
      this.v = this.P.position();
      byte var2 = 0;
      byte var3 = 0;

      try {
         var7 = this.Z(Items.ENDER_PEARL);
         if (var7) {
            var3 = this.Z(Items.WIND_CHARGE);
         }
      } finally {
         this.U();
      }

      if (!var7) {
         this.S("The pearl throw was rejected");
      } else if (var3 == 0) {
         this.S("The wind charge was rejected after the pearl, so the pearl flies on");
      } else {
         this.V = var1;
         this.n = this.M;
         this.y.J(this.M, 1);
         this.y.U();
         this.R(var1);
         this.r = "Pearl and wind charge thrown together";
         r4.V(
            "PearlCatch",
            "Instant pair thrown at yaw {} pitch {} from {} after a turn of {}; expected catch on step {} at {} ({}/{} samples)",
            Q(this.P.getYRot()),
            Q(this.P.getXRot()),
            r(this.P.getEyePosition()),
            this.b.F(),
            var1 == null ? "?" : var1.nominal().tick(),
            var1 == null ? "?" : r(var1.nominal().point()),
            var1 == null ? 0 : var1.score(),
            var1 == null ? 0 : var1.samples()
         );
      }
   }

   private void q() {
      this.v = this.P.position();
      Vec3 var1 = this.P.getEyePosition();
      Vec3 var2 = this.E();
      if (!this.G(Items.ENDER_PEARL)) {
         this.S("The pearl throw was rejected");
      } else {
         this.W = this.P.getYRot();
         this.Y = this.P.getXRot();
         this.i = var1;
         this.L = h6.p(this.W, this.Y);
         this.F = var2;
         this.y.J(this.M, this.i() ? this.p.getValue().intValue() + 1 : 21);
         this.r = this.i() ? "Pearl thrown; wind charge in " + this.p.getValue().intValue() + " ticks" : "Pearl thrown; aiming the wind charge";
         r4.V("PearlCatch", "Pearl thrown at yaw {} pitch {} from {} after a turn of {}", Q(this.W), Q(this.Y), r(var1), this.b.F());
         this.b.O();
      }
   }

   private void o() {
      this.T();
      if (this.c != null && this.c.isRemoved()) {
         this.S("The pearl came down before the wind charge");
      } else if (this.i()) {
         if (d8.m(this)) {
            this.S(this.W, this.Y);
         }

         if (this.y.q(this.M) >= this.p.getValue().intValue()) {
            d8.A(this, this::F, this::B);
         }
      } else {
         List var1 = this.R();
         if (this.U == null) {
            this.U = ((h6.q)var1.get(0)).K();
         }

         h6.n var2 = h6.g(var1, this.P.getEyePosition(), this.E(), this.o());
         if (var2 != null && !(var2.reliability() < 0.6)) {
            this.V = var2;
            float[] var3 = ep.g(Vec3.ZERO, var2.direction());
            this.W = var3[0];
            this.Y = Mth.clamp(var3[1], -90.0F, 90.0F);
            if (d8.m(this)) {
               this.S(this.W, this.Y);
            }

            d8.A(this, this::F, this::B);
            this.r = "Aiming wind charge";
         } else {
            this.r = "Waiting for a reliable catch line";
         }
      }
   }

   private boolean B() {
      if (this.isRuntimeActive() && this.y.r() == y7.c.i && this.r() == null && this.n(Items.WIND_CHARGE) && this.K(Items.WIND_CHARGE) && !AuraModule.W()) {
         Vec3 var1 = h6.p(this.P.getYRot(), this.P.getXRot());
         h6.n var2 = h6.v(this.R(), this.P.getEyePosition(), this.E(), var1, this.o());
         if (this.i()) {
            this.T = var2;
            return this.y.q(this.M) == this.p.getValue().intValue() && this.j(this.W, this.Y);
         } else if (var2 != null && !(var2.reliability() < 0.6)) {
            this.T = var2;
            return true;
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   private void F() {
      h6.n var1 = this.T;
      long var2 = this.y.q(this.M);
      if (!this.G(Items.WIND_CHARGE)) {
         this.S("The wind charge throw was rejected");
      } else {
         this.V = var1;
         this.n = this.M;
         this.y.U();
         this.R(var1);
         this.r = "Wind charge thrown";
         if (var1 == null) {
            r4.V("PearlCatch", "Wind charge thrown {} ticks after the pearl; no catch expected along this look", var2);
         } else {
            r4.V(
               "PearlCatch",
               "Wind charge thrown {} ticks after the pearl at yaw {} pitch {}; expected catch on step {} at {} ({}/{} samples){}",
               var2,
               Q(this.P.getYRot()),
               Q(this.P.getXRot()),
               var1.nominal().tick(),
               r(var1.nominal().point()),
               var1.score(),
               var1.samples(),
               var1.nominal().burst() ? ", charge bursts on you" : ""
            );
         }
      }
   }

   private void R(h6.n var1) {
      if (this.q.size() >= 4) {
         this.q.remove(0);
      }

      int var2 = (var1 == null ? 60 : var1.nominal().tick()) + this.R() + 20;
      AutoPearlCatchModule.o var3 = new AutoPearlCatchModule.o(new HashSet<>(this.s), this.v, var1, this.U, this.M, this.M + var2);
      var3.T = this.c;
      this.q.add(var3);
      this.c = null;
      this.s.clear();
      this.U = null;
   }

   private void Z() {
      Iterator var1 = this.q.iterator();

      while (var1.hasNext()) {
         AutoPearlCatchModule.o var2 = (AutoPearlCatchModule.o)var1.next();
         if (var2.T == null) {
            var2.T = this.d(var2.o);
         }

         ThrownEnderpearl var3 = var2.T;
         if (var2.W < 0L && var3 != null && (var3.isRemoved() || mc.level.getEntity(var3.getId()) != var3)) {
            var2.W = this.M;
         }

         if (var2.I == null && !var2.y && var2.W < 0L && var3 != null) {
            var2.I = h6.k(new h6.g(var3.position(), var3.getDeltaMovement(), 1 + var3.tickCount), this.o()).K();
            var2.y = (boolean)1;
         }

         if (var2.W >= 0L && this.M > var2.W) {
            this.D(var2);
            var1.remove();
         } else if (this.M - var2.G >= 0L) {
            String var4 = var3 == null ? "Pearl not seen; the outcome is unknown" : "No catch observed in time";
            r4.V("PearlCatch", "{}, {} ticks after the wind charge", var4, this.M - var2.V);
            if (!this.y.R()) {
               this.r = var4;
            }

            var1.remove();
         }
      }
   }

   private ThrownEnderpearl d(Set<Integer> var1) {
      for (Entity var3 : mc.level.entitiesForRendering()) {
         if (var3 instanceof ThrownEnderpearl var4
            && !var4.isRemoved()
            && var4.getOwner() == this.P
            && !var1.contains(var4.getId())
            && this.q.stream().noneMatch(var1x -> var1x.T == var4)) {
            return var4;
         }
      }

      return null;
   }

   private void D(AutoPearlCatchModule.o var1) {
      Vec3 var2 = this.P.position();
      Vec3 var3 = var1.e == null ? null : var1.e.nominal().point();
      double var4 = var3 == null ? Double.NaN : var2.distanceTo(var3);
      int var6 = var2.distanceTo(var1.q) > 1.0 ? 1 : 0;
      String var7;
      if (var1.I != null && var2.distanceTo(var1.I) <= 1.0) {
         var7 = "Missed: the pearl landed";
      } else if (var6 == 0 || var3 != null && !(var4 <= 3.0)) {
         if (var6 != 0) {
            var7 = String.format(
               Locale.ROOT,
               "Caught %.1f blocks out, %.1f up, %.1f blocks from the predicted point (a tick of server timing)",
               var2.distanceTo(var1.q),
               var2.y - var1.q.y,
               var4
            );
         } else {
            var7 = "No teleport: the pearl ended near where you threw it";
         }
      } else {
         var7 = String.format(Locale.ROOT, "Caught %.1f blocks out, %.1f up", var2.distanceTo(var1.q), var2.y - var1.q.y);
      }

      r4.V(
         "PearlCatch",
         "{}; now at {}, expected {} ({} blocks off), {} ticks after the wind charge",
         var7,
         r(var2),
         var3 == null ? "unknown" : r(var3),
         Double.isNaN(var4) ? "?" : Q(var4),
         this.M - var1.V
      );
      if (!this.y.R()) {
         this.r = var7;
      }
   }

   private List<h6.q> R() {
      if (this.g != this.M || this.l == null) {
         this.l = this.k(this.i, this.L, this.F, (int)Math.max(1L, this.y.q(this.M)));
         this.g = this.M;
      }

      return this.l;
   }

   private void T() {
      if (this.c == null) {
         for (Entity var2 : mc.level.entitiesForRendering()) {
            if (var2 instanceof ThrownEnderpearl var3 && !var3.isRemoved() && var3.getOwner() == this.P && !this.s.contains(var3.getId())) {
               this.c = var3;
               r4.g("PearlCatch", "Own pearl seen {} ticks after the throw, {} client ticks old", this.y.q(this.M), var3.tickCount);
               return;
            }
         }
      }
   }

   private String r() {
      if (!d8.a() || mc.gui.screen() != null) {
         return "Paused: a screen is open";
      } else if (!mc.isWindowActive()) {
         return "Paused: the window lost focus";
      } else if (rr.u() != rr.f.H && !rr.j(rr.f.H)) {
         return "Paused: Freecam or Freelook holds the view";
      } else if (this.P.isPassenger()) {
         return "Not while riding";
      } else {
         return !this.P.isEyeInFluid(FluidTags.WATER) && !this.P.isInLava() ? null : "Not with your head in water or lava";
      }
   }

   private String B() {
      if (this.P.isUsingItem()) {
         return "Stop using your item first";
      } else if (AuraModule.W()) {
         return "Paused while Kill Aura lowers your shield";
      } else if (com.larpclient.m.Q(Items.ENDER_PEARL, true) == -1) {
         return "No ender pearls in your hotbar or hands";
      } else if (com.larpclient.m.Q(Items.WIND_CHARGE, true) == -1) {
         return "No wind charges in your hotbar or hands";
      } else {
         return !this.P.getAbilities().invulnerable && this.P.getHealth() + this.P.getAbsorptionAmount() <= 5.0 ? "The 5 HP pearl hit would be lethal" : null;
      }
   }

   private boolean n(Item var1) {
      int var2 = com.larpclient.m.Q(var1, true);
      if (var2 == -1) {
         return false;
      }

      ItemStack var3 = var2 == -2 ? this.P.getOffhandItem() : this.P.getInventory().getItem(var2);
      return var3.isItemEnabled(this.R.enabledFeatures()) && !this.P.getCooldowns().isOnCooldown(var3);
   }

   private boolean K(Item var1) {
      int var2 = com.larpclient.m.Q(var1, true);
      return var2 == -2 || var2 == this.P.getInventory().getSelectedSlot() || var2 >= 0 && o_.N(this);
   }

   private boolean G(Item var1) {
      try {
         return this.Z(var1);
      } finally {
         this.U();
      }
   }

   private boolean Z(Item var1) {
      int var2 = com.larpclient.m.Q(var1, true);
      if (var2 == -1) {
         return false;
      }

      InteractionHand var3 = var2 == -2 ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND;
      if (var2 >= 0 && var2 != this.P.getInventory().getSelectedSlot() && !this.H.s(this.P, var2)) {
         return false;
      }

      boolean var4 = mc.gameMode.useItem(this.P, var3).consumesAction();
      if (var4) {
         hp.Q(this.P, var3);
         ((qa1ad45ac3ead4599)mc).larpUseDelay(4);
      }

      return var4;
   }

   private void U() {
      if (this.H.K()) {
         int var1 = this.P == null ? -1 : this.P.getInventory().getSelectedSlot();
         this.H.m(getMc(), this.e.getValue());
         if (this.P != null && this.P == mc.player && mc.gameMode != null && var1 != this.P.getInventory().getSelectedSlot()) {
            ((q143ef5d8ac4bd5b8)mc.gameMode).larpSyncCarriedItem();
         }
      }
   }

   private float[] n() {
      return !rr.e() ? new float[]{this.P.getYRot(), this.P.getXRot()} : rr.l(rr.j(), rr.T(), mc.options.getCameraType().isMirrored());
   }

   private void S(float var1, float var2) {
      rr.W(rr.f.H);
      if (rr.u() == rr.f.H) {
         float[] var3 = this.b.G(this.P.getYRot(), this.P.getXRot(), var1, var2, this.S.getValue(), this.E.getValue());
         ep.t(this, ep.z.v, var3[0], var3[1], 1.0);
      }
   }

   private boolean j(float var1, float var2) {
      return this.r(var1, var2, l0.t(this.E.getValue(), 0.05F, le.e()));
   }

   private boolean r(float var1, float var2, float var3) {
      return Math.abs(Mth.wrapDegrees(this.P.getYRot() - var1)) <= var3 && Math.abs(this.P.getXRot() - var2) <= var3;
   }

   private Vec3 E() {
      return this.P.onGround() ? new Vec3(this.z.x, 0.0, this.z.z) : this.z;
   }

   private int R() {
      ClientPacketListener var1 = mc.getConnection();
      PlayerInfo var2 = var1 != null && this.P != null ? var1.getPlayerInfo(this.P.getUUID()) : null;
      return var2 == null ? 0 : Math.max(0, Math.round(var2.getLatency() / 50.0F));
   }

   private h6.z o() {
      return new com.larpclient.l3(this.R, this.P);
   }

   private boolean w() {
      return this.G.i("Up");
   }

   private boolean i() {
      return this.a.i("Fixed Delay");
   }

   private boolean W() {
      return this.a.i("Instant");
   }

   private static String Q(double var0) {
      return String.format(Locale.ROOT, "%.2f", var0);
   }

   private static String r(Vec3 var0) {
      return String.format(Locale.ROOT, "(%.1f, %.1f, %.1f)", var0.x, var0.y, var0.z);
   }

   private void C(String var1) {
      this.r = var1;
      r4.V("PearlCatch", "Catch refused: {}", var1);
   }

   private void S(String var1) {
      boolean var2 = this.y.R();
      d8.K(this);
      this.U();
      this.k();
      if (var2) {
         this.r = var1;
         r4.V("PearlCatch", "Catch cancelled: {}", var1);
      }
   }

   private void k() {
      this.y.O();
      rr.L(rr.f.H, this.b, this.O.getValue(), this.E.getValue());
      this.s.clear();
      this.c = null;
      this.V = this.T = null;
      this.l = null;
      this.g = -1L;
      this.U = null;
   }

   @Override
   public void onEnable() {
      this.P = null;
      this.R = null;
      this.k = null;
      this.q.clear();
      this.n = Long.MIN_VALUE;
      this.k();
      this.r = this.Q.getValue() < 0 ? "Set a Catch Bind" : "Ready";
      r4.V(
         "PearlCatch",
         "Enabled; direction={}, timing={}, delay={}, turn speed={}, return speed={}, humanize={}",
         this.G.getValue(),
         this.a.getValue(),
         this.p.getValue().intValue(),
         this.S.getValue().intValue(),
         this.O.getValue().intValue(),
         this.E.getValue()
      );
   }

   @Override
   public void onDisable() {
      this.S("Disabled");
      this.q.clear();
      this.P = null;
      this.R = null;
      r4.V("PearlCatch", "Disabled; pending throws and aim released");
   }

   private static final class o {
      final Set<Integer> o;
      final Vec3 q;
      final h6.n e;
      final long V;
      final long G;
      ThrownEnderpearl T;
      Vec3 I;
      boolean y;
      long W = -1L;

      o(Set<Integer> var1, Vec3 var2, h6.n var3, Vec3 var4, long var5, long var7) {
         this.o = var1;
         this.q = var2;
         this.e = var3;
         this.I = var4;
         this.V = var5;
         this.G = var7;
      }
   }
}
