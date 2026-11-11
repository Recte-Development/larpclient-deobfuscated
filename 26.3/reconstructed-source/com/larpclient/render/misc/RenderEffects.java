package com.larpclient.render.misc;

import com.larpclient.config.HitEffectConfig;
import com.larpclient.module.misc.Module;
import com.larpclient.render.overlay.HighlightEffect;
import com.larpclient.render.overlay.PostProcessing;
import com.larpclient.render.world.EffectsRenderer;
import com.larpclient.util.misc.LarpLog;
import com.larpclient.util.misc.PerfProfiler;
import com.larpclient.util.misc.SafetyLevel;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.state.level.CameraRenderState;

final class RenderEffects {
   private static List<HighlightEffect> effects = List.of();

   static void setModules(List<Module> var0) {
      effects = var0.stream().filter(var0x -> var0x instanceof HighlightEffect).map(var0x -> (HighlightEffect)var0x).toList();
   }

   static void tick(Minecraft var0) {
      for (HighlightEffect var2 : effects) {
         if (isActive(var2)) {
            VisualsRenderer.runIsolated(var2.effectName(), () -> var2.tick(var0));
         }
      }
   }

   static void prepare(float var0, CameraRenderState var1) {
      for (HighlightEffect var3 : effects) {
         if (isActive(var3)) {
            VisualsRenderer.runIsolated(var3.effectName(), () -> {
               long var3x = PerfProfiler.startTimer();

               try {
                  var3.prepare(var0, var1);
               } finally {
                  PerfProfiler.endTimer(var3.effectName() + " preparation CPU", var3x);
               }
            });
         }
      }
   }

   static void render(EffectsRenderer var0, boolean var1) {
      if (!var1 || SafetyLevel.current != PostProcessing.Quality.OFF) {
         for (HighlightEffect var3 : effects) {
            if (isActive(var3)) {
               VisualsRenderer.runIsolated(var3.effectName(), () -> {
                  long var3x = PerfProfiler.startTimer();

                  try {
                     var3.render(var0, var1);
                  } finally {
                     var0.setSoftParticles(false);
                     var0.blend(HitEffectConfig.BlendMode.NORMAL);
                     PerfProfiler.endTimer(var3.effectName() + (var1 ? " glow CPU" : " base CPU"), var3x);
                  }
               });
            }
         }
      }
   }

   private static boolean isActive(HighlightEffect var0) {
      return !(var0 instanceof Module var1 && !var1.isRuntimeActive());
   }

   static boolean hasGlowing() {
      return SafetyLevel.current != PostProcessing.Quality.OFF
         && effects.stream().anyMatch(var0 -> isActive(var0) && !VisualsRenderer.isStageIsolated(var0.effectName()) && var0.glowing());
   }

   static boolean hasSoft() {
      return SafetyLevel.current != PostProcessing.Quality.OFF
         && effects.stream().anyMatch(var0 -> isActive(var0) && !VisualsRenderer.isStageIsolated(var0.effectName()) && var0.soft());
   }

   static void clearAll() {
      for (HighlightEffect var1 : effects) {
         clear(var1.effectName());
      }
   }

   static void clear(String var0) {
      for (HighlightEffect var2 : effects) {
         if (var2.effectName().equals(var0)) {
            try {
               var2.clear();
            } catch (RuntimeException var4) {
               LarpLog.errorOnce("Render", "Effect cleanup failed: " + var0, var4);
            }
         }
      }
   }

   private RenderEffects() {
   }
}
