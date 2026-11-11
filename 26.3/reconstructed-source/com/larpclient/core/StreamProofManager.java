package com.larpclient.core;

import com.larpclient.LarpClient;
import com.larpclient.hud.NotificationManager;
import com.larpclient.hud.StreamProofOverlay;
import com.larpclient.module.misc.Module;
import com.larpclient.module.misc.StreamProofModule;
import com.larpclient.module.render.CustomSkyModule;
import com.larpclient.module.render.SkyShaderModule;
import com.larpclient.render.misc.ClientPipelines;
import com.larpclient.render.overlay.PostProcessing;
import com.larpclient.render.overlay.StreamProofRenderer;
import com.larpclient.util.data.Marker;
import com.larpclient.util.misc.LarpLog;
import com.larpclient.util.misc.StreamProofState;
import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.blaze3d.systems.RenderSystem;
import com.sun.jna.Platform;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import net.fabricmc.fabric.api.client.rendering.v1.level.LevelRenderContext;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.state.gui.GuiRenderState;
import net.minecraft.gizmos.SimpleGizmoCollector;
import net.minecraft.gizmos.SimpleGizmoCollector.GizmoInstance;
import net.minecraft.util.Util;
import org.lwjgl.sdl.SDLVideo;

public final class StreamProofManager {
   private static final StreamProofRenderer layerRenderer = new StreamProofRenderer();
   private static final SimpleGizmoCollector gizmoCollector = new SimpleGizmoCollector();
   private static StreamProofOverlay overlay;
   private static String unavailableReason;
   private static boolean active;
   private static boolean hidingInterface;
   private static boolean presentWarned;
   private static long frameCounter;
   private static long submittedFrames;
   private static long firstSubmittedFrame = Long.MAX_VALUE;
   private static long graphicsFrame = -1L;
   private static boolean fullscreen;
   private static long fullscreenChangedAt;
   private static final long fULLSCREEN_SETTLE_MS = 400L;
   private static GuiGraphicsExtractor overlayGraphics;
   private static List<GizmoInstance> deferredGizmos = List.of();
   private static int dumpFramesRemaining;
   private static boolean dumpRequested;
   private static String dumpLabel;

   private StreamProofManager() {
   }

   public static void tick() {
      frameCounter++;
      Minecraft var0 = Minecraft.getInstance();
      StreamProofModule var1 = StreamProofModule.getInstance();
      int var2 = var1 != null && var1.isOperational() ? 1 : 0;
      if (var2 == 0) {
         unavailableReason = null;
      }

      int var3 = var2 != 0 && var0.level != null && unavailableReason == null && var0.getWindow() != null ? 1 : 0;
      if (var3 == 0) {
         if (overlay != null) {
            destroyOverlay(var2 != 0 ? "left the world" : "module turned off");
         }

         hidingInterface = (boolean)0;
         active = (boolean)0;
         setHideEverything(false);
      } else {
         if (overlay == null) {
            createOverlay(var0);
         } else {
            destroyOverlay(var0);
         }

         if (overlay != null && (overlay.getWorkerError() != null || layerRenderer.hasFailed())) {
            stopWithReason(overlay.getWorkerError() != null ? "overlay presentation failed (" + overlay.getWorkerError() + ")" : "layer rendering failed");
         }

         active = (boolean)(overlay != null ? 1 : 0);
         hidingInterface = (boolean)(active && var1.isHidingEverything() ? 1 : 0);
         setHideEverything(hidingInterface && var1.shouldPauseSceneVisuals());
      }
   }

   private static void setHideEverything(boolean var0) {
      if (var0 != StreamProofState.isHiddenGlobally()) {
         StreamProofState.setHiddenGlobally(var0);
         if (!var0) {
            LarpLog.info("StreamProof", "Scene-altering visuals resume");
         } else {
            ArrayList var1 = new ArrayList();

            for (Module var3 : LarpClient.getInstance().getModuleManager().getModules()) {
               if (var3.isEnabled() && StreamProofModule.sceneModules.contains(var3.getClass())) {
                  var3.suspendForDisconnect();
                  var1.add(var3.getName());
               }
            }

            LarpLog.info("StreamProof", "Hide Everything paused scene-altering visuals for the stream: {}", var1);
         }
      }
   }

