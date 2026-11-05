package com.larpclient.module;

import com.larp.obf.qbac0b262827a15ac.q099a584e4baa1bb9;
import com.larp.obf.qbac0b262827a15ac.qb131e08b200c8a57;
import com.larpclient._8;
import com.larpclient._m;
import com.larpclient.m9;
import com.larpclient.mt;
import com.larpclient.p1;
import com.larpclient.p6;
import com.larpclient.ti;
import com.larpclient.tq;
import com.larpclient.w7;
import com.larpclient.x0;
import java.util.List;
import java.util.Set;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.FishingHook;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.ClipContext.Block;
import net.minecraft.world.level.ClipContext.Fluid;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.HitResult.Type;

public final class RodSwapModule extends Module {
   private static RodSwapModule o;
   private final d6 r = this.addSetting(
      new d6("Mode", "Cast Only returns once the bobber hits or misses; Hook and Reel pulls a hooked player once", "Cast Only", "Cast Only", "Hook and Reel")
   );
   private final d9 a = this.addSetting(
      new d9("Range", "Farthest player distance for a throw; Kill Aura tracks players this far while a rod is ready", 6.0, 3.0, 8.0, 0.25)
   );
   private final d9 M = this.addSetting(new d9("Delay", "Milliseconds with your weapon back in hand before the next throw", 1000.0, 250.0, 5000.0, 50.0));
   private final d9 b = this.addSetting(new d9("Timeout", "Longest a throw keeps the rod out before returning to your weapon", 1000.0, 250.0, 2000.0, 50.0));
   private static final int T = 20;
   private static final long S = 250000000L;
   private static final int R = 20;
   private static final double n = 0.5;
   private final ti z = new ti(this);
   private final p6 g = new p6();
   private final p6.w L = new p6.w();
   private String x;
   private long W;
   private LocalPlayer i;
   private ClientLevel X;
   private Player E;
   private Player G;
   private FishingHook e;
   private ItemStack q = ItemStack.EMPTY;
   private int t = -1;
   private int s = -1;
   private int Z;
   private String C = "Enable Kill Aura";

   public RodSwapModule() {
      super("Rod Swap", "Kill Aura throws a hotbar rod at its player target on a delay, then returns to its weapon", Category.COMBAT, -1);
      o = this;
      this.r.h(var1 -> this.d("Mode changed", true));
   }

   @Override
   public String getDisplayName() {
      return "Auto Rod";
   }

   public List<df<?>> n() {
      return List.of(this.r);
   }

   public List<df<?>> H() {
      return List.of(this.a, this.M, this.b);
   }

   public static void f() {
      if (o != null && o.isRuntimeActive()) {
         o.d("Manual slot selection", false);
         o.g.e(System.nanoTime());
      }
   }

   public static boolean j() {
      return o != null && o.isEnabled() ? o != null && o.isRuntimeActive() && o.g.V() && o.z.L() : false;
   }

   public static void v() {
      if (j()) {
         o.d("Your attack", true);
      }
   }

   public static double o() {
      RodSwapModule var0 = o;
      return var0 != null && var0.isRuntimeActive() && var0.i != null && var0.O() >= 0 ? var0.a.P() : 0.0;
   }

   @Override
   public Set<dm> getNetworkImpacts() {
      return Set.of(dm.z, dm.j);
   }

   @Override
   public String getStatus() {
      return this.isEnabled() ? this.C : "";
   }

   @Override
   public void onEnable() {
      this.i = getMc().player;
      this.X = mc.level;
      this.g.q();
      this.L.j();
      this.T();
      this.C = "Ready";
      this.x = null;
      mt.K("RodSwap", "Auto Rod enabled; mode={}, range={}, delay={}ms, timeout={}ms", this.r.P(), this.a.P(), this.M.P().longValue(), this.b.P().longValue());
   }

   @Override
   public void onDisable() {
      this.d("Disabled or disconnected", true);
      this.T();
      this.i = null;
      this.X = null;
   }

