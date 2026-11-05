package com.larpclient.module;

import com.larpclient.te;
import java.util.Set;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class SpeedModule extends Module {
   private static SpeedModule H;
   private final d9 B = this.addSetting(new d9("Speed", "Speed multiplier", 1.5, 1.0, 10.0, 0.1));
   private final d6 T = this.addSetting(new d6("Mode", "Speed method", "Vanilla", "Vanilla", "BHop", "Strafe", "LowHop", "Entity Collision"));
   private final dh a = this.addSetting(new dh("Auto Jump", "Jump automatically", true));
   private final dh u = this.addSetting(new dh("Only On Ground", "Only boost when grounded", false));
   private final dh R = this.addSetting(new dh("Damage Boost", "Speed after taking damage", false));
   private final d9 I = this.addSetting(new d9("Strafe", "Air strafe strength", 0.8, 0.1, 2.0, 0.1));
   private final dh M = this.addSetting(new dh("Smooth Accel", "Gradual speed increase", true));
   private final d9 c = this.addSetting(new d9("Collision Boost", "Entity Collision only: horizontal boost per nearby entity", 0.02, 0.01, 0.06, 0.01));
   private final d9 w = this.addSetting(new d9("Collision Limit", "Entity Collision only: maximum entities contributing to the boost", 1.0, 1.0, 3.0, 1.0));
   private double L;
   private String p = "";
   private boolean k;
   private int s;
   private boolean K;
   private String F = "";
   private int i;
   private int y;

   @Override
   public Set<dm> getNetworkImpacts() {
      return Set.of(dm.j);
   }

   public SpeedModule() {
      super("Speed", "Move faster", Category.MOVEMENT, -1);
      H = this;
      this.c.H(() -> this.T.S("Entity Collision"));
      this.w.H(() -> this.T.S("Entity Collision"));
      this.B.H(() -> !this.T.S("Entity Collision"));
      this.a.H(() -> this.T.S("BHop") || this.T.S("Strafe") || this.T.S("LowHop"));
      this.u.H(() -> !this.T.S("Entity Collision"));
      this.R.H(() -> !this.T.S("Entity Collision"));
      this.I.H(() -> this.T.S("Strafe"));
      this.M.H(() -> !this.T.S("Entity Collision"));
   }

   @Override
   public void onEnable() {
      this.onDisable();
      this.F = this.T.P();
   }

   @Override
   public void onDisable() {
      this.x();
      this.F = "";
      this.i = 0;
      this.y = 0;
   }

   private void x() {
      this.L = 0.0;
      this.p = "";
      this.s = 0;
      this.K = (boolean)0;
      this.k = (boolean)0;
   }

   public static void w(LocalPlayer var0) {
      if (H != null && H.isRuntimeActive() && var0 == getMc().player) {
         H.i = 20;
         H.y = 0;
      }
   }

   @Override
   public void onTick() {
      this.y = 0;
      int var1 = this.i > 0 ? 1 : 0;
      if (var1 != 0) {
         byte var21 = 1;
         int var20 = this.i;
         this.i = var20 - var21;
      }

      if (this.isInGame()) {
         if (!this.F.equals(this.T.P())) {
            this.x();
            this.F = this.T.P();
         }

         if (te.i() && !TargetStrafeModule.h() && mc.gui.screen() == null && !mc.player.isInWater() && !mc.player.isInLava() && !mc.player.onClimbable()) {
            Vec3 var2 = te.S();
            boolean var3 = mc.player.onGround();
            if (this.T.S("Entity Collision")) {
               if (var1 == 0) {
                  this.d(var2);
               }
            } else if (var2.lengthSqr() != 0.0 && (!this.u.P() || var3)) {
               if (!this.p.equals(this.T.P())) {
                  this.L = 0.0;
                  this.p = this.T.P();
               }

               boolean var15 = false;
               int var14 = mc.player.hurtTime;
               int var4 = (-Integer.signum(var14) >>> 31 ^ 1) == 0 ? 1 : 0;
               if (this.R.P() && var4 != 0 && !this.K) {
                  this.s = 10;
               }

               int var24;
               label120: {
                  this.K = (boolean)var4;
                  if (this.R.P()) {
                     var24 = this.s;
                     byte var17 = 1;
                     int var16 = this.s;
                     this.s = var16 - var17;
                     if (var24 > 0) {
                        var24 = 1;
                        break label120;
                     }
                  }

                  var24 = 0;
               }

               this.k = (boolean)var24;
               boolean var19 = false;
               int var18 = this.s;
               if ((Integer.signum(var18) >>> 31 ^ 1) == 0) {
                  this.s = 0;
               }

               if (!this.T.S("Vanilla") || var3) {
                  double var5 = switch ((String)this.T.P()) {
                     case "BHop" -> 0.26;
                     case "Strafe" -> 0.28;
                     case "LowHop" -> 0.22;
                     default -> 0.13;
                  };
                  double var22 = var5 * this.B.P() * (this.k ? 1.15 : 1.0);
                  this.L = this.M.P() ? this.L + (var22 - this.L) * 0.3 : var22;
                  Vec3 var9 = mc.player.getDeltaMovement();
                  Vec3 var10 = var2.scale(this.L);
                  Vec3 var11 = new Vec3(var9.x, 0.0, var9.z);
                  double var12 = (this.T.S("Strafe") && !var3 ? 0.04 * this.I.P() : 0.08) * G(this.B.P());
                  var11 = te.j(var11, var10, var12);
                  mc.player.setDeltaMovement(var11.x, var9.y, var11.z);
                  if (var3 && this.a.P() && !this.T.S("Vanilla") && var9.y <= 0.0) {
                     if (this.T.S("LowHop")) {
                        mc.player.setDeltaMovement(var11.x, 0.32, var11.z);
                     } else {
                        mc.player.jumpFromGround();
                     }
                  }
               }
            } else {
               this.L = 0.0;
            }
         } else {
            this.x();
         }
      }
   }

   public static double G(double var0) {
      return Math.max(1.0, var0 - 4.0);
   }

   private void d(Vec3 var1) {
      LocalPlayer var2 = mc.player;
      if (var1.lengthSqr() != 0.0
         && !var2.isPassenger()
         && !var2.isSpectator()
         && !var2.getAbilities().flying
         && !var2.isFallFlying()
         && !var2.isCrouching()
         && var2.hurtTime <= 0) {
         AABB var3 = var2.getBoundingBox().inflate(0.19);

         for (Entity var5 : mc.level
            .getEntities(
               var2,
               var3,
               var0 -> var0 instanceof LivingEntity
                  && !(var0 instanceof ArmorStand)
                  && var0.isAlive()
                  && !var0.isSpectator()
                  && !var0.isPassenger()
                  && var0.isPushable()
            )) {
            if (EntitySelector.pushableBy(var5).test(var2) && ++this.y >= this.w.P().intValue()) {
               break;
            }
         }

         if (this.y != 0) {
            double var7 = Math.min(0.08, this.c.P() * this.y);
            Vec3 var6 = var2.getDeltaMovement();
            var2.setDeltaMovement(var6.x + var1.x * var7, var6.y, var6.z + var1.z * var7);
         }
      }
   }

   @Override
   public String getStatus() {
      if (!this.T.S("Entity Collision")) {
         return "";
      } else if (!this.isRuntimeActive()) {
         return "Inactive";
      } else if (this.i > 0) {
         return "Collision boost recovering";
      } else {
         return this.y == 0 ? "Waiting for collision" : "Collision boost active";
      }
   }
}
