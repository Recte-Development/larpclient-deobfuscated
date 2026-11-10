package com.larpclient;

import com.mojang.blaze3d.pipeline.BlendFunction;
import com.mojang.blaze3d.pipeline.ColorTargetState;
import com.mojang.blaze3d.pipeline.DepthStencilState;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.pipeline.RenderPipeline.Snippet;
import com.mojang.blaze3d.platform.CompareOp;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import java.util.Optional;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.rendertype.RenderSetup;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.resources.Identifier;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public final class gv {
   private static final RenderType h = RenderType.create(
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
   private static final RenderType z = RenderType.create(
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
   private static final int[] u = new int[]{-1, 1, 1, -1};
   private static final int[] o = new int[]{1, 1, -1, -1};
   private static int A = -1;
   private final hh B;
   private final Matrix4f Z;
   private final Vec3 H;
   private final Vector3f w;
   private final Vector3f f;

   public gv(PoseStack var1, hh var2, CameraRenderState var3) {
      if (A != wa.s) {
         ai.U(h.pipeline());
         ai.U(z.pipeline());
         A = wa.s;
      }

      this.B = var2;
      this.Z = var1.last().pose();
      this.H = var3.pos;
      this.w = new Vector3f(1.0F, 0.0F, 0.0F).rotate(var3.orientation);
      this.f = new Vector3f(0.0F, 1.0F, 0.0F).rotate(var3.orientation);
   }

   private void i(VertexConsumer var1, double var2, double var4, double var6, int var8) {
      var1.addVertex(this.Z, (float)(var2 - this.H.x), (float)(var4 - this.H.y), (float)(var6 - this.H.z)).setColor(var8);
   }

   public void h(Vec3 var1, Vec3 var2, double var3, int var5, int var6) {
      double var7 = var2.x + -var1.x;
      double var9 = var2.y + -var1.y;
      double var11 = var2.z + -var1.z;
      if (!(var7 * var7 + var9 * var9 + var11 * var11 < 1.0E-10)) {
         double var13 = this.H.x + -var1.x;
         double var15 = this.H.y + -var1.y;
         double var17 = this.H.z + -var1.z;
         double var19 = var9 * var17 - var11 * var15;
         double var21 = var11 * var13 - var7 * var17;
         double var23 = var7 * var15 - var9 * var13;
         if (var19 * var19 + var21 * var21 + var23 * var23 < 1.0E-10) {
            var19 = this.w.x;
            var21 = this.w.y;
            var23 = this.w.z;
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
         VertexConsumer var29 = this.B.g(h);
         this.i(var29, var1.x + var19, var1.y + var21, var1.z + var23, var5);
         this.i(var29, var2.x + var19, var2.y + var21, var2.z + var23, var6);
         this.i(var29, var2.x + -var19, var2.y + -var21, var2.z + -var23, var6);
         this.i(var29, var1.x + -var19, var1.y + -var21, var1.z + -var23, var5);
      }
   }

   public void l(Vec3 var1, double var2, int var4) {
      VertexConsumer var5 = this.B.g(z);
      float var6 = (float)(var2 * 0.5);

      for (int var7 = 0; var7 < 4; var7++) {
         float var8 = (float)(var1.x - this.H.x) + (this.w.x * u[var7] + this.f.x * o[var7]) * var6;
         float var9 = (float)(var1.y - this.H.y) + (this.w.y * u[var7] + this.f.y * o[var7]) * var6;
         float var10 = (float)(var1.z - this.H.z) + (this.w.z * u[var7] + this.f.z * o[var7]) * var6;
         var5.addVertex(this.Z, var8, var9, var10).setUv(u[var7] < 0 ? 0.0F : 1.0F, o[var7] < 0 ? 0.0F : 1.0F).setColor(var4);
      }
   }
}
