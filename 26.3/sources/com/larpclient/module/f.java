package com.larpclient.module;

import com.larpclient._p;
import com.larpclient._q;
import com.larpclient.h8;
import com.larpclient.q1;
import com.larpclient.qg;
import com.larpclient.s6;
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

public final class f {
   private final Module u;
   private final s6 g;
   private int J;
   private boolean k;
   private long Y;
   private long H;
   private long D;
   private long W;
   private boolean Q = (boolean)1;

   public f(Module var1) {
      this.u = var1;
      this.g = new s6(var1);
   }

   public void X(Minecraft var1) {
      this.g.G(var1, true);
   }

   public void b(Minecraft var1) {
      if (this.g.j()) {
         this.g.X(var1, true);
      }
   }

   public void w(LocalPlayer var1) {
      this.H = 0L;
      AuraModule.K = (boolean)0;
   }

   public boolean H(LocalPlayer var1) {
      if (!AuraModule.K) {
         return false;
      }

      if (var1 != null && !var1.onGround()) {
         if (System.currentTimeMillis() <= this.H) {
            return true;
         }

         this.w(var1);
         return false;
      } else {
         this.w(var1);
         return false;
      }
   }

   public boolean Y() {
      return this.H(Minecraft.getInstance().player);
   }

   public boolean M(LocalPlayer var1, f.c var2) {
      LivingEntity var3 = var2.target();
      if (var1 != null && var3 != null && var3.isAlive()) {
         this.O(var1, var2.crit(), var2.sprint());
         if (var2.pauseWhileEating() && com.larpclient.module.v.d()) {
            this.w(var1);
            return false;
         }

         AABB var4 = var2.box() != null ? var2.box().inflate(var3.getPickRadius()) : var3.getBoundingBox().inflate(var3.getPickRadius());
         if (com.larpclient.module.v.E(var1, var4) > var2.range()) {
            this.w(var1);
            return false;
         }

         boolean var5 = this.p(var1, var2.crit(), var2.predictCrits(), var2.blatant());
         if (var2.sprint() != f.t.k && var5 && !this.H(var1)) {
            int var6 = !var1.isSprinting() && !j() ? 0 : 1;
            this.U(var1);
            if (var6 != 0 && var2.sprint() == f.t.d) {
               return false;
            }
         }

         if (this.z(var1, var2.legacy(), var2.tpsSync(), var2.randomize(), var2.blatant()) && var2.entityHit()) {
            int var8 = var2.ignoreWalls() && !var2.raycastWalls() ? 1 : 0;
            if (!com.larpclient.module.v.B(var1, var4, var2.yaw(), var2.pitch(), var2.range(), (boolean)var8)) {
               if (!var5 && !this.H(var1)) {
                  this.w(var1);
               }

               return false;
            } else {
               if (var2.autoJump() && var2.crit() != f.v.z && T(var1)) {
                  var1.jumpFromGround();
                  return false;
               }

               if (!X(var1, var2.crit())) {
                  if (!var5 && !this.H(var1)) {
                     this.w(var1);
                  }

                  return false;
               } else if (!q1.h(var1.isSprinting())) {
                  if (!this.k) {
                     _q.c("Aura", "Attack deferred: waiting for vanilla sprint report; localSprint=" + var1.isSprinting());
                  }

                  this.k = (boolean)1;
                  return false;
               } else {
                  this.k = (boolean)0;
                  boolean var7 = _p.d(this.u, var3, () -> {
                     if (var2.beforeAttack() != null) {
                        var2.beforeAttack().run();
                     }

                     this.y(var1, var3, var2.shieldBreaker());
                  });
                  if (!var7) {
                     return false;
                  }

                  this.D = 0L;
                  if (var2.legacy()) {
                     this.X(var2.minCps(), var2.maxCps(), var2.tpsSync(), var2.blatant());
                  }

                  AuraModule.K = (boolean)0;
                  this.H = 0L;
                  return true;
               }
            }
         } else {
            if (!var5 && !this.H(var1)) {
               this.w(var1);
            }

            return false;
         }
      } else {
         this.w(var1);
         return false;
      }
   }

   private void y(LocalPlayer var1, LivingEntity var2, boolean var3) {
      Minecraft var4 = Minecraft.getInstance();
      if (var3 && !this.g.j() && var2 instanceof Player var5 && qg.S(var5, var1) && !qg.y(var5)) {
         int var6 = qg.K(var1);
         if (var6 >= 0 && this.g.h(var1, var6)) {
            qg.R(var5);
            _q.c("Aura", "Shield Breaker: hit " + var5.getId() + " with hotbar slot " + (var6 + 1));
         }
      }

      AuraModule.H(var1, "attack-before");
      var4.gameMode.attack(var1, var2);
      AuraModule.H(var1, "attack-after");
      h8.Q(var1, InteractionHand.MAIN_HAND);
      this.Q = (boolean)0;
      this.J++;
   }

   public void j() {
      this.Q = (boolean)1;
      this.D = 0L;
   }

