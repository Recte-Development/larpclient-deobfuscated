package com.larpclient.module;

import com.larp.obf.qbac0b262827a15ac.q099a584e4baa1bb9;
import com.larp.obf.qbac0b262827a15ac.qb131e08b200c8a57;
import com.larpclient._8;
import com.larpclient._d;
import com.larpclient._m;
import com.larpclient.h_;
import com.larpclient.m0;
import com.larpclient.m9;
import com.larpclient.mt;
import com.larpclient.mw;
import com.larpclient.po;
import com.larpclient.ti;
import com.larpclient.tq;
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
   private static final int d = 20;
   private static final int m = 20;
   private static final int a = 20;
   private static final int B = 3;
   private static final int k = 4;
   private static final double i = 0.6;
   private static final double z = 5.0;
   private static final float s = 0.05F;
   public static final float b = 2.5F;
   private static AutoPearlCatchModule h;
   private final dv W = this.addSetting(
      new dv("Catch Bind", "Press for one pearl catch; pressing again while it throws cancels it. Separate from the module toggle", -1)
   );
   private final d6 y = this.addSetting(new d6("Direction", "Up throws straight up for height; Look throws where you aim for distance", "Up", "Up", "Look"));
   private final d6 c = this.addSetting(
      new d6(
         "Timing",
         "Instant throws both in one tick for a catch a few blocks out; Solved aims the wind charge a tick or more later for a farther catch; Fixed Delay throws it along the same look after Delay",
         "Instant",
         "Instant",
         "Solved",
         "Fixed Delay"
      )
   );
   private final d9 Q = this.addSetting(
      new d9("Delay", "Ticks from the pearl to the wind charge; same-look catches work near straight up", 2.0, 1.0, 10.0, 1.0)
   );
   private final d9 P = this.addSetting(
      new d9(
         "Turn Speed", "Maximum player turn in degrees per tick; your camera stays on your look. 180 with Humanize off turns at once", 90.0, 10.0, 180.0, 5.0
      )
   );
   private final dh O = this.addSetting(new dh("Swap Back", "Return to your previous hotbar slot after each throw", true));
   private final d9 H = this.addSetting(
      new d9("Return Speed", "Most the player turns per tick back to your look after the last throw. 180 returns at once", 40.0, 10.0, 180.0, 5.0)
   );
   private final dh G = this.addSetting(
      new dh(
         "Humanize", "Turn like a hand on a mouse: speed up, ease in, drift a little and move in whole sensitivity steps. Off turns in straight lines", true
      )
   );
   private final h_ R = new h_();
   private final ti X = new ti(this);
   private final mw T = new mw();
   private final Set<Integer> t = new HashSet<>();
   private final List<AutoPearlCatchModule.x> v = new ArrayList<>();
   private LocalPlayer r;
   private ClientLevel Y;
   private long w;
   private long q = Long.MIN_VALUE;
   private long J = -1L;
   private float[] p;
   private float j;
   private float D;
   private List<po.f> L;
   private Vec3 x;
   private Vec3 g = Vec3.ZERO;
   private Vec3 n;
   private Vec3 V = Vec3.ZERO;
   private Vec3 I = Vec3.ZERO;
   private Vec3 C = Vec3.ZERO;
   private Vec3 F = Vec3.ZERO;
   private float N;
   private float M;
   private ThrownEnderpearl o;
   private po.n A;
   private po.n Z;
   private String K = "Set a Catch Bind";

   public AutoPearlCatchModule() {
      super("Auto Pearl Catch", "Throw a pearl, then a wind charge it hits in flight, to teleport mid-air", Category.MOVEMENT, -1);
      h = this;
      this.Q.H(this::F);
      this.y.h(var1 -> this.y("Direction changed"));
      this.c.h(var1 -> this.y("Timing changed"));
      this.W.h(var1 -> this.y("Bind changed"));
   }

   public static AutoPearlCatchModule D() {
      return h;
   }

   public List<df<?>> V() {
      return List.of(this.W, this.y, this.c, this.Q);
   }

   public List<df<?>> Q() {
      return List.of(this.P, this.H, this.G, this.O);
   }

   public h_ c() {
      return this.R;
   }

   public double o() {
      return this.H.P();
   }

   public boolean m() {
      return this.G.P();
   }

   @Override
   public Module.f getTickPhase() {
      return Module.f.Z;
   }

   @Override
   public Set<dm> getNetworkImpacts() {
      return Set.of(dm.z, dm.j);
   }

   @Override
   public String getStatus() {
      return this.isEnabled() ? this.K : "";
   }

   public boolean a() {
      return !this.isEnabled()
         ? false
         : this.isRuntimeActive() && this.r != null && this.r == mc.player && this.Y == mc.level && (this.T.t() || this.w == this.q);
   }

   public static void v(int var0) {
      if (h != null && h.isEnabled()) {
         AutoPearlCatchModule var1 = h;
         if (var1 != null && var1.isRuntimeActive() && var0 > 0 && var0 == var1.W.P()) {
            if (!var1.T.Z()) {
               var1.y("Cancelled with Catch Bind");
            }
         }
      }
   }

   @Override
   public void onTick() {
      if (!this.isInGame()) {
         this.y("Waiting for a world");
         this.v.clear();
      } else {
         if (this.r != mc.player || this.Y != mc.level) {
            float[] var15;
            label69: {
               if (!this.T.t()) {
                  long var21;
                  int var10000 = (var21 = this.w - this.q) == 0L ? 0 : (var21 < 0L ? -1 : 1);
                  boolean var10 = false;
                  int var9 = var10000;
                  int var14 = Integer.signum(var9);
                  if ((var14 | -var14) >>> 31 != 0) {
                     var15 = null;
                     break label69;
                  }
               }

               var15 = this.p;
            }

            float[] var1 = var15;
            this.y("World changed");
            this.v.clear();
            this.r = mc.player;
            this.Y = mc.level;
            this.x = null;
            if (var1 != null) {
               this.r.setYRot(var1[0]);
               this.r.setXRot(var1[1]);
               this.r.yRotO = var1[0];
               this.r.xRotO = var1[1];
               mt.K("PearlCatch", "World changed during a catch; your look (yaw {} pitch {}) restored", k(var1[0]), k(var1[1]));
            }
         }

         this.w++;
         if (tq.l() == tq.y.N) {
            this.p = tq.i(tq.J(), tq.F(), mc.options.getCameraType().isMirrored());
         }

         Vec3 var13;
         Vec3 var17;
         label63: {
            var13 = this.r.position();
            if (this.x != null) {
               double var22;
               int var10001 = (var22 = var13.distanceToSqr(this.x) - 16.0) == 0.0 ? 0 : (var22 < 0.0 ? -1 : 1);
               boolean var12 = false;
               int var11 = var10001;
               if ((-Integer.signum(var11) >>> 31 ^ 1) != 0) {
                  var17 = var13.subtract(this.x);
                  break label63;
               }
            }

            var17 = Vec3.ZERO;
         }

         this.g = var17;
         this.x = var13;
         this.Y();
         byte var2 = this.T.D();
         mw.e var3 = this.T.q();
         if (var3 == mw.e.T) {
            boolean var8 = false;
            byte var7 = var2;
            int var16 = Integer.signum(var7);
            if (((var16 | -var16) >>> 31 ^ 1) == 0 && !this.T.h()) {
               this.j = this.D = 0.0F;
               this.T.s(this.w);
            }

            if (this.T.h()) {
               this.i();
            }
         } else {
            String var4 = this.y();
            if (var4 != null) {
               this.y(var4);
            } else if (this.T.J(this.w)) {
               String var18;
               if (var3 == mw.e.d) {
                  var18 = "The view did not reach the throw angle in time";
               } else {
                  byte var19 = this.F();
                  boolean var6 = false;
                  byte var5 = var19;
                  int var20 = Integer.signum(var5);
                  var18 = ((var20 | -var20) >>> 31 ^ 1) == 0
                     ? "The wind charge could not be thrown on its tick"
                     : "No reliable catch before the pearl came down";
               }

               this.y(var18);
            } else {
               if (var3 == mw.e.d) {
                  this.t();
               } else {
                  this.L();
               }
            }
         }
      }
   }

   private void i() {
      String var1 = this.y();
      if (var1 == null) {
         var1 = this.o();
      }

      if (var1 != null) {
         this.T.k();
         this.M(var1);
      } else {
         int var2 = this.P();
         if (var2 > 0) {
            if (var2 <= 30 && !this.T.V(this.w)) {
               this.K = String.format(Locale.ROOT, "Catch in %.2f s (cooldown)", var2 / 20.0);
            } else {
               this.T.k();
               this.M(String.format(Locale.ROOT, "Ender pearl or wind charge is cooling down (%.1f s left)", var2 / 20.0));
            }
         } else {
            this.T.k();
            this.W();
         }
      }
   }

   private int P() {
      float var1 = this.S(Items.ENDER_PEARL);
      float var2 = this.S(Items.WIND_CHARGE);
      int var3 = Math.max(s(var1, this.j, Items.ENDER_PEARL), s(var2, this.D, Items.WIND_CHARGE));
      this.j = var1;
      this.D = var2;
      return var3;
   }

   private float S(Item var1) {
      int var2 = _d.B(var1, true);
      if (var2 == -1) {
         return 0.0F;
      }

      ItemStack var3 = var2 == -2 ? this.r.getOffhandItem() : this.r.getInventory().getItem(var2);
      return this.r.getCooldowns().getCooldownPercent(var3, 0.0F);
   }

   private static int s(float var0, float var1, Item var2) {
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

   private void W() {
      String var1 = this.y();
      if (var1 == null) {
         var1 = this.o();
      }

      if (var1 != null) {
         this.M(var1);
      } else {
         float[] var2 = this.I();
         float var3 = var2[0];
         float var4 = var2[1];
         float var5 = this.q() ? -90.0F : var4;
         Vec3 var6 = this.r.getEyePosition();
         po.n var7;
         if (this.C()) {
            po.q var8 = po.Y(var6, var3, var5, this.H(), this.J(), 0.6);
            if (var8 == null) {
               this.M(this.q() ? "No instant catch: clear the space above you" : "No instant catch along this look; clear the space ahead");
               return;
            }

            var5 = var8.pitch();
            var7 = var8.plan();
         } else {
            Vec3 var11 = po.y(var3, var5);
            var7 = this.F() ? po.J(this.H(var6, var11, this.H(), this.Q.P().intValue()), var6, this.H(), var11, this.J()) : this.a(var6, var11, this.H());
            if (var7 == null || var7.reliability() < 0.6) {
               this.M(this.w(var7));
               return;
            }
         }

         this.N = var3;
         this.M = var5;
         this.A = var7;
         this.t.clear();

         for (Entity var9 : mc.level.entitiesForRendering()) {
            if (var9 instanceof ThrownEnderpearl var10 && var10.getOwner() == this.r) {
               this.t.add(var10.getId());
            }
         }

         this.T.r(this.w, 20);
         this.R.b();
         this.K = this.x(var3, var5) ? "Throwing" : "Turning to throw";
         mt.K(
            "PearlCatch",
            "Catch started; direction={}, timing={}, pitch {}{}, forecast catch on step {} at {} ({}/{} samples)",
            this.y.P(),
            this.c.P(),
            k(var5),
            !this.q() && var5 < var4 - 0.5F ? " raised from " + k(var4) : "",
            var7.nominal().tick(),
            I(var7.nominal().point()),
            var7.score(),
            var7.samples()
         );
         this.t();
      }
   }

   private po.n a(Vec3 var1, Vec3 var2, Vec3 var3) {
      po.n var4 = null;

      for (int var5 = 1; var5 <= 20; var5++) {
         po.n var6 = po.l(this.H(var1, var2, var3, var5), var1, var3, this.J());
         if (var6 != null && var6.reliability() >= 0.6) {
            return var6;
         }

         if (var6 != null && (var4 == null || var6.score() > var4.score())) {
            var4 = var6;
         }
      }

      return var4;
   }

   private List<po.f> H(Vec3 var1, Vec3 var2, Vec3 var3, int var4) {
      return po.a(var1, var2, var3, var4, this.J());
   }

   private String w(po.n var1) {
      if (this.F() && !this.q()) {
         return "Fixed Delay only catches near straight up; use Instant or Solved for Look";
      } else if (var1 == null) {
         return this.q() ? "No catch: the pearl would come down first, so clear the space above you" : "No catch at this angle; aim higher or use Instant";
      } else {
         return String.format(
            Locale.ROOT,
            "Only %d of %d samples catch here; %s",
            var1.score(),
            var1.samples(),
            this.F() ? "try another Delay or Instant" : (this.q() ? "clear the space above you" : "aim higher or use Instant")
         );
      }
   }

   private void t() {
      if (_m.f(this)) {
         this.y(this.N, this.M);
      }

      _m.F(this, this.C() ? this::J : this::G, this::A);
   }

   private boolean A() {
      if (!this.isRuntimeActive()
         || this.T.q() != mw.e.d
         || this.y() != null
         || (this.C() ? !this.U(this.N, this.M, 2.5F) : !this.x(this.N, this.M))
         || !this.L(Items.ENDER_PEARL)
         || !this.L(Items.WIND_CHARGE)
         || !this.R(Items.ENDER_PEARL)
         || AuraModule.i()) {
         return false;
      } else if (!this.C()) {
         return true;
      } else if (!this.R(Items.WIND_CHARGE)) {
         return false;
      } else {
         Vec3 var1 = this.r.getEyePosition();
         Vec3 var2 = po.y(this.r.getYRot(), this.r.getXRot());
         po.n var3 = po.J(po.v(var1, var2, this.H(), this.J()), var1, this.H(), var2, this.J());
         if (var3 != null && !(var3.reliability() < 0.6)) {
            this.Z = var3;
            return true;
         } else {
            return false;
         }
      }
   }

   private void J() {
      po.n var1 = this.Z;
      this.V = this.r.position();
      byte var2 = 0;
      byte var3 = 0;

      try {
         var7 = this.B(Items.ENDER_PEARL);
         if (var7) {
            var3 = this.B(Items.WIND_CHARGE);
         }
      } finally {
         this.C();
      }

      if (!var7) {
         this.y("The pearl throw was rejected");
      } else if (var3 == 0) {
         this.y("The wind charge was rejected after the pearl, so the pearl flies on");
      } else {
         this.A = var1;
         this.q = this.w;
         this.T.X(this.w, 1);
         this.T.j();
         this.w(var1);
         this.K = "Pearl and wind charge thrown together";
         mt.K(
            "PearlCatch",
            "Instant pair thrown at yaw {} pitch {} from {} after a turn of {}; expected catch on step {} at {} ({}/{} samples)",
            k(this.r.getYRot()),
            k(this.r.getXRot()),
            I(this.r.getEyePosition()),
            this.R.H(),
            var1 == null ? "?" : var1.nominal().tick(),
            var1 == null ? "?" : I(var1.nominal().point()),
            var1 == null ? 0 : var1.score(),
            var1 == null ? 0 : var1.samples()
         );
      }
   }

   private void G() {
      this.V = this.r.position();
      Vec3 var1 = this.r.getEyePosition();
      Vec3 var2 = this.H();
      if (!this.X(Items.ENDER_PEARL)) {
         this.y("The pearl throw was rejected");
      } else {
         this.N = this.r.getYRot();
         this.M = this.r.getXRot();
         this.I = var1;
         this.C = po.y(this.N, this.M);
         this.F = var2;
         this.T.X(this.w, this.F() ? this.Q.P().intValue() + 1 : 21);
         this.K = this.F() ? "Pearl thrown; wind charge in " + this.Q.P().intValue() + " ticks" : "Pearl thrown; aiming the wind charge";
         mt.K("PearlCatch", "Pearl thrown at yaw {} pitch {} from {} after a turn of {}", k(this.N), k(this.M), I(var1), this.R.H());
         this.R.b();
      }
   }

   private void L() {
      this.o();
      if (this.o != null && this.o.isRemoved()) {
         this.y("The pearl came down before the wind charge");
      } else if (this.F()) {
         if (_m.f(this)) {
            this.y(this.N, this.M);
         }

         if (this.T.l(this.w) >= this.Q.P().intValue()) {
            _m.F(this, this::m, this::B);
         }
      } else {
         List var1 = this.d();
         if (this.n == null) {
            this.n = ((po.f)var1.get(0)).m();
         }

         po.n var2 = po.l(var1, this.r.getEyePosition(), this.H(), this.J());
         if (var2 != null && !(var2.reliability() < 0.6)) {
            this.A = var2;
            float[] var3 = m9.Z(Vec3.ZERO, var2.direction());
            this.N = var3[0];
            this.M = Mth.clamp(var3[1], -90.0F, 90.0F);
            if (_m.f(this)) {
               this.y(this.N, this.M);
            }

            _m.F(this, this::m, this::B);
            this.K = "Aiming wind charge";
         } else {
            this.K = "Waiting for a reliable catch line";
         }
      }
   }

   private boolean B() {
      if (this.isRuntimeActive() && this.T.q() == mw.e.J && this.y() == null && this.L(Items.WIND_CHARGE) && this.R(Items.WIND_CHARGE) && !AuraModule.i()) {
         Vec3 var1 = po.y(this.r.getYRot(), this.r.getXRot());
         po.n var2 = po.J(this.d(), this.r.getEyePosition(), this.H(), var1, this.J());
         if (this.F()) {
            this.Z = var2;
            return this.T.l(this.w) == this.Q.P().intValue() && this.x(this.N, this.M);
         } else if (var2 != null && !(var2.reliability() < 0.6)) {
            this.Z = var2;
            return true;
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   private void m() {
      po.n var1 = this.Z;
      long var2 = this.T.l(this.w);
      if (!this.X(Items.WIND_CHARGE)) {
         this.y("The wind charge throw was rejected");
      } else {
         this.A = var1;
         this.q = this.w;
         this.T.j();
         this.w(var1);
         this.K = "Wind charge thrown";
         if (var1 == null) {
            mt.K("PearlCatch", "Wind charge thrown {} ticks after the pearl; no catch expected along this look", var2);
         } else {
            mt.K(
               "PearlCatch",
               "Wind charge thrown {} ticks after the pearl at yaw {} pitch {}; expected catch on step {} at {} ({}/{} samples){}",
               var2,
               k(this.r.getYRot()),
               k(this.r.getXRot()),
               var1.nominal().tick(),
               I(var1.nominal().point()),
               var1.score(),
               var1.samples(),
               var1.nominal().burst() ? ", charge bursts on you" : ""
            );
         }
      }
   }

   private void w(po.n var1) {
      if (this.v.size() >= 4) {
         this.v.remove(0);
      }

      int var2 = (var1 == null ? 60 : var1.nominal().tick()) + this.h() + 20;
      AutoPearlCatchModule.x var3 = new AutoPearlCatchModule.x(new HashSet<>(this.t), this.V, var1, this.n, this.w, this.w + var2);
      var3.z = this.o;
      this.v.add(var3);
      this.o = null;
      this.t.clear();
      this.n = null;
   }

   private void Y() {
      Iterator var1 = this.v.iterator();

      while (var1.hasNext()) {
         AutoPearlCatchModule.x var2 = (AutoPearlCatchModule.x)var1.next();
         if (var2.z == null) {
            var2.z = this.z(var2.i);
         }

         ThrownEnderpearl var3 = var2.z;
         if (var2.j < 0L && var3 != null && (var3.isRemoved() || mc.level.getEntity(var3.getId()) != var3)) {
            var2.j = this.w;
         }

         if (var2.T == null && !var2.y && var2.j < 0L && var3 != null) {
            var2.T = po.v(new po.a(var3.position(), var3.getDeltaMovement(), 1 + var3.tickCount), this.J()).m();
            var2.y = (boolean)1;
         }

         if (var2.j >= 0L && this.w > var2.j) {
            this.f(var2);
            var1.remove();
         } else if (this.w - var2.e >= 0L) {
            String var4 = var3 == null ? "Pearl not seen; the outcome is unknown" : "No catch observed in time";
            mt.K("PearlCatch", "{}, {} ticks after the wind charge", var4, this.w - var2.U);
            if (!this.T.t()) {
               this.K = var4;
            }

            var1.remove();
         }
      }
   }

   private ThrownEnderpearl z(Set<Integer> var1) {
      for (Entity var3 : mc.level.entitiesForRendering()) {
         if (var3 instanceof ThrownEnderpearl var4
            && !var4.isRemoved()
            && var4.getOwner() == this.r
            && !var1.contains(var4.getId())
            && this.v.stream().noneMatch(var1x -> var1x.z == var4)) {
            return var4;
         }
      }

      return null;
   }

   private void f(AutoPearlCatchModule.x var1) {
      Vec3 var2 = this.r.position();
      Vec3 var3 = var1.J == null ? null : var1.J.nominal().point();
      double var4 = var3 == null ? Double.NaN : var2.distanceTo(var3);
      int var6 = var2.distanceTo(var1.l) > 1.0 ? 1 : 0;
      String var7;
      if (var1.T != null && var2.distanceTo(var1.T) <= 1.0) {
         var7 = "Missed: the pearl landed";
      } else if (var6 == 0 || var3 != null && !(var4 <= 3.0)) {
         if (var6 != 0) {
            var7 = String.format(
               Locale.ROOT,
               "Caught %.1f blocks out, %.1f up, %.1f blocks from the predicted point (a tick of server timing)",
               var2.distanceTo(var1.l),
               var2.y - var1.l.y,
               var4
            );
         } else {
            var7 = "No teleport: the pearl ended near where you threw it";
         }
      } else {
         var7 = String.format(Locale.ROOT, "Caught %.1f blocks out, %.1f up", var2.distanceTo(var1.l), var2.y - var1.l.y);
      }

      mt.K(
         "PearlCatch",
         "{}; now at {}, expected {} ({} blocks off), {} ticks after the wind charge",
         var7,
         I(var2),
         var3 == null ? "unknown" : I(var3),
         Double.isNaN(var4) ? "?" : k(var4),
         this.w - var1.U
      );
      if (!this.T.t()) {
         this.K = var7;
      }
   }

   private List<po.f> d() {
      if (this.J != this.w || this.L == null) {
         this.L = this.H(this.I, this.C, this.F, (int)Math.max(1L, this.T.l(this.w)));
         this.J = this.w;
      }

      return this.L;
   }

   private void o() {
      if (this.o == null) {
         for (Entity var2 : mc.level.entitiesForRendering()) {
            if (var2 instanceof ThrownEnderpearl var3 && !var3.isRemoved() && var3.getOwner() == this.r && !this.t.contains(var3.getId())) {
               this.o = var3;
               mt.U("PearlCatch", "Own pearl seen {} ticks after the throw, {} client ticks old", this.T.l(this.w), var3.tickCount);
               return;
            }
         }
      }
   }

   private String y() {
      if (!_m.z() || mc.gui.screen() != null) {
         return "Paused: a screen is open";
      } else if (!mc.isWindowActive()) {
         return "Paused: the window lost focus";
      } else if (tq.l() != tq.y.N && !tq.y(tq.y.N)) {
         return "Paused: Freecam or Freelook holds the view";
      } else if (this.r.isPassenger()) {
         return "Not while riding";
      } else {
         return !this.r.isEyeInFluid(FluidTags.WATER) && !this.r.isInLava() ? null : "Not with your head in water or lava";
      }
   }

   private String o() {
      if (this.r.isUsingItem()) {
         return "Stop using your item first";
      } else if (AuraModule.i()) {
         return "Paused while Kill Aura lowers your shield";
      } else if (_d.B(Items.ENDER_PEARL, true) == -1) {
         return "No ender pearls in your hotbar or hands";
      } else if (_d.B(Items.WIND_CHARGE, true) == -1) {
         return "No wind charges in your hotbar or hands";
      } else {
         return !this.r.getAbilities().invulnerable && this.r.getHealth() + this.r.getAbsorptionAmount() <= 5.0 ? "The 5 HP pearl hit would be lethal" : null;
      }
   }

   private boolean L(Item var1) {
      int var2 = _d.B(var1, true);
      if (var2 == -1) {
         return false;
      }

      ItemStack var3 = var2 == -2 ? this.r.getOffhandItem() : this.r.getInventory().getItem(var2);
      return var3.isItemEnabled(this.Y.enabledFeatures()) && !this.r.getCooldowns().isOnCooldown(var3);
   }

   private boolean R(Item var1) {
      int var2 = _d.B(var1, true);
      return var2 == -2 || var2 == this.r.getInventory().getSelectedSlot() || var2 >= 0 && ti.V(this);
   }

   private boolean X(Item var1) {
      try {
         return this.B(var1);
      } finally {
         this.C();
      }
   }

   private boolean B(Item var1) {
      int var2 = _d.B(var1, true);
      if (var2 == -1) {
         return false;
      }

      InteractionHand var3 = var2 == -2 ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND;
      if (var2 >= 0 && var2 != this.r.getInventory().getSelectedSlot() && !this.X.e(this.r, var2)) {
         return false;
      }

      boolean var4 = mc.gameMode.useItem(this.r, var3).consumesAction();
      if (var4) {
         _8.G(this.r, var3);
         ((qb131e08b200c8a57)mc).larpUseDelay(4);
      }

      return var4;
   }

   private void C() {
      if (this.X.k()) {
         int var1 = this.r == null ? -1 : this.r.getInventory().getSelectedSlot();
         this.X.W(getMc(), this.O.P());
         if (this.r != null && this.r == mc.player && mc.gameMode != null && var1 != this.r.getInventory().getSelectedSlot()) {
            ((q099a584e4baa1bb9)mc.gameMode).larpSyncCarriedItem();
         }
      }
   }

   private float[] I() {
      return !tq.q() ? new float[]{this.r.getYRot(), this.r.getXRot()} : tq.i(tq.J(), tq.F(), mc.options.getCameraType().isMirrored());
   }

   private void y(float var1, float var2) {
      tq.i(tq.y.N);
      if (tq.l() == tq.y.N) {
         float[] var3 = this.R.V(this.r.getYRot(), this.r.getXRot(), var1, var2, this.P.P(), this.G.P());
         m9.k(this, m9.s.H, var3[0], var3[1], 1.0);
      }
   }

   private boolean x(float var1, float var2) {
      return this.U(var1, var2, h_.r(this.G.P(), 0.05F, dj.t()));
   }

   private boolean U(float var1, float var2, float var3) {
      return Math.abs(Mth.wrapDegrees(this.r.getYRot() - var1)) <= var3 && Math.abs(this.r.getXRot() - var2) <= var3;
   }

   private Vec3 H() {
      return this.r.onGround() ? new Vec3(this.g.x, 0.0, this.g.z) : this.g;
   }

   private int h() {
      ClientPacketListener var1 = mc.getConnection();
      PlayerInfo var2 = var1 != null && this.r != null ? var1.getPlayerInfo(this.r.getUUID()) : null;
      return var2 == null ? 0 : Math.max(0, Math.round(var2.getLatency() / 50.0F));
   }

   private po.j J() {
      return new m0(this.Y, this.r);
   }

   private boolean q() {
      return this.y.S("Up");
   }

   private boolean F() {
      return this.c.S("Fixed Delay");
   }

   private boolean C() {
      return this.c.S("Instant");
   }

   private static String k(double var0) {
      return String.format(Locale.ROOT, "%.2f", var0);
   }

   private static String I(Vec3 var0) {
      return String.format(Locale.ROOT, "(%.1f, %.1f, %.1f)", var0.x, var0.y, var0.z);
   }

   private void M(String var1) {
      this.K = var1;
      mt.K("PearlCatch", "Catch refused: {}", var1);
   }

   private void y(String var1) {
      boolean var2 = this.T.t();
      _m.w(this);
      this.C();
      this.z();
      if (var2) {
         this.K = var1;
         mt.K("PearlCatch", "Catch cancelled: {}", var1);
      }
   }

   private void z() {
      this.T.F();
      tq.c(tq.y.N, this.R, this.H.P(), this.G.P());
      this.t.clear();
      this.o = null;
      this.A = this.Z = null;
      this.L = null;
      this.J = -1L;
      this.n = null;
   }

   @Override
   public void onEnable() {
      this.r = null;
      this.Y = null;
      this.x = null;
      this.v.clear();
      this.q = Long.MIN_VALUE;
      this.z();
      this.K = this.W.P() < 0 ? "Set a Catch Bind" : "Ready";
      mt.K(
         "PearlCatch",
         "Enabled; direction={}, timing={}, delay={}, turn speed={}, return speed={}, humanize={}",
         this.y.P(),
         this.c.P(),
         this.Q.P().intValue(),
         this.P.P().intValue(),
         this.H.P().intValue(),
         this.G.P()
      );
   }

   @Override
   public void onDisable() {
      this.y("Disabled");
      this.v.clear();
      this.r = null;
      this.Y = null;
      mt.K("PearlCatch", "Disabled; pending throws and aim released");
   }

   private static final class x {
      final Set<Integer> i;
      final Vec3 l;
      final po.n J;
      final long U;
      final long e;
      ThrownEnderpearl z;
      Vec3 T;
      boolean y;
      long j = -1L;

      x(Set<Integer> var1, Vec3 var2, po.n var3, Vec3 var4, long var5, long var7) {
         this.i = var1;
         this.l = var2;
         this.J = var3;
         this.T = var4;
         this.U = var5;
         this.e = var7;
      }
   }
}
