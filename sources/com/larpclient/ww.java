package com.larpclient;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.renderpearl.api.pipeline.ColorTargetState;
import com.mojang.renderpearl.api.pipeline.CompareOp;
import com.mojang.renderpearl.api.pipeline.DepthStencilState;
import com.mojang.renderpearl.api.pipeline.PrimitiveTopology;
import com.mojang.renderpearl.api.pipeline.RenderPipeline;
import com.mojang.renderpearl.api.pipeline.RenderPipeline.Builder;
import com.mojang.renderpearl.api.pipeline.RenderPipeline.Snippet;
import java.io.IOException;
import java.io.InputStream;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.rendertype.RenderSetup;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.resources.Identifier;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;
import org.joml.Quaternionfc;

public final class ww {
   public static final String i = "Tung Tung Tung Sahur";
   private static final Identifier M = Identifier.fromNamespaceAndPath("larp-client", "models/pets/sahur/mesh.bin");
   private static final RenderType t = j(false);
   private static final RenderType f = j(true);
   private static _y.w g;
   private static long x = -1L;
   private static long B = -1L;

   private static RenderType j(boolean var0) {
      Builder var1 = RenderPipeline.builder(new Snippet[]{RenderPipelines.GUI_TEXTURED_SNIPPET})
         .withLocation(Identifier.fromNamespaceAndPath("larp-client", var0 ? "sahur_strike" : "sahur_pet"))
         .withFragmentShader(Identifier.fromNamespaceAndPath("larp-client", "particle/sprite"))
         .withPrimitiveTopology(PrimitiveTopology.TRIANGLES)
         .withDepthStencilState(new DepthStencilState(CompareOp.GREATER_THAN_OR_EQUAL, true))
         .withColorTargetState(ColorTargetState.DEFAULT)
         .withCull(true);
      if (var0) {
         var1.withShaderDefine("DITHER_FADE");
      }

      return RenderType.create(
         var0 ? "larp_sahur_strike" : "larp_sahur_pet",
         RenderSetup.builder(var1.build())
            .withTexture("Sampler0", Identifier.fromNamespaceAndPath("larp-client", "models/pets/sahur/albedo.png"))
            .createRenderSetup()
      );
   }

   public static boolean j() {
      return g != null && x == m5.k;
   }

   public static _y.w r() {
      if (!j()) {
         throw new IllegalStateException("Sahur asset is not prepared");
      } else {
         return g;
      }
   }

   public static void n() {
      if (g == null || x != m5.k) {
         g = null;

         try (InputStream var0 = Minecraft.getInstance().getResourceManager().getResourceOrThrow(M).open()) {
            _y.w var1 = _y.n(var0);
            ea.z(t.pipeline());
            g = var1;
            x = m5.k;
            mt.U("Sahur", "Static mesh ready: {} triangles, resource generation {}", g.triangles(), x);
         } catch (IOException var5) {
            throw new IllegalStateException("Sahur asset load failed; reload resources to retry", var5);
         }
      }
   }

   public static void l() {
      n();
      if (B != m5.k) {
         ea.z(f.pipeline());
         B = m5.k;
      }
   }

   public static void m(ki var0, Vec3 var1, Quaternionfc var2, float var3) {
      if (j()) {
         var0.m(t, var1, var2, var3, g.vertices(), g.colors());
         kx.U("Sahur triangles", g.triangles());
      }
   }

   public static void u(PoseStack var0, xh var1, Matrix4f var2) {
      if (j()) {
         Matrix4f var3 = new Matrix4f(var0.last().pose()).mul(var2);
         ki.n(var1, t, var3, g.vertices(), g.colors());
         kx.U("Sahur triangles", g.triangles());
      }
   }

   public static void G(PoseStack var0, xh var1, Matrix4f var2, float[] var3, int[] var4) {
      if (j()) {
         Matrix4f var5 = new Matrix4f(var0.last().pose()).mul(var2);
         ki.n(var1, t, var5, var3, var4);
         kx.U("Sahur triangles", var4.length / 3);
      }
   }

   public static void d(PoseStack var0, xh var1, Matrix4f var2, float[] var3, int[] var4, double var5) {
      if (j() && !(var5 <= 0.001)) {
         Matrix4f var7 = new Matrix4f(var0.last().pose()).mul(var2);
         VertexConsumer var8 = var1.U(f);

         for (int var9 = 0; var9 < var4.length; var9++) {
            int var10 = var9 * 5;
            var8.addVertex(var7, var3[var10], var3[var10 + 1], var3[var10 + 2]).setUv(var3[var10 + 3], var3[var10 + 4]).setColor(_3.U(var4[var9], var5));
         }

         kx.U("Sahur triangles", var4.length / 3);
      }
   }

   private ww() {
   }
}
