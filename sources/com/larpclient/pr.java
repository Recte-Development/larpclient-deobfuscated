package com.larpclient;

import com.mojang.renderpearl.api.pipeline.ColorTargetState;
import com.mojang.renderpearl.api.pipeline.CompareOp;
import com.mojang.renderpearl.api.pipeline.DepthStencilState;
import com.mojang.renderpearl.api.pipeline.PrimitiveTopology;
import com.mojang.renderpearl.api.pipeline.RenderPipeline;
import com.mojang.renderpearl.api.pipeline.RenderPipeline.Snippet;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.rendertype.RenderSetup;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;
import org.joml.Quaternionfc;

public final class pr {
   public static final String w = "Bitcoin";
   public static final double v = 0.6;
   public static final double y = 1.6;
   public static final int h = -552166;
   private static final String n = "models/pets/";
   private static final RenderPipeline d = RenderPipeline.builder(new Snippet[]{RenderPipelines.GUI_TEXTURED_SNIPPET})
      .withLocation(O("bitcoin_pet"))
      .withFragmentShader(O("particle/sprite"))
      .withPrimitiveTopology(PrimitiveTopology.TRIANGLES)
      .withDepthStencilState(new DepthStencilState(CompareOp.GREATER_THAN_OR_EQUAL, true))
      .withColorTargetState(ColorTargetState.DEFAULT)
      .withCull(true)
      .build();
   public static final List<pr.h> x = List.of(
      N("Bitcoin", "bitcoin", 0.6, -552166),
      N("Solana", "solana", 0.6, -6732289),
      N("Ethereum", "ethereum", 0.6, -10322198),
      N("Litecoin", "litecoin", 0.6, -13345379),
      N("Monero", "monero", 0.6, -39424)
   );
   private static zl.j Q;
   private static long z = -1L;
   private static long L = -1L;

   private static Identifier O(String var0) {
      return Identifier.fromNamespaceAndPath("larp-client", var0);
   }

   private static pr.h N(String var0, String var1, double var2, int var4) {
      return new pr.h(
         var0,
         var1,
         var2,
         var4,
         RenderType.create(
            "larp_" + var1 + "_pet", RenderSetup.builder(d).withTexture("Sampler0", O("models/pets/" + var1 + "/albedo.png")).createRenderSetup()
         ),
         var0 + " triangles"
      );
   }

   public static pr.h y(String var0) {
      for (pr.h var2 : x) {
         if (var2.style().equals(var0)) {
            return var2;
         }
      }

      return null;
   }

   public static boolean Y() {
      return Q != null && z == m5.k;
   }

   public static void g() {
      if (!Y()) {
         Q = null;
         ResourceManager var0 = Minecraft.getInstance().getResourceManager();

         try (InputStream var1 = var0.getResourceOrThrow(O("models/pets/bitcoin/mesh.bin")).open()) {
            for (pr.h var3 : x) {
               var0.getResourceOrThrow(O("models/pets/" + var3.folder() + "/albedo.png"));
            }

            zl.j var7 = zl.E(var1);
            ea.z(d);
            Q = var7;
            z = m5.k;
            mt.U("Pets", "Crypto coins ready: {} faces, {} triangles, resource generation {}", x.size(), Q.triangles(), z);
         } catch (IOException var6) {
            throw new IllegalStateException("Crypto coin asset load failed; reload resources to retry", var6);
         }
      }
   }

   public static boolean I(String var0) {
      if (Y()) {
         return true;
      }

      if (L == m5.k) {
         return false;
      }

      try {
         g();
         return true;
      } catch (RuntimeException var2) {
         L = m5.k;
         mt.P(var0, "Crypto coins unavailable; using fallback until resource reload", var2);
         return false;
      }
   }

   public static zl.j S() {
      return Y() ? Q : null;
   }

   public static void D(ki var0, pr.h var1, Matrix4f var2) {
      if (Y()) {
         var0.j(var1.type(), var2, Q.vertices(), Q.colors());
         kx.U(var1.profile(), Q.triangles());
      }
   }

   public static void T(ki var0, pr.h var1, Vec3 var2, Quaternionfc var3, float var4) {
      if (Y()) {
         var0.m(var1.type(), var2, var3, var4, Q.vertices(), Q.colors());
         kx.U(var1.profile(), Q.triangles());
      }
   }

   private pr() {
   }

   public record h(String style, String folder, double scale, int spark, RenderType type, String profile) {
   }
}
