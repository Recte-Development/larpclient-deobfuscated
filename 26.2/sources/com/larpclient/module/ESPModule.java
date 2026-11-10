package com.larpclient.module;

import com.larpclient.bj;
import com.larpclient.k7;
import com.larpclient.kv;
import com.larpclient.ms;
import com.larpclient.p2;
import com.larpclient.s0;
import com.larpclient.uh;
import com.larpclient.z4;
import com.larpclient.zj;
import com.larpclient.zy;
import com.mojang.blaze3d.systems.RenderSystem;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.level.LevelRenderContext;
import net.fabricmc.fabric.api.client.rendering.v1.level.LevelRenderEvents;
import net.minecraft.client.Camera;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class ESPModule extends Module {
   private final l Y = this.addSetting(
      new l("Mode", "Box/corners/fill, schematic skeleton, or entity silhouette", "Corners", "Box", "Corners", "Filled", "Skeleton", "Outline", "3D Box")
   );
   private final s A = this.addSetting(new s("Players", "Show players", true));
   private final s N = this.addSetting(new s("Mobs", "Show hostile mobs", true));
   private final s d = this.addSetting(new s("Animals", "Show animals", false));
   private final e E = this.addSetting(new e("Range", "Max render distance", 128.0, 8.0, 512.0, 8.0));
   private final z4 X = new z4(var1 -> this.addSetting(var1));
   private final e Q = this.addSetting(new e("Max Entities", "Nearest entities drawn", 128.0, 16.0, 512.0, 16.0));
   private final s R = this.addSetting(new s("Health Bars", "High-contrast screen-space health bar", true));
   private final e e = this.addSetting(new e("Health Width", "Health bar width in GUI pixels", 3.0, 2.0, 6.0, 1.0));
   private final s c = this.addSetting(new s("Health Text", "Show numeric HP beneath the health bar", true));
   private static ESPModule s;
   private final Set<Integer> V = new HashSet<>();
   private final Map<Integer, Boolean> B = new HashMap<>();
   private static final int P = -670364656;
   private final s i = this.addSetting(new s("Notifications", "Notify when new player enters range", false));
   private final Set<Integer> j = new HashSet<>();
   private final Set<Integer> J = new HashSet<>();
   private boolean C = (boolean)1;

   public ESPModule() {
      super("ESP", "See entities through walls", Category.RENDER, -1);
      s = this;
      this.e.P(this.R::getValue);
      this.c.P(this.R::getValue);
      this.X.s(() -> true, () -> !this.Y.O("Outline"), () -> !this.Y.O("Outline") || this.R.getValue(), () -> this.Y.O("Filled"));
      LevelRenderEvents.BEFORE_GIZMOS.register(this::C);
      HudElementRegistry.addLast(Identifier.fromNamespaceAndPath("larp-client", "entity_esp"), this::E);
   }

   @Override
   public void onEnable() {
      this.V.clear();
      this.B.clear();
      this.j.clear();
      this.J.clear();
      this.C = (boolean)1;
   }

   public List<q<?>> N() {
      return List.of(this.A, this.N, this.d);
   }

   public List<q<?>> L() {
      return List.of(this.Y, this.E);
   }

   public List<q<?>> D() {
      return this.X.f();
   }

   public List<q<?>> J() {
      ArrayList var1 = new ArrayList<>(this.X.O());
      var1.addAll(this.X.p());
      return List.copyOf(var1);
   }

   public List<q<?>> Y() {
      return List.of(this.R, this.e, this.c);
   }

   public List<q<?>> I() {
      return List.of(this.Q, this.i);
   }

   @Override
   public void onDisable() {
      this.V.clear();
      this.B.clear();
      this.j.clear();
      this.J.clear();
   }

   @Override
   public void onTick() {
      this.B.clear();
      if (this.isInGame()) {
         this.V.clear();
         int var10000 = this.Y.O("Outline");
         boolean var15 = false;
         byte var14 = (byte)var10000;
         var10000 = Integer.signum(var14);
         if (((var10000 | -var10000) >>> 31 ^ 1) == 0) {
            for (Entity var2 : z4.G(mc.level.entitiesForRendering(), mc.player, this.E.getValue(), this.Q.getValue().intValue(), this::r)) {
               this.V.add(var2.getId());
            }
         }

         int var19 = this.i.getValue();
         boolean var13 = false;
         byte var12 = (byte)var19;
         var19 = Integer.signum(var12);
         if ((var19 | -var19) >>> 31 == 0) {
            this.j.clear();
            this.J.clear();
            this.C = (boolean)1;
         } else {
            HashSet var16 = new HashSet();

            for (Entity var3 : mc.level.entitiesForRendering()) {
               if (var3 != mc.player && var3 instanceof Player var4 && var4.isAlive()) {
                  double var5 = mc.player.distanceTo(var3);
                  if (!(var5 > this.E.getValue())) {
                     var16.add(var3.getId());
                     int var7 = !this.j.contains(var3.getId()) ? 1 : 0;
                     int var8 = !this.J.contains(var3.getId()) ? 1 : 0;
                     if ((var7 != 0 || var8 != 0) && !this.C) {
                        this.j.add(var3.getId());
                        String var9 = var4.getName().getString();
                        int var10 = (int)zy.f(var4);
                        String var11 = String.format("%.0fm", var5);
                        s0.s().k("Player Detected", var9 + " - " + var10 + "HP - " + var11, s0.u.p, 10000L);
                        ms.o();
                     } else {
                        this.j.add(var3.getId());
                     }
                  }
               }
            }

            this.j.retainAll(var16);
            this.J.clear();
            this.J.addAll(var16);
            this.C = (boolean)0;
         }
      }
   }

   private boolean r(Entity var1) {
      return var1 instanceof LivingEntity
         && (var1 instanceof Player ? this.A.getValue() : (var1 instanceof Enemy ? this.N.getValue() : var1 instanceof Animal && this.d.getValue()));
   }

   public static boolean J() {
      return s != null && s.isEnabled() && !s.V.isEmpty();
   }

   public static int x(LivingEntity var0) {
      if (s == null || !s.isEnabled()) {
         return 0;
      } else if (s != null
         && s.isOperational()
         && s.Y.O("Outline")
         && s.isInGame()
         && var0 != mc.player
         && var0.isAlive()
         && s.r(var0)
         && s.V.contains(var0.getId())) {
         double var1 = mc.player.distanceTo(var0);
         return var1 >= s.E.getValue() ? 0 : s.u(var0, var1);
      } else {
         return 0;
      }
   }

   private int u(LivingEntity var1, double var2) {
      int var4 = var1 instanceof Player ? -4221441 : (var1 instanceof Enemy ? -943189 : -7677757);
      return this.X.c(var4, var2, this.E.getValue(), z4.g(var1), var1.getId());
   }

   private void C(LevelRenderContext var1) {
      if (this.isEnabled()) {
         if (this.isOperational() && this.isInGame() && this.Y.O("3D Box")) {
            float var2 = mc.getDeltaTracker().getGameTimeDeltaPartialTick(false);
            long var3 = zj.m();

            try {
               for (Entity var6 : z4.G(mc.level.entitiesForRendering(), mc.player, this.E.getValue(), this.Q.getValue().intValue(), this::r)) {
                  int var7 = this.u((LivingEntity)var6, mc.player.distanceTo(var6));
                  AABB var8 = z4.O(var6, var2);
                  this.X.R(var8, "Box", var7);
               }
            } catch (RuntimeException var12) {
               com.larpclient.h.w("ESP", "Entity overlay failed", var12);
            } finally {
               zj.l("ESP", var3);
            }
         }
      }
   }

   private void E(GuiGraphicsExtractor var1, DeltaTracker var2) {
      if (this.isOperational() && this.isInGame() && !mc.gameRenderer.gameRenderState().guiRenderState.isHudHidden) {
         var1 = p2.x(var1, true, false);
         if (var1 != null) {
            Camera var3 = mc.gameRenderer.mainCamera();
            Matrix4f var4 = z4.l(new Matrix4f());
            int var5 = mc.getWindow().getGuiScaledWidth();
            int var6 = mc.getWindow().getGuiScaledHeight();
            float var7 = 1.0F / Math.max(1, mc.getWindow().getGuiScale());
            float var8 = var2.getGameTimeDeltaPartialTick(false);
            boolean var9 = RenderSystem.getDevice().getDeviceInfo().isZZeroToOne();
            float var10 = E(this.X.g(), var7);
            float var11 = E(0.5F, var7);
            List var12 = z4.G(mc.level.entitiesForRendering(), mc.player, this.E.getValue(), this.Q.getValue().intValue(), this::r);

            for (int var13 = var12.size() - 1; var13 >= 0; var13--) {
               Entity var14 = (Entity)var12.get(var13);
               if (this.X.t() || this.B.computeIfAbsent(var14.getId(), var1x -> mc.player.hasLineOfSight(var14))) {
                  k7.z var15 = k7.X(z4.O(var14, var8), var3.position(), var4, var5, var6, var9);
                  if (var15 != null) {
                     float var16 = f(var15.left(), var7);
                     float var17 = f(var15.top(), var7);
                     float var18 = f(var15.right(), var7);
                     float var19 = f(var15.bottom(), var7);
                     if (!(var18 - var16 < 2.0F) && !(var19 - var17 < 3.0F)) {
                        LivingEntity var20 = (LivingEntity)var14;
                        int var21 = this.u(var20, mc.player.distanceTo(var14));
                        if (var21 >>> 24 != 0) {
                           double var22 = (var21 >>> 24) / 255.0;
                           int var24 = uh.F(-670364656, var22);
                           if (!this.Y.O("Box") && !this.Y.O("Filled")) {
                              if (this.Y.O("Corners")) {
                                 float var39 = var18 - var16;
                                 float var26 = var19 - var17;
                                 float var27 = Math.min(Math.max(f(var39 * 0.28F, var7), 2.0F * var10), var39 / 2.0F - var11);
                                 float var28 = Math.min(Math.max(f(var26 * 0.2F, var7), 2.0F * var10), var26 / 2.0F - var11);
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
                                       V(var1, var35, var36, var33, var34, var27 + 2.0F * var30, var28 + 2.0F * var30, var10 + 2.0F * var30, var31);
                                    }
                                 }
                              }
                           } else {
                              if (this.Y.O("Filled")) {
                                 int var25 = uh.F(var21, this.X.H());
                                 kv.l(
                                    var1, var16 + var10 + var11, var17 + var10 + var11, var18 - var10 - var11, var19 - var10 - var11, var25, uh.F(var25, 0.45)
                                 );
                              }

                              c(var1, var16 - var11, var17 - var11, var18 + var11, var19 + var11, var10 + 2.0F * var11, var24);
                              c(var1, var16, var17, var18, var19, var10, var21);
                           }

                           if (this.Y.O("Skeleton")) {
                              try {
                                 this.P(var1, var20, var8, var3, var4, var5, var6, var9, var21, var10, var11);
                              } catch (RuntimeException var37) {
                                 com.larpclient.h.w("ESP", "Skeleton overlay failed", var37);
                              }
                           }

                           if (this.R.getValue()) {
                              this.R(var1, var20, var16, var17, var18, var19, var5, var6, var7, var11, var22, var24);
                           }
                        }
                     }
                  }
               }
            }
         }
      }
   }

   private void R(
      GuiGraphicsExtractor var1,
      LivingEntity var2,
      float var3,
      float var4,
      float var5,
      float var6,
      int var7,
      int var8,
      float var9,
      float var10,
      double var11,
      int var13
   ) {
      float var14 = E(this.e.getValue().floatValue(), var9);
      float var15 = f(2.0F, var9);
      int var16 = var3 - var10 - var15 - var14 - var10 >= 1.0F ? 1 : 0;
      float var17 = var16 != 0 ? var3 - var10 - var15 - var14 : Math.min(var7 - var14 - var10, var5 + var10 + var15);
      double var18 = Math.clamp(z4.g(var2), 0.0, 1.0);
      float var20 = f(var6 - (var6 - var4) * (float)var18, var9);
      int var21 = var18 < 0.5 ? uh.q(-44442, -10660, var18 * 2.0) : uh.q(-10660, -10951269, (var18 - 0.5) * 2.0);
      kv.g(var1, var17 - var10, var4 - var10, var17 + var14 + var10, var6 + var10, var13);
      kv.g(var1, var17, var4, var17 + var14, var6, uh.F(-1070977739, var11));
      kv.l(var1, var17, var20, var17 + var14, var6, uh.F(var21, var11), uh.F(uh.q(var21, -16777216, 0.22), var11));
      if (var2.getAbsorptionAmount() > 0.0F) {
         double var22 = Math.min(1.0F, var2.getAbsorptionAmount() / Math.max(1.0F, var2.getMaxHealth()));
         float var24 = Math.max(var9, f(var14 / 3.0F, var9));
         float var25 = f(var6 - (var6 - var4) * (float)var22, var9);
         float var26 = var16 != 0 ? var17 : var17 + var14 - var24;
         kv.g(var1, var26, var25, var26 + var24, var6, uh.F(-10660, var11));
      }

      if (this.c.getValue()) {
         String var30 = (int)Math.ceil(zy.f(var2)) + " HP";
         int var23 = bj.q(var30);
         float var31 = f(Math.clamp((var3 + var5 - var23) / 2.0F, 3.0F, Math.max(3, var7 - var23 - 3)), var9);
         float var32 = f(Math.min(var8 - 11, var6 + var10 + 3.0F), var9);
         kv.g(var1, var31 - 3.0F, var32 - 1.5F, var31 + var23 + 3.0F, var32 + 9.5F, var13);
         var1.pose().pushMatrix();

         try {
            var1.pose().translate(var31, var32);
            bj.h(var1, var30, 0, 0, uh.F(-723464, var11));
         } finally {
            var1.pose().popMatrix();
         }
      }
   }

   private static float f(float var0, float var1) {
      return Math.round(var0 / var1) * var1;
   }

   private static float E(float var0, float var1) {
      return Math.max(var1, f(var0, var1));
   }

   private static void c(GuiGraphicsExtractor var0, float var1, float var2, float var3, float var4, float var5, int var6) {
      kv.g(var0, var1, var2, var3, var2 + var5, var6);
      kv.g(var0, var1, var4 - var5, var3, var4, var6);
      kv.g(var0, var1, var2 + var5, var1 + var5, var4 - var5, var6);
      kv.g(var0, var3 - var5, var2 + var5, var3, var4 - var5, var6);
   }

   private static void V(GuiGraphicsExtractor var0, float var1, float var2, int var3, int var4, float var5, float var6, float var7, int var8) {
      kv.g(
         var0,
         Math.min(var1, var1 + var3 * var5),
         Math.min(var2, var2 + var4 * var7),
         Math.max(var1, var1 + var3 * var5),
         Math.max(var2, var2 + var4 * var7),
         var8
      );
      kv.g(
         var0,
         Math.min(var1, var1 + var3 * var7),
         Math.min(var2 + var4 * var7, var2 + var4 * var6),
         Math.max(var1, var1 + var3 * var7),
         Math.max(var2 + var4 * var7, var2 + var4 * var6),
         var8
      );
   }

   private void P(
      GuiGraphicsExtractor var1,
      LivingEntity var2,
      float var3,
      Camera var4,
      Matrix4f var5,
      int var6,
      int var7,
      boolean var8,
      int var9,
      float var10,
      float var11
   ) {
      Vec3 var12 = z4.r(var2, var3);
      double var13 = var2.getBbHeight();
      double var15 = var2.getBbWidth();
      double var17 = Math.toRadians(Mth.rotLerp(var3, var2.yBodyRotO, var2.yBodyRot));
      Vec3 var19 = new Vec3(-Math.sin(var17), 0.0, Math.cos(var17));
      Vec3 var20 = new Vec3(Math.cos(var17), 0.0, Math.sin(var17));
      double var21 = var2.walkAnimation.position(var3) * 0.6662;
      double var23 = Math.min(1.0F, var2.walkAnimation.speed(var3));
      double var25 = Math.cos(var21) * 1.4 * var23;
      double var27 = Math.cos(var21) * var23;
      Vec3 var29 = var12.add(0.0, var13 * 0.375, 0.0);
      Vec3 var30 = var12.add(0.0, var13 * 0.75, 0.0);
      Vec3 var31 = var12.add(0.0, var13 * 0.6875, 0.0);
      Vec3 var32 = var31.add(var20.scale(var15 * 0.55));
      Vec3 var33 = var31.add(var20.scale(-var15 * 0.55));
      Vec3 var34 = var29.add(var20.scale(var15 * 0.18));
      Vec3 var35 = var29.add(var20.scale(-var15 * 0.18));
      Vec3[][] var36 = new Vec3[][]{
         {var29, var30},
         {var32, var33},
         {var34, var35},
         {var32, n(var32, var19, var13 * 0.3125, var27)},
         {var33, n(var33, var19, var13 * 0.3125, -var27)},
         {var34, n(var34, var19, var13 * 0.375, -var25)},
         {var35, n(var35, var19, var13 * 0.375, var25)}
      };
      Vec3 var37 = var4.position();
      ArrayList var38 = new ArrayList(var36.length);

      for (Vec3[] var42 : var36) {
         k7.u var43 = k7.e(var42[0], var42[1], var37, var5, var6, var7, var8);
         if (var43 != null) {
            var38.add(var43);
         }
      }

      k7.o var47 = k7.I(var12.add(0.0, var13 * 0.875, 0.0), var37, var5, new Vector3f(var4.upVector()), var6, var7, var8);
      int var48 = var47 == null ? 0 : Math.max(1, Math.round(var47.scale() * (float)var13 * 0.125F));
      int var49 = uh.F(-670364656, (var9 >>> 24) / 255.0);

      for (int var50 = 0; var50 < 2; var50++) {
         int var51 = var50 == 0 ? var49 : var9;
         float var44 = var50 == 0 ? var10 + 2.0F * var11 : var10;

         for (k7.u var46 : var38) {
            kv.M(var1, var46.x1(), var46.y1(), var46.x2(), var46.y2(), var44, var51);
         }

         if (var47 != null) {
            kv.Q(var1, Math.round(var47.x()), Math.round(var47.y()), var48 + (var50 == 0 ? 1 : 0), var51);
         }
      }
   }

   private static Vec3 n(Vec3 var0, Vec3 var1, double var2, double var4) {
      return var0.add(var1.scale(-Math.sin(var4) * var2)).add(0.0, -Math.cos(var4) * var2, 0.0);
   }
}
