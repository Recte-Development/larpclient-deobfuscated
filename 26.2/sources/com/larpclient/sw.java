package com.larpclient;

import java.util.function.Consumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;

public final class sw extends Button {
   private final sw.r F;
   private final jp U = new jp(0.0);
   private m7.q n = m7.q.REST;
   private double A;
   private float W;
   private float V;
   private long b;
   public Consumer<GuiGraphicsExtractor> C;
   public static final int u = -16777216;
   public static final float T = 0.55F;
   public static final bj.j q = bj.j.Q;
   public static final float D = 1.375F;
   public static final float Q = 4.6F;
   public static final float B = 10.2F;
   public static final float e = 2.6F;
   public static final float N = 56.0F;
   public static final float I = 3.0F;
   public static final float O = 56.0F;
   public static final float f = 90.0F;

   public sw(Component var1, sw.r var2, Runnable var3, p1.e var4) {
      super(var4.x(), var4.y(), var4.width(), var4.height(), var1, var1x -> var3.run(), DEFAULT_NARRATION);
      this.F = var2;
   }

   public static int S(sw.r var0, double var1) {
      int var3 = switch (var0) {
         case t -> -2171170;
         case J -> -5592406;
         case a -> -6513508;
         case V -> -3618616;
      };
      return uh.q(var3, -657931, var1);
   }

   public static int j(double var0, boolean var2) {
      return var2 ? -657931 : uh.H(-3092272, var0 * 0.65);
   }

   public static float x(sw.r var0) {
      return switch (var0) {
         case t -> 1.2F;
         case J -> 1.12F;
         case a, V -> 1.05F;
      };
   }

   public static double W(double var0, double var2) {
      double var4 = Math.clamp(1.0 - var0 / var2, 0.0, 1.0);
      return var4 * var4 * (3.0 - 2.0 * var4);
   }

   public static void l(GuiGraphicsExtractor var0, float var1, int var2, float var3, int var4) {
      if (!(var3 <= 0.0F) && var4 >>> 24 != 0) {
         var0.pose().pushMatrix();

         try {
            var0.pose().translate(var1, var2);
            var0.pose().scale(var3 / 2.0F, 1.0F);
            var0.fill(-1, 0, 1, 1, var4);
         } finally {
            var0.pose().popMatrix();
         }
      }
   }

   public void I(m7.q var1) {
      this.n = var1;
   }

   public double E() {
      return this.n.alpha();
   }

   public double H() {
      return this.A;
   }

   protected void extractContents(GuiGraphicsExtractor var1, int var2, int var3, float var4) {
      if (!(this.n.alpha() <= 0.0)) {
         var1.pose().pushMatrix();

         try {
            float var5 = this.getX() + this.getWidth() / 2.0F;
            float var6 = this.getY() + this.getHeight() / 2.0F;
            var1.pose().translate((float)(var5 + this.n.x()), (float)(var6 + this.n.y()));
            var1.pose().rotate((float)this.n.rotation());
            var1.pose().scale((float)this.n.scaleX(), (float)this.n.scaleY());
            var1.pose().translate(-var5, -var6);
            this.y(var1, var2, var3);
         } finally {
            var1.pose().popMatrix();
         }
      }
   }