   public boolean z(LocalPlayer var1, boolean var2, boolean var3, boolean var4, boolean var5) {
      if (var1 == null) {
         return false;
      }

      if (var2) {
         return System.nanoTime() >= this.Y;
      }

      boolean var6 = com.larpclient.module.v.r();
      int var7 = !com.larpclient.module.v.N(var1) && (!var6 || var1.onGround()) ? 0 : 1;
      float var8 = !var5 && var7 == 0 ? d(var3, var4, var5) : 0.9F;
      if (var1.getAttackStrengthScale(0.5F) < var8) {
         this.D = 0L;
         return false;
      }

      if (!var5 && !this.Q && var7 == 0) {
         if (this.D == 0L) {
            this.D = System.nanoTime();
            this.W = (long)((5.0 + Math.random() * 15.0) * 1000000.0);
         }

         return System.nanoTime() - this.D >= this.W;
      } else {
         this.D = 0L;
         return true;
      }
   }

   public float v(LocalPlayer var1, boolean var2, boolean var3, boolean var4, boolean var5) {
      if (var2) {
         return Math.max(0.0F, (float)(this.Y - System.nanoTime()) / 5.0E7F);
      }

      if (var1 == null) {
         return Float.POSITIVE_INFINITY;
      }

      double var6 = var1.getAttributeValue(Attributes.ATTACK_SPEED);
      if (var6 <= 0.0) {
         return Float.POSITIVE_INFINITY;
      }

      float var8 = (float)(20.0 / var6);
      return Math.max(0.0F, (d(var3, var4, var5) - var1.getAttackStrengthScale(0.5F)) * var8);
   }

   private void U(LocalPlayer var1) {
      this.H = System.currentTimeMillis() + 1200L;
      AuraModule.K = (boolean)1;
      r(var1);
   }

   private void O(LocalPlayer var1, f.v var2, f.t var3) {
      if (var3 != f.t.k && var2 != f.v.z && !var1.getMainHandItem().is(Items.MACE)) {
         if (this.H(var1)) {
            r(var1);
         }
      } else {
         this.w(var1);
      }
   }

   private static void r(LocalPlayer var0) {
      if (var0 != null) {
         var0.setSprinting(false);
      }
   }

   private static boolean j() {
      Minecraft var0 = Minecraft.getInstance();
      return var0.options != null && var0.options.keySprint.isDown();
   }

   private static float p() {
      return Mth.clamp(20.0F / y.a(), 1.0F, 3.0F);
   }

   private static float d(boolean var0, boolean var1, boolean var2) {
      if (var2) {
         return 1.0F;
      }

      float var3 = 1.0F;
      if (var1) {
         var3 = 0.97F + (float)(Math.random() * 0.03F);
      }

      return Math.min(var3, 1.0F);
   }

   private void X(float var1, float var2, boolean var3, boolean var4) {
      float var5 = Math.max(1.0F, Math.min(var1, var2));
      float var6 = Math.max(var5, Math.max(var1, var2));
      float var7 = var4 ? var6 : var5 + (float)(Math.random() * (var6 - var5));
      long var8 = (long)(1.0E9F / var7);
      if (var3) {
         var8 = Math.max(50000000L, (long)((float)var8 * p()));
      }

      this.Y = System.nanoTime() + var8;
   }

   private static boolean X(LocalPlayer var0, f.v var1) {
      if (!var0.getMainHandItem().is(Items.MACE) && var1 != f.v.z && com.larpclient.module.v.n(var0)) {
         boolean var2 = com.larpclient.module.v.N(var0);
         if (var1 == f.v.L) {
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

   private boolean p(LocalPlayer var1, f.v var2, boolean var3, boolean var4) {
      if (var2 == f.v.z || var1.getMainHandItem().is(Items.MACE)) {
         return false;
      }

      if (k(var1)) {
         return false;
      }

      if (AuraModule.K && !var1.onGround()) {
         return true;
      }

      if (!var1.onGround() && var1.fallDistance > 0.0) {
         return true;
      }

      if (!var3) {
         return !var1.onGround() && var1.getDeltaMovement().y < 0.0;
      }

      int var5 = j(var1);
      return var5 >= 0 && var5 <= (var4 ? 1 : 2);
   }

   private static int j(LocalPlayer var0) {
      if (com.larpclient.module.v.N(var0)) {
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

   private static boolean k(LocalPlayer var0) {
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

   private static boolean T(LocalPlayer var0) {
      return var0.onGround() && !var0.onClimbable() && !var0.isInWater() && !var0.isInLava() && !var0.isPassenger() && !var0.getAbilities().flying;
   }

   public int X() {
      return this.J;
   }

   public void x() {
      this.J = 0;
      this.Q = (boolean)1;
      this.Y = 0L;
      this.k = (boolean)0;
      this.H = 0L;
      AuraModule.K = (boolean)0;
   }

   public record c(
      LivingEntity target,
      AABB box,
      float yaw,
      float pitch,
      double range,
      boolean ignoreWalls,
      f.v crit,
      f.t sprint,
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

   public enum t {
      d,
      E,
      k;
   }

   public enum v {
      z,
      S,
      L;
   }
}
