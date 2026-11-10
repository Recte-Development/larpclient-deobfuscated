package com.larpclient;

import com.larpclient.module.PlayerModelModule;
import com.mojang.blaze3d.vertex.PoseStack;
import java.io.IOException;
import java.io.InputStream;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.player.PlayerModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.resources.Identifier;

public final class qn {
   private static final Identifier r = g("models/emotes/diamond_pickaxe/mesh.bin");
   private static final Identifier d = g("models/emotes/diamond_pickaxe/albedo.png");
   private static float[] W;
   private static long e = -1L;
   private static long c = -1L;

   private qn() {
   }

   private static Identifier g(String var0) {
      return Identifier.fromNamespaceAndPath("larp-client", var0);
   }

   public static boolean M(PlayerModel var0, AvatarRenderState var1, PoseStack var2, SubmitNodeCollector var3, int var4) {
      f0 var5 = (f0)var1;
      float var6 = var5.larp$emoteWeight();
      if (!(var6 <= 0.0F) && "crypto_rock".equals(var5.larp$emoteProp())) {
         PlayerModelModule var7 = PlayerModelModule.k();
         if (var7 != null && var7.m(var1)) {
            return false;
         }

         if (!m()) {
            return false;
         }

         float[] var8 = var5.larp$emoteSample();
         int var9 = bo.i.t.ordinal() * 10;
         double var10 = e(var8[var9 + 7], 1.0) * var6;
         if (var10 < 0.001) {
            return true;
         }

         var2.pushPose();

         try {
            var0.root().translateAndRotate(var2);
            wl.z(
               var2,
               e(var8[var9], 0.0),
               e(var8[var9 + 1], 0.0),
               e(var8[var9 + 2], 0.0),
               e(var8[var9 + 3], 0.0),
               e(var8[var9 + 4], 0.0),
               e(var8[var9 + 5], 0.0),
               var10
            );
            float[] var12 = W;
            var3.submitCustomGeometry(var2, RenderTypes.entitySolid(d), (var2x, var3x) -> hr.V(var12, var2x, var3x, var4));
            he.z("Pickaxe quads", var12.length / 8 / 4);
         } finally {
            var2.popPose();
         }

         return true;
      } else {
         return false;
      }
   }

   private static double e(float var0, double var1) {
      return Float.isNaN(var0) ? var1 : var0;
   }

   private static boolean m() {
      if (c == nl.q) {
         return false;
      }

      if (W != null && e == nl.q) {
         return true;
      }

      try (InputStream var0 = Minecraft.getInstance().getResourceManager().getResourceOrThrow(r).open()) {
         Minecraft.getInstance().getResourceManager().getResourceOrThrow(d);
         W = i1.b(qu.Z(var0));
         e = nl.q;
         _q.z("Emotes", "Pickaxe ready: {} quads, resource generation {}", W.length / 8 / 4, e);
         return true;
      } catch (IOException | RuntimeException var5) {
         c = nl.q;
         W = null;
         _q.o("Emotes", "Pickaxe model unavailable; Crypto Mining shows held items until resource reload", var5);
         return false;
      }
   }
}
