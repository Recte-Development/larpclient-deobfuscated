package com.larpclient.render.misc;

import com.larpclient.render.particle.SoftParticleRenderer;
import com.larpclient.render.world.JumpRingRenderer;
import com.larpclient.util.misc.PerfProfiler;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.VertexConsumer;
import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java.util.function.UnaryOperator;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.StagedVertexBuffer;
import net.minecraft.client.renderer.StagedVertexBuffer.Draw;
import net.minecraft.client.renderer.StagedVertexBuffer.ExecuteInfo;
import net.minecraft.client.renderer.rendertype.PreparedRenderType;
import net.minecraft.client.renderer.rendertype.RenderType;

public final class EffectBatch implements AutoCloseable {
   private final StagedVertexBuffer vertexBuffer = new StagedVertexBuffer(() -> "Larp effects", 524288);
   private final LinkedHashMap<RenderType, Draw> draws = new LinkedHashMap<>();
   private RenderType currentRenderType;
   private VertexConsumer currentConsumer;

   public VertexConsumer getConsumer(RenderType var1) {
      if (var1 == this.currentRenderType && this.currentConsumer != null) {
         return this.currentConsumer;
      }

      this.currentRenderType = var1;
      return this.currentConsumer = this.vertexBuffer
         .getVertexBuilder(
            this.draws
               .computeIfAbsent(
                  var1,
                  var1x -> this.vertexBuffer
                     .appendDraw(var1x.format(), var1x.primitiveTopology(), var1x.sortOnUpload() ? RenderSystem.getProjectionType().vertexSorting() : null)
               )
         );
   }

   public void flush() {
      this.flushWith(UnaryOperator.identity());
   }

   public void flushWith(UnaryOperator<PreparedRenderType> var1) {
      if (!this.draws.isEmpty()) {
         this.currentRenderType = null;
         this.currentConsumer = null;

         try {
            this.vertexBuffer.upload();

            for (Entry var3 : this.draws.entrySet()) {
               if (!flushWith((RenderType)var3.getKey())) {
                  ExecuteInfo var4 = this.vertexBuffer.getExecuteInfo((Draw)var3.getValue());
                  if (var4 != null) {
                     PreparedRenderType var5 = var1.apply(((RenderType)var3.getKey()).prepare());
                     if (!JumpRingRenderer.drawJumpRing(var5, var4) && !SoftParticleRenderer.drawSoftParticles(var5, var4)) {
                        RenderPipelineCompat.drawImmediate(var5, var4);
                     }

                     PerfProfiler.addCounter("Effect draw groups", 1L);
                  }
               }
            }

            for (Entry var10 : this.draws.entrySet()) {
               if (flushWith((RenderType)var10.getKey())) {
                  ExecuteInfo var11 = this.vertexBuffer.getExecuteInfo((Draw)var10.getValue());
                  if (var11 != null) {
                     PreparedRenderType var12 = var1.apply(((RenderType)var10.getKey()).prepare());
                     if (!JumpRingRenderer.drawJumpRing(var12, var11) && !SoftParticleRenderer.drawSoftParticles(var12, var11)) {
                        RenderPipelineCompat.drawImmediate(var12, var11);
                     }

                     PerfProfiler.addCounter("Effect draw groups", 1L);
                  }
               }
            }
         } finally {
            this.draws.clear();
            this.vertexBuffer.endDraw();
         }
      }
   }

   static boolean flushWith(RenderType var0) {
      return var0.pipeline() == RenderPipelines.GLINT;
   }

   public void flushWith() {
      this.currentRenderType = null;
      this.currentConsumer = null;
      this.draws.clear();
      this.vertexBuffer.endDraw();
   }

   public void endFrame() {
      this.draws.clear();
      this.currentRenderType = null;
      this.currentConsumer = null;
      this.vertexBuffer.endFrame();
   }

   @Override
   public void close() {
      this.vertexBuffer.close();
   }
}
