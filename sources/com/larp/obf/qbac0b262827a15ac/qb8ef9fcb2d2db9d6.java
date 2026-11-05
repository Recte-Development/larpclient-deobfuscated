package com.larp.obf.qbac0b262827a15ac;

import com.larpclient.pd;
import com.larpclient.tq;
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
public abstract class qb8ef9fcb2d2db9d6 {
   @Shadow
   private ViewArea viewArea;
   @Unique
   private boolean v;
   @Unique
   private volatile LongOpenHashSet W;

   @Shadow
   public abstract void invalidate();

   @Inject(method = "update", at = @At("HEAD"))
   private void e(CameraRenderState var1, int var2, ChunkLoadingRenderState var3, CallbackInfo var4) {
      boolean var5 = tq.f();
      if (var5 != this.v || var5 && (!var3.addedLoadedChunks.isEmpty() || !var3.removedLoadedChunks.isEmpty())) {
         this.invalidate();
      }

      this.v = var5;
   }

   @WrapOperation(
      method = "scheduleFullUpdate",
      at = @At(value = "INVOKE", target = "Lit/unimi/dsi/fastutil/longs/LongOpenHashSet;clone()Lit/unimi/dsi/fastutil/longs/LongOpenHashSet;", ordinal = 1)
   )
   private LongOpenHashSet m(LongOpenHashSet var1, Operation<LongOpenHashSet> var2) {
      LongOpenHashSet var3 = (LongOpenHashSet)var2.call(new Object[]{var1});
      this.W = this.v ? var3 : null;
      return var3;
   }

   @Inject(method = "initializeQueueForFullUpdate", at = @At("HEAD"), cancellable = true)
   private void N(BlockPos var1, Queue<Node> var2, CallbackInfo var3) {
      LongOpenHashSet var4 = this.W;
      if (var4 != null && this.viewArea != null) {
         pd.t(var4, var1, this.viewArea.getViewDistance(), this.viewArea.minSectionY(), this.viewArea.maxSectionY(), var2x -> {
            RenderSection var4x = this.viewArea.getRenderSectionAt(SectionPos.of(var2x).origin());
            if (var4x != null) {
               var2.add(q3f561256aa948c37.larpCreateNode(var4x, null, 0));
            }
         });
         var3.cancel();
      }
   }

   @Inject(method = "waitAndReset", at = @At("TAIL"))
   private void y(ViewArea var1, CallbackInfo var2) {
      this.W = null;
      this.v = false;
   }
}
