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

public final class v0 {
   public static final int q = 4096;
   private static final int D = 16384;
   private static final int J = 48;
   private static final int F = 8192;
   private static final double n = 0.1;
   private static final double p = 0.064;
   private static final double L = 0.18;
   private static final double[] O = new double[4096];
   private static final double[] b = new double[4096];
   private static final double[] S = new double[4096];
   private static final double[] E = new double[4096];
   private static final double[] w = new double[4096];
   private static final double[] G = new double[4096];
   private static final double[] k = new double[4096];
   private static final double[] z = new double[4096];
   private static final double[] N = new double[4096];
   private static final double[] t = new double[4096];
   private static final double[] h = new double[4096];
   private static final double[] K = new double[4096];
   private static final double[] v = new double[4096];
   private static final double[] W = new double[4096];
   private static final double[] x = new double[4096];
   private static final int[] j = new int[16384];
   private static final int[] H = new int[4096];
   private static final int[] y = new int[4096];
   private static final int[] u = new int[4096];
   private static final int[] M = new int[4096];
   private static final Set<Long> Y = ConcurrentHashMap.newKeySet();
   private static final Map<Long, List<AABB>> X = new HashMap<>();
   private static final Map<Long, Long> e = new HashMap<>();
   private static ClientLevel d;
   private static int f;
   private static long V;
   private static boolean I;

   private static boolean q() {
      RealisticWaterModule var0 = RealisticWaterModule.g();
      return var0 != null && var0.isRuntimeActive() && var0.A.getValue() && !at.o();
   }

   public static int a() {
      return f;
   }

   public static boolean U(BlockPos var0) {
      return q() && Y.contains(var0.asLong());
   }

   public static double e() {
      return 0.064;
   }

   public static double K(int var0, float var1) {
      return E[var0] + (O[var0] - E[var0]) * var1;
   }

   public static double H(int var0, float var1) {
      return w[var0] + (b[var0] - w[var0]) * var1;
   }

   public static double c(int var0, float var1) {
      return G[var0] + (S[var0] - G[var0]) * var1;
   }

