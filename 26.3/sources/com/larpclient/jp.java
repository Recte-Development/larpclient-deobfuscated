package com.larpclient;

import com.larpclient.module.ViewModelModule;
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

public final class jp {
   private static final long n = 250000000L;
   private static final jp.g[] r = new jp.g[]{new jp.g(), new jp.g()};
   private static float K;
   private static float u;
   private static int v;
   private static int j;
   private static long p;

   public static boolean N() {
      Minecraft var0 = Minecraft.getInstance();
      ViewModelModule var1 = ViewModelModule.t();
      return var1 != null && var1.isRuntimeActive() && var0.player != null && var0.gui.screen() != null && var0.gui.screen().getClass() == ChatScreen.class;
   }

   public static void U() {
      if (N()) {
         Minecraft var0 = Minecraft.getInstance();
         CameraRenderState var1 = var0.gameRenderer.gameRenderState().levelRenderState.cameraRenderState;
         Window var2 = var0.getWindow();
         if (var1 != null && var2.getHeight() > 0) {
            float var3 = (float)Math.tan(Math.toRadians(var1.hudFov) / 2.0);
            u = 1.0F / var3;
            K = u * var2.getHeight() / var2.getWidth();
            v = var2.getGuiScaledWidth();
            j = var2.getGuiScaledHeight();
            p++;
         }
      }
   }

   public static void c(InteractionHand var0, boolean var1, PoseStack var2, ItemStackRenderState var3) {
      if (N() && u != 0.0F) {
         Matrix4f var4 = new Matrix4f(RenderSystem.getModelViewStack()).mul(var2.last().pose());
         jp.g var5 = j(var0, var1);
         Vector3f var6 = new Vector3f();
         var3.visitExtents(var3x -> var5.o(var4.transformPosition(var3x, var6)));
      }
   }

   public static void B(InteractionHand var0, boolean var1, PoseStack var2, ModelPart var3) {
      if (N() && u != 0.0F) {
         Matrix4f var4 = new Matrix4f(RenderSystem.getModelViewStack());
         jp.g var5 = j(var0, var1);
         PoseStack var6 = new PoseStack();
         qd.D(var6, var2.last().pose());
         Vector3f var7 = new Vector3f();
         var3.getExtentsForGui(var6, var3x -> var5.o(var4.transformPosition(var3x, var7)));
      }
   }

   public static void n(InteractionHand var0, boolean var1, PoseStack var2, float[] var3, int var4) {
      if (N() && u != 0.0F) {
         Matrix4f var5 = new Matrix4f(RenderSystem.getModelViewStack()).mul(var2.last().pose());
         jp.g var6 = j(var0, var1);
         Vector3f var7 = new Vector3f();

         for (byte var8 = 0; var8 < var3.length; var8 += 8) {
            var6.o(var5.transformPosition(var4 * var3[var8], var3[var8 + 1], var3[var8 + 2], var7));
         }
      }
   }

   public static jp.c t(InteractionHand var0) {
      jp.g var1 = r[var0.ordinal()];
      if (var1.H != 0 && System.nanoTime() - var1.R <= 250000000L) {
         double var2 = var1.w / var1.H;
         int var4 = (int)Math.floor(var1.Z);
         int var5 = (int)Math.floor(var1.d);
         return new jp.c(
            var0,
            var1.j,
            var4,
            var5,
            Math.max(1, (int)Math.ceil(var1.i) - var4),
            Math.max(1, (int)Math.ceil(var1.G) - var5),
            2.0 * var2 / (K * v),
            2.0 * var2 / (u * j)
         );
      } else {
         return null;
      }
   }

   private static jp.g j(InteractionHand var0, boolean var1) {
      jp.g var2 = r[var0.ordinal()];
      if (var2.n != p) {
         var2.Q(p, var1);
      }

      var2.R = System.nanoTime();
      return var2;
   }

   private jp() {
   }

   public record c(InteractionHand hand, boolean left, int x, int y, int width, int height, double unitsPerPixelX, double unitsPerPixelY) {
      public boolean contains(double var1, double var3) {
         return var1 >= this.x && var3 >= this.y && var1 < this.x + this.width && var3 < this.y + this.height;
      }
   }

   private static final class g {
      long n = -1L;
      long R;
      boolean j;
      int H;
      double Z;
      double d;
      double i;
      double G;
      double w;

      void Q(long var1, boolean var3) {
         this.n = var1;
         this.j = var3;
         this.H = 0;
         this.w = 0.0;
         this.Z = this.d = Double.MAX_VALUE;
         this.i = this.G = -Double.MAX_VALUE;
      }

      void o(Vector3fc var1) {
         if (!(var1.z() > -0.01F)) {
            float var2 = -var1.z();
            double var3 = (jp.K * var1.x() / var2 + 1.0F) / 2.0F * jp.v;
            double var5 = (1.0F - jp.u * var1.y() / var2) / 2.0F * jp.j;
            this.Z = Math.min(this.Z, var3);
            this.i = Math.max(this.i, var3);
            this.d = Math.min(this.d, var5);
            this.G = Math.max(this.G, var5);
            this.w += var2;
            this.H++;
         }
      }
   }
}
