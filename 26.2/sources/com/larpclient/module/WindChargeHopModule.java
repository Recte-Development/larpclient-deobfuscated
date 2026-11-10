package com.larpclient.module;

import com.larp.obf.qa300483f54d804a3.q541491ff38fb1637;
import com.larp.obf.qa300483f54d804a3.q7ae1dbb86ec789d8;
import com.larpclient.hu;
import com.larpclient.j6;
import com.larpclient.ks;
import com.larpclient.l6;
import com.larpclient.lm;
import com.larpclient.p_;
import com.larpclient.t1;
import com.larpclient.ta;
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
   private static final int O = 40;
   private static final int m = 30;
   private static WindChargeHopModule e;
   private final v x = this.addSetting(new v("Hop Bind", "Press for one wind charge hop; separate from the module toggle", -1));
   private final l u = this.addSetting(
      new l("Direction", "Up throws straight down for height; Forward throws behind you so the burst pushes you where you look", "Up", "Up", "Forward")
   );
   private final l U = this.addSetting(
      new l(
         "Jump",
         "With Throw jumps as you throw, so the push adds to the jump; Timed jumps just before the push arrives, from your ping and measured hops; Off never jumps",
         "With Throw",
         "With Throw",
         "Timed",
         "Off"
      )
   );
   private final s G = this.addSetting(new s("Swap Back", "Return to your previous hotbar slot the tick after the throw", true));
   private final e I = this.addSetting(
      new e(
         "Turn Speed",
         "Most the player turns per tick for the throw; your camera stays on your look. 180 with Humanize off turns at once",
         45.0,
         10.0,
         180.0,
         5.0
      )
   );
   private final e w = this.addSetting(
      new e("Return Speed", "Most the player turns per tick back to your look after the throw. 180 returns at once", 35.0, 10.0, 180.0, 5.0)
   );
   private final s R = this.addSetting(
      new s("Humanize", "Turn like a hand on a mouse: speed up, ease in, drift a little and move in whole sensitivity steps. Off turns in straight lines", true)
   );
   private final lm d = new lm();
   private final hu j = new hu(this);
   private static final Map<String, Integer> L = new HashMap<>();
   private WindChargeHopModule.g n = WindChargeHopModule.g.f;
   private boolean M;
   private LocalPlayer C;
   private ClientLevel Q;
   private com.larpclient.ts.q N;
   private long X;
   private long Z = Long.MIN_VALUE;
   private int z;
   private int g;
   private double v;
   private double s;
   private double T;
   private boolean a;
   private float E;
   private float y;
   private final Set<Integer> K = new HashSet<>();
   private int l;
   private String D = "Set a Hop Bind";
   private int W;

   public WindChargeHopModule() {
      super("Wind Charge Hop", "Throw a wind charge at the ground for a timed boost, with a silent turn", Category.MOVEMENT, -1);
      e = this;
      this.u.o(var1 -> this.J("Direction changed"));
      this.x.o(var1 -> this.J("Bind changed"));
   }

   public static WindChargeHopModule h() {
      return e;
   }

   public lm t() {
      return this.d;
   }

   public double r() {
      return this.w.getValue();
   }

   public boolean N() {
      return this.R.getValue();
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
      return this.isEnabled() ? this.D : "";
   }

   public boolean h() {
      return !this.isEnabled()
         ? false
         : this.isRuntimeActive() && this.C != null && this.C == mc.player && this.Q == mc.level && (this.n == WindChargeHopModule.g.M || this.X == this.Z);
   }

   public static void p(int var0) {
      if (e != null && e.isEnabled()) {
         WindChargeHopModule var1 = e;
         if (var1 != null && var1.isRuntimeActive() && var0 > 0 && var0 == var1.x.getValue()) {
            var1.M = (boolean)1;
         }
      }
   }

   @Override
   public void onTick() {
      if (!this.isInGame()) {
         this.J("Waiting for a world");
      } else {
         if (this.C != mc.player || this.Q != mc.level) {
            this.J("World changed");
            this.C = mc.player;
            this.Q = mc.level;
            this.g = 0;
         }

         this.X++;
         boolean var11 = false;
         int var10 = this.g;
         if ((-Integer.signum(var10) >>> 31 ^ 1) == 0) {
            this.M();
         }

         if (this.n != WindChargeHopModule.g.M && this.j.p() && this.X > this.Z) {
            this.V();
         }

         boolean var1 = this.M;
         this.M = (boolean)0;
         if (this.n == WindChargeHopModule.g.f) {
            if (var1) {
               this.k();
            } else {
               int var12 = this.x.getValue();
               boolean var5 = false;
               int var4 = var12;
               if ((Integer.signum(var4) >>> 31 ^ 1) == 0) {
                  this.D = "Set a Hop Bind";
               }
            }
         } else if (this.n == WindChargeHopModule.g.a) {
            this.w();
         } else {
            String var2 = this.y();
            if (var2 != null) {
               this.J(var2);
            } else {
               com.larpclient.ts.q var3 = com.larpclient.ts.o(
                  this.C.position(), this.C.getEyeHeight(), this.C.getDeltaMovement(), this.C.onGround(), this.T(), this.J()[0], this.U(), this::v
               );
               if (var3 == null) {
                  this.J("No ground close enough for a boost");
               } else {
                  this.N = var3;
                  this.L();
                  ta.F(this, this::E, () -> this.n == WindChargeHopModule.g.M && this.y() == null && this.c() && this.J());
                  byte var7 = 1;
                  int var6 = this.W;
                  int var10000 = this.W = var6 + var7;
                  byte var9 = 30;
                  int var8 = var10000;
                  if ((-Integer.compare(var8, var9) >>> 31 ^ 1) == 0) {
                     this.J(this.J() ? "The throw could not go out" : "The turn did not reach a throw angle in time");
                  }
               }
            }
         }
      }
   }

   private void k() {
      String var1 = this.y();
      if (var1 == null) {
         var1 = this.Q();
      }

      if (var1 != null) {
         this.D = var1;
         com.larpclient.h.A("WindHop", "Hop refused: {}", var1);
      } else {
         LocalPlayer var2 = this.C;
         float var3 = this.J()[0];
         int var4 = !this.U.O("Off") && var2.onGround() ? 1 : 0;
         this.N = com.larpclient.ts.o(var2.position(), var2.getEyeHeight(), var2.getDeltaMovement(), var2.onGround(), this.T(), var3, this.U(), this::v);
         if (this.N == null) {
            this.D = "No ground close enough for a boost";
            com.larpclient.h.A("WindHop", "Hop refused: no burst within {} blocks of you", 2.4);
         } else {
            this.n = WindChargeHopModule.g.M;
            this.W = 0;
            ThreadLocalRandom var5 = ThreadLocalRandom.current();
            this.E = this.U() ? (float)(var5.nextDouble() * 6.0 - 3.0) : 0.0F;
            this.y = this.U() ? (float)(var5.nextDouble() * 4.0 - 2.0) : (float)(-(var5.nextDouble() * 3.0));
            this.d.q();
            this.n();
            com.larpclient.h.A(
               "WindHop",
               "Hop: {}, best throw yaw {} pitch {}, burst after {} ticks at {}, predicted push {} up {} along ({} blocks/tick), jump {}; turn {}/tick, return {}/tick, humanize {}",
               this.u.getValue(),
               z(this.N.yaw()),
               z(this.N.pitch()),
               this.N.ticks(),
               Y(this.N.burst()),
               z(this.N.push().y),
               z(Math.hypot(this.N.push().x, this.N.push().z)),
               z(this.N.push().length()),
               var4 != 0 ? this.U.getValue() : "none",
               this.I.getValue().intValue(),
               this.w.getValue().intValue(),
               this.R.getValue()
            );
            this.L();
            ta.F(this, this::E, () -> this.n == WindChargeHopModule.g.M && this.y() == null && this.c() && this.J());
         }
      }
   }

   private boolean U() {
      return this.u.O("Forward");
   }

   private boolean T() {
      return this.U.O("With Throw") && this.C.onGround();
   }

   private com.larpclient.ts.q Y() {
      return com.larpclient.ts.V(
         this.C.position(), this.C.getEyeHeight(), this.C.getDeltaMovement(), this.C.onGround(), this.T(), this.C.getYRot(), this.C.getXRot(), this::v
      );
   }

   private boolean J() {
      return com.larpclient.ts.U(this.N, this.Y(), this.J()[0], this.U());
   }

   private float[] q() {
      return !this.U()
         ? new float[]{this.C.getYRot(), Math.min(90.0F, this.N.pitch() + this.y)}
         : new float[]{this.N.yaw() + this.E, Mth.clamp(this.N.pitch() + this.y, -90.0F, 90.0F)};
   }

   private void n() {
      int var1 = t1.M(Items.WIND_CHARGE, true);
      if (var1 >= 0 && var1 != this.C.getInventory().getSelectedSlot() && this.j.O(this.C, var1)) {
         if (mc.gameMode != null) {
            ((q541491ff38fb1637)mc.gameMode).larpSyncCarriedItem();
         }
      }
   }

   private com.larpclient.ts.u v(Vec3 var1, Vec3 var2) {
      BlockHitResult var3 = this.Q.clip(new ClipContext(var1, var2, Block.COLLIDER, Fluid.NONE, this.C));
      return var3.getType() == Type.MISS ? null : new com.larpclient.ts.u(var3.getLocation(), Vec3.atLowerCornerOf(var3.getDirection().getUnitVec3i()));
   }

   private void L() {
      if (ta.E(this)) {
         p_.p(p_.o.y);
         if (p_.A() == p_.o.y) {
            float[] var1 = this.q();
            float[] var2 = this.d.f(this.C.getYRot(), this.C.getXRot(), var1[0], var1[1], this.I.getValue(), this.R.getValue());
            j6.U(this, j6.l.b, var2[0], var2[1], 1.0);
         }
      }
   }

   private void E() {
      LocalPlayer var1 = this.C;
      com.larpclient.ts.q var2 = this.Y();
      if (this.n == WindChargeHopModule.g.M && com.larpclient.ts.U(this.N, var2, this.J()[0], this.U())) {
         int var3 = !this.U.O("Off") && var1.onGround() ? 1 : 0;
         if (this.U.O("With Throw") && var3 != 0) {
            var1.jumpFromGround();
         }

         this.K.clear();

         for (Entity var5 : mc.level.entitiesForRendering()) {
            if (var5 instanceof WindCharge var6 && var6.getOwner() == var1) {
               this.K.add(var6.getId());
            }
         }

         if (!this.H()) {
            this.J("The wind charge throw was rejected");
         } else {
            this.Z = this.X;
            this.l = -1;
            this.v = this.s = var1.getY();
            this.T = var1.getDeltaMovement().y;
            this.a = (boolean)0;
            this.g = 40;
            String var8 = this.h();
            int var9 = L.getOrDefault(var8, 0);
            this.z = com.larpclient.ts.n(var2.ticks(), this.f(), var9);
            if (this.U.O("Timed") && var3 != 0 && this.z > 0) {
               this.n = WindChargeHopModule.g.a;
               this.D = "Wind charge thrown; jump in " + this.z + " ticks";
            } else {
               if (this.U.O("Timed") && var3 != 0) {
                  var1.jumpFromGround();
               }

               this.n = WindChargeHopModule.g.f;
               this.D = "Wind charge thrown";
            }

            double var10 = com.larpclient.ts.l(this.N, this.J()[0], this.U());
            com.larpclient.h.A(
               "WindHop",
               "Thrown at yaw {} pitch {} ({} of the best push) after a turn of {}; jump {} ({} ticks from {}), round trip {} ticks",
               z(var1.getYRot()),
               z(var1.getXRot()),
               var10 > 0.0 ? String.format(Locale.ROOT, "%.0f%%", 100.0 * com.larpclient.ts.l(var2, this.J()[0], this.U()) / var10) : "?",
               this.d.u(),
               var3 != 0 ? this.U.getValue() : "none",
               this.z,
               var9 > 0 ? "measured hops" : "ping",
               this.f()
            );
         }
      }
   }

   private void w() {
      if (this.X - this.Z >= this.z) {
         this.n = WindChargeHopModule.g.f;
         if (this.C.onGround() && !this.a) {
            this.C.jumpFromGround();
            this.D = "Jumped for the burst";
         } else {
            this.D = this.a ? "The push came before the jump" : "Left the ground before the jump";
         }
      }
   }

   private void M() {
      LocalPlayer var1 = this.C;
      if (this.l < 0) {
         for (Entity var4 : mc.level.entitiesForRendering()) {
            if (var4 instanceof WindCharge var2 && var2.getOwner() == var1 && !this.K.contains(var2.getId())) {
               this.l = (int)(this.X - this.Z);
               break;
            }
         }
      }

      double var7 = var1.getDeltaMovement().y;
      if (!this.a && var7 - this.T > 0.5) {
         this.a = (boolean)1;
         int var8 = (int)(this.X - this.Z);
         L.put(this.h(), var8);
         com.larpclient.h.A("WindHop", "Push arrived {} ticks after the throw: vertical speed {} to {}", var8, z(this.T), z(var7));
      }

      this.T = var7;
      this.s = Math.max(this.s, var1.getY());
      if (--this.g <= 0) {
         String var9 = this.l >= 0
            ? "your wind charge appeared " + this.l + " ticks after the throw"
            : (
               this.a
                  ? "the burst pushed you, so the server spawned the throw"
                  : "no wind charge from you was seen and nothing pushed you: the server may not have spawned the throw (an anticheat or plugin may have cancelled it), or it burst between two ticks and missed you"
            );
         String var5 = !this.a && this.l >= 0 ? "; no push seen, so the burst missed you or this server changes wind charge knockback" : "";
         String var6 = l6.J();
         com.larpclient.h.A("WindHop", "Hop result: rose {} blocks; {}{}; server {}", z(this.s - this.v), var9, var5, var6 == null ? "singleplayer" : var6);
         if (this.n == WindChargeHopModule.g.f) {
            this.D = String.format(Locale.ROOT, "Hopped %.1f blocks", this.s - this.v);
         }
      }
   }

   private String y() {
      if (!ta.Y() || mc.gui.screen() != null) {
         return "Paused: a screen is open";
      } else if (!mc.isWindowActive()) {
         return "Paused: the window lost focus";
      } else if (p_.A() != p_.o.y && !p_.I(p_.o.y)) {
         return "Paused: Freecam, Freelook or a pearl catch holds the view";
      } else if (this.C.isPassenger()) {
         return "Not while riding";
      } else if (this.C.isInWater() || this.C.isEyeInFluid(FluidTags.WATER) || this.C.isInLava()) {
         return "Not in water or lava";
      } else {
         return this.C.isFallFlying() ? "Not while gliding" : null;
      }
   }

   private String Q() {
      if (this.C.isUsingItem()) {
         return "Stop using your item first";
      } else if (AuraModule.N()) {
         return "Paused while Kill Aura lowers your shield";
      } else {
         int var1 = t1.M(Items.WIND_CHARGE, true);
         if (var1 == -1) {
            return "No wind charges in your hotbar or hands";
         } else {
            ItemStack var2 = this.B(var1);
            if (!var2.isItemEnabled(this.Q.enabledFeatures())) {
               return "Wind charges are disabled here";
            } else {
               float var3 = this.C.getCooldowns().getCooldownPercent(var2, 0.0F);
               if (var3 > 0.0F) {
                  UseCooldown var4 = (UseCooldown)var2.get(DataComponents.USE_COOLDOWN);
                  return String.format(Locale.ROOT, "Wind charge cooling down (%.2f s)", var3 * (var4 == null ? 10 : var4.ticks()) / 20.0);
               } else {
                  return var1 >= 0 && var1 != this.C.getInventory().getSelectedSlot() && !hu.k(this) ? "Hotbar busy" : null;
               }
            }
         }
      }
   }

   private ItemStack B(int var1) {
      return var1 == -2 ? this.C.getOffhandItem() : this.C.getInventory().getItem(var1);
   }

   private boolean c() {
      int var1 = t1.M(Items.WIND_CHARGE, true);
      return var1 != -1
         && !this.C.getCooldowns().isOnCooldown(this.B(var1))
         && !AuraModule.N()
         && (var1 == -2 || var1 == this.C.getInventory().getSelectedSlot() || hu.k(this));
   }

   private boolean H() {
      int var1 = t1.M(Items.WIND_CHARGE, true);
      if (var1 == -1) {
         return false;
      }

      InteractionHand var2 = var1 == -2 ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND;
      if (var1 >= 0 && var1 != this.C.getInventory().getSelectedSlot() && !this.j.O(this.C, var1)) {
         return false;
      }

      boolean var3 = mc.gameMode.useItem(this.C, var2).consumesAction();
      if (var3) {
         ks.p(this.C, var2);
         ((q7ae1dbb86ec789d8)mc).larpUseDelay(4);
      }

      return var3;
   }

   private void V() {
      if (this.j.p()) {
         int var1 = this.C == null ? -1 : this.C.getInventory().getSelectedSlot();
         this.j.V(getMc(), this.G.getValue());
         if (this.C != null && this.C == mc.player && mc.gameMode != null && var1 != this.C.getInventory().getSelectedSlot()) {
            ((q541491ff38fb1637)mc.gameMode).larpSyncCarriedItem();
         }
      }
   }

   private float[] J() {
      return !p_.j() ? new float[]{this.C.getYRot(), this.C.getXRot()} : p_.e(p_.V(), p_.s(), mc.options.getCameraType().isMirrored());
   }

   private int f() {
      ClientPacketListener var1 = mc.getConnection();
      PlayerInfo var2 = var1 != null && this.C != null ? var1.getPlayerInfo(this.C.getUUID()) : null;
      return var2 == null ? 0 : Math.max(0, Math.round(var2.getLatency() / 50.0F));
   }

   private String h() {
      ServerData var1 = mc.getCurrentServer();
      return var1 != null ? var1.ip : (mc.hasSingleplayerServer() ? "singleplayer" : "this server");
   }

   private void J(String var1) {
      int var2 = this.n != WindChargeHopModule.g.f ? 1 : 0;
      ta.s(this);
      this.V();
      this.n = WindChargeHopModule.g.f;
      p_.t(p_.o.y, this.d, this.w.getValue(), this.R.getValue());
      if (var2 != 0) {
         this.D = var1;
         com.larpclient.h.A("WindHop", "Hop cancelled: {}", var1);
      }
   }

   private static String z(double var0) {
      return String.format(Locale.ROOT, "%.2f", var0);
   }

   private static String Y(Vec3 var0) {
      return String.format(Locale.ROOT, "(%.1f, %.1f, %.1f)", var0.x, var0.y, var0.z);
   }

   @Override
   public void onEnable() {
      this.C = null;
      this.Q = null;
      this.n = WindChargeHopModule.g.f;
      this.Z = Long.MIN_VALUE;
      this.g = 0;
      this.D = this.x.getValue() < 0 ? "Set a Hop Bind" : "Ready";
      com.larpclient.h.A(
         "WindHop",
         "Enabled; direction={}, jump={}, swap back={}, turn speed={}, return speed={}, humanize={}",
         this.u.getValue(),
         this.U.getValue(),
         this.G.getValue(),
         this.I.getValue().intValue(),
         this.w.getValue().intValue(),
         this.R.getValue()
      );
   }

   @Override
   public void onDisable() {
      this.J("Disabled");
      this.C = null;
      this.Q = null;
      com.larpclient.h.A("WindHop", "Disabled; pending throw and view released");
   }

   private enum g {
      f,
      M,
      a;
   }
}
