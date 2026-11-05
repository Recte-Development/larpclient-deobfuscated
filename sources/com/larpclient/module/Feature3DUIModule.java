package com.larpclient.module;

import com.larp.obf.qbac0b262827a15ac.q4acb8c4b35b6663e;
import com.larpclient.cp;
import com.larpclient.ef;
import com.larpclient.mt;
import com.larpclient.tq;
import java.util.Locale;
import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.ClipContext.Block;
import net.minecraft.world.level.ClipContext.Fluid;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.HitResult.Type;

public final class Feature3DUIModule extends Module {
   private static Feature3DUIModule X;
   private final d6 Z = this.addSetting(new d6("Screens", "Which menus open in 3D", "Inventory", "Inventory", "All Containers"));
   private final d6 i = this.addSetting(new d6("Side", "Screen side for the menu; the camera moves over the opposite shoulder", "Right", "Right", "Left"));
   private final d9 G = this.addSetting(new d9("Menu Angle", "Degrees the menu turns toward the center of the view", 14.0, 0.0, 45.0, 1.0));
   private final d9 W = this.addSetting(new d9("Menu Screen Height", "On-screen menu height, tabs included, as a percent of the screen", 76.0, 30.0, 90.0, 1.0));
   private final d9 s = this.addSetting(new d9("Camera Distance", "Camera distance behind the player in blocks", 1.1, 0.6, 4.0, 0.1));
   private final d9 t = this.addSetting(new d9("Camera Offset", "Sideways camera offset in blocks", 0.5, 0.0, 1.5, 0.05));
   private final d9 U = this.addSetting(new d9("Camera Height", "Camera height relative to the torso in blocks", 0.1, -0.6, 0.6, 0.05));
   private final d9 k = this.addSetting(new d9("Camera Pitch", "Camera pitch in degrees; positive looks down", 3.0, -15.0, 40.0, 1.0));
   private final d9 S = this.addSetting(new d9("Transition", "Camera and menu transition in milliseconds", 320.0, 0.0, 800.0, 10.0));
   private final dh F = this.addSetting(new dh("Dim Background", "Keep the vanilla darkened backdrop behind the menu", false));
   private final dh B = this.addSetting(new dh("Show HUD", "Keep the hotbar and HUD visible while a 3D menu is open", true));
   private final d6 E = this.addSetting(
      new d6(
         "Player Facing",
         "Where your player turns while a 3D menu is open: its own way, around to look at you, or by Player Turn. Only you see it",
         "Forward",
         "Forward",
         "Camera",
         "Custom"
      )
   );
   private final d9 K = this.addSetting(new d9("Player Turn", "Degrees your player turns from its own facing; 180 faces the camera", 180.0, -180.0, 180.0, 5.0));
   private final ef q = new ef(0.0);
   private CameraType f;
   private boolean A;
   private static final double P = 3.5;

   public Feature3DUIModule() {
      super("3D UI", "Third person shoulder view with your inventory in 3D beside you", Category.RENDER, -1);
      X = this;
      this.K.H(() -> this.E.S("Custom"));
   }

   public static Feature3DUIModule c() {
      return X;
   }

   private boolean M() {
      Minecraft var1 = getMc();
      LocalPlayer var2 = var1.player;
      return this.isRuntimeActive()
         && var2 != null
         && var1.level != null
         && var2.isAlive()
         && !var2.isSleeping()
         && !var2.isSpectator()
         && !tq.f()
         && !FreelookModule.D();
   }

   public boolean a(Screen var1) {
      if (!this.isEnabled()) {
         return false;
      } else {
         return var1 instanceof AbstractContainerScreen && this.M()
            ? this.Z.S("All Containers") || var1 instanceof InventoryScreen || var1 instanceof CreativeModeInventoryScreen
            : false;
      }
   }

   private double m() {
      boolean var1 = this.a(getMc().gui.screen());
      double var2 = this.q.y(var1 ? 1.0 : 0.0, this.S.P().intValue(), ef.q.m);
      return Math.clamp(var2, 0.0, 1.0);
   }

   public static void Y() {
      Feature3DUIModule var0 = X;
      if (var0 != null) {
         var0.F();
      }
   }

   private void F() {
      if (!this.M()) {
         this.v("view unavailable");
         this.q.Q(0.0);
      } else {
         double var1 = this.m();
         if (var1 <= 0.001 && !this.a(getMc().gui.screen())) {
            this.v("closed");
         } else {
            Options var3 = getMc().options;
            if (this.f == null) {
               this.f = var3.getCameraType();
               mt.U("Camera", "3D UI view engaged from {}", this.f);
            } else if (var3.getCameraType() != CameraType.THIRD_PERSON_BACK) {
               mt.U("Camera", "3D UI view yielded to perspective {}", var3.getCameraType());
               this.f = null;
               this.q.Q(0.0);
               return;
            }

            if (var3.getCameraType() != CameraType.THIRD_PERSON_BACK) {
               var3.setCameraType(CameraType.THIRD_PERSON_BACK);
            }
         }
      }
   }

