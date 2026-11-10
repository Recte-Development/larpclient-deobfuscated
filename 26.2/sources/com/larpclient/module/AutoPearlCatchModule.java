package com.larpclient.module;

import com.larp.obf.qa300483f54d804a3.q541491ff38fb1637;
import com.larp.obf.qa300483f54d804a3.q7ae1dbb86ec789d8;
import com.larpclient.hu;
import com.larpclient.j6;
import com.larpclient.ks;
import com.larpclient.lm;
import com.larpclient.p_;
import com.larpclient.t1;
import com.larpclient.ta;
import com.larpclient.u4;
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
   private static final int n = 20;
   private static final int S = 20;
   private static final int D = 20;
   private static final int J = 3;
   private static final int g = 4;
   private static final double r = 0.6;
   private static final double F = 5.0;
   private static final float o = 0.05F;
   public static final float C = 2.5F;
   private static AutoPearlCatchModule z;
   private final v L = this.addSetting(
      new v("Catch Bind", "Press for one pearl catch; pressing again while it throws cancels it. Separate from the module toggle", -1)
   );
   private final l j = this.addSetting(new l("Direction", "Up throws straight up for height; Look throws where you aim for distance", "Up", "Up", "Look"));
   private final l x = this.addSetting(
      new l(
         "Timing",
         "Instant throws both in one tick for a catch a few blocks out; Solved aims the wind charge a tick or more later for a farther catch; Fixed Delay throws it along the same look after Delay",
         "Instant",
         "Instant",
         "Solved",
         "Fixed Delay"
      )
   );
   private final e K = this.addSetting(new e("Delay", "Ticks from the pearl to the wind charge; same-look catches work near straight up", 2.0, 1.0, 10.0, 1.0));
   private final e U = this.addSetting(
      new e(
         "Turn Speed", "Maximum player turn in degrees per tick; your camera stays on your look. 180 with Humanize off turns at once", 90.0, 10.0, 180.0, 5.0
      )
   );
   private final s Z = this.addSetting(new s("Swap Back", "Return to your previous hotbar slot after each throw", true));
   private final e m = this.addSetting(
      new e("Return Speed", "Most the player turns per tick back to your look after the last throw. 180 returns at once", 40.0, 10.0, 180.0, 5.0)
   );
   private final s Y = this.addSetting(
      new s("Humanize", "Turn like a hand on a mouse: speed up, ease in, drift a little and move in whole sensitivity steps. Off turns in straight lines", true)
   );
   private final lm k = new lm();
   private final hu V = new hu(this);
   private final com.larpclient.q a = new com.larpclient.q();
   private final Set<Integer> P = new HashSet<>();
   private final List<AutoPearlCatchModule.f> h = new ArrayList<>();
   private LocalPlayer A;
   private ClientLevel p;
   private long v;
   private long H = Long.MIN_VALUE;
   private long f = -1L;
   private float[] I;
   private float T;
   private float N;
   private List<com.larpclient.t.b> s;
   private Vec3 M;
   private Vec3 c = Vec3.ZERO;
   private Vec3 X;
   private Vec3 G = Vec3.ZERO;
   private Vec3 d = Vec3.ZERO;
   private Vec3 e = Vec3.ZERO;
   private Vec3 y = Vec3.ZERO;
   private float R;
   private float q;
   private ThrownEnderpearl u;
   private com.larpclient.t.m w;
   private com.larpclient.t.m t;
   private String B = "Set a Catch Bind";

   public AutoPearlCatchModule() {
      super("Auto Pearl Catch", "Throw a pearl, then a wind charge it hits in flight, to teleport mid-air", Category.MOVEMENT, -1);
      z = this;
      this.K.P(this::B);
      this.j.o(var1 -> this.j("Direction changed"));
      this.x.o(var1 -> this.j("Timing changed"));
      this.L.o(var1 -> this.j("Bind changed"));
   }

   public static AutoPearlCatchModule W() {
      return z;
   }

   public List<q<?>> a() {
      return List.of(this.L, this.j, this.x, this.K);
   }

   public List<q<?>> l() {
      return List.of(this.U, this.m, this.Y, this.Z);
   }

   public lm P() {
      return this.k;
   }

   public double B() {
      return this.m.getValue();
   }

   public boolean f() {
      return this.Y.getValue();
   }

   @Override
   public Module.f getTickPhase() {
      return Module.f.u;
   }

   @Override
   public Set<m> getNetworkImpacts() {
      return Set.of(com.larpclient.module.m.K, com.larpclient.module.m.U);
   }

   @Override
   public String getStatus() {
      return this.isEnabled() ? this.B : "";
   }

   public boolean D() {
      return !this.isEnabled()
         ? false
         : this.isRuntimeActive() && this.A != null && this.A == mc.player && this.p == mc.level && (this.a.I() || this.v == this.H);
   }

   public static void T(int var0) {
      if (z != null && z.isEnabled()) {
         AutoPearlCatchModule var1 = z;
         if (var1 != null && var1.isRuntimeActive() && var0 > 0 && var0 == var1.L.getValue()) {
            if (!var1.a.n()) {
               var1.j("Cancelled with Catch Bind");
            }
         }
      }
   }

   @Override
   public void onTick() {
      if (!this.isInGame()) {
         this.j("Waiting for a world");
         this.h.clear();
      } else {
         if (this.A != mc.player || this.p != mc.level) {
            float[] var17;
            label69: {
               byte var10000 = this.a.I();
               boolean var6 = false;
               byte var5 = var10000;
               int var14 = Integer.signum(var5);
               if ((var14 | -var14) >>> 31 == 0) {
                  long var21;
                  int var15 = (var21 = this.v - this.H) == 0L ? 0 : (var21 < 0L ? -1 : 1);
                  boolean var8 = false;
                  int var7 = var15;
                  int var16 = Integer.signum(var7);
                  if ((var16 | -var16) >>> 31 != 0) {
                     var17 = null;
                     break label69;
                  }
               }

               var17 = this.I;
            }

            float[] var1 = var17;
            this.j("World changed");
            this.h.clear();
            this.A = mc.player;
            this.p = mc.level;
            this.M = null;
            if (var1 != null) {
               this.A.setYRot(var1[0]);
               this.A.setXRot(var1[1]);
               this.A.yRotO = var1[0];
               this.A.xRotO = var1[1];
               com.larpclient.h.A("PearlCatch", "World changed during a catch; your look (yaw {} pitch {}) restored", m(var1[0]), m(var1[1]));
            }
         }

         this.v++;
         if (p_.A() == p_.o.Y) {
            this.I = p_.e(p_.V(), p_.s(), mc.options.getCameraType().isMirrored());
         }

         Vec3 var13;
         Vec3 var20;
         label63: {
            var13 = this.A.position();
            if (this.M != null) {
               double var22;
               int var10001 = (var22 = var13.distanceToSqr(this.M) - 16.0) == 0.0 ? 0 : (var22 < 0.0 ? -1 : 1);
               boolean var10 = false;
               int var9 = var10001;
               if ((-Integer.signum(var9) >>> 31 ^ 1) != 0) {
                  var20 = var13.subtract(this.M);
                  break label63;
               }
            }

            var20 = Vec3.ZERO;
         }

         this.c = var20;
         this.M = var13;
         this.L();
         boolean var2 = this.a.a();
         com.larpclient.q.i var3 = this.a.W();
         if (var3 == com.larpclient.q.i.V) {
            if (var2) {
               int var18 = this.a.V();
               boolean var12 = false;
               byte var11 = (byte)var18;
               var18 = Integer.signum(var11);
               if ((var18 | -var18) >>> 31 == 0) {
                  this.T = this.N = 0.0F;
                  this.a.G(this.v);
               }
            }

            if (this.a.V()) {
               this.M();
            }
         } else {
            String var4 = this.g();
            if (var4 != null) {
               this.j(var4);
            } else if (this.a.f(this.v)) {
               this.j(
                  var3 == com.larpclient.q.i.O
                     ? "The view did not reach the throw angle in time"
                     : (this.B() ? "The wind charge could not be thrown on its tick" : "No reliable catch before the pearl came down")
               );
            } else {
               if (var3 == com.larpclient.q.i.O) {
                  this.q();
               } else {
                  this.S();
               }
            }
         }
      }
   }

   private void M() {
      String var1 = this.g();
      if (var1 == null) {
         var1 = this.r();
      }

      if (var1 != null) {
         this.a.e();
         this.m(var1);
      } else {
         int var2 = this.x();
         if (var2 > 0) {
            if (var2 <= 30 && !this.a.G(this.v)) {
               this.B = String.format(Locale.ROOT, "Catch in %.2f s (cooldown)", var2 / 20.0);
            } else {
               this.a.e();
               this.m(String.format(Locale.ROOT, "Ender pearl or wind charge is cooling down (%.1f s left)", var2 / 20.0));
            }
         } else {
            this.a.e();
            this.T();
         }
      }
   }

   private int x() {
      float var1 = this.A(Items.ENDER_PEARL);
      float var2 = this.A(Items.WIND_CHARGE);
      int var3 = Math.max(b(var1, this.T, Items.ENDER_PEARL), b(var2, this.N, Items.WIND_CHARGE));
      this.T = var1;
      this.N = var2;
      return var3;
   }

   private float A(Item var1) {
      int var2 = t1.M(var1, true);
      if (var2 == -1) {
         return 0.0F;
      }

      ItemStack var3 = var2 == -2 ? this.A.getOffhandItem() : this.A.getInventory().getItem(var2);
      return this.A.getCooldowns().getCooldownPercent(var3, 0.0F);
   }

   private static int b(float var0, float var1, Item var2) {
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

   private void T() {
      String var1 = this.g();
      if (var1 == null) {
         var1 = this.r();
      }

      if (var1 != null) {
         this.m(var1);
      } else {
         float[] var2 = this.V();
         float var3 = var2[0];
         float var4 = var2[1];
         float var5 = this.K() ? -90.0F : var4;
         Vec3 var6 = this.A.getEyePosition();
         com.larpclient.t.m var7;
         if (this.L()) {
            com.larpclient.t.v var8 = com.larpclient.t.M(var6, var3, var5, this.y(), this.A(), 0.6);
            if (var8 == null) {
               this.m(this.K() ? "No instant catch: clear the space above you" : "No instant catch along this look; clear the space ahead");
               return;
            }

            var5 = var8.pitch();
            var7 = var8.plan();
         } else {
            Vec3 var11 = com.larpclient.t.g(var3, var5);
            var7 = this.B()
               ? com.larpclient.t.o(this.z(var6, var11, this.y(), this.K.getValue().intValue()), var6, this.y(), var11, this.A())
               : this.E(var6, var11, this.y());
            if (var7 == null || var7.reliability() < 0.6) {
               this.m(this.p(var7));
               return;
            }
         }

         this.R = var3;
         this.q = var5;
         this.w = var7;
         this.P.clear();

         for (Entity var9 : mc.level.entitiesForRendering()) {
            if (var9 instanceof ThrownEnderpearl var10 && var10.getOwner() == this.A) {
               this.P.add(var10.getId());
            }
         }

         this.a.r(this.v, 20);
         this.k.q();
         this.B = this.L(var3, var5) ? "Throwing" : "Turning to throw";
         com.larpclient.h.A(
            "PearlCatch",
            "Catch started; direction={}, timing={}, pitch {}{}, forecast catch on step {} at {} ({}/{} samples)",
            this.j.getValue(),
            this.x.getValue(),
            m(var5),
            !this.K() && var5 < var4 - 0.5F ? " raised from " + m(var4) : "",
            var7.nominal().tick(),
            J(var7.nominal().point()),
            var7.score(),
            var7.samples()
         );
         this.q();
      }
   }

   private com.larpclient.t.m E(Vec3 var1, Vec3 var2, Vec3 var3) {
      com.larpclient.t.m var4 = null;

      for (int var5 = 1; var5 <= 20; var5++) {
         com.larpclient.t.m var6 = com.larpclient.t.I(this.z(var1, var2, var3, var5), var1, var3, this.A());
         if (var6 != null && var6.reliability() >= 0.6) {
            return var6;
         }

         if (var6 != null && (var4 == null || var6.score() > var4.score())) {
            var4 = var6;
         }
      }

      return var4;
   }

   private List<com.larpclient.t.b> z(Vec3 var1, Vec3 var2, Vec3 var3, int var4) {
      return com.larpclient.t.G(var1, var2, var3, var4, this.A());
   }

   private String p(com.larpclient.t.m var1) {
      if (this.B() && !this.K()) {
         return "Fixed Delay only catches near straight up; use Instant or Solved for Look";
      } else if (var1 == null) {
         return this.K() ? "No catch: the pearl would come down first, so clear the space above you" : "No catch at this angle; aim higher or use Instant";
      } else {
         return String.format(
            Locale.ROOT,
            "Only %d of %d samples catch here; %s",
            var1.score(),
            var1.samples(),
            this.B() ? "try another Delay or Instant" : (this.K() ? "clear the space above you" : "aim higher or use Instant")
         );
      }
   }

   private void q() {
      if (ta.E(this)) {
         this.P(this.R, this.q);
      }

      ta.F(this, this.L() ? this::U : this::X, this::J);
   }

   private boolean J() {
      if (!this.isRuntimeActive()
         || this.a.W() != com.larpclient.q.i.O
         || this.g() != null
         || (this.L() ? !this.B(this.R, this.q, 2.5F) : !this.L(this.R, this.q))
         || !this.N(Items.ENDER_PEARL)
         || !this.N(Items.WIND_CHARGE)
         || !this.T(Items.ENDER_PEARL)
         || AuraModule.N()) {
         return false;
      } else if (!this.L()) {
         return true;
      } else if (!this.T(Items.WIND_CHARGE)) {
         return false;
      } else {
         Vec3 var1 = this.A.getEyePosition();
         Vec3 var2 = com.larpclient.t.g(this.A.getYRot(), this.A.getXRot());
         com.larpclient.t.m var3 = com.larpclient.t.o(com.larpclient.t.r(var1, var2, this.y(), this.A()), var1, this.y(), var2, this.A());
         if (var3 != null && !(var3.reliability() < 0.6)) {
            this.t = var3;
            return true;
         } else {
            return false;
         }
      }
   }

   private void U() {
      com.larpclient.t.m var1 = this.t;
      this.G = this.A.position();
      byte var2 = 0;
      byte var3 = 0;

      try {
         var7 = this.s(Items.ENDER_PEARL);
         if (var7) {
            var3 = this.s(Items.WIND_CHARGE);
         }
      } finally {
         this.l();
      }

      if (!var7) {
         this.j("The pearl throw was rejected");
      } else if (var3 == 0) {
         this.j("The wind charge was rejected after the pearl, so the pearl flies on");
      } else {
         this.w = var1;
         this.H = this.v;
         this.a.n(this.v, 1);
         this.a.b();
         this.t(var1);
         this.B = "Pearl and wind charge thrown together";
         com.larpclient.h.A(
            "PearlCatch",
            "Instant pair thrown at yaw {} pitch {} from {} after a turn of {}; expected catch on step {} at {} ({}/{} samples)",
            m(this.A.getYRot()),
            m(this.A.getXRot()),
            J(this.A.getEyePosition()),
            this.k.u(),
            var1 == null ? "?" : var1.nominal().tick(),
            var1 == null ? "?" : J(var1.nominal().point()),
            var1 == null ? 0 : var1.score(),
            var1 == null ? 0 : var1.samples()
         );
      }
   }

   private void X() {
      this.G = this.A.position();
      Vec3 var1 = this.A.getEyePosition();
      Vec3 var2 = this.y();
      if (!this.J(Items.ENDER_PEARL)) {
         this.j("The pearl throw was rejected");
      } else {
         this.R = this.A.getYRot();
         this.q = this.A.getXRot();
         this.d = var1;
         this.e = com.larpclient.t.g(this.R, this.q);
         this.y = var2;
         this.a.n(this.v, this.B() ? this.K.getValue().intValue() + 1 : 21);
         this.B = this.B() ? "Pearl thrown; wind charge in " + this.K.getValue().intValue() + " ticks" : "Pearl thrown; aiming the wind charge";
         com.larpclient.h.A("PearlCatch", "Pearl thrown at yaw {} pitch {} from {} after a turn of {}", m(this.R), m(this.q), J(var1), this.k.u());
         this.k.q();
      }
   }

   private void S() {
      this.J();
      if (this.u != null && this.u.isRemoved()) {
         this.j("The pearl came down before the wind charge");
      } else if (this.B()) {
         if (ta.E(this)) {
            this.P(this.R, this.q);
         }

         if (this.a.R(this.v) >= this.K.getValue().intValue()) {
            ta.F(this, this::Z, this::S);
         }
      } else {
         List var1 = this.U();
         if (this.X == null) {
            this.X = ((com.larpclient.t.b)var1.get(0)).X();
         }

         com.larpclient.t.m var2 = com.larpclient.t.I(var1, this.A.getEyePosition(), this.y(), this.A());
         if (var2 != null && !(var2.reliability() < 0.6)) {
            this.w = var2;
            float[] var3 = j6.s(Vec3.ZERO, var2.direction());
            this.R = var3[0];
            this.q = Mth.clamp(var3[1], -90.0F, 90.0F);
            if (ta.E(this)) {
               this.P(this.R, this.q);
            }

            ta.F(this, this::Z, this::S);
            this.B = "Aiming wind charge";
         } else {
            this.B = "Waiting for a reliable catch line";
         }
      }
   }

   private boolean S() {
      if (this.isRuntimeActive()
         && this.a.W() == com.larpclient.q.i.F
         && this.g() == null
         && this.N(Items.WIND_CHARGE)
         && this.T(Items.WIND_CHARGE)
         && !AuraModule.N()) {
         Vec3 var1 = com.larpclient.t.g(this.A.getYRot(), this.A.getXRot());
         com.larpclient.t.m var2 = com.larpclient.t.o(this.U(), this.A.getEyePosition(), this.y(), var1, this.A());
         if (this.B()) {
            this.t = var2;
            return this.a.R(this.v) == this.K.getValue().intValue() && this.L(this.R, this.q);
         } else if (var2 != null && !(var2.reliability() < 0.6)) {
            this.t = var2;
            return true;
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   private void Z() {
      com.larpclient.t.m var1 = this.t;
      long var2 = this.a.R(this.v);
      if (!this.J(Items.WIND_CHARGE)) {
         this.j("The wind charge throw was rejected");
      } else {
         this.w = var1;
         this.H = this.v;
         this.a.b();
         this.t(var1);
         this.B = "Wind charge thrown";
         if (var1 == null) {
            com.larpclient.h.A("PearlCatch", "Wind charge thrown {} ticks after the pearl; no catch expected along this look", var2);
         } else {
            com.larpclient.h.A(
               "PearlCatch",
               "Wind charge thrown {} ticks after the pearl at yaw {} pitch {}; expected catch on step {} at {} ({}/{} samples){}",
               var2,
               m(this.A.getYRot()),
               m(this.A.getXRot()),
               var1.nominal().tick(),
               J(var1.nominal().point()),
               var1.score(),
               var1.samples(),
               var1.nominal().burst() ? ", charge bursts on you" : ""
            );
         }
      }
   }

   private void t(com.larpclient.t.m var1) {
      if (this.h.size() >= 4) {
         this.h.remove(0);
      }

      int var2 = (var1 == null ? 60 : var1.nominal().tick()) + this.P() + 20;
      AutoPearlCatchModule.f var3 = new AutoPearlCatchModule.f(new HashSet<>(this.P), this.G, var1, this.X, this.v, this.v + var2);
      var3.w = this.u;
      this.h.add(var3);
      this.u = null;
      this.P.clear();
      this.X = null;
   }

   private void L() {
      Iterator var1 = this.h.iterator();

      while (var1.hasNext()) {
         AutoPearlCatchModule.f var2 = (AutoPearlCatchModule.f)var1.next();
         if (var2.w == null) {
            var2.w = this.u(var2.k);
         }

         ThrownEnderpearl var3 = var2.w;
         if (var2.t < 0L && var3 != null && (var3.isRemoved() || mc.level.getEntity(var3.getId()) != var3)) {
            var2.t = this.v;
         }

         if (var2.o == null && !var2.B && var2.t < 0L && var3 != null) {
            var2.o = com.larpclient.t.m(new com.larpclient.t.i(var3.position(), var3.getDeltaMovement(), 1 + var3.tickCount), this.A()).X();
            var2.B = (boolean)1;
         }

         if (var2.t >= 0L && this.v > var2.t) {
            this.U(var2);
            var1.remove();
         } else if (this.v - var2.f >= 0L) {
            String var4 = var3 == null ? "Pearl not seen; the outcome is unknown" : "No catch observed in time";
            com.larpclient.h.A("PearlCatch", "{}, {} ticks after the wind charge", var4, this.v - var2.u);
            if (!this.a.I()) {
               this.B = var4;
            }

            var1.remove();
         }
      }
   }

   private ThrownEnderpearl u(Set<Integer> var1) {
      for (Entity var3 : mc.level.entitiesForRendering()) {
         if (var3 instanceof ThrownEnderpearl var4
            && !var4.isRemoved()
            && var4.getOwner() == this.A
            && !var1.contains(var4.getId())
            && this.h.stream().noneMatch(var1x -> var1x.w == var4)) {
            return var4;
         }
      }

      return null;
   }

   private void U(AutoPearlCatchModule.f var1) {
      Vec3 var2 = this.A.position();
      Vec3 var3 = var1.W == null ? null : var1.W.nominal().point();
      double var4 = var3 == null ? Double.NaN : var2.distanceTo(var3);
      int var6 = var2.distanceTo(var1.L) > 1.0 ? 1 : 0;
      String var7;
      if (var1.o != null && var2.distanceTo(var1.o) <= 1.0) {
         var7 = "Missed: the pearl landed";
      } else if (var6 == 0 || var3 != null && !(var4 <= 3.0)) {
         if (var6 != 0) {
            var7 = String.format(
               Locale.ROOT,
               "Caught %.1f blocks out, %.1f up, %.1f blocks from the predicted point (a tick of server timing)",
               var2.distanceTo(var1.L),
               var2.y - var1.L.y,
               var4
            );
         } else {
            var7 = "No teleport: the pearl ended near where you threw it";
         }
      } else {
         var7 = String.format(Locale.ROOT, "Caught %.1f blocks out, %.1f up", var2.distanceTo(var1.L), var2.y - var1.L.y);
      }

      com.larpclient.h.A(
         "PearlCatch",
         "{}; now at {}, expected {} ({} blocks off), {} ticks after the wind charge",
         var7,
         J(var2),
         var3 == null ? "unknown" : J(var3),
         Double.isNaN(var4) ? "?" : m(var4),
         this.v - var1.u
      );
      if (!this.a.I()) {
         this.B = var7;
      }
   }

   private List<com.larpclient.t.b> U() {
      if (this.f != this.v || this.s == null) {
         this.s = this.z(this.d, this.e, this.y, (int)Math.max(1L, this.a.R(this.v)));
         this.f = this.v;
      }

      return this.s;
   }

   private void J() {
      if (this.u == null) {
         for (Entity var2 : mc.level.entitiesForRendering()) {
            if (var2 instanceof ThrownEnderpearl var3 && !var3.isRemoved() && var3.getOwner() == this.A && !this.P.contains(var3.getId())) {
               this.u = var3;
               com.larpclient.h.U("PearlCatch", "Own pearl seen {} ticks after the throw, {} client ticks old", this.a.R(this.v), var3.tickCount);
               return;
            }
         }
      }
   }

   private String g() {
      if (!ta.Y() || mc.gui.screen() != null) {
         return "Paused: a screen is open";
      } else if (!mc.isWindowActive()) {
         return "Paused: the window lost focus";
      } else if (p_.A() != p_.o.Y && !p_.I(p_.o.Y)) {
         return "Paused: Freecam or Freelook holds the view";
      } else if (this.A.isPassenger()) {
         return "Not while riding";
      } else {
         return !this.A.isEyeInFluid(FluidTags.WATER) && !this.A.isInLava() ? null : "Not with your head in water or lava";
      }
   }

   private String r() {
      if (this.A.isUsingItem()) {
         return "Stop using your item first";
      } else if (AuraModule.N()) {
         return "Paused while Kill Aura lowers your shield";
      } else if (t1.M(Items.ENDER_PEARL, true) == -1) {
         return "No ender pearls in your hotbar or hands";
      } else if (t1.M(Items.WIND_CHARGE, true) == -1) {
         return "No wind charges in your hotbar or hands";
      } else {
         return !this.A.getAbilities().invulnerable && this.A.getHealth() + this.A.getAbsorptionAmount() <= 5.0 ? "The 5 HP pearl hit would be lethal" : null;
      }
   }

   private boolean N(Item var1) {
      int var2 = t1.M(var1, true);
      if (var2 == -1) {
         return false;
      }

      ItemStack var3 = var2 == -2 ? this.A.getOffhandItem() : this.A.getInventory().getItem(var2);
      return var3.isItemEnabled(this.p.enabledFeatures()) && !this.A.getCooldowns().isOnCooldown(var3);
   }

   private boolean T(Item var1) {
      int var2 = t1.M(var1, true);
      return var2 == -2 || var2 == this.A.getInventory().getSelectedSlot() || var2 >= 0 && hu.k(this);
   }

   private boolean J(Item var1) {
      try {
         return this.s(var1);
      } finally {
         this.l();
      }
   }

   private boolean s(Item var1) {
      int var2 = t1.M(var1, true);
      if (var2 == -1) {
         return false;
      }

      InteractionHand var3 = var2 == -2 ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND;
      if (var2 >= 0 && var2 != this.A.getInventory().getSelectedSlot() && !this.V.O(this.A, var2)) {
         return false;
      }

      boolean var4 = mc.gameMode.useItem(this.A, var3).consumesAction();
      if (var4) {
         ks.p(this.A, var3);
         ((q7ae1dbb86ec789d8)mc).larpUseDelay(4);
      }

      return var4;
   }

   private void l() {
      if (this.V.p()) {
         int var1 = this.A == null ? -1 : this.A.getInventory().getSelectedSlot();
         this.V.V(getMc(), this.Z.getValue());
         if (this.A != null && this.A == mc.player && mc.gameMode != null && var1 != this.A.getInventory().getSelectedSlot()) {
            ((q541491ff38fb1637)mc.gameMode).larpSyncCarriedItem();
         }
      }
   }

   private float[] V() {
      return !p_.j() ? new float[]{this.A.getYRot(), this.A.getXRot()} : p_.e(p_.V(), p_.s(), mc.options.getCameraType().isMirrored());
   }

   private void P(float var1, float var2) {
      p_.p(p_.o.Y);
      if (p_.A() == p_.o.Y) {
         float[] var3 = this.k.f(this.A.getYRot(), this.A.getXRot(), var1, var2, this.U.getValue(), this.Y.getValue());
         j6.U(this, j6.l.P, var3[0], var3[1], 1.0);
      }
   }

   private boolean L(float var1, float var2) {
      return this.B(var1, var2, lm.A(this.Y.getValue(), 0.05F, tv.g()));
   }

   private boolean B(float var1, float var2, float var3) {
      return Math.abs(Mth.wrapDegrees(this.A.getYRot() - var1)) <= var3 && Math.abs(this.A.getXRot() - var2) <= var3;
   }

   private Vec3 y() {
      return this.A.onGround() ? new Vec3(this.c.x, 0.0, this.c.z) : this.c;
   }

   private int P() {
      ClientPacketListener var1 = mc.getConnection();
      PlayerInfo var2 = var1 != null && this.A != null ? var1.getPlayerInfo(this.A.getUUID()) : null;
      return var2 == null ? 0 : Math.max(0, Math.round(var2.getLatency() / 50.0F));
   }

   private com.larpclient.t.n A() {
      return new u4(this.p, this.A);
   }

   private boolean K() {
      return this.j.O("Up");
   }

   private boolean B() {
      return this.x.O("Fixed Delay");
   }

   private boolean L() {
      return this.x.O("Instant");
   }

   private static String m(double var0) {
      return String.format(Locale.ROOT, "%.2f", var0);
   }

   private static String J(Vec3 var0) {
      return String.format(Locale.ROOT, "(%.1f, %.1f, %.1f)", var0.x, var0.y, var0.z);
   }

   private void m(String var1) {
      this.B = var1;
      com.larpclient.h.A("PearlCatch", "Catch refused: {}", var1);
   }

   private void j(String var1) {
      boolean var2 = this.a.I();
      ta.s(this);
      this.l();
      this.t();
      if (var2) {
         this.B = var1;
         com.larpclient.h.A("PearlCatch", "Catch cancelled: {}", var1);
      }
   }

   private void t() {
      this.a.T();
      p_.t(p_.o.Y, this.k, this.m.getValue(), this.Y.getValue());
      this.P.clear();
      this.u = null;
      this.w = this.t = null;
      this.s = null;
      this.f = -1L;
      this.X = null;
   }

   @Override
   public void onEnable() {
      this.A = null;
      this.p = null;
      this.M = null;
      this.h.clear();
      this.H = Long.MIN_VALUE;
      this.t();
      this.B = this.L.getValue() < 0 ? "Set a Catch Bind" : "Ready";
      com.larpclient.h.A(
         "PearlCatch",
         "Enabled; direction={}, timing={}, delay={}, turn speed={}, return speed={}, humanize={}",
         this.j.getValue(),
         this.x.getValue(),
         this.K.getValue().intValue(),
         this.U.getValue().intValue(),
         this.m.getValue().intValue(),
         this.Y.getValue()
      );
   }

   @Override
   public void onDisable() {
      this.j("Disabled");
      this.h.clear();
      this.A = null;
      this.p = null;
      com.larpclient.h.A("PearlCatch", "Disabled; pending throws and aim released");
   }

   private static final class f {
      final Set<Integer> k;
      final Vec3 L;
      final com.larpclient.t.m W;
      final long u;
      final long f;
      ThrownEnderpearl w;
      Vec3 o;
      boolean B;
      long t = -1L;

      f(Set<Integer> var1, Vec3 var2, com.larpclient.t.m var3, Vec3 var4, long var5, long var7) {
         this.k = var1;
         this.L = var2;
         this.W = var3;
         this.o = var4;
         this.u = var5;
         this.f = var7;
      }
   }
}
