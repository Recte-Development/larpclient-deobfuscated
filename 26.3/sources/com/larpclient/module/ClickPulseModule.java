package com.larpclient.module;

import com.larpclient._q;
import com.larpclient.dk;
import com.larpclient.qc;
import com.larpclient.w1;
import java.util.ArrayDeque;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.Entity;

public final class ClickPulseModule extends Module {
   private static final int d = 12;
   private static ClickPulseModule u;
   private final o t = this.addSetting(new o("Trigger", "Which clicks start a pulse", "Both", "Both", "Left", "Right", "On Hit"));
   private final b n = this.addSetting(new b("Size", "Final ring radius", 14.0, 6.0, 40.0, 1.0));
   private final b c = this.addSetting(new b("Duration", "Milliseconds until the ring fades out", 350.0, 120.0, 1000.0, 10.0));
   private final b w = this.addSetting(new b("Thickness", "Ring width in screen pixels", 2.0, 2.0, 8.0, 2.0));
   private final m r = this.addSetting(new m("Theme Color", "Follow the interface accent color", true));
   private final h A = this.addSetting(new h("Color", "Ring color and opacity", -1));
   private final ArrayDeque<Long> e = new ArrayDeque<>();

   public ClickPulseModule() {
      super("Click Pulse", "A ring pulses out of the crosshair when you click", Category.RENDER, -1);
      u = this;
      this.A.a(() -> !this.r.getValue());
      this.t.w(var1 -> {
         this.e.clear();
         _q.z("ClickPulse", "Trigger selected: {}", var1);
      });
      HudElementRegistry.addLast(
         Identifier.fromNamespaceAndPath("larp-client", "clickpulse"),
         (var1, var2) -> {
            if (!this.e.isEmpty()) {
               Minecraft var3 = getMc();
               var1 = qc.h(var1, false, false);
               if (var1 == null) {
                  this.e.clear();
               } else {
                  dk.n(var1);
                  if (this.isOperational()
                     && this.isInGame()
                     && !var3.gameRenderer.gameRenderState().guiRenderState.isHudHidden
                     && var3.options.getCameraType().isFirstPerson()
                     && (var3.gui.screen() == null || var3.gui.screen().getClass() == ChatScreen.class)) {
                     int var4 = var3.getWindow().getGuiScaledWidth();
                     int var5 = var3.getWindow().getGuiScaledHeight();
                     dk.q var6 = dk.T("crosshair");
                     if (CustomCrosshairModule.K() && !var6.X()) {
                        var6.s(var4, var5);
                        var6.p(var1, var6.f(var4), var6.p(var5));

                        try {
                           this.f(var1, var6.f(var4) + var6.w / 2, var6.p(var5) + var6.Z / 2);
                        } finally {
                           var6.I(var1);
                        }
                     } else {
                        this.f(var1, (var4 - 15) / 2 + 7.5, (var5 - 15) / 2 + 7.5);
                     }
                  } else {
                     this.e.clear();
                  }
               }
            }
         }
      );
   }

   public static ClickPulseModule N() {
      return u;
   }

   @Override
   public void onEnable() {
      this.e.clear();
   }

   @Override
   public void onDisable() {
      this.e.clear();
   }

   public static void Z(int var0) {
      if (u != null && u.isEnabled()) {
         ClickPulseModule var1 = u;
         if (var1 != null && var0 <= 1 && !var1.t.w("On Hit")) {
            if ((!var1.t.w("Left") || var0 == 0) && (!var1.t.w("Right") || var0 == 1)) {
               var1.C();
            }
         }
      }
   }

   public void R(Entity var1) {
      if (this.isEnabled()) {
         if (var1 != null && this.t.w("On Hit")) {
            this.C();
         }
      }
   }

   private void C() {
      if (this.isOperational() && getMc().player != null && getMc().gui.screen() == null) {
         if (this.e.size() >= 12) {
            this.e.removeFirst();
         }

         this.e.addLast(System.nanoTime());
      }
   }

   private int n() {
      return this.r.getValue() ? com.larpclient.c.y | 0xFF000000 : this.A.getValue();
   }

   private void f(GuiGraphicsExtractor var1, double var2, double var4) {
      long var6 = System.nanoTime();
      double var8 = this.c.getValue() * 1000000.0;

      while (!this.e.isEmpty() && var6 - this.e.peekFirst() >= var8) {
         this.e.removeFirst();
      }

      int var10 = this.n();
      int var11 = var10 >>> 24;
      if (!this.e.isEmpty() && var11 != 0) {
         double var12 = Math.hypot(var1.pose().m00(), var1.pose().m01()) * getMc().getWindow().getGuiScale();
         int var14 = Math.max(1, this.w.getValue().intValue() / 2);
         double var15 = 3.0 * var12;
         double var17 = this.n.getValue() * var12;
         var1.pose().pushMatrix();

         try {
            var1.pose().translate((float)var2, (float)var4);
            var1.pose().scale((float)(1.0 / var12));

            for (long var20 : this.e) {
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

                     w1.X(var1, -var30, -var30, 2 * var30, 2 * var30, var30, var27);
                  }
               }
            }
         } finally {
            var1.pose().popMatrix();
         }
      }
   }
}