   private static void createOverlay(Minecraft var0) {
      if (!Platform.isWindows()) {
         unavailableReason = "Stream Proof is only available on Windows";
         LarpLog.warn("StreamProof", unavailableReason);
         notifyUser("Stream Proof unavailable", unavailableReason + ".");
      } else {
         try {
            overlay = StreamProofOverlay.create(var0.getWindow().handle());
            fullscreen = requestDump(var0);
            fullscreenChangedAt = 0L;
            firstSubmittedFrame = Long.MAX_VALUE;
            presentWarned = (boolean)0;
            dumpFramesRemaining = Boolean.getBoolean("larp.streamProof.dump") ? 3 : 0;
            LarpLog.info(
               "StreamProof",
               "Enabled; backend={}, Windows build={}, display capture exclusion={}",
               RenderSystem.getBackendDescription(),
               StreamProofOverlay.getWindowsBuild(),
               overlay.isDisplayCaptureExcluded()
            );
            if (!overlay.isDisplayCaptureExcluded()) {
               notifyUser("Stream Proof limited", "Game and window capture are hidden; this Windows build cannot hide display capture.");
            }
         } catch (RuntimeException | UnsatisfiedLinkError var3) {
            unavailableReason = "overlay window could not be created";
            LarpLog.error(
               "StreamProof",
               "Stream Proof could not create its overlay window; the client is drawn normally",
               var3 instanceof RuntimeException var2 ? var2 : new RuntimeException(var3)
            );
            notifyUser("Stream Proof unavailable", "The capture-hidden overlay could not start; the client is drawn normally.");
         }
      }
   }

   private static boolean requestDump(Minecraft var0) {
      return (SDLVideo.SDL_GetWindowFlags(var0.getWindow().handle()) & 1L) != 0L;
   }

   private static void destroyOverlay(Minecraft var0) {
      boolean var1 = requestDump(var0);
      long var2 = Util.getMillis();
      if (var1 != fullscreen) {
         fullscreen = var1;
         fullscreenChangedAt = var2;
         LarpLog.info("StreamProof", "Game window {} fullscreen; recreating the overlay once it settles", var1 ? "entered" : "left");
      } else if (fullscreenChangedAt != 0L && var2 - fullscreenChangedAt >= 400L) {
         fullscreenChangedAt = 0L;
         overlay.close();
         overlay = null;
         createOverlay(var0);
         if (overlay != null) {
            LarpLog.info("StreamProof", "Overlay recreated after the fullscreen change (fullscreen={})", var1);
         }
      }
   }

   private static void stopWithReason(String var0) {
      unavailableReason = var0;
      destroyOverlay(var0);
      notifyUser("Stream Proof stopped", "The client is drawn normally again. Reason: " + var0 + ".");
   }

   private static void destroyOverlay(String var0) {
      if (overlay != null) {
         overlay.close();
         overlay = null;
      }

      layerRenderer.reset();
      deferredGizmos = List.of();
      firstSubmittedFrame = Long.MAX_VALUE;
      LarpLog.info("StreamProof", "Disabled ({})", var0);
   }

   public static void shutdown() {
      StreamProofState.setHiddenGlobally(false);
      StreamProofState.setActive(false);
      if (overlay != null) {
         destroyOverlay("client shutdown");
      } else {
         layerRenderer.reset();
      }

      unavailableReason = null;
   }

   public static void clearLayers() {
      layerRenderer.reset();
      deferredGizmos = List.of();
   }

   public static boolean isOverlayActive() {
      return overlay != null;
   }

   public static String statusText(StreamProofModule var0) {
      if (overlay == null) {
         return unavailableReason != null ? "Unavailable: " + unavailableReason : "";
      }

      String var1 = overlay.isDisplayCaptureExcluded() ? "game, window, display" : "game, window";
      return (var0.isHidingEverything() ? "Hiding all (" : "Keeping visuals (") + var1 + ")";
   }

   private static void notifyUser(String var0, String var1) {
      try {
         NotificationManager.getInstance().push(var0, var1, NotificationManager.Type.WARNING, 6000L);
      } catch (RuntimeException var3) {
      }
   }

   public static GuiGraphicsExtractor selectGraphics(GuiGraphicsExtractor var0, boolean var1, boolean var2) {
      boolean var3 = var1 ? hidingInterface : active;
      return !var3 ? var0 : overlayGraphicsForFrame();
   }

   public static GuiGraphicsExtractor getOverlayGraphics() {
      return overlayGraphicsForFrame();
   }

   public static boolean destroyOverlay(Object var0) {
      return layerRenderer.hasContent(var0) && layerRenderer.blurInterface();
   }

   public static RenderTarget getRenderTarget(Object var0) {
      return layerRenderer.hasContent(var0) ? layerRenderer.getInterfaceTarget() : null;
   }

