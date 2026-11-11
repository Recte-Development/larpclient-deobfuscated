package com.larpclient.render.overlay;

import com.larpclient.util.data.SequenceMarker;
import com.larpclient.util.misc.LarpLog;
import com.larpclient.util.misc.PerfProfiler;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.renderpearl.api.textures.GpuTexture;
import com.mojang.renderpearl.api.textures.GpuTextureView;
import net.minecraft.client.Minecraft;

public final class SceneCapture {
   private static final SequenceMarker captureState = new SequenceMarker();
   private static GpuTexture colorTexture;
   private static GpuTexture depthTexture;
   private static GpuTextureView colorView;
   private static GpuTextureView depthView;
   private static GpuTexture lastColorSource;
   private static GpuTexture lastDepthSource;
   private static GpuTexture preTranslucentDepth;
   private static GpuTextureView preTranslucentDepthView;
   private static boolean colorRequested;
   private static boolean depthRequested;
   private static boolean colorFailed;
   private static boolean depthFailed;
   private static boolean preDepthCaptured;
   private static boolean preDepthFailed;
   private static int lastGeneration = -1;
   private static final long iDLE_NANOS = 3000000000L;
   private static long lastColorUseNanos;
   private static long lastDepthUseNanos;

   public static void beginFrame() {
      if (lastGeneration != PostProcessing.reloadCount) {
         lastGeneration = PostProcessing.reloadCount;
         preDepthFailed = (boolean)0;
         depthFailed = (boolean)0;
         colorFailed = (boolean)0;

         try {
            releaseAll();
         } catch (RuntimeException var1) {
            preDepthFailed = (boolean)1;
            depthFailed = (boolean)1;
            colorFailed = (boolean)1;
            LarpLog.error("Scene resources", "Generation cleanup failed; geometric materials retained until resource reload", var1);
         }
      }

      captureState.startEpoch();
      preDepthCaptured = (boolean)0;
      depthRequested = (boolean)0;
      colorRequested = (boolean)0;
   }

   public static void capturePreTranslucentDepth() {
      if (!preDepthFailed) {
         try {
            GpuTexture var0 = Minecraft.getInstance().gameRenderer.mainRenderTarget().getDepthTexture();
            if (var0 == null) {
               return;
            }

            if (!isSameShape(preTranslucentDepth, var0)) {
               releasePreTranslucentDepth();
               preTranslucentDepth = allocateTexture("Larp depth before translucent terrain", var0);
               preTranslucentDepthView = RenderSystem.getDevice().createTextureView(preTranslucentDepth);
            }

            copyTexture(var0, preTranslucentDepth);
            preDepthCaptured = (boolean)1;
            PerfProfiler.addCounter("Scene depth copies", 1L);
         } catch (RuntimeException var3) {
            preDepthFailed = (boolean)1;

            try {
               releasePreTranslucentDepth();
            } catch (RuntimeException var2) {
               var3.addSuppressed(var2);
            }

            LarpLog.error("Scene resources", "Depth before translucent terrain failed; local glow stays hidden behind glass until resource reload", var3);
         }
      }
   }

   public static GpuTextureView beginFrame() {
      return preDepthCaptured ? preTranslucentDepthView : null;
   }

   public static void invalidateColor() {
      captureState.incrementPrimary();
   }

   public static void invalidateDepth() {
      captureState.incrementSecondary();
   }

   public static GpuTextureView getSceneColor() {
      colorRequested = (boolean)1;
      if (colorFailed) {
         return null;
      }

      try {
         GpuTexture var0 = Minecraft.getInstance().gameRenderer.mainRenderTarget().getColorTexture();
         if (var0 == null) {
            return null;
         }

         if (!isSameShape(colorTexture, var0)) {
            releaseColor();
            colorTexture = allocateTexture("Larp shared scene color", var0);
            colorView = RenderSystem.getDevice().createTextureView(colorTexture);
            captureState.incrementPrimary();
         }

         if (var0 != lastColorSource) {
            lastColorSource = var0;
            captureState.incrementPrimary();
         }

         if (!captureState.isPrimaryMarked()) {
            copyTexture(var0, colorTexture);
            captureState.markPrimary();
            PerfProfiler.addCounter("Scene color copies", 1L);
         }

         return colorView;
      } catch (RuntimeException var3) {
         colorFailed = (boolean)1;

         try {
            releaseColor();
         } catch (RuntimeException var2) {
            var3.addSuppressed(var2);
         }

         LarpLog.error("Scene resources", "Color capture failed; geometric materials retained until resource reload", var3);
         return null;
      }
   }

