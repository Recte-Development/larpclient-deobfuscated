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

public final class hr {
   public static final int C = 4096;
   private static final int t = 16384;
   private static final int L = 48;
   private static final int p = 8192;
   private static final double a = 0.1;
   private static final double z = 0.064;
   private static final double n = 0.18;
   private static final double[] i = new double[4096];
   private static final double[] o = new double[4096];
   private static final double[] O = new double[4096];
   private static final double[] Z = new double[4096];
   private static final double[] d = new double[4096];
   private static final double[] B = new double[4096];
   private static final double[] V = new double[4096];
   private static final double[] D = new double[4096];
   private static final double[] h = new double[4096];
   private static final double[] b = new double[4096];
   private static final double[] c = new double[4096];
   private static final double[] K = new double[4096];
   private static final double[] P = new double[4096];
   private static final double[] u = new double[4096];
   private static final double[] X = new double[4096];
   private static final int[] W = new int[16384];
   private static final int[] H = new int[4096];
   private static final int[] e = new int[4096];
   private static final int[] w = new int[4096];
   private static final int[] k = new int[4096];
   private static final Set<Long> l = ConcurrentHashMap.newKeySet();
   private static final Map<Long, List<AABB>> v = new HashMap<>();
   private static final Map<Long, Long> E = new HashMap<>();
   private static ClientLevel j;
   private static int x;
   private static long A;
   private static boolean M;

   private static boolean A() {
      RealisticWaterModule var0 = RealisticWaterModule.G();
      return var0 != null && var0.isRuntimeActive() && var0.V.P() && !com.larpclient.k.K();
   }

   public static int f() {
      return x;
   }

   public static boolean p(BlockPos var0) {
      return A() && l.contains(var0.asLong());
   }

   public static double f() {
      return 0.064;
   }

   public static double s(int var0, float var1) {
      return Z[var0] + (i[var0] - Z[var0]) * var1;
   }

   public static double G(int var0, float var1) {
      return d[var0] + (o[var0] - d[var0]) * var1;
   }

   public static double I(int var0, float var1) {
      return B[var0] + (O[var0] - B[var0]) * var1;
   }

