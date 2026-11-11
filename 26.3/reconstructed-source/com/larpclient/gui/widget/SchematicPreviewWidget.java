package com.larpclient.gui.widget;

import com.larpclient.gui.misc.GuiSounds;
import com.larpclient.module.world.AutoBuilderModule;
import com.larpclient.render.overlay.FontRenderer;
import com.larpclient.util.misc.LarpLog;
import com.larpclient.util.world.BlockPlacementUtil;
import com.larpclient.util.world.Schematic;
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

public final class SchematicPreviewWidget extends Widget {
   static final int mAX_VOXELS = 2000000;
   static final int pREVIEW_HEIGHT = 104;
   static final int pADDING = 6;
   static final int lINE_HEIGHT = 14;
   static final int eMPTY_HEIGHT = 46;
   private static final int mUTED_COLOR = -6643287;
   private static final int tEXT_COLOR = -2630430;
   static final int dEFAULT_BLOCK_COLOR = -4598820;
   private static final Identifier tEXTURE_ID = Identifier.fromNamespaceAndPath("larp-client", "auto_builder/schematic_preview");
   private static int rotation;
   private static Object renderedKey;
   private static Object pendingKey;
   private static SchematicPreviewWidget.PreviewImage preview;
   private static CompletableFuture<SchematicPreviewWidget.PreviewImage> pendingPreview;
   private static boolean textureRegistered;
   private final AutoBuilderModule state;

   public SchematicPreviewWidget(AutoBuilderModule var1) {
      this.state = var1;
      this.height = 46;
   }

   @Override
   public void bounds(int var1, int var2, int var3) {
      super.bounds(var1, var2, var3);
      this.height = this.state.countItem() == null ? 46 : 130;
   }

   @Override
   public void render(GuiGraphicsExtractor var1, int var2, int var3) {
      Schematic var4 = this.state.countItem();
      if (var4 == null) {
         releasePreview();
         int var15 = FontRenderer.TextStyle.BODY.lineHeight();
         FontRenderer.drawText(
            var1,
            FontRenderer.trimToWidth(this.state.getSchematic(), this.width - 12, FontRenderer.TextStyle.BODY),
            this.x + 6 + 2,
            this.y + 6 + 2,
            -2630430,
            FontRenderer.TextStyle.BODY
         );
         FontRenderer.drawText(
            var1,
            FontRenderer.trimToWidth("Choose a file in the list above", this.width - 12, FontRenderer.TextStyle.HELP),
            this.x + 6 + 2,
            this.y + 6 + 6 + var15,
            -6643287,
            FontRenderer.TextStyle.HELP
         );
      } else {
         int var5 = Math.max(1, this.width - 12);
         double var6 = Math.max(1, Minecraft.getInstance().getWindow().getGuiScale());
         SchematicPreviewWidget.PreviewKey var8 = new SchematicPreviewWidget.PreviewKey(
            var4, rotation, (int)Math.round(var5 * var6), (int)Math.round(104.0 * var6)
         );
         updatePreview(var8, var4);
         String var9 = var4.getName() + ", " + var4.getRegions() + " x " + var4.sizeY() + " x " + var4.sizeZ() + ", " + var4.blockCount() + " blocks";
         if (preview != null && renderedKey instanceof SchematicPreviewWidget.PreviewKey var10 && var10.schematic() == var4) {
            int var17 = (int)Math.round(preview.width() / var6);
            int var12 = (int)Math.round(preview.height() / var6);
            int var13 = this.x + 6 + (var5 - var17) / 2;
            int var14 = this.y + 6 + (104 - var12) / 2;
            var1.blit(
               RenderPipelines.GUI_TEXTURED,
               tEXTURE_ID,
               var13,
               var14,
               0.0F,
               0.0F,
               var17,
               var12,
               preview.width(),
               preview.height(),
               preview.width(),
               preview.height()
            );
            if (preview.group() > 1) {
               var9 = var9 + ", 1 cube is " + preview.group() + " blocks";
            }
         } else {
            FontRenderer.drawText(var1, "Drawing the preview", this.x + 6 + 2, this.y + 6 + 52 - 4, -6643287, FontRenderer.TextStyle.HELP);
         }

         int var16 = this.y + 6 + 104 + 3;
         String var18 = "Click to turn";
         int var19 = FontRenderer.measureWidth(var18, FontRenderer.TextStyle.HELP);
         FontRenderer.drawText(
            var1,
            FontRenderer.trimToWidth(var9, Math.max(1, this.width - 18 - var19), FontRenderer.TextStyle.HELP),
            this.x + 6 + 2,
            var16,
            -2630430,
            FontRenderer.TextStyle.HELP
         );
         FontRenderer.drawText(var1, var18, this.x + this.width - 6 - var19, var16, -6643287, FontRenderer.TextStyle.HELP);
      }
   }

   @Override
   public boolean click(double var1, double var3, int var5) {
      if (var5 == 0 && this.state.countItem() != null && this.contains(var1, var3)) {
         rotation = (rotation + 1) % 4;
         GuiSounds.play(GuiSounds.Sound.CLICK);
         return true;
      } else {
         return false;
      }
   }

