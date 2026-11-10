package com.larpclient.module;

import com.larpclient.nz;
import java.util.Set;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class SpeedModule extends Module {
   private static SpeedModule a;
   private final b P = this.addSetting(new b("Speed", "Speed multiplier", 1.5, 1.0, 10.0, 0.1));
   private final o j = this.addSetting(new o("Mode", "Speed method", "Vanilla", "Vanilla", "BHop", "Strafe", "LowHop", "Entity Collision"));
   private final m I = this.addSetting(new m("Auto Jump", "Jump automatically", true));
   private final m f = this.addSetting(new m("Only On Ground", "Only boost when grounded", false));
   private final m o = this.addSetting(new m("Damage Boost", "Speed after taking damage", false));
   private final b w = this.addSetting(new b("Strafe", "Air strafe strength", 0.8, 0.1, 2.0, 0.1));
   private final m d = this.addSetting(new m("Smooth Accel", "Gradual speed increase", true));
   private final b q = this.addSetting(new b("Collision Boost", "Entity Collision only: horizontal boost per nearby entity", 0.02, 0.01, 0.06, 0.01));
   private final b k = this.addSetting(new b("Collision Limit", "Entity Collision only: maximum entities contributing to the boost", 1.0, 1.0, 3.0, 1.0));
   private double G;
   private String e = "";
   private boolean n;
   private int R;
   private boolean u;
   private String x = "";
   private int H;
   private int b;

   @Override
   public Set<l> getNetworkImpacts() {
      return Set.of(l.k);
   }

   public SpeedModule() {
      super("Speed", "Move faster", Category.MOVEMENT, -1);
      a = this;
      this.q.a(() -> this.j.w("Entity Collision"));
      this.k.a(() -> this.j.w("Entity Collision"));
      this.P.a(() -> !this.j.w("Entity Collision"));
      this.I.a(() -> this.j.w("BHop") || this.j.w("Strafe") || this.j.w("LowHop"));
      this.f.a(() -> !this.j.w("Entity Collision"));
      this.o.a(() -> !this.j.w("Entity Collision"));
      this.w.a(() -> this.j.w("Strafe"));
      this.d.a(() -> !this.j.w("Entity Collision"));
   }

   @Override
   public void onEnable() {
      this.onDisable();
      this.x = this.j.getValue();
   }

   @Override
   public void onDisable() {
      this.b();
      this.x = "";
      this.H = 0;
      this.b = 0;
   }

   private void b() {
      this.G = 0.0;
      this.e = "";
      this.R = 0;
      this.u = (boolean)0;
      this.n = (boolean)0;
   }

   public static void K(LocalPlayer var0) {
      if (a != null && a.isRuntimeActive() && var0 == getMc().player) {
         a.H = 20;
         a.b = 0;
      }
   }

   @Override
   public void onTick() {
      this.b = 0;
      int var1 = this.H > 0 ? 1 : 0;
      if (var1 != 0) {
         byte var15 = 1;
         int var14 = this.H;
         this.H = var14 - var15;
      }

      if (this.isInGame()) {
         if (!this.x.equals(this.j.getValue())) {
            this.b();
            this.x = this.j.getValue();
         }

         if (nz.v() && !TargetStrafeModule.v() && mc.gui.screen() == null && !mc.player.isInWater() && !mc.player.isInLava() && !mc.player.onClimbable()) {
            Vec3 var2 = nz.b();
            boolean var3 = mc.player.onGround();
            if (this.j.w("Entity Collision")) {
               if (var1 == 0) {
                  this.z(var2);
               }
            } else if (var2.lengthSqr() != 0.0 && (!this.f.getValue() || var3)) {
               if (!this.e.equals(this.j.getValue())) {
                  this.G = 0.0;
                  this.e = this.j.getValue();
               }

               int var4 = mc.player.hurtTime > 0 ? 1 : 0;
               if (this.o.getValue() && var4 != 0 && !this.u) {
                  this.R = 10;
               }

               int var25;
               label120: {
                  this.u = (boolean)var4;
                  if (this.o.getValue()) {
                     var25 = this.R;
                     byte var17 = 1;
                     int var16 = this.R;
                     this.R = var16 - var17;
                     boolean var19 = false;
                     int var18 = var25;
                     if ((-Integer.signum(var18) >>> 31 ^ 1) == 0) {
                        var25 = 1;
                        break label120;
                     }
                  }

                  var25 = 0;
               }

               this.n = (boolean)var25;
               if (this.R < 0) {
                  this.R = 0;
               }

               if (!this.j.w("Vanilla") || var3) {
                  double var5 = switch ((String)this.j.getValue()) {
                     case "BHop" -> 0.26;
                     case "Strafe" -> 0.28;
                     case "LowHop" -> 0.22;
                     default -> 0.13;
                  };
                  double var24 = var5 * this.P.getValue();
                  boolean var21 = false;
                  byte var20 = this.n;
                  var25 = Integer.signum(var20);
                  double var22 = var24 * (((var25 | -var25) >>> 31 ^ 1) == 0 ? 1.15 : 1.0);
                  this.G = this.d.getValue() ? this.G + (var22 - this.G) * 0.3 : var22;
                  Vec3 var9 = mc.player.getDeltaMovement();
                  Vec3 var10 = var2.scale(this.G);
                  Vec3 var11 = new Vec3(var9.x, 0.0, var9.z);
                  double var12 = (this.j.w("Strafe") && !var3 ? 0.04 * this.w.getValue() : 0.08) * y(this.P.getValue());
                  var11 = nz.g(var11, var10, var12);
                  mc.player.setDeltaMovement(var11.x, var9.y, var11.z);
                  if (var3 && this.I.getValue() && !this.j.w("Vanilla") && var9.y <= 0.0) {
                     if (this.j.w("LowHop")) {
                        mc.player.setDeltaMovement(var11.x, 0.32, var11.z);
                     } else {
                        mc.player.jumpFromGround();
                     }
                  }
               }
            } else {
               this.G = 0.0;
            }
         } else {
            this.b();
         }
      }
   }

   public static double y(double var0) {
      return Math.max(1.0, var0 - 4.0);
   }

   private void z(Vec3 var1) {
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
            if (EntitySelector.pushableBy(var5).test(var2) && ++this.b >= this.k.getValue().intValue()) {
               break;
            }
         }

         if (this.b != 0) {
            double var7 = Math.min(0.08, this.q.getValue() * this.b);
            Vec3 var6 = var2.getDeltaMovement();
            var2.setDeltaMovement(var6.x + var1.x * var7, var6.y, var6.z + var1.z * var7);
         }
      }
   }

   @Override
   public String getStatus() {
      if (!this.j.w("Entity Collision")) {
         return "";
      } else if (!this.isRuntimeActive()) {
         return "Inactive";
      } else if (this.H > 0) {
         return "Collision boost recovering";
      } else {
         return this.b == 0 ? "Waiting for collision" : "Collision boost active";
      }
   }
}
