package com.larpclient.render.sky;

import com.larpclient.hud.HudRenderer;
import com.larpclient.module.render.CustomSkyModule;
import com.larpclient.render.overlay.PostProcessing;
import com.larpclient.util.data.TickTimer;
import com.larpclient.util.misc.LarpLog;
import com.larpclient.util.misc.PerfProfiler;
import com.mojang.blaze3d.pipeline.RenderTarget;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import net.minecraft.client.Minecraft;

public final class SkyRenderer2 implements AutoCloseable {
   private static SkyRenderer2 instance;
   private static final Set<String> failedModes = new HashSet<>();
   private static int failedGeneration = -1;
   private static boolean resetRequested;
   private final SkyRenderContext renderContext = new SkyRenderContext();
   private final Map<String, HudRenderer> renderers = new HashMap<>();
   private HudRenderer currentRenderer;
   private String currentMode;
   private int generation = -1;
   private double lastTime = Double.NaN;

   public SkyRenderer2() {
      instance = this;
   }

   public static boolean isModeAvailable(String var0) {
      return resetRequested || failedGeneration != PostProcessing.reloadCount || !failedModes.contains(var0);
   }

   public static boolean isNebulaAvailable() {
      return isModeAvailable("Nebula");
   }

   public static void requestReset() {
      resetRequested = (boolean)1;
   }

   public static void tick() {
      if (resetRequested || failedGeneration != PostProcessing.reloadCount) {
         failedModes.clear();
         failedGeneration = PostProcessing.reloadCount;
         resetRequested = (boolean)0;
         if (instance != null) {
            instance.reloadRenderers();
         }
      }

      CustomSkyModule var0 = CustomSkyModule.getInstance();
      if (instance != null && (var0 == null || !var0.isOperational() || Minecraft.getInstance().level == null) && instance.currentRenderer != null) {
         instance.close();
         LarpLog.info("Sky", "Sky renderer disabled; GPU resources released");
      }
   }

   public boolean render(RenderTarget var1, CustomSkyModule var2) {
      if (!isModeAvailable(var2.getSkyStyle())) {
         return false;
      }

      long var3 = PerfProfiler.startTimer();

      try {
         if (this.generation != PostProcessing.reloadCount) {
            this.reloadRenderers();
         }

         if (!var2.getSkyStyle().equals(this.currentMode)) {
            if (this.currentRenderer != null) {
               this.currentRenderer.close();
            }

            this.lastTime = Double.NaN;
            this.currentMode = var2.getSkyStyle();
            this.currentRenderer = this.renderers.computeIfAbsent(this.currentMode, var0 -> {
               return switch (var0) {
                  case "Ocean" -> new CausticsSkyShader();
                  case "Israel Flag" -> new IsraelFlagEffect();
                  default -> new NebulaCloudsSky();
               };
            });
            LarpLog.info("Sky", "Sky mode switched: {}; resource generation={}", this.currentMode, this.generation);
         }

         double var5 = TickTimer.getTime();
         this.renderContext.partialTick = Double.isNaN(this.lastTime) ? 0.0 : Math.clamp(var5 - this.lastTime, 0.0, 0.1);
         this.lastTime = var5;
         this.renderContext.target = var1;
         this.renderContext.skyModule = var2;
         this.currentRenderer.render(this.renderContext);
         return true;
      } catch (RuntimeException | IOException var11) {
         failedModes.add(var2.getSkyStyle());
         failedGeneration = PostProcessing.reloadCount;
         this.close();
         LarpLog.error("Sky", var2.getSkyStyle() + " failed; using vanilla sky until F3+T or disable/enable; generation=" + this.generation, var11);
         return false;
      } finally {
         this.renderContext.target = null;
         this.renderContext.skyModule = null;
         PerfProfiler.endTimer("Sky", var3);
      }
   }

   private void reloadRenderers() {
      for (HudRenderer var2 : this.renderers.values()) {
         var2.reload();
      }

      this.renderContext.state.close();
      this.generation = PostProcessing.reloadCount;
      this.lastTime = Double.NaN;
   }

   @Override
   public void close() {
      for (HudRenderer var2 : this.renderers.values()) {
         var2.close();
      }

      this.currentRenderer = null;
      this.renderContext.state.close();
      this.currentMode = null;
      this.lastTime = Double.NaN;
   }
}
