package com.larpclient;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

public final class df {
   public static final int B = 4096;
   private static final int z = 16384;
   private static final int W = 48;
   private static final int d = 8192;
   private static final double s = 0.1;
   private static final double u = 0.064;
   private static final double L = 0.18;
   private static final double[] k = new double[4096];
   private static final double[] C = new double[4096];
   private static final double[] A = new double[4096];
   private static final double[] m = new double[4096];
   private static final double[] R = new double[4096];
   private static final double[] F = new double[4096];
   private static final double[] I = new double[4096];
   private static final double[] e = new double[4096];
   private static final double[] P = new double[4096];
   private static final double[] H = new double[4096];
   private static final double[] f = new double[4096];
   private static final double[] M = new double[4096];
   private static final double[] x = new double[4096];
   private static final double[] o = new double[4096];
   private static final double[] N = new double[4096];
   private static final int[] p = new int[16384];
   private static final int[] T = new int[4096];
   private static final int[] t = new int[4096];
   private static final int[] g = new int[4096];
   private static final int[] y = new int[4096];
   private static final Set<Long> Y = ConcurrentHashMap.newKeySet();
   private static final Map<Long, List<AABB>> Q = new HashMap<>();
   private static final Map<Long, Long> w = new HashMap<>();
   private static ClientLevel O;
   private static int V;
   private static long U;
   private static boolean Z;

   private static boolean Y() {
      RealisticWaterModule var0 = RealisticWaterModule.L();
      return var0 != null && var0.isRuntimeActive() && var0.B.getValue() && !y3.S();
   }

   public static int u() {
      return V;
   }

   public static boolean A(BlockPos var0) {
      return Y() && Y.contains(var0.asLong());
   }

   public static double V() {
      return 0.064;
   }

   public static double L(int var0, float var1) {
      return m[var0] + (k[var0] - m[var0]) * var1;
   }

   public static double o(int var0, float var1) {
      return R[var0] + (C[var0] - R[var0]) * var1;
   }

   public static double I(int var0, float var1) {
      return F[var0] + (A[var0] - F[var0]) * var1;
   }

   public static void z(BlockPos var0, BlockState var1, BlockState var2) {
      if (Y()) {
         Minecraft var3 = Minecraft.getInstance();
         if (var3 != null
            && var3.level != null
            && var3.player != null
            && !(var3.player.distanceToSqr(var0.getX() + 0.5, var0.getY() + 0.5, var0.getZ() + 0.5) > 49.0)) {
            a(var3.level);
            boolean var4 = var1.getFluidState().is(FluidTags.WATER);
            boolean var5 = var2.getFluidState().is(FluidTags.WATER);
            if (!var1.isAir() && !var4 && var2.isAir()) {
               if (w.size() < 128) {
                  w.put(var0.asLong(), U + 20L);
               }

               for (Direction var9 : Direction.values()) {
                  if (var3.level.getFluidState(var0.relative(var9)).is(FluidTags.WATER)) {
                     o(var0);
                     break;
                  }
               }
            }

            if (var5 && (!var4 || var2.getFluidState().isSource() && !var1.getFluidState().isSource())) {
               byte var11 = 0;

               for (Direction var10 : Direction.values()) {
                  if (Y.contains(var0.relative(var10).asLong())) {
                     var11 = 1;
                     break;
                  }
               }

               int var13 = !var3.player.getMainHandItem().is(Items.WATER_BUCKET)
                     && !var3.player.getOffhandItem().is(Items.WATER_BUCKET)
                     && !var3.player.getMainHandItem().is(Items.BUCKET)
                     && !var3.player.getOffhandItem().is(Items.BUCKET)
                  ? 0
                  : 1;
               int var15 = !(
                     var3.hitResult instanceof BlockHitResult var17
                        && (var0.equals(var17.getBlockPos()) || var0.equals(var17.getBlockPos().relative(var17.getDirection())))
                  )
                  ? 0
                  : 1;
               int var18 = var2.getFluidState().isSource() && var3.options.keyUse.isDown() && var13 != 0 && var15 != 0 ? 1 : 0;
               if (var18 != 0) {
                  k(var0, true);
               } else if (w.containsKey(var0.asLong())) {
                  o(var0);
               } else if (var11 != 0) {
                  D(var0);
               }
            }

            if (!var5) {
               Y.remove(var0.asLong());
            }
         }
      }
   }