   private void y(GuiGraphicsExtractor var1, int var2, int var3) {
      boolean var4 = LarpClient.R().o().g().L;
      boolean var5 = Minecraft.getInstance().getLastInputType().isKeyboard();
      int var6 = this.active && this.isFocused() && var5 ? 1 : 0;
      double var7 = this.active && this.isHovered() && !var5 ? 1.0 : 0.0;
      double var9 = var4 ? this.U.k(var7, 140) : var7;
      if (var6 != 0) {
         var9 = 1.0;
      }

      this.A = var9;
      if (this.C != null) {
         this.C.accept(var1);
      } else {
         int var11 = this.getX();
         int var12 = this.getY();
         int var13 = this.getWidth();
         int var14 = this.getHeight();
         double var15 = this.n.alpha();
         float var17 = Math.min(x(this.F), (var14 - 4) / 9.0F) * 1.375F;
         String var18 = bj.v(this.getMessage().getString(), (int)((var13 - 40) / var17), q);
         float var19 = bj.G(var18, q) * var17;
         float var20 = var11 + (var13 - var19) / 2.0F;
         float var21 = var12 + var14 / 2.0F - 4.6F * var17;
         int var22 = this.active && var2 >= 0 && var3 >= 0 && !var5 ? 1 : 0;
         float var23 = var20 + var19 / 2.0F;
         float var24 = var12 + var14 / 2.0F;
         this.k(var22 != 0 && var4, var2, var3, var20, var21, var19, 11.0F * var17, var23, var24);
         var20 += this.W;
         var21 += this.V;
         j(var1, var18, var20, var21, var17, var6 != 0 ? 1.0 : 0.0, var22 != 0 ? var2 : Float.NaN, var22 != 0 ? var3 : Float.NaN, var15);
         var1.pose().pushMatrix();

         try {
            var1.pose().translate(var20, var21);
            var1.pose().scale(var17, var17);
            bj.Q(var1, var18, 0, 0, uh.F(S(this.F, var9), var15), uh.F(-16777216, var15), 0.55F, q);
         } finally {
            var1.pose().popMatrix();
         }

         if (var9 > 0.0) {
            l(
               var1,
               var11 + var13 / 2.0F + this.W,
               (int)Math.min(var21 + 10.2F * var17, var12 + var14 - 1),
               (float)(var19 * var9),
               uh.F(j(var9, (boolean)var6), var15)
            );
         }
      }
   }

   private void k(boolean var1, int var2, int var3, float var4, float var5, float var6, float var7, float var8, float var9) {
      float var10 = 0.0F;
      float var11 = 0.0F;
      if (var1) {
         double var12 = Math.max(0.0F, Math.max(var4 - var2, var2 - (var4 + var6)));
         double var14 = Math.max(0.0F, Math.max(var5 - var3, var3 - (var5 + var7)));
         double var16 = W(Math.hypot(var12, var14), 56.0);
         double var18 = var2 - var8;
         double var20 = var3 - var9;
         double var22 = Math.hypot(var18, var20);
         if (var16 > 0.0 && var22 > 0.001) {
            double var24 = 3.0 * var16 * Math.min(1.0, var22 / 24.0);
            var10 = (float)(var18 / var22 * var24);
            var11 = (float)(var20 / var22 * var24 * 0.6);
         }
      }

      long var26 = System.nanoTime();
      float var27 = this.b == 0L ? 1.0F : (float)(1.0 - Math.exp(-(var26 - this.b) / 1000000.0 / 90.0));
      this.b = var26;
      this.W = this.W + (var10 - this.W) * var27;
      this.V = this.V + (var11 - this.V) * var27;
      if (Math.abs(this.W) < 0.01F && Math.abs(this.V) < 0.01F && var10 == 0.0F && var11 == 0.0F) {
         this.W = this.V = 0.0F;
      }
   }

   private static void j(GuiGraphicsExtractor var0, String var1, float var2, float var3, float var4, double var5, float var7, float var8, double var9) {
      if (!(var5 <= 0.0) || !Float.isNaN(var7)) {
         int var11 = ho.J | 0xFF000000;
         float var12 = var3 + 4.6F * var4;
         float var13 = 0.0F;
         int var14 = 0;

         while (var14 < var1.length()) {
            int var15 = var1.codePointAt(var14);
            String var16 = var1.substring(var14, var14 + Character.charCount(var15));
            var14 += var16.length();
            float var17 = bj.j(var16, q);
            double var18 = Float.isNaN(var7) ? 0.0 : W(Math.hypot(var7 - (var2 + (var13 + var17 / 2.0F) * var4), var8 - var12), 56.0);
            double var20 = Math.max(var5 * 0.8, var18) * var9;
            if (var20 > 0.01 && !var16.isBlank()) {
               var0.pose().pushMatrix();

               try {
                  var0.pose().translate(var2 + var13 * var4, var3);
                  var0.pose().scale(var4, var4);
                  bj.T(var0, var16, 0, 0, uh.H(var11, var20), 2.6F, q);
               } finally {
                  var0.pose().popMatrix();
               }
            }

            var13 += var17;
         }
      }
   }

   public enum r {
      t,
      J,
      a,
      V;
   }
}
