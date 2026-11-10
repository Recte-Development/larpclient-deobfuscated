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

public final class wh implements AutoCloseable {
   private final StagedVertexBuffer S = new StagedVertexBuffer(() -> "Larp effects", 524288);
   private final LinkedHashMap<RenderType, Draw> N = new LinkedHashMap<>();
   private RenderType t;
   private VertexConsumer l;

   public VertexConsumer w(RenderType var1) {
      if (var1 == this.t && this.l != null) {
         return this.l;
      }

      this.t = var1;
      return this.l = this.S
         .getVertexBuilder(
            this.N
               .computeIfAbsent(
                  var1,
                  var1x -> this.S
                     .appendDraw(var1x.format(), var1x.primitiveTopology(), var1x.sortOnUpload() ? RenderSystem.getProjectionType().vertexSorting() : null)
               )
         );
   }

   public void S() {
      this.R(UnaryOperator.identity());
   }

   public void R(UnaryOperator<PreparedRenderType> var1) {
      if (!this.N.isEmpty()) {
         this.t = null;
         this.l = null;

         try {
            this.S.upload();

            for (Entry var3 : this.N.entrySet()) {
               if (!R((RenderType)var3.getKey())) {
                  ExecuteInfo var4 = this.S.getExecuteInfo((Draw)var3.getValue());
                  if (var4 != null) {
                     PreparedRenderType var5 = var1.apply(((RenderType)var3.getKey()).prepare());
                     if (!nt.B(var5, var4) && !d3.m(var5, var4)) {
                        p7.v(var5, var4);
                     }

                     he.Z("Effect draw groups", 1L);
                  }
               }
            }

            for (Entry var10 : this.N.entrySet()) {
               if (R((RenderType)var10.getKey())) {
                  ExecuteInfo var11 = this.S.getExecuteInfo((Draw)var10.getValue());
                  if (var11 != null) {
                     PreparedRenderType var12 = var1.apply(((RenderType)var10.getKey()).prepare());
                     if (!nt.B(var12, var11) && !d3.m(var12, var11)) {
                        p7.v(var12, var11);
                     }

                     he.Z("Effect draw groups", 1L);
                  }
               }
            }
         } finally {
            this.N.clear();
            this.S.endDraw();
         }
      }
   }

   static boolean R(RenderType var0) {
      return var0.pipeline() == RenderPipelines.GLINT;
   }

   public void R() {
      this.t = null;
      this.l = null;
      this.N.clear();
      this.S.endDraw();
   }

   public void A() {
      this.N.clear();
      this.t = null;
      this.l = null;
      this.S.endFrame();
   }

   @Override
   public void close() {
      this.S.close();
   }
}