   private static void D(BlockPos var0) {
      if (Y.size() < 4096 && Y.add(var0.asLong())) {
         Minecraft var1 = Minecraft.getInstance();
         if (var1 != null && var1.level != null) {
            var1.levelExtractor.setSectionDirtyWithNeighbors(var0.getX() >> 4, var0.getY() >> 4, var0.getZ() >> 4);
         }
      }
   }

   private static void o(BlockPos var0) {
      k(var0, false);
   }

   private static void k(BlockPos var0, boolean var1) {
      if (var1 || !Y.contains(var0.asLong())) {
         if (V + 1000 > 4096) {
            if (!Z) {
               r4.V("TerrainMaterials", "Volumetric water capacity reached ({} droplets); further pours retain native water", 4096);
               Z = (boolean)1;
            }
         } else {
            for (int var2 = 0; var2 < 10; var2++) {
               for (int var3 = 0; var3 < 10; var3++) {
                  for (int var4 = 0; var4 < 10; var4++) {
                     int var5 = V++;
                     double var6 = ((var4 * 7 + var2 * 13 + var3 * 17) % 11 - 5) * 5.0E-4;
                     k[var5] = m[var5] = var0.getX() + (var4 + 0.5) * 0.1 + var6;
                     C[var5] = R[var5] = var0.getY() + (var2 + 0.5) * 0.1 - var6;
                     A[var5] = F[var5] = var0.getZ() + (var3 + 0.5) * 0.1;
                     I[var5] = e[var5] = P[var5] = 0.0;
                  }
               }
            }

            D(var0);
            r4.g("TerrainMaterials", "Volumetric pour accepted at {}; droplets={}", var0, V);
         }
      }
   }

   public static void o(Minecraft var0) {
      if (!Y()) {
         if (V > 0 || !Y.isEmpty()) {
            o();
         }
      } else if (var0.level != null && var0.player != null && !var0.isPaused()) {
         a(var0.level);
         U++;
         w.entrySet().removeIf(var0x -> var0x.getValue() < U);
         if (V != 0) {
            System.arraycopy(k, 0, m, 0, V);
            System.arraycopy(C, 0, R, 0, V);
            System.arraycopy(A, 0, F, 0, V);
            Q.clear();

            for (int var1 = 0; var1 < 2; var1++) {
               G(var0, 0.025);
            }
         }
      }
   }

   private static void a(ClientLevel var0) {
      if (O != var0) {
         o();
         O = var0;
      }
   }

