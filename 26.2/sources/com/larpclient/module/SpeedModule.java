package com.larpclient.module;

import com.larpclient.c5;
import java.util.Set;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class SpeedModule extends Module {
   private static SpeedModule L;
   private final e b = this.addSetting(new e("Speed", "Speed multiplier", 1.5, 1.0, 10.0, 0.1));
   private final l a = this.addSetting(new l("Mode", "Speed method", "Vanilla", "Vanilla", "BHop", "Strafe", "LowHop", "Entity Collision"));
   private final s w = this.addSetting(new s("Auto Jump", "Jump automatically", true));
   private final s d = this.addSetting(new s("Only On Ground", "Only boost when grounded", false));
   private final s t = this.addSetting(new s("Damage Boost", "Speed after taking damage", false));
   private final e c = this.addSetting(new e("Strafe", "Air strafe strength", 0.8, 0.1, 2.0, 0.1));
   private final s T = this.addSetting(new s("Smooth Accel", "Gradual speed increase", true));
   private final e l = this.addSetting(new e("Collision Boost", "Entity Collision only: horizontal boost per nearby entity", 0.02, 0.01, 0.06, 0.01));
   private final e o = this.addSetting(new e("Collision Limit", "Entity Collision only: maximum entities contributing to the boost", 1.0, 1.0, 3.0, 1.0));
   private double q;
   private String u = "";
   private boolean Q;
   private int W;
   private boolean O;
   private String g = "";
   private int z;
   private int x;

   @Override
   public Set<m> getNetworkImpacts() {
      return Set.of(m.U);
   }

   public SpeedModule() {
      super("Speed", "Move faster", Category.MOVEMENT, -1);
      L = this;
      this.l.P(() -> this.a.O("Entity Collision"));
      this.o.P(() -> this.a.O("Entity Collision"));
      this.b.P(() -> !this.a.O("Entity Collision"));
      this.w.P(() -> this.a.O("BHop") || this.a.O("Strafe") || this.a.O("LowHop"));
      this.d.P(() -> !this.a.O("Entity Collision"));
      this.t.P(() -> !this.a.O("Entity Collision"));
      this.c.P(() -> this.a.O("Strafe"));
      this.T.P(() -> !this.a.O("Entity Collision"));
   }

   @Override
   public void onEnable() {
      this.onDisable();
      this.g = this.a.getValue();
   }

   @Override
   public void onDisable() {
      this.L();
      this.g = "";
      this.z = 0;
      this.x = 0;
   }

   private void L() {
      this.q = 0.0;
      this.u = "";
      this.W = 0;
      this.O = (boolean)0;
      this.Q = (boolean)0;
   }

   public static void C(LocalPlayer var0) {
      if (L != null && L.isRuntimeActive() && var0 == getMc().player) {
         L.z = 20;
         L.x = 0;
      }
   }

   @Override
   public void onTick() {
      this.x = 0;
      int var1 = this.z > 0 ? 1 : 0;
      if (var1 != 0) {
         byte var17 = 1;
         int var16 = this.z;
         this.z = var16 - var17;
      }

      if (this.isInGame()) {
         if (!this.g.equals(this.a.getValue())) {
            this.L();
            this.g = this.a.getValue();
         }

         if (c5.J() && !TargetStrafeModule.Z() && mc.gui.screen() == null && !mc.player.isInWater() && !mc.player.isInLava() && !mc.player.onClimbable()) {
            Vec3 var2 = c5.D();
            boolean var3 = mc.player.onGround();
            if (this.a.O("Entity Collision")) {
               if (var1 == 0) {
                  this.q(var2);
               }
            } else if (var2.lengthSqr() != 0.0 && (!this.d.getValue() || var3)) {
               if (!this.u.equals(this.a.getValue())) {
                  this.q = 0.0;
                  this.u = this.a.getValue();
               }

               boolean var21 = false;
               int var20 = mc.player.hurtTime;
               int var4 = (-Integer.signum(var20) >>> 31 ^ 1) == 0 ? 1 : 0;
               if (this.t.getValue() && var4 != 0 && !this.O) {
                  this.W = 10;
               }

               int var25;
               label120: {
                  this.O = (boolean)var4;
                  if (this.t.getValue()) {
                     var25 = this.W;
                     byte var19 = 1;
                     int var18 = this.W;
                     this.W = var18 - var19;
                     if (var25 > 0) {
                        var25 = 1;
                        break label120;
                     }
                  }

                  var25 = 0;
               }

               this.Q = (boolean)var25;
               if (this.W < 0) {
                  this.W = 0;
               }

               if (!this.a.O("Vanilla") || var3) {
                  double var5 = switch ((String)this.a.getValue()) {
                     case "BHop" -> 0.26;
                     case "Strafe" -> 0.28;
                     case "LowHop" -> 0.22;
                     default -> 0.13;
                  };
                  double var24 = var5 * this.b.getValue();
                  boolean var15 = false;
                  byte var14 = this.Q;
                  var25 = Integer.signum(var14);
                  double var22 = var24 * (((var25 | -var25) >>> 31 ^ 1) == 0 ? 1.15 : 1.0);
                  this.q = this.T.getValue() ? this.q + (var22 - this.q) * 0.3 : var22;
                  Vec3 var9 = mc.player.getDeltaMovement();
                  Vec3 var10 = var2.scale(this.q);
                  Vec3 var11 = new Vec3(var9.x, 0.0, var9.z);
                  double var12 = (this.a.O("Strafe") && !var3 ? 0.04 * this.c.getValue() : 0.08) * a(this.b.getValue());
                  var11 = c5.S(var11, var10, var12);
                  mc.player.setDeltaMovement(var11.x, var9.y, var11.z);
                  if (var3 && this.w.getValue() && !this.a.O("Vanilla") && var9.y <= 0.0) {
                     if (this.a.O("LowHop")) {
                        mc.player.setDeltaMovement(var11.x, 0.32, var11.z);
                     } else {
                        mc.player.jumpFromGround();
                     }
                  }
               }
            } else {
               this.q = 0.0;
            }
         } else {
            this.L();
         }
      }
   }

   public static double a(double var0) {
      return Math.max(1.0, var0 - 4.0);
   }

   private void q(Vec3 var1) {
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
            if (EntitySelector.pushableBy(var5).test(var2) && ++this.x >= this.o.getValue().intValue()) {
               break;
            }
         }

         if (this.x != 0) {
            double var7 = Math.min(0.08, this.l.getValue() * this.x);
            Vec3 var6 = var2.getDeltaMovement();
            var2.setDeltaMovement(var6.x + var1.x * var7, var6.y, var6.z + var1.z * var7);
         }
      }
   }

   @Override
   public String getStatus() {
      if (!this.a.O("Entity Collision")) {
         return "";
      } else if (!this.isRuntimeActive()) {
         return "Inactive";
      } else if (this.z > 0) {
         return "Collision boost recovering";
      } else {
         return this.x == 0 ? "Waiting for collision" : "Collision boost active";
      }
   }
}
