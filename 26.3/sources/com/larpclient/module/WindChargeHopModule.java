package com.larpclient.module;

import com.larp.obf.q41a5851bcf8e876a.q9b8c1cd7fe41113a;
import com.larp.obf.q41a5851bcf8e876a.qc90ed4dd4cb15de4;
import com.larpclient._b;
import com.larpclient._p;
import com.larpclient._q;
import com.larpclient.b6;
import com.larpclient.dg;
import com.larpclient.h8;
import com.larpclient.ij;
import com.larpclient.s6;
import com.larpclient.vi;
import com.larpclient.vm;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.component.DataComponents;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.hurtingprojectile.windcharge.WindCharge;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.UseCooldown;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.ClipContext.Block;
import net.minecraft.world.level.ClipContext.Fluid;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.HitResult.Type;

public final class WindChargeHopModule extends Module {
   private static final int W = 40;
   private static final int S = 30;
   private static WindChargeHopModule m;
   private final x K = this.addSetting(new x("Hop Bind", "Press for one wind charge hop; separate from the module toggle", -1));
   private final o Q = this.addSetting(
      new o("Direction", "Up throws straight down for height; Forward throws behind you so the burst pushes you where you look", "Up", "Up", "Forward")
   );
   private final o Z = this.addSetting(
      new o(
         "Jump",
         "With Throw jumps as you throw, so the push adds to the jump; Timed jumps just before the push arrives, from your ping and measured hops; Off never jumps",
         "With Throw",
         "With Throw",
         "Timed",
         "Off"
      )
   );
   private final m z = this.addSetting(new m("Swap Back", "Return to your previous hotbar slot the tick after the throw", true));
   private final b y = this.addSetting(
      new b(
         "Turn Speed",
         "Most the player turns per tick for the throw; your camera stays on your look. 180 with Humanize off turns at once",
         45.0,
         10.0,
         180.0,
         5.0
      )
   );
   private final b c = this.addSetting(
      new b("Return Speed", "Most the player turns per tick back to your look after the throw. 180 returns at once", 35.0, 10.0, 180.0, 5.0)
   );
   private final m E = this.addSetting(
      new m("Humanize", "Turn like a hand on a mouse: speed up, ease in, drift a little and move in whole sensitivity steps. Off turns in straight lines", true)
   );
   private final _b R = new _b();
   private final s6 B = new s6(this);
   private static final Map<String, Integer> U = new HashMap<>();
   private WindChargeHopModule.t M = WindChargeHopModule.t.C;
   private boolean e;
   private LocalPlayer A;
   private ClientLevel C;
   private dg.t F;
   private long d;
   private long b = Long.MIN_VALUE;
   private int P;
   private int o;
   private double u;
   private double Y;
   private double X;
   private boolean f;
   private float J;
   private float n;
   private final Set<Integer> g = new HashSet<>();
   private int s;
   private String j = "Set a Hop Bind";
   private int q;

   public WindChargeHopModule() {
      super("Wind Charge Hop", "Throw a wind charge at the ground for a timed boost, with a silent turn", Category.MOVEMENT, -1);
      m = this;
      this.Q.w(var1 -> this.o("Direction changed"));
      this.K.w(var1 -> this.o("Bind changed"));
   }

   public static WindChargeHopModule Q() {
      return m;
   }

   public _b x() {
      return this.R;
   }

   public double t() {
      return this.c.getValue();
   }

   public boolean c() {
      return this.E.getValue();
   }

   @Override
   public Module.a getTickPhase() {
      return Module.a.T;
   }

   @Override
   public Set<l> getNetworkImpacts() {
      return Set.of(l.F, l.k);
   }

   @Override
   public String getStatus() {
      return this.isEnabled() ? this.j : "";
   }

   public boolean D() {
      return !this.isEnabled()
         ? false
         : this.isRuntimeActive() && this.A != null && this.A == mc.player && this.C == mc.level && (this.M == WindChargeHopModule.t.f || this.d == this.b);
   }

