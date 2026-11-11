package com.larpclient.render.world;

import com.larpclient.config.HitEffectConfig;
import com.larpclient.render.overlay.ShapeOutline;
import com.larpclient.util.render.ColorUtil;
import net.minecraft.world.phys.AABB;

public final class BoxRenderer {
   public static void renderBox(
      EffectsRenderer var0, boolean var1, ShapeOutline var2, AABB var3, double var4, int var6, double var7, double var9, double var11, double var13
   ) {
      var0.blend(HitEffectConfig.BlendMode.ADDITIVE);
      AABB var15 = var3;

      for (ShapeOutline.Edge var17 : var2.edges()) {
         double var18 = mapCoordinate(var17.x1(), var15.minX, var15.maxX);
         double var20 = mapCoordinate(var17.y1(), var15.minY, var15.maxY);
         double var22 = mapCoordinate(var17.z1(), var15.minZ, var15.maxZ);
         double var24 = mapCoordinate(var17.x2(), var15.minX, var15.maxX);
         double var26 = mapCoordinate(var17.y2(), var15.minY, var15.maxY);
         double var28 = mapCoordinate(var17.z2(), var15.minZ, var15.maxZ);
         double var30 = var4 * var7;
         if (var13 > 0.0) {
            int var32 = ColorUtil.multiplyAlpha(var6, var30 * var13 * (var1 ? 0.25 : 0.09));
            var0.drawRibbon(var18, var20, var22, var24, var26, var28, var9 * 3.0, var9 * 3.0, var32, var32);
            if (!var1) {
               var0.drawRibbon(
                  var18, var20, var22, var24, var26, var28, var9 * 5.0, var9 * 5.0, ColorUtil.multiplyAlpha(var32, 0.35), ColorUtil.multiplyAlpha(var32, 0.35)
               );
            }
         }

         if (!var1) {
            int var35 = ColorUtil.multiplyAlpha(var6, var30);
            var0.drawRibbon(var18, var20, var22, var24, var26, var28, var9, var9, var35, var35);
         }
      }

      var0.blend(HitEffectConfig.BlendMode.NORMAL);
      if (!var1 && var11 > 0.0) {
         for (AABB var34 : var2.boxes()) {
            fillBox(
               var0,
               new AABB(
                  mapCoordinate(var34.minX, var15.minX, var15.maxX),
                  mapCoordinate(var34.minY, var15.minY, var15.maxY),
                  mapCoordinate(var34.minZ, var15.minZ, var15.maxZ),
                  mapCoordinate(var34.maxX, var15.minX, var15.maxX),
                  mapCoordinate(var34.maxY, var15.minY, var15.maxY),
                  mapCoordinate(var34.maxZ, var15.minZ, var15.maxZ)
               ),
               ColorUtil.multiplyAlpha(var6, var4 * var11)
            );
         }
      }
   }

   private static double mapCoordinate(double var0, double var2, double var4) {
      return ShapeOutline.map(var0, var2, var4);
   }

   private static void fillBox(EffectsRenderer var0, AABB var1, int var2) {
      double var3 = var1.minX;
      double var5 = var1.minY;
      double var7 = var1.minZ;
      double var9 = var1.maxX;
      double var11 = var1.maxY;
      double var13 = var1.maxZ;
      addQuad(var0, var2, var3, var5, var7, var9, var5, var7, var9, var11, var7, var3, var11, var7);
      addQuad(var0, var2, var3, var5, var13, var3, var11, var13, var9, var11, var13, var9, var5, var13);
      addQuad(var0, var2, var3, var5, var7, var3, var11, var7, var3, var11, var13, var3, var5, var13);
      addQuad(var0, var2, var9, var5, var7, var9, var5, var13, var9, var11, var13, var9, var11, var7);
      addQuad(var0, var2, var3, var5, var7, var3, var5, var13, var9, var5, var13, var9, var5, var7);
      addQuad(var0, var2, var3, var11, var7, var9, var11, var7, var9, var11, var13, var3, var11, var13);
   }

   private static void addQuad(
      EffectsRenderer var0,
      int var1,
      double var2,
      double var4,
      double var6,
      double var8,
      double var10,
      double var12,
      double var14,
      double var16,
      double var18,
      double var20,
      double var22,
      double var24
   ) {
      var0.vertex(var2, var4, var6, var1);
      var0.vertex(var8, var10, var12, var1);
      var0.vertex(var14, var16, var18, var1);
      var0.vertex(var20, var22, var24, var1);
   }

   private BoxRenderer() {
   }
}
