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
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.EnumMap;
import java.util.Map;
import java.util.OptionalInt;
import java.util.Random;
import net.minecraft.class_1011;
import net.minecraft.class_1041;
import net.minecraft.class_10789;
import net.minecraft.class_10799;
import net.minecraft.class_11231;
import net.minecraft.class_11241;
import net.minecraft.class_290;
import net.minecraft.class_2960;
import net.minecraft.class_310;
import net.minecraft.class_3298;
import net.minecraft.class_332;
import net.minecraft.class_6367;
import org.joml.Matrix3x2f;
import org.joml.Vector4f;
import org.lwjgl.system.MemoryStack;

public final class ly {
   private static final RenderPipeline Z = RenderPipeline.builder(new Snippet[0])
      .withLocation(class_2960.method_60655("larp-client", "menu_nebula"))
      .withVertexShader(class_2960.method_60655("larp-client", "post/fullscreen"))
      .withFragmentShader(class_2960.method_60655("larp-client", "menu/nebula"))
      .withUniform("MenuParameters", class_10789.field_60031)
      .withVertexFormat(class_290.field_60033, class_5596.field_27379)
      .withDepthTestFunction(DepthTestFunction.NO_DEPTH_TEST)
      .withDepthWrite(false)
      .withCull(false)
      .build();
   private static final long y = System.nanoTime();
   private static final double[][] M = new double[140][4];
   private static class_6367 E;
   private static GpuBuffer s;
   private static int C = -1;
   private static boolean Y;
   private static long g;
   private static float I;
   private static float G;
   private static final Map<tl, Boolean> d;
   private static int J;
   private static tl c;
   private static tl V;
   private static final qb t;
   private static final uh D;

   private ly() {
   }

   public static void p(class_332 var0, int var1, int var2, boolean var3) {
      i(var0, var1, var2, var3 ? 0.8 : 1.0, true);
   }

   private static void i(class_332 var0, int var1, int var2, double var3, boolean var5) {
      if (var1 > 0 && var2 > 0) {
         class_310 var6 = class_310.method_1551();
         double var7 = var5 ? (System.nanoTime() - y) / 1.0E9 : 0.0;
         if (C != t_.g) {
            y();
            C = t_.g;
            Y = (boolean)0;
         }

         byte var9 = 0;
         if (!Y) {
            try {
               class_1041 var10 = var6.method_22683();
               int var11 = var10.method_4489();
               int var12 = var10.method_4506();
               double var13 = Math.min(1.0, 900.0 / Math.max(1, var11));
               int var15 = Math.max(1, (int)Math.round(var11 * var13));
               int var16 = Math.max(1, (int)Math.round(var12 * var13));
               if (E == null || E.field_1482 != var15 || E.field_1481 != var16) {
                  y();
                  f2.a(Z);
                  E = new class_6367("Larp website nebula", var15, var16, false);
                  s = RenderSystem.getDevice().createBuffer(() -> "Larp menu parameters", 136, 32L);
               }

               long var17 = System.nanoTime();
               if (g == 0L || var17 - g >= 33000000L) {
                  float var19 = var5 ? (float)(var6.field_1729.method_1603() / Math.max(1, var10.method_4480()) - 0.5) : 0.0F;
                  float var20 = var5 ? (float)(0.5 - var6.field_1729.method_1604() / Math.max(1, var10.method_4507())) : 0.0F;
                  if (var5) {
                     I = I + (var19 - I) * 0.025F;
                     G = G + (var20 - G) * 0.025F;
                  } else {
                     G = 0.0F;
                     I = 0.0F;
                  }

                  CommandEncoder var21 = RenderSystem.getDevice().createCommandEncoder();
                  MemoryStack var22 = MemoryStack.stackPush();

                  try {
                     ByteBuffer var23 = Std140Builder.onStack(var22, 32)
                        .putVec4(new Vector4f(var15, var16, (float)var7, 0.0F))
                        .putVec4(new Vector4f(I, G, 0.0F, 0.0F))
                        .get();
                     var21.writeToBuffer(s.slice(), var23);
                  } catch (Throwable var28) {
                     if (var22 != null) {
                        try {
                           var22.close();
                        } catch (Throwable var26) {
                           var28.addSuppressed(var26);
                        }
                     }

                     throw var28;
                  }

                  if (var22 != null) {
                     var22.close();
                  }

                  RenderPass var35 = var21.createRenderPass(() -> "Larp menu nebula", E.method_71639(), OptionalInt.empty());

                  try {
                     yf.l(var35, Z);
                     var35.setUniform("MenuParameters", s.slice());
                     var35.draw(0, 3);
                  } catch (Throwable var27) {
                     if (var35 != null) {
                        try {
                           var35.close();
                        } catch (Throwable var25) {
                           var27.addSuppressed(var25);
                        }
                     }

                     throw var27;
                  }

                  if (var35 != null) {
                     var35.close();
                  }

                  g = var17;
               }

               var0.field_59826
                  .method_70919(
                     new class_11241(
                        class_10799.field_56883,
                        class_11231.method_70900(E.method_71639(), RenderSystem.getSamplerCache().method_75294(FilterMode.LINEAR)),
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
                        null
                     )
                  );
               var9 = 1;
            } catch (RuntimeException var29) {
               Y = (boolean)1;
               y();
               q8.u("Menu", "Nebula unavailable; using dark blue fallback until resource reload", var29);
            }
         }

         if (var9 == 0) {
            gu.R(var0, 0, 0, var1, var2, ei.D(-16116958, var3), ei.D(-16118252, var3));
         }

         double var30 = Math.max(1, var6.method_22683().method_4495());
         int var31 = Math.min(140, Math.max(1, (int)Math.round(var1 * var2 * var30 * var30 / 10000.0)));

         for (int var32 = 0; var32 < var31; var32++) {
            double[] var14 = M[var32];
            double var33 = (0.2 + (1.0 + Math.sin(var7 * 0.6 + var14[3])) * 0.18) * 0.26;
            int var34 = Math.max(1, (int)Math.round(var14[2] * 2.0 / var30));
            gu.Z(var0, (int)(var14[0] * var1), (int)(var14[1] * var2), var34, var34, ei.D(-4201985, var33 * var3));
         }
      }
   }