   public static void t(int var0) {
      if (m != null && m.isEnabled()) {
         WindChargeHopModule var1 = m;
         if (var1 != null && var1.isRuntimeActive() && var0 > 0 && var0 == var1.K.getValue()) {
            var1.e = (boolean)1;
         }
      }
   }

   @Override
   public void onTick() {
      if (!this.isInGame()) {
         this.o("Waiting for a world");
      } else {
         if (this.A != mc.player || this.C != mc.level) {
            this.o("World changed");
            this.A = mc.player;
            this.C = mc.level;
            this.o = 0;
         }

         this.d++;
         boolean var9 = false;
         int var8 = this.o;
         if ((-Integer.signum(var8) >>> 31 ^ 1) == 0) {
            this.A();
         }

         if (this.M != WindChargeHopModule.t.f && this.B.j() && this.d > this.b) {
            this.U();
         }

         byte var1 = this.e;
         this.e = (boolean)0;
         if (this.M == WindChargeHopModule.t.C) {
            boolean var11 = false;
            byte var10 = var1;
            int var12 = Integer.signum(var10);
            if (((var12 | -var12) >>> 31 ^ 1) == 0) {
               this.q();
            } else if (this.K.getValue() < 0) {
               this.j = "Set a Hop Bind";
            }
         } else if (this.M == WindChargeHopModule.t.M) {
            this.b();
         } else {
            String var2 = this.q();
            if (var2 != null) {
               this.o(var2);
            } else {
               dg.t var3 = dg.h(
                  this.A.position(), this.A.getEyeHeight(), this.A.getDeltaMovement(), this.A.onGround(), this.j(), this.m()[0], this.z(), this::Z
               );
               if (var3 == null) {
                  this.o("No ground close enough for a boost");
               } else {
                  this.F = var3;
                  this.d();
                  _p.w(this, this::H, () -> this.M == WindChargeHopModule.t.f && this.q() == null && this.k() && this.x());
                  int var10000 = ++this.q;
                  byte var5 = 30;
                  int var4 = var10000;
                  if ((-Integer.compare(var4, var5) >>> 31 ^ 1) == 0) {
                     int var10001 = this.x();
                     boolean var7 = false;
                     byte var6 = (byte)var10001;
                     var10001 = Integer.signum(var6);
                     this.o(((var10001 | -var10001) >>> 31 ^ 1) == 0 ? "The throw could not go out" : "The turn did not reach a throw angle in time");
                  }
               }
            }
         }
      }
   }

   private void q() {
      String var1 = this.q();
      if (var1 == null) {
         var1 = this.o();
      }

      if (var1 != null) {
         this.j = var1;
         _q.z("WindHop", "Hop refused: {}", var1);
      } else {
         LocalPlayer var2 = this.A;
         float var3 = this.m()[0];
         int var4 = !this.Z.w("Off") && var2.onGround() ? 1 : 0;
         this.F = dg.h(var2.position(), var2.getEyeHeight(), var2.getDeltaMovement(), var2.onGround(), this.j(), var3, this.z(), this::Z);
         if (this.F == null) {
            this.j = "No ground close enough for a boost";
            _q.z("WindHop", "Hop refused: no burst within {} blocks of you", 2.4);
         } else {
            this.M = WindChargeHopModule.t.f;
            this.q = 0;
            ThreadLocalRandom var5 = ThreadLocalRandom.current();
            this.J = this.z() ? (float)(var5.nextDouble() * 6.0 - 3.0) : 0.0F;
            this.n = this.z() ? (float)(var5.nextDouble() * 4.0 - 2.0) : (float)(-(var5.nextDouble() * 3.0));
            this.R.T();
            this.j();
            _q.z(
               "WindHop",
               "Hop: {}, best throw yaw {} pitch {}, burst after {} ticks at {}, predicted push {} up {} along ({} blocks/tick), jump {}; turn {}/tick, return {}/tick, humanize {}",
               this.Q.getValue(),
               V(this.F.yaw()),
               V(this.F.pitch()),
               this.F.ticks(),
               g(this.F.burst()),
               V(this.F.push().y),
               V(Math.hypot(this.F.push().x, this.F.push().z)),
               V(this.F.push().length()),
               var4 != 0 ? this.Z.getValue() : "none",
               this.y.getValue().intValue(),
               this.c.getValue().intValue(),
               this.E.getValue()
            );
            this.d();
            _p.w(this, this::H, () -> this.M == WindChargeHopModule.t.f && this.q() == null && this.k() && this.x());
         }
      }
   }

