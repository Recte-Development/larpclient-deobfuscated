package com.larpclient.module;

import com.larpclient.o8;
import com.larpclient.ow;
import com.larpclient.r4;
import com.larpclient.rv;
import com.larpclient.y8;
import java.util.ArrayDeque;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.Entity;

public final class ClickPulseModule extends Module {
   private static final int w = 12;
   private static ClickPulseModule j;
   private final lp n = this.addSetting(new lp("Trigger", "Which clicks start a pulse", "Both", "Both", "Left", "Right", "On Hit"));
   private final lc N = this.addSetting(new lc("Size", "Final ring radius", 14.0, 6.0, 40.0, 1.0));
   private final lc b = this.addSetting(new lc("Duration", "Milliseconds until the ring fades out", 350.0, 120.0, 1000.0, 10.0));
   private final lc J = this.addSetting(new lc("Thickness", "Ring width in screen pixels", 2.0, 2.0, 8.0, 2.0));
   private final ln z = this.addSetting(new ln("Theme Color", "Follow the interface accent color", true));
   private final lv i = this.addSetting(new lv("Color", "Ring color and opacity", -1));
   private final ArrayDeque<Long> E = new ArrayDeque<>();

   public ClickPulseModule() {
      super("Click Pulse", "A ring pulses out of the crosshair when you click", Category.RENDER, -1);
      j = this;
      this.i.x(() -> !this.z.getValue());
      this.n.W(var1 -> {
         this.E.clear();
         r4.V("ClickPulse", "Trigger selected: {}", var1);
      });
      HudElementRegistry.addLast(
         Identifier.fromNamespaceAndPath("larp-client", "clickpulse"),
         (var1, var2) -> {
            if (!this.E.isEmpty()) {
               Minecraft var3 = getMc();
               var1 = y8.U(var1, false, false);
               if (var1 == null) {
                  this.E.clear();
               } else {
                  o8.e(var1);
                  if (this.isOperational()
                     && this.isInGame()
                     && !var3.gameRenderer.gameRenderState().guiRenderState.isHudHidden
                     && var3.options.getCameraType().isFirstPerson()
                     && (var3.gui.screen() == null || var3.gui.screen().getClass() == ChatScreen.class)) {
                     int var4 = var3.getWindow().getGuiScaledWidth();
                     int var5 = var3.getWindow().getGuiScaledHeight();
                     o8.a var6 = o8.q("crosshair");
                     if (CustomCrosshairModule.S() && !var6.z()) {
                        var6.q(var4, var5);
                        var6.J(var1, var6.b(var4), var6.M(var5));

                        try {
                           this.M(var1, var6.b(var4) + var6.N / 2, var6.M(var5) + var6.F / 2);
                        } finally {
                           var6.z(var1);
                        }
                     } else {
                        this.M(var1, (var4 - 15) / 2 + 7.5, (var5 - 15) / 2 + 7.5);
                     }
                  } else {
                     this.E.clear();
                  }
               }
            }
         }
      );
   }

   public static ClickPulseModule J() {
      return j;
   }

   @Override
   public void onEnable() {
      this.E.clear();
   }

   @Override
   public void onDisable() {
      this.E.clear();
   }

   public static void j(int var0) {
      if (j != null && j.isEnabled()) {
         ClickPulseModule var1 = j;
         if (var1 != null && var0 <= 1 && !var1.n.i("On Hit")) {
            if ((!var1.n.i("Left") || var0 == 0) && (!var1.n.i("Right") || var0 == 1)) {
               var1.r();
            }
         }
      }
   }

   public void Y(Entity var1) {
      if (this.isEnabled()) {
         if (var1 != null && this.n.i("On Hit")) {
            this.r();
         }
      }
   }

   private void r() {
      if (this.isOperational() && getMc().player != null && getMc().gui.screen() == null) {
         if (this.E.size() >= 12) {
            this.E.removeFirst();
         }

         this.E.addLast(System.nanoTime());
      }
   }

   private int p() {
      return this.z.getValue() ? rv.e | 0xFF000000 : this.i.getValue();
   }

   private void M(GuiGraphicsExtractor var1, double var2, double var4) {
      long var6 = System.nanoTime();
      double var8 = this.b.getValue() * 1000000.0;

      while (!this.E.isEmpty() && var6 - this.E.peekFirst() >= var8) {
         this.E.removeFirst();
      }

      int var10 = this.p();
      int var11 = var10 >>> 24;
      if (!this.E.isEmpty() && var11 != 0) {
         double var12 = Math.hypot(var1.pose().m00(), var1.pose().m01()) * getMc().getWindow().getGuiScale();
         int var14 = Math.max(1, this.J.getValue().intValue() / 2);
         double var15 = 3.0 * var12;
         double var17 = this.N.getValue() * var12;
         var1.pose().pushMatrix();

         try {
            var1.pose().translate((float)var2, (float)var4);
            var1.pose().scale((float)(1.0 / var12));

            for (long var20 : this.E) {
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

                     ow.A(var1, -var30, -var30, 2 * var30, 2 * var30, var30, var27);
                  }
               }
            }
         } finally {
            var1.pose().popMatrix();
         }
      }
   }
}
