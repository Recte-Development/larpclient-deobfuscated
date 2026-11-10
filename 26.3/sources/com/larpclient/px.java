package com.larpclient;

import com.mojang.blaze3d.vertex.PoseStack;
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
import org.joml.Matrix4f;

public final class px {
   public static final String q = "Banana";
   private static final Identifier A = Identifier.fromNamespaceAndPath("larp-client", "models/player/banana/mesh.bin");
   private static final RenderType m = RenderType.create(
      "larp_banana_player",
      RenderSetup.builder(
            RenderPipeline.builder(new Snippet[]{RenderPipelines.GUI_TEXTURED_SNIPPET})
               .withLocation(Identifier.fromNamespaceAndPath("larp-client", "banana_player"))
               .withFragmentShader(Identifier.fromNamespaceAndPath("larp-client", "particle/sprite"))
               .withPrimitiveTopology(PrimitiveTopology.TRIANGLES)
               .withDepthStencilState(new DepthStencilState(CompareOp.GREATER_THAN_OR_EQUAL, true))
               .withColorTargetState(ColorTargetState.DEFAULT)
               .withCull(true)
               .build()
         )
         .withTexture("Sampler0", Identifier.fromNamespaceAndPath("larp-client", "models/player/banana/albedo.png"))
         .createRenderSetup()
   );
   private static b2.m i;
   private static long C = -1L;

   public static boolean I() {
      return i != null && C == nl.q;
   }

   public static b2.m O() {
      if (!I()) {
         throw new IllegalStateException("Banana asset is not prepared");
      } else {
         return i;
      }
   }

   public static void R() {
      if (!I()) {
         i = null;

         try (InputStream var0 = Minecraft.getInstance().getResourceManager().getResourceOrThrow(A).open()) {
            b2.m var1 = b2.z(var0);
            se.j(m.pipeline());
            i = var1;
            C = nl.q;
            _q.c("Banana", "Mesh ready: {} triangles, {} vertices, {} bones, resource generation {}", i.triangles(), i.vertices(), 11, C);
         } catch (IOException var5) {
            throw new IllegalStateException("Banana asset load failed; reload resources to retry", var5);
         }
      }
   }

   public static void T(PoseStack var0, wh var1, Matrix4f var2, b2 var3) {
      if (I()) {
         Matrix4f var4 = new Matrix4f(var0.last().pose()).mul(var2);
         fz.E(var1, m, var4, var3.H(), var3.o());
         he.Z("Banana triangles", var3.o().length / 3);
      }
   }

   private px() {
   }
}
