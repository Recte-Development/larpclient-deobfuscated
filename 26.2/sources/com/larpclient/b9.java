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

public final class b9 {
   public static final int Q = 4096;
   private static final int k = 16384;
   private static final int C = 48;
   private static final int L = 8192;
   private static final double w = 0.1;
   private static final double n = 0.064;
   private static final double B = 0.18;
   private static final double[] y = new double[4096];
   private static final double[] a = new double[4096];
   private static final double[] l = new double[4096];
   private static final double[] p = new double[4096];
   private static final double[] c = new double[4096];
   private static final double[] d = new double[4096];
   private static final double[] F = new double[4096];
   private static final double[] X = new double[4096];
   private static final double[] H = new double[4096];
   private static final double[] g = new double[4096];
   private static final double[] h = new double[4096];
   private static final double[] r = new double[4096];
   private static final double[] I = new double[4096];
   private static final double[] N = new double[4096];
   private static final double[] v = new double[4096];
   private static final int[] q = new int[16384];
   private static final int[] Y = new int[4096];
   private static final int[] u = new int[4096];
   private static final int[] o = new int[4096];
   private static final int[] U = new int[4096];
   private static final Set<Long> e = ConcurrentHashMap.newKeySet();
   private static final Map<Long, List<AABB>> M = new HashMap<>();
   private static final Map<Long, Long> j = new HashMap<>();
   private static ClientLevel b;
   private static int D;
   private static long G;
   private static boolean O;

   private static boolean T() {
      RealisticWaterModule var0 = RealisticWaterModule.K();
      return var0 != null && var0.isRuntimeActive() && var0.k.getValue() && !w6.C();
   }

   public static int e() {
      return D;
   }

   public static boolean K(BlockPos var0) {
      return T() && e.contains(var0.asLong());
   }

   public static double i() {
      return 0.064;
   }

   public static double l(int var0, float var1) {
      return p[var0] + (y[var0] - p[var0]) * var1;
   }

   public static double B(int var0, float var1) {
      return c[var0] + (a[var0] - c[var0]) * var1;
   }

   public static double I(int var0, float var1) {
      return d[var0] + (l[var0] - d[var0]) * var1;
   }

