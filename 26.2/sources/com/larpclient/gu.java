package com.larpclient;

import java.util.ArrayList;
import java.util.List;
import java.util.SplittableRandom;
import java.util.function.ToIntFunction;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos.MutableBlockPos;
import net.minecraft.core.Direction.Plane;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public final class gu {
   static final int I = 40;
   static final int w = 400;
   static final int h = 20;
   private final ar d;
   private final hc p;
   private final gu.z o;
   private List<zf.h> T = List.of();
   private int N;
   private int E;
   private int a;
   private int c;
   private int b;
   private BlockPos e;
   private Vec3 g;
   private Vec3 C = Vec3.ZERO;
   private boolean m;
   private boolean A;
   private boolean J;
   private String r = "";
   private boolean Z = (boolean)1;
   private double G;
   private double u;
   private double O;
   private double M;
   private double f;
   private double x;
   private double v;
   private boolean Y;
   private final SplittableRandom j = new SplittableRandom();
   static final double F = 0.16;
   static final double K = 0.18;
   static final int H = 4;
   static final double D = 7.0;
   private int n;
   private int B;
   static final double q = 3.0;
   static final int R = 8;

   public gu(ar var1, hc var2, gu.z var3) {
      this.d = var1;
      this.p = var2;
      this.o = var3;
   }

   public void M(List<zf.h> var1) {
      this.B(var1, true);
   }

   public void B(List<zf.h> var1, boolean var2) {
      this.H();
      this.T = var1 == null ? List.of() : List.copyOf(var1);
      this.Z = var2;
      this.G = 0.0;
   }

   public void B() {
      if (!this.T.isEmpty()) {
         this.G = 0.16;
         this.u = this.j.nextDouble() * Math.PI * 2.0;
         this.O = this.j.nextDouble() * Math.PI * 2.0;
         this.M = 0.35 + this.j.nextDouble() * 0.4;
         this.f = 0.35 + this.j.nextDouble() * 0.4;
         zf.h var1 = this.T.getLast();
         boolean var2 = N(var1.x(), var1.y(), var1.z());
         this.x = var2 ? 0.0 : (this.j.nextDouble() * 2.0 - 1.0) * 0.18;
         this.v = var2 ? 0.0 : (this.j.nextDouble() * 2.0 - 1.0) * 0.18;
      }
   }

   public boolean m() {
      return this.Y;
   }

   private Vec3 r(int var1, double var2) {
      zf.h var4 = this.T.get(var1);
      if (this.G == 0.0) {
         return new Vec3(var4.x() + 0.5, var2, var4.z() + 0.5);
      }

      int var5 = var1 == this.T.size() - 1 && this.Z ? 1 : 0;
      return new Vec3(
         var4.x() + 0.5 + (var5 != 0 ? this.x : Math.sin(var1 * this.M + this.u) * this.G),
         var2,
         var4.z() + 0.5 + (var5 != 0 ? this.v : Math.sin(var1 * this.f + this.O) * this.G)
      );
   }

   public boolean M() {
      return this.i() && !this.Z;
   }

   public void H() {
      if (this.e != null) {
         l0.h(this.d);
      }

      this.T = List.of();
      this.N = this.E = this.a = this.c = 0;
      this.e = null;
      this.g = null;
      this.C = Vec3.ZERO;
      this.m = this.A = this.J = (boolean)0;
      this.r = "";
   }

   public boolean i() {
      return this.N < this.T.size();
   }

   public List<zf.h> e() {
      return this.N < this.T.size() ? this.T.subList(this.N, this.T.size()) : List.of();
   }

   public BlockPos A() {
      return this.e;
   }

   public static boolean N(int var0, int var1, int var2) {
      ClientLevel var3 = Minecraft.getInstance().level;
      if (var3 == null) {
         return true;
      }

      for (Direction var5 : Plane.HORIZONTAL) {
         BlockPos var6 = new BlockPos(var0 + var5.getStepX(), var1, var2 + var5.getStepZ());
         if (j(var3, var6) && j(var3, var6.above()) && j(var3, var6.below()) && j(var3, var6.below(2))) {
            return true;
         }
      }

      return false;
   }

   private static boolean j(Level var0, BlockPos var1) {
      return var0.hasChunkAt(var1) && var0.getBlockState(var1).getCollisionShape(var0, var1).isEmpty();
   }

   public int R() {
      return this.b;
   }

   public String d() {
      return this.r;
   }

   public Vec3 q() {
      return this.C;
   }

   public boolean t() {
      return this.m;
   }

   public boolean o() {
      return this.A;
   }

   public boolean O() {
      return this.J;
   }

   public gu.r S(double var1, ToIntFunction<BlockState> var3, boolean var4) {
      return this.k(var1, var3, var4, 0);
   }

   private gu.r k(double var1, ToIntFunction<BlockState> var3, boolean var4, int var5) {
      this.C = Vec3.ZERO;
      this.m = this.A = this.J = this.Y = (boolean)0;
      Minecraft var6 = Minecraft.getInstance();
      LocalPlayer var7 = var6.player;
      if (this.N >= this.T.size()) {
         this.f();
         return gu.r.c;
      }

      zf.h var8 = this.T.get(this.N);

      for (zf.a var10 : var8.digs()) {
         BlockPos var11 = new BlockPos(var10.x(), var10.y(), var10.z());
         if (l0.B(var11)) {
            if (!var11.equals(this.e)) {
               if (this.e != null) {
                  this.b++;
               }

               this.e = var11;
               this.a = this.c = 0;
            }

            if (++this.a > 400) {
               return this.M("could not break " + var11.toShortString());
            }

            boolean var12 = l0.z(this.d, var11, var1, true, 12.0F, var3, () -> true);
            if (!var12 && ++this.c > 20) {
               return this.M("no face of " + var11.toShortString() + " in reach");
            }

            return gu.r.j;
         }
      }

      if (this.e != null) {
         this.b++;
         this.f();
      }

      Vec3 var35 = var7.position();
      if (!var7.onGround() && !var7.isInWater()) {
         Vec3 var37 = new Vec3(var8.x() + 0.5, var35.y, var8.z() + 0.5);
         if (this.G > 0.0) {
            while (
               this.N + 1 < this.T.size()
                  && this.T.get(this.N + 1).y() == this.T.get(this.N).y()
                  && !this.T.get(this.N + 1).digging()
                  && this.r(this.N + 1, var35.y).distanceToSqr(var35) <= this.r(this.N, var35.y).distanceToSqr(var35)
            ) {
               this.N++;
            }

            var37 = this.r(Math.min(this.N + 2, this.T.size() - 1), var35.y);
         }

         if (var37.subtract(var35).horizontalDistanceSqr() > 0.04) {
            this.C = var37.subtract(var35).multiply(1.0, 0.0, 1.0);
         }

         this.g = var35;
         return gu.r.Y;
      } else {
         int var36;
         for (var36 = this.Q(var35); this.N < var36 && this.a(var35, this.N, var36); this.E = 0) {
            this.N++;
         }

         var8 = this.T.get(this.N);
         int var38 = this.N == this.T.size() - 1 ? 1 : 0;
         int var39 = var38 != 0 && this.Z ? 1 : 0;
         Vec3 var13 = this.r(this.N, var35.y);
         double var14 = Math.sqrt(var13.subtract(var35).horizontalDistanceSqr());
         int var16 = Math.abs(var35.y - var8.y()) < 0.6 ? 1 : 0;
         int var17 = var36 > this.N ? 1 : 0;
         int var18 = var7.getDeltaMovement().horizontalDistanceSqr() < 1.0E-4 ? 1 : 0;
         if (var16 == 0 || !(var14 < (var39 != 0 ? 0.22 : (var17 != 0 ? 0.7 : 0.38))) && (var39 == 0 || var18 == 0 || !(var14 < 0.45))) {
            if (var35.y < var8.y() - 0.4 && var8.move() != zf.b.v && var14 < 0.5) {
               return this.M("fell below the route");
            }

            double var19 = var14;

            for (int var21 = this.N + 1; var21 < this.T.size(); var21++) {
               zf.h var22 = this.T.get(var21 - 1);
               zf.h var23 = this.T.get(var21);
               if (var23.digging()) {
                  break;
               }

               var19 += Math.hypot(var23.x() - var22.x(), var23.z() - var22.z());
            }

            Vec3 var40 = var17 != 0 ? this.r(var36, var35.y) : var13;
            Vec3 var41 = var40.subtract(var35).multiply(1.0, 0.0, 1.0);
            double var42 = var41.lengthSqr() < 1.0E-6 ? 0.0 : var7.getDeltaMovement().multiply(1.0, 0.0, 1.0).dot(var41.normalize());
            int var25 = var8.move() == zf.b.v && var35.y < var8.y() - 0.4 ? 1 : 0;
            this.A = (boolean)(var39 != 0 && var19 < 0.9 && var8.move() != zf.b.S && var25 == 0 && N(var8.x(), var8.y(), var8.z()) ? 1 : 0);
            double var26 = !this.Z ? var19 + 3.0 : (this.A ? var19 : var19 - 0.1);
            byte var28 = this.p.u(var26, var42, ta.E(this.d));
            if (var25 != 0) {
               var28 = 1;
               this.m = (boolean)(var14 < 1.4 ? 1 : 0);
            }

            if (var7.isInWater() && var8.y() >= Math.floor(var35.y)) {
               this.m = (boolean)1;
               if (var8.move() == zf.b.B || var14 > 0.2) {
                  var28 = (byte)(var28 == 0 && !(var14 > 0.2) ? 0 : 1);
               }
            }

            this.C = var28 != 0 ? var41 : Vec3.ZERO;
            byte var29 = 0;

            for (int var30 = this.N; var30 < Math.min(this.T.size(), this.N + 3); var30++) {
               if (this.T.get(var30).digging()) {
                  var29 = 1;
               }
            }

            this.J = (boolean)(var4
                  && var28 != 0
                  && !this.A
                  && var29 == 0
                  && (var19 > 2.5 || !this.Z)
                  && !var7.isInWater()
                  && var7.getFoodData().getFoodLevel() > 6
               ? 1
               : 0);
            this.Y = (boolean)(var28 != 0 && var25 == 0 && var16 != 0 && var36 - this.N >= 4 && var19 > 7.0 && var29 == 0 ? 1 : 0);
            if (var28 != 0 && var5 == 0) {
               this.n++;
               if (var7.isSprinting()) {
                  this.B++;
               }
            }

            double var43 = var7.getEyeHeight() - 0.6;
            ArrayList var32 = new ArrayList(this.T.size() - this.N);

            for (int var33 = this.N; var33 < this.T.size(); var33++) {
               var32.add(new Vec3(this.T.get(var33).x() + 0.5, this.T.get(var33).y() + var43, this.T.get(var33).z() + 0.5));
            }

            this.o.Z(L(new Vec3(var35.x, var8.y() + var43, var35.z), var32, 3.0));
            if (var28 != 0 && this.g != null && var35.distanceToSqr(this.g) < 1.0E-4) {
               this.E++;
            } else if (var28 != 0) {
               this.E = 0;
            }

            this.g = var35;
            return this.E > 40 ? this.M("movement blocked near " + BlockPos.containing(var35).toShortString()) : gu.r.x;
         } else {
            this.N++;
            this.E = 0;
            if (this.N >= this.T.size()) {
               return gu.r.c;
            } else {
               return var5 < 4 ? this.k(var1, var3, var4, var5 + 1) : gu.r.x;
            }
         }
      }
   }

   static Vec3 L(Vec3 var0, List<Vec3> var1, double var2) {
      int var4 = 0;

      while (var4 + 1 < var1.size() && ((Vec3)var1.get(var4)).subtract(var0).dot(((Vec3)var1.get(var4 + 1)).subtract((Vec3)var1.get(var4))) < 0.0) {
         var4++;
      }

      Vec3 var5 = var0;
      Vec3 var6 = null;
      double var7 = var2;

      for (Vec3 var10 : var1.subList(var4, var1.size())) {
         Vec3 var11 = var10.subtract(var5);
         double var12 = var11.length();
         if (!(var12 < 1.0E-6)) {
            if (var12 >= var7) {
               return var5.add(var11.scale(var7 / var12));
            }

            var7 -= var12;
            var6 = var11.scale(1.0 / var12);
            var5 = var10;
         }
      }

      return var6 == null ? var5 : var5.add(var6.scale(var7));
   }

   private int Q(Vec3 var1) {
      ClientLevel var2 = Minecraft.getInstance().level;
      zf.h var3 = this.T.get(this.N);
      if (!var3.digging() && (var3.move() == zf.b.t || var3.move() == zf.b.p) && !(Math.abs(var1.y - var3.y()) > 0.6)) {
         int var4 = this.N;

         for (int var5 = this.N + 1; var5 < Math.min(this.T.size(), this.N + 1 + 8); var4 = var5++) {
            zf.h var6 = this.T.get(var5);
            if (var6.digging() || var6.y() != var3.y() || var6.move() != zf.b.t && var6.move() != zf.b.p) {
               break;
            }

            Vec3 var7 = this.r(var5, 0.0);
            if (!y(var2, var1, var7.x, var7.z, var3.y())) {
               break;
            }
         }

         return var4;
      } else {
         return this.N;
      }
   }

   private boolean a(Vec3 var1, int var2, int var3) {
      zf.h var4 = this.T.get(var2);
      zf.h var5 = this.T.get(var3);
      double var6 = Math.hypot(var5.x() - var4.x(), var5.z() - var4.z());
      double var8 = Math.hypot(var5.x() + 0.5 - var1.x, var5.z() + 0.5 - var1.z);
      return var8 <= var6 + 0.05;
   }

   static boolean y(Level var0, Vec3 var1, double var2, double var4, int var6) {
      double var7 = var2 - var1.x;
      double var9 = var4 - var1.z;
      double var11 = Math.hypot(var7, var9);
      if (var11 < 1.0E-6) {
         return true;
      }

      double var13 = -var9 / var11 * 0.31;
      double var15 = var7 / var11 * 0.31;
      MutableBlockPos var17 = new MutableBlockPos();

      for (double var18 = 0.0; var18 <= var11; var18 += 0.25) {
         double var20 = var1.x + var7 * var18 / var11;
         double var22 = var1.z + var9 * var18 / var11;

         for (int var24 = -1; var24 <= 1; var24++) {
            int var25 = (int)Math.floor(var20 + var13 * var24);
            int var26 = (int)Math.floor(var22 + var15 * var24);

            for (int var27 = 0; var27 <= 1; var27++) {
               BlockState var28 = var0.getBlockState(var17.set(var25, var6 + var27, var26));
               if (!var28.getCollisionShape(var0, var17).isEmpty() || !var28.getFluidState().isEmpty()) {
                  return false;
               }
            }

            BlockState var29 = var0.getBlockState(var17.set(var25, var6 - 1, var26));
            if (var29.getCollisionShape(var0, var17).isEmpty() || var29.is(Blocks.MAGMA_BLOCK)) {
               return false;
            }
         }
      }

      return true;
   }

   public double y() {
      return this.n == 0 ? 0.0 : (double)this.B / this.n;
   }

   private gu.r M(String var1) {
      this.f();
      this.r = var1;
      this.C = Vec3.ZERO;
      return gu.r.J;
   }

   private void f() {
      if (this.e != null) {
         l0.h(this.d);
      }

      this.e = null;
      this.a = this.c = 0;
   }

   public enum r {
      x,
      j,
      Y,
      c,
      J;
   }

   public interface z {
      void Z(Vec3 var1);
   }
}
