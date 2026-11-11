package com.larpclient.render.model;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.Map.Entry;

final class SahurMeshSplitter {
   private static SahurMesh.Mesh cachedBodySource;
   private static SahurMeshSplitter.BodySplit cachedBodySplit;
   private static SahurMesh.Mesh cachedArmSource;
   private static SahurMeshSplitter.JointSplit cachedArmSplit;
   private static SahurMesh.Mesh cachedLegSource;
   private static SahurMeshSplitter.JointSplit cachedLegSplit;

   private static SahurMeshSplitter.VertexKey vertexKey(float[] var0, int var1) {
      int var2 = var1 * 5;
      return new SahurMeshSplitter.VertexKey(
         Math.round(var0[var2] * 1000000.0F), Math.round(var0[var2 + 1] * 1000000.0F), Math.round(var0[var2 + 2] * 1000000.0F)
      );
   }

   private static int compareVertices(SahurMeshSplitter.VertexKey var0, SahurMeshSplitter.VertexKey var1) {
      int var2 = Integer.compare(var0.quantX, var1.quantX);
      if (var2 == 0) {
         var2 = Integer.compare(var0.quantY, var1.quantY);
      }

      return var2 == 0 ? Integer.compare(var0.quantZ, var1.quantZ) : var2;
   }

   private static boolean isArmFace(double var0, double var2, double var4) {
      int var6 = var0 < -0.014 && var2 < -0.035 && var4 > -0.113 - 0.65 * var2 ? 1 : 0;
      int var7 = var0 < -0.024 && var2 > -0.092 && var2 < 0.095 && var4 > -0.094 && var4 < -0.027 ? 1 : 0;
      return var6 != 0 || var7 != 0;
   }

   static SahurMeshSplitter.BodySplit splitMesh(SahurMesh.Mesh var0) {
      if (var0 == cachedBodySource) {
         return cachedBodySplit;
      }

      SahurMeshSplitter.JointSplit var1 = splitMesh(var0, false, false);
      boolean[] var2 = new boolean[var1.joints.length];

      for (int var3 = 0; var3 < var2.length; var3++) {
         var2[var3] = (boolean)(var1.joints[var3] == 1 ? 1 : 0);
      }

      cachedBodySource = var0;
      cachedBodySplit = new SahurMeshSplitter.BodySplit(var1.mesh, var2);
      return cachedBodySplit;
   }

   static SahurMeshSplitter.JointSplit splitArticulated(SahurMesh.Mesh var0) {
      if (var0 != cachedArmSource) {
         cachedArmSplit = splitMesh(var0, true, false);
         cachedArmSource = var0;
      }

      return cachedArmSplit;
   }

   static SahurMeshSplitter.JointSplit splitLegs(SahurMesh.Mesh var0) {
      if (var0 != cachedLegSource) {
         cachedLegSplit = splitMesh(var0, true, true);
         cachedLegSource = var0;
      }

      return cachedLegSplit;
   }

   private static int classifyJoint(double var0, double var2, double var4, double var6, boolean var8) {
      if (var0 < -0.014 && var2 < -0.095 && var4 > -0.05 + (-var2 - 0.095) * 0.42) {
         return var8 ? 1 : -1;
      }

      if (var2 < -0.104) {
         return var0 < 0.026 ? 3 : 4;
      }

      if (var2 > -0.104 && var2 < 0.096 && var6 < 0.095) {
         double[] var9 = new double[]{0.096, 0.035, -0.005, -0.045, -0.08, -0.104};
         double[] var10 = new double[]{-0.031, -0.038, -0.039, -0.036, -0.025, -0.014};
         double[] var11 = new double[]{0.09, 0.095, 0.095, 0.088, 0.074, 0.062};
         double[] var12 = new double[]{-0.067, -0.063, -0.052, -0.039, -0.035, -0.05};
         int var13 = 0;

         while (var13 < var9.length - 2 && var2 < var9[var13 + 1]) {
            var13++;
         }

         double var14 = Math.clamp((var9[var13] - var2) / (var9[var13] - var9[var13 + 1]), 0.0, 1.0);
         double var16 = var10[var13] + (var10[var13 + 1] - var10[var13]) * var14;
         double var18 = var11[var13] + (var11[var13 + 1] - var11[var13]) * var14;
         double var20 = var12[var13] + (var12[var13 + 1] - var12[var13]) * var14;
         double var22 = var2 < -0.06 ? 0.016 : 0.01;
         if (Math.hypot(var0 - var16, var4 - var20) < var22) {
            return 1;
         }

         if (Math.hypot(var0 - var18, var4 - var20) < var22) {
            return 2;
         }
      }

      return 0;
   }

