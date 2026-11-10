package com.larp.obf.qa300483f54d804a3;

import com.larpclient.p_;
import com.larpclient.v;
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
public abstract class q152b629aef1dcb86 {
   @Shadow
   private ViewArea viewArea;
   @Unique
   private boolean Y;
   @Unique
   private volatile LongOpenHashSet N;

   @Shadow
   public abstract void invalidate();

   @Inject(method = "update", at = @At("HEAD"))
   private void X(CameraRenderState var1, int var2, ChunkLoadingRenderState var3, CallbackInfo var4) {
      boolean var5 = p_.V();
      if (var5 != this.Y || var5 && (!var3.addedLoadedChunks.isEmpty() || !var3.removedLoadedChunks.isEmpty())) {
         this.invalidate();
      }

      this.Y = var5;
   }

   @WrapOperation(
      method = "scheduleFullUpdate",
      at = @At(value = "INVOKE", target = "Lit/unimi/dsi/fastutil/longs/LongOpenHashSet;clone()Lit/unimi/dsi/fastutil/longs/LongOpenHashSet;", ordinal = 1)
   )
   private LongOpenHashSet K(LongOpenHashSet var1, Operation<LongOpenHashSet> var2) {
      LongOpenHashSet var3 = (LongOpenHashSet)var2.call(new Object[]{var1});
      this.N = this.Y ? var3 : null;
      return var3;
   }

   @Inject(method = "initializeQueueForFullUpdate", at = @At("HEAD"), cancellable = true)
   private void U(BlockPos var1, Queue<Node> var2, CallbackInfo var3) {
      LongOpenHashSet var4 = this.N;
      if (var4 != null && this.viewArea != null) {
         v.h(var4, var1, this.viewArea.getViewDistance(), this.viewArea.minSectionY(), this.viewArea.maxSectionY(), var2x -> {
            RenderSection var4x = this.viewArea.getRenderSectionAt(SectionPos.of(var2x).origin());
            if (var4x != null) {
               var2.add(q46ef8bacab480735.larpCreateNode(var4x, null, 0));
            }
         });
         var3.cancel();
      }
   }

   @Inject(method = "waitAndReset", at = @At("TAIL"))
   private void E(ViewArea var1, CallbackInfo var2) {
      this.N = null;
      this.Y = false;
   }
}
