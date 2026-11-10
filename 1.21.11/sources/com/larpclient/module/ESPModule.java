package com.larpclient.module;

import com.larpclient.c5;
import com.larpclient.eg;
import com.larpclient.ei;
import com.larpclient.gu;
import com.larpclient.j2;
import com.larpclient.l2;
import com.larpclient.q8;
import com.larpclient.tu;
import com.larpclient.uw;
import com.larpclient.wi;
import com.larpclient.wt;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.world.WorldRenderContext;
import net.fabricmc.fabric.api.client.rendering.v1.world.WorldRenderEvents;
import net.minecraft.class_1297;
import net.minecraft.class_1309;
import net.minecraft.class_1429;
import net.minecraft.class_1569;
import net.minecraft.class_1657;
import net.minecraft.class_238;
import net.minecraft.class_243;
import net.minecraft.class_2960;
import net.minecraft.class_332;
import net.minecraft.class_3532;
import net.minecraft.class_4184;
import net.minecraft.class_9779;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class ESPModule extends Module {
   private final li d = this.addSetting(
      new li("Mode", "Box/corners/fill, schematic skeleton, or entity silhouette", "Corners", "Box", "Corners", "Filled", "Skeleton", "Outline", "3D Box")
   );
   private final l3 k = this.addSetting(new l3("Players", "Show players", true));
   private final l3 x = this.addSetting(new l3("Mobs", "Show hostile mobs", true));
   private final l3 i = this.addSetting(new l3("Animals", "Show animals", false));
   private final lo V = this.addSetting(new lo("Range", "Max render distance", 128.0, 8.0, 512.0, 8.0));
   private final l2 P = new l2(var1 -> this.addSetting(var1));
   private final lo a = this.addSetting(new lo("Max Entities", "Nearest entities drawn", 128.0, 16.0, 512.0, 16.0));
   private final l3 A = this.addSetting(new l3("Health Bars", "High-contrast screen-space health bar", true));
   private final lo j = this.addSetting(new lo("Health Width", "Health bar width in GUI pixels", 3.0, 2.0, 6.0, 1.0));
   private final l3 Z = this.addSetting(new l3("Health Text", "Show numeric HP beneath the health bar", true));
   private static ESPModule y;
   private final Set<Integer> g = new HashSet<>();
   private final Map<Integer, Boolean> e = new HashMap<>();
   private static final int v = -670364656;
   private final l3 c = this.addSetting(new l3("Notifications", "Notify when new player enters range", false));
   private final Set<Integer> R = new HashSet<>();
   private final Set<Integer> T = new HashSet<>();
   private boolean K = (boolean)1;

   public ESPModule() {
      super("ESP", "See entities through walls", Category.RENDER, -1);
      y = this;
      this.j.O(this.A::getValue);
      this.Z.O(this.A::getValue);
      this.P.N(() -> true, () -> !this.d.G("Outline"), () -> !this.d.G("Outline") || this.A.getValue(), () -> this.d.G("Filled"));
      WorldRenderEvents.BEFORE_DEBUG_RENDER.register(this::z);
      HudElementRegistry.addLast(class_2960.method_60655("larp-client", "entity_esp"), this::l);
   }

   @Override
   public void onEnable() {
      this.g.clear();
      this.e.clear();
      this.R.clear();
      this.T.clear();
      this.K = (boolean)1;
   }

   public List<lb<?>> w() {
      return List.of(this.k, this.x, this.i);
   }

   public List<lb<?>> O() {
      return List.of(this.d, this.V);
   }

   public List<lb<?>> o() {
      return this.P.v();
   }

   public List<lb<?>> D() {
      ArrayList var1 = new ArrayList<>(this.P.P());
      var1.addAll(this.P.t());
      return List.copyOf(var1);
   }

   public List<lb<?>> c() {
      return List.of(this.A, this.j, this.Z);
   }

   public List<lb<?>> K() {
      return List.of(this.a, this.c);
   }

   @Override
   public void onDisable() {
      this.g.clear();
      this.e.clear();
      this.R.clear();
      this.T.clear();
   }

   @Override
   public void onTick() {
      this.e.clear();
      if (this.isInGame()) {
         this.g.clear();
         int var10000 = this.d.G("Outline");
         boolean var13 = false;
         byte var12 = (byte)var10000;
         var10000 = Integer.signum(var12);
         if (((var10000 | -var10000) >>> 31 ^ 1) == 0) {
            for (class_1297 var2 : l2.Q(mc.field_1687.method_18112(), mc.field_1724, this.V.getValue(), this.a.getValue().intValue(), this::m)) {
               this.g.add(var2.method_5628());
            }
         }

         int var19 = this.c.getValue();
         boolean var15 = false;
         byte var14 = (byte)var19;
         var19 = Integer.signum(var14);
         if ((var19 | -var19) >>> 31 == 0) {
            this.R.clear();
            this.T.clear();
            this.K = (boolean)1;
         } else {
            HashSet var16 = new HashSet();

            for (class_1297 var3 : mc.field_1687.method_18112()) {
               if (var3 != mc.field_1724 && var3 instanceof class_1657 var4 && var4.method_5805()) {
                  double var5 = mc.field_1724.method_5739(var3);
                  if (!(var5 > this.V.getValue())) {
                     var16.add(var3.method_5628());
                     int var7 = !this.R.contains(var3.method_5628()) ? 1 : 0;
                     int var8 = !this.T.contains(var3.method_5628()) ? 1 : 0;
                     if ((var7 != 0 || var8 != 0) && !this.K) {
                        this.R.add(var3.method_5628());
                        String var9 = var4.method_5477().getString();
                        int var10 = (int)j2.T(var4);
                        String var11 = String.format("%.0fm", var5);
                        wt.T().k("Player Detected", var9 + " - " + var10 + "HP - " + var11, wt.s.a, 10000L);
                        tu.E();
                     } else {
                        this.R.add(var3.method_5628());
                     }
                  }
               }
            }

            this.R.retainAll(var16);
            this.T.clear();
            this.T.addAll(var16);
            this.K = (boolean)0;
         }
      }
   }

   private boolean m(class_1297 var1) {
      return var1 instanceof class_1309
         && (
            var1 instanceof class_1657 ? this.k.getValue() : (var1 instanceof class_1569 ? this.x.getValue() : var1 instanceof class_1429 && this.i.getValue())
         );
   }

   public static boolean s() {
      return y != null && y.isEnabled() && !y.g.isEmpty();
   }

   public static int j(class_1309 var0) {
      if (y == null || !y.isEnabled()) {
         return 0;
      } else if (y != null
         && y.isOperational()
         && y.d.G("Outline")
         && y.isInGame()
         && var0 != mc.field_1724
         && var0.method_5805()
         && y.m(var0)
         && y.g.contains(var0.method_5628())) {
         double var1 = mc.field_1724.method_5739(var0);
         return var1 >= y.V.getValue() ? 0 : y.h(var0, var1);
      } else {
         return 0;
      }
   }

   private int h(class_1309 var1, double var2) {
      int var4 = var1 instanceof class_1657 ? -4221441 : (var1 instanceof class_1569 ? -943189 : -7677757);
      return this.P.C(var4, var2, this.V.getValue(), l2.l(var1), var1.method_5628());
   }

   private void z(WorldRenderContext var1) {
      if (this.isEnabled()) {
         if (this.isOperational() && this.isInGame() && this.d.G("3D Box")) {
            float var2 = mc.method_61966().method_60637(false);
            long var3 = c5.X();

            try {
               for (class_1297 var6 : l2.Q(mc.field_1687.method_18112(), mc.field_1724, this.V.getValue(), this.a.getValue().intValue(), this::m)) {
                  int var7 = this.h((class_1309)var6, mc.field_1724.method_5739(var6));
                  class_238 var8 = l2.F(var6, var2);
                  this.P.X(var8, "Box", var7);
               }
            } catch (RuntimeException var12) {
               q8.O("ESP", "Entity overlay failed", var12);
            } finally {
               c5.w("ESP", var3);
            }
         }
      }
   }

   private void l(class_332 var1, class_9779 var2) {
      if (this.isOperational() && this.isInGame() && !mc.field_1690.field_1842) {
         var1 = wi.p(var1, true, false);
         if (var1 != null) {
            class_4184 var3 = mc.field_1773.method_19418();
            Matrix4f var4 = l2.F(new Matrix4f());
            int var5 = mc.method_22683().method_4486();
            int var6 = mc.method_22683().method_4502();
            float var7 = 1.0F / Math.max(1, mc.method_22683().method_4495());
            float var8 = var2.method_60637(false);
            byte var9 = 0;
            float var10 = Y(this.P.F(), var7);
            float var11 = Y(0.5F, var7);
            List var12 = l2.Q(mc.field_1687.method_18112(), mc.field_1724, this.V.getValue(), this.a.getValue().intValue(), this::m);

            for (int var13 = var12.size() - 1; var13 >= 0; var13--) {
               class_1297 var14 = (class_1297)var12.get(var13);
               if (this.P.L() || this.e.computeIfAbsent(var14.method_5628(), var1x -> mc.field_1724.method_6057(var14))) {
                  eg.f var15 = eg.A(l2.F(var14, var8), var3.method_71156(), var4, var5, var6, (boolean)var9);
                  if (var15 != null) {
                     float var16 = p(var15.left(), var7);
                     float var17 = p(var15.top(), var7);
                     float var18 = p(var15.right(), var7);
                     float var19 = p(var15.bottom(), var7);
                     if (!(var18 - var16 < 2.0F) && !(var19 - var17 < 3.0F)) {
                        class_1309 var20 = (class_1309)var14;
                        int var21 = this.h(var20, mc.field_1724.method_5739(var14));
                        if (var21 >>> 24 != 0) {
                           double var22 = (var21 >>> 24) / 255.0;
                           int var24 = ei.R(-670364656, var22);
                           if (!this.d.G("Box") && !this.d.G("Filled")) {
                              if (this.d.G("Corners")) {
                                 float var39 = var18 - var16;
                                 float var26 = var19 - var17;
                                 float var27 = Math.min(Math.max(p(var39 * 0.28F, var7), 2.0F * var10), var39 / 2.0F - var11);
                                 float var28 = Math.min(Math.max(p(var26 * 0.2F, var7), 2.0F * var10), var26 / 2.0F - var11);
                                 var27 = Math.max(var27, var10);
                                 var28 = Math.max(var28, var10);

                                 for (int var29 = 0; var29 < 2; var29++) {
                                    float var30 = var29 == 0 ? var11 : 0.0F;
                                    int var31 = var29 == 0 ? var24 : var21;

                                    for (int var32 = 0; var32 < 4; var32++) {
                                       int var33 = (var32 & 1) == 0 ? 1 : -1;
                                       int var34 = var32 < 2 ? 1 : -1;
                                       float var35 = (var33 > 0 ? var16 : var18) - var33 * var30;
                                       float var36 = (var34 > 0 ? var17 : var19) - var34 * var30;
                                       L(var1, var35, var36, var33, var34, var27 + 2.0F * var30, var28 + 2.0F * var30, var10 + 2.0F * var30, var31);
                                    }
                                 }
                              }
                           } else {
                              if (this.d.G("Filled")) {
                                 int var25 = ei.R(var21, this.P.t());
                                 gu.i(
                                    var1, var16 + var10 + var11, var17 + var10 + var11, var18 - var10 - var11, var19 - var10 - var11, var25, ei.R(var25, 0.45)
                                 );
                              }

                              Y(var1, var16 - var11, var17 - var11, var18 + var11, var19 + var11, var10 + 2.0F * var11, var24);
                              Y(var1, var16, var17, var18, var19, var10, var21);
                           }

                           if (this.d.G("Skeleton")) {
                              try {
                                 this.G(var1, var20, var8, var3, var4, var5, var6, (boolean)var9, var21, var10, var11);
                              } catch (RuntimeException var37) {
                                 q8.O("ESP", "Skeleton overlay failed", var37);
                              }
                           }

                           if (this.A.getValue()) {
                              this.T(var1, var20, var16, var17, var18, var19, var5, var6, var7, var11, var22, var24);
                           }
                        }
                     }
                  }
               }
            }
         }
      }
   }

   private void T(
      class_332 var1, class_1309 var2, float var3, float var4, float var5, float var6, int var7, int var8, float var9, float var10, double var11, int var13
   ) {
      float var14 = Y(this.j.getValue().floatValue(), var9);
      float var15 = p(2.0F, var9);
      int var16 = var3 - var10 - var15 - var14 - var10 >= 1.0F ? 1 : 0;
      float var17 = var16 != 0 ? var3 - var10 - var15 - var14 : Math.min(var7 - var14 - var10, var5 + var10 + var15);
      double var18 = Math.clamp(l2.l(var2), 0.0, 1.0);
      float var20 = p(var6 - (var6 - var4) * (float)var18, var9);
      int var21 = var18 < 0.5 ? ei.W(-44442, -10660, var18 * 2.0) : ei.W(-10660, -10951269, (var18 - 0.5) * 2.0);
      gu.Z(var1, var17 - var10, var4 - var10, var17 + var14 + var10, var6 + var10, var13);
      gu.Z(var1, var17, var4, var17 + var14, var6, ei.R(-1070977739, var11));
      gu.i(var1, var17, var20, var17 + var14, var6, ei.R(var21, var11), ei.R(ei.W(var21, -16777216, 0.22), var11));
      if (var2.method_6067() > 0.0F) {
         double var22 = Math.min(1.0F, var2.method_6067() / Math.max(1.0F, var2.method_6063()));
         float var24 = Math.max(var9, p(var14 / 3.0F, var9));
         float var25 = p(var6 - (var6 - var4) * (float)var22, var9);
         float var26 = var16 != 0 ? var17 : var17 + var14 - var24;
         gu.Z(var1, var26, var25, var26 + var24, var6, ei.R(-10660, var11));
      }

      if (this.Z.getValue()) {
         String var30 = (int)Math.ceil(j2.T(var2)) + " HP";
         int var23 = uw.i(var30);
         float var31 = p(Math.clamp((var3 + var5 - var23) / 2.0F, 3.0F, Math.max(3, var7 - var23 - 3)), var9);
         float var32 = p(Math.min(var8 - 11, var6 + var10 + 3.0F), var9);
         gu.Z(var1, var31 - 3.0F, var32 - 1.5F, var31 + var23 + 3.0F, var32 + 9.5F, var13);
         var1.method_51448().pushMatrix();

         try {
            var1.method_51448().translate(var31, var32);
            uw.z(var1, var30, 0, 0, ei.R(-723464, var11));
         } finally {
            var1.method_51448().popMatrix();
         }
      }
   }

   private static float p(float var0, float var1) {
      return Math.round(var0 / var1) * var1;
   }

   private static float Y(float var0, float var1) {
      return Math.max(var1, p(var0, var1));
   }

   private static void Y(class_332 var0, float var1, float var2, float var3, float var4, float var5, int var6) {
      gu.Z(var0, var1, var2, var3, var2 + var5, var6);
      gu.Z(var0, var1, var4 - var5, var3, var4, var6);
      gu.Z(var0, var1, var2 + var5, var1 + var5, var4 - var5, var6);
      gu.Z(var0, var3 - var5, var2 + var5, var3, var4 - var5, var6);
   }

   private static void L(class_332 var0, float var1, float var2, int var3, int var4, float var5, float var6, float var7, int var8) {
      gu.Z(
         var0,
         Math.min(var1, var1 + var3 * var5),
         Math.min(var2, var2 + var4 * var7),
         Math.max(var1, var1 + var3 * var5),
         Math.max(var2, var2 + var4 * var7),
         var8
      );
      gu.Z(
         var0,
         Math.min(var1, var1 + var3 * var7),
         Math.min(var2 + var4 * var7, var2 + var4 * var6),
         Math.max(var1, var1 + var3 * var7),
         Math.max(var2 + var4 * var7, var2 + var4 * var6),
         var8
      );
   }

   private void G(
      class_332 var1, class_1309 var2, float var3, class_4184 var4, Matrix4f var5, int var6, int var7, boolean var8, int var9, float var10, float var11
   ) {
      class_243 var12 = l2.i(var2, var3);
      double var13 = var2.method_17682();
      double var15 = var2.method_17681();
      double var17 = Math.toRadians(class_3532.method_17821(var3, var2.field_6220, var2.field_6283));
      class_243 var19 = new class_243(-Math.sin(var17), 0.0, Math.cos(var17));
      class_243 var20 = new class_243(Math.cos(var17), 0.0, Math.sin(var17));
      double var21 = var2.field_42108.method_48572(var3) * 0.6662;
      double var23 = Math.min(1.0F, var2.field_42108.method_48570(var3));
      double var25 = Math.cos(var21) * 1.4 * var23;
      double var27 = Math.cos(var21) * var23;
      class_243 var29 = var12.method_1031(0.0, var13 * 0.375, 0.0);
      class_243 var30 = var12.method_1031(0.0, var13 * 0.75, 0.0);
      class_243 var31 = var12.method_1031(0.0, var13 * 0.6875, 0.0);
      class_243 var32 = var31.method_1019(var20.method_1021(var15 * 0.55));
      class_243 var33 = var31.method_1019(var20.method_1021(-var15 * 0.55));
      class_243 var34 = var29.method_1019(var20.method_1021(var15 * 0.18));
      class_243 var35 = var29.method_1019(var20.method_1021(-var15 * 0.18));
      class_243[][] var36 = new class_243[][]{
         {var29, var30},
         {var32, var33},
         {var34, var35},
         {var32, N(var32, var19, var13 * 0.3125, var27)},
         {var33, N(var33, var19, var13 * 0.3125, -var27)},
         {var34, N(var34, var19, var13 * 0.375, -var25)},
         {var35, N(var35, var19, var13 * 0.375, var25)}
      };
      class_243 var37 = var4.method_71156();
      ArrayList var38 = new ArrayList(var36.length);

      for (class_243[] var42 : var36) {
         eg.m var43 = eg.b(var42[0], var42[1], var37, var5, var6, var7, var8);
         if (var43 != null) {
            var38.add(var43);
         }
      }

      eg.o var47 = eg.y(var12.method_1031(0.0, var13 * 0.875, 0.0), var37, var5, new Vector3f(var4.method_19336()), var6, var7, var8);
      int var48 = var47 == null ? 0 : Math.max(1, Math.round(var47.scale() * (float)var13 * 0.125F));
      int var49 = ei.R(-670364656, (var9 >>> 24) / 255.0);

      for (int var50 = 0; var50 < 2; var50++) {
         int var51 = var50 == 0 ? var49 : var9;
         float var44 = var50 == 0 ? var10 + 2.0F * var11 : var10;

         for (eg.m var46 : var38) {
            gu.o(var1, var46.x1(), var46.y1(), var46.x2(), var46.y2(), var44, var51);
         }

         if (var47 != null) {
            gu.Q(var1, Math.round(var47.x()), Math.round(var47.y()), var48 + (var50 == 0 ? 1 : 0), var51);
         }
      }
   }

   private static class_243 N(class_243 var0, class_243 var1, double var2, double var4) {
      return var0.method_1019(var1.method_1021(-Math.sin(var4) * var2)).method_1031(0.0, -Math.cos(var4) * var2, 0.0);
   }
}
