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

public final class xh implements AutoCloseable {
   private final StagedVertexBuffer u = new StagedVertexBuffer(() -> "Larp effects", 524288);
   private final LinkedHashMap<RenderType, Draw> O = new LinkedHashMap<>();
   private RenderType a;
   private VertexConsumer w;

   public VertexConsumer U(RenderType var1) {
      if (var1 == this.a && this.w != null) {
         return this.w;
      }

      this.a = var1;
      return this.w = this.u
         .getVertexBuilder(
            this.O
               .computeIfAbsent(
                  var1,
                  var1x -> this.u
                     .appendDraw(var1x.format(), var1x.primitiveTopology(), var1x.sortOnUpload() ? RenderSystem.getProjectionType().vertexSorting() : null)
               )
         );
   }

   public void b() {
      this.g(UnaryOperator.identity());
   }

   public void g(UnaryOperator<PreparedRenderType> var1) {
      if (!this.O.isEmpty()) {
         this.a = null;
         this.w = null;

         try {
            this.u.upload();

            for (Entry var3 : this.O.entrySet()) {
               if (!c((RenderType)var3.getKey())) {
                  ExecuteInfo var4 = this.u.getExecuteInfo((Draw)var3.getValue());
                  if (var4 != null) {
                     PreparedRenderType var5 = var1.apply(((RenderType)var3.getKey()).prepare());
                     if (!t_.V(var5, var4) && !_v.j(var5, var4)) {
                        hj.V(var5, var4);
                     }

                     kx.U("Effect draw groups", 1L);
                  }
               }
            }

            for (Entry var10 : this.O.entrySet()) {
               if (c((RenderType)var10.getKey())) {
                  ExecuteInfo var11 = this.u.getExecuteInfo((Draw)var10.getValue());
                  if (var11 != null) {
                     PreparedRenderType var12 = var1.apply(((RenderType)var10.getKey()).prepare());
                     if (!t_.V(var12, var11) && !_v.j(var12, var11)) {
                        hj.V(var12, var11);
                     }

                     kx.U("Effect draw groups", 1L);
                  }
               }
            }
         } finally {
            this.O.clear();
            this.u.endDraw();
         }
      }
   }

   static boolean c(RenderType var0) {
      return var0.pipeline() == RenderPipelines.GLINT;
   }

   public void o() {
      this.a = null;
      this.w = null;
      this.O.clear();
      this.u.endDraw();
   }

   public void s() {
      this.O.clear();
      this.a = null;
      this.w = null;
      this.u.endFrame();
   }

   @Override
   public void close() {
      this.u.close();
   }
}
