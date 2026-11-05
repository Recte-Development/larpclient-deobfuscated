package com.larp.obf.qbac0b262827a15ac;

import com.larpclient.cp;
import com.larpclient.mt;
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
public abstract class q2e55c900e28b12b7 implements cp.b {
   @Unique
   private static final int G = 4;
   @Shadow
   @Final
   private List<?> strata;
   @Unique
   private final int[] n = new int[4];
   @Unique
   private final int[] g = new int[4];
   @Unique
   private final cp.l[] l = new cp.l[4];
   @Unique
   private final boolean[] u = new boolean[4];
   @Unique
   private int I;
   @Unique
   private int Y;
   @Unique
   private static boolean j;

   @Override
   public void larpWarpBegin(cp.l var1, boolean var2) {
      if (this.I == 4) {
         this.Y++;
         if (!j) {
            j = true;
            mt.T("GUI", "More than {} projected GUI ranges in one frame; extra content stays flat", 4);
         }
      } else {
         int var3 = this.I++;
         this.n[var3] = this.strata.size();
         this.g[var3] = Integer.MAX_VALUE;
         this.l[var3] = var1;
         this.u[var3] = var2;
      }
   }

   @Override
   public void larpWarpEnd() {
      if (this.Y > 0) {
         this.Y--;
      } else {
         if (this.I > 0) {
            this.g[this.I - 1] = this.strata.size();
         }
      }
   }

   @Inject(method = "reset", at = @At("HEAD"))
   private void w(CallbackInfo var1) {
      Arrays.fill(this.l, null);
      this.I = this.Y = 0;
   }

   @WrapOperation(
      method = "traverse(Ljava/util/function/Consumer;Lnet/minecraft/client/renderer/state/gui/GuiRenderState$TraverseRange;)V",
      at = @At(value = "INVOKE", target = "Ljava/util/List;get(I)Ljava/lang/Object;")
   )
   private Object V(List<?> var1, int var2, Operation<Object> var3) {
      int var4 = -1;

      for (int var5 = 0; var5 < this.I; var5++) {
         if (var2 >= this.n[var5] && var2 < this.g[var5]) {
            var4 = var5;
            break;
         }
      }

      if (var4 < 0) {
         cp.U(-1, null, false);
      } else {
         cp.U(var2 - this.n[var4], this.l[var4], this.u[var4]);
      }

      return var3.call(new Object[]{var1, var2});
   }

   @Inject(method = "traverse(Ljava/util/function/Consumer;Lnet/minecraft/client/renderer/state/gui/GuiRenderState$TraverseRange;)V", at = @At("RETURN"))
   private void J(CallbackInfo var1) {
      cp.U(-1, null, false);
   }
}