   private static GuiGraphicsExtractor overlayGraphicsForFrame() {
      if (graphicsFrame != frameCounter) {
         Minecraft var0 = Minecraft.getInstance();
         int var1 = (int)Math.round(var0.mouseHandler.getScaledXPos(var0.getWindow()));
         int var2 = (int)Math.round(var0.mouseHandler.getScaledYPos(var0.getWindow()));
         overlayGraphics = new GuiGraphicsExtractor(var0, layerRenderer.getGuiRenderState(), var1, var2);
         graphicsFrame = frameCounter;
      }

      return overlayGraphics;
   }

   public static GuiRenderState selectGuiRenderState(GuiRenderState var0) {
      return active ? layerRenderer.getGuiRenderState() : var0;
   }

   public static boolean blitOverlayTarget(RenderTarget var0) {
      RenderTarget var1 = layerRenderer.getInterfaceTarget();
      if (active && var1 != null && var1.width == var0.width && var1.height == var0.height) {
         PostProcessing.blit(ClientPipelines.cOPY, var1, var0);
         return true;
      } else {
         return false;
      }
   }

   public static boolean isActive() {
      return active;
   }

   public static boolean isOverlayScreen(Screen var0) {
      return active && var0 instanceof Marker;
   }

   public static boolean getOverlayGraphics() {
      return active;
   }

   public static boolean submitFrame() {
      return hidingInterface;
   }

   public static void clearLayers(LevelRenderContext var0, Runnable var1) {
      if (!hidingInterface) {
         var1.run();
      } else {
         layerRenderer.renderGizmos(frameCounter, () -> {
            var1.run();
            if (!deferredGizmos.isEmpty()) {
               layerRenderer.renderGizmos(deferredGizmos, var0.levelState().cameraRenderState, Minecraft.getInstance().gameRenderer.featureRenderDispatcher());
            }
         });
      }
   }

   public static void endWorldLayer() {
      if (hidingInterface) {
         layerRenderer.beginFrame(frameCounter);
      }
   }

   public static boolean isHidingInterface() {
      return hidingInterface;
   }

   public static RenderTarget tick(RenderTarget var0) {
      return hidingInterface ? layerRenderer.getSkyTarget(frameCounter) : var0;
   }

   public static RenderTarget setHideEverything(RenderTarget var0) {
      return hidingInterface ? layerRenderer.drawHandCover(frameCounter) : var0;
   }

   public static void statusText() {
      if (hidingInterface) {
         layerRenderer.composeSky(frameCounter, isSkyReplaced());
      }
   }

   private static boolean isSkyReplaced() {
      if (CustomSkyModule.isActive() && !CustomSkyModule.getInstance().isCloudsEnabled()) {
         return true;
      }

      SkyShaderModule var0 = SkyShaderModule.getInstance();
      return var0 != null && var0.getAppearanceSettings();
   }

   public static boolean statusText() {
      return hidingInterface;
   }

   public static void notifyUser() {
      layerRenderer.captureHandBackdrop(frameCounter);
      StreamProofState.setActive(true);
   }

   public static void getOverlayGraphics() {
      StreamProofState.setActive(false);
      layerRenderer.drawHandCover(frameCounter);
      layerRenderer.setRedirect(true);
   }

   public static void runInLayer(Runnable var0) {
      layerRenderer.runWithEffectsTarget(frameCounter, var0);
   }

   public static void createOverlay() {
      layerRenderer.setRedirect(false);
   }

   public static RenderTarget blitOverlayTarget(RenderTarget var0) {
      return layerRenderer.redirectTarget(var0);
   }

   public static void isOverlayActive(Runnable var0) {
      if (hidingInterface) {
         layerRenderer.reset(frameCounter, var0);
      } else {
         var0.run();
      }
   }

   public static void isOverlayScreen() {
      if (hidingInterface) {
         layerRenderer.captureSelfDepth(frameCounter);
      }
   }

   public static void runOnOverlayThread(Runnable var0) {
      if (hidingInterface) {
         layerRenderer.runOutsideLayer(var0);
      } else {
         var0.run();
      }
   }

   public static List<GizmoInstance> deferGizmos(List<GizmoInstance> var0) {
      if (!hidingInterface) {
         return var0;
      }

      deferredGizmos = var0.isEmpty() ? List.of() : List.copyOf(var0);
      return List.of();
   }

