package com.larp.obf.q41a5851bcf8e876a;

import com.larpclient.df;
import com.larpclient.ij;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import java.util.Queue;
import net.minecraft.client.renderer.SectionOcclusionGraph;
import net.minecraft.client.renderer.ViewArea;
import net.minecraft.client.renderer.SectionOcclusionGraph.Node;
import net.minecraft.client.renderer.chunk.SectionRenderDispatcher.RenderSection;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.state.level.ChunkLoadingRenderState;
import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SectionOcclusionGraph.class)
public abstract class qd86c6e550f2c23c2 {
   @Shadow
   private ViewArea viewArea;
   @Unique
   private boolean w;
   @Unique
   private volatile LongOpenHashSet C;

   @Shadow
   public abstract void invalidate();

   @Inject(method = "update", at = @At("HEAD"))
   private void H(CameraRenderState var1, int var2, ChunkLoadingRenderState var3, CallbackInfo var4) {
      boolean var5 = ij.y();
      if (var5 != this.w || var5 && (!var3.addedLoadedChunks.isEmpty() || !var3.removedLoadedChunks.isEmpty())) {
         this.invalidate();
      }

      this.w = var5;
   }

   @WrapOperation(
      method = "scheduleFullUpdate",
      at = @At(value = "INVOKE", target = "Lit/unimi/dsi/fastutil/longs/LongOpenHashSet;clone()Lit/unimi/dsi/fastutil/longs/LongOpenHashSet;", ordinal = 1)
   )
   private LongOpenHashSet C(LongOpenHashSet var1, Operation<LongOpenHashSet> var2) {
      LongOpenHashSet var3 = (LongOpenHashSet)var2.call(new Object[]{var1});
      this.C = this.w ? var3 : null;
      return var3;
   }

   @Inject(method = "initializeQueueForFullUpdate", at = @At("HEAD"), cancellable = true)
   private void H(BlockPos var1, Queue<Node> var2, CallbackInfo var3) {
      LongOpenHashSet var4 = this.C;
      if (var4 != null && this.viewArea != null) {
         df.C(var4, var1, this.viewArea.getViewDistance(), this.viewArea.minSectionY(), this.viewArea.maxSectionY(), var2x -> {
            RenderSection var4x = this.viewArea.getRenderSectionAt(SectionPos.of(var2x).origin());
            if (var4x != null) {
               var2.add(qa9dd2da739220dad.larpCreateNode(var4x, null, 0));
            }
         });
         var3.cancel();
      }
   }

   @Inject(method = "waitAndReset", at = @At("TAIL"))
   private void H(ViewArea var1, CallbackInfo var2) {
      this.C = null;
      this.w = false;
   }
}