   private static void updatePreview(SchematicPreviewWidget.PreviewKey var0, Schematic var1) {
      if (pendingPreview != null && pendingPreview.isDone()) {
         SchematicPreviewWidget.PreviewImage var2 = null;

         try {
            var2 = pendingPreview.join();
         } catch (RuntimeException var4) {
            LarpLog.warn("Auto Builder", "Schematic preview failed: {}", var4.toString());
         }

         if (var2 != null && var2.width() > 0 && var2.height() > 0) {
            uploadTexture(var2, pendingKey);
         } else {
            preview = null;
            renderedKey = pendingKey;
         }

         pendingPreview = null;
      }

      if (!var0.equals(renderedKey) && (pendingPreview == null || !var0.equals(pendingKey))) {
         if (pendingPreview == null) {
            int[] var5 = blockColors(var1);
            pendingKey = var0;
            pendingPreview = CompletableFuture.supplyAsync(
               () -> renderIsometric(var1, var5, var0.view(), var0.width(), var0.height()), Util.backgroundExecutor()
            );
         }
      }
   }

   private static void uploadTexture(SchematicPreviewWidget.PreviewImage var0, Object var1) {
      NativeImage var2 = new NativeImage(var0.width(), var0.height(), false);

      for (int var3 = 0; var3 < var0.height(); var3++) {
         for (int var4 = 0; var4 < var0.width(); var4++) {
            var2.setPixel(var4, var3, var0.pixels()[var3 * var0.width() + var4]);
         }
      }

      Minecraft.getInstance().getTextureManager().register(tEXTURE_ID, new DynamicTexture(tEXTURE_ID::toString, var2));
      textureRegistered = (boolean)1;
      preview = var0;
      renderedKey = var1;
   }

   private static void releasePreview() {
      if (pendingPreview != null) {
         pendingPreview.cancel(false);
         pendingPreview = null;
         pendingKey = null;
      }

      if (textureRegistered) {
         Minecraft.getInstance().getTextureManager().release(tEXTURE_ID);
         textureRegistered = (boolean)0;
         preview = null;
         renderedKey = null;
      }
   }

   private static int[] blockColors(Schematic var0) {
      List var1 = var0.getPalette();
      int[] var2 = new int[var1.size()];

      for (int var3 = 0; var3 < var2.length; var3++) {
         try {
            BlockState var4 = BlockPlacementUtil.parseBlockState((String)var1.get(var3));
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

   public static SchematicPreviewWidget.PreviewImage renderIsometric(Schematic var0, int[] var1, int var2, int var3, int var4) {
      if (var0.blockCount() != 0 && var3 >= 4 && var4 >= 4) {
         int var5 = (var2 & 1) == 1 ? 1 : 0;
         int var6 = var0.getRegions();
         int var7 = var0.sizeY();
         int var8 = var0.sizeZ();
         int var9 = var5 != 0 ? var8 : var6;
         int var10 = var5 != 0 ? var6 : var8;
         int var11 = 1;

         while (true) {
            long var12 = ceilDiv(var9, var11);
            long var14 = ceilDiv(var7, var11);
            long var16 = ceilDiv(var10, var11);
            if ((var12 + var16) * 2L <= var3 && var12 + var16 + var14 * 2L <= var4 && var12 * var14 * var16 <= 2000000L) {
               int var37 = ceilDiv(var9, var11);
               int var13 = ceilDiv(var7, var11);
               int var38 = ceilDiv(var10, var11);
               int var15 = 1;

               while (var15 < 48 && (var37 + var38) * (var15 + 1) * 2 <= var3 && (var37 + var38) * (var15 + 1) + var13 * (var15 + 1) * 2 <= var4) {
                  var15++;
               }

               int[] var39 = new int[var37 * var13 * var38];

               for (int var17 = 0; var17 < var0.blockCount(); var17++) {
                  int var18 = var1[var0.describe(var17)];
                  if (var18 != 0) {
                     int var19 = var0.blockX(var17) - var0.minX();
                     int var20 = var0.getFormat(var17) - var0.minY();
                     int var21 = var0.blockZ(var17) - var0.minZ();
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
               byte[] var44 = buildCubeMask(var15);
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
                           int[] var27 = new int[]{
                              var26, shadeColor(var26, 80), shadeColor(var26, 64), shadeColor(var26, 88), shadeColor(var26, 71), shadeColor(var26, 57)
                           };
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

               return new SchematicPreviewWidget.PreviewImage(var40, var41, var42, var11);
            }

            var11++;
         }
      } else {
         return new SchematicPreviewWidget.PreviewImage(0, 0, new int[0], 1);
      }
   }

   static byte[] buildCubeMask(int var0) {
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

   static int shadeColor(int var0, int var1) {
      int var2 = (var0 >> 16 & 0xFF) * var1 / 100;
      int var3 = (var0 >> 8 & 0xFF) * var1 / 100;
      int var4 = (var0 & 0xFF) * var1 / 100;
      return var0 & 0xFF000000 | var2 << 16 | var3 << 8 | var4;
   }

   private static int ceilDiv(int var0, int var1) {
      return (var0 + var1 - 1) / var1;
   }

   public record PreviewImage(int width, int height, int[] pixels, int group) {
   }

   private record PreviewKey(Schematic schematic, int view, int width, int height) {
   }
}
