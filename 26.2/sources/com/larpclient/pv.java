package com.larpclient;

import com.mojang.blaze3d.PrimitiveTopology;
import com.mojang.blaze3d.pipeline.ColorTargetState;
import com.mojang.blaze3d.pipeline.DepthStencilState;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.pipeline.RenderPipeline.Snippet;
import com.mojang.blaze3d.platform.CompareOp;
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

public final class pv {
   public static final String E = "Bitcoin";
   public static final double O = 0.6;
   public static final double I = 1.6;
   public static final int L = -552166;
   private static final String Y = "models/pets/";
   private static final RenderPipeline r = RenderPipeline.builder(new Snippet[]{RenderPipelines.GUI_TEXTURED_SNIPPET})
      .withLocation(h("bitcoin_pet"))
      .withFragmentShader(h("particle/sprite"))
      .withPrimitiveTopology(PrimitiveTopology.TRIANGLES)
      .withDepthStencilState(new DepthStencilState(CompareOp.GREATER_THAN_OR_EQUAL, true))
      .withColorTargetState(ColorTargetState.DEFAULT)
      .withCull(true)
      .build();
   public static final List<pv.p> o = List.of(
      a("Bitcoin", "bitcoin", 0.6, -552166),
      a("Solana", "solana", 0.6, -6732289),
      a("Ethereum", "ethereum", 0.6, -10322198),
      a("Litecoin", "litecoin", 0.6, -13345379),
      a("Monero", "monero", 0.6, -39424)
   );
   private static co.g a;
   private static long v = -1L;
   private static long j = -1L;

   private static Identifier h(String var0) {
      return Identifier.fromNamespaceAndPath("larp-client", var0);
   }

   private static pv.p a(String var0, String var1, double var2, int var4) {
      return new pv.p(
         var0,
         var1,
         var2,
         var4,
         RenderType.create(
            "larp_" + var1 + "_pet", RenderSetup.builder(r).withTexture("Sampler0", h("models/pets/" + var1 + "/albedo.png")).createRenderSetup()
         ),
         var0 + " triangles"
      );
   }

   public static pv.p P(String var0) {
      for (pv.p var2 : o) {
         if (var2.style().equals(var0)) {
            return var2;
         }
      }

      return null;
   }

   public static boolean t() {
      return a != null && v == wa.s;
   }

   public static void J() {
      if (!t()) {
         a = null;
         ResourceManager var0 = Minecraft.getInstance().getResourceManager();

         try (InputStream var1 = var0.getResourceOrThrow(h("models/pets/bitcoin/mesh.bin")).open()) {
            for (pv.p var3 : o) {
               var0.getResourceOrThrow(h("models/pets/" + var3.folder() + "/albedo.png"));
            }

            co.g var7 = co.L(var1);
            ai.U(r);
            a = var7;
            v = wa.s;
            h.U("Pets", "Crypto coins ready: {} faces, {} triangles, resource generation {}", o.size(), a.triangles(), v);
         } catch (IOException var6) {
            throw new IllegalStateException("Crypto coin asset load failed; reload resources to retry", var6);
         }
      }
   }

   public static boolean T(String var0) {
      if (t()) {
         return true;
      }

      if (j == wa.s) {
         return false;
      }

      try {
         J();
         return true;
      } catch (RuntimeException var2) {
         j = wa.s;
         h.m(var0, "Crypto coins unavailable; using fallback until resource reload", var2);
         return false;
      }
   }

   public static co.g v() {
      return t() ? a : null;
   }

   public static void N(kj var0, pv.p var1, Matrix4f var2) {
      if (t()) {
         var0.M(var1.type(), var2, a.vertices(), a.colors());
         zj.K(var1.profile(), a.triangles());
      }
   }

   public static void h(kj var0, pv.p var1, Vec3 var2, Quaternionfc var3, float var4) {
      if (t()) {
         var0.M(var1.type(), var2, var3, var4, a.vertices(), a.colors());
         zj.K(var1.profile(), a.triangles());
      }
   }

   private pv() {
   }

   public record p(String style, String folder, double scale, int spark, RenderType type, String profile) {
   }
}
