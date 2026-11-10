package com.larpclient;

import com.larpclient.module.EmotesModule;
import java.util.List;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;

public final class mz extends Screen implements bg {
   private static final int m = 66;
   private static final int P = 42;
   private static final int N = 6;
   private static final int F = 240;
   private static final double q = 0.11;
   private static final double Z = 12.0;
   private static final int H = 12;
   private static final int o = 11;
   private final EmotesModule O;
   private final jp S = new jp(0.0);
   private final jp[] u = new jp[]{new jp(0.0), new jp(0.0), new jp(0.0), new jp(0.0)};
   private final jp[] Q = new jp[]{new jp(0.0), new jp(0.0), new jp(0.0), new jp(0.0)};
   private int g = -1;
   private double T = Double.NaN;
   private float E = 1.0F;
   private boolean b;

   public mz(EmotesModule var1) {
      super(Component.literal("Emotes"));
      this.O = var1;
      this.S.k(1.0, 240);
   }

   public static int W(double var0, double var2, double var4) {
      if (var0 * var0 + var2 * var2 < var4 * var4) {
         return -1;
      } else if (Math.abs(var0) > Math.abs(var2)) {
         return var0 > 0.0 ? 1 : 3;
      } else {
         return var2 > 0.0 ? 2 : 0;
      }
   }

   private void z(double var1, double var3) {
      double var5 = var1 - this.width / 2.0;
      double var7 = var3 - this.height / 2.0;
      this.g = W(var5, var7, 48.0F * this.E);
      this.T = this.g < 0 ? Double.NaN : Math.atan2(var7, var5);
   }

   private void V() {
      if (!this.b) {
         this.b = (boolean)1;
         List var1 = this.O.n();
         if (this.g >= 0 && !"Empty".equals(var1.get(this.g))) {
            this.O.j(this.g);
         } else if (this.g < 0 && this.O.k()) {
            this.O.J("stopped from the wheel");
         } else {
            h.U("Emotes", "Wheel closed without an emote (slot {})", this.g < 0 ? "hub" : this.g + 1);
         }

         this.onClose();
      }
   }

   public void tick() {
      int var1 = this.O.N();
      boolean var2 = bc.I(this.minecraft.getWindow().handle(), var1);
      if (!this.O.isRuntimeActive() || this.minecraft.player == null) {
         this.onClose();
      } else if (!var2) {
         this.V();
      }
   }

   public boolean keyReleased(KeyEvent var1) {
      if (k2.a(var1) == this.O.N()) {
         this.V();
         return true;
      } else {
         return super.keyReleased(var1);
      }
   }

   public void mouseMoved(double var1, double var3) {
      this.z(var1, var3);
   }

   public boolean mouseClicked(MouseButtonEvent var1, boolean var2) {
      this.z(var1.x(), var1.y());
      if (k2.w(var1) == 0) {
         this.V();
      } else {
         this.onClose();
      }

      return true;
   }

   public boolean isPauseScreen() {
      return false;
   }

   public void extractBackground(GuiGraphicsExtractor var1, int var2, int var3, float var4) {
      kv.d(var1, 0, 0, this.width, this.height, uh.H(-16777216, 0.25 * this.S.B()));
   }

   public void extractRenderState(GuiGraphicsExtractor var1, int var2, int var3, float var4) {
      super.extractRenderState(var1, var2, var3, var4);
      List var5 = this.O.n();
      double var6 = this.S.k(1.0, 240);
      int var8 = this.width / 2;
      int var9 = this.height / 2;
      float var10 = (float)Math.min(1.0, (Math.min(this.width, this.height) - 24) / 276.0);
      this.E = (float)((0.9 + 0.1 * var6) * var10);
      this.z(var2, var3);

      try (ua var11 = ua.K(var6)) {
         var1.pose().pushMatrix();

         try {
            var1.pose().translate(var8, var9);
            var1.pose().scale(this.E, this.E);

            for (int var12 = 0; var12 < 4; var12++) {
               this.o(var1, var12, (String)var5.get(var12), var6);
            }

            this.k(var1, var5);
         } finally {
            var1.pose().popMatrix();
         }

         this.L(var1, var8, var9 + Math.round(128.0F * this.E));
      }
   }

