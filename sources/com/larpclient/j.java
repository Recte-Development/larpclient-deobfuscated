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

public final class j {
   private static final RenderType W = RenderType.create(
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
   private static final RenderType K = RenderType.create(
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
   private static final int[] T = new int[]{-1, 1, 1, -1};
   private static final int[] o = new int[]{1, 1, -1, -1};
   private static int F = -1;
   private final xh U;
   private final Matrix4f Y;
   private final Vec3 i;
   private final Vector3f B;
   private final Vector3f q;

   public j(PoseStack var1, xh var2, CameraRenderState var3) {
      if (F != m5.k) {
         ea.z(W.pipeline());
         ea.z(K.pipeline());
         F = m5.k;
      }

      this.U = var2;
      this.Y = var1.last().pose();
      this.i = var3.pos;
      this.B = new Vector3f(1.0F, 0.0F, 0.0F).rotate(var3.orientation);
      this.q = new Vector3f(0.0F, 1.0F, 0.0F).rotate(var3.orientation);
   }

   private void r(VertexConsumer var1, double var2, double var4, double var6, int var8) {
      var1.addVertex(this.Y, (float)(var2 - this.i.x), (float)(var4 - this.i.y), (float)(var6 - this.i.z)).setColor(var8);
   }

   public void H(Vec3 var1, Vec3 var2, double var3, int var5, int var6) {
      double var7 = var2.x + -var1.x;
      double var9 = var2.y + -var1.y;
      double var11 = var2.z + -var1.z;
      if (!(var7 * var7 + var9 * var9 + var11 * var11 < 1.0E-10)) {
         double var13 = this.i.x + -var1.x;
         double var15 = this.i.y + -var1.y;
         double var17 = this.i.z + -var1.z;
         double var19 = var9 * var17 - var11 * var15;
         double var21 = var11 * var13 - var7 * var17;
         double var23 = var7 * var15 - var9 * var13;
         if (var19 * var19 + var21 * var21 + var23 * var23 < 1.0E-10) {
            var19 = this.B.x;
            var21 = this.B.y;
            var23 = this.B.z;
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
         VertexConsumer var29 = this.U.U(W);
         this.r(var29, var1.x + var19, var1.y + var21, var1.z + var23, var5);
         this.r(var29, var2.x + var19, var2.y + var21, var2.z + var23, var6);
         this.r(var29, var2.x + -var19, var2.y + -var21, var2.z + -var23, var6);
         this.r(var29, var1.x + -var19, var1.y + -var21, var1.z + -var23, var5);
      }
   }

   public void U(Vec3 var1, double var2, int var4) {
      VertexConsumer var5 = this.U.U(K);
      float var6 = (float)(var2 * 0.5);

      for (int var7 = 0; var7 < 4; var7++) {
         float var8 = (float)(var1.x - this.i.x) + (this.B.x * T[var7] + this.q.x * o[var7]) * var6;
         float var9 = (float)(var1.y - this.i.y) + (this.B.y * T[var7] + this.q.y * o[var7]) * var6;
         float var10 = (float)(var1.z - this.i.z) + (this.B.z * T[var7] + this.q.z * o[var7]) * var6;
         var5.addVertex(this.Y, var8, var9, var10).setUv(T[var7] < 0 ? 0.0F : 1.0F, o[var7] < 0 ? 0.0F : 1.0F).setColor(var4);
      }
   }
}