   private static SahurMeshSplitter.JointSplit splitMesh(SahurMesh.Mesh var0, boolean var1, boolean var2) {
      float[] var3 = var0.vertices();
      int var4 = var0.colors().length;
      int[] var5 = new int[var4 / 3];
      HashMap var6 = new HashMap();
      HashMap var7 = new HashMap();

      for (int var8 = 0; var8 < var5.length; var8++) {
         double var9 = 0.0;
         double var11 = 0.0;
         double var13 = 0.0;
         double var15 = 1.0;
         double var17 = -1.0;

         for (int var19 = 0; var19 < 3; var19++) {
            int var20 = (var8 * 3 + var19) * 5;
            var9 += var3[var20] / 3.0;
            var11 += var3[var20 + 1] / 3.0;
            var13 += var3[var20 + 2] / 3.0;
            var15 = Math.min(var15, var3[var20 + 1]);
            var17 = Math.max(var17, var3[var20 + 1]);
         }

         var5[var8] = var1 ? classifyJoint(var9, var11, var13, var17 - var15, var2) : (isArmFace(var9, var11, var13) ? 1 : 0);

         for (int var63 = 0; var63 < 3; var63++) {
            int var70 = var8 * 3 + var63;
            int var21 = var8 * 3 + (var63 + 1) % 3;
            SahurMeshSplitter.VertexKey var22 = vertexKey(var3, var70);
            SahurMeshSplitter.VertexKey var23 = vertexKey(var3, var21);
            var7.putIfAbsent(var22, var70);
            var7.putIfAbsent(var23, var21);
            if (!var22.equals(var23)) {
               SahurMeshSplitter.EdgeKey var24 = compareVertices(var22, var23) < 0
                  ? new SahurMeshSplitter.EdgeKey(var22, var23)
                  : new SahurMeshSplitter.EdgeKey(var23, var22);
               var6.computeIfAbsent(var24, var0x -> new ArrayList<>()).add(new SahurMeshSplitter.EdgeUse(var8, var70, var21));
            }
         }
      }

      ArrayList var26 = new ArrayList();

      for (int var27 = 0; var27 < var5.length; var27++) {
         var26.add(new HashSet());
      }

      for (ArrayList var10 : var6.values()) {
         if (var10.size() == 2) {
            ((Set)var26.get(((SahurMeshSplitter.EdgeUse)var10.get(0)).face)).add(((SahurMeshSplitter.EdgeUse)var10.get(1)).face);
            ((Set)var26.get(((SahurMeshSplitter.EdgeUse)var10.get(1)).face)).add(((SahurMeshSplitter.EdgeUse)var10.get(0)).face);
         }
      }

      int[] var29 = (int[])var5.clone();

      for (int var30 = 0; var30 < var5.length; var30++) {
         int var32 = ((Set)var26.get(var30)).size() == 3 ? 1 : 0;

         for (int var37 : (Set)var26.get(var30)) {
            var32 &= var5[var30] != var5[var37] ? 1 : 0;
         }

         if (var32 != 0 && (!var1 || var5[var30] >= 0)) {
            int var34 = var5[((Set)var26.get(var30)).iterator().next()];
            byte var38 = 1;

            for (int var44 : (Set)var26.get(var30)) {
               var38 &= var5[var44] == var34 ? 1 : 0;
            }

            if (var38 != 0) {
               var29[var30] = var34;
            }
         }
      }

      var5 = var29;
      ArrayList var31 = new ArrayList();
      HashMap var33 = new HashMap();

      for (Entry var39 : var6.entrySet()) {
         ArrayList var41 = (ArrayList)var39.getValue();
         if (var41.size() == 2 && var5[((SahurMeshSplitter.EdgeUse)var41.get(0)).face] != var5[((SahurMeshSplitter.EdgeUse)var41.get(1)).face]) {
            for (SahurMeshSplitter.EdgeUse var16 : var41) {
               if (var5[var16.face] >= 0) {
                  var31.add(var16);
               }
            }

            SahurMeshSplitter.VertexKey var46 = ((SahurMeshSplitter.EdgeKey)var39.getKey()).low;
            SahurMeshSplitter.VertexKey var50 = ((SahurMeshSplitter.EdgeKey)var39.getKey()).high;
            var33.computeIfAbsent(var46, var0x -> new HashSet<>()).add(var50);
            var33.computeIfAbsent(var50, var0x -> new HashSet<>()).add(var46);
         }
      }

      if (var31.size() > 512) {
         throw new IllegalStateException("Sahur articulation exceeds seam budget");
      }

      HashMap var36 = new HashMap();
      HashSet var40 = new HashSet();

      for (SahurMeshSplitter.VertexKey var47 : var33.keySet()) {
         if (var40.add(var47)) {
            ArrayList var51 = new ArrayList();
            ArrayDeque var55 = new ArrayDeque();
            var55.add(var47);

            while (!var55.isEmpty()) {
               SahurMeshSplitter.VertexKey var18 = (SahurMeshSplitter.VertexKey)var55.removeFirst();
               var51.add(var18);

               for (SahurMeshSplitter.VertexKey var71 : (Set)var33.get(var18)) {
                  if (var40.add(var71)) {
                     var55.addLast(var71);
                  }
               }
            }

            float[] var59 = new float[5];

            for (SahurMeshSplitter.VertexKey var72 : var51) {
               for (int var75 = 0; var75 < 5; var75++) {
                  var59[var75] += var3[var7.get(var72) * 5 + var75] / var51.size();
               }
            }

            for (SahurMeshSplitter.VertexKey var73 : var51) {
               var36.put(var73, var59);
            }
         }
      }

      ArrayList var43 = new ArrayList();

      for (SahurMeshSplitter.EdgeUse var52 : var31) {
         int var56 = var0.colors()[var52.from];
         int var60 = -16777216;

         for (byte var67 = 0; var67 < 24; var67 += 8) {
            var60 |= (int)((var56 >>> var67 & 0xFF) * 0.72) << var67;
         }

         var43.add(new SahurMeshSplitter.SeamTriangle(var52.end, var52.from, (float[])var36.get(vertexKey(var3, var52.from)), var60, var5[var52.face]));
      }

      byte var49 = 0;

      for (int var68 : var5) {
         if (var68 >= 0) {
            var49 += 3;
         }
      }

      int var54 = var49 + var43.size() * 3;
      float[] var58 = new float[var54 * 5];
      int[] var62 = new int[var54];
      int[] var69 = new int[var54];
      int var74 = 0;

      for (int var76 = 0; var76 < var4; var76++) {
         if (var5[var76 / 3] >= 0) {
            System.arraycopy(var3, var76 * 5, var58, var74 * 5, 5);
            var62[var74] = var0.colors()[var76];
            var69[var74++] = var5[var76 / 3];
         }
      }

      for (SahurMeshSplitter.SeamTriangle var78 : var43) {
         System.arraycopy(var3, var78.from * 5, var58, var74 * 5, 5);
         System.arraycopy(var3, var78.end * 5, var58, (var74 + 1) * 5, 5);
         System.arraycopy(var78.center, 0, var58, (var74 + 2) * 5, 5);

         for (int var79 = 0; var79 < 3; var79++) {
            var58[(var74 + var79) * 5 + 3] = 0.58F;
            var58[(var74 + var79) * 5 + 4] = 0.52F;
            var62[var74 + var79] = var78.color;
            var69[var74 + var79] = var78.joint;
         }

         var74 += 3;
      }

      return new SahurMeshSplitter.JointSplit(new SahurMesh.Mesh(var58, var62, var0.minimumY(), var0.height()), var69);
   }

