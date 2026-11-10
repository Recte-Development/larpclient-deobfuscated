package com.larpclient.module;

import com.larpclient.c5;
import java.util.Set;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.player.Abilities;
import net.minecraft.world.phys.Vec3;

public class FlyModule extends Module {
   private final e S = this.addSetting(new e("Speed", "Flight speed", 2.0, 0.5, 10.0, 0.1));
   private final l F = this.addSetting(new l("Mode", "Vanilla flight or gradual glide descent", "Vanilla", "Vanilla", "Glide"));
   private final e h = this.addSetting(new e("Glide Speed", "Target descent speed", 0.02, 0.0, 0.1, 0.005));
   private static FlyModule k;
   private LocalPlayer g;
   private boolean c;
   private boolean w;
   private boolean X;
   private float q;
   private float b;

   @Override
   public Set<m> getNetworkImpacts() {
      return Set.of(m.U);
   }

   public FlyModule() {
      super("Fly", "Fly or glide slowly", Category.MOVEMENT, -1);
      k = this;
      this.h.P(() -> this.F.O("Glide"));
   }

   public static FlyModule S() {
      return k;
   }

   public boolean f() {
      return !this.isEnabled() ? false : this.isRuntimeActive() && getMc().player != null && getMc().player.isAlive();
   }

   @Override
   public String getStatus() {
      if (!this.isEnabled()) {
         return "Vanilla flight or gradual glide descent";
      } else {
         return this.f() ? "Flight active" : "Waiting for player";
      }
   }

   @Override
   public void onTick() {
      if (this.isInGame() && this.f() && c5.J()) {
         if (this.F.O("Vanilla")) {
            Abilities var1 = mc.player.getAbilities();
            if (this.g != mc.player) {
               this.P();
               this.g = mc.player;
               this.X = var1.mayfly;
               this.c = var1.flying;
               this.q = var1.flyingSpeed;
            }

            byte var7 = this.w;
            byte var6 = var1.flying;
            int var10000 = Integer.compare(var6, var7);
            if (((var10000 | -var10000) >>> 31 ^ 1) == 0) {
               this.c = var1.flying;
            }

            if (var1.flyingSpeed != this.b) {
               this.q = var1.flyingSpeed;
            }

            boolean var9 = false;
            byte var8 = var1.mayfly;
            var10000 = Integer.signum(var8);
            if ((var10000 | -var10000) >>> 31 == 0) {
               this.X = (boolean)0;
            }

            boolean var11 = false;
            byte var10 = var1.flying;
            var10000 = Integer.signum(var10);
            int var2 = (var10000 | -var10000) >>> 31 == 0 ? 1 : 0;
            var1.mayfly = (boolean)1;
            var1.flying = this.w = (boolean)1;
            var1.flyingSpeed = this.b = this.S.getValue().floatValue() * 0.05F;
            if (var2 != 0) {
               mc.player.onUpdateAbilities();
            }
         } else {
            this.P();
            Vec3 var12 = mc.player.getDeltaMovement();
            int var16 = mc.player.onGround();
            boolean var5 = false;
            byte var4 = (byte)var16;
            var16 = Integer.signum(var4);
            if ((var16 | -var16) >>> 31 == 0 && var12.y < 0.0 && !mc.player.isInWater() && !mc.player.isInLava()) {
               Vec3 var13 = c5.D().scale(this.S.getValue() * 0.15);
               Vec3 var3 = c5.S(new Vec3(var12.x, 0.0, var12.z), var13, 0.04);
               mc.player.setDeltaMovement(var3.x, Math.min(0.0, var12.y + Math.min(0.08, -this.h.getValue() - var12.y)), var3.z);
            }
         }
      } else {
         this.P();
      }
   }

   private void P() {
      if (this.g != null) {
         Abilities var1 = this.g.getAbilities();
         boolean var2 = var1.flying;
         if (var1.mayfly) {
            var1.mayfly = this.X;
         }

         if (var1.flying == this.w) {
            var1.flying = (boolean)(var1.mayfly && this.c ? 1 : 0);
         }

         if (var1.flyingSpeed == this.b) {
            var1.flyingSpeed = this.q;
         }

         if (var2 != var1.flying && this.g == getMc().player && this.g.isAlive()) {
            this.g.onUpdateAbilities();
         }

         this.g = null;
      }
   }

   public void x() {
      this.g = null;
   }

   @Override
   public void onDisable() {
      this.P();
   }
}
