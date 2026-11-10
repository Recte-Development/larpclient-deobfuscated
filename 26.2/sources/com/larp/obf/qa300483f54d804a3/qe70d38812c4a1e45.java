package com.larp.obf.qa300483f54d804a3;

import com.larpclient.h;
import com.larpclient.m_;
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
public abstract class qe70d38812c4a1e45 implements m_.i {
   @Unique
   private static final int x = 4;
   @Shadow
   @Final
   private List<?> strata;
   @Unique
   private final int[] L = new int[4];
   @Unique
   private final int[] u = new int[4];
   @Unique
   private final m_.g[] i = new m_.g[4];
   @Unique
   private final boolean[] e = new boolean[4];
   @Unique
   private int S;
   @Unique
   private int b;
   @Unique
   private static boolean P;

   @Override
   public void larpWarpBegin(m_.g var1, boolean var2) {
      if (this.S == 4) {
         this.b++;
         if (!P) {
            P = true;
            h.g("GUI", "More than {} projected GUI ranges in one frame; extra content stays flat", 4);
         }
      } else {
         int var3 = this.S++;
         this.L[var3] = this.strata.size();
         this.u[var3] = Integer.MAX_VALUE;
         this.i[var3] = var1;
         this.e[var3] = var2;
      }
   }

   @Override
   public void larpWarpEnd() {
      if (this.b > 0) {
         this.b--;
      } else {
         if (this.S > 0) {
            this.u[this.S - 1] = this.strata.size();
         }
      }
   }

   @Inject(method = "reset", at = @At("HEAD"))
   private void F(CallbackInfo var1) {
      Arrays.fill(this.i, null);
      this.S = this.b = 0;
   }

   @WrapOperation(
      method = "traverse(Ljava/util/function/Consumer;Lnet/minecraft/client/renderer/state/gui/GuiRenderState$TraverseRange;)V",
      at = @At(value = "INVOKE", target = "Ljava/util/List;get(I)Ljava/lang/Object;")
   )
   private Object g(List<?> var1, int var2, Operation<Object> var3) {
      int var4 = -1;

      for (int var5 = 0; var5 < this.S; var5++) {
         if (var2 >= this.L[var5] && var2 < this.u[var5]) {
            var4 = var5;
            break;
         }
      }

      if (var4 < 0) {
         m_.F(-1, null, false);
      } else {
         m_.F(var2 - this.L[var4], this.i[var4], this.e[var4]);
      }

      return var3.call(new Object[]{var1, var2});
   }

   @Inject(method = "traverse(Ljava/util/function/Consumer;Lnet/minecraft/client/renderer/state/gui/GuiRenderState$TraverseRange;)V", at = @At("RETURN"))
   private void t(CallbackInfo var1) {
      m_.F(-1, null, false);
   }
}
