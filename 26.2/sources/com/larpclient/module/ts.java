package com.larpclient.module;

import com.larpclient.hu;
import com.larpclient.kq;
import com.larpclient.ks;
import com.larpclient.si;
import com.larpclient.ta;
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

public final class ts {
   private final Module d;
   private final hu a;
   private int b;
   private boolean I;
   private long p;
   private long P;
   private long j;
   private long R;
   private boolean H = (boolean)1;

   public ts(Module var1) {
      this.d = var1;
      this.a = new hu(var1);
   }

   public void M(Minecraft var1) {
      this.a.J(var1, true);
   }

   public void L(Minecraft var1) {
      if (this.a.p()) {
         this.a.V(var1, true);
      }
   }

   public void l(LocalPlayer var1) {
      this.P = 0L;
      AuraModule.al = (boolean)0;
   }

   public boolean G(LocalPlayer var1) {
      if (!AuraModule.al) {
         return false;
      }

      if (var1 != null && !var1.onGround()) {
         if (System.currentTimeMillis() <= this.P) {
            return true;
         }

         this.l(var1);
         return false;
      } else {
         this.l(var1);
         return false;
      }
   }

   public boolean T() {
      return this.G(Minecraft.getInstance().player);
   }

   public boolean K(LocalPlayer var1, ts.w var2) {
      LivingEntity var3 = var2.target();
      if (var1 != null && var3 != null && var3.isAlive()) {
         this.E(var1, var2.crit(), var2.sprint());
         if (var2.pauseWhileEating() && com.larpclient.module.y.w()) {
            this.l(var1);
            return false;
         }

         AABB var4 = var2.box() != null ? var2.box().inflate(var3.getPickRadius()) : var3.getBoundingBox().inflate(var3.getPickRadius());
         if (com.larpclient.module.y.D(var1, var4) > var2.range()) {
            this.l(var1);
            return false;
         }

         boolean var5 = this.J(var1, var2.crit(), var2.predictCrits(), var2.blatant());
         if (var2.sprint() != ts.u.W && var5 && !this.G(var1)) {
            int var6 = !var1.isSprinting() && !t() ? 0 : 1;
            this.V(var1);
            if (var6 != 0 && var2.sprint() == ts.u.i) {
               return false;
            }
         }

         if (this.G(var1, var2.legacy(), var2.tpsSync(), var2.randomize(), var2.blatant()) && var2.entityHit()) {
            int var8 = var2.ignoreWalls() && !var2.raycastWalls() ? 1 : 0;
            if (!com.larpclient.module.y.t(var1, var4, var2.yaw(), var2.pitch(), var2.range(), (boolean)var8)) {
               if (!var5 && !this.G(var1)) {
                  this.l(var1);
               }

               return false;
            } else {
               if (var2.autoJump() && var2.crit() != ts.y.P && u(var1)) {
                  var1.jumpFromGround();
                  return false;
               }

               if (!w(var1, var2.crit())) {
                  if (!var5 && !this.G(var1)) {
                     this.l(var1);
                  }

                  return false;
               } else if (!kq.M(var1.isSprinting())) {
                  if (!this.I) {
                     com.larpclient.h.U("Aura", "Attack deferred: waiting for vanilla sprint report; localSprint=" + var1.isSprinting());
                  }

                  this.I = (boolean)1;
                  return false;
               } else {
                  this.I = (boolean)0;
                  boolean var7 = ta.B(this.d, var3, () -> {
                     if (var2.beforeAttack() != null) {
                        var2.beforeAttack().run();
                     }

                     this.y(var1, var3, var2.shieldBreaker());
                  });
                  if (!var7) {
                     return false;
                  }

                  this.j = 0L;
                  if (var2.legacy()) {
                     this.G(var2.minCps(), var2.maxCps(), var2.tpsSync(), var2.blatant());
                  }

                  AuraModule.al = (boolean)0;
                  this.P = 0L;
                  return true;
               }
            }
         } else {
            if (!var5 && !this.G(var1)) {
               this.l(var1);
            }

            return false;
         }
      } else {
         this.l(var1);
         return false;
      }
   }

   private void y(LocalPlayer var1, LivingEntity var2, boolean var3) {
      Minecraft var4 = Minecraft.getInstance();
      if (var3 && !this.a.p() && var2 instanceof Player var5 && si.k(var5, var1) && !si.x(var5)) {
         int var6 = si.E(var1);
         if (var6 >= 0 && this.a.O(var1, var6)) {
            si.V(var5);
            com.larpclient.h.U("Aura", "Shield Breaker: hit " + var5.getId() + " with hotbar slot " + (var6 + 1));
         }
      }

      AuraModule.M(var1, "attack-before");
      var4.gameMode.attack(var1, var2);
      AuraModule.M(var1, "attack-after");
      ks.X(var1, InteractionHand.MAIN_HAND);
      this.H = (boolean)0;
      this.b++;
   }

   public void M() {
      this.H = (boolean)1;
      this.j = 0L;
   }

