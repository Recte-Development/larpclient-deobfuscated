package com.larpclient;

import com.larpclient.module.EmotesModule;
import java.util.List;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;

public final class jh extends Screen implements n1 {
   private static final int p = 66;
   private static final int F = 42;
   private static final int c = 6;
   private static final int u = 240;
   private static final double K = 0.11;
   private static final double P = 12.0;
   private static final int l = 12;
   private static final int D = 11;
   private final EmotesModule T;
   private final fq A = new fq(0.0);
   private final fq[] w = new fq[]{new fq(0.0), new fq(0.0), new fq(0.0), new fq(0.0)};
   private final fq[] t = new fq[]{new fq(0.0), new fq(0.0), new fq(0.0), new fq(0.0)};
   private int h = -1;
   private double g = Double.NaN;
   private float R = 1.0F;
   private boolean Q;

   public jh(EmotesModule var1) {
      super(Component.literal("Emotes"));
      this.T = var1;
      this.A.n(1.0, 240);
   }

   public static int o(double var0, double var2, double var4) {
      if (var0 * var0 + var2 * var2 < var4 * var4) {
         return -1;
      } else if (Math.abs(var0) > Math.abs(var2)) {
         return var0 > 0.0 ? 1 : 3;
      } else {
         return var2 > 0.0 ? 2 : 0;
      }
   }

   private void i(double var1, double var3) {
      double var5 = var1 - this.width / 2.0;
      double var7 = var3 - this.height / 2.0;
      this.h = o(var5, var7, 48.0F * this.R);
      this.g = this.h < 0 ? Double.NaN : Math.atan2(var7, var5);
   }

   private void S() {
      if (!this.Q) {
         this.Q = (boolean)1;
         List var1 = this.T.f();
         if (this.h >= 0 && !"Empty".equals(var1.get(this.h))) {
            this.T.W(this.h);
         } else if (this.h < 0 && this.T.O()) {
            this.T.x("stopped from the wheel");
         } else {
            _q.c("Emotes", "Wheel closed without an emote (slot {})", this.h < 0 ? "hub" : this.h + 1);
         }

         this.onClose();
      }
   }

   public void tick() {
      int var1 = this.T.S();
      boolean var2 = p4.Y(this.minecraft.getWindow().handle(), var1);
      if (!this.T.isRuntimeActive() || this.minecraft.player == null) {
         this.onClose();
      } else if (!var2) {
         this.S();
      }
   }

   public boolean keyReleased(KeyEvent var1) {
      if (j2.P(var1) == this.T.S()) {
         this.S();
         return true;
      } else {
         return super.keyReleased(var1);
      }
   }

   public void mouseMoved(double var1, double var3) {
      this.i(var1, var3);
   }

   public boolean mouseClicked(MouseButtonEvent var1, boolean var2) {
      this.i(var1.x(), var1.y());
      if (j2.L(var1) == 0) {
         this.S();
      } else {
         this.onClose();
      }

      return true;
   }

   public boolean isPauseScreen() {
      return false;
   }

   public void extractBackground(GuiGraphicsExtractor var1, int var2, int var3, float var4) {
      h5.N(var1, 0, 0, this.width, this.height, vv.N(-16777216, 0.25 * this.A.K()));
   }

   public void extractRenderState(GuiGraphicsExtractor var1, int var2, int var3, float var4) {
      super.extractRenderState(var1, var2, var3, var4);
      List var5 = this.T.f();
      double var6 = this.A.n(1.0, 240);
      int var8 = this.width / 2;
      int var9 = this.height / 2;
      float var10 = (float)Math.min(1.0, (Math.min(this.width, this.height) - 24) / 276.0);
      this.R = (float)((0.9 + 0.1 * var6) * var10);
      this.i(var2, var3);

      try (oa var11 = oa.j(var6)) {
         var1.pose().pushMatrix();

         try {
            var1.pose().translate(var8, var9);
            var1.pose().scale(this.R, this.R);

            for (int var12 = 0; var12 < 4; var12++) {
               this.o(var1, var12, (String)var5.get(var12), var6);
            }

            this.r(var1, var5);
         } finally {
            var1.pose().popMatrix();
         }

         this.H(var1, var8, var9 + Math.round(128.0F * this.R));
      }
   }

