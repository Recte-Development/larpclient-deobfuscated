package com.larpclient.module;

import com.larpclient.d8;
import com.larpclient.hp;
import com.larpclient.j5;
import com.larpclient.o_;
import com.larpclient.r4;
import com.larpclient.xq;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.AABB;

public final class lq {
   private final Module c;
   private final o_ S;
   private int m;
   private boolean f;
   private long t;
   private long T;
   private long x;
   private long H;
   private boolean g = (boolean)1;

   public lq(Module var1) {
      this.c = var1;
      this.S = new o_(var1);
   }

   public void b(Minecraft var1) {
      this.S.C(var1, true);
   }

   public void i(Minecraft var1) {
      if (this.S.K()) {
         this.S.m(var1, true);
      }
   }

   public void q(LocalPlayer var1) {
      this.T = 0L;
      AuraModule.s = (boolean)0;
   }

   public boolean W(LocalPlayer var1) {
      if (!AuraModule.s) {
         return false;
      }

      if (var1 != null && !var1.onGround()) {
         if (System.currentTimeMillis() <= this.T) {
            return true;
         }

         this.q(var1);
         return false;
      } else {
         this.q(var1);
         return false;
      }
   }

   public boolean F() {
      return this.W(Minecraft.getInstance().player);
   }

   public boolean V(LocalPlayer var1, lq.u var2) {
      LivingEntity var3 = var2.target();
      if (var1 != null && var3 != null && var3.isAlive()) {
         this.t(var1, var2.crit(), var2.sprint());
         if (var2.pauseWhileEating() && lf.B()) {
            this.q(var1);
            return false;
         }

         AABB var4 = var2.box() != null ? var2.box().inflate(var3.getPickRadius()) : var3.getBoundingBox().inflate(var3.getPickRadius());
         if (lf.p(var1, var4) > var2.range()) {
            this.q(var1);
            return false;
         }

         boolean var5 = this.I(var1, var2.crit(), var2.predictCrits(), var2.blatant());
         if (var2.sprint() != lq.x.P && var5 && !this.W(var1)) {
            int var6 = !var1.isSprinting() && !E() ? 0 : 1;
            this.l(var1);
            if (var6 != 0 && var2.sprint() == lq.x.L) {
               return false;
            }
         }

         if (this.r(var1, var2.legacy(), var2.tpsSync(), var2.randomize(), var2.blatant()) && var2.entityHit()) {
            int var8 = var2.ignoreWalls() && !var2.raycastWalls() ? 1 : 0;
            if (!lf.X(var1, var4, var2.yaw(), var2.pitch(), var2.range(), (boolean)var8)) {
               if (!var5 && !this.W(var1)) {
                  this.q(var1);
               }

               return false;
            } else {
               if (var2.autoJump() && var2.crit() != lq.z.J && C(var1)) {
                  var1.jumpFromGround();
                  return false;
               }

               if (!y(var1, var2.crit())) {
                  if (!var5 && !this.W(var1)) {
                     this.q(var1);
                  }

                  return false;
               } else if (!xq.E(var1.isSprinting())) {
                  if (!this.f) {
                     r4.g("Aura", "Attack deferred: waiting for vanilla sprint report; localSprint=" + var1.isSprinting());
                  }

                  this.f = (boolean)1;
                  return false;
               } else {
                  this.f = (boolean)0;
                  boolean var7 = d8.z(this.c, var3, () -> {
                     if (var2.beforeAttack() != null) {
                        var2.beforeAttack().run();
                     }

                     this.e(var1, var3, var2.shieldBreaker());
                  });
                  if (!var7) {
                     return false;
                  }

                  this.x = 0L;
                  if (var2.legacy()) {
                     this.V(var2.minCps(), var2.maxCps(), var2.tpsSync(), var2.blatant());
                  }

                  AuraModule.s = (boolean)0;
                  this.T = 0L;
                  return true;
               }
            }
         } else {
            if (!var5 && !this.W(var1)) {
               this.q(var1);
            }

            return false;
         }
      } else {
         this.q(var1);
         return false;
      }
   }