   private static void G(Minecraft var0, double var1) {
      for (int var3 = 0; var3 < V; var3++) {
         H[var3] = k[var3];
         f[var3] = C[var3];
         M[var3] = A[var3];
         if (!O.hasChunkAt(BlockPos.containing(k[var3], C[var3], A[var3]))) {
            I[var3] = e[var3] = P[var3] = 0.0;
         } else {
            e[var3] = Math.max(-6.0, e[var3] - 9.8 * var1);
            double var4 = k[var3];
            double var6 = C[var3];
            double var8 = A[var3];
            k[var3] = k[var3] + I[var3] * var1;
            C[var3] = C[var3] + e[var3] * var1;
            A[var3] = A[var3] + P[var3] * var1;
            w(var3, var4, var6, var8);
         }
      }

      for (int var27 = 0; var27 < 2; var27++) {
         M();
         Arrays.fill(x, 0, V, 0.0);
         Arrays.fill(o, 0, V, 0.0);
         Arrays.fill(N, 0, V, 0.0);

         label108:
         for (int var29 = 0; var29 < V; var29++) {
            int var5 = 0;

            for (int var34 = -1; var34 <= 1; var34++) {
               for (int var7 = -1; var7 <= 1; var7++) {
                  for (int var36 = -1; var36 <= 1; var36++) {
                     int var9 = t[var29] + var36;
                     int var10 = g[var29] + var7;
                     int var11 = y[var29] + var34;

                     for (int var12 = p[H(var9, var10, var11)]; var12 >= 0; var12 = T[var12]) {
                        if (var12 != var29 && t[var12] == var9 && g[var12] == var10 && y[var12] == var11) {
                           double var13 = k[var29] - k[var12];
                           double var15 = C[var29] - C[var12];
                           double var17 = A[var29] - A[var12];
                           double var19 = var13 * var13 + var15 * var15 + var17 * var17;
                           if (!(var19 <= 1.0E-10) && !(var19 > 0.0289)) {
                              if (++var5 > 48) {
                                 continue label108;
                              }

                              double var21 = Math.sqrt(var19);
                              double var23 = var21 < 0.1 ? (0.1 - var21) * 0.22 : -(var21 - 0.1) * 0.014 * (1.0 - var21 / 0.17);
                              double var25 = var23 / var21;
                              x[var29] = x[var29] + var13 * var25;
                              o[var29] = o[var29] + var15 * var25;
                              N[var29] = N[var29] + var17 * var25;
                           }
                        }
                     }
                  }
               }
            }
         }

         for (int var30 = 0; var30 < V; var30++) {
            double var32 = k[var30];
            double var35 = C[var30];
            double var37 = A[var30];
            k[var30] = k[var30] + Math.clamp(x[var30], -0.015, 0.015);
            C[var30] = C[var30] + Math.clamp(o[var30], -0.015, 0.015);
            A[var30] = A[var30] + Math.clamp(N[var30], -0.015, 0.015);
            w(var30, var32, var35, var37);
         }
      }

      AABB var28 = var0.player.getBoundingBox().inflate(0.064);
      Vec3 var31 = var0.player.getDeltaMovement();

      for (int var33 = 0; var33 < V; var33++) {
         I[var33] = (k[var33] - H[var33]) / var1;
         e[var33] = (C[var33] - f[var33]) / var1;
         P[var33] = (A[var33] - M[var33]) / var1;
         if (var28.contains(k[var33], C[var33], A[var33])) {
            I[var33] = I[var33] + var31.x * 3.0;
            P[var33] = P[var33] + var31.z * 3.0;
            e[var33] = e[var33] + Math.max(0.0, var31.y) * 2.0;
         }

         I[var33] = Math.clamp(I[var33] * 0.99, -6.0, 6.0);
         e[var33] = Math.clamp(e[var33] * 0.99, -6.0, 6.0);
         P[var33] = Math.clamp(P[var33] * 0.99, -6.0, 6.0);
         if (!Double.isFinite(k[var33] + C[var33] + A[var33]) || C[var33] < O.getMinY() - 8) {
            s(var33--);
         }
      }
   }

   private static void M() {
      Arrays.fill(p, -1);
      int var0 = 0;

      while (var0 < V) {
         t[var0] = (int)Math.floor(k[var0] / 0.18);
         g[var0] = (int)Math.floor(C[var0] / 0.18);
         y[var0] = (int)Math.floor(A[var0] / 0.18);
         int var1 = H(t[var0], g[var0], y[var0]);
         T[var0] = p[var1];
         p[var1] = var0++;
      }
   }

   private static int H(int var0, int var1, int var2) {
      return (var0 * 73856093 ^ var1 * 19349663 ^ var2 * 83492791) & 16383;
   }

