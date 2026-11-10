package com.larpclient.module;

import com.larpclient.ez;
import com.larpclient.k;
import com.larpclient.q8;
import com.larpclient.wi;
import com.larpclient.ws;
import java.util.ArrayDeque;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.minecraft.class_1297;
import net.minecraft.class_2960;
import net.minecraft.class_310;
import net.minecraft.class_332;
import net.minecraft.class_408;

public final class ClickPulseModule extends Module {
   private static final int W = 12;
   private static ClickPulseModule C;
   private final li b = this.addSetting(new li("Trigger", "Which clicks start a pulse", "Both", "Both", "Left", "Right", "On Hit"));
   private final lo U = this.addSetting(new lo("Size", "Final ring radius", 14.0, 6.0, 40.0, 1.0));
   private final lo f = this.addSetting(new lo("Duration", "Milliseconds until the ring fades out", 350.0, 120.0, 1000.0, 10.0));
   private final lo G = this.addSetting(new lo("Thickness", "Ring width in screen pixels", 2.0, 2.0, 8.0, 2.0));
   private final l3 N = this.addSetting(new l3("Theme Color", "Follow the interface accent color", true));
   private final l0 v = this.addSetting(new l0("Color", "Ring color and opacity", -1));
   private final ArrayDeque<Long> t = new ArrayDeque<>();

   public ClickPulseModule() {
      super("Click Pulse", "A ring pulses out of the crosshair when you click", Category.RENDER, -1);
      C = this;
      this.v.O(() -> !this.N.getValue());
      this.b.a(var1 -> {
         this.t.clear();
         q8.G("ClickPulse", "Trigger selected: {}", var1);
      });
      HudElementRegistry.addLast(
         class_2960.method_60655("larp-client", "clickpulse"),
         (var1, var2) -> {
            if (!this.t.isEmpty()) {
               class_310 var3 = getMc();
               var1 = wi.p(var1, false, false);
               if (var1 == null) {
                  this.t.clear();
               } else {
                  k.U(var1);
                  if (this.isOperational()
                     && this.isInGame()
                     && !var3.field_1690.field_1842
                     && var3.field_1690.method_31044().method_31034()
                     && (var3.field_1755 == null || var3.field_1755.getClass() == class_408.class)) {
                     int var4 = var3.method_22683().method_4486();
                     int var5 = var3.method_22683().method_4502();
                     k.l var6 = k.m("crosshair");
                     if (CustomCrosshairModule.y() && !var6.q()) {
                        var6.O(var4, var5);
                        var6.e(var1, var6.l(var4), var6.M(var5));

                        try {
                           this.N(var1, var6.l(var4) + var6.L / 2, var6.M(var5) + var6.F / 2);
                        } finally {
                           var6.o(var1);
                        }
                     } else {
                        this.N(var1, (var4 - 15) / 2 + 7.5, (var5 - 15) / 2 + 7.5);
                     }
                  } else {
                     this.t.clear();
                  }
               }
            }
         }
      );
   }

   public static ClickPulseModule T() {
      return C;
   }

   @Override
   public void onEnable() {
      this.t.clear();
   }

   @Override
   public void onDisable() {
      this.t.clear();
   }

   public static void n(int var0) {
      if (C != null && C.isEnabled()) {
         ClickPulseModule var1 = C;
         if (var1 != null && var0 <= 1 && !var1.b.G("On Hit")) {
            if ((!var1.b.G("Left") || var0 == 0) && (!var1.b.G("Right") || var0 == 1)) {
               var1.K();
            }
         }
      }
   }

   public void y(class_1297 var1) {
      if (this.isEnabled()) {
         if (var1 != null && this.b.G("On Hit")) {
            this.K();
         }
      }
   }

   private void K() {
      if (this.isOperational() && getMc().field_1724 != null && getMc().field_1755 == null) {
         if (this.t.size() >= 12) {
            this.t.removeFirst();
         }

         this.t.addLast(System.nanoTime());
      }
   }

   private int K() {
      return this.N.getValue() ? ez.a | 0xFF000000 : this.v.getValue();
   }

   private void N(class_332 var1, double var2, double var4) {
      long var6 = System.nanoTime();
      double var8 = this.f.getValue() * 1000000.0;

      while (!this.t.isEmpty() && var6 - this.t.peekFirst() >= var8) {
         this.t.removeFirst();
      }

      int var10 = this.K();
      int var11 = var10 >>> 24;
      if (!this.t.isEmpty() && var11 != 0) {
         double var12 = Math.hypot(var1.method_51448().m00(), var1.method_51448().m01()) * getMc().method_22683().method_4495();
         int var14 = Math.max(1, this.G.getValue().intValue() / 2);
         double var15 = 3.0 * var12;
         double var17 = this.U.getValue() * var12;
         var1.method_51448().pushMatrix();

         try {
            var1.method_51448().translate((float)var2, (float)var4);
            var1.method_51448().scale((float)(1.0 / var12));

            for (long var20 : this.t) {
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

                     ws.X(var1, -var30, -var30, 2 * var30, 2 * var30, var30, var27);
                  }
               }
            }
         } finally {
            var1.method_51448().popMatrix();
         }
      }
   }
}
