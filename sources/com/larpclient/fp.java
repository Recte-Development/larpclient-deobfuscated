package com.larpclient;

import com.larpclient.module.EmotesModule;
import java.util.List;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;

public final class fp extends Screen implements h7 {
   private static final int g = 38;
   private static final int n = 32;
   private static final int l = 5;
   private final EmotesModule u;
   private final u7 a = new u7(0.0);
   private final u7[] s = new u7[]{new u7(0.0), new u7(0.0), new u7(0.0), new u7(0.0)};
   private int M = -1;
   private double W = Double.NaN;
   private float h = 1.0F;
   private boolean T;

   public fp(EmotesModule var1) {
      super(Component.literal("Emotes"));
      this.u = var1;
      this.a.y(1.0, 160);
   }

   public static int K(double var0, double var2, double var4) {
      if (var0 * var0 + var2 * var2 < var4 * var4) {
         return -1;
      } else if (Math.abs(var0) > Math.abs(var2)) {
         return var0 > 0.0 ? 1 : 3;
      } else {
         return var2 > 0.0 ? 2 : 0;
      }
   }

   private void U(double var1, double var3) {
      double var5 = var1 - this.width / 2.0;
      double var7 = var3 - this.height / 2.0;
      this.M = K(var5, var7, 38.0F * this.h);
      this.W = this.M < 0 ? Double.NaN : Math.atan2(var7, var5);
   }

   private void Q() {
      if (!this.T) {
         this.T = (boolean)1;
         List var1 = this.u.u();
         if (this.M >= 0 && !"Empty".equals(var1.get(this.M))) {
            this.u.D(this.M);
         } else if (this.M < 0 && this.u.K()) {
            this.u.k("stopped from the wheel");
         } else {
            r4.g("Emotes", "Wheel closed without an emote (slot {})", this.M < 0 ? "hub" : this.M + 1);
         }

         this.onClose();
      }
   }

   public void tick() {
      int var1 = this.u.B();
      boolean var2 = pe.A(this.minecraft.getWindow().handle(), var1);
      if (!this.u.isRuntimeActive() || this.minecraft.player == null) {
         this.onClose();
      } else if (!var2) {
         this.Q();
      }
   }

   public boolean keyReleased(KeyEvent var1) {
      if (r7.g(var1) == this.u.B()) {
         this.Q();
         return true;
      } else {
         return super.keyReleased(var1);
      }
   }

   public void mouseMoved(double var1, double var3) {
      this.U(var1, var3);
   }

   public boolean mouseClicked(MouseButtonEvent var1, boolean var2) {
      this.U(var1.x(), var1.y());
      if (r7.y(var1) == 0) {
         this.Q();
      } else {
         this.onClose();
      }

      return true;
   }

   public boolean isPauseScreen() {
      return false;
   }

   public void extractBackground(GuiGraphicsExtractor var1, int var2, int var3, float var4) {
      fr.k(var1, 0, 0, this.width, this.height, oh.i(-16777216, 0.25 * this.a.V()));
   }

   public void extractRenderState(GuiGraphicsExtractor var1, int var2, int var3, float var4) {
      super.extractRenderState(var1, var2, var3, var4);
      List var5 = this.u.u();
      double var6 = this.a.y(1.0, 160);
      int var8 = this.width / 2;
      int var9 = this.height / 2;
      float var10 = (float)Math.min(1.0, (Math.min(this.width, this.height) - 24) / 236.0);
      this.h = (float)((0.9 + 0.1 * var6) * var10);
      this.U(var2, var3);

      try (r var11 = r.p(var6)) {
         var1.pose().pushMatrix();

         try {
            var1.pose().translate(var8, var9);
            var1.pose().scale(this.h, this.h);

            for (int var12 = 0; var12 < 4; var12++) {
               this.e(var1, var12, (String)var5.get(var12));
            }

            this.D(var1, var5);
         } finally {
            var1.pose().popMatrix();
         }

         this.K(var1, var8, var9 + Math.round(108.0F * this.h));
      }
   }