   private static void w(int var0, double var1, double var3, double var5) {
      int var7 = (int)Math.floor(k[var0]);
      int var8 = (int)Math.floor(C[var0]);
      int var9 = (int)Math.floor(A[var0]);

      for (int var10 = -1; var10 <= 1; var10++) {
         for (int var11 = -1; var11 <= 1; var11++) {
            for (int var12 = -1; var12 <= 1; var12++) {
               BlockPos var13 = new BlockPos(var7 + var12, var8 + var11, var9 + var10);
               List var14 = Q.get(var13.asLong());
               if (var14 == null) {
                  if (Q.size() >= 8192 || !O.hasChunkAt(var13)) {
                     k[var0] = var1;
                     C[var0] = var3;
                     A[var0] = var5;
                     I[var0] = e[var0] = P[var0] = 0.0;
                     return;
                  }

                  var14 = O.getBlockState(var13).getCollisionShape(O, var13).toAabbs();
                  Q.put(var13.asLong(), var14);
               }

               for (AABB var16 : var14) {
                  double var17 = 0.0352;
                  double var19 = var16.minX + var13.getX() - var17;
                  double var21 = var16.maxX + var13.getX() + var17;
                  double var23 = var16.minY + var13.getY() - var17;
                  double var25 = var16.maxY + var13.getY() + var17;
                  double var27 = var16.minZ + var13.getZ() - var17;
                  double var29 = var16.maxZ + var13.getZ() + var17;
                  if (!(k[var0] <= var19) && !(k[var0] >= var21) && !(C[var0] <= var23) && !(C[var0] >= var25) && !(A[var0] <= var27) && !(A[var0] >= var29)) {
                     byte var31 = 0;
                     double var32 = k[var0] - var19;
                     double var34 = var21 - k[var0];
                     if (var34 < var32) {
                        var32 = var34;
                        var31 = 1;
                     }

                     var34 = C[var0] - var23;
                     if (var34 < var32) {
                        var32 = var34;
                        var31 = 2;
                     }

                     var34 = var25 - C[var0];
                     if (var34 < var32) {
                        var32 = var34;
                        var31 = 3;
                     }

                     var34 = A[var0] - var27;
                     if (var34 < var32) {
                        var32 = var34;
                        var31 = 4;
                     }

                     var34 = var29 - A[var0];
                     if (var34 < var32) {
                        var31 = 5;
                     }

                     switch (var31) {
                        case 0:
                           k[var0] = var19 - 1.0E-4;
                           I[var0] = Math.min(0.0, I[var0]) * 0.15;
                           break;
                        case 1:
                           k[var0] = var21 + 1.0E-4;
                           I[var0] = Math.max(0.0, I[var0]) * 0.15;
                           break;
                        case 2:
                           C[var0] = var23 - 1.0E-4;
                           e[var0] = Math.min(0.0, e[var0]) * 0.08;
                           break;
                        case 3:
                           C[var0] = var25 + 1.0E-4;
                           e[var0] = Math.max(0.0, e[var0]) * 0.08;
                           I[var0] = I[var0] * 0.97;
                           P[var0] = P[var0] * 0.97;
                           break;
                        case 4:
                           A[var0] = var27 - 1.0E-4;
                           P[var0] = Math.min(0.0, P[var0]) * 0.15;
                           break;
                        case 5:
                           A[var0] = var29 + 1.0E-4;
                           P[var0] = Math.max(0.0, P[var0]) * 0.15;
                     }
                  }
               }
            }
         }
      }
   }

   private static void s(int var0) {
      int var1 = --V;
      k[var0] = k[var1];
      C[var0] = C[var1];
      A[var0] = A[var1];
      m[var0] = m[var1];
      R[var0] = R[var1];
      F[var0] = F[var1];
      I[var0] = I[var1];
      e[var0] = e[var1];
      P[var0] = P[var1];
   }

   public static void o() {
      int var0 = !Y.isEmpty() ? 1 : 0;
      V = 0;
      U = 0L;
      Z = (boolean)0;
      Y.clear();
      Q.clear();
      w.clear();
      O = null;
      if (var0 != 0) {
         y3.z();
      }

      com.larpclient.y.Y();
   }

   private df() {
   }
}