   private boolean I() {
      Minecraft var1 = getMc();
      return this.i != null
         && this.i == var1.player
         && this.X != null
         && this.X == var1.level
         && (w7.O(var1) || w7.m(var1, tq.y.u))
         && !this.i.isSpectator()
         && this.i.isAlive();
   }

   private boolean C() {
      for (KeyMapping var4 : mc.options.keyHotbarSlots) {
         if (w7.h(var4)) {
            return true;
         }
      }

      return false;
   }

   private boolean Z() {
      if (this.i != null && this.t >= 0 && this.t < 9) {
         ItemStack var1 = this.i.getInventory().getItem(this.t);
         return !var1.is(Items.FISHING_ROD) && ItemStack.isSameItemSameComponents(this.q, var1);
      } else {
         return false;
      }
   }

   private boolean e(int var1) {
      if (this.i != null && this.X != null && var1 >= 0 && var1 < 9) {
         ItemStack var2 = this.i.getInventory().getItem(var1);
         return var2.is(Items.FISHING_ROD)
            && !var2.nextDamageWillBreak()
            && var2.isItemEnabled(this.X.enabledFeatures())
            && !this.i.getCooldowns().isOnCooldown(var2);
      } else {
         return false;
      }
   }

   private int O() {
      for (int var1 = 0; var1 < 9; var1++) {
         if (this.e(var1)) {
            return var1;
         }
      }

      return -1;
   }

   private boolean X(Entity var1) {
      return var1 instanceof Player var2 && w7.E(this.i, var2) && !this.i.isAlliedTo(var2) && var2.isPickable();
   }

   private static Vec3 S(Entity var0) {
      return var0.position().subtract(var0.xo, var0.yo, var0.zo);
   }

   private double y(Player var1) {
      return p1.X(this.i.getEyePosition(), var1.getBoundingBox());
   }

   private Entity W() {
      x0.s var1 = x0.B(this.i.getEyePosition(), this.i.getYRot(), this.i.getXRot());
      List var2 = this.X
         .getEntities(
            this.i,
            this.i.getBoundingBox().inflate(this.a.P() + 4.0),
            var1x -> var1x.isAlive() && !var1x.isSpectator() && var1x.canBeHitByProjectile() && var1x != this.i.getVehicle()
         );

      for (int var3 = 0; var3 < 20; var3++) {
         Vec3 var4 = var1.position();
         Vec3 var5 = var1.collisionEnd();
         BlockHitResult var6 = this.X.clip(new ClipContext(var4, var5, Block.COLLIDER, Fluid.ANY, this.i));
         double var7 = var6.getType() == Type.MISS ? Double.POSITIVE_INFINITY : var4.distanceToSqr(var6.getLocation());
         Entity var9 = null;

         for (Entity var11 : var2) {
            AABB var12 = var11.getBoundingBox().move(x0.j(S(var11), var3)).inflate(x0.a(var3));
            double var13 = x0.L(var4, var5, var12);
            if (var13 < var7) {
               var7 = var13;
               var9 = var11;
            }
         }

         if (var9 != null || var6.getType() != Type.MISS) {
            return var9;
         }

         var1 = var1.next();
      }

      return null;
   }

   private String a(Player var1) {
      if (!var1.isSpectator() && !var1.isCreative()) {
         if (this.i.connection.getPlayerInfo(var1.getUUID()) == null) {
            return "no player-list entry";
         } else if (this.i.isAlliedTo(var1) || !this.i.canHarmPlayer(var1)) {
            return "teammate";
         } else {
            return var1.isPickable() && var1.isAttackable() ? "friend" : "not attackable";
         }
      } else {
         return "creative or spectator";
      }
   }

   private void y(String var1) {
      long var2 = System.nanoTime();
      if (!var1.equals(this.x) && (this.x == null || var2 - this.W >= 1000000000L)) {
         this.x = var1;
         this.W = var2;
         mt.K("RodSwap", "Waiting: {}", var1);
      }
   }

