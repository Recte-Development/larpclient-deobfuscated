package com.larpclient;

import com.mojang.blaze3d.PrimitiveTopology;
import com.mojang.blaze3d.pipeline.ColorTargetState;
import com.mojang.blaze3d.pipeline.DepthStencilState;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.pipeline.RenderPipeline.Builder;
import com.mojang.blaze3d.pipeline.RenderPipeline.Snippet;
import com.mojang.blaze3d.platform.CompareOp;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
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

public final class k5 {
   public static final String e = "Tung Tung Tung Sahur";
   private static final Identifier m = Identifier.fromNamespaceAndPath("larp-client", "models/pets/sahur/mesh.bin");
   private static final RenderType c = F(false);
   private static final RenderType N = F(true);
   private static ka.u Z;
   private static long f = -1L;
   private static long K = -1L;

   private static RenderType F(boolean var0) {
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

   public static boolean v() {
      return Z != null && f == wa.s;
   }

   public static ka.u k() {
      if (!v()) {
         throw new IllegalStateException("Sahur asset is not prepared");
      } else {
         return Z;
      }
   }

   public static void H() {
      if (Z == null || f != wa.s) {
         Z = null;

         try (InputStream var0 = Minecraft.getInstance().getResourceManager().getResourceOrThrow(m).open()) {
            ka.u var1 = ka.u(var0);
            ai.U(c.pipeline());
            Z = var1;
            f = wa.s;
            h.U("Sahur", "Static mesh ready: {} triangles, resource generation {}", Z.triangles(), f);
         } catch (IOException var5) {
            throw new IllegalStateException("Sahur asset load failed; reload resources to retry", var5);
         }
      }
   }

   public static void P() {
      H();
      if (K != wa.s) {
         ai.U(N.pipeline());
         K = wa.s;
      }
   }

   public static void E(kj var0, Vec3 var1, Quaternionfc var2, float var3) {
      if (v()) {
         var0.M(c, var1, var2, var3, Z.vertices(), Z.colors());
         zj.K("Sahur triangles", Z.triangles());
      }
   }

   public static void S(PoseStack var0, hh var1, Matrix4f var2) {
      if (v()) {
         Matrix4f var3 = new Matrix4f(var0.last().pose()).mul(var2);
         kj.E(var1, c, var3, Z.vertices(), Z.colors());
         zj.K("Sahur triangles", Z.triangles());
      }
   }

   public static void w(PoseStack var0, hh var1, Matrix4f var2, float[] var3, int[] var4) {
      if (v()) {
         Matrix4f var5 = new Matrix4f(var0.last().pose()).mul(var2);
         kj.E(var1, c, var5, var3, var4);
         zj.K("Sahur triangles", var4.length / 3);
      }
   }

   public static void U(PoseStack var0, hh var1, Matrix4f var2, float[] var3, int[] var4, double var5) {
      if (v() && !(var5 <= 0.001)) {
         Matrix4f var7 = new Matrix4f(var0.last().pose()).mul(var2);
         VertexConsumer var8 = var1.g(N);

         for (int var9 = 0; var9 < var4.length; var9++) {
            int var10 = var9 * 5;
            var8.addVertex(var7, var3[var10], var3[var10 + 1], var3[var10 + 2]).setUv(var3[var10 + 3], var3[var10 + 4]).setColor(uh.F(var4[var9], var5));
         }

         zj.K("Sahur triangles", var4.length / 3);
      }
   }

   private k5() {
   }
}
