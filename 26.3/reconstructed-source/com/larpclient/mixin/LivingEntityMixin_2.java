package com.larpclient.mixin;

import com.larpclient.core.ViewController;
import com.larpclient.module.combat.AuraModule;
import com.larpclient.module.combat.AutoAnchorModule;
import com.larpclient.module.combat.AutoCrystalModule;
import com.larpclient.module.combat.AutoTrap;
import com.larpclient.module.combat.AutoWebModule;
import com.larpclient.module.movement.GrimSpeedModule;
import com.larpclient.module.movement.TargetStrafeModule;
import com.larpclient.module.player.ScaffoldModule;
import com.larpclient.module.render.JumpFXModule;
import com.larpclient.util.math.KnockbackScaler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(LivingEntity.class)
public class LivingEntityMixin_2 {
   @ModifyArg(
      method = "handleRelativeFrictionAndCalculateMovement(Lnet/minecraft/world/phys/Vec3;F)Lnet/minecraft/world/phys/Vec3;",
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/world/entity/LivingEntity;move(Lnet/minecraft/world/entity/MoverType;Lnet/minecraft/world/phys/Vec3;)V"
      ),
      index = 1
   )
   private Vec3 F(Vec3 var1) {
      return this instanceof LocalPlayer var2 ? TargetStrafeModule.modifyMovement(var2, var1) : var1;
   }

   @Inject(method = "aiStep()V", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;pushEntities()V"))
   private void w(CallbackInfo var1) {
      if (this instanceof LocalPlayer var2) {
         GrimSpeedModule.onCollisionPhase(var2);
      }
   }

   @ModifyVariable(method = "tickHeadTurn(F)V", at = @At("HEAD"), argsOnly = true)
   private float W(float var1) {
      if (this != Minecraft.getInstance().player) {
         return var1;
      }

      ScaffoldModule var2 = ScaffoldModule.getInstance();
      if (var2 != null) {
         var1 = var2.getBodyYaw(var1);
      }

      AutoTrap var3 = AutoTrap.getInstance();
      if (var3 != null) {
         var1 = var3.getAimYaw(var1);
      }

      AutoWebModule var4 = AutoWebModule.getInstance();
      if (var4 != null) {
         var1 = var4.getHeadYaw(var1);
      }

      AutoAnchorModule var5 = AutoAnchorModule.getInstance();
      if (var5 != null) {
         var1 = var5.getAnchorYaw(var1);
      }

      AutoCrystalModule var6 = AutoCrystalModule.placeCrystal();
      return var6 == null ? var1 : var6.isActionValid(var1);
   }

   @Inject(method = "jumpFromGround()V", at = @At("HEAD"))
   private void t(CallbackInfo var1) {
      if (this == Minecraft.getInstance().player) {
         AuraModule.getCombatSettings((LocalPlayer)this, "jump-before");
         JumpFXModule var2 = JumpFXModule.getInstance();
         if (var2 != null) {
            var2.onPreTick();
         }
      }
   }

   @Inject(method = "jumpFromGround()V", at = @At("RETURN"))
   private void j(CallbackInfo var1) {
      if (this == Minecraft.getInstance().player) {
         AuraModule.getCombatSettings((LocalPlayer)this, "jump-after");
      }
   }

   @Redirect(method = "jumpFromGround()V", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;getYRot()F"))
   private float m(LivingEntity var1) {
      if (var1 != Minecraft.getInstance().player) {
         return var1.getYRot();
      } else {
         float var2 = ScaffoldModule.getWalkYaw();
         if (!Float.isNaN(var2)) {
            return var2;
         } else {
            return ViewController.update() == ViewController.Owner.SCAFFOLD ? ViewController.getPlayerYaw() : var1.getYRot();
         }
      }
   }

   @ModifyArgs(
      method = "knockback(DDDLnet/minecraft/world/damagesource/DamageSource;FZ)V",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;setDeltaMovement(DDD)V")
   )
   private void Y(Args var1, double var2, double var4, double var6, DamageSource var8, float var9, boolean var10) {
      if (this == Minecraft.getInstance().player) {
         Vec3 var11 = ((LivingEntity)this).getDeltaMovement();
         Vec3 var12 = new Vec3((Double)var1.get(0), (Double)var1.get(1), (Double)var1.get(2));
         Vec3 var13 = var11.add(KnockbackScaler.scaleVelocity(var12.subtract(var11), var8 != null && var8.is(DamageTypeTags.IS_EXPLOSION)));
         var1.set(0, var13.x);
         var1.set(1, var13.y);
         var1.set(2, var13.z);
      }
   }
}
