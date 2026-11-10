package com.larpclient.module;

import com.larpclient.nz;
import java.util.Set;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.player.Abilities;
import net.minecraft.world.phys.Vec3;

public class FlyModule extends Module {
   private final b u = this.addSetting(new b("Speed", "Flight speed", 2.0, 0.5, 10.0, 0.1));
   private final o a = this.addSetting(new o("Mode", "Vanilla flight or gradual glide descent", "Vanilla", "Vanilla", "Glide"));
   private final b J = this.addSetting(new b("Glide Speed", "Target descent speed", 0.02, 0.0, 0.1, 0.005));
   private static FlyModule X;
   private LocalPlayer t;
   private boolean I;
   private boolean Y;
   private boolean W;
   private float H;
   private float y;

   @Override
   public Set<l> getNetworkImpacts() {
      return Set.of(l.k);
   }

   public FlyModule() {
      super("Fly", "Fly or glide slowly", Category.MOVEMENT, -1);
      X = this;
      this.J.a(() -> this.a.w("Glide"));
   }

   public static FlyModule Q() {
      return X;
   }

   public boolean r() {
      return !this.isEnabled() ? false : this.isRuntimeActive() && getMc().player != null && getMc().player.isAlive();
   }

   @Override
   public String getStatus() {
      if (!this.isEnabled()) {
         return "Vanilla flight or gradual glide descent";
      } else {
         return this.r() ? "Flight active" : "Waiting for player";
      }
   }

   @Override
   public void onTick() {
      if (this.isInGame() && this.r() && nz.v()) {
         if (this.a.w("Vanilla")) {
            Abilities var1 = mc.player.getAbilities();
            if (this.t != mc.player) {
               this.i();
               this.t = mc.player;
               this.W = var1.mayfly;
               this.I = var1.flying;
               this.H = var1.flyingSpeed;
            }

            byte var7 = this.Y;
            byte var6 = var1.flying;
            int var10000 = Integer.compare(var6, var7);
            if (((var10000 | -var10000) >>> 31 ^ 1) == 0) {
               this.I = var1.flying;
            }

            if (var1.flyingSpeed != this.y) {
               this.H = var1.flyingSpeed;
            }

            boolean var9 = false;
            byte var8 = var1.mayfly;
            var10000 = Integer.signum(var8);
            if ((var10000 | -var10000) >>> 31 == 0) {
               this.W = (boolean)0;
            }

            boolean var11 = false;
            byte var10 = var1.flying;
            var10000 = Integer.signum(var10);
            int var2 = (var10000 | -var10000) >>> 31 == 0 ? 1 : 0;
            var1.mayfly = (boolean)1;
            var1.flying = this.Y = (boolean)1;
            var1.flyingSpeed = this.y = this.u.getValue().floatValue() * 0.05F;
            boolean var5 = false;
            int var4 = var2;
            var10000 = Integer.signum(var4);
            if (((var10000 | -var10000) >>> 31 ^ 1) == 0) {
               mc.player.onUpdateAbilities();
            }
         } else {
            this.i();
            Vec3 var12 = mc.player.getDeltaMovement();
            if (!mc.player.onGround() && var12.y < 0.0 && !mc.player.isInWater() && !mc.player.isInLava()) {
               Vec3 var13 = nz.b().scale(this.u.getValue() * 0.15);
               Vec3 var3 = nz.g(new Vec3(var12.x, 0.0, var12.z), var13, 0.04);
               mc.player.setDeltaMovement(var3.x, Math.min(0.0, var12.y + Math.min(0.08, -this.J.getValue() - var12.y)), var3.z);
            }
         }
      } else {
         this.i();
      }
   }

   private void i() {
      if (this.t != null) {
         Abilities var1 = this.t.getAbilities();
         boolean var2 = var1.flying;
         if (var1.mayfly) {
            var1.mayfly = this.W;
         }

         if (var1.flying == this.Y) {
            var1.flying = (boolean)(var1.mayfly && this.I ? 1 : 0);
         }

         if (var1.flyingSpeed == this.y) {
            var1.flyingSpeed = this.H;
         }

         if (var2 != var1.flying && this.t == getMc().player && this.t.isAlive()) {
            this.t.onUpdateAbilities();
         }

         this.t = null;
      }
   }

   public void q() {
      this.t = null;
   }

   @Override
   public void onDisable() {
      this.i();
   }
}
