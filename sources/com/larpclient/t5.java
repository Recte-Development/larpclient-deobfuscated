package com.larpclient;

import com.larpclient.module.EmotesModule;
import java.util.List;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;

public final class t5 extends Screen implements o3 {
   private static final int f = 38;
   private static final int H = 32;
   private static final int p = 5;
   private final EmotesModule Y;
   private final ef L = new ef(0.0);
   private final ef[] F = new ef[]{new ef(0.0), new ef(0.0), new ef(0.0), new ef(0.0)};
   private int R = -1;
   private double b = Double.NaN;
   private float Q = 1.0F;
   private boolean m;

   public t5(EmotesModule var1) {
      super(Component.literal("Emotes"));
      this.Y = var1;
      this.L.m(1.0, 160);
   }

   public static int j(double var0, double var2, double var4) {
      if (var0 * var0 + var2 * var2 < var4 * var4) {
         return -1;
      } else if (Math.abs(var0) > Math.abs(var2)) {
         return var0 > 0.0 ? 1 : 3;
      } else {
         return var2 > 0.0 ? 2 : 0;
      }
   }

   private void N(double var1, double var3) {
      double var5 = var1 - this.width / 2.0;
      double var7 = var3 - this.height / 2.0;
      this.R = j(var5, var7, 38.0F * this.Q);
      this.b = this.R < 0 ? Double.NaN : Math.atan2(var7, var5);
   }

   private void o() {
      if (!this.m) {
         this.m = (boolean)1;
         List var1 = this.Y.M();
         if (this.R >= 0 && !"Empty".equals(var1.get(this.R))) {
            this.Y.t(this.R);
         } else if (this.R < 0 && this.Y.p()) {
            this.Y.p("stopped from the wheel");
         } else {
            mt.U("Emotes", "Wheel closed without an emote (slot {})", this.R < 0 ? "hub" : this.R + 1);
         }

         this.onClose();
      }
   }

   public void tick() {
      int var1 = this.Y.n();
      boolean var2 = cc.N(this.minecraft.getWindow().handle(), var1);
      if (!this.Y.isRuntimeActive() || this.minecraft.player == null) {
         this.onClose();
      } else if (!var2) {
         this.o();
      }
   }

   public boolean keyReleased(KeyEvent var1) {
      if (mo.O(var1) == this.Y.n()) {
         this.o();
         return true;
      } else {
         return super.keyReleased(var1);
      }
   }

   public void mouseMoved(double var1, double var3) {
      this.N(var1, var3);
   }

   public boolean mouseClicked(MouseButtonEvent var1, boolean var2) {
      this.N(var1.x(), var1.y());
      if (mo.Z(var1) == 0) {
         this.o();
      } else {
         this.onClose();
      }

      return true;
   }

   public boolean isPauseScreen() {
      return false;
   }

   public void extractBackground(GuiGraphicsExtractor var1, int var2, int var3, float var4) {
      c7.s(var1, 0, 0, this.width, this.height, _3.A(-16777216, 0.25 * this.L.G()));
   }

   public void extractRenderState(GuiGraphicsExtractor var1, int var2, int var3, float var4) {
      super.extractRenderState(var1, var2, var3, var4);
      List var5 = this.Y.M();
      double var6 = this.L.m(1.0, 160);
      int var8 = this.width / 2;
      int var9 = this.height / 2;
      float var10 = (float)Math.min(1.0, (Math.min(this.width, this.height) - 24) / 236.0);
      this.Q = (float)((0.9 + 0.1 * var6) * var10);
      this.N(var2, var3);

      try (de var11 = de.l(var6)) {
         var1.pose().pushMatrix();

         try {
            var1.pose().translate(var8, var9);
            var1.pose().scale(this.Q, this.Q);

            for (int var12 = 0; var12 < 4; var12++) {
               this.z(var1, var12, (String)var5.get(var12));
            }

            this.j(var1, var5);
         } finally {
            var1.pose().popMatrix();
         }

         this.F(var1, var8, var9 + Math.round(108.0F * this.Q));
      }
   }

