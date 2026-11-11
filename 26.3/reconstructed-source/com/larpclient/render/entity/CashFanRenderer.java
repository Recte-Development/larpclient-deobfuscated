package com.larpclient.render.entity;

import com.larpclient.module.render.PlayerModelModule;
import com.larpclient.render.model.FingerMeshBuilder;
import com.larpclient.render.overlay.PostProcessing;
import com.larpclient.social.emote.EmoteAnimation;
import com.larpclient.social.emote.EmoteState;
import com.larpclient.util.misc.LarpLog;
import com.larpclient.util.misc.PerfProfiler;
import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.PoseStack.Pose;
import com.mojang.renderpearl.api.textures.FilterMode;
import java.io.IOException;
import java.io.InputStream;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.player.PlayerModel;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.client.renderer.rendertype.RenderSetup;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderSetup.OutlineProperty;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.entity.HumanoidArm;

public final class CashFanRenderer {
   private static final Identifier aTLAS_SOURCE = modId("textures/particles/cash.png");
   private static final Identifier aTLAS_TEXTURE_ID = modId("dynamic/cash_fan");
   public static final int dOWNSCALE_FACTOR = 4;
   private static DynamicTexture atlasTexture;
   private static long loadedGeneration = -1L;
   private static long failedGeneration = -1L;

   private CashFanRenderer() {
   }

   private static Identifier modId(String var0) {
      return Identifier.fromNamespaceAndPath("larp-client", var0);
   }

   public static boolean renderCashFans(PlayerModel var0, AvatarRenderState var1, PoseStack var2, SubmitNodeCollector var3, int var4) {
      EmoteState var5 = (EmoteState)var1;
      float var6 = var5.larp$emoteWeight();
      if (!(var6 <= 0.0F) && "cash_fans".equals(var5.larp$emoteProp())) {
         PlayerModelModule var7 = PlayerModelModule.getInstance();
         if (var7 != null && var7.onResourceReload(var1)) {
            return false;
         }

         float[] var8 = var5.larp$emoteSample();
         int var9 = EmoteAnimation.Part.PROP.ordinal() * 10;
         double var10 = valueOrDefault(var8[var9 + 7], 1.0) * var6;
         if (!ensureAtlas()) {
            return false;
         }

         if (var10 < 0.001) {
            return true;
         }

         double var12 = valueOrDefault(var8[var9 + 3], 0.0);
         double var14 = valueOrDefault(var8[var9 + 4], 0.0);
         double var16 = valueOrDefault(var8[var9 + 5], 0.0);
         double var18 = valueOrDefault(var8[var9 + 8], 0.0);

         for (HumanoidArm var23 : HumanoidArm.values()) {
            float[] var24 = FingerMeshBuilder.buildMesh(var23 == HumanoidArm.RIGHT, var12, var14, var16, var18, var10, var5.larp$emoteCycle());
            var2.pushPose();

            try {
               var0.translateToHand(var1, var23, var2);
               var3.submitCustomGeometry(var2, CashFanRenderer.CashFanRenderTypes.cashFan, (var2x, var3x) -> submitVertices(var24, var2x, var3x, var4));
            } finally {
               var2.popPose();
            }
         }

         PerfProfiler.setCounter("Cash fan bills", 14L);
         return true;
      } else {
         return false;
      }
   }

   private static double valueOrDefault(float var0, double var1) {
      return Float.isNaN(var0) ? var1 : var0;
   }

   private static void submitVertices(float[] var0, Pose var1, VertexConsumer var2, int var3) {
      for (byte var4 = 0; var4 < var0.length; var4 += 9) {
         int var5 = Math.round(Math.clamp(var0[var4 + 8], 0.0F, 1.0F) * 255.0F);
         var2.addVertex(var1, var0[var4], var0[var4 + 1], var0[var4 + 2])
            .setColor(0xFF000000 | var5 * 65793)
            .setUv(var0[var4 + 3], var0[var4 + 4])
            .setOverlay(OverlayTexture.NO_OVERLAY)
            .setLight(var3)
            .setNormal(var1, var0[var4 + 5], var0[var4 + 6], var0[var4 + 7]);
      }
   }

   private static boolean ensureAtlas() {
      if (failedGeneration == PostProcessing.reloadCount) {
         return false;
      }

      if (atlasTexture != null && loadedGeneration == PostProcessing.reloadCount) {
         return true;
      }

      try {
         loadAtlas();
         return true;
      } catch (IOException | RuntimeException var1) {
         failedGeneration = PostProcessing.reloadCount;
         atlasTexture = null;
         LarpLog.error("Emotes", "Cash fan atlas unavailable; Money Spread shows held items until resource reload", var1);
         return false;
      }
   }

