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

public final class x3 {
   private static final long p = 250000000L;
   private static final x3.c[] D = new x3.c[]{new x3.c(), new x3.c()};
   private static float o;
   private static float A;
   private static int L;
   private static int f;
   private static long J;

   public static boolean B() {
      Minecraft var0 = Minecraft.getInstance();
      ViewModelModule var1 = ViewModelModule.Z();
      return var1 != null && var1.isRuntimeActive() && var0.player != null && var0.gui.screen() != null && var0.gui.screen().getClass() == ChatScreen.class;
   }

   public static void M() {
      if (B()) {
         Minecraft var0 = Minecraft.getInstance();
         CameraRenderState var1 = var0.gameRenderer.gameRenderState().levelRenderState.cameraRenderState;
         Window var2 = var0.getWindow();
         if (var1 != null && var2.getHeight() > 0) {
            float var3 = (float)Math.tan(Math.toRadians(var1.hudFov) / 2.0);
            A = 1.0F / var3;
            o = A * var2.getHeight() / var2.getWidth();
            L = var2.getGuiScaledWidth();
            f = var2.getGuiScaledHeight();
            J++;
         }
      }
   }

   public static void f(InteractionHand var0, boolean var1, PoseStack var2, ItemStackRenderState var3) {
      if (B() && A != 0.0F) {
         Matrix4f var4 = new Matrix4f(RenderSystem.getModelViewStack()).mul(var2.last().pose());
         x3.c var5 = G(var0, var1);
         Vector3f var6 = new Vector3f();
         var3.visitExtents(var3x -> var5.N(var4.transformPosition(var3x, var6)));
      }
   }

   public static void B(InteractionHand var0, boolean var1, PoseStack var2, ModelPart var3) {
      if (B() && A != 0.0F) {
         Matrix4f var4 = new Matrix4f(RenderSystem.getModelViewStack());
         x3.c var5 = G(var0, var1);
         PoseStack var6 = new PoseStack();
         u.v(var6, var2.last().pose());
         Vector3f var7 = new Vector3f();
         var3.getExtentsForGui(var6, var3x -> var5.N(var4.transformPosition(var3x, var7)));
      }
   }

   public static void X(InteractionHand var0, boolean var1, PoseStack var2, float[] var3, int var4) {
      if (B() && A != 0.0F) {
         Matrix4f var5 = new Matrix4f(RenderSystem.getModelViewStack()).mul(var2.last().pose());
         x3.c var6 = G(var0, var1);
         Vector3f var7 = new Vector3f();

         for (byte var8 = 0; var8 < var3.length; var8 += 8) {
            var6.N(var5.transformPosition(var4 * var3[var8], var3[var8 + 1], var3[var8 + 2], var7));
         }
      }
   }

   public static x3.n i(InteractionHand var0) {
      x3.c var1 = D[var0.ordinal()];
      if (var1.M != 0 && System.nanoTime() - var1.x <= 250000000L) {
         double var2 = var1.z / var1.M;
         int var4 = (int)Math.floor(var1.t);
         int var5 = (int)Math.floor(var1.e);
         return new x3.n(
            var0,
            var1.P,
            var4,
            var5,
            Math.max(1, (int)Math.ceil(var1.C) - var4),
            Math.max(1, (int)Math.ceil(var1.S) - var5),
            2.0 * var2 / (o * L),
            2.0 * var2 / (A * f)
         );
      } else {
         return null;
      }
   }

   private static x3.c G(InteractionHand var0, boolean var1) {
      x3.c var2 = D[var0.ordinal()];
      if (var2.h != J) {
         var2.p(J, var1);
      }

      var2.x = System.nanoTime();
      return var2;
   }

   private x3() {
   }

   private static final class c {
      long h = -1L;
      long x;
      boolean P;
      int M;
      double t;
      double e;
      double C;
      double S;
      double z;

      void p(long var1, boolean var3) {
         this.h = var1;
         this.P = var3;
         this.M = 0;
         this.z = 0.0;
         this.t = this.e = Double.MAX_VALUE;
         this.C = this.S = -Double.MAX_VALUE;
      }

      void N(Vector3fc var1) {
         if (!(var1.z() > -0.01F)) {
            float var2 = -var1.z();
            double var3 = (x3.o * var1.x() / var2 + 1.0F) / 2.0F * x3.L;
            double var5 = (1.0F - x3.A * var1.y() / var2) / 2.0F * x3.f;
            this.t = Math.min(this.t, var3);
            this.C = Math.max(this.C, var3);
            this.e = Math.min(this.e, var5);
            this.S = Math.max(this.S, var5);
            this.z += var2;
            this.M++;
         }
      }
   }

   public record n(InteractionHand hand, boolean left, int x, int y, int width, int height, double unitsPerPixelX, double unitsPerPixelY) {
      public boolean contains(double var1, double var3) {
         return var1 >= this.x && var3 >= this.y && var1 < this.x + this.width && var3 < this.y + this.height;
      }
   }
}
