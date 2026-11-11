package com.larpclient.social.emote;

import com.larpclient.module.render.PlayerModelModule;
import com.larpclient.render.model.PropMeshLoader;
import com.larpclient.render.model.RifleGeometry;
import com.larpclient.render.overlay.PostProcessing;
import com.larpclient.render.particle.CryptoRockEffects;
import com.larpclient.util.misc.LarpLog;
import com.larpclient.util.misc.PerfProfiler;
import com.mojang.blaze3d.vertex.PoseStack;
import java.io.IOException;
import java.io.InputStream;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.player.PlayerModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.resources.Identifier;

public final class PickaxeEmoteRenderer {
   private static final Identifier meshId = modId("models/emotes/diamond_pickaxe/mesh.bin");
   private static final Identifier textureId = modId("models/emotes/diamond_pickaxe/albedo.png");
   private static float[] quads;
   private static long loadedGeneration = -1L;
   private static long failedGeneration = -1L;

   private PickaxeEmoteRenderer() {
   }

   private static Identifier modId(String var0) {
      return Identifier.fromNamespaceAndPath("larp-client", var0);
   }

   public static boolean renderPickaxe(PlayerModel var0, AvatarRenderState var1, PoseStack var2, SubmitNodeCollector var3, int var4) {
      EmoteState var5 = (EmoteState)var1;
      float var6 = var5.larp$emoteWeight();
      if (!(var6 <= 0.0F) && "crypto_rock".equals(var5.larp$emoteProp())) {
         PlayerModelModule var7 = PlayerModelModule.getInstance();
         if (var7 != null && var7.onResourceReload(var1)) {
            return false;
         }

         if (!ensureLoaded()) {
            return false;
         }

         float[] var8 = var5.larp$emoteSample();
         int var9 = EmoteAnimation.Part.TOOL.ordinal() * 10;
         double var10 = valueOrDefault(var8[var9 + 7], 1.0) * var6;
         if (var10 < 0.001) {
            return true;
         }

         var2.pushPose();

         try {
            var0.root().translateAndRotate(var2);
            CryptoRockEffects.applyTransform(
               var2,
               valueOrDefault(var8[var9], 0.0),
               valueOrDefault(var8[var9 + 1], 0.0),
               valueOrDefault(var8[var9 + 2], 0.0),
               valueOrDefault(var8[var9 + 3], 0.0),
               valueOrDefault(var8[var9 + 4], 0.0),
               valueOrDefault(var8[var9 + 5], 0.0),
               var10
            );
            float[] var12 = quads;
            var3.submitCustomGeometry(var2, RenderTypes.entitySolid(textureId), (var2x, var3x) -> RifleEmoteRenderer.submitQuads(var12, var2x, var3x, var4));
            PerfProfiler.setCounter("Pickaxe quads", var12.length / 8 / 4);
         } finally {
            var2.popPose();
         }

         return true;
      } else {
         return false;
      }
   }

   private static double valueOrDefault(float var0, double var1) {
      return Float.isNaN(var0) ? var1 : var0;
   }

   private static boolean ensureLoaded() {
      if (failedGeneration == PostProcessing.reloadCount) {
         return false;
      }

      if (quads != null && loadedGeneration == PostProcessing.reloadCount) {
         return true;
      }

      try (InputStream var0 = Minecraft.getInstance().getResourceManager().getResourceOrThrow(meshId).open()) {
         Minecraft.getInstance().getResourceManager().getResourceOrThrow(textureId);
         quads = RifleGeometry.buildMesh(PropMeshLoader.load(var0));
         loadedGeneration = PostProcessing.reloadCount;
         LarpLog.info("Emotes", "Pickaxe ready: {} quads, resource generation {}", quads.length / 8 / 4, loadedGeneration);
         return true;
      } catch (IOException | RuntimeException var5) {
         failedGeneration = PostProcessing.reloadCount;
         quads = null;
         LarpLog.error("Emotes", "Pickaxe model unavailable; Crypto Mining shows held items until resource reload", var5);
         return false;
      }
   }
}
