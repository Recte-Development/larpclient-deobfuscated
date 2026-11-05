package com.larpclient.module;

import com.larpclient.yu;
import java.util.Set;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class SpeedModule extends Module {
   private static SpeedModule B;
   private final lc S = this.addSetting(new lc("Speed", "Speed multiplier", 1.5, 1.0, 10.0, 0.1));
   private final lp z = this.addSetting(new lp("Mode", "Speed method", "Vanilla", "Vanilla", "BHop", "Strafe", "LowHop", "Entity Collision"));
   private final ln E = this.addSetting(new ln("Auto Jump", "Jump automatically", true));
   private final ln k = this.addSetting(new ln("Only On Ground", "Only boost when grounded", false));
   private final ln Y = this.addSetting(new ln("Damage Boost", "Speed after taking damage", false));
   private final lc d = this.addSetting(new lc("Strafe", "Air strafe strength", 0.8, 0.1, 2.0, 0.1));
   private final ln L = this.addSetting(new ln("Smooth Accel", "Gradual speed increase", true));
   private final lc T = this.addSetting(new lc("Collision Boost", "Entity Collision only: horizontal boost per nearby entity", 0.02, 0.01, 0.06, 0.01));
   private final lc w = this.addSetting(new lc("Collision Limit", "Entity Collision only: maximum entities contributing to the boost", 1.0, 1.0, 3.0, 1.0));
   private double V;
   private String j = "";
   private boolean J;
   private int h;
   private boolean Z;
   private String H = "";
   private int u;
   private int C;

   @Override
   public Set<ld> getNetworkImpacts() {
      return Set.of(ld.c);
   }

   public SpeedModule() {
      super("Speed", "Move faster", Category.MOVEMENT, -1);
      B = this;
      this.T.x(() -> this.z.i("Entity Collision"));
      this.w.x(() -> this.z.i("Entity Collision"));
      this.S.x(() -> !this.z.i("Entity Collision"));
      this.E.x(() -> this.z.i("BHop") || this.z.i("Strafe") || this.z.i("LowHop"));
      this.k.x(() -> !this.z.i("Entity Collision"));
      this.Y.x(() -> !this.z.i("Entity Collision"));
      this.d.x(() -> this.z.i("Strafe"));
      this.L.x(() -> !this.z.i("Entity Collision"));
   }

   @Override
   public void onEnable() {
      this.onDisable();
      this.H = this.z.getValue();
   }

   @Override
   public void onDisable() {
      this.J();
      this.H = "";
      this.u = 0;
      this.C = 0;
   }

   private void J() {
      this.V = 0.0;
      this.j = "";
      this.h = 0;
      this.Z = (boolean)0;
      this.J = (boolean)0;
   }

   public static void P(LocalPlayer var0) {
      if (B != null && B.isRuntimeActive() && var0 == getMc().player) {
         B.u = 20;
         B.C = 0;
      }
   }

   @Override
   public void onTick() {
      this.C = 0;
      int var1 = this.u > 0 ? 1 : 0;
      if (var1 != 0) {
         byte var15 = 1;
         int var14 = this.u;
         this.u = var14 - var15;
      }

      if (this.isInGame()) {
         if (!this.H.equals(this.z.getValue())) {
            this.J();
            this.H = this.z.getValue();
         }

         if (yu.r() && !TargetStrafeModule.Y() && mc.gui.screen() == null && !mc.player.isInWater() && !mc.player.isInLava() && !mc.player.onClimbable()) {
            Vec3 var2 = yu.q();
            boolean var3 = mc.player.onGround();
            if (this.z.i("Entity Collision")) {
               if (var1 == 0) {
                  this.E(var2);
               }
            } else if (var2.lengthSqr() != 0.0 && (!this.k.getValue() || var3)) {
               if (!this.j.equals(this.z.getValue())) {
                  this.V = 0.0;
                  this.j = this.z.getValue();
               }

               int var4 = mc.player.hurtTime > 0 ? 1 : 0;
               if (this.Y.getValue() && var4 != 0) {
                  boolean var17 = false;
                  byte var16 = this.Z;
                  int var10000 = Integer.signum(var16);
                  if ((var10000 | -var10000) >>> 31 == 0) {
                     this.h = 10;
                  }
               }

               int var25;
               label120: {
                  this.Z = (boolean)var4;
                  if (this.Y.getValue()) {
                     var25 = this.h;
                     byte var19 = 1;
                     int var18 = this.h;
                     this.h = var18 - var19;
                     boolean var21 = false;
                     int var20 = var25;
                     if ((-Integer.signum(var20) >>> 31 ^ 1) == 0) {
                        var25 = 1;
                        break label120;
                     }
                  }

                  var25 = 0;
               }

               this.J = (boolean)var25;
               if (this.h < 0) {
                  this.h = 0;
               }

               if (!this.z.i("Vanilla") || var3) {
                  double var5 = switch ((String)this.z.getValue()) {
                     case "BHop" -> 0.26;
                     case "Strafe" -> 0.28;
                     case "LowHop" -> 0.22;
                     default -> 0.13;
                  };
                  double var22 = var5 * this.S.getValue() * (this.J ? 1.15 : 1.0);
                  this.V = this.L.getValue() ? this.V + (var22 - this.V) * 0.3 : var22;
                  Vec3 var9 = mc.player.getDeltaMovement();
                  Vec3 var10 = var2.scale(this.V);
                  Vec3 var11 = new Vec3(var9.x, 0.0, var9.z);
                  double var12 = (this.z.i("Strafe") && !var3 ? 0.04 * this.d.getValue() : 0.08) * A(this.S.getValue());
                  var11 = yu.y(var11, var10, var12);
                  mc.player.setDeltaMovement(var11.x, var9.y, var11.z);
                  if (var3 && this.E.getValue() && !this.z.i("Vanilla") && var9.y <= 0.0) {
                     if (this.z.i("LowHop")) {
                        mc.player.setDeltaMovement(var11.x, 0.32, var11.z);
                     } else {
                        mc.player.jumpFromGround();
                     }
                  }
               }
            } else {
               this.V = 0.0;
            }
         } else {
            this.J();
         }
      }
   }

   public static double A(double var0) {
      return Math.max(1.0, var0 - 4.0);
   }

   private void E(Vec3 var1) {
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
            if (EntitySelector.pushableBy(var5).test(var2) && ++this.C >= this.w.getValue().intValue()) {
               break;
            }
         }

         if (this.C != 0) {
            double var7 = Math.min(0.08, this.T.getValue() * this.C);
            Vec3 var6 = var2.getDeltaMovement();
            var2.setDeltaMovement(var6.x + var1.x * var7, var6.y, var6.z + var1.z * var7);
         }
      }
   }

   @Override
   public String getStatus() {
      if (!this.z.i("Entity Collision")) {
         return "";
      } else if (!this.isRuntimeActive()) {
         return "Inactive";
      } else if (this.u > 0) {
         return "Collision boost recovering";
      } else {
         return this.C == 0 ? "Waiting for collision" : "Collision boost active";
      }
   }
}