   private void o(GuiGraphicsExtractor var1, int var2, String var3, double var4) {
      boolean var6 = "Empty".equals(var3);
      il.w var7 = il.c(var3);
      int var8 = this.T.O() && var3.equals(this.T.J()) ? 1 : 0;
      double var9 = this.w[var2].n(var2 == this.h ? 1.0 : 0.0, 160);
      double var11 = this.t[var2].n(this.h >= 0 && var2 != this.h ? 1.0 : 0.0, 160);
      double var13 = Math.clamp((var4 - var2 * 0.11) / 0.6699999999999999, 0.0, 1.0);
      double var15 = var9 * 6.0 - (1.0 - var13) * 12.0;
      double var17 = var6 ? var9 * 0.4 : var9;
      int var19 = vv.N(com.larpclient.c.D, var6 ? 0.7 : 0.92 - 0.08 * var11);
      int var20 = vv.Q(var19, vv.N(vv.Q(com.larpclient.c.D, com.larpclient.c.y, 0.3), 0.96), var17);
      int var21 = vv.Q(vv.N(-1381654, 0.09), var6 ? vv.N(-1381654, 0.3) : com.larpclient.c.y, Math.max(var9, var8 != 0 ? 0.7 : 0.0));
      int var22 = vv.N(com.larpclient.c.y, var17);

      try (oa var23 = oa.j(var13)) {
         af.P(var1, var2, var15, var20, var21, var22);
         double var24 = (var2 - 1) * Math.PI / 2.0;
         double var26 = 82.0 + var15;
         int var28 = (int)Math.round(Math.cos(var24) * var26);
         int var29 = (int)Math.round(Math.sin(var24) * var26);
         ic.n var30 = ic.n.C;
         if (!var6 && var7 != null) {
            int var42 = vv.Q(vv.Q(-1184275, -1, var9), -5855578, var11);
            double var32 = 1.0 + 0.08 * var9;
            var1.pose().pushMatrix();

            try {
               var1.pose().translate(var28, var29);
               var1.pose().scale((float)var32, (float)var32);
               short var34 = 192;
               var1.blit(RenderPipelines.GUI_TEXTURED, var7.icon(), -33, -33, 0.0F, 0.0F, 66, 66, var34, var34, var34, var34, oa.e(var42));
               return;
            } finally {
               var1.pose().popMatrix();
            }
         }

         int var31 = vv.Q(vv.Q(com.larpclient.c.f, com.larpclient.c.W, var9), com.larpclient.c.D, 0.25 * var11);
         h5.N(var1, var28 - 5, var29 - 9, 11, 1, var31);
         h5.N(var1, var28, var29 - 14, 1, 11, var31);
         ic.N(var1, "Empty", var28 - ic.f("Empty", var30) / 2, var29 + 4, var31, var30);
      }
   }

   private void r(GuiGraphicsExtractor var1, List<String> var2) {
      h5.M(var1, 0, 0, 42, vv.N(com.larpclient.c.D, 0.9));
      wc.h(var1, -42, -42, 85, 85, 42, vv.N(-1381654, 0.12));
      int var5 = -1381654;
      String var3;
      String var4;
      if (this.h >= 0) {
         var3 = (String)var2.get(this.h);
         boolean var6 = "Empty".equals(var3);
         var4 = var6 ? "No emote" : (this.T.O() && var3.equals(this.T.J()) ? "Replay" : "Play");
         if (var6) {
            var5 = com.larpclient.c.f;
         }
      } else {
         var3 = this.T.O() ? "Stop" : "Cancel";
         var4 = this.T.O() ? this.T.J() : "Emotes";
         var5 = com.larpclient.c.W;
      }

      ic.n var13 = ic.n.Q;
      List var7 = q(var3, var13, 72);
      int var8 = -(var7.size() * 12 + 11) / 2;

      for (String var10 : var7) {
         ic.N(var1, var10, -ic.f(var10, var13) / 2, var8, var5, var13);
         var8 += 12;
      }

      ic.n var14 = ic.n.L;
      var4 = M(var4, var14, 66);
      ic.N(var1, var4, -ic.f(var4, var14) / 2, var8 + 2, com.larpclient.c.W, var14);
      if (!Double.isNaN(this.g)) {
         int var15 = (int)Math.round(Math.cos(this.g) * 45.0);
         int var11 = (int)Math.round(Math.sin(this.g) * 45.0);
         h5.M(var1, var15, var11, 2, com.larpclient.c.y);
      }
   }

   private void H(GuiGraphicsExtractor var1, int var2, int var3) {
      String var4 = p4.n(this.T.S());
      String var5 = "Release " + var4 + " to play  ·  Esc to cancel";
      ic.n var6 = ic.n.I;
      ic.N(var1, var5, var2 - ic.f(var5, var6) / 2, var3, com.larpclient.c.W, var6);
   }

   private static List<String> q(String var0, ic.n var1, int var2) {
      if (ic.f(var0, var1) <= var2) {
         return List.of(var0);
      }

      for (int var3 = var0.indexOf(32); var3 >= 0; var3 = var0.indexOf(32, var3 + 1)) {
         String var4 = var0.substring(0, var3);
         String var5 = var0.substring(var3 + 1);
         if (ic.f(var4, var1) <= var2 && ic.f(var5, var1) <= var2) {
            return List.of(var4, var5);
         }
      }

      return List.of(M(var0, var1, var2));
   }

   private static String M(String var0, ic.n var1, int var2) {
      if (ic.f(var0, var1) <= var2) {
         return var0;
      }

      String var3 = var0;

      while (var3.length() > 1 && ic.f(var3 + "...", var1) > var2) {
         var3 = var3.substring(0, var3.length() - 1);
      }

      return var3.strip() + "...";
   }
}
