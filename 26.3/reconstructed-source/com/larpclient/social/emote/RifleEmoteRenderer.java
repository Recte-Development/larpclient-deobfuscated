package com.larpclient.social.emote;

import com.larpclient.module.render.PlayerModelModule;
import com.larpclient.render.model.PropMeshLoader;
import com.larpclient.render.model.RifleGeometry;
import com.larpclient.render.overlay.PostProcessing;
import com.larpclient.util.misc.LarpLog;
import com.larpclient.util.misc.PerfProfiler;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.PoseStack.Pose;
import java.io.IOException;
import java.io.InputStream;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.player.PlayerModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.HumanoidArm;
import org.joml.Matrix3f;
import org.joml.Vector3f;

public final class RifleEmoteRenderer {
   private static final Identifier mESH_ID = id("models/emotes/ak47/mesh.bin");
   private static final Identifier tEXTURE_ID = id("models/emotes/ak47/albedo.png");
   private static final int fULL_BRIGHT = 15728880;
   private static float[] vertices;
   private static long loadedGeneration = -1L;
   private static long failedGeneration = -1L;

   private RifleEmoteRenderer() {
   }

   private static Identifier id(String var0) {
      return Identifier.fromNamespaceAndPath("larp-client", var0);
   }

   public static boolean render(PlayerModel var0, AvatarRenderState var1, PoseStack var2, SubmitNodeCollector var3, int var4) {
      EmoteState var5 = (EmoteState)var1;
      float var6 = var5.larp$emoteWeight();
      if (!(var6 <= 0.0F) && "ak47".equals(var5.larp$emoteProp())) {
         PlayerModelModule var7 = PlayerModelModule.getInstance();
         if (var7 != null && var7.onResourceReload(var1)) {
            return false;
         }

         if (!ensureLoaded()) {
            return false;
         }

         float[] var8 = var5.larp$emoteSample();
         int var9 = EmoteAnimation.Part.PROP.ordinal() * 10;
         double var10 = valueOrDefault(var8[var9 + 7], 1.0) * var6;
         if (var10 < 0.001) {
            return true;
         }

         int var12 = valueOrDefault(var8[var9 + 9], 0.0) >= 0.5 ? 1 : 0;
         var2.pushPose();

         try {
            if (var12 != 0) {
               var0.translateToHand(var1, HumanoidArm.RIGHT, var2);
            } else {
               var0.root().translateAndRotate(var2);
               var0.body.translateAndRotate(var2);
            }

            RifleGeometry.applyHeldTransform(
               var2, (boolean)var12, valueOrDefault(var8[var9 + 3], 0.0), valueOrDefault(var8[var9 + 4], 0.0), valueOrDefault(var8[var9 + 5], 0.0), var10
            );
            float[] var13 = vertices;
            var3.submitCustomGeometry(var2, RenderTypes.entitySolid(tEXTURE_ID), (var2x, var3x) -> submitQuads(var13, var2x, var3x, var4));
            PerfProfiler.setCounter("Rifle quads", var13.length / 8 / 4);
            if (var12 != 0 && var6 >= 0.999F) {
               renderMuzzleFlash(var2, var3, var4, var5.larp$emoteCycle());
            }
         } finally {
            var2.popPose();
         }

         return true;
      } else {
         return false;
      }
   }

   private static void renderMuzzleFlash(PoseStack var0, SubmitNodeCollector var1, int var2, float var3) {
      float[] var4 = RifleGeometry.buildMuzzleEffects(var3);
      if (var4.length > 0) {
         var1.order(1).submitCustomGeometry(var0, RenderTypes.eyes(tEXTURE_ID), (var1x, var2x) -> submitQuads(var4, var1x, var2x, 15728880));
      }

      Vector3f var5 = new Matrix3f(var0.last().normal()).invert().transform(new Vector3f(0.0F, -1.0F, 0.0F));
      if (var5.lengthSquared() > 1.0E-8F) {
         float[] var6 = RifleGeometry.buildShells(var3, var5.normalize());
         if (var6.length > 0) {
            var1.submitCustomGeometry(var0, RenderTypes.entitySolid(tEXTURE_ID), (var2x, var3x) -> submitQuads(var6, var2x, var3x, var2));
         }
      }
   }

   private static double valueOrDefault(float var0, double var1) {
      return Float.isNaN(var0) ? var1 : var0;
   }

   static void submitQuads(float[] var0, Pose var1, VertexConsumer var2, int var3) {
      for (byte var4 = 0; var4 < var0.length; var4 += 8) {
         var2.addVertex(var1, var0[var4], var0[var4 + 1], var0[var4 + 2])
            .setColor(-1)
            .setUv(var0[var4 + 3], var0[var4 + 4])
            .setOverlay(OverlayTexture.NO_OVERLAY)
            .setLight(var3)
            .setNormal(var1, var0[var4 + 5], var0[var4 + 6], var0[var4 + 7]);
      }
   }

   private static boolean ensureLoaded() {
      if (failedGeneration == PostProcessing.reloadCount) {
         return false;
      }

      if (vertices != null && loadedGeneration == PostProcessing.reloadCount) {
         return true;
      }

      try (InputStream var0 = Minecraft.getInstance().getResourceManager().getResourceOrThrow(mESH_ID).open()) {
         Minecraft.getInstance().getResourceManager().getResourceOrThrow(tEXTURE_ID);
         vertices = RifleGeometry.buildMesh(PropMeshLoader.load(var0));
         loadedGeneration = PostProcessing.reloadCount;
         LarpLog.info("Emotes", "Rifle ready: {} quads, resource generation {}", vertices.length / 8 / 4, loadedGeneration);
         return true;
      } catch (IOException | RuntimeException var5) {
         failedGeneration = PostProcessing.reloadCount;
         vertices = null;
         LarpLog.error("Emotes", "Rifle model unavailable; AK Spray shows held items until resource reload", var5);
         return false;
      }
   }
}
