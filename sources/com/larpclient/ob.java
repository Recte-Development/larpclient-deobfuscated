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

public final class ob {
   private static final RenderType R = RenderType.create(
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
   private static final RenderType d = RenderType.create(
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
   private static final int[] h = new int[]{-1, 1, 1, -1};
   private static final int[] Y = new int[]{1, 1, -1, -1};
   private static int x = -1;
   private final y2 U;
   private final Matrix4f T;
   private final Vec3 L;
   private final Vector3f N;
   private final Vector3f e;

   public ob(PoseStack var1, y2 var2, CameraRenderState var3) {
      if (x != jk.v) {
         e4.U(R.pipeline());
         e4.U(d.pipeline());
         x = jk.v;
      }

      this.U = var2;
      this.T = var1.last().pose();
      this.L = var3.pos;
      this.N = new Vector3f(1.0F, 0.0F, 0.0F).rotate(var3.orientation);
      this.e = new Vector3f(0.0F, 1.0F, 0.0F).rotate(var3.orientation);
   }

   private void j(VertexConsumer var1, double var2, double var4, double var6, int var8) {
      var1.addVertex(this.T, (float)(var2 - this.L.x), (float)(var4 - this.L.y), (float)(var6 - this.L.z)).setColor(var8);
   }

   public void a(Vec3 var1, Vec3 var2, double var3, int var5, int var6) {
      double var7 = var2.x + -var1.x;
      double var9 = var2.y + -var1.y;
      double var11 = var2.z + -var1.z;
      if (!(var7 * var7 + var9 * var9 + var11 * var11 < 1.0E-10)) {
         double var13 = this.L.x + -var1.x;
         double var15 = this.L.y + -var1.y;
         double var17 = this.L.z + -var1.z;
         double var19 = var9 * var17 - var11 * var15;
         double var21 = var11 * var13 - var7 * var17;
         double var23 = var7 * var15 - var9 * var13;
         if (var19 * var19 + var21 * var21 + var23 * var23 < 1.0E-10) {
            var19 = this.N.x;
            var21 = this.N.y;
            var23 = this.N.z;
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
         VertexConsumer var29 = this.U.a(R);
         this.j(var29, var1.x + var19, var1.y + var21, var1.z + var23, var5);
         this.j(var29, var2.x + var19, var2.y + var21, var2.z + var23, var6);
         this.j(var29, var2.x + -var19, var2.y + -var21, var2.z + -var23, var6);
         this.j(var29, var1.x + -var19, var1.y + -var21, var1.z + -var23, var5);
      }
   }

   public void C(Vec3 var1, double var2, int var4) {
      VertexConsumer var5 = this.U.a(d);
      float var6 = (float)(var2 * 0.5);

      for (int var7 = 0; var7 < 4; var7++) {
         float var8 = (float)(var1.x - this.L.x) + (this.N.x * h[var7] + this.e.x * Y[var7]) * var6;
         float var9 = (float)(var1.y - this.L.y) + (this.N.y * h[var7] + this.e.y * Y[var7]) * var6;
         float var10 = (float)(var1.z - this.L.z) + (this.N.z * h[var7] + this.e.z * Y[var7]) * var6;
         var5.addVertex(this.T, var8, var9, var10).setUv(h[var7] < 0 ? 0.0F : 1.0F, Y[var7] < 0 ? 0.0F : 1.0F).setColor(var4);
      }
   }
}
