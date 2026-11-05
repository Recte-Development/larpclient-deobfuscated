package com.larp.obf.qbac0b262827a15ac;

import com.larpclient.tq;
import com.larpclient.x;
import com.larpclient.module.AuraModule;
import com.larpclient.module.AutoAnchorModule;
import com.larpclient.module.AutoCrystalModule;
import com.larpclient.module.GrimSpeedModule;
import com.larpclient.module.JumpFXModule;
import com.larpclient.module.ScaffoldModule;
import com.larpclient.module.TargetStrafeModule;
import com.larpclient.module.f;
import com.larpclient.module.w;
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
public class qb504c35e7a5ca9ed {
   @ModifyArg(
      method = "handleRelativeFrictionAndCalculateMovement(Lnet/minecraft/world/phys/Vec3;F)Lnet/minecraft/world/phys/Vec3;",
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/world/entity/LivingEntity;move(Lnet/minecraft/world/entity/MoverType;Lnet/minecraft/world/phys/Vec3;)V"
      ),
      index = 1
   )
   private Vec3 K(Vec3 var1) {
      return this instanceof LocalPlayer var2 ? TargetStrafeModule.N(var2, var1) : var1;
   }

   @Inject(method = "aiStep()V", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;pushEntities()V"))
   private void d(CallbackInfo var1) {
      if (this instanceof LocalPlayer var2) {
         GrimSpeedModule.v(var2);
      }
   }

   @ModifyVariable(method = "tickHeadTurn(F)V", at = @At("HEAD"), argsOnly = true)
   private float r(float var1) {
      if (this != Minecraft.getInstance().player) {
         return var1;
      }

      ScaffoldModule var2 = ScaffoldModule.i();
      if (var2 != null) {
         var1 = var2.P(var1);
      }

      f var3 = f.X();
      if (var3 != null) {
         var1 = var3.F(var1);
      }

      w var4 = w.Y();
      if (var4 != null) {
         var1 = var4.Z(var1);
      }

      AutoAnchorModule var5 = AutoAnchorModule.S();
      if (var5 != null) {
         var1 = var5.X(var1);
      }

      AutoCrystalModule var6 = AutoCrystalModule.S();
      return var6 == null ? var1 : var6.L(var1);
   }

   @Inject(method = "jumpFromGround()V", at = @At("HEAD"))
   private void L(CallbackInfo var1) {
      if (this == Minecraft.getInstance().player) {
         AuraModule.k((LocalPlayer)this, "jump-before");
         JumpFXModule var2 = JumpFXModule.m();
         if (var2 != null) {
            var2.a();
         }
      }
   }

   @Inject(method = "jumpFromGround()V", at = @At("RETURN"))
   private void c(CallbackInfo var1) {
      if (this == Minecraft.getInstance().player) {
         AuraModule.k((LocalPlayer)this, "jump-after");
      }
   }

   @Redirect(method = "jumpFromGround()V", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;getYRot()F"))
   private float U(LivingEntity var1) {
      return var1 == Minecraft.getInstance().player && tq.l() == tq.y.Y ? tq.z() : var1.getYRot();
   }

   @ModifyArgs(
      method = "knockback(DDDLnet/minecraft/world/damagesource/DamageSource;FZ)V",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;setDeltaMovement(DDD)V")
   )
   private void q(Args var1, double var2, double var4, double var6, DamageSource var8, float var9, boolean var10) {
      if (this == Minecraft.getInstance().player) {
         Vec3 var11 = ((LivingEntity)this).getDeltaMovement();
         Vec3 var12 = new Vec3((Double)var1.get(0), (Double)var1.get(1), (Double)var1.get(2));
         Vec3 var13 = var11.add(x.l(var12.subtract(var11), var8 != null && var8.is(DamageTypeTags.IS_EXPLOSION)));
         var1.set(0, var13.x);
         var1.set(1, var13.y);
         var1.set(2, var13.z);
      }
   }
}