   private String S(long var1) {
      if (this.i.getMainHandItem().is(Items.FISHING_ROD)) {
         return "Hold your weapon, not the rod";
      } else if (this.i.getOffhandItem().is(Items.FISHING_ROD)) {
         return "Move the offhand rod to the hotbar";
      } else if (this.i.fishing != null) {
         return "Existing bobber";
      } else if (this.O() < 0) {
         return "No usable rod in hotbar";
      } else if (!this.g.C(var1) || this.C()) {
         return "Manual slot selection";
      } else if (this.i.isInWater()) {
         return "In water";
      } else if (!w7.f(mc, this.i) || w7.n(mc) || w7.s(mc)) {
         return "Your hands are busy";
      } else if (!ti.V(this)) {
         return "Another module owns the hotbar";
      } else {
         return _m.l() ? "Critical hit pending" : null;
      }
   }

   private boolean b(long var1) {
      return this.isRuntimeActive()
         && this.I()
         && !this.g.V()
         && this.S(var1) == null
         && m9.F(m9.s.t)
         && !w7.H(this.i)
         && ((qb131e08b200c8a57)mc).larpUseDelay() <= 0;
   }

   public static Vec3 r(LivingEntity var0) {
      if (o != null && o.isEnabled()) {
         RodSwapModule var1 = o;
         return var1 == null ? null : var1.d(var0);
      } else {
         return null;
      }
   }

   private Vec3 d(LivingEntity var1) {
      if (this.isRuntimeActive() && !this.g.V() && var1 instanceof Player var2 && this.I()) {
         long var3 = System.nanoTime();
         String var5 = this.Z(var2, var3);
         if (var5 != null) {
            this.T();
            if (!var5.isEmpty()) {
               this.y(var5);
            }

            return null;
         } else {
            x0.l var6 = this.X(var2);
            if (var6 == null) {
               this.T();
               this.y("No rod arc reaches " + var2.getName().getString());
               return null;
            }

            if (this.G != var2) {
               this.G = var2;
               this.Z = 0;
            }

            if (++this.Z > 20) {
               mt.K("RodSwap", "Throw at {} did not line up within {} ticks; retrying shortly", var2.getName().getString(), 20);
               this.L.P(var3, 250000000L);
               this.T();
               return null;
            } else {
               this.C = "Aiming";
               Vec3 var7 = this.i.getEyePosition();
               double var8 = Math.max(1.0, var7.distanceTo(var2.getBoundingBox().getCenter()));
               return var7.add(Vec3.directionFromRotation(var6.pitch(), var6.yaw()).scale(var8));
            }
         }
      } else {
         this.T();
         return null;
      }
   }

   private String Z(Player var1, long var2) {
      if (!this.L.R(var2)) {
         this.C = "Delay";
         return "";
      }

      if (!this.X(var1)) {
         return "Kill Aura target " + var1.getName().getString() + " ignored: " + this.a(var1);
      }

      AuraModule var4 = AuraModule.S();
      if (var4 == null) {
         return "";
      }

      double var5 = this.y(var1);
      if (var5 <= var4.v() + 0.5) {
         this.C = "Inside sword reach";
         return "";
      }

      if (var5 > this.a.P()) {
         this.C = "Beyond Range";
         return "";
      }

      String var7 = this.S(var2);
      if (var7 != null) {
         this.C = var7;
      }

      return var7;
   }

   private x0.l X(Player var1) {
      Vec3 var2 = this.i.getEyePosition();
      Vec3 var3 = var1.getBoundingBox().getCenter();
      Vec3 var4 = S(var1);
      x0.l var5 = x0.X(var2, var3, 20);

      for (int var6 = 0; var6 < 2 && var5 != null; var6++) {
         var5 = x0.X(var2, var3.add(x0.j(var4, var5.ticks())), 20);
      }

      return var5;
   }

   private void T() {
      this.G = null;
      this.Z = 0;
   }