   private void e(LocalPlayer var1, LivingEntity var2, boolean var3) {
      Minecraft var4 = Minecraft.getInstance();
      if (var3 && !this.S.K() && var2 instanceof Player var5 && j5.X(var5, var1) && !j5.F(var5)) {
         int var6 = j5.X(var1);
         if (var6 >= 0 && this.S.s(var1, var6)) {
            j5.h(var5);
            r4.g("Aura", "Shield Breaker: hit " + var5.getId() + " with hotbar slot " + (var6 + 1));
         }
      }

      AuraModule.J(var1, "attack-before");
      var4.gameMode.attack(var1, var2);
      AuraModule.J(var1, "attack-after");
      hp.o(var1, InteractionHand.MAIN_HAND);
      this.g = (boolean)0;
      this.m++;
   }

   public void n() {
      this.g = (boolean)1;
      this.x = 0L;
   }

   public boolean r(LocalPlayer var1, boolean var2, boolean var3, boolean var4, boolean var5) {
      if (var1 == null) {
         return false;
      }

      if (var2) {
         return System.nanoTime() >= this.t;
      }

      boolean var6 = lf.u();
      int var7 = lf.F(var1) || var6 && !var1.onGround() ? 1 : 0;
      float var8 = !var5 && var7 == 0 ? k(var3, var4, var5) : 0.9F;
      if (var1.getAttackStrengthScale(0.5F) < var8) {
         this.x = 0L;
         return false;
      }

      if (!var5 && !this.g && var7 == 0) {
         if (this.x == 0L) {
            this.x = System.nanoTime();
            this.H = (long)((5.0 + Math.random() * 15.0) * 1000000.0);
         }

         return System.nanoTime() - this.x >= this.H;
      } else {
         this.x = 0L;
         return true;
      }
   }

   public float a(LocalPlayer var1, boolean var2, boolean var3, boolean var4, boolean var5) {
      if (var2) {
         return Math.max(0.0F, (float)(this.t - System.nanoTime()) / 5.0E7F);
      }

      if (var1 == null) {
         return Float.POSITIVE_INFINITY;
      }

      double var6 = var1.getAttributeValue(Attributes.ATTACK_SPEED);
      if (var6 <= 0.0) {
         return Float.POSITIVE_INFINITY;
      }

      float var8 = (float)(20.0 / var6);
      return Math.max(0.0F, (k(var3, var4, var5) - var1.getAttackStrengthScale(0.5F)) * var8);
   }

   private void l(LocalPlayer var1) {
      this.T = System.currentTimeMillis() + 1200L;
      AuraModule.s = (boolean)1;
      F(var1);
   }

   private void t(LocalPlayer var1, lq.z var2, lq.x var3) {
      if (var3 != lq.x.P && var2 != lq.z.J && !var1.getMainHandItem().is(Items.MACE)) {
         if (this.W(var1)) {
            F(var1);
         }
      } else {
         this.q(var1);
      }
   }

   private static void F(LocalPlayer var0) {
      if (var0 != null) {
         var0.setSprinting(false);
      }
   }

   private static boolean E() {
      Minecraft var0 = Minecraft.getInstance();
      return var0.options != null && var0.options.keySprint.isDown();
   }

   private static float W() {
      return Mth.clamp(20.0F / lm.N(), 1.0F, 3.0F);
   }

   private static float k(boolean var0, boolean var1, boolean var2) {
      if (var2) {
         return 1.0F;
      }

      float var3 = 1.0F;
      if (var1) {
         var3 = 0.97F + (float)(Math.random() * 0.03F);
      }

      return Math.min(var3, 1.0F);
   }

