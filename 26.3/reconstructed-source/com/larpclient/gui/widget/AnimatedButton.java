package com.larpclient.gui.widget;

import com.larpclient.LarpClient;
import com.larpclient.gui.screen.HomeLayout;
import com.larpclient.gui.screen.MenuIntroAnimation;
import com.larpclient.gui.theme.Theme;
import com.larpclient.render.overlay.FontRenderer;
import com.larpclient.util.render.Animation;
import com.larpclient.util.render.ColorUtil;
import java.util.function.Consumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;

public final class AnimatedButton extends Button {
   private final AnimatedButton.Style style;
   private final Animation hoverAnimation = new Animation(0.0);
   private MenuIntroAnimation.PartTransform transform = MenuIntroAnimation.PartTransform.REST;
   private double hoverProgress;
   private float textOffsetX;
   private float textOffsetY;
   private long lastUpdateNanos;
   public Consumer<GuiGraphicsExtractor> customRenderer;
   public static final int sHADOW_COLOR = -16777216;
   public static final float sHADOW_ALPHA = 0.55F;
   public static final FontRenderer.Face fONT = FontRenderer.Face.CLICK_MEDIUM;
   public static final float tEXT_SCALE = 1.375F;
   public static final float bASELINE_OFFSET = 4.6F;
   public static final float uNDERLINE_OFFSET = 10.2F;
   public static final float gLOW_RADIUS = 2.6F;
   public static final float mAGNET_RANGE = 56.0F;
   public static final float mAGNET_MAX_SHIFT = 3.0F;
   public static final float gLOW_RANGE = 56.0F;
   public static final float mAGNET_SMOOTHING_MS = 90.0F;

   public AnimatedButton(Component var1, AnimatedButton.Style var2, Runnable var3, HomeLayout.Rect var4) {
      super(var4.left(), var4.top(), var4.width(), var4.height(), var1, var1x -> var3.run(), DEFAULT_NARRATION);
      this.style = var2;
   }

   public static int textColor(AnimatedButton.Style var0, double var1) {
      int var3 = switch (var0) {
         case PRIMARY -> -2171170;
         case SECONDARY -> -5592406;
         case TERTIARY -> -6513508;
         case QUATERNARY -> -3618616;
      };
      return ColorUtil.lerpColor(var3, -657931, var1);
   }

   public static int underlineColor(double var0, boolean var2) {
      return var2 ? -657931 : ColorUtil.withAlpha(-3092272, var0 * 0.65);
   }

   public static float fontScale(AnimatedButton.Style var0) {
      return switch (var0) {
         case PRIMARY -> 1.2F;
         case SECONDARY -> 1.12F;
         case TERTIARY, QUATERNARY -> 1.05F;
      };
   }

   public static double smoothFalloff(double var0, double var2) {
      double var4 = Math.clamp(1.0 - var0 / var2, 0.0, 1.0);
      return var4 * var4 * (3.0 - 2.0 * var4);
   }