   public boolean G(LocalPlayer var1, boolean var2, boolean var3, boolean var4, boolean var5) {
      if (var1 == null) {
         return false;
      }

      if (var2) {
         return System.nanoTime() >= this.p;
      }

      boolean var6 = com.larpclient.module.y.B();
      int var7 = !com.larpclient.module.y.V(var1) && (!var6 || var1.onGround()) ? 0 : 1;
      float var8 = !var5 && var7 == 0 ? J(var3, var4, var5) : 0.9F;
      if (var1.getAttackStrengthScale(0.5F) < var8) {
         this.j = 0L;
         return false;
      }

      if (!var5 && !this.H && var7 == 0) {
         if (this.j == 0L) {
            this.j = System.nanoTime();
            this.R = (long)((5.0 + Math.random() * 15.0) * 1000000.0);
         }

         return System.nanoTime() - this.j >= this.R;
      } else {
         this.j = 0L;
         return true;
      }
   }

   public float b(LocalPlayer var1, boolean var2, boolean var3, boolean var4, boolean var5) {
      if (var2) {
         return Math.max(0.0F, (float)(this.p - System.nanoTime()) / 5.0E7F);
      }

      if (var1 == null) {
         return Float.POSITIVE_INFINITY;
      }

      double var6 = var1.getAttributeValue(Attributes.ATTACK_SPEED);
      if (var6 <= 0.0) {
         return Float.POSITIVE_INFINITY;
      }

      float var8 = (float)(20.0 / var6);
      return Math.max(0.0F, (J(var3, var4, var5) - var1.getAttackStrengthScale(0.5F)) * var8);
   }

   private void V(LocalPlayer var1) {
      this.P = System.currentTimeMillis() + 1200L;
      AuraModule.al = (boolean)1;
      P(var1);
   }

   private void E(LocalPlayer var1, ts.y var2, ts.u var3) {
      if (var3 != ts.u.W && var2 != ts.y.P && !var1.getMainHandItem().is(Items.MACE)) {
         if (this.G(var1)) {
            P(var1);
         }
      } else {
         this.l(var1);
      }
   }

   private static void P(LocalPlayer var0) {
      if (var0 != null) {
         var0.setSprinting(false);
      }
   }

   private static boolean t() {
      Minecraft var0 = Minecraft.getInstance();
      return var0.options != null && var0.options.keySprint.isDown();
   }

   private static float a() {
      return Mth.clamp(20.0F / t3.v(), 1.0F, 3.0F);
   }

   private static float J(boolean var0, boolean var1, boolean var2) {
      if (var2) {
         return 1.0F;
      }

      float var3 = 1.0F;
      if (var1) {
         var3 = 0.97F + (float)(Math.random() * 0.03F);
      }

      return Math.min(var3, 1.0F);
   }

   private void G(float var1, float var2, boolean var3, boolean var4) {
      float var5 = Math.max(1.0F, Math.min(var1, var2));
      float var6 = Math.max(var5, Math.max(var1, var2));
      float var7 = var4 ? var6 : var5 + (float)(Math.random() * (var6 - var5));
      long var8 = (long)(1.0E9F / var7);
      if (var3) {
         var8 = Math.max(50000000L, (long)((float)var8 * a()));
      }

      this.p = System.nanoTime() + var8;
   }

   private static boolean w(LocalPlayer var0, ts.y var1) {
      if (!var0.getMainHandItem().is(Items.MACE) && var1 != ts.y.P && com.larpclient.module.y.C(var0)) {
         boolean var2 = com.larpclient.module.y.V(var0);
         if (var1 == ts.y.g) {
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

   private boolean J(LocalPlayer var1, ts.y var2, boolean var3, boolean var4) {
      if (var2 == ts.y.P || var1.getMainHandItem().is(Items.MACE)) {
         return false;
      }

      if (F(var1)) {
         return false;
      }

      if (AuraModule.al && !var1.onGround()) {
         return true;
      }

      if (!var1.onGround() && var1.fallDistance > 0.0) {
         return true;
      }

      if (!var3) {
         return !var1.onGround() && var1.getDeltaMovement().y < 0.0;
      }

      int var5 = V(var1);
      return var5 >= 0 && var5 <= (var4 ? 1 : 2);
   }

   private static int V(LocalPlayer var0) {
      if (com.larpclient.module.y.V(var0)) {
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

   private static boolean F(LocalPlayer var0) {
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

   private static boolean u(LocalPlayer var0) {
      return var0.onGround() && !var0.onClimbable() && !var0.isInWater() && !var0.isInLava() && !var0.isPassenger() && !var0.getAbilities().flying;
   }

   public int F() {
      return this.b;
   }

   public void h() {
      this.b = 0;
      this.H = (boolean)1;
      this.p = 0L;
      this.I = (boolean)0;
      this.P = 0L;
      AuraModule.al = (boolean)0;
   }

   public enum u {
      i,
      r,
      W;
   }

   public record w(
      LivingEntity target,
      AABB box,
      float yaw,
      float pitch,
      double range,
      boolean ignoreWalls,
      ts.y crit,
      ts.u sprint,
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

   public enum y {
      P,
      r,
      g;
   }
}