   private void V(float var1, float var2, boolean var3, boolean var4) {
      float var5 = Math.max(1.0F, Math.min(var1, var2));
      float var6 = Math.max(var5, Math.max(var1, var2));
      float var7 = var4 ? var6 : var5 + (float)(Math.random() * (var6 - var5));
      long var8 = (long)(1.0E9F / var7);
      if (var3) {
         var8 = Math.max(50000000L, (long)((float)var8 * W()));
      }

      this.t = System.nanoTime() + var8;
   }

   private static boolean y(LocalPlayer var0, lq.z var1) {
      if (!var0.getMainHandItem().is(Items.MACE) && var1 != lq.z.J && lf.Z(var0)) {
         boolean var2 = lf.F(var0);
         if (var1 == lq.z.u) {
            return var2;
         }

         if (var2) {
            return true;
         }

         if (!var0.onGround()) {
            return false;
         }

         Minecraft var3 = Minecraft.getInstance();
         return var3.options == null || !var3.options.keyJump.isDown();
      } else {
         return true;
      }
   }

   private boolean I(LocalPlayer var1, lq.z var2, boolean var3, boolean var4) {
      if (var2 == lq.z.J || var1.getMainHandItem().is(Items.MACE)) {
         return false;
      }

      if (f(var1)) {
         return false;
      }

      if (AuraModule.s && !var1.onGround()) {
         return true;
      }

      if (!var1.onGround() && var1.fallDistance > 0.0) {
         return true;
      }

      if (!var3) {
         return !var1.onGround() && var1.getDeltaMovement().y < 0.0;
      }

      int var5 = L(var1);
      return var5 >= 0 && var5 <= (var4 ? 1 : 2);
   }

   private static int L(LocalPlayer var0) {
      if (lf.F(var0)) {
         return 0;
      }

      double var1 = var0.getDeltaMovement().y;
      if (var0.onGround()) {
         Minecraft var3 = Minecraft.getInstance();
         if (var3.options == null || !var3.options.keyJump.isDown()) {
            return -1;
         }

         var1 = 0.42;
      }

      double var6 = var0.fallDistance;

      for (int var5 = 1; var5 <= 8; var5++) {
         var1 = (var1 - 0.08) * 0.98;
         if (var1 < 0.0) {
            var6 += -var1;
            if (var6 > 0.0) {
               return var5;
            }
         }
      }

      return -1;
   }

   private static boolean f(LocalPlayer var0) {
      return var0.onClimbable()
         || var0.isInWater()
         || var0.isInLava()
         || var0.isPassenger()
         || var0.getAbilities().flying
         || var0.isFallFlying()
         || var0.hasEffect(MobEffects.BLINDNESS)
         || var0.hasEffect(MobEffects.LEVITATION)
         || var0.hasEffect(MobEffects.SLOW_FALLING);
   }

   private static boolean C(LocalPlayer var0) {
      return var0.onGround() && !var0.onClimbable() && !var0.isInWater() && !var0.isInLava() && !var0.isPassenger() && !var0.getAbilities().flying;
   }

   public int o() {
      return this.m;
   }

   public void Q() {
      this.m = 0;
      this.g = (boolean)1;
      this.t = 0L;
      this.f = (boolean)0;
      this.T = 0L;
      AuraModule.s = (boolean)0;
   }

   public record u(
      LivingEntity target,
      AABB box,
      float yaw,
      float pitch,
      double range,
      boolean ignoreWalls,
      lq.z crit,
      lq.x sprint,
      boolean shieldBreaker,
      boolean pauseWhileEating,
      boolean raycastWalls,
      boolean entityHit,
      boolean autoJump,
      boolean predictCrits,
      Runnable beforeAttack,
      boolean legacy,
      boolean tpsSync,
      boolean randomize,
      boolean blatant,
      float minCps,
      float maxCps
   ) {
   }

   public enum x {
      L,
      t,
      P;
   }

   public enum z {
      J,
      s,
      u;
   }
}
