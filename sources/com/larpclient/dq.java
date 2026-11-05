package com.larpclient;

import com.larpclient.module.df;
import com.larpclient.module.dh;
import com.larpclient.module.dk;
import java.util.Comparator;
import java.util.List;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.StreamSupport;
import net.minecraft.gizmos.GizmoProperties;
import net.minecraft.gizmos.GizmoStyle;
import net.minecraft.gizmos.Gizmos;
import net.minecraft.gizmos.TextGizmo.Style;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public final class dq {
   private final com.larpclient.module.d6 t = new com.larpclient.module.d6(
      "Color", "Theme follows your live accent colors", "Theme", "Theme", "Category", "Custom", "Distance", "Health", "Rainbow"
   );
   private final dk S = new dk("Custom Color", "Color used by Custom mode", -4291585);
   private final com.larpclient.module.d9 f = new com.larpclient.module.d9("Line Width", "Outline width in pixels", 1.5, 0.5, 4.0, 0.25);
   private final com.larpclient.module.d9 i = new com.larpclient.module.d9("Fill Opacity", "Soft fill for Filled mode", 0.12, 0.0, 0.5, 0.02);
   private final dh J = new dh("Through Walls", "Show boxes and lines behind solid blocks; silhouettes always show through", true);

   public dq(Consumer<df<?>> var1) {
      this(var1, true);
   }

   public dq(Consumer<df<?>> var1, boolean var2) {
      this.S.H(() -> this.t.S("Custom"));

      for (df var4 : List.of(this.t, this.S, this.f, this.J)) {
         var1.accept(var4);
      }

      if (var2) {
         var1.accept(this.i);
      }
   }

   public void o(BooleanSupplier var1, BooleanSupplier var2, BooleanSupplier var3, BooleanSupplier var4) {
      this.t.H(var1);
      this.S.H(() -> var1.getAsBoolean() && this.t.S("Custom"));
      this.f.H(var2);
      this.J.H(var3);
      this.i.H(var4);
   }

   public List<df<?>> q() {
      return List.of(this.t, this.S);
   }

   public List<df<?>> g() {
      return List.of(this.f, this.J);
   }

   public List<df<?>> L() {
      return List.of(this.i);
   }

   public boolean R() {
      return this.J.P();
   }

   public double u() {
      return this.i.P();
   }

   public float T() {
      return this.f.P().floatValue();
   }

   public int w(int var1, double var2, double var4, double var6, int var8) {
      int var9 = switch ((String)this.t.P()) {
         case "Category" -> var1;
         case "Custom" -> this.S.P();
         case "Distance" -> com.larpclient.f.d(Math.clamp(var2 / var4, 0.0, 1.0));
         case "Health" -> _3.D(-1017711, -7347266, var6);
         case "Rainbow" -> _3.Z((float)(System.currentTimeMillis() % 6000L) / 6000.0F + var8 * 0.037F, 0.45F, 1.0F);
         default -> com.larpclient.f.d(0.25);
      };
      return _3.U(var9, C(var2, var4));
   }

   public static double C(double var0, double var2) {
      if (!(var2 <= 0.0) && !(var0 >= var2)) {
         double var4 = Math.clamp((var0 / var2 - 0.65) / 0.35, 0.0, 1.0);
         return 1.0 - var4 * var4 * (3.0 - 2.0 * var4);
      } else {
         return 0.0;
      }
   }

   public static List<Entity> y(Iterable<Entity> var0, Entity var1, double var2, int var4, Predicate<Entity> var5) {
      double var6 = var2 * var2;
      return StreamSupport.<Entity>stream(var0.spliterator(), false)
         .filter(var4x -> var4x != var1 && var4x.isAlive() && var1.distanceToSqr(var4x) <= var6 && var5.test(var4x))
         .sorted(Comparator.comparingDouble(var1::distanceToSqr))
         .limit(var4)
         .toList();
   }

   public static AABB k(Entity var0, float var1) {
      return var0.getBoundingBox().move(var0.getPosition(var1).subtract(var0.position())).inflate(0.025);
   }

   private void y(GizmoProperties var1) {
      if (this.J.P()) {
         var1.setAlwaysOnTop();
      }
   }

   public void n(Vec3 var1, Vec3 var2, int var3) {
      this.y(Gizmos.line(var1, var2, var3, this.T()));
   }

   public void O(AABB var1, String var2, int var3) {
      if (var3 >>> 24 != 0) {
         if (var2.equals("Corners")) {
            double var4 = var1.getXsize() * 0.25;
            double var6 = var1.getYsize() * 0.18;
            double var8 = var1.getZsize() * 0.25;

            for (int var10 = 0; var10 < 2; var10++) {
               for (int var11 = 0; var11 < 2; var11++) {
                  for (int var12 = 0; var12 < 2; var12++) {
                     Vec3 var13 = new Vec3(var10 == 0 ? var1.minX : var1.maxX, var11 == 0 ? var1.minY : var1.maxY, var12 == 0 ? var1.minZ : var1.maxZ);
                     this.n(var13, var13.add(var10 == 0 ? var4 : -var4, 0.0, 0.0), var3);
                     this.n(var13, var13.add(0.0, var11 == 0 ? var6 : -var6, 0.0), var3);
                     this.n(var13, var13.add(0.0, 0.0, var12 == 0 ? var8 : -var8), var3);
                  }
               }
            }
         } else {
            int var14 = var2.equals("Filled") ? _3.U(var3, this.i.P()) : 0;
            this.y(Gizmos.cuboid(var1, GizmoStyle.strokeAndFill(var3, this.T(), var14)));
         }
      }
   }

   public void O(AABB var1, double var2, int var4) {
      Vec3 var5 = new Vec3(var1.minX - 0.08, var1.minY, var1.minZ);
      this.y(Gizmos.line(var5, var5.add(0.0, var1.getYsize(), 0.0), _3.U(com.larpclient.f.I, (var4 >>> 24) / 255.0), this.T() + 1.0F));
      int var6 = _3.D(-1017711, -7347266, var2);
      this.n(var5, var5.add(0.0, var1.getYsize() * Math.clamp(var2, 0.0, 1.0), 0.0), _3.A(var6, (var4 >>> 24) / 255.0));
   }

   public void d(Vec3 var1, String var2, int var3) {
      this.y(Gizmos.billboardText(var2, var1, Style.forColorAndCentered(var3).withScale(0.7F)));
   }

   public static double E(LivingEntity var0) {
      return t3.f(var0);
   }
}