   public void O() {
      if (this.isEnabled()) {
         if (this.isRuntimeActive()) {
            if (this.i != getMc().player || this.X != mc.level) {
               this.d("World changed", false);
               this.L.j();
               this.T();
               this.i = mc.player;
               this.X = mc.level;
            }

            if (!this.I()) {
               this.d("Paused", true);
            } else if (this.C()) {
               this.d("Manual slot selection", false);
               this.g.e(System.nanoTime());
            } else if (this.g.V()) {
               if (!this.z.L()) {
                  this.d("Slot ownership changed", false);
                  this.g.e(System.nanoTime());
               } else if (this.Z()
                  && this.e(this.s)
                  && ti.V(this)
                  && w7.f(mc, this.i)
                  && !w7.s(mc)
                  && !w7.n(mc)
                  && (!mc.options.keyAttack.isDown() || mc.hitResult == null || mc.hitResult.getType() != Type.BLOCK)) {
                  if (!this.X(this.E)) {
                     this.d("Target unavailable", true);
                  } else {
                     FishingHook var1 = this.i.fishing;
                     if (var1 == null || var1.getPlayerOwner() == this.i && var1.level() == this.X && !var1.isRemoved()) {
                        if (this.e != null && var1 != null && var1 != this.e) {
                           this.d("Bobber replaced", true);
                        } else {
                           if (var1 != null && this.e == null) {
                              this.e = var1;
                              mt.U("RodSwap", "Owned bobber observed; entity={}", this.e.getId());
                           }

                           Entity var2 = var1 == null ? null : var1.getHookedIn();
                           int var3 = var1 != null
                                 && var1.tickCount > 2
                                 && var1.position().subtract(this.E.getBoundingBox().getCenter()).dot(var1.getDeltaMovement()) > 0.0
                                 && var1.distanceToSqr(this.E) > 2.25
                              ? 1
                              : 0;
                           p6.n var4 = this.g
                              .i(
                                 System.nanoTime(),
                                 var1 != null,
                                 var2 == this.E,
                                 var2 != null && var2 != this.E,
                                 var1 != null && var1.onGround(),
                                 (boolean)var3
                              );
                           if (var4 == p6.n.X) {
                              this.d(var2 == this.E ? "Hit" : "Missed or timed out", true);
                           } else if (var4 == p6.n.W) {
                              this.C = "Player hooked";
                           }
                        }
                     } else {
                        this.d("Bobber unavailable", true);
                     }
                  }
               } else {
                  this.d("Your input takes priority", true);
               }
            }
         }
      }
   }

   @Override
   public void onTick() {
      if (!this.I()) {
         this.d("Paused", true);
      } else {
         int var10000 = this.g.V();
         boolean var9 = false;
         byte var8 = (byte)var10000;
         var10000 = Integer.signum(var8);
         if (((var10000 | -var10000) >>> 31 ^ 1) == 0) {
            if (this.g.Y() == p6.v.E) {
               _m.F(this, this::z, this::D);
            }
         } else {
            AuraModule var1 = AuraModule.S();
            if (var1 != null && var1.isRuntimeActive()) {
               LivingEntity var3 = var1.l();
               int var17 = var3 instanceof Player;
               boolean var11 = false;
               byte var10 = (byte)var17;
               var17 = Integer.signum(var10);
               if (((var17 | -var17) >>> 31 ^ 1) == 0) {
                  Player var2 = (Player)var3;
                  if (this.G == var2) {
                     long var14 = System.nanoTime();
                     int var21 = this.b(var14);
                     boolean var13 = false;
                     byte var12 = (byte)var21;
                     var21 = Integer.signum(var12);
                     if ((var21 | -var21) >>> 31 == 0) {
                        String var15 = this.S(var14);
                        this.C = var15 == null ? "Aiming" : var15;
                     } else if (this.W() != var2) {
                        this.C = "Aiming";
                     } else {
                        int var5 = this.O();
                        _m.F(
                           this,
                           () -> this.i(var2, var5),
                           () -> this.b(System.nanoTime()) && this.e(var5) && this.X(var2) && this.G == var2 && var1.l() == var2 && this.W() == var2
                        );
                     }
                  }
               } else {
                  this.T();
                  int var19 = this.C.equals("Delay");
                  boolean var7 = false;
                  byte var6 = (byte)var19;
                  var19 = Integer.signum(var6);
                  if ((var19 | -var19) >>> 31 == 0) {
                     this.C = "No player target";
                  }
               }
            } else {
               this.T();
               this.C = "Enable Kill Aura";
            }
         }
      }
   }