   private SahurMeshSplitter() {
   }

   record BodySplit(SahurMesh.Mesh mesh, boolean[] arm) {
   }

   private record EdgeKey(SahurMeshSplitter.VertexKey a, SahurMeshSplitter.VertexKey b) {
      private final SahurMeshSplitter.VertexKey low;
      private final SahurMeshSplitter.VertexKey high;

      private EdgeKey(SahurMeshSplitter.VertexKey a, SahurMeshSplitter.VertexKey b) {
         this.low = a;
         this.high = b;
      }
   }

   private record EdgeUse(int face, int from, int to) {
      private final int end;

      private EdgeUse(int face, int from, int to) {
         this.face = face;
         this.from = from;
         this.end = to;
      }
   }

   record JointSplit(SahurMesh.Mesh mesh, int[] joints) {
   }

   private record SeamTriangle(int from, int to, float[] center, int color, int joint) {
      private final int end;

      private SeamTriangle(int from, int to, float[] center, int color, int joint) {
         this.from = from;
         this.end = to;
         this.center = center;
         this.color = color;
         this.joint = joint;
      }
   }

   private record VertexKey(int x, int y, int z) {
      private final int quantX;
      private final int quantY;
      private final int quantZ;

      private VertexKey(int x, int y, int z) {
         this.quantX = x;
         this.quantY = y;
         this.quantZ = z;
      }
   }
}
