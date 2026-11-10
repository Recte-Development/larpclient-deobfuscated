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

public final class hh implements AutoCloseable {
   private final StagedVertexBuffer e = new StagedVertexBuffer(() -> "Larp effects", 524288);
   private final LinkedHashMap<RenderType, Draw> c = new LinkedHashMap<>();
   private RenderType y;
   private VertexConsumer b;

   public VertexConsumer g(RenderType var1) {
      if (var1 == this.y && this.b != null) {
         return this.b;
      }

      this.y = var1;
      return this.b = this.e
         .getVertexBuilder(
            this.c
               .computeIfAbsent(
                  var1,
                  var1x -> this.e
                     .appendDraw(var1x.format(), var1x.primitiveTopology(), var1x.sortOnUpload() ? RenderSystem.getProjectionType().vertexSorting() : null)
               )
         );
   }

   public void K() {
      this.K(UnaryOperator.identity());
   }

   public void K(UnaryOperator<PreparedRenderType> var1) {
      if (!this.c.isEmpty()) {
         this.y = null;
         this.b = null;

         try {
            this.e.upload();

            for (Entry var3 : this.c.entrySet()) {
               if (!q((RenderType)var3.getKey())) {
                  ExecuteInfo var4 = this.e.getExecuteInfo((Draw)var3.getValue());
                  if (var4 != null) {
                     PreparedRenderType var5 = var1.apply(((RenderType)var3.getKey()).prepare());
                     if (!jv.B(var5, var4) && !he.F(var5, var4)) {
                        mx.o(var5, var4);
                     }

                     zj.K("Effect draw groups", 1L);
                  }
               }
            }

            for (Entry var10 : this.c.entrySet()) {
               if (q((RenderType)var10.getKey())) {
                  ExecuteInfo var11 = this.e.getExecuteInfo((Draw)var10.getValue());
                  if (var11 != null) {
                     PreparedRenderType var12 = var1.apply(((RenderType)var10.getKey()).prepare());
                     if (!jv.B(var12, var11) && !he.F(var12, var11)) {
                        mx.o(var12, var11);
                     }

                     zj.K("Effect draw groups", 1L);
                  }
               }
            }
         } finally {
            this.c.clear();
            this.e.endDraw();
         }
      }
   }

   static boolean q(RenderType var0) {
      return var0.pipeline() == RenderPipelines.GLINT;
   }

   public void T() {
      this.y = null;
      this.b = null;
      this.c.clear();
      this.e.endDraw();
   }

   public void g() {
      this.c.clear();
      this.y = null;
      this.b = null;
      this.e.endFrame();
   }

   @Override
   public void close() {
      this.e.close();
   }
}