   private boolean z() {
      return this.Q.w("Forward");
   }

   private boolean j() {
      return this.Z.w("With Throw") && this.A.onGround();
   }

   private dg.t H() {
      return dg.s(this.A.position(), this.A.getEyeHeight(), this.A.getDeltaMovement(), this.A.onGround(), this.j(), this.A.getYRot(), this.A.getXRot(), this::Z);
   }

   private boolean x() {
      return dg.N(this.F, this.H(), this.m()[0], this.z());
   }

   private float[] Q() {
      return !this.z()
         ? new float[]{this.A.getYRot(), Math.min(90.0F, this.F.pitch() + this.n)}
         : new float[]{this.F.yaw() + this.J, Mth.clamp(this.F.pitch() + this.n, -90.0F, 90.0F)};
   }

   private void j() {
      int var1 = vi.u(Items.WIND_CHARGE, true);
      if (var1 >= 0 && var1 != this.A.getInventory().getSelectedSlot() && this.B.h(this.A, var1)) {
         if (mc.gameMode != null) {
            ((q9b8c1cd7fe41113a)mc.gameMode).larpSyncCarriedItem();
         }
      }
   }

   private dg.i Z(Vec3 var1, Vec3 var2) {
      BlockHitResult var3 = this.C.clip(new ClipContext(var1, var2, Block.COLLIDER, Fluid.NONE, this.A));
      return var3.getType() == Type.MISS ? null : new dg.i(var3.getLocation(), Vec3.atLowerCornerOf(var3.getDirection().getUnitVec3i()));
   }

   private void d() {
      if (_p.Q(this)) {
         ij.V(ij.r.q);
         if (ij.L() == ij.r.q) {
            float[] var1 = this.Q();
            float[] var2 = this.R.i(this.A.getYRot(), this.A.getXRot(), var1[0], var1[1], this.y.getValue(), this.E.getValue());
            b6.P(this, b6.w.G, var2[0], var2[1], 1.0);
         }
      }
   }

   private void H() {
      LocalPlayer var1 = this.A;
      dg.t var2 = this.H();
      if (this.M == WindChargeHopModule.t.f && dg.N(this.F, var2, this.m()[0], this.z())) {
         int var3 = !this.Z.w("Off") && var1.onGround() ? 1 : 0;
         if (this.Z.w("With Throw") && var3 != 0) {
            var1.jumpFromGround();
         }

         this.g.clear();

         for (Entity var5 : mc.level.entitiesForRendering()) {
            if (var5 instanceof WindCharge var6 && var6.getOwner() == var1) {
               this.g.add(var6.getId());
            }
         }

         if (!this.L()) {
            this.o("The wind charge throw was rejected");
         } else {
            this.b = this.d;
            this.s = -1;
            this.u = this.Y = var1.getY();
            this.X = var1.getDeltaMovement().y;
            this.f = (boolean)0;
            this.o = 40;
            String var8 = this.x();
            int var9 = U.getOrDefault(var8, 0);
            this.P = dg.T(var2.ticks(), this.A(), var9);
            if (this.Z.w("Timed") && var3 != 0 && this.P > 0) {
               this.M = WindChargeHopModule.t.M;
               this.j = "Wind charge thrown; jump in " + this.P + " ticks";
            } else {
               if (this.Z.w("Timed") && var3 != 0) {
                  var1.jumpFromGround();
               }

               this.M = WindChargeHopModule.t.C;
               this.j = "Wind charge thrown";
            }

            double var10 = dg.z(this.F, this.m()[0], this.z());
            _q.z(
               "WindHop",
               "Thrown at yaw {} pitch {} ({} of the best push) after a turn of {}; jump {} ({} ticks from {}), round trip {} ticks",
               V(var1.getYRot()),
               V(var1.getXRot()),
               var10 > 0.0 ? String.format(Locale.ROOT, "%.0f%%", 100.0 * dg.z(var2, this.m()[0], this.z()) / var10) : "?",
               this.R.m(),
               var3 != 0 ? this.Z.getValue() : "none",
               this.P,
               var9 > 0 ? "measured hops" : "ping",
               this.A()
            );
         }
      }
   }

