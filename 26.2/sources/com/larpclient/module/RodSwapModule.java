package com.larpclient.module;

import com.larp.obf.qa300483f54d804a3.q541491ff38fb1637;
import com.larp.obf.qa300483f54d804a3.q7ae1dbb86ec789d8;
import com.larpclient.bz;
import com.larpclient.hu;
import com.larpclient.j6;
import com.larpclient.ks;
import com.larpclient.p_;
import com.larpclient.si;
import com.larpclient.ta;
import com.larpclient.wi;
import com.larpclient.wt;
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
   private static RodSwapModule R;
   private final l l = this.addSetting(
      new l("Mode", "Cast Only returns once the bobber hits or misses; Hook and Reel pulls a hooked player once", "Cast Only", "Cast Only", "Hook and Reel")
   );
   private final e U = this.addSetting(
      new e("Range", "Farthest player distance for a throw; Kill Aura tracks players this far while a rod is ready", 6.0, 3.0, 8.0, 0.25)
   );
   private final e a = this.addSetting(new e("Delay", "Milliseconds with your weapon back in hand before the next throw", 1000.0, 250.0, 5000.0, 50.0));
   private final e D = this.addSetting(new e("Timeout", "Longest a throw keeps the rod out before returning to your weapon", 1000.0, 250.0, 2000.0, 50.0));
   private static final int m = 20;
   private static final long T = 250000000L;
   private static final int K = 20;
   private static final double H = 0.5;
   private final hu S = new hu(this);
   private final bz A = new bz();
   private final bz.s g = new bz.s();
   private String Z;
   private long P;
   private LocalPlayer J;
   private ClientLevel r;
   private Player Y;
   private Player F;
   private FishingHook w;
   private ItemStack V = ItemStack.EMPTY;
   private int o = -1;
   private int p = -1;
   private int j;
   private String q = "Enable Kill Aura";

   public RodSwapModule() {
      super("Rod Swap", "Kill Aura throws a hotbar rod at its player target on a delay, then returns to its weapon", Category.COMBAT, -1);
      R = this;
      this.l.o(var1 -> this.k("Mode changed", true));
   }

   @Override
   public String getDisplayName() {
      return "Auto Rod";
   }

   public List<q<?>> D() {
      return List.of(this.l);
   }

   public List<q<?>> Q() {
      return List.of(this.U, this.a, this.D);
   }

   public static void P() {
      if (R != null && R.isRuntimeActive()) {
         R.k("Manual slot selection", false);
         R.A.k(System.nanoTime());
      }
   }

   public static boolean o() {
      return R != null && R.isEnabled() ? R != null && R.isRuntimeActive() && R.A.Z() && R.S.U() : false;
   }

   public static void Q() {
      if (o()) {
         R.k("Your attack", true);
      }
   }

   public static double Y() {
      RodSwapModule var0 = R;
      return var0 != null && var0.isRuntimeActive() && var0.J != null && var0.r() >= 0 ? var0.U.getValue() : 0.0;
   }

   @Override
   public Set<m> getNetworkImpacts() {
      return Set.of(com.larpclient.module.m.K, com.larpclient.module.m.U);
   }

   @Override
   public String getStatus() {
      return this.isEnabled() ? this.q : "";
   }

   @Override
   public void onEnable() {
      this.J = getMc().player;
      this.r = mc.level;
      this.A.g();
      this.g.X();
      this.e();
      this.q = "Ready";
      this.Z = null;
      com.larpclient.h.A(
         "RodSwap",
         "Auto Rod enabled; mode={}, range={}, delay={}ms, timeout={}ms",
         this.l.getValue(),
         this.U.getValue(),
         this.a.getValue().longValue(),
         this.D.getValue().longValue()
      );
   }

   @Override
   public void onDisable() {
      this.k("Disabled or disconnected", true);
      this.e();
      this.J = null;
      this.r = null;
   }

   private boolean i() {
      Minecraft var1 = getMc();
      return this.J != null
         && this.J == var1.player
         && this.r != null
         && this.r == var1.level
         && (si.P(var1) || si.w(var1, p_.o.b))
         && !this.J.isSpectator()
         && this.J.isAlive();
   }

   private boolean K() {
      for (KeyMapping var4 : mc.options.keyHotbarSlots) {
         if (si.h(var4)) {
            return true;
         }
      }

      return false;
   }

   private boolean N() {
      if (this.J != null && this.o >= 0 && this.o < 9) {
         ItemStack var1 = this.J.getInventory().getItem(this.o);
         return !var1.is(Items.FISHING_ROD) && ItemStack.isSameItemSameComponents(this.V, var1);
      } else {
         return false;
      }
   }

   private boolean V(int var1) {
      if (this.J != null && this.r != null && var1 >= 0 && var1 < 9) {
         ItemStack var2 = this.J.getInventory().getItem(var1);
         return var2.is(Items.FISHING_ROD)
            && !var2.nextDamageWillBreak()
            && var2.isItemEnabled(this.r.enabledFeatures())
            && !this.J.getCooldowns().isOnCooldown(var2);
      } else {
         return false;
      }
   }

   private int r() {
      for (int var1 = 0; var1 < 9; var1++) {
         if (this.V(var1)) {
            return var1;
         }
      }

      return -1;
   }

   private boolean w(Entity var1) {
      return var1 instanceof Player var2 && si.x(this.J, var2) && !this.J.isAlliedTo(var2) && var2.isPickable();
   }

   private static Vec3 F(Entity var0) {
      return var0.position().subtract(var0.xo, var0.yo, var0.zo);
   }

   private double i(Player var1) {
      return wi.j(this.J.getEyePosition(), var1.getBoundingBox());
   }

   private Entity n() {
      wt.k var1 = wt.r(this.J.getEyePosition(), this.J.getYRot(), this.J.getXRot());
      List var2 = this.r
         .getEntities(
            this.J,
            this.J.getBoundingBox().inflate(this.U.getValue() + 4.0),
            var1x -> var1x.isAlive() && !var1x.isSpectator() && var1x.canBeHitByProjectile() && var1x != this.J.getVehicle()
         );

      for (int var3 = 0; var3 < 20; var3++) {
         Vec3 var4 = var1.position();
         Vec3 var5 = var1.collisionEnd();
         BlockHitResult var6 = this.r.clip(new ClipContext(var4, var5, Block.COLLIDER, Fluid.ANY, this.J));
         double var7 = var6.getType() == Type.MISS ? Double.POSITIVE_INFINITY : var4.distanceToSqr(var6.getLocation());
         Entity var9 = null;

         for (Entity var11 : var2) {
            AABB var12 = var11.getBoundingBox().move(wt.M(F(var11), var3)).inflate(wt.W(var3));
            double var13 = wt.Z(var4, var5, var12);
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

   private String E(Player var1) {
      if (!var1.isSpectator() && !var1.isCreative()) {
         if (this.J.connection.getPlayerInfo(var1.getUUID()) == null) {
            return "no player-list entry";
         } else if (this.J.isAlliedTo(var1) || !this.J.canHarmPlayer(var1)) {
            return "teammate";
         } else {
            return var1.isPickable() && var1.isAttackable() ? "friend" : "not attackable";
         }
      } else {
         return "creative or spectator";
      }
   }

   private void c(String var1) {
      long var2 = System.nanoTime();
      if (!var1.equals(this.Z) && (this.Z == null || var2 - this.P >= 1000000000L)) {
         this.Z = var1;
         this.P = var2;
         com.larpclient.h.A("RodSwap", "Waiting: {}", var1);
      }
   }

   private String L(long var1) {
      if (this.J.getMainHandItem().is(Items.FISHING_ROD)) {
         return "Hold your weapon, not the rod";
      } else if (this.J.getOffhandItem().is(Items.FISHING_ROD)) {
         return "Move the offhand rod to the hotbar";
      } else if (this.J.fishing != null) {
         return "Existing bobber";
      } else if (this.r() < 0) {
         return "No usable rod in hotbar";
      } else if (!this.A.R(var1) || this.K()) {
         return "Manual slot selection";
      } else if (this.J.isInWater()) {
         return "In water";
      } else if (!si.M(mc, this.J) || si.B(mc) || si.A(mc)) {
         return "Your hands are busy";
      } else if (!hu.k(this)) {
         return "Another module owns the hotbar";
      } else {
         return ta.T() ? "Critical hit pending" : null;
      }
   }

   private boolean M(long var1) {
      return this.isRuntimeActive()
         && this.i()
         && !this.A.Z()
         && this.L(var1) == null
         && j6.g(j6.l.J)
         && !si.o(this.J)
         && ((q7ae1dbb86ec789d8)mc).larpUseDelay() <= 0;
   }

   public static Vec3 Y(LivingEntity var0) {
      if (R != null && R.isEnabled()) {
         RodSwapModule var1 = R;
         return var1 == null ? null : var1.j(var0);
      } else {
         return null;
      }
   }

   private Vec3 j(LivingEntity var1) {
      if (this.isRuntimeActive() && !this.A.Z() && var1 instanceof Player var2 && this.i()) {
         long var3 = System.nanoTime();
         String var5 = this.P(var2, var3);
         if (var5 != null) {
            this.e();
            if (!var5.isEmpty()) {
               this.c(var5);
            }

            return null;
         } else {
            wt.u var6 = this.B(var2);
            if (var6 == null) {
               this.e();
               this.c("No rod arc reaches " + var2.getName().getString());
               return null;
            }

            if (this.F != var2) {
               this.F = var2;
               this.j = 0;
            }

            if (++this.j > 20) {
               com.larpclient.h.A("RodSwap", "Throw at {} did not line up within {} ticks; retrying shortly", var2.getName().getString(), 20);
               this.g.Y(var3, 250000000L);
               this.e();
               return null;
            } else {
               this.q = "Aiming";
               Vec3 var7 = this.J.getEyePosition();
               double var8 = Math.max(1.0, var7.distanceTo(var2.getBoundingBox().getCenter()));
               return var7.add(Vec3.directionFromRotation(var6.pitch(), var6.yaw()).scale(var8));
            }
         }
      } else {
         this.e();
         return null;
      }
   }

   private String P(Player var1, long var2) {
      if (!this.g.J(var2)) {
         this.q = "Delay";
         return "";
      }

      if (!this.w(var1)) {
         return "Kill Aura target " + var1.getName().getString() + " ignored: " + this.E(var1);
      }

      AuraModule var4 = AuraModule.V();
      if (var4 == null) {
         return "";
      }

      double var5 = this.i(var1);
      if (var5 <= var4.s() + 0.5) {
         this.q = "Inside sword reach";
         return "";
      }

      if (var5 > this.U.getValue()) {
         this.q = "Beyond Range";
         return "";
      }

      String var7 = this.L(var2);
      if (var7 != null) {
         this.q = var7;
      }

      return var7;
   }

   private wt.u B(Player var1) {
      Vec3 var2 = this.J.getEyePosition();
      Vec3 var3 = var1.getBoundingBox().getCenter();
      Vec3 var4 = F(var1);
      wt.u var5 = wt.c(var2, var3, 20);

      for (int var6 = 0; var6 < 2 && var5 != null; var6++) {
         var5 = wt.c(var2, var3.add(wt.M(var4, var5.ticks())), 20);
      }

      return var5;
   }

   private void e() {
      this.F = null;
      this.j = 0;
   }

   public void T() {
      if (this.isEnabled()) {
         if (this.isRuntimeActive()) {
            if (this.J != getMc().player || this.r != mc.level) {
               this.k("World changed", false);
               this.g.X();
               this.e();
               this.J = mc.player;
               this.r = mc.level;
            }

            if (!this.i()) {
               this.k("Paused", true);
            } else if (this.K()) {
               this.k("Manual slot selection", false);
               this.A.k(System.nanoTime());
            } else if (this.A.Z()) {
               if (!this.S.U()) {
                  this.k("Slot ownership changed", false);
                  this.A.k(System.nanoTime());
               } else if (this.N()
                  && this.V(this.p)
                  && hu.k(this)
                  && si.M(mc, this.J)
                  && !si.A(mc)
                  && !si.B(mc)
                  && (!mc.options.keyAttack.isDown() || mc.hitResult == null || mc.hitResult.getType() != Type.BLOCK)) {
                  if (!this.w(this.Y)) {
                     this.k("Target unavailable", true);
                  } else {
                     FishingHook var1 = this.J.fishing;
                     if (var1 == null || var1.getPlayerOwner() == this.J && var1.level() == this.r && !var1.isRemoved()) {
                        if (this.w != null && var1 != null && var1 != this.w) {
                           this.k("Bobber replaced", true);
                        } else {
                           if (var1 != null && this.w == null) {
                              this.w = var1;
                              com.larpclient.h.U("RodSwap", "Owned bobber observed; entity={}", this.w.getId());
                           }

                           Entity var2 = var1 == null ? null : var1.getHookedIn();
                           int var3 = var1 != null
                                 && var1.tickCount > 2
                                 && var1.position().subtract(this.Y.getBoundingBox().getCenter()).dot(var1.getDeltaMovement()) > 0.0
                                 && var1.distanceToSqr(this.Y) > 2.25
                              ? 1
                              : 0;
                           bz.w var4 = this.A
                              .m(
                                 System.nanoTime(),
                                 var1 != null,
                                 var2 == this.Y,
                                 var2 != null && var2 != this.Y,
                                 var1 != null && var1.onGround(),
                                 (boolean)var3
                              );
                           if (var4 == bz.w.k) {
                              this.k(var2 == this.Y ? "Hit" : "Missed or timed out", true);
                           } else if (var4 == bz.w.c) {
                              this.q = "Player hooked";
                           }
                        }
                     } else {
                        this.k("Bobber unavailable", true);
                     }
                  }
               } else {
                  this.k("Your input takes priority", true);
               }
            }
         }
      }
   }

   @Override
   public void onTick() {
      int var10000 = this.i();
      boolean var7 = false;
      byte var6 = (byte)var10000;
      var10000 = Integer.signum(var6);
      if ((var10000 | -var10000) >>> 31 == 0) {
         this.k("Paused", true);
      } else if (this.A.Z()) {
         if (this.A.w() == bz.q.H) {
            ta.F(this, this::G, this::L);
         }
      } else {
         AuraModule var1 = AuraModule.V();
         if (var1 != null && var1.isRuntimeActive()) {
            LivingEntity var3 = var1.u();
            int var17 = var3 instanceof Player;
            boolean var11 = false;
            byte var10 = (byte)var17;
            var17 = Integer.signum(var10);
            if (((var17 | -var17) >>> 31 ^ 1) == 0) {
               Player var2 = (Player)var3;
               if (this.F == var2) {
                  long var14 = System.nanoTime();
                  int var21 = this.M(var14);
                  boolean var9 = false;
                  byte var8 = (byte)var21;
                  var21 = Integer.signum(var8);
                  if ((var21 | -var21) >>> 31 == 0) {
                     String var15 = this.L(var14);
                     this.q = var15 == null ? "Aiming" : var15;
                  } else if (this.n() != var2) {
                     this.q = "Aiming";
                  } else {
                     int var5 = this.r();
                     ta.F(
                        this,
                        () -> this.t(var2, var5),
                        () -> this.M(System.nanoTime()) && this.V(var5) && this.w(var2) && this.F == var2 && var1.u() == var2 && this.n() == var2
                     );
                  }
               }
            } else {
               this.e();
               int var19 = this.q.equals("Delay");
               boolean var13 = false;
               byte var12 = (byte)var19;
               var19 = Integer.signum(var12);
               if ((var19 | -var19) >>> 31 == 0) {
                  this.q = "No player target";
               }
            }
         } else {
            this.e();
            this.q = "Enable Kill Aura";
         }
      }
   }

   private void t(Player var1, int var2) {
      this.o = this.J.getInventory().getSelectedSlot();
      this.V = this.J.getMainHandItem().copy();
      this.p = var2;
      this.e();
      if (!this.S.O(this.J, var2)) {
         this.g.Y(System.nanoTime(), 250000000L);
         this.k("Hotbar busy", false);
      } else {
         this.Y = var1;
         this.w = null;
         this.A.B(System.nanoTime(), this.D.getValue().longValue() * 1000000L, this.l.O("Hook and Reel"));
         boolean var3 = mc.gameMode.useItem(this.J, InteractionHand.MAIN_HAND).consumesAction();
         if (!var3) {
            this.k("Throw rejected locally", true);
         } else {
            ks.p(this.J, InteractionHand.MAIN_HAND);
            ((q7ae1dbb86ec789d8)mc).larpUseDelay(4);
            this.q = "Thrown";
            this.Z = null;
            com.larpclient.h.A(
               "RodSwap",
               "Throw; mode={}, rodSlot={}, returnSlot={}, target={}, distance={}, yaw={}, pitch={}",
               this.l.getValue(),
               var2 + 1,
               this.o + 1,
               this.Y.getName().getString(),
               String.format("%.2f", this.i(this.Y)),
               String.format("%.1f", this.J.getYRot()),
               String.format("%.1f", this.J.getXRot())
            );
         }
      }
   }

   private boolean L() {
      return this.isRuntimeActive()
         && this.i()
         && this.A.w() == bz.q.H
         && this.S.U()
         && this.N()
         && this.V(this.p)
         && hu.k(this)
         && si.M(mc, this.J)
         && !si.A(mc)
         && !si.B(mc)
         && !si.o(this.J)
         && ((q7ae1dbb86ec789d8)mc).larpUseDelay() <= 0
         && this.w != null
         && this.J.fishing == this.w
         && this.w.getHookedIn() == this.Y
         && this.w(this.Y);
   }

   private void G() {
      if (this.L()) {
         this.A.h();
         if (!mc.gameMode.useItem(this.J, InteractionHand.MAIN_HAND).consumesAction()) {
            this.k("Reel rejected locally", true);
         } else {
            ks.p(this.J, InteractionHand.MAIN_HAND);
            ((q7ae1dbb86ec789d8)mc).larpUseDelay(4);
            this.q = "Reeled";
            com.larpclient.h.U("RodSwap", "Reel requested for confirmed target {}; awaiting bobber removal", this.Y.getId());
         }
      }
   }

   private void k(String var1, boolean var2) {
      ta.s(this);
      int var3 = !this.A.Z() && !this.S.p() ? 0 : 1;
      bz.q var4 = this.A.w();
      int var5 = this.J != null && this.J == getMc().player && this.r == mc.level && this.J.isAlive() ? 1 : 0;
      int var6 = var5 != 0 ? this.J.getInventory().getSelectedSlot() : -1;
      this.S.V(getMc(), var2 && var5 != 0 && this.N());
      if (var5 != 0 && mc.gameMode != null && var6 != this.J.getInventory().getSelectedSlot()) {
         ((q541491ff38fb1637)mc.gameMode).larpSyncCarriedItem();
      }

      if (var3 != 0) {
         this.g.G(System.nanoTime(), this.a.getValue().longValue() * 1000000L);
      }

      this.A.g();
      this.Y = null;
      this.w = null;
      this.V = ItemStack.EMPTY;
      this.o = -1;
      this.p = -1;
      this.q = var1;
      if (var3 != 0) {
         com.larpclient.h.A(
            "RodSwap", "Throw ended: {} at {}; weapon slot restored={}", var1, var4, var5 != 0 && var6 != this.J.getInventory().getSelectedSlot()
         );
      }
   }
}