   public static void submitFrame() {
      if (overlay == null) {
         layerRenderer.discardFrame();
         deferredGizmos = List.of();
      } else {
         int var0 = hidingInterface && layerRenderer.getSkyTarget(frameCounter) ? 1 : 0;
         long var1 = submittedFrames + 1L;
         int var3 = !dumpRequested && (dumpFramesRemaining <= 0 || var1 != 1L && var1 != 60L && var1 != 180L) ? 0 : 1;
         if (dumpRequested) {
            dumpRequested = (boolean)0;
            dumpFramesRemaining = Math.max(dumpFramesRemaining, 1);
         }

         int var4 = var3 != 0 && layerRenderer.hasContent() ? 1 : 0;
         StreamProofOverlay var5 = overlay;
         StreamProofRenderer.ReadBackListener var6 = var3 == 0 ? var5::submitFrame : (var3x, var5x, var6x, var7x) -> {
            runOnOverlayThread(var3x, var5x, var6x, var7x, var4, var0);
            var5.submitFrame(var3x, var5x, var6x, var7x);
         };
         boolean var7 = layerRenderer.composeInterface(frameCounter, var1, (boolean)var0, layerRenderer::renderBlur, var6);
         deferredGizmos = List.of();
         if (var7) {
            submittedFrames = var1;
            if (firstSubmittedFrame == Long.MAX_VALUE) {
               firstSubmittedFrame = var1;
            }

            RenderTarget var8 = Minecraft.getInstance().gameRenderer.mainRenderTarget();
            overlay.updateVisibility(true, firstSubmittedFrame, var8.width, var8.height);
            if (!presentWarned && var1 - firstSubmittedFrame > 240L && overlay.getPresentedSequence() < firstSubmittedFrame) {
               presentWarned = (boolean)1;
               LarpLog.warn(
                  "StreamProof",
                  "Overlay has not presented a frame after {} submitted frames (presented={}, size {}x{}); read-back delivery or UpdateLayeredWindow is failing",
                  var1 - firstSubmittedFrame,
                  overlay.getPresentedSequence(),
                  var8.width,
                  var8.height
               );
            }
         }
      }
   }

   public static void requestDump() {
      requestDumpLabeled(null);
   }

   public static void requestDumpLabeled(String var0) {
      dumpRequested = (boolean)1;
      dumpLabel = var0;
   }

   private static void runOnOverlayThread(long var0, int var2, int var3, ByteBuffer var4, boolean var5, boolean var6) {
      if (dumpFramesRemaining > 0) {
         dumpFramesRemaining--;
         long var7 = 0L;
         long var9 = 0L;
         long var11 = 0L;
         long var13 = 0L;
         NativeImage var15 = new NativeImage(var2, var3, false);

         try {
            int var16 = var4.position();

            for (int var32 = 0; var32 < var3; var32++) {
               for (int var18 = 0; var18 < var2; var18++) {
                  int var19 = var16 + (var32 * var2 + var18) * 4;
                  int var20 = var4.get(var19) & 255;
                  int var21 = var4.get(var19 + 1) & 255;
                  int var22 = var4.get(var19 + 2) & 255;
                  int var23 = var4.get(var19 + 3) & 255;
                  if (var23 == 0) {
                     var7++;
                  } else if (var23 == 255) {
                     var9++;
                     if (var22 == 0 && var21 == 0 && var20 == 0) {
                        var13++;
                     }
                  } else {
                     var11++;
                  }

                  int var24 = var23 == 0 ? 0 : Math.min(255, var22 * 255 / var23);
                  int var25 = var23 == 0 ? 0 : Math.min(255, var21 * 255 / var23);
                  int var26 = var23 == 0 ? 0 : Math.min(255, var20 * 255 / var23);
                  var15.setPixelABGR(var18, var32, var23 << 24 | var26 << 16 | var25 << 8 | var24);
               }
            }

            Path var33 = Minecraft.getInstance().gameDirectory.toPath().resolve("screenshots");
            Files.createDirectories(var33);
            String var34 = dumpLabel != null ? dumpLabel : String.valueOf(var0);
            dumpLabel = null;
            Path var35 = var33.resolve("streamproof-overlay-" + var34 + ".png");
            var15.writeToFile(var35);
            long var36 = (long)var2 * var3;
            LarpLog.info(
               "StreamProof",
               "Dumped overlay frame {} ({}x{}) to {}; transparent={}%, partial={}%, opaque={}% (opaque black={}%); interface content={}, effects layer={}",
               var0,
               var2,
               var3,
               var35,
               100L * var7 / var36,
               100L * var11 / var36,
               100L * var9 / var36,
               100L * var13 / var36,
               var5,
               var6
            );
         } catch (Exception var30) {
            LarpLog.error("StreamProof", "Overlay frame dump failed", var30 instanceof RuntimeException var17 ? var17 : new RuntimeException(var30));
         } finally {
            var15.close();
         }
      }
   }
}
