package com.larpclient.module;

import com.larpclient._q;
import com.larpclient.fm;
import com.larpclient.h5;
import com.larpclient.he;
import com.larpclient.ib;
import com.larpclient.ic;
import com.larpclient.iy;
import com.larpclient.j5;
import com.larpclient.qc;
import com.larpclient.vv;
import com.larpclient.wu;
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
   private final o C = this.addSetting(
      new o("Mode", "Box/corners/fill, schematic skeleton, or entity silhouette", "Corners", "Box", "Corners", "Filled", "Skeleton", "Outline", "3D Box")
   );
   private final m c = this.addSetting(new m("Players", "Show players", true));
   private final m X = this.addSetting(new m("Mobs", "Show hostile mobs", true));
   private final m e = this.addSetting(new m("Animals", "Show animals", false));
   private final b b = this.addSetting(new b("Range", "Max render distance", 128.0, 8.0, 512.0, 8.0));
   private final j5 L = new j5(var1 -> this.addSetting(var1));
   private final b h = this.addSetting(new b("Max Entities", "Nearest entities drawn", 128.0, 16.0, 512.0, 16.0));
   private final m n = this.addSetting(new m("Health Bars", "High-contrast screen-space health bar", true));
   private final b s = this.addSetting(new b("Health Width", "Health bar width in GUI pixels", 3.0, 2.0, 6.0, 1.0));
   private final m g = this.addSetting(new m("Health Text", "Show numeric HP beneath the health bar", true));
   private static ESPModule t;
   private final Set<Integer> U = new HashSet<>();
   private final Map<Integer, Boolean> R = new HashMap<>();
   private static final int B = -670364656;
   private final m x = this.addSetting(new m("Notifications", "Notify when new player enters range", false));
   private final Set<Integer> N = new HashSet<>();
   private final Set<Integer> D = new HashSet<>();
   private boolean H = (boolean)1;

   public ESPModule() {
      super("ESP", "See entities through walls", Category.RENDER, -1);
      t = this;
      this.s.a(this.n::getValue);
      this.g.a(this.n::getValue);
      this.L.q(() -> true, () -> !this.C.w("Outline"), () -> !this.C.w("Outline") || this.n.getValue(), () -> this.C.w("Filled"));
      LevelRenderEvents.BEFORE_GIZMOS.register(this::C);
      HudElementRegistry.addLast(Identifier.fromNamespaceAndPath("larp-client", "entity_esp"), this::n);
   }

   @Override
   public void onEnable() {
      this.U.clear();
      this.R.clear();
      this.N.clear();
      this.D.clear();
      this.H = (boolean)1;
   }

   public List<r<?>> v() {
      return List.of(this.c, this.X, this.e);
   }

   public List<r<?>> i() {
      return List.of(this.C, this.b);
   }

   public List<r<?>> u() {
      return this.L.Q();
   }

   public List<r<?>> k() {
      ArrayList var1 = new ArrayList<>(this.L.l());
      var1.addAll(this.L.M());
      return List.copyOf(var1);
   }

   public List<r<?>> p() {
      return List.of(this.n, this.s, this.g);
   }

   public List<r<?>> X() {
      return List.of(this.h, this.x);
   }

   @Override
   public void onDisable() {
      this.U.clear();
      this.R.clear();
      this.N.clear();
      this.D.clear();
   }

   @Override
   public void onTick() {
      this.R.clear();
      if (this.isInGame()) {
         this.U.clear();
         int var10000 = this.C.w("Outline");
         boolean var15 = false;
         byte var14 = (byte)var10000;
         var10000 = Integer.signum(var14);
         if (((var10000 | -var10000) >>> 31 ^ 1) == 0) {
            for (Entity var2 : j5.n(mc.level.entitiesForRendering(), mc.player, this.b.getValue(), this.h.getValue().intValue(), this::G)) {
               this.U.add(var2.getId());
            }
         }

         int var19 = this.x.getValue();
         boolean var13 = false;
         byte var12 = (byte)var19;
         var19 = Integer.signum(var12);
         if ((var19 | -var19) >>> 31 == 0) {
            this.N.clear();
            this.D.clear();
            this.H = (boolean)1;
         } else {
            HashSet var16 = new HashSet();

            for (Entity var3 : mc.level.entitiesForRendering()) {
               if (var3 != mc.player && var3 instanceof Player var4 && var4.isAlive()) {
                  double var5 = mc.player.distanceTo(var3);
                  if (!(var5 > this.b.getValue())) {
                     var16.add(var3.getId());
                     int var7 = !this.N.contains(var3.getId()) ? 1 : 0;
                     int var8 = !this.D.contains(var3.getId()) ? 1 : 0;
                     if ((var7 != 0 || var8 != 0) && !this.H) {
                        this.N.add(var3.getId());
                        String var9 = var4.getName().getString();
                        int var10 = (int)wu.W(var4);
                        String var11 = String.format("%.0fm", var5);
                        ib.L().N("Player Detected", var9 + " - " + var10 + "HP - " + var11, ib.v.o, 10000L);
                        fm.A();
                     } else {
                        this.N.add(var3.getId());
                     }
                  }
               }
            }

            this.N.retainAll(var16);
            this.D.clear();
            this.D.addAll(var16);
            this.H = (boolean)0;
         }
      }
   }

   private boolean G(Entity var1) {
      return var1 instanceof LivingEntity
         && (var1 instanceof Player ? this.c.getValue() : (var1 instanceof Enemy ? this.X.getValue() : var1 instanceof Animal && this.e.getValue()));
   }

   public static boolean H() {
      return t != null && t.isEnabled() && !t.U.isEmpty();
   }

   public static int H(LivingEntity var0) {
      if (t == null || !t.isEnabled()) {
         return 0;
      } else if (t != null
         && t.isOperational()
         && t.C.w("Outline")
         && t.isInGame()
         && var0 != mc.player
         && var0.isAlive()
         && t.G(var0)
         && t.U.contains(var0.getId())) {
         double var1 = mc.player.distanceTo(var0);
         return var1 >= t.b.getValue() ? 0 : t.m(var0, var1);
      } else {
         return 0;
      }
   }

   private int m(LivingEntity var1, double var2) {
      int var4 = var1 instanceof Player ? -4221441 : (var1 instanceof Enemy ? -943189 : -7677757);
      return this.L.p(var4, var2, this.b.getValue(), j5.w(var1), var1.getId());
   }

   private void C(LevelRenderContext var1) {
      if (this.isEnabled()) {
         if (this.isOperational() && this.isInGame() && this.C.w("3D Box")) {
            float var2 = mc.getDeltaTracker().getGameTimeDeltaPartialTick(false);
            long var3 = he.s();

            try {
               for (Entity var6 : j5.n(mc.level.entitiesForRendering(), mc.player, this.b.getValue(), this.h.getValue().intValue(), this::G)) {
                  int var7 = this.m((LivingEntity)var6, mc.player.distanceTo(var6));
                  AABB var8 = j5.X(var6, var2);
                  this.L.L(var8, "Box", var7);
               }
            } catch (RuntimeException var12) {
               _q.g("ESP", "Entity overlay failed", var12);
            } finally {
               he.T("ESP", var3);
            }
         }
      }
   }

   private void n(GuiGraphicsExtractor var1, DeltaTracker var2) {
      if (this.isOperational() && this.isInGame() && !mc.gameRenderer.gameRenderState().guiRenderState.isHudHidden) {
         var1 = qc.h(var1, true, false);
         if (var1 != null) {
            Camera var3 = mc.gameRenderer.mainCamera();
            Matrix4f var4 = j5.I(new Matrix4f());
            int var5 = mc.getWindow().getGuiScaledWidth();
            int var6 = mc.getWindow().getGuiScaledHeight();
            float var7 = 1.0F / Math.max(1, mc.getWindow().getGuiScale());
            float var8 = var2.getGameTimeDeltaPartialTick(false);
            boolean var9 = RenderSystem.getDevice().getDeviceInfo().isZZeroToOne();
            float var10 = E(this.L.q(), var7);
            float var11 = E(0.5F, var7);
            List var12 = j5.n(mc.level.entitiesForRendering(), mc.player, this.b.getValue(), this.h.getValue().intValue(), this::G);

            for (int var13 = var12.size() - 1; var13 >= 0; var13--) {
               Entity var14 = (Entity)var12.get(var13);
               if (this.L.m() || this.R.computeIfAbsent(var14.getId(), var1x -> mc.player.hasLineOfSight(var14))) {
                  iy.n var15 = iy.U(j5.X(var14, var8), var3.position(), var4, var5, var6, var9);
                  if (var15 != null) {
                     float var16 = O(var15.left(), var7);
                     float var17 = O(var15.top(), var7);
                     float var18 = O(var15.right(), var7);
                     float var19 = O(var15.bottom(), var7);
                     if (!(var18 - var16 < 2.0F) && !(var19 - var17 < 3.0F)) {
                        LivingEntity var20 = (LivingEntity)var14;
                        int var21 = this.m(var20, mc.player.distanceTo(var14));
                        if (var21 >>> 24 != 0) {
                           double var22 = (var21 >>> 24) / 255.0;
                           int var24 = vv.O(-670364656, var22);
                           if (!this.C.w("Box") && !this.C.w("Filled")) {
                              if (this.C.w("Corners")) {
                                 float var39 = var18 - var16;
                                 float var26 = var19 - var17;
                                 float var27 = Math.min(Math.max(O(var39 * 0.28F, var7), 2.0F * var10), var39 / 2.0F - var11);
                                 float var28 = Math.min(Math.max(O(var26 * 0.2F, var7), 2.0F * var10), var26 / 2.0F - var11);
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
                                       t(var1, var35, var36, var33, var34, var27 + 2.0F * var30, var28 + 2.0F * var30, var10 + 2.0F * var30, var31);
                                    }
                                 }
                              }
                           } else {
                              if (this.C.w("Filled")) {
                                 int var25 = vv.O(var21, this.L.r());
                                 h5.O(
                                    var1, var16 + var10 + var11, var17 + var10 + var11, var18 - var10 - var11, var19 - var10 - var11, var25, vv.O(var25, 0.45)
                                 );
                              }

                              z(var1, var16 - var11, var17 - var11, var18 + var11, var19 + var11, var10 + 2.0F * var11, var24);
                              z(var1, var16, var17, var18, var19, var10, var21);
                           }

                           if (this.C.w("Skeleton")) {
                              try {
                                 this.H(var1, var20, var8, var3, var4, var5, var6, var9, var21, var10, var11);
                              } catch (RuntimeException var37) {
                                 _q.g("ESP", "Skeleton overlay failed", var37);
                              }
                           }

                           if (this.n.getValue()) {
                              this.i(var1, var20, var16, var17, var18, var19, var5, var6, var7, var11, var22, var24);
                           }
                        }
                     }
                  }
               }
            }
         }
      }
   }

   private void i(
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
      float var14 = E(this.s.getValue().floatValue(), var9);
      float var15 = O(2.0F, var9);
      int var16 = var3 - var10 - var15 - var14 - var10 >= 1.0F ? 1 : 0;
      float var17 = var16 != 0 ? var3 - var10 - var15 - var14 : Math.min(var7 - var14 - var10, var5 + var10 + var15);
      double var18 = Math.clamp(j5.w(var2), 0.0, 1.0);
      float var20 = O(var6 - (var6 - var4) * (float)var18, var9);
      int var21 = var18 < 0.5 ? vv.Q(-44442, -10660, var18 * 2.0) : vv.Q(-10660, -10951269, (var18 - 0.5) * 2.0);
      h5.c(var1, var17 - var10, var4 - var10, var17 + var14 + var10, var6 + var10, var13);
      h5.c(var1, var17, var4, var17 + var14, var6, vv.O(-1070977739, var11));
      h5.O(var1, var17, var20, var17 + var14, var6, vv.O(var21, var11), vv.O(vv.Q(var21, -16777216, 0.22), var11));
      if (var2.getAbsorptionAmount() > 0.0F) {
         double var22 = Math.min(1.0F, var2.getAbsorptionAmount() / Math.max(1.0F, var2.getMaxHealth()));
         float var24 = Math.max(var9, O(var14 / 3.0F, var9));
         float var25 = O(var6 - (var6 - var4) * (float)var22, var9);
         float var26 = var16 != 0 ? var17 : var17 + var14 - var24;
         h5.c(var1, var26, var25, var26 + var24, var6, vv.O(-10660, var11));
      }

      if (this.g.getValue()) {
         String var30 = (int)Math.ceil(wu.W(var2)) + " HP";
         int var23 = ic.E(var30);
         float var31 = O(Math.clamp((var3 + var5 - var23) / 2.0F, 3.0F, Math.max(3, var7 - var23 - 3)), var9);
         float var32 = O(Math.min(var8 - 11, var6 + var10 + 3.0F), var9);
         h5.c(var1, var31 - 3.0F, var32 - 1.5F, var31 + var23 + 3.0F, var32 + 9.5F, var13);
         var1.pose().pushMatrix();

         try {
            var1.pose().translate(var31, var32);
            ic.H(var1, var30, 0, 0, vv.O(-723464, var11));
         } finally {
            var1.pose().popMatrix();
         }
      }
   }

   private static float O(float var0, float var1) {
      return Math.round(var0 / var1) * var1;
   }

   private static float E(float var0, float var1) {
      return Math.max(var1, O(var0, var1));
   }

   private static void z(GuiGraphicsExtractor var0, float var1, float var2, float var3, float var4, float var5, int var6) {
      h5.c(var0, var1, var2, var3, var2 + var5, var6);
      h5.c(var0, var1, var4 - var5, var3, var4, var6);
      h5.c(var0, var1, var2 + var5, var1 + var5, var4 - var5, var6);
      h5.c(var0, var3 - var5, var2 + var5, var3, var4 - var5, var6);
   }

   private static void t(GuiGraphicsExtractor var0, float var1, float var2, int var3, int var4, float var5, float var6, float var7, int var8) {
      h5.c(
         var0,
         Math.min(var1, var1 + var3 * var5),
         Math.min(var2, var2 + var4 * var7),
         Math.max(var1, var1 + var3 * var5),
         Math.max(var2, var2 + var4 * var7),
         var8
      );
      h5.c(
         var0,
         Math.min(var1, var1 + var3 * var7),
         Math.min(var2 + var4 * var7, var2 + var4 * var6),
         Math.max(var1, var1 + var3 * var7),
         Math.max(var2 + var4 * var7, var2 + var4 * var6),
         var8
      );
   }

   private void H(
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
      Vec3 var12 = j5.J(var2, var3);
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
         {var32, t(var32, var19, var13 * 0.3125, var27)},
         {var33, t(var33, var19, var13 * 0.3125, -var27)},
         {var34, t(var34, var19, var13 * 0.375, -var25)},
         {var35, t(var35, var19, var13 * 0.375, var25)}
      };
      Vec3 var37 = var4.position();
      ArrayList var38 = new ArrayList(var36.length);

      for (Vec3[] var42 : var36) {
         iy.q var43 = iy.x(var42[0], var42[1], var37, var5, var6, var7, var8);
         if (var43 != null) {
            var38.add(var43);
         }
      }

      iy.a var47 = iy.A(var12.add(0.0, var13 * 0.875, 0.0), var37, var5, new Vector3f(var4.upVector()), var6, var7, var8);
      int var48 = var47 == null ? 0 : Math.max(1, Math.round(var47.scale() * (float)var13 * 0.125F));
      int var49 = vv.O(-670364656, (var9 >>> 24) / 255.0);

      for (int var50 = 0; var50 < 2; var50++) {
         int var51 = var50 == 0 ? var49 : var9;
         float var44 = var50 == 0 ? var10 + 2.0F * var11 : var10;

         for (iy.q var46 : var38) {
            h5.U(var1, var46.x1(), var46.y1(), var46.x2(), var46.y2(), var44, var51);
         }

         if (var47 != null) {
            h5.M(var1, Math.round(var47.x()), Math.round(var47.y()), var48 + (var50 == 0 ? 1 : 0), var51);
         }
      }
   }

   private static Vec3 t(Vec3 var0, Vec3 var1, double var2, double var4) {
      return var0.add(var1.scale(-Math.sin(var4) * var2)).add(0.0, -Math.cos(var4) * var2, 0.0);
   }
}