   public static void E(BlockPos var0, BlockState var1, BlockState var2) {
      if (A()) {
         Minecraft var3 = Minecraft.getInstance();
         if (var3 != null
            && var3.level != null
            && var3.player != null
            && !(var3.player.distanceToSqr(var0.getX() + 0.5, var0.getY() + 0.5, var0.getZ() + 0.5) > 49.0)) {
            S(var3.level);
            boolean var4 = var1.getFluidState().is(FluidTags.WATER);
            boolean var5 = var2.getFluidState().is(FluidTags.WATER);
            if (!var1.isAir() && !var4 && var2.isAir()) {
               if (E.size() < 128) {
                  E.put(var0.asLong(), A + 20L);
               }

               for (Direction var9 : Direction.values()) {
                  if (var3.level.getFluidState(var0.relative(var9)).is(FluidTags.WATER)) {
                     B(var0);
                     break;
                  }
               }
            }

            if (var5 && (!var4 || var2.getFluidState().isSource() && !var1.getFluidState().isSource())) {
               byte var11 = 0;

               for (Direction var10 : Direction.values()) {
                  if (l.contains(var0.relative(var10).asLong())) {
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
                  o(var0, true);
               } else if (E.containsKey(var0.asLong())) {
                  B(var0);
               } else if (var11 != 0) {
                  J(var0);
               }
            }

            if (!var5) {
               l.remove(var0.asLong());
            }
         }
      }
   }

   private static void J(BlockPos var0) {
      if (l.size() < 4096 && l.add(var0.asLong())) {
         Minecraft var1 = Minecraft.getInstance();
         if (var1 != null && var1.level != null) {
            var1.levelExtractor.setSectionDirtyWithNeighbors(var0.getX() >> 4, var0.getY() >> 4, var0.getZ() >> 4);
         }
      }
   }

   private static void B(BlockPos var0) {
      o(var0, false);
   }

   private static void o(BlockPos var0, boolean var1) {
      if (var1 || !l.contains(var0.asLong())) {
         if (x + 1000 > 4096) {
            if (!M) {
               mt.K("TerrainMaterials", "Volumetric water capacity reached ({} droplets); further pours retain native water", 4096);
               M = (boolean)1;
            }
         } else {
            for (int var2 = 0; var2 < 10; var2++) {
               for (int var3 = 0; var3 < 10; var3++) {
                  for (int var4 = 0; var4 < 10; var4++) {
                     int var5 = x++;
                     double var6 = ((var4 * 7 + var2 * 13 + var3 * 17) % 11 - 5) * 5.0E-4;
                     i[var5] = Z[var5] = var0.getX() + (var4 + 0.5) * 0.1 + var6;
                     o[var5] = d[var5] = var0.getY() + (var2 + 0.5) * 0.1 - var6;
                     O[var5] = B[var5] = var0.getZ() + (var3 + 0.5) * 0.1;
                     V[var5] = D[var5] = h[var5] = 0.0;
                  }
               }
            }

            J(var0);
            mt.U("TerrainMaterials", "Volumetric pour accepted at {}; droplets={}", var0, x);
         }
      }
   }

   public static void t(Minecraft var0) {
      if (!A()) {
         if (x > 0 || !l.isEmpty()) {
            E();
         }
      } else if (var0.level != null && var0.player != null && !var0.isPaused()) {
         S(var0.level);
         A++;
         E.entrySet().removeIf(var0x -> var0x.getValue() < A);
         if (x != 0) {
            System.arraycopy(i, 0, Z, 0, x);
            System.arraycopy(o, 0, d, 0, x);
            System.arraycopy(O, 0, B, 0, x);
            v.clear();

            for (int var1 = 0; var1 < 2; var1++) {
               T(var0, 0.025);
            }
         }
      }
   }

   private static void S(ClientLevel var0) {
      if (j != var0) {
         E();
         j = var0;
      }
   }

   private static void T(Minecraft var0, double var1) {
      for (int var3 = 0; var3 < x; var3++) {
         b[var3] = i[var3];
         c[var3] = o[var3];
         K[var3] = O[var3];
         if (!j.hasChunkAt(BlockPos.containing(i[var3], o[var3], O[var3]))) {
            V[var3] = D[var3] = h[var3] = 0.0;
         } else {
            D[var3] = Math.max(-6.0, D[var3] - 9.8 * var1);
            double var4 = i[var3];
            double var6 = o[var3];
            double var8 = O[var3];
            i[var3] = i[var3] + V[var3] * var1;
            o[var3] = o[var3] + D[var3] * var1;
            O[var3] = O[var3] + h[var3] * var1;
            K(var3, var4, var6, var8);
         }
      }

      for (int var27 = 0; var27 < 2; var27++) {
         x();
         Arrays.fill(P, 0, x, 0.0);
         Arrays.fill(u, 0, x, 0.0);
         Arrays.fill(X, 0, x, 0.0);

         label108:
         for (int var29 = 0; var29 < x; var29++) {
            int var5 = 0;

            for (int var34 = -1; var34 <= 1; var34++) {
               for (int var7 = -1; var7 <= 1; var7++) {
                  for (int var36 = -1; var36 <= 1; var36++) {
                     int var9 = e[var29] + var36;
                     int var10 = w[var29] + var7;
                     int var11 = k[var29] + var34;

                     for (int var12 = W[z(var9, var10, var11)]; var12 >= 0; var12 = H[var12]) {
                        if (var12 != var29 && e[var12] == var9 && w[var12] == var10 && k[var12] == var11) {
                           double var13 = i[var29] - i[var12];
                           double var15 = o[var29] - o[var12];
                           double var17 = O[var29] - O[var12];
                           double var19 = var13 * var13 + var15 * var15 + var17 * var17;
                           if (!(var19 <= 1.0E-10) && !(var19 > 0.0289)) {
                              if (++var5 > 48) {
                                 continue label108;
                              }

                              double var21 = Math.sqrt(var19);
                              double var23 = var21 < 0.1 ? (0.1 - var21) * 0.22 : -(var21 - 0.1) * 0.014 * (1.0 - var21 / 0.17);
                              double var25 = var23 / var21;
                              P[var29] = P[var29] + var13 * var25;
                              u[var29] = u[var29] + var15 * var25;
                              X[var29] = X[var29] + var17 * var25;
                           }
                        }
                     }
                  }
               }
            }
         }

         for (int var30 = 0; var30 < x; var30++) {
            double var32 = i[var30];
            double var35 = o[var30];
            double var37 = O[var30];
            i[var30] = i[var30] + Math.clamp(P[var30], -0.015, 0.015);
            o[var30] = o[var30] + Math.clamp(u[var30], -0.015, 0.015);
            O[var30] = O[var30] + Math.clamp(X[var30], -0.015, 0.015);
            K(var30, var32, var35, var37);
         }
      }

      AABB var28 = var0.player.getBoundingBox().inflate(0.064);
      Vec3 var31 = var0.player.getDeltaMovement();

      for (int var33 = 0; var33 < x; var33++) {
         V[var33] = (i[var33] - b[var33]) / var1;
         D[var33] = (o[var33] - c[var33]) / var1;
         h[var33] = (O[var33] - K[var33]) / var1;
         if (var28.contains(i[var33], o[var33], O[var33])) {
            V[var33] = V[var33] + var31.x * 3.0;
            h[var33] = h[var33] + var31.z * 3.0;
            D[var33] = D[var33] + Math.max(0.0, var31.y) * 2.0;
         }

         V[var33] = Math.clamp(V[var33] * 0.99, -6.0, 6.0);
         D[var33] = Math.clamp(D[var33] * 0.99, -6.0, 6.0);
         h[var33] = Math.clamp(h[var33] * 0.99, -6.0, 6.0);
         if (!Double.isFinite(i[var33] + o[var33] + O[var33]) || o[var33] < j.getMinY() - 8) {
            W(var33--);
         }
      }
   }

   private static void x() {
      Arrays.fill(W, -1);
      int var0 = 0;

      while (var0 < x) {
         e[var0] = (int)Math.floor(i[var0] / 0.18);
         w[var0] = (int)Math.floor(o[var0] / 0.18);
         k[var0] = (int)Math.floor(O[var0] / 0.18);
         int var1 = z(e[var0], w[var0], k[var0]);
         H[var0] = W[var1];
         W[var1] = var0++;
      }
   }

   private static int z(int var0, int var1, int var2) {
      return (var0 * 73856093 ^ var1 * 19349663 ^ var2 * 83492791) & 16383;
   }

   private static void K(int var0, double var1, double var3, double var5) {
      int var7 = (int)Math.floor(i[var0]);
      int var8 = (int)Math.floor(o[var0]);
      int var9 = (int)Math.floor(O[var0]);

      for (int var10 = -1; var10 <= 1; var10++) {
         for (int var11 = -1; var11 <= 1; var11++) {
            for (int var12 = -1; var12 <= 1; var12++) {
               BlockPos var13 = new BlockPos(var7 + var12, var8 + var11, var9 + var10);
               List var14 = v.get(var13.asLong());
               if (var14 == null) {
                  if (v.size() >= 8192 || !j.hasChunkAt(var13)) {
                     i[var0] = var1;
                     o[var0] = var3;
                     O[var0] = var5;
                     V[var0] = D[var0] = h[var0] = 0.0;
                     return;
                  }

                  var14 = j.getBlockState(var13).getCollisionShape(j, var13).toAabbs();
                  v.put(var13.asLong(), var14);
               }

               for (AABB var16 : var14) {
                  double var17 = 0.0352;
                  double var19 = var16.minX + var13.getX() - var17;
                  double var21 = var16.maxX + var13.getX() + var17;
                  double var23 = var16.minY + var13.getY() - var17;
                  double var25 = var16.maxY + var13.getY() + var17;
                  double var27 = var16.minZ + var13.getZ() - var17;
                  double var29 = var16.maxZ + var13.getZ() + var17;
                  if (!(i[var0] <= var19) && !(i[var0] >= var21) && !(o[var0] <= var23) && !(o[var0] >= var25) && !(O[var0] <= var27) && !(O[var0] >= var29)) {
                     byte var31 = 0;
                     double var32 = i[var0] - var19;
                     double var34 = var21 - i[var0];
                     if (var34 < var32) {
                        var32 = var34;
                        var31 = 1;
                     }

                     var34 = o[var0] - var23;
                     if (var34 < var32) {
                        var32 = var34;
                        var31 = 2;
                     }

                     var34 = var25 - o[var0];
                     if (var34 < var32) {
                        var32 = var34;
                        var31 = 3;
                     }

                     var34 = O[var0] - var27;
                     if (var34 < var32) {
                        var32 = var34;
                        var31 = 4;
                     }

                     var34 = var29 - O[var0];
                     if (var34 < var32) {
                        var31 = 5;
                     }

                     switch (var31) {
                        case 0:
                           i[var0] = var19 - 1.0E-4;
                           V[var0] = Math.min(0.0, V[var0]) * 0.15;
                           break;
                        case 1:
                           i[var0] = var21 + 1.0E-4;
                           V[var0] = Math.max(0.0, V[var0]) * 0.15;
                           break;
                        case 2:
                           o[var0] = var23 - 1.0E-4;
                           D[var0] = Math.min(0.0, D[var0]) * 0.08;
                           break;
                        case 3:
                           o[var0] = var25 + 1.0E-4;
                           D[var0] = Math.max(0.0, D[var0]) * 0.08;
                           V[var0] = V[var0] * 0.97;
                           h[var0] = h[var0] * 0.97;
                           break;
                        case 4:
                           O[var0] = var27 - 1.0E-4;
                           h[var0] = Math.min(0.0, h[var0]) * 0.15;
                           break;
                        case 5:
                           O[var0] = var29 + 1.0E-4;
                           h[var0] = Math.max(0.0, h[var0]) * 0.15;
                     }
                  }
               }
            }
         }
      }
   }

   private static void W(int var0) {
      int var1 = --x;
      i[var0] = i[var1];
      o[var0] = o[var1];
      O[var0] = O[var1];
      Z[var0] = Z[var1];
      d[var0] = d[var1];
      B[var0] = B[var1];
      V[var0] = V[var1];
      D[var0] = D[var1];
      h[var0] = h[var1];
   }

   public static void E() {
      int var0 = !l.isEmpty() ? 1 : 0;
      x = 0;
      A = 0L;
      M = (boolean)0;
      l.clear();
      v.clear();
      E.clear();
      j = null;
      if (var0 != 0) {
         com.larpclient.k.k();
      }

      ua.P();
   }

   private hr() {
   }
}
