package com.larpclient.module;

import com.larp.obf.q5682c6a8aca9b6ec.q143ef5d8ac4bd5b8;
import com.larp.obf.q5682c6a8aca9b6ec.qa1ad45ac3ead4599;
import com.larpclient.d8;
import com.larpclient.ep;
import com.larpclient.hp;
import com.larpclient.l0;
import com.larpclient.m;
import com.larpclient.o_;
import com.larpclient.oz;
import com.larpclient.r4;
import com.larpclient.rr;
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
   private static final int L = 40;
   private static final int g = 30;
   private static WindChargeHopModule y;
   private final la t = this.addSetting(new la("Hop Bind", "Press for one wind charge hop; separate from the module toggle", -1));
   private final lp U = this.addSetting(
      new lp("Direction", "Up throws straight down for height; Forward throws behind you so the burst pushes you where you look", "Up", "Up", "Forward")
   );
   private final lp c = this.addSetting(
      new lp(
         "Jump",
         "With Throw jumps as you throw, so the push adds to the jump; Timed jumps just before the push arrives, from your ping and measured hops; Off never jumps",
         "With Throw",
         "With Throw",
         "Timed",
         "Off"
      )
   );
   private final ln b = this.addSetting(new ln("Swap Back", "Return to your previous hotbar slot the tick after the throw", true));
   private final lc B = this.addSetting(
      new lc(
         "Turn Speed",
         "Most the player turns per tick for the throw; your camera stays on your look. 180 with Humanize off turns at once",
         45.0,
         10.0,
         180.0,
         5.0
      )
   );
   private final lc H = this.addSetting(
      new lc("Return Speed", "Most the player turns per tick back to your look after the throw. 180 returns at once", 35.0, 10.0, 180.0, 5.0)
   );
   private final ln C = this.addSetting(
      new ln(
         "Humanize", "Turn like a hand on a mouse: speed up, ease in, drift a little and move in whole sensitivity steps. Off turns in straight lines", true
      )
   );
   private final l0 V = new l0();
   private final o_ X = new o_(this);
   private static final Map<String, Integer> a = new HashMap<>();
   private WindChargeHopModule.t o = WindChargeHopModule.t.y;
   private boolean J;
   private LocalPlayer W;
   private ClientLevel T;
   private com.larpclient.ls.f w;
   private long p;
   private long D = Long.MIN_VALUE;
   private int A;
   private int z;
   private double r;
   private double m;
   private double s;
   private boolean u;
   private float x;
   private float Q;
   private final Set<Integer> q = new HashSet<>();
   private int P;
   private String N = "Set a Hop Bind";
   private int f;

   public WindChargeHopModule() {
      super("Wind Charge Hop", "Throw a wind charge at the ground for a timed boost, with a silent turn", Category.MOVEMENT, -1);
      y = this;
      this.U.W(var1 -> this.i("Direction changed"));
      this.t.W(var1 -> this.i("Bind changed"));
   }

   public static WindChargeHopModule Q() {
      return y;
   }

   public l0 i() {
      return this.V;
   }

   public double L() {
      return this.H.getValue();
   }

   public boolean M() {
      return this.C.getValue();
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
      return this.isEnabled() ? this.N : "";
   }

   public boolean d() {
      return !this.isEnabled()
         ? false
         : this.isRuntimeActive() && this.W != null && this.W == mc.player && this.T == mc.level && (this.o == WindChargeHopModule.t.q || this.p == this.D);
   }

   public static void C(int var0) {
      if (y != null && y.isEnabled()) {
         WindChargeHopModule var1 = y;
         if (var1 != null && var1.isRuntimeActive() && var0 > 0 && var0 == var1.t.getValue()) {
            var1.J = (boolean)1;
         }
      }
   }

   @Override
   public void onTick() {
      if (!this.isInGame()) {
         this.i("Waiting for a world");
      } else {
         if (this.W != mc.player || this.T != mc.level) {
            this.i("World changed");
            this.W = mc.player;
            this.T = mc.level;
            this.z = 0;
         }

         this.p++;
         boolean var5 = false;
         int var4 = this.z;
         if ((-Integer.signum(var4) >>> 31 ^ 1) == 0) {
            this.V();
         }

         if (this.o != WindChargeHopModule.t.q && this.X.K() && this.p > this.D) {
            this.k();
         }

         byte var1 = this.J;
         this.J = (boolean)0;
         if (this.o == WindChargeHopModule.t.y) {
            boolean var7 = false;
            byte var6 = var1;
            int var12 = Integer.signum(var6);
            if (((var12 | -var12) >>> 31 ^ 1) == 0) {
               this.D();
            } else if (this.t.getValue() < 0) {
               this.N = "Set a Hop Bind";
            }
         } else if (this.o == WindChargeHopModule.t.C) {
            this.v();
         } else {
            String var2 = this.t();
            if (var2 != null) {
               this.i(var2);
            } else {
               com.larpclient.ls.f var3 = com.larpclient.ls.W(
                  this.W.position(), this.W.getEyeHeight(), this.W.getDeltaMovement(), this.W.onGround(), this.m(), this.P()[0], this.A(), this::n
               );
               if (var3 == null) {
                  this.i("No ground close enough for a boost");
               } else {
                  this.w = var3;
                  this.K();
                  d8.A(this, this::y, () -> this.o == WindChargeHopModule.t.q && this.t() == null && this.a() && this.P());
                  byte var9 = 1;
                  int var8 = this.f;
                  int var10000 = this.f = var8 + var9;
                  byte var11 = 30;
                  int var10 = var10000;
                  if ((-Integer.compare(var10, var11) >>> 31 ^ 1) == 0) {
                     this.i(this.P() ? "The throw could not go out" : "The turn did not reach a throw angle in time");
                  }
               }
            }
         }
      }
   }

   private void D() {
      String var1 = this.t();
      if (var1 == null) {
         var1 = this.g();
      }

      if (var1 != null) {
         this.N = var1;
         r4.V("WindHop", "Hop refused: {}", var1);
      } else {
         LocalPlayer var2 = this.W;
         float var3 = this.P()[0];
         int var4 = !this.c.i("Off") && var2.onGround() ? 1 : 0;
         this.w = com.larpclient.ls.W(var2.position(), var2.getEyeHeight(), var2.getDeltaMovement(), var2.onGround(), this.m(), var3, this.A(), this::n);
         if (this.w == null) {
            this.N = "No ground close enough for a boost";
            r4.V("WindHop", "Hop refused: no burst within {} blocks of you", 2.4);
         } else {
            this.o = WindChargeHopModule.t.q;
            this.f = 0;
            ThreadLocalRandom var5 = ThreadLocalRandom.current();
            this.x = this.A() ? (float)(var5.nextDouble() * 6.0 - 3.0) : 0.0F;
            this.Q = this.A() ? (float)(var5.nextDouble() * 4.0 - 2.0) : (float)(-(var5.nextDouble() * 3.0));
            this.V.O();
            this.q();
            r4.V(
               "WindHop",
               "Hop: {}, best throw yaw {} pitch {}, burst after {} ticks at {}, predicted push {} up {} along ({} blocks/tick), jump {}; turn {}/tick, return {}/tick, humanize {}",
               this.U.getValue(),
               q(this.w.yaw()),
               q(this.w.pitch()),
               this.w.ticks(),
               V(this.w.burst()),
               q(this.w.push().y),
               q(Math.hypot(this.w.push().x, this.w.push().z)),
               q(this.w.push().length()),
               var4 != 0 ? this.c.getValue() : "none",
               this.B.getValue().intValue(),
               this.H.getValue().intValue(),
               this.C.getValue()
            );
            this.K();
            d8.A(this, this::y, () -> this.o == WindChargeHopModule.t.q && this.t() == null && this.a() && this.P());
         }
      }
   }

   private boolean A() {
      return this.U.i("Forward");
   }

   private boolean m() {
      return this.c.i("With Throw") && this.W.onGround();
   }

   private com.larpclient.ls.f h() {
      return com.larpclient.ls.H(
         this.W.position(), this.W.getEyeHeight(), this.W.getDeltaMovement(), this.W.onGround(), this.m(), this.W.getYRot(), this.W.getXRot(), this::n
      );
   }

   private boolean P() {
      return com.larpclient.ls.z(this.w, this.h(), this.P()[0], this.A());
   }

   private float[] z() {
      return !this.A()
         ? new float[]{this.W.getYRot(), Math.min(90.0F, this.w.pitch() + this.Q)}
         : new float[]{this.w.yaw() + this.x, Mth.clamp(this.w.pitch() + this.Q, -90.0F, 90.0F)};
   }

   private void q() {
      int var1 = com.larpclient.m.Q(Items.WIND_CHARGE, true);
      if (var1 >= 0 && var1 != this.W.getInventory().getSelectedSlot() && this.X.s(this.W, var1)) {
         if (mc.gameMode != null) {
            ((q143ef5d8ac4bd5b8)mc.gameMode).larpSyncCarriedItem();
         }
      }
   }

   private com.larpclient.ls.p n(Vec3 var1, Vec3 var2) {
      BlockHitResult var3 = this.T.clip(new ClipContext(var1, var2, Block.COLLIDER, Fluid.NONE, this.W));
      return var3.getType() == Type.MISS ? null : new com.larpclient.ls.p(var3.getLocation(), Vec3.atLowerCornerOf(var3.getDirection().getUnitVec3i()));
   }

   private void K() {
      if (d8.m(this)) {
         rr.W(rr.f.m);
         if (rr.u() == rr.f.m) {
            float[] var1 = this.z();
            float[] var2 = this.V.G(this.W.getYRot(), this.W.getXRot(), var1[0], var1[1], this.B.getValue(), this.C.getValue());
            ep.t(this, ep.z.q, var2[0], var2[1], 1.0);
         }
      }
   }

   private void y() {
      LocalPlayer var1 = this.W;
      com.larpclient.ls.f var2 = this.h();
      if (this.o == WindChargeHopModule.t.q && com.larpclient.ls.z(this.w, var2, this.P()[0], this.A())) {
         int var3 = !this.c.i("Off") && var1.onGround() ? 1 : 0;
         if (this.c.i("With Throw") && var3 != 0) {
            var1.jumpFromGround();
         }

         this.q.clear();

         for (Entity var5 : mc.level.entitiesForRendering()) {
            if (var5 instanceof WindCharge var6 && var6.getOwner() == var1) {
               this.q.add(var6.getId());
            }
         }

         if (!this.t()) {
            this.i("The wind charge throw was rejected");
         } else {
            this.D = this.p;
            this.P = -1;
            this.r = this.m = var1.getY();
            this.s = var1.getDeltaMovement().y;
            this.u = (boolean)0;
            this.z = 40;
            String var8 = this.X();
            int var9 = a.getOrDefault(var8, 0);
            this.A = com.larpclient.ls.t(var2.ticks(), this.f(), var9);
            if (this.c.i("Timed") && var3 != 0 && this.A > 0) {
               this.o = WindChargeHopModule.t.C;
               this.N = "Wind charge thrown; jump in " + this.A + " ticks";
            } else {
               if (this.c.i("Timed") && var3 != 0) {
                  var1.jumpFromGround();
               }

               this.o = WindChargeHopModule.t.y;
               this.N = "Wind charge thrown";
            }

            double var10 = com.larpclient.ls.y(this.w, this.P()[0], this.A());
            r4.V(
               "WindHop",
               "Thrown at yaw {} pitch {} ({} of the best push) after a turn of {}; jump {} ({} ticks from {}), round trip {} ticks",
               q(var1.getYRot()),
               q(var1.getXRot()),
               var10 > 0.0 ? String.format(Locale.ROOT, "%.0f%%", 100.0 * com.larpclient.ls.y(var2, this.P()[0], this.A()) / var10) : "?",
               this.V.F(),
               var3 != 0 ? this.c.getValue() : "none",
               this.A,
               var9 > 0 ? "measured hops" : "ping",
               this.f()
            );
         }
      }
   }

   private void v() {
      if (this.p - this.D >= this.A) {
         this.o = WindChargeHopModule.t.y;
         if (this.W.onGround() && !this.u) {
            this.W.jumpFromGround();
            this.N = "Jumped for the burst";
         } else {
            this.N = this.u ? "The push came before the jump" : "Left the ground before the jump";
         }
      }
   }

   private void V() {
      LocalPlayer var1 = this.W;
      if (this.P < 0) {
         for (Entity var4 : mc.level.entitiesForRendering()) {
            if (var4 instanceof WindCharge var2 && var2.getOwner() == var1 && !this.q.contains(var2.getId())) {
               this.P = (int)(this.p - this.D);
               break;
            }
         }
      }

      double var7 = var1.getDeltaMovement().y;
      if (!this.u && var7 - this.s > 0.5) {
         this.u = (boolean)1;
         int var8 = (int)(this.p - this.D);
         a.put(this.X(), var8);
         r4.V("WindHop", "Push arrived {} ticks after the throw: vertical speed {} to {}", var8, q(this.s), q(var7));
      }

      this.s = var7;
      this.m = Math.max(this.m, var1.getY());
      if (--this.z <= 0) {
         String var9 = this.P >= 0
            ? "your wind charge appeared " + this.P + " ticks after the throw"
            : (
               this.u
                  ? "the burst pushed you, so the server spawned the throw"
                  : "no wind charge from you was seen and nothing pushed you: the server may not have spawned the throw (an anticheat or plugin may have cancelled it), or it burst between two ticks and missed you"
            );
         String var5 = !this.u && this.P >= 0 ? "; no push seen, so the burst missed you or this server changes wind charge knockback" : "";
         String var6 = oz.r();
         r4.V("WindHop", "Hop result: rose {} blocks; {}{}; server {}", q(this.m - this.r), var9, var5, var6 == null ? "singleplayer" : var6);
         if (this.o == WindChargeHopModule.t.y) {
            this.N = String.format(Locale.ROOT, "Hopped %.1f blocks", this.m - this.r);
         }
      }
   }

   private String t() {
      if (!d8.a() || mc.gui.screen() != null) {
         return "Paused: a screen is open";
      } else if (!mc.isWindowActive()) {
         return "Paused: the window lost focus";
      } else if (rr.u() != rr.f.m && !rr.j(rr.f.m)) {
         return "Paused: Freecam, Freelook or a pearl catch holds the view";
      } else if (this.W.isPassenger()) {
         return "Not while riding";
      } else if (this.W.isInWater() || this.W.isEyeInFluid(FluidTags.WATER) || this.W.isInLava()) {
         return "Not in water or lava";
      } else {
         return this.W.isFallFlying() ? "Not while gliding" : null;
      }
   }

   private String g() {
      if (this.W.isUsingItem()) {
         return "Stop using your item first";
      } else if (AuraModule.W()) {
         return "Paused while Kill Aura lowers your shield";
      } else {
         int var1 = com.larpclient.m.Q(Items.WIND_CHARGE, true);
         if (var1 == -1) {
            return "No wind charges in your hotbar or hands";
         } else {
            ItemStack var2 = this.H(var1);
            if (!var2.isItemEnabled(this.T.enabledFeatures())) {
               return "Wind charges are disabled here";
            } else {
               float var3 = this.W.getCooldowns().getCooldownPercent(var2, 0.0F);
               if (var3 > 0.0F) {
                  UseCooldown var4 = (UseCooldown)var2.get(DataComponents.USE_COOLDOWN);
                  return String.format(Locale.ROOT, "Wind charge cooling down (%.2f s)", var3 * (var4 == null ? 10 : var4.ticks()) / 20.0);
               } else {
                  return var1 >= 0 && var1 != this.W.getInventory().getSelectedSlot() && !o_.N(this) ? "Hotbar busy" : null;
               }
            }
         }
      }
   }

   private ItemStack H(int var1) {
      return var1 == -2 ? this.W.getOffhandItem() : this.W.getInventory().getItem(var1);
   }

   private boolean a() {
      int var1 = com.larpclient.m.Q(Items.WIND_CHARGE, true);
      return var1 != -1
         && !this.W.getCooldowns().isOnCooldown(this.H(var1))
         && !AuraModule.W()
         && (var1 == -2 || var1 == this.W.getInventory().getSelectedSlot() || o_.N(this));
   }

   private boolean t() {
      int var1 = com.larpclient.m.Q(Items.WIND_CHARGE, true);
      if (var1 == -1) {
         return false;
      }

      InteractionHand var2 = var1 == -2 ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND;
      if (var1 >= 0 && var1 != this.W.getInventory().getSelectedSlot() && !this.X.s(this.W, var1)) {
         return false;
      }

      boolean var3 = mc.gameMode.useItem(this.W, var2).consumesAction();
      if (var3) {
         hp.Q(this.W, var2);
         ((qa1ad45ac3ead4599)mc).larpUseDelay(4);
      }

      return var3;
   }

   private void k() {
      if (this.X.K()) {
         int var1 = this.W == null ? -1 : this.W.getInventory().getSelectedSlot();
         this.X.m(getMc(), this.b.getValue());
         if (this.W != null && this.W == mc.player && mc.gameMode != null && var1 != this.W.getInventory().getSelectedSlot()) {
            ((q143ef5d8ac4bd5b8)mc.gameMode).larpSyncCarriedItem();
         }
      }
   }

   private float[] P() {
      return !rr.e() ? new float[]{this.W.getYRot(), this.W.getXRot()} : rr.l(rr.j(), rr.T(), mc.options.getCameraType().isMirrored());
   }

   private int f() {
      ClientPacketListener var1 = mc.getConnection();
      PlayerInfo var2 = var1 != null && this.W != null ? var1.getPlayerInfo(this.W.getUUID()) : null;
      return var2 == null ? 0 : Math.max(0, Math.round(var2.getLatency() / 50.0F));
   }

   private String X() {
      ServerData var1 = mc.getCurrentServer();
      return var1 != null ? var1.ip : (mc.hasSingleplayerServer() ? "singleplayer" : "this server");
   }

   private void i(String var1) {
      int var2 = this.o != WindChargeHopModule.t.y ? 1 : 0;
      d8.K(this);
      this.k();
      this.o = WindChargeHopModule.t.y;
      rr.L(rr.f.m, this.V, this.H.getValue(), this.C.getValue());
      if (var2 != 0) {
         this.N = var1;
         r4.V("WindHop", "Hop cancelled: {}", var1);
      }
   }

   private static String q(double var0) {
      return String.format(Locale.ROOT, "%.2f", var0);
   }

   private static String V(Vec3 var0) {
      return String.format(Locale.ROOT, "(%.1f, %.1f, %.1f)", var0.x, var0.y, var0.z);
   }

   @Override
   public void onEnable() {
      this.W = null;
      this.T = null;
      this.o = WindChargeHopModule.t.y;
      this.D = Long.MIN_VALUE;
      this.z = 0;
      this.N = this.t.getValue() < 0 ? "Set a Hop Bind" : "Ready";
      r4.V(
         "WindHop",
         "Enabled; direction={}, jump={}, swap back={}, turn speed={}, return speed={}, humanize={}",
         this.U.getValue(),
         this.c.getValue(),
         this.b.getValue(),
         this.B.getValue().intValue(),
         this.H.getValue().intValue(),
         this.C.getValue()
      );
   }

   @Override
   public void onDisable() {
      this.i("Disabled");
      this.W = null;
      this.T = null;
      r4.V("WindHop", "Disabled; pending throw and view released");
   }

   private enum t {
      y,
      q,
      C;
   }
}