   public static double i() {
      return D.H();
   }

   public static double D() {
      return D.U();
   }

   public static void z(class_332 var0, int var1, int var2, tl var3, boolean var4, boolean var5) {
      if (!(class_310.method_1551().field_1755 instanceof lu)) {
         gc.C(var0, var1, var2);
         if (var4) {
            gu.Z(var0, 0, 0, var1, var2, -2012607472);
         }
      } else {
         class_310 var6 = class_310.method_1551();
         class_1041 var7 = var6.method_22683();
         D.t(
            var6.field_1729.method_1603() / Math.max(1, var7.method_4480()) * 2.0 - 1.0,
            var6.field_1729.method_1604() / Math.max(1, var7.method_4507()) * 2.0 - 1.0,
            System.nanoTime(),
            var5
         );
         if (c != var3) {
            V = c;
            c = var3;
            t.w(V != null && var5 ? 0.0 : 1.0);
         }

         double var8 = var5 ? t.a(1.0, 260) : 1.0;
         if (var8 < 0.999 && V != null) {
            n(var0, V, 0, 0, var1, var2, 1.0, var5);
         }

         n(var0, var3, 0, 0, var1, var2, var8, var5);
         if (var8 >= 0.999) {
            V = null;
         }

         if (var4) {
            gu.Z(var0, 0, 0, var1, var2, -2012607472);
         }
      }
   }

   public static void n(class_332 var0, tl var1, int var2, int var3, int var4, int var5, double var6, boolean var8) {
      if (var4 > 0 && var5 > 0 && !(var6 <= 0.0)) {
         if (var1 == tl.t) {
            var0.method_51448().pushMatrix();

            try {
               var0.method_51448().translate(var2, var3);
               if (var8) {
                  var0.method_51448().translate((float)(var4 * 0.0225 * (D.H() - 1.0)), (float)(var5 * 0.0225 * (D.U() - 1.0)));
                  var0.method_51448().scale(1.045F, 1.045F);
               }

               i(var0, var4, var5, var6, LarpClient.b().e().F().k);
            } finally {
               var0.method_51448().popMatrix();
            }
         } else {
            if (J != t_.g) {
               J = t_.g;
               d.clear();
            }

            class_2960 var9 = class_2960.method_60655("larp-client", "textures/menu/" + var1.X + ".png");
            boolean var10 = d.computeIfAbsent(var1, var2x -> {
               try {
                  class_3298 var3x = (class_3298)class_310.method_1551().method_1478().method_14486(var9).orElseThrow();

                  try (InputStream var4x = var3x.method_14482()) {
                     class_1011 var5x = class_1011.method_4309(var4x);

                     try {
                        if (var5x.method_4307() != 1672 || var5x.method_4323() != 941) {
                           throw new IOException("Wallpaper dimensions changed");
                        }
                     } catch (Throwable var10x) {
                        if (var5x != null) {
                           try {
                              var5x.close();
                           } catch (Throwable var9x) {
                              var10x.addSuppressed(var9x);
                           }
                        }

                        throw var10x;
                     }

                     if (var5x != null) {
                        var5x.close();
                     }
                  }

                  return true;
               } catch (IOException | RuntimeException var12) {
                  q8.L("Menu", "Missing/invalid {} artwork; using nebula fallback: {}", var1.G, var12.toString());
                  return false;
               }
            });
            if (!var10) {
               n(var0, tl.t, var2, var3, var4, var5, var6, var8);
            } else {
               try {
                  it var11 = it.cover(var4, var5, var8 ? 1.045 : 1.0, var8 ? D.H() : 0.0, var8 ? D.U() : 0.0);
                  var0.method_25293(
                     class_10799.field_56883, var9, var2, var3, var11.u(), var11.v(), var4, var5, var11.width(), var11.height(), 1672, 941, ei.D(-1, var6)
                  );
               } catch (RuntimeException var14) {
                  d.put(var1, false);
                  q8.u("Menu", "Wallpaper unavailable; using nebula fallback until reload", var14);
                  n(var0, tl.t, var2, var3, var4, var5, var6, var8);
               }
            }
         }
      }
   }

   public static void y() {
      gc.q();
      if (E != null) {
         E.method_1238();
         E = null;
      }

      if (s != null) {
         s.close();
         s = null;
      }

      g = 0L;
   }

   static {
      Random var0 = new Random(10842L);

      for (double[] var4 : M) {
         var4[0] = var0.nextDouble();
         var4[1] = var0.nextDouble();
         var4[2] = 0.3 + var0.nextDouble() * 0.8;
         var4[3] = var0.nextDouble() * Math.PI * 2.0;
      }

      d = new EnumMap<>(tl.class);
      J = -1;
      t = new qb(1.0);
      D = new uh();
   }
}