   private void z(GuiGraphicsExtractor var1, int var2, String var3) {
      boolean var4 = "Empty".equals(var3);
      p0.u var5 = p0.X(var3);
      double var6 = this.F[var2].m(var2 == this.R ? 1.0 : 0.0, 160);
      int var8 = var4 ? _3.A(com.larpclient.f.u, 0.5) : _3.A(com.larpclient.f.u, 0.84);
      int var9 = _3.D(var8, _3.A(_3.D(com.larpclient.f.u, com.larpclient.f.C, 0.2), 0.94), var4 ? var6 * 0.4 : var6);
      int var10 = _3.D(_3.A(-1381654, 0.09), var4 ? _3.A(-1381654, 0.3) : com.larpclient.f.C, var6);
      d8.i(var1, var2, var6 * 5.0, var9, var10);
      double var11 = (var2 - 1) * Math.PI / 2.0;
      double var13 = 67.0 + var6 * 5.0;
      int var15 = (int)Math.round(Math.cos(var11) * var13);
      int var16 = (int)Math.round(Math.sin(var11) * var13);
      k6.d var17 = k6.d.C;
      if (!var4 && var5 != null) {
         int var20 = _3.D(com.larpclient.f.s, -1381654, 0.55 + 0.45 * var6);
         var1.blit(RenderPipelines.GUI_TEXTURED, var5.icon(), var15 - 19, var16 - 19 - 5, 0.0F, 0.0F, 38, 38, 96, 96, 96, 96, de.h(var20));
         int var19 = _3.D(com.larpclient.f.J, -1381654, var6);
         k6.T(var1, var3, var15 - k6.a(var3, var17) / 2, var16 + 19 - 4, var19, var17);
      } else {
         int var18 = _3.D(com.larpclient.f.G, com.larpclient.f.s, var6);
         c7.s(var1, var15 - 5, var16 - 9, 11, 1, var18);
         c7.s(var1, var15, var16 - 14, 1, 11, var18);
         k6.T(var1, "Empty", var15 - k6.a("Empty", var17) / 2, var16 + 4, var18, var17);
      }
   }

   private void j(GuiGraphicsExtractor var1, List<String> var2) {
      c7.K(var1, 0, 0, 32, _3.A(com.larpclient.f.u, 0.9));
      pn.D(var1, -32, -32, 65, 65, 32, _3.A(-1381654, 0.12));
      int var5 = -1381654;
      String var3;
      String var4;
      if (this.R >= 0) {
         var3 = (String)var2.get(this.R);
         boolean var6 = "Empty".equals(var3);
         var4 = var6 ? "No emote" : (this.Y.p() && var3.equals(this.Y.Q()) ? "Playing" : "Release");
         if (var6) {
            var5 = com.larpclient.f.G;
         }
      } else {
         var3 = this.Y.p() ? "Stop" : "Cancel";
         var4 = this.Y.p() ? this.Y.Q() : "Emotes";
         var5 = com.larpclient.f.s;
      }

      k6.d var12 = k6.d.a;
      var3 = j(var3, var12, 54);
      k6.T(var1, var3, -k6.a(var3, var12) / 2, -8, var5, var12);
      k6.d var7 = k6.d.q;
      var4 = j(var4, var7, 50);
      k6.T(var1, var4, -k6.a(var4, var7) / 2, 5, com.larpclient.f.G, var7);
      if (!Double.isNaN(this.b)) {
         int var8 = (int)Math.round(Math.cos(this.b) * 35.0);
         int var9 = (int)Math.round(Math.sin(this.b) * 35.0);
         c7.K(var1, var8, var9, 2, com.larpclient.f.C);
      }
   }

   private void F(GuiGraphicsExtractor var1, int var2, int var3) {
      String var4 = cc.u(this.Y.n());
      String var5 = "Release " + var4 + " to play  ·  Esc to cancel";
      k6.d var6 = k6.d.K;
      k6.T(var1, var5, var2 - k6.a(var5, var6) / 2, var3, com.larpclient.f.s, var6);
   }

   private static String j(String var0, k6.d var1, int var2) {
      if (k6.a(var0, var1) <= var2) {
         return var0;
      }

      String var3 = var0;

      while (var3.length() > 1 && k6.a(var3 + "...", var1) > var2) {
         var3 = var3.substring(0, var3.length() - 1);
      }

      return var3.strip() + "...";
   }
}
