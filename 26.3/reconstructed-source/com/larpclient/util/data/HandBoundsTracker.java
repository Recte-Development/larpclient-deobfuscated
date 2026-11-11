package com.larpclient.util.data;

import com.larpclient.module.render.ViewModelModule;
import com.larpclient.util.render.PoseStackUtil;
import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.world.InteractionHand;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector3fc;

public final class HandBoundsTracker {
   private static final long sTALE_NANOS = 250000000L;
   private static final HandBoundsTracker.HandExtents[] handBounds = new HandBoundsTracker.HandExtents[]{
      new HandBoundsTracker.HandExtents(), new HandBoundsTracker.HandExtents()
   };
   private static float aspectScale;
   private static float focalScale;
   private static int guiWidth;
   private static int guiHeight;
   private static long frameId;

   public static boolean isChatEditingActive() {
      Minecraft var0 = Minecraft.getInstance();
      ViewModelModule var1 = ViewModelModule.getInstance();
      return var1 != null && var1.isRuntimeActive() && var0.player != null && var0.gui.screen() != null && var0.gui.screen().getClass() == ChatScreen.class;
   }

   public static void updateProjection() {
      if (isChatEditingActive()) {
         Minecraft var0 = Minecraft.getInstance();
         CameraRenderState var1 = var0.gameRenderer.gameRenderState().levelRenderState.cameraRenderState;
         Window var2 = var0.getWindow();
         if (var1 != null && var2.getHeight() > 0) {
            float var3 = (float)Math.tan(Math.toRadians(var1.hudFov) / 2.0);
            focalScale = 1.0F / var3;
            aspectScale = focalScale * var2.getHeight() / var2.getWidth();
            guiWidth = var2.getGuiScaledWidth();
            guiHeight = var2.getGuiScaledHeight();
            frameId++;
         }
      }
   }

   public static void trackItem(InteractionHand var0, boolean var1, PoseStack var2, ItemStackRenderState var3) {
      if (isChatEditingActive() && focalScale != 0.0F) {
         Matrix4f var4 = new Matrix4f(RenderSystem.getModelViewStack()).mul(var2.last().pose());
         HandBoundsTracker.HandExtents var5 = getHandBounds(var0, var1);
         Vector3f var6 = new Vector3f();
         var3.visitExtents(var3x -> var5.addPoint(var4.transformPosition(var3x, var6)));
      }
   }

   public static void trackModelPart(InteractionHand var0, boolean var1, PoseStack var2, ModelPart var3) {
      if (isChatEditingActive() && focalScale != 0.0F) {
         Matrix4f var4 = new Matrix4f(RenderSystem.getModelViewStack());
         HandBoundsTracker.HandExtents var5 = getHandBounds(var0, var1);
         PoseStack var6 = new PoseStack();
         PoseStackUtil.multiply(var6, var2.last().pose());
         Vector3f var7 = new Vector3f();
         var3.getExtentsForGui(var6, var3x -> var5.addPoint(var4.transformPosition(var3x, var7)));
      }
   }

   public static void trackVertices(InteractionHand var0, boolean var1, PoseStack var2, float[] var3, int var4) {
      if (isChatEditingActive() && focalScale != 0.0F) {
         Matrix4f var5 = new Matrix4f(RenderSystem.getModelViewStack()).mul(var2.last().pose());
         HandBoundsTracker.HandExtents var6 = getHandBounds(var0, var1);
         Vector3f var7 = new Vector3f();

         for (byte var8 = 0; var8 < var3.length; var8 += 8) {
            var6.addPoint(var5.transformPosition(var4 * var3[var8], var3[var8 + 1], var3[var8 + 2], var7));
         }
      }
   }

   public static HandBoundsTracker.HandBounds getBounds(InteractionHand var0) {
      HandBoundsTracker.HandExtents var1 = handBounds[var0.ordinal()];
      if (var1.pointCount != 0 && System.nanoTime() - var1.lastTouchNanos <= 250000000L) {
         double var2 = var1.depthSum / var1.pointCount;
         int var4 = (int)Math.floor(var1.minX);
         int var5 = (int)Math.floor(var1.minY);
         return new HandBoundsTracker.HandBounds(
            var0,
            var1.left,
            var4,
            var5,
            Math.max(1, (int)Math.ceil(var1.maxX) - var4),
            Math.max(1, (int)Math.ceil(var1.maxY) - var5),
            2.0 * var2 / (aspectScale * guiWidth),
            2.0 * var2 / (focalScale * guiHeight)
         );
      } else {
         return null;
      }
   }

   private static HandBoundsTracker.HandExtents getHandBounds(InteractionHand var0, boolean var1) {
      HandBoundsTracker.HandExtents var2 = handBounds[var0.ordinal()];
      if (var2.frameId != frameId) {
         var2.reset(frameId, var1);
      }

      var2.lastTouchNanos = System.nanoTime();
      return var2;
   }

   private HandBoundsTracker() {
   }

   public record HandBounds(InteractionHand hand, boolean left, int x, int y, int width, int height, double unitsPerPixelX, double unitsPerPixelY) {
      private final int screenX;
      private final int screenY;

      public HandBounds(InteractionHand hand, boolean left, int x, int y, int width, int height, double unitsPerPixelX, double unitsPerPixelY) {
         this.hand = hand;
         this.left = left;
         this.screenX = x;
         this.screenY = y;
         this.width = width;
         this.height = height;
         this.unitsPerPixelX = unitsPerPixelX;
         this.unitsPerPixelY = unitsPerPixelY;
      }

      public boolean contains(double var1, double var3) {
         return var1 >= this.screenX && var3 >= this.screenY && var1 < this.screenX + this.width && var3 < this.screenY + this.height;
      }
   }

   private static final class HandExtents {
      long frameId = -1L;
      long lastTouchNanos;
      boolean left;
      int pointCount;
      double minX;
      double minY;
      double maxX;
      double maxY;
      double depthSum;

      void reset(long var1, boolean var3) {
         this.frameId = var1;
         this.left = var3;
         this.pointCount = 0;
         this.depthSum = 0.0;
         this.minX = this.minY = Double.MAX_VALUE;
         this.maxX = this.maxY = -Double.MAX_VALUE;
      }

      void addPoint(Vector3fc var1) {
         if (!(var1.z() > -0.01F)) {
            float var2 = -var1.z();
            double var3 = (HandBoundsTracker.aspectScale * var1.x() / var2 + 1.0F) / 2.0F * HandBoundsTracker.guiWidth;
            double var5 = (1.0F - HandBoundsTracker.focalScale * var1.y() / var2) / 2.0F * HandBoundsTracker.guiHeight;
            this.minX = Math.min(this.minX, var3);
            this.maxX = Math.max(this.maxX, var3);
            this.minY = Math.min(this.minY, var5);
            this.maxY = Math.max(this.maxY, var5);
            this.depthSum += var2;
            this.pointCount++;
         }
      }
   }
}
