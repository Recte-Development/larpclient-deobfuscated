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

public final class fn {
   public static final String f = "Tung Tung Tung Sahur";
   private static final Identifier R = Identifier.fromNamespaceAndPath("larp-client", "models/pets/sahur/mesh.bin");
   private static final RenderType J = y(false);
   private static final RenderType W = y(true);
   private static qt.v X;
   private static long m = -1L;
   private static long P = -1L;

   private static RenderType y(boolean var0) {
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

   public static boolean p() {
      return X != null && m == nl.q;
   }

   public static qt.v e() {
      if (!p()) {
         throw new IllegalStateException("Sahur asset is not prepared");
      } else {
         return X;
      }
   }

   public static void G() {
      if (X == null || m != nl.q) {
         X = null;

         try (InputStream var0 = Minecraft.getInstance().getResourceManager().getResourceOrThrow(R).open()) {
            qt.v var1 = qt.s(var0);
            se.j(J.pipeline());
            X = var1;
            m = nl.q;
            _q.c("Sahur", "Static mesh ready: {} triangles, resource generation {}", X.triangles(), m);
         } catch (IOException var5) {
            throw new IllegalStateException("Sahur asset load failed; reload resources to retry", var5);
         }
      }
   }

   public static void V() {
      G();
      if (P != nl.q) {
         se.j(W.pipeline());
         P = nl.q;
      }
   }

   public static void H(fz var0, Vec3 var1, Quaternionfc var2, float var3) {
      if (p()) {
         var0.L(J, var1, var2, var3, X.vertices(), X.colors());
         he.Z("Sahur triangles", X.triangles());
      }
   }

   public static void Y(PoseStack var0, wh var1, Matrix4f var2) {
      if (p()) {
         Matrix4f var3 = new Matrix4f(var0.last().pose()).mul(var2);
         fz.E(var1, J, var3, X.vertices(), X.colors());
         he.Z("Sahur triangles", X.triangles());
      }
   }

   public static void J(PoseStack var0, wh var1, Matrix4f var2, float[] var3, int[] var4) {
      if (p()) {
         Matrix4f var5 = new Matrix4f(var0.last().pose()).mul(var2);
         fz.E(var1, J, var5, var3, var4);
         he.Z("Sahur triangles", var4.length / 3);
      }
   }

   public static void U(PoseStack var0, wh var1, Matrix4f var2, float[] var3, int[] var4, double var5) {
      if (p() && !(var5 <= 0.001)) {
         Matrix4f var7 = new Matrix4f(var0.last().pose()).mul(var2);
         VertexConsumer var8 = var1.w(W);

         for (int var9 = 0; var9 < var4.length; var9++) {
            int var10 = var9 * 5;
            var8.addVertex(var7, var3[var10], var3[var10 + 1], var3[var10 + 2]).setUv(var3[var10 + 3], var3[var10 + 4]).setColor(vv.O(var4[var9], var5));
         }

         he.Z("Sahur triangles", var4.length / 3);
      }
   }

   private fn() {
   }
}
