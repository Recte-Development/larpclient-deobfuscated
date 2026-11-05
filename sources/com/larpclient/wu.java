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

public final class wu {
   private static final String y = "models/halloween/pumpkin/";
   private static final RenderType t = s(false);
   private static final RenderType Z = s(true);
   private static zi.s r;
   private static long z = -1L;

   private static Identifier q(String var0) {
      return Identifier.fromNamespaceAndPath("larp-client", var0);
   }

   private static RenderType s(boolean var0) {
      RenderPipeline var1 = RenderPipeline.builder(new Snippet[]{RenderPipelines.GUI_TEXTURED_SNIPPET})
         .withLocation(q(var0 ? "pumpkin_emission" : "pumpkin"))
         .withFragmentShader(q("particle/sprite"))
         .withShaderDefine("DITHER_FADE")
         .withPrimitiveTopology(PrimitiveTopology.TRIANGLES)
         .withDepthStencilState(new DepthStencilState(CompareOp.GREATER_THAN_OR_EQUAL, !var0))
         .withColorTargetState(var0 ? new ColorTargetState(BlendFunction.LIGHTNING) : ColorTargetState.DEFAULT)
         .withCull(true)
         .build();
      return RenderType.create(
         var0 ? "larp_pumpkin_emission" : "larp_pumpkin",
         RenderSetup.builder(var1).withTexture("Sampler0", q("models/halloween/pumpkin/" + (var0 ? "emission.png" : "albedo.png"))).createRenderSetup()
      );
   }

   public static void H() {
      if (r == null || z != m5.k) {
         r = null;

         try (InputStream var0 = Minecraft.getInstance().getResourceManager().getResourceOrThrow(q("models/halloween/pumpkin/mesh.bin")).open()) {
            ResourceManager var1 = Minecraft.getInstance().getResourceManager();
            var1.getResourceOrThrow(q("models/halloween/pumpkin/albedo.png"));
            var1.getResourceOrThrow(q("models/halloween/pumpkin/emission.png"));
            zi.s var2 = zi.Y(var0);
            ea.z(t.pipeline());
            ea.z(Z.pipeline());
            r = var2;
            z = m5.k;
            mt.U("Halloween", "Intact pumpkin ready: {} original triangles, {} static mesh groups, generation={}", var2.triangles(), var2.chunks().size(), z);
         } catch (IOException var5) {
            throw new IllegalStateException("Halloween pumpkin asset failed; existing cosmetic quarantine retries after resource reload", var5);
         }
      }
   }

   public static void C(PoseStack var0, xh var1, ov.q var2, Vec3 var3, boolean var4) {
      if (r != null && z == m5.k && var2.pumpkin() != null) {
         if (!var4) {
            K(var0, var1, var2, var3, false);
         }

         K(var0, var1, var2, var3, true);
      }
   }

   private static void K(PoseStack var0, xh var1, ov.q var2, Vec3 var3, boolean var4) {
      VertexConsumer var5 = var1.U(var4 ? Z : t);
      Matrix4f var6 = new Matrix4f(var0.last().pose()).mul(ov.W(var2, var3));

      for (int var7 = 0; var7 < r.chunks().size(); var7++) {
         zi.c var8 = r.chunks().get(var7);
         float[] var9 = var8.vertices();
         int[] var10 = var8.colors();

         for (int var11 = 0; var11 < var10.length; var11++) {
            int var12 = var11 * 5;
            int var13 = _3.A(var4 ? -9416661 : var10[var11], var2.pumpkin().alpha());
            var5.addVertex(var6, var9[var12], var9[var12 + 1], var9[var12 + 2]).setUv(var9[var12 + 3], var9[var12 + 4]).setColor(var13);
         }

         kx.U(var4 ? "Halloween pumpkin emission triangles" : "Halloween pumpkin triangles", var8.triangles());
      }
   }

   private wu() {
   }
}
