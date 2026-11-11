package com.larpclient.module.render;

import com.larpclient.core.StreamProofManager;
import com.larpclient.gui.theme.Theme;
import com.larpclient.hud.HudLayout;
import com.larpclient.module.misc.BooleanSetting;
import com.larpclient.module.misc.Category;
import com.larpclient.module.misc.ColorSetting;
import com.larpclient.module.misc.ModeSetting;
import com.larpclient.module.misc.Module;
import com.larpclient.module.misc.NumberSetting;
import com.larpclient.render.overlay.RoundedShapes;
import com.larpclient.util.misc.LarpLog;
import java.util.ArrayDeque;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.Entity;

public final class ClickPulseModule extends Module {
   private static final int maxPulses = 12;
   private static ClickPulseModule instance;
   private final ModeSetting trigger = this.addSetting(new ModeSetting("Trigger", "Which clicks start a pulse", "Both", "Both", "Left", "Right", "On Hit"));
   private final NumberSetting size = this.addSetting(new NumberSetting("Size", "Final ring radius", 14.0, 6.0, 40.0, 1.0));
   private final NumberSetting duration = this.addSetting(new NumberSetting("Duration", "Milliseconds until the ring fades out", 350.0, 120.0, 1000.0, 10.0));
   private final NumberSetting thickness = this.addSetting(new NumberSetting("Thickness", "Ring width in screen pixels", 2.0, 2.0, 8.0, 2.0));
   private final BooleanSetting themeColor = this.addSetting(new BooleanSetting("Theme Color", "Follow the interface accent color", true));
   private final ColorSetting color = this.addSetting(new ColorSetting("Color", "Ring color and opacity", -1));
   private final ArrayDeque<Long> pulseTimes = new ArrayDeque<>();

   public ClickPulseModule() {
      super("Click Pulse", "A ring pulses out of the crosshair when you click", Category.RENDER, -1);
      instance = this;
      this.color.visibleWhen(() -> !this.themeColor.getValue());
      this.trigger.onChange(var1 -> {
         this.pulseTimes.clear();
         LarpLog.info("ClickPulse", "Trigger selected: {}", var1);
      });
      HudElementRegistry.addLast(
         Identifier.fromNamespaceAndPath("larp-client", "clickpulse"),
         (var1, var2) -> {
            if (!this.pulseTimes.isEmpty()) {
               Minecraft var3 = getMc();
               var1 = StreamProofManager.selectGraphics(var1, false, false);
               if (var1 == null) {
                  this.pulseTimes.clear();
               } else {
                  HudLayout.beginFrame(var1);
                  if (this.isOperational()
                     && this.isInGame()
                     && !var3.gameRenderer.gameRenderState().guiRenderState.isHudHidden
                     && var3.options.getCameraType().isFirstPerson()
                     && (var3.gui.screen() == null || var3.gui.screen().getClass() == ChatScreen.class)) {
                     int var4 = var3.getWindow().getGuiScaledWidth();
                     int var5 = var3.getWindow().getGuiScaledHeight();
                     HudLayout.HudElement var6 = HudLayout.getElement("crosshair");
                     if (CustomCrosshairModule.isActive() && !var6.isHidden()) {
                        var6.fitTo(var4, var5);
                        var6.getY(var1, var6.getX(var4), var6.getY(var5));

                        try {
                           this.renderPulses(var1, var6.getX(var4) + var6.width / 2, var6.getY(var5) + var6.height / 2);
                        } finally {
                           var6.popScale(var1);
                        }
                     } else {
                        this.renderPulses(var1, (var4 - 15) / 2 + 7.5, (var5 - 15) / 2 + 7.5);
                     }
                  } else {
                     this.pulseTimes.clear();
                  }
               }
            }
         }
      );
   }

   public static ClickPulseModule getInstance() {
      return instance;
   }

   @Override
   public void onEnable() {
      this.pulseTimes.clear();
   }

   @Override
   public void onDisable() {
      this.pulseTimes.clear();
   }

   public static void onClick(int var0) {
      if (instance != null && instance.isEnabled()) {
         ClickPulseModule var1 = instance;
         if (var1 != null && var0 <= 1 && !var1.trigger.isMode("On Hit")) {
            if ((!var1.trigger.isMode("Left") || var0 == 0) && (!var1.trigger.isMode("Right") || var0 == 1)) {
               var1.addPulse();
            }
         }
      }
   }

   public void onAttackEntity(Entity var1) {
      if (this.isEnabled()) {
         if (var1 != null && this.trigger.isMode("On Hit")) {
            this.addPulse();
         }
      }
   }

   private void addPulse() {
      if (this.isOperational() && getMc().player != null && getMc().gui.screen() == null) {
         if (this.pulseTimes.size() >= 12) {
            this.pulseTimes.removeFirst();
         }

         this.pulseTimes.addLast(System.nanoTime());
      }
   }

   private int getColor() {
      return this.themeColor.getValue() ? Theme.accent | 0xFF000000 : this.color.getValue();
   }

   private void renderPulses(GuiGraphicsExtractor var1, double var2, double var4) {
      long var6 = System.nanoTime();
      double var8 = this.duration.getValue() * 1000000.0;

      while (!this.pulseTimes.isEmpty() && var6 - this.pulseTimes.peekFirst() >= var8) {
         this.pulseTimes.removeFirst();
      }

      int var10 = this.getColor();
      int var11 = var10 >>> 24;
      if (!this.pulseTimes.isEmpty() && var11 != 0) {
         double var12 = Math.hypot(var1.pose().m00(), var1.pose().m01()) * getMc().getWindow().getGuiScale();
         int var14 = Math.max(1, this.thickness.getValue().intValue() / 2);
         double var15 = 3.0 * var12;
         double var17 = this.size.getValue() * var12;
         var1.pose().pushMatrix();

         try {
            var1.pose().translate((float)var2, (float)var4);
            var1.pose().scale((float)(1.0 / var12));

            for (long var20 : this.pulseTimes) {
               double var22 = Math.clamp((var6 - var20) / var8, 0.0, 1.0);
               double var24 = 1.0 - Math.pow(1.0 - var22, 3.0);
               int var26 = (int)Math.round(var11 * (1.0 - var22) * (1.0 - var22));
               if (var26 > 0) {
                  int var27 = var26 << 24 | var10 & 16777215;
                  int var28 = (int)Math.round(var15 + (var17 - var15) * var24);

                  for (int var29 = 0; var29 < var14; var29++) {
                     int var30 = var28 - var29 * 2;
                     if (var30 < 2) {
                        break;
                     }

                     RoundedShapes.drawRim(var1, -var30, -var30, 2 * var30, 2 * var30, var30, var27);
                  }
               }
            }
         } finally {
            var1.pose().popMatrix();
         }
      }
   }
}
