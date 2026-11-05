package com.larp.obf.q5682c6a8aca9b6ec;

import com.larpclient.r5;
import com.larpclient.rr;
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
public abstract class q6533f4586770860e {
   @Shadow
   private ViewArea viewArea;
   @Unique
   private boolean Y;
   @Unique
   private volatile LongOpenHashSet c;

   @Shadow
   public abstract void invalidate();

   @Inject(method = "update", at = @At("HEAD"))
   private void M(CameraRenderState var1, int var2, ChunkLoadingRenderState var3, CallbackInfo var4) {
      boolean var5 = rr.o();
      if (var5 != this.Y || var5 && (!var3.addedLoadedChunks.isEmpty() || !var3.removedLoadedChunks.isEmpty())) {
         this.invalidate();
      }

      this.Y = var5;
   }

   @WrapOperation(
      method = "scheduleFullUpdate",
      at = @At(value = "INVOKE", target = "Lit/unimi/dsi/fastutil/longs/LongOpenHashSet;clone()Lit/unimi/dsi/fastutil/longs/LongOpenHashSet;", ordinal = 1)
   )
   private LongOpenHashSet W(LongOpenHashSet var1, Operation<LongOpenHashSet> var2) {
      LongOpenHashSet var3 = (LongOpenHashSet)var2.call(new Object[]{var1});
      this.c = this.Y ? var3 : null;
      return var3;
   }

   @Inject(method = "initializeQueueForFullUpdate", at = @At("HEAD"), cancellable = true)
   private void Y(BlockPos var1, Queue<Node> var2, CallbackInfo var3) {
      LongOpenHashSet var4 = this.c;
      if (var4 != null && this.viewArea != null) {
         r5.y(var4, var1, this.viewArea.getViewDistance(), this.viewArea.minSectionY(), this.viewArea.maxSectionY(), var2x -> {
            RenderSection var4x = this.viewArea.getRenderSectionAt(SectionPos.of(var2x).origin());
            if (var4x != null) {
               var2.add(q8c3790365d862916.larpCreateNode(var4x, null, 0));
            }
         });
         var3.cancel();
      }
   }

   @Inject(method = "waitAndReset", at = @At("TAIL"))
   private void t(ViewArea var1, CallbackInfo var2) {
      this.c = null;
      this.Y = false;
   }
}
