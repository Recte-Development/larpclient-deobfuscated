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

public final class ad {
   static final int V = 40;
   static final int v = 400;
   static final int z = 20;
   private final _2 o;
   private final hw n;
   private final ad.z I;
   private List<hn.g> t = List.of();
   private int E;
   private int L;
   private int c;
   private int i;
   private int m;
   private BlockPos j;
   private Vec3 y;
   private Vec3 C = Vec3.ZERO;
   private boolean l;
   private boolean x;
   private boolean J;
   private String p = "";
   private boolean a = (boolean)1;
   private double D;
   private double f;
   private double F;
   private double M;
   private double G;
   private double g;
   private double b;
   private boolean Z;
   private final SplittableRandom Q = new SplittableRandom();
   static final double R = 0.16;
   static final double r = 0.18;
   static final int S = 4;
   static final double H = 7.0;
   private int w;
   private int e;
   static final double u = 3.0;
   static final int N = 8;

   public ad(_2 var1, hw var2, ad.z var3) {
      this.o = var1;
      this.n = var2;
      this.I = var3;
   }

   public void s(List<hn.g> var1) {
      this.c(var1, true);
   }

   public void c(List<hn.g> var1, boolean var2) {
      this.V();
      this.t = var1 == null ? List.of() : List.copyOf(var1);
      this.a = var2;
      this.D = 0.0;
   }

   public void B() {
      if (!this.t.isEmpty()) {
         this.D = 0.16;
         this.f = this.Q.nextDouble() * Math.PI * 2.0;
         this.F = this.Q.nextDouble() * Math.PI * 2.0;
         this.M = 0.35 + this.Q.nextDouble() * 0.4;
         this.G = 0.35 + this.Q.nextDouble() * 0.4;
         hn.g var1 = this.t.getLast();
         boolean var2 = m(var1.x(), var1.y(), var1.z());
         this.g = var2 ? 0.0 : (this.Q.nextDouble() * 2.0 - 1.0) * 0.18;
         this.b = var2 ? 0.0 : (this.Q.nextDouble() * 2.0 - 1.0) * 0.18;
      }
   }

   public boolean e() {
      return this.Z;
   }

   private Vec3 M(int var1, double var2) {
      hn.g var4 = this.t.get(var1);
      if (this.D == 0.0) {
         return new Vec3(var4.x() + 0.5, var2, var4.z() + 0.5);
      }

      int var5 = var1 == this.t.size() - 1 && this.a ? 1 : 0;
      return new Vec3(
         var4.x() + 0.5 + (var5 != 0 ? this.g : Math.sin(var1 * this.M + this.f) * this.D),
         var2,
         var4.z() + 0.5 + (var5 != 0 ? this.b : Math.sin(var1 * this.G + this.F) * this.D)
      );
   }

   public boolean B() {
      return this.X() && !this.a;
   }

   public void V() {
      if (this.j != null) {
         vs.B(this.o);
      }

      this.t = List.of();
      this.E = this.L = this.c = this.i = 0;
      this.j = null;
      this.y = null;
      this.C = Vec3.ZERO;
      this.l = this.x = this.J = (boolean)0;
      this.p = "";
   }

   public boolean X() {
      return this.E < this.t.size();
   }

   public List<hn.g> I() {
      return this.E < this.t.size() ? this.t.subList(this.E, this.t.size()) : List.of();
   }

   public BlockPos I() {
      return this.j;
   }

   public static boolean m(int var0, int var1, int var2) {
      ClientLevel var3 = Minecraft.getInstance().level;
      if (var3 == null) {
         return true;
      }

      for (Direction var5 : Plane.HORIZONTAL) {
         BlockPos var6 = new BlockPos(var0 + var5.getStepX(), var1, var2 + var5.getStepZ());
         if (h(var3, var6) && h(var3, var6.above()) && h(var3, var6.below()) && h(var3, var6.below(2))) {
            return true;
         }
      }

      return false;
   }

   private static boolean h(Level var0, BlockPos var1) {
      return var0.hasChunkAt(var1) && var0.getBlockState(var1).getCollisionShape(var0, var1).isEmpty();
   }

   public int U() {
      return this.m;
   }

   public String I() {
      return this.p;
   }

   public Vec3 L() {
      return this.C;
   }

   public boolean p() {
      return this.l;
   }

   public boolean v() {
      return this.x;
   }

   public boolean d() {
      return this.J;
   }

   public ad.s f(double var1, ToIntFunction<BlockState> var3, boolean var4) {
      return this.n(var1, var3, var4, 0);
   }

