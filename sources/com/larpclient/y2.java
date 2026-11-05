package com.larpclient;

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

public final class y2 implements AutoCloseable {
   private final StagedVertexBuffer z = new StagedVertexBuffer(() -> "Larp effects", 524288);
   private final LinkedHashMap<RenderType, Draw> O = new LinkedHashMap<>();
   private RenderType a;
   private VertexConsumer j;

   public VertexConsumer a(RenderType var1) {
      if (var1 == this.a && this.j != null) {
         return this.j;
      }

      this.a = var1;
      return this.j = this.z
         .getVertexBuilder(
            this.O
               .computeIfAbsent(
                  var1,
                  var1x -> this.z
                     .appendDraw(var1x.format(), var1x.primitiveTopology(), var1x.sortOnUpload() ? RenderSystem.getProjectionType().vertexSorting() : null)
               )
         );
   }

   public void v() {
      this.P(UnaryOperator.identity());
   }

   public void P(UnaryOperator<PreparedRenderType> var1) {
      if (!this.O.isEmpty()) {
         this.a = null;
         this.j = null;

         try {
            this.z.upload();

            for (Entry var3 : this.O.entrySet()) {
               if (!k((RenderType)var3.getKey())) {
                  ExecuteInfo var4 = this.z.getExecuteInfo((Draw)var3.getValue());
                  if (var4 != null) {
                     PreparedRenderType var5 = var1.apply(((RenderType)var3.getKey()).prepare());
                     if (!m1.u(var5, var4) && !jx.T(var5, var4)) {
                        of.t(var5, var4);
                     }

                     h_.A("Effect draw groups", 1L);
                  }
               }
            }

            for (Entry var10 : this.O.entrySet()) {
               if (k((RenderType)var10.getKey())) {
                  ExecuteInfo var11 = this.z.getExecuteInfo((Draw)var10.getValue());
                  if (var11 != null) {
                     PreparedRenderType var12 = var1.apply(((RenderType)var10.getKey()).prepare());
                     if (!m1.u(var12, var11) && !jx.T(var12, var11)) {
                        of.t(var12, var11);
                     }

                     h_.A("Effect draw groups", 1L);
                  }
               }
            }
         } finally {
            this.O.clear();
            this.z.endDraw();
         }
      }
   }

   static boolean k(RenderType var0) {
      return var0.pipeline() == RenderPipelines.GLINT;
   }

   public void D() {
      this.a = null;
      this.j = null;
      this.O.clear();
      this.z.endDraw();
   }

   public void R() {
      this.O.clear();
      this.a = null;
      this.j = null;
      this.z.endFrame();
   }

   @Override
   public void close() {
      this.z.close();
   }
}
