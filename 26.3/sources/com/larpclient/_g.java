package com.larpclient;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.renderpearl.api.pipeline.BlendFunction;
import com.mojang.renderpearl.api.pipeline.ColorTargetState;
import com.mojang.renderpearl.api.pipeline.CompareOp;
import com.mojang.renderpearl.api.pipeline.DepthStencilState;
import com.mojang.renderpearl.api.pipeline.RenderPipeline;
import com.mojang.renderpearl.api.pipeline.RenderPipeline.Snippet;
import java.util.Optional;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.rendertype.RenderSetup;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.resources.Identifier;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public final class _g {
   private static final RenderType a = RenderType.create(
      "larp_glyphs",
      RenderSetup.builder(
            RenderPipeline.builder(new Snippet[]{RenderPipelines.DEBUG_FILLED_SNIPPET})
               .withLocation(Identifier.fromNamespaceAndPath("larp-client", "larp/glyphs"))
               .withDepthStencilState(Optional.empty())
               .withColorTargetState(new ColorTargetState(BlendFunction.LIGHTNING))
               .withCull(false)
               .build()
         )
         .createRenderSetup()
   );
   private static final RenderType L = RenderType.create(
      "larp_pearls",
      RenderSetup.builder(
            RenderPipeline.builder(new Snippet[]{RenderPipelines.GUI_TEXTURED_SNIPPET})
               .withLocation(Identifier.fromNamespaceAndPath("larp-client", "larp/pearls"))
               .withFragmentShader(Identifier.fromNamespaceAndPath("larp-client", "larp/pearl"))
               .withDepthStencilState(new DepthStencilState(CompareOp.GREATER_THAN_OR_EQUAL, false))
               .withColorTargetState(new ColorTargetState(BlendFunction.LIGHTNING))
               .withCull(false)
               .build()
         )
         .withTexture("Sampler0", Identifier.fromNamespaceAndPath("larp-client", "shaders/larp/pearl_bloom.png"))
         .createRenderSetup()
   );
   private static final int[] d = new int[]{-1, 1, 1, -1};
   private static final int[] g = new int[]{1, 1, -1, -1};
   private static int M = -1;
   private final wh K;
   private final Matrix4f N;
   private final Vec3 y;
   private final Vector3f l;
   private final Vector3f h;

   public _g(PoseStack var1, wh var2, CameraRenderState var3) {
      if (M != nl.q) {
         se.j(a.pipeline());
         se.j(L.pipeline());
         M = nl.q;
      }

      this.K = var2;
      this.N = var1.last().pose();
      this.y = var3.pos;
      this.l = new Vector3f(1.0F, 0.0F, 0.0F).rotate(var3.orientation);
      this.h = new Vector3f(0.0F, 1.0F, 0.0F).rotate(var3.orientation);
   }

   private void x(VertexConsumer var1, double var2, double var4, double var6, int var8) {
      var1.addVertex(this.N, (float)(var2 - this.y.x), (float)(var4 - this.y.y), (float)(var6 - this.y.z)).setColor(var8);
   }

   public void M(Vec3 var1, Vec3 var2, double var3, int var5, int var6) {
      double var7 = var2.x + -var1.x;
      double var9 = var2.y + -var1.y;
      double var11 = var2.z + -var1.z;
      if (!(var7 * var7 + var9 * var9 + var11 * var11 < 1.0E-10)) {
         double var13 = this.y.x + -var1.x;
         double var15 = this.y.y + -var1.y;
         double var17 = this.y.z + -var1.z;
         double var19 = var9 * var17 - var11 * var15;
         double var21 = var11 * var13 - var7 * var17;
         double var23 = var7 * var15 - var9 * var13;
         if (var19 * var19 + var21 * var21 + var23 * var23 < 1.0E-10) {
            var19 = this.l.x;
            var21 = this.l.y;
            var23 = this.l.z;
         }

         double var25 = Math.sqrt(var19 * var19 + var21 * var21 + var23 * var23);
         if (var25 < 1.0E-5F) {
            var23 = 0.0;
            var21 = 0.0;
            var19 = 0.0;
         } else {
            var19 /= var25;
            var21 /= var25;
            var23 /= var25;
         }

         double var27 = var3 * 0.5;
         var19 *= var27;
         var21 *= var27;
         var23 *= var27;
         VertexConsumer var29 = this.K.w(a);
         this.x(var29, var1.x + var19, var1.y + var21, var1.z + var23, var5);
         this.x(var29, var2.x + var19, var2.y + var21, var2.z + var23, var6);
         this.x(var29, var2.x + -var19, var2.y + -var21, var2.z + -var23, var6);
         this.x(var29, var1.x + -var19, var1.y + -var21, var1.z + -var23, var5);
      }
   }

   public void K(Vec3 var1, double var2, int var4) {
      VertexConsumer var5 = this.K.w(L);
      float var6 = (float)(var2 * 0.5);

      for (int var7 = 0; var7 < 4; var7++) {
         float var8 = (float)(var1.x - this.y.x) + (this.l.x * d[var7] + this.h.x * g[var7]) * var6;
         float var9 = (float)(var1.y - this.y.y) + (this.l.y * d[var7] + this.h.y * g[var7]) * var6;
         float var10 = (float)(var1.z - this.y.z) + (this.l.z * d[var7] + this.h.z * g[var7]) * var6;
         var5.addVertex(this.N, var8, var9, var10).setUv(d[var7] < 0 ? 0.0F : 1.0F, g[var7] < 0 ? 0.0F : 1.0F).setColor(var4);
      }
   }
}
