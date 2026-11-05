package com.larp.obf.q5682c6a8aca9b6ec;

import com.larpclient.f6;
import com.larpclient.rr;
import com.larpclient.module.AuraModule;
import com.larpclient.module.AutoAnchorModule;
import com.larpclient.module.AutoCrystalModule;
import com.larpclient.module.GrimSpeedModule;
import com.larpclient.module.JumpFXModule;
import com.larpclient.module.ScaffoldModule;
import com.larpclient.module.TargetStrafeModule;
import com.larpclient.module.k;
import com.larpclient.module.t;
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
public class qc4cbbd3f555d7d87 {
   @ModifyArg(
      method = "handleRelativeFrictionAndCalculateMovement(Lnet/minecraft/world/phys/Vec3;F)Lnet/minecraft/world/phys/Vec3;",
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/world/entity/LivingEntity;move(Lnet/minecraft/world/entity/MoverType;Lnet/minecraft/world/phys/Vec3;)V"
      ),
      index = 1
   )
   private Vec3 n(Vec3 var1) {
      return this instanceof LocalPlayer var2 ? TargetStrafeModule.Q(var2, var1) : var1;
   }

   @Inject(method = "aiStep()V", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;pushEntities()V"))
   private void Z(CallbackInfo var1) {
      if (this instanceof LocalPlayer var2) {
         GrimSpeedModule.s(var2);
      }
   }

   @ModifyVariable(method = "tickHeadTurn(F)V", at = @At("HEAD"), argsOnly = true)
   private float j(float var1) {
      if (this != Minecraft.getInstance().player) {
         return var1;
      }

      ScaffoldModule var2 = ScaffoldModule.v();
      if (var2 != null) {
         var1 = var2.O(var1);
      }

      t var3 = t.d();
      if (var3 != null) {
         var1 = var3.b(var1);
      }

      k var4 = k.v();
      if (var4 != null) {
         var1 = var4.A(var1);
      }

      AutoAnchorModule var5 = AutoAnchorModule.y();
      if (var5 != null) {
         var1 = var5.Y(var1);
      }

      AutoCrystalModule var6 = AutoCrystalModule.T();
      return var6 == null ? var1 : var6.C(var1);
   }

   @Inject(method = "jumpFromGround()V", at = @At("HEAD"))
   private void A(CallbackInfo var1) {
      if (this == Minecraft.getInstance().player) {
         AuraModule.J((LocalPlayer)this, "jump-before");
         JumpFXModule var2 = JumpFXModule.m();
         if (var2 != null) {
            var2.v();
         }
      }
   }

   @Inject(method = "jumpFromGround()V", at = @At("RETURN"))
   private void k(CallbackInfo var1) {
      if (this == Minecraft.getInstance().player) {
         AuraModule.J((LocalPlayer)this, "jump-after");
      }
   }

   @Redirect(method = "jumpFromGround()V", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;getYRot()F"))
   private float D(LivingEntity var1) {
      return var1 == Minecraft.getInstance().player && rr.u() == rr.f.x ? rr.h() : var1.getYRot();
   }

   @ModifyArgs(
      method = "knockback(DDDLnet/minecraft/world/damagesource/DamageSource;FZ)V",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;setDeltaMovement(DDD)V")
   )
   private void c(Args var1, double var2, double var4, double var6, DamageSource var8, float var9, boolean var10) {
      if (this == Minecraft.getInstance().player) {
         Vec3 var11 = ((LivingEntity)this).getDeltaMovement();
         Vec3 var12 = new Vec3((Double)var1.get(0), (Double)var1.get(1), (Double)var1.get(2));
         Vec3 var13 = var11.add(f6.z(var12.subtract(var11), var8 != null && var8.is(DamageTypeTags.IS_EXPLOSION)));
         var1.set(0, var13.x);
         var1.set(1, var13.y);
         var1.set(2, var13.z);
      }
   }
}
