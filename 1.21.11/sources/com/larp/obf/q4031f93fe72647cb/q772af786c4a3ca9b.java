package com.larp.obf.q4031f93fe72647cb;

import com.larpclient.lb;
import com.larpclient.q8;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import java.util.Arrays;
import java.util.List;
import net.minecraft.class_11246;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(class_11246.class)
public abstract class q772af786c4a3ca9b implements lb.o {
   @Unique
   private static final int F = 4;
   @Shadow
   @Final
   private List<?> field_60043;
   @Unique
   private final int[] g = new int[4];
   @Unique
   private final int[] x = new int[4];
   @Unique
   private final lb.m[] o = new lb.m[4];
   @Unique
   private final boolean[] X = new boolean[4];
   @Unique
   private int S;
   @Unique
   private int R;
   @Unique
   private static boolean E;

   @Override
   public void larpWarpBegin(lb.m var1, boolean var2) {
      if (this.S == 4) {
         this.R++;
         if (!E) {
            E = true;
            q8.L("GUI", "More than {} projected GUI ranges in one frame; extra content stays flat", 4);
         }
      } else {
         int var3 = this.S++;
         this.g[var3] = this.field_60043.size();
         this.x[var3] = Integer.MAX_VALUE;
         this.o[var3] = var1;
         this.X[var3] = var2;
      }
   }

   @Override
   public void larpWarpEnd() {
      if (this.R > 0) {
         this.R--;
      } else {
         if (this.S > 0) {
            this.x[this.S - 1] = this.field_60043.size();
         }
      }
   }

   @Inject(method = "method_70926", at = @At("HEAD"))
   private void Z(CallbackInfo var1) {
      Arrays.fill(this.o, null);
      this.S = this.R = 0;
   }

   @WrapOperation(
      method = "method_71074(Ljava/util/function/Consumer;Lnet/minecraft/class_11246$class_11325;)V",
      at = @At(value = "INVOKE", target = "Ljava/util/List;get(I)Ljava/lang/Object;")
   )
   private Object i(List<?> var1, int var2, Operation<Object> var3) {
      int var4 = -1;

      for (int var5 = 0; var5 < this.S; var5++) {
         if (var2 >= this.g[var5] && var2 < this.x[var5]) {
            var4 = var5;
            break;
         }
      }

      if (var4 < 0) {
         lb.r(-1, null, false);
      } else {
         lb.r(var2 - this.g[var4], this.o[var4], this.X[var4]);
      }

      return var3.call(new Object[]{var1, var2});
   }

   @Inject(method = "method_71074(Ljava/util/function/Consumer;Lnet/minecraft/class_11246$class_11325;)V", at = @At("RETURN"))
   private void s(CallbackInfo var1) {
      lb.r(-1, null, false);
   }
}