   public static void u(BlockPos var0, BlockState var1, BlockState var2) {
      if (q()) {
         Minecraft var3 = Minecraft.getInstance();
         if (var3 != null
            && var3.level != null
            && var3.player != null
            && !(var3.player.distanceToSqr(var0.getX() + 0.5, var0.getY() + 0.5, var0.getZ() + 0.5) > 49.0)) {
            N(var3.level);
            boolean var4 = var1.getFluidState().is(FluidTags.WATER);
            boolean var5 = var2.getFluidState().is(FluidTags.WATER);
            if (!var1.isAir() && !var4 && var2.isAir()) {
               if (e.size() < 128) {
                  e.put(var0.asLong(), V + 20L);
               }

               for (Direction var9 : Direction.values()) {
                  if (var3.level.getFluidState(var0.relative(var9)).is(FluidTags.WATER)) {
                     f(var0);
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
                  X(var0, true);
               } else if (e.containsKey(var0.asLong())) {
                  f(var0);
               } else if (var11 != 0) {
                  k(var0);
               }
            }

            if (!var5) {
               Y.remove(var0.asLong());
            }
         }
      }
   }

   private static void k(BlockPos var0) {
      if (Y.size() < 4096 && Y.add(var0.asLong())) {
         Minecraft var1 = Minecraft.getInstance();
         if (var1 != null && var1.level != null) {
            var1.levelExtractor.setSectionDirtyWithNeighbors(var0.getX() >> 4, var0.getY() >> 4, var0.getZ() >> 4);
         }
      }
   }

   private static void f(BlockPos var0) {
      X(var0, false);
   }

   private static void X(BlockPos var0, boolean var1) {
      if (var1 || !Y.contains(var0.asLong())) {
         if (f + 1000 > 4096) {
            if (!I) {
               _q.z("TerrainMaterials", "Volumetric water capacity reached ({} droplets); further pours retain native water", 4096);
               I = (boolean)1;
            }
         } else {
            for (int var2 = 0; var2 < 10; var2++) {
               for (int var3 = 0; var3 < 10; var3++) {
                  for (int var4 = 0; var4 < 10; var4++) {
                     int var5 = f++;
                     double var6 = ((var4 * 7 + var2 * 13 + var3 * 17) % 11 - 5) * 5.0E-4;
                     O[var5] = E[var5] = var0.getX() + (var4 + 0.5) * 0.1 + var6;
                     b[var5] = w[var5] = var0.getY() + (var2 + 0.5) * 0.1 - var6;
                     S[var5] = G[var5] = var0.getZ() + (var3 + 0.5) * 0.1;
                     k[var5] = z[var5] = N[var5] = 0.0;
                  }
               }
            }

            k(var0);
            _q.c("TerrainMaterials", "Volumetric pour accepted at {}; droplets={}", var0, f);
         }
      }
   }

   public static void M(Minecraft var0) {
      if (!q()) {
         if (f > 0 || !Y.isEmpty()) {
            Q();
         }
      } else if (var0.level != null && var0.player != null && !var0.isPaused()) {
         N(var0.level);
         V++;
         e.entrySet().removeIf(var0x -> var0x.getValue() < V);
         if (f != 0) {
            System.arraycopy(O, 0, E, 0, f);
            System.arraycopy(b, 0, w, 0, f);
            System.arraycopy(S, 0, G, 0, f);
            X.clear();

            for (int var1 = 0; var1 < 2; var1++) {
               L(var0, 0.025);
            }
         }
      }
   }

   private static void N(ClientLevel var0) {
      if (d != var0) {
         Q();
         d = var0;
      }
   }

   private static void L(Minecraft var0, double var1) {
      for (int var3 = 0; var3 < f; var3++) {
         t[var3] = O[var3];
         h[var3] = b[var3];
         K[var3] = S[var3];
         if (!d.hasChunkAt(BlockPos.containing(O[var3], b[var3], S[var3]))) {
            k[var3] = z[var3] = N[var3] = 0.0;
         } else {
            z[var3] = Math.max(-6.0, z[var3] - 9.8 * var1);
            double var4 = O[var3];
            double var6 = b[var3];
            double var8 = S[var3];
            O[var3] = O[var3] + k[var3] * var1;
            b[var3] = b[var3] + z[var3] * var1;
            S[var3] = S[var3] + N[var3] * var1;
            O(var3, var4, var6, var8);
         }
      }

      for (int var27 = 0; var27 < 2; var27++) {
         o();
         Arrays.fill(v, 0, f, 0.0);
         Arrays.fill(W, 0, f, 0.0);
         Arrays.fill(x, 0, f, 0.0);

         label108:
         for (int var29 = 0; var29 < f; var29++) {
            int var5 = 0;

            for (int var34 = -1; var34 <= 1; var34++) {
               for (int var7 = -1; var7 <= 1; var7++) {
                  for (int var36 = -1; var36 <= 1; var36++) {
                     int var9 = y[var29] + var36;
                     int var10 = u[var29] + var7;
                     int var11 = M[var29] + var34;

                     for (int var12 = j[X(var9, var10, var11)]; var12 >= 0; var12 = H[var12]) {
                        if (var12 != var29 && y[var12] == var9 && u[var12] == var10 && M[var12] == var11) {
                           double var13 = O[var29] - O[var12];
                           double var15 = b[var29] - b[var12];
                           double var17 = S[var29] - S[var12];
                           double var19 = var13 * var13 + var15 * var15 + var17 * var17;
                           if (!(var19 <= 1.0E-10) && !(var19 > 0.0289)) {
                              if (++var5 > 48) {
                                 continue label108;
                              }

                              double var21 = Math.sqrt(var19);
                              double var23 = var21 < 0.1 ? (0.1 - var21) * 0.22 : -(var21 - 0.1) * 0.014 * (1.0 - var21 / 0.17);
                              double var25 = var23 / var21;
                              v[var29] = v[var29] + var13 * var25;
                              W[var29] = W[var29] + var15 * var25;
                              x[var29] = x[var29] + var17 * var25;
                           }
                        }
                     }
                  }
               }
            }
         }

         for (int var30 = 0; var30 < f; var30++) {
            double var32 = O[var30];
            double var35 = b[var30];
            double var37 = S[var30];
            O[var30] = O[var30] + Math.clamp(v[var30], -0.015, 0.015);
            b[var30] = b[var30] + Math.clamp(W[var30], -0.015, 0.015);
            S[var30] = S[var30] + Math.clamp(x[var30], -0.015, 0.015);
            O(var30, var32, var35, var37);
         }
      }

      AABB var28 = var0.player.getBoundingBox().inflate(0.064);
      Vec3 var31 = var0.player.getDeltaMovement();

      for (int var33 = 0; var33 < f; var33++) {
         k[var33] = (O[var33] - t[var33]) / var1;
         z[var33] = (b[var33] - h[var33]) / var1;
         N[var33] = (S[var33] - K[var33]) / var1;
         if (var28.contains(O[var33], b[var33], S[var33])) {
            k[var33] = k[var33] + var31.x * 3.0;
            N[var33] = N[var33] + var31.z * 3.0;
            z[var33] = z[var33] + Math.max(0.0, var31.y) * 2.0;
         }

         k[var33] = Math.clamp(k[var33] * 0.99, -6.0, 6.0);
         z[var33] = Math.clamp(z[var33] * 0.99, -6.0, 6.0);
         N[var33] = Math.clamp(N[var33] * 0.99, -6.0, 6.0);
         if (!Double.isFinite(O[var33] + b[var33] + S[var33]) || b[var33] < d.getMinY() - 8) {
            y(var33--);
         }
      }
   }

   private static void o() {
      Arrays.fill(j, -1);
      int var0 = 0;

      while (var0 < f) {
         y[var0] = (int)Math.floor(O[var0] / 0.18);
         u[var0] = (int)Math.floor(b[var0] / 0.18);
         M[var0] = (int)Math.floor(S[var0] / 0.18);
         int var1 = X(y[var0], u[var0], M[var0]);
         H[var0] = j[var1];
         j[var1] = var0++;
      }
   }

   private static int X(int var0, int var1, int var2) {
      return (var0 * 73856093 ^ var1 * 19349663 ^ var2 * 83492791) & 16383;
   }

   private static void O(int var0, double var1, double var3, double var5) {
      int var7 = (int)Math.floor(O[var0]);
      int var8 = (int)Math.floor(b[var0]);
      int var9 = (int)Math.floor(S[var0]);

      for (int var10 = -1; var10 <= 1; var10++) {
         for (int var11 = -1; var11 <= 1; var11++) {
            for (int var12 = -1; var12 <= 1; var12++) {
               BlockPos var13 = new BlockPos(var7 + var12, var8 + var11, var9 + var10);
               List var14 = X.get(var13.asLong());
               if (var14 == null) {
                  if (X.size() >= 8192 || !d.hasChunkAt(var13)) {
                     O[var0] = var1;
                     b[var0] = var3;
                     S[var0] = var5;
                     k[var0] = z[var0] = N[var0] = 0.0;
                     return;
                  }

                  var14 = d.getBlockState(var13).getCollisionShape(d, var13).toAabbs();
                  X.put(var13.asLong(), var14);
               }

               for (AABB var16 : var14) {
                  double var17 = 0.0352;
                  double var19 = var16.minX + var13.getX() - var17;
                  double var21 = var16.maxX + var13.getX() + var17;
                  double var23 = var16.minY + var13.getY() - var17;
                  double var25 = var16.maxY + var13.getY() + var17;
                  double var27 = var16.minZ + var13.getZ() - var17;
                  double var29 = var16.maxZ + var13.getZ() + var17;
                  if (!(O[var0] <= var19) && !(O[var0] >= var21) && !(b[var0] <= var23) && !(b[var0] >= var25) && !(S[var0] <= var27) && !(S[var0] >= var29)) {
                     byte var31 = 0;
                     double var32 = O[var0] - var19;
                     double var34 = var21 - O[var0];
                     if (var34 < var32) {
                        var32 = var34;
                        var31 = 1;
                     }

                     var34 = b[var0] - var23;
                     if (var34 < var32) {
                        var32 = var34;
                        var31 = 2;
                     }

                     var34 = var25 - b[var0];
                     if (var34 < var32) {
                        var32 = var34;
                        var31 = 3;
                     }

                     var34 = S[var0] - var27;
                     if (var34 < var32) {
                        var32 = var34;
                        var31 = 4;
                     }

                     var34 = var29 - S[var0];
                     if (var34 < var32) {
                        var31 = 5;
                     }

                     switch (var31) {
                        case 0:
                           O[var0] = var19 - 1.0E-4;
                           k[var0] = Math.min(0.0, k[var0]) * 0.15;
                           break;
                        case 1:
                           O[var0] = var21 + 1.0E-4;
                           k[var0] = Math.max(0.0, k[var0]) * 0.15;
                           break;
                        case 2:
                           b[var0] = var23 - 1.0E-4;
                           z[var0] = Math.min(0.0, z[var0]) * 0.08;
                           break;
                        case 3:
                           b[var0] = var25 + 1.0E-4;
                           z[var0] = Math.max(0.0, z[var0]) * 0.08;
                           k[var0] = k[var0] * 0.97;
                           N[var0] = N[var0] * 0.97;
                           break;
                        case 4:
                           S[var0] = var27 - 1.0E-4;
                           N[var0] = Math.min(0.0, N[var0]) * 0.15;
                           break;
                        case 5:
                           S[var0] = var29 + 1.0E-4;
                           N[var0] = Math.max(0.0, N[var0]) * 0.15;
                     }
                  }
               }
            }
         }
      }
   }

   private static void y(int var0) {
      int var1 = --f;
      O[var0] = O[var1];
      b[var0] = b[var1];
      S[var0] = S[var1];
      E[var0] = E[var1];
      w[var0] = w[var1];
      G[var0] = G[var1];
      k[var0] = k[var1];
      z[var0] = z[var1];
      N[var0] = N[var1];
   }

   public static void Q() {
      int var0 = !Y.isEmpty() ? 1 : 0;
      f = 0;
      V = 0L;
      I = (boolean)0;
      Y.clear();
      X.clear();
      e.clear();
      d = null;
      if (var0 != 0) {
         at.U();
      }

      v_.U();
   }

   private v0() {
   }
}
