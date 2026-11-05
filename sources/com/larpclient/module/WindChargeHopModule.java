package com.larpclient.module;

import com.larp.obf.qbac0b262827a15ac.q099a584e4baa1bb9;
import com.larp.obf.qbac0b262827a15ac.qb131e08b200c8a57;
import com.larpclient._8;
import com.larpclient._d;
import com.larpclient._m;
import com.larpclient.c2;
import com.larpclient.h7;
import com.larpclient.h_;
import com.larpclient.m9;
import com.larpclient.mt;
import com.larpclient.ti;
import com.larpclient.tq;
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
   private static final int n = 40;
   private static final int m = 30;
   private static WindChargeHopModule N;
   private final dv A = this.addSetting(new dv("Hop Bind", "Press for one wind charge hop; separate from the module toggle", -1));
   private final d6 Y = this.addSetting(
      new d6("Direction", "Up throws straight down for height; Forward throws behind you so the burst pushes you where you look", "Up", "Up", "Forward")
   );
   private final d6 l = this.addSetting(
      new d6(
         "Jump",
         "With Throw jumps as you throw, so the push adds to the jump; Timed jumps just before the push arrives, from your ping and measured hops; Off never jumps",
         "With Throw",
         "With Throw",
         "Timed",
         "Off"
      )
   );
   private final dh B = this.addSetting(new dh("Swap Back", "Return to your previous hotbar slot the tick after the throw", true));
   private final d9 Q = this.addSetting(
      new d9(
         "Turn Speed",
         "Most the player turns per tick for the throw; your camera stays on your look. 180 with Humanize off turns at once",
         45.0,
         10.0,
         180.0,
         5.0
      )
   );
   private final d9 g = this.addSetting(
      new d9("Return Speed", "Most the player turns per tick back to your look after the throw. 180 returns at once", 35.0, 10.0, 180.0, 5.0)
   );
   private final dh S = this.addSetting(
      new dh(
         "Humanize", "Turn like a hand on a mouse: speed up, ease in, drift a little and move in whole sensitivity steps. Off turns in straight lines", true
      )
   );
   private final h_ J = new h_();
   private final ti H = new ti(this);
   private static final Map<String, Integer> o = new HashMap<>();
   private WindChargeHopModule.w j = WindChargeHopModule.w.O;
   private boolean z;
   private LocalPlayer U;
   private ClientLevel C;
   private h7.z G;
   private long I;
   private long O = Long.MIN_VALUE;
   private int i;
   private int k;
   private double P;
   private double c;
   private double d;
   private boolean Z;
   private float L;
   private float s;
   private final Set<Integer> X = new HashSet<>();
   private int x;
   private String q = "Set a Hop Bind";
   private int u;

   public WindChargeHopModule() {
      super("Wind Charge Hop", "Throw a wind charge at the ground for a timed boost, with a silent turn", Category.MOVEMENT, -1);
      N = this;
      this.Y.h(var1 -> this.Z("Direction changed"));
      this.A.h(var1 -> this.Z("Bind changed"));
   }

   public static WindChargeHopModule C() {
      return N;
   }

   public h_ P() {
      return this.J;
   }

   public double e() {
      return this.g.P();
   }

   public boolean y() {
      return this.S.P();
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
      return this.isEnabled() ? this.q : "";
   }

   public boolean g() {
      return !this.isEnabled()
         ? false
         : this.isRuntimeActive() && this.U != null && this.U == mc.player && this.C == mc.level && (this.j == WindChargeHopModule.w.m || this.I == this.O);
   }

   public static void W(int var0) {
      if (N != null && N.isEnabled()) {
         WindChargeHopModule var1 = N;
         if (var1 != null && var1.isRuntimeActive() && var0 > 0 && var0 == var1.A.P()) {
            var1.z = (boolean)1;
         }
      }
   }

   @Override
   public void onTick() {
      if (!this.isInGame()) {
         this.Z("Waiting for a world");
      } else {
         if (this.U != mc.player || this.C != mc.level) {
            this.Z("World changed");
            this.U = mc.player;
            this.C = mc.level;
            this.k = 0;
         }

         this.I++;
         boolean var9 = false;
         int var8 = this.k;
         if ((-Integer.signum(var8) >>> 31 ^ 1) == 0) {
            this.B();
         }

         if (this.j != WindChargeHopModule.w.m && this.H.k() && this.I > this.O) {
            this.u();
         }

         byte var1 = this.z;
         this.z = (boolean)0;
         if (this.j == WindChargeHopModule.w.O) {
            boolean var11 = false;
            byte var10 = var1;
            int var12 = Integer.signum(var10);
            if (((var12 | -var12) >>> 31 ^ 1) == 0) {
               this.P();
            } else if (this.A.P() < 0) {
               this.q = "Set a Hop Bind";
            }
         } else if (this.j == WindChargeHopModule.w.c) {
            this.s();
         } else {
            String var2 = this.z();
            if (var2 != null) {
               this.Z(var2);
            } else {
               h7.z var3 = h7.q(
                  this.U.position(), this.U.getEyeHeight(), this.U.getDeltaMovement(), this.U.onGround(), this.G(), this.U()[0], this.z(), this::y
               );
               if (var3 == null) {
                  this.Z("No ground close enough for a boost");
               } else {
                  this.G = var3;
                  this.M();
                  _m.F(this, this::C, () -> this.j == WindChargeHopModule.w.m && this.z() == null && this.J() && this.e());
                  int var10000 = ++this.u;
                  byte var5 = 30;
                  int var4 = var10000;
                  if ((-Integer.compare(var4, var5) >>> 31 ^ 1) == 0) {
                     int var10001 = this.e();
                     boolean var7 = false;
                     byte var6 = (byte)var10001;
                     var10001 = Integer.signum(var6);
                     this.Z(((var10001 | -var10001) >>> 31 ^ 1) == 0 ? "The throw could not go out" : "The turn did not reach a throw angle in time");
                  }
               }
            }
         }
      }
   }

   private void P() {
      String var1 = this.z();
      if (var1 == null) {
         var1 = this.X();
      }

      if (var1 != null) {
         this.q = var1;
         mt.K("WindHop", "Hop refused: {}", var1);
      } else {
         LocalPlayer var2 = this.U;
         float var3 = this.U()[0];
         int var4 = !this.l.S("Off") && var2.onGround() ? 1 : 0;
         this.G = h7.q(var2.position(), var2.getEyeHeight(), var2.getDeltaMovement(), var2.onGround(), this.G(), var3, this.z(), this::y);
         if (this.G == null) {
            this.q = "No ground close enough for a boost";
            mt.K("WindHop", "Hop refused: no burst within {} blocks of you", 2.4);
         } else {
            this.j = WindChargeHopModule.w.m;
            this.u = 0;
            ThreadLocalRandom var5 = ThreadLocalRandom.current();
            this.L = this.z() ? (float)(var5.nextDouble() * 6.0 - 3.0) : 0.0F;
            this.s = this.z() ? (float)(var5.nextDouble() * 4.0 - 2.0) : (float)(-(var5.nextDouble() * 3.0));
            this.J.b();
            this.m();
            mt.K(
               "WindHop",
               "Hop: {}, best throw yaw {} pitch {}, burst after {} ticks at {}, predicted push {} up {} along ({} blocks/tick), jump {}; turn {}/tick, return {}/tick, humanize {}",
               this.Y.P(),
               m(this.G.yaw()),
               m(this.G.pitch()),
               this.G.ticks(),
               Q(this.G.burst()),
               m(this.G.push().y),
               m(Math.hypot(this.G.push().x, this.G.push().z)),
               m(this.G.push().length()),
               var4 != 0 ? this.l.P() : "none",
               this.Q.P().intValue(),
               this.g.P().intValue(),
               this.S.P()
            );
            this.M();
            _m.F(this, this::C, () -> this.j == WindChargeHopModule.w.m && this.z() == null && this.J() && this.e());
         }
      }
   }

   private boolean z() {
      return this.Y.S("Forward");
   }

   private boolean G() {
      return this.l.S("With Throw") && this.U.onGround();
   }

   private h7.z u() {
      return h7.L(this.U.position(), this.U.getEyeHeight(), this.U.getDeltaMovement(), this.U.onGround(), this.G(), this.U.getYRot(), this.U.getXRot(), this::y);
   }

   private boolean e() {
      return h7.p(this.G, this.u(), this.U()[0], this.z());
   }

   private float[] f() {
      return !this.z()
         ? new float[]{this.U.getYRot(), Math.min(90.0F, this.G.pitch() + this.s)}
         : new float[]{this.G.yaw() + this.L, Mth.clamp(this.G.pitch() + this.s, -90.0F, 90.0F)};
   }

   private void m() {
      int var1 = _d.B(Items.WIND_CHARGE, true);
      if (var1 >= 0 && var1 != this.U.getInventory().getSelectedSlot() && this.H.e(this.U, var1)) {
         if (mc.gameMode != null) {
            ((q099a584e4baa1bb9)mc.gameMode).larpSyncCarriedItem();
         }
      }
   }

   private h7.y y(Vec3 var1, Vec3 var2) {
      BlockHitResult var3 = this.C.clip(new ClipContext(var1, var2, Block.COLLIDER, Fluid.NONE, this.U));
      return var3.getType() == Type.MISS ? null : new h7.y(var3.getLocation(), Vec3.atLowerCornerOf(var3.getDirection().getUnitVec3i()));
   }

   private void M() {
      if (_m.f(this)) {
         tq.i(tq.y.B);
         if (tq.l() == tq.y.B) {
            float[] var1 = this.f();
            float[] var2 = this.J.V(this.U.getYRot(), this.U.getXRot(), var1[0], var1[1], this.Q.P(), this.S.P());
            m9.k(this, m9.s.q, var2[0], var2[1], 1.0);
         }
      }
   }

   private void C() {
      LocalPlayer var1 = this.U;
      h7.z var2 = this.u();
      if (this.j == WindChargeHopModule.w.m && h7.p(this.G, var2, this.U()[0], this.z())) {
         int var3 = !this.l.S("Off") && var1.onGround() ? 1 : 0;
         if (this.l.S("With Throw") && var3 != 0) {
            var1.jumpFromGround();
         }

         this.X.clear();

         for (Entity var5 : mc.level.entitiesForRendering()) {
            if (var5 instanceof WindCharge var6 && var6.getOwner() == var1) {
               this.X.add(var6.getId());
            }
         }

         if (!this.h()) {
            this.Z("The wind charge throw was rejected");
         } else {
            this.O = this.I;
            this.x = -1;
            this.P = this.c = var1.getY();
            this.d = var1.getDeltaMovement().y;
            this.Z = (boolean)0;
            this.k = 40;
            String var8 = this.Y();
            int var9 = o.getOrDefault(var8, 0);
            this.i = h7.H(var2.ticks(), this.Z(), var9);
            if (this.l.S("Timed") && var3 != 0 && this.i > 0) {
               this.j = WindChargeHopModule.w.c;
               this.q = "Wind charge thrown; jump in " + this.i + " ticks";
            } else {
               if (this.l.S("Timed") && var3 != 0) {
                  var1.jumpFromGround();
               }

               this.j = WindChargeHopModule.w.O;
               this.q = "Wind charge thrown";
            }

            double var10 = h7.E(this.G, this.U()[0], this.z());
            mt.K(
               "WindHop",
               "Thrown at yaw {} pitch {} ({} of the best push) after a turn of {}; jump {} ({} ticks from {}), round trip {} ticks",
               m(var1.getYRot()),
               m(var1.getXRot()),
               var10 > 0.0 ? String.format(Locale.ROOT, "%.0f%%", 100.0 * h7.E(var2, this.U()[0], this.z()) / var10) : "?",
               this.J.H(),
               var3 != 0 ? this.l.P() : "none",
               this.i,
               var9 > 0 ? "measured hops" : "ping",
               this.Z()
            );
         }
      }
   }

   private void s() {
      if (this.I - this.O >= this.i) {
         this.j = WindChargeHopModule.w.O;
         if (this.U.onGround() && !this.Z) {
            this.U.jumpFromGround();
            this.q = "Jumped for the burst";
         } else {
            this.q = this.Z ? "The push came before the jump" : "Left the ground before the jump";
         }
      }
   }

   private void B() {
      LocalPlayer var1 = this.U;
      if (this.x < 0) {
         for (Entity var4 : mc.level.entitiesForRendering()) {
            if (var4 instanceof WindCharge var2 && var2.getOwner() == var1 && !this.X.contains(var2.getId())) {
               this.x = (int)(this.I - this.O);
               break;
            }
         }
      }

      double var7 = var1.getDeltaMovement().y;
      if (!this.Z && var7 - this.d > 0.5) {
         this.Z = (boolean)1;
         int var8 = (int)(this.I - this.O);
         o.put(this.Y(), var8);
         mt.K("WindHop", "Push arrived {} ticks after the throw: vertical speed {} to {}", var8, m(this.d), m(var7));
      }

      this.d = var7;
      this.c = Math.max(this.c, var1.getY());
      if (--this.k <= 0) {
         String var9 = this.x >= 0
            ? "your wind charge appeared " + this.x + " ticks after the throw"
            : (
               this.Z
                  ? "the burst pushed you, so the server spawned the throw"
                  : "no wind charge from you was seen and nothing pushed you: the server may not have spawned the throw (an anticheat or plugin may have cancelled it), or it burst between two ticks and missed you"
            );
         String var5 = !this.Z && this.x >= 0 ? "; no push seen, so the burst missed you or this server changes wind charge knockback" : "";
         String var6 = c2.v();
         mt.K("WindHop", "Hop result: rose {} blocks; {}{}; server {}", m(this.c - this.P), var9, var5, var6 == null ? "singleplayer" : var6);
         if (this.j == WindChargeHopModule.w.O) {
            this.q = String.format(Locale.ROOT, "Hopped %.1f blocks", this.c - this.P);
         }
      }
   }

   private String z() {
      if (!_m.z() || mc.gui.screen() != null) {
         return "Paused: a screen is open";
      } else if (!mc.isWindowActive()) {
         return "Paused: the window lost focus";
      } else if (tq.l() != tq.y.B && !tq.y(tq.y.B)) {
         return "Paused: Freecam, Freelook or a pearl catch holds the view";
      } else if (this.U.isPassenger()) {
         return "Not while riding";
      } else if (this.U.isInWater() || this.U.isEyeInFluid(FluidTags.WATER) || this.U.isInLava()) {
         return "Not in water or lava";
      } else {
         return this.U.isFallFlying() ? "Not while gliding" : null;
      }
   }

   private String X() {
      if (this.U.isUsingItem()) {
         return "Stop using your item first";
      } else if (AuraModule.i()) {
         return "Paused while Kill Aura lowers your shield";
      } else {
         int var1 = _d.B(Items.WIND_CHARGE, true);
         if (var1 == -1) {
            return "No wind charges in your hotbar or hands";
         } else {
            ItemStack var2 = this.F(var1);
            if (!var2.isItemEnabled(this.C.enabledFeatures())) {
               return "Wind charges are disabled here";
            } else {
               float var3 = this.U.getCooldowns().getCooldownPercent(var2, 0.0F);
               if (var3 > 0.0F) {
                  UseCooldown var4 = (UseCooldown)var2.get(DataComponents.USE_COOLDOWN);
                  return String.format(Locale.ROOT, "Wind charge cooling down (%.2f s)", var3 * (var4 == null ? 10 : var4.ticks()) / 20.0);
               } else {
                  return var1 >= 0 && var1 != this.U.getInventory().getSelectedSlot() && !ti.V(this) ? "Hotbar busy" : null;
               }
            }
         }
      }
   }

   private ItemStack F(int var1) {
      return var1 == -2 ? this.U.getOffhandItem() : this.U.getInventory().getItem(var1);
   }

   private boolean J() {
      int var1 = _d.B(Items.WIND_CHARGE, true);
      return var1 != -1
         && !this.U.getCooldowns().isOnCooldown(this.F(var1))
         && !AuraModule.i()
         && (var1 == -2 || var1 == this.U.getInventory().getSelectedSlot() || ti.V(this));
   }

   private boolean h() {
      int var1 = _d.B(Items.WIND_CHARGE, true);
      if (var1 == -1) {
         return false;
      }

      InteractionHand var2 = var1 == -2 ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND;
      if (var1 >= 0 && var1 != this.U.getInventory().getSelectedSlot() && !this.H.e(this.U, var1)) {
         return false;
      }

      boolean var3 = mc.gameMode.useItem(this.U, var2).consumesAction();
      if (var3) {
         _8.G(this.U, var2);
         ((qb131e08b200c8a57)mc).larpUseDelay(4);
      }

      return var3;
   }

   private void u() {
      if (this.H.k()) {
         int var1 = this.U == null ? -1 : this.U.getInventory().getSelectedSlot();
         this.H.W(getMc(), this.B.P());
         if (this.U != null && this.U == mc.player && mc.gameMode != null && var1 != this.U.getInventory().getSelectedSlot()) {
            ((q099a584e4baa1bb9)mc.gameMode).larpSyncCarriedItem();
         }
      }
   }

   private float[] U() {
      return !tq.q() ? new float[]{this.U.getYRot(), this.U.getXRot()} : tq.i(tq.J(), tq.F(), mc.options.getCameraType().isMirrored());
   }

   private int Z() {
      ClientPacketListener var1 = mc.getConnection();
      PlayerInfo var2 = var1 != null && this.U != null ? var1.getPlayerInfo(this.U.getUUID()) : null;
      return var2 == null ? 0 : Math.max(0, Math.round(var2.getLatency() / 50.0F));
   }

   private String Y() {
      ServerData var1 = mc.getCurrentServer();
      return var1 != null ? var1.ip : (mc.hasSingleplayerServer() ? "singleplayer" : "this server");
   }

   private void Z(String var1) {
      int var2 = this.j != WindChargeHopModule.w.O ? 1 : 0;
      _m.w(this);
      this.u();
      this.j = WindChargeHopModule.w.O;
      tq.c(tq.y.B, this.J, this.g.P(), this.S.P());
      if (var2 != 0) {
         this.q = var1;
         mt.K("WindHop", "Hop cancelled: {}", var1);
      }
   }

   private static String m(double var0) {
      return String.format(Locale.ROOT, "%.2f", var0);
   }

   private static String Q(Vec3 var0) {
      return String.format(Locale.ROOT, "(%.1f, %.1f, %.1f)", var0.x, var0.y, var0.z);
   }

   @Override
   public void onEnable() {
      this.U = null;
      this.C = null;
      this.j = WindChargeHopModule.w.O;
      this.O = Long.MIN_VALUE;
      this.k = 0;
      this.q = this.A.P() < 0 ? "Set a Hop Bind" : "Ready";
      mt.K(
         "WindHop",
         "Enabled; direction={}, jump={}, swap back={}, turn speed={}, return speed={}, humanize={}",
         this.Y.P(),
         this.l.P(),
         this.B.P(),
         this.Q.P().intValue(),
         this.g.P().intValue(),
         this.S.P()
      );
   }

   @Override
   public void onDisable() {
      this.Z("Disabled");
      this.U = null;
      this.C = null;
      mt.K("WindHop", "Disabled; pending throw and view released");
   }

   private enum w {
      O,
      m,
      c;
   }
}
