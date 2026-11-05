package com.larpclient.module;

import com.larpclient.te;
import java.util.Set;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.player.Abilities;
import net.minecraft.world.phys.Vec3;

public class FlyModule extends Module {
   private final d9 L = this.addSetting(new d9("Speed", "Flight speed", 2.0, 0.5, 10.0, 0.1));
   private final d6 E = this.addSetting(new d6("Mode", "Vanilla flight or gradual glide descent", "Vanilla", "Vanilla", "Glide"));
   private final d9 g = this.addSetting(new d9("Glide Speed", "Target descent speed", 0.02, 0.0, 0.1, 0.005));
   private static FlyModule V;
   private LocalPlayer m;
   private boolean f;
   private boolean J;
   private boolean P;
   private float K;
   private float O;

   @Override
   public Set<dm> getNetworkImpacts() {
      return Set.of(dm.j);
   }

   public FlyModule() {
      super("Fly", "Fly or glide slowly", Category.MOVEMENT, -1);
      V = this;
      this.g.H(() -> this.E.S("Glide"));
   }

   public static FlyModule d() {
      return V;
   }

   public boolean s() {
      return !this.isEnabled() ? false : this.isRuntimeActive() && getMc().player != null && getMc().player.isAlive();
   }

   @Override
   public String getStatus() {
      if (!this.isEnabled()) {
         return "Vanilla flight or gradual glide descent";
      } else {
         return this.s() ? "Flight active" : "Waiting for player";
      }
   }

   @Override
   public void onTick() {
      if (this.isInGame() && this.s() && te.i()) {
         if (this.E.S("Vanilla")) {
            Abilities var1 = mc.player.getAbilities();
            if (this.m != mc.player) {
               this.R();
               this.m = mc.player;
               this.P = var1.mayfly;
               this.f = var1.flying;
               this.K = var1.flyingSpeed;
            }

            byte var5 = this.J;
            byte var4 = var1.flying;
            int var10000 = Integer.compare(var4, var5);
            if (((var10000 | -var10000) >>> 31 ^ 1) == 0) {
               this.f = var1.flying;
            }

            float var18;
            var10000 = (var18 = var1.flyingSpeed - this.O) == 0.0F ? 0 : (var18 < 0.0F ? -1 : 1);
            boolean var7 = false;
            int var6 = var10000;
            var10000 = Integer.signum(var6);
            if (((var10000 | -var10000) >>> 31 ^ 1) == 0) {
               this.K = var1.flyingSpeed;
            }

            boolean var9 = false;
            byte var8 = var1.mayfly;
            var10000 = Integer.signum(var8);
            if ((var10000 | -var10000) >>> 31 == 0) {
               this.P = (boolean)0;
            }

            boolean var11 = false;
            byte var10 = var1.flying;
            var10000 = Integer.signum(var10);
            int var2 = (var10000 | -var10000) >>> 31 == 0 ? 1 : 0;
            var1.mayfly = (boolean)1;
            var1.flying = this.J = (boolean)1;
            var1.flyingSpeed = this.O = this.L.P().floatValue() * 0.05F;
            if (var2 != 0) {
               mc.player.onUpdateAbilities();
            }
         } else {
            this.R();
            Vec3 var12 = mc.player.getDeltaMovement();
            if (!mc.player.onGround() && var12.y < 0.0 && !mc.player.isInWater() && !mc.player.isInLava()) {
               Vec3 var13 = te.S().scale(this.L.P() * 0.15);
               Vec3 var3 = te.j(new Vec3(var12.x, 0.0, var12.z), var13, 0.04);
               mc.player.setDeltaMovement(var3.x, Math.min(0.0, var12.y + Math.min(0.08, -this.g.P() - var12.y)), var3.z);
            }
         }
      } else {
         this.R();
      }
   }

   private void R() {
      if (this.m != null) {
         Abilities var1 = this.m.getAbilities();
         boolean var2 = var1.flying;
         if (var1.mayfly) {
            var1.mayfly = this.P;
         }

         if (var1.flying == this.J) {
            var1.flying = (boolean)(var1.mayfly && this.f ? 1 : 0);
         }

         if (var1.flyingSpeed == this.O) {
            var1.flyingSpeed = this.K;
         }

         if (var2 != var1.flying && this.m == getMc().player && this.m.isAlive()) {
            this.m.onUpdateAbilities();
         }

         this.m = null;
      }
   }

   public void i() {
      this.m = null;
   }

   @Override
   public void onDisable() {
      this.R();
   }
}
