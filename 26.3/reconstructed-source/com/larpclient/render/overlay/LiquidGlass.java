package com.larpclient.render.overlay;

import com.larpclient.module.render.SelfVisualsModule;
import com.larpclient.render.model.ModelVisualRenderer;
import com.larpclient.util.data.TickTimer;
import com.larpclient.util.misc.LarpLog;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import org.joml.Vector4f;

public final class LiquidGlass {
   private static LiquidGlass.GlassParams settings = new LiquidGlass.GlassParams(0.65, 0.78, 0.8, 0.18, false);
   private static double time;
   private static int fogColor = -7891281;
   private static int failedGeneration = -1;

   public static void updateFrame(CameraRenderState var0) {
      SelfVisualsModule var1 = SelfVisualsModule.getPrimaryColor();
      if (var1 != null) {
         settings = new LiquidGlass.GlassParams(
            var1.surfaceFlow.getValue(), var1.gloss.getValue(), var1.edgeHighlight.getValue(), var1.iridescence.getValue(), var1.affectArmor.getValue()
         );
      }

      time = TickTimer.getTime();
      if (var0.fogData != null && var0.fogData.color != null) {
         Vector4f var2 = var0.fogData.color;
         fogColor = 0xFF000000
            | Math.clamp(Math.round(var2.x * 255.0F), 0, 255) << 16
            | Math.clamp(Math.round(var2.y * 255.0F), 0, 255) << 8
            | Math.clamp(Math.round(var2.z * 255.0F), 0, 255);
      }
   }

   public static boolean isMaterialAvailable() {
      if (failedGeneration == PostProcessing.reloadCount) {
         return false;
      }

      try {
         ModelVisualRenderer.wrapCollector();
         return true;
      } catch (RuntimeException var1) {
         failedGeneration = PostProcessing.reloadCount;
         LarpLog.error("Liquid Glass", "Standard material unavailable; native skin retained until resource reload", var1);
         return false;
      }
   }

   public static boolean isArmorEnabled() {
      return settings.armor;
   }

   public static void onSubmitFailed(RuntimeException var0) {
      failedGeneration = PostProcessing.reloadCount;
      LarpLog.error("Liquid Glass", "Material submission failed; native geometry retained until resource reload", var0);
   }

   public static int packParams(SelfVisualsModule var0, double var1) {
      return (int)(Math.clamp(var1, 0.0, 1.0) * 255.0) << 24
         | (int)(settings.gloss * 255.0) << 16
         | (int)(settings.edge * 255.0) << 8
         | (int)(settings.iridescence * 255.0);
   }

   public static double flowPhase(SelfVisualsModule var0) {
      return time * settings.flow % (Math.PI * 2);
   }

   public static int fogRedGreen() {
      return fogColor >>> 8 & 65535;
   }

   public static int fogBlue() {
      return fogColor & 0xFF;
   }

   private LiquidGlass() {
   }

   private record GlassParams(double flow, double gloss, double edge, double iridescence, boolean armor) {
   }
}
