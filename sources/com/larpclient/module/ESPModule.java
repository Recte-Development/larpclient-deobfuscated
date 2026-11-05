package com.larpclient.module;

import com.larpclient._3;
import com.larpclient._q;
import com.larpclient.c7;
import com.larpclient.dq;
import com.larpclient.k6;
import com.larpclient.kx;
import com.larpclient.mt;
import com.larpclient.oj;
import com.larpclient.t3;
import com.larpclient.zo;
import com.mojang.blaze3d.systems.RenderSystem;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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
   private final d6 y = this.addSetting(
      new d6("Mode", "Box/corners/fill, schematic skeleton, or entity silhouette", "Corners", "Box", "Corners", "Filled", "Skeleton", "Outline", "3D Box")
   );
   private final dh I = this.addSetting(new dh("Players", "Show players", true));
   private final dh c = this.addSetting(new dh("Mobs", "Show hostile mobs", true));
   private final dh j = this.addSetting(new dh("Animals", "Show animals", false));
   private final d9 X = this.addSetting(new d9("Range", "Max render distance", 128.0, 8.0, 512.0, 8.0));
   private final dq R = new dq(var1 -> this.addSetting(var1));
   private final d9 g = this.addSetting(new d9("Max Entities", "Nearest entities drawn", 128.0, 16.0, 512.0, 16.0));
   private final dh G = this.addSetting(new dh("Health Bars", "High-contrast screen-space health bar", true));
   private final d9 n = this.addSetting(new d9("Health Width", "Health bar width in GUI pixels", 3.0, 2.0, 6.0, 1.0));
   private final dh T = this.addSetting(new dh("Health Text", "Show numeric HP beneath the health bar", true));
   private static ESPModule l;
   private final Set<Integer> N = new HashSet<>();
   private final dh w = this.addSetting(new dh("Notifications", "Notify when new player enters range", false));
   private final Set<Integer> f = new HashSet<>();
   private final Set<Integer> B = new HashSet<>();
   private boolean M = (boolean)1;

   public ESPModule() {
      super("ESP", "See entities through walls", Category.RENDER, -1);
      l = this;
      this.n.H(this.G::P);
      this.T.H(this.G::P);
      this.R.o(() -> true, () -> !this.y.S("Outline"), () -> !this.y.S("Outline") || this.G.P(), () -> this.y.S("Filled"));
      LevelRenderEvents.BEFORE_GIZMOS.register(this::m);
      HudElementRegistry.addLast(Identifier.fromNamespaceAndPath("larp-client", "entity_esp"), this::A);
   }

   @Override
   public void onEnable() {
      this.N.clear();
      this.f.clear();
      this.B.clear();
      this.M = (boolean)1;
   }

   public List<df<?>> C() {
      return List.of(this.I, this.c, this.j);
   }

   public List<df<?>> g() {
      return List.of(this.y, this.X);
   }

   public List<df<?>> E() {
      return this.R.q();
   }

   public List<df<?>> j() {
      ArrayList var1 = new ArrayList<>(this.R.g());
      var1.addAll(this.R.L());
      return List.copyOf(var1);
   }

   public List<df<?>> n() {
      return List.of(this.G, this.n, this.T);
   }

   public List<df<?>> U() {
      return List.of(this.g, this.w);
   }

   @Override
   public void onDisable() {
      this.N.clear();
      this.f.clear();
      this.B.clear();
   }

   @Override
   public void onTick() {
      if (this.isInGame()) {
         this.N.clear();
         int var10000 = this.y.S("Outline");
         boolean var15 = false;
         byte var14 = (byte)var10000;
         var10000 = Integer.signum(var14);
         if (((var10000 | -var10000) >>> 31 ^ 1) == 0) {
            for (Entity var2 : dq.y(mc.level.entitiesForRendering(), mc.player, this.X.P(), this.g.P().intValue(), this::Z)) {
               this.N.add(var2.getId());
            }
         }

         int var19 = this.w.P();
         boolean var13 = false;
         byte var12 = (byte)var19;
         var19 = Integer.signum(var12);
         if ((var19 | -var19) >>> 31 != 0) {
            HashSet var16 = new HashSet();

            for (Entity var3 : mc.level.entitiesForRendering()) {
               if (var3 != mc.player && var3 instanceof Player var4 && var4.isAlive()) {
                  double var5 = mc.player.distanceTo(var3);
                  if (!(var5 > this.X.P())) {
                     var16.add(var3.getId());
                     int var7 = !this.f.contains(var3.getId()) ? 1 : 0;
                     int var8 = !this.B.contains(var3.getId()) ? 1 : 0;
                     if ((var7 != 0 || var8 != 0) && !this.M) {
                        this.f.add(var3.getId());
                        String var9 = var4.getName().getString();
                        int var10 = (int)t3.Z(var4);
                        String var11 = String.format("%.0fm", var5);
                        oj.K().g("Player Detected", var9 + " - " + var10 + "HP - " + var11, oj.u.B, 10000L);
                        _q.F();
                     } else {
                        this.f.add(var3.getId());
                     }
                  }
               }
            }

            this.f.retainAll(var16);
            this.B.clear();
            this.B.addAll(var16);
            this.M = (boolean)0;
         }
      }
   }

   private boolean Z(Entity var1) {
      return var1 instanceof LivingEntity
         && (var1 instanceof Player ? this.I.P() : (var1 instanceof Enemy ? this.c.P() : var1 instanceof Animal && this.j.P()));
   }

   public static int f(LivingEntity var0) {
      if (l == null || !l.isEnabled()) {
         return 0;
      } else if (l != null
         && l.isOperational()
         && l.y.S("Outline")
         && l.isInGame()
         && var0 != mc.player
         && var0.isAlive()
         && l.Z(var0)
         && l.N.contains(var0.getId())) {
         double var1 = mc.player.distanceTo(var0);
         return var1 >= l.X.P() ? 0 : l.Y(var0, var1);
      } else {
         return 0;
      }
   }

   private int Y(LivingEntity var1, double var2) {
      int var4 = var1 instanceof Player ? -4221441 : (var1 instanceof Enemy ? -943189 : -7677757);
      return this.R.w(var4, var2, this.X.P(), dq.E(var1), var1.getId());
   }

   private void m(LevelRenderContext var1) {
      if (this.isEnabled()) {
         if (this.isOperational() && this.isInGame() && this.y.S("3D Box")) {
            float var2 = mc.getDeltaTracker().getGameTimeDeltaPartialTick(false);
            long var3 = kx.G();

            try {
               for (Entity var6 : dq.y(mc.level.entitiesForRendering(), mc.player, this.X.P(), this.g.P().intValue(), this::Z)) {
                  int var7 = this.Y((LivingEntity)var6, mc.player.distanceTo(var6));
                  AABB var8 = dq.k(var6, var2);
                  this.R.O(var8, "Box", var7);
               }
            } catch (RuntimeException var12) {
               mt.B("ESP", "Entity overlay failed", var12);
            } finally {
               kx.u("ESP", var3);
            }
         }
      }
   }

   private void A(GuiGraphicsExtractor var1, DeltaTracker var2) {
      if (this.isOperational() && this.isInGame() && !mc.gameRenderer.gameRenderState().guiRenderState.isHudHidden) {
         var1 = zo.R(var1, true, false);
         if (var1 != null) {
            Camera var3 = mc.gameRenderer.mainCamera();
            Matrix4f var4 = var3.getViewRotationProjectionMatrix(new Matrix4f());
            int var5 = mc.getWindow().getGuiScaledWidth();
            int var6 = mc.getWindow().getGuiScaledHeight();
            float var7 = var2.getGameTimeDeltaPartialTick(false);
            boolean var8 = RenderSystem.getDevice().getDeviceInfo().isZZeroToOne();

            for (Entity var10 : dq.y(mc.level.entitiesForRendering(), mc.player, this.X.P(), this.g.P().intValue(), this::Z)) {
               if (this.R.R() || mc.player.hasLineOfSight(var10)) {
                  com.larpclient.di.c var11 = com.larpclient.di.g(dq.k(var10, var7), var3.position(), var4, var5, var6, var8);
                  if (var11 != null) {
                     int var12 = var11.left();
                     int var13 = var11.top();
                     int var14 = var11.right() - var12;
                     int var15 = var11.bottom() - var13;
                     LivingEntity var16 = (LivingEntity)var10;
                     int var17 = this.Y(var16, mc.player.distanceTo(var10));
                     if (var17 >>> 24 != 0) {
                        if (this.y.S("Box") || this.y.S("Corners") || this.y.S("Filled")) {
                           if (this.y.S("Filled")) {
                              c7.s(var1, var12, var13, var14, var15, _3.U(var17, this.R.u()));
                           }

                           int var18 = Math.max(1, Math.round(this.R.T()));
                           int var19 = this.y.S("Corners") ? Math.max(3, var14 / 4) : var14;
                           int var20 = this.y.S("Corners") ? Math.max(3, var15 / 5) : var15;
                           ArrayList var21 = new ArrayList();
                           if (!this.y.S("Corners")) {
                              var21.add(new int[]{var12, var13, var14, var18});
                              var21.add(new int[]{var12, var13 + var15 - var18, var14, var18});
                              var21.add(new int[]{var12, var13, var18, var15});
                              var21.add(new int[]{var12 + var14 - var18, var13, var18, var15});
                           } else {
                              for (int var22 = 0; var22 < 2; var22++) {
                                 for (int var23 = 0; var23 < 2; var23++) {
                                    var21.add(new int[]{var22 == 0 ? var12 : var12 + var14 - var19, var23 == 0 ? var13 : var13 + var15 - var18, var19, var18});
                                    var21.add(new int[]{var22 == 0 ? var12 : var12 + var14 - var18, var23 == 0 ? var13 : var13 + var15 - var20, var18, var20});
                                 }
                              }
                           }

                           for (int[] var36 : var21) {
                              c7.s(var1, var36[0] - 1, var36[1] - 1, var36[2] + 2, var36[3] + 2, -401600234);
                           }

                           for (int[] var37 : var21) {
                              c7.s(var1, var37[0], var37[1], var37[2], var37[3], var17);
                           }
                        }

                        if (this.y.S("Skeleton")) {
                           try {
                              this.R(var1, var16, var7, var3, var4, var5, var6, var8, var17);
                           } catch (RuntimeException var28) {
                              mt.B("ESP", "Skeleton overlay failed", var28);
                           }
                        }

                        if (this.G.P()) {
                           int var30 = this.n.P().intValue();
                           int var31 = var12 - var30 - 5;
                           if (var31 < 4) {
                              var31 = Math.min(var5 - var30 - 2, var12 + var14 + 5);
                           }

                           double var32 = dq.E(var16);
                           int var35 = (int)Math.round(var15 * var32);
                           c7.s(var1, var31 - 1, var13 - 1, var30 + 2, var15 + 2, -267382506);
                           c7.s(var1, var31, var13, var30, var15, -13420994);
                           int var38 = var32 < 0.5 ? _3.D(-44442, -10660, var32 * 2.0) : _3.D(-10660, -10951269, (var32 - 0.5) * 2.0);
                           c7.s(var1, var31, var13 + var15 - var35, var30, var35, var38);
                           if (var16.getAbsorptionAmount() > 0.0F) {
                              int var24 = Math.round(var15 * Math.min(1.0F, var16.getAbsorptionAmount() / Math.max(1.0F, var16.getMaxHealth())));
                              c7.s(var1, var31 - 3, var13 + var15 - var24, 2, var24, -10660);
                           }

                           if (this.T.P()) {
                              String var39 = (int)Math.ceil(t3.Z(var16)) + " HP";
                              int var25 = k6.x(var39);
                              int var26 = Math.max(1, Math.min(var5 - var25 - 3, var31));
                              int var27 = Math.min(var6 - 12, var13 + var15 + 3);
                              c7.s(var1, var26 - 2, var27 - 1, var25 + 4, 11, -401600234);
                              k6.G(var1, var39, var26, var27, -1);
                           }
                        }
                     }
                  }
               }
            }
         }
      }
   }

   private void R(GuiGraphicsExtractor var1, LivingEntity var2, float var3, Camera var4, Matrix4f var5, int var6, int var7, boolean var8, int var9) {
      Vec3 var10 = var2.getPosition(var3);
      double var11 = var2.getBbHeight();
      double var13 = var2.getBbWidth();
      double var15 = Math.toRadians(Mth.rotLerp(var3, var2.yBodyRotO, var2.yBodyRot));
      Vec3 var17 = new Vec3(-Math.sin(var15), 0.0, Math.cos(var15));
      Vec3 var18 = new Vec3(Math.cos(var15), 0.0, Math.sin(var15));
      double var19 = var2.walkAnimation.position(var3) * 0.6662;
      double var21 = Math.min(1.0F, var2.walkAnimation.speed(var3));
      double var23 = Math.cos(var19) * 1.4 * var21;
      double var25 = Math.cos(var19) * var21;
      Vec3 var27 = var10.add(0.0, var11 * 0.375, 0.0);
      Vec3 var28 = var10.add(0.0, var11 * 0.75, 0.0);
      Vec3 var29 = var10.add(0.0, var11 * 0.6875, 0.0);
      Vec3 var30 = var29.add(var18.scale(var13 * 0.55));
      Vec3 var31 = var29.add(var18.scale(-var13 * 0.55));
      Vec3 var32 = var27.add(var18.scale(var13 * 0.18));
      Vec3 var33 = var27.add(var18.scale(-var13 * 0.18));
      Vec3[][] var34 = new Vec3[][]{
         {var27, var28},
         {var30, var31},
         {var32, var33},
         {var30, H(var30, var17, var11 * 0.3125, var25)},
         {var31, H(var31, var17, var11 * 0.3125, -var25)},
         {var32, H(var32, var17, var11 * 0.375, -var23)},
         {var33, H(var33, var17, var11 * 0.375, var23)}
      };
      Vec3 var35 = var4.position();
      ArrayList var36 = new ArrayList(var34.length);

      for (Vec3[] var40 : var34) {
         com.larpclient.di.w var41 = com.larpclient.di.I(var40[0], var40[1], var35, var5, var6, var7, var8);
         if (var41 != null) {
            var36.add(var41);
         }
      }

      com.larpclient.di.a var46 = com.larpclient.di.k(var10.add(0.0, var11 * 0.875, 0.0), var35, var5, new Vector3f(var4.upVector()), var6, var7, var8);
      int var47 = var46 == null ? 0 : Math.max(1, Math.round(var46.scale() * (float)var11 * 0.125F));
      int var48 = Math.max(1, Math.round(this.R.T()));
      int var49 = _3.U(-401600234, (var9 >>> 24) / 255.0);

      for (int var50 = 0; var50 < 2; var50++) {
         int var42 = var50 == 0 ? var49 : var9;
         float var43 = var50 == 0 ? var48 + 2 : var48;

         for (com.larpclient.di.w var45 : var36) {
            c7.s(var1, var45.x1(), var45.y1(), var45.x2(), var45.y2(), var43, var42);
         }

         if (var46 != null) {
            c7.K(var1, Math.round(var46.x()), Math.round(var46.y()), var47 + (var50 == 0 ? 1 : 0), var42);
         }
      }
   }

   private static Vec3 H(Vec3 var0, Vec3 var1, double var2, double var4) {
      return var0.add(var1.scale(-Math.sin(var4) * var2)).add(0.0, -Math.cos(var4) * var2, 0.0);
   }
}