   private void b() {
      if (this.d - this.b >= this.P) {
         this.M = WindChargeHopModule.t.C;
         if (this.A.onGround() && !this.f) {
            this.A.jumpFromGround();
            this.j = "Jumped for the burst";
         } else {
            this.j = this.f ? "The push came before the jump" : "Left the ground before the jump";
         }
      }
   }

   private void A() {
      LocalPlayer var1 = this.A;
      if (this.s < 0) {
         for (Entity var4 : mc.level.entitiesForRendering()) {
            if (var4 instanceof WindCharge var2 && var2.getOwner() == var1 && !this.g.contains(var2.getId())) {
               this.s = (int)(this.d - this.b);
               break;
            }
         }
      }

      double var7 = var1.getDeltaMovement().y;
      if (!this.f && var7 - this.X > 0.5) {
         this.f = (boolean)1;
         int var8 = (int)(this.d - this.b);
         U.put(this.x(), var8);
         _q.z("WindHop", "Push arrived {} ticks after the throw: vertical speed {} to {}", var8, V(this.X), V(var7));
      }

      this.X = var7;
      this.Y = Math.max(this.Y, var1.getY());
      if (--this.o <= 0) {
         String var9 = this.s >= 0
            ? "your wind charge appeared " + this.s + " ticks after the throw"
            : (
               this.f
                  ? "the burst pushed you, so the server spawned the throw"
                  : "no wind charge from you was seen and nothing pushed you: the server may not have spawned the throw (an anticheat or plugin may have cancelled it), or it burst between two ticks and missed you"
            );
         String var5 = !this.f && this.s >= 0 ? "; no push seen, so the burst missed you or this server changes wind charge knockback" : "";
         String var6 = vm.M();
         _q.z("WindHop", "Hop result: rose {} blocks; {}{}; server {}", V(this.Y - this.u), var9, var5, var6 == null ? "singleplayer" : var6);
         if (this.M == WindChargeHopModule.t.C) {
            this.j = String.format(Locale.ROOT, "Hopped %.1f blocks", this.Y - this.u);
         }
      }
   }

   private String q() {
      if (!_p.g() || mc.gui.screen() != null) {
         return "Paused: a screen is open";
      } else if (!mc.isWindowActive()) {
         return "Paused: the window lost focus";
      } else if (ij.L() != ij.r.q && !ij.a(ij.r.q)) {
         return "Paused: Freecam, Freelook or a pearl catch holds the view";
      } else if (this.A.isPassenger()) {
         return "Not while riding";
      } else if (this.A.isInWater() || this.A.isEyeInFluid(FluidTags.WATER) || this.A.isInLava()) {
         return "Not in water or lava";
      } else {
         return this.A.isFallFlying() ? "Not while gliding" : null;
      }
   }

   private String o() {
      if (this.A.isUsingItem()) {
         return "Stop using your item first";
      } else if (AuraModule.K()) {
         return "Paused while Kill Aura lowers your shield";
      } else {
         int var1 = vi.u(Items.WIND_CHARGE, true);
         if (var1 == -1) {
            return "No wind charges in your hotbar or hands";
         } else {
            ItemStack var2 = this.R(var1);
            if (!var2.isItemEnabled(this.C.enabledFeatures())) {
               return "Wind charges are disabled here";
            } else {
               float var3 = this.A.getCooldowns().getCooldownPercent(var2, 0.0F);
               if (var3 > 0.0F) {
                  UseCooldown var4 = (UseCooldown)var2.get(DataComponents.USE_COOLDOWN);
                  return String.format(Locale.ROOT, "Wind charge cooling down (%.2f s)", var3 * (var4 == null ? 10 : var4.ticks()) / 20.0);
               } else {
                  return var1 >= 0 && var1 != this.A.getInventory().getSelectedSlot() && !s6.L(this) ? "Hotbar busy" : null;
               }
            }
         }
      }
   }

