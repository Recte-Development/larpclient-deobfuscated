package com.larpclient.module;

import com.larpclient.ho;
import com.larpclient.m4;
import com.larpclient.p2;
import java.util.ArrayDeque;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.Entity;

public final class ClickPulseModule extends Module {
   private static final int U = 12;
   private static ClickPulseModule v;
   private final l o = this.addSetting(new l("Trigger", "Which clicks start a pulse", "Both", "Both", "Left", "Right", "On Hit"));
   private final e B = this.addSetting(new e("Size", "Final ring radius", 14.0, 6.0, 40.0, 1.0));
   private final e i = this.addSetting(new e("Duration", "Milliseconds until the ring fades out", 350.0, 120.0, 1000.0, 10.0));
   private final e j = this.addSetting(new e("Thickness", "Ring width in screen pixels", 2.0, 2.0, 8.0, 2.0));
   private final s n = this.addSetting(new s("Theme Color", "Follow the interface accent color", true));
   private final i R = this.addSetting(new i("Color", "Ring color and opacity", -1));
   private final ArrayDeque<Long> w = new ArrayDeque<>();

   public ClickPulseModule() {
      super("Click Pulse", "A ring pulses out of the crosshair when you click", Category.RENDER, -1);
      v = this;
      this.R.P(() -> !this.n.getValue());
      this.o.o(var1 -> {
         this.w.clear();
         com.larpclient.h.A("ClickPulse", "Trigger selected: {}", var1);
      });
      HudElementRegistry.addLast(
         Identifier.fromNamespaceAndPath("larp-client", "clickpulse"),
         (var1, var2) -> {
            if (!this.w.isEmpty()) {
               Minecraft var3 = getMc();
               var1 = p2.x(var1, false, false);
               if (var1 == null) {
                  this.w.clear();
               } else {
                  m4.Z(var1);
                  if (this.isOperational()
                     && this.isInGame()
                     && !var3.gameRenderer.gameRenderState().guiRenderState.isHudHidden
                     && var3.options.getCameraType().isFirstPerson()
                     && (var3.gui.screen() == null || var3.gui.screen().getClass() == ChatScreen.class)) {
                     int var4 = var3.getWindow().getGuiScaledWidth();
                     int var5 = var3.getWindow().getGuiScaledHeight();
                     m4.p var6 = m4.S("crosshair");
                     if (CustomCrosshairModule.p() && !var6.k()) {
                        var6.b(var4, var5);
                        var6.N(var1, var6.T(var4), var6.P(var5));

                        try {
                           this.r(var1, var6.T(var4) + var6.d / 2, var6.P(var5) + var6.T / 2);
                        } finally {
                           var6.w(var1);
                        }
                     } else {
                        this.r(var1, (var4 - 15) / 2 + 7.5, (var5 - 15) / 2 + 7.5);
                     }
                  } else {
                     this.w.clear();
                  }
               }
            }
         }
      );
   }

   public static ClickPulseModule J() {
      return v;
   }

   @Override
   public void onEnable() {
      this.w.clear();
   }

   @Override
   public void onDisable() {
      this.w.clear();
   }

   public static void x(int var0) {
      if (v != null && v.isEnabled()) {
         ClickPulseModule var1 = v;
         if (var1 != null && var0 <= 1 && !var1.o.O("On Hit")) {
            if ((!var1.o.O("Left") || var0 == 0) && (!var1.o.O("Right") || var0 == 1)) {
               var1.W();
            }
         }
      }
   }

   public void w(Entity var1) {
      if (this.isEnabled()) {
         if (var1 != null && this.o.O("On Hit")) {
            this.W();
         }
      }
   }

   private void W() {
      if (this.isOperational() && getMc().player != null && getMc().gui.screen() == null) {
         if (this.w.size() >= 12) {
            this.w.removeFirst();
         }

         this.w.addLast(System.nanoTime());
      }
   }

   private int v() {
      return this.n.getValue() ? ho.J | 0xFF000000 : this.R.getValue();
   }

   private void r(GuiGraphicsExtractor var1, double var2, double var4) {
      long var6 = System.nanoTime();
      double var8 = this.i.getValue() * 1000000.0;

      while (!this.w.isEmpty() && var6 - this.w.peekFirst() >= var8) {
         this.w.removeFirst();
      }

      int var10 = this.v();
      int var11 = var10 >>> 24;
      if (!this.w.isEmpty() && var11 != 0) {
         double var12 = Math.hypot(var1.pose().m00(), var1.pose().m01()) * getMc().getWindow().getGuiScale();
         int var14 = Math.max(1, this.j.getValue().intValue() / 2);
         double var15 = 3.0 * var12;
         double var17 = this.B.getValue() * var12;
         var1.pose().pushMatrix();

         try {
            var1.pose().translate((float)var2, (float)var4);
            var1.pose().scale((float)(1.0 / var12));

            for (long var20 : this.w) {
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

                     com.larpclient.tb.J(var1, -var30, -var30, 2 * var30, 2 * var30, var30, var27);
                  }
               }
            }
         } finally {
            var1.pose().popMatrix();
         }
      }
   }
}
