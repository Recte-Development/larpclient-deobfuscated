package com.larpclient;

import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.buffers.Std140Builder;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.pipeline.RenderPipeline.Snippet;
import com.mojang.blaze3d.platform.DepthTestFunction;
import com.mojang.blaze3d.systems.CommandEncoder;
import com.mojang.blaze3d.systems.RenderPass;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.textures.FilterMode;
import com.mojang.blaze3d.vertex.VertexFormat.class_5596;
import java.util.OptionalInt;
import net.minecraft.class_1041;
import net.minecraft.class_10789;
import net.minecraft.class_10799;
import net.minecraft.class_11231;
import net.minecraft.class_11241;
import net.minecraft.class_290;
import net.minecraft.class_2960;
import net.minecraft.class_310;
import net.minecraft.class_332;
import net.minecraft.class_6367;
import org.joml.Matrix3x2f;
import org.joml.Vector4f;
import org.lwjgl.system.MemoryStack;

public final class tg {
   private static final RenderPipeline U = RenderPipeline.builder(new Snippet[0])
      .withLocation(class_2960.method_60655("larp-client", "gui_aurora"))
      .withVertexShader(class_2960.method_60655("larp-client", "post/fullscreen"))
      .withFragmentShader(class_2960.method_60655("larp-client", "gui/aurora"))
      .withUniform("AuroraParameters", class_10789.field_60031)
      .withVertexFormat(class_290.field_60033, class_5596.field_27379)
      .withDepthTestFunction(DepthTestFunction.NO_DEPTH_TEST)
      .withDepthWrite(false)
      .withCull(false)
      .build();
   public static final int O = 112;
   private static final long Z = System.nanoTime();
   private static class_6367 w;
   private static GpuBuffer B;
   private static int D = -1;
   private static boolean X;
   private static long A;

   private tg() {
   }

   public static boolean E(class_332 var0, int var1, int var2, double var3, int var5, int var6, boolean var7, boolean var8, tg.c var9, double var10) {
      if (var1 > 0 && var2 > 0 && !(var3 <= 0.0)) {
         if (D != t_.g) {
            d();
            D = t_.g;
            X = (boolean)0;
         }

         if (X) {
            return false;
         }

         try {
            class_1041 var12 = class_310.method_1551().method_22683();
            double var13 = Math.min(1.0, 640.0 / Math.max(1, var12.method_4489()));
            int var15 = Math.max(1, (int)Math.round(var12.method_4489() * var13));
            int var16 = Math.max(1, (int)Math.round(var12.method_4506() * var13));
            if (w == null || w.field_1482 != var15 || w.field_1481 != var16) {
               d();
               f2.a(U);
               w = new class_6367("Larp ClickGUI aurora", var15, var16, false);
               B = RenderSystem.getDevice().createBuffer(() -> "Larp aurora parameters", 136, 112L);
               q8.v("GUI", "Aurora backdrop target {}x{}", var15, var16);
            }

            long var17 = System.nanoTime();
            if (A == 0L || var17 - A >= 33000000L) {
               float var19 = var8 ? (float)((var17 - Z) / 1.0E9 % 20000.0) : 0.0F;
               CommandEncoder var20 = RenderSystem.getDevice().createCommandEncoder();
               MemoryStack var21 = MemoryStack.stackPush();

               try {
                  Std140Builder var22 = Std140Builder.onStack(var21, 112);

                  for (Vector4f var26 : t(var15, var16, var19, var5, var6, var7, var9, var10)) {
                     var22.putVec4(var26);
                  }

                  var20.writeToBuffer(B.slice(), var22.get());
               } catch (Throwable var30) {
                  if (var21 != null) {
                     try {
                        var21.close();
                     } catch (Throwable var28) {
                        var30.addSuppressed(var28);
                     }
                  }

                  throw var30;
               }

               if (var21 != null) {
                  var21.close();
               }

               RenderPass var32 = var20.createRenderPass(() -> "Larp ClickGUI aurora", w.method_71639(), OptionalInt.empty());

               try {
                  yf.l(var32, U);
                  var32.setUniform("AuroraParameters", B.slice());
                  var32.draw(0, 3);
               } catch (Throwable var29) {
                  if (var32 != null) {
                     try {
                        var32.close();
                     } catch (Throwable var27) {
                        var29.addSuppressed(var27);
                     }
                  }

                  throw var29;
               }

               if (var32 != null) {
                  var32.close();
               }

               A = var17;
            }

            var0.field_59826
               .method_70919(
                  new class_11241(
                     class_10799.field_56883,
                     class_11231.method_70900(w.method_71639(), RenderSystem.getSamplerCache().method_75294(FilterMode.LINEAR)),
                     new Matrix3x2f(var0.method_51448()),
                     0,
                     0,
                     var1,
                     var2,
                     0.0F,
                     1.0F,
                     1.0F,
                     0.0F,
                     ei.D(-1, var3),
                     var0.field_44659.method_70863()
                  )
               );
            return true;
         } catch (RuntimeException var31) {
            X = (boolean)1;
            d();
            q8.u("GUI", var9 + " backdrop unavailable; using the plain dim until resource reload", var31);
            return false;
         }
      } else {
         return true;
      }
   }

   public static Vector4f[] l(int var0, int var1, float var2, int var3, int var4, boolean var5) {
      return t(var0, var1, var2, var3, var4, var5, tg.c.O, 0.55);
   }

   public static Vector4f[] t(int var0, int var1, float var2, int var3, int var4, boolean var5, tg.c var6, double var7) {
      float var9 = (float)Math.clamp(var7, 0.0, 1.0) * (var5 ? 0.73F : 1.0F);
      int var10 = ei.W(var3, var4, 0.5);
      int var11 = ei.W(var3, -1, 0.35);
      float var12 = (float)Math.clamp(Math.max(m(var3), m(var4)) / 0.5, 0.0, 1.0);
      float var13 = var5 ? 0.12F + 0.14F * var12 : 0.14F + 0.18F * var12;
      return new Vector4f[]{
         new Vector4f(var0, var1, var2, 0.0F),
         d(var3, 0.95F),
         d(var4, 0.85F),
         d(var10, 0.7F),
         d(var11, 0.55F),
         var5 ? new Vector4f(0.086F, 0.086F, 0.1F, var9) : new Vector4f(0.03F, 0.03F, 0.04F, var9),
         new Vector4f(var13, 0.6F, var6.ordinal(), 0.0F)
      };
   }

   static double m(int var0) {
      int var1 = var0 >> 16 & 0xFF;
      int var2 = var0 >> 8 & 0xFF;
      int var3 = var0 & 0xFF;
      return (Math.max(var1, Math.max(var2, var3)) - Math.min(var1, Math.min(var2, var3))) / 255.0;
   }

   private static Vector4f d(int var0, float var1) {
      return new Vector4f((var0 >> 16 & 0xFF) / 255.0F, (var0 >> 8 & 0xFF) / 255.0F, (var0 & 0xFF) / 255.0F, var1);
   }

   public static void d() {
      if (w != null) {
         w.method_1238();
         w = null;
      }

      if (B != null) {
         B.close();
         B = null;
      }

      A = 0L;
   }

   public enum c {
      O,
      o,
      I;
   }
}
