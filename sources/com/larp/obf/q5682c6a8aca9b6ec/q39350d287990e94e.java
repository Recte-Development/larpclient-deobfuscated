package com.larp.obf.q5682c6a8aca9b6ec;

import com.larpclient.r4;
import com.larpclient.rk;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import java.util.Arrays;
import java.util.List;
import net.minecraft.client.renderer.state.gui.GuiRenderState;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiRenderState.class)
public abstract class q39350d287990e94e implements rk.l {
   @Unique
   private static final int j = 4;
   @Shadow
   @Final
   private List<?> strata;
   @Unique
   private final int[] g = new int[4];
   @Unique
   private final int[] y = new int[4];
   @Unique
   private final rk.f[] B = new rk.f[4];
   @Unique
   private final boolean[] T = new boolean[4];
   @Unique
   private int J;
   @Unique
   private int D;
   @Unique
   private static boolean l;

   @Override
   public void larpWarpBegin(rk.f var1, boolean var2) {
      if (this.J == 4) {
         this.D++;
         if (!l) {
            l = true;
            r4.m("GUI", "More than {} projected GUI ranges in one frame; extra content stays flat", 4);
         }
      } else {
         int var3 = this.J++;
         this.g[var3] = this.strata.size();
         this.y[var3] = Integer.MAX_VALUE;
         this.B[var3] = var1;
         this.T[var3] = var2;
      }
   }

   @Override
   public void larpWarpEnd() {
      if (this.D > 0) {
         this.D--;
      } else {
         if (this.J > 0) {
            this.y[this.J - 1] = this.strata.size();
         }
      }
   }

   @Inject(method = "reset", at = @At("HEAD"))
   private void a(CallbackInfo var1) {
      Arrays.fill(this.B, null);
      this.J = this.D = 0;
   }

   @WrapOperation(
      method = "traverse(Ljava/util/function/Consumer;Lnet/minecraft/client/renderer/state/gui/GuiRenderState$TraverseRange;)V",
      at = @At(value = "INVOKE", target = "Ljava/util/List;get(I)Ljava/lang/Object;")
   )
   private Object t(List<?> var1, int var2, Operation<Object> var3) {
      int var4 = -1;

      for (int var5 = 0; var5 < this.J; var5++) {
         if (var2 >= this.g[var5] && var2 < this.y[var5]) {
            var4 = var5;
            break;
         }
      }

      if (var4 < 0) {
         rk.S(-1, null, false);
      } else {
         rk.S(var2 - this.g[var4], this.B[var4], this.T[var4]);
      }

      return var3.call(new Object[]{var1, var2});
   }

   @Inject(method = "traverse(Ljava/util/function/Consumer;Lnet/minecraft/client/renderer/state/gui/GuiRenderState$TraverseRange;)V", at = @At("RETURN"))
   private void m(CallbackInfo var1) {
      rk.S(-1, null, false);
   }
}