   private static void loadAtlas() throws IOException {
      ResourceManager var0 = Minecraft.getInstance().getResourceManager();

      int[] var1;
      int var2;
      int var3;
      try (InputStream var4 = var0.getResourceOrThrow(aTLAS_SOURCE).open()) {
         NativeImage var5 = NativeImage.read(var4);

         try {
            var2 = var5.getWidth() / 4;
            var3 = var5.getHeight() / 4;
            if (var2 * 4 != var5.getWidth() || var3 * 4 != var5.getHeight()) {
               throw new IOException("Cash atlas size " + var5.getWidth() + "x" + var5.getHeight() + " is not a multiple of 4");
            }

            var1 = downscaleAtlas(var5.getPixels(), var5.getWidth(), var5.getHeight(), 4);
         } catch (Throwable var12) {
            if (var5 != null) {
               try {
                  var5.close();
               } catch (Throwable var9) {
                  var12.addSuppressed(var9);
               }
            }

            throw var12;
         }

         if (var5 != null) {
            var5.close();
         }
      }

      NativeImage var14 = new NativeImage(var2, var3, false);

      DynamicTexture var15;
      try {
         for (int var6 = 0; var6 < var1.length; var6++) {
            var14.setPixel(var6 % var2, var6 / var2, var1[var6]);
         }

         var15 = new DynamicTexture(() -> "LARP cash fan", var14);
      } catch (RuntimeException var11) {
         var14.close();
         throw var11;
      }

      try {
         Minecraft.getInstance().getTextureManager().register(aTLAS_TEXTURE_ID, var15);
      } catch (RuntimeException var10) {
         var15.close();
         throw var10;
      }

      atlasTexture = var15;
      loadedGeneration = PostProcessing.reloadCount;
      LarpLog.info("Emotes", "Cash fan atlas ready: {}x{}, resource generation {}", var2, var3, loadedGeneration);
   }

   public static int[] downscaleAtlas(int[] var0, int var1, int var2, int var3) {
      int var4 = var1 / var3;
      int var5 = var2 / var3;
      int[] var6 = new int[var4 * var5];

      for (int var7 = 0; var7 < var5; var7++) {
         for (int var8 = 0; var8 < var4; var8++) {
            long var9 = 0L;
            long var11 = 0L;
            long var13 = 0L;
            long var15 = 0L;

            for (int var17 = 0; var17 < var3; var17++) {
               for (int var18 = 0; var18 < var3; var18++) {
                  int var19 = var0[(var7 * var3 + var17) * var1 + var8 * var3 + var18];
                  int var20 = var19 >>> 24;
                  var9 += var20;
                  var11 += (var19 >> 16 & 0xFF) * var20;
                  var13 += (var19 >> 8 & 0xFF) * var20;
                  var15 += (var19 & 0xFF) * var20;
               }
            }

            if (var9 != 0L) {
               int var29 = (int)Math.round((double)var9 / (var3 * var3));
               var6[var7 * var4 + var8] = var29 << 24
                  | (int)Math.round((double)var11 / var9) << 16
                  | (int)Math.round((double)var13 / var9) << 8
                  | (int)Math.round((double)var15 / var9);
            }
         }
      }

      for (int var23 = 0; var23 < 2; var23++) {
         int[] var24 = (int[])var6.clone();

         for (int var25 = 0; var25 < var5; var25++) {
            for (int var10 = 0; var10 < var4; var10++) {
               if (var6[var25 * var4 + var10] >>> 24 == 0) {
                  long var26 = 0L;
                  long var27 = 0L;
                  long var28 = 0L;
                  int var30 = 0;

                  for (int var31 = -1; var31 <= 1; var31++) {
                     for (int var32 = -1; var32 <= 1; var32++) {
                        int var33 = var10 + var32;
                        int var21 = var25 + var31;
                        if (var33 >= 0 && var21 >= 0 && var33 < var4 && var21 < var5) {
                           int var22 = var6[var21 * var4 + var33];
                           if (var22 >>> 24 >= 128) {
                              var26 += var22 >> 16 & 0xFF;
                              var27 += var22 >> 8 & 0xFF;
                              var28 += var22 & 0xFF;
                              var30++;
                           }
                        }
                     }
                  }

                  if (var30 > 0) {
                     var24[var25 * var4 + var10] = 0xFF000000 | (int)(var26 / var30) << 16 | (int)(var27 / var30) << 8 | (int)(var28 / var30);
                  }
               }
            }
         }

         var6 = var24;
      }

      return var6;
   }

   private static final class CashFanRenderTypes {
      static final RenderType cashFan = RenderType.create(
         "larp_cash_fan",
         RenderSetup.builder(RenderPipelines.ENTITY_CUTOUT_CULL)
            .withTexture("Sampler0", CashFanRenderer.aTLAS_TEXTURE_ID, () -> RenderSystem.getSamplerCache().getClampToEdge(FilterMode.LINEAR))
            .useLightmap()
            .useOverlay()
            .affectsCrumbling()
            .setOutline(OutlineProperty.AFFECTS_OUTLINE)
            .createRenderSetup()
      );
   }
}
