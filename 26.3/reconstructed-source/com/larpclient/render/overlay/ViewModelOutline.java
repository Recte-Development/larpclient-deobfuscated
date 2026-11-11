package com.larpclient.render.overlay;

import com.larpclient.config.GradientOutlineSettings;
import com.larpclient.gui.theme.Theme;
import com.larpclient.render.entity.GradientChamsRenderer;
import com.larpclient.util.data.HandBoundsTracker;
import com.larpclient.util.render.ColorUtil;
import com.mojang.blaze3d.platform.Window;
import java.util.ArrayList;
import net.minecraft.client.Minecraft;
import net.minecraft.world.InteractionHand;

public final class ViewModelOutline {
   private static final GradientChamsRenderer outlineRenderer = new GradientChamsRenderer("ViewModel outline");
   private static InteractionHand primaryHand;
   private static InteractionHand secondaryHand;
   private static boolean active;

   public static void setHands(InteractionHand var0, InteractionHand var1) {
      primaryHand = var0;
      secondaryHand = var1;
   }

   private static boolean canRender() {
      Minecraft var0 = Minecraft.getInstance();
      return HandBoundsTracker.isChatEditingActive()
         && var0.options.getCameraType().isFirstPerson()
         && !var0.gameRenderer.gameRenderState().guiRenderState.isHudHidden;
   }

   public static boolean beginFrame() {
      if (!canRender()) {
         close();
         return false;
      } else {
         active = (boolean)1;
         return outlineRenderer.captureHandScene();
      }
   }

   public static void render() {
      if (!canRender()) {
         close();
      } else {
         Window var0 = Minecraft.getInstance().getWindow();
         double var1 = (double)var0.getWidth() / Math.max(1, var0.getGuiScaledWidth());
         float var3 = (float)Math.max(2.0, var1);
         int var4 = (int)Math.ceil(var3 * 2.0F) + 2;
         ArrayList var5 = new ArrayList(2);

         for (InteractionHand var9 : InteractionHand.values()) {
            HandBoundsTracker.HandBounds var10 = HandBoundsTracker.getBounds(var9);
            if (var10 != null) {
               int var11 = var9 == primaryHand ? ColorUtil.withAlpha(Theme.accent, 0.95) : (var9 == secondaryHand ? -922746881 : 1627389951);
               var5.add(
                  new GradientChamsRenderer.Region(
                     (int)Math.floor(var10.screenX() * var1) - var4,
                     (int)Math.floor(var10.screenY() * var1) - var4,
                     (int)Math.ceil(var10.width() * var1) + var4 * 2,
                     (int)Math.ceil(var10.height() * var1) + var4 * 2,
                     new GradientOutlineSettings.Snapshot(var11, var11, var3, 1.0F, 0.0F, 1.0F, 0.0F, 0.0F, 0.0F, false)
                  )
               );
            }
         }

         outlineRenderer.createPostPipeline(var5);
      }
   }

   public static void close() {
      if (active) {
         active = (boolean)0;
         secondaryHand = null;
         primaryHand = null;
         outlineRenderer.close();
      }
   }

   private ViewModelOutline() {
   }
}