   public static GpuTextureView getSceneDepth() {
      depthRequested = (boolean)1;
      if (depthFailed) {
         return null;
      }

      try {
         GpuTexture var0 = Minecraft.getInstance().gameRenderer.mainRenderTarget().getDepthTexture();
         if (var0 == null) {
            return null;
         }

         if (!isSameShape(depthTexture, var0)) {
            releaseDepth();
            depthTexture = allocateTexture("Larp shared scene depth", var0);
            depthView = RenderSystem.getDevice().createTextureView(depthTexture);
            captureState.incrementSecondary();
         }

         if (var0 != lastDepthSource) {
            lastDepthSource = var0;
            captureState.incrementSecondary();
         }

         if (!captureState.isSecondaryMarked()) {
            copyTexture(var0, depthTexture);
            captureState.markSecondary();
            PerfProfiler.addCounter("Scene depth copies", 1L);
         }

         return depthView;
      } catch (RuntimeException var3) {
         depthFailed = (boolean)1;

         try {
            releaseDepth();
         } catch (RuntimeException var2) {
            var3.addSuppressed(var2);
         }

         LarpLog.error("Scene resources", "Depth capture failed; hard-depth geometry retained until resource reload", var3);
         return null;
      }
   }

   private static boolean isSameShape(GpuTexture var0, GpuTexture var1) {
      return var0 != null && var0.getWidth(0) == var1.getWidth(0) && var0.getHeight(0) == var1.getHeight(0) && var0.getFormat() == var1.getFormat();
   }

   private static GpuTexture allocateTexture(String var0, GpuTexture var1) {
      GpuTexture var2 = RenderSystem.getDevice()
         .createTexture(var0, 5 | (var1.getFormat().hasDepthAspect() ? 8 : 0), var1.getFormat(), var1.getWidth(0), var1.getHeight(0), 1, 1);
      LarpLog.debug("Scene resources", "Allocated {} {}x{}, {} bytes before alignment", var0, var1.getWidth(0), var1.getHeight(0), estimateBytes(var2));
      return var2;
   }

   private static void copyTexture(GpuTexture var0, GpuTexture var1) {
      if (var0 == var1) {
         throw new IllegalStateException("Scene capture aliases output");
      }

      RenderSystem.getDevice().createCommandEncoder().copyTextureToTexture(var0, var1, 0, 0, 0, 0, 0, var0.getWidth(0), var0.getHeight(0));
   }

   private static long estimateBytes(GpuTexture var0) {
      return var0 == null ? 0L : (long)var0.getWidth(0) * var0.getHeight(0) * var0.getFormat().blockSize();
   }

   public static long totalBytes() {
      return estimateBytes(colorTexture) + estimateBytes(depthTexture) + estimateBytes(preTranslucentDepth);
   }

   public static void endFrame() {
      long var0 = colorTexture == null && depthTexture == null ? 0L : System.nanoTime();
      if (colorRequested) {
         lastColorUseNanos = var0;
      }

      if (depthRequested) {
         lastDepthUseNanos = var0;
      }

      try {
         if (!colorRequested && var0 - lastColorUseNanos > 3000000000L) {
            releaseColor();
         }
      } finally {
         try {
            if (!depthRequested && var0 - lastDepthUseNanos > 3000000000L) {
               releaseDepth();
            }
         } finally {
            if (!preDepthCaptured) {
               releasePreTranslucentDepth();
            }
         }
      }

      PerfProfiler.setCounter("Scene attachment bytes (estimate)", totalBytes());
   }

   private static void releaseColor() {
      GpuTextureView var0 = colorView;
      GpuTexture var1 = colorTexture;
      colorView = null;
      colorTexture = null;
      lastColorSource = null;

      try {
         if (var0 != null) {
            var0.close();
         }
      } finally {
         if (var1 != null) {
            var1.close();
         }
      }

      captureState.incrementPrimary();
   }

   private static void releaseDepth() {
      GpuTextureView var0 = depthView;
      GpuTexture var1 = depthTexture;
      depthView = null;
      depthTexture = null;
      lastDepthSource = null;

      try {
         if (var0 != null) {
            var0.close();
         }
      } finally {
         if (var1 != null) {
            var1.close();
         }
      }

      captureState.incrementSecondary();
   }

   private static void releasePreTranslucentDepth() {
      GpuTextureView var0 = preTranslucentDepthView;
      GpuTexture var1 = preTranslucentDepth;
      preTranslucentDepthView = null;
      preTranslucentDepth = null;
      preDepthCaptured = (boolean)0;

      try {
         if (var0 != null) {
            var0.close();
         }
      } finally {
         if (var1 != null) {
            var1.close();
         }
      }
   }

   public static void releaseAll() {
      try {
         releaseColor();
      } finally {
         try {
            releaseDepth();
         } finally {
            releasePreTranslucentDepth();
            captureState.clearMarks();
         }
      }
   }

   private SceneCapture() {
   }
}