   public static void drawUnderline(GuiGraphicsExtractor var0, float var1, int var2, float var3, int var4) {
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

   public void setTransform(MenuIntroAnimation.PartTransform var1) {
      this.transform = var1;
   }

   public double fontScale() {
      return this.transform.alpha();
   }

   public double getHoverProgress() {
      return this.hoverProgress;
   }

   protected void extractContents(GuiGraphicsExtractor var1, int var2, int var3, float var4) {
      if (!(this.transform.alpha() <= 0.0)) {
         var1.pose().pushMatrix();

         try {
            float var5 = this.getX() + this.getWidth() / 2.0F;
            float var6 = this.getY() + this.getHeight() / 2.0F;
            var1.pose().translate((float)(var5 + this.transform.offsetX()), (float)(var6 + this.transform.offsetY()));
            var1.pose().rotate((float)this.transform.rotation());
            var1.pose().scale((float)this.transform.scaleX(), (float)this.transform.scaleY());
            var1.pose().translate(-var5, -var6);
            this.renderButton(var1, var2, var3);
         } finally {
            var1.pose().popMatrix();
         }
      }
   }

   private void renderButton(GuiGraphicsExtractor var1, int var2, int var3) {
      boolean var4 = LarpClient.getInstance().getConfigManager().getPreferences().motion;
      boolean var5 = Minecraft.getInstance().getLastInputType().isKeyboard();
      int var6 = this.active && this.isFocused() && var5 ? 1 : 0;
      double var7 = this.active && this.isHovered() && !var5 ? 1.0 : 0.0;
      double var9 = var4 ? this.hoverAnimation.animateTo(var7, 140) : var7;
      if (var6 != 0) {
         var9 = 1.0;
      }

      this.hoverProgress = var9;
      if (this.customRenderer != null) {
         this.customRenderer.accept(var1);
      } else {
         int var11 = this.getX();
         int var12 = this.getY();
         int var13 = this.getWidth();
         int var14 = this.getHeight();
         double var15 = this.transform.alpha();
         float var17 = Math.min(fontScale(this.style), (var14 - 4) / 9.0F) * 1.375F;
         String var18 = FontRenderer.truncateToWidth(this.getMessage().getString(), (int)((var13 - 40) / var17), fONT);
         float var19 = FontRenderer.measureRawWidth(var18, fONT) * var17;
         float var20 = var11 + (var13 - var19) / 2.0F;
         float var21 = var12 + var14 / 2.0F - 4.6F * var17;
         int var22 = this.active && var2 >= 0 && var3 >= 0 && !var5 ? 1 : 0;
         float var23 = var20 + var19 / 2.0F;
         float var24 = var12 + var14 / 2.0F;
         this.updateMagnet(var22 != 0 && var4, var2, var3, var20, var21, var19, 11.0F * var17, var23, var24);
         var20 += this.textOffsetX;
         var21 += this.textOffsetY;
         drawGlowText(var1, var18, var20, var21, var17, var6 != 0 ? 1.0 : 0.0, var22 != 0 ? var2 : Float.NaN, var22 != 0 ? var3 : Float.NaN, var15);
         var1.pose().pushMatrix();

         try {
            var1.pose().translate(var20, var21);
            var1.pose().scale(var17, var17);
            FontRenderer.drawOutlinedText(
               var1, var18, 0, 0, ColorUtil.multiplyAlpha(textColor(this.style, var9), var15), ColorUtil.multiplyAlpha(-16777216, var15), 0.55F, fONT
            );
         } finally {
            var1.pose().popMatrix();
         }

         if (var9 > 0.0) {
            drawUnderline(
               var1,
               var11 + var13 / 2.0F + this.textOffsetX,
               (int)Math.min(var21 + 10.2F * var17, var12 + var14 - 1),
               (float)(var19 * var9),
               ColorUtil.multiplyAlpha(underlineColor(var9, (boolean)var6), var15)
            );
         }
      }
   }

   private void updateMagnet(boolean var1, int var2, int var3, float var4, float var5, float var6, float var7, float var8, float var9) {
      float var10 = 0.0F;
      float var11 = 0.0F;
      if (var1) {
         double var12 = Math.max(0.0F, Math.max(var4 - var2, var2 - (var4 + var6)));
         double var14 = Math.max(0.0F, Math.max(var5 - var3, var3 - (var5 + var7)));
         double var16 = smoothFalloff(Math.hypot(var12, var14), 56.0);
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
      float var27 = this.lastUpdateNanos == 0L ? 1.0F : (float)(1.0 - Math.exp(-(var26 - this.lastUpdateNanos) / 1000000.0 / 90.0));
      this.lastUpdateNanos = var26;
      this.textOffsetX = this.textOffsetX + (var10 - this.textOffsetX) * var27;
      this.textOffsetY = this.textOffsetY + (var11 - this.textOffsetY) * var27;
      if (Math.abs(this.textOffsetX) < 0.01F && Math.abs(this.textOffsetY) < 0.01F && var10 == 0.0F && var11 == 0.0F) {
         this.textOffsetX = this.textOffsetY = 0.0F;
      }
   }

   private static void drawGlowText(
      GuiGraphicsExtractor var0, String var1, float var2, float var3, float var4, double var5, float var7, float var8, double var9
   ) {
      if (!(var5 <= 0.0) || !Float.isNaN(var7)) {
         int var11 = Theme.accent | 0xFF000000;
         float var12 = var3 + 4.6F * var4;
         float var13 = 0.0F;
         int var14 = 0;

         while (var14 < var1.length()) {
            int var15 = var1.codePointAt(var14);
            String var16 = var1.substring(var14, var14 + Character.charCount(var15));
            var14 += var16.length();
            float var17 = FontRenderer.forceClickUiFont(var16, fONT);
            double var18 = Float.isNaN(var7) ? 0.0 : smoothFalloff(Math.hypot(var7 - (var2 + (var13 + var17 / 2.0F) * var4), var8 - var12), 56.0);
            double var20 = Math.max(var5 * 0.8, var18) * var9;
            if (var20 > 0.01 && !var16.isBlank()) {
               var0.pose().pushMatrix();

               try {
                  var0.pose().translate(var2 + var13 * var4, var3);
                  var0.pose().scale(var4, var4);
                  FontRenderer.drawSpacedText(var0, var16, 0, 0, ColorUtil.withAlpha(var11, var20), 2.6F, fONT);
               } finally {
                  var0.pose().popMatrix();
               }
            }

            var13 += var17;
         }
      }
   }

   public enum Style {
      PRIMARY,
      SECONDARY,
      TERTIARY,
      QUATERNARY;
   }
}
