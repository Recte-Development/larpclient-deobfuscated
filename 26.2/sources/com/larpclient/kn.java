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

public final class kn {
   private static final Identifier W = v("models/emotes/diamond_pickaxe/mesh.bin");
   private static final Identifier E = v("models/emotes/diamond_pickaxe/albedo.png");
   private static float[] Z;
   private static long B = -1L;
   private static long U = -1L;

   private kn() {
   }

   private static Identifier v(String var0) {
      return Identifier.fromNamespaceAndPath("larp-client", var0);
   }

   public static boolean W(PlayerModel var0, AvatarRenderState var1, PoseStack var2, SubmitNodeCollector var3, int var4) {
      gn var5 = (gn)var1;
      float var6 = var5.larp$emoteWeight();
      if (!(var6 <= 0.0F) && "crypto_rock".equals(var5.larp$emoteProp())) {
         PlayerModelModule var7 = PlayerModelModule.E();
         if (var7 != null && var7.H(var1)) {
            return false;
         }

         if (!J()) {
            return false;
         }

         float[] var8 = var5.larp$emoteSample();
         int var9 = cx.q.V.ordinal() * 10;
         double var10 = Z(var8[var9 + 7], 1.0) * var6;
         if (var10 < 0.001) {
            return true;
         }

         var2.pushPose();

         try {
            var0.root().translateAndRotate(var2);
            k.m(
               var2,
               Z(var8[var9], 0.0),
               Z(var8[var9 + 1], 0.0),
               Z(var8[var9 + 2], 0.0),
               Z(var8[var9 + 3], 0.0),
               Z(var8[var9 + 4], 0.0),
               Z(var8[var9 + 5], 0.0),
               var10
            );
            float[] var12 = Z;
            var3.submitCustomGeometry(var2, RenderTypes.entitySolid(E), (var2x, var3x) -> pb.d(var12, var2x, var3x, var4));
            zj.s("Pickaxe quads", var12.length / 8 / 4);
         } finally {
            var2.popPose();
         }

         return true;
      } else {
         return false;
      }
   }

   private static double Z(float var0, double var1) {
      return Float.isNaN(var0) ? var1 : var0;
   }

   private static boolean J() {
      if (U == wa.s) {
         return false;
      }

      if (Z != null && B == wa.s) {
         return true;
      }

      try (InputStream var0 = Minecraft.getInstance().getResourceManager().getResourceOrThrow(W).open()) {
         Minecraft.getInstance().getResourceManager().getResourceOrThrow(E);
         Z = kd.t(s5.K(var0));
         B = wa.s;
         h.A("Emotes", "Pickaxe ready: {} quads, resource generation {}", Z.length / 8 / 4, B);
         return true;
      } catch (IOException | RuntimeException var5) {
         U = wa.s;
         Z = null;
         h.m("Emotes", "Pickaxe model unavailable; Crypto Mining shows held items until resource reload", var5);
         return false;
      }
   }
}