   private ad.s n(double var1, ToIntFunction<BlockState> var3, boolean var4, int var5) {
      this.C = Vec3.ZERO;
      this.l = this.x = this.J = this.Z = (boolean)0;
      Minecraft var6 = Minecraft.getInstance();
      LocalPlayer var7 = var6.player;
      if (this.E >= this.t.size()) {
         this.G();
         return ad.s.Z;
      }

      hn.g var8 = this.t.get(this.E);

      for (hn.n var10 : var8.digs()) {
         BlockPos var11 = new BlockPos(var10.x(), var10.y(), var10.z());
         if (vs.W(var11)) {
            if (!var11.equals(this.j)) {
               if (this.j != null) {
                  this.m++;
               }

               this.j = var11;
               this.c = this.i = 0;
            }

            if (++this.c > 400) {
               return this.n("could not break " + var11.toShortString());
            }

            boolean var12 = vs.i(this.o, var11, var1, true, 12.0F, var3, () -> true);
            if (!var12 && ++this.i > 20) {
               return this.n("no face of " + var11.toShortString() + " in reach");
            }

            return ad.s.k;
         }
      }

      if (this.j != null) {
         this.m++;
         this.G();
      }

      Vec3 var35 = var7.position();
      if (!var7.onGround() && !var7.isInWater()) {
         Vec3 var37 = new Vec3(var8.x() + 0.5, var35.y, var8.z() + 0.5);
         if (this.D > 0.0) {
            while (
               this.E + 1 < this.t.size()
                  && this.t.get(this.E + 1).y() == this.t.get(this.E).y()
                  && !this.t.get(this.E + 1).digging()
                  && this.M(this.E + 1, var35.y).distanceToSqr(var35) <= this.M(this.E, var35.y).distanceToSqr(var35)
            ) {
               this.E++;
            }

            var37 = this.M(Math.min(this.E + 2, this.t.size() - 1), var35.y);
         }

         if (var37.subtract(var35).horizontalDistanceSqr() > 0.04) {
            this.C = var37.subtract(var35).multiply(1.0, 0.0, 1.0);
         }

         this.y = var35;
         return ad.s.t;
      } else {
         int var36;
         for (var36 = this.A(var35); this.E < var36 && this.X(var35, this.E, var36); this.L = 0) {
            this.E++;
         }

         var8 = this.t.get(this.E);
         int var38 = this.E == this.t.size() - 1 ? 1 : 0;
         int var39 = var38 != 0 && this.a ? 1 : 0;
         Vec3 var13 = this.M(this.E, var35.y);
         double var14 = Math.sqrt(var13.subtract(var35).horizontalDistanceSqr());
         int var16 = Math.abs(var35.y - var8.y()) < 0.6 ? 1 : 0;
         int var17 = var36 > this.E ? 1 : 0;
         int var18 = var7.getDeltaMovement().horizontalDistanceSqr() < 1.0E-4 ? 1 : 0;
         if (var16 == 0 || !(var14 < (var39 != 0 ? 0.22 : (var17 != 0 ? 0.7 : 0.38))) && (var39 == 0 || var18 == 0 || !(var14 < 0.45))) {
            if (var35.y < var8.y() - 0.4 && var8.move() != hn.y.a && var14 < 0.5) {
               return this.n("fell below the route");
            }

            double var19 = var14;

            for (int var21 = this.E + 1; var21 < this.t.size(); var21++) {
               hn.g var22 = this.t.get(var21 - 1);
               hn.g var23 = this.t.get(var21);
               if (var23.digging()) {
                  break;
               }

               var19 += Math.hypot(var23.x() - var22.x(), var23.z() - var22.z());
            }

            Vec3 var40 = var17 != 0 ? this.M(var36, var35.y) : var13;
            Vec3 var41 = var40.subtract(var35).multiply(1.0, 0.0, 1.0);
            double var42 = var41.lengthSqr() < 1.0E-6 ? 0.0 : var7.getDeltaMovement().multiply(1.0, 0.0, 1.0).dot(var41.normalize());
            int var25 = var8.move() == hn.y.a && var35.y < var8.y() - 0.4 ? 1 : 0;
            this.x = (boolean)(var39 != 0 && var19 < 0.9 && var8.move() != hn.y.h && var25 == 0 && m(var8.x(), var8.y(), var8.z()) ? 1 : 0);
            double var26 = !this.a ? var19 + 3.0 : (this.x ? var19 : var19 - 0.1);
            byte var28 = this.n.g(var26, var42, _p.Q(this.o));
            if (var25 != 0) {
               var28 = 1;
               this.l = (boolean)(var14 < 1.4 ? 1 : 0);
            }

            if (var7.isInWater() && var8.y() >= Math.floor(var35.y)) {
               this.l = (boolean)1;
               if (var8.move() == hn.y.s || var14 > 0.2) {
                  var28 = (byte)(var28 == 0 && !(var14 > 0.2) ? 0 : 1);
               }
            }

            this.C = var28 != 0 ? var41 : Vec3.ZERO;
            byte var29 = 0;

            for (int var30 = this.E; var30 < Math.min(this.t.size(), this.E + 3); var30++) {
               if (this.t.get(var30).digging()) {
                  var29 = 1;
               }
            }

            this.J = (boolean)(var4
                  && var28 != 0
                  && !this.x
                  && var29 == 0
                  && (var19 > 2.5 || !this.a)
                  && !var7.isInWater()
                  && var7.getFoodData().getFoodLevel() > 6
               ? 1
               : 0);
            this.Z = (boolean)(var28 != 0 && var25 == 0 && var16 != 0 && var36 - this.E >= 4 && var19 > 7.0 && var29 == 0 ? 1 : 0);
            if (var28 != 0 && var5 == 0) {
               this.w++;
               if (var7.isSprinting()) {
                  this.e++;
               }
            }

            double var43 = var7.getEyeHeight() - 0.6;
            ArrayList var32 = new ArrayList(this.t.size() - this.E);

            for (int var33 = this.E; var33 < this.t.size(); var33++) {
               var32.add(new Vec3(this.t.get(var33).x() + 0.5, this.t.get(var33).y() + var43, this.t.get(var33).z() + 0.5));
            }

            this.I.p(b(new Vec3(var35.x, var8.y() + var43, var35.z), var32, 3.0));
            if (var28 != 0 && this.y != null && var35.distanceToSqr(this.y) < 1.0E-4) {
               this.L++;
            } else if (var28 != 0) {
               this.L = 0;
            }

            this.y = var35;
            return this.L > 40 ? this.n("movement blocked near " + BlockPos.containing(var35).toShortString()) : ad.s.D;
         } else {
            this.E++;
            this.L = 0;
            if (this.E >= this.t.size()) {
               return ad.s.Z;
            } else {
               return var5 < 4 ? this.n(var1, var3, var4, var5 + 1) : ad.s.D;
            }
         }
      }
   }