   private void e(GuiGraphicsExtractor var1, int var2, String var3) {
      boolean var4 = "Empty".equals(var3);
      ya.g var5 = ya.T(var3);
      double var6 = this.s[var2].y(var2 == this.M ? 1.0 : 0.0, 160);
      int var8 = var4 ? oh.i(rv.w, 0.5) : oh.i(rv.w, 0.84);
      int var9 = oh.j(var8, oh.i(oh.j(rv.w, rv.e, 0.2), 0.94), var4 ? var6 * 0.4 : var6);
      int var10 = oh.j(oh.i(-1381654, 0.09), var4 ? oh.i(-1381654, 0.3) : rv.e, var6);
      rj.j(var1, var2, var6 * 5.0, var9, var10);
      double var11 = (var2 - 1) * Math.PI / 2.0;
      double var13 = 67.0 + var6 * 5.0;
      int var15 = (int)Math.round(Math.cos(var11) * var13);
      int var16 = (int)Math.round(Math.sin(var11) * var13);
      j_.m var17 = j_.m.r;
      if (!var4 && var5 != null) {
         int var20 = oh.j(rv.G0, -1381654, 0.55 + 0.45 * var6);
         var1.blit(RenderPipelines.GUI_TEXTURED, var5.icon(), var15 - 19, var16 - 19 - 5, 0.0F, 0.0F, 38, 38, 96, 96, 96, 96, r.D(var20));
         int var19 = oh.j(rv.P, -1381654, var6);
         j_.L(var1, var3, var15 - j_.o(var3, var17) / 2, var16 + 19 - 4, var19, var17);
      } else {
         int var18 = oh.j(rv.r, rv.G0, var6);
         fr.k(var1, var15 - 5, var16 - 9, 11, 1, var18);
         fr.k(var1, var15, var16 - 14, 1, 11, var18);
         j_.L(var1, "Empty", var15 - j_.o("Empty", var17) / 2, var16 + 4, var18, var17);
      }
   }

   private void D(GuiGraphicsExtractor var1, List<String> var2) {
      fr.J(var1, 0, 0, 32, oh.i(rv.w, 0.9));
      ll.u(var1, -32, -32, 65, 65, 32, oh.i(-1381654, 0.12));
      int var5 = -1381654;
      String var3;
      String var4;
      if (this.M >= 0) {
         var3 = (String)var2.get(this.M);
         boolean var6 = "Empty".equals(var3);
         var4 = var6 ? "No emote" : (this.u.K() && var3.equals(this.u.F()) ? "Playing" : "Release");
         if (var6) {
            var5 = rv.r;
         }
      } else {
         var3 = this.u.K() ? "Stop" : "Cancel";
         var4 = this.u.K() ? this.u.F() : "Emotes";
         var5 = rv.G0;
      }

      j_.m var12 = j_.m.d;
      var3 = R(var3, var12, 54);
      j_.L(var1, var3, -j_.o(var3, var12) / 2, -8, var5, var12);
      j_.m var7 = j_.m.u;
      var4 = R(var4, var7, 50);
      j_.L(var1, var4, -j_.o(var4, var7) / 2, 5, rv.r, var7);
      if (!Double.isNaN(this.W)) {
         int var8 = (int)Math.round(Math.cos(this.W) * 35.0);
         int var9 = (int)Math.round(Math.sin(this.W) * 35.0);
         fr.J(var1, var8, var9, 2, rv.e);
      }
   }

   private void K(GuiGraphicsExtractor var1, int var2, int var3) {
      String var4 = pe.A(this.u.B());
      String var5 = "Release " + var4 + " to play  ·  Esc to cancel";
      j_.m var6 = j_.m.U;
      j_.L(var1, var5, var2 - j_.o(var5, var6) / 2, var3, rv.G0, var6);
   }

   private static String R(String var0, j_.m var1, int var2) {
      if (j_.o(var0, var1) <= var2) {
         return var0;
      }

      String var3 = var0;

      while (var3.length() > 1 && j_.o(var3 + "...", var1) > var2) {
         var3 = var3.substring(0, var3.length() - 1);
      }

      return var3.strip() + "...";
   }
}
