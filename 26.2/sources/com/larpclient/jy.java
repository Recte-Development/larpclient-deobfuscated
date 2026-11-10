package com.larpclient;

import com.mojang.blaze3d.platform.NativeImage;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Util;
import net.minecraft.world.level.EmptyBlockGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;

public final class jy extends jd {
   static final int b = 2000000;
   static final int U = 104;
   static final int z = 6;
   static final int m = 14;
   static final int E = 46;
   private static final int O = -6643287;
   private static final int w = -2630430;
   static final int V = -4598820;
   private static final Identifier v = Identifier.fromNamespaceAndPath("larp-client", "auto_builder/schematic_preview");
   private static int r;
   private static Object G;
   private static Object d;
   private static jy.f c;
   private static CompletableFuture<jy.f> k;
   private static boolean l;
   private final ak H;

   public jy(ak var1) {
      this.H = var1;
      this.J = 46;
   }

   @Override
   public void bounds(int var1, int var2, int var3) {
      super.bounds(var1, var2, var3);
      this.J = this.H.G() == null ? 46 : 130;
   }

   @Override
   public void render(GuiGraphicsExtractor var1, int var2, int var3) {
      cb var4 = this.H.G();
      if (var4 == null) {
         l();
         int var15 = bj.u.X.n();
         bj.k(var1, bj.i(this.H.l(), this.A - 12, bj.u.X), this.S + 6 + 2, this.u + 6 + 2, -2630430, bj.u.X);
         bj.k(var1, bj.i("Choose a file in the list above", this.A - 12, bj.u.M), this.S + 6 + 2, this.u + 6 + 6 + var15, -6643287, bj.u.M);
      } else {
         int var5 = Math.max(1, this.A - 12);
         double var6 = Math.max(1, Minecraft.getInstance().getWindow().getGuiScale());
         jy.q var8 = new jy.q(var4, r, (int)Math.round(var5 * var6), (int)Math.round(104.0 * var6));
         Q(var8, var4);
         String var9 = var4.o() + ", " + var4.w() + " x " + var4.K() + " x " + var4.N() + ", " + var4.h() + " blocks";
         if (c != null && G instanceof jy.q var10 && var10.schematic() == var4) {
            int var17 = (int)Math.round(c.width() / var6);
            int var12 = (int)Math.round(c.height() / var6);
            int var13 = this.S + 6 + (var5 - var17) / 2;
            int var14 = this.u + 6 + (104 - var12) / 2;
            var1.blit(RenderPipelines.GUI_TEXTURED, v, var13, var14, 0.0F, 0.0F, var17, var12, c.width(), c.height(), c.width(), c.height());
            if (c.group() > 1) {
               var9 = var9 + ", 1 cube is " + c.group() + " blocks";
            }
         } else {
            bj.k(var1, "Drawing the preview", this.S + 6 + 2, this.u + 6 + 52 - 4, -6643287, bj.u.M);
         }

         int var16 = this.u + 6 + 104 + 3;
         String var18 = "Click to turn";
         int var19 = bj.e(var18, bj.u.M);
         bj.k(var1, bj.i(var9, Math.max(1, this.A - 18 - var19), bj.u.M), this.S + 6 + 2, var16, -2630430, bj.u.M);
         bj.k(var1, var18, this.S + this.A - 6 - var19, var16, -6643287, bj.u.M);
      }
   }

   @Override
   public boolean click(double var1, double var3, int var5) {
      if (var5 == 0 && this.H.G() != null && this.contains(var1, var3)) {
         r = (r + 1) % 4;
         sr.v(sr.e.q);
         return true;
      } else {
         return false;
      }
   }

   private static void Q(jy.q var0, cb var1) {
      if (k != null && k.isDone()) {
         jy.f var2 = null;

         try {
            var2 = k.join();
         } catch (RuntimeException var4) {
            h.g("Auto Builder", "Schematic preview failed: {}", var4.toString());
         }

         if (var2 != null && var2.width() > 0 && var2.height() > 0) {
            b(var2, d);
         } else {
            c = null;
            G = d;
         }

         k = null;
      }

      if (!var0.equals(G) && (k == null || !var0.equals(d))) {
         if (k == null) {
            int[] var5 = z(var1);
            d = var0;
            k = CompletableFuture.supplyAsync(() -> Q(var1, var5, var0.view(), var0.width(), var0.height()), Util.backgroundExecutor());
         }
      }
   }