   static Vec3 b(Vec3 var0, List<Vec3> var1, double var2) {
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

   private int A(Vec3 var1) {
      ClientLevel var2 = Minecraft.getInstance().level;
      hn.g var3 = this.t.get(this.E);
      if (!var3.digging() && (var3.move() == hn.y.R || var3.move() == hn.y.L) && !(Math.abs(var1.y - var3.y()) > 0.6)) {
         int var4 = this.E;

         for (int var5 = this.E + 1; var5 < Math.min(this.t.size(), this.E + 1 + 8); var4 = var5++) {
            hn.g var6 = this.t.get(var5);
            if (var6.digging() || var6.y() != var3.y() || var6.move() != hn.y.R && var6.move() != hn.y.L) {
               break;
            }

            Vec3 var7 = this.M(var5, 0.0);
            if (!G(var2, var1, var7.x, var7.z, var3.y())) {
               break;
            }
         }

         return var4;
      } else {
         return this.E;
      }
   }

   private boolean X(Vec3 var1, int var2, int var3) {
      hn.g var4 = this.t.get(var2);
      hn.g var5 = this.t.get(var3);
      double var6 = Math.hypot(var5.x() - var4.x(), var5.z() - var4.z());
      double var8 = Math.hypot(var5.x() + 0.5 - var1.x, var5.z() + 0.5 - var1.z);
      return var8 <= var6 + 0.05;
   }

   static boolean G(Level var0, Vec3 var1, double var2, double var4, int var6) {
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

   public double V() {
      return this.w == 0 ? 0.0 : (double)this.e / this.w;
   }

   private ad.s n(String var1) {
      this.G();
      this.p = var1;
      this.C = Vec3.ZERO;
      return ad.s.d;
   }

   private void G() {
      if (this.j != null) {
         vs.B(this.o);
      }

      this.j = null;
      this.c = this.i = 0;
   }

   public enum s {
      D,
      k,
      t,
      Z,
      d;
   }

   public interface z {
      void p(Vec3 var1);
   }
}