   private ItemStack R(int var1) {
      return var1 == -2 ? this.A.getOffhandItem() : this.A.getInventory().getItem(var1);
   }

   private boolean k() {
      int var1 = vi.u(Items.WIND_CHARGE, true);
      return var1 != -1
         && !this.A.getCooldowns().isOnCooldown(this.R(var1))
         && !AuraModule.K()
         && (var1 == -2 || var1 == this.A.getInventory().getSelectedSlot() || s6.L(this));
   }

   private boolean L() {
      int var1 = vi.u(Items.WIND_CHARGE, true);
      if (var1 == -1) {
         return false;
      }

      InteractionHand var2 = var1 == -2 ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND;
      if (var1 >= 0 && var1 != this.A.getInventory().getSelectedSlot() && !this.B.h(this.A, var1)) {
         return false;
      }

      boolean var3 = mc.gameMode.useItem(this.A, var2).consumesAction();
      if (var3) {
         h8.i(this.A, var2);
         ((qc90ed4dd4cb15de4)mc).larpUseDelay(4);
      }

      return var3;
   }

   private void U() {
      if (this.B.j()) {
         int var1 = this.A == null ? -1 : this.A.getInventory().getSelectedSlot();
         this.B.X(getMc(), this.z.getValue());
         if (this.A != null && this.A == mc.player && mc.gameMode != null && var1 != this.A.getInventory().getSelectedSlot()) {
            ((q9b8c1cd7fe41113a)mc.gameMode).larpSyncCarriedItem();
         }
      }
   }

   private float[] m() {
      return !ij.g() ? new float[]{this.A.getYRot(), this.A.getXRot()} : ij.p(ij.D(), ij.J(), mc.options.getCameraType().isMirrored());
   }

   private int A() {
      ClientPacketListener var1 = mc.getConnection();
      PlayerInfo var2 = var1 != null && this.A != null ? var1.getPlayerInfo(this.A.getUUID()) : null;
      return var2 == null ? 0 : Math.max(0, Math.round(var2.getLatency() / 50.0F));
   }

   private String x() {
      ServerData var1 = mc.getCurrentServer();
      return var1 != null ? var1.ip : (mc.hasSingleplayerServer() ? "singleplayer" : "this server");
   }

   private void o(String var1) {
      int var2 = this.M != WindChargeHopModule.t.C ? 1 : 0;
      _p.e(this);
      this.U();
      this.M = WindChargeHopModule.t.C;
      ij.P(ij.r.q, this.R, this.c.getValue(), this.E.getValue());
      if (var2 != 0) {
         this.j = var1;
         _q.z("WindHop", "Hop cancelled: {}", var1);
      }
   }

   private static String V(double var0) {
      return String.format(Locale.ROOT, "%.2f", var0);
   }

   private static String g(Vec3 var0) {
      return String.format(Locale.ROOT, "(%.1f, %.1f, %.1f)", var0.x, var0.y, var0.z);
   }

   @Override
   public void onEnable() {
      this.A = null;
      this.C = null;
      this.M = WindChargeHopModule.t.C;
      this.b = Long.MIN_VALUE;
      this.o = 0;
      this.j = this.K.getValue() < 0 ? "Set a Hop Bind" : "Ready";
      _q.z(
         "WindHop",
         "Enabled; direction={}, jump={}, swap back={}, turn speed={}, return speed={}, humanize={}",
         this.Q.getValue(),
         this.Z.getValue(),
         this.z.getValue(),
         this.y.getValue().intValue(),
         this.c.getValue().intValue(),
         this.E.getValue()
      );
   }

   @Override
   public void onDisable() {
      this.o("Disabled");
      this.A = null;
      this.C = null;
      _q.z("WindHop", "Disabled; pending throw and view released");
   }

   private enum t {
      C,
      f,
      M;
   }
}
