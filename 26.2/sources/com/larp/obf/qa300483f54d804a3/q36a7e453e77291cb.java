package com.larp.obf.qa300483f54d804a3;

import com.larpclient.hz;
import com.larpclient.p_;
import com.larpclient.module.AuraModule;
import com.larpclient.module.AutoAnchorModule;
import com.larpclient.module.AutoCrystalModule;
import com.larpclient.module.GrimSpeedModule;
import com.larpclient.module.JumpFXModule;
import com.larpclient.module.ScaffoldModule;
import com.larpclient.module.TargetStrafeModule;
import com.larpclient.module.tb;
import com.larpclient.module.th;
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
public class q36a7e453e77291cb {
   @ModifyArg(
      method = "handleRelativeFrictionAndCalculateMovement(Lnet/minecraft/world/phys/Vec3;F)Lnet/minecraft/world/phys/Vec3;",
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/world/entity/LivingEntity;move(Lnet/minecraft/world/entity/MoverType;Lnet/minecraft/world/phys/Vec3;)V"
      ),
      index = 1
   )
   private Vec3 j(Vec3 var1) {
      return this instanceof LocalPlayer var2 ? TargetStrafeModule.u(var2, var1) : var1;
   }

   @Inject(method = "aiStep()V", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;pushEntities()V"))
   private void L(CallbackInfo var1) {
      if (this instanceof LocalPlayer var2) {
         GrimSpeedModule.s(var2);
      }
   }

   @ModifyVariable(method = "tickHeadTurn(F)V", at = @At("HEAD"), argsOnly = true)
   private float F(float var1) {
      if (this != Minecraft.getInstance().player) {
         return var1;
      }

      ScaffoldModule var2 = ScaffoldModule.l();
      if (var2 != null) {
         var1 = var2.s(var1);
      }

      tb var3 = tb.R();
      if (var3 != null) {
         var1 = var3.C(var1);
      }

      th var4 = th.B();
      if (var4 != null) {
         var1 = var4.J(var1);
      }

      AutoAnchorModule var5 = AutoAnchorModule.y();
      if (var5 != null) {
         var1 = var5.d(var1);
      }

      AutoCrystalModule var6 = AutoCrystalModule.S();
      return var6 == null ? var1 : var6.f(var1);
   }

   @Inject(method = "jumpFromGround()V", at = @At("HEAD"))
   private void l(CallbackInfo var1) {
      if (this == Minecraft.getInstance().player) {
         AuraModule.M((LocalPlayer)this, "jump-before");
         JumpFXModule var2 = JumpFXModule.f();
         if (var2 != null) {
            var2.s();
         }
      }
   }

   @Inject(method = "jumpFromGround()V", at = @At("RETURN"))
   private void H(CallbackInfo var1) {
      if (this == Minecraft.getInstance().player) {
         AuraModule.M((LocalPlayer)this, "jump-after");
      }
   }

   @Redirect(method = "jumpFromGround()V", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;getYRot()F"))
   private float F(LivingEntity var1) {
      if (var1 != Minecraft.getInstance().player) {
         return var1.getYRot();
      } else {
         float var2 = ScaffoldModule.X();
         if (!Float.isNaN(var2)) {
            return var2;
         } else {
            return p_.A() == p_.o.k ? p_.z() : var1.getYRot();
         }
      }
   }

   @ModifyArgs(
      method = "knockback(DDDLnet/minecraft/world/damagesource/DamageSource;FZ)V",
      at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;setDeltaMovement(DDD)V")
   )
   private void J(Args var1, double var2, double var4, double var6, DamageSource var8, float var9, boolean var10) {
      if (this == Minecraft.getInstance().player) {
         Vec3 var11 = ((LivingEntity)this).getDeltaMovement();
         Vec3 var12 = new Vec3((Double)var1.get(0), (Double)var1.get(1), (Double)var1.get(2));
         Vec3 var13 = var11.add(hz.i(var12.subtract(var11), var8 != null && var8.is(DamageTypeTags.IS_EXPLOSION)));
         var1.set(0, var13.x);
         var1.set(1, var13.y);
         var1.set(2, var13.z);
      }
   }
}