   public static void w(BlockPos var0, BlockState var1, BlockState var2) {
      if (T()) {
         Minecraft var3 = Minecraft.getInstance();
         if (var3 != null
            && var3.level != null
            && var3.player != null
            && !(var3.player.distanceToSqr(var0.getX() + 0.5, var0.getY() + 0.5, var0.getZ() + 0.5) > 49.0)) {
            o(var3.level);
            boolean var4 = var1.getFluidState().is(FluidTags.WATER);
            boolean var5 = var2.getFluidState().is(FluidTags.WATER);
            if (!var1.isAir() && !var4 && var2.isAir()) {
               if (j.size() < 128) {
                  j.put(var0.asLong(), G + 20L);
               }

               for (Direction var9 : Direction.values()) {
                  if (var3.level.getFluidState(var0.relative(var9)).is(FluidTags.WATER)) {
                     L(var0);
                     break;
                  }
               }
            }

            if (var5 && (!var4 || var2.getFluidState().isSource() && !var1.getFluidState().isSource())) {
               byte var11 = 0;

               for (Direction var10 : Direction.values()) {
                  if (e.contains(var0.relative(var10).asLong())) {
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
                  J(var0, true);
               } else if (j.containsKey(var0.asLong())) {
                  L(var0);
               } else if (var11 != 0) {
                  D(var0);
               }
            }

            if (!var5) {
               e.remove(var0.asLong());
            }
         }
      }
   }

   private static void D(BlockPos var0) {
      if (e.size() < 4096 && e.add(var0.asLong())) {
         Minecraft var1 = Minecraft.getInstance();
         if (var1 != null && var1.level != null) {
            var1.levelExtractor.setSectionDirtyWithNeighbors(var0.getX() >> 4, var0.getY() >> 4, var0.getZ() >> 4);
         }
      }
   }

   private static void L(BlockPos var0) {
      J(var0, false);
   }

   private static void J(BlockPos var0, boolean var1) {
      if (var1 || !e.contains(var0.asLong())) {
         if (D + 1000 > 4096) {
            if (!O) {
               com.larpclient.h.A("TerrainMaterials", "Volumetric water capacity reached ({} droplets); further pours retain native water", 4096);
               O = (boolean)1;
            }
         } else {
            for (int var2 = 0; var2 < 10; var2++) {
               for (int var3 = 0; var3 < 10; var3++) {
                  for (int var4 = 0; var4 < 10; var4++) {
                     int var5 = D++;
                     double var6 = ((var4 * 7 + var2 * 13 + var3 * 17) % 11 - 5) * 5.0E-4;
                     y[var5] = p[var5] = var0.getX() + (var4 + 0.5) * 0.1 + var6;
                     a[var5] = c[var5] = var0.getY() + (var2 + 0.5) * 0.1 - var6;
                     l[var5] = d[var5] = var0.getZ() + (var3 + 0.5) * 0.1;
                     F[var5] = X[var5] = H[var5] = 0.0;
                  }
               }
            }

            D(var0);
            com.larpclient.h.U("TerrainMaterials", "Volumetric pour accepted at {}; droplets={}", var0, D);
         }
      }
   }

   public static void p(Minecraft var0) {
      if (!T()) {
         if (D > 0 || !e.isEmpty()) {
            b();
         }
      } else if (var0.level != null && var0.player != null && !var0.isPaused()) {
         o(var0.level);
         G++;
         j.entrySet().removeIf(var0x -> var0x.getValue() < G);
         if (D != 0) {
            System.arraycopy(y, 0, p, 0, D);
            System.arraycopy(a, 0, c, 0, D);
            System.arraycopy(l, 0, d, 0, D);
            M.clear();

            for (int var1 = 0; var1 < 2; var1++) {
               t(var0, 0.025);
            }
         }
      }
   }

   private static void o(ClientLevel var0) {
      if (b != var0) {
         b();
         b = var0;
      }
   }

   private static void t(Minecraft var0, double var1) {
      for (int var3 = 0; var3 < D; var3++) {
         g[var3] = y[var3];
         h[var3] = a[var3];
         r[var3] = l[var3];
         if (!b.hasChunkAt(BlockPos.containing(y[var3], a[var3], l[var3]))) {
            F[var3] = X[var3] = H[var3] = 0.0;
         } else {
            X[var3] = Math.max(-6.0, X[var3] - 9.8 * var1);
            double var4 = y[var3];
            double var6 = a[var3];
            double var8 = l[var3];
            y[var3] = y[var3] + F[var3] * var1;
            a[var3] = a[var3] + X[var3] * var1;
            l[var3] = l[var3] + H[var3] * var1;
            V(var3, var4, var6, var8);
         }
      }

      for (int var27 = 0; var27 < 2; var27++) {
         Q();
         Arrays.fill(I, 0, D, 0.0);
         Arrays.fill(N, 0, D, 0.0);
         Arrays.fill(v, 0, D, 0.0);

         label108:
         for (int var29 = 0; var29 < D; var29++) {
            int var5 = 0;

            for (int var34 = -1; var34 <= 1; var34++) {
               for (int var7 = -1; var7 <= 1; var7++) {
                  for (int var36 = -1; var36 <= 1; var36++) {
                     int var9 = u[var29] + var36;
                     int var10 = o[var29] + var7;
                     int var11 = U[var29] + var34;

                     for (int var12 = q[R(var9, var10, var11)]; var12 >= 0; var12 = Y[var12]) {
                        if (var12 != var29 && u[var12] == var9 && o[var12] == var10 && U[var12] == var11) {
                           double var13 = y[var29] - y[var12];
                           double var15 = a[var29] - a[var12];
                           double var17 = l[var29] - l[var12];
                           double var19 = var13 * var13 + var15 * var15 + var17 * var17;
                           if (!(var19 <= 1.0E-10) && !(var19 > 0.0289)) {
                              if (++var5 > 48) {
                                 continue label108;
                              }

                              double var21 = Math.sqrt(var19);
                              double var23 = var21 < 0.1 ? (0.1 - var21) * 0.22 : -(var21 - 0.1) * 0.014 * (1.0 - var21 / 0.17);
                              double var25 = var23 / var21;
                              I[var29] = I[var29] + var13 * var25;
                              N[var29] = N[var29] + var15 * var25;
                              v[var29] = v[var29] + var17 * var25;
                           }
                        }
                     }
                  }
               }
            }
         }

         for (int var30 = 0; var30 < D; var30++) {
            double var32 = y[var30];
            double var35 = a[var30];
            double var37 = l[var30];
            y[var30] = y[var30] + Math.clamp(I[var30], -0.015, 0.015);
            a[var30] = a[var30] + Math.clamp(N[var30], -0.015, 0.015);
            l[var30] = l[var30] + Math.clamp(v[var30], -0.015, 0.015);
            V(var30, var32, var35, var37);
         }
      }

      AABB var28 = var0.player.getBoundingBox().inflate(0.064);
      Vec3 var31 = var0.player.getDeltaMovement();

      for (int var33 = 0; var33 < D; var33++) {
         F[var33] = (y[var33] - g[var33]) / var1;
         X[var33] = (a[var33] - h[var33]) / var1;
         H[var33] = (l[var33] - r[var33]) / var1;
         if (var28.contains(y[var33], a[var33], l[var33])) {
            F[var33] = F[var33] + var31.x * 3.0;
            H[var33] = H[var33] + var31.z * 3.0;
            X[var33] = X[var33] + Math.max(0.0, var31.y) * 2.0;
         }

         F[var33] = Math.clamp(F[var33] * 0.99, -6.0, 6.0);
         X[var33] = Math.clamp(X[var33] * 0.99, -6.0, 6.0);
         H[var33] = Math.clamp(H[var33] * 0.99, -6.0, 6.0);
         if (!Double.isFinite(y[var33] + a[var33] + l[var33]) || a[var33] < b.getMinY() - 8) {
            k(var33--);
         }
      }
   }

   private static void Q() {
      Arrays.fill(q, -1);
      int var0 = 0;

      while (var0 < D) {
         u[var0] = (int)Math.floor(y[var0] / 0.18);
         o[var0] = (int)Math.floor(a[var0] / 0.18);
         U[var0] = (int)Math.floor(l[var0] / 0.18);
         int var1 = R(u[var0], o[var0], U[var0]);
         Y[var0] = q[var1];
         q[var1] = var0++;
      }
   }

   private static int R(int var0, int var1, int var2) {
      return (var0 * 73856093 ^ var1 * 19349663 ^ var2 * 83492791) & 16383;
   }

   private static void V(int var0, double var1, double var3, double var5) {
      int var7 = (int)Math.floor(y[var0]);
      int var8 = (int)Math.floor(a[var0]);
      int var9 = (int)Math.floor(l[var0]);

      for (int var10 = -1; var10 <= 1; var10++) {
         for (int var11 = -1; var11 <= 1; var11++) {
            for (int var12 = -1; var12 <= 1; var12++) {
               BlockPos var13 = new BlockPos(var7 + var12, var8 + var11, var9 + var10);
               List var14 = M.get(var13.asLong());
               if (var14 == null) {
                  if (M.size() >= 8192 || !b.hasChunkAt(var13)) {
                     y[var0] = var1;
                     a[var0] = var3;
                     l[var0] = var5;
                     F[var0] = X[var0] = H[var0] = 0.0;
                     return;
                  }

                  var14 = b.getBlockState(var13).getCollisionShape(b, var13).toAabbs();
                  M.put(var13.asLong(), var14);
               }

               for (AABB var16 : var14) {
                  double var17 = 0.0352;
                  double var19 = var16.minX + var13.getX() - var17;
                  double var21 = var16.maxX + var13.getX() + var17;
                  double var23 = var16.minY + var13.getY() - var17;
                  double var25 = var16.maxY + var13.getY() + var17;
                  double var27 = var16.minZ + var13.getZ() - var17;
                  double var29 = var16.maxZ + var13.getZ() + var17;
                  if (!(y[var0] <= var19) && !(y[var0] >= var21) && !(a[var0] <= var23) && !(a[var0] >= var25) && !(l[var0] <= var27) && !(l[var0] >= var29)) {
                     byte var31 = 0;
                     double var32 = y[var0] - var19;
                     double var34 = var21 - y[var0];
                     if (var34 < var32) {
                        var32 = var34;
                        var31 = 1;
                     }

                     var34 = a[var0] - var23;
                     if (var34 < var32) {
                        var32 = var34;
                        var31 = 2;
                     }

                     var34 = var25 - a[var0];
                     if (var34 < var32) {
                        var32 = var34;
                        var31 = 3;
                     }

                     var34 = l[var0] - var27;
                     if (var34 < var32) {
                        var32 = var34;
                        var31 = 4;
                     }

                     var34 = var29 - l[var0];
                     if (var34 < var32) {
                        var31 = 5;
                     }

                     switch (var31) {
                        case 0:
                           y[var0] = var19 - 1.0E-4;
                           F[var0] = Math.min(0.0, F[var0]) * 0.15;
                           break;
                        case 1:
                           y[var0] = var21 + 1.0E-4;
                           F[var0] = Math.max(0.0, F[var0]) * 0.15;
                           break;
                        case 2:
                           a[var0] = var23 - 1.0E-4;
                           X[var0] = Math.min(0.0, X[var0]) * 0.08;
                           break;
                        case 3:
                           a[var0] = var25 + 1.0E-4;
                           X[var0] = Math.max(0.0, X[var0]) * 0.08;
                           F[var0] = F[var0] * 0.97;
                           H[var0] = H[var0] * 0.97;
                           break;
                        case 4:
                           l[var0] = var27 - 1.0E-4;
                           H[var0] = Math.min(0.0, H[var0]) * 0.15;
                           break;
                        case 5:
                           l[var0] = var29 + 1.0E-4;
                           H[var0] = Math.max(0.0, H[var0]) * 0.15;
                     }
                  }
               }
            }
         }
      }
   }

   private static void k(int var0) {
      int var1 = --D;
      y[var0] = y[var1];
      a[var0] = a[var1];
      l[var0] = l[var1];
      p[var0] = p[var1];
      c[var0] = c[var1];
      d[var0] = d[var1];
      F[var0] = F[var1];
      X[var0] = X[var1];
      H[var0] = H[var1];
   }

   public static void b() {
      int var0 = !e.isEmpty() ? 1 : 0;
      D = 0;
      G = 0L;
      O = (boolean)0;
      e.clear();
      M.clear();
      j.clear();
      b = null;
      if (var0 != 0) {
         w6.N();
      }

      tt.a();
   }

   private b9() {
   }
}
