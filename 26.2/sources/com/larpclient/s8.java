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

public final class s8 {
   private static final long n = 250000000L;
   private static final s8.f[] Y = new s8.f[]{new s8.f(), new s8.f()};
   private static float N;
   private static float o;
   private static int V;
   private static int a;
   private static long w;

   public static boolean A() {
      Minecraft var0 = Minecraft.getInstance();
      ViewModelModule var1 = ViewModelModule.b();
      return var1 != null && var1.isRuntimeActive() && var0.player != null && var0.gui.screen() != null && var0.gui.screen().getClass() == ChatScreen.class;
   }

   public static void c() {
      if (A()) {
         Minecraft var0 = Minecraft.getInstance();
         CameraRenderState var1 = var0.gameRenderer.gameRenderState().levelRenderState.cameraRenderState;
         Window var2 = var0.getWindow();
         if (var1 != null && var2.getHeight() > 0) {
            float var3 = (float)Math.tan(Math.toRadians(var1.hudFov) / 2.0);
            o = 1.0F / var3;
            N = o * var2.getHeight() / var2.getWidth();
            V = var2.getGuiScaledWidth();
            a = var2.getGuiScaledHeight();
            w++;
         }
      }
   }

   public static void O(InteractionHand var0, boolean var1, PoseStack var2, ItemStackRenderState var3) {
      if (A() && o != 0.0F) {
         Matrix4f var4 = new Matrix4f(RenderSystem.getModelViewStack()).mul(var2.last().pose());
         s8.f var5 = r(var0, var1);
         Vector3f var6 = new Vector3f();
         var3.visitExtents(var3x -> var5.W(var4.transformPosition(var3x, var6)));
      }
   }

   public static void H(InteractionHand var0, boolean var1, PoseStack var2, ModelPart var3) {
      if (A() && o != 0.0F) {
         Matrix4f var4 = new Matrix4f(RenderSystem.getModelViewStack());
         s8.f var5 = r(var0, var1);
         PoseStack var6 = new PoseStack();
         uf.o(var6, var2.last().pose());
         Vector3f var7 = new Vector3f();
         var3.getExtentsForGui(var6, var3x -> var5.W(var4.transformPosition(var3x, var7)));
      }
   }

   public static void H(InteractionHand var0, boolean var1, PoseStack var2, float[] var3, int var4) {
      if (A() && o != 0.0F) {
         Matrix4f var5 = new Matrix4f(RenderSystem.getModelViewStack()).mul(var2.last().pose());
         s8.f var6 = r(var0, var1);
         Vector3f var7 = new Vector3f();

         for (byte var8 = 0; var8 < var3.length; var8 += 8) {
            var6.W(var5.transformPosition(var4 * var3[var8], var3[var8 + 1], var3[var8 + 2], var7));
         }
      }
   }

   public static s8.v W(InteractionHand var0) {
      s8.f var1 = Y[var0.ordinal()];
      if (var1.Z != 0 && System.nanoTime() - var1.M <= 250000000L) {
         double var2 = var1.K / var1.Z;
         int var4 = (int)Math.floor(var1.v);
         int var5 = (int)Math.floor(var1.l);
         return new s8.v(
            var0,
            var1.t,
            var4,
            var5,
            Math.max(1, (int)Math.ceil(var1.H) - var4),
            Math.max(1, (int)Math.ceil(var1.x) - var5),
            2.0 * var2 / (N * V),
            2.0 * var2 / (o * a)
         );
      } else {
         return null;
      }
   }

   private static s8.f r(InteractionHand var0, boolean var1) {
      s8.f var2 = Y[var0.ordinal()];
      if (var2.p != w) {
         var2.r(w, var1);
      }

      var2.M = System.nanoTime();
      return var2;
   }

   private s8() {
   }

   private static final class f {
      long p = -1L;
      long M;
      boolean t;
      int Z;
      double v;
      double l;
      double H;
      double x;
      double K;

      void r(long var1, boolean var3) {
         this.p = var1;
         this.t = var3;
         this.Z = 0;
         this.K = 0.0;
         this.v = this.l = Double.MAX_VALUE;
         this.H = this.x = -Double.MAX_VALUE;
      }

      void W(Vector3fc var1) {
         if (!(var1.z() > -0.01F)) {
            float var2 = -var1.z();
            double var3 = (s8.N * var1.x() / var2 + 1.0F) / 2.0F * s8.V;
            double var5 = (1.0F - s8.o * var1.y() / var2) / 2.0F * s8.a;
            this.v = Math.min(this.v, var3);
            this.H = Math.max(this.H, var3);
            this.l = Math.min(this.l, var5);
            this.x = Math.max(this.x, var5);
            this.K += var2;
            this.Z++;
         }
      }
   }

   public record v(InteractionHand hand, boolean left, int x, int y, int width, int height, double unitsPerPixelX, double unitsPerPixelY) {
      public boolean contains(double var1, double var3) {
         return var1 >= this.x && var3 >= this.y && var1 < this.x + this.width && var3 < this.y + this.height;
      }
   }
}
