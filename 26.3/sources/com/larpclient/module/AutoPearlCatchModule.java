package com.larpclient.module;

import com.larp.obf.q41a5851bcf8e876a.q9b8c1cd7fe41113a;
import com.larp.obf.q41a5851bcf8e876a.qc90ed4dd4cb15de4;
import com.larpclient._b;
import com.larpclient._p;
import com.larpclient._q;
import com.larpclient.b6;
import com.larpclient.d8;
import com.larpclient.h8;
import com.larpclient.ij;
import com.larpclient.ne;
import com.larpclient.s6;
import com.larpclient.s9;
import com.larpclient.vi;
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
   private static final int b = 20;
   private static final int W = 20;
   private static final int Z = 20;
   private static final int l = 3;
   private static final int r = 4;
   private static final double F = 0.6;
   private static final double p = 5.0;
   private static final float u = 0.05F;
   public static final float Y = 2.5F;
   private static AutoPearlCatchModule M;
   private final x g = this.addSetting(
      new x("Catch Bind", "Press for one pearl catch; pressing again while it throws cancels it. Separate from the module toggle", -1)
   );
   private final o N = this.addSetting(new o("Direction", "Up throws straight up for height; Look throws where you aim for distance", "Up", "Up", "Look"));
   private final o K = this.addSetting(
      new o(
         "Timing",
         "Instant throws both in one tick for a catch a few blocks out; Solved aims the wind charge a tick or more later for a farther catch; Fixed Delay throws it along the same look after Delay",
         "Instant",
         "Instant",
         "Solved",
         "Fixed Delay"
      )
   );
   private final b o = this.addSetting(new b("Delay", "Ticks from the pearl to the wind charge; same-look catches work near straight up", 2.0, 1.0, 10.0, 1.0));
   private final b H = this.addSetting(
      new b(
         "Turn Speed", "Maximum player turn in degrees per tick; your camera stays on your look. 180 with Humanize off turns at once", 90.0, 10.0, 180.0, 5.0
      )
   );
   private final m t = this.addSetting(new m("Swap Back", "Return to your previous hotbar slot after each throw", true));
   private final b m = this.addSetting(
      new b("Return Speed", "Most the player turns per tick back to your look after the last throw. 180 returns at once", 40.0, 10.0, 180.0, 5.0)
   );
   private final m I = this.addSetting(
      new m("Humanize", "Turn like a hand on a mouse: speed up, ease in, drift a little and move in whole sensitivity steps. Off turns in straight lines", true)
   );
   private final _b G = new _b();
   private final s6 z = new s6(this);
   private final d8 q = new d8();
   private final Set<Integer> V = new HashSet<>();
   private final List<AutoPearlCatchModule.k> w = new ArrayList<>();
   private LocalPlayer v;
   private ClientLevel f;
   private long h;
   private long L = Long.MIN_VALUE;
   private long P = -1L;
   private float[] J;
   private float y;
   private float e;
   private List<s9.j> U;
   private Vec3 s;
   private Vec3 T = Vec3.ZERO;
   private Vec3 S;
   private Vec3 D = Vec3.ZERO;
   private Vec3 B = Vec3.ZERO;
   private Vec3 j = Vec3.ZERO;
   private Vec3 a = Vec3.ZERO;
   private float R;
   private float k;
   private ThrownEnderpearl A;
   private s9.q d;
   private s9.q c;
   private String E = "Set a Catch Bind";

   public AutoPearlCatchModule() {
      super("Auto Pearl Catch", "Throw a pearl, then a wind charge it hits in flight, to teleport mid-air", Category.MOVEMENT, -1);
      M = this;
      this.o.a(this::m);
      this.N.w(var1 -> this.f("Direction changed"));
      this.K.w(var1 -> this.f("Timing changed"));
      this.g.w(var1 -> this.f("Bind changed"));
   }

   public static AutoPearlCatchModule l() {
      return M;
   }

   public List<r<?>> J() {
      return List.of(this.g, this.N, this.K, this.o);
   }

   public List<r<?>> X() {
      return List.of(this.H, this.m, this.I, this.t);
   }

   public _b Z() {
      return this.G;
   }

   public double d() {
      return this.m.getValue();
   }

   public boolean G() {
      return this.I.getValue();
   }

   @Override
   public Module.a getTickPhase() {
      return Module.a.T;
   }

   @Override
   public Set<l> getNetworkImpacts() {
      return Set.of(com.larpclient.module.l.F, com.larpclient.module.l.k);
   }

   @Override
   public String getStatus() {
      return this.isEnabled() ? this.E : "";
   }

   public boolean j() {
      return !this.isEnabled()
         ? false
         : this.isRuntimeActive() && this.v != null && this.v == mc.player && this.f == mc.level && (this.q.H() || this.h == this.L);
   }

   public static void p(int var0) {
      if (M != null && M.isEnabled()) {
         AutoPearlCatchModule var1 = M;
         if (var1 != null && var1.isRuntimeActive() && var0 > 0 && var0 == var1.g.getValue()) {
            if (!var1.q.P()) {
               var1.f("Cancelled with Catch Bind");
            }
         }
      }
   }

   @Override
   public void onTick() {
      if (!this.isInGame()) {
         this.f("Waiting for a world");
         this.w.clear();
      } else {
         if (this.v != mc.player || this.f != mc.level) {
            float[] var15;
            label69: {
               if (!this.q.H()) {
                  long var21;
                  int var10000 = (var21 = this.h - this.L) == 0L ? 0 : (var21 < 0L ? -1 : 1);
                  boolean var12 = false;
                  int var11 = var10000;
                  int var14 = Integer.signum(var11);
                  if ((var14 | -var14) >>> 31 != 0) {
                     var15 = null;
                     break label69;
                  }
               }

               var15 = this.J;
            }

            float[] var1 = var15;
            this.f("World changed");
            this.w.clear();
            this.v = mc.player;
            this.f = mc.level;
            this.s = null;
            if (var1 != null) {
               this.v.setYRot(var1[0]);
               this.v.setXRot(var1[1]);
               this.v.yRotO = var1[0];
               this.v.xRotO = var1[1];
               _q.z("PearlCatch", "World changed during a catch; your look (yaw {} pitch {}) restored", J(var1[0]), J(var1[1]));
            }
         }

         this.h++;
         if (ij.L() == ij.r.J) {
            this.J = ij.p(ij.D(), ij.J(), mc.options.getCameraType().isMirrored());
         }

         Vec3 var13 = this.v.position();
         this.T = this.s != null && !(var13.distanceToSqr(this.s) > 16.0) ? var13.subtract(this.s) : Vec3.ZERO;
         this.s = var13;
         this.V();
         byte var2 = this.q.c();
         d8.g var3 = this.q.A();
         if (var3 == d8.g.E) {
            boolean var6 = false;
            byte var5 = var2;
            int var16 = Integer.signum(var5);
            if (((var16 | -var16) >>> 31 ^ 1) == 0) {
               int var17 = this.q.Z();
               boolean var8 = false;
               byte var7 = (byte)var17;
               var17 = Integer.signum(var7);
               if ((var17 | -var17) >>> 31 == 0) {
                  this.y = this.e = 0.0F;
                  this.q.f(this.h);
               }
            }

            int var19 = this.q.Z();
            boolean var10 = false;
            byte var9 = (byte)var19;
            var19 = Integer.signum(var9);
            if (((var19 | -var19) >>> 31 ^ 1) == 0) {
               this.L();
            }
         } else {
            String var4 = this.h();
            if (var4 != null) {
               this.f(var4);
            } else if (this.q.n(this.h)) {
               this.f(
                  var3 == d8.g.q
                     ? "The view did not reach the throw angle in time"
                     : (this.m() ? "The wind charge could not be thrown on its tick" : "No reliable catch before the pearl came down")
               );
            } else {
               if (var3 == d8.g.q) {
                  this.x();
               } else {
                  this.B();
               }
            }
         }
      }
   }

   private void L() {
      String var1 = this.h();
      if (var1 == null) {
         var1 = this.w();
      }

      if (var1 != null) {
         this.q.L();
         this.U(var1);
      } else {
         int var2 = this.Q();
         if (var2 > 0) {
            if (var2 <= 30 && !this.q.v(this.h)) {
               this.E = String.format(Locale.ROOT, "Catch in %.2f s (cooldown)", var2 / 20.0);
            } else {
               this.q.L();
               this.U(String.format(Locale.ROOT, "Ender pearl or wind charge is cooling down (%.1f s left)", var2 / 20.0));
            }
         } else {
            this.q.L();
            this.e();
         }
      }
   }

   private int Q() {
      float var1 = this.W(Items.ENDER_PEARL);
      float var2 = this.W(Items.WIND_CHARGE);
      int var3 = Math.max(c(var1, this.y, Items.ENDER_PEARL), c(var2, this.e, Items.WIND_CHARGE));
      this.y = var1;
      this.e = var2;
      return var3;
   }

   private float W(Item var1) {
      int var2 = vi.u(var1, true);
      if (var2 == -1) {
         return 0.0F;
      }

      ItemStack var3 = var2 == -2 ? this.v.getOffhandItem() : this.v.getInventory().getItem(var2);
      return this.v.getCooldowns().getCooldownPercent(var3, 0.0F);
   }

   private static int c(float var0, float var1, Item var2) {
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

   private void e() {
      String var1 = this.h();
      if (var1 == null) {
         var1 = this.w();
      }

      if (var1 != null) {
         this.U(var1);
      } else {
         float[] var2 = this.i();
         float var3 = var2[0];
         float var4 = var2[1];
         float var5 = this.n() ? -90.0F : var4;
         Vec3 var6 = this.v.getEyePosition();
         s9.q var7;
         if (this.P()) {
            s9.u var8 = s9.U(var6, var3, var5, this.s(), this.Y(), 0.6);
            if (var8 == null) {
               this.U(this.n() ? "No instant catch: clear the space above you" : "No instant catch along this look; clear the space ahead");
               return;
            }

            var5 = var8.pitch();
            var7 = var8.plan();
         } else {
            Vec3 var11 = s9.m(var3, var5);
            var7 = this.m()
               ? s9.k(this.q(var6, var11, this.s(), this.o.getValue().intValue()), var6, this.s(), var11, this.Y())
               : this.w(var6, var11, this.s());
            if (var7 == null || var7.reliability() < 0.6) {
               this.U(this.d(var7));
               return;
            }
         }

         this.R = var3;
         this.k = var5;
         this.d = var7;
         this.V.clear();

         for (Entity var9 : mc.level.entitiesForRendering()) {
            if (var9 instanceof ThrownEnderpearl var10 && var10.getOwner() == this.v) {
               this.V.add(var10.getId());
            }
         }

         this.q.G(this.h, 20);
         this.G.T();
         this.E = this.j(var3, var5) ? "Throwing" : "Turning to throw";
         _q.z(
            "PearlCatch",
            "Catch started; direction={}, timing={}, pitch {}{}, forecast catch on step {} at {} ({}/{} samples)",
            this.N.getValue(),
            this.K.getValue(),
            J(var5),
            !this.n() && var5 < var4 - 0.5F ? " raised from " + J(var4) : "",
            var7.nominal().tick(),
            C(var7.nominal().point()),
            var7.score(),
            var7.samples()
         );
         this.x();
      }
   }

   private s9.q w(Vec3 var1, Vec3 var2, Vec3 var3) {
      s9.q var4 = null;

      for (int var5 = 1; var5 <= 20; var5++) {
         s9.q var6 = s9.V(this.q(var1, var2, var3, var5), var1, var3, this.Y());
         if (var6 != null && var6.reliability() >= 0.6) {
            return var6;
         }

         if (var6 != null && (var4 == null || var6.score() > var4.score())) {
            var4 = var6;
         }
      }

      return var4;
   }

   private List<s9.j> q(Vec3 var1, Vec3 var2, Vec3 var3, int var4) {
      return s9.i(var1, var2, var3, var4, this.Y());
   }

   private String d(s9.q var1) {
      if (this.m() && !this.n()) {
         return "Fixed Delay only catches near straight up; use Instant or Solved for Look";
      } else if (var1 == null) {
         return this.n() ? "No catch: the pearl would come down first, so clear the space above you" : "No catch at this angle; aim higher or use Instant";
      } else {
         return String.format(
            Locale.ROOT,
            "Only %d of %d samples catch here; %s",
            var1.score(),
            var1.samples(),
            this.m() ? "try another Delay or Instant" : (this.n() ? "clear the space above you" : "aim higher or use Instant")
         );
      }
   }

   private void x() {
      if (_p.Q(this)) {
         this.J(this.R, this.k);
      }

      _p.w(this, this.P() ? this::r : this::m, this::l);
   }

   private boolean l() {
      if (!this.isRuntimeActive()
         || this.q.A() != d8.g.q
         || this.h() != null
         || (this.P() ? !this.Z(this.R, this.k, 2.5F) : !this.j(this.R, this.k))
         || !this.j(Items.ENDER_PEARL)
         || !this.j(Items.WIND_CHARGE)
         || !this.c(Items.ENDER_PEARL)
         || AuraModule.K()) {
         return false;
      } else if (!this.P()) {
         return true;
      } else if (!this.c(Items.WIND_CHARGE)) {
         return false;
      } else {
         Vec3 var1 = this.v.getEyePosition();
         Vec3 var2 = s9.m(this.v.getYRot(), this.v.getXRot());
         s9.q var3 = s9.k(s9.o(var1, var2, this.s(), this.Y()), var1, this.s(), var2, this.Y());
         if (var3 != null && !(var3.reliability() < 0.6)) {
            this.c = var3;
            return true;
         } else {
            return false;
         }
      }
   }

   private void r() {
      s9.q var1 = this.c;
      this.D = this.v.position();
      byte var2 = 0;
      byte var3 = 0;

      try {
         var7 = this.M(Items.ENDER_PEARL);
         if (var7) {
            var3 = this.M(Items.WIND_CHARGE);
         }
      } finally {
         this.v();
      }

      if (!var7) {
         this.f("The pearl throw was rejected");
      } else if (var3 == 0) {
         this.f("The wind charge was rejected after the pearl, so the pearl flies on");
      } else {
         this.d = var1;
         this.L = this.h;
         this.q.M(this.h, 1);
         this.q.v();
         this.T(var1);
         this.E = "Pearl and wind charge thrown together";
         _q.z(
            "PearlCatch",
            "Instant pair thrown at yaw {} pitch {} from {} after a turn of {}; expected catch on step {} at {} ({}/{} samples)",
            J(this.v.getYRot()),
            J(this.v.getXRot()),
            C(this.v.getEyePosition()),
            this.G.m(),
            var1 == null ? "?" : var1.nominal().tick(),
            var1 == null ? "?" : C(var1.nominal().point()),
            var1 == null ? 0 : var1.score(),
            var1 == null ? 0 : var1.samples()
         );
      }
   }

   private void m() {
      this.D = this.v.position();
      Vec3 var1 = this.v.getEyePosition();
      Vec3 var2 = this.s();
      if (!this.g(Items.ENDER_PEARL)) {
         this.f("The pearl throw was rejected");
      } else {
         this.R = this.v.getYRot();
         this.k = this.v.getXRot();
         this.B = var1;
         this.j = s9.m(this.R, this.k);
         this.a = var2;
         this.q.M(this.h, this.m() ? this.o.getValue().intValue() + 1 : 21);
         this.E = this.m() ? "Pearl thrown; wind charge in " + this.o.getValue().intValue() + " ticks" : "Pearl thrown; aiming the wind charge";
         _q.z("PearlCatch", "Pearl thrown at yaw {} pitch {} from {} after a turn of {}", J(this.R), J(this.k), C(var1), this.G.m());
         this.G.T();
      }
   }

   private void B() {
      this.U();
      if (this.A != null && this.A.isRemoved()) {
         this.f("The pearl came down before the wind charge");
      } else if (this.m()) {
         if (_p.Q(this)) {
            this.J(this.R, this.k);
         }

         if (this.q.H(this.h) >= this.o.getValue().intValue()) {
            _p.w(this, this::b, this::v);
         }
      } else {
         List var1 = this.q();
         if (this.S == null) {
            this.S = ((s9.j)var1.get(0)).L();
         }

         s9.q var2 = s9.V(var1, this.v.getEyePosition(), this.s(), this.Y());
         if (var2 != null && !(var2.reliability() < 0.6)) {
            this.d = var2;
            float[] var3 = b6.R(Vec3.ZERO, var2.direction());
            this.R = var3[0];
            this.k = Mth.clamp(var3[1], -90.0F, 90.0F);
            if (_p.Q(this)) {
               this.J(this.R, this.k);
            }

            _p.w(this, this::b, this::v);
            this.E = "Aiming wind charge";
         } else {
            this.E = "Waiting for a reliable catch line";
         }
      }
   }

   private boolean v() {
      if (this.isRuntimeActive() && this.q.A() == d8.g.w && this.h() == null && this.j(Items.WIND_CHARGE) && this.c(Items.WIND_CHARGE) && !AuraModule.K()) {
         Vec3 var1 = s9.m(this.v.getYRot(), this.v.getXRot());
         s9.q var2 = s9.k(this.q(), this.v.getEyePosition(), this.s(), var1, this.Y());
         if (this.m()) {
            this.c = var2;
            return this.q.H(this.h) == this.o.getValue().intValue() && this.j(this.R, this.k);
         } else if (var2 != null && !(var2.reliability() < 0.6)) {
            this.c = var2;
            return true;
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   private void b() {
      s9.q var1 = this.c;
      long var2 = this.q.H(this.h);
      if (!this.g(Items.WIND_CHARGE)) {
         this.f("The wind charge throw was rejected");
      } else {
         this.d = var1;
         this.L = this.h;
         this.q.v();
         this.T(var1);
         this.E = "Wind charge thrown";
         if (var1 == null) {
            _q.z("PearlCatch", "Wind charge thrown {} ticks after the pearl; no catch expected along this look", var2);
         } else {
            _q.z(
               "PearlCatch",
               "Wind charge thrown {} ticks after the pearl at yaw {} pitch {}; expected catch on step {} at {} ({}/{} samples){}",
               var2,
               J(this.v.getYRot()),
               J(this.v.getXRot()),
               var1.nominal().tick(),
               C(var1.nominal().point()),
               var1.score(),
               var1.samples(),
               var1.nominal().burst() ? ", charge bursts on you" : ""
            );
         }
      }
   }

   private void T(s9.q var1) {
      if (this.w.size() >= 4) {
         this.w.remove(0);
      }

      int var2 = (var1 == null ? 60 : var1.nominal().tick()) + this.U() + 20;
      AutoPearlCatchModule.k var3 = new AutoPearlCatchModule.k(new HashSet<>(this.V), this.D, var1, this.S, this.h, this.h + var2);
      var3.I = this.A;
      this.w.add(var3);
      this.A = null;
      this.V.clear();
      this.S = null;
   }

   private void V() {
      Iterator var1 = this.w.iterator();

      while (var1.hasNext()) {
         AutoPearlCatchModule.k var2 = (AutoPearlCatchModule.k)var1.next();
         if (var2.I == null) {
            var2.I = this.O(var2.t);
         }

         ThrownEnderpearl var3 = var2.I;
         if (var2.p < 0L && var3 != null && (var3.isRemoved() || mc.level.getEntity(var3.getId()) != var3)) {
            var2.p = this.h;
         }

         if (var2.O == null && !var2.N && var2.p < 0L && var3 != null) {
            var2.O = s9.X(new s9.h(var3.position(), var3.getDeltaMovement(), 1 + var3.tickCount), this.Y()).L();
            var2.N = (boolean)1;
         }

         if (var2.p >= 0L && this.h > var2.p) {
            this.p(var2);
            var1.remove();
         } else if (this.h - var2.o >= 0L) {
            String var4 = var3 == null ? "Pearl not seen; the outcome is unknown" : "No catch observed in time";
            _q.z("PearlCatch", "{}, {} ticks after the wind charge", var4, this.h - var2.C);
            if (!this.q.H()) {
               this.E = var4;
            }

            var1.remove();
         }
      }
   }

   private ThrownEnderpearl O(Set<Integer> var1) {
      for (Entity var3 : mc.level.entitiesForRendering()) {
         if (var3 instanceof ThrownEnderpearl var4
            && !var4.isRemoved()
            && var4.getOwner() == this.v
            && !var1.contains(var4.getId())
            && this.w.stream().noneMatch(var1x -> var1x.I == var4)) {
            return var4;
         }
      }

      return null;
   }

   private void p(AutoPearlCatchModule.k var1) {
      Vec3 var2 = this.v.position();
      Vec3 var3 = var1.m == null ? null : var1.m.nominal().point();
      double var4 = var3 == null ? Double.NaN : var2.distanceTo(var3);
      int var6 = var2.distanceTo(var1.Y) > 1.0 ? 1 : 0;
      String var7;
      if (var1.O != null && var2.distanceTo(var1.O) <= 1.0) {
         var7 = "Missed: the pearl landed";
      } else if (var6 == 0 || var3 != null && !(var4 <= 3.0)) {
         if (var6 != 0) {
            var7 = String.format(
               Locale.ROOT,
               "Caught %.1f blocks out, %.1f up, %.1f blocks from the predicted point (a tick of server timing)",
               var2.distanceTo(var1.Y),
               var2.y - var1.Y.y,
               var4
            );
         } else {
            var7 = "No teleport: the pearl ended near where you threw it";
         }
      } else {
         var7 = String.format(Locale.ROOT, "Caught %.1f blocks out, %.1f up", var2.distanceTo(var1.Y), var2.y - var1.Y.y);
      }

      _q.z(
         "PearlCatch",
         "{}; now at {}, expected {} ({} blocks off), {} ticks after the wind charge",
         var7,
         C(var2),
         var3 == null ? "unknown" : C(var3),
         Double.isNaN(var4) ? "?" : J(var4),
         this.h - var1.C
      );
      if (!this.q.H()) {
         this.E = var7;
      }
   }

   private List<s9.j> q() {
      if (this.P != this.h || this.U == null) {
         this.U = this.q(this.B, this.j, this.a, (int)Math.max(1L, this.q.H(this.h)));
         this.P = this.h;
      }

      return this.U;
   }

   private void U() {
      if (this.A == null) {
         for (Entity var2 : mc.level.entitiesForRendering()) {
            if (var2 instanceof ThrownEnderpearl var3 && !var3.isRemoved() && var3.getOwner() == this.v && !this.V.contains(var3.getId())) {
               this.A = var3;
               _q.c("PearlCatch", "Own pearl seen {} ticks after the throw, {} client ticks old", this.q.H(this.h), var3.tickCount);
               return;
            }
         }
      }
   }

   private String h() {
      if (!_p.g() || mc.gui.screen() != null) {
         return "Paused: a screen is open";
      } else if (!mc.isWindowActive()) {
         return "Paused: the window lost focus";
      } else if (ij.L() != ij.r.J && !ij.a(ij.r.J)) {
         return "Paused: Freecam or Freelook holds the view";
      } else if (this.v.isPassenger()) {
         return "Not while riding";
      } else {
         return !this.v.isEyeInFluid(FluidTags.WATER) && !this.v.isInLava() ? null : "Not with your head in water or lava";
      }
   }

   private String w() {
      if (this.v.isUsingItem()) {
         return "Stop using your item first";
      } else if (AuraModule.K()) {
         return "Paused while Kill Aura lowers your shield";
      } else if (vi.u(Items.ENDER_PEARL, true) == -1) {
         return "No ender pearls in your hotbar or hands";
      } else if (vi.u(Items.WIND_CHARGE, true) == -1) {
         return "No wind charges in your hotbar or hands";
      } else {
         return !this.v.getAbilities().invulnerable && this.v.getHealth() + this.v.getAbsorptionAmount() <= 5.0 ? "The 5 HP pearl hit would be lethal" : null;
      }
   }

   private boolean j(Item var1) {
      int var2 = vi.u(var1, true);
      if (var2 == -1) {
         return false;
      }

      ItemStack var3 = var2 == -2 ? this.v.getOffhandItem() : this.v.getInventory().getItem(var2);
      return var3.isItemEnabled(this.f.enabledFeatures()) && !this.v.getCooldowns().isOnCooldown(var3);
   }

   private boolean c(Item var1) {
      int var2 = vi.u(var1, true);
      return var2 == -2 || var2 == this.v.getInventory().getSelectedSlot() || var2 >= 0 && s6.L(this);
   }

   private boolean g(Item var1) {
      try {
         return this.M(var1);
      } finally {
         this.v();
      }
   }

   private boolean M(Item var1) {
      int var2 = vi.u(var1, true);
      if (var2 == -1) {
         return false;
      }

      InteractionHand var3 = var2 == -2 ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND;
      if (var2 >= 0 && var2 != this.v.getInventory().getSelectedSlot() && !this.z.h(this.v, var2)) {
         return false;
      }

      boolean var4 = mc.gameMode.useItem(this.v, var3).consumesAction();
      if (var4) {
         h8.i(this.v, var3);
         ((qc90ed4dd4cb15de4)mc).larpUseDelay(4);
      }

      return var4;
   }

   private void v() {
      if (this.z.j()) {
         int var1 = this.v == null ? -1 : this.v.getInventory().getSelectedSlot();
         this.z.X(getMc(), this.t.getValue());
         if (this.v != null && this.v == mc.player && mc.gameMode != null && var1 != this.v.getInventory().getSelectedSlot()) {
            ((q9b8c1cd7fe41113a)mc.gameMode).larpSyncCarriedItem();
         }
      }
   }

   private float[] i() {
      return !ij.g() ? new float[]{this.v.getYRot(), this.v.getXRot()} : ij.p(ij.D(), ij.J(), mc.options.getCameraType().isMirrored());
   }

   private void J(float var1, float var2) {
      ij.V(ij.r.J);
      if (ij.L() == ij.r.J) {
         float[] var3 = this.G.i(this.v.getYRot(), this.v.getXRot(), var1, var2, this.H.getValue(), this.I.getValue());
         b6.P(this, b6.w.c, var3[0], var3[1], 1.0);
      }
   }

   private boolean j(float var1, float var2) {
      return this.Z(var1, var2, _b.M(this.I.getValue(), 0.05F, com.larpclient.module.w.d()));
   }

   private boolean Z(float var1, float var2, float var3) {
      return Math.abs(Mth.wrapDegrees(this.v.getYRot() - var1)) <= var3 && Math.abs(this.v.getXRot() - var2) <= var3;
   }

   private Vec3 s() {
      return this.v.onGround() ? new Vec3(this.T.x, 0.0, this.T.z) : this.T;
   }

   private int U() {
      ClientPacketListener var1 = mc.getConnection();
      PlayerInfo var2 = var1 != null && this.v != null ? var1.getPlayerInfo(this.v.getUUID()) : null;
      return var2 == null ? 0 : Math.max(0, Math.round(var2.getLatency() / 50.0F));
   }

   private s9.v Y() {
      return new ne(this.f, this.v);
   }

   private boolean n() {
      return this.N.w("Up");
   }

   private boolean m() {
      return this.K.w("Fixed Delay");
   }

   private boolean P() {
      return this.K.w("Instant");
   }

   private static String J(double var0) {
      return String.format(Locale.ROOT, "%.2f", var0);
   }

   private static String C(Vec3 var0) {
      return String.format(Locale.ROOT, "(%.1f, %.1f, %.1f)", var0.x, var0.y, var0.z);
   }

   private void U(String var1) {
      this.E = var1;
      _q.z("PearlCatch", "Catch refused: {}", var1);
   }

   private void f(String var1) {
      boolean var2 = this.q.H();
      _p.e(this);
      this.v();
      this.y();
      if (var2) {
         this.E = var1;
         _q.z("PearlCatch", "Catch cancelled: {}", var1);
      }
   }

   private void y() {
      this.q.i();
      ij.P(ij.r.J, this.G, this.m.getValue(), this.I.getValue());
      this.V.clear();
      this.A = null;
      this.d = this.c = null;
      this.U = null;
      this.P = -1L;
      this.S = null;
   }

   @Override
   public void onEnable() {
      this.v = null;
      this.f = null;
      this.s = null;
      this.w.clear();
      this.L = Long.MIN_VALUE;
      this.y();
      this.E = this.g.getValue() < 0 ? "Set a Catch Bind" : "Ready";
      _q.z(
         "PearlCatch",
         "Enabled; direction={}, timing={}, delay={}, turn speed={}, return speed={}, humanize={}",
         this.N.getValue(),
         this.K.getValue(),
         this.o.getValue().intValue(),
         this.H.getValue().intValue(),
         this.m.getValue().intValue(),
         this.I.getValue()
      );
   }

   @Override
   public void onDisable() {
      this.f("Disabled");
      this.w.clear();
      this.v = null;
      this.f = null;
      _q.z("PearlCatch", "Disabled; pending throws and aim released");
   }

   private static final class k {
      final Set<Integer> t;
      final Vec3 Y;
      final s9.q m;
      final long C;
      final long o;
      ThrownEnderpearl I;
      Vec3 O;
      boolean N;
      long p = -1L;

      k(Set<Integer> var1, Vec3 var2, s9.q var3, Vec3 var4, long var5, long var7) {
         this.t = var1;
         this.Y = var2;
         this.m = var3;
         this.O = var4;
         this.C = var5;
         this.o = var7;
      }
   }
}
