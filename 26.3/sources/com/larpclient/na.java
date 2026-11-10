package com.larpclient;

import com.mojang.blaze3d.buffers.Std140Builder;
import com.mojang.blaze3d.pipeline.TextureTarget;
import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.renderpearl.api.GpuFormat;
import com.mojang.renderpearl.api.buffers.GpuBuffer;
import com.mojang.renderpearl.api.commands.CommandEncoder;
import com.mojang.renderpearl.api.commands.RenderPass;
import com.mojang.renderpearl.api.pipeline.BindGroupLayout;
import com.mojang.renderpearl.api.pipeline.PrimitiveTopology;
import com.mojang.renderpearl.api.pipeline.RenderPipeline;
import com.mojang.renderpearl.api.pipeline.UniformType;
import com.mojang.renderpearl.api.pipeline.RenderPipeline.Snippet;
import com.mojang.renderpearl.api.textures.FilterMode;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.EnumMap;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.render.TextureSetup;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.state.gui.BlitRenderState;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.Resource;
import org.joml.Matrix3x2f;
import org.joml.Vector4f;
import org.lwjgl.system.MemoryStack;

public final class na {
   private static final RenderPipeline A = RenderPipeline.builder(new Snippet[0])
      .withLocation(Identifier.fromNamespaceAndPath("larp-client", "menu_nebula"))
      .withVertexShader(Identifier.fromNamespaceAndPath("larp-client", "post/fullscreen"))
      .withFragmentShader(Identifier.fromNamespaceAndPath("larp-client", "menu/nebula"))
      .withBindGroupLayout(BindGroupLayout.builder().withUniform("MenuParameters", UniformType.UNIFORM_BUFFER).build())
      .withPrimitiveTopology(PrimitiveTopology.TRIANGLES)
      .withCull(false)
      .build();
   private static final long n = System.nanoTime();
   private static final double[][] i = new double[140][4];
   private static TextureTarget D;
   private static GpuBuffer L;
   private static int Z = -1;
   private static boolean e;
   private static long N;
   private static float X;
   private static float Q;
   private static final Map<pz, Boolean> c;
   private static int W;
   private static pz a;
   private static pz S;
   private static final fq I;
   private static final dv k;

   private na() {
   }

   public static void l(GuiGraphicsExtractor var0, int var1, int var2, boolean var3) {
      C(var0, var1, var2, var3 ? 0.8 : 1.0, true);
   }