   private void i(Player var1, int var2) {
      this.t = this.i.getInventory().getSelectedSlot();
      this.q = this.i.getMainHandItem().copy();
      this.s = var2;
      this.T();
      if (!this.z.e(this.i, var2)) {
         this.L.P(System.nanoTime(), 250000000L);
         this.d("Hotbar busy", false);
      } else {
         this.E = var1;
         this.e = null;
         this.g.I(System.nanoTime(), this.b.P().longValue() * 1000000L, this.r.S("Hook and Reel"));
         boolean var3 = mc.gameMode.useItem(this.i, InteractionHand.MAIN_HAND).consumesAction();
         if (!var3) {
            this.d("Throw rejected locally", true);
         } else {
            _8.G(this.i, InteractionHand.MAIN_HAND);
            ((qb131e08b200c8a57)mc).larpUseDelay(4);
            this.C = "Thrown";
            this.x = null;
            mt.K(
               "RodSwap",
               "Throw; mode={}, rodSlot={}, returnSlot={}, target={}, distance={}, yaw={}, pitch={}",
               this.r.P(),
               var2 + 1,
               this.t + 1,
               this.E.getName().getString(),
               String.format("%.2f", this.y(this.E)),
               String.format("%.1f", this.i.getYRot()),
               String.format("%.1f", this.i.getXRot())
            );
         }
      }
   }

   private boolean D() {
      return this.isRuntimeActive()
         && this.I()
         && this.g.Y() == p6.v.E
         && this.z.L()
         && this.Z()
         && this.e(this.s)
         && ti.V(this)
         && w7.f(mc, this.i)
         && !w7.s(mc)
         && !w7.n(mc)
         && !w7.H(this.i)
         && ((qb131e08b200c8a57)mc).larpUseDelay() <= 0
         && this.e != null
         && this.i.fishing == this.e
         && this.e.getHookedIn() == this.E
         && this.X(this.E);
   }

   private void z() {
      if (this.D()) {
         this.g.T();
         if (!mc.gameMode.useItem(this.i, InteractionHand.MAIN_HAND).consumesAction()) {
            this.d("Reel rejected locally", true);
         } else {
            _8.G(this.i, InteractionHand.MAIN_HAND);
            ((qb131e08b200c8a57)mc).larpUseDelay(4);
            this.C = "Reeled";
            mt.U("RodSwap", "Reel requested for confirmed target {}; awaiting bobber removal", this.E.getId());
         }
      }
   }

   private void d(String var1, boolean var2) {
      _m.w(this);
      int var3 = !this.g.V() && !this.z.k() ? 0 : 1;
      p6.v var4 = this.g.Y();
      int var5 = this.i != null && this.i == getMc().player && this.X == mc.level && this.i.isAlive() ? 1 : 0;
      int var6 = var5 != 0 ? this.i.getInventory().getSelectedSlot() : -1;
      this.z.W(getMc(), var2 && var5 != 0 && this.Z());
      if (var5 != 0 && mc.gameMode != null && var6 != this.i.getInventory().getSelectedSlot()) {
         ((q099a584e4baa1bb9)mc.gameMode).larpSyncCarriedItem();
      }

      if (var3 != 0) {
         this.L.i(System.nanoTime(), this.M.P().longValue() * 1000000L);
      }

      this.g.q();
      this.E = null;
      this.e = null;
      this.q = ItemStack.EMPTY;
      this.t = -1;
      this.s = -1;
      this.C = var1;
      if (var3 != 0) {
         mt.K("RodSwap", "Throw ended: {} at {}; weapon slot restored={}", var1, var4, var5 != 0 && var6 != this.i.getInventory().getSelectedSlot());
      }
   }
}
