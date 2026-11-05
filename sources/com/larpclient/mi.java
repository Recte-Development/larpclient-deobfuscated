package com.larpclient;

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

public final class mi {
   private final com.larpclient.module.lp O = new com.larpclient.module.lp(
      "Color", "Theme follows your live accent colors", "Theme", "Theme", "Category", "Custom", "Distance", "Health", "Rainbow"
   );
   private final com.larpclient.module.lv g = new com.larpclient.module.lv("Custom Color", "Color used by Custom mode", -4291585);
   private final com.larpclient.module.lc U = new com.larpclient.module.lc("Line Width", "Outline width in pixels", 1.5, 0.5, 4.0, 0.25);
   private final com.larpclient.module.lc z = new com.larpclient.module.lc("Fill Opacity", "Soft fill for Filled mode", 0.12, 0.0, 0.5, 0.02);
   private final com.larpclient.module.ln A = new com.larpclient.module.ln(
      "Through Walls", "Show boxes and lines behind solid blocks; silhouettes always show through", true
   );

   public mi(Consumer<com.larpclient.module.l6<?>> var1) {
      this(var1, true);
   }

   public mi(Consumer<com.larpclient.module.l6<?>> var1, boolean var2) {
      this.g.x(() -> this.O.i("Custom"));

      for (com.larpclient.module.l6 var4 : List.of(this.O, this.g, this.U, this.A)) {
         var1.accept(var4);
      }

      if (var2) {
         var1.accept(this.z);
      }
   }

   public void J(BooleanSupplier var1, BooleanSupplier var2, BooleanSupplier var3, BooleanSupplier var4) {
      this.O.x(var1);
      this.g.x(() -> var1.getAsBoolean() && this.O.i("Custom"));
      this.U.x(var2);
      this.A.x(var3);
      this.z.x(var4);
   }

   public List<com.larpclient.module.l6<?>> t() {
      return List.of(this.O, this.g);
   }

   public List<com.larpclient.module.l6<?>> J() {
      return List.of(this.U, this.A);
   }

   public List<com.larpclient.module.l6<?>> Q() {
      return List.of(this.z);
   }

   public boolean K() {
      return this.A.getValue();
   }

   public double m() {
      return this.z.getValue();
   }

   public float z() {
      return this.U.getValue().floatValue();
   }

   public int k(int var1, double var2, double var4, double var6, int var8) {
      int var9 = switch ((String)this.O.getValue()) {
         case "Category" -> var1;
         case "Custom" -> this.g.getValue();
         case "Distance" -> rv.d(Math.clamp(var2 / var4, 0.0, 1.0));
         case "Health" -> oh.j(-1017711, -7347266, var6);
         case "Rainbow" -> oh.O((float)(System.currentTimeMillis() % 6000L) / 6000.0F + var8 * 0.037F, 0.45F, 1.0F);
         default -> rv.d(0.25);
      };
      return oh.S(var9, G(var2, var4));
   }

   public static double G(double var0, double var2) {
      if (!(var2 <= 0.0) && !(var0 >= var2)) {
         double var4 = Math.clamp((var0 / var2 - 0.65) / 0.35, 0.0, 1.0);
         return 1.0 - var4 * var4 * (3.0 - 2.0 * var4);
      } else {
         return 0.0;
      }
   }

   public static List<Entity> T(Iterable<Entity> var0, Entity var1, double var2, int var4, Predicate<Entity> var5) {
      double var6 = var2 * var2;
      return StreamSupport.<Entity>stream(var0.spliterator(), false)
         .filter(var4x -> var4x != var1 && var4x.isAlive() && var1.distanceToSqr(var4x) <= var6 && var5.test(var4x))
         .sorted(Comparator.comparingDouble(var1::distanceToSqr))
         .limit(var4)
         .toList();
   }

   public static AABB R(Entity var0, float var1) {
      return var0.getBoundingBox().move(var0.getPosition(var1).subtract(var0.position())).inflate(0.025);
   }

   private void h(GizmoProperties var1) {
      if (this.A.getValue()) {
         var1.setAlwaysOnTop();
      }
   }

   public void D(Vec3 var1, Vec3 var2, int var3) {
      this.h(Gizmos.line(var1, var2, var3, this.z()));
   }

   public void I(AABB var1, String var2, int var3) {
      if (var3 >>> 24 != 0) {
         if (var2.equals("Corners")) {
            double var4 = var1.getXsize() * 0.25;
            double var6 = var1.getYsize() * 0.18;
            double var8 = var1.getZsize() * 0.25;

            for (int var10 = 0; var10 < 2; var10++) {
               for (int var11 = 0; var11 < 2; var11++) {
                  for (int var12 = 0; var12 < 2; var12++) {
                     Vec3 var13 = new Vec3(var10 == 0 ? var1.minX : var1.maxX, var11 == 0 ? var1.minY : var1.maxY, var12 == 0 ? var1.minZ : var1.maxZ);
                     this.D(var13, var13.add(var10 == 0 ? var4 : -var4, 0.0, 0.0), var3);
                     this.D(var13, var13.add(0.0, var11 == 0 ? var6 : -var6, 0.0), var3);
                     this.D(var13, var13.add(0.0, 0.0, var12 == 0 ? var8 : -var8), var3);
                  }
               }
            }
         } else {
            int var14 = var2.equals("Filled") ? oh.S(var3, this.z.getValue()) : 0;
            this.h(Gizmos.cuboid(var1, GizmoStyle.strokeAndFill(var3, this.z(), var14)));
         }
      }
   }

   public void G(AABB var1, double var2, int var4) {
      Vec3 var5 = new Vec3(var1.minX - 0.08, var1.minY, var1.minZ);
      this.h(Gizmos.line(var5, var5.add(0.0, var1.getYsize(), 0.0), oh.S(rv.f, (var4 >>> 24) / 255.0), this.z() + 1.0F));
      int var6 = oh.j(-1017711, -7347266, var2);
      this.D(var5, var5.add(0.0, var1.getYsize() * Math.clamp(var2, 0.0, 1.0), 0.0), oh.i(var6, (var4 >>> 24) / 255.0));
   }

   public void t(Vec3 var1, String var2, int var3) {
      this.h(Gizmos.billboardText(var2, var1, Style.forColorAndCentered(var3).withScale(0.7F)));
   }

   public static double D(LivingEntity var0) {
      return u_.g(var0);
   }
}
