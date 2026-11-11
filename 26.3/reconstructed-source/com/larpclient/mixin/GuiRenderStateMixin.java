package com.larpclient.mixin;

import com.larpclient.render.overlay.ScreenWarp;
import com.larpclient.util.misc.LarpLog;
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
public abstract class GuiRenderStateMixin implements ScreenWarp.WarpRenderState {
   @Unique
   private static final int E = 4;
   @Shadow
   @Final
   private List<?> strata;
   @Unique
   private final int[] t = new int[4];
   @Unique
   private final int[] j = new int[4];
   @Unique
   private final ScreenWarp.Warp[] s = new ScreenWarp.Warp[4];
   @Unique
   private final boolean[] T = new boolean[4];
   @Unique
   private int l;
   @Unique
   private int Z;
   @Unique
   private static boolean v;

   @Override
   public void larpWarpBegin(ScreenWarp.Warp var1, boolean var2) {
      if (this.l == 4) {
         this.Z++;
         if (!v) {
            v = true;
            LarpLog.warn("GUI", "More than {} projected GUI ranges in one frame; extra content stays flat", 4);
         }
      } else {
         int var3 = this.l++;
         this.t[var3] = this.strata.size();
         this.j[var3] = Integer.MAX_VALUE;
         this.s[var3] = var1;
         this.T[var3] = var2;
      }
   }

   @Override
   public void larpWarpEnd() {
      if (this.Z > 0) {
         this.Z--;
      } else {
         if (this.l > 0) {
            this.j[this.l - 1] = this.strata.size();
         }
      }
   }

   @Inject(method = "reset", at = @At("HEAD"))
   private void n(CallbackInfo var1) {
      Arrays.fill(this.s, null);
      this.l = this.Z = 0;
   }

   @WrapOperation(
      method = "traverse(Ljava/util/function/Consumer;Lnet/minecraft/client/renderer/state/gui/GuiRenderState$TraverseRange;)V",
      at = @At(value = "INVOKE", target = "Ljava/util/List;get(I)Ljava/lang/Object;")
   )
   private Object n(List<?> var1, int var2, Operation<Object> var3) {
      int var4 = -1;

      for (int var5 = 0; var5 < this.l; var5++) {
         if (var2 >= this.t[var5] && var2 < this.j[var5]) {
            var4 = var5;
            break;
         }
      }

      if (var4 < 0) {
         ScreenWarp.warpGraphics(-1, null, false);
      } else {
         ScreenWarp.warpGraphics(var2 - this.t[var4], this.s[var4], this.T[var4]);
      }

      return var3.call(new Object[]{var1, var2});
   }

   @Inject(method = "traverse(Ljava/util/function/Consumer;Lnet/minecraft/client/renderer/state/gui/GuiRenderState$TraverseRange;)V", at = @At("RETURN"))
   private void R(CallbackInfo var1) {
      ScreenWarp.warpGraphics(-1, null, false);
   }
}
