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

public final class _i {
   public static final String G = "Tung Tung Tung Sahur";
   private static final Identifier c = Identifier.fromNamespaceAndPath("larp-client", "models/pets/sahur/mesh.bin");
   private static final RenderType k = m(false);
   private static final RenderType I = m(true);
   private static ed.q V;
   private static long Z = -1L;
   private static long v = -1L;

   private static RenderType m(boolean var0) {
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

   public static boolean V() {
      return V != null && Z == jk.v;
   }

   public static ed.q P() {
      if (!V()) {
         throw new IllegalStateException("Sahur asset is not prepared");
      } else {
         return V;
      }
   }

   public static void h() {
      if (V == null || Z != jk.v) {
         V = null;

         try (InputStream var0 = Minecraft.getInstance().getResourceManager().getResourceOrThrow(c).open()) {
            ed.q var1 = ed.r(var0);
            e4.U(k.pipeline());
            V = var1;
            Z = jk.v;
            r4.g("Sahur", "Static mesh ready: {} triangles, resource generation {}", V.triangles(), Z);
         } catch (IOException var5) {
            throw new IllegalStateException("Sahur asset load failed; reload resources to retry", var5);
         }
      }
   }

   public static void c() {
      h();
      if (v != jk.v) {
         e4.U(I.pipeline());
         v = jk.v;
      }
   }

   public static void b(r6 var0, Vec3 var1, Quaternionfc var2, float var3) {
      if (V()) {
         var0.R(k, var1, var2, var3, V.vertices(), V.colors());
         h_.A("Sahur triangles", V.triangles());
      }
   }

   public static void g(PoseStack var0, y2 var1, Matrix4f var2) {
      if (V()) {
         Matrix4f var3 = new Matrix4f(var0.last().pose()).mul(var2);
         r6.D(var1, k, var3, V.vertices(), V.colors());
         h_.A("Sahur triangles", V.triangles());
      }
   }

   public static void s(PoseStack var0, y2 var1, Matrix4f var2, float[] var3, int[] var4) {
      if (V()) {
         Matrix4f var5 = new Matrix4f(var0.last().pose()).mul(var2);
         r6.D(var1, k, var5, var3, var4);
         h_.A("Sahur triangles", var4.length / 3);
      }
   }

   public static void L(PoseStack var0, y2 var1, Matrix4f var2, float[] var3, int[] var4, double var5) {
      if (V() && !(var5 <= 0.001)) {
         Matrix4f var7 = new Matrix4f(var0.last().pose()).mul(var2);
         VertexConsumer var8 = var1.a(I);

         for (int var9 = 0; var9 < var4.length; var9++) {
            int var10 = var9 * 5;
            var8.addVertex(var7, var3[var10], var3[var10 + 1], var3[var10 + 2]).setUv(var3[var10 + 3], var3[var10 + 4]).setColor(oh.S(var4[var9], var5));
         }

         h_.A("Sahur triangles", var4.length / 3);
      }
   }

   private _i() {
   }
}
