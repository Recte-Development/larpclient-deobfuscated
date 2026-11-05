package com.larpclient.module;

import com.larpclient.mq;
import com.larpclient.mt;
import com.larpclient.tl;
import com.larpclient.zo;
import java.util.ArrayDeque;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.Entity;

public final class ClickPulseModule extends Module {
   private static final int u = 12;
   private static ClickPulseModule L;
   private final d6 F = this.addSetting(new d6("Trigger", "Which clicks start a pulse", "Both", "Both", "Left", "Right", "On Hit"));
   private final d9 Z = this.addSetting(new d9("Size", "Final ring radius", 14.0, 6.0, 40.0, 1.0));
   private final d9 y = this.addSetting(new d9("Duration", "Milliseconds until the ring fades out", 350.0, 120.0, 1000.0, 10.0));
   private final d9 S = this.addSetting(new d9("Thickness", "Ring width in screen pixels", 2.0, 2.0, 8.0, 2.0));
   private final dh R = this.addSetting(new dh("Theme Color", "Follow the interface accent color", true));
   private final dk G = this.addSetting(new dk("Color", "Ring color and opacity", -1));
   private final ArrayDeque<Long> O = new ArrayDeque<>();

   public ClickPulseModule() {
      super("Click Pulse", "A ring pulses out of the crosshair when you click", Category.RENDER, -1);
      L = this;
      this.G.H(() -> !this.R.P());
      this.F.h(var1 -> {
         this.O.clear();
         mt.K("ClickPulse", "Trigger selected: {}", var1);
      });
      HudElementRegistry.addLast(
         Identifier.fromNamespaceAndPath("larp-client", "clickpulse"),
         (var1, var2) -> {
            if (!this.O.isEmpty()) {
               Minecraft var3 = getMc();
               var1 = zo.R(var1, false, false);
               if (var1 == null) {
                  this.O.clear();
               } else {
                  mq.x(var1);
                  if (this.isOperational()
                     && this.isInGame()
                     && !var3.gameRenderer.gameRenderState().guiRenderState.isHudHidden
                     && var3.options.getCameraType().isFirstPerson()
                     && (var3.gui.screen() == null || var3.gui.screen().getClass() == ChatScreen.class)) {
                     int var4 = var3.getWindow().getGuiScaledWidth();
                     int var5 = var3.getWindow().getGuiScaledHeight();
                     mq.s var6 = mq.B("crosshair");
                     if (CustomCrosshairModule.I() && !var6.N()) {
                        var6.P(var4, var5);
                        var6.v(var1, var6.W(var4), var6.i(var5));

                        try {
                           this.N(var1, var6.W(var4) + var6.W / 2, var6.i(var5) + var6.n / 2);
                        } finally {
                           var6.i(var1);
                        }
                     } else {
                        this.N(var1, (var4 - 15) / 2 + 7.5, (var5 - 15) / 2 + 7.5);
                     }
                  } else {
                     this.O.clear();
                  }
               }
            }
         }
      );
   }

   public static ClickPulseModule J() {
      return L;
   }

   @Override
   public void onEnable() {
      this.O.clear();
   }

   @Override
   public void onDisable() {
      this.O.clear();
   }

   public static void J(int var0) {
      if (L != null && L.isEnabled()) {
         ClickPulseModule var1 = L;
         if (var1 != null && var0 <= 1 && !var1.F.S("On Hit")) {
            if ((!var1.F.S("Left") || var0 == 0) && (!var1.F.S("Right") || var0 == 1)) {
               var1.p();
            }
         }
      }
   }

   public void C(Entity var1) {
      if (this.isEnabled()) {
         if (var1 != null && this.F.S("On Hit")) {
            this.p();
         }
      }
   }

   private void p() {
      if (this.isOperational() && getMc().player != null && getMc().gui.screen() == null) {
         if (this.O.size() >= 12) {
            this.O.removeFirst();
         }

         this.O.addLast(System.nanoTime());
      }
   }

   private int u() {
      return this.R.P() ? com.larpclient.f.C | 0xFF000000 : this.G.P();
   }

   private void N(GuiGraphicsExtractor var1, double var2, double var4) {
      long var6 = System.nanoTime();
      double var8 = this.y.P() * 1000000.0;

      while (!this.O.isEmpty() && var6 - this.O.peekFirst() >= var8) {
         this.O.removeFirst();
      }

      int var10 = this.u();
      int var11 = var10 >>> 24;
      if (!this.O.isEmpty() && var11 != 0) {
         double var12 = Math.hypot(var1.pose().m00(), var1.pose().m01()) * getMc().getWindow().getGuiScale();
         int var14 = Math.max(1, this.S.P().intValue() / 2);
         double var15 = 3.0 * var12;
         double var17 = this.Z.P() * var12;
         var1.pose().pushMatrix();

         try {
            var1.pose().translate((float)var2, (float)var4);
            var1.pose().scale((float)(1.0 / var12));

            for (long var20 : this.O) {
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

                     tl.M(var1, -var30, -var30, 2 * var30, 2 * var30, var30, var27);
                  }
               }
            }
         } finally {
            var1.pose().popMatrix();
         }
      }
   }
}