   private void v(String var1) {
      if (this.f != null) {
         Options var2 = getMc().options;
         if (var2.getCameraType() == CameraType.THIRD_PERSON_BACK) {
            var2.setCameraType(this.f);
         }

         mt.U("Camera", "3D UI view released ({}), perspective {}", var1, var2.getCameraType());
         this.f = null;
      }
   }

   public Feature3DUIModule.v a(Vec3 var1, float var2, float var3, Entity var4, float var5, float var6) {
      if (this.isEnabled()) {
         if (this.f != null && var4 != null) {
            double var7 = this.m();
            if (var7 <= 0.001) {
               return null;
            }

            Vec3 var9 = var4.getPosition(var5);
            Vec3 var10 = this.f.isFirstPerson() ? var9.add(0.0, var6, 0.0) : var1;
            float var11 = var4 instanceof LivingEntity var12 ? var12.getScale() : 1.0F;
            float var28 = var4.getViewYRot(var5);
            float var13 = this.k.P().floatValue();
            double var14 = Math.toRadians(var28);
            double var16 = Math.toRadians(var13);
            Vec3 var18 = new Vec3(-Math.sin(var14) * Math.cos(var16), -Math.sin(var16), Math.cos(var14) * Math.cos(var16));
            Vec3 var19 = new Vec3(-Math.cos(var14), 0.0, -Math.sin(var14));
            Vec3 var20 = var9.add(0.0, var4.getBbHeight() * 0.6 + this.U.P() * var11, 0.0);
            double var21 = (this.i.S("Right") ? 1 : -1) * this.t.P() * var11;
            Vec3 var23 = var20.subtract(var18.scale(this.s.P() * var11)).add(var19.scale(var21));
            BlockHitResult var24 = var4.level().clip(new ClipContext(var20, var23, Block.VISUAL, Fluid.NONE, var4));
            if (var24.getType() != Type.MISS) {
               Vec3 var25 = var24.getLocation().subtract(var20);
               double var26 = var25.length();
               var23 = var26 < 1.0E-4 ? var20 : var20.add(var25.scale(Math.max(0.0, var26 - 0.2) / var26));
            }

            float var29 = (float)var7;
            Vec3 var30 = var10.lerp(var23, var29);
            return new Feature3DUIModule.v(var30.x, var30.y, var30.z, var2 + Mth.wrapDegrees(var28 - var2) * var29, Mth.lerp(var29, var3, var13));
         } else {
            return null;
         }
      } else {
         return null;
      }
   }

   private Feature3DUIModule.n Q(Entity var1, float var2) {
      if (this.f != null && !this.E.S("Forward") && var1 != null && var1 == getMc().player) {
         float var3 = (float)this.m();
         if (var3 <= 0.001) {
            return null;
         }

         if (this.E.S("Custom")) {
            return new Feature3DUIModule.n(var1.getViewYRot(var2) + this.K.P().floatValue(), Float.NaN, var3);
         }

         Vec3 var4 = getMc().gameRenderer.mainCamera().position();
         Vec3 var5 = var1.getEyePosition(var2);
         double var6 = var4.x - var5.x;
         double var8 = var4.y - var5.y;
         double var10 = var4.z - var5.z;
         return new Feature3DUIModule.n(
            (float)Math.toDegrees(Math.atan2(-var6, var10)), (float)(-Math.toDegrees(Math.atan2(var8, Math.hypot(var6, var10)))), var3
         );
      } else {
         return null;
      }
   }

   public static void w(Entity var0, LivingEntityRenderState var1, float var2) {
      if (X != null && X.isEnabled()) {
         Feature3DUIModule var3 = X;
         Feature3DUIModule.n var4 = var3 == null ? null : var3.Q(var0, var2);
         if (var4 != null) {
            float var5 = var1.bodyRot;
            float var6 = var5 + var1.yRot;
            float var7 = var4.weight();
            float var8 = var5 + Mth.wrapDegrees(var4.yaw() - var5) * var7;
            float var9 = var6 + Mth.wrapDegrees(var4.yaw() - var6) * var7;
            var1.bodyRot = var8;
            var1.yRot = Mth.wrapDegrees(var9 - var8);
            if (!Float.isNaN(var4.pitch())) {
               var1.xRot = Mth.lerp(var7, var1.xRot, var4.pitch());
            }
         }
      }
   }