   private static void b(jy.f var0, Object var1) {
      NativeImage var2 = new NativeImage(var0.width(), var0.height(), false);

      for (int var3 = 0; var3 < var0.height(); var3++) {
         for (int var4 = 0; var4 < var0.width(); var4++) {
            var2.setPixel(var4, var3, var0.pixels()[var3 * var0.width() + var4]);
         }
      }

      Minecraft.getInstance().getTextureManager().register(v, new DynamicTexture(v::toString, var2));
      l = (boolean)1;
      c = var0;
      G = var1;
   }

   private static void l() {
      if (k != null) {
         k.cancel(false);
         k = null;
         d = null;
      }

      if (l) {
         Minecraft.getInstance().getTextureManager().release(v);
         l = (boolean)0;
         c = null;
         G = null;
      }
   }

   private static int[] z(cb var0) {
      List var1 = var0.r();
      int[] var2 = new int[var1.size()];

      for (int var3 = 0; var3 < var2.length; var3++) {
         try {
            BlockState var4 = b_.f((String)var1.get(var3));
            if (var4 != null && !var4.isAir()) {
               MapColor var5 = var4.getMapColor(EmptyBlockGetter.INSTANCE, BlockPos.ZERO);
               var2[var3] = var5 == MapColor.NONE ? -4598820 : 0xFF000000 | var5.col;
            }
         } catch (RuntimeException var6) {
            var2[var3] = -8355712;
         }
      }

      return var2;
   }

   public static jy.f Q(cb var0, int[] var1, int var2, int var3, int var4) {
      if (var0.h() != 0 && var3 >= 4 && var4 >= 4) {
         int var5 = (var2 & 1) == 1 ? 1 : 0;
         int var6 = var0.w();
         int var7 = var0.K();
         int var8 = var0.N();
         int var9 = var5 != 0 ? var8 : var6;
         int var10 = var5 != 0 ? var6 : var8;
         int var11 = 1;

         while (true) {
            long var12 = l(var9, var11);
            long var14 = l(var7, var11);
            long var16 = l(var10, var11);
            if ((var12 + var16) * 2L <= var3 && var12 + var16 + var14 * 2L <= var4 && var12 * var14 * var16 <= 2000000L) {
               int var37 = l(var9, var11);
               int var13 = l(var7, var11);
               int var38 = l(var10, var11);
               int var15 = 1;

               while (var15 < 48 && (var37 + var38) * (var15 + 1) * 2 <= var3 && (var37 + var38) * (var15 + 1) + var13 * (var15 + 1) * 2 <= var4) {
                  var15++;
               }

               int[] var39 = new int[var37 * var13 * var38];

               for (int var17 = 0; var17 < var0.h(); var17++) {
                  int var18 = var1[var0.A(var17)];
                  if (var18 != 0) {
                     int var19 = var0.Z(var17) - var0.Y();
                     int var20 = var0.L(var17) - var0.x();
                     int var21 = var0.d(var17) - var0.a();
                     int var22;
                     int var23;
                     switch (var2 & 3) {
                        case 1:
                           var22 = var8 - 1 - var21;
                           var23 = var19;
                           break;
                        case 2:
                           var22 = var6 - 1 - var19;
                           var23 = var8 - 1 - var21;
                           break;
                        case 3:
                           var22 = var21;
                           var23 = var6 - 1 - var19;
                           break;
                        default:
                           var22 = var19;
                           var23 = var21;
                     }

                     var39[(var20 / var11 * var38 + var23 / var11) * var37 + var22 / var11] = var18;
                  }
               }

               int var40 = (var37 + var38) * var15 * 2;
               int var41 = (var37 + var38) * var15 + var13 * var15 * 2;
               int[] var42 = new int[var40 * var41];
               int[] var43 = new int[var42.length];
               Arrays.fill(var43, -1);
               byte[] var44 = J(var15);
               int var45 = var15 * 4;

               for (int var46 = 0; var46 < var13; var46++) {
                  for (int var24 = 0; var24 < var38; var24++) {
                     for (int var25 = 0; var25 < var37; var25++) {
                        int var26 = var39[(var46 * var38 + var24) * var37 + var25];
                        if (var26 != 0
                           && (
                              var46 + 1 >= var13
                                 || var39[((var46 + 1) * var38 + var24) * var37 + var25] == 0
                                 || var25 + 1 >= var37
                                 || var39[(var46 * var38 + var24) * var37 + var25 + 1] == 0
                                 || var24 + 1 >= var38
                                 || var39[(var46 * var38 + var24 + 1) * var37 + var25] == 0
                           )) {
                           int[] var27 = new int[]{var26, O(var26, 80), O(var26, 64), O(var26, 88), O(var26, 71), O(var26, 57)};
                           int var28 = (var25 - var24 + var38 - 1) * 2 * var15;
                           int var29 = (var25 + var24) * var15 + (var13 - 1 - var46) * 2 * var15;
                           int var30 = var25 + var46 + var24;

                           for (int var31 = 0; var31 < var45; var31++) {
                              for (int var32 = 0; var32 < var45; var32++) {
                                 byte var33 = var44[var31 * var45 + var32];
                                 if (var33 != 0) {
                                    int var34 = var28 + var32;
                                    int var35 = var29 + var31;
                                    if (var34 >= 0 && var35 >= 0 && var34 < var40 && var35 < var41) {
                                       int var36 = var35 * var40 + var34;
                                       if (var30 >= var43[var36]) {
                                          var43[var36] = var30;
                                          var42[var36] = var27[var33 - 1];
                                       }
                                    }
                                 }
                              }
                           }
                        }
                     }
                  }
               }

               return new jy.f(var40, var41, var42, var11);
            }

            var11++;
         }
      } else {
         return new jy.f(0, 0, new int[0], 1);
      }
   }

