package com.larpclient;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.renderpearl.api.pipeline.BlendFunction;
import com.mojang.renderpearl.api.pipeline.ColorTargetState;
import com.mojang.renderpearl.api.pipeline.CompareOp;
import com.mojang.renderpearl.api.pipeline.DepthStencilState;
import com.mojang.renderpearl.api.pipeline.PrimitiveTopology;
import com.mojang.renderpearl.api.pipeline.RenderPipeline;
import com.mojang.renderpearl.api.pipeline.RenderPipeline.Snippet;
import java.io.IOException;
import java.io.InputStream;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.rendertype.RenderSetup;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;

public final class xy {
   private static final String p = "models/halloween/pumpkin/";
   private static final RenderType D = Q(false);
   private static final RenderType y = Q(true);
   private static xr.h H;
   private static long N = -1L;

   private static Identifier w(String var0) {
      return Identifier.fromNamespaceAndPath("larp-client", var0);
   }

   private static RenderType Q(boolean var0) {
      RenderPipeline var1 = RenderPipeline.builder(new Snippet[]{RenderPipelines.GUI_TEXTURED_SNIPPET})
         .withLocation(w(var0 ? "pumpkin_emission" : "pumpkin"))
         .withFragmentShader(w("particle/sprite"))
         .withShaderDefine("DITHER_FADE")
         .withPrimitiveTopology(PrimitiveTopology.TRIANGLES)
         .withDepthStencilState(new DepthStencilState(CompareOp.GREATER_THAN_OR_EQUAL, !var0))
         .withColorTargetState(var0 ? new ColorTargetState(BlendFunction.LIGHTNING) : ColorTargetState.DEFAULT)
         .withCull(true)
         .build();
      return RenderType.create(
         var0 ? "larp_pumpkin_emission" : "larp_pumpkin",
         RenderSetup.builder(var1).withTexture("Sampler0", w("models/halloween/pumpkin/" + (var0 ? "emission.png" : "albedo.png"))).createRenderSetup()
      );
   }

   public static void j() {
      if (H == null || N != jk.v) {
         H = null;

         try (InputStream var0 = Minecraft.getInstance().getResourceManager().getResourceOrThrow(w("models/halloween/pumpkin/mesh.bin")).open()) {
            ResourceManager var1 = Minecraft.getInstance().getResourceManager();
            var1.getResourceOrThrow(w("models/halloween/pumpkin/albedo.png"));
            var1.getResourceOrThrow(w("models/halloween/pumpkin/emission.png"));
            xr.h var2 = xr.b(var0);
            e4.U(D.pipeline());
            e4.U(y.pipeline());
            H = var2;
            N = jk.v;
            r4.g("Halloween", "Intact pumpkin ready: {} original triangles, {} static mesh groups, generation={}", var2.triangles(), var2.chunks().size(), N);
         } catch (IOException var5) {
            throw new IllegalStateException("Halloween pumpkin asset failed; existing cosmetic quarantine retries after resource reload", var5);
         }
      }
   }

   public static void c(PoseStack var0, y2 var1, ri.z var2, Vec3 var3, boolean var4) {
      if (H != null && N == jk.v && var2.pumpkin() != null) {
         if (!var4) {
            x(var0, var1, var2, var3, false);
         }

         x(var0, var1, var2, var3, true);
      }
   }

   private static void x(PoseStack var0, y2 var1, ri.z var2, Vec3 var3, boolean var4) {
      VertexConsumer var5 = var1.a(var4 ? y : D);
      Matrix4f var6 = new Matrix4f(var0.last().pose()).mul(ri.J(var2, var3));

      for (int var7 = 0; var7 < H.chunks().size(); var7++) {
         xr.n var8 = H.chunks().get(var7);
         float[] var9 = var8.vertices();
         int[] var10 = var8.colors();

         for (int var11 = 0; var11 < var10.length; var11++) {
            int var12 = var11 * 5;
            int var13 = oh.i(var4 ? -9416661 : var10[var11], var2.pumpkin().alpha());
            var5.addVertex(var6, var9[var12], var9[var12 + 1], var9[var12 + 2]).setUv(var9[var12 + 3], var9[var12 + 4]).setColor(var13);
         }

         h_.A(var4 ? "Halloween pumpkin emission triangles" : "Halloween pumpkin triangles", var8.triangles());
      }
   }

   private xy() {
   }
}