   private void o(GuiGraphicsExtractor var1, int var2, String var3, double var4) {
      boolean var6 = "Empty".equals(var3);
      mf.h var7 = mf.j(var3);
      int var8 = this.O.k() && var3.equals(this.O.I()) ? 1 : 0;
      double var9 = this.u[var2].k(var2 == this.g ? 1.0 : 0.0, 160);
      double var11 = this.Q[var2].k(this.g >= 0 && var2 != this.g ? 1.0 : 0.0, 160);
      double var13 = Math.clamp((var4 - var2 * 0.11) / 0.6699999999999999, 0.0, 1.0);
      double var15 = var9 * 6.0 - (1.0 - var13) * 12.0;
      double var17 = var6 ? var9 * 0.4 : var9;
      int var19 = uh.H(ho.A, var6 ? 0.7 : 0.92 - 0.08 * var11);
      int var20 = uh.q(var19, uh.H(uh.q(ho.A, ho.J, 0.3), 0.96), var17);
      int var21 = uh.q(uh.H(-1381654, 0.09), var6 ? uh.H(-1381654, 0.3) : ho.J, Math.max(var9, var8 != 0 ? 0.7 : 0.0));
      int var22 = uh.H(ho.J, var17);

      try (ua var23 = ua.K(var13)) {
         h9.v(var1, var2, var15, var20, var21, var22);
         double var24 = (var2 - 1) * Math.PI / 2.0;
         double var26 = 82.0 + var15;
         int var28 = (int)Math.round(Math.cos(var24) * var26);
         int var29 = (int)Math.round(Math.sin(var24) * var26);
         bj.u var30 = bj.u.X;
         if (!var6 && var7 != null) {
            int var42 = uh.q(uh.q(-1184275, -1, var9), -5855578, var11);
            double var32 = 1.0 + 0.08 * var9;
            var1.pose().pushMatrix();

            try {
               var1.pose().translate(var28, var29);
               var1.pose().scale((float)var32, (float)var32);
               short var34 = 192;
               var1.blit(RenderPipelines.GUI_TEXTURED, var7.icon(), -33, -33, 0.0F, 0.0F, 66, 66, var34, var34, var34, var34, ua.I(var42));
               return;
            } finally {
               var1.pose().popMatrix();
            }
         }

         int var31 = uh.q(uh.q(ho.j, ho.Z, var9), ho.A, 0.25 * var11);
         kv.d(var1, var28 - 5, var29 - 9, 11, 1, var31);
         kv.d(var1, var28, var29 - 14, 1, 11, var31);
         bj.k(var1, "Empty", var28 - bj.e("Empty", var30) / 2, var29 + 4, var31, var30);
      }
   }

   private void k(GuiGraphicsExtractor var1, List<String> var2) {
      kv.Q(var1, 0, 0, 42, uh.H(ho.A, 0.9));
      wp.q(var1, -42, -42, 85, 85, 42, uh.H(-1381654, 0.12));
      int var5 = -1381654;
      String var3;
      String var4;
      if (this.g >= 0) {
         var3 = (String)var2.get(this.g);
         boolean var6 = "Empty".equals(var3);
         var4 = var6 ? "No emote" : (this.O.k() && var3.equals(this.O.I()) ? "Replay" : "Play");
         if (var6) {
            var5 = ho.j;
         }
      } else {
         var3 = this.O.k() ? "Stop" : "Cancel";
         var4 = this.O.k() ? this.O.I() : "Emotes";
         var5 = ho.Z;
      }

      bj.u var13 = bj.u.i;
      List var7 = i(var3, var13, 72);
      int var8 = -(var7.size() * 12 + 11) / 2;

      for (String var10 : var7) {
         bj.k(var1, var10, -bj.e(var10, var13) / 2, var8, var5, var13);
         var8 += 12;
      }

      bj.u var14 = bj.u.m;
      var4 = r(var4, var14, 66);
      bj.k(var1, var4, -bj.e(var4, var14) / 2, var8 + 2, ho.Z, var14);
      if (!Double.isNaN(this.T)) {
         int var15 = (int)Math.round(Math.cos(this.T) * 45.0);
         int var11 = (int)Math.round(Math.sin(this.T) * 45.0);
         kv.Q(var1, var15, var11, 2, ho.J);
      }
   }

   private void L(GuiGraphicsExtractor var1, int var2, int var3) {
      String var4 = bc.G(this.O.N());
      String var5 = "Release " + var4 + " to play  ·  Esc to cancel";
      bj.u var6 = bj.u.M;
      bj.k(var1, var5, var2 - bj.e(var5, var6) / 2, var3, ho.Z, var6);
   }

   private static List<String> i(String var0, bj.u var1, int var2) {
      if (bj.e(var0, var1) <= var2) {
         return List.of(var0);
      }

      for (int var3 = var0.indexOf(32); var3 >= 0; var3 = var0.indexOf(32, var3 + 1)) {
         String var4 = var0.substring(0, var3);
         String var5 = var0.substring(var3 + 1);
         if (bj.e(var4, var1) <= var2 && bj.e(var5, var1) <= var2) {
            return List.of(var4, var5);
         }
      }

      return List.of(r(var0, var1, var2));
   }

   private static String r(String var0, bj.u var1, int var2) {
      if (bj.e(var0, var1) <= var2) {
         return var0;
      }

      String var3 = var0;

      while (var3.length() > 1 && bj.e(var3 + "...", var1) > var2) {
         var3 = var3.substring(0, var3.length() - 1);
      }

      return var3.strip() + "...";
   }
}