   static byte[] J(int var0) {
      int var1 = var0 * 4;
      byte[] var2 = new byte[var1 * var1];

      for (int var3 = 0; var3 < var1; var3++) {
         for (int var4 = 0; var4 < var1; var4++) {
            double var5 = var4 + 0.5;
            double var7 = var3 + 0.5;
            byte var9 = 0;
            double var11 = Math.abs(var5 - 2 * var0) / (2 * var0) + Math.abs(var7 - var0) / var0;
            int var10;
            if (var11 <= 1.0) {
               var9 = 1;
               var10 = (1.0 - var11) * var0 < 0.9 ? 1 : 0;
            } else if (var5 < 2 * var0 && var7 >= var0 + var5 / 2.0 && var7 <= 3 * var0 + var5 / 2.0) {
               var9 = 2;
               var10 = !(var5 < 1.0) && !(var7 - (var0 + var5 / 2.0) < 1.0) && !(3 * var0 + var5 / 2.0 - var7 < 1.0) ? 0 : 1;
            } else if (var5 >= 2 * var0 && var7 >= 2 * var0 - (var5 - 2 * var0) / 2.0 && var7 <= 4 * var0 - (var5 - 2 * var0) / 2.0) {
               var9 = 3;
               var10 = !(var5 > var1 - 1) && !(var7 - (2 * var0 - (var5 - 2 * var0) / 2.0) < 1.0) && !(4 * var0 - (var5 - 2 * var0) / 2.0 - var7 < 1.0) ? 0 : 1;
            } else {
               var10 = 0;
            }

            if (var9 != 0 && var10 != 0 && var0 >= 2) {
               var9 = (byte)(var9 + 3);
            }

            var2[var3 * var1 + var4] = var9;
         }
      }

      return var2;
   }

   static int O(int var0, int var1) {
      int var2 = (var0 >> 16 & 0xFF) * var1 / 100;
      int var3 = (var0 >> 8 & 0xFF) * var1 / 100;
      int var4 = (var0 & 0xFF) * var1 / 100;
      return var0 & 0xFF000000 | var2 << 16 | var3 << 8 | var4;
   }

   private static int l(int var0, int var1) {
      return (var0 + var1 - 1) / var1;
   }

   public record f(int width, int height, int[] pixels, int group) {
   }

   private record q(cb schematic, int view, int width, int height) {
   }
}
