package com.larpclient.render.model;

public final class FingerMeshBuilder {
   public static final int sEGMENT_COUNT = 7;
   public static final int fLOATS_PER_VERTEX = 9;
   public static final int vERTEX_COUNT = 504;
   public static final double bASE_RADIUS = 0.20625;
   public static final double oRIGIN_X = -1.0;
   public static final double oRIGIN_Y = 8.5;
   public static final double oRIGIN_Z = 0.0;
   static final double iNSET = 0.0125;
   public static final double rOOT_RADIUS = 0.2375;
   public static final double bASE_CURL = -0.38;
   static final double tAPER_PER_SEGMENT = 0.006;
   static final double wAVE_AMPLITUDE = 0.0375;
   private static final double wAVE_BEND = 0.035;
   private static final double wAVE_PHASE_STEP = 0.9;
   private static final double tIP_FALLOFF = 2.0;

   private FingerMeshBuilder() {
   }

   public static float[] buildMesh(boolean var0, double var1, double var3, double var5, double var7, double var9, double var11) {
      double var13 = var0 ? 1.0 : -1.0;
      double[] var15 = rotationMatrix(var1, var13 * var3, var13 * var5);
      double var16 = -1.0 * var13 / 16.0;
      double var18 = 0.53125;
      double var20 = 0.0;
      double var22 = (Math.PI * 4) * var11;
      double var24 = Math.clamp(var7, 0.0, 1.0);
      double var26 = 0.39999999999999997;
      double var28 = 0.0375 * (1.0 + 0.15 * Math.sin(var22 + 1.3));
      float[] var30 = new float[4536];
      double[] var31 = new double[60];
      double[] var32 = new double[5];
      double[] var33 = new double[5];
      byte var34 = 0;

      for (int var35 = 0; var35 < 7; var35++) {
         double var36 = -0.38 + var7 * var35 / 6.0 + 0.035 * var24 * Math.sin(var22 - var35 * 0.9);
         double var38 = Math.cos(var36);
         double var40 = Math.sin(var36);
         double var42 = -var13 * var40;
         double var44 = 0.0;
         double var46 = -var38;
         double var48 = var46;
         double var50 = 0.0;
         double var52 = -var42;

         for (int var54 = 0; var54 < 5; var54++) {
            double var55 = Math.max(0.0, profileOffset(var54) / var26);
            var32[var54] = 0.2375 - var35 * 0.006 - var28 * var55 * var55;
            var33[var54] = profileOffset(var54) > 0.0 ? -2.0 * var28 * var55 / var26 : 0.0;
         }

         RibbonMesh.fillFlatVertices(var31, 0.20625, var32);

         for (int var93 = 0; var93 < 18; var93++) {
            boolean var94 = RibbonMesh.isEdgeFace(var93);
            double var56 = 0.0;
            double var58 = 0.0;
            double var60 = 0.0;
            if (var94) {
               int var62 = RibbonMesh.vertexIndex(var93, 0) * 3;
               int var63 = RibbonMesh.vertexIndex(var93, 1) * 3;
               int var64 = RibbonMesh.vertexIndex(var93, 2) * 3;
               double var65 = var31[var63] - var31[var62];
               double var67 = var31[var63 + 1] - var31[var62 + 1];
               double var69 = var31[var63 + 2] - var31[var62 + 2];
               double var71 = var31[var64] - var31[var62];
               double var73 = var31[var64 + 1] - var31[var62 + 1];
               double var75 = var31[var64 + 2] - var31[var62 + 2];
               double var77 = var67 * var75 - var69 * var73;
               double var79 = var69 * var71 - var65 * var75;
               double var81 = var65 * var73 - var67 * var71;
               var56 = var42 * var77 + var48 * var79;
               var58 = var44 * var77 + var50 * var79 + var81;
               var60 = var46 * var77 + var52 * var79;
            }

            for (int var95 = 0; var95 < 4; var95++) {
               int var96 = RibbonMesh.vertexIndex(var93, var95);
               int var97 = var96 / 4;
               double var98 = var31[var96 * 3] + 0.20625 - 0.0125;
               double var99 = var31[var96 * 3 + 1];
               double var100 = var31[var96 * 3 + 2];
               double var101 = var42 * var98 + var48 * var99;
               double var102 = var44 * var98 + var50 * var99 + var100;
               double var103 = var46 * var98 + var52 * var99;
               var30[var34] = (float)(var16 + (var15[0] * var101 + var15[1] * var102 + var15[2] * var103) * var9);
               var30[var34 + 1] = (float)(var18 + (var15[3] * var101 + var15[4] * var102 + var15[5] * var103) * var9);
               var30[var34 + 2] = (float)(var20 + (var15[6] * var101 + var15[7] * var102 + var15[8] * var103) * var9);
               var30[var34 + 3] = (float)RibbonMesh.textureU(var93, var95);
               var30[var34 + 4] = (float)RibbonMesh.textureV(var93, var95);
               double var104 = var56;
               double var105 = var58;
               double var106 = var60;
               if (!var94) {
                  double var83 = var93 % 4 == 0 ? 1.0 : -1.0;
                  var104 = -var83 * var33[var97] * var42;
                  var105 = var83 * (1.0 - var33[var97] * var44);
                  var106 = -var83 * var33[var97] * var46;
               }

               double var107 = var15[0] * var104 + var15[1] * var105 + var15[2] * var106;
               double var85 = var15[3] * var104 + var15[4] * var105 + var15[5] * var106;
               double var87 = var15[6] * var104 + var15[7] * var105 + var15[8] * var106;
               double var89 = Math.sqrt(var107 * var107 + var85 * var85 + var87 * var87);
               if (var89 < 1.0E-12) {
                  var107 = 0.0;
                  var85 = 1.0;
                  var87 = 0.0;
                  var89 = 1.0;
               }

               var30[var34 + 5] = (float)(var107 / var89);
               var30[var34 + 6] = (float)(var85 / var89);
               var30[var34 + 7] = (float)(var87 / var89);
               double var91 = Math.clamp(var98 / var26, 0.0, 1.0);
               var30[var34 + 8] = (float)((var94 ? 0.9 : 0.8 + 0.2 * var91) * (1.0 - 0.03 * var35));
               var34 += 9;
            }
         }
      }

      return var30;
   }

   private static double profileOffset(int var0) {
      return (var0 * 0.5 - 1.0) * 0.20625 + 0.20625 - 0.0125;
   }

   static double[] rotationMatrix(double var0, double var2, double var4) {
      double var6 = Math.cos(var0);
      double var8 = Math.sin(var0);
      double var10 = Math.cos(var2);
      double var12 = Math.sin(var2);
      double var14 = Math.cos(var4);
      double var16 = Math.sin(var4);
      return new double[]{
         var14 * var10,
         var14 * var12 * var8 - var16 * var6,
         var14 * var12 * var6 + var16 * var8,
         var16 * var10,
         var16 * var12 * var8 + var14 * var6,
         var16 * var12 * var6 - var14 * var8,
         -var12,
         var10 * var8,
         var10 * var6
      };
   }
}
