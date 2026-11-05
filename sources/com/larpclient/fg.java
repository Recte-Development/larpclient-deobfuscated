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

public final class fg {
   private static final long g = 250000000L;
   private static final fg.z[] X = new fg.z[]{new fg.z(), new fg.z()};
   private static float d;
   private static float n;
   private static int P;
   private static int s;
   private static long M;

   public static boolean h() {
      Minecraft var0 = Minecraft.getInstance();
      ViewModelModule var1 = ViewModelModule.p();
      return var1 != null && var1.isRuntimeActive() && var0.player != null && var0.gui.screen() != null && var0.gui.screen().getClass() == ChatScreen.class;
   }

   public static void Z() {
      if (h()) {
         Minecraft var0 = Minecraft.getInstance();
         CameraRenderState var1 = var0.gameRenderer.gameRenderState().levelRenderState.cameraRenderState;
         Window var2 = var0.getWindow();
         if (var1 != null && var2.getHeight() > 0) {
            float var3 = (float)Math.tan(Math.toRadians(var1.hudFov) / 2.0);
            n = 1.0F / var3;
            d = n * var2.getHeight() / var2.getWidth();
            P = var2.getGuiScaledWidth();
            s = var2.getGuiScaledHeight();
            M++;
         }
      }
   }

   public static void E(InteractionHand var0, boolean var1, PoseStack var2, ItemStackRenderState var3) {
      if (h() && n != 0.0F) {
         Matrix4f var4 = new Matrix4f(RenderSystem.getModelViewStack()).mul(var2.last().pose());
         fg.z var5 = F(var0, var1);
         Vector3f var6 = new Vector3f();
         var3.visitExtents(var3x -> var5.S(var4.transformPosition(var3x, var6)));
      }
   }

   public static void J(InteractionHand var0, boolean var1, PoseStack var2, ModelPart var3) {
      if (h() && n != 0.0F) {
         Matrix4f var4 = new Matrix4f(RenderSystem.getModelViewStack());
         fg.z var5 = F(var0, var1);
         PoseStack var6 = new PoseStack();
         fx.Z(var6, var2.last().pose());
         Vector3f var7 = new Vector3f();
         var3.getExtentsForGui(var6, var3x -> var5.S(var4.transformPosition(var3x, var7)));
      }
   }

   public static void K(InteractionHand var0, boolean var1, PoseStack var2, float[] var3, int var4) {
      if (h() && n != 0.0F) {
         Matrix4f var5 = new Matrix4f(RenderSystem.getModelViewStack()).mul(var2.last().pose());
         fg.z var6 = F(var0, var1);
         Vector3f var7 = new Vector3f();

         for (byte var8 = 0; var8 < var3.length; var8 += 8) {
            var6.S(var5.transformPosition(var4 * var3[var8], var3[var8 + 1], var3[var8 + 2], var7));
         }
      }
   }

   public static fg.v T(InteractionHand var0) {
      fg.z var1 = X[var0.ordinal()];
      if (var1.k != 0 && System.nanoTime() - var1.I <= 250000000L) {
         double var2 = var1.n / var1.k;
         int var4 = (int)Math.floor(var1.a);
         int var5 = (int)Math.floor(var1.N);
         return new fg.v(
            var0,
            var1.A,
            var4,
            var5,
            Math.max(1, (int)Math.ceil(var1.J) - var4),
            Math.max(1, (int)Math.ceil(var1.L) - var5),
            2.0 * var2 / (d * P),
            2.0 * var2 / (n * s)
         );
      } else {
         return null;
      }
   }

   private static fg.z F(InteractionHand var0, boolean var1) {
      fg.z var2 = X[var0.ordinal()];
      if (var2.y != M) {
         var2.J(M, var1);
      }

      var2.I = System.nanoTime();
      return var2;
   }

   private fg() {
   }

   public record v(InteractionHand hand, boolean left, int x, int y, int width, int height, double unitsPerPixelX, double unitsPerPixelY) {
      public boolean contains(double var1, double var3) {
         return var1 >= this.x && var3 >= this.y && var1 < this.x + this.width && var3 < this.y + this.height;
      }
   }

   private static final class z {
      long y = -1L;
      long I;
      boolean A;
      int k;
      double a;
      double N;
      double J;
      double L;
      double n;

      void J(long var1, boolean var3) {
         this.y = var1;
         this.A = var3;
         this.k = 0;
         this.n = 0.0;
         this.a = this.N = Double.MAX_VALUE;
         this.J = this.L = -Double.MAX_VALUE;
      }

      void S(Vector3fc var1) {
         if (!(var1.z() > -0.01F)) {
            float var2 = -var1.z();
            double var3 = (fg.d * var1.x() / var2 + 1.0F) / 2.0F * fg.P;
            double var5 = (1.0F - fg.n * var1.y() / var2) / 2.0F * fg.s;
            this.a = Math.min(this.a, var3);
            this.J = Math.max(this.J, var3);
            this.N = Math.min(this.N, var5);
            this.L = Math.max(this.L, var5);
            this.n += var2;
            this.k++;
         }
      }
   }
}