   public static float j(Entity var0, float var1, float var2) {
      if (X != null && X.isEnabled()) {
         Feature3DUIModule var3 = X;
         Feature3DUIModule.n var4 = var3 == null ? null : var3.Q(var0, var2);
         return var4 == null ? var1 : var1 + Mth.wrapDegrees(var4.yaw() - var1) * var4.weight();
      } else {
         return var1;
      }
   }

   public static cp.k k(Screen var0) {
      Feature3DUIModule var1 = X;
      cp.k var2 = var1 == null ? null : var1.h(var0);
      cp.g(var0, var2);
      return var2;
   }

   private cp.k h(Screen var1) {
      if (this.f != null && this.a(var1)) {
         double var2 = this.m();
         if (var2 <= 0.001) {
            return null;
         }

         double[] var4 = cp.b(var1);
         if (var4 == null) {
            q4acb8c4b35b6663e var5 = (q4acb8c4b35b6663e)var1;
            var4 = new double[]{var5.larpLeftPos(), var5.larpTopPos(), var5.larpLeftPos() + var5.larpImageWidth(), var5.larpTopPos() + var5.larpImageHeight()};
         }

         double var41 = var4[0];
         double var7 = var4[1];
         double var9 = var4[2];
         double var11 = var4[3];
         double var13 = (var41 + var9) / 2.0;
         double var15 = (var7 + var11) / 2.0;
         int var17 = this.i.S("Right") ? 1 : -1;
         double var18 = -var17 * Math.toRadians(this.G.P());
         double var20 = 3.5 * Math.max(1.0, (var9 - var41) / 2.0);
         double[] var22 = cp.k.of(var13, var15, 0.0, 0.0, 1.0, var18, var20).bounds(var41, var7, var9, var11);
         double var23 = 8.0;
         double var25 = var1.width;
         double var27 = var1.height;
         double var29 = Math.min(
            Math.min(this.W.P() / 100.0 * var27, var27 - 2.0 * var23) / (var22[3] - var22[1]), (var25 * 0.5 - var23) / (var22[2] - var22[0])
         );
         double var31 = var17 > 0 ? var25 * 0.71 : var25 * 0.29;
         double var33 = var31 - var29 * (var22[0] + var22[2]) / 2.0;
         var33 = var17 > 0 ? Math.min(var33, var25 - var23 - var29 * var22[2]) : Math.max(var33, var23 - var29 * var22[0]);
         double var35 = var27 / 2.0 - var29 * (var22[1] + var22[3]) / 2.0;
         if (var2 >= 0.999 && !this.A) {
            this.A = (boolean)1;
            mt.U(
               "GUI",
               "3D UI menu settled: {} content {}..{} x {}..{} (measured={}), scale {}, target {} {} in {}x{}",
               var1.getClass().getSimpleName(),
               Math.round(var41),
               Math.round(var9),
               Math.round(var7),
               Math.round(var11),
               cp.b(var1) != null,
               String.format(Locale.ROOT, "%.2f", var29),
               Math.round(var33),
               Math.round(var35),
               var1.width,
               var1.height
            );
         } else if (var2 < 0.5) {
            this.A = (boolean)0;
         }

         double var37 = var2;
         double var39 = Mth.lerp(var37, 1.0, var29);
         return cp.k.of(var13, var15, Mth.lerp(var37, var13, var33), Mth.lerp(var37, var15, var35), var39, var18 * var37, var20 * var39);
      } else {
         return null;
      }
   }

   public static void F(GuiGraphicsExtractor var0, int var1, int var2) {
      Feature3DUIModule var3 = X;
      if (var3 != null) {
         float var4 = var3.F.P() ? 1.0F : (float)(1.0 - var3.m());
         if (!(var4 <= 0.01F)) {
            var0.fillGradient(0, 0, var1, var2, Q(-1072689136, var4), Q(-804253680, var4));
         }
      }
   }

   private static int Q(int var0, float var1) {
      return Math.round((var0 >>> 24) * var1) << 24 | var0 & 16777215;
   }

   public static boolean S() {
      if (X != null && X.isEnabled()) {
         Feature3DUIModule var0 = X;
         return var0 != null && !var0.B.P() && var0.f != null && var0.a(getMc().gui.screen());
      } else {
         return false;
      }
   }

   @Override
   public void onEnable() {
      mt.U(
         "Camera",
         "3D UI active: screens={}, side={}, player facing={}{}",
         this.Z.P(),
         this.i.P(),
         this.E.P(),
         this.E.S("Custom") ? " " + this.K.P().intValue() + " degrees" : ""
      );
   }

   @Override
   public void onDisable() {
      this.v("module inactive");
      this.q.Q(0.0);
      cp.g(null, null);
   }

   private record n(float yaw, float pitch, float weight) {
   }

   public record v(double x, double y, double z, float yaw, float pitch) {
   }
}
