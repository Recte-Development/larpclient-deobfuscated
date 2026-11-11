package com.larpclient.render.world;

import com.larpclient.render.misc.ClientPipelines;
import com.larpclient.render.misc.EffectBatch;
import com.larpclient.render.overlay.PostProcessing;
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

public final class WorldShapeRenderer {
   private static final RenderType glyphRenderType = RenderType.create(
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
   private static final RenderType pearlRenderType = RenderType.create(
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
   private static final int[] cornerX = new int[]{-1, 1, 1, -1};
   private static final int[] cornerY = new int[]{1, 1, -1, -1};
   private static int resourceGeneration = -1;
   private final EffectBatch bufferSource;
   private final Matrix4f pose;
   private final Vec3 cameraPos;
   private final Vector3f cameraRight;
   private final Vector3f cameraUp;

   public WorldShapeRenderer(PoseStack var1, EffectBatch var2, CameraRenderState var3) {
      if (resourceGeneration != PostProcessing.reloadCount) {
         ClientPipelines.validatePipeline(glyphRenderType.pipeline());
         ClientPipelines.validatePipeline(pearlRenderType.pipeline());
         resourceGeneration = PostProcessing.reloadCount;
      }

      this.bufferSource = var2;
      this.pose = var1.last().pose();
      this.cameraPos = var3.pos;
      this.cameraRight = new Vector3f(1.0F, 0.0F, 0.0F).rotate(var3.orientation);
      this.cameraUp = new Vector3f(0.0F, 1.0F, 0.0F).rotate(var3.orientation);
   }

   private void addVertex(VertexConsumer var1, double var2, double var4, double var6, int var8) {
      var1.addVertex(this.pose, (float)(var2 - this.cameraPos.x), (float)(var4 - this.cameraPos.y), (float)(var6 - this.cameraPos.z)).setColor(var8);
   }

   public void drawLine(Vec3 var1, Vec3 var2, double var3, int var5, int var6) {
      double var7 = var2.x + -var1.x;
      double var9 = var2.y + -var1.y;
      double var11 = var2.z + -var1.z;
      if (!(var7 * var7 + var9 * var9 + var11 * var11 < 1.0E-10)) {
         double var13 = this.cameraPos.x + -var1.x;
         double var15 = this.cameraPos.y + -var1.y;
         double var17 = this.cameraPos.z + -var1.z;
         double var19 = var9 * var17 - var11 * var15;
         double var21 = var11 * var13 - var7 * var17;
         double var23 = var7 * var15 - var9 * var13;
         if (var19 * var19 + var21 * var21 + var23 * var23 < 1.0E-10) {
            var19 = this.cameraRight.x;
            var21 = this.cameraRight.y;
            var23 = this.cameraRight.z;
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
         VertexConsumer var29 = this.bufferSource.getConsumer(glyphRenderType);
         this.addVertex(var29, var1.x + var19, var1.y + var21, var1.z + var23, var5);
         this.addVertex(var29, var2.x + var19, var2.y + var21, var2.z + var23, var6);
         this.addVertex(var29, var2.x + -var19, var2.y + -var21, var2.z + -var23, var6);
         this.addVertex(var29, var1.x + -var19, var1.y + -var21, var1.z + -var23, var5);
      }
   }

   public void drawPearl(Vec3 var1, double var2, int var4) {
      VertexConsumer var5 = this.bufferSource.getConsumer(pearlRenderType);
      float var6 = (float)(var2 * 0.5);

      for (int var7 = 0; var7 < 4; var7++) {
         float var8 = (float)(var1.x - this.cameraPos.x) + (this.cameraRight.x * cornerX[var7] + this.cameraUp.x * cornerY[var7]) * var6;
         float var9 = (float)(var1.y - this.cameraPos.y) + (this.cameraRight.y * cornerX[var7] + this.cameraUp.y * cornerY[var7]) * var6;
         float var10 = (float)(var1.z - this.cameraPos.z) + (this.cameraRight.z * cornerX[var7] + this.cameraUp.z * cornerY[var7]) * var6;
         var5.addVertex(this.pose, var8, var9, var10).setUv(cornerX[var7] < 0 ? 0.0F : 1.0F, cornerY[var7] < 0 ? 0.0F : 1.0F).setColor(var4);
      }
   }
}
