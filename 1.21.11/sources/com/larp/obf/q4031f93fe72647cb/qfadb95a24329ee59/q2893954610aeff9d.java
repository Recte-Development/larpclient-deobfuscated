package com.larp.obf.q4031f93fe72647cb.qfadb95a24329ee59;

import com.larpclient.bk;
import com.larpclient.i1;
import com.larpclient.il;
import com.larpclient.l0;
import com.larpclient.q;
import com.larpclient.q7;
import com.larpclient.t3;
import net.minecraft.class_11659;
import net.minecraft.class_1799;
import net.minecraft.class_310;
import net.minecraft.class_4587;
import net.minecraft.class_746;
import net.minecraft.class_759;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(class_759.class)
public abstract class q2893954610aeff9d implements l0 {
   @Shadow
   private class_1799 field_4047;
   @Unique
   private final i1<class_1799> v = new i1<>();

   @Override
   public void larpRig$captureHeldMap(q7 var1, class_1799 var2, int var3) {
      if (var1.Q() && var1.isDealtHandLocked()) {
         this.v.B(var1, var1.getDealtHandSequence(), var3, var2.method_7972());
      }
   }

   @Override
   public void larpRig$releaseHeldMap(q7 var1, class_1799 var2) {
      if (!var1.Q() || !var1.isDealtHandLocked() || !var1.isDealtHandSettled()) {
         this.v.n();
      }
   }

   @Inject(method = "method_22976", at = @At("HEAD"))
   private void N(float var1, class_4587 var2, class_11659 var3, class_746 var4, int var5, CallbackInfo var6) {
      class_310 var7 = class_310.method_1551();
      if (var7.field_1724 != null && var7.field_1687 != null) {
         q7 var8 = q7.L();
         if (var8.Q()) {
            var8.getDealtHandVisual(var7.field_1724.method_6047());
            this.v.n(var8, var8.getDealtHandSequence(), var7.field_1724.method_31548().method_67532(), var8.isDealtHandLocked());
            this.v.a(this.field_4047);
         }
      }
   }

   @ModifyVariable(method = "method_3233", at = @At("HEAD"), argsOnly = true)
   private class_1799 G(class_1799 var1) {
      return this.x(var1);
   }

   @ModifyVariable(method = "method_3223", at = @At("HEAD"), argsOnly = true)
   private class_1799 W(class_1799 var1) {
      return this.x(var1);
   }

   @Unique
   private class_1799 x(class_1799 var1) {
      if (var1 == this.field_4047 && var1 != null && !var1.method_7960()) {
         class_310 var2 = class_310.method_1551();
         if (var2.field_1724 != null && var2.field_1687 != null) {
            q7 var3 = q7.L();
            class_1799 var4 = var2.field_1724.method_6047();
            class_1799 var5 = var1;
            if (var3.isDealtHandLocked()) {
               var5 = var3.getDealtHandVisual(var4);
            }

            int var6 = il.I();
            var5 = var3.projectSlot(var6, var5);
            class_1799 var7 = var3.getInventorySlotProjection(var6);
            if (var7 != null) {
               var5 = var7;
            }

            q var8 = q.U();
            if (var8 != null) {
               var5 = var8.g(var5);
            }

            var5 = t3.c().Z(var6, var5);
            return bk.W().c(var6, var5);
         } else {
            return var1;
         }
      } else {
         return var1;
      }
   }
}