   private static void C(GuiGraphicsExtractor var0, int var1, int var2, double var3, boolean var5) {
      if (var1 > 0 && var2 > 0) {
         Minecraft var6 = Minecraft.getInstance();
         double var7 = var5 ? (System.nanoTime() - n) / 1.0E9 : 0.0;
         if (Z != nl.q) {
            H();
            Z = nl.q;
            e = (boolean)0;
         }

         byte var9 = 0;
         if (!e) {
            try {
               Window var10 = var6.getWindow();
               int var11 = var10.getWidth();
               int var12 = var10.getHeight();
               double var13 = Math.min(1.0, 900.0 / Math.max(1, var11));
               int var15 = Math.max(1, (int)Math.round(var11 * var13));
               int var16 = Math.max(1, (int)Math.round(var12 * var13));
               if (D == null || D.width != var15 || D.height != var16) {
                  H();
                  se.j(A);
                  D = p7.m("Larp website nebula", var15, var16, false, GpuFormat.RGBA8_UNORM);
                  L = RenderSystem.getDevice().createBuffer(() -> "Larp menu parameters", 136, 32L);
               }

               long var17 = System.nanoTime();
               if (N == 0L || var17 - N >= 33000000L) {
                  float var19 = var5 ? (float)(var6.mouseHandler.xpos() / Math.max(1, var10.getScreenWidth()) - 0.5) : 0.0F;
                  float var20 = var5 ? (float)(0.5 - var6.mouseHandler.ypos() / Math.max(1, var10.getScreenHeight())) : 0.0F;
                  if (var5) {
                     X = X + (var19 - X) * 0.025F;
                     Q = Q + (var20 - Q) * 0.025F;
                  } else {
                     Q = 0.0F;
                     X = 0.0F;
                  }

                  CommandEncoder var21 = RenderSystem.getDevice().createCommandEncoder();
                  MemoryStack var22 = MemoryStack.stackPush();

                  try {
                     ByteBuffer var23 = Std140Builder.onStack(var22, 32)
                        .putVec4(new Vector4f(var15, var16, (float)var7, 0.0F))
                        .putVec4(new Vector4f(X, Q, 0.0F, 0.0F))
                        .get();
                     var21.writeToBuffer(L.slice(), var23);
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

                  RenderPass var35 = var21.createRenderPass(() -> "Larp menu nebula", D.getColorTextureView(), Optional.empty());

                  try {
                     p7.K(var35, A);
                     var35.setUniform("MenuParameters", L.slice());
                     var35.draw(3, 1, 0, 0);
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

                  N = var17;
               }

               var0.guiRenderState
                  .addGuiElement(
                     new BlitRenderState(
                        RenderPipelines.GUI_TEXTURED,
                        TextureSetup.singleTexture(D.getColorTextureView(), RenderSystem.getSamplerCache().getClampToEdge(FilterMode.LINEAR)),
                        new Matrix3x2f(var0.pose()),
                        0,
                        0,
                        var1,
                        var2,
                        0.0F,
                        1.0F,
                        1.0F,
                        0.0F,
                        vv.N(-1, var3),
                        null
                     )
                  );
               var9 = 1;
            } catch (RuntimeException var29) {
               e = (boolean)1;
               H();
               _q.o("Menu", "Nebula unavailable; using dark blue fallback until resource reload", var29);
            }
         }

         if (var9 == 0) {
            h5.f(var0, 0, 0, var1, var2, vv.N(-16116958, var3), vv.N(-16118252, var3));
         }

         double var30 = Math.max(1, var6.getWindow().getGuiScale());
         int var31 = Math.min(140, Math.max(1, (int)Math.round(var1 * var2 * var30 * var30 / 10000.0)));

         for (int var32 = 0; var32 < var31; var32++) {
            double[] var14 = i[var32];
            double var33 = (0.2 + (1.0 + Math.sin(var7 * 0.6 + var14[3])) * 0.18) * 0.26;
            int var34 = Math.max(1, (int)Math.round(var14[2] * 2.0 / var30));
            h5.N(var0, (int)(var14[0] * var1), (int)(var14[1] * var2), var34, var34, vv.N(-4201985, var33 * var3));
         }
      }
   }

   public static double Q() {
      return k.N();
   }

   public static double m() {
      return k.e();
   }

   public static void Q(GuiGraphicsExtractor var0, int var1, int var2, pz var3, boolean var4, boolean var5) {
      if (!(Minecraft.getInstance().gui.screen() instanceof fx)) {
         h.z(var0, var1, var2);
         if (var4) {
            h5.N(var0, 0, 0, var1, var2, -2012607472);
         }
      } else {
         Minecraft var6 = Minecraft.getInstance();
         Window var7 = var6.getWindow();
         k.h(
            var6.mouseHandler.xpos() / Math.max(1, var7.getScreenWidth()) * 2.0 - 1.0,
            var6.mouseHandler.ypos() / Math.max(1, var7.getScreenHeight()) * 2.0 - 1.0,
            System.nanoTime(),
            var5
         );
         if (a != var3) {
            S = a;
            a = var3;
            I.p(S != null && var5 ? 0.0 : 1.0);
         }

         double var8 = var5 ? I.n(1.0, 260) : 1.0;
         if (var8 < 0.999 && S != null) {
            h(var0, S, 0, 0, var1, var2, 1.0, var5);
         }

         h(var0, var3, 0, 0, var1, var2, var8, var5);
         if (var8 >= 0.999) {
            S = null;
         }

         if (var4) {
            h5.N(var0, 0, 0, var1, var2, -2012607472);
         }
      }
   }

   public static void h(GuiGraphicsExtractor var0, pz var1, int var2, int var3, int var4, int var5, double var6, boolean var8) {
      if (var4 > 0 && var5 > 0 && !(var6 <= 0.0)) {
         if (var1 == pz.Z) {
            var0.pose().pushMatrix();

            try {
               var0.pose().translate(var2, var3);
               if (var8) {
                  var0.pose().translate((float)(var4 * 0.0225 * (k.N() - 1.0)), (float)(var5 * 0.0225 * (k.e() - 1.0)));
                  var0.pose().scale(1.045F, 1.045F);
               }

               C(var0, var4, var5, var6, LarpClient.N().h().k().w);
            } finally {
               var0.pose().popMatrix();
            }
         } else {
            if (W != nl.q) {
               W = nl.q;
               c.clear();
            }

            Identifier var9 = Identifier.fromNamespaceAndPath("larp-client", "textures/menu/" + var1.D + ".png");
            boolean var10 = c.computeIfAbsent(var1, var2x -> {
               try {
                  Resource var3x = (Resource)Minecraft.getInstance().getResourceManager().getResource(var9).orElseThrow();

                  try (InputStream var4x = var3x.open()) {
                     NativeImage var5x = NativeImage.read(var4x);

                     try {
                        if (var5x.getWidth() != 1672 || var5x.getHeight() != 941) {
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
                  _q.R("Menu", "Missing/invalid {} artwork; using nebula fallback: {}", var1.J, var12.toString());
                  return false;
               }
            });
            if (!var10) {
               h(var0, pz.Z, var2, var3, var4, var5, var6, var8);
            } else {
               try {
                  s1 var11 = s1.cover(var4, var5, var8 ? 1.045 : 1.0, var8 ? k.N() : 0.0, var8 ? k.e() : 0.0);
                  var0.blit(
                     RenderPipelines.GUI_TEXTURED, var9, var2, var3, var11.u(), var11.v(), var4, var5, var11.width(), var11.height(), 1672, 941, vv.N(-1, var6)
                  );
               } catch (RuntimeException var14) {
                  c.put(var1, false);
                  _q.o("Menu", "Wallpaper unavailable; using nebula fallback until reload", var14);
                  h(var0, pz.Z, var2, var3, var4, var5, var6, var8);
               }
            }
         }
      }
   }

   public static void H() {
      h.z();
      if (D != null) {
         D.destroyBuffers();
         D = null;
      }

      if (L != null) {
         L.close();
         L = null;
      }

      N = 0L;
   }

   static {
      Random var0 = new Random(10842L);

      for (double[] var4 : i) {
         var4[0] = var0.nextDouble();
         var4[1] = var0.nextDouble();
         var4[2] = 0.3 + var0.nextDouble() * 0.8;
         var4[3] = var0.nextDouble() * Math.PI * 2.0;
      }

      c = new EnumMap<>(pz.class);
      W = -1;
      I = new fq(1.0);
      k = new dv();
   }
}
